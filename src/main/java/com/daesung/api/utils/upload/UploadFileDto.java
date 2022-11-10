package com.daesung.api.utils.upload;

import lombok.Data;

@Data
public class UploadFileDto {

	private String originalName;
	private String savedName;
	private String savedPath;
}
