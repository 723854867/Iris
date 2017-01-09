/**
 * Created by huoshanwei on 2015/10/30.
 */
var datagridId = "#tt";
var deleteConfirmMessage = "你确定要删除吗?";
var noSelectedRowMessage = "你没有选择行";
var searchFormId = "#searchForm";
var pageSize = 50;
var listUrl = "evaluation/searchListPage";
var deleteUrl = "evaluation/deleteallpage";
$(function () {
    datagridList();
});
function datagridList() {
    $(datagridId).datagrid({
        fitColumns: true,
        fit: true,
        rownumbers: true,
        pagination: true,
        pagePosition: 'bottom',
        selectOnCheck: true,
        url: listUrl,
        striped:true,
        nowrap:true,
        columns: [[
            {field: 'ck', wdith: 100, checkbox: true},
            {field: 'createDate', title: '创建时间', width: 100,align:"center"},
            {field: 'content', title: '内容', width: 100},
            {
                field: 'creatorName', title: '评论人', width: 100, formatter: function (value, row, index) {
                return '<a href="video/userdetail?uid=' + row.creatorId + '">' + value + '</a>';
            }
            },
            {field: 'creatorId', title: '评论人ID', width: 100,align:"center"},
            {field: 'username', title: '评论人用户名', width: 100,align:"center"},
            {field: 'phone', title: '评论人手机', width: 100,align:"center"},
            {
                field: 'operation',
                title: '操作',
                align: 'center',
                width: 50,
                formatter: function (value, row, index) {
                    return '<a href="javascript:void(0)" onclick="javascript:deleteEvaluation(' + row.id + ')" class="easyui-linkbutton" iconCls="icon-ok">删除</a>';
                }
            }
        ]],
        onLoadSuccess: function () {
            $(datagridId).datagrid('clearSelections');
        },
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
}

function deleteEvaluations() {
    var row = $(datagridId).datagrid('getChecked');
    var ids = [];
    var inum = 0;
    for (var r in row) {
        ids.push(row[r]['id']);
    }
    ids = ids.join(",");
    if (row.length > 0) {
        $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
            if (r) {
                $.post('evaluation/logicremove', {ids: ids}, function (result) {
                    if (result == "ok") {
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

function deleteEvaluation(evaId) {
    $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
        if (r) {
            $.get('evaluation/remove', {id: evaId}, function (result) {
                if (result == "ok") {
                    $(datagridId).datagrid('reload'); // reload the user data
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }
    });
}

function doSearch(){
    var queryParams = $(datagridId).datagrid('options').queryParams;
    queryParams.evaluationPersonType = $("#evaluationPersonType").combobox("getValue");
    queryParams.evaluationPerson = $("#evaluationPerson").val();
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');
    $(datagridId).datagrid({url: listUrl});
}