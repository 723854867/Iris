<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
 -->
<script src="js/fp/vendor/jquery.ui.widget.js"></script>
<script src="js/fp/jquery.iframe-transport.js"></script>
<script src="js/fp/jquery.fileupload.js"></script>
<title>生成密码</title>
<script type="text/javascript">
var playId="";
$(function () {
	//启用表单验证
	$('.validatebox-text').bind('blur', function(){
		$(this).validatebox('enableValidation').validatebox('validate');
	});

    $('#hlsfiles').fileupload({
    	url: 'activity/activityupload',
        sequentialUploads: true,
        dataType: 'json',
        type:'post',
        crossDomain:true,
        done: function (e, data) {
        	uploadresult=data.result;
        	
        	$("#cover").val(uploadresult["result"]);
        	showMessage("通知","恭喜您上传成功");
        },
        progress:function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#p').progressbar('setValue', progress);
        },
        start:function (e) {
        	$('#p').show();
        	$('#p').progressbar('setValue', 0);
        }
    });
    
});


function clickupload(){
	var id;
	$("#playId").val(playId);
	var formjson=getFormJson( '#myform');
	if($.trim(playId)=="error"){
		showMessage("Error","文件上传失败，请重新选择文件上传");
		return;
	}
	if($.trim(playId)==""){
		showMessage("提示","请等待文件上传完成后，再点击确认");
		return;
	}
	$.ajax({
	      url: 'video/uploadsave',
	      data: formjson,
	      type: "post",
	      dataType: "json",
	      beforeSend: function(){
	       return $( '#myform' ).form('enableValidation').form( 'validate' );
	      },
	      success: function (result){
	        location.href="video/checledVideos";
	      }
	  });
}

function start(){
	var value = $('#p').progressbar('getValue');
	if (value < 100){
		value += Math.floor(Math.random() * 10);
		$('#p').progressbar('setValue', value);
		setTimeout(arguments.callee, 200);
	}
};
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" action="videoUploader/savePassword">
				<table cellpadding="5" style="margin:0 auto;width:900px;text-align: center;" class="form-body" >
				<input name="id" id="id"  type="hidden" style="width:50%" value="${id }"></input>
				<tr>
					<td>输入新密码:</td>
					<td><input class="easyui-validatebox f1 easyui-textbox" name="password" id="password" style="width:50%" data-options="validType:'length[0,20]',novalidate:true"></input></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
					<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="$('#myform').submit();">保存</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>