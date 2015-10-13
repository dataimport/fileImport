package com.xxx.core.exception;

public class FileImportException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7670110783919112099L;

	public  void FileImportException(Integer typeId,String message){
		this.id = typeId;
		this.message = message;
	}
}
