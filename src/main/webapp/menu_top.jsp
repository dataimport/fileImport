<%@page contentType="text/html; charset=UTF-8"%>
<div class="container-fluid">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="./">天眼数据系统 Beta1.0</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">

			<ul class="nav navbar-nav navbar-right">
				<li><a href="#"> [当前用户：<font color="red">Qiyb</font>
						角色：系统管理员]
				</a></li>
			</ul>

			<ul class="nav navbar-nav navbar-right">
				<li><a href="index.jsp">系统总览</a></li>
				<li><a href="data_frame.jsp?t=task_list.htm_not_finish">入库任务监控</a></li>
				<li><a href="elastic.jsp?t=0">入索引任务监控</a></li>
				<li><a href="data_frame.jsp?t=mongo_allCollections.htm">Mongo数据维护</a></li>

			</ul>


			<!--  <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
          </form> -->
		</div>
	</div>
</div>
