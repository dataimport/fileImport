package com.xxx.admin.service;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.xxx.admin.bean.MongoSolrInfo;
import com.xxx.admin.bean.NoRepeatColls;
import com.xxx.admin.data.mongo.MongoCollRepository;
import com.xxx.admin.data.mongo.MongoToSolrRepository;

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
	

    @Resource
    MongoToSolrRepository mongoToSolrRepository;
    @Resource
    MongoCollRepository mongoCollRepository;
	
}
