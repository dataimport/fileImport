package com.xxx.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {
	public static  String replaceBlank(String str) {		 		 
		 if (str!=null) {		
			 str = str.replace("\"", "&quot;").replace("\\", "&#x5c;").replace(" ","");			
		     Pattern p = Pattern.compile("\\s*|\t|\r|\n");		 
		     Matcher m = p.matcher(str);		 
		     str = m.replaceAll("");	 
		    }		 		 
		      return str;		 
		 }
}
