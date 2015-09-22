package com.xxx.utils;

import java.io.*;
public abstract class FileLineCount 
{
 public static void main(String args[])throws IOException
 {
  //1000w数据耗时 16秒，我的笔记本测试结果
  File f=new File("c:/file_test/info-1000000.txt");
  long start = System.currentTimeMillis();
  int count=0;
  if(f.exists())
  { 
   FileReader read=new FileReader(f);
   BufferedReader buff=new BufferedReader(read);
   boolean isEnd=false;
   while(!isEnd)
   {
    String line=buff.readLine();
    if(line==null)
    {
      isEnd=true;
    }
    else
    {
     count++;
    }
   }
   System.out.println("文件共有"+count+"行");
   
  }
  else
  {
   System.out.println("你输入的文件不存在，请正确使用：");
  }
  
  long end = System.currentTimeMillis();
  System.out.println("总耗时："+(end-start)+" ms");
  
 }
    
}
