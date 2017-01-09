<%--
  Created by IntelliJ IDEA.
  User: huoshanwei
  Date: 2015/10/19
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加/修改弹出框</title>
</head>
<body>
<!-- 弹出的添加对话框 start-->
<div id="dlg" class="easyui-dialog" style="width:400px;height:400px;padding:10px 20px" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post" action="music/add" novalidate enctype="multipart/form-data">
        <table class="doc-table">
            <tr>
                <td><label>上传音乐:</label></td>
                <td><input type="file" id="file2" name="files" accept="mp3,wav" required="true"/></td>
            </tr>
            <tr>
                <td><label>上传封面:</label></td>
                <td><input type="file" id="file1" name="faceFile" accept="jpg,png,gif" required="true"/></td>
            </tr>
            <tr>
                <td><label>名称:</label></td>
                <td><input name="name" id="musicName" class="easyui-validatebox" required="true"></td>
            </tr>
            <tr>
                <td><label>描述:</label></td>
                <td>
                    <input name="description" id="desc" class="easyui-validatebox">
                </td>
            </tr>
            <tr>
                <td><label>类型:</label></td>
                <td>
                    <input class="easyui-combobox" required="true" id="musicType" name="typeId" style="width:50%"
                           data-options="url: 'comboBox/getMusicTypeComboBox',method: 'get',valueField:'id',textField:'text'">
                </td>
            </tr>
            <tr>
                <td><label>状态:</label></td>
                <td>
                    <select id="musicStatus" class="easyui-combobox" name="status" required="true">
                        <option value="" selected="selected">请选择</option>
                        <option value="1" selected="true">有效</option>
                        <option value="0">无效</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2"><span id="warnning" style="color:red"></span></td>
            </tr>
            <input type="hidden" name="id" id="musicId"/>
        </table>
    </form>
</div>
<!-- 对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#dlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>
<!-- 弹出的添加对话框 end-->
</body>
</html>
