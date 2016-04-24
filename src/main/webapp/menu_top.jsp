<%@page contentType="text/html; charset=UTF-8"%>
<div class="navbar">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="btn btn-navbar" data-toggle="collapse" data-target=".top-nav.nav-collapse,.sidebar-nav.nav-collapse">
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</a>
				<a class="brand" href="index.jsp"><span>天眼数据系统 Beta1.0</span></a>
								
				<!-- start: Header Menu -->
				<div class="nav-no-collapse header-nav">
					<ul class="nav pull-right">
						<li class="hidden-phone"><a href="index.jsp" class="btn dropdown-toggle">系统总览</a></li>
						<li class="hidden-phone" style="line-height:40px;">|</li>
				        <li class="hidden-phone"><a href="data_frame.jsp?t=task_list.htm_not_finish" class="btn dropdown-toggle">入库任务监控</a></li>
				        <li class="hidden-phone" style="line-height:40px;">|</li>
				        <li class="hidden-phone"><a href="elastic.jsp?t=0" class="btn dropdown-toggle">入索引任务监控</a></li>
				        <li class="hidden-phone" style="line-height:40px;">|</li>
				        <li class="hidden-phone"><a href="data_frame.jsp?t=mongo_allCollections.htm" class="btn dropdown-toggle">Mongo数据维护</a></li>
						<!-- start: User Dropdown -->
						<li class="dropdown">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								[当前用户：<font color="red">Qiyb</font>
						角色：系统管理员]
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li class="dropdown-menu-title">
 									<span>账号设置</span>
								</li>
								<li><a href="#" style="color: #000000 !;">退出</a></li>
							</ul>
						</li>
						<!-- end: User Dropdown -->
					</ul>
				</div>
				<!-- end: Header Menu -->
				
			</div>
		</div>
	</div>
