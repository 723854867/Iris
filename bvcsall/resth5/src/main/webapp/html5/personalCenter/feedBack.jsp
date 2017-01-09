<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>意见反馈</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/personalCenter/feedBack.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
	<jsp:include  page="../cache.jsp"/>
	<!-- <h2>意见反馈</h2> -->
	<div class="main">
	<header>
		<span class="feedBackTitke"><a href="javascript:history.go(-1)"></a></span><span class="feedBack">意见反馈</span>
	</header>
	
		<ul>
			<li>视频不清晰<span class="nor"></span><input class="val" type="hidden" /></li>
			<li>视频加载时间过长或加载失败<span class="nor"></span><input class="val" type="hidden" /></li>
			<li>视频播放卡顿<span class="nor"></span><input class="val" type="hidden" /></li>
			<li>找不到想看的视频<span class="nor"></span><input class="val" type="hidden" /></li>
			<li>操作不方便<span class="nor"></span><input class="val" type="hidden" /></li>
		</ul>
		<textarea id="myFeedBack" rows="4" placeholder="请填写您的详细反馈信息，我们会积极采纳！"></textarea>
		<div id="connectWay">
			<input placeholder="请填写您的联系方式（手机或者QQ）" />
		</div>
		<div id="subMit">
			<a>提交反馈</a>
		</div>
	</div>
	<input type="hidden" name="uid" id="uid" value="${uid}">
	<input type="hidden" name="access_token" id="access_token" value="${access_token}">
</body>
</html>

<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/feedBack" src="<%=request.getContextPath()%>/js/libs/require.js"></script>
