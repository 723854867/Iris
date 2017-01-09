/**
 * Created by huoshanwei on 2015/10/30.
 */
var datagridId = "#tt";
var listUrl = "sign/signlist";

$(function () {
    $(datagridId).datagrid({
        fitColumns: true,
        rownumbers: true,
        striped: true,
        fit: true,
        pagination: true,
        pagePosition: 'bottom',
        singleSelect: true,
        selectOnCheck: true,
        nowrap: true,
        url: listUrl,
        columns: [[

            {field: 'allNum', title: '获得积分用户数', width: 100, align: 'center'},
            {field: 'allSignNum', title: '积分数量', width: 40, align: 'center'},
            {field: 'dateMark', title: '日期', width: 100, align: 'center'}

        ]],
        onLoadSuccess: function () {
            $(datagridId).datagrid('clearSelections');
        },
        toolbar: "#tb",
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });


});

function doSearch() {
    var queryParams = $(datagridId).datagrid('options').queryParams;
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');
    queryParams.startCount = $('#startCount').val();
    queryParams.endCount = $('#endCount').val();
    $(datagridId).datagrid({url: listUrl});
}