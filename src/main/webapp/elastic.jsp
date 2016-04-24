<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	
	<!-- start: Meta -->
	<meta charset="utf-8">
	<title>天眼数据系统</title>
	<meta name="description" content="Bootstrap Metro Dashboard">
	<meta name="author" content="luyu">
	<!-- end: Meta -->
	
	<!-- start: Mobile Specific -->
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- end: Mobile Specific -->
	
	<!-- start: CSS -->
	<link id="bootstrap-style" href="static_bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="static_bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link id="base-style" href="static_bootstrap/css/style.css" rel="stylesheet">
	<link id="base-style-responsive" href="static_bootstrap/css/style-responsive.css" rel="stylesheet">
	
	<!-- end: CSS -->
		
	<!-- start: Favicon -->
	<link rel="shortcut icon" href="static_bootstrap/img/favicon.ico">
	<!-- end: Favicon -->
	
		
		
		
</head>

<body>
		<!-- start: Header -->
	<%@ include  file="menu_top.jsp"%>
	<!-- start: Header -->
	
		<div class="container-fluid-full">
				<div class="row-fluid">		
			<!-- start: Main Menu -->
    <%@ include  file="menu_left.jsp"%>
			<!-- end: Main Menu -->
			
			<noscript>
				<div class="alert alert-block span10">
					<h4 class="alert-heading">Warning!</h4>
					<p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a> enabled to use this site.</p>
				</div>
			</noscript>
			
			<!-- start: Content -->
			<div id="content" class="span10">
			
			
			<ul class="breadcrumb">
			
			<% String type = request.getParameter("t");
    		   if(type!=null && type.trim().equals("0")){
    		%>       
    		    <li>
					<i class="icon-home"></i>
					<a href="elastic.jsp?t=1">索引管理&nbsp;&nbsp;></a> 
					
				</li>
				<li><a href="#">入索引任务监控</a></li>
    		<%}else if(type!=null && type.trim().equals("1")){ %>
    			<li>
					<i class="icon-home"></i>
					<a href="elastic.jsp?t=1">索引管理&nbsp;&nbsp;></a> 
					
				</li>
				<li><a href="#">入索引任务日志</a></li>
    		<%}else{ %>
            	<li>
					<i class="icon-home"></i>
					<a href="elastic.jsp?t=1">索引管理&nbsp;&nbsp;></a> 
					
				</li>
				<li><a href="#">索引数据维护</a></li>
            <%} %>
			
				
			</ul>

			<div class="row-fluid" style="margin: 0 auto;text-align: center;">

            <% 
    		   if(type!=null && type.trim().equals("0")){
    		%>       
    		    <iframe src="solrTask/list.htm?status=888" frameborder="0" scrolling="auto" width="100%" height="550"></iframe>
    		<%}else if(type!=null && type.trim().equals("1")){ %>
    			<iframe src="solrTask/list.htm?pageSize=2" frameborder="0" scrolling="auto" width="100%" height="550"></iframe>
    		<%}else{ %>
            	<iframe src="http://localhost:9200/_plugin/head/" frameborder="0" scrolling="auto" width="100%" height="550"></iframe>
            <%} %>
      </div>

		</div>
	</div>
	</div>

			
			
	
	<footer style="position: fixed;bottom:0;left:0;width:100%;">
			<div style="margin: 0 auto;text-align: center;">&copy; 2016   北京傲思信息技术有限公司</div>
	</footer>
	
	<!-- start: JavaScript-->
 <script src="static_bootstrap/js/ie10-viewport-bug-workaround.js"></script>
		<script src="static_bootstrap/js/jquery-1.9.1.min.js"></script>
	<script src="static_bootstrap/js/jquery-migrate-1.0.0.min.js"></script>
	
		<script src="static_bootstrap/js/jquery-ui-1.10.0.custom.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.ui.touch-punch.js"></script>
	
		<script src="static_bootstrap/js/modernizr.js"></script>
	
		<script src="static_bootstrap/js/bootstrap.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.cookie.js"></script>
	
		<script src='static_bootstrap/js/fullcalendar.min.js'></script>
	
		<script src='static_bootstrap/js/jquery.dataTables.min.js'></script>

		<script src="static_bootstrap/js/excanvas.js"></script>
	<script src="static_bootstrap/js/jquery.flot.js"></script>
	<script src="static_bootstrap/js/jquery.flot.pie.js"></script>
	<script src="static_bootstrap/js/jquery.flot.stack.js"></script>
	<script src="static_bootstrap/js/jquery.flot.resize.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.chosen.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.uniform.min.js"></script>
		
		<script src="static_bootstrap/js/jquery.cleditor.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.noty.js"></script>
	
		<script src="static_bootstrap/js/jquery.elfinder.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.raty.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.iphone.toggle.js"></script>
	
		<script src="static_bootstrap/js/jquery.uploadify-3.1.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.gritter.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.imagesloaded.js"></script>
	
		<script src="static_bootstrap/js/jquery.masonry.min.js"></script>
	
		<script src="static_bootstrap/js/jquery.knob.modified.js"></script>
	
		<script src="static_bootstrap/js/jquery.sparkline.min.js"></script>
	
		<script src="static_bootstrap/js/counter.js"></script>
	
		<script src="static_bootstrap/js/retina.js"></script>

		<script src="static_bootstrap/js/custom.js"></script>
	<!-- end: JavaScript-->
	
</body>
</html>
