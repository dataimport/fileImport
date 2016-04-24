package com.xxx.admin.file.analysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xxx.utils.FileCharsetDetector;


@Component("excelFileAnalysis")
public class ExcelFileAnalysis  {
	private static final Logger log = LoggerFactory.getLogger(ExcelFileAnalysis.class);
	
	public static final int BUFFER_SIZE = 0x300000;//缓冲区大小为3M
	
	public static final int DEFAULT_LINENUM = 10;//默认预览返回的行数
	
	public static final int MAX_LINENUM = 1000;//最大能预览的行数
	
	public static final int FILESIZE_TIPPINGPOINT = 100;//文件算大文件还是小文件区分临界值  默认100行

	
	public   Map<String,Object>   previewFileByLineNum(String filePath, Integer viewNum,String excelVersion)	throws Exception {
		if("xls".equals(excelVersion)){//2003
			return preview2003FileByLineNum(filePath, viewNum);
		}else{//2007
			return preview2007FileByLineNum(filePath, viewNum);
		} 
		
	}

	
	
	/**
	 * 读取大文件的行数
	 * @param filePath 文件路径
	 * @throws IOException 
	 * @throws FileNotFoundException
	 */
	public String getFileLineNum(String filePath){
		String excelVersion  = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());
		try{
			if("xls".equals(excelVersion.toLowerCase())){//2003
				InputStream input = new FileInputStream(filePath);
				POIFSFileSystem fs = new POIFSFileSystem(input);
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);
				return String.valueOf(sheet.getLastRowNum());
			}else{//2007
				 XSSFWorkbook wbs = new XSSFWorkbook(new FileInputStream(filePath));
		         XSSFSheet sheet = wbs.getSheetAt(0);
		         return String.valueOf(sheet.getLastRowNum());
			}		
		}catch(Exception ex){
			ex.printStackTrace();
			log.error("获取excel文件总行数异常： ",ex);
		}
		
		return "1";
		
	}
	
	public String getCharset(String filePath){
		try{
			return new FileCharsetDetector().guestFileEncoding(filePath);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return "UTF-8";
	}
	
	private String[] getColumns(List<String> lines,String separator){
		 if(lines.size()>0){
			 return lines.get(0).split(separator);
		 }else{
			 return null;
		 }	 
	}
	
	public Map<String,Object> readAllLines(String filePath,int totalCount) throws Exception{
		String extension  = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());
		if("xls".equals(extension)){//2003
			return preview2003FileByLineNum(filePath, totalCount);
		}else{//2007
			return preview2007FileByLineNum(filePath, totalCount);
		} 
	}
	
	
	/**
	 * 预览2003版本excel
	 * @param filePath
	 * @param viewNum
	 * @param excelVersion
	 * @return
	 * @throws Exception
	 */
	private  Map<String,Object> preview2003FileByLineNum(String filePath, Integer viewNum)	throws Exception {
			try {			
				InputStream input = new FileInputStream(filePath);
				POIFSFileSystem fs = new POIFSFileSystem(input);
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);
				// Iterate over each row in the sheet
				//Iterator<Row> rows = sheet.rowIterator();
			    //获取表头
		        int begin = sheet.getFirstRowNum(); 
		        //HSSFRow firstRow = sheet.getRow(begin);
		        //int cellTotal = firstRow.getPhysicalNumberOfCells();
		        int cellTotal = 0;
		        int cellTotalMax=0;
		        if(viewNum==null){
		        	viewNum = DEFAULT_LINENUM;
		        }
		        
		        if(viewNum>sheet.getLastRowNum()){
		        	viewNum = sheet.getLastRowNum();
		        }
		          
		        List<String[]> poiList = new ArrayList<String[]>();
		        Map map = new HashMap<String,Object>();
				 for(int i = begin;i <= viewNum;i++){				
					 	HSSFRow row = sheet.getRow(i);  //一行的所有单元格格式都是常规的情况下，返回的row为null
			            if(null != row){
			            	//cellTotal = row.getPhysicalNumberOfCells();
			            	cellTotal = row.getLastCellNum();
			            	if(cellTotal>cellTotalMax){
			            		cellTotalMax = cellTotal;
			            	}
			              String[] cells = new String[cellTotal];
			              for(int k=0;k<cellTotal;k++){
			                HSSFCell cell = row.getCell(k);
			                if(cell!=null){
			                	cells[k] = getStringXLSCellValue(cell);
			                }else{
			                	 cells[k] = "";
			                }			                
			              }
			              poiList.add(cells);
			            }
			          }
				   map.put("list", poiList);
		           map.put("cellTotalMax", cellTotalMax);	
		           return map;
				} catch (Exception ex) {
					ex.printStackTrace();
					throw ex;
				}		
	}
	
	 /**
     * 预览2007文件
     * @param file
     * @param resultLineNum
     * @return
     * @throws FileNotFoundException
     */
	
	private Map<String,Object> preview2007FileByLineNum(String filePath, Integer viewNum)	throws Exception {
	        List<String[]> poiList = new ArrayList<String[]>();
	        Map map = new HashMap<String,Object>();
	        try{
	          XSSFWorkbook wbs = new XSSFWorkbook(new FileInputStream(filePath));
	          XSSFSheet childSheet = wbs.getSheetAt(0);
	          //获取表头
	          int begin = childSheet.getFirstRowNum(); 
	          //XSSFRow firstRow = childSheet.getRow(begin);
	          //int cellTotal = firstRow.getPhysicalNumberOfCells();
	          int cellTotal = 0;
	          int cellTotalMax=0;
//	          //是否跳过表头解析数据
//	          if(isHeader){
//	           begin += 1;
//	          }
	          
		      if(viewNum==null){
		        	viewNum = DEFAULT_LINENUM;
		       }		        
		      if(viewNum>childSheet.getLastRowNum()){
		        	viewNum = childSheet.getLastRowNum();
		      }
	          for(int i = begin;i <= viewNum;i++){
	            XSSFRow row = childSheet.getRow(i);  //一行的所有单元格格式都是常规的情况下，返回的row为null
	            if(null != row){
	            	//cellTotal = row.getPhysicalNumberOfCells();
	            	cellTotal = row.getLastCellNum();
	            	if(cellTotal>cellTotalMax){
	            		cellTotalMax = cellTotal;
	            	}	       
	            	 
	              String[] cells = new String[cellTotal];
	              for(int k=0;k<cellTotal;k++){
	                XSSFCell cell = row.getCell(k);
	                if(cell!=null){
	                	 cells[k] = getStringXLSXCellValue(cell);
	                }else{
	                	 cells[k] = "";
	                }
	               
	              }
	              poiList.add(cells);
	            }
	          }
	          
	          map.put("list", poiList);
	          map.put("cellTotalMax", cellTotalMax);	
	          return map;
	        }catch(Exception e){
	          e.printStackTrace();
	         throw e;
	        }
	}
	
	/**
	 * 2003
	 * @param cell
	 * @return
	 */
	 private static String getStringXLSCellValue(HSSFCell cell) {
	      String strCell = "";
	      if (cell == null) {
	        return "";
	      }
	      
	      //将数值型参数转成文本格式，该算法不能保证1.00这种类型数值的精确度
	      DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();  
	      StringBuffer sb = new StringBuffer();
	      sb.append("0");
	      df.applyPattern(sb.toString());  
	      
	      switch (cell.getCellType()) {
	      case HSSFCell.CELL_TYPE_STRING:
	        strCell = cell.getStringCellValue();
	        break;
	      case HSSFCell.CELL_TYPE_NUMERIC:
	        double value = cell.getNumericCellValue();
	        while(Double.parseDouble(df.format(value))!=value){
	          if("0".equals(sb.toString())){
	            sb.append(".0");
	          }else{
	            sb.append("0");
	          }
	          df.applyPattern(sb.toString());
	        }
	        strCell = df.format(value);
	        break;
	      case HSSFCell.CELL_TYPE_BOOLEAN:
	        strCell = String.valueOf(cell.getBooleanCellValue());
	        break;
	      case HSSFCell.CELL_TYPE_BLANK:
	        strCell = "";
	        break;
	      default:
	        strCell = "";
	        break;
	      }
	      if (strCell == null || "".equals(strCell)) {
	        return "";
	      }
	      return strCell;
	    }
	 
	 /**
	  * 2007
	  * @param cell
	  * @return
	  */
	private static String getStringXLSXCellValue(XSSFCell cell) {
        String strCell = "";
        if (cell == null) {
          return "";
        }
        //将数值型参数转成文本格式，该算法不能保证1.00这种类型数值的精确度
        DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();  
        StringBuffer sb = new StringBuffer();
        sb.append("0");
        df.applyPattern(sb.toString()); 
        
        switch (cell.getCellType()) {
        case XSSFCell.CELL_TYPE_STRING:
          strCell = cell.getStringCellValue();
          break;
        case XSSFCell.CELL_TYPE_NUMERIC:
          double value = cell.getNumericCellValue();
          while(Double.parseDouble(df.format(value))!=value){
            if("0".equals(sb.toString())){
              sb.append(".0");
            }else{
              sb.append("0");
            }
            df.applyPattern(sb.toString());
          }
          strCell = df.format(value);
          break;
        case XSSFCell.CELL_TYPE_BOOLEAN:
          strCell = String.valueOf(cell.getBooleanCellValue());
          break;
        case XSSFCell.CELL_TYPE_BLANK:
          strCell = "";
          break;
        default:
          strCell = "";
          break;
        }
        if (strCell == null || "".equals(strCell)) {
          return "";
        }
        return strCell;
      }
	
}
