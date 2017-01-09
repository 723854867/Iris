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
	var uid = info.getCookie('uid');
	var heartbeat_timer = 0;
	var last_health = -1;
	var health_timeout = 3000;
	var picHeight = $(window).height();
	$('.shareBtn').attr('data-videoid',roomId);
	var chatContent = document.getElementById('chatContent');
	var oVideo = document.getElementById('video');
	var reviewURL = $('#reviewURL').val();
	var live = {
			init:function(){//初始化函数
				$('.videoBox').css('height',$(window).height());
				var oHeight = $('#chatContent ul').height();
//				$(function(){
					live.websorket();
//				})
				
				$(window).scroll(function(){
					info.lazyPicLoad($('.videoListBox li a img'));
					info.lazyPicLoad($('.hotUesrPic'));
				});
				$('.openApp').css('margin-top',$(window).height()-40);
				//live.reViewFn();
				info.personalPortrait(serverUrl,$('.userPortrait-1'));
//				info.videoInfo(serverUrl,$('.videoPic'));
				//获取直播列表
				live.roomList();
				live.showBox();
				//添加跳转连接
				$('.canPlay,.commit em').on('click',function(){
					live.jump($(this));
				});
				//分享
				$('.shareBtn').click(function(){
					live.shareFn();
				});
				live.hideShowFun($('.hide'));
				//获取用户列表
				var size=10;
				var page=1;
				live.getRoomUser(page,size,roomId);
				live.downloadApp();
			},
			addVideo:function(){
				if(browse.versions.ios){
					if(($(window).width()==320 && !browse.versions.isQQ) || browse.versions.isWeiBo || browse.versions.webApp){
						var oPlay = document.getElementById('playBtn');
						var videoBox = document.getElementById('videoBox');
						oPlay.style.display = 'block';
						var sVideo = '<video id="video" class="video-js vjs-default-skin" autoplay="autoplay" x-webkit-playsinline webkit-playsinline><source src="'+$('#videoURL').val()+'" type="application/x-mpegURL"></video>';
						var sHtml = '<img id="poster" height="'+$(window).height()+'" src="' + serverUrl + $('#posterURL').val() + '">';
						videoBox.innerHTML = sHtml;
						$('#poster')[0].onload = function(){
							live.imgLoadCompleted();
						}
						
						oPlay.addEventListener('click',function(){
							$('.videoBox').append(sVideo);
							var oVideo = document.getElementById('video');
							
							oVideo.style.display = 'block';
							oVideo.play();
							
							oVideo.addEventListener('webkitendfullscreen',function(){
								oVideo.style.display = 'none';
								$('.main').show();
								oPlay.style.display = 'block';
							},false);
						})
					}else{
						var sVideo = '<video id="video" class="video-js vjs-default-skin" autoplay="autoplay" x-webkit-playsinline webkit-playsinline width="100%"><source src="'+$('#videoURL').val()+'" type="application/x-mpegURL"></video>';
						$('.videoBox').append(sVideo);
						
						if(browse.versions.isWeiBo || browse.versions.webApp){
							$('.playBtn').show();
							live.playFun();
							
						}else{
							$('.playBtn').hide();
						}
					}
					
				}else if(browse.versions.android){
					var sVideo = '<video id="video" class="" autoplay="autoplay" width="100%" preload="auto"><source src="'+$('#videoURL').val()+'" type="application/x-mpegURL"></video>'
					$('.videoBox').append(sVideo);
					
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
				    $('#videoBox').append('<img id="poster" height="'+picHeight+'" src="' + serverUrl + $('#posterURL').val() +'">');
				    
				    $('#video_html5_api').siblings().hide();	
				    $('#video_html5_api').attr('poster',serverUrl+$('#posterURL').val());
					$('#video_html5_api').css('width','100%');
					$('#video_html5_api').hide();
					$('.playBtn').show();
					$('.playBtn').on('click',function(){
						$('#video_html5_api').show();
						$('#video').css('z-index','-9999');
						myPlayer.play();
						$('.playBtn').hide();
						$('#video img').hide();
					});
					document.getElementById('poster').onload = function(){
						var flag = 1;
				    	live.imgLoadCompleted(flag);
					}

					var img1 = document.getElementById('poster');
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
					$('.showBox').show();
					$('.bgBox').show();
					$('.showBox img.imgBox').attr('src','../../img/personalBg/attentionBox.png');
					$('.closeBtn').on('click',function(){
						$('.showBox').hide();
						$('.bgBox').hide();
						$('.showBox img.imgBox').attr('src','');
					})
				});
				$('.message').on('click',function(){
					$('.showBox').show();
					$('.bgBox').show();
					$('.showBox img.imgBox').attr('src','../../img/personalBg/commentBox.png');
					$('.closeBtn').on('click',function(){
						$('.showBox').hide();
						$('.bgBox').hide();
						$('.showBox img.imgBox').attr('src','');
					})
				});
				$('.gift').on('click',function(){
					$('.showBox').show();
					$('.bgBox').show();
					$('.showBox img.imgBox').attr('src','../../img/personalBg/giftBox.png');
					$('.closeBtn').on('click',function(){
						$('.showBox').hide();
						$('.bgBox').hide();
						$('.showBox img.imgBox').attr('src','');
					})
				})
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
								console.log(data);
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
									if(reviewURL != ''){
										$('.liveEnd').hide();
										live.reViewFn();
										$('.gift,.message').remove();
									}else{
										$('.liveEnd').show();
										$('.bottomDownload').show();
									}
									service.getRoomById(roomId).done(function(data){
										$('.maxAccessNumber').text(data.result.maxAccessNumber);
										$('.praiseNumber').text(data.result.praiseNumber);
										$('#timestamp').text(live.timeFn(data.result.duration));
										console.log(data.result.duration);
									});
								}else if(data.childCode == '0001'){
									var oLi = '<li>'+data.extra.name+'来了</li>';
									var oLi1 = '<li><img src='+serverUrl+data.extra.pic+' onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'"></li>';
									$('#chatContent ul').append(oLi);
									$('.audienceImg ul').prepend(oLi1);
									live.swiperFlag('true');
									
									
									$('.audienceImg ul').width(($('.audienceImg').children().children('li').width()+10)*$('.audienceImg').children().children('li').length);
								}else if(data.childCode == '0002'){
									$('.onlineCount').text(data.extra.onlineNumber);
									$('.allCount').text(data.extra.maxAccessNumber);
								}else if(data.childCode == '0009'){
									$('#video,.funPerson,.audienceImg,.playBtn,#chatContent,.openApp').hide();
									$('#video').remove();
									if(reviewURL != ''){
										$('.liveEnd').hide();
										live.reViewFn();
										$('.gift,.message').remove();
									}else{
										$('.liveEnd').show();
										$('.bottomDownload').show();
									}
									
									service.getRoomById(roomId).done(function(data){
										$('.maxAccessNumber').text(data.result.maxAccessNumber);
										$('.praiseNumber').text(data.result.praiseNumber);
										$('#timestamp').text(live.timeFn(data.result.duration));
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

								var oHeight = $('#chatContent ul').height();
								var chatContent = document.getElementById('chatContent'),
								oUl1 = chatContent.getElementsByTagName('ul')[0],
							       startX, startY;
								
							    // touch start listener
							    function touchStart(event) {
							    	live.swiperFlag();
							    }
							    chatContent.addEventListener('touchstart', touchStart, false);
							function touchEnd(event) {
								if(data.childCode == '1001' || data.childCode == '0006' || data.childCode == '0001'){
								}
							}
							 
							chatContent.addEventListener('touchend', touchEnd, false);
								//调用时间
								var duration = $('.duration').attr('data-duration');
								$('.duration span').text(live.timeFn(duration));
								
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
				var page = 1,size = 18,isLive = 1,userId = '',target = $('.videoListBox ul');
				service.getRoomList(page,size,isLive,userId).done(function(json){
					live.makeLiveList(json.result,target);
//					if(json.result.length<18){
//						live.makeLiveList(json.result,target);
//						var page = 1,size = 18-json.result.length,isLive = 0,userId = '',target = $('.videoListBox ul');
//						service.getRoomList(page,size,isLive,userId).done(function(json1){
//							live.makeLiveList(json1.result,target);
//						});
//					}else{
//						
//					}
				});
			},
			makeLiveList:function(data){
				var target = $('.videoListBox ul');
				if(data==''){
					$('.hotRecommend,.reBotton').hide();
					$('.liveEnd').css('height',$(window).height()-150);
//					$('.bottomDownload').css('height','150px');
				}else{
				
				$.each(data,function(m,n){
					if(n.id == roomId && data.length == 1){
						$('.hotRecommend,.bottomDownload').hide();
						$('.liveEnd').css('height',$(window).height()-55);
						$('body').css({
							'height':$(window).height(),
							'overflow':'hidden'
						})
						
						return true;
					}
					else{
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
					var tempList= '<li>'+
					'<a href="javascript:void(0)"><img data-userPic="'+ n.roomPic +'" data-roomId="'+ n.id +'"src="" class="hotPic"></a>'+'</li>';
					if(n.id == roomId) return true;//跳过本次循环 不显示自身直播
					target.append(tempList);
					}
				});
				$('.hotRecommend,.bottomDownload').show();
				}
				//$('.hotRecommend,.reBotton').hide();
				if(data.length<=6){
					$('.hotRecommend .videoListBox ul li').css({
						'width':'100%',
						'margin-bottom':'2px'
					});
				}else{
					$('.hotRecommend .videoListBox ul li').css({
						'width':'33.3%',
						'height':$(window).width()/3,
						'margin-bottom':'0px'
					});
					$('.hotRecommend .videoListBox ul li a img').css('height',$(window).width()/3);
				}
				info.personalPortrait(serverUrl,$('.hotPic'));
				info.personalPortrait(serverUrl,$('.hotUesrPic'));
				$('.videoListBox ul li a').on('click',function(){
					window.location.href = interUrl + '/page/live/shareLive?roomId=' + $(this).find('img').attr('data-roomId');
				});
				
			},
			shareFn: function(){
				var tit = $('.shareBtn').attr('data-name');
				var desc = $('.shareBtn').attr('data-desc');
				var pic = serverUrl+$('.shareBtn').attr('data-videopic');//缩略图
				var videoId = $('#roomId').val();
				var userId = $('.shareBtn').attr('data-userId');
				var isLive=1;
				
				share.init(tit,desc,pic,videoId,uid,userId,isLive);
			},
			goPage: function(type,typeId,anchor){
				var url= '';
				var params= {};

				if(type == 'video'){
					url= interUrl+"/page/video/videoDetail?videoId="+typeId+anchor;
					params= {
						uid: uid,
						isNext: videoShow.isNext,
						nextType: videoShow.nextType,
						activityId: videoShow.activityId,
						tag: videoShow.tag,
						userId: videoShow.userId
					}
				}else if(type == 'userId'){
					url= interUrl+'/page/user/userDetail?userId='+typeId+anchor;
					params= {
						uid: uid
					}
				}
				info.postform(url,params);
			},
			jump:function(obj){
				var roomid = $('#roomId').val();
					if (browse.versions.ios) {
						if(browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
							$('.skyBox').show();
						} else {
							obj.attr("href","wopai://roomid="+roomid);
							timer();
						}
					} else if (browse.versions.android){
						if (browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
							$('.skyBox-anz').show();
						} else {
							obj.attr("href","com.busap.myvideo://main/openwith?roomId="+roomId+"&type=11");
							timer();
						}
					} else {
						window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
					};

					function timer(){
						setTimeout(function(){
							window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
						},3000)
					}
//				}
				$('.skyBox,.skyBox-anz').click(function(){
					$(this).hide();
				});
			},
			hideShowFun:function(target){
				target.on('click',function(){
					if(target.hasClass('hide')){
						target.removeClass('hide').addClass('show');
						$('.funPerson').hide();
						$(this).siblings().hide();
						$('#chatContent').hide();
						$('.audienceImg').hide();
					}else{
						target.removeClass('show').addClass('hide');
						$('.funPerson').show();
						$(this).siblings().show();
						$('#chatContent').show();
						$('.audienceImg').show();
					}
				})
			},
			reViewFn:function(){
				$('.funPerson,.openApp').show();
				var reviewVideoURL = $("#reviewVideoURL").val();
				var reviewVideoDuration = $('#reviewVideoDuration').val();console.log(reviewVideoDuration);
				$('#videoDuration').text('直播时长：'+live.timeFn(reviewVideoDuration));
				$('.playBtn').show();
					if(browse.versions.ios){
						var oPlay = document.getElementById('playBtn');
						var videoBox = document.getElementById('videoBox');
						oPlay.style.display = 'block';
						var sVideo = '<video id="video1" class="video-js vjs-default-skin" autoplay="autoplay" x-webkit-playsinline webkit-playsinline><source src="'+reviewURL+'" type="application/x-mpegURL"></video>';
						var sHtml = '<img id="poster" height="'+$(window).height()+'" src="' + serverUrl + $('#posterURL').val() + '">';
						videoBox.innerHTML = sHtml;
						$('#poster')[0].onload = function(){
							live.imgLoadCompleted();
						}
						
						oPlay.addEventListener('click',function(){
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
			downloadApp:function(){
				 $('body').on('touchstart','.openwopai',function(){
				        if(browse.versions.ios)
				        {
				           window.location.href = 'https://itunes.apple.com/us/app/wo-pai-wo-jiu-shi-zhu-jiao/id934254637?ls=1&mt=8';
				        }
				        if(browse.versions.android)
				        {
				            window.location.href = 'http://www.wopaitv.com/download/myVideo_guanwang_2.3.0.apk';
				        }
				        if(browse.versions.isWx)
				        {
				            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
				        }
				       
				    })
				   $('.openwopai').click(function(){
				        
				        if(!browse.versions.ios && !browse.versions.android)
				        {
				            window.location.href = 'http://www.wopaitv.com/download/myVideo_guanwang_2.3.0.apk';
				        }
				   })
			}
	}
	
	live.init();
});
