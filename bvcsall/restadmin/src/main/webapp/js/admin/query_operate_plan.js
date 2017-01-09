/**
 * Created by huoshanwei on 2015/10/30.
 */
var datagridId = "#tt";
var adddialogueId = "#dlg";
var editdialogueId = "#updatedlg";
var addFormId = "#fm";
var editFormId = "#updatefm";
var addTitle = "新增计划";
var editTitle = "编辑计划";
var deleteConfirmMessage = "你确定要删除吗?";
var noSelectedRowMessage = "你没有选择行";
var searchFormId = "#searchForm";

var listUrl = "operatePlan/queryOperatePlanList";
var updateUrl = "operatePlan/updatepage";
var deleteUrl = "operatePlan/delete";
var addUrl = "operatePlan/add";

var url;

$(function () {
    $(datagridId).datagrid({
        fitColumns: true,
        rownumbers: true,
        striped: true,
        fit: true,
        pagination: true,
        pagePosition: 'bottom',
        singleSelect: true,
        selectOnCheck: false,
        nowrap: true,
        url: listUrl,
        columns: [[
            {field: 'planType', title: '计划类型', align: 'center', width: 100},
            {field: 'targetNum', title: '计划数值', align: 'center',width: 50},
            {
                field: 'startTime', title: '计划时间', width: 200, align: 'center', formatter: function (value, row, index) {
                return row.startDate + " - " + row.endDate;
            }
            },
            {
                field: 'timeUnit', title: '计划单位', width: 100, align: 'center', formatter: function (value, row, index) {
                if (value == 'week')
                    return "周";
                if (value == 'month')
                    return "月";
            }
            },
            {
                field: 'actualNum', title: '完成进度', width: 100, align: 'center', formatter: function (value, row, index) {
                return ((value / row.targetNum) * 100) + "%";
            }
            }
        ]],
        onLoadSuccess: function () {
            $(datagridId).datagrid('clearSelections');
        },
        toolbar: "#tb",
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });


});

var url;

function destroyOperatePlan() {
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

function newOperatePlan() {
    $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
    $(addFormId).form('clear');
    url = addUrl;
}

function editOperatePlan() {
    var row = $(datagridId).datagrid('getSelected');
    if (row) {
        $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
        $(editFormId).form('load', row);
        url = updateUrl;
    } else {
        showMessage("Error", noSelectedRowMessage);
    }
}

function saveOperatePlan(mydialogueId, myFormId) {
    if ($(myFormId).form('validate')) {
        var formjson = getFormJson(myFormId);
        if (formjson.startDate == 'undefined' || formjson.startDate == '' || formjson.endDate == 'undefined' || formjson.endDate == '') {
            showMessage("Error", "计划起止时间不能为空！");
            return false;
        }
        if (formjson.timeUnit == 'week') {
            var beginms = Date.parse(new Date(formjson.startDate.replace(/-/g, "/")));
            var endms = Date.parse(new Date(formjson.endDate.replace(/-/g, "/")));
            var days = Math.floor((endms - beginms) / (24 * 3600 * 1000))
            if (days >= 7) {
                showMessage("Error", "周计划起止时间不能大于7天！");
                return false;
            }
        } else if (formjson.timeUnit == 'month') {
            var beginms = Date.parse(new Date(formjson.startDate.replace(/-/g, "/")));
            var endms = Date.parse(new Date(formjson.endDate.replace(/-/g, "/")));
            var days = Math.floor((endms - beginms) / (24 * 3600 * 1000))
            if (days >= 31) {
                showMessage("Error", "周计划起止时间不能大于31天！");
                return false;
            }
        } else {
            showMessage("Error", "请选择计划时间单位！");
            return false;
        }
        $.ajax({
            url: 'operatePlan/add',
            data: formjson,
            type: "post",
            dataType: "json",
            beforeSend: function () {
                return $(myFormId).form('enableValidation').form('validate');
            },
            success: function (result) {
                if (result["success"] == true) {
                    $(datagridId).datagrid('reload'); // reload the user data
                } else {
                    showMessage("Error", result["message"]);
                }
            }
        });
    } else {
        showMessage("Error", "不合法的输入！");
        return false;
    }
    $(mydialogueId).dialog('close');
}