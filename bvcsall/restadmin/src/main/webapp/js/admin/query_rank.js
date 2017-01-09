/**
 * Created by caojunming on 2016/7/27.
 */
var listUrl = "logsta/queryStaList?functionId=10";
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
                    field: 'rechargeNo', 
                    title: '<span class="columnTitle">日充值排行</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value;
                    }
                }, {
                	field: 'rechargeValue', 
                	title: '<span class="columnTitle">日充值金额</span>', 
                	width: 120, 
                	align: 'center',
                	formatter: function (value) {
                    	return value;
                    }
                }, {
                    field: 'consumeNo', 
                    title: '<span class="columnTitle">日消费排行</span>', 
                    width: 120, 
                    align: 'center',
                    formatter: function (value) {
                    	return value;
                    }
                }, {
                    field: 'consumeValue', 
                    title: '<span class="columnTitle">日消费金币</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value) {
                        return value;
                    }
                }, {
                	field: 'incomeNo', 
                	title: '<span class="columnTitle">主播收入排行</span>', 
                	width: 120,
                	align:'center',
                	formatter: function (value) {
                        return value;
                    }
                }, {
                    field: 'incomeValue', 
                    title: '<span class="columnTitle">主播收入金币</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value) {
                        return value;
                    }
                }, {
                    field: 'recThirNo', 
                    title: '<span class="columnTitle">月充值排行</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value;
                    }
                }, {
                	field: 'recThirValue', 
                	title: '<span class="columnTitle">月充值金额</span>', 
                	width: 120, 
                	align: 'center',
                	formatter: function (value) {
                    	return value;
                    }
                }, {
                    field: 'conThirNo', 
                    title: '<span class="columnTitle">月消费排行</span>', 
                    width: 120, 
                    align: 'center',
                    formatter: function (value) {
                    	return value;
                    }
                }, {
                    field: 'conThirValue', 
                    title: '<span class="columnTitle">月消费金币</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value) {
                        return value;
                    }
                }, {
                	field: 'incThirNo', 
                	title: '<span class="columnTitle">主播月收入排行</span>', 
                	width: 120,
                	align:'center',
                	formatter: function (value) {
                        return value;
                    }
                }, {
                    field: 'incThirValue', 
                    title: '<span class="columnTitle">主播月收入金币</span>', 
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
        },
        pageSize: 100,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
});

//排序
function activitySort(activityId,type){
    $.ajax({
        url: 'logsta/queryRechargeList',
        data: {'type':type,'activityId': activityId},
        type: "post",
        dataType: "json",
        success: function (result) {
            if(result.resultCode == "ok"){
                doSearch()
            }else{
                showMessage("错误提示",result.resultMessage);
            }
        }
    });
}

//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.startTime = $('#startTime').datebox('getValue');
    /*            queryParams.pStartTime = $('#pStartTime').datebox('getValue');
     queryParams.pEndTime = $('#pEndTime').datebox('getValue');*/
    $('#displayTable').datagrid({url: listUrl});
}
