<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>忘记密码</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/log_reg/forgetPwd.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
<jsp:include  page="../cache.jsp"/>
<div class="main">
	<%-- <header>
		<span class="indexRecommend"><a href="javascript:;" onclick="history.go(-1)"><img src="<%=request.getContextPath()%>/img/icons-2x/left_icon.png"></a></span>
		<span class="forgetPwd">找回密码</span>
	</header> --%>
	<div class="regContent">
		<p>
			<!-- <span class="login_icon_id"></span> -->
			<input id="tel" type="tel" name="tel" placeholder="您注册或绑定的手机号" />
		</p>
		<!-- <div class="line"></div> -->
		<p>
			<!-- <span class="login_icon_code"></span> -->
			<input id="code" class="codeInput" type="text" name="code" placeholder="输入验证码" maxlength="6" />
			<input id="getCodeBtn" class="btnCode1" type="button" value="获取验证码" readonly >
		</p>
		<!-- <div class="line"></div> -->
		<!-- <p>
			<span class="login_icon_nickname"></span>
			<input type="text" name="nickname" placeholder="请输入昵称" />
		</p>
		<div class="line"></div> -->
		<p>
			<!-- <span class="login_icon_password fl"></span> -->
			<input id="password" class="pwdSH fl" type="password" name="password" placeholder="新密码（6-20位数字或英文）" maxlength="20" />
			<a id="togglePassword" class="pwdSHBtn1 fr" href="javascript:void(0);"></a>
		</p>
		<div style="clear:both;"></div>
	</div>
	<div class="btnForget">
		<input id="btnForget" type="button" value="提交">
	</div>
</div>	
<div class="bgBox2">请竖屏浏览</div>
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/forgetPwd" src="<%=request.getContextPath()%>/js/libs/require.js"></script>