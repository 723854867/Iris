<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>标签管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <style type="text/css">
        #fm {
            margin: 0;
            padding: 10px 30px;
        }

        .ftitle {
            font-size: 14px;
            font-weight: bold;
            padding: 5px 0;
            margin-bottom: 10px;
            border-bottom: 1px solid #ccc;
        }

        .fitem {
            margin-bottom: 5px;
        }

        .fitem label {
            display: inline-block;
            width: 80px;
        }
    </style>
    <script type="text/javascript">
        var datagridId = "#tt";
        var adddialogueId = "#dlg";
        var editdialogueId = "#updatedlg";
        var addFormId = "#fm";
        var editFormId = "#updatefm";
        var addTitle = "新增标签";
        var editTitle = "编辑标签";
        var deleteConfirmMessage = "你确定要删除吗?";
        var noSelectedRowMessage = "你没有选择行";
        var searchFormId = "#searchForm";
        var pageSize = 50;

        var listUrl = "tag/listpage";
        var updateUrl = "tag/updatepage";
        var deleteUrl = "tag/delete";
        var addUrl = "tag/create";

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
                    {field: 'name', title: '标签名称', width: 100},
                    {
                        field: 'status', title: '状态', width: 100, formatter: function (value, row, index) {
                        if (value == 'recommend') return '推荐';
                        if (value == 'active') return '活动';
                    }
                    },
                ]],
            });
        });

        var url;

        function destroyUser() {
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
        }

        function newUser() {
            $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
            url = addUrl;
            $(addFormId).form('clear');
            $('#status').val('active');
        }

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

        function jump(event, mydialogueId, myFormId) {
            var event = event || window.event;//这里的event兼容跟上面不同，关于event的兼容，请猛戳这里
            if (event.keyCode == 13) {
                saveUser(mydialogueId, myFormId);
            }
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
<div id="tb" style="padding:5px;height:auto">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">标签管理</h2>
    </div>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:newUser()"
       plain="true">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:editUser()"
       plain="true">编辑</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="javascript:destroyUser()"
       plain="true">删除</a>
</div>
<!-- 弹出的添加或者编辑对话框 -->
<div id="dlg" class="easyui-dialog" style="width:400px;height:380px;padding:10px 20px" closed="true"
     buttons="#dlg-buttons">
    <div class="ftitle">新增标签</div>
    <!-- 添加 -->
    <form id="fm" method="post" novalidate>
        <input type="hidden" name="status" id="status" value="active"/>

        <div class="fitem">
            <label>标签名称:</label>
            <input name="name" class="easyui-validatebox" required="true" onkeypress="jump(event,'#dlg','#fm');">
        </div>
        <input type="hidden" name="id"/>
    </form>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#dlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<div id="updatedlg" class="easyui-dialog" style="width:400px;height:380px;padding:10px 20px" closed="true"
     buttons="#updatedlg-buttons">
    <div class="ftitle">修改标签</div>
    <!-- 修改 -->
    <form id="updatefm" method="post" novalidate>
        <div class="fitem">
            <label>标签名称:</label>
            <input name="name" class="easyui-validatebox" required="true"
                   onkeypress="jump(event,'#updatedlg','#updatefm');">
        </div>
        <div class="fitem">
            <label>标签状态:</label>
            <select class="easyui-select" name="status" onkeypress="jump(event,'#updatedlg','#updatefm');">
                <option value="recommend">推荐</option>
                <option value="active">活动</option>
            </select>
        </div>
        <input type="hidden" name="id"/>
    </form>
</div>

<!-- 编辑对话框里的保存和取消按钮 -->
<div id="updatedlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveUser('#updatedlg','#updatefm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
</div>

</body>
</html>
