package com.xxx.admin.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxx.admin.bean.MongoSolrInfo;
import com.xxx.admin.bean.NoRepeatColls;
import com.xxx.admin.service.MongoFileService;
import com.xxx.utils.ResponseUtils;

@Controller
@RequestMapping("/mongo")
public class MongoFileCollAct {
	
	/**
	 * 检查表名是否存在
	 * @param collectionName
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getByCollectionName.htm")
	public String  getByCollectionName(String collectionName,ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		
		
		Map map = new HashMap();	
		try{
			map.put("code", 200);
			map.put("exist", false);
			if(StringUtils.isNotBlank(collectionName)){
				List<MongoSolrInfo> list =  mongoFileService.getByCollectionName(collectionName);
				if(list!=null&&list.size()>0){	
					map.put("code", 100);
					map.put("exist", true);
					//json.put("content", net.sf.json.JSONArray.fromObject(list));
				}else{
					map.put("code", 200);
					map.put("exist", false);
				}				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}	
		
		ResponseUtils.renderJson(response,JSON.toJSONString(map));
		return null;
	}
	
	@RequestMapping(value = "getByKeyWord.htm")
	public String  getByKeyWord(String KeyWord,ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		
		JSONArray jsonArray = new JSONArray(); 
		JSONObject json = new JSONObject();	
		try{
			json.put("code", 200);
			json.put("exist", false);
			if(StringUtils.isNotBlank(KeyWord)){
				List<MongoSolrInfo> list =  mongoFileService.getByNameOrTagOrOrgin(KeyWord);
				if(list!=null&&list.size()>0){	
					json.put("exist", true);
					json.put("content", net.sf.json.JSONArray.fromObject(list));
				}else{
					json.put("code", 200);
					json.put("exist", false);
				}				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}	
		jsonArray.add(json);
		ResponseUtils.renderJson(response,jsonArray.toString());
		return null;
	}

	@RequestMapping(value = "allCollections.htm")
	public String allCollections(ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		
		try{
		 List<NoRepeatColls>  list = mongoFileService.allCollectons();
		 	model.put("list", list);		
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		 return "mongo/collList";
	}
	
	@Autowired
	private MongoFileService mongoFileService;
}
