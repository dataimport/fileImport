package com.xxx.admin.service;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xxx.admin.bean.Folder;
import com.xxx.admin.data.mongo.FolderRepository;
import com.xxx.core.exception.MongoDBException;

@Service("folderService")
public class FolderService {

	public void add(Folder folder)throws MongoDBException {		
		folderRepository.saveObject(folder);
	}
	
	public boolean deleteById(String id) throws MongoDBException {		
		return	folderRepository.deleteObjectById(id);
	}
	
	public List<Folder> findByFields(String[] fields,String[] values)throws MongoDBException {		
		return	folderRepository.getObjectByFields(fields,values);
	}
	
	public List<Folder> list()throws MongoDBException {		
		return folderRepository.getAllObjects();
	}
	
	public File[] getChild(String folderPath) {			
		File file = new File(folderPath);
		if(file.exists()){
			File[] child = file.listFiles();		
			Arrays.sort(child, new Comparator<File>(){ 
			public int compare(File f1, File f2) 
			 { 
			    return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()); 
			 } 
			}); 
			return child;
		}else{
			return null;
		}
		
	}
	
	public File[] getChildByLastModified(final Long lastModified,String folderPath) {			
		File file = new File(folderPath);
	
		if(file.exists()){
			//过滤文件找到最后修改时间相近，还不是文件夹的文件
		    FileFilter filefilter = new FileFilter() {		    	
		        public boolean accept(File file) {
		        	if (!file.isDirectory()&&file.lastModified()<=lastModified) {
		                return true;
		            }
		            return false;
		        }
		    };
		   		    
			File[] child = file.listFiles(filefilter);			
			Arrays.sort(child, new Comparator<File>(){ 
			public int compare(File f1, File f2) 
			 { 
			    return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()); 
			 } 
			}); 
			return child;
		}else{
			return null;
		}
		
	}

	
	@Resource(name = "folder")
	FolderRepository folderRepository;
	
}
