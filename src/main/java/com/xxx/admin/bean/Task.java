package com.xxx.admin.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xxx.admin.bean.base.BaseTask;


@Document(collection = AllCollectionName.TASKINFO_COLLECTIONNAME) 

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
	
	public Task(String uid, String tableName, String tableNameAlias, String origin, String tags, String[] columnName,
			String[] columnNameTag, Integer[] columnIndex, String separator, String runTime, String startDate,
			String endDate, String filePath, String fileName, String fileCode, long fileSize, String leftTime,
			long totalCount, int taskStatus, String timeUse, int runNum, String cleanOrAppend, int beginLineNum,
			String createUser, boolean firstLineIgnore) {
		super(uid, tableName, tableNameAlias, origin, tags, columnName, columnNameTag, columnIndex, separator, runTime,
				startDate, endDate, filePath, fileName, fileCode, fileSize, leftTime, totalCount, taskStatus, timeUse, runNum,
				cleanOrAppend, beginLineNum, createUser, firstLineIgnore);
		// TODO Auto-generated constructor stub
	}
	
}
