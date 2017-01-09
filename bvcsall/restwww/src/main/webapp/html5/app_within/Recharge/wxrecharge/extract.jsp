<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html>
<head lang="en">
	<meta charset="UTF-8">
	<title>提现</title>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">

</head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/css/extract.css?t=2">
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/js/jquery-2.1.0.min.js'></script>

<body>
<jsp:include  page="cache.jsp"/>
<input type="hidden" value="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/" id="picSouceUrl"/>

<div class='main'>
	<div class="user-box">
		<div class="user-msg">
			<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/user_icon_48px.png" alt="用户头像" class="user-pho">
			<span class="user-name"></span>
		</div>
		<a href="javascript:;" class='switch-user'>解除绑定</a>
	</div>
	<div class="money-box">
		<p class="money-num">收益<em>0</em>金豆，￥<span>0</span></p>
		<a href="toExchange?channel=exchange" class="money-btn">兑换金币</a>
		<a href="toExchange?channel=wx_pub" class="money-btn">提现</a>
		<!-- <p class="rule">提现规则</p> -->
		<p class="rule">每日可提现总金额最多2000元</p>
	</div>
</div>
<div class="rule-info">
	<em class="close"></em>
	<p>每天微信认证用户最多提现￥20000，非认证用户最多提现￥2000</p>
	<p>微信实名认证：绑定银行卡的微信用户</p>
</div>
<%--遮罩层--%>
<div class="skyBox"></div>
<%--提示框--%>
<%--<div class='buy-ok'>--%>
	<%--<em class="close-btn"></em>--%>
	<%--<div class='buy-ok-warp'>--%>
		<%--<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/ok-icon.png" class="ok-pic" alt=""/>--%>
		<%--<h4>提现成功</h4>--%>
		<%--<p class="ok-msg">已为账户：<span class="user-name2"></span>充值<span class="gold-num"></span>,可以给主播送礼物啦，GO！</p>--%>
	<%--</div>--%>
<%--</div>--%>
<%--<div class="load-box">--%>
	<%--<div class="loading">--%>
		<%--<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/londing.png" alt=""/>--%>
	<%--</div>--%>
<%--</div>--%>
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/common.js'></script>

<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/extract.js'></script>

</body>
</html>