package com.xxx.admin.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.xxx.admin.bean.MongoInToErrorLog;
import com.xxx.admin.bean.NoRepeatColls;
import com.xxx.admin.bean.Task;
import com.xxx.admin.bean.base.BaseTask;
import com.xxx.admin.data.mongo.FileInMongoRepository;
import com.xxx.admin.data.mongo.MongoCollRepository;
import com.xxx.admin.data.mongo.MongoIntoErrorInfo;
import com.xxx.admin.file.analysis.ExcelFileAnalysis;
import com.xxx.admin.file.analysis.TxtFileAnalysis;
import com.xxx.core.exception.ReadFileException;
import com.xxx.elasticsearch.data.mongo.SolrTaskRepository;

@Service("fileService")
public class FileService {
	
	private static final Logger log = LoggerFactory.getLogger(FileService.class);
	
	public List<String> previewTxtFile(String filePath) throws ReadFileException{		
		return previewTxtFile(filePath,null);
	}
	
	public  Map<String,Object>  previewExcelFile(String filePath,Integer lineNum,String excelVersion) throws Exception{		
		return excelFileAnalysis.previewFileByLineNum(filePath,lineNum,excelVersion);
	}
	
	
	public List<String> previewTxtFile(String filePath,Integer lineNum) throws ReadFileException{		
		return (List<String>)txtFileAnalysis.previewFileByLineNum(filePath,lineNum);
	}
	
	public Map viewBySeparator(String filePath,String separator) throws ReadFileException{		
		return viewBySeparator(filePath, separator,null);
	}
	
	public Map viewBySeparator(String filePath,String separator,Integer lineNum) throws ReadFileException{		
		return txtFileAnalysis.previewFileBySeparator(filePath, separator,lineNum);
	}
		
	/**
	 * 保存文件到mongodb中
	 * @param t
	 * @return
	 */
	public int saveFileToMongo(Task t) {		
	    String filePath = t.getFilePath();
		File file = new File(filePath);
		if(file.exists()){
		//int successNum=-1;			  
		if("clean".equals(t.getCleanOrAppend())){//清空后追加
				fmRepository.dropCollection(t.getTableNameAlias());
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
					if(!fmRepository.collectionExists(t.getTableNameAlias())){
						canBreak = true;
					}
					//System.out.println(" dropCollection 第  " +i+" 次判断结果："+canBreak);
					i++;					
				}
			
				if(!canBreak){
					return -1;
				}				
		  }
		  
		 String extension  = t.getFilePath().substring(filePath.lastIndexOf(".")+1, filePath.length());
		 if("txt".equals(extension.toLowerCase())){//txt文件
			 return  txtFileImport(file,t);
		 }else if("xlsx".equals(extension.toLowerCase())||"xls".equals(extension.toLowerCase())){//excel
			 try{
				 return  excelFileImport(t);
			 }catch(Exception e){
				 log.error("excel 导入到mong异常： ",e.getMessage());
				 return -1;
			 }
		 }else{
			//文件不存在，导入失败，记录到失败日志
			 MongoInToErrorLog mel = new MongoInToErrorLog(t.getUid(),t.getFilePath(),"文件格式不是txt、xls、或者xlsx的",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			 mongoErrorLog.saveObject(mel);
			 return -1;
		 }
		}else{
			//文件不存在，导入失败，记录到失败日志
			MongoInToErrorLog mel = new MongoInToErrorLog(t.getUid(),t.getFilePath(),"文件不存在",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			mongoErrorLog.saveObject(mel);
			return -1;
			
		}
		
	}
	
	public MongoInToErrorLog getErrorLogByTaskId(String id){
		try{
			return mongoErrorLog.getObjectByTaskId(id);
		}catch(Exception ex){
			log.error("根据taskId获取所有的错误日志失败",ex);
		}
		return new MongoInToErrorLog();
		
	}
	
	public List<MongoInToErrorLog> getAllErrorLogByTaskId(String id){
		try{
			return mongoErrorLog.getAllObjectByTaskId(id);
		}catch(Exception ex){
			log.error("根据taskId获取所有的错误日志失败",ex);
		}
		return new ArrayList<MongoInToErrorLog>();
		
	}
		
	public int getFileLineNumber(String filePath){
		String extension  = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());
		if("txt".equals(extension.toLowerCase())){
			return txtFileAnalysis.getBigFileLineNumByCommand(filePath);
		}else if("xlsx".equals(extension.toLowerCase())||"xls".equals(extension.toLowerCase())){
			return excelFileAnalysis.getFileLineNum(filePath);
		}
		return 1;
	}
	
	
	@Async
	public void getAndUpdateFileTotalCount(String uid,String filePath){
		int count = getFileLineNumber(filePath);
		fmRepository.updateFileInfoByField(uid, new String[]{"totalCount"},
				new Object[]{count});
	}
	
