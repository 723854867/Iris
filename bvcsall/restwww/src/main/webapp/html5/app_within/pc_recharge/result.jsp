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
//	interfaceurl = "http://localhost:8080";
%>

<%--图片地址--%>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<%--视频地址--%>
<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<input type="hidden" id="pay_status" value="${payStatus}"/>

<!DOCTYPE html>
<html>
<head lang="en">
	<meta charset="UTF-8">
	<title>LIVE充值平台</title>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
</head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/pc_recharge/css/style.css?t=2">
<script src='<%=request.getContextPath()%>/html5/app_within/pc_recharge/js/jquery-1.7.min.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/pc_recharge/js/jQuery.md5.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/pc_recharge/js/common.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/pc_recharge/js/recharge.js'></script>

<body>
<div class='main'>
	<div class="top">
		<div class="top_box">
			<a href="http://www.wopaitv.com/"><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/logo.png" class="fl logo" alt=""/></a>
			<div class="fr user-box">
				<img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/user_icon_60px.png" class="user_pic fl" alt=""/>
				<span class="fl user-info">
					<p><span class="user_name"></span> <a href="javascript:;" class="exit_btn"></a><span class="exit_box">退出</span></p>
					<%--<a href="javascript:;" class="exit">退出</a>--%>
					<p>ID：<span class="user_code"></span></p>
				</span>

			</div>
			<div class="fr for_log">请登录</div>
		</div>
	</div>
	<div class="warp">
		<div class="fl warp_l">
			<div class="warp_l_tit">金币充值</div>
			<div class="warp_l_info">
				遇充值问题请联系微信客服：LIVE官方助手
			</div>
		</div>
		<div class="fr warp_r">
			<div class="warp_con">
				<div class="sucess pay_status_box">
					<span class="sucess_bg fl"></span>
					<div class="fl pay_info">
						<p>充值成功</p>
						<p>订单号：<span>${orderNo }</span></p>
						<p>充值金额：<span>${amount/100 }元</span></p>
					</div>
					<a href="${returnUrl}" class="fl go_recharge">继续充值</a>
				</div>
				<div class="fial pay_status_box">
					<span class="fial_bg fl"></span>

					<div class="fl pay_info">
						<p>充值失败</p>
						<p>高峰时段，可能会有延迟到账的情况！</p>
						<p>如有充值问题请联系微信客服：LIVE官方助手</p>
					</div>
					<a href="${returnUrl}" class="fl go_recharge">重新充值</a>
				</div>
			</div>
		</div>
	</div>
	<div class="foot">
		<p>版权所有©2016 巴士在线科技有限公司 赣ICP备12001072号-9</p>
		<p>地址:南昌市高新开发区火炬大街201号 | 联系电话:0791-82212891</p>
	</div>
</div>

<div class="log_box">
	<div class="log_top">
		登录LIVE <img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/close.png" class="close_btn"/>
	</div>
	<div class="log_warp">
		<div class="log_item">
			<label for="">手机号：</label>
			<input type="text" class="log_txt phone"/>
		</div>
		<div class="log_item">
			<label for="">密码：</label>
			<input type="password" class="log_txt pwd"/>
		</div>
		<div class="err_msg"></div>
		<div class="log_item">
			<a href="javascript:;" class="log_btn">登录</a>
		</div>
	</div>
	<div class="log_tips">
		<p>1.请填写您注册LIVE或在LIVE绑定的手机号和密码；</p>

		<p>2.如果您忘记密码请到LIVE客户端内找回密码；</p>
	</div>
</div>
<%--遮罩层--%>
<div class="skyBox"></div>
<%--提示框--%>
<%--<div class='buy-ok'>--%>
	<%--<em class="close-btn"></em>--%>
	<%--<div class='buy-ok-warp'>--%>
		<%--<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/ok-icon.png" class="ok-pic" alt=""/>--%>
		<%--<h4>提现成功</h4>--%>
		<%--<p class="ok-msg">已为账户：<span class="user-name2"></span>充值<span class="gold-num"></span>,可以给主播送礼物啦，GO！</p>--%>
	<%--</div>--%>
<%--</div>--%>
<%--<div class="load-box">--%>
	<%--<div class="loading">--%>
		<%--<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/londing.png" alt=""/>--%>
	<%--</div>--%>
<%--</div>--%>
<%--<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/common.js'></script>--%>

<%--<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/extract.js'></script>--%>

</body>
</html>