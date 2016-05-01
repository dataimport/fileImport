package com.xxx.admin.data.mongo;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.xxx.admin.bean.AllCollectionName;
import com.xxx.admin.bean.AllFileInfo;
import com.xxx.admin.bean.Task;
import com.xxx.mongo.repository.base.BaseRepository;
import com.xxx.utils.Pagination;

@Repository("task")
public class TaskRepository implements BaseRepository<Task> {
 
	@Autowired
    MongoTemplate mongoTemplate;
 
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
 
    /**
     * Get all Tasks.
     */
    public List<Task> getAllObjects() {
        return mongoTemplate.findAll(Task.class,AllCollectionName.ALLFILEINFO_COLLECTIONNAME);
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
    
    
    public Pagination getObjectsByStatus(Integer pageNo, Integer pageSize,Integer status) {
    	return getObjectsByStatus(pageNo, pageSize,status,AllCollectionName.ALLFILEINFO_COLLECTIONNAME);
    }

	public Pagination getObjectsByStatus(Integer pageNo, Integer pageSize,Integer status,String collectionName) {
		Query query = new Query();
		if(pageNo==null){
			pageNo =1;
		}
		if(pageSize==null){
			pageSize=20;
		}
		if(status!=null){
			if(status==888){
				//所有状态不是2的任务列表
				//也就是所有运行中的任务状态
				Criteria criteriaStatus = Criteria.where("taskStatus").ne(2);
				query.addCriteria(criteriaStatus);
			}else{
				Criteria criteriaStatus = Criteria.where("taskStatus").is(status);
				query.addCriteria(criteriaStatus);
			}
		}
		long totalCount = this.mongoTemplate.count(query, Task.class,collectionName); 
		Pagination page = new Pagination(pageNo, pageSize, totalCount);  
		query.with(new Sort(Direction.DESC,"runTime"));
		query.skip(page.getFirstResult());  
	    query.limit(pageSize);
		page.setList(mongoTemplate.find(query, Task.class,collectionName));
		return page;
	}
	
	public long getTotalCountByStatus(Integer status) {
		return getTotalCountByStatus(status,AllCollectionName.ALLFILEINFO_COLLECTIONNAME);				
	}
	
	public long getTotalCountByStatus(Integer status,String collectionName) {
		Query query = new Query();
		if(status!=null){
			Criteria criteriaStatus = Criteria.where("taskStatus").is(status);
			query.addCriteria(criteriaStatus);
		}		
		long totalCount = this.mongoTemplate.count(query, Task.class,collectionName);		
		return totalCount;
	}

	public Task getObjectsByFilePath(String filePath) {
		Query query = new Query();
		Criteria criteriaStatus = Criteria.where("filePath").is(filePath);
		query.addCriteria(criteriaStatus);
		query.with(new Sort(Direction.DESC,"runTime"));
		return mongoTemplate.findOne(query, Task.class,AllCollectionName.ALLFILEINFO_COLLECTIONNAME);
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
	
	
	public void taskRun(Task task,List<String> list){		
		DBCollection dbColleciton =mongoTemplate.getCollection(task.getTableName()); 
		DBObject data = new BasicDBObject();
		String[] columns = task.getColumnName();
		Integer[] columnIndex = task.getColumnIndex();
		long start = System.currentTimeMillis();
		//int columnSize = columns.length;
		int columnIndexSize = columnIndex.length;
		if(task.getFirstLineIgnore()){
			list.remove(0);
		}		
		int valuesSize = list.size();

		String[] lineSeparator;
		int nowNum =0;
		String[] keys = new String[]{"runNum","timeUse"};
		String[] values = new String[2];
		String timeUse = "0 秒";
		long l,day,hour,min,s;
		for(int i=0;i<valuesSize;i++){
			//dbColleciton = MongoDBFactory.getDB().getCollection(task.getTableName());
			data = new BasicDBObject(); //处理只能保存一条数据的问题
			lineSeparator = list.get(i).split(task.getSeparator());			
			for(int j=0;j<columnIndexSize;j++){
				data.put(columns[j], lineSeparator[columnIndex[j]-1]);
			}			
			dbColleciton.insert(data);
			nowNum++;			
			if(nowNum==valuesSize||nowNum%10==0){//每10条更新一次任务表进度
				l=System.currentTimeMillis()-start;
				s=(l/1000);
				min=(s/60);
				hour=(s/60);
				day = (hour/24);			
				if(day>0){
					timeUse = ""+day+"天"+hour+"小时"+min+"分"+s+"秒";	
				}else if(hour>0){
					timeUse = ""+hour+"小时"+min+"分"+s+"秒";
				}else if(min>0){
					timeUse = ""+min+"分"+s+"秒";
				}else if(s>0){
					timeUse = s+"秒";
				}else{
					timeUse = l+"毫秒";
				}
				values[0]=String.valueOf(nowNum);
				values[1]=timeUse;		
				updateTaskByField(task.getUid(),keys,values,AllCollectionName.TASKINFO_COLLECTIONNAME);
			}
		}	
	}
	
	public void updateTaskByField(String uid, String[] key, Object[] value,String collctionName) {
		DBCollection dbColleciton =mongoTemplate.getCollection(collctionName); 	
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

	
	/**
	 * 获取所有保存数据的表
	 * @return
	 */
    public CommandResult getDBStats() {
    	try{
    		//DBCollection coll = mongoTemplate.getCollection(AllCollectionName.ALLFILEINFO_COLLECTIONNAME);
    		CommandResult commandResult = 	mongoTemplate.getDb().getStats();
    		return commandResult;      
    		//int totalTableSize = coll.distinct("tableNameAlias").size();        
        }catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return null;        
    }
    
    /**
     * 获取半年内的数据总行数
     * @return
     */
    public Double getFileTotalCount(){  
    	try{
//    		TypedAggregation<AllFileInfo> agg = Aggregation.newAggregation(
//			AllFileInfo.class,
//			Aggregation.group("tableName").sum("totalCount").as("totalCount"),
//			Aggregation.match(Criteria.where("createTime").gte("2012-05-01 00:00:00"))
//			);
//			AggregationResults result = mongoTemplate.aggregate(agg, AllFileInfo.class);
//			List<DBObject> list2= result.getMappedResults();
    		DBCollection coll= mongoTemplate.getCollection(AllCollectionName.ALLFILEINFO_COLLECTIONNAME);
    		DBObject match=new BasicDBObject("$match", new BasicDBObject());
    		DBObject group=new BasicDBObject("$group",new BasicDBObject("_id",null).append("total", new BasicDBObject("$sum","$totalCount")));
    		List<DBObject> list=(List<DBObject>)coll.aggregate(match,group).results();
    		if(list.size()>0){
    			return Double.valueOf(list.get(0).get("total").toString());
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}    	
		return 0D;		      
    }  
    
}