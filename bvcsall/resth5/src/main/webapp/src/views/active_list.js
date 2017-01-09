
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','head'], function(jquery,info,aler,sever,head) {


//分享 
//share.init();
//弹出框
//aler.alertMsg('msg');

var serverUrl = $('#serverUrlimg').val();
var activeFn = {
	init: function(){

		var groupType = 1;
		var dataBox = $('.activeList ul');
		info.personalPortrait(serverUrl,$('.activeImg'));
		sever.getActList(groupType).done(function(json){

			activeFn.activeList(json.result,dataBox);
		});
		$(window).scroll(function(){
			info.lazyPicLoad($('.actListPic'),true);
		});

		$('.downClose').click(function(){ //cnzz关闭下载
			_czc.push(["_trackEvent","关闭活动列表下载按钮","closeBottomDownload"]);
		});
	},
	setLoad: function(){
		$(window).scroll(function(){
			var scrollTop = $(window).scrollTop();
			return scrollTop;
		})
	},
	activeList: function(data,target){
		if(data == ''){
			aler.alertMsg('没有数据了！');
		}
		$.each(data,function(m,n){
			var actList = '<li><a href="/restwww/page/activity/activityIndex?activityId='+ n.id+'"><img onerror="this.src=\'/restwww/img/portrait.png\'" class="actListPic" data-id="'+ n.id+'" lazy_src="" data-userPic="'+ n.cover+'" src="" alt=""></a></li>';
			$(target).append(actList);
			info.personalPortrait(serverUrl,$('.actListPic'));
		})
	}
}
activeFn.init();
})