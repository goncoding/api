package com.daesung.api.utils.upload;

public class UploadFile {

	boolean fileNameEmpty = false;
	boolean sizeOver = false;
	boolean wrongType = false;

	String originName;
	String newName;
	String realPath;
	String uriPath;


	public UploadFile setFileNameEmpty(boolean bool) {
		fileNameEmpty = bool;
		return this;
	}

	public UploadFile setSizeOver(boolean bool) {
		sizeOver = bool;
		return this;
	}

	public UploadFile setWrongType(boolean bool) {
		wrongType = bool;
		return this;
	}

	public boolean isFileNameEmpty() {
		return fileNameEmpty;
	}

	public boolean isSizeOver() {
		return sizeOver;
	}

	public boolean isWrongType() {
		return wrongType;
	}


	public boolean hasError() {
		return fileNameEmpty || sizeOver || wrongType;
	}


	public String getOriginName() {
		return originName;
	}

	public UploadFile setOriginName(String originName) {
		this.originName = originName;
		return this;
	}

	public String getNewName() {
		return newName;
	}

	public UploadFile setNewName(String newName) {
		this.newName = newName;
		return this;
	}

	public String getRealPath() {
		return realPath;
	}

	public UploadFile setRealPath(String realPath) {
		this.realPath = realPath;
		return this;
	}

	public String getUriPath() {
		return uriPath;
	}

	public UploadFile setUriPath(String uriPath) {
		this.uriPath = uriPath;
		return this;
	}
}