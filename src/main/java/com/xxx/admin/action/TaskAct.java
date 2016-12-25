package com.xxx.admin.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.xxx.admin.bean.AllCollectionName;
import com.xxx.admin.bean.Task;
import com.xxx.admin.service.FileService;
import com.xxx.admin.service.FolderService;
import com.xxx.admin.service.TaskService;
import com.xxx.core.exception.ReadFileException;
import com.xxx.utils.Pagination;
import com.xxx.utils.ResponseUtils;
import com.xxx.utils.StrUtils;

@Controller
@RequestMapping("/task")
public class TaskAct {
	//增加任务的创建者
	public String createUser="lxc_admin";
	
	@RequestMapping(value = "list.htm")
	public String getAllTask(ModelMap model,Integer status,Integer pageNo, Integer pageSize,HttpServletRequest request,HttpServletResponse response) {
		String collectionName="";
		if(status!=null&&888==status){
			collectionName = AllCollectionName.TASKINFO_COLLECTIONNAME;
		}else{
			collectionName = AllCollectionName.ALLFILEINFO_COLLECTIONNAME;
		}
		//collectionName = AllCollectionName.ALLFILEINFO_COLLECTIONNAME;
		Pagination page = taskService.getTaskByStatus(pageNo,pageSize,status,collectionName);
		//System.out.println(page.getTotalPage()+" ######");
		//System.out.println( page.getList().size()+" ## page.getList()####");
		
		long totalCount = taskService.getTotalCountByStatus(null,collectionName);
		long runingCount = taskService.getTotalCountByStatus(1,collectionName);
		long failedCount = taskService.getTotalCountByStatus(-2,collectionName);
		long waitCount = taskService.getTotalCountByStatus(0,collectionName);
		
		model.put("list", page.getList());
		model.put("page", page);
		model.put("status", status);
		model.put("pageNo", page.getPageNo());
		
		model.put("totalCount", totalCount);
		model.put("runingCount", runingCount);
		model.put("failedCount", failedCount);
		model.put("waitCount", waitCount);
		
		String type = request.getParameter("type");
		if("ajax".equals(type)){
//			String jsonText = JSON.toJSONString(page.getList());  
//			ResponseUtils.renderJson(response, jsonText);
//			return null;
			return "task/list_ajax";	
		}else{
			return "task/list";	
		}
		
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
	public String v_task(String filePath,String separator,String fileCode,boolean firstLineIgnore,String[] lines,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
			
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
				separator = StrUtils.separatorCheck(separator);//特殊字符特换
			}			
			columns = lines[0].split(separator,-1);						
		}
		model.put("columns", columns);
		int columnsSize = columns.length; 
		//System.out.println(columnsSize);
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
				returnList.add(lines[i]);
			}			
		}
		
		
		if(firstLineIgnore){
			model.put("text", returnList.subList(1, returnList.size()));	
		}else{
			model.put("text", returnList);	
		}
		try{
			model.put("filePath", java.net.URLDecoder.decode(filePath,"UTF-8"));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		model.put("unDeCodeFilePath", filePath);
		
		if(firstLineIgnore){
			model.put("firstLineIgnore", "true");
		}else{
			model.put("firstLineIgnore", "false");
		}		
		if(message.length()>0){
			model.put("errorMessage", "提醒：此文件的 第： "+message.toString()+" ...... 行的列数与第一行的列数不相等，如果勾选了不相等的列，导入数据的时候也会被忽略");
		}
		model.put("fileCode", fileCode);
		model.put("fileName", getFileName(filePath));
		return "task/task_view";
	}	
	
	
	/**
	 * 导入excel 任务预览
	 * @param filePath
	 * @param separator
	 * @param firstLineIgnore
	 * @param lines
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "v_excelTask.htm")
	public String v_excelTask(String filePath,boolean firstLineIgnore,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
				
		try{
			String extension  = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());	
			
			Map<String,Object>   map =   fileService.previewExcelFile(java.net.URLDecoder.decode(filePath,"UTF-8"),10,extension.toLowerCase());
			if(firstLineIgnore){
				model.put("firstLineIgnore", "true");	
			}else{
				model.put("firstLineIgnore", "false");
			}		
			model.put("list", map.get("list"));
			model.put("cellTotalMax", map.get("cellTotalMax"));
			model.put("filePath", java.net.URLDecoder.decode(filePath,"UTF-8"));
			model.put("fileName", getFileName(filePath));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		

		return "task/task_view_excel";
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
	public String v_taskBySameRule(String preFilePath,String fileCode,ModelMap model,HttpServletRequest request,HttpServletResponse response)throws ReadFileException {

		if(StringUtils.isNotBlank(preFilePath)){
			File file = new File(preFilePath);
			if(file.exists()){
				Task task = taskService.getTaskByFilePath(preFilePath);
				if(task!=null){				
					File[] childList = folderService.getChildByLastModified(file.lastModified(), preFilePath.substring(0, preFilePath.lastIndexOf("\\")));
					if(childList!=null&&childList.length>1){
						String thisFilePath = childList[1].getPath();
						if(!new File(thisFilePath).isDirectory()){
							
							model.put("task", task);
							String extension  = thisFilePath.substring(thisFilePath.lastIndexOf(".")+1, thisFilePath.length());
							if("txt".equals(extension.toLowerCase())){
								List<String> list = fileService.previewTxtFile(thisFilePath,fileCode);
								String[] lines = new String[list.size()]; 
								for(int i=0;i<list.size();i++){
									lines[i] = list.get(i);
								}
								return v_task(thisFilePath,task.getSeparator(),fileCode,task.getFirstLineIgnore(),
										lines,model,request,response);
							}else if("xlsx".equals(extension.toLowerCase())||"xls".equals(extension.toLowerCase())){						
								return v_excelTask(thisFilePath,task.getFirstLineIgnore(),model,request,response);
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
			
			
			
			
		}else{
			return null;
		}
	}	

	@RequestMapping(value = "o_task.htm")
	public String o_task(Task task,ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		task.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
		task.setCreateUser(createUser);
		task.setCreateTime(nowTime);		
		if("0000-00-00 00:00:00".equals(task.getRunTime()))//立即执行
		{	
			task.setStartDate(nowTime);
			task.setRunTime(nowTime);
			task.setTaskStatus(1);//执行中
			task.setTotalCount(1l);
			//boolean result = taskService.createTask(task);//创建任务
			boolean result = taskService.createTask(task,false);//立即执行也放到task表中
			
			if(result){
				int[] successAndFailNum = fileService.saveFileToMongo(task);
				if(successAndFailNum[0]!=-1){
					if(successAndFailNum[1]>0){
						ResponseUtils.renderJson(response, "{\"code\":200,\"msg\":\"创建任务并且入库成功，本次任务共导入【  "+successAndFailNum[0]+" 】条数据,【  "+successAndFailNum[1]+" 】条数据与设置列数不符，未导入\"}");
					}else{
						ResponseUtils.renderJson(response, "{\"code\":200,\"msg\":\"创建任务并且入库成功，本次任务共导入【  "+successAndFailNum[0]+"  】条数据\"}");
					}
					
					//导入完成，删除taskInfo表中的数据
					taskService.deleteByUid(task.getUid());
				}else{
					ResponseUtils.renderJson(response, "{\"code\":500,\"taskId\":\""+task.getUid()+"\",\"msg\":\"创建任务成功,但是导入数据失败\"}");
				}			
			}else{
				taskService.taskUpdate(task, null,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),-2);
				ResponseUtils.renderJson(response, "{\"code\":500,\"taskId\":\""+task.getUid()+"\",\"msg\":\"创建任务失败\"}");
			}			
		}else{//创建任务
			task.setTaskStatus(0);
			taskService.createTask(task,false);//创建任务
			ResponseUtils.renderJson(response, "{\"code\":200,\"msg\":\"创建任务成功\"}");
		}		
		return null;
	}
	
	@RequestMapping(value = "percent.htm")
	public void getPercent(String filePath,HttpServletResponse response) {	
		String percent ="0";		
		try{
			percent = taskService.getPercent(java.net.URLDecoder.decode(filePath,"UTF-8"));
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		ResponseUtils.renderJson(response, "{\"code\":200,\"msg\":"+percent+"}");
	}
	
	
	public static String getFileName(String filePath){
		try{
			filePath=java.net.URLDecoder.decode(filePath,"UTF-8");
			
			int lastSlashIndex=filePath.lastIndexOf(File.separatorChar);				
			
			int lastDotIndex=filePath.lastIndexOf('.');
			String fileName=filePath.substring(lastSlashIndex+1, lastDotIndex);
			
			if(fileName!=null){
				return fileName;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
		
	@Autowired
	private FileService fileService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FolderService folderService;
}
