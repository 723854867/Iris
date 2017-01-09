<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
	WebClientUtils cu=WebClientUtils.getInstance();
	String cdnurl=cu.loadConfigUrl("html5", "cdnurl");
	String imageurl=cu.loadConfigUrl("html5", "imageurl");
	if(cdnurl==null){
		cdnurl="http://cdn.wopaitv.com";
	}
	if(imageurl==null){
		imageurl="http://api.wopaitv.com";
	}

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/attention_empty.css">
	<script src=''></script>
	<title>我的关注</title>
</head>
<body>
<jsp:include  page="../head.jsp"/>
<%--图片地址--%>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<%--视频地址--%>
<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
<!-- 内容 -->
<div class='main'>
	<div class='nav'>
		<p>可能感兴趣的人</p>
	</div>
	<div class='funPerson'>
		<dl>
			<dt><img src="../../../img/icons-2x/icon_tabbar_profile_sel.png" alt="头像">
				<em class='addV'></em>
			</dt>
			<dd>
				<p>草帽的舞台</p>
				<p>你又调皮了西崽哈哈哈哈哈</p>
			</dd>
			<dd>
				<a class='atten' href="javascript:;"></a>
			</dd>
		</dl>
	</div>
	<div class='videoList1'>
		<div class='emptyIcon'>
			<span class='empty1'></span>
			<div class='empty2'>
				
			</div>
			<p>此处应该有视频，关注后可看到TA的视频~</p>
		</div>
	</div>
	<div class='downBar'><a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载我拍，查看更多精彩</a></div>
</div>
</body>
</html>