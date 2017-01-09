<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<jsp:include page="cache.jsp" />
<div class='head'>
	<div class='logo'></div>
	<div class='lagarNav'>
		<ul>
			<li>
				<a href="<%=request.getContextPath()%>/page/homePage">
					<img src="<%=request.getContextPath()%>/img/icons-2x/icon_tabbar_index_nor.png" alt="">
					<p>推荐</p>
				</a>
			</li>
			<li>
				<a href="<%=request.getContextPath()%>/page/activity/home">
					<img src="<%=request.getContextPath()%>/img/icons-2x/icon_tabbar_activity_nor.png" alt="">
					<p>活动</p>
				</a>
			</li>
			<li>
				<a href="<%=request.getContextPath()%>/page/rank">
					<img src="<%=request.getContextPath()%>/img/icons-2x/login_icon_cup.png" alt="">
					<p>排行</p>
				</a>
			</li>
			<li>
				<a id="toAttention" href="javascript:void(0);">
					<img src="<%=request.getContextPath()%>/img/icons-2x/login_btn_plain_off.png" alt="">
					<p>关注</p>
				</a>
			</li>
			<li>
				<a id="toPerCener" href="javascript:void(0);">
					<img id="userPho" src="<%=request.getContextPath()%>/img/icons-2x/login_icon_nickname.png" alt="">
					<p>我</p>
				</a>
			</li>
		</ul>
	</div>
</div>
