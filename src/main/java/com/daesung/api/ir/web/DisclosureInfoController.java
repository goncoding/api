package com.daesung.api.ir.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.ir.domain.DisclosureInfo;
import com.daesung.api.ir.domain.IrInfo;
import com.daesung.api.ir.repository.DisclosureInfoRepository;
import com.daesung.api.ir.resource.DisclosureInfoResource;
import com.daesung.api.ir.web.dto.DisclosureInfoDto;
import com.daesung.api.ir.web.dto.DisclosureInfoInsertResponse;
import com.daesung.api.utils.StrUtil;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/disclosure-info")
public class DisclosureInfoController {

    @Value("${file.dir}")
    private String fileDir;

    String savePath = "/disclosure_info";

    String whiteList = "all"; //모든 파일 통과

    String imageWhiteList = "jpg, gif, png";

    private final DisclosureInfoRepository disclosureInfoRepository;
    private final FileStore fileStore;

    //todo 리스트 조회 테스트 필요
    /**
     * 전자공시 리스트 조회
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

        Page<DisclosureInfo> disclosureInfoPage = disclosureInfoRepository.searchDisclosureInfoList(search, pageable);
        PagedModel<DisclosureInfoResource> pagedModel = assembler.toModel(disclosureInfoPage, d -> new DisclosureInfoResource(d));

        return ResponseEntity.ok(pagedModel);
    }



    /**
     * 전자공시 등록
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity disclosureInfoInsert(@RequestPart(name = "disClosureDto") @Valid DisclosureInfoDto disClosureDto, Errors errors,
                                               @RequestPart(name = "attachFile", required = false) MultipartFile attachFile,
                                               @PathVariable(name = "lang") String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        String title = disClosureDto.getDiTitle().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");

        if (attachFile != null) {
            try {

                int width = 0;
                int height = 0;

                boolean  extCheck = fileStore._typeOk(imageWhiteList, attachFile.getOriginalFilename());
                if (extCheck) {
                    BufferedImage bufferedImage = ImageIO.read(attachFile.getInputStream());

                    width = bufferedImage.getWidth();
                    height = bufferedImage.getHeight();
                }

                UploadFile uploadFile = fileStore.storeFile(attachFile, savePath, whiteList);
                if (uploadFile.isWrongType()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 사이즈를 확인 해주세요.", "400"));
                }

                DisclosureInfo disclosureInfo = DisclosureInfo.builder()
                        .diTitle(disClosureDto.getDiTitle())
                        .diFileOriginalName(uploadFile.getOriginName())
                        .diFileSavedName(uploadFile.getNewName())
                        .diFileSavedPath(uploadFile.getRealPath())
                        .language(lang)
                        .build();

                DisclosureInfo savedDisclosure = disclosureInfoRepository.save(disclosureInfo);

                DisclosureInfoInsertResponse insertResponse = DisclosureInfoInsertResponse.builder()
                        .disclosureInfo(savedDisclosure)
                        .width(width)
                        .height(height)
                        .build();

                return ResponseEntity.ok(insertResponse);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("파일첨부는 필수입니다.", "400"));
        }
    }


    /**
     * 전자공시 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity disclosureInfoDelete(@PathVariable(name = "id") Long id, @PathVariable(name = "lang") String lang) {

        Optional<DisclosureInfo> optionalDisclosureInfo = disclosureInfoRepository.findById(id);
        if (!optionalDisclosureInfo.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 전자공시 정보가 없습니다. id를 확인해주세요.","400"));
        }

        DisclosureInfo disclosureInfo = optionalDisclosureInfo.get();
        disclosureInfoRepository.deleteById(disclosureInfo.getId());
        deleteFile(disclosureInfo);

        return ResponseEntity.ok(id + "번 전자공시가 삭제되었습니다.");
    }

    /**
     * 전자공시 단건 다운로드
     */
    @GetMapping(value = "/down/{id}")
    public ResponseEntity recordFileDown(HttpServletResponse response,
                                         @PathVariable(name = "id") Long id,
                                         @PathVariable(name = "lang") String lang) throws UnsupportedEncodingException {

        Optional<DisclosureInfo> optionalDisclosureInfo = disclosureInfoRepository.findById(id);
        if (!optionalDisclosureInfo.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 전자공시 정보가 없습니다. 파일 id를 확인해주세요.","400"));
        }
        DisclosureInfo disclosureInfo = optionalDisclosureInfo.get();

        return download(response, disclosureInfo);
    }

    public ResponseEntity<?> download(HttpServletResponse response, DisclosureInfo disclosureInfo) {

        String fileSavedPath = fileDir + disclosureInfo.getDiFileSavedPath() + "/" + disclosureInfo.getDiFileSavedName();

        File file = new File(fileSavedPath);

        String name = StrUtil.encodeKR(disclosureInfo.getDiFileOriginalName().replaceAll("(%20| |\\+)+", " "));

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


    public void  deleteFile(DisclosureInfo disclosureInfo) {

        String fileSavedPath = fileDir + disclosureInfo.getDiFileSavedPath() + "/" + disclosureInfo.getDiFileSavedName();

        File file = new File(fileSavedPath);
        if (file.exists()) {
            file.delete();
        }
    }


}









































