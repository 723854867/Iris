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

	
	<title>我拍-首页</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/player/js/jquery.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/jQuery.md5.js"></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/js/players/jwplayer.js'></script>
	<script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/zepto.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/api.js"></script>
	<script src="<%=request.getContextPath()%>/js/MobileSlider.js"></script>
		<script src="<%=request.getContextPath()%>/js/rlapi.js"></script>
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
		
	function playVideo(id, title, videoPic,videoId,selectid){
        var videourl = videohost+"/hls/play/"+id+".m3u8";
        var poster = pichost+"/restwww/download"+videoPic;
        var playvideoid = $("#playvideoid").val();
        var playvideopic = $("#playvideopic").val();
        $("#playvideoid").val(selectid+videoId);
        $("#playvideopic").val(videoPic);
        if(id){
            //document.title = username + ' 邀请您加入【我拍】';
           // $('.user, .body').show();
           if(playvideoid){
           $('.'+playvideoid).empty();
           $('.'+playvideoid).hide();
           var imgsrc="<%=imageurl%><%=request.getContextPath()%>/download"+playvideopic;
           $('#'+playvideoid).html("<img src='"+imgsrc+"'/>");
           $('#'+playvideoid).show();
           }
            $('#'+selectid+videoId).empty();
            $('#'+selectid+videoId).hide();
           // $('.user .name').text(username);
           // $('.user .date').text(date);
           // $('.description').text(desc);
           // $('.user img').attr('src', avatar);
           // $(window).scrollTop(0);
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
            $('.'+selectid+videoId).get(0).appendChild(scriptElem);
            $('.'+selectid+videoId).show();
            
        }else{
            //document.title = '您浏览的视频不存在！';
           // $('.body').hide();
            //$('.description').text('您浏览的视频不存在！');
        }
       // getevaluationList(1,1,videoId);
       // ispraise();
    }
	
	function rlclose(){
	    var playvideoid = $("#playvideoid").val();
        var playvideopic = $("#playvideopic").val();
        if(playvideoid){
            $('.'+playvideoid).empty();
            $('.'+playvideoid).hide();
            var imgsrc="<%=imageurl%><%=request.getContextPath()%>/download"+playvideopic;
            $('#'+playvideoid).html("<img src='"+imgsrc+"'/>");
            $('#'+playvideoid).show();
            }
	}
    function addpraise(videoId){
     	 if( !checklogin()){
    		 return;
    	 }
     	var wopaiuid = getCookie("wopaiuid");
    	var access_token = getCookie("wopaitoken");
    	var addPraise="<%=request.getContextPath()%>/praise/savePraise";
    	var deletePraise="<%=request.getContextPath()%>/praise/deletePraise";
    	var praiseCount=parseInt($("#praiseCountspan"+videoId).html());
    	var selectid="praiseh";
    	var color=$("#"+selectid+videoId).css("color");
    	if(color!=null&&color=="rgb(254, 85, 140)"){
    	 	$.ajax({
    			type: "POST",
    			dataType:'json',
    			url: deletePraise,
    			data: {
    				videoId : videoId,
    				access_token : access_token
    			},
    			beforeSend: function(XMLHttpRequest) {
      		       XMLHttpRequest.setRequestHeader("uid",wopaiuid);
      			},
    			success: function(data){
    				if(data!=null&&data.code!=null){
    					var code=data.code;
    					if(code=="200"||code==200){
        					var selectid="praiseh";
    						if($("#"+selectid+"i"+videoId)){
    							$("#"+selectid+videoId).css("color","#90a092");
        						$("#"+selectid+"i"+videoId).removeClass("icon-heart-empty2");
        						$("#"+selectid+"i"+videoId).addClass("icon-heart-empty");
    						}
	    					selectid="praisen";
	    					if($("#"+selectid+"i"+videoId)){
	    						$("#"+selectid+videoId).css("color","#90a092");
	    						$("#"+selectid+"i"+videoId).removeClass("icon-heart-empty2");
	    						$("#"+selectid+"i"+videoId).addClass("icon-heart-empty");
	    					}
	    					$("#praiseCountspan"+videoId).html(praiseCount-1);
    					}else{
    						if(data.code!=null&&data.code=="401"){
      						  clearCookie("wopaiuid");
  							   clearCookie("wopaitoken");
  							   openlogin();
      					}
    						}
    				}
    			}
    		});
    	 	
    	}else{
    	
    	
    	$.ajax({
			type: "POST",
			dataType:'json',
			url: addPraise,
			data: {
				videoId : videoId,
				access_token : access_token,
				uid:wopaiuid
			},
			beforeSend: function(XMLHttpRequest) {
   		       XMLHttpRequest.setRequestHeader("uid",wopaiuid);
   			},
			success: function(data){
				if(data!=null&&data.code!=null){
					var code=data.code;
					if(code=="200"||code==200){
							var selectid="praiseh";
    						if($("#"+selectid+"i"+videoId)){
    							$("#"+selectid+videoId).css("color","#FE558C");
        						$("#"+selectid+"i"+videoId).removeClass("icon-heart-empty");
        						$("#"+selectid+"i"+videoId).addClass("icon-heart-empty2");
    						}
	    					selectid="praisen";
	    					if($("#"+selectid+"i"+videoId)){
	    						$("#"+selectid+videoId).css("color","#FE558C");
	    						$("#"+selectid+"i"+videoId).removeClass("icon-heart-empty");
	    						$("#"+selectid+"i"+videoId).addClass("icon-heart-empty2");
	    					}
        						$("#praiseCountspan"+videoId).html(praiseCount+1);
					}else{
						if(data.code!=null&&data.code=="401"){
  						  clearCookie("wopaiuid");
							   clearCookie("wopaitoken");
							   openlogin();
  					}
					}
				}
			}
		});
    	}
    }
    function addAttention(attentionId){
    	 if( !checklogin()){
    		 return;
    	 }
     	var wopaiuid = getCookie("wopaiuid");
    	var access_token = getCookie("wopaitoken");
    	var Attentionhtml=$("#Attention"+attentionId).html();
    	var isAttention=0;
    	if(Attentionhtml=="已订阅"){
    		isAttention=1;
    	}
    	var addAttention="<%=request.getContextPath()%>/attention/payAttention";
    	var dataFrom="h5";
    	$.ajax({
			type: "POST",
			dataType:'json',
			url: addAttention,
			data: {
				attentionId : attentionId,
				isAttention : isAttention,
				dataFrom:dataFrom,
				access_token : access_token,
				uid:wopaiuid
			},
			beforeSend: function(XMLHttpRequest) {
   		       XMLHttpRequest.setRequestHeader("uid",wopaiuid);
   			},
			success: function(data){
				if(data!=null&&data.code!=null){
					var code=data.code;
					if(code=="200"||code==200){
						if(isAttention==0){
							
							$("p[name='Attention"+attentionId+"']").html("已订阅");
						}else{
							$("p[name='Attention"+attentionId+"']").html("+订阅");	
						}
					}else{
						if(data.code!=null&&data.code=="401"){
  						  clearCookie("wopaiuid");
							   clearCookie("wopaitoken");
							   openlogin();
  					}
					}
				}
			}
		});
    }
    
    function morevideo(videotype){
    	var pagesize=5;
    	var wopaiuid = getCookie("wopaiuid");
    	if(videotype=="hot"){
    		var page=$("#hotvideopage").val();
    		var startIndex=parseInt(parseInt(page)*parseInt(pagesize));
        	var loadmorevide="<%=request.getContextPath()%>/html5/findHotIndiceVideos";
        	//var activityid='${activityid}';
        	$.ajax({
    			type: "POST",
    			dataType:'json',
    			url: loadmorevide,
    			data: {
    				startIndex : startIndex,
    				activityId : 0,
    				count : pagesize,
    				uid:wopaiuid
    			},
    			success: function(data){
    				if(data!=null&&data.result!=null){
    					var videos=data.result;
    					var apphtml="";
    					if(videos.length<pagesize){
    						$("#morehot").html("没有更多了~");
    					}
    					for(var i=0;i<videos.length;i++){
    						var videoh=videos[i];
    					 apphtml+="<div class='f-head' id='div"+videoh.id+"'><span class='js-span-a' data-href='/user/1022971854' >";
    					if(videoh.user.pic==null){
    					 apphtml+="<img class='f-avatar js-avatar' src='<%=imageurl%><%=request.getContextPath()%>/img/avatar.png' width='30' height='30' alt=''></span>";
    					}else{
    					 apphtml+="<img class='f-avatar js-avatar' src='<%=imageurl%><%=request.getContextPath()%>/download"+videoh.user.pic+"' width='30' height='30' alt=''></span>";
    					}
    					
   					
    					if(videoh.user.vipStat==1){
    						apphtml+= "<span id='lanv'><img class='lanv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/lanv.png'/></span>";
    					}else if(videoh.user.vipStat==2){
    						apphtml+= "<span id='hangv'><img class='hangv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/huangv.png'/></span>";
    					}
    					
    					
    					
    					 apphtml+="	<p class='f-name box ellipsis'>"+videoh.user.name+"</p><p class='f-location box ellipsis'>"+videoh.formatCreateDateStr+"</p>";
    					if(videoh.attentionAuthor==1||videoh.attentionAuthor=='1'){
    					 apphtml+="<p id='Attention"+videoh.user.id+"' name='Attention"+videoh.user.id+"' class='f-sub' onclick=\"addAttention('"+videoh.user.id+"');\">已订阅</p></div>";
    					}else{
    					 apphtml+="<p id='Attention"+videoh.user.id+"' name='Attention"+videoh.user.id+"' class='f-sub' onclick=\"addAttention('"+videoh.user.id+"');\">+订阅</p></div>";
    					}
    					 var playVideo="'"+videoh.playKey+"','"+videoh.name+"','"+videoh.videoPic+"','"+videoh.id+"','bodyh'";
    					 apphtml+="<div class='body' id='bodyh"+videoh.id+"' onclick=\"playVideo('"+videoh.playKey+"','"+videoh.nameReplace+"','"+videoh.videoPic+"','"+videoh.id+"','bodyh');\">";
    					 apphtml+="<img src='<%=imageurl%><%=request.getContextPath()%>/download"+videoh.videoPic+"' class='pic_con'/></div>";
    					 apphtml+="<div class='bodyh"+videoh.id+" body'  style='display:none'></div>";
    					 apphtml+="<p class='f-description break'>"+videoh.description+"</p><div class='f-bottom'>";
    					if(videoh.praise==true||videoh.praise=='true'){
    						
    					 apphtml+="<span class='f-info-item33' id='praiseh"+videoh.id+"' style='color:#FE558C;'  onclick=\"addpraise('"+videoh.id+"');\"><i class='icon-heart-empty2' id='praisehi"+videoh.id+"'></i><span id='praiseCountspan"+videoh.id+"'>"+videoh.praiseCount+" </span></span>";
    					}else{
    					 apphtml+="<span class='f-info-item33' id='praiseh"+videoh.id+"' onclick=\"addpraise('"+videoh.id+"');\"><i class='icon-heart-empty' id='praisehi"+videoh.id+"'></i><span id='praiseCountspan"+videoh.id+"'>"+videoh.praiseCount+" </span></span>";
    					}
    					 apphtml+="<span  class='js-span-comment f-info-item33 box f-info-comment' onclick=\"openbanner('video',"+videoh.id+")\"><i class='icon-comment-text'></i>"+videoh.evaluationCount+"</span>	";
    					 apphtml+="<span class='f-info-item33 f_btn' id='f_btn' onclick=\"openfenxiang('"+videoh.playKey+"');setfenxiang('"+videoh.playKey+"','"+videoh.user.name+"','"+videoh.description+"','"+videoh.videoPic+"');\"><i class='icon-heart-share' ></i>分享</span></div>";
    					}
    					
    					$("#hotsection").append(apphtml);
    					
    						$("#hotvideopage").val(parseInt(page)+1);
    						 if(wopaiuid!=null&&wopaiuid.length>0){
    						  $("p[name='Attention"+wopaiuid+"']").hide();
    						   }
    					}else{
    						$("#morehot").html("没有更多了~");
    						if(data.code!=null&&data.code=="401"){
      						  clearCookie("wopaiuid");
  							   clearCookie("wopaitoken");
  							   openlogin();
      					}
    					}	
    			}
    		});
    	}else{
    		var page=$("#newvideopage").val();
        	var loadmorevide="<%=request.getContextPath()%>/html5/findNewActVideos";
        	var activityid='${activityid}';
        	$.ajax({
    			type: "POST",
    			dataType:'json',
    			url: loadmorevide,
    			data: {
    				page : page,
    				actId : activityid,
    				size : pagesize,
    				uid:wopaiuid
    			},
    			success: function(data){
    				if(data!=null&&data.result!=null){
    					var videos=data.result;
    					var apphtml="";
    					if(videos.length<pagesize){
    						$("#morenew").html("没有更多了~");
    					}
    					for(var i=0;i<videos.length;i++){
    						var videon=videos[i];
    					 apphtml+=" <div class='f-head' id='div"+videon.id+"'><span class='js-span-a' data-href='/user/1022971854' >";
    					if(videon.user.pic==null){
    					 apphtml+="<img class='f-avatar js-avatar' src='<%=imageurl%><%=request.getContextPath()%>/img/avatar.png' width='30' height='30' alt=''></span>";
    					}else{
    					 apphtml+="<img class='f-avatar js-avatar' src='<%=imageurl%><%=request.getContextPath()%>/download"+videon.user.pic+"' width='30' height='30' alt=''></span>";
    						
    					}
    					
    					if(videoh.user.vipStat==1){
    						apphtml+= "<span id='lanv'><img class='lanv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/lanv.png'/></span>";
    					}else if(videoh.user.vipStat==2){
    						apphtml+= "<span id='hangv'><img class='hangv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/huangv.png'/></span>";
    					}
    					
    					 apphtml+="	<p class='f-name box ellipsis'>"+videon.user.name+"</p><p class='f-location box ellipsis'>"+videon.formatCreateDateStr+"</p>";
    					if(videon.attentionAuthor==1||videon.attentionAuthor=='1'){
    					 apphtml+="<p id='Attention"+videon.user.id+"' name='Attention"+videon.user.id+"' class='f-sub' onclick=\"addAttention('"+videon.user.id+"');\">已订阅</p></div>";
    					}else{
    					 apphtml+="<p id='Attention"+videon.user.id+"' name='Attention"+videon.user.id+"' class='f-sub' onclick=\"addAttention('"+videon.user.id+"');\">+订阅</p></div>";
    					}
    					 apphtml+="<div class='body' id='bodyn"+videon.id+"' onclick=\"playVideo('"+videon.playKey+"','"+videon.nameReplace+"','"+videon.videoPic+"','"+videon.id+"','bodyn')\">"; 
    					 apphtml+="<img src='<%=imageurl%><%=request.getContextPath()%>/download"+videon.videoPic+"' class='pic_con'/></div>";
    					 apphtml+="<div class='bodyn"+videon.id+" body' style='display:none'></div>";
    					 apphtml+="<p class='f-description break'>"+videon.description+"</p><div class='f-bottom'>";
    					if(videon.praise==true||videon.praise=='true'){
    						
    					 apphtml+="<span class='f-info-item33' id='praisen"+videon.id+"' style='color:#FE558C;'  onclick=\"addpraise('"+videoh.id+"');\"><i class='icon-heart-empty2' id='praiseni"+videon.id+"'></i><span id='praiseCountspan"+videon.id+"'>"+videon.praiseCount+" </span></span>";
    					}else{
    					 apphtml+="<span class='f-info-item33' id='praisen"+videon.id+"' onclick=\"addpraise('"+videon.id+"');\"><i class='icon-heart-empty' id='praiseni"+videon.id+"'></i><span id='praiseCountspan"+videon.id+"'>"+videon.praiseCount+" </span></span>";
    						
    					}
    					 apphtml+="<span  class='js-span-comment f-info-item33 box f-info-comment'><i class='icon-comment-text'></i>"+videon.evaluationCount+"</span>	";
    					 apphtml+="<span class='f-info-item33 f_btn' id='f_btn' onclick=\"openfenxiang('"+videon.playKey+"');setfenxiang('"+videoh.playKey+"','"+videoh.user.name+"','"+videoh.description+"','"+videoh.videoPic+"');\"><i class='icon-heart-share' ></i>分享</span></div>";
    					}
    					
    					$("#newsection").append(apphtml);
    					$("#newvideopage").val(parseInt(page)+1);
						 if(wopaiuid!=null&&wopaiuid.length>0){
   						  $("p[name='Attention"+wopaiuid+"']").hide();
   						   }
    				}else{
    					$("#morenew").html("没有更多了~");
    					if(data.code!=null&&data.code=="401"){
  						  clearCookie("wopaiuid");
							   clearCookie("wopaitoken");
							   openlogin();
  					}
    				}
    			}
    		});
    	}
    }
    function openfenxiang(playKey){
    	$(".pop_wrap").show();
    }
    function opengeren(){
    	   var wopaiuid = getCookie("wopaiuid");
    	   if(wopaiuid!=null&&wopaiuid.length>0){
    	$(".a_wrap_box").show();
    	   }else{
    		   openlogin();
    	   }
    }
    function closegeren(){
    	$(".a_wrap_box").hide();
    }
    function closefenxiang(){
    	$(".pop_wrap").hide();
    }
	function openbanner(opentype,openid){
		if(opentype=="activity"){
			window.location.href="<%=request.getContextPath()%>/html5/activity?activityid="+openid;   
		}else if(opentype=="video"){
			window.location.href="<%=request.getContextPath()%>/html5/videodetail?videoId="+openid;  
		}else if(opentype=="user"){
			
		}
		
	}
	$(function(){
		  $(".popDie1").hide();
		  closegeren();
		  closeoropengr();
		   var wopaiuid = getCookie("wopaiuid");
		   if(wopaiuid!=null&&wopaiuid.length>0){
		  $("p[name='Attention"+wopaiuid+"']").hide();
		   }
		})
	function openlogin(){
        window.location.href="<%=request.getContextPath()%>/h5/login.jsp";
            	
   }
   function openregister(){
       	window.location.href="<%=request.getContextPath()%>/h5/reg.jsp";
            	
   }		
   function closeoropengr(){
	   var wopaiuid = getCookie("wopaiuid");
	   if(wopaiuid!=null&&wopaiuid.length>0){
		   
	 $(".font-personal").show();
	 getUserInfo();
	   }else{
	// $(".font-personal").hide();
		   
	   }
	 
   }
   function exituser(){
	   clearCookie("wopaiuid");
	   clearCookie("wopaitoken");
	   openindex();
	   
   }
   
   function openindex(){
   	var uid= getCookie("wopaiuid");
   	if(uid!=null&&uid.length>0){
   		window.location.href="<%=request.getContextPath()%>/html5/index?uid="+uid;
   	}else{
   	window.location.href="<%=request.getContextPath()%>/html5/index";
   	}
   	
   }
   function setfenxiang(shareplayKey,shareName,sharedescription,sharepic){
   	$("#shareplaykey").val(shareplayKey);
   	$("#shareName").val(shareName);
   	$("#sharedescription").val(sharedescription);
   	$("#sharepic").val(sharepic);
   }
   
   function openshare(type){
		var shareplaykey=$("#shareplaykey").val();
		var shareName=$("#shareName").val();
		var sharedescription=$("#sharedescription").val();
		var sharepic=$("#sharepic").val();
		var pics="<%=imageurl%><%=request.getContextPath()%>/download"+sharepic;
		var title=shareName+"的【我拍】";
		var wopaiuid = getCookie("wopaiuid");
		var url;
		if(wopaiuid!=null&&wopaiuid.length>0){
		url=window.location.host+"<%=request.getContextPath()%>/video/thirdVideo?playKey="+shareplaykey+"&shareuid="+wopaiuid;
		}else{
		url=window.location.host+"<%=request.getContextPath()%>/video/thirdVideo?playKey="+shareplaykey;
		}
		var uid=getCookie("wopaiuid");
		if(sharepic==null||sharepic.length<=0){
    		pics="http://api.wopaitv.com/restwww/img/logo.png";
    		}else{
    			if(pics.indexOf("http://") >= 0 ){
    			}else{
    			pics="http://"+pics;
    				
    			}
    		}
        	if(type=="qzone"){
        		window.open("http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url="+encodeURIComponent("http://"+url)+"&summary="+sharedescription+"&pics="+pics+"&title="+title);
        	}else if(type=="qqfriend"){
        		window.open("http://connect.qq.com/widget/shareqq/index.html?title="+title+"&url="+encodeURIComponent("http://"+url)+"&summary="+sharedescription+"&pics="+pics);
        	}else if(type=="weibo"){
        		window.open("http://v.t.sina.com.cn/share/share.php?url="+encodeURIComponent("http://"+url)+"&title="+title+"&pics="+pics);
        	}
}
   
   function openallactivity(){
		var uid=getCookie("wopaiuid");
		if(uid!=null||uid.length>0){
			
	   window.location.href="<%=request.getContextPath()%>/html5/allactivity?uid="+uid;
		}else{
			 window.location.href="<%=request.getContextPath()%>/html5/allactivity";
		}
   }
	</script>
	
	</script>
