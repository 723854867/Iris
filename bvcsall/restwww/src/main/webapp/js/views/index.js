requirejs.config({
	paths:{
		jquery: '../libs/jquery-2.1.4',
		info:'../common/info',
		aler:'../common/aler'
	}
});
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','head'], function(jquery,info,aler,service,head) {

var serverUrl = info.getPicPerfix()
var interUrl = info.getRequestPrefix();
var uid = info.getCookie('uid');

var indexFn = {
	init: function(){
		_this = this;
		var navW = $('.nav ul');
		var navLi = $('.nav ul li');
		var _czc = _czc || [];
		_this.isNext= 0;
		_this.nextType= 1;
		_this.dataBox = $('.videoList ul');
		_this.morePage = 1;
		_this.count = 20;
		_this.clear = true;
		_this.actId = $('.nav ul li').eq(0).attr('data-id');
		
		var url = window.location.hash;
		if(url!=''){
			var channel = url.split('=');
			info.setCookie('channel',channel[1],1);
		}else{
			//info.clearCookie('channel');
		}
		var oWidth = $(window).width();
		var oHeight = $(window).height();
		$('.listScroll').height(oHeight-47-54);
		$('.listScroll').css('background-color','#ffffff');
		if(oWidth >= 640){
			$('.videoList ul li a').css('height','320px');
		}else{
			$('.videoList ul li a').css('height',oWidth/2);
		}
		navLi.first().find('a').addClass('on');
		$('.dropTabList .activeItem').first().find('em').addClass('on');
		navW.width(indexFn.StatWidth(navLi));
		info.playNum($('.playNumVal'));
		info.videoInfo(serverUrl,$('.videoPic'));
		info.personalPortrait(serverUrl,$('.userImg'));
		indexFn.changeShare();
		_this.onId= $('.navList>li .on').parent().attr('data-id');


		//懒加载图片
		$(window).scroll(function(){
			info.lazyPicLoad($('.videoPic'));
			info.lazyPicLoad($('.userImg'));
		});

		$('.closeIcon').click(function(){
			$('.dropTabList').hide();
			$('.videoList').show();
		});
		$('.dropArrow').click(function(){
			$('.dropTabList').show();
			$('.videoList').hide();
			
		});
		$('.nav li').on('click',function(){
			indexFn.activeSwich($(this));
		});

		$('.activeItem').on('click',function(){
			$('.nav').scrollLeft(0);
			indexFn.activeSwich($(this));
			var liIndex = $(this).index();
			var thisLeft = $('.nav ul li').eq(liIndex).position().left;
			$('.nav').scrollLeft(thisLeft)
			$('.dropTabList').hide();
			$('.videoList').show();
		});

		$('.videoList').delegate('.userPho a','click',function(){
			var userId = $(this).attr('data-userId');
			indexFn.goPage('userId',userId);
		});
		$('.videoList').delegate('.govideoShow','click',function(){
			var videoId = $(this).attr('video-Id');
			indexFn.goPage('video',videoId);
		});
		//加载更多
		$('#btnMore').on('click',function(){
			_this.morePage += 1;
			_this.clear = false;
			service.getActive(_this.count,_this.actId,_this.morePage).done(function(json){
				indexFn.indexList(json.result,_this.dataBox,_this.clear);
			});
		});
	},
	changeShare: function(){
		service.CustomShare('1').done(function(json){
			if(json.result != ''){
				document.title=json.result.shareText;
				var oimg= $('<img class="sharePic" src="'+(serverUrl+json.result.shareImg)+'" width="0" height="0"/>');
				$('body').prepend(oimg)
			}else{
				return;
			}
		})
	},
	activeSwich: function(obj){
		actId = indexFn.onId= indexFn.actId = $(obj).attr('data-id');
		$('.nav ul li,.activeItem').each(function(){
			if($(this).attr('data-id')==actId){
				$(this).children('.itemSele').addClass('on');
				$(this).siblings().find('.itemSele').removeClass('on');
			}
		});
		$(obj).children('.itemSele').addClass('on');
		$(obj).siblings().find('.itemSele').removeClass('on');
		indexFn.morePage = 1;
		indexFn.clear = true;;
		service.getActive(indexFn.count,actId,indexFn.morePage).done(function(json){
			indexFn.indexList(json.result,indexFn.dataBox,indexFn.clear);
		});
	},
	goPage: function(type,typeId){
		var url= ''
		var params= {};
		if(type == 'video'){
			url= interUrl+"/page/video/videoDetail?videoId="+typeId;
			params= {
				uid: uid,
				isNext: indexFn.isNext,
				nextType: indexFn.nextType,
				activityId: indexFn.onId
			}
		}else if(type == 'userId'){
			url= interUrl+'/page/user/userDetail?userId='+typeId;
			params= {
				uid: uid
			}
		}
		info.postform(url,params)
	},
	//小导航自定义宽度
	StatWidth: function(obj){
		var widthAll =0;
		var wArr = [];
		obj.each(function(m,n){
			wArr.push(parseInt($(this).width())+5)
		})
		for(var i=0;i<wArr.length;i++){
			widthAll+=parseFloat(wArr[i])
		}
		return widthAll;
	},
	indexList: function(json,target,clear){
		var data = json;
		if(clear){
			$(target).empty();
		};
		if(data == ''){
			aler.alertMsg('没有数据了！');
		};
		$.each(data,function(m,n){
			var vStart;
			if(n.user.vipStat == 1){
				vStart= '<em class="userV1"></em>';

			}else if(n.user.vipStat == 2){
				vStart= '<em class="userV2"></em>';
			}else if(n.user.vipStat == 3){
				vStart= '<em class="userV3"></em>';
			}else{
				vStart= '';
			}
			var dataList = '<li>'+
				'<a href="javascript:;" class="govideoShow" video-Id="'+n.id+'" data-userId="'+ n.user.id +'"><img class="videoPic" data-videoPic="'+n.videoPic+'" src="" lazy_src="" onerror="this.src=\'/restwww/img/portrait.png\'">'+
				'<div class="filtlayer"></div>'+
				'<p class="videoMsg">'+n.description+'</p></a>'+
				'<span class="userPho">'+
				'<a href="javascript:;" data-userId="'+ n.user.id +'"><img class="userImg" data-userPic="'+n.user.pic+'" src="" lazy_src="" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
				vStart+
				'</a>'+
				'</span>'+
				'<span class="playData"><em></em><span class="playNumVal">'+n.showPlayCount+'</span></span>'+
				'</li>';

			target.append(dataList);
			info.playNum($('.playNumVal'));
			info.videoInfo(serverUrl,$('.videoPic'))
			info.personalPortrait(serverUrl,$('.userImg'),true);


		});
	},

}

indexFn.init();
})