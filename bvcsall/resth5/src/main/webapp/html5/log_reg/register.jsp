<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>注册</title>
	<meta name="viewport"
		  content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
	<meta name="format-detection" content="telephone=no,email=no,address=no"/>
	<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
	<meta name="apple-mobile-web-app-capable" content="yes"/>
	<meta name="Pramgma" content="no-cache"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/log_reg/register.css?t=<%=new java.util.Date().getTime()%>">
     <script type="text/javascript" src="<%=request.getContextPath()%>/js/libs/jquery-2.1.4.js"></script>
</head>
<body>
<jsp:include  page="../cache.jsp"/>
<div class="main">
	<header>
		<span class="indexRecommend"><a href="<%=request.getContextPath()%>/page/homePage">首页推荐</a></span><span class="register">注册</span>
	</header>
	<div class="regContent">
		<p>
			<span class="login_icon_id"></span>
			<input id="tel" type="tel" name="tel" placeholder="请输入手机号" />
		</p>
		<div class="line"></div>
		<p>
			<span class="login_icon_code"></span>
			<input id="code" class="codeInput" type="text" name="code" placeholder="短信验证码" maxlength="6" />
			<input id="getCodeBtn" class="btnCode1" type="button" value="获取验证码" readonly >
		</p>
		<!-- <div class="line"></div> -->
		<!-- <p>
			<span class="login_icon_nickname"></span>
			<input id="nickname" type="text" name="nickname" placeholder="请输入昵称" />
		</p> -->
		<div class="line"></div>
		<p>
			<span class="login_icon_password fl"></span>
			<input id="password" class="pwdSH fl" type="password" name="password" placeholder="设置新密码" maxlength="18" />
			<a id="togglePassword" class="pwdSHBtn1 fr" href="javascript:void(0);"></a>
		</p>
	</div>
	<div class="btnReg">
		<input id="regBtn" type="button" value="完成注册">
	</div>
</div>	
<div class="bgBox2">请竖屏浏览</div>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/register" src="<%=request.getContextPath()%>/js/libs/require.js"></script>
