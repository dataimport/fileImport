package com.xxx.admin.action;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.CommandResult;
import com.xxx.admin.data.mongo.TaskRepository;



@Controller
@RequestMapping("/index")
public class indexAct {
	private static final Logger log = LoggerFactory.getLogger(indexAct.class);
	
	@RequestMapping(value = "index.htm")
	public @ResponseBody
	String index(HttpServletRequest request,HttpServletResponse response) {
		 CommandResult commandResult = taskRepository.getDBStats();		
		 String jsonReturn = "{\"collections\":"+0+",\"storageSize\":"+0+"}";
		 if(commandResult!=null){
			 jsonReturn = "{\"collections\":"+commandResult.get("collections")+",\"storageSize\":"+commandResult.get("storageSize")+"}";	 
		 }
		
         return jsonReturn;
	}
	
    @Resource(name = "task")
    TaskRepository taskRepository; 
}
