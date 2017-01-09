/**
 * Created by busap on 2016/1/13.
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
        url: "living/queryNoticeList",
        columns: [
            [
                {
                    field: 'pic', title: '<span class="columnTitle">预告封面</span>', width: 100, align: 'center',
                    formatter: function (value, row) {
                        return '<img src="/restadmin/download' + value + '" style="width:100px;height:100px;">';
                    }
                },
                {field: 'title', title: '<span class="columnTitle">描述</span>', width: 120, align: 'center'},
                {field: 'anchorName', title: '<span class="columnTitle">主播昵称</span>', width: 120, align: 'center'},
                {field: 'creatorId', title: '<span class="columnTitle">主播ID</span>', width: 80, align: 'center'},
                {field: 'praiseNumber', title: '<span class="columnTitle">直播时间</span>', width: 80, align: 'center'},
                {field: 'createDate', title: '<span class="columnTitle">创建时间</span>', width: 80, align: 'center'},
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        return '<span class="l-btn-text">已过期</span>';
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
