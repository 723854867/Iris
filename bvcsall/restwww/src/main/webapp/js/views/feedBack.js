require(['../libs/jquery-2.1.4','../common/playbox2','../common/info','head','../common/ajax','../common/aler','../service/service'],function(jquery,playbox2,info,head,Ajax,alert,service){
	var ctn = [];
	var arr = [];
	var interUrl = info.getRequestPrefix();
	var interfaceurl = $('#interfaceurl').val();
	var feedBack = {
		init:function(){
			
			feedBack.addFeedback();
			feedBack.feedSelect($('.main ul li'));
			feedBack.myFeedBack($('#myFeedBack'));
		},
		goPage: function(type,accessToken){
			var url= ''
			var params= {};
			if(type == 'user'){
				url= interUrl+"/page/user/userCenter?access_token="+accessToken;
				params= {
					uid: uid
				}
			}else if(type == 'userId'){
				url= interUrl+'/page/user/userDetail?userId='+typeId;
				$("#attentions").attr('href',url);
				params= {
					uid: uid
				}
			}else if(type == 'attention'){
				url= interUrl+'/page/attention/getAttention?userId='+typeId+'&access_token='+accessToken;
				params= {
						uid: uid
					}
			}else if(type == 'fans'){
				url= interUrl+'/page/attention/getFans?userId='+typeId+'&access_token='+accessToken;
				params= {
						uid: uid
					}
			}else if(type=='feedback'){
				url= interUrl+'/page/feedback/addFeedback?access_token='+accessToken;
				params= {
						uid: uid
					}
			}
			info.postform(url,params)
		},
		feedSelect:function(obj){
			obj.on('click',function(){
				var _thisClass = $(this).find('span').hasClass('nor');
				if(_thisClass){
					$(this).find('span').removeClass('nor').addClass('select');
					var _val = $(this).text();
					$(this).find('input').val(_val);
				}else{
					$(this).find('span').removeClass('select').addClass('nor');
					$(this).find('input').val('');
				}
			})
		},
		myFeedBack:function(obj){
			obj.on('focus',function(){
			})
		},
		addFeedback:function(){
			$('#subMit a').on('click',function(content,contact,dataFrom){
				arr=[];
				ctn=[];
				$('.val').each(function(){
					arr.push($(this).val());
				});
				var add = arr.toString().split(',');
				for(var i=0;i<add.length;i++){
					if(add[i]==''){
						delete add[i];
					}else{
						ctn.push(add[i]);
					}
				}
				var content = ctn.toString()+$('#myFeedBack').val();
				var contact = $('#connectWay input').val();
				var dataFrom = 'h5';
				var accessToken = info.getCookie('access_token');
				var uid = info.getCookie('uid');
				if(content == ''){
					alert.alertMsg('请选择反馈信息');return;
				}else{
					if(contact==''){
						alert.alertMsg('请填写联系方式');
						return;
					}else{
						service.getFeedback(content,contact,dataFrom).done(function(data){
							alert.alertMsg('反馈成功');
							setInterval(function(){
								location.href = interfaceurl + '/restwww/page/user/userCenter?access_token='+accessToken+'&uid='+uid;
								//feedBack.goPage('user',access_token);
							},1000)
						});
					}
				}
			})
		}
	}
	feedBack.init();
});