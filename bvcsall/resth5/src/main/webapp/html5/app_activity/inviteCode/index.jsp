<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
	WebClientUtils cu=WebClientUtils.getInstance();
	String cdnurl=cu.loadConfigUrl("html5", "cdnurl");
	String imageurl=cu.loadConfigUrl("html5", "imageurl");
	String interfaceurl=cu.loadConfigUrl("html5", "interfaceurl");
	if(cdnurl==null){
		cdnurl="http://cdn.wopaitv.com";
	}
	if(imageurl==null){
		imageurl="http://api.wopaitv.com";
	}
	if(interfaceurl==null){
		interfaceurl="http://api.wopaitv.com";
	}
	//interfaceurl = "http://192.168.151.88:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title></title>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/html5/app_activity/inviteCode/style.css">
</head>
<body>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
	<div class="inviteBox">
		<p><input id="userId" type="number" placeholder="输入ID"></p>
		<p><input id="inviteCode" type="text" placeholder="邀请码"></p>
		<button id="btn">发送邀请</button>
	</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/html5/app_activity/inviteCode/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/html5/app_activity/inviteCode/index.js"></script>
</body>
</html>