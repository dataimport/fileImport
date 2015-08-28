package com.xxx.admin.data.mongo;

import java.text.SimpleDateFormat;
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
import com.mongodb.WriteResult;
import com.xxx.admin.bean.MongoSolrInfo;
import com.xxx.mongo.repository.base.BaseRepository;

@Repository("mongoSolr")
public class MongoToSolrRepository implements BaseRepository<MongoSolrInfo> {
 
	@Autowired
    MongoTemplate mongoTemplate;
 
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
 

    public List<MongoSolrInfo> getAllObjects() {
        return mongoTemplate.findAll(MongoSolrInfo.class);
    }
 

    public void saveObject(MongoSolrInfo FileCollectionInfo) {
        mongoTemplate.insert(FileCollectionInfo);
    }
 
    public void saveObject(MongoSolrInfo FileCollectionInfo,String collectionName) {
        mongoTemplate.insert(FileCollectionInfo,collectionName);
    }
    

    public MongoSolrInfo getObjectByUid(String uid) {
        return mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid)),
        		MongoSolrInfo.class);
    }
 

    public WriteResult updateObject(String id, String name) {
        return mongoTemplate.updateFirst(
                new Query(Criteria.where("id").is(id)),
                Update.update("name", name), MongoSolrInfo.class);
    }
 

    public boolean deleteObjectById(String id) {
    	 WriteResult result =   mongoTemplate
                .remove(new Query(Criteria.where("uid").is(id)), MongoSolrInfo.class);
    	 result.getLastError();
		 if (null != result) {
             if (result.getN() > 0) {
                 return true;
             }
         }
		 return false;
    }
 

    public void createCollection() {
        if (!mongoTemplate.collectionExists(MongoSolrInfo.class)) {
            mongoTemplate.createCollection(MongoSolrInfo.class);
        }
    }
    
    public void createCollection(String name) {
        if (!mongoTemplate.collectionExists(MongoSolrInfo.class)) {
            mongoTemplate.createCollection(name);
        }
    }
 

    public void dropCollection() {
        if (mongoTemplate.collectionExists(MongoSolrInfo.class)) {
            mongoTemplate.dropCollection(MongoSolrInfo.class);
        }
    }
    
    public List<MongoSolrInfo> getObjectsByRunTime(String date){
    	Query query = new Query();
		Criteria criteria = Criteria.where("runTime").lte(date);
		Criteria criteriaStatus = Criteria.where("FileCollectionInfoStatus").is(0);
		query.addCriteria(criteria).addCriteria(criteriaStatus);
		query.with(new Sort(Direction.ASC,"runTime"));
		return mongoTemplate.find(query, MongoSolrInfo.class);
    }

	public List<MongoSolrInfo> getObjectsByStatus(int status) {
		Query query = new Query();
		Criteria criteriaStatus = Criteria.where("FileCollectionInfoStatus").is(status);
		query.addCriteria(criteriaStatus);
		query.with(new Sort(Direction.DESC,"runTime"));
		return mongoTemplate.find(query, MongoSolrInfo.class);
	}

	public MongoSolrInfo getObjectsByFilePath(String filePath) {
		Query query = new Query();
		Criteria criteriaStatus = Criteria.where("filePath").is(filePath);
		query.addCriteria(criteriaStatus);
		query.with(new Sort(Direction.DESC,"runTime"));
		return mongoTemplate.findOne(query, MongoSolrInfo.class);
	}
	
	@Override
	public boolean deleteObject(MongoSolrInfo object) {
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
	public MongoSolrInfo getObjectById(String uid) {
		 return mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid)),
					  MongoSolrInfo.class);
	}
	
}