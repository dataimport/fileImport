<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>天眼数据系统  - 登录系统</title>

    <!-- Bootstrap core CSS -->
    <link href="static/css/login.css" rel="stylesheet">
     <script src="static/js/jquery/1.11.3/jquery.min.js"></script>
    <script src="static/js/bootstrap.min.js"></script> 

 <style type="text/css">
body{ background:#0066A8 no-repeat center 0px;}
.tit{ margin:auto; margin-top:170px; text-align:center; width:350px; padding-bottom:20px;}
.login-wrap{ width:220px; padding:30px 50px 0 330px; height:220px; background:#fff url(static/images/login/left_pic.jpg) no-repeat 30px 40px; margin:auto; overflow: hidden;}
.login_input{ display:block;width:210px;}
.login_user{ background: url(static/images/login/input_icon_1.png) no-repeat 200px center; font-family: "Lucida Sans Unicode", "Lucida Grande", sans-serif}
.login_password{ background: url(static/images/login/input_icon_2.png) no-repeat 200px center; font-family:"Courier New", Courier, monospace}
.btn-login{ background:#40454B; box-shadow:none; text-shadow:none; color:#fff; border:none;height:35px; line-height:26px; font-size:14px; font-family:"microsoft yahei";}
.btn-login:hover{ background:#333; color:#fff;}
.copyright{ margin:auto; margin-top:10px; text-align:center; width:370px; color:#CCC}
@media (max-height: 700px) {.tit{ margin:auto; margin-top:100px; }}
@media (max-height: 500px) {.tit{ margin:auto; margin-top:50px; }}
</style>
</head>

<body>
<div class="tit"><img src="static/images/login/title_pic.png" alt="" /></div>
<div class="login-wrap">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td height="25" valign="bottom">用户名：</td>
    </tr>
    <tr>
      <td><input type="text" class="login_input login_user" value="admin" /></td>
    </tr>
    <tr>
      <td height="35" valign="bottom">密  码：</td>
    </tr>
    <tr>
      <td><input type="password"  class="login_input login_password" value="12345678" /></td>
    </tr>
    <tr>
      <td height="60" valign="bottom"><a href="index.jsp" class="btn btn-block btn-login">登录</a></td>
    </tr>
   
  </table>
</div>
<div class="copyright">建议使用IE8以上版本或谷歌浏览器</div>
    
</body>
</html>
