<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2015/12/23
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>更新赠送信息</title>
</head>
<body>
<!-- 添加礼物弹出框 start -->
<div class="easyui-layout" id="update_dlg" data-options="fit:true">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">赠送数量：</td>
                <input type="hidden" name="type" id="type" value="${resultMap.type}">
                <td style="text-align: left;">
                    <input type="text" name="diamond" id="diamond" style="width: 200px;" class="easyui-numberbox"
                           min="0" precision="0" value="${resultMap.diamond}" missingMessage="必须填写大于或者等于0的整数"
                           value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">赠送次数：</td>
                <td style="text-align: left;">
                    <input type="text" name="count" id="count"  style="width: 200px;" class="easyui-numberbox" min="0" precision="0" value="${resultMap.count}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">提示信息：</td>
                <td style="text-align: left;">
                    <input type="text" name="tips" id="tips" style="width: 200px;"
                           class="easyui-validatebox" required="true"
                           value="${resultMap.tips}">
                </td>
            </tr>
        </table>
    </form>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0;">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doUpdate()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dialogWindow').dialog('close')">取消</a>
    </div>
</div>

</body>
</html>
