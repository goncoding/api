package com.daesung.api.history.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.history.domain.History;
import com.daesung.api.history.domain.HistoryDetail;
import com.daesung.api.history.repository.HistoryDetailRepository;
import com.daesung.api.history.repository.HistoryRepository;
import com.daesung.api.history.resource.HistoryDetailResource;
import com.daesung.api.history.resource.HistoryListResource;
import com.daesung.api.history.resource.HistoryResource;
import com.daesung.api.history.web.dto.HistoryDetailDto;
import com.daesung.api.history.web.dto.HistorytDto;
import com.daesung.api.news.domain.News;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.api.ApiUtils.CHARSET_UTF8;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{lang}/history")
public class HistoryController {

    @Value("${file.dir}")
    private String fileDir;


    String savePath = "/history";
    String thumbWhiteList = "jpg, gif, png";

    private final FileStore fileStore;
    private final HistoryRepository historyRepository;
    private final HistoryDetailRepository historyDetailRepository;

    //연혁 리스트
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity getHistoryListGet(Pageable pageable,
                                         PagedResourcesAssembler<History> assembler,
                                         @RequestParam(name = "page", required = false, defaultValue = "") String page,
                                         @RequestParam(name = "size", required = false, defaultValue = "") String size) {

        Page<History> historyPage = historyRepository.findAll(pageable);
        PagedModel<HistoryListResource> pagedModel = assembler.toModel(historyPage, h -> new HistoryListResource(h));
        return ResponseEntity.ok().body(pagedModel);
    }

    //연혁 불러오기
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity historyGet(@PathVariable(name = "id", required = false) Long id,
                                           @PathVariable(name = "lang", required = true) String lang){

        Optional<History> optionalHistory = historyRepository.findById(id);
        if (!optionalHistory.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 연혁 정보가 없습니다.","400"));
        }
        History history = optionalHistory.get();

        return ResponseEntity.ok(new HistoryResource(history));
    }


