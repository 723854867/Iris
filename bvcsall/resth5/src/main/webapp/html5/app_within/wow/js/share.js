$(function(){
	
	// 发送手机验证码
	 function getCode(phone, isReg){
		 if(phone == ''){
			 return;
		 }else{
			 $.ajax({
			 		url : $('#interfaceurl').val()+"/resth5"+'/wow/getCode',
			 		data : {
			 			'phone' : phone,
			 			'isReg' : isReg
			 		},
			 		type : 'post',
			 		async : false,
			 		cache : false,
			 		dataType : 'json',
			 		success : function(data) {
			 			if (data["code"] == "200") { // code==200,表示请求成功
			 				var _this =$('#btnCode');
			 			    if(_this.hasClass('c2')){}else{
			 			    	var html = _this.html();
			 			    	_this.addClass('c2').html(59+'秒后重试');
			 			    	var t =59;
			 			    	var setInterval1 = setInterval(function(){
			 			    		t--;
			 			    		_this.html(t+'秒后重试');
			 		         
			 		         if(t==0) {
			 		          	clearInterval(setInterval1);
			 		            _this.html(html).removeClass('c2').html(html);
			 		         }
			 			  },1000);
			 		    	
			 		      }
			 			} else{
			 				aler.alertMsg(data["message"]);
			 			}
			 		},
			 		error : function() {
			 			alert("发送手机验证码失败！");
			 		}
			 	});
		 }
	 }
	//短信获取
    $('#btnCode').on('click',function(){
    	var phone = $('.phoneNum').val();
  		 var isReg = false;
  		 if(phone == ''){
  			 aler.alertMsg('请输入手机号');return;
  		 }
  		getCode(phone,isReg);
    });
    var baseUrl = window.location.href;
    $('.tijiao').on('click',function(){
    	var phone = $('.phoneNum').val();
    	var code = $('.code').val();
    	var pass = $('.pass').val();
    	var inviteUid = baseUrl.split('?')[1].split('=')[1];
    	if(phone == ''){
    		aler.alertMsg('请填写手机号');return;
    	}else if(code == ''){
    		aler.alertMsg('请填写验证码');return;
    	}else if(pass == ''){
    		aler.alertMsg('请填写密码');return;
    	}else{
    		$.ajax({
    	 		url : $('#interfaceurl').val() + '/resth5/wow/register',
    	 		data : {
    	 			'code':code,
    	 			'phone' : phone,
    	 			'pass' : $.md5(pass),
    	 			'inviteUid':inviteUid
    	 		},
    	 		type : 'post',
    	 		async : false,
    	 		cache : false,
    	 		dataType : 'json',
    	 		success : function(data) {
    	 			if (data["code"] == "200") { // code==200,表示请求成功
    	 				$('.successBox').show();
    	 				//关闭按钮
    	 				$('.close').click(function(){
    	 					$(this).parent().hide();
    	 				});
    	 				$("input[type='text'],input[type='number']").val('');
    	 			} else {
    	 				aler.alertMsg(data["message"]);
    	 			}
    	 		},
    	 		error : function() {
    	 			aler.alertMsg('注册失败');
    	 		}
    	 	});
    	}
    });
    var browser={
    	    versions:function(){
    	            var u = navigator.userAgent, app = navigator.appVersion;
    	            return {         //移动终端浏览器版本信息
    	                 trident: u.indexOf('Trident') > -1, //IE内核
    	                presto: u.indexOf('Presto') > -1, //opera内核
    	                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
    	                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
    	                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
    	                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
    	                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
    	                iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
    	                iPad: u.indexOf('iPad') > -1, //是否iPad
    	                webApp: u.indexOf('Safari') > -1, //是否web应该程序，没有头部与底部
    	                isWx: u.indexOf('MicroMessenger') > -1

    	            };
    	         }(),
    	         language:(navigator.browserLanguage || navigator.language).toLowerCase()
    	}
    	 $('body').on('touchstart','.download,.dl',function(){
	        if(browser.versions.ios)
	        {
	           window.location.href = 'https://itunes.apple.com/us/app/wo-pai-wo-jiu-shi-zhu-jiao/id934254637?ls=1&mt=8';
	        }
	        if(browser.versions.android)
	        {
	            window.location.href = 'http://wopaitv.com/shengdan/myVideo_wopaichunwan_2.0.0.apk';
	        }
	        if(browser.versions.isWx)
	        {
	            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
	        }
	    });
    var aler = {
    		alertMsg: function(msg,callFn){
    			var box = $('<div class="promptBox">'+msg+'</div>');
    			$('body').append(box);
    			setTimeout(function(){
    				box.remove();
    			},1500);
    			callFn && callFn();
    		},
    		alertJamp: function(msg,url,callFn){
    			var box = $('<div class="promptBox">'+msg+'</div>');
    			$('body').append(box);
    			setTimeout(function(){
    				box.remove();
    				callFn && callFn();
    				window.location.href = url;
    			},1500);
    		}
    	};
});