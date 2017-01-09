<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
	WebClientUtils cu=WebClientUtils.getInstance();
	String cdnurl=cu.loadConfigUrl("html5", "interfaceurl");
	String imageurl=cu.loadConfigUrl("html5", "imageurl");
	String interfaceurl=cu.loadConfigUrl("html5", "interfaceurl");
	if(cdnurl==null){
		cdnurl="http://api.wopaitv.com";
	}
	if(imageurl==null){
		imageurl="http://api.wopaitv.com";
	}
	if(interfaceurl==null){
		interfaceurl="http://api.wopaitv.com";
	}
//    interfaceurl = "http://localhost:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>我是B星人</title>
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/bstar/css/style.css"/>
	<script src="<%=request.getContextPath()%>/html5/app_within/bstar/js/jquery-2.1.0.min.js"></script>
</head>

<body>
<input type="hidden" id="interurl" value="<%=interfaceurl%>"/>
<div class="main">
	<img src="<%=request.getContextPath()%>/html5/app_within/bstar/img/bj.png" alt="" class="bj"/>
	<div class="form">
		<img src="<%=request.getContextPath()%>/html5/app_within/bstar/img/form-bj.png" class="form-bj"/>
		<form class="form-box">
			<div class="form-item">
				<input type="text" name='uid' class="txt3 txt-check" placeholder="用户ID"/>
				<span class="hideSele">
					<span class="showSele">男 </span><em class="arrow"></em>
					<select name="sex" class="txt1">
						<option value="1">男</option>
						<option value="0">女</option>
					</select>
				</span>

			</div>
			<div class="form-item">
				<input type="text" name="number" placeholder="QQ号/微信号" class="txt4 txt-check"/>
			</div>
			<div class="form-item">
				<input type="phone" name='phone' placeholder="手机号" class="txt4 txt-check"/>
			</div>
			<div class="form-item">
				<input type="text" name='code' placeholder="验证码" class="txt2 txt-check"/>
				<a href='javascript:;' class="yz-btn">获取验证码</a>
			</div>
		</form>
	</div>
	<div class="sub-btn"><img src="<%=request.getContextPath()%>/html5/app_within/bstar/img/sub-btn.png" alt=""/></div>
</div>
<script src="<%=request.getContextPath()%>/html5/app_within/bstar/js/index.js"></script>
</body>
</html>