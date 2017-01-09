/**
 * Created by huoshanwei on 2016/9/1.
 */

var listUrl = "user/queryWCBlackList";
$(function () {
    $('#displayTable').datagrid({
        nowrap: true, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: false,
        singleSelect: true, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">用户ID</span>', width: 80, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 80, align: 'center'},
                {field: 'orgName', title: '<span class="columnTitle">机构</span>', width: 80, align: 'center'},
                {
                    field: 'phone',
                    title: '<span class="columnTitle">手机号</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'createDateStr',
                    title: '<span class="columnTitle">注册时间</span>',
                    width: 120,
                    align: 'center'
                },
                {
                    field: 'modify',
                    title: '<span class="columnTitle">操作</span>',
                    width: 120,
                    align: 'center',
                    formatter: function (value, row) {
                        var deleteOpt = "<span style='color:#969696;'><a href='javascript:void(0)' onclick='removeWCBlackList(" + row.id + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>移除</span><span class='l-btn-icon icon-cut'>&nbsp;</span></span></a></span>";
                        return deleteOpt;
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

function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.userKeyword = $('#userKeyword').val();
    queryParams.user = $('#user').combobox("getValue");
    $('#displayTable').datagrid({url: listUrl});
}


function insertWCBlackList() {
    var userId = $("#userId").val();
    $.ajax({
        url: 'user/insertWCBlackList',
        data: {'userId': userId},
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.resultCode == "success") {
                showMessage("提示信息", result.resultMessage);
                doSearch()
            } else {
                showMessage("错误提示", result.resultMessage);
            }
        }
    });
}

function removeWCBlackList(userId){
    if(confirm("确定要将此用户移除提现黑名单吗？")){
        $.ajax({
            url: 'user/removeWCBlackList',
            data: {'userId': userId},
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.resultCode == "success") {
                    showMessage("提示信息", result.resultMessage);
                    doSearch();
                } else {
                    showMessage("错误提示", result.resultMessage);
                }
            }
        });
    }
}