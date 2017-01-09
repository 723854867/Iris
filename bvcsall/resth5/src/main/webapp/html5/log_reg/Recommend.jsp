<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>推荐</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/log_reg/Recommend.css?t=<%=new java.util.Date().getTime()%>">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/Connect.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
	<jsp:include page="../head.jsp" />
	<div class="main">
	<div class="recommendContent">
		<p>
			恭喜注册成功！
			<a class="allAttention" href="javascript:void(0);">一键关注</a>
			<a href="<%=request.getContextPath()%>/page/homePage">跳过</a>
		</p>
		<c:forEach var="ruser" items="${ruserList}" varStatus="status" >
			<div>
				<img class="fl userPortrait" src="<%=request.getContextPath()%>/img/personalBg/demo.jpg" alt="" data-userPic="${ ruser.pic }">
				<c:if test="${ ruser.vipStat eq 2}">
					<img class="addV" src="../../img/icons-2x/user_icon_yellow.png"  alt="">
				</c:if>
				<c:if test="${ ruser.vipStat eq 1}">
					<img class="addV" src="../../img/icons-2x/user_icon_blue.png"  alt="">
				</c:if>
				<c:if test="${ ruser.vipStat eq 3}">
					<img class="addV" src="../../img/icons-2x/user_icon_green.png" alt="">
				</c:if>
				<c:if test="${ ruser.vipStat eq 0}">
					
				</c:if>
				<span class="reTitle">${ruser.name }</span><br>
				<span class="reName">${ruser.signature }</span>
				<!-- <a href="#" class="addAttention fr ww"></a> -->
				<a href="javascript:;" class='playData'><em class="attenBtn" data-isAttention="${ruser.isAttention}" data-userId="${ruser.id}"></em></a>
			</div>
		</c:forEach>
	</div>
	</div>
	<div class="bgBox2">请竖屏浏览</div>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/Recommend" src="<%=request.getContextPath()%>/js/libs/require.js"></script>
