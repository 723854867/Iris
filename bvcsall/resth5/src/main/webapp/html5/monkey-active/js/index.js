function init(){
	if (window.DeviceMotionEvent) {
		// 移动浏览器支持运动传感事件
		window.addEventListener('devicemotion', deviceMotionHandler, false);
	}
// liveOpenWopai
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
// _czc.push(["_trackEvent","积分确定按钮","活动结束"]);
 jQuery.extend({
	gameinit: function(){
		 _this= this;
		 _this.resultKey= null;
		 _this.serviceUrl= $("#interfaceurl").val();


		if(!jQuery.versions.isWx){
			$('body').html("请在微信中打开！").css({
				'textAlign':'center',
				'color':'#fff'
			})
		}
		 _this.arrDesc=[
			 '2016猴年运势：我缺少性生活',
			 '2016猴年运势：我缺乏新鲜感',
			 '2016猴年运势：我缺少肺活量',
			 '2016猴年运势：我缺乏安全感',
			 '2016猴年运势：我缺少回头率',
			 '2016猴年运势：我缺少脑容量'
		 ]
		 _this.isShow = true;
		 _this.backUrl= window.location.href;

		 _this.timestamp= $('#timestamp').val();
		 _this.noncestr= $('#noncestr').val();
		 _this.signature= $('#signature').val();
		 _this.shareDesc= '摇一摇，猴运来';
		 _this.sharePic= _this.serviceUrl+'/restwww/html5/monkey-active/img/share-icon.jpg';


		 //_this.skyH= $(document).height()> 520?$(document).height():550;
		 _this.skyH= $(document).height();
		 _this.skyW= $(document).width();
		 $('.skyBox,.result-1>div').height(_this.skyH);
		 $('.bg-img').css({
		 	'height':_this.skyH

		 })

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
				'onMenuShareQZone'
			] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
		 wx.ready(function(){
			 //好友
			 var shareData1 = {
				 title: '测2016猴年运势',
				 desc: jQuery.shareDesc,
				 link: _this.backUrl,
				 imgUrl: jQuery.sharePic
			 };
			 //朋友圈
			 var shareData2 = {
				 title: '测2016猴年运势',
				 //desc: jQuery.shareDesc,
				 link: _this.backUrl,
				 imgUrl: jQuery.sharePic
			 };
			 wx.onMenuShareAppMessage(shareData1);
			 wx.onMenuShareTimeline(shareData2);
		 });
		 wx.error(function (res) {
			alert(res.errMsg);  //打印错误消息。及把 debug:false,设置为debug:ture就可以直接在网页上看到弹出的错误提示
		 });
		$('.draw1,.monkey-tips').one('click',function(){

			$('.draw1').hide();
			$('.draw2').show();
			setTimeout(function(){
				$('.draw1').show();
			    $('.draw2').hide();
			    $('.stick-warp').show().addClass('skyBox');
			    $('.skyBox').css('height',_this.skyH);
			    jQuery.animateFn($('.stick'));
			},2000);

			jQuery.isShow= false;
		})
	 },
	 animateFn: function(obj){
		$('.audio2').get(0).play();
	 	obj.css('display','block');
		 console.log(jQuery.isShow)
	 	obj.animate({
	 		'top': '20px',
	 		'width': '60px',
	 		'margin-left': '-30px'
	 	},500,function(){
	 		obj.addClass('rotat');
	 		jQuery.resultFn(obj);
	 	})
	 },
	 // 点击金箍棒出现结果
	 resultFn: function(obj){

	 	$(obj).click(function(){
	 		$('.stick-warp').hide();
	 		jQuery.resultKey= parseInt(Math.random()*6,10);
			console.log(jQuery.resultKey)
			var shareResult = {
				title: '测2016猴年运势',
				desc: jQuery.arrDesc[jQuery.resultKey],
				link: _this.backUrl,
				imgUrl: jQuery.sharePic
			};
			jQuery.isShow= false;
	 		switch(jQuery.resultKey){
	 			case 0:
	 			$('.result-1-1').show();
	 			break;
	 			case 1:
	 			$('.result-1-2').show();
	 			break;
	 			case 2:
	 			$('.result-1-3').show();
	 			break;
	 			case 3:
	 			$('.result-1-4').show();
	 			break;
	 			case 4:
	 			$('.result-1-5').show();
	 			break;
	 			case 5:
	 			$('.result-1-6').show();
	 			break;
	 			case 6:
	 			$('.result-1-6').show();
	 			break;
	 		}
			wx.onMenuShareAppMessage(shareResult);
			wx.onMenuShareTimeline({
				title: jQuery.arrDesc[jQuery.resultKey],
				//desc: jQuery.shareDesc,
				link: _this.backUrl,
				imgUrl: jQuery.sharePic,
				trigger: function (res) {
					// 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
					//alert('用户点击分享到朋友圈');
				},
				success: function (res) {
					$('.share-tips').hide();
					//$('.result-1 div').hide();
					//document.title= jQuery.arrDesc[jQuery.resultKey]
					//switch(jQuery.resultKey){
					//	case 0:
					//		$('.result-2-1').show();
					//		break;
					//	case 1:
					//		$('.result-2-2').show();
					//		break;
					//	case 2:
					//		$('.result-2-3').show();
					//		break;
					//	case 3:
					//		$('.result-2-4').show();
					//		break;
					//	case 4:
					//		$('.result-2-5').show();
					//		break;
					//	case 5:
					//		$('.result-2-6').show();
					//		break;
					//}
				},
				cancel: function (res) {
					//alert('已取消');
				},
				fail: function (res) {
					//alert(JSON.stringify(res));
				}
			});
	 	});
		 $('.share-btn').click(function(){
			 $('.share-tips').show();
			 $('.share-tips').click(function(){
				 $(this).hide();
			 });
		 })
	 	jQuery.getresult($('.result-btn'));
	 },
	 getresult: function(obj){
	 	$(obj).click(function(){
			$('.result-2>div').height(jQuery.skyH);
			$('.result-1 div').hide();
			//alert(jQuery.arrDesc[jQuery.resultKey])
			switch(jQuery.resultKey){
				case 0:
					$('.result-2-1').show();
					break;
				case 1:
					$('.result-2-2').show();
					break;
				case 2:
					$('.result-2-3').show();
					break;
				case 3:
					$('.result-2-4').show();
					break;
				case 4:
					$('.result-2-5').show();
					break;
				case 5:
					$('.result-2-6').show();
					break;
			}
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
			if(jQuery.isShow){
				$('.draw1').hide();
				$('.draw2').show();
				jQuery.isShow= false;
				count++;
				var px = count*19;
				var text = count*8;
				setTimeout(function(){
					//alert(jQuery.isShow);
					$('.draw1').show();
					$('.draw2').hide();
					$('.stick-warp').show().height(jQuery.skyH).addClass('skyBox')
					jQuery.animateFn($('.stick'));
					jQuery.isShow= false;
				},2000);
			}

		}
		last_x = x;
		last_y = y;
		last_z = z;
	}
}