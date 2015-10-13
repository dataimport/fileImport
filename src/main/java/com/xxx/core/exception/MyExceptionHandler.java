package com.xxx.core.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MyExceptionHandler implements HandlerExceptionResolver {  
	  
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,  
            Exception ex) {  
        Map<String, Object> model = new HashMap<String, Object>();  
        model.put("ex", ex);  
        //根据不同的异常 处理不同的情况  
        if(ex instanceof FileImportException) {//导入数据异常
        	//异常处理
        	
        	//跳转到相应的提示页面
            return new ModelAndView("redirect:/error.html", model);  
        }else if(ex instanceof ReadFileException) {//读文件异常
        	return new ModelAndView("redirect:/error.html",model);          
        } else {  
            return new ModelAndView("redirect:/error.html", model);  
        }  
    }  
}  