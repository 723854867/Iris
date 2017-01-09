/**
 * Created by huoshanwei on 2016/3/30.
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
        queryParams: {
            id: $("#userId").val()
        },
        url: "ruser/queryAnchorLiveDetailRecord",
        columns: [
            [
                {field: 'title', title: '<span class="columnTitle">直播主题</span>', width: 120, align: 'center'},
                {
                    field: 'createDate',
                    title: '<span class="columnTitle">直播时间</span>',
                    width: 120,
                    align: 'center'
                },
                {
                    field: 'duration', title: '<span class="columnTitle">时长</span>', width: 80, align: 'center',
                    formatter: function (value, row) {
                        if (value == null) {
                            return "";
                        } else {
                            return formatDuring(value, 2);
                        }
                    }
                },
                {
                    field: 'giftNumber',
                    title: '<span class="columnTitle">礼物数</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'pointNumber', title: '<span class="columnTitle">金豆</span>', width: 70, align: 'center'
                },
                {
                    field: 'maxAccessNumber',
                    title: '<span class="columnTitle">观看人数</span>',
                    width: 100,
                    align: 'center'
                },
                {
                    field: 'praiseNumber', title: '<span class="columnTitle">赞数量</span>', width: 70, align: 'center'
                }

            ]
        ],
        toolbar: "#displayTableToolbar",
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

function formatDuring(mss, type) {
    if (type == 1) {
        var days = mss / (1000 * 60 * 60 * 24);
        var hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        var minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        var seconds = (mss % (1000 * 60)) / 1000;
        return days + "天" + hours + "时" + minutes + "分" + seconds + "秒";
    } else {
        var hours = parseInt(mss / (1000 * 60 * 60));
        if (hours < 1) {
            var minutes = parseInt(mss / (1000 * 60));
            if (minutes < 1) {
                var seconds = parseInt(mss / 1000);
                return seconds + "秒";
            } else {
                var seconds = parseInt((mss % (1000 * 60)) / 1000);
                return minutes + "分" + seconds + "秒";
            }
        } else {
            var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = parseInt((mss % (1000 * 60)) / 1000);
            return hours + "时" + minutes + "分" + seconds + "秒";
        }

    }
}

function doSearch(){
    var id = $("#userId").val();
    var startDate = $('#startDate').datebox('getValue');
    var endDate = $('#endDate').datebox('getValue');
    window.location.href = "ruser/forwardUserLiveDetail?id=" + id + "&startDate="+startDate+"&endDate="+endDate;
}
