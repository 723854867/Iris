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
//	interfaceurl = "http://localhost:8080";
	interfaceurl = "http://192.168.0.71:8080";

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>LIVE直播预告</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/html5/app_within/live_forenotice/css/index.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
<img class='userPortrait-1' style='height:0;' src="" data-userPic='${ruser.pic }' data-userId="${ruser.id }" onerror='this.src="<%=request.getContextPath()%>/img/portrait.png"'  alt="" >
<%--图片地址--%>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<%--视频地址--%>
<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<input type="hidden" id="name" value="${ruser.name }"/>
<input type="hidden" id="description" value="${notice.description }"/>
<input type="hidden" id="pic" value="${notice.pic }"/>
<input type="hidden" id="noticeId" value="${notice.id }"/>
<input type="hidden" id="userId" value="${ruser.id }"/>
	<div class="main">
	<a class='topBanner' href='javascript:void(0);'><img src='<%=request.getContextPath()%>/html5/app_within/live_forenotice/img/icons/topBanner.jpg' /></a>
		<div class='playBox'>
			<img class='videoPic' src=""  alt="" data-videoPic='${notice.pic }' onerror='this.src="../../img/portrait.png"'>
			<a class="canPlay"></a>
			<div class='liveInfo'>
				<p class='noticeTime' data-time='${notice.showTime }'></p>
				<p class='address'>直播地点：<span>LIVE</span></p>
				<p class='name'>
					<em class='liverName'>直  播&nbsp;&nbsp;人：</em>
					<a class='portraitV'>
						<img class='userPortrait-1' src="" data-userPic='${ruser.pic }' data-userId="${ruser.id }" onerror='this.src="<%=request.getContextPath()%>/img/portrait.png"'  alt="" >
						<c:if test="${ ruser.vipStat eq 2}">
							<img class='addV'src="../../img/icons-2x/user_icon_yellow.png"  alt="">
						</c:if>
						<c:if test="${ ruser.vipStat eq 1}">
							<img class='addV' src="../../img/icons-2x/user_icon_blue.png"  alt="">
						</c:if>
						<c:if test="${ ruser.vipStat eq 3}">
							<img class='addV'src="../../img/icons-2x/user_icon_green.png" alt="">
						</c:if>
						<c:if test="${ ruser.vipStat eq 0}">
							
						</c:if>
					</a>
					
					<em class='dis'>${ruser.name }</em>
				</p>
			</div>
		</div>
		<p class='liverSentence'>TA说：${notice.description }</p>
	</div>
	<div class='icons'></div>
	<div class='bottomBanner'>
		<a href='javascript:void(0);'><img src='<%=request.getContextPath()%>/html5/app_within/live_forenotice/img/icons/bottomBanner.png' /></a>
	</div>
	<div class="skyBox">
		<img src="<%=request.getContextPath()%>/img/gobrowser.png" alt=""/>
	</div>
	<div class="skyBox1">
		<img src="<%=request.getContextPath()%>/html5/app_within/live_forenotice/img/mask.png" alt=""/>
	</div>
	<div class="skyBox-anz">
		<img src="<%=request.getContextPath()%>/img/anz-browser.png" alt=""/>
	</div>
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/html5/app_within/live_forenotice/js/views/index.js" src="<%=request.getContextPath()%>/html5/app_within/live_forenotice/js/libs/require.js"></script>