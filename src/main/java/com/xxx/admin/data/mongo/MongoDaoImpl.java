package com.xxx.admin.data.mongo;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.xxx.admin.bean.Task;
import com.xxx.admin.data.base.MongoDBFactory;


public class MongoDaoImpl implements MongoDao {

	Logger logger = Logger.getLogger(MongoDaoImpl.class);

	@Override
	public void taskRun(Task task,List<String> list)
			throws MongoException, UnknownHostException {
		DBCollection dbColleciton = MongoDBFactory.getDB().getCollection(task.getTableName());
		DBObject data = new BasicDBObject();
		String[] columns = task.getColumnName();
		Integer[] columnIndex = task.getColumnIndex();
		
		int columnSize = columns.length;
		int columnIndexSize = columnIndex.length;
		list.remove(0);
		int valuesSize = list.size();

		String[] lineSeparator;
		for(int i=0;i<valuesSize;i++){
			//dbColleciton = MongoDBFactory.getDB().getCollection(task.getTableName());
			data = new BasicDBObject(); //处理只能保存一条数据的问题
			lineSeparator = list.get(i).split(task.getSeparator());
			
			for(int j=0;j<columnIndexSize;j++){
				data.put(columns[j], lineSeparator[columnIndex[j]-1]);
			}
			
			dbColleciton.insert(data);
		}	
	}

	@Override
	public void updateTaskStatus(Task task) throws MongoException, UnknownHostException {
		DBCollection dbColleciton = MongoDBFactory.getDB().getCollection("taskInfo");
		BasicDBObject query = new BasicDBObject();
		query.put("uid", task.getUid());
		DBObject taskDB = dbColleciton.findOne(query);
		if (taskDB != null){
			taskDB.put("taskStatus", task.getTaskStatus());
			taskDB.put("startDate", task.getStartDate());
			taskDB.put("endDate", task.getEndDate());
			taskDB.put("timeUse", task.getTimeUse());
			dbColleciton.update(query, taskDB);
		}
		
	}
}