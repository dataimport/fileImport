package com.xxx.admin.bean;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection =  AllCollectionName.FOLDERINFO_COLLECTIONNAME) 
public class Folder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1470786812183947814L;
	@Id  
	private String id;
	private String  folderId  ;       //目录的唯一id
	protected String  folderPath;    //目录的路径
	private String    createTime; //记录创建时间
	private String    updateTime;//记录更新时间
	private String  createUser;//目录的创建者
	
	
	//重新equals方法，用于验证目录的path即可
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Folder){
			Folder folder = (Folder) obj;
			if(this.folderPath.equals(folder.getFolderPath())){
				return true;
			}
		}
		return false;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public Folder(String folderId, String folderPath, String createTime,
			String updateTime, String createUser) {
		super();
		this.folderId = folderId;
		this.folderPath = folderPath;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.createUser = createUser;
	}
	
	public Folder() {
		
	}
		
}
