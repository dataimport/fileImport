package com.xxx.admin.data.mongo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
 
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.xxx.admin.bean.Collection;
import com.xxx.admin.bean.Task;
import com.xxx.mongo.repository.base.BaseRepository;

@Repository("fileMogo")
public class FileInMongoRepository implements BaseRepository<Task> {
 
	@Autowired
    MongoTemplate mongoTemplate;
 
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
 
    /**
     * Get all Tasks.
     */
    public List<Task> getAllObjects() {
        return mongoTemplate.findAll(Task.class);
    }
 
    /**
     * Saves a {<span class="referer">@link</span>  Task}.
     */
    public void saveObject(Task task) {
        mongoTemplate.insert(task);
    }
 
    public void saveObject(Task task,String collectionName) {
        mongoTemplate.insert(task,collectionName);
    }
    
    /**
     * Gets a {<span class="referer">@link</span>  Task} for a particular id.
     */
    public Task getObjectByUid(String uid) {
        return mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid)),
        		Task.class);
    }
 
    /**
     * Updates a {<span class="referer">@link</span>  Task} name for a particular id.
     */
    public WriteResult updateObject(String id, String name) {
        return mongoTemplate.updateFirst(
                new Query(Criteria.where("id").is(id)),
                Update.update("name", name), Task.class);
    }
 
    /**
     * Delete a {<span class="referer">@link</span>  Task} for a particular id.
     */
    public boolean deleteObjectById(String id) {
    	 WriteResult result =   mongoTemplate
                .remove(new Query(Criteria.where("uid").is(id)), Task.class);
    	 result.getLastError();
		 if (null != result) {
             if (result.getN() > 0) {
                 return true;
             }
         }
		 return false;
    }
 
    /**
     * Create a {<span class="referer">@link</span>  Task} collection if the collection does not already
     * exists
     */
    public void createCollection() {
        if (!mongoTemplate.collectionExists(Task.class)) {
            mongoTemplate.createCollection(Task.class);
        }
    }
    
    public void createCollection(String name) {
        if (!mongoTemplate.collectionExists(Task.class)) {
            mongoTemplate.createCollection(name);
        }
    }
 
    /**
     * Drops the {<span class="referer">@link</span>  Task} collection if the collection does already exists
     */
    public void dropCollection() {
        if (mongoTemplate.collectionExists(Task.class)) {
            mongoTemplate.dropCollection(Task.class);
        }
    }
    
    public List<Task> getObjectsByRunTime(String date){
    	Query query = new Query();
		Criteria criteria = Criteria.where("runTime").lte(date);
		Criteria criteriaStatus = Criteria.where("taskStatus").is(0);
		query.addCriteria(criteria).addCriteria(criteriaStatus);
		query.with(new Sort(Direction.ASC,"runTime"));
		return mongoTemplate.find(query, Task.class);
    }

	public List<Task> getObjectsByStatus(int status) {
		Query query = new Query();
		Criteria criteriaStatus = Criteria.where("taskStatus").is(status);
		query.addCriteria(criteriaStatus);
		query.with(new Sort(Direction.DESC,"runTime"));
		return mongoTemplate.find(query, Task.class);
	}

	public Task getObjectsByFilePath(String filePath) {
		Query query = new Query();
		Criteria criteriaStatus = Criteria.where("filePath").is(filePath);
		query.addCriteria(criteriaStatus);
		query.with(new Sort(Direction.DESC,"runTime"));
		return mongoTemplate.findOne(query, Task.class);
	}
	
	@Override
	public boolean deleteObject(Task object) {
		WriteResult result = mongoTemplate.remove(object);		
		result.getLastError();
		 if (null != result) {
             if (result.getN() > 0) {
                 return true;
             }
         }
		 return false;
	}

	@Override
	public Task getObjectById(String uid) {
		 return mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid)),
					  Task.class);
	}
	
	
	public void FilePushToMongo(Task task,List<String> list){	
		FilePushToMongo(task,list,null,null,null);
	}
	
	public void FilePushToMongo(Task task,List<String> list,Boolean isBigFile,Integer runNum,Long time){	
		long start = System.currentTimeMillis();
		int valuesSize = list.size();		
		DBCollection dbColleciton =mongoTemplate.getCollection(task.getTableName()); 
		DBObject data = new BasicDBObject();
		String[] columns = task.getColumnName();
		Integer[] columnIndex = task.getColumnIndex();
		int columnIndexSize = columnIndex.length;
		if(task.isFirstLineIgnore()){
			list.remove(0);
		}		
		String[] lineSeparator;
		int nowNum = 0;
		String[] keys = new String[]{"runNum","timeUse"};
		Object[] values = new Object[2];
		String timeUse = "0 秒";
		long l=0l;
		for(int i=0;i<valuesSize;i++){
			data = new BasicDBObject(); 
			lineSeparator = list.get(i).split(task.getSeparator());			
			for(int j=0;j<columnIndexSize;j++){
				data.put(columns[j], lineSeparator[columnIndex[j]-1]);
			}			
			dbColleciton.insert(data);		
			if(!isBigFile){//不是大文件 按行数更新
				nowNum++;			
				if(nowNum==valuesSize||nowNum%10==0){//每10条更新一次任务表进度
					l=System.currentTimeMillis()-start;
					timeUse = getTimeUse(l);
					values[0]=String.valueOf(nowNum);
					values[1]=timeUse;					
					if(nowNum==valuesSize){
						 keys = new String[]{"runNum","timeUse","endDate","taskStatus","totalCount"};
						 values = new Object[5];
						 values[0]=nowNum;
						 values[1]=timeUse;	
						 values[2]=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						 values[3]=2;
						 values[4]=valuesSize;
					}
					updateFileInfoByField(task.getUid(),keys,values);
				}				
			}	
		}	
		
		if(isBigFile){//大文件 按buffer更新
			timeUse = getTimeUse(time);		
			values[0]=runNum;
			values[1]=timeUse;	
			updateFileInfoByField(task.getUid(),keys,values);
		}		
	}
	
	private String getTimeUse(long time){		
		if(time/86400000>0){
			return ""+time/86400000+"天"+time%86400000/3600000+"小时"+time%86400000%3600000/60000+"分"+time%86400000%3600000%60000/1000+"秒";	
		}else if(time%86400000/3600000>0){
			return  ""+time%86400000/3600000+"小时"+time%86400000%3600000/60000+"分"+time%86400000%3600000%60000/1000+"秒";
		}else if(time%86400000%3600000/60000>0){
			return  ""+time%86400000%3600000/60000+"分"+time%86400000%3600000%60000/1000+"秒";
		}else if(time%86400000%3600000%60000/1000>0){
			return   time%86400000%3600000%60000/1000+"秒";
		}else{
			return  time+"毫秒";
		}			
	}
	
	/**
	* 根据指定的字段还有value 更新所有文件信息表
	* @param uid
	* @param key
	* @param value
	*/
	public void updateFileInfoByField(String uid, String[] key, Object[] value) {
		DBCollection dbColleciton =mongoTemplate.getCollection(Collection.ALLFILEINFO_COLLECTION_NAME); 	
		BasicDBObject query = new BasicDBObject();
		query.put("uid", uid);
		DBObject taskDB = dbColleciton.findOne(query);
		if (taskDB != null){			
			for(int i=0;i<key.length;i++){
				taskDB.put(key[i], value[i]);
			}
			dbColleciton.update(query, taskDB);
		}		
	}

}