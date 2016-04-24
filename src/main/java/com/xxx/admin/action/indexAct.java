package com.xxx.admin.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.CommandResult;
import com.xxx.admin.data.mongo.TaskRepository;



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
		
    @Resource(name = "task")
    TaskRepository taskRepository; 
}
