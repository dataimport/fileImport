package com.xxx.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.xxx.admin.service.SolrTaskService;
import com.xxx.admin.service.TaskService;

public class taskRun {

	public void run() {		
		System.out.println("......执行定时任务.....");
		taskService.taskQuartz();
		
		//solr任务
		solrTaskService.taskQuartz();
		
	}
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SolrTaskService solrTaskService;
}
