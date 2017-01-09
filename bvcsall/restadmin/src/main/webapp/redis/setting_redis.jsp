<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>默认redis设置</title>
	<script>
		function updateRedis(type,value) {
			$.ajax({
				url: 'redis/updateRedis',
				type: 'post',
				data: {type: type,value:value},
				async: false, //默认为true 异步
				error: function () {
					showMessage("提示信息", "更新失败，请重试！");
				},
				success: function (result) {
					if (result == "success") {
						showMessage("提示信息", "更新成功！");
					} else {
						showMessage("提示信息", "更新失败，请重试！");
					}
				}
			});
		}
	</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
			<h3 align="center">默认redis</h3>
			<table cellpadding="5" style="margin:0 auto;width:1200px;text-align: left;" class="form-body" >
				<tr>
					<td>
						CHECK_GROUP_ID:
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" id="checkGroupId" value="${checkGroupId}" >
					</td>
					<td>
						<input type="button" onclick="updateRedis('checkGroupId',$('#checkGroupId').val())" class="button green bigrounded" data-options="iconCls:'icon-ok'" value="保存" />
					</td>
				</tr>
				<tr>
					<td>
						CHECK_ADMIN_GROUP_ID:
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" id="checkAdminGroupId" value="${checkAdminGroupId}" >
					</td>
					<td>
						<input type="button" onclick="updateRedis('checkAdminGroupId',$('#checkAdminGroupId').val())" class="button green bigrounded" data-options="iconCls:'icon-ok'" value="保存" />
					</td>
				</tr>
				<tr>
					<td>
						bStarName:
					</td>
					<td>
						<input class="easyui-validatebox" id="bStarName" value="${bStarName}" >
					</td>
					<td>
						<input type="button" onclick="updateRedis('bStarName',$('#bStarName').val())" class="button green bigrounded" data-options="iconCls:'icon-ok'" value="保存" />
					</td>
				</tr>
				<tr>
					<td>
						firstLiveTimes(小时):
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" id="firstLiveTimes" value="${firstLiveTimes}" >
					</td>
					<td>
						<input type="button" onclick="updateRedis('firstLiveTimes',$('#firstLiveTimes').val())" class="button green bigrounded" data-options="iconCls:'icon-ok'" value="保存" />
					</td>
				</tr>
			</table>
	</div>
</div>

</body>
</html>