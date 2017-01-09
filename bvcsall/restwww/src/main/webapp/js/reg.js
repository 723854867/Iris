var authorization_code = ""; // 登陆授权码
var access_token = ""; // 登陆token
var uid; // 登陆用户id，用户唯一标识


//url前缀-----测试环境
var requestUrlPrefix = "http://127.0.0.1:8080/restwww"; // 请求url前缀
var videoUrlPrefix = "http://192.168.108.160:63880/hls/play/"; // 视频url前缀
var picUrlPrefix = "http://192.168.108.160:8080/restadmin/download"; // 图片资源url前缀

//url前缀-----正式环境
//var requestUrlPrefix = "http://api.wopaitv.com/restwww"; // 请求url前缀
//var videoUrlPrefix = "http://cdn.wopaitv.com/hls/play/"; // 视频url前缀
//var picUrlPrefix = "http://api.wopaitv.com/restwww/download"; // 图片资源url前缀


$(document).ready(function(){
	$("#getCodeBtn").unbind("click");
	$("#regBtn").unbind("click");

	$("#getCodeBtn").click(function (){
		getCode($("#username").val(),true);
	});

	$("#regBtn").click(function (){
		reg($("#username").val(),$("#password").val(),$("#code").val());
		//注册并登陆成功，打印token
		alert(access_token);
	});
});

// 发送手机验证码
function getCode(phone, isReg) {
	$.ajax({
		url : requestUrlPrefix+'/user/getCode',
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
				
			} else {
				alert(data["message"]);
			}
		},
		error : function() {
			alert("发送手机验证码失败！");
		}
	});
}

// 用户注册
function reg(phone, password,code) {
	$.ajax({
		url : requestUrlPrefix+'/user/reg',
		data : {
			'phone' : phone,
			'pass' : $.md5(password),
			'code' : code
		},
		type : 'post',
		async : false,
		cache : false,
		dataType : 'json',
		success : function(data) {
			if (data["code"] == "200") { // code==200,表示请求成功
				getAuthorization_code(phone,password);
				getAccess_token();
			} else {
				alert(data["message"]);
			}
		},
		error : function() {
			alert("注册失败！");
		}
	});
}


// 获得用户登陆授权码
function getAuthorization_code(username, password) {
	$.ajax({
		url : requestUrlPrefix+'/authorize',
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
		success : function(data) {
			if (data["code"] == "200") { // code==200,表示请求成功
				//alert(data["result"]["authorization_code"]);
				authorization_code = data["result"]["authorization_code"];  //获得登陆授权码
			} else {
				alert(data["message"]);
			}
		},
		error : function() {
			alert("获得登陆授权码失败！");
		}
	});
}

// 获得用户登陆token
function getAccess_token() {
	$.ajax({
		url : requestUrlPrefix+'/accessToken',
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
			} else {
				alert(data["message"]);
			}
		},
		error : function() {
			alert("获得登陆token失败！");
		}
	});
}

// 根据热度指数查询视频（首页视频列表）
function getHotIndiceVideos(startIndex,count) {
	$.ajax({
		url : requestUrlPrefix+'/video/findHotIndiceVideos',
		data : {
			'startIndex' : startIndex,
			'count' : count,
			'activityId' : 0
		},
		type : 'post',
		cache : false,
		dataType : 'json',
		success : function(data) {
			if (data["code"] == "200") {
				$.each(data["result"],function(){
					playVideo(this["id"],this["playKey"],this["videoPic"]);
				});
			} else {
				alert(data["message"]);
			}
		},
		error : function() {
			alert("获取资源失败！");
		}
	});
}

// 获得用户信息
function getUserInfo() {
	$.ajax({
		url : requestUrlPrefix+'/userInfo',
		data : {
			'access_token' : access_token
		},
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			if (data["code"] == "200") {
				alert(data["result"]["name"]);
			} else {
				alert(data["message"]);
			}
		},
		error : function() {
			alert("获取资源失败！");
		}
	});
}

// 播放视频
function playVideo(videoId,playKey,img){
	var _player = null;
	
	var player = $('<div/>');
	$(player).attr('id', 'pl'+videoId);
	
	$('#playerContainer').append(player);
	var conf = {
		file: videoUrlPrefix+playKey+'.m3u8',
		image: picUrlPrefix+img,
		height: 200,
		width: 300,
		autostart: false,
		repeat:true,
		analytics: { enabled: false}
	};
	_player = jwplayer('pl'+videoId).setup(conf);
}