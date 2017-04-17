//获取项目根目录
function getRootPath_web() {
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return  projectName;
}

var path = getRootPath_web();

Div=function(exp1, exp2) {
	var n1 = Math.round(exp1); // 四舍五入
	var n2 = Math.round(exp2); // 四舍五入
	var rslt = n1 / n2; // 除
	rslt = Math.ceil(rslt); // 返回值为大于等于其数字参数的最小整数。
	return rslt;
}
getUrlParam=function(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}
getLocalTime=function(nS) {
	return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " "); 
}
initLoading=function(){
    $("body").append("<div class='modal fade bs-example-modal-sm' id='loading' tabindex='-2' role='dialog' aria-labelledby='myModalLabel' data-backdrop='static'>" +
				    	"<div class='modal-dialog modal-sm' role='document'>" +
				    		"<div class='modal-content'>" +
				    			"<div class='modal-header'></div>" +
				    			"<div id='loadingText' class='modal-body'>" +
				    				"<span class='glyphicon glyphicon-refresh' aria-hidden='true'></span>处理中，请稍候。。。" +
				    			"</div>" +
				    		"</div>" +
				    	"</div>" +
				    "</div>"
    );
}
showLoading=function(text){
    $("#loadingText").html(text);
    $("#loading").modal("show");
}
hideLoading=function(){
	$("#loading").modal("hide");
}
RemoveLoading=function(){
    $("#loading").remove();
    $(".modal-backdrop").remove();
    //移除多余的样式
    $("body").removeClass("modal-open");
}
centerModals=function() {   
	$('#myModal').each(function(i) {   
		var $clone = $(this).clone().css('display','block').appendTo('body');
		var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 2);
		top = top > 0 ? top : 0;
		$clone.remove();
		$(this).find('.modal-content').css("margin-top", top);
	});
};