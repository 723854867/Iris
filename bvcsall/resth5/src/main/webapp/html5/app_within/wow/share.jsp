<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
    WebClientUtils cu=WebClientUtils.getInstance();
    String cdnurl=cu.loadConfigUrl("html5", "interfaceurl");
    String imageurl=cu.loadConfigUrl("html5", "imageurl");
    String interfaceurl=cu.loadConfigUrl("html5", "interfaceurl");
    if(cdnurl==null){
        cdnurl="http://api.wopaitv.com";
    }
    if(imageurl==null){
        imageurl="http://api.wopaitv.com";
    }
    if(interfaceurl==null){
        interfaceurl="http://api.wopaitv.com";
    }
    //interfaceurl = "http://192.168.151.181:8080";
%>
<%--图片地址--%>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<%--视频地址--%>
<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link class="linkUrl" rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/wow/css/share.css?t=1">
	<title>LIVE请你看《魔兽》，观影礼券正版周边等你来拿！</title>
</head>
<body>
	<img style="opacity:0; width:100%;height:0;" src="<%=request.getContextPath()%>/html5/app_within/wow/images/faceimg.png">
	<div class="form">
		<p>成为LIVE用户，与你的小伙伴并肩战斗</p>
		<div class="form-group">
			<label>手机号：</label><input class="phoneNum" placeholder="您的手机号码" type="number" />
		</div>
		<div class="form-group">
			<label>验证码：</label><input class="code" type="text" placeholder="验证码"/><a href="javascript:;" id="btnCode">获取验证码</a>
		</div>
		<div class="form-group">
			<label>密　码：</label><input class="pass" type="password" placeholder="6-20位数字或英文" />
		</div>
		
		<a class="tijiao" href="javascript:void(0);"><img width="100%" src="<%=request.getContextPath()%>/html5/app_within/wow/images/sig_btn_godata.png"></a>
		<a class="download"  href="javascript:void(0);"><img width="100%" src="<%=request.getContextPath()%>/html5/app_within/wow/images/sig_btn_dl.png"></a>
		<%-- <a class="share"  href="javascript:void(0);"><img width="100%" src="<%=request.getContextPath()%>/html5/app_within/wow/images/sig_btn_share.png"></a> --%>
	</div>
	<div class="successBox">
	<img class="close" src="<%=request.getContextPath()%>/html5/app_within/wow/images/close.png">
		<img src="<%=request.getContextPath()%>/html5/app_within/wow/images/state.png">
		<img class="dl" src="<%=request.getContextPath()%>/html5/app_within/wow/images/download.png">
	</div>
	<script src="<%=request.getContextPath()%>/html5/app_within/wow/js/jquery-2.1.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/html5/app_within/wow/js/md5Utils.js"></script>
	<script src="<%=request.getContextPath()%>/html5/app_within/wow/js/share.js"></script>
</body>
</html>