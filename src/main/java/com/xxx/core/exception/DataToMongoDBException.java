package com.xxx.core.exception;

public class DataToMongoDBException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7670110783919112099L;

	public DataToMongoDBException(Integer typeId,String message){
		this.id = typeId;
		this.message = message;
	}
	
	public DataToMongoDBException(){
		
	}
}
