package com.xxx.admin.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xxx.admin.bean.Task;
import com.xxx.admin.service.FileService;
import com.xxx.admin.service.TaskService;
import com.xxx.utils.ResponseUtils;

@Controller
@RequestMapping("/task")
public class TaskAct {
	

	@RequestMapping(value = "list.htm")
	public String getAllTask(ModelMap model,Integer status,HttpServletRequest request,HttpServletResponse response) {
		List<Task> list = null;
		if(status!=null){
		   list = taskService.getTaskByStatus(status);
		}else{
		   list = taskService.allTask();
		}		
		model.put("list", list);
		return "task/list";
	}
	
	@RequestMapping(value = "v_task.htm")
	public String v_task(String filePath,String separator,boolean firstLineIgnore,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		Map map =  fileService.view(filePath, separator);
		model.put("text", (List<String>) map.get("text"));
		model.put("columns", (String[])map.get("columns"));
		model.put("separator", separator);
		model.put("filePath", filePath);
		if(firstLineIgnore){
			model.put("firstLineIgnore", "true");
		}else{
			model.put("firstLineIgnore", "false");
		}
		
		return "file/task_view";
	}
	

	@RequestMapping(value = "o_task.htm")
	public String o_task(Task task,ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		task.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
		//taskService.createTask(task);
		if(task.getRunTime().equals("0000-00-00 00:00:00"))//立即执行
		{	
			task.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			task.setRunTime(task.getStartDate());
			task.setTaskStatus(1);//执行中
			List<String> lines  = taskService.createTask(task);//创建任务
			if(lines!=null){
				taskService.taskRun(task,lines);//执行入库
				taskService.taskUpdate(task, null,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), 2);//执行完毕 ，更新task
				ResponseUtils.renderJson(response, "{\"msg\":\"创建任务,并且入库成功\"}");
			}else{
				taskService.taskUpdate(task, null,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),-2);
				ResponseUtils.renderJson(response, "{\"msg\":\"创建任务,入库失败\"}");
			}			
		}else{//创建任务
			task.setTaskStatus(0);
			taskService.createTask(task);//创建任务
			ResponseUtils.renderJson(response, "{\"msg\":\"创建任务成功\"}");
		}		
		return null;
	}

	@Autowired
	private FileService fileService;
	@Autowired
	private TaskService taskService;
}
