


$.extend({
	init: function(){
		_this= this;
		_this.jsonData={};
		_this.serverUrl= $('#interfaceurl').val()+$('#catalog').val();
		_this.access_token=$('#access_token').val();
		_this.uid=$('#uid').val();
		_this.subBoolean= false;  //判断是否修改过暑假
		_this.timer;  //发送验证码时的定时器
		_this.netType= $('.item-select1 .select-word').attr('data-type');
		_this.carIdType= $('input[name=certificateType]').val();
		//接口数据
		_this.postUrl1= _this.serverUrl+"/live/getIdentifyStatus"; //主播状态
		_this.postUrl2= _this.serverUrl+"/live/getCode"; //获取验证码（手机号）
		_this.postUrl3= _this.serverUrl+"/live/anchorValidate"; //验证主播信息合法性
		_this.postUrl4= _this.serverUrl+"/live/anchorIdentify"; //提交主播信息

		var linkUrl=$('link').attr('href');
		var tempData= new Date().getTime();

		var skyH= $(window).height();
		var skyW= $(window).width();
		//$('link').attr('href',linkUrl+"?t="+tempData)
		$('.live-agreement').css('height',skyH);
		$('.live-agreement-msg').css('height',skyH-120);

	//	数据交互层

		$.postDate(_this.postUrl1,'',function(data){  //主播申请状态
			if(data.code == 200){
				if(data.result.isExist==0){
					//$.tempTip('无审核信息');
					$('.first').show();
				}else if(data.result.isExist==1){
					if(data.result.status==0){
						$('.from-box4').show();
						$('.first').hide();
						$('.from-box4 .apply-status').html('您的直播认证信息正在审核中...');
					}else if(data.result.status==1){
						$('.first').hide();
						$('.apply-success').show();
						$('.from-box4 .apply-status').html('您的直播申请通过');
					}else if(data.result.status==-1){
						var failSex= $('.form-fail').attr('sex-data'),cateType= $('.form-fail').attr('cateType-data'),qqData= $('.form-fail').attr('qq-data'),wxData= $('.form-fail').attr('wx-data');
						var failSearon= $('.apply-status-fail').attr('fail-data');
						var failArr= failSearon.split(';')
						$('.from-box1').show();
						$('.from-box1 .apply-status').hide();
						//alert(failArr.length)
						var failList;
						$("input[name='certificateType']").val(cateType);
						$('.apply-status-fail').show();
						$.each(failArr,function(m,n){
							if(n==''){
								failArr.splice(m,1)
							}else{
								failList = $('<p>'+ (m+1)+'.'+n+'</p>')
							}
							$('.fial-list').append(failList);
						});
						//判断性别
						if(failSex == 1){
							//男
							$('.sex-box label').find('em').removeClass('sex-icon-ok');
							$('.sex-box label:eq(0)').find('em').addClass('sex-icon-ok');
						}else{
							$('.sex-box label').find('em').removeClass('sex-icon-ok');
							$('.sex-box label:eq(1)').find('em').addClass('sex-icon-ok');
						}
						//判断QQ号
						if(qqData != ''){
							$('.item-select1 .select-word').html('QQ号码：').attr('data-type','qq');
							$('.net-number').val(qqData).attr('re','^\\d+$').attr('name','qq').attr('placeholder','QQ号码');
							$('.net-number').html("QQ号码：");
						}else if(wxData != ''){
							$('.item-select1 .select-word').html('微信号：').attr('data-type','wechat');
							$('.net-number').val(wxData).attr('re','').attr('name','wechat').attr('placeholder','微信号');;
							$('.net-number').html("微信号：");
						}
						//判断证件
						if(cateType==1){
							$('.item-select .select-word').html('身份证号').attr('data-type','1').attr('re',$('.select-list li:eq(0)').attr('re'));
							$('input[name=certificateNumber]').attr('re',$('.select-list li:eq(0)').attr('re'))
						}else if(cateType==2){
							$('.item-select .select-word').html('护照').attr('data-type','2').attr('re',$('.select-list li:eq(1)').attr('re'));
							$('.from-box1 .doc-type1 dl:eq(1),.liver-msg-pic dl:eq(1)').hide();
							$('input[name=certificateNumber]').attr('re',$('.select-list li:eq(1)').attr('re'));
							$('.doc-type1 dl dd').hide();
							$('.liver-msg-pic dl dd').hide();
						}else if(cateType==3){
							$('.item-select .select-word').html('台胞证').attr('data-type','3').attr('re',$('.select-list li:eq(2)').attr('re'));
							$('.from-box1 .doc-type1 dl:eq(1),.liver-msg-pic dl:eq(1)').hide();
							$('input[name=certificateNumber]').attr('re',$('.select-list li:eq(2)').attr('re'));
							$('.doc-type1 dl dd').hide();
							$('.liver-msg-pic dl dd').hide();
						}else{
							$('.item-select .select-word').html('身份证号').attr('data-type','1').attr('re',$('.select-list li:eq(0)').attr('re'));
						}
					}
				}
			}
		});
	//	逻辑判断层
		//点击查看直播协议
		$('.go-agreement').click(function(){
			$('.live-agreement').show();
			$('.first').hide();
		})
		//用户点击关闭协议层
		$('.close-live-agreement').click(function(){
			$(this).closest('.live-agreement').hide();
			$('.first').show();
		});
		//用户同意协议复选框
		$('.agree-check').click(function(){
			if($(this).hasClass('agree-check-ok')){
				$(this).addClass('agree-check-no').removeClass('agree-check-ok');
				$('.go-renzheng').addClass('go-renzheng-no').removeClass('go-renzheng-ok');
			}else{
				$(this).addClass('agree-check-ok').removeClass('agree-check-no');
				$('.go-renzheng').addClass('go-renzheng-ok').removeClass('go-renzheng-no')
			}
		});
		//点击认证按钮进行下一步认证
		$('.first').delegate('.go-renzheng-ok','click',function(){
			$('.first').hide();
			$('.from-box1').show();
		});
		//点击提交按钮第一次提交内容  本地缓存数据
		$(".form-submit1").on('click',function(){
			var isTrue= true;
			var params= $('#from1').serialize();
			_this.jsonData= $.arrTojson(params);
			_this.netType= $('.item-select1 .select-word').attr('data-type');
			_this.carIdType= $('input[name=certificateType]').val();
			$('.from-box1 .item-txt-exsc').each(function(m,n){
				var inputVal= $.trim($(n).val()),inputRe= $(n).attr('re'),inputPlace=$(n).attr('placeholder'),objName=$(n).attr('name');
				var re=new RegExp(inputRe);
				if(inputVal==''){
					isTrue= false;
					if(objName=='realName'){
						$.tempTip('姓名不能为空');
						return false;
					}else if(objName=='phone'){
						$.tempTip('手机号码不能为空');
						return false;
					}else if(objName=='code'){
						$.tempTip('验证码不能为空');
						return false;
					}else if(objName=='qq'){
						$.tempTip('QQ号码不能为空');
						return false;
					}else if(objName=='wechat'){
						$.tempTip('微信号不能为空');
						return false;
					}else if(objName=='certificateNumber'){
						$.tempTip('证件号码不能为空');
						return false;
					}

					//else if(objName=='file1'){
					//	if($(".doc-type1 dl:eq(0)").is(':visible')){
					//		if($("input[name='file1']").val()!=' '){
					//			$.tempTip('照片不能为空');
					//		}
					//	}
					//	return false;
					//}else if(objName=='file2'){
					//	if($(".doc-type1 dl:eq(1)").is(':visible')){
					//		if($("input[name='file2']").val()!=' '){
					//			$.tempTip('照片不能为空');
					//		}
					//	}else{
					//		isTrue= true;
					//	}
					//	return false;
					//}
				}else{
					if(re.test(inputVal)){
						isTrue= true;
					}else{
						isTrue= false;
						switch (objName){
							case 'realName':
								$.tempTip('姓名格式不对');
								return false;
							case 'phone':
								$.tempTip('手机号码格式不对');
								return false;
							case 'qq':
								$.tempTip('QQ号码格式不对');
								return false;
							case 'wechat':
								$.tempTip('微信号格式不对');
								return false;
							case 'certificateNumber':
								$.tempTip('证件号码格式不对');
								return false;
						}
						return;
					}
				}
			});
			if(isTrue){
				$.postDate($.postUrl3,params,function(data){
					if(data.code == 350){
						$.tempTip(data.message);
					}else{
						if(_this.netType=='qq'){
							$('.net-number').html('QQ号码：');
							$('.from2-netNumber').html(_this.jsonData.qq);
							$('.from3-netNumber').val(_this.jsonData.qq);
						}else if(_this.netType=='wechat'){
							$('.net-number').html('微信号：');
							$('.from2-netNumber').html(decodeURIComponent(_this.jsonData.wechat));
							$('.from3-netNumber').val(decodeURIComponent(_this.jsonData.wechat));
						}
						if($.carIdType==1){
							$('.userid-number').html('身份证号：');
						}else if($.carIdType==2){
							$('.userid-number').html('护照：');
						}else if($.carIdType==3){
							$('.userid-number').html('台胞证：');
						}
						//第一次释放本地数据
						$('.from2-realName').html(decodeURIComponent(_this.jsonData.realName));
						$('.from2-sex').html($.sexFn(_this.jsonData.sex));
						$('.from2-phone').html(_this.jsonData.phone);
						$('.from2-carNumber').html(decodeURIComponent(_this.jsonData.certificateNumber));

						//第二次释放本地数据 可编辑状态
						$('.from3-realName').val(decodeURIComponent(_this.jsonData.realName));
						$('.from3-sex').val($.sexFn(_this.jsonData.sex));

						$('.from3-phone').val(_this.jsonData.phone);
						$('.form3-old-phone').html(_this.jsonData.phone);
						$('.form3-carNumber').val(decodeURIComponent(_this.jsonData.certificateNumber));
						$('.from3-code').val(_this.jsonData.code)
						if(_this.jsonData.sex==1){
							$("#from3 .sex-icon:eq("+(_this.jsonData.sex-1)+")").addClass('sex-icon-ok');
						}else{
							$("#from3 .sex-icon:eq("+(_this.jsonData.sex-1)+")").addClass('sex-icon-ok');

						}
						$('.form-submit1').closest('.from-box1').hide();
						$('.from-box2').show();
						clearInterval(_this.timer);
						$('.ident-code').val('获取验证码').addClass('ident-code-ok').removeClass('ident-code-no');
					}
				})
			}
		});
		//点击手机号修改
		$('.phone-modify-btn').click(function(){
			$(this).closest('.box3-phone-number').hide();
			$(this).closest('.box3-phone-number').siblings('input').show();
			$('.phone-inspect').show();
		})
		//获取验证码
		var isCode= true;
		$('.ident-code-ok').on('click',function(){


			var phoneNum= $(this).closest('#from1').find('input[name=phone]').val();
			var inputRe= $('.from3-phone').attr('re');
			var re=new RegExp(inputRe);

			_this.timer= null;
			var datatimer= 60;



			if(phoneNum== ''){
				$.tempTip('请输入手机号');return;
			}else{
				if(!re.test(phoneNum)){
					$.tempTip('手机号格式不对');
					return;
				}
			}
			if(isCode){
				setTime();
				_this.timer= setInterval(setTime,1000)
				$.postDate(_this.postUrl2,'phone='+phoneNum,function(data){
					if(data.code==200){

					}else{
						$.tempTip(data.message);
						clearInterval(_this.timer)
						$('.ident-code').val('获取验证码').addClass('ident-code-ok').removeClass('ident-code-no').attr('disabled',false);
					}
				})
				isCode= false;
				return;
			}
			function setTime(){
				$('.ident-code').val((datatimer--)+'s').addClass('ident-code-no').removeClass('ident-code-ok').attr('disabled','disabled');
				if(datatimer<=0){
					$('.ident-code').val('获取验证码').addClass('ident-code-ok').removeClass('ident-code-no').attr('disabled',false);
					clearInterval(_this.timer);
				}
			}
			setTimeout(function(){
				isCode= true;
			},3000)
		});
		//点击修改返回修改
		$('.modify-btn').click(function(){
			_this.subBoolean=true;
			$(this).closest('.from-box2').hide();
			$('.from-box1').show();
			$('.box3-phone-number').show();

			if($('.from3-phone').is(':visible')){
				$('.from3-phone').hide();
				$('.phone-inspect').hide();

			}
		});
        //点击提交数据 再次进入确认页
		$('.sub-btn').click(function(){
			$('#from1').ajaxSubmit({
				type:'post',
				url:_this.postUrl4,
				success:function(data){
					$('.loading').hide();
					if(data.code==200){

						$('.from-box2').hide();
						$('.apply-success').show();
					}else if(data.code==303){
						$.tempTip(data.message);
						$('.from-box2').hide();
						$('.from-box1').show();
					}else if(data.code==350){
						$.tempTip(data.message);
						$('.from-box2').hide();
						$('.apply-fail').show();
					}
				},
				beforeSend: function(request){
					request.setRequestHeader('access_token', $.access_token);
					request.setRequestHeader('uid', $.uid);
					$('.loading').show();
				},
				error:function(XmlHttpRequest,textStatus,errorThrown){
					//console.log(XmlHttpRequest);
					//console.log(textStatus);
					//console.log(errorThrown);
				}
			});

		});
		//提交失败后再次提交
		$('.again-sub').click(function(){
			$(this).closest('.apply-fail').hide();
			$('.from-box1').show();
		})
		$('.see-fail').click(function(){
			if($('.fial-list').is(':visible')){
				$('.fial-list').hide();
				$(this).html('查看');
			}else{
				$('.fial-list').show();
				$(this).html('隐藏');
			}
		})

		//性别选择
		$('.sex-icon').click(function(){
			$('.sex-select').attr('value',$(this).attr('data'));
			$(this).closest('.item-right').find('.sex-icon').removeClass('sex-icon-ok');
			$(this).addClass('sex-icon-ok');
		});
		//关闭证件提示
		$('.close-parmpt').click(function(){
			$(this).closest('.parmpt-warp').hide();
		});
		//点击查看示例图片
		$(".go-cart-tips").click(function(){
			var key= $('.item-select .select-word').attr('data-type');
			$(".parmpt-warp").show();
			$(".parmpt-warp>div").hide();
			$('.pic'+key).show();
		});
		//QQ号 微信号切换
		$.selectFn($(".item-select1"),$(".select-list1"),function(){
			$.netType= $(".item-select1 .select-word").attr("data-type");
			var netTxt= $(".item-select1 .select-word").html();
			var netRe= $(".item-select1 .select-word").attr('re');
			$('.net-number,.from3-netNumber').attr('name',$.netType);

			$('.net-number').attr('placeholder',netTxt);
			$('.net-number').attr('re',netRe);
		});

		$(document).click(function(){
			$(".select-list1,.select-list").hide();
		})

		//身份证件切换
		$.selectFn($(".item-select"),$(".select-list"),function(){
			$.carIdType= $(".item-select .select-word").attr('data-type');
			var carIdTxt= $(".item-select1 .select-word").html();
			var carRe= $(".item-select .select-word").attr('re');

			$("input[name='certificateType']").attr('value',$.carIdType);
			//$("input[name='certificateType']").attr('value',$.carIdType);
			$("input[name='certificateNumber']").attr('re',carRe);

			if($.carIdType != 1){
				$('.doc-type1 dl:eq(1)').hide();
				$('.liver-msg-pic dl:eq(1)').hide();
				$('.doc-type1 dl dd').hide();
				$('.liver-msg-pic dl dd').hide();
			}else{
				$('.doc-type1 dl').show();
				$('.liver-msg-pic dl').show();
				$('.doc-type1 dl dd').show();
				$('.liver-msg-pic dl dd').show();
			}



		});
		//图片预览
		$.picView($('.file1'));
		$.picView($('.file2'));
	},
	//验证主播信息有效性
	Validate: function(params){
		var isOk= true;
		$.postDate($.postUrl3,params,function(data){
			if(data.code == 350){
				$.tempTip(data.message);
				isOk= false;
			}else{
				isOk= true;
			}
		})
		return isOk;
	},
	//图片预览
	picView: function(obj){
		$(obj).change(function(event){
			picthis=this;
			if (!window.FileReader) return;
			var files = event.target.files;
			for (var i = 0, f; f = files[i]; i++) {
				if (!f.type.match('image.*')) {
					continue;
				}
				var reader = new FileReader();
				reader.onload = (function(theFile) {
					return function(e) {
						if(obj.hasClass('file1')){
							$('.from2-file1').attr('src',e.target.result)
							$('.preview-pho1').attr('src',e.target.result);
						}
						if(obj.hasClass('file2')){
							$('.from2-file2').attr('src',e.target.result);
							$('.preview-pho2').attr('src',e.target.result);
						}
						$(picthis).siblings('img').attr('src',e.target.result);
						//document.getElementById('previewImage').src = e.target.result;
					};
				})(f);
				reader.readAsDataURL(f);
			}
		})
	},
	selectFn: function(selecttit,selectlist,callFn){
		$(selecttit).find('h3').click(function(){
			if($(selectlist).is(":hidden")){
				$(selectlist).show();
			}else{
				$(selectlist).hide();
			}
			return false;
		});
		$(selectlist).find("li").click(function(){
			$(selecttit).find('.select-word').html($(this).html()).attr('data-type',$(this).attr('data-type')).attr('re',$(this).attr('re'));
			$(selectlist).hide();
			callFn && callFn();
			return false;
		})

	},
	sexFn: function(sex){
		var sexVal;
		if(sex==1){
			sexVal='男';
		}else{
			sexVal='女';
		}
		return sexVal;
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
	//验证方法
	exscFn: function(reg,val,msg){
		if(val!=''){
			var re=new RegExp('^'+reg+'$', 'i');
			if(re.test(val)){
				$.tempTip(msg);
				return false;
			}
		}else{
			$.tempTip(msg);
			return false;
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
				request.setRequestHeader('access_token', $.access_token);
				request.setRequestHeader('uid', $.uid);
			},
			dataType: 'json'
		})
	},
	arrTojson: function (str){
		var  arr1= str.split('&');
		var jsonA={};
		for(var i=0;i<arr1.length;i++){
			var ab1=arr1[i].split('=')[0];
			var ab2=arr1[i].split('=')[1];
			jsonA[ab1]=ab2;
		}
		return jsonA;
	}
})

$.init();
