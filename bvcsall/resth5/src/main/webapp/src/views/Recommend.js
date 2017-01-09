require(['../libs/jquery-2.1.4','../common/myClick','../common/info','../service/service','head','../common/aler'],function(jquery,myClick,info,sever,head,alert){
    var serverUrlimg = info.getPicPerfix();
    var interUrl = info.getRequestPrefix();
	info.personalPortrait(serverUrlimg,'.userPortrait');
	var uid = info.getCookie('uid');
	
	var Recommend = {
			init:function(){
				Recommend.attention($('.playData em'));
				Recommend.allAttention($('.allAttention'));
				Recommend.checkOrientation();
			},
			attention:function(obj){//关注
				obj.on('click',function(){
					var userId = $(this).attr('data-userId'); //用户Id
					var isAttention = $(this).attr('data-isAttention');
					var dataFrom = 'h5'; //写上来源的标示
					if(uid== '0'){
						aler.alertMsg('未登录，请登录后操作！');
						setTimeout(function(){
							window.location.href= interUrl+'/page/user/login';
						},1000);

						return;
					}
					if(isAttention==0){
						var self = this;
						
						sever.addAttention(userId,dataFrom,isAttention).done(function(json){
							$(self).attr('data-isAttention','1');
							$(self).removeClass('attenBtn').addClass('attenBtnActive');

						})

					}else if(isAttention==1){
						var self = this;
						
						sever.addAttention(userId,dataFrom,isAttention).done(function(json){
							$(self).attr('data-isAttention','0');
							$(self).removeClass('attenBtnActive').addClass('attenBtn');
						})
					}
				})
			},
			allAttention:function(obj){//一键关注
				obj.on('click',function(){
					var str = '';
					var uid = info.getCookie('uid');
					
					var dataFrom = 'h5'; //写上来源的标示
					$(this).parent().siblings().find('a').find('em').each(function(i){
						var userId = $(this).attr('data-userId'); //用户Id
						
						str += userId+',';
						
					});
					var len = str.length;
					str= str.substring(0,len-1);
					if(uid== '0'){
						aler.alertMsg('未登录，请登录后操作！');
						setTimeout(function(){
							window.location.href= interUrl+'/restwww/page/user/login';
						},500);
				
						return;
					}
					var self = $(this);
					sever.allAttention(str,dataFrom).done(function(json){
						
						$(self).parent().siblings().find('a').find('em').attr('data-isAttention',1);
						$(self).parent().siblings().find('a').find('em').removeClass('attenBtn').addClass('attenBtnActive');
						alert.alertMsg('关注成功');
						setTimeout(function(){
							window.location.href=info.getRequestPrefix()+'/page/homePage';
						},500);
					})
				})
			},
			checkOrientation:function(){
				window.onorientationchange=function(){
					if(window.orientation==0 || window.orientation==180){
					        $('.bgBox2').hide().siblings().show();
					        $('#cnzz_stat_icon_1256005299').hide();
				    }else
				    {
				        $('.bgBox2').show().siblings().hide();
				        
				    }
				};
				window.orientationchange=function(){
					if(window.orientation==0 || window.orientation==180){
				        $('.bgBox2').hide().siblings().show();
				        $('#cnzz_stat_icon_1256005299').hide();
				    }else
				    {
				        $('.bgBox2').show().siblings().hide();

				    }
				};
			}
	}
	Recommend.init();
});