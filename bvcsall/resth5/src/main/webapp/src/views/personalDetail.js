require(['../libs/jquery-2.1.4','../common/playbox2','../common/info','../common/share','../service/service','head','../common/aler','../common/browse'],function(jquery,playbox2,info,share,service,head,alert,browse){
	var serverUrlimg = info.getPicPerfix();
	var serverUrlvid = info.getVideoPrefix();
	var interUrl = info.getRequestPrefix();
	var uid = info.getCookie('uid');
	var userId = $('#attentionBtn').attr('data-userId'); //用户Id
	var personalDetail = {
		init:function(){
			info.personalInfo(serverUrlimg,'.userHomePic');
			info.faceFn($('.face'));
			$(window).scroll(function(){
				info.lazyPicLoad('.hotPic');
			});
			personalDetail.getPersonalList(userId);
			personalDetail.jump();
		},
		getPersonalList:function(userId){
			
			service.getLiveInfoByUserId(userId).done(function(json){
				
				var data = json.result;
				if(data ==''){
					var timestamp = 0;
					var count = 18;
				}else{
					var timestamp = 0;
					var count = 17;
				}
				$.each(data,function(m,n){
					var sHtml = '<li><a href="javascript:void(0)"><img data-userPic="'+ n.roomPic +'" data-roomId="'+ n.id +'"src="" class="hotPic"><img class="Icon" src="'+interUrl+'/img/icons-2x/live_icon.png"></a></li>';
					$('.personalList ul').append(sHtml);
//					$('.personalList ul li').css('height',$(window).width()/3);
//					$('.personalList ul li').css('width',$(window).width()/3);
//					$('.personalList ul').css('width',$(window).width()+6);
//					$('.personalList').css('height',$('.personalList ul').height());
				});
				info.personalPortrait(serverUrlimg,'.hotPic');
				$('.personalList ul li a img').on('click',function(){
					var roomId = $(this).attr('data-roomId');
					window.location.href = interUrl + '/live/shareLive?roomId='+roomId;
				});
			
			
			service.addMore(timestamp,count,userId).done(function(json){
				var data1 = json.result.videos;
				if(data1 == '' && data == ''){
					var sHtml = '<div style="width:100%;"><p style="width:100%;height:300px;line-height:300px;text-align:center;color:#999999;font-size:16px;background:#ffffff;">这个懒家伙竟然什么都没有发过~</p></div>';
					$('.personalList').append(sHtml);
				}else{
					
				
				$.each(data1,function(m,n){
					var sHtml = '';
					if(n.type == 1){
						var sHtml = '<li><a href="javascript:void(0)"><img data-userPic="'+ n.videoPic +'" data-videoId="'+ n.id +'"src="" class="hotPic video"><img class="Icon" src="'+interUrl+'/img/icons-2x/video_icon.png"></a></li>';
					}else if(n.type == 2){
						var sHtml = '<li><a href="javascript:void(0)"><img data-userPic="'+ n.videoPic +'" data-roomId="'+ n.liveNoticeId +'"src="" class="hotPic review"><img class="Icon" src="'+interUrl+'/img/icons-2x/review_icon.png"></a></li>';
					}else if(n.type == 3){
						var sHtml = '<li><a href="javascript:void(0)"><img data-userPic="'+ n.videoPic +'" data-videoId="'+ n.id +'"src="" class="hotPic video"><img class="Icon" src="'+interUrl+'/img/icons-2x/pic_icon.png"></a></li>';
					}else if(n.type == 4){
						var sHtml = '<li><a href="javascript:void(0)"><img data-userPic="'+ n.videoPic +'" data-noticeId="'+ n.liveNoticeId +'"src="" class="hotPic yugao"><img class="Icon" src="'+interUrl+'/img/icons-2x/yugao_icon.png"></a></li>';
					}
					$('.personalList ul').append(sHtml);
//					$('.personalList ul li').css('height',$(window).width()/3);
//					$('.personalList ul li').css('width',$(window).width()/3);
//					$('.personalList ul').css('width',$(window).width()+6);
//					$('.personalList').css('height',$('.personalList ul').height());
				});
				}
				info.personalPortrait(serverUrlimg,'.hotPic');
				$('.personalList ul li a img.review').on('click',function(){
					var roomId = $(this).attr('data-roomId');
					window.location.href = interUrl + '/live/shareLive?roomId='+roomId;
				});
				$('.personalList ul li a img.video').on('click',function(){
					var videoId = $(this).attr('data-videoId');
					window.location.href = interUrl + '/video/videoDetail?videoId='+videoId;
				});
				$('.personalList ul li a img.yugao').on('click',function(){
					var noticeId = $(this).attr('data-noticeId');
					window.location.href = interUrl + '/live/shareLiveNotice?noticeId='+noticeId;
				});
			});
			});
		},
		goPage: function(type,typeId){
			var url= ''
			var params= {};
			if(type == 'video'){
				url= interUrl+"/video/videoDetail?videoId="+typeId
				params= {
					uid: uid,
					userId:userId,
					isNext: personalDetail.isNext,
					nextType: personalDetail.nextType
				}
			}else if(type == 'userId'){
				url= interUrl+'/user/userDetail?userId='+typeId;
				params= {
					uid: uid
				}
			}else if(type == 'live'){
				url= interUrl+'/live/shareLive?roomId='+typeId;
				params= {
					uid: uid
				}
			}
			info.postform(url,params)
		},
		jump:function(obj){
			 $('.down-Btn a,.downloadBtn,.openwopai,.attenBtn,.bottomBanner a').click(function(){
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
		},
}
	personalDetail.init();
	
	
	
});
