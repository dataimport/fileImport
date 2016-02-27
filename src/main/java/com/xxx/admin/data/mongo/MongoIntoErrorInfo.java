package com.xxx.admin.data.mongo;

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
import com.xxx.admin.bean.AllCollectionName;
import com.xxx.admin.bean.MongoInToErrorLog;
import com.xxx.core.exception.MongoDBException;
import com.xxx.mongo.repository.base.BaseRepository;

@Repository("mongoErrorLog")
public class MongoIntoErrorInfo implements BaseRepository<MongoInToErrorLog> {
 
	@Autowired
    MongoTemplate mongoTemplate;
 
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
 
    /**
     * Get all Tasks.
     */
    public List<MongoInToErrorLog> getAllObjects() {
        return mongoTemplate.findAll(MongoInToErrorLog.class);
    }
 
    /**
     * Saves a {<span class="referer">@link</span>  Task}.
     */
    public void saveObject(MongoInToErrorLog mel) {
        mongoTemplate.insert(mel);
    }
 
   
    /**
     * Gets a {<span class="referer">@link</span>  Task} for a particular id.
     */
    public MongoInToErrorLog getObjectByTaskId(String uid) {
        return mongoTemplate.findOne(new Query(Criteria.where("taskId").is(uid)),
        		MongoInToErrorLog.class);
    }
 
    /**
     * Updates a {<span class="referer">@link</span>  Task} name for a particular id.
     */
    public WriteResult updateObject(String id, String message) {
        return mongoTemplate.updateFirst(
                new Query(Criteria.where("id").is(id)),
                Update.update("message", message), MongoInToErrorLog.class);
    }
 
    /**
     * Delete a {<span class="referer">@link</span>  Task} for a particular id.
     */
    public boolean deleteObjectById(String id) {
    	 WriteResult result =   mongoTemplate
                .remove(new Query(Criteria.where("id").is(id)), MongoInToErrorLog.class);
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
        if (!mongoTemplate.collectionExists(MongoInToErrorLog.class)) {
            mongoTemplate.createCollection(MongoInToErrorLog.class);
        }
    }
    
    public void createCollection(String name) {
        if (!mongoTemplate.collectionExists(MongoInToErrorLog.class)) {
            mongoTemplate.createCollection(name);
        }
    }
 
    /**
     * Drops the {<span class="referer">@link</span>  Task} collection if the collection does already exists
     */
    public void dropCollection() {
       
    }
    
   public void dropCollection(String collectionName) {
        if(mongoTemplate.collectionExists(collectionName)){
        	mongoTemplate.dropCollection(collectionName);
        }
    }
    	
	@Override
	public boolean deleteObject(MongoInToErrorLog object) {
		WriteResult result = mongoTemplate.remove(object);		
		result.getLastError();
		 if (null != result) {
             if (result.getN() > 0) {
                 return true;
             }
         }
		 return false;
	}


	/**
	* 根据指定的字段还有value 更新所有文件信息表
	* @param uid
	* @param key
	* @param value
	*/
	public void updateFileInfoByField(String id, String[] key, Object[] value) {
		DBCollection dbColleciton =mongoTemplate.getCollection(AllCollectionName.MONGOINTOERRORLOG); 	
		BasicDBObject query = new BasicDBObject();
		query.put("id", id);
		DBObject taskDB = dbColleciton.findOne(query);
		if (taskDB != null){			
			for(int i=0;i<key.length;i++){
				taskDB.put(key[i], value[i]);
			}
			dbColleciton.update(query, taskDB);
		}		
	}

    public List<MongoInToErrorLog> allCollectons() {
       return mongoTemplate.findAll(MongoInToErrorLog.class);
    }

	@Override
	public void saveObject(MongoInToErrorLog object, String collectionName) throws MongoDBException {
		 mongoTemplate.insert(object,collectionName);
		
	}

	@Override
	public MongoInToErrorLog getObjectById(String id) throws MongoDBException {
		Query query = new Query(Criteria.where("id").is(id));
		query.with(new Sort(Direction.DESC,"createTime"));
		return mongoTemplate.findOne(query,MongoInToErrorLog.class);
	}
	
	
	public List<MongoInToErrorLog> getAllObjectByTaskId(String taskId) throws MongoDBException {
		Query query = new Query(Criteria.where("id").is(taskId));
		query.with(new Sort(Direction.DESC,"createTime"));
		return mongoTemplate.find(query,MongoInToErrorLog.class);
	}

}