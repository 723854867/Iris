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
	<%-- <meta name="description" itemprop="description" content="${video.description }"> --%>
	<meta itemprop="image" id="cover" content="<%=imageurl%>/restwww/download${video.videoPic }"><!-- 图片 -->
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/video_show.css?t=<%=new java.util.Date().getTime()%>">
	<script src="<%=request.getContextPath()%>/js/libs/require.js?t=10" data-main="<%=request.getContextPath()%>/js/views/video_show"></script>
	<title>${video.description }</title>
</head>
<body>
<jsp:include  page="../head.jsp"/>
<input type="hidden" id="videoId" value="${video.id }"/>
<input type="hidden" id="creatId" value="${video.creatorId }"/>
<%--//默认调取评论数需要视频ID 用户UserID--%>
<!-- 内容 -->
<div class='main'>
	<div class='videoList1'>
		<c:if test="${video.type eq 1}">
			<div class='playBox' data-videoKey='${video.playKey }' data-videoId='${video.id }'>
				<div class="loadingGif" style="width:30px;height:30px;min-height:30px;position:absolute;left:0;top:0;display:none;z-index:999999;background-color:#000000;opacity:0.6;width:100%;height:50%;color:#ffffff;text-align:center;padding-top:50%;font-size:18px;"></div>
				<img data-videoPic="${video.videoPic }" src="" alt="" class='videoPic'>
				<em class='canPlay'></em>
			</div>
		</c:if>
		<c:if test="${video.type eq 3}">
			<div class='playBox' data-videoKey='${video.playKey }' data-videoId='${video.id }'>
				<img data-videoPic="${video.videoPic }" src="" class="videoPic-img" alt="">
			</div>
		</c:if>
		<div class='listUserMsg'>
			<dl>
				<dt>
					<img class="userImg userPortrait-1" data-userPic="${video.user.pic }" data-userId="${video.user.id }" src="" onerror="this.src='<%=request.getContextPath()%>/img/icons-2x/user_icon_24px.png'" alt="">
					<c:if test="${ video.user.vipStat eq 1}">
						<em class="userV1"></em>
					</c:if><c:if test="${ video.user.vipStat eq 2}">
						<em class="userV2"></em>
					</c:if>
					<c:if test="${ video.user.vipStat eq 3}">
						<em class="userV3"></em>
					</c:if>
					<c:if test="${ video.user.vipStat eq 0}">
							
					</c:if>
				</dt>
				<dd>
					<p>${video.user.name }</p>
					<p class="myDate" data-time='${video.createDate }'></p>
				</dd>
				<dd>
					<a href="javascript:;" class='playData'>
						<img src="<%=request.getContextPath()%>/img/icons-2x/taIcon.png">
					</a>
				</dd>
			</dl>
		</div>
		<p class='liverSentence'>${video.description }<img src="<%=request.getContextPath()%>/img/icons-2x/corner.png"></p>
		<div class='shareBox'>
			<div class='read'>
				<em></em>
				<span>${video.showPlayCount}</span>
			</div>
			<div class='commit'>
				<em></em>
				<span>${video.evaluationCount }</span>
			</div>
			<div class='zan'>
				<em></em>
				<span>${ video.praiseCount }</span>
			</div>
			<div class='Forward' data-name = '${video.user.name }' data-desc = '${video.description }' data-videopic = '${video.videoPic }' data-videoId = '${video.id }' data-userId = '${video.user.id }'>
				<em></em>
					<%-- <span>${ video.favoriteCount }</span> --%>
			</div>
		</div>
	</div>
	<div class="hotRecommend">
		<h2>热门直播推荐</h2>
		<div class='videoListBox'>
			<ul>
				
			</ul>
		</div>
	</div>
	<div class='commentBox'>
		<h2>评论</h2>
		<div class="evalutionListCon"></div>
	</div>
	<div class='bottomBanner'>
		<a href='javascript:void(0);'>LIVE直播，薛之谦出任第五位导师！</a>
	</div>
</div>
<div class="icon"></div>
<div class="skyBox">
	<img src="<%=request.getContextPath()%>/img/gobrowser.png" alt=""/>
</div>
<div class="skyBox-anz">
	<img src="<%=request.getContextPath()%>/img/anz-browser.png" alt=""/>
</div>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>