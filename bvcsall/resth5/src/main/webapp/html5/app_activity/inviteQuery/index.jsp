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
	//interfaceurl = "http://localhost:8080";

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>LIVE邀请查询</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/html5/app_activity/inviteQuery/style.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/html5/app_activity/inviteQuery/jquery-2.1.4.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/html5/app_activity/inviteQuery/index.js"></script>
</head>
<body>
	<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
	<input type="hidden" id="isAdmin" value="${isAdmin }"/>
	<header>
		<img src="<%=request.getContextPath()%>/html5/app_activity/inviteQuery/images/logo_new.png"><span>LIVE邀请查询</span>
	</header>
	<div class="item">
		<label>　邀请码：</label><input id="inviteCode" type="text">
	</div>
	<div class="item">
		<label>注册日期：</label><input id="startTime" type="date" > <span style='float:left; display:inline-block;padding:0 5px; line-height:20px;'> - </span> <input id="endTime" type="date">
	</div>
	
	<button id="query">查询</button>
	<p class="warning">说明：输入邀请点击查询才能看到数据</p>
	<div id="content">
		<p></p>
		<table cellpadding="0" cellspacing="0">
			
		</table>
	</div>
</body>

</html>