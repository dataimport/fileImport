package com.xxx.admin.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xxx.admin.bean.Task;
import com.xxx.admin.service.FileService;
import com.xxx.admin.service.FolderService;
import com.xxx.admin.service.TaskService;
import com.xxx.core.exception.ReadFileException;
import com.xxx.utils.Pagination;
import com.xxx.utils.ResponseUtils;

@Controller
@RequestMapping("/task")
public class TaskAct {
	//增加任务的创建者
	public String createUser="lxc_admin";
	
	@RequestMapping(value = "list.htm")
	public String getAllTask(ModelMap model,Integer status,Integer pageNo, Integer pageSize,HttpServletRequest request,HttpServletResponse response) {
		Pagination page = taskService.getTaskByStatus(pageNo,pageSize,status);
		//System.out.println(page.getTotalPage()+" ######");
		//System.out.println( page.getList().size()+" ## page.getList()####");
		model.put("list", page.getList());
		model.put("page", page);
		model.put("status", status);
		model.put("pageNo", page.getPageNo());
		return "task/list";
	}
	
	/**
	 * 导入文件前任务预览效果
	 * @param filePath
	 * @param separator
	 * @param firstLineIgnore
	 * @param lines
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "v_task.htm")
	public String v_task(String filePath,String separator,boolean firstLineIgnore,String[] lines,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
			
		if(StringUtils.isBlank(separator)){
			separator="\\s+"; //代表多个空格
			//separator="wKhTglXeaGY";
			model.put("separator", " ");
		}else{
			model.put("separator", separator);
		}
				
		String[] columns =new String[]{};
		if(lines.length>0){
			if(!"\\s+".equals(separator)){
				separator = separatorCheck(separator);//特殊字符特换
			}			
			columns = lines[0].split(separator,-1);						
		}
		model.put("columns", columns);
		int columnsSize = columns.length; 
		
		//多个空格替换成一个
		int num =10;
		int lenght = lines.length;
		
		Pattern p = Pattern.compile("\\s+");	
		if(lenght<10){
			num = lines.length;
		}
		List<String>  returnList = new ArrayList<String>();  
		StringBuffer message = new StringBuffer();
		for(int i=0;i<num;i++){
			if(lines[i].split(separator,-1).length==columnsSize){
				Matcher	m = p.matcher(lines[i]);				
				returnList.add(m.replaceAll(" "));
			}else{
				message.append(i+1+",");				
			}			
		}
		
		
		if(firstLineIgnore){
			model.put("text", returnList.subList(1, returnList.size()));	
		}else{
			model.put("text", returnList);	
		}
		
		model.put("filePath", filePath);
		if(firstLineIgnore){
			model.put("firstLineIgnore", "true");
		}else{
			model.put("firstLineIgnore", "false");
		}		
		if(message.length()>0){
			model.put("errorMessage", "提醒：此文件的 第： "+message.toString()+" ...... 行的列数与第一行的列数不相等，显示的时候已经忽略，导入数据的时候也会被忽略");
		}
		return "file/task_view";
	}	
	
	/**
	 *  根据相同规则导入下一个文档预览效果接口
	 * @param preFilePath 上一个文件路径 用于建设规则
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "v_taskBySameRule.htm")
	public String v_taskBySameRule(String preFilePath,ModelMap model,HttpServletRequest request,HttpServletResponse response)throws ReadFileException {

		if(StringUtils.isNotBlank(preFilePath)){
			File file = new File(preFilePath);
			if(file.exists()){
				Task task = taskService.getTaskByFilePath(preFilePath);
				if(task!=null){				
					File[] childList = folderService.getChildByLastModified(file.lastModified(), preFilePath.substring(0, preFilePath.lastIndexOf("\\")));
					if(childList!=null&&childList.length>1){
						String thisFilePath = childList[1].getPath();
						if(!new File(thisFilePath).isDirectory()){
							List<String> list = fileService.previewTxtFile(thisFilePath);
							String[] lines = new String[list.size()]; 
							for(int i=0;i<list.size();i++){
								lines[i] = list.get(i);
							}
							model.put("task", task);
							return v_task(thisFilePath,task.getSeparator(),task.isFirstLineIgnore(),
									lines,model,request,response);
						}else{
							return null;
						}
						
					}else{
						return null;
					}
					
				}else{
					return null;
				}
			}else{
				return null;
			}
			
			
			
			
		}else{
			return null;
		}
	}	

	@RequestMapping(value = "o_task.htm")
	public String o_task(Task task,ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		task.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
		task.setCreateUser(createUser);
		if("0000-00-00 00:00:00".equals(task.getRunTime()))//立即执行
		{	
			task.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			task.setRunTime(task.getStartDate());
			task.setTaskStatus(1);//执行中
			boolean result = taskService.createTask(task);//创建任务
			
			if(result){
				int successNum = fileService.saveFileToMongo(task);
				if(successNum!=-1){
					ResponseUtils.renderJson(response, "{\"code\":200,\"msg\":\"创建任务,并且入库成功，本次任务共导入【  "+successNum+"  】条数据\"}");
				}else{
					ResponseUtils.renderJson(response, "{\"code\":200,\"msg\":\"创建任务成功,入库失败\"}");
				}			
			}else{
				taskService.taskUpdate(task, null,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),-2);
				ResponseUtils.renderJson(response, "{\"msg\":\"创建任务失败\"}");
			}			
		}else{//创建任务
			task.setTaskStatus(0);
			taskService.createTask(task,false);//创建任务
			ResponseUtils.renderJson(response, "{\"msg\":\"创建任务成功\"}");
		}		
		return null;
	}
	
	private String separatorCheck (String separator){
		  if(separator.indexOf("\\")!=-1){
			  separator = separator.replace("\\", "\\\\");
		  }
		  if(separator.indexOf("|")!=-1){
			  separator = separator.replace("|", "\\|");
		  }
		  if(separator.indexOf("[")!=-1){
			  separator = separator.replace("[", "\\[");
		  }
		  if(separator.indexOf("]")!=-1){
			  separator = separator.replace("[", "\\]");
		  }		  
		  if(separator.indexOf(".")!=-1){
			  separator = separator.replace(".", "[.]");
		  }
		  if(separator.indexOf("*")!=-1){
			  separator = separator.replace("*", "\\*");
		  }
		  
		  return separator;

	}
	
	@Autowired
	private FileService fileService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FolderService folderService;
}
