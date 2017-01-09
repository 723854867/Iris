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
	//interfaceurl = "http://192.168.11.141:8080";

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<%--<meta content="target-densitydpi=device-dpi,width=device-width,initial-scale=1,maximum-scale=1" name="viewport">--%>
	<title>你摇苹果 我送平安</title>
	<meta name="keywords" content="短视频，直播，我拍">
	<meta name="description" content="摇一摇，圣诞苹果免费送，你摇一个送给我吧。">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_active/css/index.css">
	<script src='<%=request.getContextPath()%>/html5/app_active/js/jquery-1.8.3.min.js'></script>
	<script src='<%=request.getContextPath()%>/html5/app_active/js/shakenum.js?t=1'></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<body>
<%--<button class="btn btn_primary" id="checkJsApi" style="width:200px; height:100px;background:red;">checkJsApi</button>--%>
<%--<button class="btn btn_primary" id="sharebtn" style="width:200px; height:100px;background:red;">share 分享好友</button>--%>
<%--<button class="btn btn_primary" id="sharebtnquan" style="width:200px; height:100px;background:red;">share 分享朋友圈</button>--%>
<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>

<input type="hidden" id="timestamp" value="${ timestamp}"/>
<input type="hidden" id="noncestr" value="${ noncestr}"/>
<input type="hidden" id="signature" value="${ signature}"/>
<img src="<%=request.getContextPath()%>/html5/app_active/img/apple-share.jpg"  alt="" class="share-icon"/>
<%----%>
<div class="main">
	<img src="<%=request.getContextPath()%>/html5/app_active/img/gameBg.jpg" alt="" class="bgImg">
	<div class="black"></div>
	<div class="introduces-btn"><img src="<%=request.getContextPath()%>/html5/app_active/img/introduces-btn.png" alt=""/></div>
	<div class='tree'>

		<div class="tree-body">
			<img src="<%=request.getContextPath()%>/html5/app_active/img/tree.png" alt="" class="tree-head">
		</div>
		<em class="tips">
			<img src="<%=request.getContextPath()%>/html5/app_active/img/yao.png" alt=""/>
		</em>
		<img src="<%=request.getContextPath()%>/html5/app_active/img/treeFoot.png" alt="" class="tree-foot">
	</div>
	<div class='shareBtn' id="shareBtn"><img src="<%=request.getContextPath()%>/html5/app_active/img/icon/shareBtn.png" alt=""></div>
	<div class="rewardList">
		<em class="horm"></em>
		<ul>
			<li>..获得平安果一枚</li>
			<li>你是获得30积分</li>
			<li>大大开获得平安果一枚</li>
			<li>佐罗0获得平安果一枚</li>
			<li>嘻哈获得平安果一枚</li>
			<li>大头获得平安果一枚</li>
			<li>一凡获得平安果一枚</li>
			<li>废柴获得30积分</li>
			<li>哈拉路获得平安果一枚</li>
			<li>老人二获得平安果一枚</li>
			<li>天地人获得30积分</li>
			<li>唐瑾获得平安果一枚</li>
			<li>我拍天使获得30积分</li>
			<li>大法师获得平安果一枚</li>
			<li>行者得平安果一枚</li>
		</ul>
	</div>
</div>


<%--第一次摇奖--%>
<div class="firstwin skyLayer close-warp" sytle="display:block;">
	<div class="win-box1">
		<img src="<%=request.getContextPath()%>/html5/app_active/img/notWin.png" alt="" />
		<span class="close-box"></span>
		<span class="ok-box"></span>
	</div>
</div>
<%--第二次摇奖--%>

<%--送积分--%>
<div class="winin-integral skyLayer close-warp">
	<div class="win-box2">
		<span class="close-box"></span>
		<div class="win-msg">
			恭喜您获得了 <strong>我拍 <span>30</span> 积分</strong>，还有更多礼物免费兑换！
		</div>
		<div class="complete-btn"></div>
	</div>
</div>
<%--送苹果--%>
<div class="winin-apple skyLayer close-warp">
	<div class="win-box2">
		<span class="close-box"></span>
		<div class="win-msg">
			<p class="ward-msg">恭喜您，获得了<strong>圣诞平安果</strong>一个，我拍给您无限惊喜</p>
			<div class="info-item">
				<label for="">姓名：</label>
				<input type="text" class="info-txt form-txt" id="apple-user-name" value="" />
			</div>
			<div class="info-item">
				<label for="">手机号：</label>
				<input type="text" class="info-txt form-txt" id="apple-user-phone" value=""/>
			</div>
			<div class="info-item">
				<label for="">收货地址：</label>
				<textarea name="" class="address form-txt" id="apple-user-address" cols="30" rows="10" value=""></textarea>
			</div>
		</div>
		<div class="complete-btn"></div>
	</div>
</div>
<%--送手机--%>
<%--<div class="winin-phone skyLayer close-warp">--%>
	<%--<div class="win-box2">--%>
		<%--<span class="close-box"></span>--%>
		<%--<div class="win-msg">--%>
			<%--<p class="ward-msg">恭喜您，获得了<strong>iPone 6S</strong>一部，我拍TV给您无限惊喜</p>--%>
			<%--<div class="info-item">--%>
				<%--<label for="">姓名：</label>--%>
				<%--<input type="text" class="info-txt form-txt" value=""/>--%>
			<%--</div>--%>
			<%--<div class="info-item">--%>
				<%--<label for="">手机号：</label>--%>
				<%--<input type="text" class="info-txt form-txt" value=""/>--%>
			<%--</div>--%>
			<%--<div class="info-item">--%>
				<%--<label for="">收货地址：</label>--%>
				<%--<textarea name="" id="" class="address form-txt" cols="30" rows="10" value=""></textarea>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="complete-btn"></div>--%>
	<%--</div>--%>
<%--</div>--%>
<%--快递打包中--%>
<div class="winin-express skyLayer close-warp" >
	<div class="win-box2">
		<span class="close-box"></span>
		<div class="win-msg">
			您的快递正在打包......
		</div>
		<div class="complete-btn"></div>
	</div>
</div>
<%--活动介绍--%>
<div class="introduces skyLayer close-warp">
	<div class="win-box1">
		<img src="<%=request.getContextPath()%>/html5/app_active/img/introduces.png" alt="" />
		<span class="close-box"></span>
		<span class="ok-box"></span>
	</div>
</div>

<%--微信提示--%>
<div class="shareTips skyLayer close-warp">
	<img src="<%=request.getContextPath()%>/html5/app_active/img/share-tips.png" alt="" />
</div>
<%--活动结束--%>
<div class="end-body close-warp">
	<div class="end-main">
		<h3 class="end-title">我拍 圣诞狂欢</h3>
		<div class="activePic">
			<a href="http://www.wopaitv.com/#cnzz_name=loading&cnzz_from=game-end-active"><img src="<%=request.getContextPath()%>/html5/app_active/img/activeList.png" alt=""></a>
		</div>
		<div class="end-btn">
			<a href="javascript:void(0);" class="fl back-game"><img src="<%=request.getContextPath()%>/html5/app_active/img/icon/backBtn.png" alt=""></a>
			<a href="http://www.wopaitv.com/#cnzz_name=loading&cnzz_from=game-end" class="fr down-app"><img src="<%=request.getContextPath()%>/html5/app_active/img/icon/downBtn.png" alt=""></a>
		</div>
	</div>
</div>

<script>
$(function(){
	init();
	jQuery.gameinit();

})
</script>

<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
</body>
</html>