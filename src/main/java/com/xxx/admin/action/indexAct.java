package com.xxx.admin.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.CommandResult;
import com.xxx.admin.bean.Folder;
import com.xxx.admin.data.mongo.TaskRepository;
import com.xxx.admin.service.FolderService;
import com.xxx.elasticsearch.data.mongo.SolrTaskRepository;



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
		 long totalCount= taskRepository.getFileTotalCount();
		 long notImportSolrCout=solrTaskRepository.getObjectsByStatus(888, "solrTaskInfo");
		 String jsonReturn = "{\"collections\":"+0+",\"storageSize\":"+0+",\"totalCount\":"+0+",\"notImportFileCout\":"+0+",\"notImportSolrCout\":"+0+",\"totalFile\":"+0+",\"totalFileLength\":"+0+"}";
		 long notImportFileCount = 0l;
		 long totalFile = 0l;
		 long totalFileLength=0l;
		 
	 if (!System.getProperty("os.name").contains("Windows")) {
		 Long notImportFileCountSession = null;	
		 Long totalFileCountSession = null;	
		 Long totalFileLengthSession = null;	
		 Cookie[] cookies = request.getCookies();
		 if(cookies!=null){
			 for(Cookie cookie : cookies){
				 if("notImportFileCout".equals(cookie.getName())){
					 notImportFileCountSession = Long.valueOf(cookie.getValue());
				 }else if("totalFile".equals(cookie.getName())){
					 totalFileCountSession = Long.valueOf(cookie.getValue());
				 }else if("totalFileLength".equals(cookie.getName())){
					 totalFileLengthSession = Long.valueOf(cookie.getValue());
				 }
			 }
		 }

		 if(notImportFileCountSession!=null&&totalFileCountSession!=null&&totalFileLengthSession!=null){
			 notImportFileCount = notImportFileCountSession;
			 totalFile = totalFileCountSession;
			 totalFileLength = totalFileLengthSession;
		 }else{//重新计算
			     //计算设置的文件夹下有多少文件
				 List<Folder> folderList = null;
				 try{
					 folderList = folderService.list();
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
								
				 Map<String,String> map = new HashMap();
				 if(folderList!=null){				
					 boolean canCount = true;
					 for(Folder folder:folderList){					 
						 for (String v : map.values()){//即使做了判断，如果先统计的子目录，再统计父目录，还是会存在重复计算的问题。
							 if(v.indexOf(folder.getFolderPath())>=0){							 
								 canCount = false;
								 break;
							 }
						 }					 
						 if(canCount){
							 totalFile=totalFile+getFileCountByFolderPath(folder.getFolderPath(),"txt");
							 System.out.println("totalFile: "+totalFile+" folder.getFolderPath(): "+folder.getFolderPath());							 
							 totalFileLength = totalFileLength+getFileLengthByFolderPath(folder.getFolderPath());
							 System.out.println("totalFileLength: "+totalFileLength+" folder.getFolderPath(): "+folder.getFolderPath());	
							 map.put(folder.getFolderId(), folder.getFolderPath());
						 }
						 
					 }
				 }	
	            if(totalFile>totalCount){
	            	notImportFileCount = totalFile - totalCount;	            	
	            	Cookie cookie = new Cookie("notImportFileCout",String.valueOf(notImportFileCount));	            	
	            	cookie.setMaxAge(43200);//12小时	            	 
	            	cookie.setPath("/");
	            	response.addCookie(cookie);
	            	
	            	Cookie cookie2 = new Cookie("totalFile",String.valueOf(totalFile));	            	
	            	cookie2.setMaxAge(43200);//12小时	            	 
	            	cookie2.setPath("/");
	            	response.addCookie(cookie2);
	            	
	            	Cookie cookie3 = new Cookie("totalFileLength",String.valueOf(totalFileLength));	            	
	            	cookie3.setMaxAge(43200);//12小时	            	 
	            	cookie3.setPath("/");
	            	response.addCookie(cookie3);
	            }else{         	
	            	Cookie cookie = new Cookie("notImportFileCout","0");	            	
	            	cookie.setMaxAge(3600);//1小时	            	 
	            	cookie.setPath("/");
	            	response.addCookie(cookie);
	            	
	            	Cookie cookie2 = new Cookie("totalFile","0");	            	
	            	cookie2.setMaxAge(3600);//1小时	            	 
	            	cookie2.setPath("/");
	            	response.addCookie(cookie2);
	            	
	            	Cookie cookie3 = new Cookie("totalFileLength","0");	            	
	            	cookie3.setMaxAge(3600);//1小时	            	 
	            	cookie3.setPath("/");
	            	response.addCookie(cookie3);
	            }

		 }
	 }else{
		 //windows环境 不计算			 
	 } 	 
		
		 if(commandResult!=null){			
			 jsonReturn = "{\"collections\":"+commandResult.get("collections")+",\"storageSize\":"+commandResult.get("storageSize")
			 				+",\"totalCount\":"+totalCount+",\"notImportFileCout\":"+notImportFileCount+",\"notImportSolrCout\":"+notImportSolrCout+",\"totalFile\":"+totalFile+",\"totalFileLength\":"+totalFileLength+"}";	 
		 }			
//		 request.getSession().setAttribute("indexAct_index", jsonReturn);
//		 request.getSession().setMaxInactiveInterval(600);//缓存10分钟
	     return jsonReturn;		
	}
	
	@RequestMapping(value = "es_status.htm")
	public @ResponseBody
	String elasticSearchStatus(HttpServletRequest request,HttpServletResponse response) throws MalformedURLException, IOException, Exception {	
		String jsonReturn = "{\"collections\":"+0+",\"storageSize\":"+0+",\"totalCount\":"+0+"}";
		 
		String url="http://localhost:9200/_status";
		InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	        sb.append((char) cp);
	      }
	      
	      JSONObject jsonobj = new JSONObject(sb.toString());
	      
	      jsonobj = new JSONObject(jsonobj.get("indices").toString());
	      long indicesTotal=0;
	      long docsTotal=0;
	      long sizeTotal=0;
	        Map result = new HashMap();
	        Iterator iterator = jsonobj.keys();
	        String key = null;
	        JSONObject childObj = null;
	        
	        while (iterator.hasNext()) {
	        	indicesTotal++;
	            key = (String) iterator.next();
	            childObj = (JSONObject) jsonobj.get(key);
	            
	            JSONObject docsObj = (JSONObject) childObj.get("docs");
	            
	            Long num_docs  = docsObj.getLong("num_docs");
	            docsTotal+=num_docs;
	            
	            JSONObject indexObj = (JSONObject) childObj.get("index");
	            long  size_in_bytes = indexObj.getLong("primary_size_in_bytes");
	            sizeTotal+=size_in_bytes;
	            
	            System.out.println("key:"+key+":"+num_docs+" docsTotal:"+docsTotal);
	            
	        }
	        jsonReturn = "{\"collections\":"+indicesTotal+",\"storageSize\":"+sizeTotal+",\"totalCount\":"+docsTotal+"}";
			 
	    } finally {
	      try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
		 
	    return jsonReturn;
		
	}
	
	private long getFileCountByFolderPath(String folderPath,String fileSuffix){
			
		String command="ls -lR "+folderPath+"|grep "+fileSuffix+"|wc -l";	
		Runtime runtime=Runtime.getRuntime();
		String temp;
		long count=0l;
		try{
			String[] cmdA = { "/bin/sh", "-c", command };
			Process process  =  runtime.exec(cmdA);			 
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));	
			//BufferedReader brError = new BufferedReader(new InputStreamReader(process.getErrorStream()));	
			temp = br.readLine();					
			System.out.println("getFileCountByFolderPath Command:  "+command+" temp:  "+temp);
			if(StringUtils.isNotBlank(temp)){				
				count = Long.valueOf(temp);	
			}			
		br.close();
		return count;
	}catch(Exception e){
		e.printStackTrace();	
		return count;
	}
}
	
	private long getFileLengthByFolderPath(String folderPath){
		
		String command="du -sb "+folderPath;	
		Runtime runtime=Runtime.getRuntime();
		String temp;
		long count=0l;
		try{
			String[] cmdA = { "/bin/sh", "-c", command };
			Process process  =  runtime.exec(cmdA);			 
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));	
			//BufferedReader brError = new BufferedReader(new InputStreamReader(process.getErrorStream()));	
			temp = br.readLine();					
			System.out.println("getFileLengthByFolderPath Command:  "+command+" temp:  "+temp);
			if(StringUtils.isNotBlank(temp)){				
				count = Long.valueOf(temp.replace(folderPath, "").trim());	
			}			
		br.close();
		return count;
	}catch(Exception e){
		e.printStackTrace();	
		return count;
	}
}
	
	public static void main(String[] args) throws Exception, IOException {
		String url="http://localhost:9200/_status";
		InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	        sb.append((char) cp);
	      }
	      
	      JSONObject jsonobj = new JSONObject(sb.toString());
	      
	      jsonobj = new JSONObject(jsonobj.get("indices").toString());
	      long indicesTotal=0;
	      long docsTotal=0;
	      long sizeTotal=0;
	        Map result = new HashMap();
	        Iterator iterator = jsonobj.keys();
	        String key = null;
	        JSONObject childObj = null;
	        
	        while (iterator.hasNext()) {
	        	indicesTotal++;
	            key = (String) iterator.next();
	            childObj = (JSONObject) jsonobj.get(key);
	            
	            JSONObject docsObj = (JSONObject) childObj.get("docs");
	            
	            Long num_docs  = docsObj.getLong("num_docs");
	            docsTotal+=num_docs;
	            
	            JSONObject indexObj = (JSONObject) childObj.get("index");
	            long  size_in_bytes = indexObj.getLong("primary_size_in_bytes");
	            sizeTotal+=size_in_bytes;
	            
	            System.out.println("key:"+key+":"+num_docs+" docsTotal:"+docsTotal);
	            
	        }
	      
	      
	        String jsonReturn = "{\"collections\":"+indicesTotal+",\"storageSize\":"+sizeTotal+",\"totalCount\":"+docsTotal+"}";
			 
	      
	      
	    } finally {
	      try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	}
	
		
    @Resource(name = "task")
    TaskRepository taskRepository;
    @Resource(name = "solrTask")
    SolrTaskRepository solrTaskRepository;
	@Autowired
	private FolderService folderService;
}
