package com.xxx.admin.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;  
import org.springframework.web.bind.annotation.RequestMapping;
import com.xxx.admin.bean.Folder;
import com.xxx.admin.bean.Task;
import com.xxx.admin.service.FolderService;
import com.xxx.admin.service.TaskService;
import com.xxx.utils.ResponseUtils;



@Controller
@RequestMapping("/folder")
public class FolderAct {
	private static final Logger log = LoggerFactory.getLogger(FolderAct.class);

	@RequestMapping(value = "add.htm")
	public String addPath(Folder folder,ModelMap model,HttpServletRequest request,HttpServletResponse response) {		
		List<Folder> list = folderService.findByFields(new String[]{"folderPath"}, new String[]{folder.getFolderPath()});
		if(list.size()==0){
			folder.setFolderId(UUID.randomUUID().toString().replaceAll("-", ""));
			folder.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
			folderService.add(folder);		
			ResponseUtils.renderJson(response, "{\"code\":"+200+",\"msg\":\"添加成功\"}");
		}else{
			ResponseUtils.renderJson(response, "{\"code\":"+500+",\"msg\":\"已经存在\"}");
		}		
		return null;
	}
	
	@RequestMapping(value = "delete.htm")
	public String deletePath(String id,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		boolean result = folderService.deleteById(id);
		if(result){
			ResponseUtils.renderJson(response, "{\"code\":"+200+",\"msg\":\"删除成功\"}");
		}else{
			ResponseUtils.renderJson(response, "{\"code\":"+500+",\"msg\":\"删除失败\"}");
		}
		return null;
	}
	
	@RequestMapping(value = "list.htm")
	public String list(ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		List<Folder> list = folderService.list();
		model.put("list", list);
		return "folder/list";
	}
	
	@RequestMapping(value = "child.htm")
	public String child(String folderPath,Boolean append,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		List<Folder> folderList = folderService.list();	
		String defaultPath = "";
		if(StringUtils.isNotBlank(folderPath)){
			defaultPath	= folderPath;
		}else if(folderList.size()>0){
			defaultPath = folderList.get(0).getFolderPath();			
		}		
		model.put("folderList",folderList);
		model.put("folderPath",defaultPath);		
		if(StringUtils.isNotBlank(defaultPath)){
			List fileTaskInfo =  new ArrayList<File>();
			File[] childList = folderService.getChild(defaultPath);
			if(childList!=null){
				int i=0;
				for(File file : childList){
					if(i==20){
						break;
					}
					Task t = taskService.getTaskByFilePath(file.getPath());				
					if(t==null){
						t = new Task();
					}
					fileTaskInfo.add(t);	
					i++;
				}
			}			
			model.put("fileTaskList", fileTaskInfo);
			model.put("folderChild", childList);
		}else{
			model.put("fileTaskList",new ArrayList<File>());
			model.put("folderChild", new ArrayList<File>());			
		}
				
		if(append!=null&&append){
			return "folder/childAppend";
		}else{
			return "folder/child";			
		}
		
	}	
	
	@Autowired
	private FolderService folderService;
	@Autowired
	private TaskService taskService;
	
}
