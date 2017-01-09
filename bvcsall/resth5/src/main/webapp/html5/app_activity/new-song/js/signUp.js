$(function(){
	var signUpObj = {
			init:function(){
				var serverUrl = $('#serverUrlimg').val();
				var shareImg = serverUrl+"/resth5/html5/app_activity/new-song/img/suoluetu.png";
				var shareTitle = '加入LIVE直播战队';
				var shareText = '直通《中国新歌声》总决赛","中国新歌声LIVE直播第五战队征召正式启动，现在加入即可直通决赛舞台，更有冠军同款奇瑞汽车等你拿！';
				
				var interUrl = initFn.getRequestPrefix();
				var target = $('.shareBtn');
				var uid = initFn.getCookie('uid');
				var accessToken = initFn.getCookie('access_token');
				var activityId = initFn.getCookie('liveActivityId');
				var isJoin = initFn.getCookie('isJoin');
				var params = {'uid':uid,'accessToken':accessToken,'id':activityId};
				var ajaxUrl = interUrl + '/restwww/liveActivity/joinLiveActivity';
				var isLive = window.location.hash;
				var backurl1 = window.location.href;
				var arrObj = interUrl.split('//');
				var commonUrl = arrObj[1];
				
				var regPlatform;
				if(backurl1.indexOf('&')>0){
					var a = backurl1.split('?')[1].split('&');
					var str = '';
					for(var i=0;i<a.length;i++){
						if(a[i].indexOf("pos")>='0'){
							str=a[i];
						}else{
							
						}
					}
					var backurl =(backurl1.split('?')[0]+'?'+str).split('#')[0];
					
				}else{
					var backurl = window.location.href.split('#')[0];
				}
				initFn.setCookie('backurl',encodeURIComponent(backurl),7);
				if(initFn.getCookie('channel')==''){
			 		regPlatform = 'h5';
				}else{
					regPlatform = 'h5-'+initFn.getCookie('channel');
				}
				if(browser.versions.isWx){
					if(accessToken == '""'){
						window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2fresth5%2fthirdPart%2fwechatCallback&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect%3fregPlatform='+regPlatform;
						return;
					}else{
						
					}
				}else if(browser.versions.isQQ){
					signUpObj.jumpDownload();
					$('.signUp').on('click',function(){
						initFn.goDown();
					})
				}else if(browser.versions.isWeiBo){
					signUpObj.jumpDownload();
					$('.signUp').on('click',function(){
						initFn.goDown();
					})
				}
				
				wx.ready(function(){

					 
				
				wx.onMenuShareTimeline({
				    title: shareTitle, // 分享标题
				    link: '', // 分享链接
				    imgUrl: shareImg, // 分享图标
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
				    imgUrl: shareImg, // 分享图标
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
				    imgUrl: shareImg, // 分享图标
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
				    imgUrl: shareImg, // 分享图标
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
				    imgUrl: shareImg, // 分享图标
				    success: function () { 
				       // 用户确认分享后执行的回调函数
				    },
				    cancel: function () { 
				        // 用户取消分享后执行的回调函数
				    }
				});
			});
				
				
				if(isLive.indexOf('islive') >0 ){
					$('.topBlock').hide();
					if(isJoin == '0'){
						$('.signUp').on('click',function(){
							signUpObj.signUpFn(ajaxUrl,params,$(this),activityId);

							_czc.push(﻿["_trackEvent","规则页面报名","报名"]);
						});
					}else if(isJoin == '1'){
//						$('.signUp').text("立即开播");
						$('.signUp').text("已报名");
//						$('.signUp').on('click',function(){
//							initFn.navType('2',activityId,uid);
//						});
					}
				}else{
					$('.fix a.shareBtn').hide();
					$('.fix a.signUp').css('margin-left','20%');
					if(browser.versions.isWx){
						if(isJoin == '0'){
							$('.signUp').on('click',function(){
								signUpObj.signUpFn(ajaxUrl,params,$(this),activityId);
							});
						}else if(isJoin == '1'){
//							$('.signUp').text("立即开播");
							$('.signUp').text("已报名");
//							$('.signUp').on('click',function(){
//								signUpObj.jumpDownload();
//							});
						}
					}else{
						$('.signUp').on('click',function(){
							initFn.goDown();
						});
					}

				}
				
				signUpObj.jumpDownload();
				signUpObj.splitUrl();
				signUpObj.shareApp(target,interUrl,backurl);
				
			},
			splitUrl:function(){
				var num = initFn.getCookie('pos');
				$('.ruleBlock .ac_entrance').eq(num).show().siblings().hide();
			},
			signUpFn:function(ajaxUrl,params,obj,activityId,uid){
				initFn.AJAX(ajaxUrl,params,activityId,uid).done(function(json){
					if(json.code == 200){
//						obj.text("立即开播");
						obj.text("已报名");
//						obj.on('click',function(){
//							initFn.navType('2',activityId,uid);
//						});
					}
				});
			},
			shareApp:function(target,interUrl,backurl){
				target.on('click',function(){
					if(browser.versions.ios){
						
						sharefn("加入LIVE直播战队，直通《中国新歌声》总决赛","中国新歌声LIVE直播第五战队征召正式启动，现在加入即可直通决赛舞台，更有冠军同款奇瑞汽车等你拿！",interUrl+"/resth5/html5/app_activity/new-song/img/suoluetu.png",backurl);
					}
					if(browser.versions.android){
						
						window.share.onShareClick(interUrl+"/resth5/html5/app_activity/new-song/img/suoluetu.png","加入LIVE直播战队","中国新歌声LIVE直播第五战队征召正式启动，现在加入即可直通决赛舞台，更有冠军同款奇瑞汽车等你拿！",backurl);
					}
				});
			},
			jumpDownload:function(){
				 $('body').on('touchstart','.joinBtn',function(e){

					 e.preventDefault();
					 if(browser.versions.ios)
				        {
				        	if(browser.versions.isWeiBo){
				        		$('.skyBox').show();
					        }else{
					        	window.location.href = 'https://itunes.apple.com/us/app/wo-pai-wo-jiu-shi-zhu-jiao/id1077305226?ls=1&mt=8';
					        }
				        }
				        if(browser.versions.android)
				        {
				        	if(browser.versions.isWeiBo){
				        		$('.skyBox-anz').show();
					        }else{
					        	window.location.href = initFn.appDownUrl;
					        }
				        }
				        if(browser.versions.isWx)
				        {
				            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
				        }
				       
				    })
				    $('.skyBox,.skyBox-anz').click(function(){
				    	$(this).hide();
				    });
					_czc.push(﻿["_trackEvent","顶部下载按钮","下载"]);
			}
	}
	signUpObj.init();
});