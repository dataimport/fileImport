package com.xxx.admin.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xxx.admin.bean.base.BaseTask;


@Document(collection = "taskInfo") 

public class Task extends BaseTask{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8693317133670857920L;
	@Id  
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Task(){
		super();
	}
	
	public Task(String uid, String tableName, String origin, String tags,
			String[] columnName, Integer[] columnIndex, String separator,
			String runTime, String startDate, String endDate, String filePath,
			long fileSize, String leftTime, long totalCount, int taskStatus,
			String timeUse, int runNum, int updateOrAdd, int beginLineNum,
			String createUser, boolean firstLineIgnore, String id) {
		super(uid, tableName, origin, tags, columnName, columnIndex, separator,
				runTime, startDate, endDate, filePath, fileSize, leftTime,
				totalCount, taskStatus, timeUse, runNum, updateOrAdd,
				beginLineNum, createUser, firstLineIgnore);
		this.id = id;
	}
	

}
