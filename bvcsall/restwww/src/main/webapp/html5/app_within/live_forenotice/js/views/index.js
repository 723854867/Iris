define(['../libs/jquery-2.1.4','../common/newAlert','../common/info','../common/share','../common/browse'],function(jquery,alert,info,share,browse){
	var serverUrl = info.getPicPerfix();
	var interUrl = info.getRequestPrefix();
	var uid = info.getCookie('uid');
	var noticeId = $('#noticeId').val();
	var userId = $('#userId').val();
	var liveForenotice = {
		init:function(){
			$('body').css('height',$(window).height());
			info.videoInfo(serverUrl,$('.videoPic'));
			info.personalPortrait(serverUrl,$('.userPortrait-1'));
			$('.userPortrait-1').on('click',function(){
				liveForenotice.goPage('userId',$(this).attr('data-userId'));
			});
//			$('.name').css('width',$('.name em.dis').width()+120);
			liveForenotice.shareFn();
			liveForenotice.noticeTime();
			$('.topBanner,.bottomBanner a').on('click',function(){
				if (browse.versions.ios) {
					if(browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
						$('.skyBox').show();
					} else {
						$(this).attr("href","wopai://type=notice&userId=" + userId);
						timer();
					}
				} else if (browse.versions.android){
					if (browse.versions.isWx || browse.versions.isQQ || browse.versions.isWeiBo){
						$('.skyBox-anz').show();
					} else {
						$(this).attr("href","com.busap.myvideo://main/openwith?userId="+userId+"&type=12");
						timer();
					}
				} else {
					window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
				};

				function timer(){
					setTimeout(function(){
						window.location.href='http://www.wopaitv.com#cnzz_name=loading&cnzz_from=videoDetailDownload';
					},3000)
				}
			});
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
				url= interUrl+'/page/user/userDetail?userId='+typeId;
				params= {
					uid: uid
				}
			}
			info.postform(url,params);
		},
		noticeTime:function(){
			var noticeTime = $('.noticeTime').attr('data-time');
			var d = new Date();
			var date=new Date(parseInt(noticeTime));
			$('.noticeTime').text('直播时间：'+ date.getFullYear()+"-"+fixZero(date.getMonth()+1,2)+"-"+fixZero(date.getDate(),2)+" "+fixZero(date.getHours(),2)+":"+fixZero(date.getMinutes(),2));
			function fixZero(num,length){
				var str=""+num;
				var len=str.length;
				var s="";
				for(var i=length;i-->len;){
					s+="0";
				}
				return s+str;
			}
		}
	}
	liveForenotice.init();
})