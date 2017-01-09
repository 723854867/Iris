/**
 * Created by huoshanwei on 2015/12/23.
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
        url: "room/queryLiveDetailList",
        columns: [
            [
                /*{field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center', sortable: true},*/
                {field: 'userId', title: '<span class="columnTitle">用户ID</span>', width: 120, align: 'center'},
                {
                    field: 'name',
                    title: '<span class="columnTitle">昵称</span>',
                    width: 100,
                    align: 'center'
                },
                {
                    field: 'bandPhone',
                    title: '<span class="columnTitle">手机号</span>',
                    width: 100,
                    align: 'center',
                    formatter: function (value, row) {
                        if (value == "") {
                            return row.phone;
                        }else {
                            return value;
                        }
                    }
                },
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
                    title: '<span class="columnTitle">观看人次（pv）</span>',
                    width: 100,
                    align: 'center'
                },
                {
                    field: 'uv',
                    title: '<span class="columnTitle">观看人数(uv)</span>',
                    width: 100,
                    align: 'center',
                    formatter: function (value, row) {
                        if (value > 0) {
                            return (value-1);
                        }
                    }
                },
                {
                    field: 'userChatCount', title: '<span class="columnTitle">用户发言量</span>', width: 70, align: 'center'
                },
                {
                    field: 'anchorChatCount', title: '<span class="columnTitle">主播发言量</span>', width: 70, align: 'center'
                },
                {
                    field: 'praiseNumber', title: '<span class="columnTitle">赞数量</span>', width: 70, align: 'center'
                },
                {
                    field: 'finishDate',
                    title: '<span class="columnTitle">结束时间</span>',
                    width: 120,
                    align: 'center'
                },
                {
                    field: 'platform', title: '<span class="columnTitle">平台</span>', width: 70, align: 'center'
                },
                {
                    field: 'appVersion', title: '<span class="columnTitle">版本</span>', width: 70, align: 'center'
                },
                {
                    field: 'channel', title: '<span class="columnTitle">渠道</span>', width: 70, align: 'center'
                },
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function () {
            $('#displayTable').datagrid('clearSelections');
        },
        pageSize: 30,
        pageList: [30, 100, 150, 200, 500],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
});

function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.userKey = $('#userKey').combobox("getValue");
    queryParams.userKeyword = $('#userKeyword').val();
    queryParams.startDate = $('#startDate').datebox("getValue");
    queryParams.endDate = $('#endDate').datebox("getValue");
    queryParams.platform = $('#platform').combobox("getValue");
    queryParams.appVersion = $('#appVersion').combobox("getValue");
    queryParams.channel = $('#channel').combobox("getValue");
    $('#displayTable').datagrid({url: "room/queryLiveDetailList"});
}

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