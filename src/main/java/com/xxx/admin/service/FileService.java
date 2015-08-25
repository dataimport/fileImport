package com.xxx.admin.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxx.admin.bean.Task;
import com.xxx.admin.data.mongo.FileInMongoRepository;
import com.xxx.admin.data.mongo.TaskRepository;
import com.xxx.admin.file.analysis.TxtFileAnalysis;

@Service("fileService")
public class FileService {
	
	public List<String> previewTxtFile(String filePath) {		
		return previewTxtFile(filePath,null);
	}
	
	public List<String> previewTxtFile(String filePath,Integer lineNum) {		
		return (List<String>)txtFileAnalysis.previewFileByLineNum(filePath,lineNum);
	}
	
	public Map viewBySeparator(String filePath,String separator) {		
		return viewBySeparator(filePath, separator,null);
	}
	
	public Map viewBySeparator(String filePath,String separator,Integer lineNum) {		
		return txtFileAnalysis.previewFileBySeparator(filePath, separator,lineNum);
	}
	
	/**
	 * 保存文件到mongodb中
	 * @param t
	 * @return
	 */
	public boolean saveFileToMongo(Task t) {		
		File file = new File(t.getFilePath());
		if(file.exists()){
		  if(file.length()/1048576>TxtFileAnalysis.FILESIZE_TIPPINGPOINT){//大文件			 
			  try {
				  FileChannel fcin = new RandomAccessFile(file, "r").getChannel();			
				  ByteBuffer rBuffer = ByteBuffer.allocate(TxtFileAnalysis.BUFFER_SIZE);
				  String enterStr = "\n"; // 换行符
				  byte[] bs = new byte[TxtFileAnalysis.BUFFER_SIZE];
				  int fromIndex=0, endIndex=0, rSize=0;
				  StringBuffer strBuf = new StringBuffer("");
				  String line = "";
				  List<String> lines = new ArrayList<String>();
				  int runNum=0;
				  long start = System.currentTimeMillis();			  
				  while (fcin.read(rBuffer) != -1) {				
					  lines.clear();
					  lines = txtFileAnalysis.LoopBigFileByBuffer(fromIndex, endIndex, rSize,
								bs,enterStr,line,strBuf,
								fcin,rBuffer,lines);	  
					  runNum+=lines.size();
					 fmRepository.FilePushToMongo(t, lines,true,runNum,System.currentTimeMillis()-start);					 
					}					
				  System.out.println(" 总共 "+runNum+" 条记录 ");
				} catch (IOException e) {
					e.printStackTrace();					
				}			  
			  return true;
		  }else{
			  try{
				  List<String> lines = txtFileAnalysis.readSmallFile(t.getFilePath());
				  fmRepository.FilePushToMongo(t, lines);
				  return true;
			  }catch(Exception ex){
				  ex.printStackTrace();
				  return false;
			  }			  
		  }
		  
		}else{
			return false;
		}
		
	}


    @Resource
    FileInMongoRepository fmRepository;
	@Autowired
	private TxtFileAnalysis txtFileAnalysis;
	
}
