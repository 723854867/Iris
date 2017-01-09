require.config({
	shim:{
		'../libs/jquery.toggle-password':['../libs/jquery-2.1.4']
	}
});
require(['../libs/jquery-2.1.4','../libs/md5Utils','verification','../common/info','../libs/jquery.toggle-password','../common/aler'],function(jquery,md5Utils,verification,info,togglePassword,alert){
	
	 var register = {
		 init:function(){
			 register.checkTelNumber();
			 register.checkPass();
			 register.lookPass();
			 register.bindBtn();
			 register.checkOrientation();
			 $('.indexRecommend').show();
			 if(info.getCookie('liveSign')==1){
				 $('.indexRecommend').hide();
				 info.clearCookie('liveSign');
			 }
		 },
		 checkTelNumber:function(){//校验手机号码
				$("#tel").change(function(){
					var phoneNumber = $("#tel").val();
					var codeId = $('#getCodeBtn');
					verification.checkMobile(phoneNumber,codeId);
				});
		},
		checkPass:function(){//检验验证码
			$("#password").change(function(){
				var pwdVal = $("#password").val();
				verification.checkPassword(pwdVal);
			});
		},
		lookPass:function(){//显隐密码
			$('#password').togglePassword({
				el: '#togglePassword'
			});
		},
		bindBtn:function(){
			 var authorization_code = ""; // 登陆授权码
			 var access_token = ""; // 登陆token
			 var uid; // 登陆用户id，用户唯一标识
			 var redirectFlag = false;
			$('#code').next().attr("disabled", true);

			$(document).ready(function(){
			 	$("#getCodeBtn").unbind("click");
			 	$("#regBtn").unbind("click");

			 	//绑定发送手机验证码按钮
			 	$("#getCodeBtn").click(function (){
			 		info.getCode($("#tel").val(),true);
			 	});

			 	//绑定注册按钮
			 	var regPlatform;
			 	if(info.getCookie('channel')==''){
			 		regPlatform = 'h5';
			 	}else{
			 		regPlatform = 'h5-'+info.getCookie('channel');
			 	}
			 	$("#regBtn").click(function (){
			 		info.reg($("#tel").val(),$("#password").val(),$("#code").val(),regPlatform);
					_czc.push(["_trackEvent","注册","注册按钮"]);
			 	});
			 });
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
	 register.init();
	
});