
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','../common/share','head'], function(jquery,info,alert,service,share,head) {


//分享
//share.init();
//弹出框
//aler.alertMsg('msg');

var serverUrl = info.getPicPerfix();
var interUrl = info.getRequestPrefix();
var uid = info.getCookie('uid');
var start = 0;
var count = 5;
var type = 1;
var activeFn = {
	init: function(){
		_this=this;
		this.tagName= $('#tagName').val();
//		_this.start = 0;
//		_this.count = 5;
		_this.dataType= 'shoushi';
		_this.isNext= 0;
		_this.nextType= 4;
		activeFn.getrankVideoList(start,count); //默认初始化视频榜单
		info.personalPortrait(serverUrl,$('.activeImg'));
		activeFn.changeShare();
		$(window).scroll(function(){
			info.lazyPicLoad($('.playBox img'));
			info.lazyPicLoad($('.actvideoPic'));
		});
		$('.rankloadMore').click(function(){
			//_this.start+=5;
			start +=5;
			if(start>=50){
				alert.alertMsg('没有更更多数据！');
				return;
			}
			if(_this.dataType=='shoushi'){
				activeFn.getrankVideoList(start,count);
			}else if(_this.dataType=='renqi'){
				activeFn.getPopularList(start,count,false);
			}
		});
		$('.rankHead button').click(function(){
			$('.rankHead button').removeClass('on');
			_this.dataType= $(this).attr('data');
			_this.start= 0;
			$(this).addClass('on');
			if(_this.dataType == 'shoushi'){
				activeFn.getrankVideoList(0,count,true);
				_czc.push(["_trackEvent","切换收视榜","videoTop"]);
			}else if(_this.dataType == 'renqi'){
				activeFn.getPopularList(0,count,true);
				_czc.push(["_trackEvent","切换人气榜","userTop"]);
			}
		});
		//转发到我拍 后期要优化这个方法
		$('body').delegate('.sharewp','click',function(){
			var videoId= $(this).attr('data-videoid');
			var userId= $(this).attr('data-userId');
			service.shareWopaiap(videoId,userId,'',1,'h5').done(function(json){
				if(json.code=='200'){
					alert.alertMsg('转发成功！');
					$('.shareWrap').remove();
				}
			}).fail(function(json){
				if(json.code=='344'){
					alert.alertMsg('你已经转发过该视频！')
					$('.shareWrap').remove();
					return;
				}
				alert.alertMsg("请登录后操作");
				window.location.href = info.getRequestPrefix()+"/page/user/login";
			});
		});

	},
	changeShare: function(){
		service.CustomShare('2').done(function(json){
			if(json.result != ''){
				document.title=json.result.shareText;
				var oimg= $('<img class="sharePic" src="'+(serverUrl+json.result.shareImg)+'" width="0" height="0"/>');
				$('body').prepend(oimg)
			}else{
				return;
			}
		})
	},
	getPopularList: function(start,count,clear){  //人气榜
		service.getRankPopul(start,count).done(function(json){
			if(json.result==''){
				alert.alertMsg('没有更更多数据！');
				return;
			}
			activeFn.takePopulerList(json.result,$('.videoList1 ul'),clear);
		});
	},
	getrankVideoList: function(start,count,clear){ //视频榜
		service.getRankVideo(type,start,count).done(function(json){
			if(json.result.length>5){
				alert.alertMsg('没有更更多数据！');
				return;
			}
			activeFn.activeList(json.result,$('.videoList1 ul'),clear);
		});
	},
	goPage: function(type,typeId){
		var url= ''
		var params= {};
		if(type == 'video'){
			url= interUrl+"/page/video/videoDetail?videoId="+typeId
			params= {
				uid: uid,
				isNext: activeFn.isNext,
				nextType: activeFn.nextType
			}
		}else if(type == 'userId'){
			url= interUrl+'/page/user/userDetail?userId='+typeId;
			params= {
				uid: uid
			}
		}
		info.postform(url,params);
	},
	shareFn: function(obj){
		var tit = $(obj).attr('data-name')
		var desc = $(obj).attr('data-desc')
		var pic = serverUrl+$(obj).attr('data-videopic');//缩略图
		var videoId = $(obj).attr('data-videoId');
		var userId = $(obj).attr('data-userId');
		share.init(tit,desc,pic,videoId,uid,userId);
	},
	numFn: function(num){
		if(num >= 10000){
			num=(num/10000).toFixed(2)+'万';
		}else{
			num=num;
		}
		return num;
	},
	takePopulerList: function(data,target,clear){
		if(clear){
			$(target).empty();
		}
		data.length=5;
		if($('.rankPopuItem').length>=50){
			alert.alertMsg('没有更多数据！');return
		};
		$.each(data,function(m,n){
			var vStart;
			if(n.vipStat == 1){
				vStart= '<em class="userV1"></em>';
			}else if(n.vipStat == 2){
				vStart= '<em class="userV2"></em>';
			}else if(n.vipStat == 3){
				vStart= '<em class="userV3"></em>';
			}else{
				vStart= '';
			};
			var sign;
			if(n.signature != '' && n.signature != null){
				sign = n.signature;
			}else{
				if(n.addr != '' && n.addr != null){
					sign = 'TA来自'+n.addr;
				}else{
					if(n.age != '' && n.age != null){
						sign = 'TA是'+n.age;
					}else{
						if(n.sex != '' && n.sex != null){
							if(n.sex==1){
								sign = 'TA的性别是男';
							}
							if(n.sex==0){
								sign = 'TA的性别是女';
							}
							if(n.sex==2){
								sign = 'TA的性别是男';
							}
						}else{
							sign = '懒人没前途o(∩_∩)o 哈哈~';
						}
					}
				}
			};
			var fansCount=activeFn.numFn(n.dayPopularity);

			var actList = '<li class="rankPopuItem" data-id="'+ n.id +'"><div class="listUserMsg">'+
				'<span class="fl rankKey">'+(m+1)+'</span><dl>'+
				'<dt><img class="actvideoPic" data-userId="'+ n.id +'" data-userPic="'+ n.pic +'" src="" alt="头像" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
				vStart+'</dt>'+
				'<dd><p>'+ n.name +'</p>' +
				'<p>'+sign+'</p>'+
				'<p>人气：'+fansCount+'</p>'+
				'</dd>'+
				'</dl></div>'+
				'</div></li>';
			$(target).append(actList);
		});
		$('.videoList1 ul .rankPopuItem').each(function(){
			var index = $(this).index();
			$('.rankKey').eq(index).text(index+1);
			if(index==0){
				$('.rankKey').eq(index).addClass('rankKey1').empty();
			}else if(index==1){
				$('.rankKey').eq(index).addClass('rankKey2').empty();
			}else if(index==2){
				$('.rankKey').eq(index).addClass('rankKey3').empty();
			}
		});
		info.personalPortrait(serverUrl,$('.actvideoPic'));
		$('.rankPopuItem').on('click','.actvideoPic',function(){
			var userId= $(this).attr('data-userId');
			activeFn.goPage('userId',userId)
		});
	},
	activeList: function(data,target,clear){
		if(clear){
			$(target).empty();
		}
		
		//data.length=5;
		if(data.length>=5){
			data.length=5
		}
		if($('.videoList1 ul li').length>=50){
			alert.alertMsg('没有更多数据！');return
		};
		
		$.each(data,function(m,n){
			var vStart;
			if(data[m].user.vipStat == 1){
				vStart= '<em class="userV1"></em>';
			}else if(data[m].user.vipStat == 2){
				vStart= '<em class="userV2"></em>';
			}else if(data[m].user.vipStat == 3){
				vStart= '<em class="userV3"></em>';
			}else{
				vStart= '';
			};
			var heat;
			if(data[m].dayHotValue >= 10000){
				heat = (data[m].dayHotValue/10000).toFixed(1)+'万';
			}else{
				heat = data[m].dayHotValue;
			}
			var actList = '<li data-id="'+ n.id +'"><div class="listUserMsg">'+
				'<dl><span class="fl rankKey">'+(m+1)+'</span>'+
				'<dt><img class="actvideoPic" data-userId="'+ n.user.id +'" data-userPic="'+ n.user.pic +'" src="" alt="头像" onerror="this.src=\'/restwww/img/icons-2x/user_icon_24px.png\'">'+
				vStart+'</dt>'+
				'<dd><p>'+ n.user.name +'</p><p class="myDate" data-time="'+ n.createDate+'"></p></dd>'+
				'<dd><em class="playIcon"></em><span class="playData">'+ heat +'</span> 热度</dd></dl></div>'+
				'<div class="playBox" data-videoid="'+ n.id+'" data-videoKey="'+ n.playKey+'">'+
				'<div class="loadingGif" style="width:30px;height:30px;min-height:30px;position:absolute;left:0;top:0;display:none;z-index:999999;background-color:#000000;opacity:0.6;width:100%;height:50%;color:#ffffff;text-align:center;padding-top:50%;font-size:18px;"></div>'+
				'<img data-videoPic="'+ n.videoPic +'" src="" alt="视频" class="videoPic"><em class="canPlay"></em></div>'+
				'<div class="videoListTag face">'+ n.description +'</div>'+
				'<div class="shareBox"><div class="zan" data-praise='+ n.praise +' data-videoid='+ n.id +'><em class="off"></em><span>'+ n.praiseCount +'</span></div>'+
				'<div class="commit" data-video='+ n.id +'><em></em><span>'+ n.evaluationCount +'</span></div>'+
				'<div class="Forward" data-name="'+n.user.name+'" data-userId="'+ n.user.id+ '" data-videoid="'+ n.id+'" data-desc="'+n.description+'" data-videoPic="'+ n.videoPic+ '">' +
				'<em></em></div>'+
				'</div></li>';
			$(target).append(actList);
		});
		$('.videoList1 ul li').each(function(){
			var index = $(this).index();
			$('.rankKey').eq(index).text(index+1);
			if(index==0){
				$('.rankKey').eq(index).addClass('rankKey1').empty();
			}else if(index==1){
				$('.rankKey').eq(index).addClass('rankKey2').empty();
			}else if(index==2){
				$('.rankKey').eq(index).addClass('rankKey3').empty();
			}
		});
		info.videoUrl(serverUrlvid);
		info.personalPortrait(serverUrl,$('.actvideoPic'));
		info.videoInfo(serverUrl,$('.videoPic'));
		info.timeTransform('.myDate','data-time','MM-dd hh:mm');
		info.faceFn($('.face'));

		//分享
		$('.Forward').click(function(){
			activeFn.shareFn($(this));
		});
		$('.commit').each(function(){
			$(this).click(function(){
				var videoId = $(this).attr('data-video');
				activeFn.goPage('video',videoId);
			})
		});
		$('.listUserMsg').on('click','.actvideoPic',function(){
			var userId= $(this).attr('data-userId')
			activeFn.goPage('userId',userId)
		});
		$('.zan').each(function(){
			if($(this).attr('data-praise') == 'true'){
				$(this).find('em').addClass('on');
			}
			$(this).click(function(){
				var zanNum = parseInt($(this).find('span').text());
				var ab=  $(this).attr('data-videoid');
				var bc = $(this).parent().attr('data-createrId');
				if($(this).attr('data-praise') == 'false' || $(this).attr('data-praise') == false){
					//点赞
					var self = this;
					service.savePraise(ab).done(function(json) {
						$(self).attr('data-praise',true);
						if($(self).find('em').hasClass('on')){
							$(self).find('em').removeClass('on');
							$(self).find('em').addClass('off');
							$(self).find('span').text(zanNum-1);
						}else{
							$(self).find('em').removeClass('off');
							$(self).find('em').addClass('on');
							$(self).find('span').text(zanNum+1);
						}
					}).fail(function(json){
						alert.alertMsg("请登录后操作");
						window.location.href = info.getRequestPrefix()+"/page/user/login";

					});
				};
				if($(this).attr('data-praise') == 'true' || $(this).attr('data-praise') == true){
					//取消赞
					var self = this;
					service.deletePraise(ab).done(function(json) {
						$(self).attr('data-praise', false);
						if($(self).find('em').hasClass('on')){
							$(self).find('em').removeClass('on');
							$(self).find('em').addClass('off');
							$(self).find('span').text(zanNum-1);
						}else{
							$(self).find('em').removeClass('off');
							$(self).find('em').addClass('on');
							$(self).find('span').text(zanNum+1);
						}
					}).fail(function(json){
						alert.alertMsg("请登录后操作");
						window.location.href = info.getRequestPrefix()+"/page/user/login";
					});
				}
			})
		})


	}

}
activeFn.init();
})