
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','head','../common/share'], function(jquery,info,alert,service,head,share) {


var uid = info.getCookie('uid');
var access_token = info.getCookie('access_token');
var attentionFn = {
		init:function(){
			info.personalPortrait(serverUrlimg,'.userPortrait');
			//加减关注
			var obj = $('.atten');
			var obj1 = $('.atten em');
			
			attentionFn.tapAttention(obj,obj1,uid);
			attentionFn._allAttention();
			attentionFn.picTap();

			$('.downClose').click(function(){ //cnzz关闭下载
				_czc.push(["_trackEvent","关闭感兴趣的人下载按钮","closeBottomDownload"]);
			});
		},
		picTap:function(){
			$('.userPortrait').on('click',function(){
				var userId = $(this).attr('data-userId');
				var url = info.getRequestPrefix() + '/page/user/userDetail?' + 'userId=' +userId;
				attentionFn.Jump(userId,url);
			})
		},
		Jump:function(userId,url){
			if(userId==''){
				return;
			}else{
				window.location.href = url;
			}
		},
		tapAttention:function(obj,obj1,uid,dataFrom,isAttention){
			obj1.click(function(){
				var dataFrom = 'h5'; //写上来源的标示
				var userId =  $(this).attr('data-userId');
				var isAttention = $(this).attr('data-isAttention');
				if(uid== 0){
					alert.alertMsg('未登录，请登录后操作！');
					setTimeout(function(){
						window.location.href= interUrl+'/restwww/page/user/login';
					},1000);

					return;
				}
				
				if(isAttention==0){
					var self = this;
					
					service.addAttention(userId,dataFrom,isAttention).done(function(json){
						$(self).removeClass('attenBtn').addClass('attenBtnActive');
						$(self).attr('data-isAttention','1');
						alert.alertMsg('关注成功');

					})
				}
			});
		},
		_allAttention:function(){
			$('.allAttention').one('click',function(){
				var str = '';
				var uid = info.getCookie('uid');
				
				var dataFrom = 'h5'; //写上来源的标示
				$(this).parent().parent().siblings().find('a').find('em').each(function(i){
					var userId = $(this).attr('data-userId'); //用户Id
					
					str += userId+',';
					
				});
				var len = str.length;
				str= str.substring(0,len-1);
				if(uid== '0'){
					aler.alertMsg('未登录，请登录后操作！');
					setTimeout(function(){
						window.location.href= interUrl+'/restwww/page/user/login';
					},1000);
			
					return;
				}
				var self = $(this);
				service.allAttention(str,dataFrom).done(function(json){
					$(self).parent().parent().hide();
					$(self).parent().parent().siblings().find('a').find('em').attr('data-isAttention',1);
					$(self).parent().parent().siblings().find('a').find('em').removeClass('attenBtn').addClass('attenBtnActive');
					alert.alertMsg('关注成功');
				})
			})
		}
}
attentionFn.init();
})