<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2016/8/2
  Time: 10:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>榜单管理</title>
    <script type='text/javascript' src='js/admin/query_voice_list.js'></script>
</head>
<body>
<!-- 列表 -->
<table id="displayListTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
            榜单管理
        </h2>
        <h2 style="float:left;padding-left:10px;margin: 1px">
        	<a href="varietyHistory/listVarietyHistory" style="text-decoration: none;color: #000;">网综往期</a>
        </h2>
    </div>
    <div>
        <a href="javascript:;" onclick="insertVoiceListDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">设置榜单</a>
    </div>
</div>
<!-- 添加榜单弹出框 start -->
<div id="insert_voice_list_dlg" class="easyui-dialog" style="width:500px;height:310px;" closed="true"
     buttons="#insert-voice-list-dlg-buttons">
    <form id="insertVoiceListForm" method="post">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">榜单类型：</td>
                <td style="text-align: left;">
                    <select name="type" class="easyui-combobox">
                        <option value="1">学员榜</option>
                        <option value="2">网综榜</option>
                        <option value="3">主播榜</option>
                        <option value="4">贡献榜</option>
                        <option value="5">贡献榜总榜</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" required="true" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">开始时间：</td>
                <td style="text-align: left;">
                    <input type="text" name="startTime" id="startTime" required="true" style="width: 200px;" class="easyui-datetimebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">结束时间：</td>
                <td style="text-align: left;">
                    <input type="text" name="endTime" id="endTime" required="true" style="width: 200px;" class="easyui-datetimebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">榜单人数：</td>
                <td style="text-align: left;">
                    <input type="text" name="personNumber" required="true" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">url地址：</td>
                <td style="text-align: left;">
                    <input type="text" name="url" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-voice-list-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertVoiceList()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_voice_list_dlg').dialog('close')">取消</a>
</div>
<!-- 添加榜单弹出框 end -->
<c:import url="/main/common.jsp"/>
</body>
</html>
