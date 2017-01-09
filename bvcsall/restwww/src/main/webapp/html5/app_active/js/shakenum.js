function init(){
	if (window.DeviceMotionEvent) {
		// 移动浏览器支持运动传感事件
		window.addEventListener('devicemotion', deviceMotionHandler, false);
	}

}
var SHAKE_THRESHOLD = 10000;
// 定义一个变量保存上次更新的时间
var last_update = 0;
// 紧接着定义x、y、z记录三个轴的数据以及上一次出发的时间
var x;
var y;
var z;
var last_x;
var last_y;
var last_z;
var count = 0;

 jQuery.extend({
	 gameinit: function(){
		 _this= this;
		 _this.serviceUrl= $("#interfaceurl").val();
		 var arrObj = _this.serviceUrl.split('//');
		 var arrObj1 = arrObj[1].split('/');
		 var commonUrl = arrObj1[0];
		 _this.isShow = true;
		 _this.postIndex= _this.serviceUrl+"/restwww/lotteryDraw/index"; //活动首页接口
		 _this.postInvita= _this.serviceUrl+"/restwww/lotteryDraw/inviteFriend"; //邀请好友接口
		 _this.postDraw= _this.serviceUrl+"/restwww/lotteryDraw/doRaffle";  //抽奖动作
		 _this.postSaveUserInfo= _this.serviceUrl+"/restwww/lotteryDraw/saveUserInfo";  //保存用户信息
		 _this.backUrl= window.location.href;

		 _this.timestamp= $('#timestamp').val();
		 _this.noncestr= $('#noncestr').val();
		 _this.signature= $('#signature').val();
		 _this.shareDesc= '摇一摇，圣诞苹果免费送，你摇一个送给我吧。';
		 _this.sharePic= _this.serviceUrl+'/restwww/html5/app_active/img/apple-share.jpg';


		 //jQuery.setCookie('uid','118896');
		 //jQuery.setCookie('access_token','1f6d39ee9e666e4aede1bb4d8af69f00');
		 _this.uid= jQuery.getCookie('uid');
		 _this.access_token= jQuery.getCookie('access_token');

		 _this.params= {
			 'userId': _this.uid,
			 'uid': _this.uid
		 }
		 var skyH= $(document).height();
		 var skyW= $(document).width();
		 $('.main,.end-main,.skyLayer,.end-body').height(skyH);

		 jQuery.formFn($(".winin-phone .form-txt"));
		 jQuery.formFn($(".winin-apple .form-txt"));

		 wx.config({
			 debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			 appId: 'wx9c59e9a110527d17', // 必填，公众号的唯一标识
			 timestamp: _this.timestamp, // 必填，生成签名的时间戳
			 nonceStr: _this.noncestr, // 必填，生成签名的随机串
			 signature: _this.signature,// 必填，签名，见附录1
			 jsApiList: [
				 'checkJsApi',
				 'onMenuShareTimeline',
				 'onMenuShareAppMessage',
				 'onMenuShareQQ',
				 'onMenuShareWeibo',
				 'onMenuShareQZone',
				 'hideMenuItems',
				 'showMenuItems',
				 'hideAllNonBaseMenuItem',
				 'showAllNonBaseMenuItem',
				 'translateVoice',
				 'startRecord',
				 'stopRecord',
				 'onVoiceRecordEnd',
				 'playVoice',
				 'onVoicePlayEnd',
				 'pauseVoice',
				 'stopVoice',
				 'uploadVoice',
				 'downloadVoice',
				 'chooseImage',
				 'previewImage',
				 'uploadImage',
				 'downloadImage',
				 'getNetworkType',
				 'openLocation',
				 'getLocation',
				 'hideOptionMenu',
				 'showOptionMenu',
				 'closeWindow',
				 'scanQRCode',
				 'chooseWXPay',
				 'openProductSpecificView',
				 'addCard',
				 'chooseCard',
				 'openCard'
			 ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		 });
		 wx.ready(function(){
			 //好友
			 var shareData1 = {
				 title: '你摇苹果 我送平安',
				 desc: jQuery.shareDesc,
				 link: 'http://api.wopaitv.com/restwww/page/prize/getPrize',
				 imgUrl: jQuery.sharePic
			 };
			 //朋友圈
			 var shareData2 = {
				 title: jQuery.shareDesc,
				 //desc: jQuery.shareDesc,
				 link: 'http://api.wopaitv.com/restwww/page/prize/getPrize',
				 imgUrl: jQuery.sharePic
			 };
			 wx.onMenuShareAppMessage(shareData1);
			 wx.onMenuShareTimeline(shareData2);

		 });
		 //验证微信
		 if(_this.uid && _this.access_token){
			 jQuery.postDate(_this.postIndex,"userId="+_this.uid+"&uid="+_this.uid,function(data) {
				 if(data.code !=200){
					 window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2frestwww%2fpage%2fthirdPart%2fwechatCallback&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect';
				 }else{
					 if(data.code == 200){
						 if(data.result.raffleTime <= 2){
							 if(data.result.giftType== '1'){
								 if(data.result.sequence!=0){
									 _this.shareDesc= '我是第'+data.result.sequence+'位收到圣诞礼物的小骚年';
								 }
								 jQuery.isShow= false;
								 jQuery.wxFn();
								 return false;
								 //积分
							 }else if(data.result.giftType== '2'){
								 if(data.result.sequence!=0){
									 _this.shareDesc= '我是第'+data.result.sequence+'位收到圣诞礼物的小骚年';
								 }
								 jQuery.isShow= false;
								 jQuery.wxFn();
								 return false;
								 //水果
							 }
							 jQuery.wxFn()
						 }else{
							 $('.end-body').show();
							 jQuery.isShow= false;
						 }
					 }else{
						 $('.end-body').show();
						 jQuery.isShow= false;
					 }
				 }
			 })
		 }else{
			 window.location.href='https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2frestwww%2fpage%2fthirdPart%2fwechatCallback&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect';
		 }
		 $('.ok-box').click(function(){  //第一次摇奖关闭弹层的按钮
			 $(this).closest('.close-warp').hide();
			 jQuery.isShowFn();
		 })
		 $('.close-box').click(function(){  //所有弹层右上角关闭按钮
			 $(this).closest('.close-warp').hide();
			 _czc.push(["_trackEvent","x关闭按钮","所有弹层"]);
			 jQuery.isShowFn();
		 })

		 //各个弹层逻辑处理 略乱

		 $('.introduces-btn').click(function(){ //活动介绍弹层
			$('.introduces').css('zIndex','26').show();
		 });

		 $('.firstwin .ok-box,.firstwin .close-box').click(function(){
			 jQuery.postDate(_this.postInvita,_this.params,function(data){
				 $('.shareTips').show();
				 _czc.push(["_trackEvent","炫耀一下按钮","邀请好友"]);
			 })
		 })
		 $('.firstwin .ok-box').click(function(){
			 jQuery.postDate(_this.postInvita,_this.params,function(data){
				 $('.shareTips').show();
				 _czc.push(["_trackEvent","炫耀一下按钮","邀请好友"]);
			 })
		 })
		 $('.back-game').click(function(){  //返回游戏
			 $(this).closest('.close-warp').hide();
			 _czc.push(["_trackEvent","返回游戏按钮","返回游戏"]);
		 })
		 $('.winin-integral .complete-btn').click(function(){  //获得积分操作的弹层按钮
			 $(this).closest('.close-warp').hide();
			 $('.end-body').show();
			 jQuery.isShowFn();
			 _czc.push(["_trackEvent","积分确定按钮","活动结束"]);
		 });
		 $('body').delegate('.winin-apple .btn-ok','click',function(){
			 $(this).closest('.close-warp').hide();
			 jQuery.isShowFn();
			 var infoParams= {
				 "userId": _this.uid,
				 "uid": _this.uid,
				 "realName": $('#apple-user-name').val(),
				 "phone": $('#apple-user-phone').val(),
				 "address": $('#apple-user-address').val()
			 }
			 jQuery.postDate(_this.postSaveUserInfo,infoParams,function(data){
				 $('.winin-express').show();
				 _czc.push(["_trackEvent","获取苹果点击确定按钮","获取苹果"]);
			 })

		 });
		 $('.winin-integral .complete-btn').click(function(){  //获取积分弹层 查看更多活动
			 $(this).closest('.close-warp').hide();
			 jQuery.isShowFn();
			 $('.end-body').show();
			 _czc.push(["_trackEvent","积分打开活动结束弹层","弹层"]);
		 })
		 $('.winin-express .complete-btn').click(function(){ //打包弹层
			 $(this).closest('.close-warp').hide();
			 jQuery.isShowFn();
			 jQuery.postDate(_this.postInvita,_this.params,function(data){
				 $('.shareTips').show();
				 _czc.push(["_trackEvent","炫耀一下按钮","邀请好友"]);
			 })
		 })
		 $('#shareBtn').click(function(){   //分享弹层并送一次获奖机会
			 jQuery.postDate(_this.postInvita,_this.params,function(data){
				 $('.shareTips').show();
				 _czc.push(["_trackEvent","游戏页面邀请按钮","邀请好友"]);
			 })
		 });
		 $('.shareTips').click(function(){  //关闭分享到微信的弹层
			 $('.shareTips').hide();
		 });
		 $('.tree-head,.tips').click(function(){
			 $('.tree-head').attr('src',_this.serviceUrl+'/restwww/html5/app_active/img/tree-move.gif')
			 setTimeout(function(){
				 $('.tree-head').attr('src',_this.serviceUrl+'/restwww/html5/app_active/img/tree.png');
				 jQuery.postDate(jQuery.postDraw,jQuery.params,function(data){
					if(data.code == 200){
						 if(data.result.raffleTime <= 2){
							 if(data.result.giftType== '0') {
								 $('.firstwin').show();
								 jQuery.isShow= false;
								 return false;
								 //空的
							 }else if(data.result.giftType== '1'){
								 $('.winin-integral').show();
								 if(data.result.sequence!=0){
									 _this.shareDesc= '我是第'+data.result.sequence+'位收到圣诞礼物的小骚年';
								 }
								 console.log(_this.shareDesc)
								 jQuery.isShow= false;
								 jQuery.wxFn();
								 return false;
								 //积分
							 }else if(data.result.giftType== '2'){
								 $('.winin-apple').show();
								 if(data.result.sequence!=0){
									 _this.shareDesc= '我是第'+data.result.sequence+'位收到圣诞礼物的小骚年';
								 }
								 console.log(_this.shareDesc)
								 jQuery.isShow= false;
								 jQuery.wxFn();
								 return false;
								 //水果
							 }
							 jQuery.wxFn()
						 }else{
							 $('.end-body').show();
							 jQuery.isShow= false;
						 }
					}else{
						 $('.end-body').show();
						 jQuery.isShow= false;
					}

				 });
			 },2000);

		 })
		//底部获奖名单滚动
		 setInterval(function () {
			 jQuery.scrollNews()
		 }, 1000);
	 },
	 isShowFn: function(){
		 jQuery.isShow= true;
	 },
	 postDate: function(url,params,callBack){
		 $.ajax({
			 type: "post",
			 url: url,
			 data: params,
			 success: callBack,
			 beforeSend: function(request){
				 request.setRequestHeader('access_token',jQuery.access_token)
			 },
			 dataType: 'json'
		 })
	 },
	 formFn: function(obj){
		$(obj).keyup(function(){
			if($(obj).eq(0).val()!= ''){
				if($(obj).eq(1).val()!= ''){

					if($(obj).eq(2).val()!= ''){
						$('.complete-btn').addClass('btn-ok');

					}else{
						$('.complete-btn').removeClass('btn-ok')
					}
				}else{
					$('.complete-btn').removeClass('btn-ok')
				}
			}else{
				$('.complete-btn').removeClass('btn-ok')
			}
		})
	 },
	 wxFn: function(){
		 wx.config({
			 debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			 appId: 'wx9c59e9a110527d17', // 必填，公众号的唯一标识
			 timestamp: _this.timestamp, // 必填，生成签名的时间戳
			 nonceStr: _this.noncestr, // 必填，生成签名的随机串
			 signature: _this.signature,// 必填，签名，见附录1
			 jsApiList: [
				 'checkJsApi',
				 'onMenuShareTimeline',
				 'onMenuShareAppMessage',
				 'onMenuShareQQ',
				 'onMenuShareWeibo',
				 'onMenuShareQZone',
				 'hideMenuItems',
				 'showMenuItems',
				 'hideAllNonBaseMenuItem',
				 'showAllNonBaseMenuItem',
				 'translateVoice',
				 'startRecord',
				 'stopRecord',
				 'onVoiceRecordEnd',
				 'playVoice',
				 'onVoicePlayEnd',
				 'pauseVoice',
				 'stopVoice',
				 'uploadVoice',
				 'downloadVoice',
				 'chooseImage',
				 'previewImage',
				 'uploadImage',
				 'downloadImage',
				 'getNetworkType',
				 'openLocation',
				 'getLocation',
				 'hideOptionMenu',
				 'showOptionMenu',
				 'closeWindow',
				 'scanQRCode',
				 'chooseWXPay',
				 'openProductSpecificView',
				 'addCard',
				 'chooseCard',
				 'openCard'
			 ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		 });
		 //wx.ready(function(){
			 var shareData1 = {
				 title: '你摇苹果 我送平安',
				 desc: jQuery.shareDesc,
				 link: 'http://api.wopaitv.com/restwww/page/prize/getPrize',
				 imgUrl: jQuery.sharePic
			 };
			 //朋友圈
			 var shareData2 = {
				 title: jQuery.shareDesc,
				 //desc: jQuery.shareDesc,
				 link: 'http://api.wopaitv.com/restwww/page/prize/getPrize',
				 imgUrl: jQuery.sharePic
			 };
			 wx.onMenuShareAppMessage(shareData1);
			 wx.onMenuShareTimeline(shareData2);

		 //});
	 },
	 scrollNews: function () {
		var $news = $('.rewardList>ul');
		var $lineHeight = $news.find('li:first').height();
		$news.animate({ 'marginTop': -$lineHeight + 'px' }, 600, function () {
			$news.css({ margin: 0 }).find('li:first').appendTo($news);
		});
	},
	touchFn: function(){
		$('.tree-head').click(function(){
			$(this).addClass('cur');
		})
	 },
	 setCookie: function (cname, cvalue, exdays) {
		var d = new Date();
		d.setTime(d.getTime() + (exdays*24*60*60*1000));
		var expires = "expires="+d.toUTCString();
		document.cookie = cname + "=" + cvalue + "; " + expires;
	 },
	 //获取cookie
	 getCookie: function (cname) {
		var name = cname + "=";
		var ca = document.cookie.split(';');
		for(var i=0; i<ca.length; i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') c = c.substring(1);
			if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
		}
		return "";
	 },
	 //清除cookie
	 clearCookie: function (name) {
		setCookie(name, "", -1);
	 },
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
			 isWx: u.indexOf('MicroMessenger') > -1,
			 isQQ:u.indexOf('QQ/') > -1,
			 isQQbrowser: u.indexOf('QQBrowser'),
			 isWeiBo:u.indexOf('weibo') > -1

		 };
	 }(),
});
function deviceMotionHandler(eventData) {
	// 获取含重力的加速度
	var acceleration = eventData.accelerationIncludingGravity;

	// 获取当前时间
	var curTime = new Date().getTime();
	var diffTime = curTime -last_update;
	// 固定时间段
	if (diffTime > 100) {
		last_update = curTime;

	x = acceleration.x;
	y = acceleration.y;
	z = acceleration.z;
	var speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 30000;

	if (speed > SHAKE_THRESHOLD) {
			jQuery.isShow = true;
			if($(".close-warp").is(":visible"))
			{
				jQuery.isShow = false;
			}
		    // TODO:在此处可以实现摇一摇之后所要进行的数据逻辑操作
			count++;
			var px = count*19;
			var text = count*8;

			$('.tree-head').attr('src',_this.serviceUrl+'/restwww/html5/app_active/img/tree-move.gif');
			setTimeout(function(){
				$('.tree-head').attr('src',_this.serviceUrl+'/restwww/html5/app_active/img/tree.png');
				if(jQuery.isShow)
				{
					jQuery.postDate(jQuery.postDraw,jQuery.params,function(data){
						if(data.code == 200) {
							if (data.result.raffleTime <= 2) {
								if (data.result.giftType == '0') {
									$('.firstwin').show();
									jQuery.isShow = false;
									return false;
									//空的
								} else if (data.result.giftType == '1') {
									$('.winin-integral').show();
									if(data.result.sequence!=0){
										jQuery.shareDesc= '我是第'+data.result.sequence+'位收到圣诞礼物的小骚年';
									}
									jQuery.isShow = false;
									document.title= jQuery.shareDesc;
									jQuery.wxFn();
									return false;
									//积分
								} else if (data.result.giftType == '2') {
									$('.winin-apple').show();
									if(data.result.sequence!=0){
										jQuery.shareDesc= '我是第'+data.result.sequence+'位收到圣诞礼物的小骚年';
									}
									document.title= jQuery.shareDesc
									jQuery.isShow = false;
									jQuery.wxFn();
									return false;
									//水果
								}

							} else {
								$('.end-body').show();
								jQuery.isShow = false;
							}
						}else{
							$('.end-body').show();
							jQuery.isShow= false;
						}
					});
				}
			},3000)

	}

			//$(".tree-head").removeClass('cur');
			//


		last_x = x;
		last_y = y;
		last_z = z;
	}
}