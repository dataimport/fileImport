package com.xxx.admin.service;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.xxx.admin.bean.SolrTask;
import com.xxx.elasticsearch.data.mongo.SolrTaskRepository;


@Service("solrImportService")
public class SolrImportService {
	
	@Autowired
    MongoTemplate mongoTemplate;
 
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
	
	/**
	 * 将mongodb中的数据导入到solr中
	 * 此处底层用的是es
	 * @param t
	 * @return
	 */
	public boolean saveMongoToSolr(SolrTask task) {		
		System.out.println("SolrImportService-> save mongo to solr!! ===success");
		String uid = task.getUid();
		String[] key = new String[]{"taskStatus","startDate","endDate"};
		Object[] value = new Object[]{task.getTaskStatus(),task.getStartDate(),task.getEndDate()};
		solrTaskRepository.updateTaskByField(uid, key, value);
		//读取mongo中的Collection信息和字段信息
		task.getColumnIndex();
		
		String indexName = task.getTableName();
		String tags = task.getTags();
		
		//创建solr库
		
		//读取Collection中的数据，每100条写一次库
		
		
		
		//完成solr库的导入
		batchImport(uid,tags,indexName,task.getColumnName());
		
		//更新task的状态
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(now);
		
		key = new String[]{"taskStatus","endDate"};
		value = new Object[]{2,nowTime};
		solrTaskRepository.updateTaskByField(uid, key, value);
		
		return true;
		
	}
	
	
	public void batchImport(String taskId,String tags,String indexName,String[] fieldNames ){
		long start =System.currentTimeMillis();
		Client client = new TransportClient()
						.addTransportAddress(
						new InetSocketTransportAddress("localhost", 9300)
						);
		BulkRequestBuilder bulkRequest = client.prepareBulk();   
				try{
				    String collectionName = indexName;
					if(StringUtils.isNotBlank(collectionName)){
						
						
						//按分页的方式取数据，并循环入库
						DBCollection dbColleciton =mongoTemplate.getCollection(collectionName); 	
						long totalCount = dbColleciton.count();
						//更新任务中需要处理的真实totalCount
						String[] key = new String[]{"totalCount"};
						Object[] value = new Object[]{totalCount};
						solrTaskRepository.updateTaskByField(taskId, key, value);
					    //从mongodb中取出相应的数据，循环写入solr
						DBCursor cursor = dbColleciton.find();
					    Iterator<DBObject> it = cursor.iterator();
						long i=0;
						//批次信息
						long batchNo = 0;
						while (it.hasNext()) {
							i++;
							DBObject currentData = it.next();
													
							System.out.println("line no:"+i); 
								XContentBuilder xcb = jsonBuilder().startObject();
								for(String fieldName:fieldNames){
									xcb.field(fieldName,currentData.get(fieldName));
								}
								xcb.endObject();
								bulkRequest.add(client.prepareIndex(indexName, "tuser", i+"")
										.setSource(xcb)
										);  
								if(i%100==0 || i==totalCount){
									batchNo++;
									BulkResponse bulkResponse = bulkRequest.execute().actionGet();   
									if (bulkResponse.hasFailures()) {   
										//打印出错误信息，后续改为log的方式输出
										System.err.println("本批次有数据写入失败,taskId:"+taskId+
												",批次编号:"+batchNo+
												",每批次的条数："+100+
												",当前行号："+i);
										//此处是有异常的情况，不是完全失败的情况
										//-100,写入solr时，某一部分数据异常
										key = new String[]{"taskStatus"};
										value = new Object[]{-100};
										solrTaskRepository.updateTaskByField(taskId, key, value);
									}else{
										//更新task中的成功条数
										key = new String[]{"runNum"};
										value = new Object[]{i};
										solrTaskRepository.updateTaskByField(taskId, key, value);
									}
									bulkRequest=null; 
									bulkRequest = client.prepareBulk();
									
								}
						}
						//				long duration= (System.currentTimeMillis()-start)/1000;
						long duration= (System.currentTimeMillis()-start);
						key = new String[]{"timeUse"};
						value = new Object[]{duration};
						solrTaskRepository.updateTaskByField(taskId, key, value);
						
						System.out.println("入库到索引"+indexName+",总耗时:"+duration+"ms,totalCount:"+totalCount);
					}
					
					
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	

    @Resource
    SolrTaskRepository solrTaskRepository; 
    
}
