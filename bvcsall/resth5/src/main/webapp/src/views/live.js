requirejs.config({
	shim:{
		'../libs/jquery.qqFace':['../libs/jquery-2.1.4']
	}
});
define(['../libs/jquery-2.1.4','head','../common/info','../common/aler','../service/service','../common/share','../common/browse'],function(jquery,head,info,alert,service,share,browse){
	var serverUrl = info.getPicPerfix();
	var interUrl = info.getRequestPrefix();
	var chaturl = $('#chaturl').val();
	var roomId = $('#roomId').val();
	var reviewId = $('#videoId').val();
	var uid = info.getCookie('uid');
	var roomCreatorId = $('#roomCreatorId').val();
	var accessToken = info.getCookie('access_token');
	var heartbeat_timer = 0;
	var last_health = -1;
	var health_timeout = 3000;
	var picHeight = $(window).height();
	$('.shareBtn').attr('data-videoid',roomId);
	var chatContent = document.getElementById('chatContent');
	var oVideo = document.getElementById('video');
	var reviewURL = $('#reviewURL').val();
	var arrObj = interUrl.split('//');
	var arrObj1 = arrObj[1].split('/');
	var commonUrl = arrObj1[0];
	var backurl = window.location.href;
	info.setCookie('backurl',encodeURIComponent(backurl),7);
	var wowCnz=_czc || [];
	wowCnz.push(["_setAccount", "1259916005"]);
	var regPlatform;
	if(info.getCookie('channel')==''){
 		regPlatform = 'h5';
	}else{
		regPlatform = 'h5-'+info.getCookie('channel');
	}
	var shareImg = $('#shareImg').val();
	var shareTitle = $('#shareTitle').val();
	var shareText = $('#shareText').val();
	var timestamp = $('#timestamp').val();
	var noncestr = $('#noncestr').val();
	var signature = $('#signature').val();
	var live = {
			init:function(){//初始化函数
				wx.config({
				    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				    appId: 'wx9c59e9a110527d17', // 必填，公众号的唯一标识
				    timestamp: timestamp, // 必填，生成签名的时间戳
				    nonceStr: noncestr, // 必填，生成签名的随机串
				    signature: signature,// 必填，签名，见附录1
				    jsApiList: [
								'checkJsApi',
								'onMenuShareTimeline',
								'onMenuShareAppMessage',
								'onMenuShareQQ',
								'onMenuShareWeibo',
								'onMenuShareQZone'
							] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
				});
				wx.ready(function(){

					 
				
				wx.onMenuShareTimeline({
				    title: shareTitle, // 分享标题
				    link: '', // 分享链接
				    imgUrl: serverUrl + shareImg, // 分享图标
				    success: function () { 
				        // 用户确认分享后执行的回调函数
				    },
				    cancel: function () { 
				        // 用户取消分享后执行的回调函数
				    } 
				});
				wx.onMenuShareAppMessage({
				    title: shareTitle, // 分享标题
				    desc: shareText, // 分享描述
				    link: '', // 分享链接
				    imgUrl: serverUrl + shareImg, // 分享图标
				    type: '', // 分享类型,music、video或link，不填默认为link
				    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
				    success: function () { 
				        // 用户确认分享后执行的回调函数
				    },
				    cancel: function () { 
				        // 用户取消分享后执行的回调函数
				    }
				});
				wx.onMenuShareQQ({
				    title: shareTitle, // 分享标题
				    desc: shareText, // 分享描述
				    link: '', // 分享链接
				    imgUrl: serverUrl + shareImg, // 分享图标
				    success: function () { 
				       // 用户确认分享后执行的回调函数
				    },
				    cancel: function () { 
				       // 用户取消分享后执行的回调函数
				    }
				});
				wx.onMenuShareWeibo({
				    title: shareTitle, // 分享标题
				    desc: shareText, // 分享描述
				    link: '', // 分享链接
				    imgUrl: serverUrl + shareImg, // 分享图标
				    success: function () { 
				       // 用户确认分享后执行的回调函数
				    },
				    cancel: function () { 
				        // 用户取消分享后执行的回调函数
				    }
				});
				wx.onMenuShareQZone({
				    title: shareTitle, // 分享标题
				    desc: shareText, // 分享描述
				    link: '', // 分享链接
				    imgUrl: serverUrl + shareImg, // 分享图标
				    success: function () { 
				       // 用户确认分享后执行的回调函数
				    },
				    cancel: function () { 
				        // 用户取消分享后执行的回调函数
				    }
				});
			});
				if(browse.versions.isWx){
					if(accessToken == ""){
						window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2fresth5%2fthirdPart%2fwechatCallback&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect%3fregPlatform='+regPlatform;
						return;
					}else{
					}
				}
				if($('#videoURL').val()=='' && $('#reviewURL').val()==''){
					$('.liveEnd').show();
					$('.playBtn').hide();
					$('.main').css('position','inherit');
					service.getRoomById(roomId).done(function(data){
						$('.maxAccessNumber').text('总观看人数：' + data.result.maxAccessNumber);
						$('.praiseNumber').text(data.result.praiseNumber);
						$('#timestamp1').text(live.timeFn(data.result.duration));
					});
				}else if($('#videoURL').val()=='' && $('#reviewURL').val()!=''){
					$('.main').css('z-index','999');
					live.reViewFn();
				}else if($('#videoURL').val()!=''){
					live.addVideo();
				}
//				var duration = $('.duration').attr('data-duration');
//				$('.duration span').text(live.timeFn(duration));
				$('.videoBox').css('height',$(window).height());
				info.personalPortrait(serverUrl,$('.userPortrait-1'));
				info.personalPortrait(serverUrl,$('.userPortrait'));
				var oHeight = $('#chatContent ul').height();
//				if(reviewURL == ''){
//					live.websorket();
//				}else{
//					live.reViewFn();
//				}
				$(window).scroll(function(){
					info.lazyPicLoad($('.videoListBox li a img'));
					info.lazyPicLoad($('.hotUesrPic'));
				});
				if(browse.versions.isWx){
					live.attenFn();
				}else{
					live.showBox();
				}
				$('.joinBtn').on('click',function(){
					wowCnz.push(["_trackEvent","下载","我玩统计直播下载Top","","",".joinBtn"]);
				});
				$('.bottomDownload').on('click',function(){
					wowCnz.push(["_trackEvent","下载","我玩统计直播下载Bottom","","",".bottomDownload"]);
				});
				$('.attenBtn').on('click',function(){
					wowCnz.push(["_trackEvent","关注","我玩统计直播关注","关注埋点","",".attenBtn"]);
				});	
				//获取直播列表
				live.roomList();
//				live.showBox();                           
				//添加跳转连接
//				$('.canPlay,.commit em').on('click',function(){
//					live.jump($(this));
//				});
				//分享
//				$('.shareBtn').click(function(){
//					live.shareFn();
//				});
//				live.hideShowFun($('.hide'));
				//获取用户列表
//				var size=10;
//				var page=1;
//				live.getRoomUser(page,size,roomId);
//				$('.open-app-btn a').click(function(){
//					wowCnz.push(["_trackEvent","下载","我玩统计直播下载","","",".open-app-btn a"]);
//					live.jump($(this));
//				});
//				live.jumpNew();
				
				live.jumpDownload();
//				var oVideo = document.getElementsByTagName('video')[0];
//				var oPlay = document.getElementById('playBtn');
//				oVideo.addEventListener('playing',function(){
//					var vHeight = $('video').height();
//					var screenHeight = $(window).height();
//					if(vHeight<screenHeight){
//						$(this).css('margin-top',(screenHeight - vHeight)/2);
//					}
//					$('.userPortrait-1').hide();
//					$('.loading').hide();
//					oPlay.style.display = 'none';
//				})
			},
			addVideo:function(){
				if(browse.versions.ios){
					if(($(window).width()==320 && !browse.versions.isQQ)){
						$('.loading').hide();
						var oPlay = document.getElementById('playBtn');
						var videoBox = document.getElementById('videoBox');
						oPlay.style.display = 'block';
						var sVideo = '<video id="video" class="video-js vjs-default-skin" autoplay="autoplay" x-webkit-playsinline webkit-playsinline="controls"><source src="'+$('#videoURL').val()+'" type="application/x-mpegURL"></video>';
//						var sHtml = '<img id="poster" height="'+$(window).height()+'" src="' + serverUrl + $('#posterURL').val() + '">';
						//videoBox.innerHTML = sHtml;
						$('.videoBox').append(sVideo);
//						$('#poster')[0].onload = function(){
//							live.imgLoadCompleted();
//						}
						var oVideo = document.getElementById('video');
						oVideo.addEventListener('playing',function(){
							var vHeight = $('video').height();
							var screenHeight = $(window).height();
							if(vHeight<screenHeight){
								$(this).css('margin-top',(screenHeight - vHeight)/2);
							}
						})
						
						oPlay.addEventListener('click',function(){
							
							
//							makeVideoPlayableInline(oVideo);
//							oVideo.style.display = 'block';
							oVideo.play();
							
							
						})
						oVideo.addEventListener('playing',function(){
							$('.userPortrait-1').hide();
							$('.loading').hide();
							oPlay.style.display = 'none';
						})
						oVideo.addEventListener('webkitendfullscreen',function(){
//							oVideo.style.display = 'none';
							oVideo.pause();
							$('.main').show();
							$('.userPortrait-1').show();
							oPlay.style.display = 'block';
						},false);
					}else{
						if(browse.versions.isSafari){
							$('.loading').hide();
						}else if(!browse.versions.isWeiBo){
							$('.loading').show();
						}else{
							$('.loading').hide();
						}
						
						var sVideo = '<video id="video" class="video-js vjs-default-skin" autoplay="autoplay" x-webkit-playsinline webkit-playsinline="controls" width="100%"><source src="'+$('#videoURL').val()+'" type="application/x-mpegURL"></video>';
						$('.videoBox').append(sVideo);
						var oVideo = document.getElementById('video');
						oVideo.addEventListener('play',function(){
							$('.userPortrait-1').hide();
							$('.loading').hide();
							$('#playBtn').hide();
						})
						oVideo.addEventListener('playing',function(){
							$('.userPortrait-1').hide();
							$('.loading').hide();
							$('#playBtn').hide();
							var vHeight = $('video').height();
							var screenHeight = $(window).height();
							if(vHeight<screenHeight){
								$(this).css('margin-top',(screenHeight - vHeight)/2);
							}
						})
						oVideo.addEventListener('seeking',function(){
							//$('.userPortrait-1').hide();
							
						})
						oVideo.addEventListener('seeked',function(){
							//$('.userPortrait-1').hide();
							
						})
						oVideo.addEventListener('stalled',function(){
							//alert.alertMsg('dddd');
						})
						oVideo.addEventListener('canplaythrough',function(){
//							alert.alertMsg('eeee');
						})
						oVideo.addEventListener('ratechange',function(){
							//alert.alertMsg('fff');
						})
						oVideo.addEventListener('durationchange',function(){
							//alert.alertMsg('ggg');
						})
						if(browse.versions.isWeiBo || browse.versions.webApp){
							$('.playBtn').show();
							live.playFun();
							
						}else{
							$('.playBtn').hide();
						}
					}
					
				}else if(browse.versions.android){
					$('.loading').hide();
					var sVideo = '<video id="video" class="" autoplay="autoplay" width="100%" preload="auto" x-webkit-playsinline webkit-playsinline="controls"><source src="'+$('#videoURL').val()+'" type="application/x-mpegURL"></video>'
					$('.videoBox').append(sVideo);
//					makeVideoPlayableInline(oVideo);
					$('#video').css({
						'position':'absolute',
						'top':'0',
						'z-index':'-999'
					});
					var myPlayer = videojs('video');
				    videojs("video").ready(function(){
				        var myPlayer = this;
				        myPlayer.play();
				        myPlayer.cancelFullScreen();
				        if(myPlayer.ended){
							$('#video_html5_api').hide();
						}
				    });
				    var picHeight = $(window).height();
				    $('#video').css('min-height','400px');
				    
				    $('#video_html5_api').siblings().hide();	
				    $('#video_html5_api').attr('poster',serverUrl+$('#posterURL').val());
					$('#video_html5_api').css('width','100%');
					$('#video_html5_api').hide();
					$('.playBtn').show();
					$('.playBtn').one('click',function(){
						$('#video_html5_api').show();
						$('#video').css('z-index','-9999');
						myPlayer.play();
						$('.userPortrait-1').hide();
						$('.playBtn').hide();
						$('#video img').hide();
					});
				}
			},
			imgLoadCompleted:function(flag){
				if(flag){
					$('#video').css({
			    		'height':picHeight,
			    		'overflow':'hidden'
			    	})
					$('#poster').css({
						'position':'absolute',
						'left':'50%',
						'margin-left': - $('#poster').width()/2,
						'max-width':'inherit'
					});
				}else{
					$('#poster').css({
						'position':'absolute',
						'left':'50%',
						'margin-left': - $('#poster').width()/2,
						'max-width':'inherit'
					});
				}
				
			},
			playFun:function(){
				var oPlay = document.getElementById('playBtn');
				var oVideo = document.getElementById('video');
				oPlay.addEventListener('click',function(){
					oVideo.play();
					oPlay.style.display = 'none';
				},false);
				oVideo.addEventListener('webkitendfullscreen',function(){
					oVideo.pause();
					oPlay.style.display = 'block';
				},false);
				if(oVideo.played){
					
				}else{
					oPlay.style.display = 'block';
				}
				if(oVideo.paused){
					oPlay.style.display = 'block';
				}
			},
			showBox:function(){
				$('.attenBtn').on('click',function(){
					wowCnz.push(["_trackEvent","关注","我玩统计直播关注","关注埋点","",".attenBtn"]);
					var shtml = '<p>下载LIVE关注ta</p><p>ta的直播会在第一时间通知你哦</p>';
					$('.words').append(shtml);
					$('.words p').css('color','#9fbc16');
					$('.showBox').show();
					$('.bgBox').show();
					$('.showBox img.imgBox').attr('src','/resth5/img/personalBg/attentionBox.png');
					$('.closeBtn').on('click',function(){
						$('.showBox').hide();
						$('.bgBox').hide();
						$('.words p').remove();
						$('.showBox img.imgBox').attr('src','');
					})
				});
			},
			websorket:function(){//连接websorket
					
					var wschat = null;
					 if('WebSocket' in window){
							try{
								wschat = new WebSocket(chaturl+"?roomId="+roomId);
								
							}catch(e){
								console.log("聊天系统未连接:"+chaturl);
							}
							}else if ('MozWebSocket' in window){
								wschat = new MozWebSocket(chaturl+"?roomId="+roomId);
							}else{
								console.log("not support");
							}   
					 		
							wschat.onmessage = function(evt) {
								var data = JSON.parse(evt.data);
								var oVideo = document.getElementById('video');
//								console.log(data);
								if(data.childCode == '1001'){
									var oLi = '<li class="face"><span  class="color'+Math.floor(Math.random()*2+1)+'">'+data.senderName+':</span>'+data.content+'</li>';
									$('#chatContent ul').append(oLi);
									info.faceFn($('.face'));
									live.swiperFlag('true');
								}else if(data.childCode == '0006'){
									live.addVideo();
									$('#video,.funPerson,.audienceImg,#chatContent,.openApp').show();
									var oLi = '<li>'+data.content+'</li>';
									$('#chatContent ul').append(oLi);
									live.swiperFlag('true');
									

									$('.liveEnd').hide();
								}else if(data.childCode == '2002'){
									$('#video,.funPerson,.audienceImg,.playBtn,#chatContent,.openApp').hide();
									$('.liveEnd').show();
									$('#video').remove();
										$('.bottomDownload').show();
									service.getRoomById(roomId).done(function(data){
										$('.maxAccessNumber').text('总观看人数：' + data.result.maxAccessNumber+'人');
//										$('.praiseNumber').text(data.result.praiseNumber);
//										$('#timestamp').text(live.timeFn(data.result.duration));
//										console.log(data.result.duration);
									});
								}else if(data.childCode == '0001'){
									var oLi = '<li>'+data.extra.name+'来了</li>';
									//var oLi1 = '<li><img src='+serverUrl+data.extra.pic+' onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'"></li>';
									$('#chatContent ul').append(oLi);
									//$('.audienceImg ul').prepend(oLi1);
									live.swiperFlag('true');
									
									
									//$('.audienceImg ul').width(($('.audienceImg').children().children('li').width()+10)*$('.audienceImg').children().children('li').length);
								}else if(data.childCode == '0002'){
									$('.onlineCount').text(data.extra.onlineNumber);
									$('.allCount').text(data.extra.maxAccessNumber);
								}else if(data.childCode == '0009' || data.childCode =='2005'){
									$('#video,.funPerson,.audienceImg,.playBtn,#chatContent,.openApp').hide();
									$('#video').remove();
									$('.loading').hide();
										$('.liveEnd').show();
										$('.bottomDownload').show();
									service.getRoomById(roomId).done(function(data){
										$('.maxAccessNumber').text('总观看人数：' + data.result.maxAccessNumber+'人');
										$('.praiseNumber').text(data.result.praiseNumber);
//										$('#timestamp').text(live.timeFn(data.result.duration));
									});
								}else if(data.childCode == '2001'){
									$('#video,.funPerson,.audienceImg,.playBtn,#chatContent,.openApp').show();
									$('.liveEnd').hide();
								}else if(data.childCode == '0005'){
									$('.audienceImg ul li img').each(function(){
										if($(this).attr('src') == serverUrl + data.extra.pic){
											$(this).parent().remove();
										}
									});
								}else if(data.childCode == '2003'){
									var oLi = $('<li>'+data.content+'</li>');
									$('#chatContent ul').append(oLi);
								}else if(data.childCode == '2004'){
									if(browse.versions.ios){
										$('#video').attr('src',$('#videoURL').val());
									}else if(browse.versions.android){
										$('#video').remove();
										var sVideo = '<video id="video" class="" autoplay="autoplay" width="100%" preload="auto"><source src="'+$('#videoURL').val()+'" type="application/x-mpegURL"></video>'
										$('.videoBox').append(sVideo);
										
										var myPlayer = videojs('video');
									    videojs("video").ready(function(){
									        var myPlayer = this;
									        myPlayer.play();
									        myPlayer.cancelFullScreen();
									        if(myPlayer.ended){
												$('#video_html5_api').hide();
											}
									    });
									   
									    $('#video_html5_api').hide();
									    $('#video').css('min-height','400px');
									    $('#video').append('<img width="100%" height="'+picHeight+'" src="' + serverUrl + $('#posterURL').val() + '" >');
									    $('#video_html5_api').attr('poster',serverUrl+$('#posterURL').val());
										$('#video_html5_api').css('width','100%');
										//$('#video_html5_api').hide();
										$('.playBtn').show();
										$('.playBtn').on('click',function(){
											$('#video_html5_api').show();
											$('#video').css('z-index','-9999');
											myPlayer.play();
											$('.playBtn').hide();
											$('#video img').hide();
										});
									}
								}

//								var oHeight = $('#chatContent ul').height();
//								var chatContent = document.getElementById('chatContent'),
//								oUl1 = chatContent.getElementsByTagName('ul')[0],
//							       startX, startY;
//								
//							    // touch start listener
//							    function touchStart(event) {
//							    	live.swiperFlag();
//							    }
//							    chatContent.addEventListener('touchstart', touchStart, false);
//								function touchEnd(event) {
//									if(data.childCode == '1001' || data.childCode == '0006' || data.childCode == '0001'){
//									}
//								}
							 
//							chatContent.addEventListener('touchend', touchEnd, false);
								//调用时间
//								var duration = $('.duration').attr('data-duration');
//								$('.duration span').text(live.timeFn(duration));
								
							};

							wschat.onclose = function(evt) {
								
								console.log("聊天服务器已关闭，请重新登陆");
							};   
							wschat.onopen = function(evt) {   
								heartbeat_timer = setInterval( function(){live.keepalive(wschat)}, 30000 );
							};
							wschat.onerror = function(evt) { 
					            //alert.alertMsg(evt); 
					        };
				
			},
			swiperFlag:function(flag){
				var oHeight = $('#chatContent ul').height();
				if(flag=='true'){
					$('#chatContent').scrollTop(oHeight);
				}else{
					
				}
			},
			keepalive:function(ws){
				var time = new Date();
				if( last_health != -1 && ( time.getTime() - last_health > health_timeout ) ){
						//此时即可以认为连接断开，可是设置重连或者关闭
						console.log("服务器没有响应.");
						//ws.close();
				}
				else{
					console.log("连接正常.");
					if( ws.bufferedAmount == 0 ){
						ws.send(JSON.stringify({"childCode":"0004","code":"000","roomId":roomId}));
					}
				}
			},
			roomList:function(){
				var page = 1,size = 18,type = 1,target = $('.videoListBox ul');
				service.getLiveAndRecordList(page,size,type).done(function(json){
					live.makeLiveList(json.result,target);
				});
			},
			makeLiveList:function(data,target){
				if(data.liveList == '' && data.backList ==''){
					$('.hotRecommend,.reBotton').hide();
					$('.liveEnd').css('height',$(window).height()-99);
				}else{
					var liveList = data.liveList;
					$.each(liveList,function(m,n){
						var vStart;
						if(n.anchorVstat == 1){
							vStart= '<em class="userV1"></em>';

						}else if(n.anchorVstat == 2){
							vStart= '<em class="userV2"></em>';
						}else if(n.anchorVstat == 3){
							vStart= '<em class="userV3"></em>';
						}else{
							vStart= '';
						};
						var liveName;
						if(!n.title){
							liveName = n.anchorName;
						}else{
							liveName = n.title;
						}
						if(roomId == n.id){
							return;
						}else{
							if(n.onlineNumber>10000){
								var allCount = (n.onlineNumber/10000).toFixed(1)+'万人';
							}else{
								var allCount = n.onlineNumber+'人';
							}
							var tempList= '<li>'+
							'<a href="javascript:void(0)"><img data-userPic="'+ n.roomPic +'" data-roomId="'+ n.id +
							'"src="'+interUrl+'/img/personalBg/personalBg.jpg" class="hotPic"></a>'+
							'<span class="nickName" style="color:#999999;">'+n.anchorName+'</span>'+
							'<span style="float:right;color:#f0be9f;">'+allCount+'</span>'+
							'</li>';
							target.append(tempList);
						}
						
					});
					if(liveList.length<18){
						var backList = data.backList;
						$.each(backList,function(m,n){
							var vStart;
							if(n.anchorVstat == 1){
								vStart= '<em class="userV1"></em>';
							}else if(n.anchorVstat == 2){
								vStart= '<em class="userV2"></em>';
							}else if(n.anchorVstat == 3){
								vStart= '<em class="userV3"></em>';
							}else{
								vStart= '';
							};
							var liveName;
							if(!n.title){
								liveName = n.anchorName;
							}else{
								liveName = n.title;
							}
							if(roomId == n.liveNoticeId){
								return;
							}else{
								var tempList= '<li>'+
								'<a href="javascript:void(0)"><img data-userPic="'+ n.videoPic +'" data-roomId="'+ n.liveNoticeId +'"src="" class="hotPic"></a>'+'</li>';
								target.append(tempList);
							}
						})
					}
					var imgWidth = ($('body').width()*0.98/2)*0.985;
					console.log(imgWidth);
					$('.hotRecommend .videoListBox ul li a img').css('height',imgWidth);
					$('.hotRecommend,.bottomDownload').show();
					info.personalPortrait(serverUrl,$('.hotPic'));
					info.personalPortrait(serverUrl,$('.hotUesrPic'));
					$('.videoListBox ul li a').on('click',function(){
						window.location.href = interUrl + '/live/shareLive?roomId=' + $(this).find('img').attr('data-roomId');
					});
				}
			},
			shareFn: function(){
				var tit = $('.shareBtn').attr('data-name');
				var desc = $('.shareBtn').attr('data-desc');
				var pic = serverUrl+$('.shareBtn').attr('data-videopic');//缩略图
				var videoId = $('#roomId').val();
				var userId = $('.shareBtn').attr('data-userId');
				var isLive=1;
				
				share.init(tit,desc,pic,videoId,userId,isLive);
			},
			jump:function(obj){
				var roomid = $('#roomId').val();
				
					if (browse.versions.ios) {
						if(browse.versions.isWx || browse.versions.isWeiBo){
							$('.skyBox').show();
						}else if($('#reviewURL').val() != ''){
//								window.location.href = "wopai://reviewId="+reviewId;
								obj.attr("href","wopai://reviewId="+reviewId);
//							timer();
						}
						else {
//							obj.attr("href","wopai://roomid="+roomid);
							window.location.href = "wopai://roomid="+roomid;
//							timer();
						}
					} else if (browse.versions.android){
						if (browse.versions.isWx || browse.versions.isWeiBo){
							$('.skyBox-anz').show();
						}else if($('#reviewURL').val() != ''){
//							obj.attr("href","com.busap.myvideo://main/openwith?reviewId="+reviewId+"&type=12");
							window.location.href = "com.busap.myvideo://main/openwith?reviewId="+reviewId+"&type=12";
//							timer();
						} else {
//							obj.attr("href","com.busap.myvideo://main/openwith?roomId="+roomId+"&type=11");
							window.location.href = "com.busap.myvideo://main/openwith?roomId="+roomId+"&type=11";
//							timer();
						}
					} else {
						window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
					};

					function timer(){
						setTimeout(function(){
							window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
						},3000)
					}
				$('.skyBox,.skyBox-anz').click(function(){
					$(this).hide();
				});
			},
			jumpNew:function(obj){  
				var roomid = $('#roomId').val();
				
					if (browse.versions.ios) {
						if($('#reviewURL').val() != ''){
								window.location.href = "wopai://reviewId="+reviewId;
//								obj.attr("href","wopai://reviewId="+reviewId);
//							timer();
						}
						else {
//							obj.attr("href","wopai://roomid="+roomid);
							window.location.href = "wopai://roomid="+roomid;
//							timer();
						}
					} else if (browse.versions.android){
						if($('#reviewURL').val() != ''){
//							obj.attr("href","com.busap.myvideo://main/openwith?reviewId="+reviewId+"&type=12");
							window.location.href = "com.busap.myvideo://main/openwith?reviewId="+reviewId+"&type=12";
//							timer();
						} else {
//							obj.attr("href","com.busap.myvideo://main/openwith?roomId="+roomId+"&type=11");
							window.location.href = "com.busap.myvideo://main/openwith?roomId="+roomId+"&type=11"
//							timer();
						}
					} else {
						window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
					};

					function timer(){
						setTimeout(function(){
							window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
						},3000)
					}
			},
			reViewFn:function(){
//				$('.funPerson,.openApp').show();
				var reviewVideoURL = $("#reviewVideoURL").val();
				var reviewVideoDuration = $('#reviewVideoDuration').val();
				$('#videoDuration').text('观看人数：'+reviewVideoDuration+'人');
				$('.playBtn').show();
					if(browse.versions.ios){
						
						var sVideo = '<video id="video1" class="video-js vjs-default-skin" autoplay="autoplay" x-webkit-playsinline webkit-playsinline><source src="'+reviewURL+'" type="application/x-mpegURL"></video>';
						var sHtml = '<img id="poster" height="'+$(window).height()+'" src="' + serverUrl + $('#posterURL').val() + '">';
						var oPlay = document.getElementById('playBtn');
						var videoBox = document.getElementById('videoBox');
						oPlay.style.display = 'block';
//						videoBox.innerHTML = sHtml;
//						$('#poster')[0].onload = function(){
//							live.imgLoadCompleted();
//						}
						
						oPlay.addEventListener('click',function(){
							$('.loading').show();
							$('.videoBox').append(sVideo);
							$('#video1').css('width',$(window).width());
							var oVideo = document.getElementById('video1');
							$('#poster').remove();
							oPlay.style.display = 'none';
							oVideo.style.display = 'block';
							oVideo.play();
							
							oVideo.addEventListener('webkitendfullscreen',function(){
								videoBox.innerHTML = sHtml;
								oVideo.style.display = 'none';
								$('.main').show();
								oPlay.style.display = 'block';
							},false);
							oVideo.addEventListener('playing',function(){
								$('.userPortrait-1').hide();
								$('.loading').hide();
								$('#playBtn').hide();
								var vHeight = $('video').height();
								var screenHeight = $(window).height();
								if(vHeight<screenHeight){
									$(this).css('margin-top',(screenHeight - vHeight)/2);
								}
							})
						})
					}else if(browse.versions.android){
						
					   
					    var picHeight = $(window).height();
					    $('#video1').css('min-height','400px');
					    $('.videoBox').append('<img id="poster" height="'+picHeight+'" src="' + serverUrl + $('#posterURL').val() +'">');
					    
					    	
					    $('#video1_html5_api').attr('poster',serverUrl+$('#posterURL').val());
						$('#video1_html5_api').css('width','100%');
						$('#video1_html5_api').hide();
						$('.playBtn').show();
						$('.playBtn').on('click',function(){
							var sVideo = '<video id="video1" class="" autoplay="autoplay" width="100%" preload="auto"><source src="'+reviewURL+'" type="application/x-mpegURL"></video>'
							$('.videoBox').append(sVideo);
							$('#video1').css({
								'position':'absolute',
								'top':'0',
								'z-index':'-999'
							});
							$('#video1_html5_api').show().css('width','100%');
							$('#video').css('z-index','-9999');
							
							$('.playBtn').hide();
							$('#poster').hide();
							var myPlayer = videojs('video1');
						    videojs("video1").ready(function(){
						        var myPlayer = this;
						        myPlayer.play();
						        myPlayer.cancelFullScreen();
						        if(myPlayer.ended){
									$('#video_html5_api').hide();
								}
						    });
						    $('#video1_html5_api').siblings().remove();
						});
						document.getElementById('poster').onload = function(){
							var flag = 1;
					    	live.imgLoadCompleted(flag);
						}

						var img1 = document.getElementById('poster');
					}
					
			},
			attenFn:function(){
				var otherUid = roomCreatorId;
				service.isAttention(otherUid).done(function(json){
					var isAttention = json.result;
					if(isAttention == '0'){
						var attentionId = roomCreatorId;
						var dataFrom = 'h5';
						$('.atten em').click(function(){
							service.addAttention(attentionId,dataFrom,isAttention).done(function(json){
								if(json.result == 1){
									$('.atten em').remove();
									alert.alertMsg('关注成功！');
								}
							});
						});
					}else{
						$('.atten em').remove();
					}
				}).fail(function(error){
					if(error.code == '401' || error.code == '400_5' || error.code == '503'){
						info.clearCookie('uid');
						info.clearCookie('access_token');
						window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2fresth5%2fthirdPart%2fwechatCallback&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect%3fregPlatform='+regPlatform;
					}
				});

			},
			timeFn:function(timestamp){
				if(timestamp/3600000>1){
					if(parseInt(timestamp/3600000)>10){
						var hour = parseInt(timestamp/3600000);
					}else{
						var hour = '0'+parseInt(timestamp/3600000);
					}
					if(timestamp%3600000/60000>1){
						if(parseInt(timestamp%3600000/60000)>10){
							var min = parseInt(timestamp%3600000/60000);
						}else{
							var min = '0'+parseInt(timestamp%3600000/60000);
						}
						
						if(timestamp%3600000%60000>1){
							if(parseInt(timestamp%3600000%60000/1000)>10){
								var sec = parseInt(timestamp%3600000%60000/1000);
							}else{
								var sec = '0'+parseInt(timestamp%3600000%60000/1000);
							}
							
						}
					}else{
						var hour = '00';
						if(timestamp/60000>1){
							if(parseInt(timestamp%60000)>10){
								var min = parseInt(timestamp%60000);
							}else{
								var min = '0'+parseInt(timestamp%60000);
							}
							
							if(timestamp%60000/1000>1){
								if(parseInt(timestamp%60000%1000)>10){
									var sec = parseInt(timestamp%60000%1000);
								}else{
									var sec = '0'+parseInt(timestamp%60000%1000);
								}
								
							}
						}else{
							var min = '00';
							if(timestamp/1000>10){
								var sec = timestamp/1000;
							}else{
								var sec = '0'+timestamp/1000;
							}
							
						}
					}
				}else{
					var hour = '00';
					if(timestamp/60000>1){
						if(parseInt(timestamp/60000)>10){
							var min = parseInt(timestamp/60000);
						}else{
							var min = '0'+parseInt(timestamp/60000);
						}
						
						if(timestamp%60000/1000>1){
							if(timestamp%60000/1000>10){
								var sec = parseInt(timestamp%60000/1000);
							}else{
								var sec = '0'+parseInt(timestamp%60000/1000);
							}
							
						}
					}else{
						var min = '00';
						if(timestamp/1000>10){
							var sec = parseInt(timestamp/1000);
						}else{
							var sec = '0'+parseInt(timestamp/1000);
						}
					}
				}
				return hour+':'+min+':'+sec;
			},
			getRoomUserList:function(data){
				var target = $('.audienceImg ul');
				$.each(data,function(m,n){
					var sHtml = $('<li><img src="'+ serverUrl + n.pic +'"></li>');
					target.prepend(sHtml);
					$('.audienceImg ul').width(($('.audienceImg').children().children('li').width()+10)*$('.audienceImg').children().children('li').length);
				});
			},
			getRoomUser:function(page,size,roomId){
				
				service.getRoomUser(page,size,roomId).done(function(json){
					live.getRoomUserList(json.result);
				});
			},
			jumpDownload:function(){
				 $('body').on('touchstart','.bottomDownload,.downloadBtn,.joinBtn',function(e){

					 e.preventDefault();
					 if(browse.versions.ios)
				        {
				        	if(browse.versions.isWeiBo){
				        		$('.skyBox').show();
					        }else{
					        	window.location.href = 'https://itunes.apple.com/us/app/wo-pai-wo-jiu-shi-zhu-jiao/id1077305226?ls=1&mt=8';
					        }
				        }
				        if(browse.versions.android)
				        {
				        	if(browse.versions.isWeiBo){
				        		$('.skyBox-anz').show();
					        }else{
					        	window.location.href = info.appDownUrl;
					        }
				        }
				        if(browse.versions.isWx)
				        {
				            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
				        }
				       
				    })
				    $('.skyBox,.skyBox-anz').click(function(){
				    	$(this).hide();
				    });
				   $('.bottomDownload .openwopai').click(function(){
				        
				        if(!browse.versions.ios && !browse.versions.android)
				        {
				            window.location.href = info.appDownUrl;
				        }
				   });
				   
			}
	}
	
	live.init();
});
