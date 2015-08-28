package com.xxx.admin.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxx.admin.bean.AllCollectionName;
import com.xxx.admin.bean.Task;
import com.xxx.admin.data.mongo.TaskRepository;
import com.xxx.admin.file.analysis.TxtFileAnalysis;

@Service("taskService")
public class TaskService {

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
			taskRepository.saveObject(task,AllCollectionName.ALLFILEINFO_COLLECTIONNAME); //保存到所有文档表中			
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
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
	
	public List<Task> getTaskByStatus(int status) {
		try{
			return taskRepository.getObjectsByStatus(status);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  new ArrayList<Task>();
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
	
}
