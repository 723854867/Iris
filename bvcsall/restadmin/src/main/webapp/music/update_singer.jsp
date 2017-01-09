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
    <title>更新歌手信息</title>
</head>
<body>
<!-- 添加礼物弹出框 start -->
<div class="easyui-layout" id="update_dlg" data-options="fit:true">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">歌手名称：</td>
                <td style="text-align: left;">
                    <input type="hidden" name="id" style="width: 200px;" value="${singer.id}">
                    <input type="text" name="name" required="true" style="width: 200px;" class="easyui-validatebox" value="${singer.name}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">歌手类型：</td>
                <td style="text-align: left;">
                    <select name="singerType" id="singerType" class="easyui-validatebox easyui-combobox"
                            style="width: 120px;" data-options="
                            valueField: 'id', textField: 'text',
                            url:'comboBox/getSingerTypeCombobox?selected=${singer.singerType}',
                            require:true"/>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">状态：</td>
                <td style="text-align: left;">
                    <select class="easyui-combobox" name="state">
                        <option value="1" <c:if test="${singer.state eq 1}">selected="selected"</c:if>>正常</option>
                        <option value="0" <c:if test="${singer.state eq 0}">selected="selected"</c:if>>删除</option>
                    </select>
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
