package com.xxx.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxx.admin.manager.TaskManager;

@Service
public class taskRunService {

	public void taskRun() {		
		System.out.println("......执行定时任务.....");
		taskManager.taskQuartz();		
	}
	
	@Autowired
	private TaskManager taskManager;	
}
