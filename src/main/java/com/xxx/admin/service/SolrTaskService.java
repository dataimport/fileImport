package com.xxx.admin.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxx.admin.bean.AllCollectionName;
import com.xxx.admin.bean.MongoSolrInfo;
import com.xxx.admin.bean.SolrTask;
import com.xxx.admin.data.mongo.MongoToSolrRepository;
import com.xxx.admin.file.analysis.TxtFileAnalysis;
import com.xxx.elasticsearch.data.mongo.SolrTaskRepository;
import com.xxx.utils.Pagination;

@Service("solrTaskService")
public class SolrTaskService {

	/**
	 * 创建任务
	 * @param task
	 */
	public boolean createTask(SolrTask task,Boolean runNow) {
		try{
			setTaskInfo(task);
			if(!runNow){//不是立即执行，放到任务表中
				solrTaskRepository.saveObject(task,AllCollectionName.TASKINFO_COLLECTIONNAME); //保存到任务表中
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
			solrTaskRepository.saveObject(task,AllCollectionName.ALLFILEINFO_COLLECTIONNAME); 	
		
			//保存一份任务信息到solr入库的任务表中
			solrTaskRepository.saveObject(task, AllCollectionName.SOLR_TASKINFO_COLLECTIONNAME);
			
			//保存到表-文件信息表中			
			MongoSolrInfo msi = new MongoSolrInfo();
			msi.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
			msi.setCollectionName(task.getTableName());
			msi.setFilePath(task.getFilePath());
			msi.setFileInfoUid(task.getUid());
			msi.setTags(task.getTags());
			msi.setStatus(0);
			msi.setOrigin(task.getOrigin());
			msi.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));				  
			mongoToSolrRepository.saveObject(msi);
			
			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		return false;
	
	}

	
	public boolean createTask(SolrTask task) {
		return createTask(task,true);
	}	
	
	
	/**
	 * 更新任务
	 * @param task
	 */
	public void taskUpdate(SolrTask task,String startDate,String endDate,int status) {
		try{
			//更新任务表
			if(StringUtils.isNotBlank(startDate)){
				task.setStartDate(startDate);
			}	
			if(StringUtils.isNotBlank(endDate)){
				task.setEndDate(endDate);
			}			
			task.setTaskStatus(status);						
			solrTaskRepository.updateTaskByField(task.getUid(), new String[]{"taskStatus","startDate","endDate"},
					new Object[]{task.getTaskStatus(),task.getStartDate(),task.getEndDate()});
			
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
			System.out.println("solrTask - quartz");
			List<SolrTask> list = solrTaskRepository.getObjectsByRunTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			for(SolrTask task:list){
				System.out.println("solrTaskService,task list:"+task.getTableName());
				taskUpdate(task, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),null, 1);//更新状态为正在执行
					solrImportService.saveMongoToSolr(task);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public List<SolrTask> allTask() {
		try{
			return solrTaskRepository.getAllObjects();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  new ArrayList<SolrTask>();
	}
	
	public Pagination getTaskByStatus(Integer pageNo, Integer pageSize,Integer status) {
		try{
			return solrTaskRepository.getObjectsByStatus(pageNo, pageSize,status);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  new Pagination(pageNo, pageSize, 0);
	}
	
	public SolrTask getTaskByFilePath(String filePath) {
		try{
			return solrTaskRepository.getObjectsByFilePath(filePath);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  null;
	}

	private void setTaskInfo(SolrTask task){
		File file  = new File(task.getFilePath());
		task.setFileSize(file.length());
		task.setFileName(file.getName());
	}	
	
    @Resource
	private TxtFileAnalysis fileAnalysis;
	@Autowired
	private SolrImportService solrImportService;
    @Resource
    MongoToSolrRepository mongoToSolrRepository;
    
    @Resource
    SolrTaskRepository solrTaskRepository;
	
}
