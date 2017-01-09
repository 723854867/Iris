/**
 * Created by huoshanwei on 2015/11/2.
 */
var datagridId = "#tt";
var adddialogueId = "#dlg";
var editdialogueId = "#updatedlg";
var addFormId = "#fm";
var editFormId = "#updatefm";
var addTitle = "新增角色";
var editTitle = "编辑角色";
var deleteConfirmMessage = "你确定要删除吗?";
var noSelectedRowMessage = "你没有选择行";
var searchFormId = "#searchForm";
var pageSize = 50;

var listUrl = "role/queryRoleList";
var updateUrl = "role/update";
var deleteUrl = "role/delete";
var addUrl = "role/create";

var url;


$(function () {
    $(datagridId).datagrid({
        fitColumns: true,
        rownumbers: true,
        striped: true,
        pagination: true,
        pageNumber: 1,
        pageList: [pageSize, pageSize * 2, pageSize * 3],
        pageSize: pageSize,
        pagePosition: 'bottom',
        singleSelect: true,
        selectOnCheck: false,
        nowrap: true,
        fit: true,
        url: listUrl,
        columns: [[
            {field: 'name', title: '角色名称', width: 100, align: 'center'},
            {field: 'description', title: '描述', width: 100, align: 'center'},
            {
                field: 'status', title: '状态', width: 100, align: 'center',
                formatter: function (value, row, index) {
                    if (value == 0) {
                        return '正常';
                    } else {
                        return "冻结";
                    }
                }
            },
            {
                field: 'modify', title: '操作', align: 'center', width: 100,
                formatter: function (value, row, index) {
                    var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="updateRole(' + row.id + ',\'' + row.name + '\',\'' + row.description + '\')" style="width:80px;height: 25px;" title="修改"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">修改</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                    var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteRole(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                    return editStr + " " + deleteStr;
                }
            }
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
    $(addFormId).form('clear');
    $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
    url = addUrl;
    $("#permissionTree").tree({
        url: 'role/permissionListAjax',
        method: 'get',
        animate: true,
        checkbox: true
    });
}

function editUser() {
    var row = $(datagridId).datagrid('getSelected');
    if (row) {
        $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
        $(editFormId).form('load', row);
        url = updateUrl;

        $("#editPermissionTree").tree({
            url: 'role/permissionListAjax',
            method: 'get',
            animate: true,
            checkbox: true,
            onlyLeafCheck: false,
            onLoadSuccess: function (onLoadSuccess) {
                $.get("role/get", {id: row.id}, function (result) {
                    if (result["success"] == true) {
                        var pids = result["result"];
                        for (var i = 0; i < pids.length; i++) {
                            var node = $('#editPermissionTree').tree('find', pids[i]);
                            $('#editPermissionTree').tree('check', node.target);
                        }
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

function updateRole(id, name, description) {
    $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
    $("#name_update").textbox('setValue', name);
    $("#description_update").textbox('setValue', description);
    $("#update_id").val(id);
    url = updateUrl;
    $("#editPermissionTree").tree({
        url: 'role/permissionListAjax',
        method: 'get',
        animate: true,
        checkbox: true,
        onlyLeafCheck: true,
        onLoadSuccess: function (onLoadSuccess) {
            $.get("role/get", {id: id}, function (result) {
                if (result["success"] == true) {
                    var pids = result["result"];
                    for (var i = 0; i < pids.length; i++) {
                        var node = $('#editPermissionTree').tree('find', pids[i]);
                        $('#editPermissionTree').tree('check', node.target);
                    }
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }
    });
}

function deleteRole(id) {
    if (confirm(deleteConfirmMessage)) {
        if (id > 0) {
            $.ajax({
                url: 'role/deleteRole',
                data: {'id': id},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.resultCode == "ok") {
                        showMessage("提示信息", result.resultMessage);
                        doSearch()
                    } else {
                        showMessage("错误提示", result.resultMessage);
                    }
                }
            });
        } else {
            showMessage("错误提示", "请选择有效的行！");
        }
    }
}

function saveUser(mydialogueId, myFormId, permissionTreeId) {
    //获取选择的权限树
    var nodes = $(permissionTreeId).tree('getChecked');
    var s = '';
    for (var i = 0; i < nodes.length; i++) {
        if (s != '') s += ',';
        s += nodes[i].id;
    }
    if ($.trim(s) == '') {
        showMessage("错误", "请为角色分配权限");
        return;
    }
    var formJson = getFormJson(myFormId);
    formJson["permissionId"] = s;
    $.ajax({
        url: url,
        data: formJson,
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