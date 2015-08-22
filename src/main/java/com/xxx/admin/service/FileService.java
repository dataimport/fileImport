package com.xxx.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxx.admin.file.analysis.FileAnalysis;

@Service("fileService")
public class FileService {
	
	public List<String> preview(String filePath) {		
		return (List<String>)fileAnalysis.parse(filePath);
	}
	
	public Map view(String filePath,String separator) {		
		return fileAnalysis.getContentBySeparator(filePath, separator);
	}

	
	@Autowired
	private FileAnalysis fileAnalysis;
	
}
