<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/rank.css?t=<%=new java.util.Date().getTime()%>">
	<title>排行榜</title>
</head>
<body>
<!-- head -->
<jsp:include page="../head.jsp" />
<!-- 排行榜 -->
<div class='main'>
	<div class="rankHead">
		<button class="on" data="shoushi">收视榜</button>
		<button  data="renqi">人气榜</button>

	</div>
	<div class='videoList1'>
		<ul></ul>
		<div class="rankloadMore" id="btnMore" href="javascript:void(0);">加载更多</div>
	</div>
	

	<div class="downBar2">
		下载我拍，拍摄视频打榜
		<a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=rankDownload">下载</a>
	</div>

</div>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/rank.js" src="<%=request.getContextPath()%>/js/libs/require.js"></script>