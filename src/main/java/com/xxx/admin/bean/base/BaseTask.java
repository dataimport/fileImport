package com.xxx.admin.bean.base;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

public abstract class BaseTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5193521874045600482L;
	
	@Id  
	private Integer id;
	private String tableName;
	private String origin;
	private String tags;
	private String[] columnName;
	private Integer[] columnIndex;
	private String separator;
	private String runTime;
	private String startDate;
	private String endDate;
	private String filePath;
	private long fileSize;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String[] getColumnName() {
		return columnName;
	}
	public void setColumnName(String[] columnName) {
		this.columnName = columnName;
	}
	public Integer[] getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(Integer[] columnIndex) {
		this.columnIndex = columnIndex;
	}
	public String getSeparator() {
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	 @PersistenceConstructor  
	  public BaseTask(Integer id,String tableName,String origin,String tags,String[] columnName,Integer[] columnIndex,
			  	   String separator,String runTime,String startDate,String endDate,String filePath,Integer fileSize) {  
	    this.id = id;
		this.tableName = tableName;  
	    this.origin = origin;  
	    this.tags = tags;  
	    this.columnName = columnName;  
	    this.columnIndex = columnIndex;  
	    this.separator = separator;  
	    this.runTime = runTime;  
	    this.startDate = startDate;  
	    this.endDate = endDate; 
	    this.filePath = filePath;
	    this.fileSize = fileSize;
	  }  
}
