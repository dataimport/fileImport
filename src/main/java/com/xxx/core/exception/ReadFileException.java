package com.xxx.core.exception;

public class ReadFileException  extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7670110783919112099L;

	public ReadFileException(int typeId,String message){
		this.id = typeId;
		this.message = message;
	}
	
	public ReadFileException(){
		
	}
}
