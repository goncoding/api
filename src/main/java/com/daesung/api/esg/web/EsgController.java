package com.daesung.api.esg.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.esg.domain.Esg;
import com.daesung.api.esg.repository.EsgRepository;
import com.daesung.api.esg.resource.esgResource;
import com.daesung.api.esg.upload.EsgThumbFileStore;
import com.daesung.api.ir.web.dto.IrInfoDto;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/esg")
public class EsgController {


    @Value("${file.dir}")
    private String fileDir;

    String savePath = "/esg";

    String whiteList = "pdf"; //모든 파일 통과

    private final EsgRepository esgRepository;
    private final EsgThumbFileStore esgThumbFileStore;
    private final FileStore fileStore;

    /**
     * ESG List 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity esgList(Pageable pageable,
                                  PagedResourcesAssembler<Esg> assembler,
                                  @PathVariable(name = "lang") String lang) {

        Page<Esg> esgPage = esgRepository.findAll(pageable);
        PagedModel<esgResource> esgResources = assembler.toModel(esgPage, e -> new esgResource(e));
        return ResponseEntity.ok().body(esgResources);
    }


    /**
     * ESG 등록
     */
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity esgInsert(@RequestParam(name = "category") String category,
                                    MultipartFile attachFile,
                                    @PathVariable(name = "lang") String lang) {

        Esg esg = new Esg();
        esg.setEsgCategory(category);
        esg.setLanguage(lang);

        if (attachFile != null) {

            try {
                UploadFile uploadFile = fileStore.storeFile(attachFile, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("확장자(pdf), 사이즈(10MB)를 확인 해주세요.", "400"));
                }

                esg.setEsgFileOriginalName(uploadFile.getOriginName());
                esg.setEsgFileSavedName(uploadFile.getNewName());
                esg.setEsgFileSavedPath(uploadFile.getRealPath());

                esgRepository.save(esg);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        }

        return ResponseEntity.ok(esg);
    }

    /**
     *  ESG 수정
     */
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity esgUpdate(MultipartFile attachFile,
                                    @PathVariable(name = "id") Long id,
                                    @PathVariable(name = "lang") String lang) {

        Optional<Esg> optionalEsg = esgRepository.findById(id);
        if (!optionalEsg.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 ESG 정보가 없습니다. id를 확인해주세요.","400"));
        }
        Esg esg = optionalEsg.get();

        if (attachFile != null) {

            try {
                UploadFile uploadFile = esgThumbFileStore.storeFile(attachFile, savePath, whiteList, id);
                if (uploadFile.isWrongType()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("확장자(pdf), 사이즈(10MB)를 확인 해주세요.", "400"));
                }


                esg.changeFileInfo(uploadFile);
                Esg updatedEsg = esgRepository.save(esg);

                return ResponseEntity.ok(updatedEsg);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(), "500 (IOException)"));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("수정 시 파일첨부는 필수입니다.","400"));
        }
    }


}
