package com.xxx.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

public class FileCharsetDetector {

	 private boolean found = false;

	 /**
	  * 如果完全匹配某个字符集检测算法, 则该属性保存该字符集的名称. 否则(如二进制文件)其值就为默认值 null, 这时应当查询属性
	  */
	 private String encoding = null;

	 public static void main(String[] argv) throws Exception {
      File file = new File ("F:\\work2space\\testdata\\info-10000000.txt");
      System.out.println(file.getPath());	
	  System.out.println("文件编码:" + new FileCharsetDetector().guestFileEncoding(file.getPath()));
	 }

	 /**
	  * 传入一个文件(File)对象，检查文件编码
	  * 
	  * @param file
	  *            File对象实例
	  * @return 文件编码，若无，则返回null
	  * @throws FileNotFoundException
	  * @throws IOException
	  */
	 public String guestFileEncoding(File file) throws FileNotFoundException,
	   IOException {
	  return geestFileEncoding(file, new nsDetector());
	 }

	 /**
	  * 获取文件的编码
	  * 
	  * @param file
	  *            File对象实例
	  * @param languageHint
	  *            语言提示区域代码 eg：1 : Japanese; 2 : Chinese; 3 : Simplified Chinese;
	  *            4 : Traditional Chinese; 5 : Korean; 6 : Dont know (default)
	  * @return 文件编码，eg：UTF-8,GBK,GB2312形式，若无，则返回null
	  * @throws FileNotFoundException
	  * @throws IOException
	  */
	 public String guestFileEncoding(File file, int languageHint)
	   throws FileNotFoundException, IOException {
	  return geestFileEncoding(file, new nsDetector(languageHint));
	 }

	 /**
	  * 获取文件的编码
	  * 
	  * @param path
	  *            文件路径
	  * @return 文件编码，eg：UTF-8,GBK,GB2312形式，若无，则返回null
	  * @throws FileNotFoundException
	  * @throws IOException
	  */
	 public String guestFileEncoding(String path) throws FileNotFoundException,
	   IOException {
	  return guestFileEncoding(new File(path));
	 }

	 /**
	  * 获取文件的编码
	  * 
	  * @param path
	  *            文件路径
	  * @param languageHint
	  *            语言提示区域代码 eg：1 : Japanese; 2 : Chinese; 3 : Simplified Chinese;
	  *            4 : Traditional Chinese; 5 : Korean; 6 : Dont know (default)
	  * @return
	  * @throws FileNotFoundException
	  * @throws IOException
	  */
	 public String guestFileEncoding(String path, int languageHint)
	   throws FileNotFoundException, IOException {
	  return guestFileEncoding(new File(path), languageHint);
	 }

	 /**
	  * 获取文件的编码
	  * 
	  * @param file
	  * @param det
	  * @return
	  * @throws FileNotFoundException
	  * @throws IOException
	  */
	 private String geestFileEncoding(File file, nsDetector det)
	   throws FileNotFoundException, IOException {
	  // Set an observer...
	  // The Notify() will be called when a matching charset is found.
	  det.Init(new nsICharsetDetectionObserver() {
	   public void Notify(String charset) {
	    found = true;
	    encoding = charset;
	   }
	  });

	  BufferedInputStream imp = new BufferedInputStream(new FileInputStream(
	    file));

	  byte[] buf = new byte[1024];
	  int len;
	  boolean done = false;
	  boolean isAscii = true;

	  while ((len = imp.read(buf, 0, buf.length)) != -1) {
	   // Check if the stream is only ascii.
	   if (isAscii)
	    isAscii = det.isAscii(buf, len);

	   // DoIt if non-ascii and not done yet.
	   if (!isAscii && !done)
	    done = det.DoIt(buf, len, false);
	  }
	  det.DataEnd();

	  if (isAscii) {
	   encoding = "ASCII";
	   found = true;
	  }

	  if (!found) {
	   String prob[] = det.getProbableCharsets();
	   if (prob.length > 0) {
	    // 在没有发现情况下，则取第一个可能的编码
	    encoding = prob[0];
	   } else {
	    return null;
	   }
	  }
	  return encoding;
	 }
	 

//	    /** 
//	     * 利用第三方开源包cpdetector获取文件编码格式. 
//	     * @param filePath 
//	     * @return 
//	     */ 
//	    public static String getFileEncode(File file) {  
//	        /** 
//	         * <pre> 
//	         * 1、cpDetector内置了一些常用的探测实现类,这些探测实现类的实例可以通过add方法加进来, 
//	         * 如:ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector.  
//	         * 2、detector按照“谁最先返回非空的探测结果,就以该结果为准”的原则.  
//	         * 3、cpDetector是基于统计学原理的,不保证完全正确. 
//	         * </pre> 
//	         */ 
//	        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();  
//	           
//	        detector.add(new ParsingDetector(false));  
//	        detector.add(UnicodeDetector.getInstance());  
//	        detector.add(JChardetFacade.getInstance());//内部引用了 chardet.jar的类  
//	        detector.add(ASCIIDetector.getInstance());  
//	           
//	        Charset charset = null;  
//	        try {  
//	            charset = detector.detectCodepage(file.toURI().toURL());  
//	        } catch (Exception e) {  
//	            e.printStackTrace();  
//	        }  
//	           
//	        //默认为GBK  
//	        String charsetName = "GBK";  
//	        if (charset != null) {  
//	            if (charset.name().equals("US-ASCII")) {  
//	                charsetName = "ISO_8859_1";  
//	            } else{  
//	                charsetName = charset.name();  
//	            }  
//	        }  
//	        return charsetName;  
//	    }  
	}
