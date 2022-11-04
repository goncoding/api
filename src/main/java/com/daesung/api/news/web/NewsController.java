package com.daesung.api.news.web;

import com.daesung.api.news.web.dto.NewsForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
public class NewsController {

    @GetMapping("/news")
    public String news() {
        return "news/news_add";
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
