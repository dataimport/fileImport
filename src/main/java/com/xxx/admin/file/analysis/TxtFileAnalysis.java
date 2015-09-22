package com.xxx.admin.file.analysis;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


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
	public Map previewFileBySeparator(String filePath,String separator,Integer lineNum){
		List<String> lines = previewFileByLineNum(filePath,lineNum);//预览指定行数
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
	public  List<String> previewFileByLineNum(String filePath){	
		return previewFileByLineNum(filePath,null);
	}
	
	/**
	 * 预览文件，返回指定行数，用于创建入库任务时使用
	 * @param filePath  文件路径
	 * @param viewNum   预览的文件行数
	 * @return
	 */
	public  List<String> previewFileByLineNum(String filePath,Integer viewNum){		      
		  try {  
               File file = new File(filePath);
               if(file.exists()){
            	   if (viewNum==null || viewNum > MAX_LINENUM) {
					   viewNum = DEFAULT_LINENUM;
				   }
            	   if(file.length()/1048576>FILESIZE_TIPPINGPOINT){//大文件
            		  log.debug("按大文件解析");
            		  return  previewBigFileByLineNum(file,viewNum);
            	   }else{//小文件
            		  log.debug("按小文件解析");
            		  List<String> lines = readSmallFile(filePath);
            		  if(lines.size()>viewNum){
            			  return lines.subList(0, viewNum);
            		  } 		  
            		  return lines;
            	   }
               }else{//文件不存在
            	   return new ArrayList<String>();   
               }             
		   } catch (IOException e) {  
		       e.printStackTrace();  
		   }  
		   return new ArrayList<String>();
		}
	
	
	/**
	* 按行读小文件所有内容
	* @param filePath
	* @param viewNum
	* @return
	*/
	public List<String> readSmallFile(String filePath)
			throws IOException {
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath),Charset.forName(getCharset(filePath)));			
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
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
			throws IOException {

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
	}
	
    /**
     * 预览大文件
     * @param file
     * @param resultLineNum
     * @return
     * @throws FileNotFoundException
     */
	private List<String> previewBigFileByLineNum(File file, Integer viewNum)
			throws IOException {
		FileChannel fcin = new RandomAccessFile(file, "r").getChannel();
		ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		String enterStr = "\n"; // 换行符
		try {
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
				String tempString = new String(bs, 0, rSize,getCharset(file.getPath()));
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
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	
	
	/**
	 * 返回指定行数的大文件
	 * @param file
	 * @param resultLineNum
	 * @throws FileNotFoundException
	 */
	private void ReadBigFileByLine(File file,Integer resultLineNum) throws FileNotFoundException{
		FileChannel fcin = new RandomAccessFile(file, "r").getChannel(); 
		ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER_SIZE); 
		
		String enterStr = "\n"; //换行符
        try{ 
	        byte[] bs = new byte[BUFFER_SIZE]; 
	        
	        long start = System.currentTimeMillis();
	        int i = 0,fromIndex=0,endIndex=0,rSize=0;
	        StringBuffer strBuf = new StringBuffer(""); 
	        String line ="";
	        boolean continueGo = true;
	        
	        while(fcin.read(rBuffer) != -1&&continueGo){		        
	              rSize = rBuffer.position(); 
	              rBuffer.rewind(); 
	              rBuffer.get(bs); 
	              rBuffer.clear(); 
	              String tempString = new String(bs, 0, rSize); 
	             // System.out.println("##### "+tempString); 
	              fromIndex = 0; 
	              endIndex = 0; 
	              while((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1){ 
	               if(i>=resultLineNum){
	            	   continueGo = false;
	            	   break;
	               }
	               line = tempString.substring(fromIndex, endIndex); 
	               line = new String(strBuf.toString() + line); 
	               System.out.println("line ###### "+line); 
	           
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
		        System.out.println("读取文件文件内容花费：" + (end - start) + "毫秒");
        } catch (IOException e) { 
        // TODO Auto-generated catch block 
        e.printStackTrace(); 
        } 
	}
	
	public String getCharset(String filePath) throws IOException{  
        
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filePath));    
        int p = (bin.read() << 8) + bin.read();    
          
        System.out.println(p);
        String code = null;    
          
        switch (p) {    
        	case 58791:    
        		code = "UTF-8";      
            	break;	
            case 0xefbb:    
                code = "UTF-8";    
                break;    
            case 0xfffe:    
                code = "Unicode";    
                break;    
            case 0xfeff:    
                code = "UTF-16BE";    
                break;    
            default:    
                code = "GBK";    
        }    
        return code;  
}  
	
//	private  BufferToLine(FileChannel fcin){
//        while(fcin.read(rBuffer) != -1&&continueGo){		        
//            rSize = rBuffer.position(); 
//            rBuffer.rewind(); 
//            rBuffer.get(bs); 
//            rBuffer.clear(); 
//            String tempString = new String(bs, 0, rSize); 
//           // System.out.println("##### "+tempString); 
//            fromIndex = 0; 
//            endIndex = 0; 
//            while((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1){ 
//             if(i>=resultLineNum){
//          	   continueGo = false;
//          	   break;
//             }
//             line = tempString.substring(fromIndex, endIndex); 
//             line = new String(strBuf.toString() + line); 
//             System.out.println("line ###### "+line); 
//         
//             strBuf.delete(0, strBuf.length()); 
//             fromIndex = endIndex + 1; 
//             i++;
//            } 
//            if(rSize > tempString.length()){ 
//          	  strBuf.append(tempString.substring(fromIndex, tempString.length())); 
//            }else{ 
//          	  strBuf.append(tempString.substring(fromIndex, rSize)); 
//            }   
//          
//      	} 
//	}
		
	private String[] getColumns(List<String> lines,String separator){
		 if(lines.size()>0){
			 return lines.get(0).split(separator);
		 }else{
			 return null;
		 }	 
	}
		
	public static void main(String[] args){
		TxtFileAnalysis td = new TxtFileAnalysis();
		int resultLineNum = 5;
		String filePath = "F:\\work2space\\testdata\\test2.txt";
		String separator ="##";
		try{
			//td.ReadBigFileByLine(new File(filePath),resultLineNum);		
			 List<String> lines = td.previewBigFileByLineNum(new File(filePath), 10);
			 for(String s:lines){
				 System.out.println(s);
			 }
			
		}catch(Exception ex){
			ex.printStackTrace();
		}

		//td.Parse("F:\\work2space\\测试数据\\test.txt", "##");
		
		//td.getFileInfo(filePath, separator);//读文件 获取文件内容，获取列数， 获取第一行，返回头字段
		
		//td.createTask(filePath, separator, rowNum, mongoCollectionName);//创建清洗任务
				
	}
	
	
	
/*	private String Read(File file) throws IOException{
		
        *//**
         * map(FileChannel.MapMode mode,long position, long size) mode -
         * 根据是按只读、读取/写入或专用（写入时拷贝）来映射文件，分别为 FileChannel.MapMode 类中所定义的
         * READ_ONLY、READ_WRITE 或 PRIVATE 之一 position - 文件中的位置，映射区域从此位置开始；必须为非负数
         * size - 要映射的区域大小；必须为非负数且不大于 Integer.MAX_VALUE
         * 所以若想读取文件后半部分内容，如例子所写；若想读取文本后1
         * /8内容，需要这样写map(FileChannel.MapMode.READ_ONLY,
         * f.length()*7/8,f.length()/8)
         * 想读取文件所有内容，需要这样写map(FileChannel.MapMode.READ_ONLY, 0,f.length())
         *//*
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
	         
	            System.out.println(new String(dst, 0, length));
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
	}*/
	
	
/*	public String getFileInfo(String filePath,String separator){
		
		List<String> lines = readFileByLine(filePath);//获取所有行
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
	}*/
}
