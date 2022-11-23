package com.daesung.api.ir.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.ir.domain.IrYear;
import com.daesung.api.ir.domain.enumType.IrType;
import com.daesung.api.ir.repository.IrInfoRepository;
import com.daesung.api.ir.repository.IrYearRepository;
import com.daesung.api.ir.resource.IrInfoListResource;
import com.daesung.api.ir.web.dto.IrInfoDto;
import com.daesung.api.utils.search.Search;
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
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/ir")
public class IrInfoController {


    @Value("${file.dir}")
    private String fileDir;

    String savePath = "/irInfo";

    String whiteList = "all"; //모든 파일 통과

    private final IrInfoRepository irInfoRepository;
    private final IrYearRepository irYearRepository;
    private final FileStore fileStore;


    /**
     * IR 자료관리 리스트 조회
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity irInfoList(Pageable pageable,
                                     PagedResourcesAssembler<IrInfo> assembler,
                                     @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                                     @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
                                     @RequestParam(name = "irType", required = false, defaultValue = "") String irType,
                                     @RequestParam(name = "page", required = false, defaultValue = "") Integer page,
                                     @RequestParam(name = "size", required = false, defaultValue = "") Integer size,
                                     @PathVariable(name = "lang") String lang) {

//      MP("경영실적"), AR("감사보고서"),BR("사업보고서");
        Search search = Search.builder()
                .searchType(searchType)
                .searchTitle(searchText)
                .searchText(searchText)
                .build();

        if ("tit".equals(searchType)) {
            search.setSearchTitle(searchText);
        }
        if ("MP".equals(irType)) {
            search.setIrType(IrType.MP);
        }
        if ("AR".equals(irType)) {
            search.setIrType(IrType.AR);
        }
        if ("BR".equals(irType)) {
            search.setIrType(IrType.BR);
        }

        Page<IrInfo> irInfos = irInfoRepository.searchIrInfoRepository(search, pageable);

        PagedModel<IrInfoListResource> irInfoListResources = assembler.toModel(irInfos, i -> new IrInfoListResource(i));

        return ResponseEntity.ok(irInfoListResources);
    }



    /**
     * IR 자료관리 등록
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity irInfoInsert(@RequestPart @Valid IrInfoDto irInfoDto, Errors errors,
                                       @RequestPart(name = "attachFile", required = false) MultipartFile attachFile,
                                       @PathVariable(name = "lang") String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        String title = irInfoDto.getIrTitle().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

        //MP("경영실적"), AR("감사보고서"),BR("사업보고서");
        IrType irType = getIrType(irInfoDto);
        if (irType == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("IR 카테고리는 필수입니다.", "400"));
        }

        //파일 업로드
        if (attachFile != null) {

            try {
                UploadFile uploadFile = fileStore.storeFile(attachFile, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 사이즈를 확인 해주세요.", "400"));
                }

                Optional<IrYear> optionalIrYear = irYearRepository.findByIyYear(irInfoDto.getIrYear());
                if (!optionalIrYear.isPresent()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("해당 연도를 연도관리에서 추가 해주세요.", "400"));
                }

                IrInfo irInfo = IrInfo.builder()
                        .irType(irType)
                        .irYear(optionalIrYear.get())
                        .irTitle(irInfoDto.getIrTitle())
                        .irFileOriginalName(uploadFile.getOriginName())
                        .irFileSavedName(uploadFile.getNewName())
                        .irFileSavedPath(uploadFile.getRealPath())
                        .language(lang)
                        .build();

                IrInfo savedIrInfo = irInfoRepository.save(irInfo);

                return ResponseEntity.ok(savedIrInfo);

            } catch (IOException e) {
                return ResponseEntity.ok(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }

        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("파일첨부는 필수입니다.","400"));
        }
    }

    /**
     * IR 자료관리 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity irInfoDelete(@PathVariable(name = "id") Long id, @PathVariable(name = "lang") String lang) {

        Optional<IrInfo> optionalIrInfo = irInfoRepository.findById(id);
        if (!optionalIrInfo.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 자료관리 정보가 없습니다. id를 확인해주세요.","404"));
        }

        IrInfo irInfo = optionalIrInfo.get();
        irInfoRepository.deleteById(irInfo.getId());
        deleteFile(irInfo);

        return ResponseEntity.ok(id+"번 자료관리가 삭제되었습니다.");
    }

    private static void deleteFile(IrInfo irInfo) {

        String fileSavedPath = irInfo.getIrFileSavedPath() + "/" + irInfo.getIrFileSavedName();

        File file = new File(fileSavedPath);
        if (file.exists()) {
            file.delete();
        }
    }


    private IrType getIrType(IrInfoDto irInfoDto) {
        if ("MP".equals(irInfoDto.getIrType())) {
            return IrType.MP;
        }
        if ("AR".equals(irInfoDto.getIrType())) {
            return IrType.AR;
        }
        if ("BR".equals(irInfoDto.getIrType())) {
            return IrType.BR;
        }
        return null;
    }
}
