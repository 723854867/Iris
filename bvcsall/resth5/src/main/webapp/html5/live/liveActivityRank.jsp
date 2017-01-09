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

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title></title>
    <meta id="shareImg" itemprop="image" content=""><!-- 图片 -->
    <meta id="shareDescription" name="description" itemprop="description" content="">
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/live/liveActivityRank.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
<input id="liveActivityId" type="hidden" value = '${liveActivityId }' />
<jsp:include page="../head.jsp" />
<div class="main">
	
</div>
<div class="checkContent">
	<div class="checkNav">
		<ul>
			<li class="latest on">最新</li>
			<li class="rank">排行榜</li>
			<li class="canyu"><a href="javascript:void(0);"></a></li>
		</ul>
	</div>
	<div class="navContent">
		<div class="latestContent">
			<ul>
				
			</ul>
		</div>
		<div class="rankContent">
			<ul>
			
			</ul>
		</div>
	</div>
	<div class='bottomDownload'>
		<a class='openwopai'><img width='250' src='<%=request.getContextPath()%>/img/icons-2x/DownloadBlivebutton.png' ></a>
	</div>
</div>
<div class="skyBox">
	<img src="<%=request.getContextPath()%>/img/gobrowser.png" alt=""/>
</div>
<div class="skyBox-anz">
	<img src="<%=request.getContextPath()%>/img/anz-browser.png" alt=""/>
</div>
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/liveActivityRank.js" src="<%=request.getContextPath()%>/js/libs/require.js"></script>