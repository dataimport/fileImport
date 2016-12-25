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
import com.xxx.admin.bean.SubFolder;
import com.xxx.admin.bean.Task;
import com.xxx.admin.service.FolderService;
import com.xxx.admin.service.TaskService;
import com.xxx.core.exception.MongoDBException;
import com.xxx.utils.Pagination;
import com.xxx.utils.ResponseUtils;



@Controller
@RequestMapping("/folder")
public class FolderAct {
	private static final Logger log = LoggerFactory.getLogger(FolderAct.class);


	@RequestMapping(value = "add.htm")
	public String addPath(Folder folder,ModelMap model,HttpServletRequest request,HttpServletResponse response) throws MongoDBException{		
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
	public String deletePath(String id,ModelMap model,HttpServletRequest request,HttpServletResponse response) throws MongoDBException {
		boolean result = folderService.deleteById(id);
		if(result){
			ResponseUtils.renderJson(response, "{\"code\":"+200+",\"msg\":\"删除成功\"}");
		}else{
			ResponseUtils.renderJson(response, "{\"code\":"+500+",\"msg\":\"删除失败\"}");
		}
		return null;
	}
	
	@RequestMapping(value = "list.htm")
	public String list(ModelMap model,HttpServletRequest request,HttpServletResponse response)throws MongoDBException {
		List<Folder> list = folderService.list();
		model.put("list", list);
		return "folder/list";
	}
	
	@RequestMapping(value = "child.htm")
	public String child(String folderPath,String folderId,Boolean append,Integer pageNo, Integer pageSize,ModelMap model,HttpServletRequest request,HttpServletResponse response)throws MongoDBException {
		long start = System.currentTimeMillis();	
		boolean isRoot =false;
		List<Folder> folderList = folderService.list();	
		log.debug(" 从mongodb中加载文件列表耗时："+(System.currentTimeMillis()-start)+" 毫秒");
		String defaultPath = "";
		Folder folder= new Folder();
		if(StringUtils.isNotBlank(folderPath)){
			defaultPath	= folderPath;
		}else if(StringUtils.isNotBlank(folderId)){
			List<Folder> fl =  folderService.findByFields(new String[]{"folderId"}, new String[]{folderId});
			if(fl!=null&&fl.size()>0){
				defaultPath = fl.get(0).getFolderPath();
			}
			model.put("folderId",folderId);
		}else if(folderList.size()>0){
			defaultPath = folderList.get(0).getFolderPath();
		}		
		model.put("folderList",folderList);
		model.put("folderPath",defaultPath);
		/**
		 * 处理是否是根目录的逻辑
		 * contains方法需要在Folder对象中重写
		 * 否则默认比较的是两个obj
		 * 坑点详解：https://www.zhihu.com/question/26872848
		 * */
		folder.setFolderPath(defaultPath);
		if(folderList.contains(folder)){
			isRoot=true;
		}
		if(StringUtils.isNotBlank(defaultPath)){
			List fileTaskInfo =  new ArrayList<File>();
			long start2 = System.currentTimeMillis();	
			File[] childList =  folderService.getChild(defaultPath);
			log.debug(" 根据文件路径加载文件列表耗时："+(System.currentTimeMillis()-start2)+" 毫秒");
			long start3 = System.currentTimeMillis();	
			System.out.println("当前目录下的文件总数："+  childList.length);
			if(pageNo==null){
				pageNo=1;
			}
			if(pageSize==null){
				pageSize=20;
			}				
			File[] files = new File[pageSize];
			Pagination page = new Pagination(pageNo, pageSize, 0);
			if(childList!=null){				
				int fileSize = childList.length;
				int begin = (pageNo-1)*pageSize>fileSize?fileSize:(pageNo-1)*pageSize;
				int end = (begin+pageSize)>fileSize?fileSize:(begin+pageSize);
				int index=0;		
				if(end-begin<pageSize){
					files = new File[end-begin];
				}
				for(int i=begin;i<end;i++){
					files[index++] = childList[i];
					Task t = taskService.getTaskByFilePath(childList[i].getPath());				
					if(t==null){
						t = new Task();
					}
					fileTaskInfo.add(t);	
				}					
				page = new Pagination(pageNo, pageSize,fileSize);				
//				int i=0;
//				for(File file : childList){
//					if(i==20){
//						break;
//					}
//					Task t = taskService.getTaskByFilePath(file.getPath());				
//					if(t==null){
//						t = new Task();
//					}
//					fileTaskInfo.add(t);	
//					i++;
//				}
			}		
			
			//request.getSession().setAttribute("childList", childList);//分布式部署后，需要改成缓存
			log.debug(" 从mongodb中获取每个文件的任务信息耗时："+(System.currentTimeMillis()-start3)+" 毫秒");
			model.put("fileTaskList", fileTaskInfo);
			//model.put("folderChild", childList);
			model.put("folderChild", files);
			List<SubFolder> subFolderList = new ArrayList<SubFolder>();
			for(File file:files){
				if(file!=null && file.isDirectory()){
					SubFolder subFolder = new SubFolder(file.getName().trim(),file.getAbsolutePath());
					subFolderList.add(subFolder);
				}
			}
			model.put("subFolderList", subFolderList);
			model.put("isRoot", isRoot);
			model.put("page", page);
		}else{
			model.put("fileTaskList",new ArrayList<File>());
			model.put("folderChild",null);
			model.put("subFolderList", null);
			model.put("isRoot", isRoot);
			model.put("files", new File[1]);	
		}
				
		if(append!=null&&append){
			return "folder/files";
		}else{
			return "folder/child";			
		}
		
	}	
	
	@Autowired
	private FolderService folderService;
	@Autowired
	private TaskService taskService;
	
}
