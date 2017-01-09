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
	<title>周五没看够，周末Live go!</title>
	<meta name="description" content="想跟48位学员零距离互动，想听学员和主播们现场飙歌，还想探听最新鲜导师八卦，锁定《中国新歌声LIVE战队》！" />
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
</head>
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
		position: relative;
	}
	.down-btn{
		width:60%;
		margin:auto;
		display: block;

	}
	.box-bg{
		width:100%;
	}
	.footer{
		position: absolute;
		/*bottom: 3%;*/
		text-align: center;
	}
	.footBtn{
		width:80%;
		padding:20px 0 0 0;
	}
	span[id^="cnzz"] {
		display: none;
	}
</style>
<body>
<input id="timestamp" type="hidden" value="${timestamp }" />
<input id="noncestr" type="hidden" value="${noncestr }" />
<input id="signature" type="hidden" value="${signature }" />
<div class="box">
	<img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/1112.jpg" class="box-bg" alt=""/>
	<div class="footer">
		<p>
			<a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/downApp.png" class="footBtn" alt=""/></a>
		</p>
		<p>
			<a href="javascript:;"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/shareBtn.png" class="footBtn share" alt=""/></a>
		</p>
	</div>
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
		var shareTit = '周五没看够，周末Live go!';
		var sharedesc = '想跟48位学员零距离互动，想听学员和主播们现场飙歌，还想探听最新鲜导师八卦，锁定《中国新歌声LIVE战队》！';
		var sharePic = 'http://api.wopaitv.com/resth5/html5/app_activity/new-song/img/1112share.jpg';

		$('.share').click(function(){
			if(browser.versions.ios){
				if(!browser.versions.isWx){
					sharefn(shareTit,sharedesc,sharePic,url);
				}else{
					$('.share-tip').show();
				}
			}
			if(browser.versions.android){
				if(!browser.versions.isWx){
					window.share.onShareClick(sharePic,shareTit,sharedesc,url)
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
				title: shareTit,
				desc: sharedesc,
				link: '',
				imgUrl: sharePic
			};
			//朋友圈
			shareData2 = {
				title: shareTit,
				link: '',
				imgUrl: sharePic
			};
			wx.onMenuShareAppMessage(shareData1);
			wx.onMenuShareTimeline(shareData2);
		});
	})

</script>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
