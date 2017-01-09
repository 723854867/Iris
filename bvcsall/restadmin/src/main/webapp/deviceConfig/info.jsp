<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
 -->
<link rel="stylesheet" href="files/Jcrop/css/jquery.Jcrop.css" type="text/css" />
<script src="js/fp/vendor/jquery.ui.widget.js"></script>
<script src="js/fp/jquery.iframe-transport.js"></script>
<script src="js/fp/jquery.fileupload.js"></script>
<script src="files/Jcrop/js/jquery.Jcrop.js"></script>
<title>设备信息配置</title>
<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}
</style>
<script type="text/javascript">
$(function () {
	$('#configFile').fileupload({
    	url: 'deviceConfig/uploadConfig',
        sequentialUploads: true,
        dataType: 'json',
        type:'post',
        crossDomain:true,
        done: function (e, data) {
        	uploadresult=data.result;
        	$("#url").val(uploadresult["result"]);//配置文件路径
        },
        progress:function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#q').progressbar('setValue', progress);
        },
        start:function (e) {
        	$('#q').show();
        	$('#q').progressbar('setValue', 0);
        },
        change:function(e,data){
        	var fileName = data.files[0].name;
        	var fileext = fileName.substring(fileName.lastIndexOf("."));
			fileext = fileext.toLowerCase();
			if ((fileext != '.xml')) {
				showMessage( "Error","对不起，请上传xml格式文件");
				return false;
			}
        }
    });
})

function save(){
	var formjson=getFormJson('#myform');
	$.ajax({
	      url: 'deviceConfig/save',
	      data: formjson,
	      type: "post",
	      dataType: "json",
	      beforeSend: function(){
	       return $( '#myform' ).form('enableValidation').form( 'validate' );
	      },
	      success: function (result){
	    	  if (result["result"] == "1"){
	    		  showMessage( "提示","保存成功");
	    	  }
	      }
	  });
	
}

</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" enctype="multipart/form-data">
			<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="form-body" >
				<tr>
					<td>配置信息版本:</td>
					<td>
					<input class="easyui-validatebox easyui-text" name="version" id="version" data-options="multiline:true,validType:'length[0,100]'" required="true" value="${version} " style="width:50%;">
				</tr>
				<tr>
					<td>配置信息URL:</td>
					<td>
					<input class="easyui-validatebox easyui-text" name="url" id="url"  value="${url}" style="width:50%;" readonly="readonly">
				</tr>
				<tr>
					<td style="width: 20%;">选择文件:</td>
					<td style="width: 80%;">
						<input name="configFile" id="configFile" type="file" style="width:50%"></input>
						<div style="margin-left:0px;margin-right:auto;width:90%;display:none" id="q" class="easyui-progressbar" ></div>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="save()">保存</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>