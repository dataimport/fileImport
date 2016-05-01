package com.xxx.admin.file.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xxx.core.exception.BaseException;
import com.xxx.core.exception.ReadFileException;
import com.xxx.utils.FileCharsetDetector;


@Component("fileAnalysis")
public class TxtFileAnalysis  {
	private static final Logger log = LoggerFactory.getLogger(TxtFileAnalysis.class);
	
	public static final int BUFFER_SIZE = 0x300000;//缓冲区大小为3M
	
	public static final int DEFAULT_LINENUM = 10;//默认预览返回的行数
	
	public static final int MAX_LINENUM = 1000;//最大能预览的行数
	
	public static final int FILESIZE_TIPPINGPOINT = 20;//文件算大文件还是小文件区分临界值 20M
	
	
	/**
	 * 根据文件分隔符预览文件
	 * @param filePath
	 * @param separator
	 * @return
	 */
	public Map previewFileBySeparator(String filePath,String fileCode,String separator,Integer lineNum)throws ReadFileException{
		List<String> lines = previewFileByLineNum(filePath,fileCode,lineNum);//预览指定行数
		String[] columns = getColumns(lines,separator);//获取表头字段,不管有没有
		Map map = new HashMap();
		map.put("text", lines);
		map.put("columns", columns);
		return map;
	}
	
	/**
	 * 预览文件，默认返回10条记录
	 * @param filePath
	 * @return
	 */
	public  List<String> previewFileByLineNum(String filePath,String fileCode)throws ReadFileException{	
		return previewFileByLineNum(filePath,fileCode,null);
	}
	
