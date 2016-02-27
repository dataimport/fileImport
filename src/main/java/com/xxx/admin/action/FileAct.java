package com.xxx.admin.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;  
import org.springframework.web.bind.annotation.RequestMapping;

import com.xxx.admin.bean.MongoInToErrorLog;
import com.xxx.admin.bean.Task;
import com.xxx.admin.service.FileService;
import com.xxx.admin.service.TaskService;
import com.xxx.core.exception.ReadFileException;


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
	
//	@RequestMapping(value = "view.htm")
//	public String view(String filePath,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
//		List<String> list = fileService.previewTxtFile(filePath);
//		model.put("list", list);
//		return "file/view";
//	}
	
	@RequestMapping(value = "preview.htm")
	public String preview(ModelMap model,HttpServletRequest request,HttpServletResponse response,String filePath) throws ReadFileException, UnsupportedEncodingException{ 
		//String filePath = new String(request.getParameter("filePath").getBytes("iso-8859-1"), "utf-8");  
		System.out.println("########## "+java.net.URLDecoder.decode(filePath,"UTF-8"));
		List<String>  list =   fileService.previewTxtFile(java.net.URLDecoder.decode(filePath,"UTF-8"));
//		List<String>  returnList = new ArrayList<String>();  
//		//多个空格替换成一个
//		Pattern p = Pattern.compile("\\s+");			
//		for(String lineStr:list){
//			Matcher	m = p.matcher(lineStr);
//			returnList.add(m.replaceAll(" "));
//		}
//		model.put("list", returnList);
		model.put("list", list);
		model.put("filePath", filePath);
		model.put("filePathShow",java.net.URLDecoder.decode(filePath,"UTF-8"));
		return "file/preview";
	}
	
	
	@RequestMapping(value = "errorLogByTaskId.htm")
	public String errorLog(ModelMap model,HttpServletRequest request,HttpServletResponse response,String id) throws ReadFileException, UnsupportedEncodingException{ 
		List list = new ArrayList<MongoInToErrorLog>();
		if(StringUtils.isNotBlank(id)){
			MongoInToErrorLog mongoInToErrorLog  = fileService.getErrorLogByTaskId(id);
			if(mongoInToErrorLog!=null){
				list.add(mongoInToErrorLog);
			}			
		}
		model.put("list", list);		
		return "file/errorLog";
	}
	
	@RequestMapping(value = "allErrorLogByTaskId.htm")
	public String allErrorLog(ModelMap model,HttpServletRequest request,HttpServletResponse response,String id){ 
		 List list = new ArrayList<MongoInToErrorLog>();
		if(StringUtils.isNotBlank(id)){
			list =   fileService.getAllErrorLogByTaskId(id);
		}		
		model.put("list", list);		
		return "file/errorLog";
	}
 
	@Autowired
	private FileService fileService;
	@Autowired
	private TaskService taskService;
	
	public static void main(String[] args) {
		String str="164 gudei f2f76cdbd3972231a603094a0605b69b gudei@126.com 119.131.169.76";
//		String str="12 34";
		String[] a=str.split("\\b");
		for(String m:a){
		System.out.println(m+"kkk");
		}
	}
	
}
