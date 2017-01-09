require(['../libs/jquery-2.1.4','../common/info'],function(jquery,info){
	//发布时间
	

//	$(function(){
//		document.title = $(window).width();
//		alert($(window).width())
//	})

	$(".myDate").each(function(){
		var de = $(this).attr("data-time");
		var newDate = new Date();
		newDate.setTime(de);
		Date.prototype.format = function(format) {
	       var date = {
	              "M+": this.getMonth() + 1,
	              "d+": this.getDate(),
	              "h+": this.getHours(),
	              "m+": this.getMinutes(),
	              "s+": this.getSeconds(),
	              "q+": Math.floor((this.getMonth() + 3) / 3),
	              "S+": this.getMilliseconds()
	       };
	       if (/(y+)/i.test(format)) {
	              format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
	       }
	       for (var k in date) {
	              if (new RegExp("(" + k + ")").test(format)) {
	                     format = format.replace(RegExp.$1, RegExp.$1.length == 1
	                            ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
	              }
	       }
	       return format;
	}
		
		var needTime = newDate.format('MM-dd hh:mm');
		$(this).text(needTime);
	});
	var serverUrlimg = $('#serverUrlimg').val();
	var serverUrlvid = $('#serverUrlvid').val();
	info.personalInfo(serverUrlimg,'.userHomePic');
	info.videoInfo(serverUrlimg,'.videoPic');
	info.videoUrl(serverUrlvid);
	info.personalPortrait(serverUrlimg,'.userPortrait');
//	infos.timeTransform('.myDate','data-time','MM-dd hh:mm');

	
});