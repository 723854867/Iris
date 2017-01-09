<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta property="qc:admins" content="342145635167701146637572534777" />
	<meta name="renderer" content="webkit">
	<meta http-equiv= "X-UA-Compatible" content = "IE=edge,chrome=1"/>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/index.css?t=<%=new java.util.Date().getTime()%>">
	<script src="<%=request.getContextPath()%>/js/libs/require.js" data-main="<%=request.getContextPath()%>/js/views/index"></script>
	<title>LIVE</title>
</head>
<body>
<jsp:include  page="../head.jsp"/>
<%--<a href="wopai://videoid=26191">打开LIVETV视频详情</a><br/>--%>
<%--<a href="wopai://activityid=98">打开LIVETV活动页</a><br/>--%>
<%--<a href="wopai://hottagname=郭毅最帅">打开LIVETV话题页</a>--%>
<%--<br/>--%>
<%--<br/>--%>
<%--<br/>--%>
<%--<br/>--%>
<%--<a href="com.busap.myvideo://main/openwidth?type=0">打开LIVETV视频详情</a><br/>--%>
<%--<a href="com.busap.myvideo://main/openwidth?activityId=98&activityTit=发的&type=1">打开LIVETV活动页</a><br/>--%>
<%--<a href="com.busap.myvideo://main/openwith?Tag=郭毅最帅&type=2">打开LIVETV话题页</a>--%>
<div class='main'>
	<div class="dropArrow" onclick='_czc.push(["_trackEvent","分类下拉按钮","indexClass"])'><em class="arrrowD-icon"></em></div>
	<div class="dropTabList">
		<div class="skyBoxFFF"></div>
		<div class="dropCon">
			<p>
				兴趣领域
			</p>
			<div class="closeDrop"><em class="closeIcon"></em></div>
			<div class="listScroll">
				<c:forEach var="active" items="${activityList}" varStatus="status" >
					<div class="activeItem" data-id="${active.id }"><a href="javascript:;" >${active.title }</a> <em class="fr itemSele"></em></div>
				</c:forEach>
			</div>
		</div>

	</div>
	<div class='nav'>
		<div>
			<ul class="navList">
				<c:forEach var="active" items="${activityList}" varStatus="status" >
					<li data-id="${active.id }"><a href="javascript:;" class="itemSele">${active.title }</a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<div class='videoList'>
		<ul >
			<c:forEach var="videolist" items="${videoList }" varStatus="status" end="9" step="1">
				<li>
					<a href="javascript:;" class="govideoShow" video-Id="${videolist.id }" data-userId="${videolist.user.id }">
						<img class='videoPic' data-videoPic="${videolist.videoPic }" lazy_src="" src="" alt="">
						<div class="filtlayer"></div>
						<p class='videoMsg'><span>${videolist.description }</span></p></a>
					<span class='userPho'>
						<a href="javascript:;" data-userId="${videolist.user.id}">
							<img class="userImg" data-userPic="${videolist.user.pic }" lazy_src="" src="" alt="">
							<c:if test="${ videolist.user.vipStat eq 1}">
								<em class="userV1"></em>
							</c:if><c:if test="${ videolist.user.vipStat eq 2}">
								<em class="userV2"></em>
							</c:if>
							<c:if test="${ videolist.user.vipStat eq 3}">
								<em class="userV3"></em>
							</c:if>
						</a>
					</span>
					<span class='playData'><em></em><span class="playNumVal">${videolist.showPlayCount}</span></span>
				</li>
			</c:forEach>

		</ul>
		<div id="btnMore" class="loadMore" style="display:block;">加载更多</div>
	</div>
	<div class='downBar'><span class="downClose" onclick='_czc.push(["_trackEvent","首页下载按钮","closeBottomDownload"])'></span><a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载LIVE，查看更多精彩</a></div>
</div>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
</body>
</html>









