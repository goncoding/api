package com.daesung.api.history.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import com.daesung.api.history.domain.enumType.HrCategory;
import com.daesung.api.history.repository.HistoryRecordFileRepository;
import com.daesung.api.history.repository.HistoryRecordRepository;
import com.daesung.api.history.resource.*;
import com.daesung.api.history.web.dto.*;
import com.daesung.api.utils.StrUtil;
import com.daesung.api.utils.search.Search;
import com.daesung.api.utils.search.SearchDto;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/history/record")
public class HistoryRecordController {

    @Value("${file.dir}")
    private String fileDir;

    String savePath = "/history_record";

    String whiteList = "jpg,png,gif,hwp,pdf,pptx,ppt,xlsx,xls,xps,zip";
    private final HistoryRecordRepository historyRecordRepository;
    private final HistoryRecordFileRepository historyRecordFileRepository;
    private final FileStore fileStore;

//    private final CustomPagedResourceAssembler<HistoryRecord> assembler;

    /**
     * 히스토리 리스트 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity recordList(Pageable pageable,
                                     PagedResourcesAssembler<HistoryRecord> assembler,
                                     @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                     @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                     @RequestParam(name = "recordType", required = false, defaultValue = "") String recordType,
                                     @RequestParam(name = "page", required = false, defaultValue = "") Integer page,
                                     @RequestParam(name = "size", required = false, defaultValue = "") Integer size,
                                     @PathVariable(name = "lang", required = true) String lang) {

        Search search = getSearch(searchType, searchText, recordType, lang);

        Page<HistoryRecord> historyRecords = historyRecordRepository.searchRecordList(search, pageable);

        SearchDto searchDto = new SearchDto(searchType,searchText,recordType);

        PagedModel<EntityModel<HistoryRecord>> pagedModel = assembler.toModel(historyRecords, e -> {
            HistoryRecordResource historyRecordResource = new HistoryRecordResource(e);
            historyRecordResource.setSearchDto(searchDto);
            return historyRecordResource;
        });

        RecordListResponseDto recordListResponseDto = new RecordListResponseDto(pagedModel, search);

        PagedModel<EntityModel<HistoryRecord>> pagedModel01 = assembler.toModel(historyRecords);

        return ResponseEntity.ok(pagedModel01);
    }

    /**
     * 히스토리 단건 조회
     */
    //todo 이전글 이후글 기능 구현
    @GetMapping(value = "{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity recordGet(@PathVariable(name = "id", required = true) Long id,
                                    @RequestParam(name = "searchType", required = false) String searchType,
                                    @RequestParam(name = "searchText", required = false) String searchText,
                                    @RequestParam(name = "recordType", required = false) String recordType,
                                    @RequestParam(name = "page", required = false) Integer page,
                                    @RequestParam(name = "size", required = false) Integer size,
                                    @PathVariable(name = "lang", required = true) String lang) {

        Optional<HistoryRecord> optionalHistoryRecord = historyRecordRepository.findByIdAndLanguage(id, lang);
        if (!optionalHistoryRecord.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 히스토리 정보가 없습니다. id, 언어를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id, 언어를 확인해주세요.","400"));
        }

        HistoryRecord historyRecord = optionalHistoryRecord.get();

        Search search = getSearch(searchType, searchText, recordType, lang);

        HistoryRecord prevRecord = historyRecordRepository.searchPrevRecord(id, search);
        HistoryRecord nextRecord = historyRecordRepository.searchNextRecord(id, search);


        List<HistoryRecordFile> fileList = historyRecordFileRepository.findByHrId(id);

        HistoryRecordGetResponse historyRecordGetResponse = HistoryRecordGetResponse.builder()
                .historyRecord(historyRecord)
                .historyRecordFileList(fileList)
                .prevRecord(prevRecord)
                .nextRecord(nextRecord)
                .build();

        HistoryRecordGetResource getResource = new HistoryRecordGetResource(historyRecordGetResponse);

        return ResponseEntity.ok(getResource);
    }


    /**
     * 히스토리 단건 조회 다운로드
     */
    @GetMapping(value = "/down/{fileId}")
    public ResponseEntity recordFileDown(HttpServletResponse response,
                                         @PathVariable(name = "fileId") Long id,
                                         @PathVariable(name = "lang") String lang) throws UnsupportedEncodingException {

        Optional<HistoryRecordFile> optionalRecordFile = historyRecordFileRepository.findById(id);
        if (!optionalRecordFile.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 파일 정보가 없습니다. 파일 id를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 파일 정보가 없습니다. 파일 id를 확인해주세요.","400"));
        }
        HistoryRecordFile recordFile = optionalRecordFile.get();

        return download(response, recordFile);
    }


