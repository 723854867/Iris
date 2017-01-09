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
//  interfaceurl = "http://localhost:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>周五没看够，周末Live go!</title>
	<meta name="description" content="想跟48位学员零距离互动，想听学员和主播们现场飙歌，还想探听最新鲜导师八卦，锁定《中国新歌声LIVE战队》！" />
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
</head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_activity/new-song/css/active.css"/>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js "></script>
<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/jquery-2.1.0.min.js"></script>

<body>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>

<input id="timestamp" type="hidden" value="${timestamp }" />
<input id="noncestr" type="hidden" value="${noncestr }" />
<input id="signature" type="hidden" value="${signature }" />
<div class="main">
	<div class='top'>
		<img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/singSport/bg_top.jpg" alt="">
		<img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/singSport/bg_middle.jpg" alt="">
		<img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/singSport/bg_bottom.jpg" alt="">
	</div>
	<div class='btn_box'>
		<a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/singSport/download.png" class='download'></a>
		<img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/singSport/shareBtn.png" class='share'>
	</div>
</div>

</body>
</html>
<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/common.js"></script>
<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/active.js"></script>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
