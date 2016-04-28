package com.xxx.admin.action;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.xxx.admin.bean.AllFileInfo;
import com.xxx.admin.bean.Task;
import com.xxx.admin.service.MongoFileService;
import com.xxx.utils.Pagination;

@Controller
@RequestMapping("/mongo")
public class MongoAct {
		
//	@RequestMapping(value = "list.htm")
//	public String getAllTask(ModelMap model,String collectionName,Integer pageNo, Integer pageSize,HttpServletRequest request,HttpServletResponse response) {
//		Map<String,Object> map  = mongoFileService.getObjectsByCollectionName(pageNo, pageSize, collectionName);	
//		model.put("list", (List<Map<String,String>>)map.get("value"));
//		model.put("page", (Pagination)map.get("page"));
//		model.put("collectionName", collectionName);
//		model.put("pageNo", ((Pagination)map.get("page")).getPageNo());
//		model.put("fields", (LinkedHashSet<String>)map.get("fields"));		
//		return "mongo/list";
//	}

	@RequestMapping(value = "list.htm")
	public String list(ModelMap model,String searchField,String searchValue,String catalog,Integer pageNo, Integer pageSize,HttpServletRequest request,HttpServletResponse response) {		
		Map map = mongoFileService.getTableByCataLog(catalog);
		List<String> list = new ArrayList<String>();
		if(map!=null){				
		    for (Object value : map.values()) {  
		    	Map mapV = (Map<String,String>)JSON.parse(String.valueOf(value));
		    	String count = String.valueOf(mapV.get("count"));
		    	count  = count.substring(0,count.lastIndexOf("."));
		    	list.add(mapV.get("catalog")+"("+count+")");
		    }  
		}
		model.put("catalogList", list);	
		
		if(StringUtils.isBlank(catalog)){
			if(list.size()>0){
			    catalog = list.get(0).substring(0,  list.get(0).lastIndexOf("("));   	
			 }
		}else{
			 catalog = catalog.substring(0,  catalog.lastIndexOf("("));   	
		}		
		
		Pagination page = null;
		if(StringUtils.isNotBlank(searchField)){
			page = mongoFileService.getTaskByField(pageNo, pageSize, searchField, searchValue);
		}else{
			page = mongoFileService.getTaskByCataLog(pageNo,pageSize,catalog);
		}		
		
		if(page!=null){
			List<Task> taskList = page.getList();
			model.put("taskList", taskList);			
		}else{
			model.put("taskList", new ArrayList<Task>());
		}		
		model.put("searchField", searchField);
		model.put("page", page);
		model.put("catalog", catalog);
		return "mongo/list";
		
	}
	
	@RequestMapping(value = "tableInfo.htm")
	public String tableInfo(ModelMap model,String uid,HttpServletRequest request,HttpServletResponse response) {
		AllFileInfo  allFileInfo = mongoFileService.getByUidFromAllFileInfo(uid);
		model.put("task", allFileInfo);
		return "mongo/tableInfo";
		
	}
	
	@RequestMapping(value = "tableValues.htm")
	public String tableValues(ModelMap model,String uid,String tableNameAlias,Integer pageNo, Integer pageSize,HttpServletRequest request,HttpServletResponse response) {
		AllFileInfo  allFileInfo = mongoFileService.getByUidFromAllFileInfo(uid);
		model.put("task", allFileInfo);
		
		List<AllFileInfo>  list = mongoFileService.getByTableNameAliasFromAllFileInfo(tableNameAlias);
		model.put("list", list);		
		
		//Pagination page = mongoFileService.getFromTable(tableNameAlias, pageNo, pageSize);
		//model.put("listValue", page.getList());
		//model.put("page", page);
		
		Map<String,Object> map  = mongoFileService.getObjectsByCollectionName(pageNo, pageSize, tableNameAlias);	
		model.put("listValue", (List<Map<String,String>>)map.get("value"));
		model.put("page", (Pagination)map.get("page"));		
		model.put("pageNo", ((Pagination)map.get("page")).getPageNo());
		model.put("fields", (LinkedHashSet<String>)map.get("fields"));	
		
		model.put("tableNameAlias", tableNameAlias);
		model.put("uid", uid);
		return "mongo/tableValues";
		
		
		
		
	}
	
	
		@Autowired
	private MongoFileService mongoFileService;
}
