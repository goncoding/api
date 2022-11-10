package com.daesung.api.utils.upload;

import com.daesung.api.utils.HtmlStringUtil;
import com.daesung.api.utils.date.DaesungDateUtil;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Log4j2
@Component
public class NasFileComponent {

//	@Value("${heavy.file.nas-storage:null}")
//	private String nasStorage;

	@Value("${file.dir}")
	private String nasStorage;


	@SneakyThrows
	public UploadFileDto saveMultipartFile(MultipartFile multipartFile, String nasSubPath) {
		Date currentDate = new Date();

		String currentDateSubPath = DaesungDateUtil.utcDateToStandardDateStringWithoutDash(currentDate);
		String originalFileName = multipartFile.getOriginalFilename();
		String fileName = DaesungDateUtil.utcDateToStandardTimeStringWithoutDash(currentDate) + "_" + originalFileName;

		Path filePath = Paths.get(nasStorage, nasSubPath, currentDateSubPath, fileName);
		Files.createDirectories(filePath.getParent());
		multipartFile.transferTo(filePath);

		UploadFileDto uploadFileDto = new UploadFileDto();

		uploadFileDto.setOriginalName(originalFileName);
		uploadFileDto.setSavedName(fileName);
		uploadFileDto.setSavedPath(nasSubPath + "/" + currentDateSubPath);

		return uploadFileDto;
	}

	public static void putFileToResponseStreamAsView(HttpServletResponse response, File file) {
		putFileToResponseStream(response, file);
	}

	public static void putFileToResponseStreamAsDownload(HttpServletResponse response, File file) {
		putFileToResponseStreamAsDownload(response, file, null);
	}

	public static void putFileToResponseStreamAsDownload(HttpServletResponse response, File file, String fileName) {
		setDownloadHeaders(response, file, fileName);
		putFileToResponseStream(response, file);
	}

	private static void putFileToResponseStream(HttpServletResponse response, File file) {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			IOUtils.copy(fileInputStream, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			log.info("Database entry is set but file is not found on server {}", file.getAbsolutePath());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not exists on the server");
		}
	}

	private static void setDownloadHeaders(HttpServletResponse response, File file, String fileName) {
		int fileSize = (int) file.length();
		if(fileName != null && !fileName.isEmpty()) {
			fileName = HtmlStringUtil.encodeUrlAsUtf8(fileName);
		} else {
			fileName = HtmlStringUtil.encodeUrlAsUtf8(file.getName());
		}

		response.setContentType("application/octet-stream");
		response.setContentLength(fileSize);
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";", fileName));
		response.setHeader("Content-Transfer-Encoding", "binary");
	}

	private NasFileComponent() {
	}
}
