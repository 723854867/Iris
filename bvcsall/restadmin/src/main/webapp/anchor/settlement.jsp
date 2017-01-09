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
    <title>结算</title>
</head>
<body>
<!-- 添加礼物弹出框 start -->
<div class="easyui-layout" id="update_dlg" data-options="fit:true">
    <form id="settlementForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">用户ID：</td>
                <td style="text-align: left;">
                    ${anchor.creatorId}
                    <input type="hidden" value="${anchor.id}" name="id" id="id">
                        <input type="hidden" value="${anchor.pointCount}" name="pointCount" id="pointCount">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">可用点数：</td>
                <td style="text-align: left;">
                    ${anchor.pointCount}
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">结算点数：</td>
                <td style="text-align: left;">
                    <input type="text" name="settlementPointCount" id="settlementPointCount" style="width: 200px;"
                           class="easyui-numberbox" min="0" precision="0" required="true" missingMessage="必须填写大于或者等于0的整数"
                           value="">
                </td>
            </tr>
        </table>
    </form>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0;">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doSettlement()">结算</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dialogWindow').dialog('close')">取消</a>
    </div>
</div>

<!-- 添加礼物弹出框 end -->
</body>
</html>
