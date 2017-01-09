var authorization_code = ""; // ��½��Ȩ��
var access_token = ""; // ��½token
var uid; // ��½�û�id���û�Ψһ��ʶ


//urlǰ׺-----���Ի���
var requestUrlPrefix = "http://127.0.0.1:8080/restwww"; // ����urlǰ׺
var videoUrlPrefix = "http://192.168.108.160:63880/hls/play/"; // ��Ƶurlǰ׺
var picUrlPrefix = "http://192.168.108.160:8080/restadmin/download"; // ͼƬ��Դurlǰ׺

//urlǰ׺-----��ʽ����
//var requestUrlPrefix = "http://api.wopaitv.com/restwww"; // ����urlǰ׺
//var videoUrlPrefix = "http://cdn.wopaitv.com/hls/play/"; // ��Ƶurlǰ׺
//var picUrlPrefix = "http://api.wopaitv.com/restwww/download"; // ͼƬ��Դurlǰ׺

//������
function Close_hide(){
	$('.bg').css('display','none');
	$('.popDie').css('display','none');		
}
function showPop(classN){
	$('.bg').css('display','block');
	$('.' + classN).css('display','block');
}; 

//tab�л�
function tabs(tabTit, on, tabCon) {
    $(tabCon).each(function () {
        $(this).children().eq(0).show();
    });
    $(tabTit).each(function () {
        $(this).children().eq(0).addClass(on);
    });
    $(tabTit).children().click(function () {//��ꡰhover����Ч��
        $(this).addClass(on).siblings().removeClass(on);
        var index = $(tabTit).children().index(this);
        $(tabCon).children().eq(index).show().siblings().hide();
    });
}
tabs(".tab-hd", "active", ".tab-bd");



// ����û���Ϣ
function getUserInfo1() {
	$.ajax({
		url : requestUrlPrefix+'/userInfo',
		data : {
			'access_token' : access_token
		},
		type : 'get',
		cache : false,
		dataType : 'json',
		success : function(data) {
			if (data["code"] == "200") {
				alert(data["result"]["name"]);
			} else {
				//alert(data["message"]);
				if(data["code"] == "401"){
					   clearCookie("wopaiuid");
					   clearCookie("wopaitoken");
				}
			}
		},
		error : function() {
			alert("服务器错误，稍后重试！");
		}
	});
}



/*
 *  操作 cookie代码
 */

//设置cookie
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (2*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires+";path=/";
}
//获取cookie
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
    }
    return "";
}
//清除cookie  
function clearCookie(name) {  
    setCookie(name, "", -1);  
} 

function getRootPath(){
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht+projectName);
}