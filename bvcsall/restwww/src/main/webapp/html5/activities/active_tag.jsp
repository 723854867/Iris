<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/activities/active_tag.css?t=<%=new java.util.Date().getTime()%>">
	<script src="<%=request.getContextPath()%>/js/libs/require.js" data-main="<%=request.getContextPath()%>/js/views/active_tag"></script>
	<title>${tag}</title>
</head>
<body>
<iframe src="#" frameborder="2" width="0" height="0" name="openapp" id="openapp"></iframe>
<jsp:include  page="../head.jsp"/>

<input type="hidden" value="${tag}" id="tagName"/>
<!-- 内容 -->
<div class='main'>
	<div class='activeIndex'>
		<div class='activeUseList'>
			<div class='videoList1'>
				<p class="tag-tit"><span class="tag-name">#${tag}#</span> <a href="javascript:void(0);" class="join-tag fr"></a></p>
				<ul>

				</ul>
				<div id="btnMore" class="loadMore" href="javascript:void(0)">加载更多</div>
			</div>
		</div>
	</div>
	<div class='downBar'><span class="downClose"></span><a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载我拍，查看更多精彩</a></div>
</div>
<div class="skyBox">
	<img src="<%=request.getContextPath()%>/img/gobrowser.png" alt=""/>
</div>
<div class="skyBox-anz">
	<img src="<%=request.getContextPath()%>/img/anz-browser.png" alt=""/>
</div>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>