<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
	WebClientUtils cu=WebClientUtils.getInstance();
	String cdnurl=cu.loadConfigUrl("html5", "cdnurl");
	String imageurl=cu.loadConfigUrl("html5", "imageurl");
	String interfaceurl=cu.loadConfigUrl("html5", "interfaceurl");
	if(cdnurl==null){
		cdnurl="http://cdn.wopaitv.com";
	}
	if(imageurl==null){
		imageurl="http://api.wopaitv.com";
	}
	if(interfaceurl==null){
		interfaceurl="http://api.wopaitv.com";
	}
	//interfaceurl = "http://10.12.129.112:8080";

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>LIVE直播邀请</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
</head>
<style type="text/css">
	html,body{
		width: 100%;
		height: 100%;
	}
	img{
		width: 100%;
		border: none;
	}
	*{
		margin: 0;
		padding: 0;
	}
	div.topCtn{
		width: 100%;
		position: relative;
		float: left;
	}
	div.topCtn .btn{
		width: 256px;
		position: absolute;
		bottom: 40px;
		left: 50%;
		margin-left: -128px;
	}
	.bottomCtn{
		width: 100%;
		height: 90px;
		background-color: #f8698f;
		float: left;
		margin-top: 38px;
	}
	.bottomCtn img{
		width: 256px;
		margin: 24px auto;
		display: block;
	}
</style>
<body>
	<div class="topCtn">
		<img width="100%" src="<%=request.getContextPath()%>/img/personalBg/topBanner.png">
		<img class="btn" src="<%=request.getContextPath()%>/img/personalBg/button.png">
	</div>
	<img src="<%=request.getContextPath()%>/img/personalBg/topPic.png">
	<img src="<%=request.getContextPath()%>/img/personalBg/bottomPic.png">
	<div class="bottomCtn">
		<img class="btn" src="<%=request.getContextPath()%>/img/personalBg/button.png">
	</div>
</body>
</html>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/libs/jquery-2.1.4.js"></script>
<script type="text/javascript">
	var browser={
	    versions:function(){
	            var u = navigator.userAgent, app = navigator.appVersion;
	            return {         //移动终端浏览器版本信息
	                trident: u.indexOf('Trident') > -1, //IE内核
	                presto: u.indexOf('Presto') > -1, //opera内核
	                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
	                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
	                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
	                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
	                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
	                iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
	                iPad: u.indexOf('iPad') > -1, //是否iPad
	                webApp: u.indexOf('Safari') > -1, //是否web应该程序，没有头部与底部
	                isWx: u.indexOf('MicroMessenger') > -1

	            };
	         }(),
	         language:(navigator.browserLanguage || navigator.language).toLowerCase()
	}
	 $('body').on('touchstart','.btn',function(){
        if(browser.versions.ios)
        {
           window.location.href = 'https://itunes.apple.com/us/app/wo-pai-wo-jiu-shi-zhu-jiao/id934254637?ls=1&mt=8';
        }
        if(browser.versions.android)
        {
            window.location.href = 'http://wopaitv.com/shengdan/myVideo_oupu_2.0.0.apk';
        }
        if(browser.versions.isWx)
        {
            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
        }
    })
</script>