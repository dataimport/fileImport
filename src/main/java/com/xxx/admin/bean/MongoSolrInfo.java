package com.xxx.admin.bean;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = AllCollectionName.MONGOTOSOLR_COLLECTIONNAME) 
public class MongoSolrInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -586475284371595282L;
	
	@Id
    private String id;
	private String  uid  ;       //目录的唯一id
	private String  filePath  ;       //文件路径
	private String  fileName;    //文件名称
	private String  fileInfoUid; //所有文件信息表中的uid
	private String  updateTime;//记录更新时间
	private String  createTime;//创建时间
	private String  createUser;//目录的创建者
	private String  collectionName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileInfoUid() {
		return fileInfoUid;
	}
	public void setFileInfoUid(String fileInfoUid) {
		this.fileInfoUid = fileInfoUid;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public MongoSolrInfo(){
		
	}
	public MongoSolrInfo(String id, String uid, String filePath,
			String fileName, String fileInfoUid, String updateTime,
			String createTime, String createUser, String collectionName) {
		super();
		this.id = id;
		this.uid = uid;
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileInfoUid = fileInfoUid;
		this.updateTime = updateTime;
		this.createTime = createTime;
		this.createUser = createUser;
		this.collectionName = collectionName;
	}
		
}
