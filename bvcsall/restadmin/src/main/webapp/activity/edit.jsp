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
<title>视频上传</title>
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
        },
        change:function(e,data){
        	var fileName = data.files[0].name;
        	var fileext = fileName.substring(fileName.lastIndexOf("."));
			fileext = fileext.toLowerCase();
			if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
				showMessage( "Error","对不起，系统仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
				return false;
			}
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
		<form id="myform" method="post" action="activity/updatepage">
				<table cellpadding="5" style="margin:0 auto;width:900px;text-align: center;" class="form-body" >
				<input name="id" id="id"  type="hidden" style="width:50%" value="${entity['id'] }"></input>
				<tr>
					<td>输入标题:</td>
					<td><input class="easyui-validatebox f1 easyui-textbox" name="title" id="title" value="${entity['title'] }" style="width:50%" data-options="validType:'length[0,20]',novalidate:true"></input></td>
				</tr>
				<tr>
					<td>封面:</td>
					<td>
					<input name="hlsfiles" id="hlsfiles" type="file" style="width:50%"></input>
					<input name="cover" id="cover"  type="hidden" style="width:50%" value="${entity['cover'] }"></input>
					<div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p" class="easyui-progressbar" ></div>
					<img src="/restadmin/download${entity['cover'] }"/>
					</td>
				</tr>
				<tr>
					<td>类型:</td>
					<td>
					<select class="easyui-combobox easyui-validatebox" required="true" name="groupType" style="width:50%;">
							<option value="1" <c:if test="${groupType eq 1 }">selected="true"</c:if>>所有活动</option>
							<option value="0" <c:if test="${groupType eq 0 }">selected="true"</c:if>>首页推荐</option>
							<option value="2" <c:if test="${groupType eq 2 }">selected="true"</c:if>>发现页推荐</option>
					</select>
					<div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p" class="easyui-progressbar" ></div>
					</td>
				</tr>
				<tr>
					<td>排序权重:</td>
					<td>
					<input name="order_num" id="order_num" value="${entity['order_num'] }" class="easyui-validatebox f1 easyui-textbox"  style="width:50%"></input>
					<div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p" class="easyui-progressbar" ></div>
					</td>
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