package com.xxx.admin.bean;


public class SubFolder extends Folder{
	/**
	 * 文件夹列表
	 */
	private static final long serialVersionUID = 1L;
	private String folderName;
    
    public SubFolder(String folderName,String folderPath){
    	this.folderName=folderName;
    	super.folderPath=folderPath;
    }


	public String getFolderName() {
		return folderName;
	}



	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

}
