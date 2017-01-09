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
	//interfaceurl = "http://localhost:8080";
	interfaceurl = "http://192.168.0.71:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<c:if test="${empty video.playKey}">
	<title>${room.anchorName }的LIVE直播</title>
	</c:if>
	<c:if test="${not empty video.playKey}">
	<title>${room.anchorName }的LIVE回放</title>
	</c:if>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<meta name="description" itemprop="description" content="小伙伴们~我的直播开始啦~还在等什么~快来找我玩~">
    <meta itemprop="name" content="${room.anchorName }的LIVE直播">
    <meta itemprop="image" content="http://static.acg12.com/uploads/2015/08/d338720078a8177c2ca568e91f36e4e7-192x192.png"><!-- 图片 -->
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/live/live.css?t=<%=new java.util.Date().getTime()%>">

   <%--  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/libs/video-js.css"> --%>
    <script src="<%=request.getContextPath()%>/js/libs/video.js"></script>
   <%--  <script src="<%=request.getContextPath()%>/js/libs/videojs-hls.js"></script> --%>
</head>
<body style="width:100%;height:100%; background:#000;">
	<div id="videoBox" class="videoBox" style="overflow:hidden;width:100%;height:100%;">
		
	</div>
	<input id="chaturl" type="hidden" value="<%=chaturl%>" />
	<input id="roomId" type="hidden" value="${room.id}" />
	<input id="videoURL" type="hidden" value="${room.hlsLiveUrl}" />
	<input id="posterURL" type="hidden" value="${room.roomPic}" />
	<input id="reviewURL" type="hidden" value="${video.playKey}" />
	<input id="reviewVideoURL" type="hidden" value="${video.videoPic}" />
	<input id="reviewVideoDuration" type="hidden" value="${video.duration}" />
	<jsp:include page="../cache.jsp" />
	
	<%-- <img style="opacity:0;position:absolute;display:block;top:0;" class='userPortrait-1' src="" data-userPic='${room.anchorPic }' data-userId="${room.creatorId }" onerror='this.src="<%=request.getContextPath()%>/img/portrait.png"'  alt="" > --%>
	<div class="main">
	<div class="bgBox"></div>
		<div class="showBox">
			<img class='closeBtn' width='25' src="<%=request.getContextPath()%>/img/icons-2x/close.png">
			<img class='imgBox' src="">
			<a class='openwopai'><img class='downloadBtn' width='210' src="<%=request.getContextPath()%>/img/personalBg/download.png"></a>
		</div>
		<div class='funPerson'>
			<dl>
				<dt>
					<img class='userPortrait-1' src="" data-userPic='${room.anchorPic }' data-userId="${room.creatorId }" onerror='this.src="<%=request.getContextPath()%>/img/portrait.png"'  alt="" >
					<c:if test="${ room.anchorVstat eq 2}">
						<img class='addV'src="../../img/icons-2x/user_icon_yellow.png"  alt="服务器错误，请重新加载">
					</c:if>
					<c:if test="${ room.anchorVstat eq 1}">
						<img class='addV' src="../../img/icons-2x/user_icon_blue.png"  alt="服务器错误，请重新加载">
					</c:if>
					<c:if test="${ room.anchorVstat eq 3}">
						<img class='addV'src="../../img/icons-2x/user_icon_green.png" alt="">
					</c:if>
				</dt>
				<dd>
					<p class="duration">${room.anchorName }</p>
					<p id='videoDuration'>当前<span class="onlineCount">${room.onlineNumber }</span>人/共<span class="allCount">${room.maxAccessNumber }</span>人</p>
				</dd>
			</dl>
			<a href="javascript:;" class='atten'><em class="attenBtn" data-userId="${room.creatorId }"></em></a>
		</div>
		<div class="audienceImg">
			<ul>
				
			</ul>
		</div>
		
		<div class='openApp'>
			<img id='playBtn' class='playBtn' width='50' src="<%=request.getContextPath()%>/img/icons-2x/play-grey-120.png">
			<div class="barBlock"></div>
			<div class='boxOver'>
				<div id="chatContent">
					<ul>
					</ul>
				</div>
			</div>
			
			<!-- <a href="http://wopaitv.com" class="open-app-btn"></a> -->
			<!-- 工具栏 -->
			<ul class='settings'>
				<li class="hide"></li>
				<li class="message"></li>
				<li class="share shareBtn" data-name="${room.anchorName }" data-videoid="" data-userId='' data-desc="${room.anchorSignature}" data-videoPic="${room.roomPic}"></li>
				<li class="gift"></li>
				<li class="open-app-btn"><a class='openwopai'></a></li>
			</ul>
		</div>
		<div class="liveEnd">
		<h2>此直播已结束</h2>
		<dl>
			<dt>
				<img class='userPortrait-1' src="" data-userPic='${room.anchorPic }' data-userId="${room.creatorId }" onerror='this.src="<%=request.getContextPath()%>/img/portrait.png"'  alt="" >
				<c:if test="${ room.anchorVstat eq 2}">
					<img class='addV'src="../../img/icons-2x/user_icon_yellow.png"  alt="服务器错误，请重新加载">
				</c:if>
				<c:if test="${ room.anchorVstat eq 1}">
					<img class='addV' src="../../img/icons-2x/user_icon_blue.png"  alt="服务器错误，请重新加载">
				</c:if>
				<c:if test="${ room.anchorVstat eq 3}">
					<img class='addV'src="../../img/icons-2x/user_icon_green.png" alt="">
				</c:if>
			</dt>
		</dl>
		<p>${room.anchorName}</p>
		<a href="javascript:;" class='atten'><em class="attenBtn" data-userId="${room.creatorId }"></em></a>
		<ul class='circleBox'>
			<li>
				<em id='timestamp' data-duration='${room.duration}'></em>
				<p>时长</p>
			</li>
			<li>
				<em class="maxAccessNumber"></em>
				<p>观众</p>
			</li>
			<li>
				<em class="praiseNumber"></em>
				<p>点赞</p>
			</li>
		</ul>
		<div class="reBotton">
			<a class="review"></a>
			<a class="openwopai"></a>
		</div>
	</div>
		<div class="hotRecommend">
			<h2>当前最热直播</h2>
			<div class='videoListBox'>
				<ul>
					
				</ul>
			</div>
		</div>
		<div class='bottomDownload'>
			<a class='openwopai'><img width='250' src='<%=request.getContextPath()%>/img/icons-2x/DownloadBlivebutton.png' ></a>
		</div>
		
	</div>
	
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
<div class="skyBox">
	<img src="<%=request.getContextPath()%>/img/gobrowser.png" alt=""/>
</div>
<div class="skyBox-anz">
	<img src="<%=request.getContextPath()%>/img/anz-browser.png" alt=""/>
</div>
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/live.js" src="<%=request.getContextPath()%>/js/libs/require.js"></script>
