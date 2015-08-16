package com.xxx.admin.data.mongo;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.xxx.admin.bean.Task;
import com.xxx.admin.data.base.MongoDBFactory;


public class MongoDaoImpl implements MongoDao {

	Logger logger = Logger.getLogger(MongoDaoImpl.class);

	@Override
	public void taskRun(Task task,List<String> list)
			throws MongoException, UnknownHostException {
		DBCollection dbColleciton =getDBCollcetion(task.getTableName()); 
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
		DBCollection dbColleciton =getDBCollcetion("taskInfo"); 
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

	@Override
	public boolean addFilePath(String path){
		try{
			DBCollection dbColleciton = getDBCollcetion("filePathInfo"); 
			DBObject saveData = new BasicDBObject();
			saveData.put("path", path);
			saveData.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			dbColleciton.insert(saveData);	
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
			return true;
	}

	@Override
	public boolean deleteFilePath(String path){		
		try{
			DBCollection dbColleciton =getDBCollcetion("filePathInfo"); 
			BasicDBObject del = new BasicDBObject();
			del.put("path", path);
			dbColleciton.remove(del);	
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<String> viewFilePath(){
		List list = new ArrayList<String>();
		try{
			DBCollection dbColleciton =getDBCollcetion("filePathInfo"); 
			BasicDBObject query = new BasicDBObject();
			DBCursor dbCursor = dbColleciton.find(query).sort(new BasicDBObject("createTime", -1));
			Iterator<DBObject> iterator = dbCursor.iterator();
			DBObject dbObject = null;			
			while (iterator.hasNext()) {
				dbObject = iterator.next();
				list.add((String)dbObject.get("path"));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return list;
	}
	
	
	private DBCollection getDBCollcetion(String collectionName){
		return MongoDBFactory.getDB().getCollection("filePathInfo");
	}
	
}