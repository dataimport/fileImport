package com.xxx.admin.data.mongo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.xxx.admin.bean.NoRepeatColls;
import com.xxx.mongo.repository.base.BaseRepository;

@Repository("mongoColl")
public class MongoCollRepository implements BaseRepository<NoRepeatColls> {
 
	@Autowired
    MongoTemplate mongoTemplate;
 
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
 
    /**
     * Get all Tasks.
     */
    public List<NoRepeatColls> getAllObjects() {
        return mongoTemplate.findAll(NoRepeatColls.class);
    }
 
    /**
     * Saves a {<span class="referer">@link</span>  Task}.
     */
    public void saveObject(NoRepeatColls nrc) {
        mongoTemplate.insert(nrc);
    }
 
    public void saveObject(NoRepeatColls nrc,String collectionName) {
        mongoTemplate.insert(nrc,collectionName);
    }
    
    /**
     * Gets a {<span class="referer">@link</span>  Task} for a particular id.
     */
    public NoRepeatColls getObjectByUid(String uid) {
        return mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid)),
        		NoRepeatColls.class);
    }
 
    /**
     * Updates a {<span class="referer">@link</span>  Task} name for a particular id.
     */
    public WriteResult updateObject(String id, String name) {
        return mongoTemplate.updateFirst(
                new Query(Criteria.where("id").is(id)),
                Update.update("name", name), NoRepeatColls.class);
    }
 
    /**
     * Delete a {<span class="referer">@link</span>  Task} for a particular id.
     */
    public boolean deleteObjectById(String id) {
    	 WriteResult result =   mongoTemplate
                .remove(new Query(Criteria.where("uid").is(id)), NoRepeatColls.class);
    	 result.getLastError();
		 if (null != result) {
             if (result.getN() > 0) {
                 return true;
             }
         }
		 return false;
    }
    
    public boolean deleteObjectByNanmeAlias(String name) {
   	 WriteResult result =   mongoTemplate
               .remove(new Query(Criteria.where("nameAlias").is(name)), NoRepeatColls.class);
   	 result.getLastError();
		 if (null != result) {
            if (result.getN() > 0) {
                return true;
            }
        }
		 return false;
   }
 
	public NoRepeatColls getObjectsByName(String name) {
		Query query = new Query();
		Criteria criteriaStatus = Criteria.where("name").is(name);
		query.addCriteria(criteriaStatus);
		return mongoTemplate.findOne(query, NoRepeatColls.class);
	}
	
    /**
     * Create a {<span class="referer">@link</span>  Task} collection if the collection does not already
     * exists
     */
    public void createCollection() {
        if (!mongoTemplate.collectionExists(NoRepeatColls.class)) {
            mongoTemplate.createCollection(NoRepeatColls.class);
        }
    }
    
    public void createCollection(String name) {
        if (!mongoTemplate.collectionExists(NoRepeatColls.class)) {
            mongoTemplate.createCollection(name);
        }
    }
 
    /**
     * Drops the {<span class="referer">@link</span>  Task} collection if the collection does already exists
     */
    public void dropCollection() {
       
    }
    
    public boolean collectionExists(String collectionName) {
        return mongoTemplate.collectionExists(collectionName);
    }
    
    public void dropCollection(String collectionName) {
        if(mongoTemplate.collectionExists(collectionName)){
        	mongoTemplate.dropCollection(collectionName);
        }
    }
    	
	@Override
	public boolean deleteObject(NoRepeatColls object) {
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
	public NoRepeatColls getObjectById(String uid) {
		 return mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid)),
				 NoRepeatColls.class);
	}
		
	/**
	* 根据指定的字段还有value 更新所有文件信息表
	* @param uid
	* @param key
	* @param value
	*/
	public void updateFileInfoByField(String uid, String[] key, Object[] value) {
		DBCollection dbColleciton =mongoTemplate.getCollection(AllCollectionName.ALLFILEINFO_COLLECTIONNAME); 	
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

    public List<NoRepeatColls> allCollectons() {
       return mongoTemplate.findAll(NoRepeatColls.class);
    }

}