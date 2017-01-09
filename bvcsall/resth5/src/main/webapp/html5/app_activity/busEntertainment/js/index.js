$(function(){
	var interUrl = initFn.getRequestPrefix();
	var target = $('.content ul');
	var serverUrl = $('#serverUrlimg').val()+'/restwww/download';
	var uid = initFn.getCookie('uid');
	var accessToken = initFn.getCookie('access_token');
	var ajaxUrlAttentionList = interUrl + '/restwww/attention/getAttentionList';//关注列表api
	var Bus = {
			init:function(){
				var params = {'uid':uid,'access_token':accessToken,'timestamp':0,'count':20};
				var backurl = window.location.href;
				var arrObj = interUrl.split('//');
				var commonUrl = arrObj[1];
				var regPlatform;
				initFn.setCookie('backurl',encodeURIComponent(backurl),7);
				if(initFn.getCookie('channel')==''){
			 		regPlatform = 'h5';
				}else{
					regPlatform = 'h5-'+initFn.getCookie('channel');
				}
				
				if(accessToken == ''){
					window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2fresth5%2fthirdPart%2fwechatCallback&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect%3fregPlatform='+regPlatform;
					return;
				}else{
					
				}
				Bus.getAttentionList(ajaxUrlAttentionList,params);
				Bus.jumpDownload();
			},
			getAttentionList:function(ajaxUrlAttentionList,params){
				initFn.AJAX(ajaxUrlAttentionList,params).done(function(json){
					Bus.htmlFn(json.result);
				}).fail(function(error){
					if(error.code == '401' || error.code == '400_5' || error.code == '503'){
						Bus.clearCookie('uid');
						Bus.clearCookie('access_token');
						window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2fresth5%2fthirdPart%2fwechatCallback&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect%3fregPlatform='+regPlatform;
					}
				});
			},
			getAttentionList1:function(ajaxUrlAttentionList,params){
				initFn.AJAX(ajaxUrlAttentionList,params).done(function(json){
					Bus.htmlFn1(json.result);
				}).fail(function(error){
					if(error.code == '401' || error.code == '400_5' || error.code == '503'){
						Bus.clearCookie('uid');
						Bus.clearCookie('access_token');
						window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c59e9a110527d17&redirect_uri=http%3a%2f%2f'+commonUrl+'%2fresth5%2fthirdPart%2fwechatCallback&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect%3fregPlatform='+regPlatform;
					}
				});
			},
			htmlFn:function(data){
				if(data==''){
					$('body').css('background-color','#f7f0f1');
					$('body').append($('<img width="100%" style="margin-top:20px;" src="'+interUrl+'/resth5/html5/app_activity/busEntertainment/images/empty.png">'));
					$('.addMore').remove();
					
				}else if(data.length<20){
					$('.addMore').text('已经加载全部数据');
					$('.addMore').unbind('click');
				}else{
					$('.addMore').on('click',function(){
						var newTimestamp = $('.content ul li').eq($('.content ul li').length-1).find('.date').attr('data-time');
						var params = {'uid':uid,'access_token':accessToken,'timestamp':newTimestamp,'count':20};
						Bus.getAttentionList1(ajaxUrlAttentionList,params);
					})
				}
				$.each(data,function(m,n){
					if(n.isAttention == 1){
						var attenImg = '<a class="attentionBtn" href="javascript:void(0);"><img src="'+interUrl+'/resth5/html5/app_activity/busEntertainment/images/attention.png"></a>';
					}else if(n.isAttention == 2){
						var attenImg = '<a class="attentionBtn" href="javascript:void(0);"><img src="'+interUrl+'/resth5/html5/app_activity/busEntertainment/images/attentionBoth.png"></a>';
					}
					
					var shtml = $('<li><dl><dt><a href="javascript:void(0);"><img src='+serverUrl+n.pic+'><a/></dt>'+'<dd>'+n.name+'</dd>'+'<dd class="date" data-time="'+n.createDate+'"></dd>'+'</dl>'+attenImg+'</li>');
					target.append(shtml);
				});
				$('.date').each(function(){
					Bus.createTime($(this));
				});
				
				
			},
			htmlFn1:function(data){
				if(data=='' || data.length<20){
					$('.addMore').text('已经加载全部数据');
					$('.addMore').unbind('click');
				}else{
					$('.addMore').on('click',function(){
						var newTimestamp = $('.content ul li').eq($('.content ul li').length-1).find('.date').attr('data-time');
						var params = {'uid':uid,'access_token':accessToken,'timestamp':newTimestamp,'count':20};
						Bus.getAttentionList1(ajaxUrlAttentionList,params);
					})
				}
				$.each(data,function(m,n){
					if(n.isAttention == 1){
						var attenImg = '<a class="attentionBtn" href="javascript:void(0);"><img src="'+interUrl+'/resth5/html5/app_activity/busEntertainment/images/attention.png"></a>';
					}else if(n.isAttention == 2){
						var attenImg = '<a class="attentionBtn" href="javascript:void(0);"><img src="'+interUrl+'/resth5/html5/app_activity/busEntertainment/images/attentionBoth.png"></a>';
					}
					
					var shtml = $('<li><dl><dt><a href="javascript:void(0);"><img src='+serverUrl+n.pic+'><a/></dt>'+'<dd>'+n.name+'</dd>'+'<dd class="date" data-time="'+n.createDate+'"></dd>'+'</dl>'+attenImg+'</li>');
					target.append(shtml);
				});
				$('.date').each(function(){
					Bus.createTime($(this));
				});
				
				
			},
			createTime:function(obj){
				var noticeTime = obj.attr('data-time');
				var d = new Date();
				var date=new Date(parseInt(noticeTime));
				obj.text(fixZero(date.getMonth()+1,2)+"."+fixZero(date.getDate(),2)+" "+fixZero(date.getHours(),2)+":"+fixZero(date.getMinutes(),2));
				function fixZero(num,length){
					var str=""+num;
					var len=str.length;
					var s="";
					for(var i=length;i-->len;){
						s+="0";
					}
					return s+str;
				}
			},
			clearCookie:function(name){
				document.cookie = name+"=;expires=0;;path=/";
			},
			jumpDownload:function(){
				 $('body').on('touchstart','.content ul li dl',function(e){

					 e.preventDefault();
					 if(browser.versions.ios)
				        {
				        	if(browser.versions.isWeiBo){
				        		$('.skyBox').show();
					        }else{
					        	window.location.href = 'https://itunes.apple.com/us/app/wo-pai-wo-jiu-shi-zhu-jiao/id1077305226?ls=1&mt=8';
					        }
				        }
				        if(browser.versions.android)
				        {
				        	if(browser.versions.isWeiBo){
				        		$('.skyBox-anz').show();
					        }else{
					        	window.location.href = initFn.appDownUrl;
					        }
				        }
				        if(browser.versions.isWx)
				        {
				            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
				        }
				       
				    })
				    $('.skyBox,.skyBox-anz').click(function(){
				    	$(this).hide();
				    });
			}
	}
	Bus.init();
})