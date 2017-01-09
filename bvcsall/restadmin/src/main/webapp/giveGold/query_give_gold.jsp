<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2015/12/23
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>赠送金币设置</title>
    <style>
        .bc{
            background-color: #666666;
            color: #FFFFFF!important;
            padding: 4px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $('#displayTable').datagrid({
                nowrap: true, //是否换行
                autoRowHeight: true, //自动行高
                fitColumns: true,
                fit: true,
                striped: true,
                pageNumber: 1,
                collapsible: true, //是否可折叠
                remoteSort: false,
                singleSelect: true, //是否单选
                pagination: true, //分页控件
                rownumbers: true, //行号
                pagePosition: 'bottom',
                scrollbarSize: 0,
                loadMsg: "数据加载中.....",
                url: "giveGold/queryGiveGoldList",
                columns: [
                    [
                        {field: 'type', title: '<span class="columnTitle">类型</span>', width: 40, align: 'center',
                            formatter: function (value, row) {
                                if(value == "1"){
                                    return "分享成功";
                                }else if(value == "2"){
                                    return "首次直播";
                                }else{
                                    return "分享按钮";
                                }
                            }
                        },
                        {field: 'diamond', title: '<span class="columnTitle">赠送金币数量</span>', width: 40, align: 'center'},
                        {field: 'count', title: '<span class="columnTitle">赠送次数</span>', width: 120, align: 'center'},
                        {field: 'tips', title: '<span class="columnTitle">提示信息</span>', width: 120, align: 'center'},
                        {
                            field: 'modify', title: '<span class="columnTitle">操作</span>', width: 200, align: 'center',
                            formatter: function (value, row) {
                                var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="dialogUpdate(' + row.type + ')" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                                return editStr;
                            }
                        }
                    ]
                ],
                toolbar: "#dataGridToolbar",
                onLoadSuccess: function () {
                    $('#displayTable').datagrid('clearSelections');
                },
                pageSize: 20,
                pageList: [20, 40, 60, 80, 100],
                beforePageText: '第', //页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
        });
        function doSearch() {
            $('#displayTable').datagrid({url: "giveGold/queryGiveGoldList"});
        }
        function insertDialog() {
            $("#insert_dlg").dialog('open').dialog('setTitle', "添加");
        }
        function doInsert() {
            $('#insertForm').form('submit', {
                url: "giveGold/insertGiveGold",
                success: function (response) {
                    var parsedJson = jQuery.parseJSON(response);
                    if (parsedJson.resultCode == "success") {
                        showMessage("提示信息", parsedJson.resultMessage);
                        $('#insert_dlg').dialog('close');
                        doSearch();
                    } else {
                        showMessage("错误信息", parsedJson.resultMessage);
                    }
                }
            });
        }
        function dialogUpdate(value) {
            var title = '请求明细';
            var url = 'giveGold/updateGiveGoldTemplate?type=' + value;
            initWindow(title, url, 500, 300);
        }

        function doUpdate() {
            $('#updateForm').form('submit', {
                url: "giveGold/insertGiveGold",
                success: function (response) {
                    var parsedJson = jQuery.parseJSON(response);
                    if (parsedJson.resultCode == "success") {
                        showMessage("提示信息", parsedJson.resultMessage);
                        $('#dialogWindow').dialog('close');
                        doSearch();
                    } else {
                        showMessage("错误信息", parsedJson.resultMessage);
                    }
                }
            });
        }
    </script>
</head>
<body>
<!-- 列表 -->
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
            <a href="diamond/forwardDiamondList" <c:if test="${selected eq 'diamond'}">class="bc" </c:if> style="text-decoration: none;color: #000;">金币管理</a>
            <a href="exchange/forwardExchangeList?type=1" <c:if test="${selected eq 'exchange'}">class="bc" </c:if> style="text-decoration: none;color: #000;">兑换管理</a>
            <a href="exchange/forwardExchangeList?type=2" <c:if test="${selected eq 'withdrawals'}">class="bc" </c:if> style="text-decoration: none;color: #000;">提现管理</a>
            <a href="giveGold/forwardGiveGold" <c:if test="${selected eq 'giveGold'}">class="bc" </c:if> style="text-decoration: none;color: #000;">赠送金币设置</a>
        </h2>
    </div>
    <div>
        <a href="javascript:;" onclick="insertDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
    </div>
</div>
<!-- 添加弹出框 start -->
<div id="insert_dlg" class="easyui-dialog" style="width:400px;height:400px;" closed="true"
     buttons="#insert-dlg-buttons">
    <form id="insertForm" method="post">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">赠送类型：</td>
                <td style="text-align: left;">
                    <select name="type" id="type" required="true" style="width: 200px;"
                            class="easyui-validatebox easyui-combobox">
                        <option value="1">分享成功</option>
                        <option value="2">首次直播</option>
                        <option value="2">分享按钮</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">赠送数量：</td>
                <td style="text-align: left;">
                    <input type="text" name="diamond" id="diamond" style="width: 200px;" class="easyui-numberbox"
                           min="0" precision="0" missingMessage="必须填写大于或者等于0的整数"
                           value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">赠送次数：</td>
                <td style="text-align: left;">
                    <input type="text" name="count" id="count" style="width: 200px;" class="easyui-numberbox" min="0" precision="0" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">提示信息：</td>
                <td style="text-align: left;">
                    <input type="text" name="tips" id="tips" style="width: 200px;"
                           class="easyui-validatebox" required="true"
                           value="">
                </td>
            </tr>
        </table>
    </form>
    <div id="insert-dlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsert()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="javascript:$('#insert_dlg').dialog('close')">取消</a>
    </div>
</div>

<!-- 添加弹出框 end -->
<c:import url="/main/common.jsp"/>
</body>
</html>
