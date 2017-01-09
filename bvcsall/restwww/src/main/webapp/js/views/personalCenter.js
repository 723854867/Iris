require(['../libs/jquery-2.1.4','../common/playbox2','../common/info','head','../common/ajax','../common/aler','../service/service'],function(jquery,playbox2,info,head,Ajax,alert,sever){
	var serverUrlimg = $('#serverUrlimg').val();
	info.personalPortrait(serverUrlimg,'.userPortrait');
	info.timeTransform('.myDate','data-time','MM-dd hh:mm');
	var uid = info.getCookie('uid');
	var accessToken = info.getCookie('access_token');
	var interfaceurl = $('#interfaceurl').val();
	var interUrl = info.getRequestPrefix();

	$('.waiting').on('click',function(){
		alert.alertMsg('模块开发中');
	})
	
	var personalCenter = {
		init:function(){
			$('window').scrollTop(0);
			var uidCallback = $('#uid').val();
			var access_tokenCallback = $('#access_token').val();
			if(uidCallback != '' &&  access_tokenCallback != ''){
				info.setCookie('uid',uidCallback,7);
				info.setCookie('access_token',access_tokenCallback,7);
				uid = uidCallback;
				accessToken = access_tokenCallback;
//				
//				var interfaceurl = $('#interfaceurl').val();
//				$('#toPerCener').attr('href',interfaceurl + '/restwww/page/user/userCenter'+'?uid='+uidCallback+'&access_token='+access_tokenCallback);
//				$('#toAttention').attr('href',interfaceurl + '/restwww/page/attention/attentionVideoList'+'?uid='+uidCallback+'&access_token='+access_tokenCallback);
			}
			
			personalCenter.logOut();
			$("#attentions").on('click',function(){
				var userId = $('#perCent').attr('data-userId');
				personalCenter.goPage('attention',userId);
			});
			$('#perCent').on('click',function(){
				var userId = $('#perCent').attr('data-userId');
				personalCenter.goPage('userId',userId);
			});
			$('#fans').on('click',function(){
				var userId = $('#perCent').attr('data-userId');
				personalCenter.goPage('fans',userId);
			});
			$('#feedBack').on('click',function(){
				//var userId = $('#feedBack').attr('data-userId');
				personalCenter.goPage('feedback');
			});
			$('.downClose').click(function(){ //cnzz关闭下载
				_czc.push(["_trackEvent","关闭个人中心下载按钮","closeBottomDownload"]);
			});
		},
		goPage: function(type,typeId){
			var url= ''
			var params= {};
			if(type == 'video'){
				url= interUrl+"/page/video/videoDetail?videoId="+typeId
				params= {
					uid: uid
				}
			}else if(type == 'userId'){
				url= interUrl+'/page/user/userDetail?userId='+typeId;
				$("#attentions").attr('href',url);
				params= {
					uid: uid
				}
			}else if(type == 'attention'){
				url= interUrl+'/page/attention/getAttention?userId='+typeId+'&access_token='+accessToken;
				params= {
						uid: uid
					}
			}else if(type == 'fans'){
				url= interUrl+'/page/attention/getFans?userId='+typeId+'&access_token='+accessToken;
				params= {
						uid: uid
					}
			}else if(type=='feedback'){
				url= interUrl+'/page/feedback/addFeedback?access_token='+accessToken;
				params= {
						uid: uid
					}
			}
			info.postform(url,params)
		},
		addPersonalCenterUrl:function(userId,uid){//添加跳转到个人主页url
			var url = interfaceurl + '/restwww/page/user/userDetail?'+'userId='+userId+'&uid='+uid;
			$("#perCent").attr('href',url);
		},
		addAttentionUrl:function(accessToken,uid){
			var url = interfaceurl + '/restwww/page/attention/getAttention?'+'access_token='+accessToken+'&uid='+uid;
			$("#attentions").attr('href',url);
		},
		addFanUrl:function(accessToken,uid){
			var url = interfaceurl + '/restwww/page/attention/getFans?'+'access_token='+accessToken+'&uid='+uid;
			$("#fans").attr('href',url);
		},
		logOut:function(){
			$('.logout').on('click',function(){
				var data_from = 'h5';
				info.clearCookie('login');
				sever.logOut(data_from).done(function(data){
			 			if (data["code"] == "200") { // code==200,表示请求成功
							info.clearCookie("uid");
							info.clearCookie("access_token");
							alert.alertMsg('退出成功');
			 				setInterval(function(){
			 					window.location.href="/restwww/page/homePage";
			 				},2000);
			 			} else {
			 				
			 			}
				});
			})
		}
	}
	personalCenter.init();
});