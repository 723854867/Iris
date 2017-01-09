$(function(){
    var service = $('#interfaceurl').val();
    var serverUrlimg = $('#serverUrlimg').val()+'/restwww/download';

    var url = window.location.href;
    var dataType = url.split("#")[1];

    var sharetit = '周五没看够，周末Live go!';
    var sharedesc = '想跟48位学员零距离互动，想听学员和主播们现场飙歌，还想探听最新鲜导师八卦，锁定《中国新歌声LIVE战队》！';
    var shareImg = 'http://api.wopaitv.com/resth5/html5/app_activity/new-song/img/singSport/sharePic.png';

    $('.share').click(function(){
        if(browser.versions.ios){
            if(!browser.versions.isWx){
                sharefn(sharetit,sharedesc,shareImg,url);
            }else{
                $('.share-tip').show();
            }
        }
        if(browser.versions.android){
            if(!browser.versions.isWx){
                window.share.onShareClick(shareImg,sharetit,sharedesc,url)
            }else{
                $('.share-tip').show();
            }
        }
    })
    $('.share-tip').click(function(){
        $(this).hide()
    })

    wx.ready(function(){
        //好友
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

})