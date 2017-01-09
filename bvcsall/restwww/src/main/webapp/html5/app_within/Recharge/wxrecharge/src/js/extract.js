
$.extend({
	init: function(){
		_this= this;

		_this.userInfoApi= $.requestUrlPrefix+'/pay/getUser';
		_this.uid= $.getCookie('uid');
		_this.token= $.getCookie('access_token');
		_this.bind= $.requestUrlPrefix+'/pay/bind';      //绑定接口
		_this.unbind= $.requestUrlPrefix+'/pay/unbind';   //解绑接口

        //------默认请求用户信息-------
		$.postDate(_this.userInfoApi,'',function(data){
			if(data.code=='200'){
				var userPic= _this.picUrlPrefix+data['result']['pic'];
				var userName= data['result']['name'];
				$('.user-pho').attr('src',userPic);
				$('.user-name').html(userName);
				$('.money-num em').html(data['result']['point']);
				$('.money-num span').html(data['result']['price']);
			}else if(data.code=='400_5'){
				window.location.href='login.jsp';
			}
		})

		//规则提示
//		$('.rule').click(function(){
//			$('.rule-info,.skyBox').show();
//		});
		$('.rule-info .close').click(function(e){
			$('.rule-info,.skyBox').hide();
		});
		//规则提示结束


		//解除绑定
		$('.switch-user').click(function(){
			$.postDate(_this.unbind,'',function(data){
				if(data["code"] == 200){
					$.tempTip(data["result"]);
					$.clearCookie('uid');
					$.clearCookie('access_token');
					setTimeout(function(){
						window.location.href='login?back=unbind';
					},500)

				}else if(data["code"] == "400_5"){
					$.tempTip('解绑失败！');
				}
			})
			

		});
	}







})
$.init();
