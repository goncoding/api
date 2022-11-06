package com.daesung.api.upload;

import com.daesung.api.news.repository.NewsFileRepository;
import com.daesung.api.news.repository.NewsThumbnailFileRepository;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final NewsFileRepository newsFileRepository;
    private final NewsThumbnailFileRepository newsThumbnailFileRepository;
    private final FileStore fileStroe;
    

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm itemForm) {
        return "upload/item-form";
    }

    @PostMapping("/items/new")
    public String postNewItem(@ModelAttribute ItemForm itemForm, RedirectAttributes redirectAttributes) throws IOException {

        System.out.println("itemForm = " + itemForm);

        String savePath = "/news";

        UploadFile uploadFile = fileStroe.storeFile(itemForm.getAttachFile(), savePath);

//        List<UploadFile02> uploadFileList = fileStroe.storeFiles(itemForm.getAttachFileList());
//
//        //db저장
//        NewsThumbnailFile thumbnailFile = NewsThumbnailFile.builder()
//                .thumbnailFileOriginalName(uploadFile.getUploadFileName())
//                .thumbnailFileSavedName(uploadFile.getStoreFileName())
//                .build();

//        NewsThumbnailFile savedFile = newsThumbnailFileRepository.save(thumbnailFile);

//        redirectAttributes.addAttribute("thumbId",savedFile.getId());

        return "redirect:/items/";
    }




}
