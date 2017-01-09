


$.extend({
	init: function(){
		_this= this;
		_this.jsonData={};
		_this.serverUrl= $('#interfaceurl').val()+$('#catalog').val()+'/identify';
		_this.access_token= $.getCookie('access_token');
		_this.uid= $.getCookie('uid');

		_this.subBoolean= false;  //判断是否修改过暑假
		_this.timer;  //发送验证码时的定时器
		_this.netType= $('.item-select1 .select-word').attr('data-type');
		_this.carIdType= $('input[name=certificateType]').val();
		//接口数据
		_this.postUrl1= _this.serverUrl+"/getIdentifyStatus"; //主播状态
		_this.postUrl2= _this.serverUrl+"/getCode"; //获取验证码（手机号）
		_this.postUrl3= _this.serverUrl+"/anchorValidate"; //验证主播信息合法性
		_this.postUrl4= _this.serverUrl+"/anchorIdentify"; //提交主播信息


		var skyH= $(window).height();
		var skyW= $(window).width();

		$('.live-agreement').css('height',skyH);
		$('.live-agreement-msg').css('height',skyH-120);

	//	数据交互层
		$.postDate(_this.postUrl1,'',function(data){  //主播申请状态
			if(data.code == 200){
				//if(data.result.isExist==0){
				//	$('.first').show();
				//}else if(data.result.isExist==1){
					if(data.result.status==0){
						//$('.apply-status').html('您的直播认证');
					}else if(data.result.status==-1){
						var failSearon= data.result.reason;
						$('.fial-list').html(failSearon);
						$('.apply-status').hide();
						$('.apply-status-fail').show();

						$('.from-box1').show();
						$('.first').hide();
						//var failArr= failSearon.split(';')
						//var failList;
						//$.each(failArr,function(m,n){
						//	if(n==''){
						//		failArr.splice(m,1)
						//	}else{
						//		failList = $('<p>'+ (m+1)+'.'+n+'</p>')
						//	}
						//	$('.fial-list').append(failList);
						//});
					}
				//}
			}
		});
		//判断是否有手机号
		var formData= $('.form-data').val();
		if(formData){
			$('.from3-phone').val(formData).hide();
			$('.from3-phone').parent().append('<p>'+formData+'</p>');
			$('input[type=password]').closest('.form-item').hide();
		}
	//	逻辑判断层
		//点击认证按钮进行下一步认证
		$('.first').delegate('.go-renzheng','click',function(){
			$('.first').hide();
			$('.from-box1').show();
		});

		$('#s_province').change(function(){
			var oVal= $(this).val();
			$(this).siblings('.sele-key').html(oVal);
			$('#s_city').siblings('.sele-key').html('城市');
		})
		$('#s_city').change(function(){
			var oVal= $(this).val();
			$(this).siblings('.sele-key').html(oVal)
		});
		$('#s_bankType').change(function(){
			var oVal= $(this).val();
			$(this).siblings('.sele-key').html(oVal)
		})
		//$('.back-name').focus(function(){
		//	var sheng= $('#s_province').val(),shi= $('#s_city').val();
		//	if(sheng== '省份'){
		//		$.tempTip('省份和城市不能为空');
		//	}else if(shi== '城市'){
		//		$.tempTip('省份和城市不能为空');
		//	}
		//});
		$('#pwd').blur(function(){
			$('#pwd1').val(hex_md5($(this).val()))
		})
		//点击提交按钮第一次提交内容  本地缓存数据
		$(".form-submit1").on('click',function(){

			var isTrue= true;
			var params= $('#from1').serialize();
			_this.jsonData= $.arrTojson(params);
			_this.netType= $('.item-select1 .select-word').attr('data-type');
			_this.carIdType= $('input[name=certificateType]').val();
			$('.from-box1 .item-txt-exsc').each(function(m,n){
				var inputVal= $.trim($(n).val()),inputRe= $(n).attr('re'),objName=$(n).attr('name');
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
					}else if(objName=='test1'){
						if($('#pwd').closest('.form-item').is(':visible')){
							if(!$("input[name='test1']").val()){
								$.tempTip('请设置6-20位密码');
							}
							return false;
						}

					}else if(objName=='bankNumber'){
						$.tempTip('银行卡号不能为空');
						return false;
					}else if(objName=='certificateNumber'){
						$.tempTip('证件号码不能为空');
						return false;
					}else if(objName=='bankAddress'){
						$.tempTip('支行名称不能为空');
						return false;
					}else if(objName=='certificateNumber'){
						$.tempTip('证件号不能为空');
						return false;
					}else if(objName=='file1'){
						$.tempTip('请上传身份证正面照');
						return false;
					}else if(objName=='file2'){
						$.tempTip('请上传身份证反面照');
						return false;
					}else if(objName=='file3'){
						$.tempTip('请上传手持身份证照片');
						return false;
					}


					//不用的
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
						var sheng= $('#s_province').val(),shi= $('#s_city').val(),bankName=$('#s_bankType').val();
						if(sheng== '省份'){
							$.tempTip('省份和城市不能为空');
							return false;
						}else if(shi== '城市'){
							$.tempTip('省份和城市不能为空');
							return false;

						}else if(bankName== '请选择'){
							$.tempTip('请选择开户行');
							return false;
						}
						switch (objName){
							case 'realName':
								$.tempTip('姓名格式不对');
								return false;
							case 'phone':
								$.tempTip('手机号码格式不对');
								return false;
							case 'test1':
								$.tempTip('密码格式不对（6-20位字符）');
								return false;
							case 'bankNumber':
								$.tempTip('银行卡格式不对');
								return false;
							case 'certificateNumber':
								$.tempTip('证件号码格式不对');
								return false;
						}
						//return;
					}
				}
			});
			if(isTrue){
				$('#pwd').attr('name','');  //在提交数据之前将test1的值删掉
				$('#from1').ajaxSubmit({
					type:'post',
					url:_this.postUrl4,
					success:function(data){
						$('.loading').hide();
						if(data.code==200){
							$('.apply-success').show();
							$('.from-box1').hide();
						}else{
							$.tempTip(data.message);
						}
					},
					beforeSend: function(request){
						//var queryString = $.param(formData);
						//console.log(formData);
						//$('#from1').child().not('input[]').serialize()

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
			}

		});

		//获取验证码
		var isCode= true;
		$('.ident-code-ok').on('click',function(){
			var phoneNum= $(this).closest('#from1').find('input[name=phone]').val();
			var inputRe= $('.from3-phone').attr('re');
			var re=new RegExp(inputRe);

			_this.timer= null;
			var datatimer= 60; //60
			if(phoneNum== ''){
				$.tempTip('请输入手机号');return;
			}else{
				if(!re.test(phoneNum)){
					$.tempTip('手机号格式不对');
					return false;
				}
			}
			if(isCode){
				setTime();
				_this.timer= setInterval(setTime,1000);
				$.postDate(_this.postUrl2,'phone='+phoneNum,function(data){
					$.tempTip(data.message);

					if(data.code==200){

					}else{
						$.tempTip(data.message);
						clearInterval(_this.timer)
						$('.ident-code').val('获取验证码').addClass('ident-code-ok').removeClass('ident-code-no').attr('disabled',false);
					}
				})
				isCode= false;
				//return;
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
		$('.see-fail').click(function(){
			if($('.fial-list').is(':visible')){
				$('.fial-list').hide();
				$(this).html('查看');
			}else{
				$('.fial-list').show();
				$(this).html('隐藏');
			}
		})
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
		//图片预览
		$.picView($('.file1'));
		$.picView($('.file2'));
		$.picView($('.file3'));

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
							$('.preview-pho1').attr('src',e.target.result);
						}
						if(obj.hasClass('file2')){
							$('.preview-pho2').attr('src',e.target.result);
						}
						if(obj.hasClass('file3')){
							$('.preview-pho3').attr('src',e.target.result);
						}
						$(picthis).siblings('img').attr('src',e.target.result);
						//document.getElementById('previewImage').src = e.target.result;
					};
				})(f);
				reader.readAsDataURL(f);
			}
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
		$('.tipsWarp').css({
			'top': $.skyH/2+'px'
		});
		$('body').append(box);
		setTimeout(function(){
			box.remove();
		},1000);
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
	},
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
