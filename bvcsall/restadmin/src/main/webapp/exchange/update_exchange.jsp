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
    <title>更新信息</title>
</head>
<body>
<!-- 添加礼物弹出框 start -->
<div class="easyui-layout" id="update_dlg" data-options="fit:true">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" required="true" id="name" style="width: 200px;"
                           class="easyui-validatebox" value="${exchange.name}">
                    <input type="hidden" name="id" required="true" id="id" style="width: 200px;"
                           class="easyui-validatebox" value="${exchange.id}">
                    <input type="hidden" name="type" required="true" id="type" style="width: 200px;"
                           class="easyui-validatebox" value="${exchange.type}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">icon：</td>
                <td style="text-align: left;">
                    <input type="file" name="iconUrl" id="iconUrl" accept="jpg,png,gif"
                           class="easyui-validatebox">
                    <img src="/restadmin/download/${exchange.iconUrl}" style="width: 30px;height: 30px;">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">金豆：</td>
                <td style="text-align: left;">
                    <input type="text" name="pointCount" disabled="disabled" id="pointCount"
                           style="width: 200px;"
                           class="easyui-numberbox" min="1" precision="0" required="true"
                           missingMessage="请填写大于0的整数"
                           value="${exchange.pointCount}">
                </td>
            </tr>
                    <tr>
                        <td style="text-align: right;">金币数量：</td>
                        <td style="text-align: left;">
                            <input type="text" name="diamondCount" disabled="disabled" id="diamondCount"
                                   style="width: 200px;"
                                   class="easyui-numberbox" min="1" precision="0" required="true"
                                   missingMessage="请填写大于0的整数"
                                   value="${exchange.diamondCount}">
                        </td>
                    </tr>
            <tr>
                <td style="text-align: right;">状态：</td>
                <td style="text-align: left;">
                    <select name="state" id="state" style="width: 200px;" required="true"
                            class="easyui-validatebox easyui-combobox">
                        <option value="1" <c:if test="${exchange.state eq 1}">selected="selected"</c:if>>上架</option>
                        <option value="0" <c:if test="${exchange.state eq 0}">selected="selected"</c:if>>下架</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">权重：</td>
                <td style="text-align: left;">
                    <input type="text" name="weight" style="width: 200px;" id="weight" class="easyui-numberbox" min="1"
                           precision="2" required="true" missingMessage="请填写大于0的数字"
                           value="${exchange.weight}">
                </td>
            </tr>
        </table>
    </form>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0;">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doUpdate()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="javascript:$('#dialogWindow').dialog('close')">取消</a>
    </div>
</div>

<!-- 添加礼物弹出框 end -->
</body>
</html>
