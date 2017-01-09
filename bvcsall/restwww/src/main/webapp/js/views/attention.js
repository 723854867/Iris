
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','head','../common/share'], function(jquery,info,alert,service,head,share) {

var serverUrl = $('#serverUrlimg').val();
var uid = info.getCookie('uid');
var accessToken = info.getCookie('access_token');
var serverUrlvid = $('#serverUrlvid').val();
var interUrl = info.getRequestPrefix();
var attentionFn = {
		init: function(){

			_this= this;
			_this.isNext= 0;
			_this.nextType= 5;
			attentionFn.renderCon();
			attentionFn.addAttention();
			attentionFn.tapAddMore();
			info.personalInfo(serverUrlimg,'.userHomePic');
			info.videoInfo(serverUrlimg,'.videoPic');
			info.videoUrl(serverUrlvid);
			info.personalPortrait(serverUrl,'.userPortrait-1');
			
			info.timeTransform('.myDate','data-time','MM-dd hh:mm');
			info.scrollTop();
			$(window).scroll(function(){
				info.lazyPicLoad('.videoPic');
				info.lazyPicLoad('.userPortraits');
			});
			$('.downClose').click(function(){ //cnzz关闭下载
				_czc.push(["_trackEvent","关闭关注页下载按钮","closeBottomDownload"]);
			});
			$('.seeMore').on('click',function(){
				attentionFn.goPage('dynamicRecommend');
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
					}else if(json.code == '343'){
						alert.alertMsg(json.message);
						$('.shareWrap').remove();
						return;
					}

					alert.alertMsg("请登录后操作");
					window.location.href = info.getRequestPrefix()+"/page/user/login";
				});
			});
			
		},
		picTap:function(){
			$('.actvideoPuseric,.userPortrait,.userPortrait-1').on('click',function(){
				var userId = $(this).attr('data-userId');
				if(userId==''){
					return;
				}else{
					attentionFn.goPage('userId',userId);
				}
			})
		},
		Jump:function(userId,uid,url){
			if(userId==''){
				return;
			}else{
				window.location.href = url;
			}
		},
		renderCon:function(){//页面渲染
			var timestamp = 0;
			var count = 10;
			var dataBox = $('.videoList1 ul')
			service.getAttentionVideoList(timestamp,count).done(function(json){
				attentionFn.attentionList(json.result,dataBox);
			});
		},
		addAttention:function(){//加减关注
			var obj = $('.atten');
			var obj1 = $('.atten');
			var userId =  $('.atten em').attr('data-userId');
			attentionFn.tapAttention(obj,obj1,userId,uid);
			
		},
		
		shareFn: function(obj){
			var tit = obj.attr('data-name')
			var desc = obj.attr('data-desc')
			var pic = serverUrl +obj.attr('data-videopic');//缩略图
			var videoId = obj.attr('data-videoId');
			var userId = obj.attr('data-userId');
			share.init(tit,desc,pic,videoId,uid,userId);
		},
		tapAddMore:function(){//点击加载更多视频
			$("#btnMore").on('click',function(){
				var timestamp = $('.videoList1 ul li').last().find('.playBox').attr('data-timeStamp');
				var count = 10;
				var dataBox = $('.videoList1 ul')
				service.getAttentionVideoList(timestamp,count).done(function(json){
					if(json==''){
						alert.alertMsg('没有更多视频了');
					}
					attentionFn.attentionList(json.result,dataBox);
				});
			});
		},
		attentionList: function(data,target){
			if(data==''){
				alert.alertMsg('没有更多视频了');
			}
			$.each(data,function(m,n){
				var vstats;
				var isForwards;
				if(n.user.vipStat==1){
					vstats='<img class="user_icon_blue" src="'+info.getRequestPrefix()+'/img/icons-2x/user_icon_blue.png" />';
				}else if(n.user.vipStat==2){
					vstats='<img class="user_icon_yellow" src="'+ info.getRequestPrefix()+'/img/icons-2x/user_icon_yellow.png" />';
				}else if(n.user.vipStat==3){
					vstats='<img class="user_icon_green" src="'+ info.getRequestPrefix()+'/img/icons-2x/user_icon_green.png" />';
				}else if(n.user.vipStat==0){
					vstats='';
				}
				if(n.isForward==1){
					isForwards='<div class="lineBg"><p><a class="fl"><img src="../../img/icons-2x/Forward-48.png" alt=""></a><img class="userPortraits" src="../../img/personalBg/personalPortriat.jpg" data-userPic="'
						+n.forwardUser.pic+'" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'" alt=""><span>'
						+n.forwardUser.name+'转发了'+n.user.name+'的视频</span></p></div>';
				}else{
					isForwards='';
				}
				var actList = '<li>'+isForwards+'<div class="listUserMsg">'+
				'<dl><dt><img class="userPortrait" data-userPic="'+ n.user.pic +'" src="" lazy_src="" alt="头像" data-userId="'+n.user.id+'" onerror="this.src=\'../../img/portrait.png\'">'+vstats+'</dt>'+
				'<dd><p>'+ n.user.name +'</p><p class="myDate" data-time="'+ n.createDate+'"></p></dd>'+
				'<dd><em class="playIcon"></em><span class="playData">'+ n.showPlayCount +'</span></dd></dl></div>'+
				'<div class="playBox" data-videoid="'+ n.id+'" data-videoKey="'+ n.playKey+'" data-timeStamp="'+ n.createDate+'">'+
				'<div class="loadingGif" style="width:30px;height:30px;min-height:30px;position:absolute;left:0;top:0;display:none;z-index:999999;background-color:#000000;opacity:0.6;width:100%;height:50%;color:#ffffff;text-align:center;padding-top:50%;font-size:18px;"></div>'+
				'<img class="videoPic" data-videoPic="'+ n.videoPic +'" src="" alt="视频" class="videoPic"><em class="canPlay"></em></div>'+
				'<span class="videoDiscription face">'+n.description+'</span>'+
				'<div class="shareBox"><div class="zan" data-praise='+ n.praise +' data-videoid='+ n.id +'><em class="off"></em><span>'+ n.praiseCount +'</span></div>'+
				'<div class="commit" data-video='+ n.id +'><em></em><span>'+ n.evaluationCount +'</span></div>'+
				'<div class="Forward" data-name="'+n.user.name+'" data-videoid="'+ n.id+'" data-userId="'+ n.user.id+'" data-desc="'+n.description+'" data-videoPic="'+ n.videoPic+ '">' +
				'<em></em></div>'+
				'</div></li>';

				$(target).append(actList);
			});
				info.videoUrl(serverUrlvid);
				info.personalPortrait(serverUrl,'.userPortrait');
				info.personalPortrait(serverUrl,'.userPortraits');
				info.videoInfo(serverUrl,$('.videoPic'));
				info.timeTransform('.myDate','data-time','MM-dd hh:mm');
				info.faceFn($('.face'));
				info.playNum($('.playData'));
				attentionFn.picTap();
			$('.commit').each(function(){
				$(this).click(function(){
					var videoId = $(this).attr('data-video');
					//window.location.href=info.getRequestPrefix()+'/page/video/videoDetail?videoId='+videoId+'&uid='+uid;
					attentionFn.goPage('video',videoId);
				})
			});
			$('.Forward').click(function(){
				attentionFn.shareFn($(this));

			});
			$('.zan').each(function(){

				if($(this).attr('data-praise') == 'true'){
					$(this).find('em').addClass('on');
				}
				$(this).click(function(){
					var zanNum = parseInt($(this).find('span').text());
					var ab=  $(this).attr('data-videoid');
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
		tapAttention:function(obj,obj1,userId,uid,dataFrom,isAttention){
			if(userId==uid){
				obj.hide();
			}
			obj1.click(function(){
				var dataFrom = 'h5'; //写上来源的标示
				var isAttention = $('#attentionBtn').attr('data-isAttention');
				if(uid== 0){
					alert.alertMsg('未登录，请登录后操作！');
					setTimeout(function(){
						window.location.href= interUrl+'/page/user/login';
					},1000);

					return;
				}
				
				if(isAttention==0){
					service.addAttention(userId,dataFrom,isAttention).done(function(json){
						
						$('#attentionBtn').attr('data-isAttention',1);
						$('#attentionBtn').removeClass('attenBtn').addClass('attenBtnActive');
						attentionFn.goPage('attentionVideoList');
					})

				}else if(isAttention==1){
					service.addAttention(userId,dataFrom,isAttention).done(function(json){
						
						$('#attentionBtn').attr('data-isAttention',0);
						$('#attentionBtn').removeClass('attenBtnActive');
						$('#attentionBtn').addClass('attenBtn');
					})
				}
			});
		},
		
		addMoreVideo:function(data,target){
			if(data==''){
				alert.alertMsg('没有更多视频了');
			}
			$.each(data,function(m,n){
				var actList = '<li><div class="listUserMsg">'+
				'<dl><dt><img class="userPortrait" data-userPic="'+ n.videoPic +'" src="" alt="头像" onerror="this.src=\'../../img/portrait.png\'"></dt>'+
				'<dd><p>'+ n.user.name +'</p><p class="myDate" data-time="'+ n.user.createDate+'"></p></dd>'+
				'<dd><em class="playIcon"></em><span class="playData">'+ n.showPlayCount +'</span></dd></dl></div>'+
				'<div class="playBox" data-videoid="'+ n.id+'" data-videoKey="'+ n.playKey+'" data-timeStamp="'+ n.createDate+'"><img data-videoPic="'+ n.videoPic +'" src="" alt="视频" class="videoPic"><em class="canPlay"></em></div>'+
				'<div class="shareBox"><div class="zan" data-praise='+ n.praise +' data-videoid='+ n.id +'><em class="off"></em><span>'+ n.praiseCount +'</span></div>'+
				'<div class="commit" data-video='+ n.id +'><em></em><span>'+ n.evaluationCount +'</span></div>'+
				'<div class="Forward" data-name="'+n.user.name+'" data-userId="'+ n.user.id+'" data-videoid="'+ n.id+'" data-desc="'+n.description+'" data-videoPic="'+ n.videoPic+ '">' +
				'<em></em></div>'+
				'</div></li>';

				$(target).append(actList);
			});
				$('.Forward em').on('touchstart',function(){
					share.init();
				});
				$(window).scroll(function(){
					info.lazyPicLoad('.videoPic');
					info.lazyPicLoad('.userPortrait');
				})
				info.videoUrl(serverUrlvid);
				info.personalPortrait(serverUrl,$('.userPortrait'));
				info.videoInfo(serverUrl,$('.videoPic'));
				info.timeTransform('.myDate','data-time','MM-dd hh:mm');

			$('.commit').each(function(){
				$(this).click(function(){
					var videoId = $(this).attr('data-video');
					window.location.href=info.getRequestPrefix()+'/page/video/videoDetail?videoId='+videoId+'&uid='+uid;
				})
			});
			$('.Forward').click(function(){
				attentionFn.shareFn($(this));

			});
			$('.zan').each(function(){
				if($(this).attr('data-praise') == 'true'){
					$(this).find('em').addClass('on');
				}
				$(this).click(function(){
					var zanNum = parseInt($(this).find('span').text());
					var ab=  $(this).attr('data-videoid');
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
					isNext: attentionFn.isNext,
					nextType: attentionFn.nextType
				}
			}else if(type == 'userId'){
				url= interUrl+'/page/user/userDetail?access_token='+accessToken+'&userId='+typeId;
				$("#toPerCener").attr('href',url);
				params= {
					uid: uid
				}
			}else if(type == 'attention'){
				url= interUrl+'/page/attention/getAttention?userId='+typeId+'&access_token='+accessToken;
				params= {
						uid: uid
					}
			}else if(type == 'dynamicRecommend'){
				url= interUrl+'/page/attention/dynamicRecommend?'+'access_token='+accessToken;
				params= {
						uid: uid
					}
			}else if(type == 'attentionVideoList'){
				url= interUrl+'/page/attention/attentionVideoList?'+'access_token='+accessToken;
				params= {
						uid: uid
					}
			}
			info.postform(url,params)
		},
//		seeMore:function(uid,accessToken){
//			var url = info.getRequestPrefix()+'/page/attention/dynamicRecommend?uid='+uid+'&access_token='+accessToken;
//			$('.seeMore a').attr('href',url);
//		}

	}

attentionFn.init();
})