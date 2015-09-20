package com.xxx.admin.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import com.xxx.admin.bean.MongoSolrInfo;
import com.xxx.admin.bean.NoRepeatColls;
import com.xxx.admin.data.mongo.FileInMongoRepository;
import com.xxx.admin.data.mongo.MongoCollRepository;
import com.xxx.admin.data.mongo.MongoToSolrRepository;
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


    @Resource
    MongoToSolrRepository mongoToSolrRepository;
    @Resource
    MongoCollRepository mongoCollRepository;
    @Resource
    FileInMongoRepository fileInMongoRepository;
	
}
