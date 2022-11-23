package com.daesung.api.history.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import com.daesung.api.history.domain.enumType.HrCategory;
import com.daesung.api.history.repository.HistoryRecordFileRepository;
import com.daesung.api.history.repository.HistoryRecordRepository;
import com.daesung.api.history.resource.*;
import com.daesung.api.history.web.dto.RecordDto;
import com.daesung.api.history.web.dto.RecordListResponseDto;
import com.daesung.api.history.web.dto.recordResponseDto;
import com.daesung.api.news.domain.NewsThumbnailFile;
import com.daesung.api.news.web.dto.NewsGetResponseDto;
import com.daesung.api.utils.HtmlStringUtil;
import com.daesung.api.utils.StrUtil;
import com.daesung.api.utils.search.Search;
import com.daesung.api.utils.search.SearchDto;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
//        NEW_YEAR_ADDRESS("신년사"), COMMEMORATIVE("기념사"), CI("CI");
        Search search = new Search();
        //todo 상황에 따라서 뒤에 queryString을 만들 필요가 있다.
        search.setRecordType(recordType);
        search.setSearchType(searchType);
        search.setSearchText(searchText);
//        search.setPage(page);
//        search.setSize(size);

        if ("tit".equals(searchType)) {
            search.setSearchTitle(searchText);
        }
        if ("NY".equals(recordType)) {
            search.setHrCategory(HrCategory.NEW_YEAR_ADDRESS);
        }
        if ("CO".equals(recordType)) {
            search.setHrCategory(HrCategory.COMMEMORATIVE);
        }
        if ("CI".equals(recordType)) {
            search.setHrCategory(HrCategory.CI);
        }

