package com.xxx.admin.action;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xxx.admin.service.MongoFileService;
import com.xxx.utils.Pagination;

@Controller
@RequestMapping("/mongo")
public class MongoAct {
		
	@RequestMapping(value = "list.htm")
	public String getAllTask(ModelMap model,String collectionName,Integer pageNo, Integer pageSize,HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map  = mongoFileService.getObjectsByCollectionName(pageNo, pageSize, collectionName);	
		model.put("list", (List<Map<String,String>>)map.get("value"));
		model.put("page", (Pagination)map.get("page"));
		model.put("collectionName", collectionName);
		model.put("pageNo", ((Pagination)map.get("page")).getPageNo());
		model.put("fields", (LinkedHashSet<String>)map.get("fields"));		
		return "mongo/list";
	}
		@Autowired
	private MongoFileService mongoFileService;
}
