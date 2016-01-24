package com.xxx.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MyExceptionHandler implements HandlerExceptionResolver {  
	  
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,  
            Exception ex) {  
        ModelAndView mv = new ModelAndView("common/error");  
        mv.addObject("ex", ex);  
        //根据不同的异常 处理不同的情况  
        if(ex instanceof MongoDBException) {//MongoDB异常
        	//异常处理
//        	if(((MongoDBException)ex).getId()==BaseException.MONGODB_FOLDER_CODE){//操作文件夹相关异常
//        		
//        	}  
        	//跳转到相应的提示页面 
            return mv;
        }else if(ex instanceof ReadFileException) {//读文件异常
        	 return mv;
        }else if(ex instanceof DataToMongoDBException) {//读文件异常
        	
       	 return mv;
       } else {          	
        	 mv.addObject("ex", new MongoDBException(10000,"请求异常"));  
        	 return mv;
        }  
    }  
}  