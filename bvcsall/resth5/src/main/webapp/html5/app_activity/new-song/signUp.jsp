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
	//interfaceurl = "http://192.168.151.214:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/html5/app_activity/new-song/css/signUp.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/html5/app_activity/new-song/css/common.css">
	<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/jquery-2.1.0.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
	<title></title>
</head>
<body>
	<%--图片地址--%>
	<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
	<%--视频地址--%>
	<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
	<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
	<input id="shareImg" type="hidden" value="${shareImg }" />
	<input id="shareTitle" type="hidden" value="${shareTitle }" />
	<input id="shareText" type="hidden" value="${shareText }" />
	<input id="timestamp" type="hidden" value="${timestamp }" />
	<input id="noncestr" type="hidden" value="${noncestr }" />
	<input id="signature" type="hidden" value="${signature }" />
	<div class="topBlock">
		<img class="logo" width="40" src="<%=request.getContextPath()%>/img/icons-2x/87.png">
		<dl>
			<p>LIVE直播</p>
			<p>中国新歌声第五战队集结</p>
		</dl>
		<a class="joinBtn" href="javascript:void(0);">立即参战</a>
	</div>
	<div class="ruleBlock">
		<div class="ac_entrance"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/signUp/signUp_one.jpg"></div>
		<div class="ac_entrance"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/signUp/signUp_two.jpg"></div>
		<div class="ac_entrance"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/signUp/signUp_three.jpg"></div>
		<div class="ac_entrance"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/signUp/signUp_four.jpg"></div>
		<div class="ac_entrance"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/signUp/signUp_five.jpg"></div>
		<div class="ac_entrance"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/signUp/signUp_six.jpg"></div>
		<div class="ac_entrance"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/signUp/signUp_seven.jpg"></div>
	</div>
	<div class="fix">
		<a class="shareBtn" href="javascript:void(0);">分享</a>
		<a class="signUp" href="javascript:void(0);">报名新歌声</a>
	</div>
	<div class="skyBox">
		<img src="<%=request.getContextPath()%>/img/gobrowser.png" alt=""/>
	</div>
	<div class="skyBox-anz">
		<img src="<%=request.getContextPath()%>/img/anz-browser.png" alt=""/>
	</div>

</body>
</html>
<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/common.js"></script>
<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/signUp.js"></script>