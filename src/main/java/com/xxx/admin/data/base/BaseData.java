package com.xxx.admin.data.base;

import java.io.File;
import java.io.Serializable;

public abstract class BaseData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8478437595743237957L;

	/**
	 * 解析文件
	 * @param filePath
	 * @param separator
	 */
	public Object parse(String filePath){
		return null;
	}
	
	/**
	 * 获取文件信息，文件大小、路径等等。
	 * @param filePath
	 * @param separator
	 * @return
	 */
	public String getFileInfo(String filePath,String separator){
		return  null;
	}
	
	/**
	 * 创建定时任务
	 * @param fileInfo 文件信息
	 * @param separator 分隔符
	 * @param columns 行字段
	 * @param rowNum  选择那几列
	 * @param mongoCollectionName 存储的mogodb
	 * @return
	 */
	public String createTask(File fileInfo,String separator,String[] columns,String[] rowNum,String mongoCollectionName){
		return  null;
	}
	
}
