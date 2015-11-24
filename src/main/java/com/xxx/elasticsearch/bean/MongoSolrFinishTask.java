package com.xxx.elasticsearch.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xxx.admin.bean.AllCollectionName;
import com.xxx.admin.bean.base.BaseTask;


@Document(collection = AllCollectionName.SOLR_FINISH_TASKINFO_COLLECTIONNAME) 

public class MongoSolrFinishTask extends BaseTask{
	
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
	
	public MongoSolrFinishTask(){
		super();
	}
	
	public MongoSolrFinishTask(String uid, String tableName, String tableNameAlias, String origin, String tags,
			String[] columnName, Integer[] columnIndex, String separator,
			String runTime, String startDate, String endDate, String filePath,
			String fileName, long fileSize, String leftTime, long totalCount,
			int taskStatus, String timeUse, int runNum, int updateOrAdd,
			int beginLineNum, String createUser, boolean firstLineIgnore,
			String id) {
		super(uid, tableName,tableNameAlias, origin, tags, columnName, columnIndex, separator,
				runTime, startDate, endDate, filePath, fileName, fileSize,
				leftTime, totalCount, taskStatus, timeUse, runNum, updateOrAdd,
				beginLineNum, createUser, firstLineIgnore);
		this.id = id;
	}

}
