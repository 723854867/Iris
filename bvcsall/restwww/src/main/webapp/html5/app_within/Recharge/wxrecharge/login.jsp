<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>LIVE账户</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
</head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/css/extract.css?t=1">
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/js/jquery-2.1.0.min.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/js/jQuery.md5.js'></script>



<body>
<jsp:include  page="cache.jsp"/>
<%--<input type="hidden" value="433443443" id="openid"/>--%>
<input type="hidden" value="${openId}" id="openid"/>
<input type="hidden" value="${back}" id="back"/>
<div class="main">
    <div class="log-1">
        <div class="login-item">
            <em class="phone-icon"></em>
            <input type="text" class="user-txt user-phone" name="user-phone" placeholder="请输入您手机号"/>
        </div>
        <div class="login-item">
            <em class="pwd-icon"></em>
            <input type="password" class="user-txt user-pwd" name="user-pwd" placeholder="请输入密码"/>
            <em class="pwd-block"></em>
        </div>
        <p class="forget-pwd"><a href="<%=request.getContextPath()%>/page/user/forgetPwd">忘记密码？</a></p>
        <div class="login-sub">
            <a href="javascript:;" class="login-btn">绑定</a>
            <a href="javascript:;" class="unbind_btn">解绑</a>
        </div>
        <p class="login-tip">请填写您登录LIVE或在LIVE绑定的手机号和密码</p>
    </div>
</div>
<div class="load-box">
	<div class="loading">
		<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/londing.png" alt=""/>
	</div>
</div>
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/common.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/login.js'></script>

</body>
</html>