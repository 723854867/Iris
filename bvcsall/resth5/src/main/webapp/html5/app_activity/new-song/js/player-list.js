$(function(){
    var service = $('#interfaceurl').val();
    var serverUrlimg = $('#serverUrlimg').val()+'/restwww/download';
    //http://ceshi.wopaitv.com/restwww/download/userHeadPic/2015-10-13/2015-10-13_100429_1444701865580.png

    var rankApi = service+'/restwww/singVote/getSingRank';  //type page size
    var rankType = $('#rankType').val();


    var xybJson = {
        'title' : '来LIVE直播为你爱的新歌声投一票 ',
        'desc' :  '新歌声学员榜排名靠前就有机会加入第五战队，礼物、投票，爱ta就刷起来~ '
    }
    var zbJson = {
        'title' : '错过了新歌声？LIVE直播带你加入第五战队 ',
        'desc' :  '中秋踢馆，澳门演唱会，加入新歌声第五战队，做LIVE直播人气王~'
    }
    var zybJson = {
        'title' : 'LIVE直播“挑战星势力”快来瞅瞅，下一个网红就是你 ',
        'desc' :  '“挑战星势力”，好看、好玩、有颜、有料的综艺咖都在这儿~'
    }

    var sharetit = '';
    var sharedesc = '';
    var shareUrl = window.location.href;
    var shareImg = service+'/resth5/html5/app_activity/new-song/img/slt.png';

    var timer = null;
    var params = {
        type : rankType,
        page : 1,
        size : 300
    }
    var rankNum = 0;

    //tab层处理
    $('.main-'+rankType).show();
    initFn.AJAX(rankApi,params).done(function(json){
        countTime(json.result.dateline,$('.count-time-'+rankType));
        var stuList;
        $('.main-'+rankType).find('ul').empty();
        $.each(json.result.rankList,function(m,n){
            rankNum++;
            if(rankType==1){
                stuList ="<li data-id='"+ n.id +"'>"+
                "<span class='pic fl'>"+
                "<em class='rank-num'>"+rankNum+"</em>"+
                "<img class='xyb_user_pic' src='"+ serverUrlimg+n.pic+ "'>"+
                "<span class='pic-bg'></span>"+
                "</span>"+
                "<span class='nice-name fl'>"+
                "<h4>"+ cutStr(n.name) +"</h4>"+
                "<p class='user-info'><em class='user-icon'></em>人气值："+ n.popularity+"</p>"+
                "</span>"+
                "<span class='opertion fr'></span><a data-name='"+ n.name +"' href='javascript:;' class='tirck'></a>"+
                "</li>";
            }else if(rankType==2){
                stuList = "<li>"+
                "<span class='pic fl'>"+
                "<em class='rank-num'>"+rankNum+"</em>"+
                "<img src='"+ serverUrlimg+n.pic+ "'>"+
                "<span class='pic-bg'></span>"+
                "</span>"+
                "<span class='nice-name fl'>"+
                "<h4>"+ cutStr(n.name) +"</h4>"+
                "</span>"+
                "<span class='opertion fr'><p class='user-info'><em class='user-icon'></em>人气值："+ n.popularity+"</p></span>"+
                "</li>";
            }else if(rankType==3){
                stuList = "<li>"+
                "<span class='pic fl'>"+
                "<em class='rank-num'>"+rankNum+"</em>"+
                "<img src='"+ serverUrlimg+n.pic+ "'>"+
                "<span class='pic-bg'></span>"+
                "</span>"+
                "<span class='nice-name fl'>"+
                "<h4>"+ cutStr(n.name) +"</h4>"+
                "</span>"+
                "<span class='opertion fr'><p class='user-info'><em class='user-icon'></em>人气榜："+ n.popularity+"</p></span>"+
                "</li>";
            }
            $('.main-'+rankType).find('ul').append(stuList);
        })
    });




    $('.xyb-banner').click(function(){
        _czc.push(﻿["_trackEvent","学员榜跳转规则页","跳转"]);
    })
    $('.zyb-banner').click(function(){
        _czc.push(﻿["_trackEvent","学员榜跳转规则页","跳转"]);
    })
    $('.zbb-banner').click(function(){
        _czc.push(﻿["_trackEvent","学员榜跳转规则页","跳转"]);
    })

    $('.joinBtn').click(function(){
        if(browser.versions.isWx)
        {
            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
        }else{
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
        }
        _czc.push(﻿["_trackEvent","顶部下载按钮","下载"]);
        $('.skyBox,.skyBox-anz').click(function(){
            $(this).hide();
        });
    });
    if(rankType==1){
        //学员榜
        document.title = xybJson.title;
        sharetit = xybJson.title;
        sharedesc = xybJson.desc;
    }else if(rankType==2){
        document.title = zbJson.title;
        sharetit = zbJson.title;
        sharedesc = zbJson.desc;
    }else if(rankType==3){
        document.title = zybJson.title;
        sharetit = zybJson.title;
        sharedesc = zybJson.desc;
    }
    $('body').on('click','.tirck',function(){
    	if(browser.versions.isWx)
        {
    		$('.skyBox').show();
            var name = $(this).attr('data-name');
            sharetit = '我是'+name+'，爱我就投我一票';
            sharedesc = '我是新歌声学员'+name+'，我在LIVE直播，快为我的梦想投上你珍贵的一票~';
            shareImg = $(this).siblings('.pic').find('.xyb_user_pic').attr('src');
        }else{
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
        }
        wx.ready(function(){
            //好友
            shareData1 = {
                title: sharetit,
                desc: sharedesc,
                imgUrl: shareImg
            };
            //朋友圈
            shareData2 = {
                title: sharetit,
                imgUrl: shareImg
            };
            wx.onMenuShareAppMessage(shareData1);
            wx.onMenuShareTimeline(shareData2);
        });
    })
    $('.skyBox').click(function(){
    	$(this).hide();
    })

    $('body').on('touchstart','.main-1 .opertion',function(){
        //initFn.goDown();

        if(browser.versions.isWx)
        {
            window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653';
        }else{
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
        }

        $('.skyBox,.skyBox-anz').click(function(){
            $(this).hide();
        });

        _czc.push(﻿["_trackEvent","支持他","下载"]);


    })



    wx.ready(function(){
        shareData1 = {
            title: sharetit,
            desc: sharedesc,
            link: '',
            imgUrl: shareImg
        };
        //朋友圈
        shareData2 = {
            title: sharetit,
            link: '',
            imgUrl: shareImg
        };


        wx.onMenuShareAppMessage(shareData1);
        wx.onMenuShareTimeline(shareData2);
    });
    function cutStr(str){
        var winW = $(window).width();
        if(winW == 320){
            if(str.length > 7){
                str= str.substring(0,7)+'..';
            }else{
                str = str;
            }
        }
        return str;
    }
    function countTime(t,target){
        if(t<=0){
            clearInterval(timer)
            $(target).html('已结束')
            return false;
        }

        t=t-1000;

        var d = Math.floor(t/1000/60/60/24);
        var h = Math.floor(t/1000/60/60%24);
        var i = Math.floor(t/1000/60%60);
        var s = Math.floor(t/1000%60);
        var abc = d+"天"+h+"小时"+i+"分";
        timer=setTimeout(function(){
            countTime(t)
        }, 1000);
        $('.count-time-'+rankType).html(abc);

    }

})