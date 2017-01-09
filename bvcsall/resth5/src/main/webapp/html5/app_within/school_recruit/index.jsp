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
    <title>校园大使招聘</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <link class="linkUrl" rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/school_recruit/css/style.css?t=1">
    <script src="<%=request.getContextPath()%>/html5/app_within/school_recruit/js/jquery-2.1.0.min.js"></script>
    <script src="<%=request.getContextPath()%>/html5/app_within/school_recruit/js/jquery.form.js"></script>
</head>
<body>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<input type="hidden" id="catalog" value="<%=request.getContextPath()%>"/>
<%--<input type="hidden" id="uid" name="uid" value="${uid }"/>--%>
<%--<input type="hidden" id="access_token" name="access_token" value="${accessToken }"/>--%>

<div class="main">
    <div class="top-msg">
        <em class="msg_arm"><img src="<%=request.getContextPath()%>/html5/app_within/school_recruit/img/arm.png" alt=""/></em>除邀请码均为必填项，请认真填写
    </div>
    <form action="" id="from1" enctype="multipart/form-data" method="post">
        <div class="form-item">
            <span class="item-left">用户ID</span>
            <span class="item-right">
                <input type="text" value="" name="uid" re="\d+" maxlength="15" placeholder="个人头像右下角进入" class="item-txt item-txt-exsc"/>
            </span>
        </div>
        <div class="form-item">
            <span class="item-left">邀请码</span>
            <span class="item-right">
                <input type="text" value="" name="inviteCode" maxlength="15" placeholder="只能绑定一次邀请码" class="item-txt"/>
            </span>
        </div>
        <%--<div class="form-item">--%>
            <%--<span class="item-left">年龄</span>--%>
            <%--<span class="item-right">--%>
                <%--<input type="text" value="" name="age" required="required" re="\d+" placeholder="您的年龄" class="item-txt item-txt-exsc"/>--%>
            <%--</span>--%>
        <%--</div>--%>
        <div class="form-item">
            <span class="item-left">学校</span>
            <span class="item-right">
                <input type="text" value="" name="school" placeholder="这一定是所伟大的学校" class="item-txt item-txt-exsc"/>
            </span>
        </div>
        <%--<div class="form-item">--%>
            <%--<span class="item-left item-select1">--%>
                <%--<h3 class="select-tit"><span class="select-word" data-type="qq">QQ号</span> <em class="arrow-down"></em></h3>--%>
                <%--<ul class="select-list1">--%>
                    <%--<li data-type="qq">QQ号 </li>--%>
                    <%--<li data-type="wechat">微信号 </li>--%>
                    <%--<em class='s_arrow'></em>--%>
                <%--</ul>--%>
            <%--</span>--%>
            <%--<span class="item-right">--%>
                <%--<input type="text" name="qq" re="\d+" class="item-txt net-number item-txt-exsc" placeholder="有号码的人一定很帅"/>--%>
                <%--<input type="text" name="wechat" class="item-txt net-number item-txt-exsc" placeholder="有号码的人一定很帅"/>--%>
            <%--</span>--%>
        <%--</div>--%>
        <div class="form-item">
            <span class="item-left">手机号</span>
				<span class="item-right">
					<input type="text" value="" name="phone" re="(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}" placeholder="打死我们也不泄露" class="item-txt item-txt-exsc"/>
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
        <%--<div class="form-item">--%>
            <%--<span class="item-left fl">自我介绍 </span>--%>
            <%--<span class="item-right">--%>
                <%--<textarea name="introduction" class="item-txtarea item-txt-exsc" placeholder="认真写的人，马上就会有桃花运" value=""></textarea>--%>
            <%--</span>--%>
            <%--<em class='word_num'>30</em>--%>
        <%--</div>--%>
        <%--<div class="form-item">--%>
            <%--<p class="file_msg">请上传您的生活照<span class="colccc">（小于2M的jpg或png图片）</span></p>--%>
            <%--<div class="doc-type1">--%>
                <%--<dl>--%>
                    <%--<dt>--%>
                        <%--<img src="<%=request.getContextPath()%>/html5/app_within/school_recruit/img/pic-border-icon.png" class="preview-pho1"/>--%>
                        <%--<input type="file" name="file" data-num="1" class="file-btn item-txt-exsc" accept="image/*">--%>
                    <%--</dt>--%>
                <%--</dl>--%>
            <%--</div>--%>
		<%--</div>--%>
        <div class="form-item">
            <a href="javascript:;" class="form-sub">提交申请</a>
        </div>
    </form>
</div>
<div class="sub_ok">
    <img src="<%=request.getContextPath()%>/html5/app_within/school_recruit/img/ok-icon.png" alt=""/>
    <p>已成功提交</p>
</div>
<div class="loading">
    <img src="<%=request.getContextPath()%>/html5/app_within/school_recruit/img/londing.png" alt=""/>
</div>
<script src="<%=request.getContextPath()%>/html5/app_within/school_recruit/js/index.js?t=2"></script>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
</body>
</html>