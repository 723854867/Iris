<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>充值表</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type='text/javascript' src="./js/fp/vendor/jquery.ui.widget.js"></script>
    <script type='text/javascript' src="./js/fp/jquery.iframe-transport.js"></script>
    <script type='text/javascript' src="./js/fp/jquery.fileupload.js"></script>
    <script type='text/javascript' src="./js/admin/query_recharge.js"></script>
</head>
<body>

<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>

<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">充值表</h2>
    </div>
    <div>
        <table>
            <tr>
                <td><label>平台：</label></td>
                <td>
                    <select id="platform" class="easyui-combobox"></select>
                </td>
                <td><label>渠道：</label></td>
                <td>
                    <select id="channel" class="easyui-combobox"></select>
                </td>
                <td><label>统计日期：</label></td>
                <td>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="startTime" id="startTime">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="endTime" id="endTime">
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
