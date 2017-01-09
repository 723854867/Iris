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

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>${ ruser.name }的个人主页</title>
	<meta name="description" itemprop="description" content="<c:if test="${ruser.signature == '' || ruser.signature==  null}">懒人没前途o(∩_∩)o 哈哈~</c:if>
				<c:if test="${ruser.signature != '' && ruser.signature !=  null}">
					${ruser.signature }
				</c:if>">
	<meta itemprop="image" id="cover" content="<%=imageurl%>/restwww/download${ruser.pic }"><!-- 图片 -->
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/personalCenter/personalDetail.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
	<jsp:include page="../head.jsp" />
	<div class="personalBg">
		<a href="javascript:;" class='playData'><em id="attentionBtn" class="attenBtn" data-userId="${ruser.id}"></em></a>
		<div class="personalInfo">
			<img class='userHomePic' data-homePicSrc='${ruser.pic }' src='' onerror='this.src=""'   alt="" />
			<c:if test="${ ruser.vipStat eq 2}">
				<img class="vIcon" src="<%=request.getContextPath()%>/img/icons-2x/yellow_icon.png"  alt="">
			</c:if>
			<c:if test="${ ruser.vipStat eq 1}">
				<img class="vIcon" src="<%=request.getContextPath()%>/img/icons-2x/blue_icon.png"  alt="">
			</c:if>
			<c:if test="${ ruser.vipStat eq 3}">
				<img class="vIcon" src="<%=request.getContextPath()%>/img/icons-2x/green_icon.png" alt="">
			</c:if>
			<c:if test="${ ruser.vipStat eq 0}">
				
			</c:if>
			<ul>
				<li><strong>${ ruser.name }</strong>
				<c:if test="${ruser.sex eq 0}">
					<img id="sex" src="<%=request.getContextPath()%>/img/icons-2x/sex_woman_icon.png"  alt="">
				</c:if>
				<c:if test="${ruser.sex eq 1}">
					<img id="sex" src="<%=request.getContextPath()%>/img/icons-2x/sex_man_icon.png"  alt="">
				</c:if>
				<c:if test="${ruser.sex eq 2}">
					<img id="sex" src="<%=request.getContextPath()%>/img/icons-2x/sex_man_icon.png"  alt="">
				</c:if>
				<img src="<%=request.getContextPath()%>/img/icons-2x/liveIcon.png">
				</li>
				<li>关注&nbsp; ${ ruser.attentionCount }   &nbsp;&nbsp;&nbsp;   粉丝 &nbsp;${ ruser.fansCount }</li>
				<c:if test="${ruser.signature == '' || ruser.signature==  null}">
					<li>懒人没前途o(∩_∩)o 哈哈~</li>
				</c:if>
				<c:if test="${ruser.signature != '' && ruser.signature !=  null}">
					<li>${ruser.signature }</li>
				</c:if>
			</ul>
		</div>
	</div>
	<div class="personalList">
		<ul>
		
		</ul>
	</div>
	<div class='bottomDownload'>
		<a class='openwopai'>LIVE直播，薛之谦出任第五位导师！</a>
	</div>
	<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
	<div class="bgBox"></div>
	<div class="showBox">
		<img class='closeBtn' width='25' src="<%=request.getContextPath()%>/img/icons-2x/close.png">
		<img class='imgBox' src="">
		<a class='openwopai'><img class='downloadBtn' width='210' src="<%=request.getContextPath()%>/img/personalBg/download.png"></a>
	</div>
	<div class="skyBox-anz">
		<img src="<%=request.getContextPath()%>/img/anz-browser.png" alt=""/>
	</div>
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/personalDetail" src="<%=request.getContextPath()%>/js/libs/require.js"></script>