</head>
<body>


	<input type="hidden" name="shareplaykey" id="shareplaykey" value=""/>
	<input type="hidden" name="shareName" id="shareName" value=""/>
	<input type="hidden" name="sharedescription" id="sharedescription" value=""/>
	<input type="hidden" name="sharepic" id="sharepic" value=""/>

<input type="hidden" id="playvideoid" name="playvideoid" value=""/>
<input type="hidden" id="playvideopic" name="playvideopic" value=""/>
<input type="hidden" id="hotvideopage" name="playvideopic" value="1"/>
<input type="hidden" id="newvideopage" name="newvideopage" value="2"/>
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
        
        <span class="font-personal" onclick="opengeren();" ><i class="icon-personal"></i></span>
       <div class="a_wrap_box">
        	<i class="pop_jt"></i>
        	<!-- <a href="javascript:void(0);" class="a_hr"><i class="pop_icon1"></i>个人中心</a> -->
        	<span onclick="closegeren();" class="dele_icon1 "></span>
        	<a href="javascript:exituser();"><i class="pop_icon2"></i>退出账号</a>
        </div>
    <ul class="tab-bd">
        <li class="pad_40">
        	<!-- <div class="f-head-add">
        		<span class="js-span-a">
				<img class="f-avatar js-avatar" src="http://mvavatar2.meitudata.com/55291d9c2fe93431.jpg!thumb60" width="30" height="30" alt=""></span>
				<p class="f-name box ellipsis">王某某、</p>
				<p class="f-location box ellipsis">你们有5个共同好友</p>
				<a href="javascript:void(0)" class="f-sub add_btn_vipWarp"><i class="add_btn_vip"></i>订阅</a>
        	</div> -->
        	<!--订阅-->
		    <section >
		    <!--订阅-->
		    <div id="hotsection">
		     <c:forEach var="videoh" items="${videolist}" varStatus="status" >
		     
		    <div class="f-head" id="div${videoh.id}">
					<span class="js-span-a" data-href="/user/1022971854" >
						<img class="f-avatar js-avatar" src="<c:if test="${videoh.user.pic==null}"><%=imageurl%><%=request.getContextPath()%>/img/avatar.png</c:if><c:if test="${videoh.user.pic!=null}"><%=imageurl%><%=request.getContextPath()%>/download${videoh.user.pic}</c:if>" width="30" height="30" alt="">
									</span>
									            <span id="lanv">
                <c:if test="${videoh.user.vipStat==1}">
                
                <img class="lanv1" width="13" height="13" src="<%=imageurl%><%=request.getContextPath()%>/img/lanv.png"/>
                </c:if>
                </span>
                <span id="hangv">
                <c:if test="${videoh.user.vipStat==2}">
                
                 <img class="hangv1" width="13" height="13" src="<%=imageurl%><%=request.getContextPath()%>/img/huangv.png"/>
                </c:if>
                </span>
					<p class="f-name box ellipsis">${videoh.user.name}</p>
					<p class="f-location box ellipsis"><fmt:formatDate value="${videoh.createDateStr}" pattern="MM-dd HH:mm"/></p>
					<c:if test="${videoh.attentionAuthor==1||videoh.attentionAuthor=='1'}">
					<p id="Attention${videoh.user.id}" name="Attention${videoh.user.id}" class="f-sub" onclick="addAttention(${videoh.user.id});">已订阅</p>
					</c:if>
					<c:if test="${videoh.attentionAuthor==0||videoh.attentionAuthor=='0'}">
					<p class="f-sub" id="Attention${videoh.user.id}" name="Attention${videoh.user.id}" onclick="addAttention(${videoh.user.id});">+订阅</p>
					</c:if>
					
			</div>
			<div class="body" id="bodyh${videoh.id}" onclick="playVideo('${videoh.playKey}','${videoh.nameReplace}','${videoh.videoPic}','${videoh.id}','bodyh')" >
			<img src="<%=imageurl%><%=request.getContextPath()%>/download${videoh.videoPic}" class="pic_con"/>
			</div>
			
			
			<div class="bodyh${videoh.id} body"  style='display:none'></div>
			
			
			
			<p class="f-description break">${videoh.description}</p>
			<!--分享点赞-->
			<div class="f-bottom">
			<c:if test="${videoh.praise==true||videoh.praise=='true'}">
			
				<span class="f-info-item33" id="praiseh${videoh.id}" style="color:#FE558C;" onclick="addpraise(${videoh.id});"><i class="icon-heart-empty2" id="praisehi${videoh.id}"></i><span id="praiseCountspan${videoh.id}">${videoh.praiseCount} </span></span>
			</c:if>
			<c:if test="${videoh.praise==false||videoh.praise=='false'}">
				<span class="f-info-item33" id="praiseh${videoh.id}" onclick="addpraise(${videoh.id});"><i class="icon-heart-empty"id="praisehi${videoh.id}"></i><span id="praiseCountspan${videoh.id}">${videoh.praiseCount} </span></span>
			</c:if>	
				<span  class="js-span-comment f-info-item33 box f-info-comment">
						<i class="icon-comment-text" onclick="openbanner('video',${videoh.id})"></i>${videoh.evaluationCount}</span>
				<span class="f-info-item33 f_btn" id="f_btn"  onclick="openfenxiang('${videoh.playKey}');setfenxiang('${videoh.playKey}','${videoh.user.name}','${videoh.description}','${videoh.videoPic}');"><i class="icon-heart-share"></i>分享</span>
			</div>
			
			</c:forEach>
			</div>
			
			<a href="javascript:morevideo('hot');" id="morehot" class="moreLoding">点击加载更多</a>
			<!-- <p class="footer_link"><a href="javascript:void(0);" class="link1">联系我们</a><a href="javascript:void(0);" class="link2">关于我们</a></p> -->
			<section>
		</li>
        <li class="pad_40 hide">
        	<div class="wrap_padding">
	        	<!-- <div class="btn_wrap5">
	        		<input  type="text" placeholder="请输入搜索词" autocomplete="off" class="InputSeach">
	        	</div> -->
	        	<!--轮播-->
	        	<div class="slider">
				  <ul>
				   <c:forEach var="banner" items="${banners}" varStatus="status" >
				    <li ><img onclick="openbanner('${banner.targetType}','${banner.targetId}');" src="<%=imageurl%><%=request.getContextPath()%>/download${banner.imgSrc}" alt="我拍"/>
				    
				    <c:if test="${banner.tag=='0'||banner.tag==0}">
				                    <span class="indexcorner" ><img   src="<%=imageurl%><%=request.getContextPath()%>/img/hotcorner.png"/></span>
				                    </c:if>
				    <c:if test="${banner.tag=='1'||banner.tag==1}">
				                    <span class="indexcorner" ><img  src="<%=imageurl%><%=request.getContextPath()%>/img/newcorner.png"/></span>
				                    </c:if>
				    <c:if test="${banner.tag=='2'||banner.tag==2}">
				                    <span class="indexcorner" ><img   src="<%=imageurl%><%=request.getContextPath()%>/img/activitycorner.png"/></span>
				                    </c:if>
				    </li>
				    </c:forEach>
				  </ul>
				</div>
				<!--所有-->
				<a href="javascript:openallactivity();" class="active_btn"><i class="gift_icon"></i>所有活动</a>
				<div class="div1">
				 	<div class="div2"> <a href="<%=request.getContextPath()%>/html5/activity?activityid=${activitys[0].id}" ><img style="height:200px;" src="<%=imageurl%><%=request.getContextPath()%>/download${activitys[0].cover}" /></a></div>
				    <div class="div3">
						  <a href="<%=request.getContextPath()%>/html5/activity?activityid=${activitys[1].id}" class="div3-1"><img style="height:100px;" src="<%=imageurl%><%=request.getContextPath()%>/download${activitys[1].cover}" /></a>
				        <a href="<%=request.getContextPath()%>/html5/activity?activityid=${activitys[2].id}" class="div3-2"><img style="height:100px;" src="<%=imageurl%><%=request.getContextPath()%>/download${activitys[2].cover}" /></a>
				    </div>
				</div> 
				
				
				
					
				
				
				
				<ul class="piclist">
					<li>
					<c:forEach var="activity" items="${activitys}" varStatus="status" >
					 <c:if test="${status.count%2==1&&status.count>3}">
						<div class="picimg fl">
							<a href="<%=request.getContextPath()%>/html5/activity?activityid=${activity.id}" ><img style="height:100px;" src="<%=imageurl%><%=request.getContextPath()%>/download${activity.cover}" /></a>
							<p class="f-head3">
								${activity.title}					
							</p>
						</div>
						 </c:if>
				        <c:if test="${status.count%2==0&&status.count>3}">
						<div class="picimg fr">
							<a href="<%=request.getContextPath()%>/html5/activity?activityid=${activity.id}"><img  style="height:100px;"src="<%=imageurl%><%=request.getContextPath()%>/download${activity.cover}" /></a>
							<p class="f-head3">
								${activity.title}					
							</p>
						</div>
						</c:if>
						</c:forEach>
					</li>
				</ul>
			</div>			
		</li>
        
    </ul>
