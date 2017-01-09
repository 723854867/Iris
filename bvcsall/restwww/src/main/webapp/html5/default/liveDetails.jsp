<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>LIVE直播现场</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <!-- <link rel="stylesheet" type="text/css" href="../../../css/Connect.css"> -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/index/liveDetails.css?t=<%=new java.util.Date().getTime()%>">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/libs/jquery-2.1.4.js"></script>
</head>
<body>
	<input id="roomId" type="hidden" value="${room.id}" />
	<jsp:include page="../cache.jsp" />
	<div class="main">
	<div class="bgBox"></div>
		<div class="showBox">
			<span></span>
			<h2></h2>
			<div class="downloadwopaiApp">
				<a href="javascript:void(0)">
					<em></em>
					<p class="showBox-href">就差一步！下载LIVE  即可观看!</p>
				</a>
			</div>
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
					<p>${room.anchorName}</p>
					<p class="duration" data-duration="${room.duration}"></p>
				</dd>
				<dd>
					<a href="javascript:;" class='atten'><em id="attentionBtn" class="attenBtn" data-userId="${room.creatorId }"></em></a>
				</dd>
			</dl>
		</div>
		<div class='playBox' data-videoKey='' data-videoId=''>
		<div class="blackBox"></div>
			<img class='videoPic' src=""  alt="" data-videoPic='${room.roomPic }' onerror='this.src="../../img/portrait.png"'>
			<a class="canPlay"></a>
			<p>有${room.maxAccessNumber +2000}人观看此直播了！你呢？</p>
			<div class="operation">
				<div class="commit"><em></em></div>
				<div class="praise">
					<span class="">${room.praiseNumber + room.mjPraiseNumber +2000}</span>
					<div class="zan"><em class="off"></em></div>
				</div>
			</div>
		</div>
		<div class='openApp'>
			<a href="http://wopaitv.com" class="open-app-btn">下载LIVE 秀出你自己！</a>
			<div class="fr">
				<span class="shareBtn" data-name="${room.anchorName }" data-videoid="" data-userId='' data-desc="${room.anchorSignature}" data-videoPic="${room.roomPic}">
					<em class='shareIcon'></em>
					<p>转发</p>
				</span>
			</div>
		</div>
		<div class="hotRecommend">
			<h2>热门推荐</h2>
			<div class='videoListBox'>
				<ul>
					
				</ul>
			</div>
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
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/liveDetails.js" src="<%=request.getContextPath()%>/js/libs/require.js"></script>