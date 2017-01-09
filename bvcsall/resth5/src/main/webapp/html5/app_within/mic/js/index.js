
$.extend({
    init: function(){
        _this= this;

        _this.picSrc= $('#serverUrlimg').val()+'/restwww/download';
        _this.activeId= $('#activeId').val();
        _this.server= '/resth5';
        _this.joinApi=_this.server+'/live/joinLiveActivity';  //报名接口
        _this.giftApi=_this.server+'/live/beanRank';  //礼物榜接口
        _this.popularApi=_this.server+'/live/popularityRank';  //人气榜接口
        _this.totalApi=_this.server+'/live/totalRank';  //总排名接口
        _this.width= $(window).width();
        _this.height= $(window).height();
        $('.sign-from,.sign-success').height(_this.height);

        $.postDate(_this.totalApi,'liveActivityId='+_this.activeId,function(json){
            $.showList(json.result,$('.tab1:eq(0) ul'),0);
        })

        $('.prize-tit').click(function(){
           $('.rule').show();
           $('.show-box').hide();
        });
        $('.close-rule').click(function(){
            $(this).parent().hide();
            $('.show-box').show();
        })
        $('.sign-btn').click(function(){
            $('.sign-from').show();
            $('.show-box').hide();
        });
        $('.iKonw').click(function(){
            $('.show-box').show();
            $('.sign-success').hide();
        });
        $('.sign-sub').click(function(){
            if($("input[name='uid']").val()==''){
                $.tempTip('用户ID 不能为空');
            }else if($("input[name='school']").val()==''){
                $.tempTip('学校不能为空');
            }else if($("input[name='area']").val()==''){
                $.tempTip('请选择地区');
            }else{
                if(isNaN($("input[name='uid']").val())){
                    $.tempTip('用户ID必须为数字');
                }else{
                    var params= $('#sign-form').serialize();
                    $.postDate(_this.joinApi,params,function(json){
                        if(json.code=='200'){
                            $('.sign-success').show();
                            $('.sign-from').hide();
                            $('.item-name').html(json.result.name);
                            $('.item-school').html(json.result.school);
                            $('.item-area').html(json.result.area);
                        }else{
                            $.tempTip(json.message);
                        }
                    })
                }

            }

        });

        $('.tab-head ul li').click(function(){
            $('.tab-head ul li').removeClass('on');
            $(this).addClass('on');
            var index= $(this).index();
            $('.tab1').hide();
            $('.tab1:eq('+index+')').show();
            if(index==0){
                $.postDate(_this.totalApi,'liveActivityId='+_this.activeId,function(json){
                    $.showList(json.result,$('.tab1:eq(0) ul'),0);
                })
            }else if(index==1){
                $.postDate(_this.popularApi,'liveActivityId='+_this.activeId,function(json){
                    $.showList(json.result,$('.tab1:eq(1) ul'),1);
                })
            }else if(index==2){
                $.postDate(_this.giftApi,'liveActivityId='+_this.activeId,function(json){
                    $.showList(json.result,$('.tab1:eq(2) ul'),2);
                })
            }
        })

    },
    showList: function(json,target,type){
        $(target).empty();
        var oli;
        var nameType='';
        var vStart='';
        $.each(json,function(m,n){
            if(type==0){
                nameType= '热度：'+ parseInt(n.total).toFixed(1);
            }else if(type==1){
                nameType= '人气：'+ parseInt(n.popularity).toFixed(1);
            }else if(type==2){
                nameType= '金豆数：'+ parseInt(n.bean).toFixed(1);
            }
            if(n.vipStat==1){
                vStart='<em class="v-start1"></em>';
            }else if(n.vipStat==2){
                vStart='<em class="v-start2"></em>';
            }else if(n.vipStat==3){
                vStart='<em class="v-start3"></em>';
            }else{
                vStart='';
            }

            var area= n.area;
            var school= n.school;
            if(!n.area){
                area= '未知';
            };
            if(!n.school){
                school= '未知';
            };

            oli= '<li>'+
            '<div class="rank"><em>'+(m+1)+'</em></div>'+
            '<div class="user-info">'+
            '<div class="user-pic-box">'+
            '<img src="'+ ($.picSrc+n.pic)+'" class="fl user-pic" alt=""/>'+vStart+
            '</div>'+
            '<span class="user-info-1">'+
            '<p>'+ n.name+' <em>'+area+'</em></p>'+
            '<p>'+nameType+'</p>'+
            '</span>'+
            '<span class="fr user-info-2">'+school+'</span>'+
            '</div>'+
            '</li>';
            $(target).append(oli);
        });

    },
    //ajax方法
    postDate: function(url,params,callBack){
        $.ajax({
            type: "post",
            url: url,
            data: params,
            success: callBack,
            beforeSend: function(request){
                $('.load-box').show();
                //request.setRequestHeader('access_token', $.access_token);
                //request.setRequestHeader('uid', $.uid);
            },
            dataType: 'json',
            complete: function(){
                $('.load-box').hide();
            }
        })
    },
    //提示语
    tempTip: function(msg,callFn){
        var box = $('<div class="tipsWarp">'+msg+'</div>');
        $('body').append(box);
        setTimeout(function(){
            box.remove();
        },2000);
        callFn && callFn();
    },
    //cookie方法
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
    clearCookie:function(name){
        document.cookie = name+"=;expires=0;;path=/";
    }
})
$.init();
