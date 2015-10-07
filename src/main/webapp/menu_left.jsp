<%@page contentType="text/html; charset=UTF-8"%>
<ul class="nav nav-sidebar">
	<li class="active"><a href="index.jsp">系统总览 <span
			class="sr-only">(current)</span></a></li>
	<li><a href="data_frame.jsp?t=task_list.htm_not_finish">|--入库任务监控</a></li>
	<li><a href="data_frame.jsp?t=task_list.htm">|--入库任务日志</a></li>

</ul>
<hr>
<ul class="nav nav-sidebar">
	<li class="active"><a href="data_frame.jsp?t=mongo_allCollections.htm">数据维护</a></li>
	<li><a href="data_frame.jsp?t=folder_list.htm">|--注册路径管理</a></li>
	<li><a href="data_frame.jsp?t=folder_child.htm">|--查看目录树</a></li>
    <li><a href="data_frame.jsp?t=mongo_allCollections.htm">|--Mongo数据维护</a></li>
</ul>
<hr>
<ul class="nav nav-sidebar">
	<li class="active"><a href="elastic.jsp?t=0">索引管理</a></li>
	<li><a href="elastic.jsp?t=0">|--入索引任务监控</a></li>
	<li><a href="elastic.jsp?t=1">|--入索引任务日志</a></li>
	<li><a href="elastic.jsp">|--索引数据维护</a></li>

</ul>
