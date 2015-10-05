package com.xxx.admin.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.xxx.admin.bean.AllCollectionName;
import com.xxx.admin.bean.NoRepeatColls;
import com.xxx.admin.bean.Task;
import com.xxx.admin.bean.base.BaseTask;
import com.xxx.admin.data.mongo.FileInMongoRepository;
import com.xxx.admin.data.mongo.MongoCollRepository;
import com.xxx.admin.file.analysis.TxtFileAnalysis;
import com.xxx.elasticsearch.data.mongo.SolrTaskRepository;

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
		  if("clean".equals(t.getCleanOrAppend())){//清空后追加
				fmRepository.dropCollection(t.getTableName());
				//mongo 方法是异步的，需要等几秒，确认已经drop掉
				int i=1;
				boolean canBreak = false;
				while(i<4&&!canBreak){//最多执行3次，					
					try{
						Thread.sleep(i*5000);						
					}catch(Exception ex){
						canBreak = true;
						ex.printStackTrace();
					}
					if(!fmRepository.collectionExists(t.getTableName())){
						canBreak = true;
					}
					//System.out.println(" dropCollection 第  " +i+" 次判断结果："+canBreak);
					i++;					
				}
			
				if(!canBreak){
					return false;
				}				
		  }
		  
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
				  String charset = txtFileAnalysis.getCharset(t.getFilePath());
				  while (fcin.read(rBuffer) != -1) {				
					  lines.clear();
					  lines = txtFileAnalysis.LoopBigFileByBuffer(fromIndex, endIndex, rSize,
								bs,enterStr,line,strBuf,
								fcin,rBuffer,lines,charset);	  
					  runNum+=lines.size();
					 fmRepository.FilePushToMongo(t, lines,true,runNum,System.currentTimeMillis()-start);					 
				  }					

				  //更新状态为导入完毕
				  String[] keys = new String[]{"taskStatus"};
				  Object[] values = new Object[]{BaseTask.TASK_STATUS_SUCCESS};			
				  fmRepository.updateFileInfoByField(t.getUid(), keys, values);
				  
				  //更新solrTask为等待状态
				  solrTaskRepository.updateTaskByField(t.getUid(), 
						  new String[]{"taskStatus","totalCount"},
						  new Object[]{BaseTask.TASK_STATUS_WAITING,runNum});
					
				  saveToRepeatColls(t);
				  System.out.println(" 总共 "+runNum+" 条记录 ");
				} catch (IOException e) {
					e.printStackTrace();					
				}			  
			  return true;
		  }else{
			  try{
				  List<String> lines = txtFileAnalysis.readSmallFile(t.getFilePath(),txtFileAnalysis.getCharset(t.getFilePath()));
				  fmRepository.FilePushToMongo(t, lines);
				  
				  //更新状态为导入完毕
				  String[] keys = new String[]{"taskStatus"};
				  Object[] values = new Object[]{BaseTask.TASK_STATUS_SUCCESS};			
				  fmRepository.updateFileInfoByField(t.getUid(), keys, values);
				  
				//更新solrTask为等待状态
				  solrTaskRepository.updateTaskByField(t.getUid(), 
						    new String[]{"taskStatus","totalCount"},
							new Object[]{BaseTask.TASK_STATUS_WAITING,lines.size()});
				  
				  saveToRepeatColls(t);
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
	
	private void saveToRepeatColls(Task t){
		 //保存mongodb中存数据的collection的信息				  
		  NoRepeatColls nrc =  mcRepository.getObjectsByName(t.getTableName());
		  if(nrc==null){
			  nrc = new NoRepeatColls();  						 
			  nrc.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
			  nrc.setName(t.getTableName());
			  mcRepository.saveObject(nrc);
		  }			
	}
	public int getFileLineNumber(String filePath){
		return txtFileAnalysis.getBigFileLineNumByCommand(filePath);
	}
	
	
	@Async
	public void getAndUpdateFileTotalCount(String uid,String filePath){
		int count = getFileLineNumber(filePath);
		fmRepository.updateFileInfoByField(uid, new String[]{"totalCount"},
				new Object[]{count});
	}

    @Resource
    FileInMongoRepository fmRepository;
    @Resource
    MongoCollRepository mcRepository;
	@Autowired
	private TxtFileAnalysis txtFileAnalysis;
	@Resource
	SolrTaskRepository solrTaskRepository;
}
