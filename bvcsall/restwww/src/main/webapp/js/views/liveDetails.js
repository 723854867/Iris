define(['../libs/jquery-2.1.4','head','../common/info','../common/aler','../service/service','../common/share','../common/browse'],function(jquery,head,info,alert,service,share,browse){
	var serverUrl = info.getPicPerfix();
	var interUrl = info.getRequestPrefix();
	var uid = info.getCookie('uid');
	var access_token = info.getCookie('access_token');
	var roomId = $('#roomId').val();
	$('.shareBtn').attr('data-videoid',roomId);
	var liveDetails = {
			init:function(){
				$(window).scroll(function(){
					info.lazyPicLoad($('.videoListBox li a img'));
					info.lazyPicLoad($('.hotUesrPic'));
				});
				if(parseInt($('.praise span').text())>=10000){
					var praiseNum = (parseInt($('.praise span').text())/10000).toFixed(1);
					$('.praise span').text(praiseNum+'万');
				}
				info.personalPortrait(serverUrl,$('.userPortrait-1'));
				info.videoInfo(serverUrl,$('.videoPic'));
				//获取直播列表
				liveDetails.roomList();
				$(function(){
					$('.playBox').css('height',$('.playBox').width())
					$('.blackBox').css('height',$('.playBox').width());
				})
				
				$('.blackBox').click(function(){
					_czc.push(["_trackEvent","livePlay","播放按钮"]);
				})
				//点击头像跳转
				$('div').delegate('.go-user-page,.userPortrait-1','click',function(){
					window.location.href = "http://wopaitv.com";
					_czc.push(["_trackEvent","liveUserHead","直播人头像"]);
				});
				//添加跳转连接
				$('.canPlay,.commit em').on('click',function(){
					liveDetails.jump($(this));
				});
				//分享
				$('.shareBtn').click(function(){
					liveDetails.shareFn();
					_czc.push(["_trackEvent","liveShare","分享按钮"]);
				});
				//判断
				var sign = info.getCookie('sign');
				if(sign=='bbb'){
					$('.showBox').show();
					$('.bgBox').show();
					$('.showBox h2').text('关注成功！');
					info.clearCookie('sign');
					$('.showBox-href').click(function(){
						location.href = "http://wopaitv.com/#cnzz_name=loading&cnzz_from=liveAttentionDownload";
					})
					info.clearCookie('sign');
				};
				//埋点事件
				$('div').delegate('.zan','click',function(){
					window.location.href = "http://wopaitv.com/#cnzz_name=loading&cnzz_from=livePraise";
				});
				$('div').delegate('.commit','click',function(){
					window.location.href = "http://wopaitv.com/#cnzz_name=loading&cnzz_from=liveComment";
				});

				$('.openApp a').click(function(){
					window.location.href = "http://wopaitv.com/#cnzz_name=loading&cnzz_from=liveDownload";
				})
				//调用时间
				var duration = $('.duration').attr('data-duration');
				liveDetails.timeFn(duration);
				$('.duration').text(liveDetails.timeFn(duration));
				liveDetails.closeBox($('.showBox span'),$('.showBox'));
				liveDetails.closeBox($('.showBox span'),$('.bgBox'));
				//关注
				if(sign=='aaa'){
					var userId = $('#attentionBtn').attr('data-userId');
					service.isAttention(userId).done(function(json){
						if(json.result == 0){
							$('#attentionBtn').attr('data-isAttention','0');
							$('#attentionBtn').removeClass('attenBtnActive').addClass('attenBtn');
							$('#attentionBtn').one('click',function(){
								liveDetails.attention();
							})
						}else if(json.result == 1){
							$('#attentionBtn').attr('data-isAttention','1');
							$('#attentionBtn').removeClass('attenBtn').addClass('attenBtnActive');
						}
					});
				}else if(sign=='bbb'){
					var userId = $('#attentionBtn').attr('data-userId');
					service.isAttention(userId).done(function(json){
						if(json.result == 0){
							$('#attentionBtn').attr('data-isAttention','0');
							$('#attentionBtn').removeClass('attenBtnActive').addClass('attenBtn');
								liveDetails.attention();
						}else if(json.result == 1){
							$('#attentionBtn').attr('data-isAttention','1');
							$('#attentionBtn').removeClass('attenBtn').addClass('attenBtnActive');
						}
					});
				}else if(sign=='' && uid != ''){
					var userId = $('#attentionBtn').attr('data-userId');
					service.isAttention(userId).done(function(json){
						console.log(json)
						if(json.code=='200'){
							if(json.result == 0){
								$('#attentionBtn').attr('data-isAttention','0');
								$('#attentionBtn').removeClass('attenBtnActive').addClass('attenBtn');
								$('#attentionBtn').one('click',function(){
									liveDetails.attention();
								})
							}else if(json.result == 1){
								$('#attentionBtn').attr('data-isAttention','1');
								$('#attentionBtn').removeClass('attenBtn').addClass('attenBtnActive');
							}
						}
					}).fail(function(json){
						if(json.code=='401'){
							info.clearCookie('uid');
							$('.canPlay').attr('src','');
							location.reload();
						}
					});
				}else if(sign=='' && uid == ''){
					$('#attentionBtn').on('click',function(){
						alert.alertMsg('请登录');
						setInterval(function(){
							window.location.href = info.getRequestPrefix() + '/page/user/login';
							info.setCookie('login','');
						},1000)
					})
				}
			},
			roomList:function(){
				var page = 1,size = 6,isLive = 1,userId = '',target = $('.videoListBox ul');
				service.getRoomList(page,size,isLive,userId).done(function(json){
					if(json.result.length<7){
						liveDetails.makeLiveList(json.result,target);
						var page = 1,size = 6-json.result.length,isLive = 0,userId = '',target = $('.videoListBox ul');
						service.getRoomList(page,size,isLive,userId).done(function(json1){
							liveDetails.makeLiveList(json1.result,target);
						});
					}else{
						
					}
				});
			},
			makeLiveList:function(data){
				var target = $('.videoListBox ul');
				$.each(data,function(m,n){
					if(n.id==roomId){
						return true;
					}else{
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
					'<a href="http://wopaitv.com/#cnzz_name=loading&cnzz_from=liveList"><img data-userPic="'+ n.roomPic +'" data-videoId="'+ roomId +'"src="" class="hotPic"></a>'+
					'<div class="videoList">'+
					'<span class="fr"><em class="playNum"></em>'+ n.maxAccessNumber +'</span>'+
					'</div>' +
					'<div class="userTit">' +
					'<div class="useradddV">'+
					'<img class="fl hotUesrPic go-user-page" data-userId="'+ n.creatorId +'" data-userPic="'+ n.anchorPic +'" src="" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
					vStart+
					'</div>'+
					'<span class="userName">'+ liveName+'</span>' +
					'</div>'
					'</li>';
					target.append(tempList);
					$('.hotPic').css('height',$('.hotPic').width());
					}
				});
				info.personalPortrait(serverUrl,$('.hotPic'));
				info.personalPortrait(serverUrl,$('.hotUesrPic'));
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
			attention:function(){
				var attentionId = $('#attentionBtn').attr('data-userId');
				var dataFrom = 'h5';
				var isAttention = $('#attentionBtn').attr('data-isAttention');
				if(uid==''){
					window.location.href = info.getRequestPrefix() + '/page/user/login';
				}else{
					service.addAttention(attentionId,dataFrom,isAttention).done(function(json){
						$('#attentionBtn').attr('data-isAttention','1');
						$('#attentionBtn').removeClass('attenBtn').addClass('attenBtnActive');
						$('.showBox').show();
						$('.bgBox').show();
						$('.showBox h2').text('关注成功！');

					})
				}
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
			shareFn: function(){
				var tit = $(this).attr('data-name')
				var desc = $(this).attr('data-desc')
				var pic = serverUrl+$(this).attr('data-videopic');//缩略图
				var videoId = $(this).attr('data-videoId');
				var userId = $(this).attr('data-userId');
				var isLive=1;
				_czc.push(["_trackEvent","分享按钮","videoDetailTurn"])
				share.init(tit,desc,pic,videoId,uid,userId,isLive);
			},
			closeBox:function(obj,box){
				obj.on('click',function(){
					box.hide();
				})
			},
			timeFn:function(timestamp){
				if(timestamp/3600000>1){
					var hour = parseInt(timestamp/3600000);
					if(timestamp%3600000/60000>1){
						var min = parseInt(timestamp%3600000/60000);
						if(timestamp%3600000%60000>1){
							var sec = parseInt(timestamp%3600000%60000/1000);
						}
					}else{
						var hour = '0';
						if(timestamp/60000>1){
							var min = parseInt(timestamp%60000);
							if(timestamp%60000/1000>1){
								var sec = parseInt(timestamp%60000%1000);
							}
						}else{
							var min = '0';
							var sec = timestamp/1000;
						}
					}
				}else{
					var hour = '0';
					if(timestamp/60000>1){
						var min = parseInt(timestamp/60000);
						if(timestamp%60000/1000>1){
							var sec = parseInt(timestamp%60000/1000);
						}
					}else{
						var min = '0';
						if(timestamp/1000>1){
							var sec = parseInt(timestamp%1000);
						}
					}
				}
				return hour+':'+min+':'+sec;
			}
	}
	liveDetails.init();
})