package com.xxx.admin.action;

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
import com.xxx.admin.service.FileService;
import com.xxx.admin.service.TaskService;


@Controller
@RequestMapping("/file")
public class FileAct {
	private static final Logger log = LoggerFactory.getLogger(FileAct.class);
	
	@RequestMapping(value = "fileTaskInfo.htm")
	public String fileInfo(String filePath,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		if (StringUtils.isBlank(filePath) ) {
			return "file/fileTaskInfo";
		} else {
			Task task = taskService.getTaskByFilePath(filePath);
			model.put("task", task);
			return "file/fileTaskInfo";
		}			
	}
	
//	@RequestMapping(value = "tree.htm")
//	public String tree(ModelMap model,HttpServletRequest request,HttpServletResponse response) {
//		String root = request.getParameter("root");
//		log.debug("tree path={}", root);
//		if (StringUtils.isBlank(root) || "source".equals(root)) {
//			root = fileRoot;
//			model.addAttribute("isRoot", true);
//		} else {
//			model.addAttribute("isRoot", false);
//		}
//		
//		//List<? extends File> fileList = fileService.getChild(root);
//		List<? extends File> fileList = null;
//		model.put("root", root);
//		model.put("list", fileList);
//		return "file/list";
//	}
	
	@RequestMapping(value = "view.htm")
	public String view(String filePath,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		List<String> list = fileService.preview(filePath);
		model.put("list", list);
		return "file/view";
	}
	
	@RequestMapping(value = "preview.htm")
	public String v_task(String filePath,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		List<String>  list =   fileService.preview(filePath);
		model.put("list", list);
		model.put("filePath", filePath);
		return "file/preview";
	}
	

	@Autowired
	private FileService fileService;
	@Autowired
	private TaskService taskService;
	
}
