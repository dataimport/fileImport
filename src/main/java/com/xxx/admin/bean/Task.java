package com.xxx.admin.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "taskInfo") 

public class Task {
	
	@Id  
	private String id;
	private String uid;
	private String tableName;
	private String origin;
	private String tags;
	private String[] columnName;
	private Integer[] columnIndex;
	private String separator;
	private String runTime;
	private String startDate="";
	private String endDate="";
	private String filePath;
	private long fileSize;
	private String leftTime;
	private long totalCount= 0l;
	private long executeCount;//已经处理过的行数
	private int taskStatus;  // 0 未执行  1 执行中 2 已完成  -2 失败
	private String timeUse;
	
	public String getTimeUse() {
		return timeUse;
	}
	public void setTimeUse(String timeUse) {
		this.timeUse = timeUse;
	}
		public int getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}
	public long getExecuteCount() {
		return executeCount;
	}
	public void setExecuteCount(long executeCount) {
		this.executeCount = executeCount;
	}
	public String getLeftTime() {
		return leftTime;
	}
	public void setLeftTime(String leftTime) {
		this.leftTime = leftTime;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Task(String uid,String id,String tableName,String origin,String tags,String[] columnName,Integer[] columnIndex,
			  	   String separator,String runTime,String startDate,String endDate,String filePath,Integer fileSize,
			  	   String leftTime,long totalCount,long executeCount,int taskStatus) {  
	    this.uid = uid;
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
	    this.leftTime = leftTime;
	    this.totalCount = totalCount;
	    this.executeCount = executeCount;
	    this.taskStatus = taskStatus;
	  }  
	
	  public Task(){
		  
	  }
}
