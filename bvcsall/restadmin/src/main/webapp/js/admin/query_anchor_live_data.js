/**
 * Created by huoshanwei on 2016/3/22.
 */
$(function () {
    $('#displayAnchorLiveDataTable').datagrid({
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
        url: "organization/queryOrgAnchorLiveDataList",
        queryParams: {
            organizationId: $("#organizationId").val()
        },
        columns: [
            [
                {field: 'userId', title: '<span class="columnTitle">用户ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 120, align: 'center'},
                {
                    field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 80, align: 'center'
                },

                {
                    field: 'days',
                    title: '<span class="columnTitle">直播天数</span>',
                    width: 100,
                    align: 'center'
                },
                {
                    field: 'totalDuration',
                    title: '<span class="columnTitle">直播时长</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'pointNumber',
                    title: '<span class="columnTitle">金豆数</span>',
                    width: 80,
                    align: 'center'
                }
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function () {
            $('#displayAnchorLiveDataTable').datagrid('clearSelections');
        },
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#startDate').datetimebox({
        required : false,
        onShowPanel:function(){
            $(this).datetimebox("spinner").timespinner("setValue","00:00:00");
        }
    });
    $('#endDate').datetimebox({
        required : false,
        onShowPanel:function(){
            $(this).datetimebox("spinner").timespinner("setValue","23:59:59");
        }
    });
});

function doSearch() {
    var queryParams = $('#displayAnchorLiveDataTable').datagrid('options').queryParams;
    queryParams.startDate = $("#startDate").datebox("getValue");
    queryParams.endDate = $("#endDate").datebox("getValue");
    queryParams.userKey = $("#user").combobox("getValue");
    queryParams.userKeyword = $("#userKeyword").val();
    $('#displayAnchorLiveDataTable').datagrid({url: "organization/queryOrgAnchorLiveDataList"});
}

function exportOrgAnchorLiveData(organizationId){
    var startDate = $("#startDate").datebox("getValue");
    var endDate = $("#endDate").datebox("getValue");
    var userKey = $("#user").combobox("getValue");
    var userKeyword = $("#userKeyword").val();
    window.location.href = "organization/exportOrgAnchorLiveData?organizationId=" + organizationId+"&userKey="+userKey+"&userKeyword="+userKeyword+"&startDate="+startDate+"&endDate="+endDate;
}
