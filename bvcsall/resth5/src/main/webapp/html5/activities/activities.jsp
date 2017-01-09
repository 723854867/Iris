<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/activities/activities.css?t=<%=new java.util.Date().getTime()%>">
	<script src="<%=request.getContextPath()%>/js/libs/require.js" data-main="<%=request.getContextPath()%>/js/views/active"></script>
	<script src='../../js/libs/TouchSlide.1.1.js'></script>

	<title>活动</title>
</head>
<body>
<jsp:include  page="../head.jsp"/>

<!-- 内容 -->
<div class='main'>
	<div class='activeIndex'>
		<div class='focus' id="slideBox">
			<div class="hd">
				<ul>
					<%--<c:forEach var="bannerList" items="${bannerList }" varStatus="status" >--%>
						<%--<li></li>--%>
					<%--</c:forEach>--%>
				</ul>
			</div>
			<div class="bd">
				<ul>
					<c:forEach var="bannerList" items="${bannerList }" varStatus="status" >
						<li data-id="${bannerList.targetId }" data-type="${bannerList.targetType }"><img data-userPic='${bannerList.imgSrc }' class="activeImg" src="" alt=""></li>
					</c:forEach>
				</ul>
			</div>

		</div>
		<div class='allActiveBtn'>
			<a href="/restwww/page/activity/activityList"><em class='giftIcon'></em>所有活动</a>
		</div>
		<div class='tagList'>
			<ul>
				<c:forEach var="hotLabelList" items="${hotLabelList }" varStatus="status" >
					<li><a href="javascript:;" data-id="${hotLabelList.id}" data-tagName="${hotLabelList.labelName}"><span class="activeTag">${hotLabelList.labelName}</span></a></li>
				</c:forEach>
			</ul>
		</div>
		<div class='activeList'>
			<ul>
				<c:forEach var="activityList" items="${activityList }" varStatus="status" >
					<li><a href="/restwww/page/activity/activityIndex?activityId=${activityList.id}"><img data-id="${activityList.id}" data-userPic='${activityList.cover}' class="activeImg" src="" alt=""></a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<div class='downBar'><span class="downClose"></span><a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载我拍，查看更多精彩</a></div>
</div>

<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
</body>
</html>