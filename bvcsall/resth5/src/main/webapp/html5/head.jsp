<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<jsp:include page="cache.jsp" />
<!-- <div class='head'>
	<div class='logo'></div>
	<div class="word-logo">直播就是LIVE</div>
	<div class="down-Btn">
		<a></a>
	</div>
</div> -->
<div class="topBlock">
	<img class="logo" width="40" src="<%=request.getContextPath()%>/img/icons-2x/87.png">
	<dl>
		<p>LIVE直播</p>
		<p>中国新歌声第五战队集结</p>
	</dl>
	<a class="joinBtn" href="javascript:void(0);">立即参战</a>
</div>
