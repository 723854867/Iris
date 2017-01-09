
define(['../libs/jquery-2.1.4','../common/info','../common/aler','../service/service','../common/share','head'], function(jquery,info,alert,service,share,head) {

var serverUrl = info.getPicPerfix()
var interUrl = info.getRequestPrefix();
var uid = info.getCookie('uid');
var prize = {
	init: function(){

		info.personalPortrait(serverUrl,$('.actvideoPic'));
		$(window).scroll(function(){
			info.lazyPicLoad($('.actvideoPic'));
		});

		$('.resultList .prizeItem').each(function(){
			var index = $(this).index();
			if(index==0){
				$(this).addClass('firstPrize');
			}else if(index==1){
				$(this).addClass('twoPrize');
			}else if(index==2){
				$(this).addClass('threePrize');
			}
		});
	}
}
prize.init();
})