package com.daesung.api.ir.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.esg.upload.EsgThumbFileStore;
import com.daesung.api.history.domain.HistoryRecordFile;
import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.ir.domain.IrYear;
import com.daesung.api.ir.domain.enumType.IrType;
import com.daesung.api.ir.repository.IrInfoRepository;
import com.daesung.api.ir.repository.IrYearRepository;
import com.daesung.api.ir.resource.IrInfoListResource;
import com.daesung.api.ir.upload.IrInfoFileStore;
import com.daesung.api.ir.web.dto.IrInfoDto;
import com.daesung.api.ir.web.dto.IrInfoInsertResponse;
import com.daesung.api.ir.web.dto.IrInfoUpdateResponse;
import com.daesung.api.utils.StrUtil;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/ir-info")
public class IrInfoController {


    @Value("${file.dir}")
    private String fileDir;

    String savePath = "/ir_info";

    String whiteList = "all"; //모든 파일 통과

    String imageWhiteList = "jpg, gif, png";



    private final IrInfoRepository irInfoRepository;
    private final IrYearRepository irYearRepository;
    private final FileStore fileStore;

    private final IrInfoFileStore irInfoFileStore;


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

        Page<IrInfo> irInfos = irInfoRepository.searchIrInfoList(search, pageable);

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
            log.error("status = {}, message = {}", "400", "IR 자료관리 등록 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        String title = irInfoDto.getIrTitle().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

        //MP("경영실적"), AR("감사보고서"),BR("사업보고서");
        IrType irType = getIrType(irInfoDto);
        if (irType == null) {
            log.error("status = {}, message = {}", "400", "IR 카테고리는 필수입니다.");
            return ResponseEntity.badRequest().body(new ErrorResponse("IR 카테고리는 필수입니다.", "400"));
        }

        Optional<IrYear> optionalIrYear = irYearRepository.findByIyYear(irInfoDto.getIrYear());
        if (!optionalIrYear.isPresent()) {
            log.error("status = {}, message = {}", "400", "해당 연도를 연도관리에서 추가 해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("해당 연도를 연도관리에서 추가 해주세요.", "400"));
        }
        IrYear irYear = optionalIrYear.get();

        //파일 업로드
        if (attachFile != null) {
            try {

                int width = 0;
                int height = 0;
                long frontId = 0L;

                boolean  extCheck = fileStore._typeOk(imageWhiteList, attachFile.getOriginalFilename());
                if (extCheck) {
                    BufferedImage bufferedImage = ImageIO.read(attachFile.getInputStream());

                    width = bufferedImage.getWidth();
                    height = bufferedImage.getHeight();
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    log.error("status = {}, message = {}", "400", "파일명, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 사이즈를 확인 해주세요.", "400"));
                }

                //front id 기준
                List<IrInfo> irInfos = irInfoRepository.findbyLastIrId(PageRequest.of(0, 1));
                for (IrInfo irInfo : irInfos) {
                    if (irInfo.getFrontId() == null) {
                        frontId = 0;
                    } else {
                        frontId = irInfo.getFrontId();
                    }
                }

                IrInfo irInfo = IrInfo.builder()
                        .frontId(++frontId)
                        .irType(irType)
                        .irYear(irYear)
                        .irTitle(irInfoDto.getIrTitle())
                        .irFileOriginalName(uploadFile.getOriginName())
                        .irFileSavedName(uploadFile.getNewName())
                        .irFileSavedPath(uploadFile.getRealPath())
                        .year(irYear.getIyYear())
                        .language(lang)
                        .build();

                IrInfo savedIrInfo = irInfoRepository.save(irInfo);

                IrInfoInsertResponse insertResponse = IrInfoInsertResponse.builder()
                        .irInfo(savedIrInfo)
                        .width(width)
                        .height(height)
                        .build();


                return ResponseEntity.ok(insertResponse);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }

        } else {
            log.error("status = {}, message = {}", "400", "파일첨부는 필수입니다.");
            return ResponseEntity.badRequest().body(new ErrorResponse("파일첨부는 필수입니다.","400"));
        }
    }

