<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>个人中心</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/personalCenter/personalCenter.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
	<jsp:include  page="../head.jsp"/>
<div class="main">
	<div class="personalCenter">
	
		<a id="perCent" data-userId="${ruser.id }">
			<img class="fl userPortrait userHomePic" width='60' height='60' src="" data-userPic='${ruser.pic }' onerror='this.src="../../img/portrait.png"'>
			<c:if test="${ruser.vipStat eq 1 }">
				<img class="user_icon_blue" src="../../img/icons-2x/user_icon_blue.png" alt="">
			</c:if>
			<c:if test="${ruser.vipStat eq 2 }">
				<img class="user_icon_yellow" src="../../img/icons-2x/user_icon_yellow.png" alt="">
			</c:if>
			<c:if test="${ruser.vipStat eq 3 }">
				<img class="user_icon_green" src="../../img/icons-2x/user_icon_green.png" alt="">
			</c:if>
			<c:if test="${ruser.vipStat eq 0 }">
				
			</c:if>
			<ul>
				<li class="nickName">
					${ ruser.name }
					<c:if test="${ruser.sex eq 0}">
						<img id="sex" src="../../img/icons-2x/prof_icon_woman.png"  alt="服务器错误，请重新加载">
					</c:if>
					<c:if test="${ruser.sex eq 1}">
						<img id="sex" src="../../img/icons-2x/prof_icon_man.png"  alt="服务器错误，请重新加载">
					</c:if>
				</li>
				<c:if test="${ruser.signature == '' || ruser.signature==  null}">
					<li>懒人没前途o(∩_∩)o 哈哈~</li>
				</c:if>
				<c:if test="${ruser.signature != '' && ruser.signature !=  null}">
					<li>${ruser.signature }</li>
				</c:if>
				<li>积分 ${ ruser.signSum }</li>
			</ul>
			<img class="fr music_btn_more" src="../../img/icons-2x/back-arrow-right.png" alt="">
		</a>
		<!-- <p>
			<a class="waiting" href="javascript:void(0);">
				<img src="../../img/icons-2x/prof_icon_-program.png" alt=""><span>节目</span>
			</a>
		</p> -->
		<p class="doubleLine">
			<a id="attentions" href="javascript:void(0);"">
				<img src="../../img/icons-2x/prof_icon_attention.png" alt="">
				<span>关注</span>
				<img class="music_btn_more1" src="../../img/icons-2x/back-arrow-right.png" alt="">
			</a>
			<a class="littleLine"></a>
			<a id="fans" href="javascript:void(0);">
				<img src="../../img/icons-2x/prof_icon_fans.png" alt=""><span>粉丝</span>
				<img class="music_btn_more1" src="../../img/icons-2x/back-arrow-right.png" alt="">
			</a>
		</p>
		<p>
			<a id="feedBack" href="javascript:void(0);">
				<img src="../../img/icons-2x/feedback.png" alt=""><span>意见反馈</span>
				<img class="music_btn_more1" src="../../img/icons-2x/back-arrow-right.png" alt="">
			</a>
		</p>
		<!-- <p>
			<a class="waiting" href="javascript:void(0);">
				<img src="../../img/icons-2x/prof_icon_news.png" alt=""><span>消息</span>
				<span class="fr news"></span>
			</a>
		</p> -->
		<p>
			<a class="logout" href="javascript:void(0);">退出登录</a>
		</p>
	</div>
	<div class="downBar">
		<span class="downClose"></span>
		<a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载我拍，查看更多</a>
	</div>
</div>	
	<input type="hidden" name="uid" id="uid" value="${uid}">
	<input type="hidden" name="access_token" id="access_token" value="${access_token}">
</body>
</html>

<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/personalCenter" src="<%=request.getContextPath()%>/js/libs/require.js"></script>



