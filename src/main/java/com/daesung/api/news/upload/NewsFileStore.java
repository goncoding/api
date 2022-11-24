package com.daesung.api.news.upload;


import com.daesung.api.news.domain.NewsFile;
import com.daesung.api.news.domain.NewsThumbnailFile;
import com.daesung.api.news.repository.NewsFileRepository;
import com.daesung.api.news.repository.NewsThumbnailFileRepository;
import com.daesung.api.utils.StrUtil;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NewsFileStore {

    @Value("${file.dir}")
    private String fileDir;

    private final NewsFileRepository newsFileRepository;
    private final NewsThumbnailFileRepository newsThumbnailFileRepository;


    int size = 10;

    //다중 upload 처리
    public List<UploadFile> storeFileList(List<MultipartFile> multipartFiles, String savePath, String whiteList, Long id) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, savePath, whiteList,id));
            }
        }
        return storeFileResult;
    }

    //단일 upload 처리
    public UploadFile storeFile(MultipartFile multipartFile, String savePath, String whiteList, Long id) throws IOException {

        int max = 10; //최대사이즈 : 10MB;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());

        if (multipartFile != null && multipartFile.getSize() > 0 && !StrUtil.isEmpty(multipartFile.getName())) {

            String dir = fileDir + savePath + "/" + strToday;

//            String whiteList = "jpg, png, gif, hwp, pdf, ppt, pptx, xls, xlsx, zip, doc";

            String originalFilename = multipartFile.getOriginalFilename();

            String originName = multipartFile.getOriginalFilename()
                    .substring(multipartFile.getOriginalFilename().lastIndexOf("\\") + 1); //IE, EDGE has file path

            //서버저장
            String uuid = UUID.randomUUID().toString();

            boolean sizeOver = multipartFile.getSize() > size * 1024 * 1024;
            boolean badType = !_typeOk(whiteList, originalFilename);
            boolean noFileName = multipartFile == null || StrUtil.isEmpty(originName);

            UploadFile up = new UploadFile();
            if (noFileName) return up.setFileNameEmpty(true);
            if (sizeOver) return up.setSizeOver(true);
            if (badType) return up.setWrongType(true);

            List<NewsFile> newsFileList = newsFileRepository.findByNewsId(id);
            for (NewsFile newsFile : newsFileList) {
                String fileSavedPath = newsFile.getNewsFileSavedPath() + "/" + newsFile.getNewsFileSavedName();

                File file = new File(fileSavedPath);
                if (file.exists()) {
                    file.delete();
                }
            }
            newsFileRepository.deleteByNewsId(id);

            //coffee01.png
            String ext = originName.substring(originName.lastIndexOf('.'));
            String originName2 = originName.substring(0, originName.lastIndexOf('.'));
            String newFileName = originName2 + "_" + UUID.randomUUID().toString() + ext;

            File upFile = new File(dir, newFileName);
            if (!upFile.exists()) {
                upFile.getParentFile().mkdirs();
            }

            multipartFile.transferTo(upFile);
            return up.setNewName(newFileName)
                    .setOriginName(originName)
                    .setRealPath(String.format("%s", dir));

        }

        return null;
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }


    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);

    }

    private static final boolean _typeOk(String whiteList, String fileName) {
        System.out.println("########## whiteList : " + whiteList);
        System.out.println("########## fileName : " + fileName);
        String fileExt = getExtension(fileName);
        //return !fileExt.isEmpty();
        return ! fileExt.isEmpty() && whiteList.contains( fileExt );
    }

    public static final String getExtension(String fileName) {
        return StrUtil.nvl(fileName, "").replaceAll(".+\\.", "").toLowerCase();
    }


}
