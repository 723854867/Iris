define(['../libs/jquery-2.1.4','../common/newAlert','../common/info','../common/share','../common/browse','../service/service'],function(jquery,alert,info,share,browse,service){
	var serverUrl = info.getPicPerfix();
	var interUrl = info.getRequestPrefix();
	var uid = info.getCookie('uid');
	var noticeId = $('#noticeId').val();
	var userId = $('#userId').val();
	var videoId = $('#videoId').val();
	var liveForenotice = {
		init:function(){
			liveForenotice.jump();
			$('body').css('height',$(window).height());
			info.videoInfo(serverUrl,$('.videoPic'));
			info.personalPortrait(serverUrl,$('.userPortrait-1'));
			$('.playData').on('click',function(){
				liveForenotice.goPage('userId',userId);
			});
			//分享
			$('.Forward').click(function(){
				liveForenotice.shareFn($(this));
			});
			//获取直播列表
			liveForenotice.roomList();
			//lazyload
			$(window).scroll(function(){
				info.lazyPicLoad($('.videoListBox li a img'));
				info.lazyPicLoad($('.hotUesrPic'));
			});
			//评论
			service.getVidEoeva(0,1,videoId,8).done(function(json){
				if(json.result == '')
				{
					$('.evalutionListCon').html('<p class="emptydata">暂无评论</p>');
					$('.evalutionListCon p').css('text-indent','10px');
				}
				liveForenotice.makeEvaluationList(json.result,$('.evalutionListCon'),false);
			});
			$('.zan em').on('click',function(){
				window.location.href = 'http://wopaitv.com';
			});
//			liveForenotice.shareFn();
			liveForenotice.createTime($('.myDate'));
			liveForenotice.noticeTime($('.noticeTime'));
//			$('.topBanner,.bottomBanner a').on('click',function(){
//				if (browse.versions.ios) {
//					if(browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
//						$('.skyBox').show();
//					} else {
//						$(this).attr("href","wopai://type=notice&userId=" + userId);
//						timer();
//					}
//				} else if (browse.versions.android){
//					if (browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
//						$('.skyBox-anz').show();
//					} else {
//						$(this).attr("href","com.busap.myvideo://main/openwith?userId="+userId+"&type=12");
//						timer();
//					}
//				} else {
//					window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
//				};
//
//				function timer(){
//					setTimeout(function(){
//						window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
//					},3000)
//				}
//			});
			$('.skyBox,.skyBox-anz,.skyBox1').on('click',function(){
				$(this).hide();
			})
		},
		shareFn: function(){
			var tit = $('#name').val();
			var desc = $('#description').val();
			var pic = serverUrl+$('#pic').val();//缩略图
			var noticeId = $('#noticeId').val();
			share.init(tit,desc,pic,noticeId);
		},
		goPage: function(type,typeId){
			var url= '';
			var params= {};
			if(type == 'userId'){
				url= interUrl+'/user/userDetail?userId='+typeId;
				params= {
					uid: uid
				}
			}
			info.postform(url,params);
		},
		noticeTime:function(obj){
			var noticeTime = obj.attr('data-time');
			var d = new Date();
			var date=new Date(parseInt(noticeTime));
			obj.text('直播时间：'+ date.getFullYear()+"-"+fixZero(date.getMonth()+1,2)+"-"+fixZero(date.getDate(),2)+" "+fixZero(date.getHours(),2)+":"+fixZero(date.getMinutes(),2));
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
		roomList:function(){
			var page = 1,size = 6,type = 1,target = $('.videoListBox ul');
			service.getLiveAndRecordList(page,size,type).done(function(json){
				liveForenotice.makeLiveList(json.result,target);
//				if(json.result.length<6){
//					liveForenotice.makeLiveList(json.result,target);
//					var page = 1,size = 6-json.result.length,isLive = 2,userId = '',target = $('.videoListBox ul');
//					service.getRoomList(page,size,isLive,userId).done(function(json1){
//						liveForenotice.makeLiveList(json1.result,target);
//					});
//				}else{
//					
//				}
			});
		},
		makeLiveList:function(data,target){
			var liveList = data.liveList;
			$.each(liveList,function(m,n){
				var vStart;
				if(n.anchorVstat == 1){
					vStart= '<em class="userV1"></em>';

				}else if(n.anchorVstat == 2){
					vStart= '<em class="userV2"></em>';
				}else if(n.anchorVstat == 3){
					vStart= '<em class="userV3"></em>';
				}else{
					vStart= '';
				};
				var liveName;
				if(!n.title){
					liveName = n.anchorName;
				}else{
					liveName = n.title;
				}
				var tempList= '<li>'+
				'<a href="javascript:void(0)"><img data-userPic="'+ n.roomPic +'" data-roomId="'+ n.id +'"src="" class="hotPic"></a>'+'</li>';
				target.append(tempList);
			});
			if(liveList.length<6){
				var backList = data.backList;
				$.each(backList,function(m,n){
					var vStart;
					if(n.anchorVstat == 1){
						vStart= '<em class="userV1"></em>';
					}else if(n.anchorVstat == 2){
						vStart= '<em class="userV2"></em>';
					}else if(n.anchorVstat == 3){
						vStart= '<em class="userV3"></em>';
					}else{
						vStart= '';
					};
					var liveName;
					if(!n.title){
						liveName = n.anchorName;
					}else{
						liveName = n.title;
					}
					var tempList= '<li>'+
					'<a href="javascript:void(0)"><img data-userPic="'+ n.videoPic +'" data-roomId="'+ n.liveNoticeId +'"src="" class="hotPic"></a>'+'</li>';
					target.append(tempList);
				});
			}
			
			
			
			
			$('.videoListBox ul li').css('height',$(window).width()/3);
			$('.videoListBox ul li').css('width',$(window).width()/3);
			$('.videoListBox ul').css('width',$(window).width()+6);
			$('.videoListBox').css('height',$('.videoListBox ul').height());
			info.personalPortrait(serverUrl,$('.hotPic'));
			info.personalPortrait(serverUrl,$('.hotUesrPic'));
			$('.videoListBox ul li a').on('click',function(){
				window.location.href = interUrl + '/live/shareLive?roomId=' + $(this).find('img').attr('data-roomId');
			});
		},
		makeEvaluationList: function(data,target,clear){ //最新评论
			if(clear){
				$(target).empty();
			}
			$.each(data,function(m,n){
				var vStart;
				if(n.user.vipStat == 1){
					vStart= '<em class="userV1"></em>';

				}else if(n.user.vipStat == 2){
					vStart= '<em class="userV2"></em>';
				}else if(n.user.vipStat == 3){
					vStart= '<em class="userV3"></em>';
				}else{
					vStart= '';
				};
				var tempList = '<dl>'+
					'<dt><img class="evaluationList go-user-page" data-userId="'+ n.user.id+'" data-userPic="'+ n.user.pic +'" src="" alt="头像" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
					vStart+'</dt>'+
					'<dd><p>'+ n.user.name+'</p><p class="face">'+ n.content +'</p>'+
					'</dd><dd class="myDate" data-time="'+ n.createDate+'"></dd></dl>';
				target.append(tempList);

			});
			info.personalPortrait(serverUrl,$('.evaluationList'));
			info.timeTransform('.myDate','data-time','MM-dd hh:mm');
			info.faceFn($('.face'));
		},
		jump:function(){
			 $('.down-Btn a,.downloadBtn,.openwopai,.bottomBanner a').click(function(){
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
				        	window.location.href = 'http://wopaitv.com/shengdan/myVideo_wopaichunwan_2.0.0.apk';
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
		}
	}
	liveForenotice.init();
})