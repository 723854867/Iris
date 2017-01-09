requirejs.config({
	shim:{
		'../libs/jquery.qqFace':['../libs/jquery-2.1.4']
	}
});
require(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','../common/share','head','../common/browse'], function(jquery,info,alert,service,share,head,browse) {
	var serverUrl = info.getPicPerfix();
	var interUrl = info.getRequestPrefix();
	var videoId = $('#videoId').val();
	var userId = $('#creatId').val();
	var praiseUrl = interUrl + '/page/praise/praiseList?videoId='+videoId;

var videoShow = {
	init: function(){
		var count = 8;
		info.personalPortrait(serverUrl,$('.userPortrait-1'));
		info.videoInfo(serverUrl,$('.videoPic'));
		info.videoInfo(serverUrl,$('.videoPic-img'));
		info.personalPortrait('.hotPic');
		info.videoUrl(info.getVideoPrefix());
		videoShow.roomList();
		//分享
		$('.Forward').click(function(){
			videoShow.shareFn($(this));
		});
		//滚动加载事件
		$(window).scroll(function(){
			info.lazyPicLoad($('.hotPic'));
		});
		service.getVidEoeva(0,1,videoId,count).done(function(json){
			if(json.result == '')
			{
				$('.evalutionListCon').html('<p class="emptydata">暂无评论</p>');
				$('.evalutionListCon p').css('text-indent','10px');
			};
			videoShow.makeEvaluationList(json.result,$('.evalutionListCon'),false);
		});
		$('.zan em').on('click',function(){
			window.location.href = 'http://wopaitv.com';
		});
		$('.playData').on('click',function(){
			window.location.href = interUrl + '/user/userDetail?userId=' + userId;
		});

		videoShow.jump();

	},
	//跳转页面 隐藏uid参数
	goPage: function(type,typeId,anchor){
		var url= '';
		var params= {};

		if(type == 'video'){
			url= interUrl+"/page/video/videoDetail?videoId="+typeId+anchor;
			params= {
				uid: uid,
				userId: videoShow.userId
			}
		}else if(type == 'userId'){
			url= interUrl+'/page/user/userDetail?userId='+typeId+anchor;
			params= {
				uid: uid
			}
		};
		info.postform(url,params);
	},
	shareFn: function(obj){
		var tit = obj.attr('data-name');
		var desc = obj.attr('data-desc');
		var pic = serverUrl+obj.attr('data-videopic');//缩略图
		var userId = $('#creatId').val();
		_czc.push(["_trackEvent","分享按钮","videoDetailTurn"])
		share.init(tit,desc,pic,videoId,userId);
	},
	noticeTime:function(obj){
		var noticeTime = obj.attr('data-time');
		var d = new Date();
		var date=new Date(parseInt(noticeTime));
		obj.text('直播时间：'+ date.getFullYear()+"-"+fixZero(date.getMonth()+1,2)+"-"+fixZero(date.getDate(),2)+" "+fixZero(date.getHours(),2)+":"+fixZero(date.getMinutes(),2));
		function fixZero(num,length){
			var str=""+num;
			var len=str.length;
			var s="";
			for(var i=length;i-->len;){
				s+="0";
			}
			return s+str;
		};
	},
	createTime:function(obj){
		var noticeTime = obj.attr('data-time');
		var d = new Date();
		var date=new Date(parseInt(noticeTime));
		obj.text(fixZero(date.getMonth()+1,2)+"."+fixZero(date.getDate(),2)+" "+fixZero(date.getHours(),2)+":"+fixZero(date.getMinutes(),2));
		function fixZero(num,length){
			var str=""+num;
			var len=str.length;
			var s="";
			for(var i=length;i-->len;){
				s+="0";
			}
			return s+str;
		};
	},
	roomList:function(){
		var page = 1,size = 6,type = 1,target = $('.videoListBox ul');
		service.getLiveAndRecordList(page,size,type).done(function(json){
			videoShow.makeLiveList(json.result,target);
//			if(json.result.length<6){
//				liveForenotice.makeLiveList(json.result,target);
//				var page = 1,size = 6-json.result.length,isLive = 2,userId = '',target = $('.videoListBox ul');
//				service.getRoomList(page,size,isLive,userId).done(function(json1){
//					liveForenotice.makeLiveList(json1.result,target);
//				});
//			}else{
//
//			}
		});
	},
	makeLiveList:function(data,target){
		var liveList = data.liveList;
//		if(liveList!=''){
			$.each(liveList,function(m,n){
				var tempList= '<li>'+
				'<a href="javascript:void(0)"><img data-userPic="'+ n.roomPic +'" lazy-src="" data-roomId="'+ n.id +'"src="" class="hotPic"></a>'+'</li>';
				target.append(tempList);
			});
//		}
//		if(liveList.length<6){
//			var backList = data.backList;
//			$.each(backList,function(m,n){
//				var tempList= '<li>'+
//				'<a href="javascript:void(0)"><img data-userPic="'+ n.videoPic +'" data-roomId="'+ n.liveNoticeId +'"src="" class="hotPic"></a>'+'</li>';
//				target.append(tempList);
//				$('.videoListBox ul li').css('height',$(window).width()/3);
//				$('.videoListBox ul li').css('width',$(window).width()/3);
//				$('.videoListBox ul').css('width',$(window).width()+6);
//				$('.videoListBox').css('height',$('.videoListBox ul').height());
//			});
//		}


		$('.videoListBox ul li').css('height',$(window).width()/3);
		$('.videoListBox ul li').css('overflow','hidden');
		$('.videoListBox ul li').css('width',$(window).width()/3);
		$('.videoListBox ul').css('width',$(window).width()+6);
		$('.videoListBox').css('height',$('.videoListBox ul').height());
		info.personalPortrait(serverUrl,$('.hotPic'));
		info.personalPortrait(serverUrl,$('.hotUesrPic'));
		$('.videoListBox ul li a').on('click',function(){
			window.location.href = interUrl + '/live/shareLive?roomId=' + $(this).find('img').attr('data-roomId');
		});
	},
	makeEvaluationList: function(data,target,clear){ //最新评论
		if(clear){
			$(target).empty();
		}
		$.each(data,function(m,n){
			var tempList = '<dl>'+
				'<dt><img class="evaluationList go-user-page" data-userId="'+ n.user.id+'" data-userPic="'+ n.user.pic +'" src="" alt="头像" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
				'</dt>'+
				'<dd><p>'+ n.user.name+'</p><p class="face">'+ n.content +'</p>'+
				'</dd><dd class="myDate" data-time="'+ n.createDate+'"></dd></dl>';
			target.append(tempList);

		});
		info.personalPortrait(serverUrl,$('.evaluationList'));
		info.timeTransform('.myDate','data-time','MM-dd hh:mm');
		info.faceFn($('.face'));
	},
	jump:function(obj){
		$(".down-Btn a,.bottomBanner a").click(function(e){
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
		});
		$('.skyBox,.skyBox-anz').click(function(){
			$(this).hide();
		});
	}

}


videoShow.init();
})