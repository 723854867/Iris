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

String uid = (String)request.getParameter("uid");

%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="format-detection" content="email=no" />
	
	

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8">
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">

	
	<title>我拍-所有活动页面</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/player/js/jquery.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/jQuery.md5.js"></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/js/players/jwplayer.js'></script>
	<script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
	<script src="<%=request.getContextPath()%>/js/api.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/zepto.min.js"></script>
	
	<script type="text/javascript">
	   function openactivity(activityid){
			var uid='<%=uid%>';
			if(uid!=null){
				
		   window.location.href="<%=request.getContextPath()%>/html5/activity?activityid="+activityid+"&uid="+uid;
			}else{
				 window.location.href="<%=request.getContextPath()%>/html5/activity?activityid="+activityid;
			}
	   }
	
	    function openindex(){
	    	var uid= getCookie("wopaiuid");
	    	if(uid!=null&&uid.length>0){
	    		window.location.href="<%=request.getContextPath()%>/html5/index?uid="+uid;
	    	}else{
	    	window.location.href="<%=request.getContextPath()%>/html5/index";
	    	}
	    	
	    }
	</script>
</head>
<body>
<!--弹框-->
<div class="login_head">
		<span class="login_return"  style="cursor:pointer;" onclick="javascript:history.go(-1);"></span>
		<span class="login_head_txt">所有活动</span>
	</div>
<!--图-->
<ul class="pic_wrap_ul">
 <c:forEach var="activity" items="${allactivity}" varStatus="status" >
	<li ><a href="javascript:openactivity(${activity.id});"><img class="activity_cover" src="<%=imageurl%><%=request.getContextPath()%>/download${activity.cover}"/></a></li>
</c:forEach>
</ul>
<footer class="footer_fixed hide" style="display: block;">
	<span class="pr db fixed-l" onclick="openindex();"><img width="58" alt="我拍" class="pr db header-logo" onclick="javascript:openindex();" src="../img/index.png"></span>
    <span id="downloadBtn" data-href=" " class="js-span-a btn-bg1 btn-download">
    	<i class="icon-download"></i> <a href="http://www.wopaitv.com/oupeng/oupppp234/" target="_blank" style="color: #fff;">下载我拍</a>
	</span>
 </footer>
 
 <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299%26show%3Dpic1' type='text/javascript'%3E%3C/script%3E"));</script>
 <script type="text/javascript">
 $(function(){
	 $("#cnzz_stat_icon_1256005299").hide();
 });
 
 </script>
</body>
</html>