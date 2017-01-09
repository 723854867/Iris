require(['../libs/jquery-2.1.4','head','../common/info','../common/aler','../service/service'],function(jquery,head,info,alert,service){
	
	var uid = info.getCookie('uid');
	var interUrl = info.getRequestPrefix();
	var serverUrl = $('#serverUrlimg').val();
	var serverUrlvid = info.getVideoPrefix();
	var invite = {
			init:function(){
				info.timeTransform('.myDate','data-time','MM-dd hh:mm');
				info.personalPortrait(serverUrl,'.userPortrait');
				info.personalPortrait(serverUrl,'.userPortrait-1');
				info.videoInfo(serverUrl,'.videoPic');
				info.videoUrl(serverUrlvid);
				invite.picTap();
				$(window).scroll(function(){
					info.lazyPicLoad('.userPortrait');
					info.lazyPicLoad('.videoPic');
				})
			},
			picTap:function(){
				$('.userPortrait').on('click',function(){
					var userId = $(this).attr('data-userId');
					var uid = info.getCookie('uid');
					var url = info.getRequestPrefix() + '/page/user/userDetail?' + 'userId=' +userId +'&uid=' + uid;
					invite.Jump(userId,uid,url);
				})
			},
			Jump:function(userId,uid,url){
				if(userId==''){
					return;
				}else{
					window.location.href = url;
				}
			}
	}
	invite.init();
	
});