//        Pageable pageRequest = PageRequest.of(0, 10);

        //todo page 넘기면서 검색 값 같이 넘기기
        Page<HistoryRecord> historyRecords = historyRecordRepository.searchRecordList(search, pageable);

        SearchDto searchDto = new SearchDto(searchType,searchText,recordType);

        PagedModel<EntityModel<HistoryRecord>> pagedModel = assembler.toModel(historyRecords, e -> {
            HistoryRecordResource historyRecordResource = new HistoryRecordResource(e);
            historyRecordResource.setSearchDto(searchDto);
            return historyRecordResource;
        });

        RecordListResponseDto recordListResponseDto = new RecordListResponseDto(pagedModel, search);

        PagedModel<EntityModel<HistoryRecord>> pagedModel01 = assembler.toModel(historyRecords, e -> {
            HistoryRecordResource historyRecordResource = new HistoryRecordResource(e);
            return historyRecordResource;
        });



        return ResponseEntity.ok(pagedModel01);
    }

    /**
     * 히스토리 단건 조회
     */
    //todo 이전글 이후글 기능 구현
    @GetMapping(value = "{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity recordGet(@PathVariable(name = "id", required = true) Long id,
                                    @PathVariable(name = "lang", required = true) String lang) {

        Optional<HistoryRecord> optionalHistoryRecord = historyRecordRepository.findById(id);
        if (!optionalHistoryRecord.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id를 확인해주세요.","404"));
        }

        HistoryRecord historyRecord = optionalHistoryRecord.get();



        //todo update 만들면 resource 추가
//        historyRecordResource.add(linkTo());

        List<HistoryRecordFile> historyRecordFileList = historyRecordFileRepository.findByHrId(id);

        recordResponseDto recordResponseDto = new recordResponseDto(historyRecord, historyRecordFileList);

//        HistoryRecordResource historyRecordResource = new HistoryRecordResource(historyRecord);

        long before = id - 1;
        long after = id + 1;

        HistoryRecordGetResource historyRecordGetResource = new HistoryRecordGetResource(recordResponseDto);
        historyRecordGetResource.add(linkTo(methodOn(HistoryRecordController.class).recordUpdate(historyRecord.getId(),null,null,null,null,null,null,null, lang)).withRel("update-historyRecord"));
        historyRecordGetResource.add(linkTo(methodOn(HistoryRecordController.class).recordGet(before,lang)).withRel("get-next"));
        historyRecordGetResource.add(linkTo(methodOn(HistoryRecordController.class).recordGet(after,lang)).withRel("get-prev"));;

        return ResponseEntity.ok(historyRecordGetResource);
    }


    /**
     * 히스토리 단건 조회 다운로드
     */
    @GetMapping(value = "/down/{hrId}")
    public ResponseEntity recordFileDown(HttpServletResponse response,
                                         @PathVariable(name = "hrId") Long hrId,
                                         @PathVariable(name = "lang") String lang) throws UnsupportedEncodingException {

        Optional<HistoryRecordFile> optionalRecordFile = historyRecordFileRepository.findById(hrId);
        if (!optionalRecordFile.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 파일 정보가 없습니다. 파일 id를 확인해주세요.","404"));
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
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id를 확인해주세요.","404"));
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
            return ResponseEntity.badRequest().body(errors);
        }
        //html 태그 제거
        String content = recordDto.getHrContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

        //NEW_YEAR_ADDRESS("신년사"), COMMEMORATIVE("기념사"), CI("CI");
        HrCategory enumCategory = getEnumCategory(recordDto);
        String description = enumCategory.getDescription();
        if (enumCategory == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("히스토리 카테고리가 null입니다.", "400"));
        }

        HistoryRecord historyRecord = HistoryRecord.builder()
                .hrCategory(enumCategory)
                .hrCategoryName(description)
                .hrTitle(recordDto.getHrTitle())
                .hrContent(content)
                .language(lang)
                .build();

        HistoryRecord savedRecord = historyRecordRepository.save(historyRecord);

        //뉴스 섬네일 업로드
        if (attachFile01 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile01, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

        if (attachFile02 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile02, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

        if (attachFile03 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile03, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

        if (attachFile04 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile04, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

        if (attachFile05 != null) {
            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile05, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

        HistoryRecordFileResource historyRecordFileResource = new HistoryRecordFileResource(savedRecord);
        historyRecordFileResource.add(linkTo(HistoryRecordController.class, lang).withSelfRel());
        historyRecordFileResource.add(linkTo(methodOn(HistoryRecordController.class).recordGet(savedRecord.getId(), lang)).withRel("get-historyRecord"));
        historyRecordFileResource.add(linkTo(methodOn(HistoryRecordController.class).recordUpdate(savedRecord.getId(),null,null,null,null,null,null,null, lang)).withRel("update-historyRecord"));

        return ResponseEntity.ok(historyRecordFileResource);

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

        Optional<HistoryRecord> optionalHistoryRecord = historyRecordRepository.findById(id);
        if (!optionalHistoryRecord.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id를 확인해주세요.","404"));
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        HistoryRecord historyRecord = optionalHistoryRecord.get();

        //html 태그 제거
        String content = recordDto.getHrContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

        //NEW_YEAR_ADDRESS("신년사"), COMMEMORATIVE("기념사"), CI("CI");
        HrCategory enumCategory = getEnumCategory(recordDto);
        String description = enumCategory.getDescription();
        if (enumCategory == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("히스토리 카테고리가 null입니다.", "404"));
        }

        historyRecord.updateRecord(recordDto, historyRecord, content, enumCategory, description);

        HistoryRecord savedRecord = historyRecordRepository.save(historyRecord);


        if (attachFile01 != null) {
            try {

                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "01");
                if (fileOptional.isPresent()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("1번 첨부파일이 존재합니다. 삭제 후 진행하세요.","400"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile01, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

        if (attachFile02 != null) {
            try {

                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "02");
                if (fileOptional.isPresent()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("2번 첨부파일이 존재합니다. 삭제 후 진행하세요.","404"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile02, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

        if (attachFile03 != null) {
            try {
                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "03");
                if (fileOptional.isPresent()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("3번 첨부파일이 존재합니다. 삭제 후 진행하세요.","404"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile03, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

        if (attachFile04 != null) {
            try {
                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "04");
                if (fileOptional.isPresent()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("4번 첨부파일이 존재합니다. 삭제 후 진행하세요.","404"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile04, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

        if (attachFile05 != null) {
            try {
                Optional<HistoryRecordFile> fileOptional = historyRecordFileRepository.findByHrIdAndSeq(savedRecord.getId(), "05");
                if (fileOptional.isPresent()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("5번 첨부파일이 존재합니다. 삭제 후 진행하세요.","404"));
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile05, savePath, whiteList);
                if (uploadFile.isWrongType()) {
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
                throw new RuntimeException(e);
            }
        }

//        if (recordFiles != null) {
//            try {
//                List<UploadFile> uploadFiles = fileStore.storeFileList(recordFiles, savePath, whiteList);
//
//                for (UploadFile uploadFile : uploadFiles) {
//                    if (uploadFile.isWrongType()) {
//                        return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
//                    }
//
//                    HistoryRecordFile recordFile = HistoryRecordFile.builder()
//                            .hrFileOriginalName(uploadFile.getOriginName())
//                            .hrFileSavedName(uploadFile.getNewName())
//                            .hrFileSavedPath(uploadFile.getRealPath())
//                            .historyRecord(savedRecord)
//                            .build();
//
//                    HistoryRecordFile savedFile = historyRecordFileRepository.save(recordFile);
//                    recordFileList.add(savedFile);
//                }
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

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

        Optional<HistoryRecord> optionalHistoryRecord = historyRecordRepository.findById(id);
        if (!optionalHistoryRecord.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id를 확인해주세요.","404"));
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
    @DeleteMapping("/files/{hrId}/{seq}")
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




    private static void deleteFile(HistoryRecordFile historyRecordFile) {

        String fileSavedPath = historyRecordFile.getHrFileSavedPath() + "/" + historyRecordFile.getHrFileSavedName();

        File file = new File(fileSavedPath);
        if (file.exists()) {
            file.delete();
        }
    }

    private static HrCategory getEnumCategory(RecordDto recordDto) {
        String hrCategory = recordDto.getHrCategory();

        if ("HY".equals(hrCategory)) {
            return HrCategory.NEW_YEAR_ADDRESS;
        }
        if ("CO".equals(hrCategory)) {
            return HrCategory.COMMEMORATIVE;
        }
        if ("CI".equals(hrCategory)) {
            return HrCategory.CI;
        }
        return null;
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
