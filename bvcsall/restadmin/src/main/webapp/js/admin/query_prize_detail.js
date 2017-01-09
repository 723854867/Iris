/**
 * Created by huoshanwei on 2015/10/9.
 */
var listUrl = "prize/queryPrizeDetails";
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
            prizeId: $("#commonPrizeId").val()
        },
        columns: [
            [
                {field: 'prizeLevelName', title: '<span class="columnTitle">奖项名称</span>', width: 120, align: 'center'},
                {field: 'prizeName', title: '<span class="columnTitle">奖品名称</span>', width: 120, align: 'center'},
                {field: 'userId', title: '<span class="columnTitle">用户ID</span>', width: 120, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 120, align: 'center'},
                {field: 'username', title: '<span class="columnTitle">用户名</span>', width: 120, align: 'center'},
                {field: 'phone', title: '<span class="columnTitle">用户手机</span>', width: 120, align: 'center'},
                {field: 'createDateStr', title: '<span class="columnTitle">创建时间</span>', width: 120, align: 'center'},
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="updateDialog(' + row.id + ')" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deletePrizeDetail(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                        return editStr + " " + deleteStr;
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

$(function () {
    $('#displayUserTable').datagrid({
        nowrap: true, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: true,
        singleSelect: false, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        queryParams: {
            activityId: $("#commonActivityId").val()
        },
        url: "prize/querySelectUsers",
        columns: [
            [
                {
                    field: 'pic', title: '<span class="columnTitle">用户头像</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        var pic = "<img src='/restadmin/download/" + value + "' style='width:70px;height:70px;border:1px solid #CCCCCC;'>";
                        return pic;
                    }
                },
                {field: 'id', title: '<span class="columnTitle">用户ID</span>', width: 120, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">用户昵称</span>', width: 120, align: 'center'},
                {field: 'username', title: '<span class="columnTitle">用户名</span>', width: 120, align: 'center'},
                {field: 'phone', title: '<span class="columnTitle">手机号码</span>', width: 120, align: 'center'},
                {
                    field: 'vipStat', title: '<span class="columnTitle">用户等级</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if (value == 0) {
                            return "普通";
                        } else if (value == 1) {
                            return "蓝V";
                        } else if (value == 2) {
                            return "黄V";
                        } else if (value == 3) {
                            return "绿V";
                        } else {
                            return "普通";
                        }
                    }
                }
            ]
        ],
        onLoadSuccess: function () {
            $('#displayUserTable').datagrid('clearSelections');
        },
        toolbar: "#dataGridToolbarUser",
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
});

function doSearchUsers() {
    var queryParams = $('#displayUserTable').datagrid('options').queryParams;
    queryParams.user = $('#queryUser').combobox("getValue");
    queryParams.userKeyword = $('#queryUserKeyword').val();
    queryParams.userType = $('#userType').combobox("getValue");
    queryParams.vipStat = $('#vipStat').combobox("getValue");
    $('#displayUserTable').datagrid({url: "prize/querySelectUsers"});
}

function selectUsers() {
    $("#select_user_dlg").dialog('open').dialog('setTitle', "选择中奖用户");
}

function doSelectUser() {
    var selectObj = $('#displayUserTable').datagrid("getSelections");
    var idStr = "";
    var picStr = "";
    for (var i = 0; i < selectObj.length; i++) {
        idStr += selectObj[i]["id"] + ",";
        picStr += selectObj[i]["pic"] + ",";
    }
    var ids = idStr.substring(0, idStr.length - 1);
    $("#userIds").val(ids);
    $("#picInput").val(picStr);
    $('#select_user_dlg').dialog('close');
}

function insertDialog() {
    $("#insert_dlg").dialog('open').dialog('setTitle', "添加");
}

function doInsert() {
    var prizeId = $("#commonPrizeId").val();
    var prizeName = $("#prizeName").val();
    var prizeLevel = $("#prizeLevel").combobox("getValue");
    var prizeLevelName = $("#prizeLevel").combobox("getText");
    var userIds = $("#userIds").val();
    $.ajax({
        url: 'prize/insertPrizeDetail',
        data: {
            'prizeId': prizeId,
            'prizeName': prizeName,
            'prizeLevel': prizeLevel,
            'prizeLevelName': prizeLevelName,
            'userIds': userIds
        },
        type: "post",
        dataType: "json",
        success: function (result) {
            $('#insert_dlg').dialog('close');
            showMessage("提示信息", result.resultMessage);
            doSearch();
        }
    });
}

function updateDialog(id) {
    $.ajax({
        url: 'prize/queryPrizeDetailById',
        data: {'id': id},
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.resultCode == "success") {
                $("#update_id").val(result.data.id);
                $("#update_prize_name").val(result.data.prizeName);
                $("#update_prize_level").combobox("setValue", result.data.prizeLevel);
                $("#update_user_id").combogrid("setValue", result.data.userId);
                $("#update_dlg").dialog('open').dialog('setTitle', "更新");
            } else {
                showMessage("提示信息", "获取数据失败，请重试！");
            }
        }
    });
}

function doUpdate() {
    var id = $("#update_id").val();
    var prizeName = $("#update_prize_name").val();
    var prizeLevel = $("#update_prize_level").combobox("getValue");
    var userId = $("#update_user_id").combogrid("getValue");
    $.ajax({
        url: 'prize/updatePrizeDetail',
        data: {"id": id, "prizeName": prizeName, "prizeLevel": prizeLevel, "userId": userId},
        type: "post",
        dataType: "json",
        success: function (result) {
            $('#update_dlg').dialog('close');
            showMessage("提示信息", result.resultMessage);
            doSearch();
        }
    });
}

function deletePrizeDetail(id) {
    if (confirm("您确定要执行删除操作吗？")) {
        if (id > 0) {
            $.ajax({
                url: 'prize/deletePrizeDetail',
                data: {"id": id},
                type: "post",
                dataType: "json",
                success: function (result) {
                    showMessage("提示信息", result.resultMessage);
                    doSearch();
                }
            });
        } else {
            showMessage("提示信息", "删除失败，不要淘气哦！")
        }
    }
}

function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.user = $('#user').combobox("getValue");
    queryParams.userKeyword = $('#userKeyword').val();
    queryParams.prizeLevel = $('#query_prize_level').combobox("getValue");
    $('#displayTable').datagrid({url: listUrl});
}

