<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
	WebClientUtils cu=WebClientUtils.getInstance();
	String cdnurl=cu.loadConfigUrl("html5", "interfaceurl");
	String imageurl=cu.loadConfigUrl("html5", "imageurl");
	String interfaceurl=cu.loadConfigUrl("html5", "interfaceurl");
	String chaturl=cu.loadConfigUrl("html5", "chaturl");
	if(cdnurl==null){
		cdnurl="http://api.wopaitv.com";
	}
	if(imageurl==null){
		imageurl="http://api.wopaitv.com";
	}
	if(interfaceurl==null){
		interfaceurl="http://api.wopaitv.com";
	}
	if(chaturl==null){
		chaturl="ws://chat.wopaitv.com/ws/chat";
	}
	//interfaceurl = "http://192.168.151.149:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>我的关注</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/html5/app_activity/busEntertainment/style.css">
	<script src="<%=request.getContextPath()%>/html5/app_activity/busEntertainment/js/jquery-2.1.0.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<body>
	<%--图片地址--%>
	<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
	<%--视频地址--%>
	<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
	<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
	<div class="content">
		<ul>
			
		</ul>
	</div>
	<a class="addMore">加载更多</a>
</body>
</html>
<script src="<%=request.getContextPath()%>/html5/app_activity/busEntertainment/js/common.js"></script>
<script src="<%=request.getContextPath()%>/html5/app_activity/busEntertainment/js/index.js"></script>