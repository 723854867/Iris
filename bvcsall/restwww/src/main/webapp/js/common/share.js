define(['../libs/jquery-2.1.4','../common/browse','../common/aler','../common/info'],function(jquery,browse,alert,info) {

    //'http://connect.qq.com/widget/loader/loader.js'

    var sharebox = {
        maskEl: null,
        shareEl: null,
        //分享需要参数 视频标题标题 分享的地址（生成） 描述 缩略图（视频缩略图） 视频id 分享者Id(uid)
        init: function(tit,desc,pic,videoId,uid,userId,isLive){
           if(browse.versions.isWx)
           {
                shareList = '<li><a href="javascript:;" class="QZone"></a><p>QZone</p></li>'+
                        '<li><a href="javascript:;" class="wx"></a><p>微信</p></li>'+
                        '<li><a href="javascript:;" class="wxq"></a><p>微信朋友圈</p></li>'+
                        '<li><a href="javascript:;" class="sinawb"></a><p>新浪微博</p></li>';
           }else{
               shareList= '<li><a href="javascript:;" class="QZone"></a><p>QZone</p></li>'+
                       '<li><a href="javascript:;" class="sinawb"></a><p>新浪微博</p></li>';
           }
           if(window.location.href.indexOf('live')>0){
        	   shareList = '<li><a href="javascript:;" class="QZone"></a><p>QZone</p></li>'+
               '<li><a href="javascript:;" class="wx"></a><p>微信</p></li>'+
               '<li><a href="javascript:;" class="wxq"></a><p>微信朋友圈</p></li>'+
               '<li><a href="javascript:;" class="sinawb"></a><p>新浪微博</p></li>';
        	   var wopai = '';  
        	   var con = [
        	              '<div class="maskBox"></div>',
        	              '<div class="shareBox1">',
        	                  '<div class="shareList">',
        	                      '<ul>'+shareList+'</ul>',
        	                  '</div>',
        	                  wopai,
        	                  '<div class="cancel">取消</div></div>'].join('');
           }else{
        	   var wopai = '<div class="sharewpBox" ><a href="javascript:;" data-videoid="'+videoId+'" data-userId="'+userId+'" class="sharewp">转发到我拍</a></div>';
        	   var con = [
        	              '<div class="maskBox"></div>',
        	              '<div class="shareBox1">',
        	              '<h4>排行受视频数、发布数、粉丝数、赞等影响</h4>',
        	                  '<div class="shareList">',
        	                      '<ul>'+shareList+'</ul>',
        	                  '</div>',
        	                  wopai,
        	                  '<div class="cancel">取消</div></div>'].join('');
           }
            this.shareWxMask = $('<div class="shareWxMask"></div>'); //在微信中得提示
            var shareBox = $('<div class="shareWrap"></div>').html(con);


            $('body').append(shareBox);
            this.shareBoxEl = $('.shareWrap');
            this.shareList = $('.shareList ul');
            this.shareLi = this.shareList.find('li');
            var len = this.shareLi.length;
            var liW = this.shareLi.width();

            this.shareList.css('width',len*(liW+30));

            //调取分享页
            this.shareList.on('click','li a',function(){

                var type = $(this).attr('class');
                sharebox.shareOther(type,tit,desc,pic,videoId,uid,isLive);
            });
            //$('body').off('click','.shareList ul li a',sharebox.shareOther);

            var self = this;
            this.shareBoxEl.on('click','.cancel, .maskBox',function(){
                //this.shareBoxEl.remove();
                sharebox.hide();
                event.preventDefault();
            });

        },

        hide: function(){
            $('.shareWrap').remove();
        },
        shareOther: function(type,tit,desc,pic,videoId,uid,isLive){
        	if(isLive==1){
        		var url= window.location.host+'/restwww/page/live/shareLive' //获取当前页面url不带参数'http://www.baidu.com/';//
        		var params1= {
                        title: tit+"的我拍直播",
                        url: encodeURIComponent('http://'+url+'?roomId='+videoId),  //qq分享的是url地址的问题
                        summary: "小伙伴们~我的直播开始啦~还在等什么~快来找我玩~",
                        pics: pic
                    };
        		var params2= {
                        showcount: 1,
                        url: encodeURIComponent(url+'?roomId='+videoId),
                        title: tit+"的我拍直播",
                        summary: "小伙伴们~我的直播开始啦~还在等什么~快来找我玩~",
                        pics: pic
                    };
        		var urlwb= url+'?roomId='+videoId;

                var arr1= [];
                var arr2= [];
                for(var i in params1){
                    arr1.push(i+'='+params1[i] || '');
                }
                for(var i in params2){
                    arr2.push(i+'='+params2[i] || '');
                }
                if(type == 'qq'){
                    window.open('http://connect.qq.com/widget/shareqq/index.html?'+arr1.join('&'));
                }else if(type == 'QZone'){
                    window.open("http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?"+arr2.join('&'));
                }else if(type == 'wx'){
                    this.shareBoxEl.append(this.shareWxMask);
                }else if(type == 'wxq'){
                    this.shareBoxEl.append(this.shareWxMask);
                }else if(type == 'sinawb'){
                    window.open("http://v.t.sina.com.cn/share/share.php?url="+encodeURI("http://"+urlwb)+"&title="+tit+"的我拍直播&pic="+pic+"&content="+"小伙伴们~我的直播开始啦~还在等什么~快来找我玩~");
                }
        		
        	}else if(isLive==2){
        		
        	}
        	else{
        		var url= window.location.host+'/restwww/page/video/videoDetail' //获取当前页面url不带参数'http://www.baidu.com/';//
                var params1= {
                    title: tit+"的【我拍】",
                    url: encodeURIComponent('http://'+url+'?videoId='+videoId),  //qq分享的是url地址的问题
                    summary: desc,
                    pics: pic
                }
                var params2= {
                    showcount: 1,
                    url: encodeURIComponent(url+'?videoId='+videoId),
                    title: tit+"的【我拍】",
                    summary: desc,
                    pics: pic
                };

                var urlwb= url+'?videoId='+videoId;

                var arr1= [];
                var arr2= [];
                for(var i in params1){
                    arr1.push(i+'='+params1[i] || '');
                }
                for(var i in params2){
                    arr2.push(i+'='+params2[i] || '');
                }
        	
            
            if(type == 'qq'){
                window.open('http://connect.qq.com/widget/shareqq/index.html?'+arr1.join('&'));
            }else if(type == 'QZone'){
                window.open("http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?"+arr2.join('&'));
            }else if(type == 'wx'){
                this.shareBoxEl.append(this.shareWxMask);
            }else if(type == 'wxq'){
                this.shareBoxEl.append(this.shareWxMask)
            }else if(type == 'sinawb'){
                window.open("http://v.t.sina.com.cn/share/share.php?url="+encodeURI("http://"+urlwb)+"&title="+tit+"的【我拍】&pic="+pic+"&content="+desc);
            }
        	}
        }
    };

    return sharebox;

    //     function openshare(type){
    //             var shareplaykey=$("#shareplaykey").val();
    //             var shareName=$("#shareName").val();
    //             var sharedescription=$("#sharedescription").val();
    //             var sharepic=$("#sharepic").val();
    //             var pics="http://api.wopaitv.com/restwww/download"+sharepic;
    //             var title=shareName+"的【我拍TV】";
    //             var wopaiuid = getCookie("wopaiuid");
    //             var url;
    //             if(wopaiuid!=null&&wopaiuid.length>0){
    //             url=window.location.host+"/restwww/video/thirdVideo?playKey="+shareplaykey+"&shareuid="+wopaiuid;
    //             }else{
    //             url=window.location.host+"/restwww/video/thirdVideo?playKey="+shareplaykey;
    //             }
    //             var uid=getCookie("wopaiuid");
    //             if(sharepic==null||sharepic.length<=0){
    //                 pics="http://api.wopaitv.com/restwww/img/logo.png";
    //                 }else{
    //                     if(pics.indexOf("http://") >= 0 ){
    //                     }else{
    //                     pics="http://"+pics;

    //                     }
    //                 }
    //                 if(type=="qzone"){
    //                     window.open("http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url="+encodeURIComponent("http://"+url)+"&summary="+sharedescription+"&pics="+pics+"&title="+title);
    //                 }else if(type=="qqfriend"){
    //                     window.open("http://connect.qq.com/widget/shareqq/index.html?title="+title+"&url="+encodeURIComponent("http://"+url)+"&summary="+sharedescription+"&pics="+pics);
    //                 }else if(type=="weibo"){
    //                     window.open("http://v.t.sina.com.cn/share/share.php?url="+encodeURIComponent("http://"+url)+"&title="+title+"&pics="+pics);
    //                 }
    //     }





    // }
});
