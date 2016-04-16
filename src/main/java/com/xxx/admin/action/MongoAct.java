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
	public String list(ModelMap model,String catalog,Integer pageNo, Integer pageSize,HttpServletRequest request,HttpServletResponse response) {
		Map map = mongoFileService.getTableByCataLog(catalog);
		List<String> list = new ArrayList<String>();
		if(map!=null){				
		    for (Object value : map.values()) {  		
		    	Map mapV = (Map<String,String>)JSON.parse(String.valueOf(value));
		    	list.add(mapV.get("catalog")+"");
		    }  
		}
		model.put("catalog", list);
	   
		if(StringUtils.isBlank(catalog)){
			if(list.size()>0){
			    catalog = list.get(0);   	
			 }
		}
		
		Pagination page = mongoFileService.getTaskByCataLog(pageNo,pageSize,catalog);
		if(page!=null){
			List<Task> taskList = page.getList();
			model.put("taskList", taskList);			
		}else{
			model.put("taskList", new ArrayList<Task>());
		}		
		return "mongo/list";
		
	}
	
		@Autowired
	private MongoFileService mongoFileService;
}
