package com.xxx.core.exception;

public abstract class BaseException  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6741666371073462645L;

	public static final int PREVIEW_FILE_CODE = 1;
	public static final int READSMALL_FILE_CODE = 2;
	public static final int READBIG_FILE_CODE = 3;
	public static final int GET_FILE_LINENUMBER_CODE = 4;
	
	public Integer id;
	public String message;
	
}
