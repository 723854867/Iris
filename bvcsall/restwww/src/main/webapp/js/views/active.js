
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','head','../libs/TouchSlide.1.1'], function(jquery,info,aler,service,head,touch) {


//分享 
//share.init();
//弹出框
//aler.alertMsg('msg');

var serverUrl = info.getPicPerfix()
var interUrl = info.getRequestPrefix();
var uid = info.getCookie('uid');
var activeFn = {
	init: function(){
		info.personalPortrait(serverUrl,$('.activeImg'));
		activeFn.changeShare();
		TouchSlide({
			slideCell:"#slideBox",
			titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
			mainCell:".bd ul",
			effect:"leftLoop",
			autoPage:true,//自动分页
			autoPlay:true //自动播放
		});
		//截取标签
		activeFn.splitTag('.activeTag');
		$('.bd ul li img').on('click',function(){
			var targetType = $(this).parent().attr('data-type');

			var dataId = $(this).parent().attr('data-id');
			if(targetType == 'activity'){//活动
				window.location.href = '/restwww/page/activity/activityIndex?activityId='+dataId;

			}else if(targetType == 'user'){//个人主页
				activeFn.goPage('userId',dataId);
				//window.location.href = '/restwww/page/user/userDetail?userId='+dataId+'&uid='+uid;

			}else if(targetType == 'video'){//视频详情
				activeFn.goPage('video',dataId);
			}else{
				return;
			}
		});
		$('.tagList ul li a').click(function(){
			var dataTag= $(this).attr('data-tagName');
			activeFn.goPageTag(dataTag);
		});
		$('.downBar').click(function(){//cnzz
			_czc.push(["_trackEvent","底部下载","底部下载按钮"]);
		});
		$('.downClose').click(function(){ //cnzz关闭下载
			_czc.push(["_trackEvent","关闭活动页下载按钮","closeBottomDownload"]);
		});
	},
	changeShare: function(){
		service.CustomShare('3').done(function(json){
			if(json.result != ''){
				document.title=json.result.shareText;
				var oimg= $('<img class="sharePic" src="'+(serverUrl+json.result.shareImg)+'" width="0" height="0"/>');
				$('body').prepend(oimg)
			}else{
				return;
			}
		})
	},
	splitTag:function(obj){//截取标签
		$(obj).each(function(){
			var oLength = $(this).parent().attr('data-tagname').length;
			if(oLength>=6){
				val= $(this).text().substring(0,4)+'...'+$(this).text().substring(oLength-2,oLength);
			}else{
				val= $(this).text();
			}
			$(this).html(' # '+val+'# ');
		})
	},
	goPageTag: function(tag){
		window.location.href= interUrl+'/page/activity/activityTag?tag='+encodeURI(tag);
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
			params= {
				uid: uid
			}
		}
		info.postform(url,params)
	},


}
activeFn.init();
})