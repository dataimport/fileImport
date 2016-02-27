package com.xxx.admin.bean;
 
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = AllCollectionName.MONGOINTOERRORLOG) 
public class MongoInToErrorLog  implements Serializable{
 
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 208502938909182292L;

	@Id
	private String id;
	
	private String taskId;
    private String message;
    private String filePath;
    private String createTime;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MongoInToErrorLog(){
		
	}
	
	public MongoInToErrorLog(String taskId, String filePath, String message, String createTime) {
		super();
		this.taskId = taskId;
		this.filePath = filePath;
		this.message = message;
		this.createTime = createTime;
	}
    
}
