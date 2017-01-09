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
<title>默认关注用户设置</title>
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
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" enctype="multipart/form-data" action="setting/defaultAttentionSetting">
			<h3 align="center">默认关注用户设置</h3>
			<table cellpadding="5" style="margin:0 auto;width:1200px;text-align: left;" class="form-body" >
				<tr>
					<td>默认关注用户id：
						<input class="easyui-validatebox easyui-numberbox" name="uid" data-options="validType:'Long'" required="true" value="${uid}" >
					</td>
				</tr>
				<tr>
					<td colspan="3" align="center">
						<input type="submit" style="width: 50px;height:25px" class="button green bigrounded" data-options="iconCls:'icon-ok'" value="保存"/>
						
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>

</body>
</html>