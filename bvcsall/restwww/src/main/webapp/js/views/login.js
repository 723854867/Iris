require(['verification','../common/browse','../common/aler', '../libs/md5Utils','../common/info'],function(verification,browse,aler, md5Utils,info){
	var interUrl = info.getRequestPrefix();
	var arrObj = interUrl.split('//');
	var arrObj1 = arrObj[1].split('/');
	var commonUrl = arrObj1[0];
	info.clearCookie("uid");
	info.clearCookie("access_token");
	var login = {
			init:function(){
				login.checkTelNumber();
				login.checkPass();
				login.checkBrowser();
				login.bindLoginBtn();
				login.checkOrientation();
				var backurl=document.referrer;
				$('.indexRecommend').show();
				if(backurl.indexOf('live') > 0){
					$('.indexRecommend').hide();
					info.setCookie('liveSign',1);
				}
				
			 	info.setCookie('backurl',encodeURIComponent(backurl),7);
			 	var regPlatform;
			 	if(info.getCookie('channel')==''){
			 		regPlatform = 'h5';
			 		$('.webChat').on('click',function(){
						window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2frestwww%2fpage%2fthirdPart%2fwechatCallback&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect%3fregPlatform='+regPlatform;
					});
					$('.tercentQQ').on('click',function(){
						window.location.href='https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101254957&redirect_uri=http%3a%2f%2f'+commonUrl+'%2frestwww%2fpage%2fthirdPart%2fqqCallback%3fregPlatform='+regPlatform;
					});
					$('.sina').on('click',function(){
						window.location.href='https://api.weibo.com/oauth2/authorize?response_type=code&client_id=255443166&display=mobile&forcelogin=true&redirect_uri=http%3a%2f%2f'+commonUrl+'%2frestwww%2fpage%2fthirdPart%2fsinaCallback%3fregPlatform='+regPlatform;
						_czc.push(["_trackEvent","新浪微博登录","weiboLogin"]);
					});
			 	}else{
			 		regPlatform = 'h5-'+info.getCookie('channel');
			 		$('.webChat').on('click',function(){
						window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2frestwww%2fpage%2fthirdPart%2fwechatCallback%3fregPlatform='+regPlatform+'&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect';
					});
					$('.tercentQQ').on('click',function(){
						window.location.href='https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101254957&redirect_uri=http%3a%2f%2f'+commonUrl+'%2frestwww%2fpage%2fthirdPart%2fqqCallback%3fregPlatform='+regPlatform;
					});
					$('.sina').on('click',function(){
						window.location.href='https://api.weibo.com/oauth2/authorize?response_type=code&client_id=255443166&display=mobile&forcelogin=true&redirect_uri=http%3a%2f%2f'+commonUrl+'%2frestwww%2fpage%2fthirdPart%2fsinaCallback%3fregPlatform='+regPlatform;
						_czc.push(["_trackEvent","新浪微博登录","weiboLogin"]);
					});
			 	}
				if ($('#thirdPartLoginFlag').val() == 'fail') {
					alert.alertMsg('第三方登陆失败');
				}
				
			},
			bindLoginBtn:function(){
				 var authorization_code = ""; // 登陆授权码
				 var access_token = ""; // 登陆token
				 var uid; // 登陆用户id，用户唯一标识
				 var redirectFlag = false;

				 $(document).ready(function(){
				 	$("#loginBtn").unbind("click");
				 	//绑定登陆按钮
				 	$("#loginBtn").click(function (){
				 		info.login($("#tel").val(),$("#password").val());
						_czc.push(["_trackEvent","登录按钮","登录页面的登录按钮"]);
				 	});
				 });
			},
			checkTelNumber:function(){//校验手机号码
				$("#tel").change(function(){
					var phoneNumber = $("#tel").val();
					verification.checkMobile(phoneNumber);
				});
			},
			checkPass:function(){//检验验证码
				$("#password").change(function(){
					var pwdVal = $("#password").val();
					verification.checkPassword(pwdVal);
				});
			},
			checkBrowser:function(){//判断浏览器
				if(browse.versions.isWx){
			        $('.webChat').css('display','inline-block');
			        $('.tercentQQ').css('display','inline-block');
			        $('.sina').css('display','inline-block');
			    }else if(browse.versions.isQQ){
			        $('.webChat').css('display','none');
			        $('.tercentQQ').css('display','inline-block');
			        $('.sina').css('display','none');
			    }else if(browse.versions.isWeiBo){
			        $('.webChat').css('display','none');
			        $('.tercentQQ').css('display','none');
			        $('.sina').css('display','inline-block');
			    }else{
			    	$('.webChat').css('display','none');
			        $('.tercentQQ').css('display','inline-block');
			        $('.sina').css('display','inline-block');
			    }

			},
			checkOrientation:function(){
				window.onorientationchange=function(){
					if(window.orientation==0 || window.orientation==180){
					        $('.bgBox2').hide().siblings().show();
					        $('#cnzz_stat_icon_1256005299').hide();
				    }else
				    {
				        $('.bgBox2').show().siblings().hide();
				        
				    }
				};
				window.orientationchange=function(){
					if(window.orientation==0 || window.orientation==180){
				        $('.bgBox2').hide().siblings().show();
				        $('#cnzz_stat_icon_1256005299').hide();
				    }else
				    {
				        $('.bgBox2').show().siblings().hide();

				    }
				};
			}
	}
	login.init();
});