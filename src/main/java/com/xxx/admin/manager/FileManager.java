package com.xxx.admin.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.xxx.admin.data.TxtData;
import com.xxx.admin.data.mongo.MongoDao;

public class FileManager {

	public List<File> getChild(String filePath) {
		
		File file = new File(filePath);
		File[] child = file.listFiles();
		if (child != null) {
			List<File> list = new ArrayList<File>(child.length);
			for (File f : child) {
				list.add(f);
			}
			return list;
		} else {
			return new ArrayList<File>();
		}
	}
	
	public List<String> view(String filePath) {		
		return (List<String>)txtData.parse(filePath);
	}
	
	public Map taskView(String filePath,String separator) {		
		return txtData.TaskView(filePath, separator);
	}
	
	public boolean addFilePath(String filePath) {		
		return  mongoDao.addFilePath(filePath);
	}
	
	public boolean deleteFilePath(String filePath) {		
		return	mongoDao.deleteFilePath(filePath);
	}
	
	public List<String>  viewFilePath() {		
		return mongoDao.viewFilePath();
	}
	

	@Autowired
	private  MongoDao mongoDao;
	
	@Autowired
	private TxtData txtData;
	
}
