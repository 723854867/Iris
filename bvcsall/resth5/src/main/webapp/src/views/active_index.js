
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','../common/share','head','../common/browse'], function(jquery,info,alert,service,share,head,browse) {


var serverUrl = info.getPicPerfix()
var interUrl = info.getRequestPrefix();
var uid = info.getCookie('uid');

var actId = $('#actId').val();
var activeTit= $('.active-tit').text();
var activeFn = {
	init: function(){
		var page= 1;
		var latest = $('.latest');
		var hot = $('.hot');
		var hotContent = $('.hotContent');
		var latestContent = $('.latestContent');
		activeFn.swiperFn(latest,hot,hotContent,latestContent);
		_this=this;

		_this.size = 10;
		_this.startIndex=0;
		_this.isNext= 0;
		_this.nextType= 2;
		_this.type = 'active'; //默认加载活动视频列表
		_this.dataBox = $('.videoList1 ul');
		activeFn.jump();
		info.personalPortrait(serverUrl,$('.actIndexCover'));
		$(window).scroll(function(){
			info.lazyPicLoad($('.playBox img'));
			info.lazyPicLoad($('.actvideoPic'));
		});
		$(".active-desc-arrow").click(function(){
			$(".activeMsgCon").toggleClass("drop-desc");
			$(this).toggleClass("arrrow-up");
		});
		service.getActiveNewList(10,actId,1).done(function(json){
			var target = $('.latestContent ul');
			activeFn.activeList(json.result,target,true);
		});
		service.getActiveHotList(_this.startIndex,_this.size ,actId).done(function(json){
			var target = $('.hotContent ul');
			if(json.result==''){
				alert.alertMsg('没有更多数据！');
				return;
			}
			activeFn.activeList(json.result,target,false);
		});
		$('.canyu a').on('click',function(){
			window.location.href = 'http://wopaitv.com';
		});
		$(window).scroll(function(){
			if($(window).scrollTop()>=$('.head').height()+$('.activeIndex').height()+8){
				$('.videoListSort').css({
					'position':'fixed',
					'top':'0',
					'z-index':'99'
				});
			}else{
				$('.videoListSort').css({
					'position':'inherit',
					'top':'0',
					'z-index':'99'
				});
			}
		});
	},
	swiperFn:function(latest,hot,hotContent,latestContent){
		latestContent.hide();
		latest.on('click',function(){
			$(this).addClass('on').removeClass('off');
			hot.removeClass('on').addClass('off');
			latestContent.show();
			hotContent.hide();
			
		});
		hot.on('click',function(){
			$(this).addClass('on').removeClass('off');
			latest.removeClass('on').addClass('off');
			hotContent.show();
			latestContent.hide();
			
		})
	},
	shareFn: function(obj){
		var tit = obj.attr('data-name');
		var desc = obj.attr('data-desc');
		var pic = serverUrl+obj.attr('data-videopic');//缩略图
		var videoId = obj.attr('data-videoId');
		var userId = obj.attr('data-userId');
		share.init(tit,desc,pic,videoId,uid,userId);
	},
	activeList: function(data,target,isclear){
		if(isclear){
			$(target).empty();
		}
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
			};
			var actList = '<li data-id="'+ n.id +'"><div class="listUserMsg">'+
			'<dl><dt><img class="actvideoPic" data-userId="'+ n.user.id +'" data-userPic="'+ n.user.pic +'" src="" alt="头像" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
			vStart+
			'</dt>'+
			'<dd><p>'+ n.user.name +'</p><p class="myDate" data-time="'+ n.createDate+'"></p></dd>'+
			'</dl></div>'+
			'<div class="playBox" data-videoid="'+ n.id+'" data-videoKey="'+ n.playKey+'">'+
			'<div class="loadingGif" style="width:30px;height:30px;min-height:30px;position:absolute;left:0;top:0;display:none;z-index:999999;background-color:#000000;opacity:0.6;width:100%;height:50%;color:#ffffff;text-align:center;padding-top:50%;font-size:18px;"></div>'+
			'<img data-videoPic="'+ n.videoPic +'" src="" alt="视频" class="videoPic"><em class="canPlay"></em></div>'+
			"<div class='videoListTag face'>"+ n.description +"<span data-userId="+n.user.id+"></span></div>"+
			'<div class="shareBox">'+
			'<div class="read"><em></em><span>'+ n.playCount +'</span></div>'+
			'<div class="commit" data-video='+ n.id +'><em></em><span>'+ n.evaluationCount +'</span></div>'+
			'<div class="zan" data-praise='+ n.praise +' data-videoid='+ n.id +'><em class="off"></em><span>'+ n.praiseCount +'</span></div>'+
			'<div class="Forward" data-name="'+n.user.name+'" data-userId="'+ n.user.id+'" data-videoid="'+ n.id+'" data-desc="'+n.description+'" data-videoPic="'+ n.videoPic+ '">' +
			'<em></em></div>'+
			'</div></li>';
			$(target).append(actList);
		});
			info.videoUrl();
			info.faceFn($('.face'));
			info.personalPortrait(serverUrl,$('.actvideoPic'));
			info.videoInfo(serverUrl,$('.videoPic'));
			info.timeTransform('.myDate','data-time','MM.dd hh:mm');
			info.playNum($('.playData'));
			$('.videoListTag span').on('click',function(){
				var userId = $(this).attr('data-userId');
				window.location.href = interUrl + '/user/userDetail?userId=' + userId;
			});
			$('.zan em').on('click',function(){
				window.location.href = 'http://wopaitv.com';
			})
		//分享
		$('.Forward').click(function(){
			activeFn.shareFn($(this));
		});
//		$('.listUserMsg').on('click','.actvideoPic',function(){
//			var userId= $(this).attr('data-userId')
//			activeFn.goPage('userId',userId)
//		});
	},
	goPage: function(type,typeId){
		var url= ''
		var params= {};
		if(type == 'video'){
			url= interUrl+"/video/videoDetail?videoId="+typeId
			params= {
				uid: uid,
				isNext: activeFn.isNext,
				nextType: activeFn.nextType,
				activityId: actId
			}
		}else if(type == 'userId'){
			url= interUrl+'/user/userDetail?userId='+typeId;
			params= {
				uid: uid
			}
		}
		info.postform(url,params)
	},
	jump:function(){
		 $('body').on('touchstart','.down-Btn,.downloadBtn,.openwopai,.bottomBanner a',function(e){
			 e.preventDefault();
			 if(browse.versions.ios)
		        {
		        	if(browse.versions.isWeiBo){
		        		$('.skyBox').show();
			        }else if(browse.versions.isWx){
			        	 window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
			        }else{
			        	window.location.href = 'https://itunes.apple.com/us/app/wo-pai-wo-jiu-shi-zhu-jiao/id934254637?ls=1&mt=8';
			        }
		        }
		        if(browse.versions.android)
		        {
		        	if(browse.versions.isWeiBo){
		        		$('.skyBox-anz').show();
			        }else if(browse.versions.isWx){
			        	 window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
			        }else{
			        	window.location.href = info.appDownUrl;
			        }
		        }
		        if(browse.versions.isWx)
		        {
		            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
		        }
		    })
		$('.skyBox,.skyBox-anz').click(function(){
			$(this).hide();
		});
	}
}
activeFn.init();
})