<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>背景音乐</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type='text/javascript' src='js/players/bootstrap.min.js'></script>
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
    <script type='text/javascript' src='js/admin/query_music.js'></script>
</head>
<body>
<table style="width: 100%;" id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">背景音乐</h2>
    </div>
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <label>音乐名称：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:150px" name="name" id="name" value=""/>
                </td>
                <td>
                    <label>状态：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="musicState" id="musicState">
                        <option value="">请选择</option>
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                    </select>
                </td>
                <td>
                    <label>音乐类型</label>
                </td>
                <td>
                    <input class="easyui-combobox" id="musicTypeId" name="typeId"
                           data-options="url: 'comboBox/getMusicTypeComboBox',method: 'get',valueField:'id',textField:'text'">
                </td>
                <td>
                    <label>上传人：</label>
                </td>
                <td>
                    <input class="easyui-combobox" id="createPerson" name="createPerson"
                           data-options="url: 'comboBox/getUserComboBox',method: 'get',valueField:'id',textField:'text'">
                </td>
                <td>
                    <label>上传时间：</label>
                </td>
                <td>
                    <input name="startDate" class="easyui-datebox" id="startDate" type="text"/>
                    至
                    <input name="endDate" id="endDate" class="easyui-datebox" type="text"/>
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
    <div style="height: 35px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="insertMusicDialog()"
           plain="true">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="deleteMusics()"
           plain="true">批量删除</a>
    </div>
</div>
<!-- 弹出的添加对话框 -->
<div id="insertDialog" class="easyui-dialog" style="width:400px;height:390px;padding:2px 5px" closed="true"
     buttons="#dlg-buttons">
    <form id="insertForm" method="post" action="music/add" novalidate enctype="multipart/form-data">
        <table class="table-doc" width="100%">
            <tr>
                <td><label>上传音乐:</label></td>
                <td style="text-align: left;"><input type="file" id="file2" name="files" accept="mp3,wav" /></td>
            </tr>
            <tr>
                <td><label>上传封面:</label></td>
                <td style="text-align: left;"><input type="file" id="file1" name="faceFile" accept="jpg,png,gif"/></td>
            </tr>
            <tr>
                <td><label>名称:</label></td>
                <td style="text-align: left;"><input name="name" id="musicName" class="easyui-validatebox"></td>
            </tr>
            <tr>
                <td><label>描述:</label></td>
                <td style="text-align: left;">
                    <input name="description" id="desc" class="easyui-validatebox">
                </td>
            </tr>
            <tr>
                <td><label>类型:</label></td>
                <td style="text-align: left;">
                    <input class="easyui-combobox" id="musicType" name="typeId" style="width:50%"
                           data-options="url: 'comboBox/getMusicTypeComboBox',method: 'get',valueField:'id',textField:'text'">
                    <input type="hidden" name="id" value="">
                </td>
            </tr>
            <tr>
                <td><label>状态:</label></td>
                <td style="text-align: left;">
                    <select id="musicStatus" class="easyui-combobox" name="status">
                        <option value="" selected="selected">请选择</option>
                        <option value="1" selected="true">有效</option>
                        <option value="0">无效</option>
                    </select>
                </td>
            </tr>
             <tr>
                <td><label>权重:</label></td>
                <td style="text-align: left;"><input name="orderNumber" id="musicOrderNumber" class="easyui-validatebox" value="0"></td>
            </tr>
        </table>
    </form>
</div>
<!-- 对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" onclick="saveMusic('#insertDialog','#insertForm')" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:void(0)" id="insert_dialog_close_button" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
</div>

<!--修改 start-->
<div id="updateDialog" class="easyui-dialog" style="width:500px;height:390px;padding:2px 5px" closed="true"
     buttons="#updlg-buttons">
    <form id="updateForm" method="post" action="music/add" novalidate enctype="multipart/form-data">
        <table class="table-doc" width="100%">
            <tr>
                <td><label>上传音乐:</label></td>
                <td style="text-align: left;"><input type="file" name="files" id="update_music_file" accept="mp3,wav"/></td>
            </tr>
            <tr>
                <td><label>上传封面:</label></td>
                <td style="text-align: left;">
                    <input type="file" name="faceFile" id="update_face_url" accept="jpg,png,gif"/>
                    <img src="" id="faceUrlImg" style="height: 70px;" alt="封面图片">
                </td>
            </tr>
            <tr>
                <td><label>名称:</label></td>
                <td style="text-align: left;"><input name="name" id="update_name" class="easyui-validatebox" required="true"></td>
            </tr>
            <tr>
                <td><label>描述:</label></td>
                <td style="text-align: left;">
                    <input name="description" id="update_description" class="easyui-validatebox">
                </td>
            </tr>
            <tr>
                <td><label>类型:</label></td>
                <td style="text-align: left;">
                    <input class="easyui-combobox" required="true" name="typeId" id="update_type_id" style="width:50%"
                           data-options="url: 'comboBox/getMusicTypeComboBox',method: 'get',valueField:'id',textField:'text'">
                </td>
            </tr>
            <tr>
                <td><label>状态:</label></td>
                <td style="text-align: left;">
                    <select class="easyui-combobox" name="status" id="update_status" required="true">
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                    </select>
                    <input type="hidden" name="id" id="update_id">
                </td>
            </tr>
             <tr>
                <td><label>权重:</label></td>
                <td style="text-align: left;"><input name="orderNumber" id="update_orderNumber" class="easyui-validatebox" required="true"></td>
            </tr>
        </table>
    </form>
</div>
<!--修改 end-->
<div id="updlg-buttons">
    <a href="javascript:void(0)" onclick="saveMusic('#updateDialog','#updateForm')" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:void(0)" id="dialog_close_button" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
</div>
</body>
</html>
