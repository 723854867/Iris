/**
 * Created by knight on 2016-04-25
 */
var listUrl = "schoolRegister/queryList";
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
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 30, align: 'center'},
                {
                    field: 'img', title: '<span class="columnTitle">图片</span>', width: 120,align:'center',
                    formatter: function (value, row) {
                        if (value == null) {
                            return "";
                        } else {
                            return "<div><img style='height:100px;cursor: pointer;border: none;margin: 3px auto;' src='/restadmin/download" + value + "' title='活动图片'/></div>";
                        }
                    }
                },
                {field: 'name', title: '<span class="columnTitle">姓名</span>', width: 40, align: 'center'},
                {field: 'age', title: '<span class="columnTitle">年龄</span>', width: 30, align: 'center'},
                {field: 'school', title: '<span class="columnTitle">学校</span>', width: 120, align: 'center'},
                {field: 'phone', title: '<span class="columnTitle">手机号码</span>', width: 80,align:'center'},
                {field: 'wechat', title: '<span class="columnTitle">微信</span>', width: 60,align:'center'},
                {field: 'qq', title: '<span class="columnTitle">QQ</span>', width: 60,align:'center'},
                {field: 'introduction', title: '<span class="columnTitle">介绍</span>', width: 240,align:'center'}
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

//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.type = $('#type').combobox("getValue");
    queryParams.param = $('#param').val();

    $('#displayTable').datagrid({url: listUrl});
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