    //연혁 수정
    @PostMapping(value = "/modify/{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity historyUpdate(
            @PathVariable Long id,
            @RequestPart(name = "requestDto") @Valid HistorytDto requestDto,
            Errors errors,
            @RequestPart(name = "thumbnailFile",required = false) MultipartFile thumbnailFile,
            Model model,
            @PathVariable(name = "lang", required = true) String lang) {

        Optional<History> optionalHistory = historyRepository.findById(id);
        if (!optionalHistory.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("일치하는 연혁 정보가 없습니다. 연혁 id를 확인해주세요."));
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        History history = optionalHistory.get();
        history.changeContent(requestDto.getContent());

        if (thumbnailFile != null) {
            try {
                UploadFile uploadFile = fileStore.storeFile(thumbnailFile, savePath, thumbWhiteList);

                if (uploadFile.isWrongType()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }
                history.changeFileInfo(uploadFile);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        History updatedHistory = historyRepository.save(history);
        return ResponseEntity.ok(updatedHistory);
    }


    @GetMapping(value = "/detail", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity historyDetailGetList(Pageable pageable,
                                               PagedResourcesAssembler<HistoryDetail> assembler) {

        Sort sort = Sort.by("hdYear").descending().and(Sort.by("hdMonth").descending()).and(Sort.by("hdSequence").descending());
        Pageable pageRequest = PageRequest.of(0, 10, sort);

        Page<HistoryDetail> historyPage = historyDetailRepository.findAll(pageRequest);

        PagedModel<HistoryDetailResource> pagedModel = assembler.toModel(historyPage, h -> new HistoryDetailResource(h));

        return ResponseEntity.ok().body(pagedModel);
    }

    //연혁 상세 등록
    @PostMapping(value = "/detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity historyDetailInsert(@RequestBody @Valid HistoryDetailDto detailDto,
                                              Errors errors,
                                              @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        //해당 날짜에 맞는 history 등록
        String dtoHdYear = detailDto.getHdYear();
        History searchHistory = historyRepository.searchHistoryBetween(dtoHdYear);

        List<HistoryDetail> sequenceEqYear = historyDetailRepository.findByHdSequenceEqYearPlus(detailDto.getHdYear(), detailDto.getHdMonth(), detailDto.getHdSequence());
        for (HistoryDetail historyDetail : sequenceEqYear) {
            historyDetail.plusSequence();
        }

        HistoryDetail historyDetail = HistoryDetail.builder()
                .hdYear(detailDto.getHdYear())
                .hdMonth(detailDto.getHdMonth())
                .content(detailDto.getContent())
                .hdSequence(detailDto.getHdSequence())
                .history(searchHistory)
                .language(lang)
                .build();

        HistoryDetail savedDetail = historyDetailRepository.save(historyDetail);

        return ResponseEntity.ok(new HistoryDetailResource(savedDetail));
    }

    //연혁 세부 upadte
    @PutMapping(value = "/detail/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity historyDetailUpdate(@RequestBody @Valid HistoryDetailDto detailDto,
                                              Errors errors,
                                              @PathVariable(name = "id") Long id,
                                              @PathVariable(name = "lang", required = true) String lang) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<HistoryDetail> optionalHistoryDetail = historyDetailRepository.findById(id);
        if (!optionalHistoryDetail.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 연혁 세부 정보가 없습니다.","400"));
        }

        HistoryDetail getDetail = optionalHistoryDetail.get();

        if (getDetail.getHdYear().equals(detailDto.getHdYear()) && getDetail.getHdMonth().equals(detailDto.getHdMonth())) {
            if (detailDto.getHdSequence() > getDetail.getHdSequence()) {
                List<HistoryDetail> sequenceLtInputSeq = historyDetailRepository.findByHdSequenceLtInputSeq(getDetail.getHdYear(), getDetail.getHdMonth(), detailDto.getHdSequence(), getDetail.getHdSequence());
                for (HistoryDetail historyDetail : sequenceLtInputSeq) {
                    historyDetail.minusSequence();
                }
            }

        if (detailDto.getHdSequence() < getDetail.getHdSequence()) {
            List<HistoryDetail> sequenceLtInputSeq = historyDetailRepository.findByHdSequenceGtInputSeq(getDetail.getHdYear(), getDetail.getHdMonth(), detailDto.getHdSequence(), getDetail.getHdSequence());
            for (HistoryDetail historyDetail : sequenceLtInputSeq) {
                historyDetail.plusSequence();
            }
        }
        }

        HistoryDetail updatedDetail = historyDetailRepository.save(getDetail);

        return ResponseEntity.ok(new HistoryDetailResource(updatedDetail));
    }



    @GetMapping(value = "/management/{hitoryId}", produces = MediaTypes.HAL_JSON_VALUE + CHARSET_UTF8)
    public ResponseEntity historyManagementGet(@PathVariable(name = "hitoryId", required = false) Long hitoryId,
                                               @PathVariable(name = "lang", required = true) String lang) {

        List<HistoryDetail> detailList = historyDetailRepository.findByHistoryId(hitoryId);
        return ResponseEntity.ok().body(detailList);
    }


    //연혁 상세 불러오기
    @GetMapping(value = "/detail/{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity historyDetailGet(@PathVariable(name = "id", required = false) Long id,
                                            @PathVariable(name = "lang", required = true) String lang){

        Optional<HistoryDetail> optionalHistoryDetail = historyDetailRepository.findById(id);
        if (!optionalHistoryDetail.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 연혁 세부 정보가 없습니다.","400"));
        }
        HistoryDetail historyDetail = optionalHistoryDetail.get();

        HistoryDetailResource historyDetailResource = new HistoryDetailResource(historyDetail);
        historyDetailResource.add(linkTo(methodOn(HistoryController.class).historyDetailGet(historyDetail.getId(),historyDetail.getLanguage())).withSelfRel());
        historyDetailResource.add(linkTo(methodOn(HistoryController.class).historyDetailGet(historyDetail.getId(),historyDetail.getLanguage())).withSelfRel());

        return ResponseEntity.ok(new HistoryDetailResource(historyDetail));
    }

    @DeleteMapping(value = "/detail/{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity historyDetailDelete(@PathVariable(name = "id", required = false) Long id,
                                           @PathVariable(name = "lang", required = true) String lang){

        Optional<HistoryDetail> optionalHistoryDetail = historyDetailRepository.findById(id);
        if (!optionalHistoryDetail.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 연혁 세부 정보가 없습니다.","400"));
        }
        HistoryDetail historyDetail = optionalHistoryDetail.get();

        List<HistoryDetail> historyDetails = historyDetailRepository.findByHdSequenceEqYearMinus(historyDetail.getHdYear(), historyDetail.getHdMonth(), historyDetail.getHdSequence());

        for (HistoryDetail detail : historyDetails) {
            detail.minusSequence();
        }

        historyDetailRepository.deleteById(id);

        return ResponseEntity.ok(id+"번 연혁 상세정보 삭제 성공");
    }


}