    /**
     * IR 자료관리 수정
     */
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity irInfoUpdate(@RequestPart @Valid IrInfoDto irInfoDto, Errors errors,
                                       @RequestPart(name = "attachFile", required = false) MultipartFile attachFile,
                                       @PathVariable(name = "id") Long id,
                                       @PathVariable(name = "lang") String lang) {


        Optional<IrInfo> optionalIrInfo = irInfoRepository.findById(id);
        if (!optionalIrInfo.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 자료관리 정보가 없습니다. id를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 자료관리 정보가 없습니다. id를 확인해주세요.","400"));
        }

        IrInfo irInfo = optionalIrInfo.get();


        if (errors.hasErrors()) {
            log.error("status = {}, message = {}", "400", "IR 자료관리 수정 필수 값을 확인 해 주세요.");
            return ResponseEntity.badRequest().body(errors);
        }

        String title = irInfoDto.getIrTitle().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

        //MP("경영실적"), AR("감사보고서"),BR("사업보고서");
        IrType irType = getIrType(irInfoDto);
        if (irType == null) {
            log.error("status = {}, message = {}", "400", "IR 카테고리는 필수입니다.");
            return ResponseEntity.badRequest().body(new ErrorResponse("IR 카테고리는 필수입니다.", "400"));
        }

        Optional<IrYear> optionalIrYear = irYearRepository.findByIyYear(irInfoDto.getIrYear());
        if (!optionalIrYear.isPresent()) {
            log.error("status = {}, message = {}", "400", "해당 연도를 연도관리에서 추가 해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("해당 연도를 연도관리에서 추가 해주세요.", "400"));
        }
        IrYear irYear = optionalIrYear.get();

        if (attachFile != null) {
            try {

                UploadFile uploadFile = irInfoFileStore.storeFile(attachFile, savePath, whiteList, id);
                if (uploadFile.isWrongType()) {
                    log.error("status = {}, message = {}", "400", "파일명, 사이즈를 확인 해주세요.");
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 사이즈를 확인 해주세요.", "400"));
                }

                irInfo.updateFileIrInfo(uploadFile);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }

        }

        String year = irInfoDto.getIrYear();//list 화면 확인

        irInfo.updateIrInfo(irType, irYear, year, title);

        IrInfo updatedIrInfo = irInfoRepository.save(irInfo);

        IrInfoUpdateResponse updateResponse = IrInfoUpdateResponse.builder()
                .irInfo(updatedIrInfo)
                .irYear(updatedIrInfo.getIrYear().getIyYear())
                .build();

        return ResponseEntity.ok(updateResponse);
    }

    /**
     * IR 자료관리 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity irInfoDelete(@PathVariable(name = "id") Long id, @PathVariable(name = "lang") String lang) {

        Optional<IrInfo> optionalIrInfo = irInfoRepository.findById(id);
        if (!optionalIrInfo.isPresent()) {
            log.error("status = {}, message = {}", "400", "일치하는 자료관리 정보가 없습니다. id를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 자료관리 정보가 없습니다. id를 확인해주세요.","400"));
        }

        IrInfo irInfo = optionalIrInfo.get();
        irInfoRepository.deleteById(irInfo.getId());
        deleteFile(irInfo);

        return ResponseEntity.ok(id+"번 자료관리가 삭제되었습니다.");
    }

    /**
     * IR 자료관리 단건 다운로드
     */
    @GetMapping(value = "/down/{id}")
    public ResponseEntity recordFileDown(HttpServletResponse response,
                                         @PathVariable(name = "id") Long id,
                                         @PathVariable(name = "lang") String lang) throws UnsupportedEncodingException {

        Optional<IrInfo> optionalIrInfo = irInfoRepository.findById(id);
        if (!optionalIrInfo.isPresent()) {
            log.info("status = {}, message = {}", "400", "일치하는 IR 자료관리 정보가 없습니다. id를 확인해주세요.");
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 IR 자료관리 정보가 없습니다. 파일 id를 확인해주세요.","400"));
        }
        IrInfo irInfo = optionalIrInfo.get();

        return download(response, irInfo);
    }

    public ResponseEntity<?> download(HttpServletResponse response, IrInfo irInfo) {

        String fileSavedPath = fileDir + irInfo.getIrFileSavedPath() + "/" + irInfo.getIrFileSavedName();

        File file = new File(fileSavedPath);

        String name = StrUtil.encodeKR(irInfo.getIrFileOriginalName().replaceAll("(%20| |\\+)+", " "));

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


    public void deleteFile(IrInfo irInfo) {

        String fileSavedPath = fileDir + irInfo.getIrFileSavedPath() + "/" + irInfo.getIrFileSavedName();

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
