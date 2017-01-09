<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>我的关注</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/personalCenter/myAttention.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
	<%-- <div class="loading" style="display:none;width:100%;height:100%;position:absolute;top:0;left:0;background-color:#000;opacity:0.3;z-index:9999;"><img style="position:absolute;top:50%;left:50%;margin-left:-25px" width="50" src="<%=request.getContextPath()%>/img/icons-2x/loading.gif" /></div> --%>
	<jsp:include page="../head.jsp" />
	<div class="main">
		<div class="recommendContent">
			<!-- <div> -->
				<%-- <img class="fl headPortrait" src="<%=request.getContextPath()%>/img/icons-2x/icon_tabbar_profile_sel.png" alt="">
				<img class="user_icon_blue" src="<%=request.getContextPath()%>/img/icons-2x/user_icon_blue.png" alt="">
				<span class="reTitle">èå¸½çèå°</span><br>
				<span class="reName">äººæ°:7.3ä¸</span>
				<a href="#" class="allready-addAttention fr"></a> --%>
			<!-- </div> -->
		</div>
		<div class="loadMore" id="btnMore" href="javascript:void(0);">加载更多</div>
		<div class="downBar">
			<span class="downClose"></span>
			<a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载我拍，查看更多</a>
		</div>
	</div>	
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/myAttention" src="<%=request.getContextPath()%>/js/libs/require.js"></script>