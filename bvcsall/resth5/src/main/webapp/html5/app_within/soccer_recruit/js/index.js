$.extend({
	init: function(){
		_this= this;
		_this.serverUrl= $('#interfaceurl').val()+$('#catalog').val();
		//_this.access_token=$('#access_token').val();
		//_this.uid=$('#uid').val();

		_this.timer;  //发送验证码时的定时器
		_this.netType= $('.item-select1 .select-word').attr('data-type');
		_this.carIdType= $('input[name=certificateType]').val();
		//接口数据
		_this.postUrl2= _this.serverUrl+"/footballGirl/getCode"; //获取验证码（手机号）  提测试的时候要加上 /resth5/
		_this.postUrl4= _this.serverUrl+"/footballGirl/register"; //提交信息    提测试的时候要加上 /resth5/
		var skyH= $(window).height();
		var skyW= $(window).width();
		$('.sub_ok').height($(document).height());
	//	逻辑判断层

		$('.form-sub').click(function(){
				var isOk= true;
				$('.item-txt-exsc').each(function(m,n){
					var objName= $(this).attr('name')
					var reg= $(this).attr('re');
					var objOvl= $.trim($(this).val());
					if(objOvl==''){
						isOk= false;
						if(objName=='uid'){
							$.tempTip('请填写您的LIVEID');
							return false;
						}else if(objName=='job'){
							$.tempTip('请填写您的职位');
							return false;
						}else if(objName=='area'){
							$.tempTip('请填写您的地区');
							return false;
						}else if(objName=='phone'){
							$.tempTip('请填写您的手机号');
							return false;
						}else if(objName=='code'){
							$.tempTip('请填写您的验证码');
							return false;
						}
					}else{
						if(reg!=undefined){
							var re=new RegExp('^'+reg+'$', 'i');
							//console.log(re)
							if(re.test(objOvl)){
								isOk= true;
							}else{
								if(objName=='uid'){
									$.tempTip('LIVEID必须为数字');
									isOk= false;
									return false;
								}else if(objName=='phone'){
									$.tempTip('手机号码格式错误');
									isOk= false;
									return false;
								}

							}
						}
					}
				})

			if(isOk){
				$('#from1').ajaxSubmit({
					type:'post',
					url:_this.postUrl4,
					success:function(data){
						$('.loading').hide();
						if(data.code==200){
							$('.sub_ok').show();
							$('.main').hide();
						}else{
							$.tempTip(data["message"]);
						}
					},
					beforeSend: function(request){
						$('.loading').show();
					},
					error:function(XmlHttpRequest,textStatus,errorThrown){
						//console.log(XmlHttpRequest);
						//console.log(textStatus);
						//console.log(errorThrown);
					}
				});

			}
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

			$this= $(this);
			if($this.hasClass('ident-code-no')){}else{
				$this.val(datatimer+'s').addClass('ident-code-no');
				_this.timer= setInterval(function(){
					var _txt= $this.val();
					datatimer--;
					$('.ident-code').val(datatimer+'s').addClass('ident-code-no').removeClass('ident-code-ok').attr('disabled','disabled');
					if(datatimer==0){
						$('.ident-code').val('获取验证码').addClass('ident-code-ok').removeClass('ident-code-no').attr('disabled',false);
						clearInterval(_this.timer);
					}

				},1000);
				$.postDate(_this.postUrl2,'phone='+phoneNum,function(data){
					if(data.code==200){
						//	验证码成功
						$.tempTip(data.message);
					}else{
						$.tempTip(data.message);
						clearInterval(_this.timer)
						$('.ident-code').val('获取验证码').addClass('ident-code-ok').removeClass('ident-code-no').attr('disabled',false);
					}
				})

			}

			//if(isCode){
			//	setTime();
			//	_this.timer= setInterval(setTime,1000)
			//	$.postDate(_this.postUrl2,'phone='+phoneNum,function(data){
			//		if(data.code==200){
			//		//	验证码成功
			//		}else{
			//			$.tempTip(data.message);
			//			clearInterval(_this.timer)
			//			$('.ident-code').val('获取验证码').addClass('ident-code-ok').removeClass('ident-code-no').attr('disabled',false);
			//		}
			//	})
			//	isCode= false;
			//	//return;
			//}
			//function setTime(){
			//	$('.ident-code').val((datatimer--)+'s').addClass('ident-code-no').removeClass('ident-code-ok').attr('disabled','disabled');
			//	if(datatimer<=0){
			//		$('.ident-code').val('获取验证码').addClass('ident-code-ok').removeClass('ident-code-no').attr('disabled',false);
			//		clearInterval(_this.timer);
			//	}
			//}
			//setTimeout(function(){
			//	isCode= true;
			//},3000)
		});

		////图片预览
		//$.picView($('.file-btn'));
	},

	//图片预览
	picView: function(obj){
		$(obj).change(function(event) {
			picthis = this;

			if (!window.FileReader) return;
			var files = event.target.files;
			for (var i = 0, f; f = files[i]; i++) {
				if (!f.type.match('image.*')) {
					continue;
				}
				var reader = new FileReader();
				reader.onload = (function (theFile) {
					return function (e) {
						if (obj.hasClass('file1')) {

							$('.preview-pho1').attr('src', e.target.result);
						}
						$(picthis).siblings('img').attr('src', e.target.result);
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
			$(selecttit).find('.select-word').html($(this).html());
			$(selectlist).hide();
			if($(this).index()==0){
				$('input[name=wechat]').hide().val('');
				$('input[name=qq]').show();

			}else{
				$('input[name=qq]').hide().val('');
				$('input[name=wechat]').show();
			}
			callFn && callFn();
			return false;
		})

	},
	//sexFn: function(sex){
	//	var sexVal;
	//	if(sex==1){
	//		sexVal='男';
	//	}else{
	//		sexVal='女';
	//	}
	//	return sexVal;
	//},
	//提示语
	tempTip: function(msg,callFn){
		var box = $('<div class="tipsWarp">'+msg+'</div>');
		$('body').find('.tipsWarp').remove();
		//box.remove();
		$('body').append(box);
		setTimeout(function(){
			box.remove();
		},2000);
		callFn && callFn();
	},
	//验证方法
	exscFn: function(reg,val,msg){
		var isbd= false;
		if(val!=''){
			if(reg!= undefined){
				var re=new RegExp('^'+reg+'$', 'i');
				if(re.test(val)){
					isbd= true;
				}else{
					isbd= false;
				}
			}
		}else{
			isbd= false;
		}
		return isbd;
	},
	//ajax方法
	postDate: function(url,params,callBack){
		$.ajax({
			type: "post",
			url: url,
			data: params,
			success: callBack,
			beforeSend: function(request){

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
