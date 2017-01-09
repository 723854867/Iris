require(['../libs/jquery-2.1.4','../common/playbox2','../common/info','../common/share','../service/service','head','../common/aler'],function(jquery,playbox2,info,share,service,head,alert){
	var serverUrlimg = info.getPicPerfix();
	var serverUrlvid = info.getVideoPrefix();
	var interUrl = info.getRequestPrefix();
	var uid = info.getCookie('uid');
	var userId = $('#attentionBtn').attr('data-userId'); //用户Id
	if($('.videoList1 ul li').length == 0){
		$('#btnMore').hide();
		alert.alertMsg('竟然一条视频都没有');
	}
	
	var personalDetail = {
		init:function(){

			_this= this;
			_this.isNext= 0;
			_this.nextType= 6;

			//点击头像跳转
			$('.userPortrait').on('click',function(){
				var userId = $(this).attr('data-userId');
				if(userId==''){
					return;
				}else{
					personalDetail.goPage('userId',userId);
				}
				
			});
			
			
			info.personalInfo(serverUrlimg,'.userHomePic');
			info.videoInfo(serverUrlimg,'.videoPic');
			info.videoUrl(serverUrlvid);
			info.personalPortrait(serverUrlimg,'.userPortrait');
			info.personalPortrait(serverUrlimg,'.userPortraits');
			info.timeTransform('.myDate','data-time','MM-dd hh:mm');
			info.faceFn($('.face'));
			$(window).scroll(function(){
				info.lazyPicLoad('.videoPic');
				info.lazyPicLoad('.userPortraits');
				info.lazyPicLoad('.userPortrait');
			})
			//关注
			var obj = $('.playData');
			var obj1 = $('.playData em');
			personalDetail.attention(obj,obj1,userId,uid);
			//分享
			$('.Forward').click(function(){
				personalDetail.shareFn($(this));
			});
			//添加评论跳转url
			$('.commit').on('click',function(){
				var videoId = $(this).find('a').attr('data-video');
				//var userId = $(this).parent().attr('data-createrId');
				info.setCookie('userId',userId);
				personalDetail.goPage('video',videoId,userId);
			});
			$('.downClose').click(function(){ //cnzz关闭下载
				_czc.push(["_trackEvent","关闭个人详情下载按钮","closeBottomDownload"]);
			});
			//转发到我拍 后期要优化这个方法
			$('body').delegate('.sharewp','click',function(){
				if(uid== 0){
					alert.alertMsg('未登录，请登录后操作！');
					setTimeout(function(){
						window.location.href= interUrl+'/page/user/login';
					},1000);

					return;
				}
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
			//赞
			$('.zan em').on('click',function(){
				var zanNum = $(this).next().val();
				var zanNum = parseInt($(this).next().text());
				var ab=  $(this).parent().attr('data-videoid');
				var bc = $(this).parent().parent().attr('data-createrId');
				if(uid== 0){
					alert.alertMsg('未登录，请登录后操作！');
					setTimeout(function(){
						window.location.href= interUrl+'/page/user/login';
					},1000);

					return;
				}

				if($(this).parent().attr('data-praise') == 'false' || $(this).parent().attr('data-praise') == false){
					//点赞
					var self = this;
					service.savePraise(ab).done(function(json) {
		    			$(self).parent().attr('data-praise',true);
		    			info.tabLove(self,zanNum);
		            }).fail(function(json){
						alert.alertMsg("请登录后操作");
						setInterval(function(){
							window.location.href = info.getRequestPrefix()+"/page/user/login";
						},1000)
					});
				}
				
				if($(this).parent().attr('data-praise') == 'true' || $(this).parent().attr('data-praise') == true){
					 //取消赞
					var self = this;
					service.deletePraise(ab).done(function(json) {
		    			$(self).parent().attr('data-praise', false);
		    			info.tabLove(self,zanNum);
		            }).fail(function(json){
						alert.alertMsg("请登录后操作");
						setInterval(function(){
							window.location.href = info.getRequestPrefix()+"/page/user/login";
						},1000)
						
					});
				}
			});
			$('#btnMore').on('click',function(){
				var userid = $('.shareBox').attr('data-createrId') || 0;
				var count = 5;
				var lengtha =  $('.shareBox').length;
				var timestamp = $('.shareBox').eq(lengtha-1).attr('data-createDate');
				if(userid == 0){
					alert.alertMsg("没有视频了");
					return;
				}
				
				service.addMore(timestamp,count,userid).done(function(json) {
					personalDetail.personalVideoMore(json.result,'.videoList1 ul');
		        	info.personalInfo(serverUrlimg,'.userHomePic');
		        	info.videoInfo(serverUrlimg,'.videoPic');
		        	info.videoUrl(serverUrlvid);
		        	info.personalPortrait(serverUrlimg,'.userPortrait');
		        	info.timeTransform('.myDate','data-time','MM-dd hh:mm');
		        	personalDetail.addCommentUrl($('.commit'));
		        }).fail(function(){
		        	alert.alertMsg('请重新登录');
		        	setInterval(function(){
		        		window.location.href = info.getRequestPrefix()+"/page/user/login";
		        	},1000);
		        	
		        });
			})
		},	
		Jump:function(userId,uid,url){
			if(userId==''){
				return;
			}else{
				window.location.href = url;
			}
		},
		attention:function(obj,obj1,userId,uid,dataFrom,isAttention){
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

					})

				}else if(isAttention==1){
					service.addAttention(userId,dataFrom,isAttention).done(function(json){
						
						$('#attentionBtn').attr('data-isAttention',0);
						$('#attentionBtn').removeClass('attenBtnActive').addClass('attenBtn');
					})
				}
			});
		},
		shareFn: function(obj){
			var tit = obj.attr('data-name')
			var desc = obj.attr('data-desc')
			var pic = interUrl +$(this).attr('data-videopic');//缩略图
			var videoId = obj.attr('data-videoId');
			var userId = obj.attr('data-userId');
			share.init(tit,desc,pic,videoId,uid,userId);
		},
		addCommentUrl:function(commitObj){
			
			commitObj.each(function(){
				var interfaceurl = info.getRequestPrefix() + '/page/video/videoDetail';
				var videoId = $(this).find('a').attr('data-video');
				
				var url = interfaceurl + '?videoId='+videoId;
				$(this).find('a').attr('href',url);
			});
		},
		
		goPage: function(type,typeId,userId){
			var url= ''
			var params= {};
			if(type == 'video'){
				url= interUrl+"/page/video/videoDetail?videoId="+typeId
				params= {
					uid: uid,
					userId:userId,
					isNext: personalDetail.isNext,
					nextType: personalDetail.nextType
				}
			}else if(type == 'userId'){
				url= interUrl+'/page/user/userDetail?userId='+typeId;
				params= {
					uid: uid
				}
			}
			info.postform(url,params)
		},
		personalVideoMore:function(json,target){
			var userMsg = json.user;
			var data = json.videos;	
			if(data==""){
				alert.alertMsg('竟然没有视频了');
			}
			$.each(data,function(m,n){
				if(n.praise == false){
					flag="off";
				}else{
					flag="on";
				}
				var shtml = '<li>'+
				'<div class="listUserMsg">'+
					'<dl>'+
						'<dt><img class="userPortrait" data-userPic="'+userMsg.pic+'"  alt="" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'"><em class="addV"></em></dt>'+
						'<dd>'+
							'<p class="personalName">'+userMsg.name+'</p>'+
							'<p class="myDate" data-time="'+n.createDate+'"></p>'+
						'</dd>'+
						'<dd>'+
							'<em class="playIcon"></em><span class="playData1">'+n.showPlayCount+'</span>'+
						'</dd>'+
					'</dl>'+
				'</div>'+
				'<div class="playBox" data-videoKey="'+n.playKey+'" data-videoId="'+n.id+'">'+
				'<div class="bgBox"></div>'+
				'<div class="loadingGif" style="width:30px;height:30px;min-height:30px;position:absolute;left:0;top:0;display:none;z-index:999999;background-color:#000000;opacity:0.6;width:100%;height:50%;color:#ffffff;text-align:center;padding-top:50%;font-size:18px;"></div>'+
					'<img class="videoPic" src=""  alt="" data-videoPic="'+n.videoPic+'" onerror="this.src=\'/restwww/img/portrait.png\'">'+
					

					'<em class="canPlay" data-url="'+n.url+'"></em>'+
				'</div>'+'<span class="videoDiscription face">'+n.description+'</span>'+
				'<div class="shareBox" data-createrId="'+n.id+'" data-createDate="'+n.createDate+'">'+
					'<div class="zan" data-praise="'+n.praise+'" data-videoid="'+n.id+'">'+
						'<em class="'+flag+'"></em>'+
						'<span>'+n.praiseCount+'</span>'+
					'</div>'+
					'<div class="commit">'+
						'<a href="javascript:;" data-video="'+n.id+'">'+
							'<em></em>'+
						'</a>'+
						'<span>'+n.evaluationCount+'</span>'+
					'</div>'+
					'<div class="Forward" data-name="'+userMsg.name+'" data-userId="'+ userMsg.id+'" data-videoid="'+ n.id+'" data-desc="'+n.description+'" data-videoPic="'+ n.videoPic+ '">'+
						'<em></em>'+
					'</div>'+
				'</div>'+
			'</li>';
				$(target).append(shtml);
			});
			info.faceFn($('.face'));
			info.playNum($('.playData1'));
			$('.Forward').click(function(){
				personalDetail.shareFn();
			})
			$('.zan em').on('touchstart',function(Turl){
				var zanNum = $(this).next().val();
				var zanNum = parseInt($(this).next().text());
				var ab=  $(this).parent().attr('data-videoid');
				var bc = $(this).parent().parent().attr('data-createrId');
				if(uid== 0){
					alert.alertMsg('请登录后操作');
					setTimeout(function(){
						window.location.href= interUrl+'/page/user/login';
					},1000);

					return;
				}
				if($(this).parent().attr('data-praise') == 'false' || $(this).parent().attr('data-praise') == false){
					//点赞
					var self = this;
					service.savePraise(ab).done(function(json) {
		    			$(self).parent().attr('data-praise',true);
		    			info.tabLove(self,zanNum);
		            }).fail(function(json){
						alert.alertMsg("请登录后操作");
						setInterval(function(){
							window.location.href = info.getRequestPrefix()+"/page/user/login";
						},1000)
					});
				}
				
				if($(this).parent().attr('data-praise') == 'true' || $(this).parent().attr('data-praise') == true){
					 //取消赞
					var self = this;
					service.deletePraise(ab).done(function(json) {
		    			$(self).parent().attr('data-praise', false);
		    			info.tabLove(self,zanNum);
		            }).fail(function(json){
						alert.alertMsg("请登录后操作");
						setInterval(function(){
							window.location.href = info.getRequestPrefix()+"/page/user/login";
						},1000)
						
					});
				}
			});
	},
	getEnd:function(video){
		var end = 0
		  try {
		    end = video.buffered.end(0) || 0
		    end = parseInt(end * 1000 + 1) / 1000
		  } catch(e) {
		  }
		  return end
	}
}
	personalDetail.init();
	
	
	
});
