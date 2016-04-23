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
					<a href="index.html">系统总览&nbsp;&nbsp;></a> 
					
				</li>
				<li><a href="#">当前环境信息</a></li>
			</ul>
            <div style="margin:0 auto;text-align: center;width: 1000px;">
			<div style="margin-top: 150px;">
			<div style="float:left;width:200px;">
				<h1>FS</h1>
				<img alt="" src="resources/project_img/data.png" border="0">
				<p class="label-success">文件数量:22455</p>
				<p class="label-warning">存储容量:22455</p>
				<p class="label-info">路径注册</p>
			</div>
			<div style="float:left;width:200px;margin-top: 60px;">
				<p class="label label-important">未完成入库任务12345</p>
				<p><img alt="" src="resources/project_img/jt.png" border="0" ></p>
			</div>
			<div style="float:left;width:200px;">
				<h1>入库数据</h1>
				<img alt="" src="resources/project_img/data.png" border="0">
				<p class="label-success">数据表:22455</p>
				<p class="label-warning">记录行数:22455</p>
				<p class="label-info">存储容量:22455</p>
			</div>
			<div style="float:left;width:200px;margin-top: 60px;">
				<p class="label label-important">未完成索引任务12345</p>
				<p><img alt="" src="resources/project_img/jt.png" border="0" ></p>
			</div>
			<div style="float:left;width:200px;">
				<h1>全文检索</h1>
				<img alt="" src="resources/project_img/data.png" border="0">
				<p class="label-success">文件数量:22455</p>
				<p class="label-warning">索引行数:22455</p>
				<p class="label-info">索引容量:22455</p>
			</div>
		    </div>
		    </div>
		    </div>
	</div>
	</div>

			
			
	
	<footer style="position: fixed;bottom:0;left:0;width:100%;">
			<div style="margin: 0 auto;text-align: center;">&copy; 2016 <a href="#">北京傲思信息技术有限公司</a></div>
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
