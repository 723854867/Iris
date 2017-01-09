/**
 * Created by caojunming on 2016/7/27.
 */
var listUrl = "logsta/queryStaList?functionId=5";
$(function () {
	$.getJSON('logsta/querySelect?param=platform&functionId=5', function(json){
		console.log(json);
		$('#platform').combobox({
			data : json.rows,
			valueField : 'id',
			textField : 'text'
		});
	});
	$.getJSON('logsta/querySelect?param=channel&functionId=5', function(json){
		console.log(json);
		$('#channel').combobox({
			data : json.rows,
			valueField : 'id',
			textField : 'text'
		});
	});
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
                    field: 'payNum', 
                    title: '<span class="columnTitle">支付人数</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value;
                    }
                }, {
                	field: 'payTimes', 
                	title: '<span class="columnTitle">支付次数</span>', 
                	width: 120, 
                	align: 'center'
                }, {
                    field: 'paySum', 
                    title: '<span class="columnTitle">总金额</span>', 
                    width: 120, 
                    align: 'center',
                    formatter: function (value) {
                    	return value;
                    }
                }, {
                    field: 'regPayNum', 
                    title: '<span class="columnTitle">当日注册支付人数</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                        return value;
                    }
                }, {
                	field: 'regPayTimes', 
                	title: '<span class="columnTitle">当日注册支付次数</span>', 
                	width: 120,
                	align:'center'
                }, {
                    field: 'regPaySum', 
                    title: '<span class="columnTitle">当日注册支付金额</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value, row) {
                        return value;
                    }
                }, {
                    field: 'platform', 
                    title: '<span class="columnTitle">平台</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value, row) {
                        return value;
                    }
                }, {
                    field: 'channel', 
                    title: '<span class="columnTitle">渠道</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value, row) {
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
    queryParams.platform = $('#platform').combobox("getValue");
    queryParams.channel = $('#channel').combobox("getValue");
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');
    /*            queryParams.pStartTime = $('#pStartTime').datebox('getValue');
     queryParams.pEndTime = $('#pEndTime').datebox('getValue');*/
    $('#displayTable').datagrid({url: listUrl});
}
