package com.xxx.admin.action;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xxx.core.exception.MongoDBException;



@Controller
public class LoginAct {
	private static final Logger log = LoggerFactory.getLogger(LoginAct.class);

	@RequestMapping(value = "/login.htm", method = {RequestMethod.POST})
	public String login(ModelMap model,HttpServletRequest request,HttpServletResponse response) throws MongoDBException, Exception{		
		ResourceBundle conf= ResourceBundle.getBundle("users");
		String username= conf.getString("username");
		String password=conf.getString("password");
		
		String p_username = request.getParameter("username");
		String p_password = request.getParameter("password");
		
		if(p_username!=null && p_password!=null){
			if(p_username.equals(username) && p_password.equals(password)){
				log.info("登录成功，用户名:"+p_username);
				try {
					response.sendRedirect("index.jsp");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				response.sendRedirect("login.jsp");
			}
		}else{
			response.sendRedirect("login.jsp");
		}
		
		return null;
	}
	
}
