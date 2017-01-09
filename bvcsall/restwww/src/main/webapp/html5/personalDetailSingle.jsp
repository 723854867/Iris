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
	<title>${ruser.name }的个人主页</title>
	<meta name="viewport" content="width=device-width,target-densitydpi=high-dpi,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/personalCenter/personalDetailSingle.css?t=<%=new java.util.Date().getTime()%>" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/Connect.css?t=<%=new java.util.Date().getTime()%>" />
</head>
<body>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
	<div class="header">
		<a class="head-logo" href="http://cdn.wopaitv.com/restwww/html5/index"></a>
		<a href="http://www.wopaitv.com" id="downloadBtn" class="js-span-a btn-bg1 btn-download">
			<i class="icon-download"></i> 下载我拍
		</a>
	</div>
	
	<div class="personalBg">
		<img class='userHomePic' data-homePicSrc='${ruser.pic }' src=''   alt="服务器错误，请重新加载" />
		<ul>
			<li><strong>${ ruser.name }</strong>
			<c:if test="${ruser.sex eq 0}">
				<img id="sex" src="../img/icons-2x/prof_icon_woman.png"  alt="服务器错误，请重新加载">
			</c:if>
			<c:if test="${ruser.sex eq 1}">
				<img id="sex" src="../img/icons-2x/prof_icon_man.png"  alt="服务器错误，请重新加载">
			</c:if>
			
			<c:if test="${ ruser.vipStat eq 0}">
				<img src="../img/icons-2x/user_icon_yellow.png"  alt="服务器错误，请重新加载">
			</c:if>
			<c:if test="${ ruser.vipStat eq 1}">
				<img src="../img/icons-2x/user_icon_blue.png"  alt="服务器错误，请重新加载">
			</c:if>
			<c:if test="${ ruser.vipStat eq 2}">
				
			</c:if>
			</li>
			<li>${ ruser.signature}</li>
			<li>关注 ${ ruser.attentionCount }   |   粉丝 ${ ruser.fansCount }</li>
		</ul>
	</div>

	<div class='activeUseList'>
		<div class='videoList1'>
			<ul>
			<c:forEach var="video" items="${videoList}" varStatus="status" >
				<li>
					<div class='listUserMsg'>
						<dl>
							<dt><img class='userPortrait' src="" data-userPic='${ruser.pic }' ><em class='addV'></em></dt>
							<dd>
								
							<c:if test="${video.isForward eq 1}">
								<p class="personalName">${ruser.name }转发的${video.user.name }</p>
							</c:if>
							<c:if test="${video.isForward eq 0}">
								<p class="personalName">${ruser.name }</p>
							</c:if>
								<p class="myDate" data-time='${video.createDate}'></p>
							</dd>
							<dd>
								<em class='playIcon'></em><span class='playData'>${video.showPlayCount}</span>
							</dd>
						</dl>
					</div>
					<div class='playBox' data-videoId='${video.playKey }'>
						
							<img class='videoPic' src="" onerror='this.src="../img/portrait.png"' alt="服务器错误，请重新加载"   alt="服务器错误，请重新加载" data-videoPic='${video.videoPic }'>
							<em class="canPlay" data-url='${video.url}'></em>
						
					</div>
				</li>
			</c:forEach>
			</ul>
		</div>
	</div>
	<footer>
		更多视频请去客户端查看
	</footer>
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/personalDetailSingle" src="<%=request.getContextPath()%>/js/libs/require.js"></script>

