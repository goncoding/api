package com.daesung.api.upload;


import com.daesung.api.utils.StrUtil;
import com.daesung.api.utils.upload.UploadFile;
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
public class FileStroe {

    @Value("${file.dir}")
    private String fileDir;

    String savePath = "/news";
    int size = 10;
//    public List<UploadFile02> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
//        List<UploadFile02> storeFileResult = new ArrayList<>();
//        for (MultipartFile multipartFile : multipartFiles) {
//            if (!multipartFile.isEmpty()) {
//                storeFileResult.add(storeFile(multipartFile));
//            }
//        }
//        return storeFileResult;
//    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        int max = 10; //최대사이즈 : 10MB;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());


        if (multipartFile.isEmpty()) {
            return null;
        }

        if (multipartFile != null && multipartFile.getSize() > 0 && !StrUtil.isEmpty(multipartFile.getName())) {

            String dir = fileDir + savePath + "/" + strToday;

            String whiteList = "jpg, png, gif, hwp, pdf, ppt, pptx, xls, xlsx, zip, doc";

            String originalFilename = multipartFile.getOriginalFilename();

            String originName = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("\\") + 1); //IE, EDGE has file path

            //서버저장
            String uuid = UUID.randomUUID().toString();

            boolean sizeOver = multipartFile.getSize() > size * 1024 * 1024;
            boolean badType = !_typeOk(whiteList, originalFilename);
            boolean noFileName = multipartFile == null || StrUtil.isEmpty(originName);

            UploadFile up = new UploadFile();
            if (noFileName) return up.setFileNameEmpty(true);
            if (sizeOver) return up.setSizeOver(true);
            if (badType) return up.setWrongType(true);

            String ext = originName.substring(originName.lastIndexOf('.'));
            String originName2 = originName.substring(0, originName.lastIndexOf('.'));
            String newFileName = originName2 + "_" + UUID.randomUUID().toString() + ext;

            File upFile = new File(dir, newFileName);
            upFile.getParentFile().mkdirs();

            multipartFile.transferTo(upFile);
            return up.setNewName(originName)
                    .setOriginName(newFileName)
                    .setRealPath(String.format("%s/%s", dir, newFileName));


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
