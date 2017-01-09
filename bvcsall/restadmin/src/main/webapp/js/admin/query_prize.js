/**
 * Created by busap on 2015/10/9.
 */
var listUrl = "activity/queryActivityPrizes";
$(function () {
    $('#displayTable').datagrid({
        nowrap: true, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: true,
        singleSelect: true, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        queryParams: {
            activityId: $("#commonActivityId").val()
        },
        columns: [
            [
                {field: 'ck', width: 100, checkbox: true},
                {field: 'name', title: '<span class="columnTitle">中奖活动名称</span>', width: 120, align: 'center'},
                {field: 'startDate', title: '<span class="columnTitle">开始时间</span>', width: 120, align: 'center'},
                {field: 'endDate', title: '<span class="columnTitle">结束时间</span>', width: 80, align: 'center'},
                {field: 'createDateStr', title: '<span class="columnTitle">创建时间</span>', width: 80, align: 'center'},
                {field: 'username', title: '<span class="columnTitle">创建人</span>', width: 80, align: 'center'},
                {
                    field: 'status', title: '<span class="columnTitle">状态</span>', width: 100, align: 'center',
                    formatter: function (value, row) {
                        if (value == 0) {
                            return "有效";
                        } else {
                            return "无效";
                        }
                    }
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="updateDialog(' + row.id + ')" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var prizeDetail = '<a class="easyui-linkbutton" href="prize/prizeDetail?activityId='+$("#commonActivityId").val()+'&prizeId=' + row.id + '" style="width:80px;height: 25px;" title="中奖名单"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">中奖名单</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="doDelete(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                        return editStr + " " + prizeDetail + " " + deleteStr;
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

function insertDialog() {
    $("#insert_dlg").dialog('open').dialog('setTitle', "添加");
}

function doInsert() {
    var activityId = $("#commonActivityId").val();
    var name = $("#name").val();
    var startDate = $("#startDate").datebox("getValue");
    var endDate = $("#endDate").datebox("getValue");
    var status = $("#status").combobox("getValue");
    $.ajax({
        url: 'prize/insertPrize',
        data: {'activityId': activityId, 'name': name, 'startDate': startDate, 'endDate': endDate, 'status': status},
        type: "post",
        dataType: "json",
        success: function (result) {
            $('#insert_dlg').dialog('close');
            showMessage("提示信息", result.resultMessage);
            doSearch();
        }
    });
}

function updateDialog(prizeId) {
    $.ajax({
        url: 'prize/queryPrizeById',
        data: {'prizeId': prizeId},
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.resultCode == "success") {
                $("#update_id").val(result.data.id);
                $("#update_name").val(result.data.name);
                $("#update_startDate").datebox("setValue", result.data.startDate);
                $("#update_endDate").datebox("setValue", result.data.endDate);
                $("#update_status").combobox("setValue", result.data.status)
                $("#update_dlg").dialog('open').dialog('setTitle', "更新");
            } else {
                showMessage("提示信息", "获取数据失败，请重试！");
            }
        }
    });
}

function doUpdate() {
    var id = $("#update_id").val();
    var name = $("#update_name").val();
    var startDate = $("#update_startDate").datebox("getValue");
    var endDate = $("#update_endDate").datebox("getValue");
    var status = $("#update_status").combobox("getValue");
    $.ajax({
        url: 'prize/updatePrize',
        data: {"id": id, "name": name, "startDate": startDate, "endDate": endDate, "status": status},
        type: "post",
        dataType: "json",
        success: function (result) {
            $('#update_dlg').dialog('close');
            showMessage("提示信息", result.resultMessage);
            doSearch();
        }
    });
}

function doDelete(id) {
    if (confirm("您确定要执行删除操作吗？")) {
        if (id > 0) {
            $.ajax({
                url: 'prize/deletePrize',
                data: {"id": id},
                type: "post",
                dataType: "json",
                success: function (result) {
                    showMessage("提示信息", result.resultMessage);
                    doSearch();
                }
            });
        } else {
            showMessage("提示信息", "删除失败，不要淘气哦")
        }
    }
}

function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.name = $('#queryName').val();
    queryParams.status = $('#queryStatus').combobox("getValue");
    queryParams.startDate = $('#queryStartDate').datebox("getValue");
    queryParams.endDate = $('#queryEndDate').datebox("getValue");
    $('#displayTable').datagrid({url: listUrl});
}

