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
    <title>更新拍币信息</title>
</head>
<body>
<!-- 添加礼物弹出框 start -->
<div class="easyui-layout" id="update_dlg" data-options="fit:true">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">拍币名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" required="true" id="name" style="width: 200px;" class="easyui-validatebox" value="${diamond.name}">
                    <input type="hidden" name="id" required="true" id="id" style="width: 200px;" class="easyui-validatebox" value="${diamond.id}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">拍币icon：</td>
                <td style="text-align: left;">
                    <input type="file" name="diamondIconUrl" id="diamondIconUrl" accept="jpg,png,gif"
                           class="easyui-validatebox">
                    <img src="/restadmin/download/${diamond.diamondIconUrl}" style="width: 30px;height: 30px;">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">价格（人民币）：</td>
                <td style="text-align: left;">
                    <input type="text" name="price" id="price" style="width: 200px;" class="easyui-numberbox" min="0.01" precision="2" required="true" missingMessage="必须填写大于0的数字"
                           value="${diamond.price}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">支付方式：</td>
                <td style="text-align: left;">
                    <select name="payMethod" id="payMethod" style="width: 200px;"
                            class="easyui-validatebox easyui-combobox" required="true">
                        <option value="0" <c:if test="${diamond.payMethod eq 0}">selected="selected"</c:if>>苹果</option>
                        <option value="1" <c:if test="${diamond.payMethod eq 1}">selected="selected"</c:if>>其它</option>
                        <option value="2" <c:if test="${diamond.payMethod eq 2}">selected="selected"</c:if>>大额支付</option>
                        <option value="3" <c:if test="${diamond.payMethod eq 3}">selected="selected"</c:if>>应用宝支付</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">拍币数量：</td>
                <td style="text-align: left;">
                    <input type="text" name="diamondCount" id="diamondCount" style="width: 200px;"
                           class="easyui-numberbox" min="1" precision="0" required="true" missingMessage="请填写大于0的整数"
                           value="${diamond.diamondCount}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">是否赠送：</td>
                <td style="text-align: left;">
                    <input type="radio" name="isGive" class="easyui-validatebox" onclick="giveCountDisplay(0)" value="0" <c:if test="${diamond.isGive eq 0}">checked="checked"</c:if>>不赠送
                    <input type="radio" name="isGive" class="easyui-validatebox" onclick="giveCountDisplay(1)" value="1" <c:if test="${diamond.isGive eq 1}">checked="checked"</c:if>>赠送
                </td>
            </tr>
            <tr id="giveCountTr" <c:if test="${diamond.isGive eq 0}">style="display: none;"</c:if>>
                <td style="text-align: right;">赠送数量：</td>
                <td style="text-align: left;">
                    <input type="text" name="giveCount" id="giveCount" class="easyui-numberbox" min="0" max="1000000" precision="0" missingMessage="请填写大于0的整数" value="${diamond.giveCount}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">状态：</td>
                <td style="text-align: left;">
                    <select name="state" id="state" style="width: 200px;" required="true" class="easyui-validatebox easyui-combobox">
                        <option value="1" <c:if test="${diamond.state eq 1}">selected="selected"</c:if>>上架</option>
                        <option value="0" <c:if test="${diamond.state eq 0}">selected="selected"</c:if>>下架</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">权重：</td>
                <td style="text-align: left;">
                    <input type="text" name="weight" style="width: 200px;" id="weight" class="easyui-numberbox" min="1" precision="2" required="true" missingMessage="请填写大于0的数字"
                           value="${diamond.weight}">
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
