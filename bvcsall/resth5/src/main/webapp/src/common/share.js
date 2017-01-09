define(['../libs/jquery-2.1.4','../common/browse','../common/aler','../common/info'],function(jquery,browse,alert,info) {

    //'http://connect.qq.com/widget/loader/loader.js'

    var sharebox = {
        maskEl: null,
        shareEl: null,
        //分享需要参数 视频标题标题 分享的地址（生成） 描述 缩略图（视频缩略图） 视频id 分享者Id(uid)
        init: function(tit,desc,pic,videoId,userId,isLive){
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
//        	   var wopai = '<div class="sharewpBox" ><a href="javascript:;" data-videoid="'+videoId+'" data-userId="'+userId+'" class="sharewp">转发到LIVE</a></div>';
        	   var con = [
        	              '<div class="maskBox"></div>',
        	              '<div class="shareBox1">',
        	                  '<div class="shareList">',
        	                      '<ul>'+shareList+'</ul>',
        	                  '</div>',
//        	                  wopai,
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
                sharebox.shareOther(type,tit,desc,pic,videoId,isLive);
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
        shareOther: function(type,tit,desc,pic,videoId,isLive){
        	if(isLive==1){
        		var url= window.location.host+'/resth5/live/shareLive' //获取当前页面url不带参数'http://www.baidu.com/';//
        		var params1= {
                        title: tit+"的LIVE直播",
                        url: encodeURIComponent('http://'+url+'?roomId='+videoId),  //qq分享的是url地址的问题
                        summary: "小伙伴们~我的直播开始啦~还在等什么~快来找我玩~",
                        pics: pic
                    };
        		var params2= {
                        showcount: 1,
                        url: encodeURIComponent(url+'?roomId='+videoId),
                        title: tit+"的LIVE直播",
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
                    window.open('http://connect.qq.com/widget/shareqq/index.jsp?'+arr1.join('&'));
                }else if(type == 'QZone'){
                    window.open("http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?"+arr2.join('&'));
                }else if(type == 'wx'){
                    this.shareBoxEl.append(this.shareWxMask);
                }else if(type == 'wxq'){
                    this.shareBoxEl.append(this.shareWxMask);
                }else if(type == 'sinawb'){
                    window.open("http://v.t.sina.com.cn/share/share.php?url="+encodeURI("http://"+urlwb)+"&title="+tit+"的LIVE直播&pic="+pic+"&content="+"小伙伴们~我的直播开始啦~还在等什么~快来找我玩~");
                }
        		
        	}else if(isLive==2){
        		
        	}
        	else{
        		var url1= window.location.host+'/resth5/video/videoDetail' //获取当前页面url不带参数'http://www.baidu.com/';//
                var params1= {
                    title: tit+"的【LIVE】",
                    url: encodeURIComponent('http://'+url1+'?videoId='+videoId),  //qq分享的是url地址的问题
                    summary: desc,
                    pics: pic
                }
                var params2= {
                    showcount: 1,
                    url: encodeURIComponent(url1+'?videoId='+videoId),
                    title: tit+"的【LIVE】",
                    summary: desc,
                    pics: pic
                };

                var urlwb= url1+'?videoId='+videoId;

                var arr1= [];
                var arr2= [];
                for(var i in params1){
                    arr1.push(i+'='+params1[i] || '');
                }
                for(var i in params2){
                    arr2.push(i+'='+params2[i] || '');
                }
        	
            
            if(type == 'qq'){
                window.open('http://connect.qq.com/widget/shareqq/index.jsp?'+arr1.join('&'));
            }else if(type == 'QZone'){
                window.open("http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?"+arr2.join('&'));
            }else if(type == 'wx'){
                this.shareBoxEl.append(this.shareWxMask);
            }else if(type == 'wxq'){
                this.shareBoxEl.append(this.shareWxMask)
            }else if(type == 'sinawb'){
                window.open("http://v.t.sina.com.cn/share/share.php?url="+encodeURI("http://"+urlwb)+"&title="+tit+"的【LIVE】&pic="+pic+"&content="+desc);
            }
        	}
        }
    };

    return sharebox;
});
