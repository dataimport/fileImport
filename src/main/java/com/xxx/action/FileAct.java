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

import com.xxx.admin.manager.FileManager;
import com.xxx.admin.manager.TaskManager;



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
	
	@Autowired
	private FileManager fileManager;
	@Autowired
	private TaskManager taskManager;
	
}
