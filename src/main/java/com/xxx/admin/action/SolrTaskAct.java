package com.xxx.admin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xxx.admin.service.SolrTaskService;
import com.xxx.utils.Pagination;

@Controller
@RequestMapping("/solrTask")
public class SolrTaskAct {
	//增加任务的创建者
	public String createUser="solr_sys_admin";
	
	@RequestMapping(value = "list.htm")
	public String getAllTask(ModelMap model,Integer status,Integer pageNo, Integer pageSize,HttpServletRequest request,HttpServletResponse response) {
		Pagination page = solrTaskService.getTaskByStatus(pageNo,pageSize,status);
		System.out.println(page.getTotalPage()+" ---solrTask---");
		System.out.println( page.getList().size()+" ## page.getList()---solrTask");
		model.put("list", page.getList());
		model.put("page", page);
		model.put("status", status);
		model.put("pageNo", page.getPageNo());
		String type = request.getParameter("type");
		if("ajax".equals(type)){
			return "solrTask/list_ajax";	
		}else{
			return "solrTask/list";	
		}
	}
	
	
	
	@Autowired
	private SolrTaskService solrTaskService;
}
