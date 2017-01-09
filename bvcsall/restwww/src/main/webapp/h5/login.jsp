<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String ContextPath =request.getContextPath();
String playKey=(String)request.getParameter("playKey");
String shareuid=(String)request.getParameter("shareuid");
String getbackurl=(String)request.getParameter("backurl");
String backurl=ContextPath+"/html5/index";
if(playKey!=null&&!playKey.isEmpty()){
	backurl=ContextPath+"/video/thirdVideo?playKey="+playKey+"&shareuid="+shareuid;
}else if(getbackurl!=null&&!getbackurl.isEmpty()){
	backurl=getbackurl;
}


%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="format-detection" content="email=no" />
	<title>我拍-登录</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/player/js/jquery.min.js"></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/js/players/jwplayer.js'></script>
	<script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/md5.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/zepto.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/api.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/rlapi.js"></script>
	<script type="text/javascript">
	var backurl='<%=backurl%>';
	var playKey='<%=playKey%>';
	var shareuid='<%=shareuid%>';
	function openlogin(){
		var username = $("#username").val();
		var password=$("#password").val();
		login(username,password,backurl);
	}
	function openregister(){
		if(playKey!=null&&playKey.length>0){
		window.location.href="<%=request.getContextPath()%>/h5/reg.jsp?backurl="+backurl;
			
		}else{
			window.location.href="<%=request.getContextPath()%>/h5/reg.jsp?playKey="+playKey+"&shareuid="+shareuid;
		}
	}
	</script>
	
</head>
<body class="login_bg">
<!--头部-->
	<div class="login_head">
		<span class="login_return" onclick="javascript:history.go(-1);"></span>
		<span class="login_head_txt">登录</span>
		<a href="javascript:openregister();" class="login_head_zc">注册</a>
	</div>
<!--登录框-->
<div class="login_wrap">
	<div class="btn_wrap4">
		<input  type="tel" id="username" name="username" placeholder="请输入注册时的手机号" autocomplete="off" class="LoginInputPhone2" maxlength="11">
	</div>
	<div class="btn_wrap3">
		<input  type="password" id="password" name="password" placeholder="请输入密码" autocomplete="off" class="LoginInputpassword">
	</div>
	<!-- <p class="f_password tr"><a href="password.html">忘记密码?</a></p> -->
	<a href="javascript:openlogin();" class="login_btn" id="loginBtn" name="loginBtn">登录</a>
	<div class="f-hr"><hr style="border:1px solid #999;" />
	<!--    <p class="title_top2">第三方登录</p>
	 </div>
	    <div class="icon_wrap_login2">
	    	<a href="javascript:void(0);"><i class="icon_wechat"></i>微信好友</a>
	    	<a href="javascript:void(0);"><i class="icon_qq"></i>QQ好友</a>
	    	<a href="javascript:void(0);"><i class="icon_weibo"></i>新浪微博</a>
	    </div> 
	<a href="javascript:void(0);" class="login_btn2">登录</a>-->
</div>


</body>
</html>