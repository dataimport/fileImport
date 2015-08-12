package com.xxx.admin.data.mongo;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.MongoException;
import com.xxx.admin.bean.Task;


public interface MongoDao {
	public void taskRun(Task task,List<String> list)throws MongoException, UnknownHostException;
	
	public void updateTaskStatus(Task task)throws MongoException, UnknownHostException;
}
