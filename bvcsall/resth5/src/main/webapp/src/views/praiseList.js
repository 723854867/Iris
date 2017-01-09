require(['../libs/jquery-2.1.4','head','../common/info','../common/aler','../service/service'],function(jquery,head,info,alert,service){
	
	var uid = info.getCookie('uid');
	var interUrl = info.getRequestPrefix();
	var arr=[];
	var MyVideoPraiseList = {
			init:function(){
				//点击头像跳转
				$('.userPortrait').on('click',function(){
					var userId = $(this).attr('data-userId');
					if(userId==''){
						return;
					}else{
						personalDetail.goPage('userId',userId);
					}
				})
				
				var serverUrlimg = $('#serverUrlimg').val();
				MyVideoPraiseList.renderPraiseList();
				$('#btnMore').on('click',function(){
					MyVideoPraiseList.addMore();
				});

				$('.downClose').click(function(){ //cnzz关闭下载
					_czc.push(["_trackEvent","关闭点赞的人下载按钮","closeBottomDownload"]);
				});
				
			},
			picTap:function(){
				$('.userPortrait').on('click',function(){
					var userId = $(this).attr('data-userId');
					var uid = info.getCookie('uid');
					var url = info.getRequestPrefix() + '/page/user/userDetail?' + 'userId=' +userId +'&uid=' + uid;
					MyVideoPraiseList.Jump(userId,uid,url);
				})
			},
			Jump:function(userId,uid,url){
				if(userId==''){
					return;
				}else{
					window.location.href = url;
				}
			},
			renderPraiseList:function(){
				var vidUrl = window.location.search;
				var vid = parseInt(vidUrl.split('=')[1]);
				var minPraiseId = 0;
				var count = 10;
				service.getVideoPraiseList(vid,minPraiseId,count).done(function(json){
					MyVideoPraiseList.myPraiseList(json.result,'.recommendContent');
				});
			},
			myPraiseList:function(data,target){
				if(data==""){
					alert.alertMsg('竟然没有已经赞的人了');
				}
				
				$.each(data,function(m,n){
					var praiseId=n.praiseId;
					var vstat;
					if(n.vipStat==1){
						vstat='<img class="user_icon_blue" src="'+info.getRequestPrefix()+'/img/icons-2x/user_icon_blue.png" />';
					}else if(n.vipStat==2){
						vstat='<img class="user_icon_yellow" src="'+ info.getRequestPrefix()+'/img/icons-2x/user_icon_yellow.png" />';
					}else if(n.vipStat==3){
						vstat='<img class="user_icon_green" src="'+ info.getRequestPrefix()+'/img/icons-2x/user_icon_green.png" />';
					}else if(n.vipStat==0){
						vstat='';
					}
					var signatures;
					if(n.signature==""){
						signatures="懒人没前途o(∩_∩)o 哈哈~";
					}else{
						signatures=n.signature;
					}
					var attenReturn;
					if(n.attention==0){
						attenReturn='attenBtn';
					}else if(n.attention==1){
						attenReturn='attenBtnActive';
					}
					var PraiseList = '<div>'+'<img class="fl userPortrait" data-userId="'+n.id+'" data-userPic="'+n.pic+'" alt="" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+vstat+
					'<span class="reTitle">'+n.name+'</span><br>'+'<span class="reName">'+signatures+'</span>'+
					'<a class="atten" href="javascript:void(0);"><em class="'+attenReturn+' att" data-isAttention="'+n.attention+'" data-userid="'+n.id+'"></em></a>'+'</div>';
					$(target).append(PraiseList);
					arr.push(praiseId);
				});
				info.personalPortrait(serverUrlimg,'.userPortrait');
				var obj = $('.atten');
				var obj1 = $('.att');
				$('.atten').each(function(){
					if($(this).find('em').attr('data-userId')==uid){
						$(this).hide();
						$(this).prev().text("懒人没前途o(∩_∩)o 哈哈~");
					}
				})
				
				MyVideoPraiseList.tapPraise(obj,obj1,uid);
				MyVideoPraiseList.picTap();
			},
			tapPraise:function(obj,obj1,uid,dataFrom,isAttention){
				obj1.click(function(){
					var dataFrom = 'h5'; //写上来源的标示
					var userId =  $(this).attr('data-userId');
					var isAttention = $(this).attr('data-isAttention');
					if(uid== 0){
						alert.alertMsg('未登录，请登录后操作！');
						setTimeout(function(){
							window.location.href= interUrl+'/page/user/login';
						},1000);
						return;
					}
					
					if(isAttention==0){
						var self = this;
						
						service.addAttention(userId,dataFrom,isAttention).done(function(json){
							
							$(self).attr('data-isAttention','1');
							$(self).removeClass('attenBtn').addClass('attenBtnActive');

						})

					}else if(isAttention==1){
						var self = this;
						
						service.addAttention(userId,dataFrom,isAttention).done(function(json){
							
							$(self).attr('data-isAttention','0');
							$(self).removeClass('attenBtnActive').addClass('attenBtn');
						})
					}
				});
			},
			addMore:function(){
				var vidUrl = window.location.search;
				var vid = vidUrl.split('=')[1];
				var minPraiseId = arr.sort()[0];
				console.log(arr.sort()[0]);
				var count = 20;
				service.getVideoPraiseList(vid,minPraiseId,count).done(function(json){
					MyVideoPraiseList.myPraiseList(json.result,'.recommendContent');
				});
			}
	}
	
	MyVideoPraiseList.init();
	
});