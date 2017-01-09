$(function(){



    var browser={
        versions:function(){
            var u = navigator.userAgent, app = navigator.appVersion;
            return {         //移动终端浏览器版本信息
                trident: u.indexOf('Trident') > -1, //IE内核
                presto: u.indexOf('Presto') > -1, //opera内核
                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
                iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
                iPad: u.indexOf('iPad') > -1, //是否iPad
                webApp: u.indexOf('Safari') > -1, //是否web应该程序，没有头部与底部
                isWx: u.indexOf('MicroMessenger') > -1,
                isQQ:u.indexOf('QQ/') > -1,
                isQQbrowser: u.indexOf('QQBrowser'),
                isWeiBo:u.indexOf('weibo') > -1,
                isOpera:u.indexOf('Opera') >= 0
            };
         }(),
         language:(navigator.browserLanguage || navigator.language).toLowerCase()
    }
    $('.nav span').click(function(){
        $('.nav span').removeClass('on');
        $(this).addClass('on');
        $('.tab1').hide();
        $('.tab1').eq($(this).index()).show();
        if($(this).hasClass('service_btn')){
            $(this).find('img').attr('src','img/service_on.png');
            $('.gongyue_btn img').attr('src','img/gongyue.png');
        }else if($(this).hasClass('gongyue_btn')){
            $(this).find('img').attr('src','img/gongyue_on.png');
            $('.service_btn img').attr('src','img/service.png');
        }



        
    });
    // console.log(browser.versions.ios)
    if(browser.versions.ios){
        $('.anzhuo').hide();
        $('.ios').show();
    }else if(browser.versions.android){
        $('.anzhuo').show();
        $('.ios').hide();
    }
    $('.nav span.on').find('img').attr('src','img/service_on.png');
})











