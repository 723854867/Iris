/**
 * Created by huoshanwei on 2016/4/25.
 */
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
        url: "living/queryIrregularityImage",
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">用户ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 40, align: 'center'},
                {field: 'username', title: '<span class="columnTitle">用户名</span>', width: 40, align: 'center'},
                {field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 40, align: 'center'},
                {
                    field: 'imageUrl', title: '<span class="columnTitle">违规图片</span>', width: 100, align: 'center',
                    formatter: function (value, row) {
                        return '<img src="' + value + '" style="width:100px;height:100px;">';
                    }
                },
                {field: 'type', title: '<span class="columnTitle">违规类型</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if (value == 1) {
                            return "涉黄";
                        }else{
                            return "暴恐";
                        }
                    }
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                            var offLine = '<a class="easyui-linkbutton" href="javascript:;" onclick="settingLiveOfflineDlg(' + row.roomId + ',\'' + row.streamId + '\')" style="width:80px;height: 25px;" title="下线"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">下线</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                            var ignore = '<a class="easyui-linkbutton" href="javascript:;" onclick="ignoreIrregularityRoom(\'' + row.streamId + '\')" style="width:80px;height: 25px;" title="忽略"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">忽略</span><span class="l-btn-icon icon-cancel">&nbsp;</span></span></a>';
                            return offLine+" "+ignore;
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
    $('#displayTable').datagrid({url: "living/queryIrregularityImage"});
}

function settingLiveOfflineDlg(id,streamId){
    $("#roomId").val(id);
    $("#streamId").val(streamId);
    $("#dlg").dialog('open').dialog('setTitle', "下线禁播");
}

function offline() {
    var id = $("#roomId").val();
    var mess = $('#message').val();
    var expire = $('#expireMin').val();
    var streamId = $('#streamId').val();
    if(mess=='' || expire==''){
        showMessage("提示信息", "请输入数据！");
        return;
    }
    if(mess.length>16){
        showMessage("提示信息", "输入禁播原因不能超过16个字！");
        return;
    }
    $.ajax({
        url: 'living/offlineIrregularityRoom',
        type: 'get',
        data: {roomId: id,message:mess,expireMin:expire,streamId:streamId},
        async: false, //默认为true 异步
        error: function () {
            showMessage("提示信息", "下线失败，请重试！");
        },
        success: function (result) {
            if (result == "success") {
                showMessage("提示信息", "下线成功！");
                $('#dlg').dialog('close');
                doSearch();
            } else {
                showMessage("提示信息", "下线失败，请重试！");
            }
        }
    });
}

function ignoreIrregularityRoom(streamId){
    if(confirm("确定要忽略该条信息吗？")){
        $.ajax({
            url: 'living/ignoreIrregularityRoom',
            type: 'get',
            data: {streamId:streamId},
            async: false, //默认为true 异步
            error: function () {
                showMessage("提示信息", "忽略失败，请重试！");
            },
            success: function (result) {
                if (result == "success") {
                    showMessage("提示信息", "忽略成功！");
                    doSearch();
                } else {
                    showMessage("提示信息", "忽略失败，请重试！");
                }
            }
        });
    }
}