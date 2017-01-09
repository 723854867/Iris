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
    <title>更新机构主播信息</title>
</head>
<body>
<div class="easyui-layout" id="update_dlg" data-options="fit:true">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">主播ID：</td>
                <td style="text-align: left;">
                    ${orgAnchor.userId}
                    <input type="hidden" id="id" name="id" value="${orgAnchor.id}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">真实姓名：</td>
                <td style="text-align: left;">
                    <input type="text" name="realName" id="realName" style="width: 200px;" class="easyui-validatebox"
                           value="${orgAnchor.realName}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">手机号码：</td>
                <td style="text-align: left;">
                    <input type="text" name="phone" id="phone" style="width: 200px;" class="easyui-validatebox"
                           value="${orgAnchor.phone}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">开户行：</td>
                <td style="text-align: left;">
                    <input type="text" name="bankName" id="bankName" style="width: 200px;" class="easyui-validatebox"
                           value="${orgAnchor.bankName}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">支行：</td>
                <td style="text-align: left;">
                    <input type="text" name="branchBankName" id="branchBankName" style="width: 200px;" class="easyui-validatebox"
                           value="${orgAnchor.branchBankName}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">卡号：</td>
                <td style="text-align: left;">
                    <input type="text" name="bankNumber" id="bankNumber" style="width: 200px;" class="easyui-validatebox"
                           value="${orgAnchor.bankNumber}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">证件类型：</td>
                <td style="text-align: left;">
                    <select name="certificateType" id="certificateType" style="width: 200px;"
                            class="easyui-validatebox easyui-combobox">
                        <option value="1" <c:if test="${orgAnchor.certificateType eq 1}">selected="selected"</c:if>>身份证</option>
                        <option value="2" <c:if test="${orgAnchor.certificateType eq 2}">selected="selected"</c:if>>护照</option>
                        <option value="3" <c:if test="${orgAnchor.certificateType eq 3}">selected="selected"</c:if>>台胞证</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">证件号码：</td>
                <td style="text-align: left;">
                    <input type="text" name="certificateNumber" id="certificateNumber" style="width: 200px;" class="easyui-validatebox"
                           value="${orgAnchor.certificateNumber}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">底薪（元）：</td>
                <td style="text-align: left;">
                    <input type="text" name="basicSalary" id="basicSalary" style="width: 200px;" class="easyui-numberbox" min="0.01" precision="2" required="true" missingMessage="必须填写大于0的数字"
                           value="${orgAnchor.basicSalary}">
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
