package com.xxx.action;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;  
import org.springframework.web.bind.annotation.RequestMapping;

import com.xxx.admin.bean.Task;
import com.xxx.admin.manager.FileManager;
import com.xxx.admin.manager.TaskManager;
import com.xxx.utils.ResponseUtils;



@Controller
@RequestMapping("/file")
public class FileAct {
	private static final Logger log = LoggerFactory.getLogger(FileAct.class);
	private static ResourceBundle rb = null;
	private static String cfg = "conf";
	private static String fileRoot ="";
	
	static
	{
	    rb = ResourceBundle.getBundle(cfg);
	    fileRoot = rb.getString("fileRoot");
	}
	  
	@RequestMapping(value = "rootInfo.htm")
	public String allRoot(String root,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		List<String> rootList = fileManager.viewFilePath();		
		String filePath =root;
		if(StringUtils.isBlank(root)){
			root="";
			filePath = rootList.get(0);
		}
		if(rootList.size()>0){
			model.put("rootFolder", rootList.get(0));
			List<? extends File> fileList = fileManager.getChild(filePath);
			model.put("fileList", fileList);
		}
		model.put("root", root);	
		model.put("rootList", rootList);	
		model.put("contextPath", request.getContextPath());
		return "file/rootList";
	}	
	
	@RequestMapping(value = "fileTaskInfo.htm")
	public String fileInfo(String filePath,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		if (StringUtils.isBlank(filePath) ) {
			return "file/fileTaskInfo";
		} else {
			List<Task> list = taskManager.getTaskByFilePath(filePath);
			model.put("list", list);
			return "file/fileTaskInfo";
		}	
		
	}
	
	@RequestMapping(value = "tree.htm")
	public String tree(ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		String root = request.getParameter("root");
		log.debug("tree path={}", root);
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			root = fileRoot;
			model.addAttribute("isRoot", true);
		} else {
			model.addAttribute("isRoot", false);
		}
		
		List<? extends File> fileList = fileManager.getChild(root);
		model.put("root", root);
		model.put("list", fileList);
		return "file/list";
	}
	
	@RequestMapping(value = "view.htm")
	public String view(String filePath,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		List<String> list = fileManager.view(filePath);
		model.put("list", list);
		return "file/view";
	}
	
	@RequestMapping(value = "addPath.htm")
	public String addPath(String filePath,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		boolean result = fileManager.addFilePath(filePath);		
		if(result){
			ResponseUtils.renderJson(response, "{\"code\":"+200+",\"msg\":\"添加成功\"}");
		}else{
			ResponseUtils.renderJson(response, "{\"code\":"+500+",\"msg\":\"添加失败\"}");
		}
		
		return null;
	}
	
	@RequestMapping(value = "delePath.htm")
	public String delePath(String filePath,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		boolean result = fileManager.deleteFilePath(filePath);
		if(result){
			ResponseUtils.renderJson(response, "{\"code\":"+200+",\"msg\":\"删除成功\"}");
		}else{
			ResponseUtils.renderJson(response, "{\"code\":"+500+",\"msg\":\"删除失败\"}");
		}
		return null;
	}
	
	@RequestMapping(value = "viewPath.htm")
	public String viewPath(ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		List<String> list = fileManager.viewFilePath();
		 model.put("list", list);
		 return "file/root";
	}
	
	@Autowired
	private FileManager fileManager;
	@Autowired
	private TaskManager taskManager;
	
}
