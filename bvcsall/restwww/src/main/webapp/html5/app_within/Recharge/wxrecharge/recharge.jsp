<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>LIVE金币充值</title>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="format-detection" content="telphone=no, email=no"/>
</head>

<link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/css/style.css?t=1">
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/js/jquery-2.1.0.min.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/js/pingpp.js'></script>
<body>
<input type="hidden" value="${openId}" id="openid"/>
<input type="hidden" value="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/" id="picSouceUrl"/>

<div class='main'>
	<div class='one'>
		<div class='logo'>
			<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/logo.png" width="150" alt="">
		</div>
		<div class='id-warp'>
			<input type="text" class='id-txt' placeholder="请输入您的LIVE ID">
			<p class="get-tips">如何获取LIVE ID?</p>
		</div>
		<div class='right-btn'>确定</div>
		<div class="big-warp">
			<div class="tips-warp">
				<h4>第1步：点击进入个人中心  如图所示 </h4>
				<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/test1.png" alt="">
				<hr/>
				<h4>第2步：点击进入编辑资料 如图所示</h4>
				<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/test2.png" alt="">
				<hr/>
				<h4>第3步：点击复制LIVE ID号 如图所示</h4>
				<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/test3.png" alt="">

			</div>
			<a href="javascript:;" class='iKnow-btn'>我知道啦</a>
		</div>

	</div>

	<div class='two'>
		<div class='list-head'>
			<div class="user-msg">
				<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/ok-icon.png" alt="用户头像" class="user-pho">
				<br/><p class="user-name"></p>
			</div>
			<p class='switch-user'>切换账户</p>
		</div>
		<div class='list-gold'>
			<ul>
				<!--<li data-id="32">-->
				<!--<img src="dist/img/gold.png" alt="" class="gold-icon"/>-->
				<!--<span class='list-left'>100</span>-->
				<!--<span class='list-middle'>赠送10拍币</span>-->
				<!--<span class='list-right'>￥6</span>-->
				<!--</li>-->
				<!--<li data-id="31">-->
				<!--<img src="dist/img/gold.png" alt="" class="gold-icon"/>-->
				<!--<span class='list-left'>100</span>-->
				<!--<span class='list-middle'>赠送10拍币</span>-->
				<!--<span class='list-right'>￥6</span>-->
				<!--</li>-->
			</ul>
			<div class='buy-btn'>立即支付</div>
		</div>
	</div>
</div>
<div class="skyBox"></div>
<div class="dialog-user-info">
	<div class="dialog-user">
		<img src="" alt="" class="dialog-userpic"/><br/>
		<p class="dialog-username"></p>
	</div>
	<div class="dialog-box">
		<p class="dialog-user-code">
			LIVEID <br/>
			<span class='dialog-user-id'>
				4324234
			</span>
		</p>
		<p>这是您的账号吗？</p>
		<p>
			<a href="javascript:;" class="dialog-btn dialog-reset">重新输入</a><a href="javascript:;" class="dialog-btn dialog-sub">是</a>
		</p>
	</div>

</div>
<div class='buy-ok'>
	<em class="close-btn"></em>
	<div class='buy-ok-warp'>
		<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/ok-icon.png" class="ok-pic" alt=""/>
		<h4>充值成功</h4>
		<p class="ok-msg">已为账户：<span class="user-name2"></span>充值<span class="gold-num"></span>,可以给主播送礼物啦，GO！</p>
	</div>
</div>
<div class="load-box">
	<div class="loading">
		<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/londing.png" alt=""/>
	</div>
</div>

<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/index.js?t=0'></script>
</body>
</html>