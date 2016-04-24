<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- ä¸è¿°3ä¸ªmetaæ ç­¾*å¿é¡»*æ¾å¨æåé¢ï¼ä»»ä½å¶ä»åå®¹é½*å¿é¡»*è·éå¶åï¼ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>天眼数据系统</title>

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="static_bootstrap/js/ie-emulation-modes-warning.js"></script>

	
	<!-- start: CSS -->
	<link id="bootstrap-style" href="static_bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="static_bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link id="base-style" href="static_bootstrap/css/style.css" rel="stylesheet">
	<link id="base-style-responsive" href="static_bootstrap/css/style-responsive.css" rel="stylesheet">
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&subset=latin,cyrillic-ext,latin-ext' rel='stylesheet' type='text/css'>
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
				<li>
					<i class="icon-home"></i>
					<a href="#">系统总览&nbsp;&nbsp;></a> 
					
				</li>
				<li><a href="#">入库任务监控</a></li>
			</ul>

			<div class="row-fluid">
				
			<% String type = request.getParameter("t");
			   Integer pageSize=2; //每页数据条数
    		   if(type!=null && type.trim().equals("task_list.htm_not_finish")){
    		%>       
    		    <iframe src="task/list.htm?status=888" frameborder="0" scrolling="auto" width="100%" height="600"></iframe>
    		<%}else if(type!=null && type.trim().equals("task_list.htm")){ %>
    			<iframe src="task/list.htm?pageSize=<%=pageSize %>" frameborder="0" scrolling="auto" width="100%" height="600"></iframe>
    		<%}else if(type!=null && type.trim().equals("folder_list.htm")){ %>
    			<iframe src="folder/list.htm?pageSize=<%=pageSize %>" frameborder="0" scrolling="auto" width="100%" height="600"></iframe>
    		<%}else if(type!=null && type.trim().equals("folder_child.htm")){ %>
    			<iframe src="folder/child.htm?pageSize=20" frameborder="0" scrolling="auto" width="100%" height="850"></iframe>
    		<%}else if(type!=null && type.trim().equals("mongo_allCollections.htm")){ %>
    			<iframe src="mongo/list.htm?pageSize=<%=pageSize %>" frameborder="0" scrolling="auto" width="100%" height="900"></iframe>
    		<%}else{ %>
            	<iframe src="task/list.htm?status=888" frameborder="0" scrolling="auto" width="100%" height="600"></iframe>
            <%} %>
				
			</div>	
		</div>
	</div>
	</div>

			
			
	
	<footer style="position: fixed;bottom:0;left:0;width:100%;">
			<div style="margin: 0 auto;text-align: center;">&copy; 2016 <a href="http://x.news.cn">北京傲思信息技术有限公司</a></div>
	</footer>



     <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
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
  </body>
</html>