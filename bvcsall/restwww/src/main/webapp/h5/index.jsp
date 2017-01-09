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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="format-detection" content="email=no" />
	<title>首页</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/player/js/jquery.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/jQuery.md5.js"></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/js/players/jwplayer.js'></script>
	<script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
	<script src="<%=request.getContextPath()%>/js/api.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/zepto.min.js"></script>
	<script type="text/javascript">
	  var pichost = 'http://api.wopaitv.com/restwww/download';
      var videohost = 'http://cdn.wopaitv.com/hls/play';
      var loadvideohost="<%=request.getContextPath()%>/video/loadconfigurl";
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
	            
					}
				}
			});
	
	</script>
	<script type="text/javascript">
//var requestUrlPrefix = getRootPath();
var pichost = 'http://api.wopaitv.com/restwww/download';
var videohost = 'http://cdn.wopaitv.com/hls/play';
var clientPf="html5";
var dataresult=null;


//根据热度指数查询视频（首页视频列表）
/* $(function(){
	var videopage=$("#videopage").val();
	var startIndex = parseInt((videopage-1)*3);
	$.ajax({
		type: "POST",
		dataType:'json',
		url: requestUrlPrefix+'/video/loadconfigurl',
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
	    
				}
			}
		});
}) */

    function playVideo(id, title, desc, username, avatar, date,videoPic,videoId,status){
                var videourl = videohost+"/hls/play/"+id+".m3u8";
                var poster = pichost+"/restwww/download"+videoPic;
                var playerContainer = "playerContainer_"+status;
                if(id){
                    //document.title = username + ' 邀请您加入【我拍】';
                    $("#"+playerContainer+"").show();
                    $("#"+playerContainer+"").empty();
                  
                    $('#description').text(desc);
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
          
                 $("#"+playerContainer+"").get(0).appendChild(scriptElem);
                }else{
                    //document.title = '您浏览的视频不存在！';
                    $("#"+playerContainer+"").hide();
                    //$('.description').text('您浏览的视频不存在！');
                }
            }
</script>
</head>


<body>

<!--弹框
<div class="bg" id="bg" > </div>
<div class="popDie popShowDel" >
    	<p class="h_30"><a href="javascript:void(0);" class="dele_icon popClose"></a></p>
		<a href="#" class="login">我拍账号登录</a>
	    <a href="#" class="register">注册</a>	
	    <div class="f-hr"><hr style="border:1px solid #999;" />
	    <p class="title_top">第三方登录</p></div>
	    <div class="icon_wrap_login">
	    	<a href="javascript:void(0);"><i class="icon_wechat"></i>微信好友</a>
	    	<a href="javascript:void(0);"><i class="icon_qq"></i>QQ好友</a>
	    	<a href="javascript:void(0);"><i class="icon_weibo"></i>新浪微博</a>
	    </div>
    
