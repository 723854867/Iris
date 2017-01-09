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
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>足球宝贝招聘</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <link class="linkUrl" rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/css/style.css?t=1">
    <script src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/js/jquery-2.1.0.min.js"></script>
    <script src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/js/jquery.form.js"></script>
</head>
<body>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<input type="hidden" id="catalog" value="<%=request.getContextPath()%>"/>
<%--<input type="hidden" id="uid" name="uid" value="${uid }"/>--%>
<%--<input type="hidden" id="access_token" name="access_token" value="${accessToken }"/>--%>

<div class="main">
    <div class="top-msg">
        <em class="msg_arm"><img src="<%=request.getContextPath()%>/html5/app_within/school_recruit/img/arm.png" alt=""/></em>以下均为必填项，请认真填写
    </div>
    <form action="" id="from1" enctype="multipart/form-data" method="post">
        <div class="form-item">
            <span class="item-left">用户ID</span>
            <span class="item-right">
                <input type="text" value="" name="uid" re="\d+" maxlength="15" placeholder="个人头像右下角进入" class="item-txt item-txt-exsc"/>
            </span>
        </div>
        <div class="form-item">
            <span class="item-left">职业</span>
            <span class="item-right">
                <input type="text" value="" name="job" maxlength="15" placeholder="您的职业" class="item-txt item-txt-exsc"/>
            </span>
        </div>

        <div class="form-item">
            <span class="item-left">地区</span>
            <span class="item-right">
                <input type="text" value="" name="area" placeholder="您所在城市" class="item-txt item-txt-exsc"/>
            </span>
        </div>
        <div class="form-item">
            <span class="item-left">手机号</span>
				<span class="item-right">
					<input type="text" value="" placeholder="您经常使用的号码" name="phone" re="(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}" class="item-txt item-txt-exsc"/>
				</span>
        </div>
        <div class="form-item">
            <span class="item-left">验证码</span>
            <span class="item-right">
                <input type="text" value="" name="code" placeholder="验证码" class="item-txt1 item-txt-exsc"/>
                <%--<span class="ident-code-ok">获取验证码</span>--%>
                <input type="button" value="获取验证码" class="ident-code ident-code-ok"/>
            </span>
        </div>
        <div class="form-item">
            <a href="javascript:;" class="form-sub">提交申请</a>
        </div>
    </form>
</div>
<div class="sub_ok">
    <img src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/img/ok-icon.png" alt=""/>
    <p>已成功提交</p>
</div>
<div class="loading">
    <img src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/img/londing.png" alt=""/>
</div>
<script src="<%=request.getContextPath()%>/html5/app_within/soccer_recruit/js/index.js?t=2"></script>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
</body>
</html>