</div>
<footer class="footer_fixed hide" style="display: block;">
	<!-- <span class="pr db fixed-l"><i class="fixed-icon"></i>个人中心</span> -->
    <a href="http://www.wopaitv.com/oupeng/oupppp234/" id="downloadBtn" class="js-span-a btn-bg1 btn-download">
            	<i class="icon-download"></i> 下载我拍
    </a>
 </footer>
 
 <div class="popDie popShowDel" >
    	<p class="h_30"><a href="javascript:void(0);" class="dele_icon popClose"></a></p>
		<a href="javascript:openlogin();" class="login">我拍账号登录</a>
	    <a href="javascript:openregister();" class="register">注册</a>	
	   <!--    <div class="f-hr"><hr style="border:1px solid #999;" />
	    <p class="title_top">第三方登录</p></div>
	  <div class="icon_wrap_login">
	    	<a href="javascript:void(0);"><i class="icon_wechat"></i>微信好友</a>
	    	<a href="javascript:void(0);"><i id="qqLoginBtn"></i>QQ好友</a>
	    	<a href="javascript:void(0);"><i class="icon_weibo"></i>新浪微博</a>
	    </div> -->
</div>
 	<div class="pop_wrap">
	<div class="icon_wrap_login2">
				<a href="javascript:void(0);" onclick="openshare('qzone');" title="分享到QQ空间"><i class="qq_kong"></i>QQ空间</a>
		<a href="javascript:void(0);" onclick="openshare('qqfriend');" title="分享到朋友社区"><i class="icon_qq"></i>QQ好友</a>
		<a href="javascript:void(0);" onclick="openshare('weibo');"><i class="icon_weibo"></i>新浪微博</a>
	</div>
	<!-- <div class="icon_wrap_login2 mt10">
		<a href="javascript:void(0);"><i class="icon_wechat"></i>微信好友</a>
		<a href="javascript:void(0);"><i class="icon_wechatq"></i>微信朋友圈</a>
	</div> -->
	<a href="javascript:closefenxiang();" class="login_btn2">取消</a>
