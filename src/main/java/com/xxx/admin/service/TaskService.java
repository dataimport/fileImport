package com.xxx.admin.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.xxx.admin.bean.Task;
import com.xxx.admin.data.mongo.TaskRepository;
import com.xxx.admin.file.analysis.FileAnalysis;

@Service("taskService")
public class TaskService {

	/**
	 * 创建任务
	 * @param task
	 */
	public List<String> createTask(Task task) {
		try{
			List<String> lines = setTaskInfo(task);
			taskRepository.saveObject(task); //保存到任务表中
			taskRepository.saveObject(task,"documentInfo"); //保存到所有文档表中			
			return lines;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 执行任务
	 * @param task
	 */
	public boolean taskRun(Task task,List<String> lines) {
		try{					
			taskRepository.taskRun(task,lines);//开始执行导入文件任务		
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
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
				if(taskRun(task, getLines(task.getFilePath()))){//入库
					taskUpdate(task, null,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), 2);//更新状态为已经完成
				}else{
					taskUpdate(task, null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),-2);//更新状态为失败
				}
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

	private List<String> setTaskInfo(Task task){
		File file  = new File(task.getFilePath());
		task.setFileSize(file.length());
		List<String> lines = (List<String>)fileAnalysis.parse(task.getFilePath());
		task.setTotalCount(lines.size()-1);	
		return lines;
	}
	
	private List<String> getLines(String filePath){
		List<String> lines = (List<String>)fileAnalysis.parse(filePath);
		return lines;
	}
	
    @Resource(name = "task")
    TaskRepository taskRepository;
    @Resource
	private FileAnalysis fileAnalysis;
	
}
