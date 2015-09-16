package com.xxx.admin.data.mongo;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;
import com.xxx.admin.bean.Folder;
import com.xxx.mongo.repository.base.BaseRepository;

@Repository("folder")
public class FolderRepository implements BaseRepository<Folder> {
 
	@Autowired
    MongoTemplate mongoTemplate;
 
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
 
    
    @Override
    public List<Folder> getAllObjects() {
        return mongoTemplate.findAll(Folder.class);
    }
 
    @Override
    public void saveObject(Folder folder) {
        mongoTemplate.insert(folder);
    }
 
    @Override
    public void saveObject(Folder folder,String collectionName) {
        mongoTemplate.insert(folder,collectionName);
    }
    

	@Override
    public boolean deleteObjectById(String id) {
    	 WriteResult result =  mongoTemplate
                .remove(new Query(Criteria.where("folderId").is(id)), Folder.class);	
		result.getLastError();
		 if (null != result) {
             if (result.getN() > 0) {
                 return true;
             }
         }
		 return false;
    }
 
	@Override
    public void createCollection() {
        if (!mongoTemplate.collectionExists(Folder.class)) {
            mongoTemplate.createCollection(Folder.class);
        }
    }
	@Override
    public void createCollection(String name) {
        if (!mongoTemplate.collectionExists(Folder.class)) {
            mongoTemplate.createCollection(name);
        }
    }
 
	@Override
    public void dropCollection() {
        if (mongoTemplate.collectionExists(Folder.class)) {
            mongoTemplate.dropCollection(Folder.class);
        }
    }

	@Override
	public boolean deleteObject(Folder folder) {
		WriteResult result = mongoTemplate.remove(folder);		
		result.getLastError();
		 if (null != result) {
             if (result.getN() > 0) {
                 return true;
             }
         }
		 return false;
	}

	@Override
	public Folder getObjectById(String uid) {
		  return mongoTemplate.findOne(new Query(Criteria.where("folderId").is(uid)),
				  Folder.class);
	}


	public List<Folder> getObjectByFields(String[] fields,String[] values) {
		Query query = new Query();
		for(int i=0;i<fields.length;i++){
			query.addCriteria(Criteria.where(fields[i]).is(values[i]));
		}
		List<Folder> list =   mongoTemplate.find(query, Folder.class);
    	return list;
	}
}