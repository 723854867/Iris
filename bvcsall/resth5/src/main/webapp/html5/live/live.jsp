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
	//interfaceurl = "http://192.168.151.181:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<c:if test="${empty room.hlsLiveUrl && empty video.playKey}">
		<title>${room.anchorName }的LIVE直播</title>
	</c:if>
	<c:if test="${not empty room.hlsLiveUrl}">
		<title>${room.anchorName }的LIVE直播</title>
	</c:if>
	<c:if test="${empty room.hlsLiveUrl && not empty video.playKey}">
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
    <script src="<%=request.getContextPath()%>/js/common/video-dev.js"></script>
    <script src="<%=request.getContextPath()%>/js/common/iphone-inline-video.browser.js"></script>
    <script src="<%=request.getContextPath()%>/js/common/videojs-hls.js"></script>
    <script src="<%=request.getContextPath()%>/js/common/videojs-media-sources.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
   <%--  <script src="<%=request.getContextPath()%>/js/libs/videojs-hls.js"></script> --%>
</head>
<style>
video::-webkit-media-controls-play-button,
video::-webkit-media-controls-start-playback-button {
    opacity: 0;
    pointer-events: none;
    width: 5px;
}
.loading{
	display:none;
}
</style>
<body style="width:100%;height:100%; background:#000;">
<img style="position:absolute;display:block;top:0;opacity:0;width:100px;" class='userPortrait-1' src="" data-userPic='${room.roomPic }' data-userId="${room.creatorId }" alt="" >
	<c:if test="${not empty room.hlsLiveUrl}">
		<div id="videoBox" class="videoBox" style="overflow:hidden;width:100%;height:100%;position:relative;">
			<img class="loading" style="position:absolute;display:none;top:200px;left:50%;width:150px;margin-left:-75px;z-index:999;" src="<%=request.getContextPath()%>/img/icons-2x/gif_live_waiting.gif">
			<img style="position:absolute;display:block;top:0;-webkit-transform:scale(2.5);transform:scale(2.5);" class='userPortrait-1' src="" data-userPic='${room.roomPic }' data-userId="${room.creatorId }" alt="" >
			<%-- <video id="video" class="" autoplay="autoplay" width="100%" preload="auto" x-webkit-playsinline webkit-playsinline="controls"><source src="${room.hlsLiveUrl}" type="application/x-mpegURL"></video> --%>
		</div>
	</c:if>
	<c:if test="${empty room.hlsLiveUrl  && not empty video.playKey}">
		<div id="videoBox" class="videoBox" style="overflow:hidden;width:100%;height:100%;position:relative; z-index:999;">
			<img class="loading" style="position:absolute;display:block;top:200px;left:50%;width:150px;margin-left:-75px;z-index:999;display:none;" src="<%=request.getContextPath()%>/img/icons-2x/gif_live_waiting.gif">
			<img style="position:absolute;display:block;top:0;-webkit-transform:scale(2.5);transform:scale(2.5);" class='userPortrait-1' src="" data-userPic='${room.roomPic }' data-userId="${room.creatorId }" alt="" >
			<%-- <video id="video" class="" autoplay="autoplay" width="100%" preload="auto" x-webkit-playsinline webkit-playsinline="controls"><source src="${video.playKey}" type="application/x-mpegURL"></video> --%>
		</div>
	</c:if>
	
	<input id="chaturl" type="hidden" value="<%=chaturl%>" />
	<input id="roomId" type="hidden" value="${room.id}" />
	<input id="videoURL" type="hidden" value="${room.hlsLiveUrl}" />
	<input id="posterURL" type="hidden" value="${room.roomPic}" />
	<input id="videoId" type="hidden" value="${video.id}" />
	<input id="reviewURL" type="hidden" value="${video.playKey}" />
	<input id="reviewVideoURL" type="hidden" value="${video.videoPic}" />
	<input id="reviewVideoDuration" type="hidden" value="${video.playCountToday}" />
	<input id="roomCreatorId" type="hidden" value="${room.creatorId }" />
	<input id="shareImg" type="hidden" value="${shareImg }" />
	<input id="shareTitle" type="hidden" value="${shareTitle }" />
	<input id="shareText" type="hidden" value="${shareText }" />
	<input id="timestamp" type="hidden" value="${timestamp }" />
	<input id="noncestr" type="hidden" value="${noncestr }" />
	<input id="signature" type="hidden" value="${signature }" />
	<jsp:include page="../cache.jsp" />
	
	<div class="main">
	<div class="topBlock">
		<img class="logo" width="40" src="<%=request.getContextPath()%>/img/icons-2x/87.png">
		<dl>
			<p>LIVE直播</p>
			<p>中国新歌声第五战队集结</p>
		</dl>
		<a class="joinBtn" href="javascript:void(0);">立即参战</a>
	</div>
	<div class="bgBox"></div>
		<div class="showBox">
			<img class='closeBtn' width='25' src="<%=request.getContextPath()%>/img/icons-2x/close.png">
			<img class='imgBox' src="">
			<div class='words'></div>
			<a class='openwopai'><img class='downloadBtn' width='128' src="<%=request.getContextPath()%>/img/icons-2x/openApp_new.png"></a>
		</div>
		<img id='playBtn' class='playBtn' width='100' src="<%=request.getContextPath()%>/img/icons-2x/play.png">
		<!-- <div class="audienceImg">
			<ul>
				
			</ul>
		</div> -->
		
		<%-- <div class='openApp'>
			
			<div class='funPerson'>
				
				<dl>
					<dt>
						<img class='userPortrait' src="" data-userPic='${room.anchorPic }' data-userId="${room.creatorId }" alt="" >
						<c:if test="${ room.anchorVstat eq 2}">
							<img class='addV'src="<%=request.getContextPath()%>/img/icons-2x/yellow_icon.png"  alt="">
						</c:if>
						<c:if test="${ room.anchorVstat eq 1}">
							<img class='addV' src="<%=request.getContextPath()%>/img/icons-2x/blue_icon.png"  alt="">
						</c:if>
						<c:if test="${ room.anchorVstat eq 3}">
							<img class='addV'src="<%=request.getContextPath()%>/img/icons-2x/green_icon.png" alt="">
						</c:if>
					</dt>
					<dd>
						<p class="duration">${room.anchorName }</p>
						<p id='videoDuration'><span class="onlineCount">${room.onlineNumber }</span>/<span class="allCount">${room.maxAccessNumber }</span></p>
					</dd>
					<dd>
						<a href="javascript:;" class='atten'><em class="attenBtn" data-userId="${room.creatorId }"></em></a>
					</dd>
				</dl>
				<a href="javascript:;" class='atten'><em class="attenBtn" data-userId="${room.creatorId }"></em></a>
				<a class="LiveID">ID:${room.creatorId}</a>
			</div>
			<img id='playBtn' class='playBtn' width='100' src="<%=request.getContextPath()%>/img/icons-2x/play.png">
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
				<!-- <li class="open-app-btn"><a class='openwopai'></a></li> -->
			</ul>
		</div>
		--%>
		<div class="liveEnd">
		<%-- <div class="topBlock">
			<img class="logo" width="40" src="<%=request.getContextPath()%>/img/icons-2x/87.png">
			<dl>
				<p>LIVE直播</p>
				<p>中国新歌声第五战队集结</p>
			</dl>
			<a class="joinBtn" href="javascript:void(0);">立即参战</a>
		</div> --%>
		<h2>直播已结束</h2>
		<dl class="info">
			<dt>
				<img class='userPortrait-1' src="" data-userPic='${room.anchorPic }' data-userId="${room.creatorId }" onerror='this.src="<%=request.getContextPath()%>/img/portrait.png"'  alt="" >
				<c:if test="${ room.anchorVstat eq 2}">
					<img class='addV'src="<%=request.getContextPath()%>/img/icons-2x/yellow_icon.png"  alt="服务器错误，请重新加载">
				</c:if>
				<c:if test="${ room.anchorVstat eq 1}">
					<img class='addV' src="<%=request.getContextPath()%>/img/icons-2x/blue_icon.png"  alt="服务器错误，请重新加载">
				</c:if>
				<c:if test="${ room.anchorVstat eq 3}">
					<img class='addV'src="<%=request.getContextPath()%>/img/icons-2x/green_icon.png" alt="">
				</c:if>
			</dt>
		</dl>
		<p>${room.anchorName}</p>
		<p>ID:${room.creatorId }</p>
		<p class="maxAccessNumber"></p>
		<a href="javascript:;" class='atten'><em class="attenBtn" data-userId="${room.creatorId }"></em></a>
		<%--<ul class='circleBox'>--%>
			<%--<li>--%>
				<%--<em id='timestamp1' data-duration='${room.duration}'></em>--%>
				<%--<p>时长</p>--%>
			<%--</li>--%>
			<%--<li>--%>
				<%--<em class="maxAccessNumber"></em>--%>
				<%--<p>观众</p>--%>
			<%--</li>--%>
			<%--<li>--%>
				<%--<em class="praiseNumber"></em>--%>
				<%--<p>点赞</p>--%>
			<%--</li>--%>
		<%--</ul>--%>
		<!-- <div class="reBotton">
			<a class="review"></a>
			<a class="openwopai"></a>
		</div> -->
	</div> 
		
		
	</div>
	<div class="hotRecommend">
			<h2><strong>I&nbsp;</strong>热门直播</h2>
			<div class='videoListBox'>
				<ul>
					
				</ul>
			</div>
		</div>
		<div class='bottomDownload'>
			LIVE直播，薛之谦出任第五位导师！
		</div>
<div class="skyBox">
	<img src="<%=request.getContextPath()%>/img/gobrowser.png" alt=""/>
</div>
<div class="skyBox-anz">
	<img src="<%=request.getContextPath()%>/img/anz-browser.png" alt=""/>
</div>
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/live.js" src="<%=request.getContextPath()%>/js/libs/require.js"></script>
<%--下面live 上面我玩--%>

<script src="http://s11.cnzz.com/z_stat.php?id=1259916005&web_id=1259916005" language="JavaScript"></script>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

