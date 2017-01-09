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
<html>
<head> 
	<!--  
    <meta name="viewport" content="width=device-width, minimal-ui">
    <meta name="format-detection" content="telephone=no, email=no">
    -->
    <meta charset="utf-8"> 
    <meta content="telephone=no" name="format-detection" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="default" name="apple-mobile-web-app-status-bar-style"> 
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, minimal-ui" name="viewport">
    
    <title>${video.user.name}的【我拍】“${video.description}”</title>
    <meta name="description" content="${video.description}">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/share.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/player/js/jquery.min.js"></script>
</head>
<body>
<div class="main">
    <div class="main-inner">
        <div style="display:none;"><img src="<%=imageurl%><%=request.getContextPath()%>/download${video.videoPic}"></div>
        <div class="user">
            <c:if test="${video.user.pic==null}">
            <img src="<%=imageurl%><%=request.getContextPath()%>/img/avatar.png"
                width="30" height="30" class="avatar">
            </c:if>
            <c:if test="${video.user.pic!=null}">
            <img src="<%=imageurl%><%=request.getContextPath()%>/download${video.user.pic}"
                width="30" height="30" class="avatar">
            </c:if>
            <div class="info">
                <div class="name">${video.user.name}</div>
                <div class="date">
                    <fmt:formatDate value="${video.createDateStr}" pattern="MM-dd HH:mm"/>
                </div>
            </div>
        </div>
        <div class="body"></div>
        <script type="text/javascript">
        var pichost = 'http://api.wopaitv.com';
        var videohost = 'http://cdn.wopaitv.com';
            var loadvideohost="loadconfigurl";
            var clientPf="html5";
           
    		$.ajax({
    			type: "POST",
    			dataType:'json',
    			url: loadvideohost,
    			data: {
    				clientPf : clientPf
    			},
    			success: function(data){
    				if(data!=null&&data.result!=null){
    					var listurl=data.result;
    					for(var i=0;i<listurl.length;i++){
    						if(listurl[i].type=='cdnurl'){
    							videohost=listurl[i].url;
    							continue;
    						}
    						if(listurl[i].type=='imageurl'){
    							pichost=listurl[i].url;
    							continue;
    						}
    					}
    					 playVideo('${video.playKey}','${video.description}','${video.description}','${video.user.name}',
    		                		'<c:if test="${video.user.pic==null}"><%=imageurl%><%=request.getContextPath()%>/img/avatar.png</c:if><c:if test="${video.user.pic!=null}"><%=imageurl%><%=request.getContextPath()%>/download${video.user.pic}</c:if>',
    		                		'<fmt:formatDate value="${video.createDateStr}" pattern="MM-dd HH:mm"/>','${video.videoPic}');
    					}
    				}
    			});
            //var lhost = '192.168.108.160:63880';
            //var lprotocol = 'http:';

            function playVideo(id, title, desc, username, avatar, date,videoPic){
                var videourl = videohost+"/hls/play/"+id+".m3u8";
                var poster = pichost+"/restwww/download"+videoPic;

                if(id){
                    document.title = username + '的【我拍】“'+ title +'”';
                    $('.user, .body').show();
                    $('.body').empty();
                    $('.user .name').text(username);
                    $('.user .date').text(date);
                    $('.description').text(desc);
                    $('.user img').attr('src', avatar);
                    $(window).scrollTop(0);
                    // 使用setup API设置时存在问题
                    // * 没有更新视频地址
                    // TODO querystring 方式传参会重复下载文件
                    // jQ 插入 script 有重复执行的问题（手机上会卡？）
                    var scriptElem = document.createElement('script');
                    scriptElem.src = 'http://api.wopaitv.com/restwww/js/player/sewise.player.min.js?' + decodeURIComponent($.param({
                        server: "vod",
                        type: "m3u8",
                        videourl: videourl,
                        skin: "vodWhite",
                        title: title,
                        lang: 'zh_CN',
                        logo: "http://api.wopaitv.com/restwww/img/empty.png",
                        poster: poster,
                        autostart: "true",
                        topbardisplay: "disable"
                    }));
                    $('.body').get(0).appendChild(scriptElem);
                }else{
                    document.title = '您浏览的视频不存在！';
                    $('.user, .body').hide();
                    $('.description').text('您浏览的视频不存在！');
                }
            }

            $(function(){
                $('a.more').click(function(e){
                    $('.main').scrollTop($('.head').offset().top);
                    return false;
                });

                $('.more').on('click', '.item', function(e){
                });

                if(/iPhone|iPad|iPod|android/i.test(navigator.userAgent)){
                    $('.download').attr('href', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo');
                }
            })
        </script>
        <p class="description">${video.description}</p>
        <div class="opera">
            用欧朋浏览器打开观看更流畅：
            <img src="<%=imageurl%><%=request.getContextPath()%>/img/opera.png" alt="欧朋浏览器">
            <a href="http://m.oupeng.com">欧朋浏览器</a>
        </div>
        <div class="more">
            <div class="head">精彩视频</div>
            <!-- 视频列表 -->
            <c:forEach var="one" items="${topHot}">
            <div class="item"
            onclick="playVideo('${one.playKey}','${one.name}','${one.description}','${one.user.name}',
                    		'<c:if test="${one.user.pic==null}"><%=imageurl%><%=request.getContextPath()%>/img/avatar.png</c:if><c:if test="${one.user.pic!=null}"><%=imageurl%><%=request.getContextPath()%>/download${one.user.pic}</c:if>',
                    		'<fmt:formatDate value="${one.createDateStr}" pattern="MM-dd HH:mm"/>','${one.videoPic}')"
            >
                <img src="<%=imageurl%><%=request.getContextPath()%>/download${one.videoPic}" width="320" class="poster">
                <div class="info">
                    <div class="left">
                    <c:if test="${one.user.pic==null}">
		            <img src="<%=imageurl%><%=request.getContextPath()%>/img/avatar.png" width="30" height="30" class="avatar">
		            </c:if>
		            <c:if test="${one.user.pic!=null}">
		            <img src="<%=imageurl%><%=request.getContextPath()%>/download${one.user.pic}" width="30" height="30" class="avatar">
		            </c:if>
                    <span class="title">${one.description}</span>
                    </div>
                </div>
            </div>
            </c:forEach>
            <!-- 视频列表结束 -->
        </div>
    </div>
</div>
<div class="foot">
    <div class="foot-inner">
        <a href="javascript:;" class="more">更多精彩视频</a>
        <a href="http://www.wopaitv.com" target="_blank" class="download">下载我拍</a>
    </div>
</div>
</body>
</html>
