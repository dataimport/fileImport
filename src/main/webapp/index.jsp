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
					<p>You need to have JavaScript enabled to use this site.</p>
				</div>
			</noscript>
			
			<!-- start: Content -->
			<div id="content" class="span10">
			
			
			<ul class="breadcrumb">
				<li>
					<i class="icon-home"></i>
					<a href="index.jsp">系统总览&nbsp;&nbsp;></a> 
					
				</li>
				<li><a href="#">当前环境信息</a></li>
			</ul>
            <div style="margin:0 auto;text-align: center;width: 1000px;">
			<div style="margin-top: 150px;">
			<div style="float:left;width:200px;">
				<h1>FS</h1>
				<img alt="" src="resources/project_img/data.png" border="0">
				<p class="label-success" id="totalFile">文件数量:加载中...</p>
				<p class="label-warning" id="totalFileLength" >存储容量:加载中...</p>
				<p class="label-info">路径注册</p>
			</div>
			<div style="float:left;width:200px;margin-top: 60px;">
				<p class="label label-important" id="notImportFile">未完成入库任务:加载中...</p>
				<p><img alt="" src="resources/project_img/jt.png" border="0" ></p>
			</div>
			<div style="float:left;width:200px;">
				<h1>入库数据</h1>
				<img alt="" src="resources/project_img/data.png" border="0">
				<p class="label-success" id="mongo_collections" >数据表:加载中...</p>
				<p class="label-warning" id="mongo_lines" >记录行数:加载中...</p>
				<p class="label-info" id="mongo_storageSize" >存储容量:加载中...</p>
			</div>
			<div style="float:left;width:200px;margin-top: 60px;">
				<p class="label label-important">未完成索引任务12345</p>
				<p><img alt="" src="resources/project_img/jt.png" border="0" ></p>
			</div>
			<div style="float:left;width:200px;">
				<h1>全文检索</h1>
				<img alt="" src="resources/project_img/data.png" border="0">
				<p class="label-success" id="es_collections">索引数量:加载中...</p>
				<p class="label-warning" id="es_lines">索引行数:加载中...</p>
				<p class="label-info" id="es_storageSize">索引容量:加载中...</p>
			</div>
		    </div>
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
<script>
$.ajax(  
        {  
            url:'./index/index.htm', 
            type:"post",             
            data:{},  
            dataType:"json",  
            timeout:"10000",  
            error:function(){
            	
            },  
            success:function(data)  
            {  	   
	            $("#mongo_collections").text("数据表:"+data.collections);
	            $("#mongo_lines").text("记录行数:"+data.totalCount);            	
	            var storageSizeShow="0";
				 if(data.storageSize>=1073741824){//GB
					 storageSizeShow =  (data.storageSize/1073741824).toFixed(2)+" GB";
				 }else if(data.storageSize>=1048576){//MB
					 storageSizeShow = (data.storageSize/1048576).toFixed(2)+" MB";
				 }else if(data.storageSize>=1024){//kb
				 	storageSizeShow = (data.storageSize/1024).toFixed(2)+" KB";
				 }else{
					 storageSizeShow = (data.storageSize).toFixed(2)+" B";
				 }
				 $("#mongo_storageSize").text("存储容量:"+storageSizeShow);
				 $("#totalFile").text("文件数量:"+data.totalFile);
				 $("#notImportFile").text("未完成入库任务:"+data.notImportFileCout);
				 
				 var totalFileLength="0";
				 if(data.totalFileLength>=1073741824){//GB
					 totalFileLength =  (data.totalFileLength/1073741824).toFixed(2)+" GB";
				 }else if(data.totalFileLength>=1048576){//MB
					 totalFileLength = (data.totalFileLength/1048576).toFixed(2)+" MB";
				 }else if(data.storageSize>=1024){//kb
					 totalFileLength = (data.totalFileLength/1024).toFixed(2)+" KB";
				 }else{
					 totalFileLength = (data.totalFileLength).toFixed(2)+" B";
				 }
				 
				 $("#totalFileLength").text(" 存储容量:"+totalFileLength);
	        }  
        }  
    ); 
    
$.ajax(  
        {  
            url:'./index/es_status.htm', 
            type:"post",             
            data:{},  
            dataType:"json",  
            timeout:"10000",  
            error:function(){
            	
            },  
            success:function(data)  
            {  	   
	            $("#es_collections").text("索引数量:"+data.collections);
	            $("#es_lines").text("索引行数:"+data.totalCount);            	
	            var storageSizeShow="0";
				 if(data.storageSize>=1073741824){//GB
					 storageSizeShow =  (data.storageSize/1073741824).toFixed(2)+" GB";
				 }else if(data.storageSize>=1048576){//MB
					 storageSizeShow = (data.storageSize/1048576).toFixed(2)+" MB";
				 }else if(data.storageSize>=1024){//kb
				 	storageSizeShow = (data.storageSize/1024).toFixed(2)+" KB";
				 }else{
					 storageSizeShow = (data.storageSize).toFixed(2)+" B";
				 }
				 $("#es_storageSize").text("索引容量:"+storageSizeShow);
	            }  
        }  
    );
    
</script>
</body>
</html>
