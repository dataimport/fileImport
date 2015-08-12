package com.xxx.action;

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
import com.xxx.admin.manager.FileManager;
import com.xxx.admin.manager.TaskManager;
import com.xxx.utils.ResponseUtils;

@Controller
@RequestMapping("/task")
public class TaskAct {
	@RequestMapping(value = "list.htm")
	public String getAllTask(ModelMap model,Integer status,HttpServletRequest request,HttpServletResponse response) {
		List<Task> list = null;
		if(status!=null){
		   list = taskManager.getTaskByStatus(status);
		}else{
		   list = taskManager.allTask();
		}		
		model.put("list", list);
		return "task/list";
	}
	
	@RequestMapping(value = "v_task.htm")
	public String v_task(String filePath,String separator,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		Map map =  fileManager.taskView(filePath, separator);
		model.put("text", (List<String>) map.get("text"));
		model.put("columns", (String[])map.get("columns"));
		model.put("separator", separator);
		model.put("filePath", filePath);
		return "file/task_view";
	}
	
	@RequestMapping(value = "o_task.htm")
	public String o_task(Task task,ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		task.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
		//taskManager.createTask(task);
		if(task.getRunTime().equals("0000-00-00 00:00:00"))//立即执行
		{	
			task.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			task.setRunTime(task.getStartDate());
			task.setTaskStatus(1);//执行中
			List<String> lines  = taskManager.createTask(task);//创建任务
			if(lines!=null){
				taskManager.taskRun(task,lines);//执行入库
				taskManager.taskUpdate(task, null,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), 2);//执行完毕 ，更新task
				ResponseUtils.renderJson(response, "{\"msg\":\"创建任务,并且入库成功\"}");
			}else{
				taskManager.taskUpdate(task, null,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),-2);
				ResponseUtils.renderJson(response, "{\"msg\":\"创建任务,入库失败\"}");
			}			
		}else{//创建任务
			task.setTaskStatus(0);
			taskManager.createTask(task);//创建任务
			ResponseUtils.renderJson(response, "{\"msg\":\"创建任务成功\"}");
		}		
		return null;
	}

	@Autowired
	private FileManager fileManager;
	@Autowired
	private TaskManager taskManager;
}
