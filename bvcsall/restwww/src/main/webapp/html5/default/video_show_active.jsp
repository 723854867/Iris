<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/video_show_active.css?t=<%=new java.util.Date().getTime()%>">
	<script src=''></script>
	<title>视频详情</title>
</head>
<body>
<jsp:include  page="../head.jsp"/>
<!-- 内容 -->
<div class='main'>
	<div class='videoList1'>
		
		<div class='playBox'>
			<img src="../../../img/test.jpg" alt="视频" class='videoPic'>
			<span class='playBtn'>
				<em class='canPlay'></em>
			</span>
			<!-- <video style='position:relative; z-index:10;' src="../../../data/3.mp4" width="100%" height="300" loop="loop" x-webkit-airplay="" webkit-playsinline="" style="height: 360px; width:100%;" autoplay='autoplay'></video> -->
		</div>
		<div class='openApp'>打开LIVE视频播放更流畅</div>
		<div class='commontBox'>
			<div class='greet'>
				<a href="javascript:;"><em class='loveIcon'></em>赞</a>
				<a href="javascript:;"><em class='shareIcon'></em>分享</a>
			</div>
			<div class='activeAbout'>
				<p>此视频正在参加大喵喵的活动</p>
				<a href="" class='seeActive'>查看活动详情及所有视频</a>
			</div>
			<div class='listUserMsg'>
				<dl>
					<dt><img src="../../../img/icons-2x/icon_tabbar_profile_sel.png" alt="头像"></dt>
					<dd>
						<p>草帽的舞台</p>
						<p>03-09 09:19</p>
					</dd>
					<dd>
						<a href="javascript:;" class='playData'></a>
					</dd>
				</dl>
			</div>
			<p class='wishWord'>希望你们喜欢 #LIVE娱乐# </p>
			<dl class='likeUser'>
				<dt><img src='../../../img/icons-2x/icon_tabbar_profile_sel.png'></dt>
				<dt><img src='../../../img/icons-2x/icon_tabbar_profile_sel.png'></dt>
				<dt><img src='../../../img/icons-2x/icon_tabbar_profile_sel.png'></dt>
				<dt><img src='../../../img/icons-2x/icon_tabbar_profile_sel.png'></dt>
				<dt><img src='../../../img/icons-2x/icon_tabbar_profile_sel.png'></dt>
				<dt><img src='../../../img/icons-2x/icon_tabbar_profile_sel.png'></dt>
				<dd class='likeNum'>122</dd>
			</dl>
			
			<div class='commont'>
				<ol class='commontHead'>
					<li class='on'><label for="">最新评论(<span class='commonNum'>43</span>)</label></li>
					<li><label for="">相关视频</label></li>
				</ol>
				<div class='listUserMsg'>
					<dl>
						<dt><img src="../../../img/icons-2x/icon_tabbar_profile_sel.png" alt="头像"></dt>
						<dd>
							<p>草帽的舞台</p>
							<p>返回电话号 好看 会发生的....</p>
						</dd>
						<dd>
							2015-01-21
						</dd>
					</dl>
				</div>
			</div>
		</div>
		<div class='commontFireBox'>
			<input type="text"  class='commontTxt' placeholder='评论'>
			<span class='commontSub'>发送</span>
		</div>	
	</div>
</div>
</body>
</html>