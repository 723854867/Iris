
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
			"userId" : $.getCookie('uid'),             //用户id
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



		//----展示记录-----
		if(_this.trueNum==1){ //兑换
			$('.record_list').show();
			var exParams= {
				"userId": $.getCookie('uid'),
				"channel": 'exchange'
			}
			$.recordFn(exParams)
		}else if(_this.trueNum==2){ //提现
			$('.record_list_wiht').show();
			var exParams= {
				"userId": $.getCookie('uid'),
				"channel": 'wx_pub'
			}
			$.recordFn(exParams)
		}
		//$('.close_record').click(function(){
		//	window.history.back();
		//	$('.record_list').hide();
		//	$('.record_list_wiht').hide();
		//});
		//----展示记录结束-----
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
		$('.record_exchange .dou_num').html(data.result.diamond);
		if(data.result.list==''){
			$('.record_exchange_list').html('<h2>暂无兑换记录</h2>');
		}else{
			$('.record_exchange_list ul').empty();
			$.each(data.result.list,function(m,n){
				var oLi= '<li>'+
					'<span class="gold_bi"><em class="gold_bi_icon"></em>'+(n.diamond) +' </span>'+
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
	}



})
$.init();
