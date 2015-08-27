package com.xxx.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import com.xxx.admin.service.TaskService;

public class taskRun {

	public void taskRun() {		
		System.out.println("......执行定时任务.....");
		taskService.taskQuartz();		
	}
	
	@Autowired
	private TaskService taskService;
}
