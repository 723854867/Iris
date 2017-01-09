requirejs.config({
	shim:{
		'../libs/jquery.qqFace':['../libs/jquery-2.1.4']
	}
});
require(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','../common/share','head','../common/browse'], function(jquery,info,alert,service,share,head,browse) {
	var serverUrl = info.getPicPerfix();
	var interUrl = info.getRequestPrefix();
	var uid = info.getCookie('uid');
	var access_token = info.getCookie('access_token');


var videoShow = {
	init: function(){
		_this= this;
		var videoId = $('.videoList1 .playBox').attr('data-videoId');
		var praiseUrl = interUrl + '/page/praise/praiseList?videoId='+videoId;


		var page=1;
		var count= 8;
		var videoId = $('#videoId').val();
		var UserId= _this.userId= $('#userId').val();
		_this.creatId= $('#creatId').val();
		_this.isNext= $('#isNext').val()?$('#isNext').val():0;
		_this.nextType= $('#nextType').val();
		_this.activityId= $('#activityId').val();
		_this.tag= $('#tag').val();
		_this.praiseNum= $(".likeNum a");
		_this.hasNext = $('#hasNext').val();
		info.setCookie("isNext",_this.isNext);
		info.setCookie("nextType",_this.nextType);
		info.setCookie("activityId",_this.activityId);
		info.setCookie("tag",encodeURI(_this.tag));

		info.videoInfo(serverUrl,$('.videoPic'));
		info.videoInfo(serverUrl,$('.videoPic-img'));
		info.personalPortrait(serverUrl,$('.userImg'));
		info.personalPortrait(serverUrl,$('.likeUser dt img'));
		info.videoUrl(info.getVideoPrefix());
		info.timeTransform('.myDate','data-time','MM-dd hh:mm');
		videoShow.videoTag();
		videoShow.praiseTab();
		if(uid && UserId == uid){
			$('.playData').empty();
		}
		$(".likeNum a").attr('href',praiseUrl+'#cnzz_name=videoPraiseList&cnzz_from=videoDetailPraiseList');


		window.alert = function(){
			return;
		}
		$('.commontFireBox').width($('.main').width()-16)

		$(window).scroll(function(){
			if($(window).scrollTop()<=300){
				$('.commontFireBox').css({
					'position': 'relative',
					'bottom': '0'
				});
				$('.evalutionList').css('padding','0');
			}
			info.lazyPicLoad($('.hotPic'));
		});
		$('.open-app-btn').click(function(){
			if(browse.versions.ios){
				if(browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
					$('.skyBox').show();
				}
				$(this).attr("href","wopai://videoid="+videoId);
				timer();
			}else if(browse.versions.android){
				if(browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
					$('.skyBox-anz').show();
				}else{
					$('#openapp').attr("src","com.busap.myvideo://main/openwith?videoId="+videoId+"&type=3");
					timer();
				}
			}else{
				window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
			};
			$('.skyBox').click(function(){
				$(this).hide();
			});
			$('.skyBox-anz').click(function(){
				$(this).hide();
			});
			function timer(){
				setTimeout(function(){
					window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
				},3000)
			}
		});
		$('.next-video').click(function(){
			_this.isNext= 1;
			if(_this.hasNext && _this.hasNext==0){
				alert.alertMsg('没有视频了');return;
			}else{
				videoShow.goPage('video',videoId,"#cnzz_name=videoDetail&cnzz_from=nextVideo");
			}
			
		})
		//转发到我拍 后期一定要优化这个方法
		$('body').delegate('.sharewp','click',function(){
			console.log(_this.creatId)
			service.shareWopaiap(videoId,_this.creatId,'',1,'h5').done(function(json){
				if(json.code=='200'){
					alert.alertMsg('转发成功！');
					$('.shareWrap').remove();
				}
			}).fail(function(json){
				if(json.code=='344'){
					alert.alertMsg('你已经转发过该视频！')
					$('.shareWrap').remove();
					return;
				}else if(json.code == '343'){
					alert.alertMsg(json.message);
					$('.shareWrap').remove();
					return;
				}
				alert.alertMsg("请登录后操作");
				window.location.href = info.getRequestPrefix()+"/page/user/login";
			});
		});
		service.getVidEoeva(0,1,videoId,8).done(function(json){
			if(json.result == '')
			{
				$('.evalutionListCon').html('<p class="emptydata">暂无数据</p>')
			}
			videoShow.makeEvaluationList(json.result,$('.evalutionListCon'),false);
		});
		//赞
		$('.zan').on('click',function(){
			if(uid== 0){
				alert.alertMsg('未登录，请登录后操作！');
				setTimeout(function(){
					window.location.href= interUrl+'/page/user/login/#cnzz_name=userLogin&cnzz_from=videoDetailNotLoginPraise';
				},1000);
				return;
			}
			videoShow.Praise($(this));
		});
		//关注按钮
		$('.playData em').click(function(){
			if(uid== 0){
				alert.alertMsg('未登录，请登录后操作！');
				setTimeout(function(){
					window.location.href= interUrl+'/page/user/login';
				},1000);
				return;
			}
			videoShow.switchAttention($(this))
			});
		//分享
		$('.shareBtn').click(videoShow.shareFn);

		if(info.getCookie('closeVideoShowAdd')){
			$('.integral-ad').hide();
		}else{
			$('.integral-ad').show();
		};
		$('.integral-ad-close-warp').on('click',function(){
			$(this).parent().hide();
			info.setCookie('closeVideoShowAdd','closeVideoShowAdd',1);

		});


		if(info.getCookie('channel')=='openg'){
			$('.downBar,.active-go-app,.downBar2,.join-tag,.integral-ad').hide();
			$('.activeIndex').css('padding-bottom','0px');
			$('.videoList1,.videoList').css('padding-bottom','0px');
			$('.funPerson,.personalCenter,.activeUseList').css('margin-bottom','0px');
			$('#btnMore').css('margin-bottom','5px');
		}else{
			$('.downBar,.active-go-app,.downBar2,.join-tag,.integral-ad').show();
		}


		$('.wishWord a').click(function(){
			var key = $(this).text().split('#')[1].split("#")[0];
			window.location.href=interUrl+'/page/video/tagVideos?tag='+encodeURI(key);
		})
		$('.videoListBox').delegate('.hotPic','click',function(){
			var videoId = $(this).attr('data-videoId');
			videoShow.goPage('video',videoId,"#cnzz_name=videoDetail&cnzz_from=videoDetail");
		});

		$('div').delegate('.go-user-page,.openg','click',function(){
			var userId = $(this).attr('data-userId');
			videoShow.goPage('userId',userId,"#cnzz_name=userDetail&cnzz_from=videoDetailUserHead");
		});
		$('.commontFireBox').on('focus','.commontTxt',function(){
			if(!info.getCookie('uid')){
				alert.alertMsg("请登录后操作");
				window.location.href = info.getRequestPrefix()+"/page/user/login#cnzz_name=userLogin&cnzz_from=videoDetailNotLoginComment";
			}
		});
		//	发送评论
		$('.evalutionList').on('touchstart','.commontSub',function(){
			videoShow.sendEvaluation(videoId,uid);
		});
		$('.commontHead li').click(function(){
			videoShow.switchEvalutionList($(this));
		});
		//评论列表滑动底部加载
		$('.evalutionListConWarp').scroll(function(){
			var scroTop= $('.evalutionListConWarp').scrollTop();
			var warpH= $('.evalutionList').height();
			var boxH= $('.evalutionListCon').innerHeight();
			if(scroTop<=100){
				$('.commontFireBox').css({'position': 'absolute'});
				$('.evalutionList').css('padding','0');
			}else{
				$('.commontFireBox').css({
					'position': 'fixed',
					'bottom': '0'
				});
				$('.evalutionList').css('padding','0 0 50px 0');
			}
			if(warpH+scroTop >= boxH){

				count= count+8;
				service.getVidEoeva(0,0,videoId,count).done(function(json){
					if(json.result == '')
					{
						count= 8;
						$('.evalutionListCon').html('<p class="emptydata">暂无数据</p>')
					}
					videoShow.makeEvaluationList(json.result,$('.evalutionListCon'),true);
				});
			}
		});
	},
	//点赞人个数判断为0隐藏
	praiseTab: function(){
		if(_this.praiseNum.text() == 0){
			videoShow.praiseNum.closest('.likeUser').hide()
		}
	},
	//视频标签跳转
	videoTag: function(){
		var tagList = $('.wishWord');
		var tagTxt=tagList.text();
		var newArr = tagTxt.split('#');
		var resultStr='';
		var videoTagUrl=interUrl+'/page/activity/activityTag?tag=';
		$.each(newArr,function(m,n){
			if(m%2){
				resultStr+='<a href="'+(videoTagUrl+n)+'">#'+n+'#</a>';
			}else{
				resultStr+=n;
			}
		});
		tagList.html(resultStr);
	},
	//评论切换列表
	switchEvalutionList: function(obj){   //四个参数  flag 最新评论  startid 开始id videoid 视频id count 条数
		var index = obj.index();
		var videoId= obj.attr('data-videoId');
		var page = 1;
		var clear = true;
		$('.commontHead li').removeClass('on')
		obj.addClass('on');
		if(index == '0'){
			var target = $('.evalutionList');
			target.show();
			clear = false;
			$('.videoListBox').hide();
			if(target.html() == ''){
				service.getVidEoeva(0,1,videoId,10).done(function(json){
					videoShow.makeEvaluationList(json.result,$('.evalutionListCon'),clear);
				})
			}
		}else{
			var target= $('.videoListBox ul');
			$('.videoListBox').show();
			$('.evalutionList').hide();
			if(target.html() == ''){
				service.getHotVideo(videoId,page,6).done(function(json){
					videoShow.makeHotVideoList(json.result,target,clear)
					//默认请求热门视频数据
				})
				$('.hotlistMore').on('click',function(){
					page++;
					clear= false;
					service.getHotVideo(videoId,page,6).done(function(json){
						videoShow.makeHotVideoList(json.result,target,clear)
						//请求更多热门视频数据
					})
				})
			}
		}
	},
	//发送评论
	sendEvaluation: function(videoId,uid){
		var content = $('.commontTxt').val();
		var page= 1;
		if(content != ''){
			service.saveEvaluation(videoId,uid,content).done(function(json){
				_czc.push(["_trackEvent","登录攒按钮","videoDetailPraise"]);
				if(json.code == 200){
					var target = $('.evalutionListCon');
					target.empty();
					alert.alertMsg('提交成功！');
					$('.commontTxt').val('');
					service.getVidEoeva(0,1,videoId,8).done(function(json){
						videoShow.makeEvaluationList(json.result,target,false);
						//默认请求最新评论数据
					})
				}
			}).fail(function(json){
				alert.alertMsg("请登录后操作");
				window.location.href = info.getRequestPrefix()+"/page/user/login";
			});
		}else{
			alert.alertMsg('请输入内容');
		}
	},
	//关注按钮
	switchAttention: function(obj){
		var userId = obj.attr('data-userId'); //用户Id
		var isAttVal= obj.attr('data-isattention'); //是否关注参数
		var dataFrom = 'h5'; //写上来源的标示

		if(isAttVal == 0){
			service.addAttention(userId,dataFrom,isAttVal).done(function(json){
				_czc.push(["_trackEvent","关注按钮登录","videoDetailAttention"]);
				obj.attr('data-isattention',1);

				obj.addClass('attenBtnActive').removeClass('attenBtn');
			}).fail(function(json){
				_czc.push(["_trackEvent","关注按钮未登录","videoDetailAttention"]);
				alert.alertMsg("请登录后操作");
				window.location.href = info.getRequestPrefix()+"/page/user/login/#cnzz_name=userLogin&cnzz_from=videoDetailNotLoginAttention";

			});
		}
		//if(isAttVal == 1){ //取消关注的功能暂时屏蔽
		//	service.addAttention(userId,dataFrom,isAttVal).done(function(json){
        //
		//		obj.attr('data-isattention',0);
		//		obj.addClass('attenBtn').removeClass('attenBtnActive');
		//	}).fail(function(json){
		//		alert.alertMsg("请登录后操作");
		//		window.location.href = info.getRequestPrefix()+"/page/user/login";
        //
		//	});
		//}
	},
	//赞
	Praise: function(obj){
		var isZambia= obj.attr('data-praise');
		var videoId= obj.attr('data-videoid');
		var self = obj;

		if(isZambia == 'false' || isZambia == false){
			service.savePraise(videoId).done(function(json) {
				$(self).attr('data-praise',true);
				$(self).find('em').addClass('loveIconOn').removeClass('loveIcon');
				_czc.push(["_trackEvent","登录攒按钮","videoDetailPraise"]);
				service.getPraiseList(videoId,0,20).done(function(json){
					videoShow.makePraiseList(json.result,$('.listUserList'),true);
					$('.likeNum a').text(parseInt($('.likeNum a').text())+1)
				})
			}).fail(function(json){
				alert.alertMsg("请登录后操作");
				window.location.href = info.getRequestPrefix()+"/page/user/login";
			});
		}else{
			service.deletePraise(videoId).done(function(json) {
				$(self).attr('data-praise',false);
				$(self).find('em').addClass('loveIcon').removeClass('loveIconOn');
				service.getPraiseList(videoId,0,20).done(function(json){
					videoShow.makePraiseList(json.result,$('.listUserList'),true);
					$('.likeNum a').text(parseInt($('.likeNum a').text())-1)
				})
			}).fail(function(json){
				alert.alertMsg("请登录后操作");
				window.location.href = info.getRequestPrefix()+"/page/user/login";
			});
		}
	},
	//跳转页面 隐藏uid参数
	goPage: function(type,typeId,anchor){
		var url= '';
		var params= {};

		if(type == 'video'){
			url= interUrl+"/page/video/videoDetail?videoId="+typeId+anchor;
			params= {
				uid: uid,
				isNext: videoShow.isNext,
				nextType: videoShow.nextType,
				activityId: videoShow.activityId,
				tag: videoShow.tag,
				userId: videoShow.userId
			}
		}else if(type == 'userId'){
			url= interUrl+'/page/user/userDetail?userId='+typeId+anchor;
			params= {
				uid: uid
			}
		}
		info.postform(url,params);
	},
	shareFn: function(){
		var tit = $(this).attr('data-name')
		var desc = $(this).attr('data-desc')
		var pic = serverUrl+$(this).attr('data-videopic');//缩略图
		var videoId = $(this).attr('data-videoId');
		var userId = $(this).attr('data-userId');
		_czc.push(["_trackEvent","分享按钮","videoDetailTurn"])
		share.init(tit,desc,pic,videoId,uid,userId);
	},
	//实例化赞用户列表
	makePraiseList: function(json,target,clear){
		if(clear){
			$(target).empty();
		}
		var vStart;
		$.each(json,function(m,n){
			//var vStart;
			if(n.vipStat == 1){
				vStart= '<em class="userV1"></em>';

			}else if(n.vipStat == 2){
				vStart= '<em class="userV2"></em>';
			}else if(n.vipStat == 3){
				vStart= '<em class="userV3"></em>';
			}else{
				vStart= '';
			}
			var praisList = '<dt>'+
			'<img class="go-user-page" data-userPic="'+ n.pic +'" data-userId="'+ n.id +'" src="" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
			vStart+
			'</dt>';
			target.append(praisList);
			info.personalPortrait(serverUrl,$('.likeUser dt img'));
		})
	},
	makeEvaluationList: function(data,target,clear){ //最新评论
		if(clear){
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
			var tempList = '<dl>'+
				'<dt><img class="evaluationList go-user-page" data-userId="'+ n.user.id+'" data-userPic="'+ n.user.pic +'" src="" alt="头像" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
				vStart+'</dt>'+
				'<dd><p>'+ n.user.name+'</p><p class="face">'+ n.content +'</p>'+
				'</dd><dd class="myDate" data-time="'+ n.createDate+'"></dd></dl>';
			target.append(tempList);

		});
		info.personalPortrait(serverUrl,$('.evaluationList'));
		info.timeTransform('.myDate','data-time','MM-dd hh:mm');
		info.faceFn($('.face'));
	},
	makeHotVideoList: function(json,target,clear){ //最热视频
		if(clear){
			$(target).empty();
		}
		$.each(json,function(m,n){
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
			var tempList= '<li>'+
				'<a href="javascript:void(0);"><img data-userPic="'+ n.videoPic +'" data-videoId="'+ n.id +'"src="" class="hotPic"></a>'+
				'<div class="videoList">'+

				'<span class="fr"><em class="playNum"></em>'+ n.showPlayCount +'</span>'+
				'</div>' +
				//'<div class="videoMsg">'+ n.user.name +'</div>'+
				'<div class="userTit">' +
				'<div class="useradddV">'+
				'<img class="fl hotUesrPic go-user-page" data-userId="'+ n.user.id +'" data-userPic="'+ n.user.pic +'" src="" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
				vStart+
				'</div>'+
				'<span class="userName">'+ n.description+'</span>' +
				'</div>'
				'</li>';
			$(target).append(tempList)
		})
		info.personalPortrait(serverUrl,$('.hotPic,.hotUesrPic'),false);
	}
}


videoShow.init();
})