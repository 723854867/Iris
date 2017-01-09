<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<html>
<head>
    <title>记录列表</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/css/extract.css?t=2">
    <script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/js/jquery-2.1.0.min.js'></script>
</head>
<body>
<jsp:include  page="cache.jsp"/>
<div class="main">
    <div class="exchange_top">
        <div class="bean_box">
            金豆：<span class="bean_num"></span>
        </div>
        <div class="record_btn">提现记录</div>
    </div>
    <div class="record_list_wiht">
        <div class="exchange_top">
            <div class="bean_box record_info">
                已提现 ￥<span class="money_num">0</span>  <span class="dou_num">0金豆</span>
            </div>
        </div>
        <div class="record_list_1 record_wiht_list">
            <ul>

            </ul>
        </div>
    </div>
</div>
<%--遮罩层--%>
<div class="skyBox"></div>
<%--提示框--%>
<div class='buy-ok'>
    <em class="close-btn"></em>
    <div class='buy-ok-warp'>
        <img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/ok-icon.png" class="ok-pic" alt=""/>
        <h4>提现成功</h4>
        <p class="ok-msg">已向您的微信零钱转账<span class="gold-num">0</span>，请稍后注意查收！ </p>
    </div>
</div>




<div class="load-box">
    <div class="loading">
        <img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/londing.png" alt=""/>
    </div>
</div>
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/common.js'></script>

<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/extract_wiht_record.js'></script>

</body>
</html>
