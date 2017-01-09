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
//    interfaceurl = "http://localhost:8080";
%>

<!DOCTYPE html>
<html lang="en">
<head>

	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link class="linkUrl" rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/mic/css/style.css?t=1">
	<title>我是麦霸</title>
</head>
<body>
<!--<%--图片地址--%>-->
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<!--<%--视频地址--%>-->
<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<%--<input type="hidden" id="activeId" value=" ${liveActivityId }"/>--%>
<div class="main">
    <div class="show-box">
        <div class="top">
            <img src="<%=request.getContextPath()%>/html5/app_within/mic/img/bj.png" class="bj" alt=""/>
        </div>
        <div class="tab-box">
            <div class="top-1">
                <p><img src="<%=request.getContextPath()%>/html5/app_within/mic/img/sign-btn.png" class="sign-btn" alt=""/></p>
                <p><img src="<%=request.getContextPath()%>/html5/app_within/mic/img/prize-tit.png" class="prize-tit" alt=""/></p>
            </div>
            <div class="tab-head">
                <ul>
                    <li class="on">总排名</li>
                    <li>人气榜</li>
                    <li>礼物榜</li>
                </ul>
            </div>
            <div class="tab-list">
                <div class="show tab1">
                    <ul>
                    </ul>
                </div>
                <div class="tab1">
                    <ul>
                    </ul>
                </div>
                <div class="tab1">
                    <ul>
                    </ul>
                </div>
            </div>
        </div>

    </div>
    <%--规则--%>
    <div class="rule">
        <em class="close-rule">关闭</em>
        <img src="<%=request.getContextPath()%>/html5/app_within/mic/img/guize.jpg" class="rule-img" alt=""/>
    </div>
    <%--报名--%>
    <div class="sign-box">
        <div class="sign-from">
            <div class="sign-from-1">
                <h3 class="sign-from-tit">在线报名</h3>
                <form action="" id="sign-form">
                    <input type="hidden" id="activeId" name="liveActivityId" value=" ${liveActivityId }"/>
                    <div class="input-item">
                        <label for="">用户ID：</label>
                        <input type="text" name="uid" class="item-txt"/>
                    </div>
                    <div class="input-item">
                        <label for="">学校名称：</label>
                        <input type="text" name="school" class="item-txt"/>
                    </div>
                    <div class="input-item">
                        <label for="">地区：</label>
                        <span>
                            <label for="area1"><input id="area1" type="radio" name="area" value="华东" checked="checked"/>华东</label>
                            <label for="area2"><input id="area2" type="radio" name="area" value="华北"/>华北</label>
                            <label for="area3"><input id="area3" type="radio" name="area" value="华南"/>华南</label>
                            <label for="area4"><input id="area4" type="radio" name="area" value="华中"/>华中</label>
                            <label for="area5"><input id="area5" type="radio" name="area" value="东北"/>东北</label>
                            <label for="area6"><input id="area6" type="radio" name="area" value="西南"/>西南</label>
                            <label for="area7"><input id="area7" type="radio" name="area" value="西北"/>西北</label>
                        </span>
                    </div>
                </form>
                <a href="javascript:;" class="sign-sub">报名</a>
            </div>

        </div>
        <div class="sign-success">
            <div class="sign-success-1">
                <div class="succ-top">
                    <div class="ok-box">
                        <img src="<%=request.getContextPath()%>/html5/app_within/mic/img/sign-ok.png" class="ok-icon" alt=""/>
                        <p>报名成功</p>
                    </div>
                    <div class="form-succ-item item-name">
                        用户名喝咖啡和好看
                    </div>
                    <div class="form-succ-item item-school">
                        用户名喝咖啡和范德萨发的 范德萨好看
                    </div>
                    <div class="form-succ-item item-area">
                        用户名喝咖啡和好看
                    </div>
                </div>
                <div class="succ-foot">
                    <img src="<%=request.getContextPath()%>/html5/app_within/mic/img/sign-tips.png" class="sign-pic" alt=""/>
                    <div class="iKonw">我知道了</div>
                </div>
            </div>

        </div>
    </div>
</div>
	<script src="<%=request.getContextPath()%>/html5/app_within/mic/js/jquery-2.1.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/html5/app_within/mic/js/index.js"></script>
</body>
</html>