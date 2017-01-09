/**
 * Created by busap on 2015/12/23.
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
        url:"anchor/queryAnchorIncomeList",
        columns: [
            [
                {field: 'userId', title: '<span class="columnTitle">用户ID</span>', width: 40, align: 'center'},
                {field: 'userName', title: '<span class="columnTitle">用户名</span>', width: 120, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 120, align: 'center'},
                {field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 80, align: 'center'},
                {field: 'pointCount',title: '<span class="columnTitle">余额(金豆数/现金)</span>',width: 80,align: 'center',
                	formatter:function(value,row){
                		return value+"/"+(value/50);
                	}
                },
                {field: 'lockPoints',title: '<span class="columnTitle">结算中(金豆数/现金)</span>',width: 80,align: 'center',
                	formatter:function(value,row){
                		return value+"/"+(value/50);
                	}
                },
                {field: 'settledPoints',title: '<span class="columnTitle">已结算(金豆数/现金)</span>',width: 80,align: 'center',
                	formatter:function(value,row){
                		return value+"/"+(value/50);
                	}
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var settlement = "";
                        if (row.pointCount > 0) {
                            settlement = '<a class="easyui-linkbutton" href="javascript:;" onclick="dialogSettlement(' + row.userId + ')" style="width:80px;height: 25px;" title="结算"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">结算</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        }
                        //var history = '<a class="easyui-linkbutton" href="javascript:;" onclick="querySettlementRecord(' + row.userId + ')" style="width:80px;height: 25px;" title="历史纪录"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">历史纪录</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        return settlement ;//+ " " + history;
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
    
    getTotal();
});

function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.key = $('#key').val();
    queryParams.value = $('#value').val();
    $('#displayTable').datagrid({url: "anchor/queryAnchorIncomeList"});
    
    getTotal();
}

function dialogSettlement(value) {
    var title = '结算';
    var url = 'anchor/ajaxSettlementTemplate?id=' + value;
    initWindow(title, url, 450, 270);
}


function doSettlement() {
    var id = $("#id").val();
    var settlementPointCount = $("#settlementPointCount").val();
    var pointCount = $("#pointCount").val();
    if (parseInt(settlementPointCount) <= 0 || settlementPointCount == "") {
        showMessage("提示信息", "结算点数必须大于0哦！");
        return;
    }
    if (parseInt(settlementPointCount)%50 > 0 || settlementPointCount == "") {
        showMessage("提示信息", "结算点数必须是50的整数倍哦！");
        return;
    }
    if (parseInt(settlementPointCount) > parseInt(pointCount)) {
        showMessage("提示信息", "您最多只能结算" + pointCount + "点哦！");
        return;
    }
    if (confirm("您确定要执行结算操作吗？")) {
        $('#settlementForm').form('submit', {
            url: "anchor/doAnchorSettlement",
            success: function (response) {
                var parsedJson = jQuery.parseJSON(response);
                if (parsedJson.resultCode == "success") {
                    showMessage("提示信息", parsedJson.resultMessage);
                    $('#dialogWindow').dialog('close');
                    doSearch();
                } else {
                    showMessage("错误信息", parsedJson.resultMessage);
                }
            },
            error: function (response) {
                showMessage("错误信息", "结算失败");
            }
        });
    }
}

function querySettlementRecord(id) {
    $("#settlement_record_dlg").dialog('open').dialog('setTitle', "主播收益历史纪录");
    $("#id").val(id);
    getSettlementRecord(id);
}
function doSearchSettlementRecord() {
    var id = $("#id").val();
    var queryParams = $('#displaySettlementRecordTable').datagrid('options').queryParams;
    queryParams.startDate = $('#queryStartDate').datebox("getValue");
    queryParams.endDate = $('#queryEndDate').datebox("getValue");
    $('#displaySettlementRecordTable').datagrid({url: "anchor/querySettlementRecord?id="+id});
}
function exportAnchorIncome(){
	var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.key = $('#key').val();
    queryParams.value = $('#value').val();
    
    window.location.href = "anchor/exportSettlementRecordToExcel?key="+queryParams.key+"&value="+queryParams.value;
}

function getSettlementRecord(id) {
    $('#displaySettlementRecordTable').datagrid({
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
        url: "anchor/querySettlementRecord?id=" + id,
        columns: [
            [
                {field: 'createDateStr', title: '<span class="columnTitle">操作时间</span>', width: 120, align: 'center'},
                {field: 'creatorId', title: '<span class="columnTitle">操作人ID</span>', width: 120, align: 'center'},
                {field: 'points', title: '<span class="columnTitle">结算点数</span>', width: 120, align: 'center'},
                {
                    field: 'reciever', title: '<span class="columnTitle">接收人ID</span>', width: 120, align: 'center'
                }
            ]
        ],
        toolbar: "#dataGridToolbarSettlementRecord",
        onLoadSuccess: function () {
            $('#displaySettlementRecordTable').datagrid('clearSelections');
        },
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
}

function getTotal(){
	 var queryParams = $('#displayTable').datagrid('options').queryParams;
	    queryParams.key = $('#key').val();
	    queryParams.value = $('#value').val();
    
	$.get('anchor/getTotal', queryParams, function (result) {
        if (result["success"] == true) {
            $("#totalPoints").html(result["result"]);
            $("#totalCash").html(result["result"]/50);
        } else {
            showMessage("Error", result["message"]);
        }
    });
}
