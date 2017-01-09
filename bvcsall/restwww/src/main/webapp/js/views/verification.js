define(['../common/aler'],function(alert){
	// 手机号验证

	var checkInput = {
		init : function(){

		},
		checkMobile : function(str,codeId){
			var re = /^1\d{10}$/;
		    if (re.test(str)) {
		        $(codeId).removeClass('btnCode1').addClass('btnCode').removeAttr('disabled');
		    }
		    else {
		        alert.alertMsg("手机号不正确！");
		        $(codeId).removeClass('btnCode').addClass('btnCode1').attr('disabled',true);
		    }
		},
		checkPassword : function(pwd){
			var re = /.{6,18}/;
		    if (re.test(pwd)) {

		    }else if(pwd==""){
		        alert.alertMsg("密码不能为空");
		    }
		    else {
		        alert.alertMsg("请输入6-18位密码");
		    }
		},
		time : function(o,codeId){
			var wait=10;
			if (wait == 0) {
				o.removeAttribute("disabled");
				o.value="重新获取";
				$(codeId).removeClass('btnCode1').addClass('btnCode');
				wait = 10;
			} else {
				o.setAttribute("disabled", true);
				o.value=wait +'s';
				wait--;
				setTimeout(function() {
					callback;
				},
				1000)
			}
		}
	};

	return checkInput;
});
