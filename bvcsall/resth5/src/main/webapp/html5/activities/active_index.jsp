<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
	WebClientUtils cu=WebClientUtils.getInstance();
	String cdnurl=cu.loadConfigUrl("html5", "cdnurl");
	String imageurl=cu.loadConfigUrl("html5", "imageurl");
	String interfaceurl=cu.loadConfigUrl("html5", "interfaceurl");
	if(cdnurl==null){
		cdnurl="http://cdn.wopaitv.com";
	}
	if(imageurl==null){
		imageurl="http://api.wopaitv.com";
	}
	if(interfaceurl==null){
		interfaceurl="http://api.wopaitv.com";
	}
	//interfaceurl = "http://10.12.129.85:8080";

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="description" itemprop="description" content="${activity.description}">
	<meta itemprop="image" id="cover" content="<%=imageurl%>/restwww/download${activity.cover}"><!-- 图片 -->
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/activities/active_index.css?t=<%=new java.util.Date().getTime()%>">
	<script data-main="<%=request.getContextPath()%>/js/views/active_index" src="<%=request.getContextPath()%>/js/libs/require.js"></script>
	<title>${activity.title }</title>
</head>
<body>
<img width="0" height="0" src="<%=imageurl%>/restwww/download${activity.cover}" style="float:left;">
<!-- <iframe src="#" frameborder="2" width="0" height="0" name="openapp" id="openapp"></iframe> -->
<jsp:include  page="../head.jsp"/>
<input type="hidden" id="actId" value="${activity.id }"/>
<!-- 内容 -->
<div class='main'>
	<div class='activeIndex'>
		<div class='activeIndexHead'>
			<div class='activeTitPic'>
				<img data-userPic="${activity.cover }" class="actIndexCover" src="" alt="">
				<p class="active-tit">${activity.title }</p>
			</div>
			<div class='activeMsg'>
				<span class='activeMsgCon'>
					${activity.description}
				</span>
				<i class="active-desc-arrow"></i>
			</div>
		</div>
		<div class='activeUseList'>
			<div class='videoListSort'>
				<ul>
					<li class="hot on">最热</li>
					<li class="latest">最新</li>
					<li class="canyu"><a href="javascript:void(0);"></a></li>
				</ul>
			</div>
			<div class="navContent">
				<div class="latestContent">
					<ul>
						
					</ul>
				</div>
				<div class="hotContent">
					<ul>
					
					</ul>
				</div>
			</div>
			</div>
			<!-- <div class='videoList1'>
				<ul>
				</ul>
				<div id="btnMore" class="loadMore" href="javascript:void(0)" >加载更多</div>
			</div> -->
		</div>
	</div>
	<div class='bottomDownload'>
		<a class='openwopai'><img width='250' src='<%=request.getContextPath()%>/img/icons-2x/DownloadBlivebutton.png' ></a>
	</div>
	
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