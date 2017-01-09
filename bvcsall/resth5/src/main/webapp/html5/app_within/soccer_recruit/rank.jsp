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
    //interfaceurl = "http://192.168.151.116:8080";
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <link class="linkUrl" rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/css/rank.css?t=1">
    <script src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/js/jquery-2.1.0.min.js"></script>
</head>
<body>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<input id="serverUrl" value="<%=request.getContextPath()%>" type="hidden"/>
	<img width="100%" src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/img/bg.png">
	<div class="imgGroup">
		<a class="ac_function_btn" href="javascript:void(0);"></a>
		<img width="100%" src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/img/soc_function.png">
	</div>
	<div class="moreGreen"></div>
	<div class="rank">
		<div class="rankCtn">
			<c:forEach var="list" items="${list}" varStatus="status">
		    	<p data-pic="${list.pic }">
		    	<span class="indexNum">${status.count }</span>
		    		<span class="userPic"></span><span class="userName">${list.name }</span>
		    		
		    		<span class="popularity"><em>人气</em><em>${list.popularity }</em></span>
		    	</p>
		    </c:forEach>
		</div>
	</div>
	<div class="ac_rule">
		<img class="ruleBg" width="80%" src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/img/soc_dg.png">
		<img class="closeBtn" width="50" src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/img/10.png">
		<div class="ac_copy">
			<p>活动时间：</p>
			<p>2016.6.11-2016.7.11</p>
			<p>活动规则：</p>
			<p>1.  6.11-7.04号期间进行足球宝贝海选，共计选出4名足球宝贝，角逐最后的冠军。</p>
			<p>6.11-6.15，按照活动专区专属礼物排行榜，选出排名第一的一名足球宝贝；</p>
			<p>6.15-6.23，按照人气排行榜，选出排名第一的一名足球宝贝；</p>
			<p>6.23-6.28，按照人气排行榜，选出排名第一的一名足球宝贝；</p>
			<p>6.28-7.04，按照人气排行榜，选出排名第一的一名足球宝贝；</p>
			<p>2.  已选出的4名足球宝贝，根据总人气排行，按照先后顺序分别选择欧洲杯4强中自己支持的球队。</p>
			<p>3.  冠军球队足球宝贝即为第一名，亚军球队足球宝贝即为第二名，其余两名足球宝贝即为三四名。</p>
			<p>活动奖品：</p>
			<p>1.第一名，将获得100000金币奖励</p>
			<p>2.第二名，将获得60000金币奖励</p>
			<p>3.三四名分别获得20000金币奖励</p>
			<p>4.进入欧洲杯4强的足球宝贝，分别获得欧洲杯周边套装一套。</p>
			<p>获奖指南：</p>
			<p>1.直播间观看直播的用户越多，人气越高，赶快邀请你的好友来观看你的直播吧。</p>
			<p>2.本次足球宝贝的评选仅限定女性用户；</p>
			<p>3.所有参加活动用户必须通过页面下方“报名入口”完成报名。</p>
		</div>
	</div>
	<div class="fixedBar">
		<a href="<%=interfaceurl%>/resth5/footballGirl/home"><img src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/img/soc_btn.png"></a>
	</div>
	<div class="blackBox"></div>
<script src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/js/rank.js?t=2"></script>
</body>
</html>