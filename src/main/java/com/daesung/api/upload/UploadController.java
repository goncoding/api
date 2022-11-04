package com.daesung.api.upload;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/spring/v1")
public class UploadController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload/upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file,
                           HttpServletRequest request) throws IOException {
        System.out.println("request = " + request);
        System.out.println("itemName = " + itemName);
        System.out.println("file = " + file);

        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            System.out.println("fullPath = " + fullPath);
            file.transferTo(new File(fullPath));
        }

        return "/upload/upload-form";


    }

}












