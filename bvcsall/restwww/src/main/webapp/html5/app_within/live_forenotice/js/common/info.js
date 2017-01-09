
require.config({
	shim:{
		'../libs/jquery.qqFace':['../libs/jquery-2.1.4']
	}
});
define(['../libs/jquery-2.1.4','../libs/jquery.qqFace','../common/share','../common/playbox2','../libs/md5Utils','../common/newAlert'],function(jquery,face,share,playbox,md5Utils,alert){
	var a = 0;
	
	var infos = {
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
		 
		getRequestPrefix: function(){
			return $('#interfaceurl').val()+"/restwww";
		},
		getVideoPrefix: function(){
			return $('#serverUrlvid').val()+"/hls/play/";
		},
		getPicPerfix: function(){
			return $('#serverUrlimg').val()+"/restwww/download";
		},
		
		//用户头像地址
		personalInfo:function(pichost,obj){
			 var userBgsrc = $(obj).attr('data-homePicSrc');
	         var personalPoster = infos.getPicPerfix()+userBgsrc;
	         $(obj).attr('src',personalPoster);
		},
		//用户头像地址
		personalPortrait:function(pichost,obj,isTrue){
			$(obj).each(function(){
				var userPortrait = $(this).attr('data-userPic');
				var userPortraitUrl = infos.getPicPerfix()+userPortrait;
				if($(this).closest('li').length >0 ){
					$(this).attr('lazy_src',userPortraitUrl);
					infos.lazyPicLoad($(this));
				}else{
					$(this).attr('src',userPortraitUrl);
				}
			})
		},
		//视频封面地址
		videoInfo:function(pichost,obj){
			 $(obj).each(function(){
				var videoPic = $(this).attr('data-videoPic');
				var videoPoster = infos.getPicPerfix() + videoPic;

				if($(this).closest('li').length >0 ){
					$(this).attr('lazy_src',videoPoster);
					infos.lazyPicLoad($(this));
				}else{
					$(this).attr('src',videoPoster);
				}

			}); 
		},
		//视频文件地址
		videoUrl:function(videohost){
			$('body').on('touchstart','.canPlay',function(){
				var videoKey = $(this).parent().attr('data-videoKey');
				var videoId = $(this).parent().attr('data-videoId');
				var videourl = infos.getVideoPrefix()+videoKey+".mp4";
				playbox.playV(videourl,$(this));
				var params= {
					id: videoId,
					c: 1
				}
				$(this).off('touchstart');
				$.ajax({
					type: 'post',
					url: infos.getRequestPrefix()+'/video/incCount',
					data: params,
					success:function(msg){
					},
					error:function(){
					}
				})
				//event.preventDefault();
			})
			$('body').on('click','.videoPic',function(){
				infos.videoPlay($(this));
			})
		},
		videoPlay: function(obj){
			var videoKey = obj.parent().attr('data-videoKey');
			var videoId = obj.parent().attr('data-videoId');
			var videourl = infos.getVideoPrefix()+videoKey+".mp4";
			playbox.playV(videourl,obj);
			var params= {
				id: videoId,
				c: 1
			}
			obj.off('touchstart');
			$.ajax({
				type: 'post',
				url: infos.getRequestPrefix()+'/video/incCount',
				data: params,
				success:function(msg){
				},
				error:function(){
				}
			})
		},
		//时间转化函数
		timeTransform:function(obj,attrs,format){
			var myDate = $(obj);
			myDate.each(function(){
				var de = $(this).attr(attrs);
				var newDate = new Date();
				newDate.setTime(de);
				Date.prototype.format = function(format) {
			       var date = {
			              "M+": this.getMonth() + 1,
			              "d+": this.getDate(),
			              "h+": this.getHours(),
			              "m+": this.getMinutes(),
			              "s+": this.getSeconds(),
			              "q+": Math.floor((this.getMonth() + 3) / 3),
			              "S+": this.getMilliseconds()
			       };
			       if (/(y+)/i.test(format)) {
			              format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
			       }
			       for (var k in date) {
			              if (new RegExp("(" + k + ")").test(format)) {
			                     format = format.replace(RegExp.$1, RegExp.$1.length == 1
			                            ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
			              }
			       }
			       return format;
			}
				var now = new Date().getTime();
				var needTime = newDate.format(format);
				var timeDifference = now - de;
				if(timeDifference > 2*24*60*60*1000 && timeDifference < 365*24*60*60*1000){
					$(this).text(needTime);
				}else if(timeDifference<2*24*60*60*1000 && timeDifference > 1*24*60*60*1000){
					$(this).text('昨天'+needTime.split(' ')[1]);
				}else if(timeDifference<24*60*60*1000 && timeDifference > 60*60*1000){
					$(this).text(parseInt(timeDifference/1000/60/60)+'小时前');
				}else if(timeDifference<60*60*1000 && timeDifference > 60*1000){
					$(this).text(parseInt(timeDifference/1000/60)+'分钟前');
				}else if(timeDifference<60*1000){
					$(this).text('刚刚');
				}else if(timeDifference > 365*24*60*60*1000){
					var ti = new Date(parseInt(timeDifference)).toLocaleString().replace(/:\d{1,2}$/,' ').split(' ')[3];
					$(this).text(ti+'年 '+needTime);
				}
			});
		},
		// 获得用户登陆授权码
		getAuthorization_code:function(username, password){
			$.ajax({
		 		url : infos.getRequestPrefix()+'/authorize',
		 		data : {
		 			'response_type' : 'code',
		 			'client_id' : 'c1ebe466-1cdc-4bd3-ab69-77c3561b9dee',
		 			'username' : username,
		 			'password' : md5Utils.md5(password)
		 		},
		 		type : 'post',
		         async : false,
		 		cache : false,
		 		dataType : 'json',
		 		success : function(data) {
		 			if (data["code"] == "200") { // code==200,表示请求成功
		 				authorization_code = data["result"]["authorization_code"];  //获得登陆授权码
		 				infos.getAccess_token();
		 			} else {
						alert.alertMsg(data["message"]);
		 			}
		 		},
		 		error : function() {
		 			alert.alertMsg("获得登陆授权码失败！");
		 		}
		 	});
		},
		// 获得用户登陆token
		getAccess_token:function(){
			$.ajax({
		 		url : infos.getRequestPrefix()+'/accessToken',
		 		data : {
		 			'grant_type' : 'authorization_code',
		 			'code' : authorization_code,
		 			'client_id' : 'c1ebe466-1cdc-4bd3-ab69-77c3561b9dee',
		 			'client_secret' : 'd8346ea2-6017-43ed-ad68-19c0f971738b',
		 			'redirect_uri' : 'url',
		 		},
		 		type : 'post',
		         async : false,
		 		cache : false,
		 		dataType : 'json',
		 		success : function(data) {
		 			if (data["code"] == "200") {
		 				//alert(data["result"]["access_token"]);
		 				access_token = data["result"]["access_token"];  //获得登陆token
		 				infos.setCookie("access_token",access_token,7);
		 				redirectFlag = true;
		 				infos.getUserInfo();
		 			} else {
						alert.alertMsg(data["message"]);
		 			}
		 		},
		 		error : function() {
					alert.alertMsg("获得登陆token失败！");
		 		}
		 	});
		},
		// 获得用户信息
		getUserInfo:function(){
			$.ajax({
		 		url : infos.getRequestPrefix()+'/userInfo',
		 		data : {
		 			'access_token' : access_token
		 		},
		 		type : 'get',
		 		cache : false,
		 		dataType : 'json',
		 		success : function(data) {
		 			if (data["code"] == "200") {
		 				infos.setCookie("uid",data["result"]["id"],7);
		 				var uid = infos.getCookie('uid');
		 				var access_token = infos.getCookie('access_token');
		 				var backurl=document.referrer;
	 			 		infos.setCookie('backurl',backurl,7);
	 			 		var backurl = decodeURIComponent(infos.getCookie('backurl'));
		 				if (a==1) {
		 					if(backurl.indexOf('live') > 0){
		 						location.href=backurl;
		 					}else{
		 						location.href=infos.getRequestPrefix() + "/page/user/getRecommendUser?" + "uid=" + uid + '&access_token=' + access_token;
		 					}
		 			 	}else{
		 			 		if(backurl=='' || backurl==infos.getRequestPrefix()+'/page/user/login'){
		 			 			location.href=infos.getRequestPrefix() + "/page/homePage";
		 			 		}else if(backurl==infos.getRequestPrefix()+'/page/homePage' || backurl==infos.getRequestPrefix()+'/page/rank'){
		 			 			location.href=backurl;
		 			 		}else if(backurl.indexOf('live') > 0){
//		 			 			if(infos.getCookie('login')=='on'){
//		 			 				infos.setCookie('sign','aaa');
//		 			 				infos.goPage('live',infos.getCookie('backurl').split('?')[1].split('=')[1]);
//			 			 			
//		 			 			}else{
		 			 				infos.setCookie('sign','bbb');
		 			 				infos.goPage('live',infos.getCookie('backurl').split('?')[1].split('=')[1]);
//			 			 			
//		 			 			}
//		 			 			if(infos.getCookie('login')=='on'){
//		 			 				infos.setCookie('sign','aaa');
//		 			 				infos.goPage('live',infos.getCookie('backurl').split('?')[1].split('=')[1]);
//			 			 			
//		 			 			}
		 			 		}else{
								var params= {
									uid: uid,
									isNext: infos.getCookie('isNext'),
									nextType: infos.getCookie('nextType')
								}
		 			 			//location.href=backurl+'&uid='+uid;
								infos.postform(backurl,params);
		 			 		}
		 			 	}
		 				alert.alertMsg('登录成功');
		 			} else {
						alert.alertMsg(data["message"]);
		 			}
		 		},
		 		error : function() {
					alert.alertMsg("获取资源失败！");
		 		}
		 	});
		},
		login:function(username,password){
			infos.getAuthorization_code(username,password);
		},
		 // 用户注册
		 reg:function(phone, password,code,regPlatform){
//			 infos.getAuthorization_code(phone,password);
		 	$.ajax({
		 		url : infos.getRequestPrefix()+'/user/reg',
		 		data : {
		 			'phone' : phone,
		 			'pass' : md5Utils.md5(password),
		 			'code' : code,
		 			'regPlatform':regPlatform
		 		},
		 		type : 'post',
		 		async : false,
		 		cache : false,
		 		dataType : 'json',
		 		success : function(data) {
		 			if (data["code"] == "200") { // code==200,表示请求成功
		 				infos.getAuthorization_code(phone,password);
		 				a = 1;
		 			} else {
		 				alert.alertMsg(data["message"]);
		 			}
		 		},
		 		error : function() {
		 			alert.alertMsg("注册失败！");
		 		}
		 	});
		 },
		 // 发送手机验证码
		 getCode:function(phone, isReg){
			 function time(o,wait) {
			 		if (wait == 0) {
			 			o.removeAttribute("disabled");
			 			o.value="重新获取";
			 			$('#getCodeBtn').removeClass('btnCode1').addClass('btnCode');
			 			wait = wait;
			 		} else {
			 			o.setAttribute("disabled", true);
			 			o.value=wait +'s';
			 			wait--;
			 			setTimeout(function() {
			 				time(o,wait)
			 			},
			 			1000)
			 		}
			 	};
		 	$.ajax({
		 		url : infos.getRequestPrefix()+'/user/getCode',
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
		 				var btnCode = document.getElementById("getCodeBtn");
		 					time(btnCode,60);
		 					$('#btnCode').attr("disabled", true);
		 					$('#getCodeBtn').removeClass('btnCode').addClass('btnCode1');
		 				
		 			} else if(data["code"] == "336") {
		 				alert.alertMsg(data["message"]);
		 				
		 			} else if(data["code"] == "301") {
		 				alert.alertMsg(data["message"]);
		 				
		 			}
		 		},
		 		error : function() {
		 			alert.alertMsg("发送手机验证码失败！");
		 		}
		 	});
		 },
		 Sub:function(phone,code,password){
				if (password==''){
				alert.alertMsg("请输入密码！");
			} else {
				$.ajax({
					url : infos.getRequestPrefix()+'/user/resetPass',
					data : {
						'phone' : phone,
						'code' : code,
						'pass':md5Utils.md5(password)
					},
					type : 'post',
					async : false,
					cache : false,
					dataType : 'json',
					success : function(data) {
						if (data["code"] == "200") { // code==200,表示请求成功
							alert.alertMsg('修改密码成功');
							setInterval(function(){
								window.location.href="info.getRequestPrefix()+'/page/user/login";
							},1000);
							
						} else {
							alert.alertMsg(data["message"]);
						}
					},
					error : function() {
						alert.alertMsg("发送手机验证码失败！");
					}
				});
			}
		 },
		 tabLove:function(self,zanNum){//点击红心切换
			 if($(self).hasClass('on')){
 				$(self).removeClass('on');
 				$(self).addClass('off');
 				$(self).next().text(zanNum-1);
 			}else{
 				$(self).removeClass('off');
 				$(self).addClass('on');
 				$(self).next().text(zanNum+1);
 			}
		 },
		postform: function(URL, PARAMS) {  //隐藏uid跳转页面
			var temp = document.createElement("form");
			temp.action = URL;
			temp.method = "post";
			temp.style.display = "none";
			for (var x in PARAMS) {
				var opt = document.createElement("textarea");
				opt.name = x;
				opt.value = PARAMS[x];
				// alert(opt.name)
				temp.appendChild(opt);
			}
			document.body.appendChild(temp);
			temp.submit();
			return temp;
		},
		lazyPicLoad: function(obj){
			var scrollTop= $(document).scrollTop();
			var skyH= $(window).height();
			$(obj).each(function(){
				var imgTop= $(this).closest('li').offset().top;
				if(imgTop < scrollTop+skyH){
					$(this).attr('src',$(this).attr('lazy_src'))
				}
			})
		},
		faceFn: function(ele){
			$(ele).each(function(m,n){
				$(n).replace_face($(this).html());
			})

		},
		scrollTop:function(){
			var shtml = '<div class="scrollTop"></div>';
			$('body').append(shtml);
			$('.scrollTop').hide();
			$(window).scroll(function(){
				var scrolltop = $(window).scrollTop();
				if(scrolltop > 500){
					$('.scrollTop').show();
				}
				if(scrolltop < 500){
					$('.scrollTop').hide();
				}
				$('.scrollTop').on('click',function(){
					$(window).scrollTop(0);
				})
			})
		},
		playNum: function(ele){

			$(ele).each(function(m,n){
				var txt = $(this).text();
				var num;
				if(txt > 10000){

					num= (txt/10000);
					num= num.toFixed(2)
					$(this).text(num+'万');
				}else{
					$(this).text(num);
				}
			});
		},
		goPage: function(type,typeId){
			var interUrl = infos.getRequestPrefix();
			var uid = infos.getCookie('uid');
			var access_token = infos.getCookie('access_token');
			var url= ''
			var params= {};
			if(type == 'video'){
				url= interUrl+"/page/video/videoDetail?videoId="+typeId
				params= {
					uid: uid,
					isNext: activeFn.isNext,
					nextType: activeFn.nextType
				}
			}else if(type == 'userId'){
				url= interUrl+'/page/user/userDetail?userId='+typeId;
				params= {
					uid: uid
				}
			}else if(type== 'live'){
				url= interUrl+'/page/live/shareLive?roomId='+typeId;
				params= {
					uid: uid,
					access_token:access_token
				}
			}
			infos.postform(url,params);
		}
	}
	
	return infos;
});