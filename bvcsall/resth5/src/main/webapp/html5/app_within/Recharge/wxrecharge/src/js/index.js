
$.extend({
	init: function(){
		_this= this;
		//_this.httpUrl= 'http://ceshi.wopaitv.com/restwww/download/';
		_this.httpUrl= 'http://cdn.wopaitv.com/restwww/download/';
		_this.server= '/restwww';
		_this.idverification=_this.server+'/pay/getUserInfo';  //用户ID接口
		_this.idpay=_this.server+'/pay/create';  //支付接口 json格式字符串
		_this.getDiamondList= _this.server+'/live/getDiamondList';  //获取钻石列表
		_this.ip=returnCitySN.cip;
		_this.id=0;

		_this.picSouce= $('#picSouceUrl').val();
		var params= {
			"userId" : 0,             //用户id
			"produceId" : 0,            //产品id
			"channel" : "wx_pub",         // 微信公众账号支付
			"client_ip" : _this.ip,  // 终端ip
			"openId":''
		}
		var openid = $('#openid').val();
		if(openid != ''){
			params.openId=openid;
			//document.title=openid
		}else{
			window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?scope=snsapi_base&response_type=code&redirect_uri=http%3A%2F%2Fapi.wopaitv.com%2Frestwww%2Fpay%2FwechatCallback&state=STATE&appid=wx8a908b16f1e26ade&connect_redirect=1#wechat_redirect';
		}
		//判断cookie是否存在
		if($.getCookie('userId')){
			_this.id= $.getCookie('userId');
			$('.id-txt').val(_this.id);
			$.postDate(_this.idverification,'userId='+_this.id,function(data){
				if(data.code == 200){
					$('.user-name,.user-name2').html(data.result.name);
					$('.user-pho').attr('src',_this.httpUrl+data.result.pic);

					$('.one').hide();
					$('.two').show();
					$.setCookie('userId',_this.id)
				}else{
					$.tempTip(data.message);
				}
			})
		}else{
			$('.one').show();
			$('.two').hide();
		}
		$.postDate(_this.getDiamondList,'isApple=0',function(data){
			if(data.code== 200){
				$.each(data.result,function(m,n){
					var aLi;
					if(n.isGive=='0'){
						aLi= $("<li data-id='"+ n.id +"'>"+
						"<img src='"+_this.picSouce+"/dist/img/gold.png' class='gold-icon'/>"+
						"<span class='list-left'>"+ n.name+"</span>"+
						"<span class='list-right'>￥"+ n.price+"</span>"+
						"</li>");
					}else{
						aLi= $("<li data-id='"+ n.id +"'>"+
						"<img src='"+_this.picSouce+"/dist/img/gold.png' class='gold-icon'/>"+
						"<span class='list-left'>"+ n.name+"</span>"+
						"<span class='list-middle'>赠送"+ n.giveCount+"金币</span>"+
						"<span class='list-right'>￥"+ n.price+"</span>"+
						"</li>");
					}
					$('.list-gold ul').append(aLi);
				})
			}else{
				$.tempTip(data.message)
			}
		})
		//小提示
		$('.get-tips').click(function(){
			$('.big-warp').show();
		});
		$('.iKnow-btn').click(function(e){
			$('.big-warp').hide();
			e.preventDefault();
			$(document.body).animate({scrollTop:0})
		})

		$('.right-btn').click(function(){
			var texVal= $('.id-txt');
			_this.id=  texVal.val();
			if(isNaN(_this.id)){
				$.tempTip('ID号必须为数字');
				return;
			}
			if(!_this.id && _this.id == ''){
				$.tempTip('账号不能为空！');
			}else{
				$.postDate(_this.idverification,'userId='+_this.id,function(data){
					if(data.code == 200){
						$('.user-name,.user-name2').html(data.result.name);
						$('.user-pho').attr('src',_this.httpUrl+data.result.pic);

						$('.one').hide();
						$('.two').show();
						$.setCookie('userId',_this.id)
					}else{
						$.tempTip(data.message);
					}
				})
				
			}
		});
		$('.switch-user').click(function(){
			if($.getCookie('userId')){
				$('.id-txt').val(_this.id)
			}else{
				$('.id-txt').val('')
			}
			$('.one').show();
			$('.two').hide();
		});
		$('body').delegate('.list-gold ul li','touchstart',function(){
			$('.list-gold ul li').removeClass('active');
			$(this).addClass('active');
			params.produceId= parseInt($(this).attr('data-id'));
		})
        //购买金币
		$('.buy-btn').click(function(){
			params.userId= parseInt(_this.id);
			if(params.produceId==0){
				$.tempTip('请选择购买的金币数');
				return;
			}
			$('.gold-num').html($('.active').find('.list-left').html());

			$.postDate(_this.idpay,'paramJson='+JSON.stringify(params),function(data){
				if(data.code == 200){
					pingpp.createPayment(data.result, function(result, error){
						if (result == "success") {
							$.tempTip('支付成功');
							$('.buy-ok,.skyBox').show();

							// 只有微信公众账号 wx_pub 支付成功的结果会在这里返回，其他的 wap 支付结果都是在 extra 中对应的 URL 跳转。
						} else if (result == "fail") {
							// charge 不正确或者微信公众账号支付失败时会在此处返回
							$.tempTip('支付失败');
						} else if (result == "cancel") {
							// 微信公众账号支付取消支付
							$.tempTip('取消支付');
						}
					});
				}else{
					$.tempTip(data.message);
				}
			})
		});
		$('.close-btn').click(function(){
			$('.buy-ok,.skyBox').hide();
		})


	},
	//ajax方法
	postDate: function(url,params,callBack){
		$.ajax({
			type: "post",
			url: url,
			data: params,
			success: callBack,
			beforeSend: function(request){
				$('.load-box').show();
				request.setRequestHeader('access_token', $.access_token);
				request.setRequestHeader('uid', $.uid);
			},
			dataType: 'json',
			complete: function(){
				$('.load-box').hide();
			}
		})
	},
	//提示语
	tempTip: function(msg,callFn){
		var box = $('<div class="tipsWarp">'+msg+'</div>');
		$('body').append(box);
		setTimeout(function(){
			box.remove();
		},2000);
		callFn && callFn();
	},
	//cookie方法
	setCookie:function (cname, cvalue, exdays){
		var d = new Date();
		d.setTime(d.getTime() + (exdays*24*60*60*1000));
		var expires = "expires="+d.toUTCString();
		document.cookie = cname + "=" + cvalue + "; " + expires+";path=/";
	},
	getCookie:function(cname){
		var name = cname + "=";
		var ca = document.cookie.split(';');
		for(var i=0; i<ca.length; i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') c = c.substring(1);
			if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
		}
		return "";
	},
	clearCookie:function(name){
		document.cookie = name+"=;expires=0;;path=/";
	}
})
$.init();
