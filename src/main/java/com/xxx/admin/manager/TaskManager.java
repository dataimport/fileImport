package com.xxx.admin.manager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxx.admin.bean.Task;
import com.xxx.admin.data.TxtData;
import com.xxx.admin.data.mongo.MongoDao;
import com.xxx.admin.data.mongo.Repository;

@Service
public class TaskManager {

	/**
	 * 创建任务
	 * @param task
	 */
	public List<String> createTask(Task task) {
		try{
			List<String> lines = setTaskInfo(task);
			repository.saveObject(task); //保存到任务表中
			repository.saveObject(task,"documentInfo"); //保存到所有文档表中			
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
			mongoDao.taskRun(task,lines);//开始执行导入文件任务		
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
//			if(StringUtils.isNotBlank(task.getStartDate())&&StringUtils.isNotBlank(task.getEndDate())){
//				String timeUse = "0 秒";
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				Date end = df.parse(task.getStartDate());
//				Date start=df.parse(task.getEndDate());
//				long l=end.getTime()-start.getTime();
//				long day=l/(24*60*60*1000);
//				long hour=(l/(60*60*1000)-day*24);
//				long min=((l/(60*1000))-day*24*60-hour*60);
//				long s=(l/1000-day*24*60*60-hour*60*60-min*60);
//				if(day>0){
//					timeUse = ""+day+"天"+hour+"小时"+min+"分"+s+"秒";	
//				}else if(hour>0){
//					timeUse = ""+hour+"小时"+min+"分"+s+"秒";
//				}else if(min>0){
//					timeUse = ""+min+"分"+s+"秒";
//				}
//				System.out.println(timeUse);
//				task.setTimeUse(timeUse);
//			}
			task.setTaskStatus(status);
			mongoDao.updateTaskStatus(task);
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
			List<Task> list = repository.getObjectsByRunTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
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
			return repository.getAllObjects();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  new ArrayList<Task>();
	}
	
	public List<Task> getTaskByStatus(int status) {
		try{
			return repository.getObjectsByStatus(status);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  new ArrayList<Task>();
	}
	
	public List<Task> getTaskByFilePath(String filePath) {
		try{
			return repository.getObjectsByFilePath(filePath);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return  new ArrayList<Task>();
	}

	private List<String> setTaskInfo(Task task){
		File file  = new File(task.getFilePath());
		task.setFileSize(file.length());
		List<String> lines = (List<String>)txtData.parse(task.getFilePath());
		task.setTotalCount(lines.size()-1);	
		return lines;
	}
	
	private List<String> getLines(String filePath){
		List<String> lines = (List<String>)txtData.parse(filePath);
		return lines;
	}
	
	
	@Autowired
	private  MongoDao mongoDao;
	@Autowired
	private  Repository repository;
	@Autowired
	private TxtData txtData;
	
}
