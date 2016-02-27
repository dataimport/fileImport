package com.xxx.admin.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.xxx.admin.bean.AllCollectionName;
import com.xxx.admin.bean.MongoInToErrorLog;
import com.xxx.admin.bean.MongoSolrInfo;
import com.xxx.admin.bean.SolrTask;
import com.xxx.admin.bean.Task;
import com.xxx.admin.bean.base.BaseTask;
import com.xxx.admin.data.mongo.MongoIntoErrorInfo;
import com.xxx.admin.data.mongo.MongoToSolrRepository;
import com.xxx.admin.data.mongo.TaskRepository;
import com.xxx.admin.file.analysis.TxtFileAnalysis;
import com.xxx.elasticsearch.data.mongo.SolrTaskRepository;
import com.xxx.utils.Pagination;
import com.xxx.utils.StrUtils;

@Service("taskService")
public class TaskService {
	private static final Logger log = LoggerFactory.getLogger(TaskService.class);
	/**
	 * 创建任务
	 * @param task
	 */
	public boolean createTask(Task task,Boolean runNow) {
		try{
			
			setTaskInfo(task);
			if(!runNow){//不是立即执行，放到任务表中
				taskRepository.saveObject(task,AllCollectionName.TASKINFO_COLLECTIONNAME); //保存到任务表中
			}			
			
			if("append".equals(task.getCleanOrAppend())){//追加
				 MongoSolrInfo msi =  mongoToSolrRepository.getOneByFields(
						  new String[]{"collectionName"},new String[]{task.getTableName()});
				  if(msi!=null){
					  task.setTags(msi.getTags());
					  task.setOrigin(msi.getOrigin());
				  }
			}
			
			//保存到所有文档表中	
			taskRepository.saveObject(task,AllCollectionName.ALLFILEINFO_COLLECTIONNAME); 	
		
			
			//保存到表-文件信息表中			
			MongoSolrInfo msi = new MongoSolrInfo();
			msi.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
			msi.setCollectionName(task.getTableName());
			msi.setCollectionNameAlias(task.getTableNameAlias());
			msi.setFilePath(task.getFilePath());
			msi.setFileInfoUid(task.getUid());
			msi.setTags(task.getTags());
			msi.setStatus(0);
			msi.setOrigin(task.getOrigin());
			msi.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));				  
			mongoToSolrRepository.saveObject(msi);
			
			//更新文件总行数
			//不能直接这么用，异步调用，会报异常org.springframework.aop.AopInvocationException: Null return value from advice does not match primitive return type for: public int com.xxx.admin.service.FileService.getAndUpdateFileTotalCount(java.lang.String,java.lang.String)

//			int totalCount = fileService.getAndUpdateFileTotalCount(task.getUid(),task.getFilePath());
			fileService.getAndUpdateFileTotalCount(task.getUid(),task.getFilePath());
			
			//保存一份任务信息到solr入库的任务表中
			//TODO 其实在这个环节，写入，有些不妥，应该在入mongo任务完成后
			
			SolrTask slorTask = new SolrTask(task.getUid(), task.getTableName(),task.getTableNameAlias(), task.getOrigin(), task.getTags(),
					task.getColumnName(),task.getColumnNameTag(), task.getColumnIndex(), task.getSeparator(),
					task.getRunTime(), task.getStartDate(), task.getEndDate(), task.getFilePath(),
					task.getFileName(), task.getFileSize(), task.getLeftTime(),task.getTotalCount(),
					BaseTask.TASK_STATUS_SOLR_WAITING,
					task.getTimeUse(), task.getRunNum(), 0,
					task.getBeginLineNum(), task.getCreateUser(), false,
					task.getId());
			solrTaskRepository.saveObject(slorTask, AllCollectionName.SOLR_TASKINFO_COLLECTIONNAME);
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("创建任务异常 ", ex);	
			MongoInToErrorLog mel = new MongoInToErrorLog(task.getUid(),task.getFilePath(),"创建任务异常： "+ex.getMessage(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			mongoErrorLog.saveObject(mel);
		}		
		return false;
	
	}

	
	public boolean createTask(Task task) {
		return createTask(task,true);
	}	
	
	
	/**
	 * 更新任务
	 * @param task
	 */
	public void taskUpdate(Task task,String startDate,String endDate,int status) {
		try{
			//更新任务表
			if(StringUtils.isNotBlank(startDate)){
				task.setStartDate(startDate);
			}	
			if(StringUtils.isNotBlank(endDate)){
				task.setEndDate(endDate);
			}			
			task.setTaskStatus(status);						
			taskRepository.updateTaskByField(task.getUid(), new String[]{"taskStatus","startDate","endDate"},
					new Object[]{task.getTaskStatus(),task.getStartDate(),task.getEndDate()},AllCollectionName.TASKINFO_COLLECTIONNAME);
			taskRepository.updateTaskByField(task.getUid(), new String[]{"taskStatus","startDate","endDate"},
					new Object[]{task.getTaskStatus(),task.getStartDate(),task.getEndDate()},AllCollectionName.ALLFILEINFO_COLLECTIONNAME);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 更新任务
	 * @param task
	 */
	public void taskQuartz() {
		try{
			List<Task> list = taskRepository.getObjectsByRunTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			for(Task task:list){
					taskUpdate(task, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),null, 1);//更新状态为正在执行
					fileService.saveFileToMongo(task);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public List<Task> allTask() {
		try{
			return taskRepository.getAllObjects();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  new ArrayList<Task>();
	}
	
	public Pagination getTaskByStatus(Integer pageNo, Integer pageSize,Integer status) {
		try{
			return taskRepository.getObjectsByStatus(pageNo, pageSize,status);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  new Pagination(pageNo, pageSize, 0);
	}
	
	public Task getTaskByFilePath(String filePath) {
		try{
			return taskRepository.getObjectsByFilePath(filePath);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  null;
	}

	private void setTaskInfo(Task task){
		task.setTableNameAlias(StrUtils.toMD5(task.getTableName()));//根据tableName设置mongodb的collection 别名
		File file  = new File(task.getFilePath());
		task.setFileSize(file.length());
		task.setFileName(file.getName());
	}	
	
    @Resource(name = "task")
    TaskRepository taskRepository;    
    @Resource
	private TxtFileAnalysis fileAnalysis;
	@Autowired
	private FileService fileService;
    @Resource
    MongoToSolrRepository mongoToSolrRepository;    
    @Resource
    SolrTaskRepository solrTaskRepository;
	@Resource
	MongoIntoErrorInfo mongoErrorLog;
	
}
