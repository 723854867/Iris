
$.extend({
	init: function(){
		_this= this;

		_this.bind= $.requestUrlPrefix+'/pay/bind';      //绑定接口
		_this.unbind= $.requestUrlPrefix+'/pay/unbind';   //解绑接口


		_this.openid = $.getCookie('openId');
		if(_this.openid == ''){
			if ($('#back').val() == ''){
				window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?scope=snsapi_base&response_type=code&redirect_uri=http%3A%2F%2Fapi.wopaitv.com%2Frestwww%2Fpay%2Fwithdraw&state=STATE&appid=wx8a908b16f1e26ade&connect_redirect=1#wechat_redirect';
			}
		} else {
			_this.userExist = $.getCookie('userExist');
			if (_this.userExist == '1') {
				// 已经登陆过
				window.location.href = 'extract';
			}
		}
		//--------绑定-------
		$('.login-btn').click(function(){
			var userPhone= $('.log-1 .user-phone').val();
			var userPwd= $('.log-1 .user-pwd').val();
			//要加判断条件
			if(userPhone == '' || userPwd == ''){
				$.tempTip('用户名或者密码不能为空！');
			}
			$.getAuthorization_code(userPhone,userPwd);

		});

		//解除绑定
//		$('.unbind_btn').click(function(){
//			$.postDate(_this.unbind,'',function(data){
//				if(data["code"] == 200){
//					$.tempTip(data["result"]);
//					setTimeout(function(){
//						window.location.href='login';
//					},500)
//
//				}else if(data["code"] == "400_5"){
//					window.location.href='login';
//				}
//			})
//		});


		//登陆的输入框效果
		$('.user-phone').focus(function(){
			$(this).parent('.login-item').addClass('txt_on');
		}).blur(function(){
			$(this).parent('.login-item').removeClass('txt_on');
		});
		$('.user-pwd').focus(function(){
			$(this).parent('.login-item').addClass('pwd_on');
		}).blur(function(){
			$(this).parent('.login-item').removeClass('pwd_on');
		});
		$('.record_btn').click(function(){
			$('.record_list').show();
		});
		$('.close_record').click(function(){
			$('.record_list').hide();
		});
		$('.pwd-block').click(function(){
			if($(this).hasClass('pwd-hide')){
				$(this).removeClass('pwd-hide');
				$('.log-1 .user-pwd').attr('type','password')
			}else{
				$(this).addClass('pwd-hide');
				$('.log-1 .user-pwd').attr('type','text');
			}
		});
		//登陆的输入框效果结束

	},
//	cbFn: function(){
//		$.setCookie('uid', $.uid,7);
//		$.setCookie('access_token', $.access_token,7);
//		$.setCookie('openId', $.openid,7);
//		$.postDate($.bind,'',function(data){
//			if(data["code"]==200){
//				console.log(data);
//				if(data["result"]["isForbid"]=='true'){
//					$.tempTip('您暂时无法提现，请联系微信客服');
//				}else{
//					window.location.href='extract.jsp';
//				}
//			}
//
//		})
//	}

})
$.init();
