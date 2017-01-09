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
    <title>更新机构信息</title>
</head>
<body>
<!-- 添加礼物弹出框 start -->
<div class="easyui-layout" id="update_dlg" data-options="fit:true">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
        	<tr>
                <td style="text-align: right;">机构类型：</td>
                <td style="text-align: left;">
                    <select id="orgType" name="orgType" class="easyui-combobox">
                        <option value="1" <c:if test="${organization.orgType eq '1' }">selected= 'true' </c:if> >返点</option>
                        <option value="2" <c:if test="${organization.orgType eq '2' }">selected= 'true' </c:if> >底薪</option>
                    </select>
                    
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">机构名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="orgName" required="true" id="orgName" style="width: 200px;" class="easyui-validatebox" value="${organization.orgName}">
                    <input type="hidden" name="id" required="true" id="id" style="width: 200px;" class="easyui-validatebox" value="${organization.id}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">负责人：</td>
                <td style="text-align: left;">
                    <input type="text" name="leader" id="leader" style="width: 200px;" class="easyui-validatebox" required="true" value="${organization.leader}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">负责人电话：</td>
                <td style="text-align: left;">
                    <input type="text" name="leaderPhone" id="leaderPhone" style="width: 200px;"
                           class="easyui-validatebox" required="true" value="${organization.leaderPhone}">
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
