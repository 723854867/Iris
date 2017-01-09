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

<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>好声音排名</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href=""/>
    <link class="linkUrl" rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/hsy_rank/css/style.css?t=1">
    <script src="<%=request.getContextPath()%>/html5/app_within/hsy_rank/js/libs/jquery-2.1.0.min.js"></script>
    <script src="<%=request.getContextPath()%>/html5/app_within/hsy_rank/js/index.js"></script>
</head>
<body>
<%--图片地址--%>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<input id="serverUrl" value="<%=request.getContextPath()%>" type="hidden"/>
<div class="main">
    <div class="top"><img src="<%=request.getContextPath()%>/html5/app_within/hsy_rank/img/top.png" alt=""/></div>
    <div class="top-tit"><img src="<%=request.getContextPath()%>/html5/app_within/hsy_rank/img/title.png" alt=""/></div>
    <div class="rank-list">
        <ul>
            <c:forEach var="hsylist" items="${list }" varStatus="status">
            <li>

                <div class="pho-box fl">
                    <span class="fl user-count"> ${status.count}</span>
                    <img src="" onerror="this.src='<%=request.getContextPath()%>/html5/app_within/hsy_rank/img/no-pho.jpg'" data-src="${hsylist.pic}" class="user-pho" alt=""/>
                    <div class="pho-bg">

                    </div>
                </div>
                <span class="user-info fl">
                    <h3>${hsylist.name}</h3>
                    <p>
                        ${hsylist.signature}
                    </p>
                </span>
                <span class="user-popularity fr">
                    <p>人气</p>
                    ${hsylist.popularity}
                </span>
            </li>
            </c:forEach>
        </ul>

    </div>
    <%--<div class="more">--%>
        <%--<a href="javascript:;" class="show-more"></a>--%>
    <%--</div>--%>

</div>
</body>
</html>