</div>-->
 <div class="tab">
    <ul class="tab-hd pr">
        <li class="active">首页</li><li>发现</li></ul>
        <span class="font-personal" ><i class="icon-personal"></i></span>
        <div class="a_wrap_box">
        	<i class="pop_jt"></i>
        	<a href="javascript:void(0);" class="a_hr"><i class="pop_icon1"></i>个人中心</a>
        	<a href="javascript:void(0);"><i class="pop_icon2"></i>退出账号</a>
        </div>
    <ul class="tab-bd">
        <li class="pad_40">
        <input type="hidden" id="videopage" name="videopage" value="1">
          <c:forEach var="one" items="${videolist}" varStatus="status" >
        	<div class="f-head-add">
        		<span class="js-span-a">
        		 <c:if test="${one.user.pic==null}">
				            <img src="<%=imageurl%><%=request.getContextPath()%>/img/avatar.png" width="30" height="30" class="f-avatar js-avatar">
				            </c:if>
				            <c:if test="${one.user.pic!=null}">
				            <img src="<%=imageurl%><%=request.getContextPath()%>/download${one.user.pic}" width="30" height="30" class="f-avatar js-avatar">
				  </c:if>
				</span>
				<p class="f-name box ellipsis">${one.user.name}</p>
				<p class="f-location box ellipsis"><fmt:formatDate value="${one.createDateStr}" pattern="MM-dd HH:mm"/></p>
				<a href="javascript:void(0)" class="f-sub add_btn_vipWarp"><i class="add_btn_vip"></i>订阅</a>
        	</div>
        	
        	<!--订阅-->
		    <section>
		    <!--订阅-->
		    <div class="f-head">
		          <!--      
					<span class="js-span-a" data-href="/user/1022971854">
						<img class="f-avatar js-avatar" src="http://mvavatar2.meitudata.com/55291d9c2fe93431.jpg!thumb60" width="30" height="30" alt="">
									</span>
					<p class="f-name box ellipsis">FlareOne</p>
					<p class="f-location box ellipsis">刚刚</p>
					<p class="f-sub">已订阅</p>
				  -->  
			</div>
			 <div id="playerContainer_${status.count}" class="body"
            onclick="playVideo('${one.playKey}','${one.name}','${one.description}','${one.user.name}','<c:if test="${one.user.pic==null}"><%=imageurl%><%=request.getContextPath()%>/img/avatar.png</c:if><c:if test="${one.user.pic!=null}"><%=imageurl%><%=request.getContextPath()%>/download${one.user.pic}</c:if>','<fmt:formatDate value="${one.createDateStr}" pattern="MM-dd HH:mm"/>','${one.videoPic}','${one.id}','${status.count}')">
              <img src="<%=imageurl%><%=request.getContextPath()%>/download${one.videoPic}" width="320" class="pic_con">
			</div>
			<p class="f-description break" id="description">${one.description}</p>
			<!--分享点赞-->
			<div class="f-bottom">
				<span class="f-info-item33" onclick="savePraise();"><i class="icon-heart-empty"></i>点赞</span>
				<span  class="js-span-comment f-info-item33 box f-info-comment">
						<i class="icon-comment-text"></i>评论</span>
				<span class="f-info-item33"><i class="icon-heart-share"></i>分享</span>
			</div>
			    </c:forEach>
			<a href="javascript:void(0);" class="moreLoding">点击加载更多</a>
			<p class="footer_link"><a href="javascript:void(0);" class="link1">联系我们</a><a href="javascript:void(0);" class="link2">关于我们</a></p>
			<section>
		</li>
        <li class="pad_40 hide">
        	<div class="wrap_padding">
	        	<div class="btn_wrap5">
	        		<input  type="text" placeholder="请输入搜索词" autocomplete="off" class="InputSeach">
	        	</div>
	        	<!--轮播-->
	        	<div class="slider">
				  <ul>
				    <li><a href="javascript:void(0);" target="_blank"><img src="<%=request.getContextPath()%>/img/1.jpg" alt="我拍"></a></li>
					<li><a href="javascript:void(0);" target="_blank"><img src="<%=request.getContextPath()%>/img/2.jpg" alt="我拍"></a></li>
					<li><a href="javascript:void(0);" target="_blank"><img src="<%=request.getContextPath()%>/img/3.jpg" alt="我拍"></a></li>
					<li><a href="javascript:void(0);" target="_blank"><img src="<%=request.getContextPath()%>/img/4.jpg" alt="我拍"></a></li>
					<li><a href="javascript:void(0);" target="_blank"><img src="<%=request.getContextPath()%>/img/5.jpg" alt="我拍"></a></li>
				  </ul>
				</div>
				<!--所有-->
				<a href="javascript:void(0);" class="active_btn"><i class="gift_icon"></i>所有活动</a>
				<div class="div1">
				 	<div class="div2"></div>
				    <div class="div3">
				        <a href="javascript:void(0);" class="div3-1"><img src="<%=request.getContextPath()%>/img/pic3.png" /></a>
				        <a href="javascript:void(0);" class="div3-2"><img src="<%=request.getContextPath()%>/img/pic3.png" /></a>
				    </div>
				</div>
				
				<ul class="piclist">
					<li>
						<div class="picimg fl">
							<img src="<%=request.getContextPath()%>/img/pic5.jpg" class="picw100">
							<p class="f-head3">
								美女					
							</p>
						</div>
						<div class="picimg fr">
							<img src="<%=request.getContextPath()%>/img/pic5.jpg" class="picw100">
							<p class="f-head3">
								搞笑						
							</p>
						</div>
					</li>
				</ul>
			</div>			
		</li>
        
    </ul>
</div>
<footer class="footer_fixed hide" style="display: block;">
	<span class="pr db fixed-l"><i class="fixed-icon"></i>个人中心</span>
    <span id="downloadBtn" data-href=" " class="js-span-a btn-bg1 btn-download">
    	<i class="icon-download"></i> <a href="http://www.wopaitv.com/oupeng/oupppp234/">下载我拍</a>
	</span>
 </footer>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/MobileSlider.js"></script>
<script type="text/javascript">
tabs(".tab-hd", "active", ".tab-bd");
$(".slider").yxMobileSlider({width:640,height:320,during:3000});
/*(function($){
	$('.popClose').on("tap",function(){
		$('.bg').css('display','none');
		$('.popDie').css('display','none');
	});
	$('.font-personal').on("tap",function(){
		showPop('popShowDel');
	});

})(Zepto)
(function($){
	$('.font-personal').on("tap",function(even){
		event.stopPropagation(); 
		$('.a_wrap_box').toggle(); 
		$(document).on("tap",function (event) { 
			$('.a_wrap_box').hide(); });  
	});

})(Zepto)*/
(function($){
	$('.font-personal').on("tap",function(even){
		
		$('.a_wrap_box').toggle();
		$(document).on("tap",function(e){
	    if(!$(e.target).hasClass("icon-personal")){
	        $(".a_wrap_box").hide();
	    }
	})


	});
	

})(Zepto)

Date.prototype.format = function(format){ 
var o = { 
"M+" : this.getMonth()+1, //month 
"d+" : this.getDate(), //day 
"h+" : this.getHours(), //hour 
"m+" : this.getMinutes(), //minute 
"s+" : this.getSeconds(), //second 
"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
"S" : this.getMilliseconds() //millisecond 
} 

if(/(y+)/.test(format)) { 
format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
} 

for(var k in o) { 
if(new RegExp("("+ k +")").test(format)) { 
format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
} 
} 
return format; 
} 
 </script>
 <script type="text/javascript">
	function savePraise(){
	var url='<%=request.getContextPath()%>/praise/sharePraise';
		$.ajax({
			type: "POST",
			dataType:'json',
			url: url,
			beforeSend: function(request) {
                        request.setRequestHeader("uid",loginid);
                        request.setRequestHeader("access_token",access_token);
                  },
			data: {
				invitedUid : majia,
				videoId:evaluationVideo
			},
			success: function(data){
				
				}
			});
	}
	
	 function checklogin(){
		 var user = getCookie("loginid");
		  if (user != "") {
		       
		    } else {
		    	showPop('popDie');
		    }
	} 
 </script>
</body>
</html>