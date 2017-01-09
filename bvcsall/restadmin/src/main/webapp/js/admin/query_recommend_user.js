/**
 * Created by huoshanwei on 2016/2/26.
 */
$(function () {
    queryRecommendUser(1);
    $('#tabs').tabs({
        border: false,
        onSelect: function (title, index) {
            queryRecommendUser(index + 1)
            $("#position").combobox("setValue",index + 1);
        }
    });
})
function queryRecommendUser(type) {
    var listUrl = "ruser/queryRecommendUser";
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
            type: type
        },
        columns: [
            [
                {
                    field: 'pic', title: '<span class="columnTitle">用户头像</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        return "<div><img style='height:50px;cursor: pointer;border: none;margin: 3px auto;' src='/restadmin/download" + value + "' title='用户头像'/></div>";
                    }
                },
                {field: 'name', title: '<span class="columnTitle">用户昵称</span>', width: 120, align: 'center'},
                {field: 'id', title: '<span class="columnTitle">用户ID</span>', width: 120, align: 'center'},
                {
                    field: 'isAnchor', title: '<span class="columnTitle">是否主播</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if (value == 1) {
                            return "是";
                        } else {
                            return "否";
                        }
                    }
                },
                {field: 'weight', title: '<span class="columnTitle">权重</span>', width: 120, align: 'center'},
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteRecommendUser(' + type + ',' + row.id + ',' + row.weight + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                        return deleteStr;
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
}

function doSearch() {
    $('#displayTable').datagrid('reload');
}

function insertDialog() {
    $("#insert_dlg").dialog('open').dialog('setTitle', "添加");
    $("#userId").val("");
    $("#weight").numberbox("clear");
}

function doInsert() {
    var userId = $("#userId").val();
    var type = $("#position").combobox("getValue");
    var weight = $("#weight").val();
    $.ajax({
        url: 'ruser/insertRecommendUser',
        data: {
            'type': type,
            'userId': userId,
            'weight': weight
        },
        type: "post",
        dataType: "json",
        success: function (result) {
            $('#insert_dlg').dialog('close');
            showMessage("提示信息", result.resultMessage);
            if (type == 1) {
                queryRecommendUser(1);
                $("#position").combobox("setValue","1");
            } else if(type == 2) {
                queryRecommendUser(2);
                $("#position").combobox("setValue","2");
            } else{
                queryRecommendUser(3);
                $("#position").combobox("setValue","3");
            }
        }
    });
}

function deleteRecommendUser(type, value, weight) {
    if (confirm("确定要执行删除操作吗？")) {
        $.ajax({
            url: 'ruser/deleteRecommendUser',
            data: {
                'type': type,
                'userId': value,
                'weight': weight
            },
            type: "post",
            dataType: "json",
            success: function (result) {
                showMessage("提示信息", result.resultMessage);
                if (type == 1) {
                    queryRecommendUser(1);
                } else if(type == 2) {
                    queryRecommendUser(2);
                } else{
                    queryRecommendUser(3);
                }
            }
        });
    }
}

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
        singleSelect: true, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: "combogrid/getCombogridUserList",
        columns: [
            [
                {
                    field: 'isAnchor', title: '<span class="columnTitle">是否主播</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if (value == 0) {
                            return "否";
                        } else {
                            return "是";
                        }
                    }
                },
                {
                    field: 'pic', title: '<span class="columnTitle">用户头像</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        var pic = "<img src='/restadmin/download" + value + "' style='width:70px;height:70px;border:1px solid #CCCCCC;'>";
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
    $('#displayUserTable').datagrid({url: "combogrid/getCombogridUserList"});
}

function selectUsers() {
    $("#select_user_dlg").dialog('open').dialog('setTitle', "选择用户");
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
    $("#userId").val(ids);
    $("#picInput").val(picStr);
    $('#select_user_dlg').dialog('close');
}
