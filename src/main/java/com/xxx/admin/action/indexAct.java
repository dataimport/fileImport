package com.xxx.admin.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.CommandResult;
import com.xxx.admin.data.mongo.TaskRepository;
import com.xxx.utils.JsonUtil;



@Controller
@RequestMapping("/index")
public class indexAct {
	//private static final Logger log = LoggerFactory.getLogger(indexAct.class);
	
	@RequestMapping(value = "index.htm")
	public @ResponseBody
	String index(HttpServletRequest request,HttpServletResponse response) {	
		
//		Object result = request.getSession().getAttribute("indexAct_index");//从缓存中取
//		if(result!=null){
//			return String.valueOf(result);
//		}
		 CommandResult commandResult = taskRepository.getDBStats();		
		 Double totalCount= taskRepository.getFileTotalCount();
		 String jsonReturn = "{\"collections\":"+0+",\"storageSize\":"+0+",\"totalCount\":"+0+"}";
		 if(commandResult!=null){
			
			 jsonReturn = "{\"collections\":"+commandResult.get("collections")+",\"storageSize\":"+commandResult.get("storageSize")+",\"totalCount\":"+totalCount+"}";	 
		 }			
//		 request.getSession().setAttribute("indexAct_index", jsonReturn);
//		 request.getSession().setMaxInactiveInterval(600);//缓存10分钟
	     return jsonReturn;		
	}
	
	@RequestMapping(value = "es_status.htm")
	public @ResponseBody
	String elasticSearchStatus(HttpServletRequest request,HttpServletResponse response) throws MalformedURLException, IOException, Exception {	
		String jsonReturn = "{\"collections\":"+0+",\"storageSize\":"+0+",\"totalCount\":"+0+"}";
		 
		String url="http://localhost:9200/_status";
		InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	        sb.append((char) cp);
	      }
	      
	      JSONObject jsonobj = new JSONObject(sb.toString());
	      
	      jsonobj = new JSONObject(jsonobj.get("indices").toString());
	      long indicesTotal=0;
	      long docsTotal=0;
	      long sizeTotal=0;
	        Map result = new HashMap();
	        Iterator iterator = jsonobj.keys();
	        String key = null;
	        JSONObject childObj = null;
	        
	        while (iterator.hasNext()) {
	        	indicesTotal++;
	            key = (String) iterator.next();
	            childObj = (JSONObject) jsonobj.get(key);
	            
	            JSONObject docsObj = (JSONObject) childObj.get("docs");
	            
	            Long num_docs  = docsObj.getLong("num_docs");
	            docsTotal+=num_docs;
	            
	            JSONObject indexObj = (JSONObject) childObj.get("index");
	            long  size_in_bytes = indexObj.getLong("primary_size_in_bytes");
	            sizeTotal+=size_in_bytes;
	            
	            System.out.println("key:"+key+":"+num_docs+" docsTotal:"+docsTotal);
	            
	        }
	        jsonReturn = "{\"collections\":"+indicesTotal+",\"storageSize\":"+sizeTotal+",\"totalCount\":"+docsTotal+"}";
			 
	    } finally {
	      try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
		 
	    return jsonReturn;
		
	}
	
	
	public static void main(String[] args) throws Exception, IOException {
		String url="http://localhost:9200/_status";
		InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	        sb.append((char) cp);
	      }
	      
	      JSONObject jsonobj = new JSONObject(sb.toString());
	      
	      jsonobj = new JSONObject(jsonobj.get("indices").toString());
	      long indicesTotal=0;
	      long docsTotal=0;
	      long sizeTotal=0;
	        Map result = new HashMap();
	        Iterator iterator = jsonobj.keys();
	        String key = null;
	        JSONObject childObj = null;
	        
	        while (iterator.hasNext()) {
	        	indicesTotal++;
	            key = (String) iterator.next();
	            childObj = (JSONObject) jsonobj.get(key);
	            
	            JSONObject docsObj = (JSONObject) childObj.get("docs");
	            
	            Long num_docs  = docsObj.getLong("num_docs");
	            docsTotal+=num_docs;
	            
	            JSONObject indexObj = (JSONObject) childObj.get("index");
	            long  size_in_bytes = indexObj.getLong("primary_size_in_bytes");
	            sizeTotal+=size_in_bytes;
	            
	            System.out.println("key:"+key+":"+num_docs+" docsTotal:"+docsTotal);
	            
	        }
	      
	      
	        String jsonReturn = "{\"collections\":"+indicesTotal+",\"storageSize\":"+sizeTotal+",\"totalCount\":"+docsTotal+"}";
			 
	      
	      
	    } finally {
	      try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	}
	
		
    @Resource(name = "task")
    TaskRepository taskRepository;
}
