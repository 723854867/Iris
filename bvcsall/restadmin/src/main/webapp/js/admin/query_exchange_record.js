/**
 * Created by huoshanwei on 2016/4/13.
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
        url: "userExchangeRecord/queryUserExchangeRecord",
        rowStyler: function (index, row) {
            if (row.status == "failed") {
                return 'color:red;';
            }
        },
        //手机号 提现金额（金豆/现金）类型 结算时间  状态 操作
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center', sortable: true},
                {field: 'name', title: '<span class="columnTitle">用户名</span>', width: 120, align: 'center'},
                {
                    field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 80, align: 'center'
                },
                {field: 'orgName', title: '<span class="columnTitle">机构</span>', width: 120, align: 'center'},
                {
                    field: 'amountSettle', title: '<span class="columnTitle">提现金额</span>', width: 80, align: 'center',
                    formatter: function (value,row) {
                        if(row.channel == "wx_pub"){
                            return row.exchangePoint + "金豆/" +row.amount/100+"元";
                        }else if(row.channel == "org"){
                            return row.exchangePoint + "金豆/" +row.amount/100+"元";
                        }else{
                            return row.exchangePoint + "金豆/" +row.received+"金币";
                        }
                    }
                },
                {
                    field: 'channel', title: '<span class="columnTitle">类型</span>', width: 80, align: 'center',
                    formatter: function (value, row) {
                        if(value == "wx_pub"){
                            return "提现";
                        }else if(value == "exchange"){
                            return "兑换";
                        }else if(value == "org"){
                            return "机构";
                        }else{
                            return "未知";
                        }
                    }
                },
                {
                    field: 'created',
                    title: '<span class="columnTitle">结算时间</span>',
                    width: 100,
                    align: 'center',
                    formatter: function (value) {
                        return formatDate(value*1000);
                    }
                },
                {
                    field: 'status', title: '<span class="columnTitle">状态</span>', width: 70, align: 'center',
                    formatter: function (value) {
                        if(value == "pending"){
                            return "处理中";
                        }else if(value == "paid"){
                            return "成功";
                        }else{
                            return "失败";
                        }
                    }
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 200, align: 'center',
                    formatter: function (value, row) {
                        var detailStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="dialogUpdate(' + row.id + ')" style="width:80px;height: 25px;" title="详情"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">详情</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        return detailStr;
                    }
                }
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function (result) {
            $("#goldCoinCount").text(result.result["exchangePoint"]+"个");
            $("#cashCount").text(result.result["amount"]/100+"元");
            $("#personCount").text(result.result["personCount"]+"人");
            $('#displayTable').datagrid('clearSelections');
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
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.userKeyword = $('#userKeyword').val();
    queryParams.status = $('#status').combobox("getValue");
    queryParams.channel = $('#channel').combobox("getValue");
    queryParams.userKey = $('#userKey').combobox("getValue");
    queryParams.organizationId = $('#organizationId').combobox("getValue");
    queryParams.startDate = $("#startDate").datebox("getValue");
    queryParams.endDate = $("#endDate").datebox("getValue");
    $('#displayTable').datagrid({url: "userExchangeRecord/queryUserExchangeRecord"});
}

function exportExcel(){
    var userKeyword = $('#userKeyword').val();
    var status = $('#status').combobox("getValue");
    var channel = $('#channel').combobox("getValue");
    var userKey = $('#userKey').combobox("getValue");
    var organizationId = $('#organizationId').combobox("getValue");
    var startDate = $("#startDate").datebox("getValue");
    var endDate = $("#endDate").datebox("getValue");
    window.location.href = "userExchangeRecord/exportUserExchangeRecord?startDate="+
        startDate+"&endDate="+endDate+"&userKeyword="+userKeyword+"&status="+status+"&channel="+channel+
    "&userKey="+userKey+"&organizationId="+organizationId;
}

