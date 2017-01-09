<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>版本管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        var datagridId = "#tt";
        //var adddialogueId = "#dlg";
        var editdialogueId = "#updatedlg";
        var addFormId = "#fm";
        var editFormId = "#updatefm";
        var addTitle = "新增版本";
        var editTitle = "编辑版本";
        var deleteConfirmMessage = "你确定要删除吗?";
        var noSelectedRowMessage = "你没有选择行";
        var searchFormId = "#searchForm";
        var pageSize = 50;
        var listUrl = "version/listpage";
        var updateUrl = "version/updatepage";
        var deleteUrl = "version/delete";
        var addUrl = "version/create";
        var url;

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
                columns: [[
                    {field: 'newVersion', title: '最新版本', width: 100},
                    {field: 'forceVersion', title: '强制版本', width: 100},
                    {field: 'downloadUrl', title: '下载地址', width: 100},
                    {field: 'type', title: '平台类型', width: 100},
                    {
                        field: 'versionType',
                        title: '版本类型',
                        width: 100,
                        align: 'center',
                        formatter: function (value, row, index) {
                            if (value == 0) return '忽略';
                            if (value == 1) return '提示';
                            if (value == 2) return '强制';
                        }
                    },
                    {field: 'description', title: '描述', width: 100}
                ]],
            });
        });

        var url;

/*        function destroyUser() {
            var row = $(datagridId).datagrid('getSelected');
            if (row) {
                $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
                    if (r) {
                        $.get(deleteUrl, {id: row.id}, function (result) {
                            if (result["success"] == true) {
                                $(datagridId).datagrid('reload'); // reload the user data
                            } else {
                                showMessage("Error", result["message"]);
                            }
                        });
                    }
                });
            } else {
                showMessage("Error", noSelectedRowMessage);
            }
        }*/

/*        function newUser() {
            $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
            url = addUrl;
            $(addFormId).form('clear');
            $('#useStat').val('active');
        }*/

        function editUser() {
            var row = $(datagridId).datagrid('getSelected');
            if (row) {
                $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
                $(editFormId).form('load', row);
                url = updateUrl;
            } else {
                showMessage("Error", noSelectedRowMessage);
            }
        }

        function saveUser(mydialogueId, myFormId) {
            $.ajax({
                url: url,
                data: getFormJson(myFormId),
                type: "post",
                dataType: "json",
                beforeSend: function () {
                    return $(myFormId).form('validate');
                },
                success: function (result) {
                    if (result["success"] == true) {
                        $(mydialogueId).dialog('close'); // close the dialog
                        $(datagridId).datagrid('reload'); // reload the user data
                    } else {
                        showMessage("Error", result["message"]);
                    }
                }
            });
        }

        function doSearch() {
            $(datagridId).datagrid('reload', getFormJson(searchFormId));
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
        <h2 style="float:left;padding-left:10px;margin: 1px">版本管理</h2>
    </div>
    <div style="margin-bottom:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:editUser()"
           plain="true">编辑</a>
    </div>
</div>
<div id="updatedlg" class="easyui-dialog" style="width:400px;height:380px;padding:10px 20px" closed="true"
     buttons="#updatedlg-buttons">
    <div class="ftitle">编辑版本</div>
    <!-- 修改 -->
    <form id="updatefm" method="post" novalidate>
        <table class="doc-table">
            <tbody>
            <tr>
                <th width="25%"></th>
                <th width="75%"></th>
            </tr>
            <tr>
                <td>最新版本号:</td>
                <td>
                    <input name="newVersion" class="easyui-numberbox" required="true">
                </td>
            </tr>
            <tr>
                <td>强制版本号:</td>
                <td>
                    <input name="forceVersion" class="easyui-numberbox" required="true">
                </td>
            </tr>
            <tr>
                <td>下载地址:</td>
                <td>
                    <input name="downloadUrl" class="easyui-validatebox" required="true">
                </td>
            </tr>
            <tr>
                <td>平台类型:</td>
                <td>
                    <select class="easyui-combobox" name="type" id="type" style="width:50%">
                        <option value="ios">ios</option>
                        <option value="android">android</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>版本类型:</td>
                <td>
                    <select class="easyui-combobox" name="versionType" id="versionType" style="width:50%">
                        <option value="0">忽略</option>
                        <option value="1">提示</option>
                        <option value="2">强制</option>
                    </select>
                    <input type="hidden" name="id"/>
                </td>
            </tr>
            <tr>
                <td>描述:</td>
                <td>
                	<textarea name="description" class="easyui-validatebox" required="true" rows="" cols=""></textarea>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <!-- 编辑对话框里的保存和取消按钮 -->
                    <div id="updatedlg-buttons">
                        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
                           onclick="saveUser('#updatedlg','#updatefm')">保存</a>
                        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
                           onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </form>
</div>


</body>
</html>
