<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<title>支付渠道设置</title>
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
		<h3 align="center">支付渠道设置</h3>
		<form method="post" enctype="multipart/form-data" action="payChannel/index">
			<table cellpadding="5" style="margin:0 auto;width:1200px;text-align: left;" class="form-body" >
				<input type="hidden" name='isUpdate' id="isUpdate" value="1">
				<tr>
					<td style="width: 35%;"><b>android 支付渠道：</b></td>
					<td style="width: 65%;">
						<input type="checkbox" name="androidChannel" value="wx" <c:if test="${fn:contains(androidChannelString, 'wx,')}">checked="checked"</c:if>/>微信手机支付&nbsp;&nbsp;
						<input type="checkbox" name="androidChannel" value="alipay" <c:if test="${fn:contains(androidChannelString, 'alipay,')}">checked="checked"</c:if>/>支付宝手机支付
				</tr>
				
				<tr>
					<td style="width: 35%;"><b>ios 支付渠道：</b></td>
					<td style="width: 65%;">
						<input type="checkbox" name="iosChannel" value="wx" <c:if test="${fn:contains(iosChannelString, 'wx,')}">checked="checked"</c:if>/>微信手机支付&nbsp;&nbsp;
						<input type="checkbox" name="iosChannel" value="alipay" <c:if test="${fn:contains(iosChannelString, 'alipay,')}">checked="checked"</c:if>/>支付宝手机支付&nbsp;&nbsp;
						<input type="checkbox" name="iosChannel" value="apple_pay" <c:if test="${fn:contains(iosChannelString, 'apple_pay,')}">checked="checked"</c:if>/>APPLE_PAY
					</td>
				</tr>
				
				<tr>
					<td colspan="3" align="center">
						<input type="submit" style="width: 50px;height:25px" class="button green bigrounded" data-options="iconCls:'icon-ok'"value="保存"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>

</body>
</html>