    /**
     * 히스토리 단건 파일 조회
     */
    @GetMapping(value = "/files/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity recordFilesGet(@PathVariable(name = "id") Long id,
                                         @PathVariable(name = "lang") String lang) {

        Optional<HistoryRecord> optionalHistoryRecord = historyRecordRepository.findById(id);
        if (!optionalHistoryRecord.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 히스토리 정보가 없습니다. id를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id를 확인해주세요.","400"));
        }

        HistoryRecord historyRecord = optionalHistoryRecord.get();
        List<HistoryRecordFile> recordFiles = historyRecordFileRepository.findByHrId(historyRecord.getId());

        return ResponseEntity.ok(recordFiles);
    }

    /**
     * 히스토리 등록
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity recordInsert(
            @RequestPart @Valid RecordDto recordDto, Errors errors,
            @RequestPart(name = "attachFile01", required = false) MultipartFile attachFile01,
            @RequestPart(name = "attachFile02",required = false) MultipartFile attachFile02,
            @RequestPart(name = "attachFile03",required = false) MultipartFile attachFile03,
            @RequestPart(name = "attachFile04",required = false) MultipartFile attachFile04,
            @RequestPart(name = "attachFile05",required = false) MultipartFile attachFile05,
            @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            log.error("status = {}, message = {}", "400", "히스토리 등록 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

//        String content = recordDto.getHrContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

        //NEW_YEAR_ADDRESS("신년사"), COMMEMORATIVE("기념사"), CI("CI");
        HrCategory enumCategory = getEnumCategory(recordDto);
        if (enumCategory == null) {
            log.error("status = {}, message = {}", "400", "히스토리 카테고리가 null입니다.");
            return ResponseEntity.badRequest().body(new ErrorResponse("히스토리 카테고리가 null입니다.", "400"));
        }

        HistoryRecord historyRecord = HistoryRecord.builder()
                .hrCategory(enumCategory)
                .hrCategoryName(recordDto.getHrCategoryName())
                .hrTitle(recordDto.getHrTitle())
                .hrContent(recordDto.getHrContent())
                .language(lang)
                .build();

        HistoryRecord savedRecord = historyRecordRepository.save(historyRecord);

        //뉴스 섬네일 업로드
        if (attachFile01 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile01, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("01")
                        .build();

                historyRecordFileRepository.save(recordFile);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        if (attachFile02 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile02, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("02")
                        .build();

                historyRecordFileRepository.save(recordFile);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        if (attachFile03 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile03, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("03")
                        .build();

                historyRecordFileRepository.save(recordFile);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        if (attachFile04 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile04, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("04")
                        .build();

                historyRecordFileRepository.save(recordFile);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        if (attachFile05 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile05, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("05")
                        .build();

                historyRecordFileRepository.save(recordFile);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        List<HistoryRecordFile> fileList = historyRecordFileRepository.findByHrId(savedRecord.getId());

        HistoryRecordInsertResponse insertResponse = HistoryRecordInsertResponse.builder()
                .historyRecord(savedRecord)
                .historyRecordFileList(fileList)
                .build();

        return ResponseEntity.ok(new HistoryRecordInsertResource(insertResponse));
    }


    /**
     * 히스토리 수정
     */
    @PostMapping(value = "/modify/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity recordUpdate(@PathVariable(name = "id") Long id,
                                       @RequestPart(name = "recordDto") @Valid RecordDto recordDto, Errors errors,
                                       @RequestPart(name = "attachFile01",required = false) MultipartFile attachFile01,
                                       @RequestPart(name = "attachFile02",required = false) MultipartFile attachFile02,
                                       @RequestPart(name = "attachFile03",required = false) MultipartFile attachFile03,
                                       @RequestPart(name = "attachFile04",required = false) MultipartFile attachFile04,
                                       @RequestPart(name = "attachFile05",required = false) MultipartFile attachFile05,
                                       @PathVariable(name = "lang") String lang) {

        List<HistoryRecordFile> recordFileList = new ArrayList<>();

        Optional<HistoryRecord> optionalHistoryRecord = historyRecordRepository.findByIdAndLanguage(id, lang);
        if (!optionalHistoryRecord.isPresent()) {
            log.info("status = {}, message = {}", "400", "일치하는 히스토리 정보가 없습니다. id, 언어를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id, 언어를 확인해주세요.","400"));
        }

