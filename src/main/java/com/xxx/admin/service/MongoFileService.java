package com.xxx.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.springframework.stereotype.Service;

import com.xxx.admin.bean.AllFileInfo;
import com.xxx.admin.bean.MongoSolrInfo;
import com.xxx.admin.bean.NoRepeatColls;
import com.xxx.admin.bean.Task;
import com.xxx.admin.data.mongo.FileInMongoRepository;
import com.xxx.admin.data.mongo.MongoCollRepository;
import com.xxx.admin.data.mongo.MongoToSolrRepository;
import com.xxx.admin.data.mongo.TaskRepository;
import com.xxx.utils.Pagination;

@Service("mongofileService")
public class MongoFileService {
	
	public List<MongoSolrInfo> getByCollectionName(String collectionName) {	
		
		String[] keys = new String[]{"collectionName"}; 
		String[] values = new String[]{collectionName}; 		
		
		return mongoToSolrRepository.getByFields(keys,values);
	}
	
	public List<MongoSolrInfo> getByNameOrTagOrOrgin(String keyWord) {	
		return mongoToSolrRepository.getByNameOrTagOrOrgin(keyWord);
		
	}
	
	public  List<NoRepeatColls>  allCollectons() {
		return mongoCollRepository.allCollectons();
	}
	
	public Map<String,Object> getObjectsByCollectionName(Integer pageNo, Integer pageSize,String collectionName) {
		Pagination page =  fileInMongoRepository.getObjectsByCollectionName(pageNo, pageSize, collectionName);
		List<String> list = page.getList();
		Set<String> fields = new LinkedHashSet<String>();
		Map map = new HashMap();
		List<Map<String,String>> listVaule = new ArrayList<Map<String,String>>();	
		Object[] object =null;
		for(String s:list){
			JSONObject jsonObj =JSONObject.fromObject(s);			
			Iterator it = jsonObj.keys();
			Map<String,String> mapTemp =new HashMap<String,String>();
			String objectNext = null;	
			while(it.hasNext()){				
				objectNext = new String();
				objectNext = it.next().toString();
				mapTemp.put(objectNext, jsonObj.getString(objectNext));
				fields.add(objectNext);	
			}	
			listVaule.add(mapTemp);
		}
		map.put("fields", fields);
		map.put("value", listVaule);
		map.put("page", page);
		
		return map;
		
	}
	
	/**
	 * 根据目录查看mongodb 表信息
	 * @param pageNo
	 * @param pageSize
	 * @param collectionName
	 * @return
	 */
	public Map getTableByCataLog(String catalog) {
		return 	fileInMongoRepository.getTableByCataLog(catalog);		
	}
	
	public Pagination getTaskByCataLog(Integer pageNo, Integer pageSize,String catalog) {
		return fileInMongoRepository.getTaskByCataLog(pageNo, pageSize, catalog);		 
	}
	
	
	public Pagination getTaskByField(Integer pageNo, Integer pageSize,String field,Object value) {
		return fileInMongoRepository.getTaskByField(pageNo, pageSize, field,value);		 
	}

	
	public AllFileInfo getByUidFromAllFileInfo(String uid) {
		return fileInMongoRepository.getByUidFromAllFileInfo(uid);
		//return null;
	}
	
	public  List<AllFileInfo> getByTableNameAliasFromAllFileInfo(String tableNameAlias) {
		return fileInMongoRepository.getByTableNameAliasFromAllFileInfo(tableNameAlias);
		//return null;
	}
	
	public  Pagination getFromTable(String tableNameAlias,Integer pageNo, Integer pageSize) {
		return fileInMongoRepository.getFromTable(tableNameAlias,pageNo,pageSize);
		//return null;
	}
	
	
	public  Boolean deleteCollection(String uid,String collectionName) {
		try{
			fileInMongoRepository.deleteObjectByIdFromAllFileInfo(uid);//删除总表记录中的数据
			mongoCollRepository.deleteObjectByNanmeAlias(collectionName);//删除norepeae表记录
			mongoCollRepository.dropCollection(collectionName);//drop
			mongoCollRepository.dropCollection(collectionName+"_fail");//drop
			//删除索引中的数据
			Client  client = ElasticSearchManager.getClient();
			client.prepareDelete().setIndex(collectionName).execute().actionGet();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
    @Resource(name = "task")
    TaskRepository taskRepository;   
    @Resource
    MongoToSolrRepository mongoToSolrRepository;
    @Resource
    MongoCollRepository mongoCollRepository;
    @Resource
    FileInMongoRepository fileInMongoRepository;
	
}
