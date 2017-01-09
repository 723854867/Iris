
$.extend({
    init: function(){
        _this= this;

        _this.interurl=$('#interurl').val();
        _this.subApi= _this.interurl+'/resth5/bstar/register ';
        _this.getCode= _this.interurl+'/resth5/bstar/getCode'
        _this.width= $(window).width();
        _this.height= $(window).height();
        var bjH=$('.bj').height();
        $('.form').css('top',bjH*0.53);
        $('.form-box').css('marginTop',bjH*0.1);
        $('.sub-btn').css('top',bjH*0.9);

        $('.txt1').change(function(){

            $('.showSele').empty().html($(this).find("option:selected").text())
        })



        $('.sub-btn').click(function(){
                var oUid= $('input[name=uid]').val();
                var sex= $('input[name=sex]').val();
                var netNum= $.trim($('input[name=number]').val());
                var phone= $('input[name=phone]').val();
                var code= $('input[name=code]').val();
                if(oUid==''){
                    $.tempTip('请输入ID');
                    return false;
                }else{
                    if(isNaN(oUid)){
                        $.tempTip('ID必须为数字');
                        return false;
                    }
                }
                if(netNum==''){
                    $.tempTip('请输入QQ/微信号');
                    return false;
                }
                if(phone==''){
                    $.tempTip('请输入手机号');
                    return false;
                }else{
                    var re = /^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$/
                    if(!re.test(phone)){
                        $.tempTip('手机号格式不符');
                        return false;
                    }
                }
                if(code==''){
                    $.tempTip('请输入验证码');
                    return false;
                }else{
                    if(isNaN(code)){
                        $.tempTip('验证码为数字');
                        return false;
                    }
                }

                $.postDate(_this.subApi,$('.form-box').serialize(),function(json){

                    if(json.code==200){
                        $.tempTip('提交成功');
                    }else{
                        $.tempTip(json.message);
                    }
                    $('.form-box').find('input[type=text]').val('');
                    $('.form-box').find('input[type=phone]').val('');
                })


        });

        $('.yz-btn').click($.DateEnd);
    },
    
    //倒计时
    DateEnd: function(phone){
        var _this =$(this);
        var phone= $.trim($('input[name=phone]').val());
        if(phone==''){
            $.tempTip('请输入手机号');
            return;
        }else{
            var re = /^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$/
            if(!re.test(phone)){
                $.tempTip('手机号格式不符');
                return false;
            }
        }
        if(_this.hasClass('c2')){}else{
            var html = _this.html();
             _this.addClass('c2').html(59+'秒后重试');
            var t =59;
            var setInterval1 = setInterval(function(){
                 t--;
                 _this.html(t+'秒后重试');
                 
                 if(t==0) {
                    clearInterval(setInterval1);
                    _this.html(html).removeClass('c2').html(html);
                 }
            },1000);
            $.postDate($.getCode,'phone='+phone,function(json){
                if(json.code==200){
                    $.tempTip('发送成功');
                }else{
                    $.tempTip(json.message);
                }

            })
        } 
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
