<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>操作日志管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        var datagridId = "#tt";
        var searchFormId = "#searchForm";
        var pageSize = 50;
        var listUrl = "operationLog/queryOperationLogs";

        $(function () {
            $(datagridId).datagrid({
                fitColumns: true,
                rownumbers: true,
                striped: true,
                pagination: true,
                pageNumber: 1,
                fit: true,
                pageList: [pageSize, pageSize * 2, pageSize * 3],
                pageSize: pageSize,
                pagePosition: 'bottom',
                singleSelect: true,
                selectOnCheck: false,
                nowrap: true,
                url: listUrl,
                queryParams: {
                    uname: '${searchUsername}'
                },
                columns: [[
                    {field: 'uname', title: '操作人', align: 'center', width: 100},
                    {field: 'name', title: '所属组', align: 'center', width: 100},
                    {field: 'createDateStr', title: '操作时间', align: 'center', width: 100},
                    {field: 'ip', title: 'IP', align: 'center', width: 100},
                    /*{field: 'reqUrl', title: '请求url', width: 100},*/
                    {field: 'description', title: '操作描述', align: 'center', width: 100},
                    {field: 'permissionName', title: '操作模块', align: 'center', width: 100},
                    /*{field: 'uid', title: '操作用户id', width: 100},*/
                ]],
            });
        });

        function doSearch() {
            var queryParams = $(datagridId).datagrid('options').queryParams;
            queryParams.uname = $('#uname').val();
            //alert(queryParams.uname);
            queryParams.roleId = $('#group').combobox("getValue");
            queryParams.permissionId = $('#permissionId').combobox("getValue");
            queryParams.startTime = $('#startTime').datebox('getValue');
            queryParams.endTime = $('#endTime').datebox('getValue');
            $(datagridId).datagrid({url: listUrl});
        }
    </script>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">操作日志管理</h2>
    </div>
    <div>
            <span>
                <label>用户名：</label>
                <span>
                    <input type="text" class="textbox" id="uname" value="${searchUsername}" style="padding:2px;">
                </span>
            </span>
            <span>
                <label>日期：</label>
                <span>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="startTime" id="startTime">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="endTime" id="endTime">
                </span>
            </span>
            <span>
                <label>
                    所属管理组：
                </label>
                <span>
                    <input id="group" class="easyui-combobox"
                           data-options="valueField:'id',textField:'name',url:'user/rolelistAjax'">
                </span>
            </span>
            <span>
                <label>
                    操作模块：
                </label>
                <span>
                    <input id="permissionId" class="easyui-combobox"
                           data-options="valueField:'id',textField:'text',url:'comboBox/getPermissionCombobox'">
                </span>
            </span>
            <span>
            <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
               iconCls="icon-search">搜索</a>
         </span>
    </div>
</div>

</body>
</html>
