var browser={
    versions:function(){
        var u = navigator.userAgent, app = navigator.appVersion;
        return {         //移动终端浏览器版本信息
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
            iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            isWx: u.indexOf('MicroMessenger') > -1,
            isQQ:u.indexOf('QQ/') > -1,
            isQQbrowser: u.indexOf('QQBrowser'),
            isWeiBo:u.indexOf('weibo') > -1,
            isOpera:u.indexOf('Opera') >= 0,
            isSafari:u.indexOf("Safari") > -1
        };
    }(),
    language:(navigator.browserLanguage || navigator.language).toLowerCase()
};

var _czc = _czc || [];
_czc.push(["_setAccount", "1256005299"]);


var sendings = {};
var initFn = {
	appDownUrl: "http://wopaitv.com/download/myVideo_guanwang_3.1.3.apk",
    navType : function(type,id1,id2){
        if(browser.versions.ios){
            CallAppEvent(type,id1,id2);
        }else{
            window.jump.toPage(type,id1,id2);

        }
    },
    goDown : function(){

        if(browser.versions.isWx)
        {
            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
        }
        if(browser.versions.ios)
        {
            if(browser.versions.isWeiBo){
                $('.skyBox').show();
            }else{
                window.location.href = 'https://itunes.apple.com/us/app/wo-pai-wo-jiu-shi-zhu-jiao/id1077305226?ls=1&mt=8';
            }
        }
        if(browser.versions.android)
        {
            if(browser.versions.isWeiBo){
                $('.skyBox-anz').show();
            }else{
                window.location.href = initFn.appDownUrl;
            }
        }

        $('.skyBox,.skyBox-anz').click(function(){
            $(this).hide();
        });
    },
    appShare : function(title,desc,pic,url){
        if(browser.versions.ios){
            if(!browser.versions.isWx){
                sharefn(title,desc,pic,url);
            }else{
                //$('.share-tip').show();  //提示语
            }
        }
        if(browser.versions.android){
            if(!browser.versions.isWx){
                window.share.onShareClick(title,desc,pic,url)
            }else{
                //$('.share-tip').show();
            }
        }
    },
    getAccessToken: function() {
		var access_token = initFn.getCookie('access_token');
        return access_token || "";
    },

    getCurrentUid: function() {
		var uid = initFn.getCookie('uid');
        return uid || "0";
    },
    
    _onced: false,
    once: function() {
        this._onced = true;
        return this;
    },

    _quieted: false,
    quiet: function() {
        this._quieted = true;
        return this;
    },
    AJAX : function(url, params) {
        var dtd = $.Deferred();

        if (this._onced && sendings[url]) {
            this._onced = false;
            return dtd;
        }

        var quited = this._quieted;

        if (this._quieted) {
            this._quieted = false;
        }

        params || (params = {}); //参数
        params.access_token = this.getAccessToken();
        var currentUid = this.getCurrentUid();
        sendings[url] = true;
        $.ajax({
            type: "post",
            data: params,
            dataType: "json",
            url: url,
            beforeSend: function (request) {
            	request.setRequestHeader("uid", currentUid);
            }
        }).done(
            function(json) {
                delete sendings[url];
                var message = '系统繁忙，请稍后再试。';
                if (!json || json.code != '200' && (message = json.message)) {
                    if (!quited) {
                    }
                    dtd.reject(json);
                } else {
                    dtd.resolve(json);
                }
            }).fail(function() {
                if (!quited) {
                }
                delete sendings[url];
                dtd.reject({});
            });
        return dtd;
    },
    setCookie:function (cname, cvalue, exdays){
		var d = new Date();
	    d.setTime(d.getTime() + (exdays*24*60*60*1000));
	    var expires = "expires="+d.toUTCString();
	    document.cookie = cname + "=" + cvalue + "; " + expires+";path=/";
	},
    getCookie:function(cname){
		var name = cname + "=";
	    var ca = document.cookie.split(';');
	    for(var i=0; i<ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0)==' ') c = c.substring(1);
	        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
	    }
	    return "";
	},
	getRequestPrefix: function(){
		return $('#interfaceurl').val();
	},
}