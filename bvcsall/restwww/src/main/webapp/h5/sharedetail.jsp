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

	
	<title>${video.user.name}的【我拍】“${video.description}”</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/player/js/jquery.min.js"></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/js/players/jwplayer.js'></script>
	<script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/zepto.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/rlapi.js"></script>
	<script src="<%=request.getContextPath()%>/js/api.js"></script>
</head>
  <script type="text/javascript">
        var pichost = 'http://api.wopaitv.com/restwww/download';
        var videohost = 'http://cdn.wopaitv.com/hls/play';
        var loadvideohost="../video/loadconfigurl";
        var clientPf="html5";
        var shareuid='${shareuid}';
        var shareplaykey='${video.playKey}';
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
	                playVideo('${video.playKey}','${video.nameReplace}','${video.description}','${video.user.name}',
	                		'<c:if test="${video.user.pic==null}"><%=imageurl%><%=request.getContextPath()%>/img/avatar.png</c:if><c:if test="${video.user.pic!=null}"><%=imageurl%><%=request.getContextPath()%>/download${video.user.pic}</c:if>',
	                		'<fmt:formatDate value="${video.createDateStr}" pattern="MM-dd HH:mm"/>','${video.videoPic}','${video.id}','${video.user.vipStat}','${video.praiseCount}','${video.evaluationCount}');
	                setfenxiang('${video.playKey}','${video.user.name}','${video.description}','${video.videoPic}')
					}
				}
			});
            //var lhost = 'cdn.wopaitv.com';
            //var lhost = '192.168.108.160:63880';
            //var lprotocol = 'http:';

            function playVideo(id, title, desc, username, avatar, date,videoPic,videoId,vstat,praiseCount,evaluationCount){
                var videourl = videohost+"/hls/play/"+id+".m3u8";
                var poster = pichost+"/restwww/download"+videoPic;
                $("#videoId").val(videoId);
                if(id){
                    //document.title = username + ' 邀请您加入【我拍】';
                    $('.user, .body').show();
                    $('.body').empty();
                    $('.user .name').text(username);
                    $('.user .date').text(date);
                    $('.description').text(desc);
                    $('.user .avatar').attr('src', avatar);
                    if(vstat==1||vstat=='1'){
                    	$('#lanv').html("<img class='lanv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/lanv.png'/>");
                    }else if(vstat==2||vstat=='2'){
                    	$('#hangv').html("<img class='hangv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/huangv.png'/>");
                    }else{
                    	$('#lanv').html("");
                    	$('#hangv').html("");
                    }
                    $(window).scrollTop(0);
                    
                    $("#praiseCountspan").html(praiseCount);
                    $("#evaluationCountspan").html(evaluationCount);
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
                    //document.title = '您浏览的视频不存在！';
                    $('.body').hide();
                    //$('.description').text('您浏览的视频不存在！');
                }
                getevaluationList(1,1,videoId);
                ispraise();
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
            
            
            
            
            function getevaluationList(page,isrefresh,videoId){
            	var videoIds ;
            	var wopaiuid = getCookie("wopaiuid");
            	if(videoId!=null){
            		videoIds=videoId;
            	}else{
            		videoIds=$("#videoId").val();
            	}
            	var evaluationpage = $("#evaluationpage").val();
            	var evaluations=$("#evaluations").html()
            	var evaluationnum=$("#evaluationnum").val()

            	var startId;
            	if(page==null){
            		startId = parseInt(parseInt(evaluationpage-1)*10)+parseInt(evaluationnum);
            	}else{
            		if(isrefresh==1){
            			
            		startId = 0;
            		}else{
            			startId = parseInt(parseInt(evaluationpage-1)*10)+parseInt(evaluationnum);
            		}
            	}
            	var count=10;
            	var flag=1;
            	var findEvaluationList="../evaluation/findEvaluationListpage";
            	$.ajax({
        			type: "POST",
        			dataType:'json',
        			url: findEvaluationList,
        			data: {
        				videoId : videoIds,
        				flag:flag,
        				start:startId,
        				count:count
        			},
        			beforeSend: function(XMLHttpRequest) {
           		       XMLHttpRequest.setRequestHeader("uid",wopaiuid);
           			},
        			success: function(data){
        				if(data!=null&&data.result!=null){
        					var listurl=data.result;
        					var htmlevaluation="";
        					for(var i=0;i<listurl.length;i++){
        						var evaluation=listurl[i];
        						var userPic;
        						var userv="";
            					if(evaluation.user.pic!=null){
            						userPic="<%=imageurl%><%=request.getContextPath()%>/download"+evaluation.user.pic;
            					}else{
            						userPic="<%=imageurl%><%=request.getContextPath()%>/img/avatar.png";
            					}
            					
             					if(evaluation.user.vipStat==1||evaluation.user.vipStat=='1'){
                                	userv=" <span id='lanv'><img class='lanv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/lanv.png'/></span>";
                                }else if(evaluation.user.vipStat==2||evaluation.user.vipStat=='2'){
                                	userv="<span id='hangv'><img class='hangv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/huangv.png'/></span>";
                                }
            					htmlevaluation+="<li><div class='f-head'><span data-href='/user/1022971854' class='js-span-a'>"+
    								"<img width='30' height='30' alt='' src='"+userPic+"' class='f-avatar js-avatar'></span>"+userv+
    							"<p class='f-name2 box '>"+subNameString(evaluation.user.name)+"</p><p class='f-location2 box '>"+evaluation.content+"</p><p class='f-sub'>"+evaluation.formatCreateDateStr+"</p></div></li>";
        					}
        					
        					evaluations+=htmlevaluation
        					if(isrefresh==1){
        						$("#evaluations").html(htmlevaluation);
        					}else{
        						$("#evaluations").html(evaluations);
        					}
        					if(isrefresh==1){
        						$("#evaluationpage").val("1");
        						$("#evaluationnum").val("0");
        						$("#evaluationpage").val(2);
        					}else{
        						
        					$("#evaluationpage").val(parseInt(evaluationpage)+1);
        					}
        					if(listurl.length>=count){
        						
        					$("#moreLoding").html("点击加载更多");
        					}else{
        						
        					$("#moreLoding").html("没有更多了");
        					}
        				}else{
        					$("#moreLoding").html("没有更多了");
        					if(data.code!=null&&data.code=="401"){
        						  clearCookie("wopaiuid");
    							   clearCookie("wopaitoken");
    							   openlogin();
        					}
        				}
        			}
        		});
            }
            function addevaluation(){
              	 if( !checklogin()){
            		 return;
            	 }
            	var videoId = $("#videoId").val();
            	var content = $("#commentInput").val();
            	if(content==null||content.length==0){
            		alert("评价内容不能为空~");
            		return;
            	}
            	var evaluations=$("#evaluations").html();
            	var evaluationnum=$("#evaluationnum").val();
            	var addEvaluation="../evaluation/saveEvaluation";
             	var wopaiuid = getCookie("wopaiuid");
            	var access_token = getCookie("wopaitoken");
            	$.ajax({
        			type: "POST",
        			dataType:'json',
        			url: addEvaluation,
        			data: {
        				videoId : videoId,
        				content:content,
        				access_token : access_token
        			},
        			beforeSend: function(XMLHttpRequest) {
          		       XMLHttpRequest.setRequestHeader("uid",wopaiuid);
          			},
        			success: function(data){
        				if(data!=null&&data.result!=null){
        					var htmlevaluation="";
        					var evaluation=data.result;
        					var userPic;
        					var uservv="";
        					if(evaluation.user.pic!=null){
        						userPic="<%=imageurl%><%=request.getContextPath()%>/download"+evaluation.user.pic;
        					}else{
        						userPic="<%=imageurl%><%=request.getContextPath()%>/img/avatar.png";
        					}
         					if(evaluation.user.vipStat==1||evaluation.user.vipStat=='1'){
                            	uservv=" <span id='lanv'><img class='lanv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/lanv.png'/></span>";
                            }else if(evaluation.user.vipStat==2||evaluation.user.vipStat=='2'){
                            	uservv="<span id='hangv'><img class='hangv1' width='13' height='13' src='<%=imageurl%><%=request.getContextPath()%>/img/huangv.png'/></span>";
                            }
        					htmlevaluation+="<li><div class='f-head'><span data-href='/user/1022971854' class='js-span-a'>"+
							"<img width='30' height='30' alt='' src='"+userPic+"' class='f-avatar js-avatar'></span>"+uservv+
						"<p class='f-name2 box '>"+subNameString(evaluation.user.name)+"</p><p class='f-location2 box '>"+evaluation.content+"</p><p class='f-sub'>"+evaluation.formatCreateDateStr+"</p></div></li>";
							$("#evaluationnum").val(evaluationnum+1)
							evaluations=htmlevaluation+evaluations
							$("#evaluations").html(evaluations);
							$("#commentInput").val("");
        				}else{
        					if(data.code!=null&&data.code=="401"){
      						  clearCookie("wopaiuid");
  							   clearCookie("wopaitoken");
  							   openlogin();
      					}
        				}
        			}
        		});
            }
              function addpraise(){
             	 if( !checklogin()){
            		 return;
            	 }
            	var videoId = $("#videoId").val();
            	var addPraise="../praise/saveSharePraise";
            	var deletePraise="../praise/deletePraise";
            	var shareuid='${shareuid}';
            	var wopaiuid = getCookie("wopaiuid");
            	var access_token = getCookie("wopaitoken");
            	var praiseCount=parseInt($("#praiseCountspan").html());
            	
            	var color=$("#praisespan").css("color");
            	if(color=="rgb(254, 85, 140)"){
            	 	$.ajax({
            			type: "POST",
            			dataType:'json',
            			url: deletePraise,
            			data: {
            				videoId : videoId,
            				access_token : access_token,
            				shareUid:shareuid
            			},
            			beforeSend: function(XMLHttpRequest) {
              		       XMLHttpRequest.setRequestHeader("uid",wopaiuid);
              			},
            			success: function(data){
            				if(data!=null&&data.code!=null){
            					var code=data.code;
            					if(code=="200"||code==200){
            						$("#praisespan").css("color","#90a092");
                					$("#praisespani").removeClass("icon-heart-empty2");
                					$("#praisespani").addClass("icon-heart-empty");
                					$("#praiseCountspan").html(praiseCount-1);
                					
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
        				shareUid : shareuid,
        				access_token : access_token
        			},
        			beforeSend: function(XMLHttpRequest) {
           		       XMLHttpRequest.setRequestHeader("uid",wopaiuid);
           			},
        			success: function(data){
        				if(data!=null&&data.code!=null){
        					var code=data.code;
        					if(code=="200"||code==200){
            					$("#praisespan").css("color","#FE558C");
            					$("#praisespani").removeClass("icon-heart-empty");
            					$("#praisespani").addClass("icon-heart-empty2");
            					$("#praiseCountspan").html(praiseCount+1);
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
            function ispraise(){
            	var videoId = $("#videoId").val();
            	var addPraise="../praise/isPraised";
            	var shareuid='${shareuid}';
            	var wopaiuid = getCookie("wopaiuid");
            	$.ajax({
        			type: "POST",
        			dataType:'json',
        			url: addPraise,
        			data: {
        				videoId : videoId,
        				shareUid : shareuid,
        				uid:wopaiuid
        			},
            		beforeSend: function(XMLHttpRequest) {
          		       XMLHttpRequest.setRequestHeader("uid",wopaiuid);
          			},
        			success: function(data){
        				if(data!=null&&data.result!=null){
        					var result=data.result;
        					if(result==true||result=='true'){
        						
        					$("#praisespan").css("color","#FE558C");
        					$("#praisespani").removeClass("icon-heart-empty");
        					$("#praisespani").addClass("icon-heart-empty2");
        					}else{
        						$("#praisespan").css("color","#90a092");
            					$("#praisespani").removeClass("icon-heart-empty2");
            					$("#praisespani").addClass("icon-heart-empty");	
        					}
        				}else{
        					if(data.code!=null&&data.code=="401"){
      						  clearCookie("wopaiuid");
  							   clearCookie("wopaitoken");
  							   openlogin();
      					}
        				}
        			}
        		});
            }
            
            function openlogin(){
            	var nowplayid=$("#videoId").val();
            	window.location.href="<%=request.getContextPath()%>/h5/login.jsp?playKey="+shareplaykey+"&shareuid="+shareuid;
            	
            }
            function openregister(){
            	var nowplayid=$("#videoId").val();
            	window.location.href="<%=request.getContextPath()%>/h5/reg.jsp?playKey="+shareplaykey+"&shareuid="+shareuid;
            	
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
            function openfenxiang(shareplayKey,shareName,sharedescription,sharepic){
            	$(".pop_wrap").show();
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
        </script>
     
<body>
	<input type="hidden" name="shareplaykey" id="shareplaykey" value=""/>
	<input type="hidden" name="shareName" id="shareName" value=""/>
	<input type="hidden" name="sharedescription" id="sharedescription" value=""/>
	<input type="hidden" name="sharepic" id="sharepic" value=""/>
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
	<a href="javascript:void(0);" class="login_btn2">取消</a>
    </div>
<header class="header">
			<img src="../img/index.png" width="58" onclick="javascript:openindex();" class="pr db header-logo" alt="我拍">
		     <a href="http://www.wopaitv.com/oupeng/oupppp234/" id="downloadBtn" class="js-span-a btn-bg1 btn-download">
            	<i class="icon-download"></i> 下载我拍
            	</a>
    </header>
    <!--订阅-->
  <!-- <div class="login_head"> <span class="login_return" style="cursor:pointer;" onclick="javascript:history.go(-1);"></span></div>-->
    <div class="f-head">
            <div class="user">
            <c:if test="${video.user.pic==null}">
            <img src="<%=imageurl%><%=request.getContextPath()%>/img/avatar.png"
                width="30" height="30" class="avatar f-avatar"/>
                       <span id="lanv">
                <c:if test="${video.user.vipStat==1}">
                
                <img class="lanv1" width="13" height="13" src="<%=imageurl%><%=request.getContextPath()%>/img/lanv.png"/>
                </c:if>
                </span>
                <span id="hangv">
                <c:if test="${video.user.vipStat==2}">
                
                 <img class="hangv1" width="13" height="13" src="<%=imageurl%><%=request.getContextPath()%>/img/huangv.png"/>
                </c:if>
                </span>
            </c:if>
            <c:if test="${video.user.pic!=null}">
            <img src="<%=imageurl%><%=request.getContextPath()%>/download${video.user.pic}"
                width="30" height="30" class="avatar f-avatar"/>
                     <span id="lanv">
                <c:if test="${video.user.vipStat==1}">
                
                <img class="lanv1" width="13" height="13" src="<%=imageurl%><%=request.getContextPath()%>/img/lanv.png"/>
                </c:if>
                </span>
                <span id="hangv">
                <c:if test="${video.user.vipStat==2}">
                
                 <img class="hangv1" width="13" height="13" src="<%=imageurl%><%=request.getContextPath()%>/img/huangv.png"/>
                </c:if>
                </span>
            </c:if>
            <div class="info">
            <p class="f-name box ellipsis name">${video.user.name}</p>
			<p class="f-location box ellipsis date"><fmt:formatDate value="${video.createDateStr}" pattern="MM-dd HH:mm"/></p>
            </div>
        </div>
        
        </div>
      
        
        <div class="vodie_wrap">
			<div class="vodie_child">
				<div class="body"></div>
				   <p class="description">${video.description}</p>
			</div>
		</div>
        
        <!--  
			<span class="js-span-a" data-href="/user/1022971854">
				<img class="f-avatar js-avatar" src="http://mvavatar2.meitudata.com/55291d9c2fe93431.jpg!thumb60" width="30" height="30" alt="">
							</span>
			<p class="f-name box ellipsis">FlareOne</p>
			<p class="f-location box ellipsis">刚刚</p>
			<p class="f-sub">已订阅</p>
			-->
			
	<!-- </div>
	<div class="vodie_wrap">
		<div class="vodie_child"><img src="../img/2.jpg"/></div>
	</div>
		
	<p class="f-description break">呆萌小妹房顶犯二，要请她吃饭的快快点赞吧！</p>
	 -->
	 
	<!--分享点赞-->
	<!-- <div class="f-bottom">
		<span class="f-info-item"><i class="icon-heart-empty"></i>分享</span>
		<span  class="f-info-item box f-info-comment">
				<i class="icon-comment-text"></i>107</span>
		<span class="f-info-item2"><i class="icon-heart-vedio"></i>相关视频</span>
		<span class="f-info-item3 f-info-left"><i class="icon-heart-share"></i>分享</span>
	</div> -->
	<!--分享点赞2-->
	<div class="f-bottom2">
		<div class="f-bottom pr" >
			<span class="f-info-item2 pa_l" style="cursor:pointer;" id="praisespan" onclick="addpraise();"><i id="praisespani" class="icon-heart-empty"></i><span id="praiseCountspan">${video.praiseCount} </span></span>
			<ul class="tab-hd3">
	        	<li class="active tablist1 fl bl"  ><a href="javascript:void(0);"><i class="icon-heart-vedio"></i>相关视频</a></li>
	        	<li class="tablist2" ><a href="javascript:void(0);"><i class="icon-comment-text"></i><span id='evaluationCountspan'>${video.evaluationCount}</span></a></li>
	        
	        </ul>
	        <span class="f-info-item3 pa_r" style="cursor:pointer" onclick="openfenxiang();"><i class="icon-heart-share" ></i>分享</span>
        </div>
        <ul class="tab-bd3">
          	<!--2con-->
        	<li class="hide">
				<ul class="piclist">
				
				
		    <c:forEach var="one" items="${topHot}" varStatus="status" >
				   
			<c:if test="${status.count%2==1}">
			 <div class="picimg fl"
            onclick="playVideo('${one.playKey}','${one.nameReplace}','${one.description}','${one.user.name}',
                    		'<c:if test="${one.user.pic==null}"><%=imageurl%><%=request.getContextPath()%>/img/avatar.png</c:if><c:if test="${one.user.pic!=null}"><%=imageurl%><%=request.getContextPath()%>/download${one.user.pic}</c:if>',
                    		'<fmt:formatDate value="${one.createDateStr}" pattern="MM-dd HH:mm"/>','${one.videoPic}','${one.id}','${one.user.vipStat}','${one.praiseCount}','${one.evaluationCount}');setfenxiang('${one.playKey}','${one.user.name}','${one.description}','${one.videoPic}');"
            >
                <img src="<%=imageurl%><%=request.getContextPath()%>/download${one.videoPic}" width="320" class="picw100">
                <div class="f-head2">
                 	<span class="js-span-a" data-href="/user/1022971854">
		                    <c:if test="${one.user.pic==null}">
				            <img src="<%=imageurl%><%=request.getContextPath()%>/img/avatar.png" width="30" height="30" class="f-avatar js-avatar">
				            </c:if>
				            <c:if test="${one.user.pic!=null}">
				            <img src="<%=imageurl%><%=request.getContextPath()%>/download${one.user.pic}" width="30" height="30" class="f-avatar js-avatar">
				            </c:if>
				             </span>
	                    <p class="f-name2 box ellipsis">${one.user.name}</p>
	                    <p class="f-location1 box ellipsis"><fmt:formatDate value="${one.createDateStr}" pattern="MM-dd HH:mm"/></p>
                   </div>
               
            </div>
			</c:if>	   
			
			<c:if test="${status.count%2==0}">
			 <div class="picimg fr"
            onclick="playVideo('${one.playKey}','${one.nameReplace}','${one.description}','${one.user.name}',
                    		'<c:if test="${one.user.pic==null}"><%=imageurl%><%=request.getContextPath()%>/img/avatar.png</c:if><c:if test="${one.user.pic!=null}"><%=imageurl%><%=request.getContextPath()%>/download${one.user.pic}</c:if>',
                    		'<fmt:formatDate value="${one.createDateStr}" pattern="MM-dd HH:mm"/>','${one.videoPic}','${one.id}','${one.user.vipStat}','${one.praiseCount}','${one.evaluationCount }');setfenxiang('${one.playKey}','${one.user.name}','${one.description}','${one.videoPic}');"
            >
                <img src="<%=imageurl%><%=request.getContextPath()%>/download${one.videoPic}" width="320" class="picw100">
               <div class="f-head2">
                 	<span class="js-span-a" data-href="/user/1022971854">
		                    <c:if test="${one.user.pic==null}">
				            <img src="<%=imageurl%><%=request.getContextPath()%>/img/avatar.png" width="30" height="30" class="f-avatar js-avatar">
				            </c:if>
				            <c:if test="${one.user.pic!=null}">
				            <img src="<%=imageurl%><%=request.getContextPath()%>/download${one.user.pic}" width="30" height="30" class="f-avatar js-avatar">
				            </c:if>
				             </span>
	                    <p class="f-name2 box ellipsis">${one.user.name}</p>
	                    <p class="f-location1 box ellipsis"><fmt:formatDate value="${one.createDateStr}" pattern="MM-dd HH:mm"/></p>
                   </div>
                
            </div>
			</c:if>
            </c:forEach>
			</ul>
        	</li>
        	<li id="li2">
				<!--评论-->
					<div class="d-conmment-wrap box" style="padding-left:8px;">
						<!-- <a href="javascript:void(0);" class="smileface"></a> -->
						<div class="d-conmment-input-wrap">
							<input id="commentInput" name="content" type="text" class="d-conmment-input" placeholder="说点什么吧～" >
								<input type="hidden"  id="videoId" name="videoId" value="">
								<input type="hidden" id="evaluationpage" name="evaluationpage" value="1">
								<input type="hidden"  id="evaluationnum" name="evaluationnum" value="0">
								
						</div><span id="commentSend" data-id="" class="d-comment-send" onclick="addevaluation();">发送</span>
					</div>
					<ul class="commentListWrap">
					<div id="evaluations">
		            </div>
						
					</ul>
					<a class="moreLoding" id="moreLoding" href="javascript:getevaluationList(1);">点击加载更多</a>
        	</li>
      
        </ul>
	</div>
<!--  <footer class="footer_fixed hide" style="display: block;">-->
	<!-- <span class="pr db fixed-l"><i class="fixed-icon"></i>个人中心</span>
	<span class="pr db fixed-l" onclick="openindex();"><i class="fixed-icon"></i>首页</span>
    <a href="http://www.wopaitv.com" id="downloadBtn" class="js-span-a btn-bg1 btn-download">
            	<i class="icon-download"></i> 下载我拍 -->
 <!--    </a>
 </footer> -->
	


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

<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" data-appid="1103444532" data-redirecturi="http://wopai.com/restwww/video/thirdH5Video?playKey=20a1249b-b65f-4c81-910c-e9891dccd287" charset="utf-8"></script>
<script type="text/javascript">
	tabs(".tab-hd3", "active", ".tab-bd3");
	(function($){
/* 		$('.f-info-item2').on("tap",function(){
			$('.f-info-item2 i').removeClass("icon-heart-empty").addClass("icon-heart-empty2");
			$(this).css("color","#fe558c");
		});
 */

		$('.login_btn2').on("tap",function(){
			$('.pop_wrap').css('display','none');
			$('.bg').css('display','none');
		});
/* 		$('#f_btn').on("tap",function(){
				showPop('pop_wrap');
			}else{
				showPop('popDie');
			}
			
		});  */


		$('.popClose').on("tap",function(){
			$('.popDie').css('display','none');
			$('.bg').css('display','none');
		});
	/* 	$('#num').on("tap",function(){
			showPop('popDie');
		});

		
		$('#num2').on("tap",function(){
			showPop('popDie');
		});
		 */

	})(Zepto)
	</script>
	<script type="text/javascript">
	  	var evaluationVideo = ${video.id};
	    var majia = ${video.user.id};
	    loginid=getCookie('loginid');
	
	
		function saveEvaluation(){
		var content = $("#commentInput").val();
		
		if(content && content.length>0){
			$.post("evaluation/saveEvaluation",{videoId:evaluationVideo,majia:majia,content:content},function(result){
				if (result["success"]==true){
					$("#commentInput").val("");
				} else {
					showMessage("Error",result["message"]);
				}
			});
		}
	}
	
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
	
	$(function(){
	  $(".pop_wrap").hide();
	  $("#li2").hide();
	})
	
	
  /*    function checklogin(){
		 var user = getCookie("logintaken");
		  if (user != "") {
		       
		    } else {
		    	showPop('popDie');
		    }
	} */
	</script>
<script type="text/javascript">
    QC.Login({
       btnId:"qqLoginBtn"    
});
</script>

 <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299%26show%3Dpic1' type='text/javascript'%3E%3C/script%3E"));</script>
 <script type="text/javascript">
 $(function(){
	 $("#cnzz_stat_icon_1256005299").hide();
 });
 
 </script>
</body>
</html>
