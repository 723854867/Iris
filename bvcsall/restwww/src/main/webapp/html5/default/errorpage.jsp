<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
	WebClientUtils cu=WebClientUtils.getInstance();
	String cdnurl=cu.loadConfigUrl("html5", "cdnurl");
	String imageurl=cu.loadConfigUrl("html5", "imageurl");
	if(cdnurl==null){
		cdnurl="http://cdn.wopaitv.com";
	}
	if(imageurl==null){
		imageurl="http://api.wopaitv.com";
	}

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/index.css">
	<title>地址错误</title>
</head>
<body>
<jsp:include  page="../head.jsp"/>
<%--图片地址--%>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<%--视频地址--%>
<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
<div class='main-error'>
	<img src="<%=request.getContextPath()%>/img/404.jpg" />
	<br><a href="/restwww/page/homePage"><h2>返回首页</h2></a>
</div>
<div class='downBar'><a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载我拍，查看更多精彩</a></div>

</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/head" src="<%=request.getContextPath()%>/js/libs/require.js"></script>









