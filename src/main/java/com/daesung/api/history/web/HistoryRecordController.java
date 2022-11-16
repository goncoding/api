package com.daesung.api.history.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import com.daesung.api.history.domain.enumType.HrCategory;
import com.daesung.api.history.repository.HistoryRecordFileRepository;
import com.daesung.api.history.repository.HistoryRecordRepository;
import com.daesung.api.history.resource.HistoryRecordFileResource;
import com.daesung.api.history.resource.HistoryRecordResource;
import com.daesung.api.history.resource.HistoryResource;
import com.daesung.api.history.web.dto.RecordDto;
import com.daesung.api.history.web.dto.recordResponseDto;
import com.daesung.api.news.domain.NewsThumbnailFile;
import com.daesung.api.news.web.dto.NewsGetResponseDto;
import com.daesung.api.utils.HtmlStringUtil;
import com.daesung.api.utils.StrUtil;
import com.daesung.api.utils.search.Search;
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

    /**
     * 히스토리 리스토 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity recordList(Pageable pageable,
                                     PagedResourcesAssembler<HistoryRecord> assembler,
                                     @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                     @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                     @RequestParam(name = "recordType", required = false, defaultValue = "") String recordType,
                                     @RequestParam(name = "page", required = false, defaultValue = "") String page,
                                     @RequestParam(name = "size", required = false, defaultValue = "") String size,
                                     @PathVariable(name = "lang", required = true) String lang) {
//        NEW_YEAR_ADDRESS("신년사"), COMMEMORATIVE("기념사"), CI("CI");
        Search search = new Search();

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

        Pageable pageRequest = PageRequest.of(0, 10);

        Page<HistoryRecord> historyRecords = historyRecordRepository.searchRecordList(search, pageRequest);
        PagedModel<EntityModel<HistoryRecord>> pagedModel = assembler.toModel(historyRecords, e -> new HistoryRecordResource(e));

        return ResponseEntity.ok(pagedModel);
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

        HistoryRecordResource historyRecordResource = new HistoryRecordResource(historyRecord);
        //todo update 만들면 resource 추가
//        historyRecordResource.add(linkTo());

        List<HistoryRecordFile> historyRecordFileList = historyRecordFileRepository.findByHrId(id);

        recordResponseDto recordResponseDto = new recordResponseDto(historyRecord, historyRecordFileList);

        return ResponseEntity.ok(recordResponseDto);
    }


    /**
     * 히스토리 단건 조회 다운로드
     */
    @GetMapping(value = "/down/{hrId}")
    public ResponseEntity recordFileDown(HttpServletResponse response,
                                         @PathVariable("hrId") Long hrId,
                                         @PathVariable(name = "lang", required = true) String lang) throws UnsupportedEncodingException {

        Optional<HistoryRecordFile> optionalRecordFile = historyRecordFileRepository.findById(hrId);
        if (!optionalRecordFile.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 파일 정보가 없습니다. 파일 id를 확인해주세요.","404"));
        }
        HistoryRecordFile recordFile = optionalRecordFile.get();

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
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getLocalizedMessage(),"500"));
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


    /**
     * 히스토리 리스토 등록
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity recordInsert(
            @RequestPart @Valid RecordDto recordDto, Errors errors,
            @RequestPart(required = false) MultipartFile attachFile01,
            @RequestPart(required = false) MultipartFile attachFile02,
            @RequestPart(required = false) MultipartFile attachFile03,
            @RequestPart(required = false) MultipartFile attachFile04,
            @RequestPart(required = false) MultipartFile attachFile05,
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
            return ResponseEntity.badRequest().body(new ErrorResponse("히스토리 카테고리가 null입니다.", "404"));
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
        historyRecordFileResource.add(linkTo(methodOn(HistoryRecordController.class).recordUpdate(savedRecord.getId(),null,null,null, lang)).withRel("update-historyRecord"));

        return ResponseEntity.ok(historyRecordFileResource);

    }

    //todo 수정시 file seq에 따라서 잘 수정되는지 체크하기
    /**
     * 히스토리 수정 조회
     */
    @PostMapping(value = "/modify/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity recordUpdate(@PathVariable(name = "id") Long id,
                                       @RequestPart(name = "recordDto") @Valid RecordDto recordDto, Errors errors,
                                       @RequestPart(name = "recordFiles") List<MultipartFile> recordFiles,
                                       @PathVariable(name = "lang", required = true) String lang) {

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

        if (recordFiles != null) {
            try {
                List<UploadFile> uploadFiles = fileStore.storeFileList(recordFiles, savePath, whiteList);

                for (UploadFile uploadFile : uploadFiles) {
                    if (uploadFile.isWrongType()) {
                        return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                    }

                    HistoryRecordFile recordFile = HistoryRecordFile.builder()
                            .hrFileOriginalName(uploadFile.getOriginName())
                            .hrFileSavedName(uploadFile.getNewName())
                            .hrFileSavedPath(uploadFile.getRealPath())
                            .historyRecord(savedRecord)
                            .build();

                    HistoryRecordFile savedFile = historyRecordFileRepository.save(recordFile);
                    recordFileList.add(savedFile);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        HistoryRecordFileResource historyRecordFileResource = new HistoryRecordFileResource(savedRecord);

        recordResponseDto recordResponseDto = new recordResponseDto(savedRecord, recordFileList);

        return ResponseEntity.ok(recordResponseDto);
    }


    /**
     * 히스토리 단건 조회 다운로드
     */

    /**
     * 히스토리 삭제 조회
     */


    /**
     * 히스토리 파일 삭제
     */
    @DeleteMapping("/deleteAttachFile/{hrId}/{seq}")
    public ResponseEntity deleteAttachFile(@PathVariable("seq") String seq,
                                           @PathVariable("hrId") Long hrId) {

        if (seq == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("파일 순서가 없습니다. 순서를 확인해주세요.","404"));
        }

        Optional<HistoryRecord> optionalHistoryRecord = historyRecordRepository.findById(hrId);
        if (!optionalHistoryRecord.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 히스토리 정보가 없습니다. id를 확인해주세요.","404"));
        }

        HistoryRecord historyRecord = optionalHistoryRecord.get();

        HistoryRecordFile historyRecordFile = historyRecordFileRepository.findByHrIdAndSeq(historyRecord.getId(), seq);

        String fileSavedPath = historyRecordFile.getHrFileSavedPath() + "/" + historyRecordFile.getHrFileSavedName();

        File file = new File(fileSavedPath);
        if (file.exists()) {
            file.delete();
        }

        historyRecordFileRepository.deleteById(historyRecordFile.getId());

        return ResponseEntity.ok(seq+"번이 삭제 완료되었습니다.");

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
}
