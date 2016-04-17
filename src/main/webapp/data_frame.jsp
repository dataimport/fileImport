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

    <!-- Bootstrap core CSS -->
    <link href="static/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="static/css/dashboard.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="static/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
       <%@ include  file="menu_top.jsp"%>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
           <%@ include  file="menu_left.jsp"%>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <% String type = request.getParameter("t");
    		   if(type!=null && type.trim().equals("task_list.htm_not_finish")){
    		%>       
    		    <iframe src="task/list.htm?status=888" frameborder="0" scrolling="auto" width="100%" height="600"></iframe>
    		<%}else if(type!=null && type.trim().equals("task_list.htm")){ %>
    			<iframe src="task/list.htm" frameborder="0" scrolling="auto" width="100%" height="600"></iframe>
    		<%}else if(type!=null && type.trim().equals("folder_list.htm")){ %>
    			<iframe src="folder/list.htm" frameborder="0" scrolling="auto" width="100%" height="600"></iframe>
    		<%}else if(type!=null && type.trim().equals("folder_child.htm")){ %>
    			<iframe src="folder/child.htm" frameborder="0" scrolling="auto" width="100%" height="850"></iframe>
    		<%}else if(type!=null && type.trim().equals("mongo_allCollections.htm")){ %>
    			<iframe src="mongo/list.htm" frameborder="0" scrolling="auto" width="100%" height="600"></iframe>
    		<%}else{ %>
            	<iframe src="task/list.htm?status=888" frameborder="0" scrolling="auto" width="100%" height="600"></iframe>
            <%} %>
        </div>
      </div>
    </div>

     <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="static/js/jquery/1.11.3/jquery.min.js"></script>
    <script src="static/js/bootstrap.min.js"></script>  
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="static/js/ie10-viewport-bug-workaround.js"></script>
  </body>
</html>