<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>登录</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <!-- <link rel="stylesheet" type="text/css" href="../../../css/Connect.css"> -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/log_reg/login.css?t=<%=new java.util.Date().getTime()%>">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/libs/jquery-2.1.4.js"></script>
</head>
<body>
<jsp:include  page="../cache.jsp"/>
	<div class="main">
	<header>
		<span class="indexRecommend"><a href="<%=request.getContextPath()%>/page/homePage">首页推荐</a></span><span class="login">登录</span>
	</header>
	
	<div class="indexContent">
		<p>
			<span class="login_icon_id"></span>
			<input id="tel" type="tel" name="tel" placeholder="请输入手机号" />
		</p>
		<div class="line"></div>
		<p>
			<span class="login_icon_password"></span>
			<input id="password" type="password" name="password" placeholder="请输入密码" />
		</p>
		<div style="clear:both;"></div>
	</div>
	<div class="btnLogin">
		<input id="loginBtn" type="button" value="登录">
	</div>
	<div class="password">
		<a class="fl" href="<%=request.getContextPath()%>/page/user/forgetPwd">忘记密码</a>
		<a class="fr" href="<%=request.getContextPath()%>/page/user/register">注册</a>
	</div>
	 <div class="thirdParty">
		 <div class="title">
            <span class="halfLine fl"></span>
            <span class="word">第三方登录</span>
            <span class="halfLine fr"></span>
        </div>
        <div class="thirdPartyContent">
            <a href="javascript:void(0);" class="tercentQQ"><span>QQ</span></a>
            <a href="javascript:void(0);" class="webChat"><span>微信</span></a>
            <a href="javascript:void(0);" class="sina"><span>微博</span></a>
        </div>
		<footer>
			我同意《我拍服务使用协议》
		</footer>
	</div>
	</div>
	<div class="bgBox2">请竖屏浏览</div>
	<input type="hidden" name="thirdPartLoginFlag" id="thirdPartLoginFlag" value="${thirdPartLoginFlag}">
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/login" src="<%=request.getContextPath()%>/js/libs/require.js"></script>