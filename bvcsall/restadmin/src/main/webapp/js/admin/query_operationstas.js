/**
 * Created by zx on 2015/12/23.
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
        url: "operationSta/diamondMonthRemainingList",
        
       
        rowStyler: function (index, row) {
            if (row.state == 0) {
                return 'color:red;';
            }
        },
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center', sortable: true},
                {
                    field: 'mon', title: '<span class="columnTitle">月份</span>', width: 160, align: 'center',
                    formatter: formatterYearMonth
                },
                {
                    field: 'diamondCount',
                    title: '<span class="columnTitle">金币数</span>',
                    width: 80,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'price',
                    title: '<span class="columnTitle">人民币（元）</span>',
                    width: 100,
                    align: 'center',
                    sortable: true
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

function formatterYearMonth(val, row) {
	if (val != null) {
		var date = new Date(val);
		return date.getFullYear() + '-' + (date.getMonth() + 1) ;
		//+ '-'+ date.getDate();
	}
}

function doSearch(type) {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    //queryParams.id = $('#queryId').val();
    //queryParams.name = $('#queryName').val();
    //queryParams.isGive = $('#queryIsGive').combobox("getValue");
    //queryParams.payMethod = $('#queryPayMethod').combobox("getValue");
    //queryParams.state = $('#queryState').combobox("getValue");
    alert();
    if(type&&type=='dmr'){
    	$('#displayTable').datagrid({url: "operationSta/diamondMonthRemainingList"});
    }
}

