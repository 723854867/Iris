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
<html>
<head> 
	<!--  
    <meta name="viewport" content="width=device-width, minimal-ui">
    <meta name="format-detection" content="telephone=no, email=no">
    -->
    <meta charset="utf-8"> 
    <meta content="telephone=no" name="format-detection" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="default" name="apple-mobile-web-app-status-bar-style"> 
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, minimal-ui" name="viewport">
    
    <title>${user.name} 邀请您加入【LIVE】</title>
    <meta name="description" content="${video.description}">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/index/invite.css">
</head>
<body>
<jsp:include page="/html5/head.jsp" />
<div class="main">
    <div class="main-inner">
    	<img src="<%=request.getContextPath()%>/img/personalBg/activity.jpg" />
    	<div class="personalPor">
    		<img class='userPortrait-1' src="" lazy_src="" data-userPic='${user.pic }' data-userId='${user.id }' data-uid='${user.id }' onerror='this.src="<%=request.getContextPath()%>/img/icons-2x/user_icon_24px.png"'  alt="" >
    		<%-- <p>${user.name }</p> --%>
    		<c:choose>
				<c:when test="${user.name != '' && user.name != null}">
					<p>${user.name }</p> 
				</c:when>
				<c:otherwise> 
					<p>我就是主角</p>
				</c:otherwise>
			</c:choose>
    	</div>
    	<div class="inviteWords">
    		<c:choose>
				<c:when test="${user.name != '' && user.name != null}">
					<p>关注“${user.name }”,<br />可以看到奴家的视频哦！</p>
				</c:when>
				<c:otherwise> 
					<p></p><br>
				</c:otherwise>
			</c:choose>
    		<a class="downloadApp" href="http://www.wopaitv.com"><img alt="" src="<%=request.getContextPath()%>/img/personalBg/downloadApp.png"></a>
    	</div>
		<div class='activeUseList'>
			<div class='videoList1'>
				<ul>
					<c:forEach var="video" items="${topHot}">
					<li>
						<div class='listUserMsg'>
							<dl>
								<dt>
									<img class='userPortrait' src="" lazy_src="" data-userPic='${video.user.pic }' data-userId='${video.user.id }' data-uid='${ruser.id }' onerror='this.src="<%=request.getContextPath()%>/img/icons-2x/user_icon_24px.png"'  alt="" >
									<c:if test="${ video.user.vipStat eq 2}">
										<img class='addV' src="<%=request.getContextPath()%>/img/icons-2x/user_icon_yellow.png"  alt="">
									</c:if>
									<c:if test="${ video.user.vipStat eq 1}">
										<img class='addV' src="<%=request.getContextPath()%>/img/icons-2x/user_icon_blue.png"  alt="">
									</c:if>
									<c:if test="${ video.user.vipStat eq 3}">
										<img class='addV' src="<%=request.getContextPath()%>/img/icons-2x/user_icon_green.png" alt="">
									</c:if>
								</dt>
								<dd>
									<p class="personalName">${video.user.name }</p>
									<p class="myDate" data-time='${video.createDate}'></p>
								</dd>
								<dd>
									<em class='playIcon'></em><span class='playData1'>${video.showPlayCount}</span>
								</dd>
							</dl>
						</div>
						<div class='playBox' data-videoKey='${video.playKey }' data-videoId='${video.id }'>
							<img class='videoPic' src=""  alt="" lazy_src="" data-videoPic='${video.videoPic }' onerror='this.src="<%=request.getContextPath()%>/img/portrait.png"'>
							<em class="canPlay" data-url='${video.url}'></em>
						</div>
						<span class="videoDescription">${video.description}</span>
					</li>
				</c:forEach>
				</ul>
			</div>
		</div>
    </div>
</div>
<div class="download">
	<a href="http://www.wopaitv.com"><img alt="" src="<%=request.getContextPath()%>/img/personalBg/downloadApp.png"></a>
</div>
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/invite" src="<%=request.getContextPath()%>/js/libs/require.js"></script>