	/**
	 * 预览文件，返回指定行数，用于创建入库任务时使用
	 * @param filePath  文件路径
	 * @param viewNum   预览的文件行数
	 * @return
	 */
	public  List<String> previewFileByLineNum(String filePath,String fileCode,Integer viewNum) throws ReadFileException{    
		  
		 	   File file = new File(filePath);
               if(file.exists()){
            	String charSet = getCharset(filePath,fileCode);
            	   if (viewNum==null || viewNum > MAX_LINENUM) {
					   viewNum = DEFAULT_LINENUM;
				   }
            	   if(file.length()/1048576>FILESIZE_TIPPINGPOINT){//大文件
            		  log.debug("按大文件解析");
            		  try {   
            			  return  previewBigFileByLineNum(file,viewNum,charSet);
            		  }catch (ReadFileException e) {  
           		       e.printStackTrace(); 
           		       throw e;
           		   }  	
            	   }else{//小文件
            		  log.debug("按小文件解析");
            		  List<String> lines = readSmallFile(filePath,charSet);
            		  if(lines.size()>viewNum){
            			  return lines.subList(0, viewNum);
            		  } 		  
            		  return lines;
            	   }
               }else{//文件不存在
            	throw new ReadFileException(BaseException.PREVIEW_FILE_CODE,"文件不存在");
               }             
		 	  
		}
	
	
	/**
	* 按行读小文件所有内容
	* @param filePath
	* @param viewNum
	* @return
	*/
	public List<String> readSmallFile(String filePath,String charset)throws ReadFileException {
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath),Charset.forName(charset));			
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadFileException(BaseException.READSMALL_FILE_CODE,e.getMessage());
		}
	}
	
	/**
	* 按缓冲区读大文件所有内容
	* @param filePath
	* @param viewNum
	* @return
	*/
	public List<String> LoopBigFileByBuffer(int fromIndex, int endIndex, int rSize,
			byte[] bs,String enterStr,String line,StringBuffer strBuf,
			FileChannel fcin,ByteBuffer rBuffer,List<String> lines,String charset)
			throws ReadFileException {
		  try{
				rSize = rBuffer.position();
				rBuffer.rewind();
				rBuffer.get(bs);
				rBuffer.clear();				
				String tempString = new String(bs, 0, rSize,charset);
				fromIndex = 0;
				endIndex = 0;
				while((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {					
					line = tempString.substring(fromIndex, endIndex);
					line = new String(strBuf.toString() + line);
					strBuf.delete(0, strBuf.length());
					fromIndex = endIndex + 1;

					lines.add(line);
				}
				if (rSize > tempString.length()) {
					strBuf.append(tempString.substring(fromIndex,
							tempString.length()));
				} else {
					strBuf.append(tempString.substring(fromIndex, rSize));
				}	
				return lines;	 
		  }catch(Exception ex){
			  ex.printStackTrace();
			  throw new ReadFileException(BaseException.READBIG_FILE_CODE,ex.getMessage());
		  }
	
	}
	
    /**
     * 预览大文件
     * @param file
     * @param resultLineNum
     * @return
     * @throws FileNotFoundException
     */
	private List<String> previewBigFileByLineNum(File file, Integer viewNum,String charSet)	throws ReadFileException {
		try {
			FileChannel fcin = new RandomAccessFile(file, "r").getChannel();
			ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER_SIZE);
			String enterStr = "\n"; // 换行符        
			//long start = System.currentTimeMillis();		
			byte[] bs = new byte[BUFFER_SIZE];
			int i = 0, fromIndex, endIndex, rSize;
			StringBuffer strBuf = new StringBuffer("");
			String line = "";
			boolean continueGo = true;
			List<String> lines = new ArrayList<String>();
			while (fcin.read(rBuffer) != -1 && continueGo) {
				rSize = rBuffer.position();
				rBuffer.rewind();
				rBuffer.get(bs);
				rBuffer.clear();
				String tempString = new String(bs, 0, rSize,charSet);
				fromIndex = 0;
				endIndex = 0;
				while ((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {
					if (i >= viewNum) {
						continueGo = false;
						break;
					}
					line = tempString.substring(fromIndex, endIndex);
					line = new String(strBuf.toString() + line);
					strBuf.delete(0, strBuf.length());
					fromIndex = endIndex + 1;
					i++;
					lines.add(line);
				}
				if (rSize > tempString.length()) {
					strBuf.append(tempString.substring(fromIndex,
							tempString.length()));
				} else {
					strBuf.append(tempString.substring(fromIndex, rSize));
				}
			}
			
//        	long end = System.currentTimeMillis();	 
//	        System.out.println("预览大文件耗时：" + (end - start) + "毫秒");
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
			throw new ReadFileException(BaseException.PREVIEW_FILE_CODE,e.getMessage());
		}
	}

	
	
	/**
	 * 读取大文件的行数
	 * @param filePath 文件路径
	 * @throws FileNotFoundException
	 */
	public int getBigFileLineNum(String filePath) throws FileNotFoundException{
		File file = new File (filePath);
		if(file.exists()){
			FileChannel fcin = new RandomAccessFile(file, "r").getChannel(); 
			ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER_SIZE); 
			
			String enterStr = "\n"; //换行符
	        try{ 
		        byte[] bs = new byte[BUFFER_SIZE]; 
		        
		        long start = System.currentTimeMillis();
		        int i = 0,fromIndex=0,endIndex=0,rSize=0;
		        StringBuffer strBuf = new StringBuffer(""); 
		   
		        while(fcin.read(rBuffer) != -1){		        
		              rSize = rBuffer.position(); 
		              rBuffer.rewind(); 
		              rBuffer.get(bs); 
		              rBuffer.clear(); 
		              String tempString = new String(bs, 0, rSize); 
		             // System.out.println("##### "+tempString); 
		              fromIndex = 0; 
		              endIndex = 0; 
		              while((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1){ 	              
		               strBuf.delete(0, strBuf.length()); 
		               fromIndex = endIndex + 1; 
		               i++;
		              } 
		              if(rSize > tempString.length()){ 
		            	  strBuf.append(tempString.substring(fromIndex, tempString.length())); 
		              }else{ 
		            	  strBuf.append(tempString.substring(fromIndex, rSize)); 
		              }   
		            
		        	} 
		        	long end = System.currentTimeMillis();	 
			        System.out.println("读取文件内容耗时：" + (end - start) + "毫秒");
			        return i;
	        } catch (IOException e) { 	       
		        e.printStackTrace(); 
		        return 1;
	        } 
		}else{
			return 1;
		}
		
	}
	
	
	private String getBigFileLineNumByWindowsCommand(String filePath){
		String command = "find /V \"\" /C "+filePath;			
		Runtime runtime=Runtime.getRuntime();
		String temp;
		String number="1";
		try{
			Process process  = runtime.exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));		
			while ((temp = br.readLine()) != null) {
				if(StringUtils.isNotBlank(temp)){					
					//System.out.println(" result : " + temp.substring(temp.lastIndexOf(":")+1).trim());
					number = temp.substring(temp.lastIndexOf(":")+1).trim();					
					break;
				}				
			}
		}catch(Exception e){
			e.printStackTrace();	
			return "1";
		}
        return number;
	}

	
	/**
	 * 用命令读取大文件的行数   linux下非常快
	 * @param file
	 * @throws FileNotFoundException
	 */
	public String getBigFileLineNumByCommand(String filePath,boolean firstLineIgnore){
		//long start = System.currentTimeMillis();
		if (System.getProperty("os.name").contains("Windows")) {
			return getBigFileLineNumByWindowsCommand(filePath);
		} else {
			String command="cat "+filePath+" |wc -l";			
			Runtime runtime=Runtime.getRuntime();
			String temp;
			String number="1";
			try{
				String[] cmdA = { "/bin/sh", "-c", command };
				Process process  =  runtime.exec(cmdA);			 
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));	
				//BufferedReader brError = new BufferedReader(new InputStreamReader(process.getErrorStream()));	
				temp = br.readLine();		
				if(StringUtils.isNotBlank(temp)){				
					number = temp;	
				}			
			br.close();
		}catch(Exception e){
			e.printStackTrace();	
			return "1";
		}
			//long end = System.currentTimeMillis();	 
			//System.out.println("读取文件行数耗时：" + (end - start) + "毫秒");
		 if(firstLineIgnore&&Integer.valueOf(number)>1){
			 return String.valueOf(Integer.valueOf(number)-1);
		 }else{
			 return number;
		 }
		
		}       
	}

	public String getCharset(String filePath,String fileCode){
		if(StringUtils.isNotBlank(fileCode)){
			return fileCode;
		}else{
			try{
				return new FileCharsetDetector().guestFileEncoding(filePath);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return "UTF-8";
		}
		
	}
	
	private String[] getColumns(List<String> lines,String separator){
		 if(lines.size()>0){
			 return lines.get(0).split(separator);
		 }else{
			 return null;
		 }	 
	}

}
