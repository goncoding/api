package com.daesung.api.history.web;

import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.history.domain.History;
import com.daesung.api.history.repository.HistoryDetailRepository;
import com.daesung.api.history.repository.HistoryRepository;
import com.daesung.api.history.web.dto.HistoryRequestDto;
import com.daesung.api.news.web.dto.NewsDto;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.upload.UploadUtil.CHARSET_UTF8;

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

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)

    @PostMapping(value = "/modify/{id}", produces = MediaTypes.HAL_JSON_VALUE+CHARSET_UTF8)
    public ResponseEntity historyPut(
            @PathVariable Long id,
            @RequestPart(name = "requestDto") @Valid HistoryRequestDto requestDto,
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




}
