/**
 * Created by huoshanwei on 2015/11/3.
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
        remoteSort: true,
        singleSelect: false, //是否单选
        pagination: false, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: "integral/queryIntegralMultiTypeStatistics",
        columns: [
            [
                {field: 'date', title: '<span class="columnTitle">日期</span>', width: 120, align: 'center'},
                {field: 'signSum', title: '<span class="columnTitle">签到</span>', width: 120, align: 'center'},
                {field: 'taskSum', title: '<span class="columnTitle">日常任务</span>', width: 80, align: 'center'},
                {
                    field: 'sum', title: '<span class="columnTitle">总和</span>', width: 80, align: 'center',
                    formatter: function (value, row, index) {
                        return row.signSum + row.taskSum;
                    }
                }
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function () {
            $('#displayTable').datagrid('clearSelections');
        }
    });
});
