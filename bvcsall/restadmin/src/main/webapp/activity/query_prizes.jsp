<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>活动奖项</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_prize.js"></script>
</head>
<body>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">活动奖项</h2>
    </div>
    <div style="margin-top: 10px;">
        <span>
                <label>中奖活动名称：</label>
                <span>
                    <input type="text" class="textbox" style="width: 120px;" id="queryName" name="name">
                </span>
        </span>
        <span>
                <label>状态：</label>
                <span>
                    <select name="status" id="queryStatus" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="0">有效</option>
                        <option value="1">无效</option>
                    </select>
                </span>
        </span>
        <span>
                <label>开始时间：</label>
                <span>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="startDate"
                           id="queryStartDate">
                </span>
        </span>
        <span>
                <label>结束时间：</label>
                <span>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="endDate" id="queryEndDate">
                </span>
            </span>
        <span>
            <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
               iconCls="icon-search">搜索</a>
         </span>
    </div>
    <div>
        <a href="javascript:;" onclick="insertDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
    </div>
</div>
<input type="hidden" value="${activityId}" id="commonActivityId">

<!--添加弹出框 start-->
<div id="insert_dlg" class="easyui-dialog" style="width:400px;height:400px;" closed="true"
     buttons="#insert-dlg-buttons">
    <table class="table-doc" cellspacing="0" width="100%">
        <caption></caption>
        <tr>
            <td class="row">中奖活动名称：</td>
            <td class="row">
                <input type="text" name="name" id="name" class="easyui-validatebox" value="">
            </td>
        </tr>
        <tr>
            <td class="row">开始时间：</td>
            <td class="row">
                <input type="text" name="startDate" id="startDate" class="easyui-validatebox easyui-datebox" value="">
            </td>
        </tr>
        <tr>
            <td class="row">结束时间：</td>
            <td class="row">
                <input type="text" name="endDate" id="endDate" class="easyui-validatebox easyui-datebox" value="">
            </td>
        </tr>
        <tr>
            <td class="row">状态：</td>
            <td>
                <select name="status" id="status" class="easyui-validatebox easyui-combobox">
                    <option value="0">有效</option>
                    <option value="1">无效</option>
                </select>
            </td>
        </tr>
    </table>
</div>
<!-- 添加弹出框里的保存和取消按钮 -->
<div id="insert-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsert()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_dlg').dialog('close')">取消</a>
</div>

<!--编辑弹出框开始-->
<div id="update_dlg" class="easyui-dialog" style="width:400px;height:400px;" closed="true"
     buttons="#update-dlg-buttons">
    <table class="table-doc" cellspacing="0" width="100%">
        <caption></caption>
        <tr>
            <td class="row">中奖活动名称：</td>
            <td class="row">
                <input type="text" name="name" id="update_name" class="easyui-validatebox" value="">
                <input type="hidden" name="id" id="update_id" class="easyui-validatebox" value="">
            </td>
        </tr>
        <tr>
            <td class="row">开始时间：</td>
            <td class="row">
                <input type="text" name="startDate" id="update_startDate" class="easyui-validatebox easyui-datebox" value="">
            </td>
        </tr>
        <tr>
            <td class="row">结束时间：</td>
            <td class="row">
                <input type="text" name="endDate" id="update_endDate" class="easyui-validatebox easyui-datebox" value="">
            </td>
        </tr>
        <tr>
            <td class="row">状态：</td>
            <td>
                <select name="status" id="update_status" class="easyui-validatebox easyui-combobox">
                    <option value="0">有效</option>
                    <option value="1">无效</option>
                </select>
            </td>
        </tr>
    </table>
</div>
<!-- 编辑弹出框里的保存和取消按钮 -->
<div id="update-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doUpdate()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#update_dlg').dialog('close')">取消</a>
</div>
<!--编辑弹出框结束-->
</body>
</html>
