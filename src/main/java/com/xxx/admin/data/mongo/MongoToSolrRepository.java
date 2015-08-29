package com.xxx.admin.data.mongo;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
	
	/**
	* 根据指定的字段还有value 精确查询符合条件的数据
	* @param uid
	* @param key
	* @param value
	*/
	public List<MongoSolrInfo> getByFields(String[] keys, String[] values) {
		Query query = new Query();
		for(int i=0;i<keys.length;i++){
			if(StringUtils.isNotBlank(values[i])){	
				query.addCriteria(Criteria.where(keys[i]).is(values[i]));				
			}			
		}		  
		query.with(new Sort(Direction.DESC,"createTime"));	
		return mongoTemplate.find(query, MongoSolrInfo.class);
	}
	
	/**
	* 根据指定的字段还有value 精确查询符合条件的数据
	* @param uid
	* @param key
	* @param value
	*/
	public MongoSolrInfo getOneByFields(String[] keys, String[] values) {
		Query query = new Query();
		for(int i=0;i<keys.length;i++){
			if(StringUtils.isNotBlank(values[i])){	
				query.addCriteria(Criteria.where(keys[i]).is(values[i]));				
			}			
		}		  
		query.with(new Sort(Direction.DESC,"createTime"));	
		return mongoTemplate.findOne(query, MongoSolrInfo.class);
	}
	
	
	
	/**
	* 根据指定的字段还有value 查询符合条件的数据
	* @param uid
	* @param key
	* @param value
	*/
	public List<MongoSolrInfo> getByNameOrTagOrOrgin(String keyWord) {
		Query query = new Query();
		Criteria criatira = new Criteria();
		criatira.orOperator(
				Criteria.where("collectionName").regex(Pattern.compile(
				"^.*" + keyWord + ".*$", Pattern.CASE_INSENSITIVE)), 
				Criteria.where("tags").regex(Pattern.compile(
						"^.*" + keyWord + ".*$", Pattern.CASE_INSENSITIVE)),
				Criteria.where("origin").regex(Pattern.compile(
								"^.*" + keyWord + ".*$", Pattern.CASE_INSENSITIVE))
							);			
		query.addCriteria(criatira);
		query.with(new Sort(Direction.DESC,"createTime"));	
		return mongoTemplate.find(query, MongoSolrInfo.class);		
	}

}