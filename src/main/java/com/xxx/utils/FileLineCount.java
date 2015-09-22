package com.xxx.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public abstract class FileLineCount {
	private static String filename="c:/file_test/info-1000000.txt";

	/**
	 * 
	 * lnr和fbr两个方法的效率差不多，都是BIO，只不过lnr的代码写起来更简洁一些
	 * 
	 * testNIO效率更好，是BIO耗时的1/4
	 * 
	 */
	public static void lnr() throws IOException {
		// 1000w数据耗时 15秒，我的笔记本测试结果
		// LineNumberReader
		File f = new File("c:/file_test/info-1000000.txt");
		long start = System.currentTimeMillis();
		int count = 0;
		if (f.exists()) {
			FileReader read = new FileReader(f);
			LineNumberReader lnr = new LineNumberReader(read);
			lnr.skip(f.length());
			count = lnr.getLineNumber();
			lnr.close();
			read.close();
			System.out.println("文件共有" + count + "行");

		} else {
			System.out.println("你输入的文件不存在，请正确使用：");
		}

		long end = System.currentTimeMillis();
		System.out.println("总耗时：" + (end - start) + " ms");
	}

	public static void fbr() throws IOException {
		// 1000w数据耗时 16秒，我的笔记本测试结果

		//BIO的方式
		File f = new File("c:/file_test/info-1000000.txt");
		long start = System.currentTimeMillis();
		int count = 0;
		if (f.exists()) {
			FileReader read = new FileReader(f);
			BufferedReader buff = new BufferedReader(read);
			boolean isEnd = false;
			while (!isEnd) {
				String line = buff.readLine();
				if (line == null) {
					isEnd = true;
				} else {
					count++;
				}
			}
			System.out.println("文件共有" + count + "行");

		} else {
			System.out.println("你输入的文件不存在，请正确使用：");
		}

		long end = System.currentTimeMillis();
		System.out.println("总耗时：" + (end - start) + " ms");
	}
	

	 public static void testNIO() {
		 // testNIO,1000w数据耗时 4秒，我的笔记本测试结果
	        FileInputStream fis = null;
	        try {
	            long now;
	            int lines = 1, readCount;
	            fis = new FileInputStream(new File(filename));
	            int size = 1024 * 512;
	            ByteBuffer buffer = ByteBuffer.allocate(size);
	            now = System.currentTimeMillis();
	            FileChannel channel = fis.getChannel();
	            while (0 != (readCount = channel.read(buffer))) {
	                buffer.flip();
	                for (int i = 0; i < readCount; i++) {
	                    if (((int) buffer.get(i)) == '\n') {
	                        lines++;
	                    }
	                }
	            }
	            System.out.println(lines);
	            System.out.println("总耗时：" + (System.currentTimeMillis() - now)+ " ms");
	            channel.close();
	        } catch (FileNotFoundException ex) {
//	            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (IOException ex) {
//	            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            try {
	                if (null != fis) {
	                    fis.close();
	                }
	            } catch (IOException ex) {
//	                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	    }
	 

	public static void main(String args[]) throws IOException {
		//lnr();
//		 fbr();
		testNIO();
	}

}
