package com.daesung.api.utils.upload;

import com.daesung.api.utils.StrUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public class UploadUtil {

	public static final String UPLOAD_PATH = "/upload";
	public static final int IMAGE = 1;
	public static final int DOCUMENT = 2;
	public static final int VIDEO = 3;
	public static final int ALL = 9;
	

	/**
	 * Return extension from file name
	 *
	 * @param fileName
	 * @return String
	 */
	public static final String getExtension(String fileName) {
		return StrUtil.nvl(fileName, "").replaceAll(".+\\.", "").toLowerCase();
	}

	/**
	 * Return extension of file from its name
	 *
	 * @return String
	 */
	public static final String getExtension(File file) {
		return getExtension(file.getName());
	}

	/**
	 * Return whether this file type is allowed or not.
	 *
	 * @param whiteList
	 * @param fileName
	 * @return true if this file has allowed extension.
	 */
	private static final boolean _typeOk(String whiteList, String fileName) {
		System.out.println("########## whiteList : " + whiteList);
		System.out.println("########## fileName : " + fileName);
		String fileExt = getExtension(fileName);
		//return !fileExt.isEmpty();
		return ! fileExt.isEmpty() && whiteList.contains( fileExt );
	}

	public static final UploadFile heavyUpload(MultipartFile multipart, String dir, String uriPath, String whiteList, int size) throws Exception {
		UploadFile up = new UploadFile();
		
		String originName = multipart.getOriginalFilename().substring(multipart.getOriginalFilename().lastIndexOf("\\") + 1); //IE, EDGE has file path
		boolean sizeOver = multipart.getSize() > size * 1024 * 1024;
		boolean badType = !_typeOk(whiteList, originName);
		boolean noFileName = multipart == null || StrUtil.isEmpty(originName);

		if (noFileName) return up.setFileNameEmpty(true);
		if (sizeOver) return up.setSizeOver(true);
		if (badType) return up.setWrongType(true);

		String ext = originName.substring(originName.lastIndexOf('.'));
		String originName2 = originName.substring(0, originName.lastIndexOf('.'));
		String newFileName = originName2 + "_" + UUID.randomUUID().toString() + ext;		
		
		File upFile = new File(dir, newFileName);
		upFile.getParentFile().mkdirs();

		multipart.transferTo(upFile);
		return up.setNewName(originName)
				.setOriginName(newFileName)
				.setRealPath(String.format("%s/%s", dir, newFileName))
				.setUriPath(String.format("%s/%s", uriPath, newFileName));
	}	
}