package com.daesung.api.common.web;

import com.daesung.api.common.domain.enumType.EditorFile;
import com.daesung.api.common.repository.EditorFileRepository;
import com.daesung.api.common.response.ErrorResponse;
import com.daesung.api.history.domain.HistoryRecord;
import com.daesung.api.history.domain.HistoryRecordFile;
import com.daesung.api.utils.image.AccessLogUtil;
import com.daesung.api.utils.upload.FileStore;
import com.daesung.api.utils.upload.NasFileComponent;
import com.daesung.api.utils.upload.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.daesung.api.utils.upload.UploadUtil.UPLOAD_PATH;

@RestController
@RequiredArgsConstructor
public class UploadController {


    @Value("${file.dir}")
    private String fileDir;

    private final FileStore fileStore;
    private final EditorFileRepository editorFileRepository;


    String whiteList = "jpg,gif,png,hwp,pdf,pptx,ppt,xlsx,xls,xps,zip";


    /**
     * 파일 이미지 보이기
     */
    @GetMapping("/upload/{savePath}/{folderName}/{fileSaveName}")
    public void viewEditorImages(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable String savePath,
            @PathVariable String folderName,
            @PathVariable String fileSaveName) {

        AccessLogUtil.fileViewAccessLog(request);

        File serverFile = new File(fileDir + UPLOAD_PATH + "/" + savePath + "/" + folderName + "/" +fileSaveName);
        NasFileComponent.putFileToResponseStreamAsView(response, serverFile);
    }

    /**
     * 에디터 파일 이미지 저장
     */
    @PostMapping("/editor-upload/{savePath}")
    public ResponseEntity uploadEditorPost(MultipartFile editorFile,
                                       @PathVariable("savePath") String savePath) {

        if (editorFile != null) {

            savePath = "/"+savePath;

            try {
                UploadFile uploadFile = fileStore.storeFile(editorFile, savePath, whiteList);

                if (uploadFile.isWrongType()) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("파일명, 확장자, 사이즈를 확인 해주세요.","400"));
                }

                EditorFile file = EditorFile.builder()
                        .editFileOriginalName(uploadFile.getOriginName())
                        .editFileSavedName(uploadFile.getNewName())
                        .editFileSavedPath(uploadFile.getRealPath())
                        .build();

                EditorFile savedEditorFile = editorFileRepository.save(file);

                return ResponseEntity.ok(savedEditorFile);

            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage(),"500 (IOException)"));
            }

        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("에디터 첨부파일을 등록 해주세요.","400"));
        }
    }

   /**
     * 에디터 파일 이미지 삭제
     */
    @DeleteMapping("/editor-upload/{id}")
    public ResponseEntity uploadEditorDelete(MultipartFile editorFile,
                                       @PathVariable("id") Long id) {

        Optional<EditorFile> optionalEditorFile = editorFileRepository.findById(id);
        if (!optionalEditorFile.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("일치하는 에디터 첨부파일 정보가 없습니다. id를 확인해주세요.","400"));
        }

        EditorFile savedEditorFile = optionalEditorFile.get();

        editorFileRepository.deleteById(savedEditorFile.getId());
        deleteFile(savedEditorFile);

        return ResponseEntity.ok(id + "번 에디터 첨부파일이 삭제되었습니다.");
    }

    public void deleteFile(EditorFile editorFile) {

        String fileSavedPath = fileDir + editorFile.getEditFileSavedPath() + "/" + editorFile.getEditFileSavedName();

        File file = new File(fileSavedPath);
        if (file.exists()) {
            file.delete();
        }
    }


}
