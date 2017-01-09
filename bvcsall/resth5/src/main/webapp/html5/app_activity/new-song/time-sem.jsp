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
//	interfaceurl = "http://192.168.151.214:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>《中国新歌声LIVE战队》即将开播</title>
	<meta name="description" content="独家视频、精彩爆料、学员八卦……你想知道的都在这里" />
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
</head>
<link rel="stylesheet" href="css/voice.css"/>
<script src=" https://res.wx.qq.com/open/js/jweixin-1.0.0.js "></script>
<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/jquery-2.1.0.min.js"></script>


<style>
	*{
		margin:0;
		padding:0;
		list-style: none;
	}
	body{
		background: #000;
	}
	.box{
		width:100%;
		max-width: 640px;
		min-width: 320px;
		margin:auto;
		padding-bottom: 20px;
	}
	.down-btn{
		width:60%;
		margin:auto;
		display: block;

	}
	.box-bg{
		width:100%;
	}
</style>
<body>
<input id="timestamp" type="hidden" value="${timestamp }" />
<input id="noncestr" type="hidden" value="${noncestr }" />
<input id="signature" type="hidden" value="${signature }" />
<div class="box">
	<img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/landingpage.jpg" class="box-bg" alt=""/>
	<img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/btn_share.png" class="down-btn" alt=""/>
</div>

</body>
</html>

<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/common.js"></script>
<script>

	var browser={
		versions:function(){
			var u = navigator.userAgent, app = navigator.appVersion;
			return {         //移动终端浏览器版本信息
				presto: u.indexOf('Presto') > -1, //opera内核
				webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
				gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
				mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
				ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
				android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
				iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
				iPad: u.indexOf('iPad') > -1, //是否iPad
				isWx: u.indexOf('MicroMessenger') > -1,
				isQQ:u.indexOf('QQ/') > -1,
				isQQbrowser: u.indexOf('QQBrowser'),
				isWeiBo:u.indexOf('weibo') > -1,
				isOpera:u.indexOf('Opera') >= 0,
				isSafari:u.indexOf("Safari") > -1
			};
		}(),
		language:(navigator.browserLanguage || navigator.language).toLowerCase()
	};
	$(function(){
		var url = window.location.href;
		var dataType = url.split("#")[1];

		$('.down-btn').click(function(){
			if(browser.versions.ios){
				if(!browser.versions.isWx){
					sharefn("《中国新歌声LIVE战队》即将开播","中国新歌声+LIVE直播 联手打造全新综艺新模式","http://api.wopaitv.com/resth5/html5/app_activity/new-song/img/slt.png",url);
				}else{
					$('.share-tip').show();
				}
			}
			if(browser.versions.android){
				if(!browser.versions.isWx){
					window.share.onShareClick("http://api.wopaitv.com/resth5/html5/app_activity/new-song/img/slt.png","《中国新歌声LIVE战队》即将开播","中国新歌声+LIVE直播 联手打造全新综艺新模式",url)
				}else{
					$('.share-tip').show();
				}
			}
		})
		$('.share-tip').click(function(){
			$(this).hide()
		})



		wx.ready(function(){
			//好友
			shareData1 = {
				title: '《中国新歌声LIVE战队》即将开播',
				desc: '中国新歌声+LIVE直播 联手打造全新综艺新模式',
				link: '',
				imgUrl: 'http://api.wopaitv.com/resth5/html5/app_activity/newVoice-3/img/sharepic.png'
			};
			//朋友圈
			shareData2 = {
				title: '《中国新歌声LIVE战队》即将开播',
				link: '',
				imgUrl: 'http://api.wopaitv.com/resth5/html5/app_activity/newVoice-3/img/sharepic.png'
			};
			wx.onMenuShareAppMessage(shareData1);
			wx.onMenuShareTimeline(shareData2);
		});
	})

</script>