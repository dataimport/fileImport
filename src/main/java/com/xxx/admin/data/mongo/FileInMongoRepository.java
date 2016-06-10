package com.xxx.admin.data.mongo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.xxx.admin.bean.AllCollectionName;
import com.xxx.admin.bean.AllFileInfo;
import com.xxx.admin.bean.MongoSolrInfo;
import com.xxx.admin.bean.Task;
import com.xxx.mongo.repository.base.BaseRepository;
import com.xxx.utils.Pagination;
import com.xxx.utils.StrUtils;

@Repository("fileMogo")
public class FileInMongoRepository implements BaseRepository<Task> {
 
	private static final Logger log = LoggerFactory.getLogger(FileInMongoRepository.class);
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
 
    public boolean deleteObjectByIdFromAllFileInfo(String id) {
   	 WriteResult result =   mongoTemplate
               .remove(new Query(Criteria.where("uid").is(id)), AllFileInfo.class);
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
       
    }
    
    public boolean collectionExists(String collectionName) {
        return mongoTemplate.collectionExists(collectionName);
    }
    
    public void dropCollection(String collectionName) {
        if(mongoTemplate.collectionExists(collectionName)){
        	mongoTemplate.dropCollection(collectionName);
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
	
	
	public int FilePushToMongo(Task task,List<String> list,int runNum,int totalSuccessNum){	
		return FilePushToMongo(task,list,false,runNum,totalSuccessNum,null);
	}
	
	public int FilePushToMongo(Task task,List<String> list,Boolean isBigFile,int runNum,int totalSuccessNum,Long time){	
		long start = System.currentTimeMillis();
		if(task.getFirstLineIgnore()){
			list.remove(0);
		}		
		int valuesSize = list.size();		
		DBCollection dbColleciton =mongoTemplate.getCollection(task.getTableNameAlias()); 
		DBObject data = new BasicDBObject();
		String[] columns = task.getColumnName();
		Integer[] columnIndex = task.getColumnIndex();
		int columnIndexSize = columnIndex.length;
		
		String[] lineSeparator;
		int successNum=0;
		String[] keys = new String[]{"runNum","timeUse"};
		Object[] values = new Object[2];
		String timeUse = "0 秒";
		long l=0l;
		boolean failCollectionRemoveOld = true;
		String separator = task.getSeparator();
		if(StringUtils.isBlank(separator)){
			separator="\\s+"; //代表多个空格			
		}else{
			if(!"\\s+".equals(separator)){				
				separator = StrUtils.separatorCheck(separator);//特殊字符特换
			}			
		}
		
		
		for(int i=0;i<valuesSize;i++){
			runNum++;
			try{//加上异常处理，这样个别数据有问题，不会影响整体数据的导入
				data = new BasicDBObject(); 
				
				lineSeparator = list.get(i).trim().split(separator,-1);	
				
				if(lineSeparator.length>=columnIndexSize){//处理虽然有换行但是没有数据的情况，或者数据分割后，总数跟填写的字段数不匹配。 --//如果当前行的列数与设置的列名数一致 则导入
					//if(lineSeparator.length>=columnIndexSize){//处理虽然有换行但是没有数据的情况，或者数据分割后，总数跟填写的字段数不匹配。
						for(int j=0;j<columnIndexSize;j++){
							if(lineSeparator.length>=columnIndex[j]-1){
								data.put(columns[j], lineSeparator[columnIndex[j]-1]);
							}else{												
									saveFailData(task,runNum, list.get(i),failCollectionRemoveOld);//没导入，记录到失败记录表里
									failCollectionRemoveOld = false;
								continue;
							}							
						}			
						
						dbColleciton.insert(data);	
						successNum++;
						if(isBigFile!=null&&!isBigFile){//不是大文件 按行数更新		
							//System.out.println("successNum: "+successNum +" valuesSize :"+valuesSize +" i: "+i );
							if(i==valuesSize||successNum==valuesSize||successNum%10==0){//每10条更新一次任务表进度
								l=System.currentTimeMillis()-start;
								timeUse = getTimeUse(l);
								values[0]=String.valueOf(successNum);
								values[1]=timeUse;					
								//if(i==valuesSize||successNum==valuesSize){
									 keys = new String[]{"runNum","timeUse","endDate","taskStatus"};
									 values = new Object[5];
									 values[0]=successNum;
									 values[1]=timeUse;	
									 values[2]=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
									 values[3]=2;
									 //values[4]=valuesSize;
								//}
								updateFileInfoByField(task.getUid(),keys,values);
								updateFileInfoByField(task.getUid(),keys,values,AllCollectionName.TASKINFO_COLLECTIONNAME);
							}				
						}	
						
					//}			
				}else{
					//System.out.println(" run " +runNum +" ### "+  list.get(i).trim()	);
					saveFailData(task,runNum, list.get(i),failCollectionRemoveOld);//没导入，记录到失败记录表里
					failCollectionRemoveOld = false;
				}
				
			}catch(Exception ex){
				ex.printStackTrace();
				runNum++;//错误记录也加入到导入行中，否则算百分比的时候不正确。
				saveFailData(task,runNum, list.get(i),failCollectionRemoveOld);
				failCollectionRemoveOld = false;
			}			
		}	
		
		if(isBigFile){//大文件 按buffer更新
			timeUse = getTimeUse(time);		
			values[0]=totalSuccessNum+successNum;
			values[1]=timeUse;	
			updateFileInfoByField(task.getUid(),keys,values);
			updateFileInfoByField(task.getUid(),keys,values,AllCollectionName.TASKINFO_COLLECTIONNAME);
		}	
		
		return successNum;
	}
	
	
	/**
	 * 把excel文件导入到mongodb中
	 * @param task
	 * @param list
	 * @param isBigFile
	 * @param runNum
	 * @param time
	 * @return
	 */
	public int excelFilePushToMongo(Task task,List<String[]> list){	
		long start = System.currentTimeMillis();
		if(task.getFirstLineIgnore()){
			list.remove(0);
		}		
		int valuesSize = list.size();		
		DBCollection dbColleciton =mongoTemplate.getCollection(task.getTableNameAlias()); 
		DBObject data = new BasicDBObject();
		String[] columns = task.getColumnName();
		Integer[] columnIndex = task.getColumnIndex();
		int columnIndexSize = columnIndex.length;
		
		int nowNum = 0,successNum=0,runNum=0;
		String[] keys = new String[]{"runNum","timeUse"};
		Object[] values = new Object[2];
		String timeUse = "0 秒";
		long l=0l;
		int excelRowCellNum = 0;
		boolean failCollectionRemoveOld = true;
		for(int i=0;i<valuesSize;i++){
			try{//加上异常处理，这样个别数据有问题，不会影响整体数据的导入
				data = new BasicDBObject(); 
				for(int j=0;j<columnIndexSize;j++){					
					excelRowCellNum = list.get(i).length;
					//log.debug(" ## " +i+" #excelRowCellNum#  "+excelRowCellNum+" #columnIndex[j]-1# "+(columnIndex[j]-1));
					if((columnIndex[j]-1)>=excelRowCellNum){//表格的列数与前台设置的不一致，数据设置成空
						data.put(columns[j], "");
					}else{
						data.put(columns[j], list.get(i)[columnIndex[j]-1]);
					}				
				}			
				dbColleciton.insert(data);		
				nowNum++;			
				if(nowNum==valuesSize||nowNum%10==0){//每10条更新一次任务表进度
					l=System.currentTimeMillis()-start;
					timeUse = getTimeUse(l);
					values[0]=String.valueOf(nowNum);
					values[1]=timeUse;					
					if(nowNum==valuesSize){;
						keys = new String[]{"runNum","timeUse","endDate","taskStatus"};
						values = new Object[5];
						values[0]=nowNum;
						values[1]=timeUse;	
						values[2]=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						values[3]=2;
						 //values[4]=valuesSize;
					}
						updateFileInfoByField(task.getUid(),keys,values);
				 }				
				successNum++;
			}catch(Exception ex){
				ex.printStackTrace();				
				saveFailData(task,runNum+i,"",failCollectionRemoveOld);
				failCollectionRemoveOld = false;
			}			
		}	
		
//		if(isBigFile){//大文件 按buffer更新
//			timeUse = getTimeUse(time);		
//			values[0]=runNum;
//			values[1]=timeUse;	
//			updateFileInfoByField(task.getUid(),keys,values);
//		}	
		
		return successNum;
	}
	
	/**
	 * 保存失败的记录
	 * @param task
	 */
	private void saveFailData(Task task,int failNum,String value,boolean removeOld){
		DBCollection dbColleciton =mongoTemplate.getCollection(task.getTableNameAlias()+"_falie"); 
		if(removeOld){
			dbColleciton.remove(new BasicDBObject());	
		}		
		DBObject data = new BasicDBObject();
		data.put("num", failNum);
		data.put("value", value);
		dbColleciton.insert(data);
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
		updateFileInfoByField(uid,key,value,AllCollectionName.ALLFILEINFO_COLLECTIONNAME);	
	}

	public void updateFileInfoByField(String uid, String[] key, Object[] value,String collectionName) {
		DBCollection dbColleciton =mongoTemplate.getCollection(collectionName); 	
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
	
	public Pagination getObjectsByCollectionName(Integer pageNo, Integer pageSize,String collectionName) {
		if(pageNo==null){
			pageNo =1;
		}
		if(pageSize==null){
			pageSize=20;
		}		
		DBCollection dbColleciton =mongoTemplate.getCollection(collectionName); 	
		long totalCount = dbColleciton.count();
		Pagination page = new Pagination(pageNo, pageSize, totalCount); 		
	    DBCursor cursor = dbColleciton.find().skip(page.getFirstResult()).limit(pageSize);
	    Iterator<DBObject> it = cursor.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {	
			list.add(it.next().toString());
		}
		page.setList(list);
		return page;
	}
	
	public Map getTableByCataLog(String catalog) {
		try{			
			 String reduce = "function(doc, catalog){" + "catalog.count += 1;" + "        }";
			 BasicDBObject queryObject = new BasicDBObject();
			 
		     DBObject result = mongoTemplate.getCollection(AllCollectionName.ALLFILEINFO_COLLECTIONNAME).group(new BasicDBObject("catalog", 1), queryObject,
		                new BasicDBObject("count", 0), reduce);//对mongo数据库进行分组统计查询
		      
		     return result.toMap(); 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
        return null;
	}
	
	public Pagination getTaskByCataLog(Integer pageNo, Integer pageSize,String catalog) {
		if(pageNo==null){
			pageNo =1;
		}
		if(pageSize==null){
			pageSize=20;
		}		

		try{
			Query query = new Query();
			Criteria criteria = Criteria.where("catalog").is(catalog);
			query.addCriteria(criteria);
			long totalCount  = mongoTemplate.count(query,AllFileInfo.class);		
			query.with(new Sort(Direction.ASC,"startDate"));
			Pagination page = new Pagination(pageNo, pageSize, totalCount); 
			List<AllFileInfo> list =  mongoTemplate.find(query, AllFileInfo.class);			
			page.setList(list);
			
//			DBCollection dbColleciton =mongoTemplate.getCollection(collectionName); 	
//			long totalCount = dbColleciton.count();
//			Pagination page = new Pagination(pageNo, pageSize, totalCount); 		
//		    DBCursor cursor = dbColleciton.find().skip(page.getFirstResult()).limit(pageSize);
//		    Iterator<DBObject> it = cursor.iterator();
//			List list = new ArrayList();
//			while (it.hasNext()) {	
//				list.add(it.next().toString());
//			}
//			page.setList(list);
			
			return page;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
		}
	
	
	
	public Pagination getTaskByField(Integer pageNo, Integer pageSize,String field,Object value) {
		if(pageNo==null){
			pageNo =1;
		}
		if(pageSize==null){
			pageSize=20;
		}		

		try{
			Query query = new Query();
			Criteria criatira = new Criteria();
			criatira.orOperator(
					Criteria.where(field).regex(Pattern.compile(
					"^.*" + value + ".*$", Pattern.CASE_INSENSITIVE))
			);						
			query.addCriteria(criatira);
			long totalCount  = mongoTemplate.count(query,AllFileInfo.class);		
			query.with(new Sort(Direction.ASC,"startDate"));

			List<AllFileInfo> list =  mongoTemplate.find(query, AllFileInfo.class);
			Pagination page = new Pagination(pageNo, pageSize, totalCount); 
			page.setList(list);

			return page;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
		}
	
    public AllFileInfo getByUidFromAllFileInfo(String uid) {
        Query query = new Query();
		Criteria criteria = Criteria.where("uid").is(uid);
		query.addCriteria(criteria);
		return   mongoTemplate.findOne(query, AllFileInfo.class);
    }
    
	
    public List<AllFileInfo> getByTableNameAliasFromAllFileInfo(String tableNameAlias) {      
        Query query = new Query();
		Criteria criteria = Criteria.where("tableNameAlias").is(tableNameAlias);
		query.addCriteria(criteria);
		return   mongoTemplate.find(query, AllFileInfo.class);
    }
    
    public Pagination getFromTable(String tableNameAlias,Integer pageNo, Integer pageSize) {      
    	if(pageNo==null){
			pageNo =1;
		}
		if(pageSize==null){
			pageSize=20;
		}		
		DBCollection dbColleciton =mongoTemplate.getCollection(tableNameAlias); 	
		long totalCount = dbColleciton.count();
		Pagination page = new Pagination(pageNo, pageSize, totalCount); 		
	    DBCursor cursor = dbColleciton.find().skip(page.getFirstResult()).limit(pageSize);
	    Iterator<DBObject> it = cursor.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {	
			list.add(it.next().toString());
		}
		page.setList(list);
		return page;
  }
    
}