package com.daesung.api.popup.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import com.daesung.api.ir.domain.DisclosureInfo;
import com.daesung.api.ir.resource.DisclosureInfoResource;
import com.daesung.api.popup.domain.Popup;
import com.daesung.api.popup.repository.PopupRepository;
import com.daesung.api.popup.upload.PopupFileStore;
import com.daesung.api.popup.web.dto.PopupDto;
import com.daesung.api.utils.search.Search;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/popup")
public class PopupController {

    @Value("${file.dir}")
    private String fileDir;

    String savePath = "/popup";

    String whiteList = "jpg, png";

    private final PopupRepository popupRepository;
    private final FileStore fileStore;
    private final PopupFileStore popupFileStore;

    /**
     * 팝업관리 리스트 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity disclosureInfoList(Pageable pageable,
                                             PagedResourcesAssembler<DisclosureInfo> assembler,
                                             @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                             @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                             @RequestParam(name = "page", required = false, defaultValue = "") Integer page,
                                             @RequestParam(name = "size", required = false, defaultValue = "") Integer size,
                                             @PathVariable(name = "lang") String lang) {

        Search search = Search.builder()
                .searchType(searchType)
                .searchText(searchText)
                .build();

        if ("tit".equals(searchType)) {
            search.setSearchTitle(searchText);
        }

        Pageable pageRequest = PageRequest.of(0, 9999);

        Page<Popup> popupList = popupRepository.searchPopupList(search, pageRequest);

        return ResponseEntity.ok(popupList);
    }

    /**
     * 팝업관리 단건 조회
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity popupGet(@PathVariable(name = "id") Long id,
                                   @PathVariable(name = "lang") String lang) {

        Optional<Popup> optionalPopup = popupRepository.findById(id);
        if (!optionalPopup.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 팝업 정보가 없습니다. id를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 팝업 정보가 없습니다. id를 확인해주세요.","400"));
        }
        Popup popup = optionalPopup.get();

        return ResponseEntity.ok(popup);
    }

    /**
     * 팝업관리 등록
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity popupInsert(@Valid @RequestPart(name = "popupDto") PopupDto popupDto, Errors errors,
                                      @RequestPart(name = "attachFile", required = false) MultipartFile attachFile,
                                      @PathVariable(name = "lang") String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Popup popup = Popup.builder()
                .puTitle(popupDto.getTitle())
                .puStartDate(popupDto.getStartDate())
                .puEndDate(popupDto.getEndDate())
                .puSequence(popupDto.getSequence())
                .language(lang)
                .build();


        if (attachFile != null) {

            try {

                UploadFile uploadFile = fileStore.storeFile(attachFile, savePath, whiteList);

                popup.changeFileInfo(uploadFile, popupDto);

                if (uploadFile.isWrongType()) {
                    log.error("status = {}, message = {}", "400", "파일명, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("확장자, 파일명, 사이즈를 확인 해주세요.", "400"));
                }

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(), "500 (IOException)"));
            }
        } else {

            if (popupDto.getFileSummary() != null) {
                log.info("status = {}, message = {}", "400", "이미지 설명은 파일 업로드 후 등록 해주세요.");
                return ResponseEntity.badRequest().body(new ErrorResponse("이미지 설명은 파일 업로드 후 등록 해주세요.", "400"));
            }
        }

        Popup savedPopup = popupRepository.save(popup);

        return ResponseEntity.ok(savedPopup);
    }

    /**
     * 팝업관리 수정
     */
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity popupUpdate(@Valid @RequestPart(name = "popupDto") PopupDto popupDto, Errors errors,
                                      @RequestPart(name = "attachFile", required = false) MultipartFile attachFile,
                                      @PathVariable(name = "id") Long id,
                                      @PathVariable(name = "lang") String lang) {


        Optional<Popup> optionalPopup = popupRepository.findById(id);
        if (!optionalPopup.isPresent()) {
            log.info("status = {}, message = {}", "400", "일치하는 팝업 정보가 없습니다. id를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 팝업 정보가 없습니다. id를 확인해주세요.","400"));
        }
        Popup popup = optionalPopup.get();


        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        popup.changePopup(popupDto);

        if (attachFile != null) {

            try {


                UploadFile uploadFile = popupFileStore.storeFile(attachFile, savePath, whiteList, popup.getPuFileSavedPath(), popup.getPuFileSavedName());

                popup.changeFileInfo(uploadFile, popupDto);

                if (uploadFile.isWrongType()) {
                    log.info("status = {}, message = {}", "400", "파일명, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("확장자, 파일명, 사이즈를 확인 해주세요.", "400"));
                }

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(), "500 (IOException)"));
            }
        }

        Popup savedPopup = popupRepository.save(popup);

        return ResponseEntity.ok(savedPopup);
    }


    /**
     * 팝업관리 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity popupDelete(@PathVariable("id") Long id, @PathVariable(name = "lang") String lang) {

        Optional<Popup> optionalPopup = popupRepository.findById(id);
        if (!optionalPopup.isPresent()) {
            log.info("status = {}, message = {}", "400", "일치하는 팝업 정보가 없습니다. id를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 팝업 정보가 없습니다. id를 확인해주세요.","400"));
        }

        Popup popup = optionalPopup.get();

        popupRepository.deleteById(popup.getId());

        deleteFile(popup);

        return ResponseEntity.ok("팝업관리 "+ popup.getId() +"번 게시글이 삭제 완료되었습니다.");

    }



    /**
     * 팝업관리 단건 파일 삭제
     */
    @DeleteMapping("/file/{id}")
    public ResponseEntity deleteAttachFile(@PathVariable("id") Long id,
                                           @PathVariable(name = "lang") String lang) {

        Optional<Popup> optionalPopup = popupRepository.findById(id);
        if (!optionalPopup.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 팝업 정보가 없습니다. id를 확인해주세요.","400"));
        }

        Popup popup = optionalPopup.get();

        deleteFile(popup);

        popup.deleteFileInfo(popup);

        popupRepository.save(popup);

        return ResponseEntity.ok("히스토리 "+popup.getId()+" 게시글의 "+" 이미지 파일이 삭제 완료되었습니다.");

    }

    public void deleteFile(Popup popup) {

        String fileSavedPath = fileDir + popup.getPuFileSavedPath() + "/" + popup.getPuFileSavedName();

        File file = new File(fileSavedPath);
        if (file.exists()) {
            file.delete();
        }
    }


}