	private void saveToRepeatColls(Task t){
		 //保存mongodb中存数据的collection的信息				  
		  NoRepeatColls nrc =  mcRepository.getObjectsByName(t.getTableName());
		  if(nrc==null){
			  nrc = new NoRepeatColls();  						 
			  nrc.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
			  nrc.setName(t.getTableName());
			  nrc.setNameAlias(t.getTableNameAlias());
			  mcRepository.saveObject(nrc);
		  }			
	}

    /**
     * 导入txt文件	
     * @param file
     * @param t
     * @param successNum
     * @return
     */
	private int txtFileImport(File file,Task t){
		  int successNum = -1;
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
				  int nownum=0;
				  long start = System.currentTimeMillis();		
				  String charset = txtFileAnalysis.getCharset(t.getFilePath());
				  while (fcin.read(rBuffer) != -1) {				
					  lines.clear();
					  lines = txtFileAnalysis.LoopBigFileByBuffer(fromIndex, endIndex, rSize,
								bs,enterStr,line,strBuf,
								fcin,rBuffer,lines,charset);	 					 
					 successNum+=fmRepository.FilePushToMongo(t, lines,true,runNum,System.currentTimeMillis()-start);		
					 runNum+=lines.size();
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
				} catch (Exception e) {
					e.printStackTrace();
					log.error("保存大文件到mongodb异常： ", e);	
					MongoInToErrorLog mel = new MongoInToErrorLog(t.getUid(),t.getFilePath(),"导入数据异常："+e.getMessage(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					mongoErrorLog.saveObject(mel);
					//更新状态为导入失败
					String[] keys = new String[]{"taskStatus"};
					Object[] values = new Object[]{BaseTask.TASK_STATUS_FAILED};			
					fmRepository.updateFileInfoByField(t.getUid(), keys, values);
					return -1;
				}			  
			  return successNum;
		  }else{//小文件
			  try{
				  List<String> lines = txtFileAnalysis.readSmallFile(t.getFilePath(),txtFileAnalysis.getCharset(t.getFilePath()));
				  successNum = fmRepository.FilePushToMongo(t, lines,0);
				  
				  //更新状态为导入完毕
				  String[] keys = new String[]{"taskStatus"};
				  Object[] values = new Object[]{BaseTask.TASK_STATUS_SUCCESS};			
				  fmRepository.updateFileInfoByField(t.getUid(), keys, values);
				  
				//更新solrTask为等待状态
				  solrTaskRepository.updateTaskByField(t.getUid(), 
						    new String[]{"taskStatus","totalCount"},
							new Object[]{BaseTask.TASK_STATUS_WAITING,lines.size()});
				  
				  saveToRepeatColls(t);
				  return successNum;
			  }catch(Exception ex){
				  ex.printStackTrace();
				  log.error("保存小文件到mongodb异常： ", ex);	
				  MongoInToErrorLog mel = new MongoInToErrorLog(t.getUid(),t.getFilePath(),"导入数据异常："+ex.getMessage(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				  mongoErrorLog.saveObject(mel);
				  //更新状态为导入失败
				  String[] keys = new String[]{"taskStatus"};
				  Object[] values = new Object[]{BaseTask.TASK_STATUS_FAILED};			
				  fmRepository.updateFileInfoByField(t.getUid(), keys, values);
				  return -1;
			  }			  
		  }
	}
	
	/**
	 * 导入excel
	 * @param file
	 * @param t
	 * @param successNum
	 * @return
	 * @throws Exception 
	 */
	private int excelFileImport(Task t) throws Exception{
		int successNum = -1;
		int totalNumber = excelFileAnalysis.getFileLineNum(t.getFilePath());
		Map<String,Object> map= excelFileAnalysis.readAllLines(t.getFilePath(),totalNumber);
		List<String[]>  list = ( List<String[]>)map.get("list");
		successNum = fmRepository.excelFilePushToMongo(t, list);
		  //更新状态为导入完毕
		  String[] keys = new String[]{"taskStatus"};
		  Object[] values = new Object[]{BaseTask.TASK_STATUS_SUCCESS};			
		  fmRepository.updateFileInfoByField(t.getUid(), keys, values);
		  
		//更新solrTask为等待状态
		solrTaskRepository.updateTaskByField(t.getUid(), 
				    new String[]{"taskStatus","totalCount"},
					new Object[]{BaseTask.TASK_STATUS_WAITING,successNum});
		  
		  saveToRepeatColls(t);
		  return successNum;
	}
	
	
    @Resource
    FileInMongoRepository fmRepository;
    @Resource
    MongoCollRepository mcRepository;
	@Autowired
	private TxtFileAnalysis txtFileAnalysis;
	@Autowired
	private ExcelFileAnalysis excelFileAnalysis;
	@Resource
	SolrTaskRepository solrTaskRepository;
	@Resource
	MongoIntoErrorInfo mongoErrorLog;
}
