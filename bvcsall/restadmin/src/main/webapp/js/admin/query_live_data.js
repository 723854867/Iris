/**
 * Created by huoshanwei on 2015/10/29.
 */
var listUrl = "liveParam/getDailyLiveData";
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
        singleSelect: false, //是否单选
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        columns: [
            [
                {field: 'date', title: '<span class="columnTitle">日期</span>', width: 100, align: 'center'},
                {field: 'maxOnlineNum', title: '<span class="columnTitle">最大同时在线人数</span>', width: 100, align: 'center'},
                {field: 'liveNum', title: '<span class="columnTitle">当日直播人数</span>', width: 100, align: 'center'},
                {field: 'newRegLiveNum', title: '<span class="columnTitle">当日注册开播人数</span>', width: 100, align: 'center'},
                {field: 'newLiveNum', title: '<span class="columnTitle">当日新开播人数</span>', width: 100, align: 'center'},
                {field: 'modify', title: '<span class="columnTitle">详情</span>', width: 100, align: 'center',
                    formatter: function (value, row) {
                        var detail = '<a class="easyui-linkbutton" href="javascript:;" onclick="dialogDetail(\'' + row.date + '\','+row.liveNum+')" style="width:80px;height: 25px;" title="详情"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">详情</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        return detail;
                    }
                }
            ]
        ],
        onLoadSuccess: function () {
            $("#count").text($('#displayTable').datagrid('getData')['totalLiveNum']);
            $('#displayTable').datagrid('clearSelections');
        }
    });
});

function compute(colName) {
    var rows = $('#displayTable').datagrid('getRows');
    var total = 0;
    for (var i = 0; i < rows.length; i++) {
        total += parseFloat(rows[i][colName]);
    }
    return total;
}

//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    var startTime = $('#startTime').datebox('getValue');
    queryParams.platform = $('#platform').combobox('getValue');
    queryParams.regPlatform = $('#regPlatform').combobox('getValue');
    queryParams.appVersion = $('#appVersion').combobox('getValue');
    if (startTime && startTime.length > 0) {
        queryParams.startTime = $('#startTime').datebox('getValue');
        queryParams.endTime = $('#endTime').datebox('getValue');
        $('#displayTable').datagrid({url: listUrl});
    } else {
        alert("请选择一个开始时间");
    }
}


function updateActiveStatus(obj, activityId, status) {

    $.ajax({
        url: 'activity/updateActiveStatus',
        data: {'activeId': activityId, 'status': status},
        type: "post",
        dataType: "json",
        success: function (result) {
            //alert("修改成功!");
            doSearch();
        }
    });
}

function deleteActivity(id) {
    if (confirm("您确定要执行删除操作吗？")) {
        if (id > 0) {
            $.ajax({
                url: 'liveActivity/deleteLiveActivity',
                data: {'activityId': id},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.resultCode == "ok") {
                        alert(result.resultMessage);
                        doSearch();
                    } else {
                        alert(result.resultMessage);
                    }
                }
            });
        } else {
            alert("系统出错，请重试！");
        }
    }
}

function onLineAll(status) {
    if (confirm("您确定要执行此操作吗？")) {
        var selectObj = $('#displayTable').datagrid("getSelections");
        var idStr = "";
        for (var i = 0; i < selectObj.length; i++) {
            idStr += selectObj[i]["id"] + ","
        }
        var ids = idStr.substring(0, idStr.length - 1);
        if (ids.length <= 0) {
            alert("请至少选择一行！");
        } else {
            $.ajax({
                url: 'liveActivity/updateStatus',
                data: {'ids': ids, "status": status},
                type: "post",
                dataType: "json",
                success: function (result) {
                    doSearch();
                }
            });
        }
    }
}

function showData(id) {
    $.ajax({
        url: 'activity/datas',
        data: {'id': id},
        type: "get",
        dataType: "json",
        success: function (result) {
            var content = "<div>马甲视频:" + result['majiaVideoCount']
                +"&nbsp;&nbsp;我拍视频:" + result['userVideoCount']
                + "</div><div>参与人数:" + result['userCount']
                + "&nbsp;&nbsp;点赞总数:" + result['praiseCount']
                + "&nbsp;&nbsp;评论总数:" + result['evaluationCount'] + "</div>";
            $("#datas" + id).html(content);
            $("#datas" + id).attr("style", "display:block");
        }
    });
}

function editActivityOrderNum(id) {
    var orderNum = $("#orderNum" + id).val();
    $.ajax({
        url: 'activity/editActivityOrderNum',
        type: 'post',
        data: {'id':id,'orderNum':orderNum},
        async: false, //默认为true 异步
        error: function () {
            showMessage('错误提示',"更新失败，请重试！");
        },
        success: function (data) {
            doSearch();
        }
    });
}

function liveDayDetailData(date,platform,regPlatform,appVersion,count) {
    $('#displayDetailTable').datagrid({
        nowrap: true, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: true,
        singleSelect: true, //是否单选
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: "liveParam/getLiveDetailData",
        queryParams:{date:date,platform:platform,regPlatform:regPlatform,appVersion:appVersion},
        columns: [
            [
                {field: 'time', title: '<span class="columnTitle">时间</span>', width: 100, align: 'center'},
                {field: 'count', title: '<span class="columnTitle">直播人次</span>', width: 100, align: 'center'},
                {field: 'modify', title: '<span class="columnTitle">占比</span>', width: 100, align: 'center',
                    formatter: function (value, row) {
                        return (Math.round(row.count / compute() * 10000) / 100.00 + "%");
                    }
                }
            ]
        ],
        onLoadSuccess: function () {
            $('#displayDetailTable').datagrid('clearSelections');
        }
    });
}

function dialogDetail(date,count){
    var platform = $('#platform').combobox('getValue');
    var regPlatform = $('#regPlatform').combobox('getValue');
    var appVersion = $('#appVersion').combobox('getValue');
    liveDayDetailData(date,platform,regPlatform,appVersion,count);
    $("#detail_dlg").dialog('open').dialog('setTitle', "详情");
}

function compute() {
    var rows = $('#displayDetailTable').datagrid('getRows')//获取当前的数据行
    var total = 0;
    for (var i = 0; i < rows.length; i++) {
        total += rows[i]['count'];
    }
    return total;
}