/**
 * Created by huoshanwei on 2015/11/2.
 */
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

var listUrl = "permission/listpage";
var updateUrl = "permission/update";
var deleteUrl = "permission/delete";
var addUrl = "permission/create";
var saveUrl = "permission/saveAjax";

var url;
var editingId;

$(function () {
    $(datagridId).treegrid({
        height: 550,
        fitColumns: true,
        fit: true,
        rownumbers: true,
        animate: true,
        url: listUrl,
        idField: 'id',//id=productId_propId_largetypeId_midlletypeId_comtypeId;
        treeField: 'name',
        onContextMenu: onContextMenu,
        columns: [[
            {field: 'name', title: '菜单名称', width: 100, editor: 'text'},
            {field: 'value', title: '访问地址', width: 100, editor: 'text'},
            {field: 'createDateStr', title: '添加时间', width: 100, align: 'center', editor: 'text'},
            /*                    {field: 'pid', title: 'PID', width: 100},
             {field: 'id', title: 'ID', width: 100},*/
            {field: 'sort', title: '权重', align: 'center', width: 100}
        ]],
        onDblClickRow: function (row) {
            if (row.id != editingId) {
                if (editingId != undefined) {
                    $(datagridId).treegrid("cancelEdit", editingId);
                }
            }
            editingId = row.id;
            $(datagridId).treegrid("beginEdit", editingId);
        }
    });

});

function onContextMenu(e, row) {
    e.preventDefault();
    $(this).treegrid('select', row.id);
    $('#mm').menu('show', {
        left: e.pageX,
        top: e.pageY
    });
}

var url;

function destroyPermission() {
    var row = $(datagridId).datagrid('getSelected');
    if (row) {
        $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
            if (r) {
                $.get(deleteUrl, {id: row.id}, function (result) {
                    if (result["success"] == true) {
                        window.location.reload();// reload the user data
                    } else {
                        showMessage("Error", result["result"]);
                    }
                });
            }
        });
    } else {
        showMessage("Error", noSelectedRowMessage);
    }
}

function newPermission() {
    $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
    url = addUrl;
    $(addFormId).form('clear');

    $.get(
        "permission/getMaxSort",
        {},
        function (result) {
            //$("#addPid").textbox("setText",1);
            $("#addSort").textbox("setValue", result["result"]);
        }
    );

}

function editPermission() {
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
                window.location.reload(); // reload the user data
            } else {
                showMessage("Error", result["message"]);
            }
        }
    });
}

function doSearch() {
    $(datagridId).datagrid('reload', getFormJson(searchFormId));
}

function save() {
    if (editingId != undefined) {
        var t = $(datagridId);
        var row = t.treegrid("find", editingId);
        t.treegrid('endEdit', editingId);
        $.ajax({
            url: saveUrl,
            data: {name: row.name, value: row.value, id: row.id},
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result["success"] == true) {
                    showMessage("成功提示", "操作成功");

                } else {
                    showMessage("Error", result["message"]);
                }
            }
        });
        editingId = undefined;
    }
}

function cancel() {
    if (editingId != undefined) {
        $(datagridId).treegrid('cancelEdit', editingId);
        editingId = undefined;
    }
}

var idIndex = 100;
function append() {
    idIndex++;
    var node = $(datagridId).treegrid('getSelected');
    var name = "新建菜单";
    var value = "index/welcome";
    if (node.pid != 0) {
        showMessage("错误", "此处不允许添加菜单");
        return;
    }
    $.ajax({
        url: addUrl,
        data: {pid: node.id, name: name, value: value},
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result["success"] == true) {
                $(datagridId).treegrid('append', {
                    parent: node.id,
                    data: [{
                        id: result["result"]["id"],
                        name: name,
                        value: value,
                        pid: result["result"]["pid"],
                        sort: result["result"]["sort"]
                    }]
                });

                editingId = result["result"]["id"];
                $(datagridId).treegrid("beginEdit", editingId);
            } else {
                showMessage("Error", result["message"]);
            }
        }
    });
}