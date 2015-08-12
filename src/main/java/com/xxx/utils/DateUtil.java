package com.xxx.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil { 

	/** 
	* 北京时间 转化成格林威治时间 
	* 格林威治时间比北京时间少8小时 
	* @param date 
	* @return 
	*/ 
	public static String beijingTime2GMTTime(Date date) { 
	DateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.ENGLISH); 
	gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT")); 
	return gmtDateFormat.format(date); 
	} 

	/** 
	* 北京时间 转化�?格林威治时间 
	* 格林威治时间比北京时间少8小时 
	* @param dateStr 时间字符�?
	* @param formatStr 时间格式 
	* @return 
	* @throws ParseException 
	*/ 
	public static String beijingTime2GMTTime(String dateStr,String formatStr) { 
	DateFormat dateFormat = new SimpleDateFormat(formatStr, Locale.ENGLISH); 
	Date date = null; 
	try { 
	date = dateFormat.parse(dateStr); 
	} catch (Exception e) { 
	throw new RuntimeException(e); 
	} 
	return beijingTime2GMTTime(date); 
	} 
	/** 
	* 北京时间 转化�?格林威治时间 
	* 格林威治时间比北京时间少8小时 
	* 默认的时间格式为 "yyyy-MM-dd HH:mm:ss" 
	* @param dateStr 时间字符�?
	* @return 
	* @throws ParseException 
	*/ 
	public static String beijingTime2GMTTime(String dateStr) { 
	if(dateStr==null || dateStr.length()!=19||!dateStr.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}")){ 
	return dateStr; 
	} 
	String formatStr = "yyyy-MM-dd HH:mm:ss"; 
	return beijingTime2GMTTime(dateStr,formatStr); 
	} 

	} 

