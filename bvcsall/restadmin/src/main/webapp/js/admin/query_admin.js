/**
 * Created by huoshanwei on 2015/11/2.
 */
var datagridId = "#tt";
var adddialogueId = "#dlg";
var editdialogueId = "#updatedlg";
var addFormId = "#fm";
var editFormId = "#updatefm";
var addTitle = "新增用户";
var editTitle = "编辑用户";
var deleteConfirmMessage = "你确定要删除吗?";
var noSelectedRowMessage = "你没有选择行";
var searchFormId = "#searchForm";
var pageSize = 50;

var listUrl = "user/queryUserList";
var updateUrl = "user/update";
var deleteUrl = "user/deleteUser";
var addUrl = "user/create";

var url;


$(function () {
    $(datagridId).datagrid({
        fitColumns: true,
        rownumbers: true,
        striped: true,
        fit: true,
        pagination: true,
        pageNumber: 1,
        pageList: [pageSize, pageSize * 2, pageSize * 3],
        pageSize: pageSize,
        pagePosition: 'bottom',
        singleSelect: true,
        selectOnCheck: true,
        nowrap: true,
        url: listUrl,
        rowStyler: function (index, row) {
            if (row.isEnabled == 0 || row.isLocked == 1) {
                return 'color:red;';
            }
        },
        columns: [[
// 					{field:'ck',checkbox:true},
            {field: 'username', title: '用户名', align: 'center', width: 100},
            {field: 'groupName', title: '所属组', align: 'center', width: 100},
            {field: 'name', title: '姓名', align: 'center', width: 100},
            {field: 'email', title: 'Email', align: 'center', width: 100},
            {field: 'phone', title: '手机号码', align: 'center', width: 100},
            {field: 'loginDate', title: '最后登录时间', align: 'center', width: 100},
            {field: 'loginIp', title: '最后登录IP', align: 'center', width: 100},
            {
                field: 'isEnabled',
                title: '账户状态',
                align: 'center',
                width: 50,
                formatter: function (value, row, index) {
                    if (value == 0) return '不可用';
                    if (value == 1) return '可用';
                }
            },
            {
                field: 'isLocked',
                title: '锁定状态',
                align: 'center',
                width: 50,
                formatter: function (value, row, index) {
                    if (value == 0) return '未锁定';
                    if (value == 1) return '已锁定';
                }
            },
            {
                field: 'modify',
                title: '操作',
                align: 'center',
                width: 120,
                formatter: function (value, row, index) {
                    var operationStr = '<a class="easyui-linkbutton" href="operationLog/operationLoglist?searchUsername=' + row.username + '" style="width:80px;height: 25px;" title="操作记录"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">操作记录</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                    var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="destroyUser(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                    return operationStr + " " + deleteStr;
                }
            }
        ]],
        onLoadSuccess: function () {
            $(this).datagrid('enableDnd');
        }
    });
});

var url;

function destroyUser(id) {
    if (id > 0) {
        $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
            if (r) {
                $.get(deleteUrl, {id: id}, function (result) {
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
    $('#isEnabled').val('1');
    $('#type').val('admin');

    $('#addrolelist').combobox({
        valueField: 'id',
        textField: 'name',
        multiple: true,
        panelHeight: 'auto',
        url: 'user/rolelistAjax'
    });

}

function editUser() {
    var row = $(datagridId).datagrid('getSelected');
    if (row) {
        $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
        $('#editrolelist').combobox({
            valueField: 'id',
            textField: 'name',
            multiple: true,
            panelHeight: 'auto',
            url: 'user/rolelistAjax'
        });
        $.get(
            "user/roleListByUserAjax",
            {id: row.id},
            function (result) {
                for (var i = 0; i < result.length; i++) {
                    $('#editrolelist').combobox('select', result[i]["id"]);
                }
            });

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
                showMessage("错误提示", result["result"], 3000);
            }
        },
        error: function (result) {
            showMessage("Error", JSON.stringify(result), 5000);
        }
    });
}

function doSearch() {
    var queryParams = $(datagridId).datagrid('options').queryParams;
    queryParams.email = $('#email').val();
    queryParams.username = $('#username').val();
    queryParams.phone = $('#phone').val();
    queryParams.group = $('#group').combobox("getValue");
    queryParams.isLocked = $('#isLockedSearch').combobox("getValue");
    queryParams.isEnabled = $('#isEnabledSearch').combobox("getValue");
    $(datagridId).datagrid({url: listUrl});
}