<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2016/9/6
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>连麦白名单</title>
    <script>
        $(function () {
            $('#displayTable').datagrid({
                nowrap: true, //是否换行
                autoRowHeight: true, //自动行高
                fitColumns: true,
                fit: true,
                striped: true,
                collapsible: true, //是否可折叠
                remoteSort: false,
                singleSelect: true, //是否单选
                pagination: false, //分页控件
                rownumbers: true, //行号
                pagePosition: 'bottom',
                scrollbarSize: 0,
                loadMsg: "数据加载中.....",
                url: "connectMicrophone/queryCMWhiteList",
                columns: [
                    [
                        {field: 'userId', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
                        {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 120, align: 'center'},
                        {field: 'phone', title: '<span class="columnTitle">手机号码</span>', width: 120, align: 'center'},
                        {field: 'createTime', title: '<span class="columnTitle">添加时间</span>', width: 120, align: 'center'},
                        {
                            field: 'modify', title: '<span class="columnTitle">操作</span>', width: 200, align: 'center',
                            formatter: function (value, row) {
                                var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="removeCMWhiteList(' + row.userId + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                                return editStr;
                            }
                        }
                    ]
                ],
                toolbar: "#dataGridToolbar",
                onLoadSuccess: function () {
                    $('#displayTable').datagrid('clearSelections');
                }
            });
        });
        function insertCMWhiteList() {
            var userId = $("#userId").val();
            $.ajax({
                url: 'connectMicrophone/insertCMWhiteList',
                data: {'userId': userId},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.resultCode == "success") {
                        showMessage("提示信息", result.resultMessage);
                        doSearch()
                    } else {
                        showMessage("错误提示", result.resultMessage);
                    }
                }
            });
        }
        
        function removeCMWhiteList(userId) {
            $.ajax({
                url: 'connectMicrophone/removeCMWhiteList',
                data: {'userId': userId},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.resultCode == "success") {
                        showMessage("提示信息", result.resultMessage);
                        doSearch()
                    } else {
                        showMessage("错误提示", result.resultMessage);
                    }
                }
            });
        }
        
        function doSearch() {
            $('#displayTable').datagrid({url: "connectMicrophone/queryCMWhiteList"});
        }

        function insertCMWLCondDialog(){
            $("#insert_dlg").dialog('open').dialog('setTitle', "连麦条件设置");
            var diamondData = $("#diamondData").val();
            $("#diamond").numberbox("setValue",diamondData);
        }
        function doInsert(){
            var condition = $("#condition").combobox("getValue");
            var diamond = $("#diamond").val();
            if(diamond == "" || diamond == null || diamond == "undefined"){
                showMessage("错误提示", "金豆数不能为空，只能为大于0的数字！");
            }else{
                $.ajax({
                    url: 'connectMicrophone/insertCMWLCondition',
                    data: {'diamond': diamond},
                    type: "post",
                    dataType: "json",
                    success: function (result) {
                        if (result.resultCode == "success") {
                            showMessage("提示信息", result.resultMessage);
                            $('#insert_dlg').dialog('close');
                        } else {
                            showMessage("错误提示", result.resultMessage);
                        }
                    }
                });
            }
        }
    </script>
</head>
<body>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">连麦白名单</h2>
    </div>
    <div>
        <input type="text" id="userId" name="userId" value="" style="width: 150px;">
        <a href="javascript:;" onclick="insertCMWhiteList()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
    </div>
    <div>
        <input type="hidden" id="diamondData" name="diamondData" value="${diamond}" style="width: 150px;">
        <a href="javascript:;" onclick="insertCMWLCondDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">连麦条件设置</a>
    </div>
</div>

<!--连麦设置dialog start-->
<div id="insert_dlg" class="easyui-dialog" style="width:500px;height:210px;" closed="true"
     buttons="#insert-dlg-buttons">
    <form id="insertForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">条件：</td>
                <td style="text-align: left;">
                    <select name="condition" id="condition" required="true" style="width: 200px;"
                            class="easyui-validatebox easyui-combobox">
                        <option value="1">金豆</option>
                        <%--<option value="2">人气榜</option>--%>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">金豆数：</td>
                <td style="text-align: left;">
                    <input type="text" name="diamond" id="diamond" required="true" style="width: 200px;" class="easyui-validatebox easyui-numberbox" value="${diamond}">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsert()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_dlg').dialog('close')">取消</a>
</div>
<!--连麦设置dialog end-->
<c:import url="/main/common.jsp"/>
</body>
</html>