        if (errors.hasErrors()) {
            log.info("status = {}, message = {}", "400", "히스토리 수정 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        HistoryRecord historyRecord = optionalHistoryRecord.get();

        //html 태그 제거
        String content = recordDto.getHrContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

        //NEW_YEAR_ADDRESS("신년사"), COMMEMORATIVE("기념사"), CI("CI");
        HrCategory enumCategory = getEnumCategory(recordDto);
        String description = enumCategory.getDescription();
        if (enumCategory == null) {
            log.info("status = {}, message = {}", "400", "히스토리 카테고리는 필수입니다.");
            return ResponseEntity.badRequest().body(new ErrorResponse("히스토리 카테고리는 필수입니다.", "400"));
        }

        historyRecord.updateRecord(recordDto, historyRecord, enumCategory, description);

        HistoryRecord savedRecord = historyRecordRepository.save(historyRecord);




        if (attachFile01 != null) {
            try {

                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "01");
                if (fileOptional.isPresent()) {
                    log.info("status = {}, message = {}", "400", "1번 첨부파일이 존재합니다. 삭제 후 진행하세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("1번 첨부파일이 존재합니다. 삭제 후 진행하세요.","400"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile01, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("01")
                        .build();

                HistoryRecordFile savedFile01 = historyRecordFileRepository.save(recordFile);
                recordFileList.add(savedFile01);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        if (attachFile02 != null) {
            try {

                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "02");
                if (fileOptional.isPresent()) {
                    log.info("status = {}, message = {}", "400", "2번 첨부파일이 존재합니다. 삭제 후 진행하세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("2번 첨부파일이 존재합니다. 삭제 후 진행하세요.","400"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile02, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("02")
                        .build();

                HistoryRecordFile savedFile02 = historyRecordFileRepository.save(recordFile);
                recordFileList.add(savedFile02);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        if (attachFile03 != null) {
            try {
                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "03");
                if (fileOptional.isPresent()) {
                    log.info("status = {}, message = {}", "400", "3번 첨부파일이 존재합니다. 삭제 후 진행하세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("3번 첨부파일이 존재합니다. 삭제 후 진행하세요.","400"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile03, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("03")
                        .build();

                HistoryRecordFile savedFile03 = historyRecordFileRepository.save(recordFile);
                recordFileList.add(savedFile03);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        if (attachFile04 != null) {
            try {
                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "04");
                if (fileOptional.isPresent()) {
                    log.info("status = {}, message = {}", "400", "4번 첨부파일이 존재합니다. 삭제 후 진행하세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("4번 첨부파일이 존재합니다. 삭제 후 진행하세요.","400"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile04, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("04")
                        .build();

                HistoryRecordFile savedFile04 = historyRecordFileRepository.save(recordFile);
                recordFileList.add(savedFile04);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        if (attachFile05 != null) {
            try {
                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "05");
                if (fileOptional.isPresent()) {
                    log.info("status = {}, message = {}", "400", "5번 첨부파일이 존재합니다. 삭제 후 진행하세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("5번 첨부파일이 존재합니다. 삭제 후 진행하세요.","400"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile05, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 확장자, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                HistoryRecordFile recordFile = HistoryRecordFile.builder()
                        .hrFileOriginalName(uploadFile.getOriginName())
                        .hrFileSavedName(uploadFile.getNewName())
                        .hrFileSavedPath(uploadFile.getRealPath())
                        .historyRecord(savedRecord)
                        .hrFileSeq("05")
                        .build();

                HistoryRecordFile savedFile05 = historyRecordFileRepository.save(recordFile);
                recordFileList.add(savedFile05);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        HistoryRecordUpdateResource updateResource = new HistoryRecordUpdateResource(savedRecord);
        updateResource.add(linkTo(methodOn(HistoryRecordController.class).recordUpdate(savedRecord.getId(), null,null,null,null,null,null,null,lang)).withSelfRel());
        updateResource.add(linkTo(methodOn(HistoryRecordController.class).recordFilesGet(savedRecord.getId(), lang)).withRel("get-files"));

        recordResponseDto recordResponseDto = new recordResponseDto(savedRecord, recordFileList);

        return ResponseEntity.ok(updateResource);
    }

    /**
     * 히스토리 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity recordDelete(@PathVariable(name = "id") Long id,
                                       @PathVariable(name = "lang") String lang) {

        Optional<HistoryRecord> optionalHistoryRecord = historyRecordRepository.findByIdAndLanguage(id, lang);
        if (!optionalHistoryRecord.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id, 언어를 확인해주세요.","400"));
        }

        HistoryRecord historyRecord = optionalHistoryRecord.get();

        Long recordId = historyRecord.getId();

        List<HistoryRecordFile> recordFileList = historyRecordFileRepository.findByHrId(recordId);

        if (recordFileList != null) {
            for (HistoryRecordFile historyRecordFile : recordFileList) {
                historyRecordFileRepository.deleteByHrId(recordId);
                deleteFile(historyRecordFile);
            }
        }

        historyRecordRepository.deleteById(recordId);

        return ResponseEntity.ok(recordId+"번 히스토리 기록이 삭제되었습니다.");
    }


    /**
     * 히스토리 파일 삭제
     */
    @DeleteMapping("/file/{hrId}/{seq}")
    public ResponseEntity deleteAttachFile(@PathVariable("seq") String seq,
                                           @PathVariable("hrId") Long hrId) {

        if (seq == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("파일 순서가 없습니다. 순서를 확인해주세요.","400"));
        }

        Optional<HistoryRecord> optionalHistoryRecord = historyRecordRepository.findById(hrId);
        if (!optionalHistoryRecord.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id를 확인해주세요.","400"));
        }

        HistoryRecord historyRecord = optionalHistoryRecord.get();

        Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(historyRecord.getId(), seq);
        if (!fileOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id, seq를 확인해주세요.","500"));
        }

        HistoryRecordFile historyRecordFile = fileOptional.get();

        deleteFile(historyRecordFile);

        historyRecordFileRepository.deleteById(historyRecordFile.getId());

        return ResponseEntity.ok("히스토리 "+hrId+" 게시글의 "+seq+"번 파일이 삭제 완료되었습니다.");

    }


    public void deleteFile(HistoryRecordFile historyRecordFile) {

        String fileSavedPath = fileDir + historyRecordFile.getHrFileSavedPath() + "/" + historyRecordFile.getHrFileSavedName();

        File file = new File(fileSavedPath);
        if (file.exists()) {
            file.delete();
        }
    }

    private static HrCategory getEnumCategory(RecordDto recordDto) {
        String hrCategory = recordDto.getHrCategory();

        if ("NA".equals(hrCategory)) {
            return HrCategory.NA;
        }
        if ("CS".equals(hrCategory)) {
            return HrCategory.CS;
        }
        if ("CI".equals(hrCategory)) {
            return HrCategory.CI;
        }
        return null;
    }

    private static Search getSearch(String searchType, String searchText, String recordType, String lang) {
        Search search = new Search();
        search.setSearchType(searchType);
        search.setSearchText(searchText);
        search.setLanguage(lang);

        if ("tit".equals(searchType)) {
            search.setSearchTitle(searchText);
        }
        if ("NA".equals(recordType)) {
            search.setHrCategory(HrCategory.NA);
        }
        if ("CS".equals(recordType)) {
            search.setHrCategory(HrCategory.CS);
        }
        if ("CI".equals(recordType)) {
            search.setHrCategory(HrCategory.CI);
        }

        return search;
    }

    private static ResponseEntity<?> download(HttpServletResponse response, HistoryRecordFile recordFile) {
        File file = new File(recordFile.getHrFileSavedPath(), recordFile.getHrFileSavedName());

        String name = StrUtil.encodeKR(recordFile.getHrFileOriginalName().replaceAll("(%20| |\\+)+", " "));

        response.setContentType("application/octet-stream");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", name));
        response.setHeader("Content-Transfer-Encoding", "binary");

        OutputStream out = null;
        FileInputStream fis = null;
        try {
            out = response.getOutputStream();
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
            out.flush();

            return ResponseEntity.ok("파일 다운로드 성공");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getLocalizedMessage(), "500"));
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception e) {
            }
            try {
                if (fis != null) fis.close();
            } catch (Exception e) {
            }
        }
    }



}
