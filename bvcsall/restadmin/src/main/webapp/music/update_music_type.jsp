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
    <title>更新音乐类型信息</title>
</head>
<body>
<!-- 添加礼物弹出框 start -->
<div class="easyui-layout" id="update_dlg" data-options="fit:true">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">音乐类型名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="typeName" id="typeName" style="width: 200px;" class="easyui-validatebox" value="${musicType.typeName}">
                    <input type="hidden" name="id" id="id" style="width: 200px;" class="easyui-validatebox" value="${musicType.id}">
                </td>
            </tr>
        </table>
    </form>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0;">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doUpdate()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dialogWindow').dialog('close')">取消</a>
    </div>
</div>

<!-- 添加礼物弹出框 end -->
</body>
</html>
