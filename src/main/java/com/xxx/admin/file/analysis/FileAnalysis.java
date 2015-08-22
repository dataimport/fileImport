package com.xxx.admin.file.analysis;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.xxx.admin.data.base.BaseData;

@Component("fileAnalysis")
public class FileAnalysis extends BaseData {

	public static final int BUFFER_SIZE = 0x300000;//缓冲区大小为3M
	
	public Object parse(String filePath){
		
		List<String> lines = ReadFileByLine(filePath);//获取所有行
//		String[] columns = getColumn(lines,separator);//获取字段
//		if(lines.size()>0){
//			lines.remove(0);//移除首行
//		}		
//		MongoDao md = new MongoDaoImpl();
//		
//		for(String line:lines){
//			try{
//				md.add("userInfo", columns, line.split(separator));
//			}catch(Exception ex){
//				ex.printStackTrace();
//			}
//		}
		return lines;
	}
	
	public String getFileInfo(String filePath,String separator){
		
		List<String> lines =  (List<String>)parse(filePath);//获取所有行
		if(lines.size()>0){
			String[] columns = getColumn(lines,separator);//获取字段			
			try{
				File fin = new File(filePath);			
				JSONObject fileInfo = new JSONObject();
				fileInfo.put("fileName", fin.getName());
				fileInfo.put("fileSize", fin.length());
				fileInfo.put("filePath", fin.getPath());			
				JSONObject object = new JSONObject();			
				JSONObject columnInfo = new JSONObject();
				int columnSize = columns.length;
				for(int i=0;i<columnSize;i++){
					columnInfo.put(String.valueOf(i), columns[i].trim());
				}
				
				object.put("fileInfo", fileInfo);
				object.put("columnInfo", columnInfo);
				System.out.println(object.toString());
				return object.toString();
			}catch(JSONException ex){
				ex.printStackTrace();
			}	
		}else{
			return null;
		}
		return null;
	}
	
	public Map getContentBySeparator(String filePath,String separator){
		List<String> lines = ReadFileByLine(filePath);//获取所有行
		String[] columns = getColumn(lines,separator);//获取表头字段,不管有没有
		Map map = new HashMap();
		map.put("text", lines);
		map.put("columns", columns);
		return map;
}
	

	
//	public String createTask(String filePath,String separator,String[] columns,String[] rowNum,String mongoCollectionName){
//			MongoDao md = new MongoDaoImpl();	
//			try{
//			//	md.add("taskInfo", columns, line.split(separator));
//			}catch(Exception ex){
//				ex.printStackTrace();
//			}
//			return "";
//	}
	
	private void ReadBigFileByLine(File fin,String separator) throws FileNotFoundException{
		FileChannel fcin = new RandomAccessFile(fin, "r").getChannel(); 
		ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER_SIZE); 
		
		String enterStr = "\n"; 
        try{ 
	        byte[] bs = new byte[BUFFER_SIZE]; 
	        
	        long start = System.currentTimeMillis();
	        
	        StringBuffer strBuf = new StringBuffer(""); 
	        while(fcin.read(rBuffer) != -1){ 
	              int rSize = rBuffer.position(); 
	              rBuffer.rewind(); 
	              rBuffer.get(bs); 
	              rBuffer.clear(); 
	              String tempString = new String(bs, 0, rSize); 
	 
	              int fromIndex = 0; 
	              int endIndex = 0; 
	              while((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1){ 
	               String line = tempString.substring(fromIndex, endIndex); 
	               line = new String(strBuf.toString() + line); 
	               System.out.print(line); 
	           
	               strBuf.delete(0, strBuf.length()); 
	               fromIndex = endIndex + 1; 
	              } 
	              if(rSize > tempString.length()){ 
	            	  strBuf.append(tempString.substring(fromIndex, tempString.length())); 
	              }else{ 
	            	  strBuf.append(tempString.substring(fromIndex, rSize)); 
	              }   
	        	} 
	        	long end = System.currentTimeMillis();	 
		        System.out.println("读取文件文件内容花费：" + (end - start) + "毫秒");
        } catch (IOException e) { 
        // TODO Auto-generated catch block 
        e.printStackTrace(); 
        } 
	}
	
	
	private  List<String> ReadFileByLine(String filePath){		  
		try {  
			long start = System.currentTimeMillis();
            List<String> lines = Files.readAllLines(Paths.get(filePath),  
                    Charset.defaultCharset());  
            long end = System.currentTimeMillis();	 
	        //System.out.println("读取文件文件内容花费：" + (end - start) + "毫秒");
	        return lines;
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		return new ArrayList<String>();
	}
	
	private String[] getColumn(List<String> lines,String separator){
		 if(lines.size()>0){
			 return lines.get(0).split(separator);
		 }else{
			 return null;
		 }	 
	}
	
	private String Read(File file) throws IOException{
			
        /**
         * map(FileChannel.MapMode mode,long position, long size) mode -
         * 根据是按只读、读取/写入或专用（写入时拷贝）来映射文件，分别为 FileChannel.MapMode 类中所定义的
         * READ_ONLY、READ_WRITE 或 PRIVATE 之一 position - 文件中的位置，映射区域从此位置开始；必须为非负数
         * size - 要映射的区域大小；必须为非负数且不大于 Integer.MAX_VALUE
         * 所以若想读取文件后半部分内容，如例子所写；若想读取文本后1
         * /8内容，需要这样写map(FileChannel.MapMode.READ_ONLY,
         * f.length()*7/8,f.length()/8)
         * 想读取文件所有内容，需要这样写map(FileChannel.MapMode.READ_ONLY, 0,f.length())
         */
	RandomAccessFile raf = null;
	try{
			raf = new RandomAccessFile(file, "r");        
	       	MappedByteBuffer inputBuffer = raf.getChannel().map(FileChannel.MapMode.READ_ONLY,0, file.length());
	     
	       	byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容
	 
	        long start = System.currentTimeMillis();
	 
	        for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {
	 
	            if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {
	 
	                for (int i = 0; i < BUFFER_SIZE; i++)
	 
	                    dst[i] = inputBuffer.get(offset + i);
	 
	            } else {
	 
	                for (int i = 0; i < inputBuffer.capacity() - offset; i++)
	 
	                    dst[i] = inputBuffer.get(offset + i);
	 
	            }
	 
	            int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE : inputBuffer.capacity() % BUFFER_SIZE;
	         
	            //System.out.println(new String(dst, 0, length));
	            // new String(dst,0,length)这样可以取出缓存保存的字符串，可以对其进行操作 
        }

	       long end = System.currentTimeMillis();	 
	        System.out.println("读取文件文件内容花费：" + (end - start) + "毫秒");
	        
	 }catch(Exception ex){
     	ex.printStackTrace();
     	throw ex;
     }finally{
    	 if(raf!=null){
    		 raf.close();
    	 }    	 
     }        
        return null;
	}
	
	public static void main(String[] args){
		FileAnalysis td = new FileAnalysis();
		String filePath = "F:\\work2space\\测试数据\\test.txt";
		String separator ="##";
		//td.Parse("F:\\work2space\\测试数据\\test.txt", "##");
		
		td.getFileInfo(filePath, separator);//读文件 获取文件内容，获取列数， 获取第一行，返回头字段
		
		//td.createTask(filePath, separator, rowNum, mongoCollectionName);//创建清洗任务
				
	}
}
