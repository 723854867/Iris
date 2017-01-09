
$.extend({
	init: function(){
		_this= this;
		//_this.httpUrl= 'http://ceshi.wopaitv.com/restwww/download/';
		_this.exchangeApi=$.requestUrlPrefix+'/exchange/getExchangeListByType'; //提现、兑换信息列表
		_this.transApi= $.requestUrlPrefix+'/pay/transfer'; //提现接口
		_this.recordApi= $.requestUrlPrefix+'/pay/getTransferRecord';   //'requestUrlPrefix'; //记录接口
		_this.goExchangeApi= $.requestUrlPrefix+'/pay/exchange'; //置换接口
		_this.href= window.location.href;
		_this.trueNum=1;   //1:兑换  2:提现
		_this.params= {};
		_this.exchangeId;

		//判断列表类型
		if(_this.href.indexOf('exchange') != -1){
			_this.trueNum=1;

		}else if(_this.href.indexOf('wx_pub') != -1){
			_this.trueNum=2;

		}
		//-----ajax请求兑换/提现列表-----
		_this.params= {
			"openId" : $.getCookie('openId'),             //用户openId
			"type" : _this.trueNum
		};
		$.postDate(_this.exchangeApi,_this.params,function(data){
			if(data.code == '200'){
				if(_this.trueNum==1){ //兑换
					$.exchangeFn(data);
				}else if(_this.trueNum==2){ //提现
					$.withdrawalsFn(data);
				}
			}else if(data.code == '400_5'){
				window.location.href='login.jsp';
			}
		});
		//-----ajax请求兑换/提现列表结束-----

		//兑换列表点击事件
		$('.exchange_list').delegate('ul li','click',function(){
			$('.exchange_list ul li').removeClass('on');
			$(this).addClass('on');
			_this.exchangeId= $(this).attr('data-id');
			_this.exchangeBi = $(this).find('span.gold_dou').text();
		});
		//提现列表点击
		$('.exchange_list_wiht').delegate('ul li','click',function(){
			$('.exchange_list_wiht ul li').removeClass('on');
			$(this).addClass('on');
			_this.exchangeId= $(this).attr('data-id');
			_this.exchangeGoldBi = $(this).find('span.gold_dou').text();
			_this.exchangeGold = $(this).find('span.gold_bi').text();
			console.log(_this.exchangeGoldBi);
		});

		//----兑换、提现按钮操作-----
        //兑换
		$('.duihuan').click(function(){
			//console.log(_this.exchangeParams);
			if(_this.exchangeId== undefined || _this.exchangeId== ''){
				$.tempTip('请选择兑换的金币数');return;
			}else{
				if(parseInt(_this.exchangeBi) > parseInt($('.bean_num').html())){
					$.tempTip('您的金豆数量不足！');
				}else{
					$.postDate(_this.goExchangeApi,'exchangeId='+_this.exchangeId,function(data){
						if(data.code=='200'){
							//置换成功 同时给弹出框赋值
							$('.buy-ok').show();
							$('.skyBox').show();
							$('.gold-num').html($('.on .gold_bi').html());
							$('.bean_num').html(data["result"]["point"]);
						}else if(data.code=='400_5'){
							window.location.href='login.jsp';
						}
					})
				}
			}

		})
		//提现
		$('.tixian').click(function(){
			var $this= $(this)
			var num = 10;
			var timer=null;
			var htmlVal= $this.html();

			if(_this.exchangeId== undefined || _this.exchangeId== ''){
				$.tempTip('请选择提现金额')
			}else{
				if(parseInt(_this.exchangeGoldBi) > parseInt($('.bean_num').html())){
					$.tempTip('您的金豆数量不足！');return;
				}else{

					if($this.hasClass('on')){
					}else{
						$.postDate(_this.transApi,'transferId='+_this.exchangeId,function(data){
							if(data.code=='200'){
									//提现成功  同时给弹出框赋值
								$('.buy-ok').show();
								$('.skyBox').show();
								$('.gold-num').html($('.on .gold_bi').html());
								$('.bean_num').html(data["result"]["point"]);
								//处理用户多次点击问题

									$this.addClass('on');
									timer=setInterval(function(){

										$this.html(num+'秒后可再次提现');
										num--;
										if(num <=0){
											clearInterval(timer);
											$this.html(htmlVal);
											$this.removeClass('on')
										}
									},1000);

							}else if(data.code=='400_5'){
								window.location.href='login';
							}else{
								$.tempTip(data["message"]);
							}
						})
					}
				}
			}
		})
		$('.close-btn').click(function(){
			$('.buy-ok,.skyBox').hide();
		});
		//----兑换、提现按钮操作-----




	},
	//请求记录接口
	recordFn: function(params){
		$.postDate(_this.recordApi,params,function(data){
			if(data.code=='200'){
				if(_this.trueNum==1){
				//	兑换
					$.recordList1(data);
				}else if(_this.trueNum==2){
					//提现
					$.recordList2(data)
				}
			}else if(data.code=='400_5'){
				window.location.href='login.jsp';
			}
		})
	},
	//兑换记录实例化
	recordList1: function(data){
		$('.record_exchange .dou_num').html(data.result.pointCount);
		if(data.result.list==''){
			$('.record_exchange_list').html('<h2>暂无兑换记录</h2>');
		}else{
			$('.record_exchange_list ul').empty();
			$.each(data.result.list,function(m,n){
				var oLi= '<li>'+
					'<span class="gold_bi"><em class="gold_bi_icon"></em>'+(n.diamond) +'<em>'+n.exchangePoint+'金豆</em> </span>'+
					'<span class="gold_dou">'+ n.date +'</span>'+
					'</li>';
				$('.record_exchange_list ul').append(oLi);
			})

		}
	},
	//提现记录实例化
	recordList2:function(data){
		$('.money_num').html((data.result.price/100));
		$('.record_list_wiht .dou_num').html(data.result.pointCount+'金豆');
		if(data.result.list==''){
			$('.record_wiht_list').html('<h2>暂无提现记录</h2>');
		}else{
			$('.record_wiht_list ul').empty();
			$.each(data.result.list,function(m,n){
				var oLi= '<li>'+
					'<span class="gold_bi">￥'+ (n.amount/100) +'<em>'+ n.exchangePoint +'金豆</em> </span>'+
					'<span class="gold_dou">'+ n.date +'</span>'+
					'</li>';
				$('.record_wiht_list ul').append(oLi);
			});

		}
	},






	//提现实例化
	withdrawalsFn: function(data){
		$.each(data.result,function(m,n){
			var oLi= '<li data-id="'+ n.id +'">'+
				'<span class="gold_bi">￥ '+n.price+'</span>'+
				'<span class="gold_dou">'+n.pointCount +'<em class="gold_dou_icon"></em></span>'+
				'</li>';
			$('.exchange_list_wiht ul').append(oLi);
		})
		$('.bean_num').html(data.extraData);
	},
	//兑换实例化
	exchangeFn: function(data){
		$.each(data.result,function(m,n){
			var oLi= '<li data-id="'+ n.id +'">'+
						'<span class="gold_bi"><em class="gold_bi_icon"></em>'+ n.diamondCount +'</span>'+
						'<span class="gold_dou">'+  n.pointCount +' <em class="gold_dou_icon"></em></span>'+
			         '</li>';
			$('.exchange_list ul').append(oLi);
		})
		$('.bean_num').html(data.extraData);
	}


})
$.init();
