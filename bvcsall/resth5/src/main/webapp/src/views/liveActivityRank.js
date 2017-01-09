define(['../libs/jquery-2.1.4','../common/info','../common/share','../common/browse','../service/service'],function(jquery,info,share,browse,service){
	var serverUrl = info.getPicPerfix();
	var interUrl = info.getRequestPrefix();
	var liveActivityId = $('#liveActivityId').val();
	var liveActivityRank = {
		init:function(){
			var latest = $('.latest');
			var rank = $('.rank');
			var rankContent = $('.rankContent');
			var latestContent = $('.latestContent');
			liveActivityRank.renderFun();
			liveActivityRank.swiperFn(latest,rank,rankContent,latestContent);
			liveActivityRank.hotList(rankContent);
			liveActivityRank.newList(latestContent);
			liveActivityRank.jump();
			$(window).scroll(function(){
				if($(window).scrollTop()>= $('.head').height()+$('.main').height()+8){
					$('.checkNav').css({
						'position':'fixed',
						'top':'0',
						'z-index':'99'
					});
				}else{
					$('.checkNav').css({
						'position':'inherit',
						'top':'0',
						'z-index':'99'
					});
				}
				info.lazyPicLoad($('.hotPic'));
			});
			$('.canyu a').on('click',function(){
				window.location.href = 'http://wopaitv.com';
			});
		},
		renderFun:function(){
			service.findLiveActivityById(liveActivityId).done(function(json){
				var data = json.result;
				$('title').html(data.title);
				var shtml = '<img class="activityCover" src="'+serverUrl+data.cover+'" /><h2 class="activityTitle">'+data.title+'</h2>'+
				'<div class="activeMsg"><span class="activeMsgCon">'+data.description+'</span><i class="active-desc-arrow"></i></div>';
				$('.main').append(shtml);
				$(".active-desc-arrow").click(function(){
					$(".activeMsgCon").toggleClass("drop-desc");
					$(this).toggleClass("arrrow-up");
				})
				$('#shareImg').attr('content',serverUrl+data.cover);
				$('#shareDescription').attr('content',data.description);
			});
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
		makeLiveList:function(data,target){
			var liveList = data.liveList;
			$.each(liveList,function(m,n){
				var liveName;
				if(!n.title){
					liveName = n.anchorName;
				}else{
					liveName = n.title;
				}
				var tempList= '<li>'+
				'<a href="javascript:void(0)"><img data-userPic="'+ n.roomPic +'" data-roomId="'+ n.id +'"src="" class="hotPic"><img class="Icon" src="'+interUrl+'/img/icons-2x/live_icon.png"></a>'+'</li>';
				target.append(tempList);
			});
			if(liveList.length<24){
				var backList = data.backList;
				$.each(backList,function(m,n){
					var tempList= '<li>'+
					'<a href="javascript:void(0)"><img data-userPic="'+ n.videoPic +'" data-roomId="'+ n.liveNoticeId +'"src="" class="hotPic"><img class="Icon" src="'+interUrl+'/img/icons-2x/review_icon.png"></a>'+'</li>';
					target.append(tempList);
				});
			}
			
			if(liveList == '' && backList == ''){
				var tempList = '<img class="null" style="width:80%;display:block;margin:0 auto;" src="' + interUrl + '/img/icons-2x/null.jpg" />';
				target.append(tempList);
				$('.checkContent .navContent .latestContent ul').css('position','inherit');
				$('.checkContent .navContent .latestContent ul li').css('margin-left','0');
				
			}
			
			
			$('.latestContent ul li').css('height',$(window).width()/3);
			$('.latestContent ul li').css('width',$(window).width()/3);
			$('.latestContent ul').css('width',$(window).width()+6);
			info.personalPortrait(serverUrl,$('.hotPic'));
			info.personalPortrait(serverUrl,$('.hotUesrPic'));
			$('.latestContent ul li a').on('click',function(){
				window.location.href = interUrl + '/live/shareLive?roomId=' + $(this).find('img').attr('data-roomId');
			});
			
		},
		swiperFn:function(latest,rank,rankContent,latestContent){
			latest.on('click',function(){
				$(this).addClass('on').removeClass('off');
				rank.removeClass('on').addClass('off');
				latestContent.show();
				rankContent.hide();
			});
			rank.on('click',function(){
				$(this).addClass('on').removeClass('off');
				latest.removeClass('on').addClass('off');
				rankContent.show();
				latestContent.hide();
			})
		},
		hotList:function(rankContent){//排行
			var id = $('#liveActivityId').val();
			var page = 1;
			var size = 10;
			service.getRank(id,page,size).done(function(json){
				var data = json.result.rank;
				var i = 0;
				if(data == ''){
					var tempList = '<img class="null" style="width:80%;display:block;margin:0 auto;" src="' + interUrl + '/img/icons-2x/null.jpg" />';
					rankContent.find('ul').append(tempList);
					$('.checkContent .navContent .rankContent ul li').css({
						'padding':'0',
						'text-indent':'0',
						'border':'none',
						'margin-top':'2px'
					});
				}
				$.each(data,function(m,n){
					i++;
					if(i>3 && i<10){
						var shtml = '<li><strong>0'+i+'</strong><img class="userPortrait" src="'+ serverUrl + n.userInfo.pic +'"><span class="nickname">'+n.userInfo.name+'</span><span class="giftNum"><em></em><a>'+n.giftNumber+'</a></span></li>';
					}else if(i<=3){
						var shtml = '<li><img class="img'+i+' imgRank" src="'+interUrl+'/img/icons-2x/ico'+i+'.png"><img class="userPortrait" src="'+ serverUrl + n.userInfo.pic +'"><span class="nickname">'+n.userInfo.name+'</span><span class="giftNum"><em></em><a>'+n.giftNumber+'</a></span></li>';
					}else{
						var shtml = '<li><strong>'+i+'</strong><img class="userPortrait" src="'+ serverUrl + n.userInfo.pic +'"><span class="nickname">'+n.userInfo.name+'</span><span class="giftNum"><em></em><a>'+n.giftNumber+'</a></span></li>';
					}
					rankContent.find('ul').append(shtml);
				});
			});
		},
		newList:function(latestContent){//最新
			var page = 1,size = 24,type = 1,liveActivityId = $('#liveActivityId').val(),target = $('.latestContent ul');
			service.getLiveAndRecordList(page,size,type,liveActivityId).done(function(json){
				liveActivityRank.makeLiveList(json.result,target);
				
			});
			
		},
		jump:function(obj){
			 $('body').on('touchstart','.down-Btn a,.downloadBtn,.openwopai,.bottomBanner a',function(e){
				 e.preventDefault();
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
	liveActivityRank.init();
})