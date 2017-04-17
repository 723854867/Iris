exit=function(){
	$.ajax({
	    type: 'POST',
	    url: path+'/backstage',
	    data: {'action':'login_out'},
	    dataType: 'json',
	    success: function (json) {
	    	if(json["code"]==0)
	    		window.location.href='login.html';
	    },
	    error: function () {
	        alert("数据加载失败");
	    }
	});
}
showPasswordModal=function(){
	$("body").append("<div class='modal fade ' id='passwordModal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' data-backdrop='static'>" +
	    	"<div class='modal-dialog modal-sm' role='document'>" +
	    		"<div class='modal-content'>" +
	    			"<div class='modal-header'><button type='button' class='close' onclick='removePasswordModal()'><span>&times;</span></button><h4 class='modal-title'>修改密码</h4></div>" +
	    			"<div id='loadingText1' class='modal-body'>" +
	    				"<div class='form-inline form-group'>"+
			    			"<label class='control-label'>旧密码：</label>"+
			    			"<input type='password' id='oldPwd' name='oldPwd' class='form-control' placeholder='输入密码' required='required'>"+
			    		"</div>"+
			    		"<div class='form-inline form-group'>"+
			    			"<label class='control-label'>新密码：</label>"+
			    			"<input type='password' id='newPwd' name='newPwd' class='form-control' placeholder='输入密码' required='required'>"+
			    		"</div>"+
	    			"</div>" +
	    			"<div class='modal-footer'><a  class='btn btn-info' href='javascript:void(0)' onclick='passwordEdit()'>保存</a></div>"+
	    		"</div>" +
	    	"</div>" +
	    "</div>"
	);
    $("#passwordModal").modal();
}
passwordEdit=function(){
	
}
removePasswordModal=function(){
	$("#passwordModal").remove();
	$(".modal-backdrop").remove();
	$("body").removeClass("modal-open");
}