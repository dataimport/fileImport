package com.xxx.admin.service;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xxx.admin.bean.Folder;
import com.xxx.admin.data.mongo.FolderRepository;

@Service("folderService")
public class FolderService {

	public void add(Folder folder) {		
		folderRepository.saveObject(folder);
	}
	
	public boolean deleteById(String id) {		
		return	folderRepository.deleteObjectById(id);
	}
	
	public List<Folder> findByFields(String[] fields,String[] values) {		
		return	folderRepository.getObjectByFields(fields,values);
	}
	
	public List<Folder> list() {		
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
	
	@Resource(name = "folder")
	FolderRepository folderRepository;
	
}
