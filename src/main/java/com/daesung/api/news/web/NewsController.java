package com.daesung.api.news.web;

import com.daesung.api.news.domain.News;
import com.daesung.api.news.domain.NewsFile;
import com.daesung.api.news.domain.NewsThumbnailFile;
import com.daesung.api.news.domain.enumType.NbType;
import com.daesung.api.news.repository.NewsFileRepository;
import com.daesung.api.news.repository.NewsRepository;
import com.daesung.api.news.repository.NewsThumbnailFileRepository;
import com.daesung.api.news.resource.NewsResource;
import com.daesung.api.news.web.dto.NewsForm;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/{lang}/news")

public class NewsController {

    String savePath = "/news";

    private final ModelMapper modelMapper;
    private final FileStore fileStore;
    private final NewsRepository newsRepository;
    private final NewsThumbnailFileRepository newsThumbnailFileRepository;
    private final NewsFileRepository newsFileRepository;

    @GetMapping
    public String newsGet() {
        return "news/news_add";
    }

//    @ResponseBody
    @PostMapping
    public String newsPost(
            @Valid NewsForm newsForm, Errors errors, Model model,
            @PathVariable(name = "lang", required = true) String lang) {

        //뉴스
        if ("NE".equals(newsForm.getNbType())) {
            News news = News.builder()
                    .nbType(NbType.NE)
                    .title(newsForm.getTitle())
                    .content(newsForm.getContent())
                    .viewCnt(newsForm.getViewCnt())
                    .language(lang)
                    .build();

            News savedNews = newsRepository.save(news);
            System.out.println("savedNews = " + savedNews);


            if (!newsForm.getThumbnailFile().isEmpty()) {
                try {
                    UploadFile uploadFile = fileStore.storeFile(newsForm.getThumbnailFile(), savePath);

                    NewsThumbnailFile thumbnailFile = NewsThumbnailFile.builder()
                            .news(savedNews)
                            .thumbnailFileOriginalName(uploadFile.getOriginName())
                            .thumbnailFileSavedName(uploadFile.getNewName())
                            .thumbnailFileSavedPath(uploadFile.getRealPath())
                            .build();

                    newsThumbnailFileRepository.save(thumbnailFile);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (!newsForm.getNewsFiles().isEmpty()) {
                try {
                    List<UploadFile> uploadFiles = fileStore.storeFileList(newsForm.getNewsFiles(), savePath);

                    for (UploadFile uploadFile : uploadFiles) {

                        NewsFile newsFile = NewsFile.builder()
                                .news(savedNews)
                                .newsFileOriginalName(uploadFile.getOriginName())
                                .newsFileSavedName(uploadFile.getNewName())
                                .newsFileSavedPath(uploadFile.getRealPath())
                                .build();

                        newsFileRepository.save(newsFile);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            NewsResource newsResource = new NewsResource(savedNews);

            return null;
//            return ResponseEntity.ok().body(newsResource);

        }

        //보도
        if ("RE".equals(newsForm.getNbType())) {
            News news = News.builder()
                    .nbType(NbType.RE)
                    .newCompany(newsForm.getNewCompany())
                    .link(newsForm.getLink())
                    .viewCnt(newsForm.getViewCnt())
                    .language(lang)
                    .build();

            News savedNews = newsRepository.save(news);
            System.out.println("savedNews = " + savedNews);

            NewsResource newsResource = new NewsResource(savedNews);

            return null;
//            return ResponseEntity.ok().body(newsResource);
        }






        return null;
    }

//    @PostMapping("/news_add")
//    public String news_add(
//            @Valid NewsForm newsForm, Errors errors, Model model,
//            @RequestParam("attachFile") MultipartFile thumbnailFile,
//            @RequestParam("attachFileList")List<MultipartFile> newsFiles,
//            @PathVariable(name = "lang", required = true) String lang) {
//
//
//
//        if (!thumbnailFile.isEmpty()) {
//
//        }
//
//        for (MultipartFile newsFile : newsFiles) {
//            if (!newsFiles.isEmpty()) {
//
//            }
//        }
//
//
//    }


}
