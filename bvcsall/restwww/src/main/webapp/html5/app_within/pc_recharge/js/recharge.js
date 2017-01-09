$(function(){
    var token = $.getCookie('access_token');
    var payStatus = $('#pay_status').val();
    var listApi = $.requestUrlPrefix + '/live/getDiamondList';  //参数2 为大额支付
    var creat99Api = $.requestUrlPrefix + '/pay/create99billOrder';
    var signMsgValApi =  $.requestUrlPrefix + '/pay/signMsgVal';

    var payApi = $.requestUrlPrefix + '/pay/pay99Bill';

    var payParams = {    //支付参数
        productId : 0,
        uid : $('#uid').val(),
        bankId : 0,
        oAmt : 0
    }

    $('.warp_l').height($('.warp').height());
    if(payStatus){
        if(payStatus == 1){
            $('.sucess').show();
            $('.fial').hide();
        }else if(payStatus == 2){
            $('.sucess').hide();
            $('.fial').show();
        }
    }
    if(!$.getCookie('uid')){
        $('.log_box,.skyBox').show();   //暂时关闭自动弹出登录
        $('.for_log').show();
        $('.user-box').hide();
    }else{
        $.ajax({
            url : $.requestUrlPrefix+'/userInfo',
            data : {
                'access_token' : token
            },
            type : 'get',
            cache : false,
            dataType : 'json',
            beforeSend: function(request){
                $('.load-box').show();
                request.setRequestHeader('access_token', $.getCookie('access_token'));
                request.setRequestHeader('uid', $.getCookie('uid'));
                request.setRequestHeader('openID', $.getCookie('openid'));
            },
            success : function(data) {
                if (data["code"] == "200") {
                    $('.user_pic').attr('src', $.picUrlPrefix+data.result.pic)
                    $('.user_name').html(data.result.name);
                    $('.user_code').html(data.result.id);
                    $('.balance').html(data.result.diamondCount);

                    $('#uid').val($.getCookie('uid'))
                    $('#tk').val($.getCookie('access_token'))

                    $('.for_log').hide();
                    $('.user-box').show();

                }else if(data["code"] == "401"){
                    $('.for_log').show();
                    $('.user-box').hide();
                    $.clearCookie('uid');
                    $.clearCookie('access_token')
                }else {
                    $.tempTip(data["message"]);
                }
            },
            error : function() {
                $.tempTip("获取资源失败！");
            }
        });
    };

    $.postDate(listApi,'isApple='+2,function(data){
        var customCount = '<li><p>自定义金额</p></li>'
        $.each(data.result,function(m,n){
            var giveCount = '';
            var DiamondList;
            if(n.isGive=='1'){
                giveCount = '<span class="giveCount">赠送'+ n.giveCount +'金币</span>';
                DiamondList = '<li data-id="'+ n.id +'" data-price="'+ n.price +'"><input type="radio" name="productId" value="'+ n.id +'"/>'+ n.diamondCount +'金币<br>'+
                '￥'+ n.price +' '+ giveCount +'</li>';
            }else{
                DiamondList = '<li class="is_give_list" data-id="'+ n.id +'" data-price="'+ n.price +'"><input type="radio" name="productId" value="'+ n.id +'"/>'+ n.diamondCount +'金币<br>'+
                '￥'+ n.price +'</li>';
            }
            $('.recharge_list ul').prepend(DiamondList)

        })
    })

    $('.zdy_txt').keyup(function(){
        var thisVal = $(this).val();

        this.value=this.value.replace(/[^\d]/ig,'');
        var newVal = parseInt(this.value);
        if(!isNaN(newVal)){
            $('.diamond_count').html(newVal*10);
            $('.zdy_count').html(newVal);
        }else{
            $('.diamond_count').html('');
            $('.zdy_count').html('');
        }

        checkBtn();
    });
    //$('.zdy_txt').blur(function(){
    //    if($(this).val() == 0){
    //        $.tempTip('金额不能为零！');
    //        $(this).val('');
    //        $('.diamond_count').html('0');
    //        $('.zdy_count').html('0');
    //    }
    //})
    $('body').on('click','.recharge_btn_ok',function(){
        payParams.uid = $('#uid').val();
        if(!$.getCookie('uid')){
            $('.log_box,.skyBox').show();
        }else{
            if(payParams.productId == 0){
                payParams.oAmt = $('.zdy_txt').val();
                //creatPayParams.oAmt = $('.zdy_txt').val();
                if(payParams.oAmt == 0){
                    //$.tempTip("请输入自定义金额！");
                    checkBtn()
                    return;
                }
            }
            if(payParams.bankId==0){
                //$.tempTip("选择购付费银行！");
                checkBtn()
            }else{
                $.ajax({
                    type: "post",
                    url: creat99Api,
                    data: payParams,
                    cache:false,
                    async:false,
                    beforeSend: function(request){
                        request.setRequestHeader('access_token', $.getCookie('access_token'));
                        request.setRequestHeader('uid', $.getCookie('uid'));
                    },
                    success: function(data){
                        var val1 = data.result.orderAmount;
                        var val2 = data.result.productName;
                        var val3 = data.result.orderId;
                        $('input[name=orderAmount]').val(val1);
                        $('input[name=productName]').val(val2);
                        $('input[name=orderId]').val(val3);
                        var signParams = {
                            inputCharset : $('input[name=inputCharset]').val(),
                            pageUrl : $('input[name=pageUrl]').val(),
                            bgUrl : $('input[name=bgUrl]').val(),
                            version : $('input[name=version]').val(),
                            language: $('input[name=language]').val(),
                            signType: $('input[name=signType]').val(),
                            merchantAcctId: $('input[name=merchantAcctId]').val(),
                            payerName: $('input[name=payerName]').val(),
                            payerContactType : $('input[name=payerContactType]').val(),
                            payerContact : $('input[name=payerContact]').val(),
                            orderId : $('input[name=orderId]').val(),
                            orderAmount : $('input[name=orderAmount]').val(),
                            orderTime : $('input[name=orderTime]').val(),
                            productName : $('input[name=productName]').val(),
                            productNum : $('input[name=productNum]').val(),
                            productId : $('input[name=productId]').val(),
                            productDesc : $('input[name=productDesc]').val(),
                            ext1 : $('input[name=ext1]').val(),
                            ext2 : $('input[name=ext2]').val(),
                            payType : $('input[name=payType]').val(),
                            bankId : $('input[name=bankId]').val(),
                            redoFlag : $('input[name=redoFlag]').val(),
                            pid : $('input[name=pid]').val()
                        }

                        $.ajax({
                            type: "post",
                            url: signMsgValApi,
                            data: signParams,
                            cache:false,
                            async:false,
                            beforeSend: function(request){
                                request.setRequestHeader('access_token', $.getCookie('access_token'));
                                request.setRequestHeader('uid', $.getCookie('uid'));
                            },
                            success: function(data){
                                var signMsg = data.result.signMsg;

                                $('input[name=signMsg]').val(signMsg);
                                $('.tip_box,.skyBox').show();
                            }

                        })

                    }

                })
                $('#form1').submit();
            }
        }
    })
    //$('#pay_type ul li').click(function(){  //支付宝
    //    $('#pay_type ul li').removeClass('on');
    //    $(this).addClass('on');
    //    payParams.payType = 1;
    //})
    $('#bank_list ul li').click(function(){
        var bankId = $(this).attr('data-type');
        $('#bank_list ul li').removeClass('on');
        $('#bank_list ul li input[type=radio]').attr('checked',false);
        $('#bank_list ul li .corner').hide();
        $(this).addClass('on');
        $(this).find('.corner').show();
        $(this).find('input[type=radio]').attr('checked','checked');
        $('input[name=bankId]').val(bankId);



        payParams.bankId = bankId;
        checkBtn()
    })

    $('.close_btn,.close_tip').click(function(){
        $('.log_box,.tip_box,.skyBox').hide();
    })

    $('.for_log').click(function(){
        $('.log_box,.skyBox').show();
    })
    $('body').on('click','.recharge_list ul li',function(){
        var thisId = $(this).attr('data-id')
        var liLen = $('.recharge_list ul li').length;
        var liIndex = $(this).index();
        var thisPrice = $(this).attr('data-price');
        $('.recharge_list ul li').removeClass('on');
        $('.recharge_list ul li input[type=radio]').attr('checked',false);
        $(this).addClass('on');
        $(this).find('input[type=radio]').attr('checked','checked');
        //alert(+':'+liLen)
        if(liIndex == liLen-1){
            $('.zdy_val_box').show();   //用户自定义金额
            payParams.productId = 0;
            $('input[name=productId]').val('0');
            $(".zdy_txt").val('');
            $(".zdy_count").html('')
        }else{
            payParams.productId = thisId;
            $('input[name=productId]').val(thisId);
            $(".zdy_count").html(thisPrice);
            $(".diamond_count").html('');
            $('.zdy_val_box').hide();   //选择金额
        }
        checkBtn();
    })


    //银行更多下拉收起
    $('.more_bank').click(function(){
        payParams.bankId = 0;
        $(this).hide();
        $('.hide_bank_list').show();
    })
    $('.more_up').click(function(){
        payParams.bankId = 0;
        $('.more_bank').show();
        $('.hide_bank_list').hide();
    })

    $('.log_btn').click(function(){
        loginFn();
    });

    $('.exit_btn').click(function(){
        $('.exit_box').show();
        $('.exit_box').click(function(){
            $.clearCookie('access_token');
            $.clearCookie('uid');
            window.location.href = 'http://www.wopaitv.com/';
        });
    });

    $('.log_txt').keyup(function(ev){
        if(ev.keyCode == '13'){
            loginFn();
        }
    })




    function loginFn(){
        var userPhone= $('.phone').val();
        var userPwd= $('.pwd').val();


        //要加判断条件
        if(userPhone == '' || userPwd == ''){
            //$.tempTip('用户名或者密码不能为空！');
            $('.err_msg').html('用户名或者密码不能为空!')
            return;
        }
        $.getAuthorization_code(userPhone,userPwd);
    }


    $('.log_txt').keyup(function(){
        checkTxt();
    })
    function checkTxt(){
        var userPhone= $('.phone').val();
        var userPwd= $('.pwd').val();
        if(userPhone.length == 11 && userPwd.length >= 6){

            $('.log_btn').addClass('log_btn_ok')
        }else{
            $('.log_btn').removeClass('log_btn_ok')
        }
    }
    function checkBtn(){
        if(payParams.bankId != 0){
            if(payParams.productId == 0){
                if($('.zdy_txt').val() != ''){
                    $('.recharge_btn').addClass('recharge_btn_ok');
                }else{
                    $('.recharge_btn').removeClass('recharge_btn_ok');
                }
            }else{
                $('.recharge_btn').addClass('recharge_btn_ok');
            }
        }else{
            $('.recharge_btn').removeClass('recharge_btn_ok');
        }
    }




})