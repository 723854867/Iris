
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','../common/share','head','../common/browse'], function(jquery,info,alert,service,share,head,browse) {


var serverUrl = info.getPicPerfix()
var interUrl = info.getRequestPrefix();
var uid = info.getCookie('uid');

var actId = $('#actId').val();
var activeTit= $('.active-tit').text();
var activeFn = {
	init: function(){
		var page= 1;

		_this=this;

		this.size = 10;
		_this.startIndex=0;
		_this.isNext= 0;
		_this.nextType= 2;
		_this.type = 'active'; //默认加载活动视频列表
		_this.dataBox = $('.videoList1 ul');

		info.personalPortrait(serverUrl,$('.actIndexCover'));
		activeFn.getHotList(_this.startIndex);
		$(window).scroll(function(){
			info.lazyPicLoad($('.playBox img'));
			info.lazyPicLoad($('.actvideoPic'));
		});
		$('.downClose').click(function(){ //cnzz关闭下载
			_czc.push(["_trackEvent","关闭活动详情下载按钮","closeBottomDownload"]);
		});
		$(".active-desc-arrow").click(function(){
			$(".activeMsgCon").toggleClass("drop-desc");
			$(this).toggleClass("arrrow-up");
		})
		$('#btnMore').click(function(){
			page++;
			if(_this.type=='active'){
				activeFn.getActiveList(page);
			}else if(_this.type=='newList'){
				activeFn.getNewList(page);

			}else if(_this.type=='hotList'){
				_this.startIndex+=10;
				activeFn.getHotList(_this.startIndex);
			}else{
				return;
			}
		});
		//转发到我拍 后期要优化这个方法
		$('body').delegate('.sharewp','click',function(){
			var videoId= $(this).attr('data-videoid');
			var userId= $(this).attr('data-userId');
			service.shareWopaiap(videoId,userId,'',1,'h5').done(function(json){
				if(json.code=='200'){
					alert.alertMsg('转发成功！');
					$('.shareWrap').remove();
				}
			}).fail(function(json){
				if(json.code=='344'){
					alert.alertMsg('你已经转发过该视频！');
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
		$('.videoListSort ').click(function(e){
			$('.tabHidden').show();
			$(document).on("click", function(){
				$("#tabList").hide();
			});
			e.stopPropagation();
		});
		$('.tabHidden p').click(function(){
			//activeFn.hotTab($(this),dataBox);
			if($(this).text() == '最热'){
				activeFn.type='hotList';
				_this.startIndex= 10;
				$(this).parent().hide();
				$('.tabHot').text('最热');
				service.getActiveHotList(0,_this.startIndex,actId).done(function(json){
					activeFn.activeList(json.result,_this.dataBox,true);
				});
			}else{
				activeFn.type='newList';
				$(this).parent().hide();
				$('.tabHot').text('最新');
				service.getActiveNewList(10,actId,1).done(function(json){
					activeFn.activeList(json.result,_this.dataBox,true);
				});
			}
			return false;
		});

		$('.active-go-app').click(function(){
			if(browse.versions.ios){
				if(browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
					$('.skyBox').show();
				}else{
					$(this).attr("href","wopai://activityid="+actId);
					timer();
				}
			}else if(browse.versions.android){
				if(browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
					$('.skyBox-anz').show();
				}else{
					$('#openapp').attr('src',"com.busap.myvideo://main/openwith?activityId="+actId+"&activityTit="+activeTit+"&type=1");
					//$(this).attr("href","com.busap.myvideo://main/openwith?activityId="+actId+"&activityTit="+activeTit+"&type=1");
					//window.location.href='com.busap.myvideo://main/openwith?activityId='+actId+'&activityTit='+activeTit+'&type=1';
					timer();
				}
			}else{
				window.location.href='http://www.wopaitv.com/';
			};
			$('.skyBox').click(function(){
				$(this).hide();
			});
			$('.skyBox-anz').click(function(){
				$(this).hide();
			});
			function timer(){
				setTimeout(function(){
					window.location.href='http://www.wopaitv.com/';
				},3000)
			}
		})
	},
	getActiveList: function(page){
		service.getActVideoList(page,10,actId).done(function(json){
			if(json.result==''){
				alert.alertMsg('没有更多数据！');
				return;
			}
			activeFn.activeList(json.result,_this.dataBox,false);
		});
	},
	getNewList: function(page){
		service.getActiveNewList(10,actId,page).done(function(json){
			if(json.result==''){
				alert.alertMsg('没有更多数据！');
				return;
			}
			activeFn.activeList(json.result,activeFn.dataBox,false);
		});
	},
	getHotList: function(startIndex){
		service.getActiveHotList(startIndex,activeFn.size,actId).done(function(json){
			if(json.result==''){
				alert.alertMsg('没有更多数据！');
				return;
			}
			activeFn.activeList(json.result,activeFn.dataBox,false);
		});
	},
	hotTab: function(obj,target){
		if(obj.text() == '最热'){
			$(obj).parent().hide();
			$('.tabHot').text('最热');
			service.getActiveHotList(0,10,actId).done(function(json){
				activeFn.activeList(json.result,target,true);
			});
		}else{

			$(obj).parent().hide();
			$('.tabHot').text('最新');
			service.getActiveNewList(10,actId,1).done(function(json){
				activeFn.activeList(json.result,target,true);
			});
		}
		return false;
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
			'<dd><em class="playIcon"></em><span class="playData">'+ n.showPlayCount +'</span></dd></dl></div>'+
			'<div class="playBox" data-videoid="'+ n.id+'" data-videoKey="'+ n.playKey+'">'+
			'<div class="loadingGif" style="width:30px;height:30px;min-height:30px;position:absolute;left:0;top:0;display:none;z-index:999999;background-color:#000000;opacity:0.6;width:100%;height:50%;color:#ffffff;text-align:center;padding-top:50%;font-size:18px;"></div>'+
			'<img data-videoPic="'+ n.videoPic +'" src="" alt="视频" class="videoPic"><em class="canPlay"></em></div>'+
			"<div class='videoListTag face'>"+ n.description +"</div>"+
			'<div class="shareBox"><div class="zan" data-praise='+ n.praise +' data-videoid='+ n.id +'><em class="off"></em><span>'+ n.praiseCount +'</span></div>'+
			'<div class="commit" data-video='+ n.id +'><em></em><span>'+ n.evaluationCount +'</span></div>'+
			'<div class="Forward" data-name="'+n.user.name+'" data-userId="'+ n.user.id+'" data-videoid="'+ n.id+'" data-desc="'+n.description+'" data-videoPic="'+ n.videoPic+ '">' +
			'<em></em></div>'+
			'</div></li>';

			$(target).append(actList);


		});
			info.videoUrl();
			info.faceFn($('.face'));
			info.personalPortrait(serverUrl,$('.actvideoPic'));
			info.videoInfo(serverUrl,$('.videoPic'));
			info.timeTransform('.myDate','data-time','MM-dd hh:mm');
			info.playNum($('.playData'));


		$('.commit').each(function(){
			$(this).click(function(){
				var videoId = $(this).attr('data-video');
				activeFn.goPage('video',videoId);
			})
		});
		//分享
		$('.Forward').click(function(){
			activeFn.shareFn($(this));

		});
		//$('body').off('touchstart','.Forward',activeFn.shareFn);
		$('.listUserMsg').on('click','.actvideoPic',function(){
			var userId= $(this).attr('data-userId')
			activeFn.goPage('userId',userId)
		});
		$('.zan').each(function(){
			if($(this).attr('data-praise') == 'true'){
				$(this).find('em').addClass('on');
			}
			$(this).click(function(){
				var zanNum = parseInt($(this).find('span').text());
				var ab=  $(this).attr('data-videoid');
				var bc = $(this).parent().attr('data-createrId');
				if($(this).attr('data-praise') == 'false' || $(this).attr('data-praise') == false){
					//点赞
					var self = this;

					service.savePraise(ab).done(function(json) {
						$(self).attr('data-praise',true);

						if($(self).find('em').hasClass('on')){
							$(self).find('em').removeClass('on');
							$(self).find('em').addClass('off');
							$(self).find('span').text(zanNum-1);
						}else{
							$(self).find('em').removeClass('off');
							$(self).find('em').addClass('on');
							$(self).find('span').text(zanNum+1);
						}
					}).fail(function(json){
						alert.alertMsg("请登录后操作");
						window.location.href = info.getRequestPrefix()+"/page/user/login";

					});
				}

				if($(this).attr('data-praise') == 'true' || $(this).attr('data-praise') == true){
					//取消赞
					var self = this;

					service.deletePraise(ab).done(function(json) {
						$(self).attr('data-praise', false);

						if($(self).find('em').hasClass('on')){
							$(self).find('em').removeClass('on');
							$(self).find('em').addClass('off');
							$(self).find('span').text(zanNum-1);
						}else{
							$(self).find('em').removeClass('off');
							$(self).find('em').addClass('on');
							$(self).find('span').text(zanNum+1);
						}
					}).fail(function(json){
						alert.alertMsg("请登录后操作");
						window.location.href = info.getRequestPrefix()+"/page/user/login";

					});
				}
			})
		})


	},
	goPage: function(type,typeId){
		var url= ''
		var params= {};
		if(type == 'video'){
			url= interUrl+"/page/video/videoDetail?videoId="+typeId
			params= {
				uid: uid,
				isNext: activeFn.isNext,
				nextType: activeFn.nextType,
				activityId: actId
			}
		}else if(type == 'userId'){
			url= interUrl+'/page/user/userDetail?userId='+typeId;
			params= {
				uid: uid
			}
		}
		info.postform(url,params)
	}
}
activeFn.init();
})