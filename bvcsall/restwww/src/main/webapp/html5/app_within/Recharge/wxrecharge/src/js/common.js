
$.extend({
	init: function(){
		_this= this;
		_this.bind= $.requestUrlPrefix+'/pay/bind';      //绑定接口
		_this.unbind= $.requestUrlPrefix+'/pay/unbind';   //解绑接口
		_this.authorization_code = ""; // 登陆授权码
		_this.access_token = ""; // 登陆token
		_this.uid= ''; // 登陆用户id，用户唯一标识
		//本地环境
		_this.requestUrlPrefix = $('#interfaceurl').val()+"/restwww"; // 请求url前缀
		_this.picUrlPrefix= $('#serverUrlimg').val()+'/restwww/download/';

		//测试环境
		//_this.httpUrl= _this.requestUrlPrefix+'/download/';
		//_this.picUrlPrefix = "http://192.168.108.160:8080/restadmin/download"; // 图片资源url前缀

		//url前缀-----正式环境
		//_this.picUrlPrefix = _this.requestUrlPrefix+"/download"; // 图片资源url前缀
		//_this.httpUrl= _this.requestUrlPrefix+'/download';



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
				request.setRequestHeader('access_token', $.getCookie('access_token'));
				request.setRequestHeader('uid', $.getCookie('uid'));
				request.setRequestHeader('openId', $.getCookie('openId'));
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
	},

	// 获得用户信息
	 getUserInfo: function(token,callFn) {
		$.ajax({
			url : $.requestUrlPrefix+'/userInfo',
			data : {
				'access_token' : token
			},
			type : 'get',
			cache : false,
			dataType : 'json',
			success : function(data) {
				if (data["code"] == "200") {
					//alert(data["result"]["name"]);
					$.uid= data["result"]["id"];
					$.setCookie('uid', $.uid,7);
					callFn && callFn();
				} else {
					$.tempTip(data["message"]);
				}
			},
			error : function() {
				$.tempTip("获取资源失败！");
			}
		});
	},
	// 获得用户登陆token
	 getAccess_token: function() {
		$.ajax({
			url : $.requestUrlPrefix+'/accessToken',
			data : {
				'grant_type' : 'authorization_code',
				'code' : $.authorization_code,
				'client_id' : 'c1ebe466-1cdc-4bd3-ab69-77c3561b9dee',
				'client_secret' : 'd8346ea2-6017-43ed-ad68-19c0f971738b',
				'redirect_uri' : 'url'
			},
			type : 'post',
			async : false,
			cache : false,
			dataType : 'json',
			beforeSend: function(request){
				$('.load-box').show();
				request.setRequestHeader('access_token', $.getCookie('access_token'));
				request.setRequestHeader('uid', $.getCookie('uid'));
				request.setRequestHeader('openID', $.getCookie('openid'));
			},
			success : function(data) {
				if (data["code"] == "200") {
					//alert(data["result"]["access_token"]);
					$.access_token = data["result"]["access_token"];  //获得登陆token
					$.setCookie('access_token', $.access_token,7);
					$.getUserInfo($.access_token,$.cbFn);
				} else {
					$.tempTip(data["message"]);
				}
			},
			error : function() {
				$.tempTip("获得登陆token失败！");
			}
		});
	 },
	// 获得用户登陆授权码
	 getAuthorization_code: function(username, password) {
		$.ajax({
			url : $.requestUrlPrefix+'/authorize',
			data : {
				'response_type' : 'code',
				'client_id' : 'c1ebe466-1cdc-4bd3-ab69-77c3561b9dee',
				'username' : username,
				'password' : $.md5(password)
			},
			type : 'post',
			async : false,
			cache : false,
			dataType : 'json',
//			beforeSend: function(request){
//				$('.load-box').show();
//				request.setRequestHeader('access_token', $.getCookie('access_token'));
//				request.setRequestHeader('uid', $.getCookie('uid'));
//				request.setRequestHeader('openID', $.openid);
//			},
			success : function(data) {
				if (data["code"] == "200") { // code==200,表示请求成功
					//alert(data["result"]["authorization_code"]);
					$.authorization_code = data["result"]["authorization_code"];  //获得登陆授权码
					$.getAccess_token();
				} else if (data["code"] == "619") {
					$.tempTip('绑定失败！此账户已绑定在其他微信上！');
				} else {
					$.tempTip(data['message']);
					//alert(data["code"]+data["message"]);
				}
			},
			error : function() {
				$.tempTip("获得登陆授权码失败！");
			}
		});
	 },
	 cbFn: function(){
			$.setCookie('uid', $.uid,7);
			$.setCookie('access_token', $.access_token,7);
			//$.setCookie('openId', $.openid,7);
			$.postDate($.bind,'',function(data){
				if(data["code"]=='616'){
					$.tempTip('您暂时无法提现，请联系微信客服');
				}else{
					if(data["code"]==200){
						//console.log(data);
						window.location.href='extract';
					}else {
						$.tempTip(data['message']);
					}
				}
			})
		}


})
$.init();
