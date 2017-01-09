<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>找回密码</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
</head>

<link rel="stylesheet" href="dist/css/extract.css?t=1">
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/js/jquery-2.1.0.min.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/js/jQuery.md5.js'></script>
<body>
<jsp:include  page="cache.jsp"/>
<div class="main">
    <div class="log-2">
        <div class="login-item">
            
            <input type="text" class="user-txt" name="user-phone" placeholder="您注册的手机号"/>
        </div>
        <div class="login-item">
            
            <input type="text" class="user-txt" name="user-phone" placeholder="输入验证码"/>
            <em class="ver-icon">获取验证码</em>
        </div>
        <div class="login-item">
            <input type="password" class="user-txt" name="user-pwd" placeholder="新密码(6-20位数字或英文)"/>
            <em class="pwd-block"></em>
        </div>
        <div class="login-sub">
            <a href="javascript:;" class="login-btn">提交</a>
        </div>
    </div>
</div>
<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/common.js'></script>

<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/login.js'></script>
</body>
</html>