</div>
<script type="text/javascript">
tabs(".tab-hd", "active", ".tab-bd");
$(".slider").yxMobileSlider({width:640,height:320,during:3000});
(function($){
	$('.popClose').on("tap",function(){
		$('.bg').css('display','none');
		$('.popDie').css('display','none');
	});
/* 	$('.font-personal').on("tap",function(){
		document.documentElement.scrollTop = document.body.scrollTop =0;
		showPop('popShowDel');
	}); */

})(Zepto)
/*(function($){
	$('.font-personal').on("tap",function(even){
		event.stopPropagation(); 
		$('.a_wrap_box').toggle(); 
		$(document).on("tap",function (event) { 
			$('.a_wrap_box').hide(); });  
	});

})(Zepto)*/
/* (function($){
	$('.font-personal').on("tap",function(even){
		
		$('.a_wrap_box').toggle();
		$(document).on("tap",function(e){
	    if(!$(e.target).hasClass("icon-personal")){
	        $(".a_wrap_box").hide();
	    }
	})


	});
	

})(Zepto) */
 </script>
 
 <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299%26show%3Dpic1' type='text/javascript'%3E%3C/script%3E"));</script>
 <script type="text/javascript">
 $(function(){
	 $("#cnzz_stat_icon_1256005299").hide();
 });
 
 </script>
</body>
</html>