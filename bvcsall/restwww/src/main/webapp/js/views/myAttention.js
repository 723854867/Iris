require(['../libs/jquery-2.1.4','head','../common/info','../common/aler','../service/service'],function(jquery,head,info,alert,service){
	
	var uid = info.getCookie('uid');
	var myAttention = {
			init:function(){
				info.scrollTop();
				var serverUrlimg = $('#serverUrlimg').val();
				myAttention.renderAttentionList();
				$('#btnMore').on('click',function(){
					myAttention.addMore();
				});

				$('.downClose').click(function(){ //cnzz关闭下载
					_czc.push(["_trackEvent","关闭我的关注下载按钮","closeBottomDownload"]);
				});
			},
			picTap:function(){
				$('.userPortrait').on('click',function(){
					var userId = $(this).attr('data-userId');
					var uid = info.getCookie('uid');
					var url = info.getRequestPrefix() + '/page/user/userDetail?' + 'userId=' +userId +'&uid=' + uid;
					myAttention.Jump(userId,uid,url);
				})
			},
			Jump:function(userId,uid,url){
				if(userId==''){
					return;
				}else{
					window.location.href = url;
				}
			},
			renderAttentionList:function(){
				var lastId = 0;
				var count = 20;
				var dataBox = $('.recommendContent');
				service.getAttentionList(lastId,count).done(function(json){
					myAttention.myAttentionList1(json.result,dataBox);
				});
			},
			myAttentionList1:function(data,target){
				if(data==""){
					$('#btnMore').hide();
					alert.alertMsg('您没有关注任何人');
				}
				$.each(data,function(m,n){
					var vstat;
					if(n.vstat==1){
						vstat='<img class="user_icon_blue" src="'+info.getRequestPrefix()+'/img/icons-2x/user_icon_blue.png" />';
					}else if(n.vstat==2){
						vstat='<img class="user_icon_yellow" src="'+ info.getRequestPrefix()+'/img/icons-2x/user_icon_yellow.png" />';
					}else if(n.vstat==3){
						vstat='<img class="user_icon_green" src="'+ info.getRequestPrefix()+'/img/icons-2x/user_icon_green.png" />';
					}else if(n.vstat==0){
						vstat='';
					}
					
					var attenList = '<div>'+'<img class="fl userPortrait" data-userPic="'+n.pic+'" alt="" data-userId="'+n.attentionId+'" onerror="this.src=\'../../img/portrait.png\'">'+vstat+
					'<span class="reTitle">'+n.name+'</span><br>'+'<span class="reName">人气：'+n.dayPopularity+'</span>'+
					'<a class="atten" href="javascript:void(0);"><em class="attenBtnActive att" data-isAttention="'+n.isAttention+'" data-id="'+n.id+'" data-userid="'+n.attentionId+'" data-createDate="'+n.createDate+'"></em></a>'+'</div>';

					$(target).append(attenList);
					
				});
				info.personalPortrait(serverUrlimg,'.userPortrait');
				var obj = $('.atten');
				var obj1 = $('.att');
				
				myAttention.tapAttention(obj,obj1,uid);
				myAttention.picTap();
			},
			myAttentionList:function(data,target){
				console.log(data.length);
				if(data==""){
					
					alert.alertMsg('竟然没有已经关注的人了');
				}
				
				$.each(data,function(m,n){
					var vstat;
					if(n.vstat==1){
						vstat='<img class="user_icon_blue" src="'+info.getRequestPrefix()+'/img/icons-2x/user_icon_blue.png" />';
					}else if(n.vstat==2){
						vstat='<img class="user_icon_yellow" src="'+ info.getRequestPrefix()+'/img/icons-2x/user_icon_yellow.png" />';
					}else if(n.vstat==3){
						vstat='<img class="user_icon_green" src="'+ info.getRequestPrefix()+'/img/icons-2x/user_icon_green.png" />';
					}else if(n.vstat==0){
						vstat='';
					}
					
					var attenList = '<div>'+'<img class="fl userPortrait" data-userPic="'+n.pic+'" alt="" data-userId="'+n.attentionId+'" onerror="this.src=\'../../img/portrait.png\'">'+vstat+
					'<span class="reTitle">'+n.name+'</span><br>'+'<span class="reName">人气：'+n.dayPopularity+'</span>'+
					'<a class="atten" href="javascript:void(0);"><em class="attenBtnActive att" data-isAttention="'+n.isAttention+'" data-id="'+n.id+'" data-userid="'+n.attentionId+'" data-createDate="'+n.createDate+'"></em></a>'+'</div>';

					$(target).append(attenList);
					
				});
				info.personalPortrait(serverUrlimg,'.userPortrait');
				var obj = $('.atten');
				var obj1 = $('.att');
				
				myAttention.tapAttention(obj,obj1,uid);
				myAttention.picTap();
			},
			tapAttention:function(obj,obj1,uid,dataFrom,isAttention){
				$('.att').attr('data-isAttention',1);
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
				var count = 20;
				var lengtha =  $('.recommendContent div').length;
				var lastId = $('.recommendContent div').eq(lengtha-1).find('em').attr('data-id');
				var dataBox = $('.recommendContent');
				service.getAttentionList(lastId,count).done(function(json){
					myAttention.myAttentionList(json.result,dataBox);
				});
			}
	}
	
	myAttention.init();
	
});