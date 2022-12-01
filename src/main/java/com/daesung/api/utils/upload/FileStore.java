package com.daesung.api.utils.upload;


import com.daesung.api.utils.StrUtil;
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

import static com.daesung.api.utils.upload.UploadUtil.UPLOAD_PATH;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    int size = 10;

    //다중 upload 처리
    public List<UploadFile> storeFileList(List<MultipartFile> multipartFiles, String savePath, String whiteList) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, savePath, whiteList));
            }
        }
        return storeFileResult;
    }

    //단일 upload 처리
    public UploadFile storeFile(MultipartFile multipartFile, String savePath, String whiteList) throws IOException {

        int max = 10; //최대사이즈 : 10MB;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());

        if (multipartFile != null && multipartFile.getSize() > 0 && !StrUtil.isEmpty(multipartFile.getName())) {

            String filePath = savePath + "/" + strToday;
            String dir = fileDir + UPLOAD_PATH + filePath;

//            String whiteList = "jpg, png, gif, hwp, pdf, ppt, pptx, xls, xlsx, zip, doc";

            String originalFilename = multipartFile.getOriginalFilename();

            String originName = multipartFile.getOriginalFilename()
                    .substring(multipartFile.getOriginalFilename().lastIndexOf("\\") + 1); //IE, EDGE has file path

            //서버저장
            String uuid = UUID.randomUUID().toString();

            boolean sizeOver = multipartFile.getSize() > size * 1024 * 1024;
            boolean badType = !_typeOk(whiteList, originalFilename);
            if ("all".equals(whiteList)) {
                badType = false;
            }
            boolean noFileName = multipartFile == null || StrUtil.isEmpty(originName);

            UploadFile up = new UploadFile();
            if (noFileName) return up.setFileNameEmpty(true);
            if (sizeOver) return up.setSizeOver(true);
            if (badType) return up.setWrongType(true);
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
                    .setRealPath(String.format("%s%s", UPLOAD_PATH, filePath));

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

    public boolean _typeOk(String whiteList, String fileName) {
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
