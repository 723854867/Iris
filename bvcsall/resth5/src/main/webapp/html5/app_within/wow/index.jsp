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
    //interfaceurl = "http://localhost:8080";
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
	<link class="linkUrl" rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/wow/css/index.css?t=1">
	<title></title>
</head>
<body>
	<div class="entrance">
		<div class="ruleBlock">
			<img src="<%=request.getContextPath()%>/html5/app_within/wow/images/pk_poptextbg.png">
			<img class="returnBtn" src="<%=request.getContextPath()%>/html5/app_within/wow/images/pk_popbtn.png">
		</div>
		<div class="btnBlock">
			<a class="user1" ><img src="<%=request.getContextPath()%>/html5/app_within/wow/images/pk_btn_buluo.png"></a>
			<a class="user2"><img src="<%=request.getContextPath()%>/html5/app_within/wow/images/pk_btnlianmeng.png"></a>
			<a class="rule"><img src="<%=request.getContextPath()%>/html5/app_within/wow/images/pk_btn_rule.png"></a>
		</div>
	</div>
	<div>
		
	</div>
	<div class="addId bg1">

		<div class="enterId">
			<p>在个人资料内可查看您的LIVE ID</p>
			<input class="LIVEID_lianmeng" type="text" placeholder="请输入LIVEID">
			<input class="makeSure_lianmeng" type="button" value="">
		</div>
		
	</div>
	<div class="addId bg2">
		<div class="enterId">
			<p>在个人资料内可查看您的LIVE ID</p>
			<input class="BliveID_buluo" type="text" placeholder="请输入LIVEID">
			<input class="makeSure_buluo" type="button" value="">
		</div>
	</div>
	<div id="urlBox">
		<img width="100%" src="<%=request.getContextPath()%>/html5/app_within/wow/images/call_pop.png">
		<p class="url"></p>
		<img class="closeBtn" src="<%=request.getContextPath()%>/html5/app_within/wow/images/call_btnclose.png">
	</div>
	<div class="blackBox"></div>
	<script src="<%=request.getContextPath()%>/html5/app_within/wow/js/jquery-2.1.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/html5/app_within/wow/js/index.js"></script>
</body>
</html>