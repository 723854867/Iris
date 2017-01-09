/**
 * Created by caojunming on 2016/7/27.
 */
var listUrl = "logsta/queryStaList?functionId=9";
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
                {
                    field: 'statDate', 
                    title: '<span class="columnTitle">统计日期</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value, row) {
                        return value;
                    }
                }, {
                    field: 'paySum', 
                    title: '<span class="columnTitle">充值总数</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value;
                    }
                }, {
                	field: 'revenueSum', 
                	title: '<span class="columnTitle">营收豆数</span>', 
                	width: 120, 
                	align: 'center',
                	formatter: function (value) {
                    	return value;
                    }
                }, {
                    field: 'rate', 
                    title: '<span class="columnTitle">付费率</span>', 
                    width: 120, 
                    align: 'center',
                    formatter: function (value) {
                    	return value;
                    }
                }, {
                    field: 'arpu', 
                    title: '<span class="columnTitle">arpu01</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value) {
                        return value;
                    }
                }, {
                	field: 'payUserSum', 
                	title: '<span class="columnTitle">付费用户数</span>', 
                	width: 120,
                	align:'center',
                	formatter: function (value) {
                        return value;
                    }
                }, {
                    field: 'revAnchorSum', 
                    title: '<span class="columnTitle">有营收主播数</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value) {
                        return value;
                    }
                }
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function (data) {
            $('#displayTable').datagrid('clearSelections');
            $('#startTime').datebox('setValue', data.startDate);
        	$('#endTime').datebox('setValue', data.endDate);
        },
        pageSize: 1000,
        pageList: [1000, 2000, 3000, 5000, 10000],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
});

//排序
function activitySort(activityId,type) {
    $.ajax({
        url: 'logsta/queryRechargeList',
        data: {'type':type,'activityId': activityId},
        type: "post",
        dataType: "json",
        success: function (result) {
            if(result.resultCode == "ok") {
                doSearch()
            } else {
                showMessage("错误提示",result.resultMessage);
            }
        }
    });
}

//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');
    $('#displayTable').datagrid({url: listUrl});
}