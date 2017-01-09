
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','../common/share','head','../common/browse'], function(jquery,info,alert,service,share,head,browse) {


//分享
//share.init();
//弹出框
//aler.alertMsg('msg');

var serverUrl = info.getPicPerfix()
var interUrl = info.getRequestPrefix();
var uid = info.getCookie('uid');
var activeFn = {
	init: function(){

		this.tagName= $('#tagName').val();
		_this=this;
		_this.isNext= 0;
		_this.nextType= 3;

		activeFn.getTagList('');
		$('#btnMore').click(function(){
			this.lastId= $('.videoList1 ul li').last().attr('data-id');
			activeFn.getTagList(this.lastId);
		});
		$('body').delegate('.Forward','click',activeFn.shareFn);
		info.personalPortrait(serverUrl,$('.activeImg'));
		$(window).scroll(function(){
			info.lazyPicLoad($('.playBox img'));
			info.lazyPicLoad($('.actvideoPic'));
		});

		$('.downClose').click(function(){ //cnzz关闭下载
			_czc.push(["_trackEvent","关闭活动标签下载按钮","closeBottomDownload"]);
		});
		$('.join-tag').click(function(){
			if(browse.versions.ios){
				if(browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
					$('.skyBox').show();
				}else{
					$(this).attr("href","wopai://hottagname="+encodeURI(_this.tagName));
					timer();
				}
			}else if(browse.versions.android){
				if(browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
					$('.skyBox-anz').show();
				}else{
					$('#openapp').attr("src","com.busap.myvideo://main/openwith?Tag="+_this.tagName+"&type=2");
					//window.location.href='com.busap.myvideo://main/openwith?Tag='+_this.tagName+'&type=2';
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
					alert.alertMsg('你已经转发过该视频！')
					$('.shareWrap').remove();
					return;
				}
				alert.alertMsg("请登录后操作");
				window.location.href = info.getRequestPrefix()+"/page/user/login";
			});
		});
	},


	getTagList: function(lastId){
		service.getActiveTagList(activeFn.tagName,10,lastId).done(function(json){
			if(json.result.content==''){
				alert.alertMsg('没有更更多数据！');
				return;
			}
			activeFn.activeList(json.result.content,$('.videoList1 ul'));
		});
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
				tag: activeFn.tagName
			}
		}else if(type == 'userId'){
			url= interUrl+'/page/user/userDetail?userId='+typeId;
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
		share.init(tit,desc,pic,videoId,uid,userId);
	},
	activeList: function(data,target){

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
			var newTagStr= [];
			var tagStar;
			$.each(n.tags,function(m,n){
				newTagStr.push('#'+n+'#');
				tagStar=newTagStr.join(" ");
			})
			var actList = '<li data-id="'+ n.id +'"><div class="listUserMsg">'+
				'<dl><dt><img class="actvideoPic" data-userId="'+ n.user.id +'" data-userPic="'+ n.user.pic +'" src="" alt="头像" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
				vStart+'</dt>'+
				'<dd><p>'+ n.user.name +'</p><p class="myDate" data-time="'+ n.createDate+'"></p></dd>'+
				'<dd><em class="playIcon"></em><span class="playData">'+ n.showPlayCount +'</span></dd></dl></div>'+
				'<div class="playBox" data-videoid="'+ n.id+'" data-videoKey="'+ n.playKey+'">'+
				'<div class="loadingGif" style="width:30px;height:30px;min-height:30px;position:absolute;left:0;top:0;display:none;z-index:999999;background-color:#000000;opacity:0.6;width:100%;height:50%;color:#ffffff;text-align:center;padding-top:50%;font-size:18px;"></div>'+
				'<img data-videoPic="'+ n.videoPic +'" src="" alt="视频" class="videoPic"><em class="canPlay"></em></div>'+
				'<div class="videoListTag face">'+ tagStar +'</div>'+
				'<div class="shareBox"><div class="zan" data-praise='+ n.praise +' data-videoid='+ n.id +'><em class="off"></em><span>'+ n.praiseCount +'</span></div>'+
				'<div class="commit" data-video='+ n.id +'><em></em><span>'+ n.evaluationCount +'</span></div>'+
				'<div class="Forward" data-name="'+n.user.name+'" data-userId="'+ n.user.id+ '" data-videoid="'+ n.id+'" data-desc="'+n.description+'" data-videoPic="'+ n.videoPic+ '">' +
				'<em></em></div>'+
				'</div></li>';
			$(target).append(actList);
		});
		info.videoUrl(serverUrlvid);
		info.personalPortrait(serverUrl,$('.actvideoPic'));
		info.videoInfo(serverUrl,$('.videoPic'));
		info.timeTransform('.myDate','data-time','MM-dd hh:mm');
		info.faceFn($('.face'));
		info.playNum($('.playData'));

		//分享
		$('.Forward').click(function(){
			activeFn.shareFn($(this));

		});
		$('.commit').each(function(){
			$(this).click(function(){
				var videoId = $(this).attr('data-video');
				//window.location.href=info.getRequestPrefix()+'/page/video/videoDetail?videoId='+videoId+'&uid='+uid;
				activeFn.goPage('video',videoId);
			})
		});
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

				//var zanNum = parseInt($(this).next().text());
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


	}

}
activeFn.init();
})