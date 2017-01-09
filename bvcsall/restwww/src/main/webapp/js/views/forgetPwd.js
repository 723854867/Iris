require.config({
	shim:{
		'../libs/jquery.toggle-password':['../libs/jquery-2.1.4']
	}
});
require(['../libs/jquery-2.1.4','verification','../common/info','../common/aler','../libs/jquery.toggle-password','../libs/md5Utils'],function(jquery,verification,info,alert,togglePassword,md5Utils){

	var forgetPwd = {
			init:function(){
				forgetPwd.checkTelNumber();
				forgetPwd.checkPass();
				forgetPwd.lookPass();
				forgetPwd.bindBtn();
				forgetPwd.checkOrientation();
				$('.regContent p input').on('focus',function(){
					$(this).addClass('se').parent().siblings().children().removeClass('se');
				});
				$('.regContent p input').on('blur',function(){
					$(this).removeClass('se');
				});
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
				$('#code').next().attr("disabled", true);

			 	var btnCode = document.getElementById("getCodeBtn");
				btnCode.onclick=function(){
					info.getCode($("#tel").val(),false);
				}

				$('#btnForget').on('click',function(){
					info.Sub($('#tel').val(),$('#code').val(),$('#password').val());
				})
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
	forgetPwd.init();
});