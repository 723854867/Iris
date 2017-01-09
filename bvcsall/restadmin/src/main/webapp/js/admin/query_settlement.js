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
        url:"settlement/querySettlementList?status=0",
        columns: [
            [
                {field: 'userId', title: '<span class="columnTitle">用户ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 120, align: 'center'},
                {field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 80, align: 'center'},
                {field: 'points',title: '<span class="columnTitle">提现金额(金豆数/现金)</span>',width: 80,align: 'center',
                	formatter:function(value,row){
                		return value+"/"+(value/50);
                	}
                },
                {field: 'createDate',title: '<span class="columnTitle">申请时间</span>',width: 80,align: 'center'},
                {field: 'approveTime',title: '<span class="columnTitle">审核时间</span>',width: 80,align: 'center'},
                {field: 'settleTime',title: '<span class="columnTitle">结算时间</span>',width: 80,align: 'center'},
                {field: 'status',title: '<span class="columnTitle">状态</span>',width: 80,align: 'center',
                	formatter:function(value,row){
                		if(value=='0')
                			return '新申请';
                		else if(value=='1')
                			return '已审核';
                		else if(value=='2')
                			return '已结算';
                	}
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var settlement = "";
                        if (row.status == 0) {
                            settlement = '<a class="easyui-linkbutton" href="javascript:;" onclick="doApprove(' + row.id + ')" style="width:80px;height: 25px;" title="审核"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">审核</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        }
                        if (row.status == 1) {
                            settlement = '<a class="easyui-linkbutton" href="javascript:;" onclick="doSettle(' + row.id + ')" style="width:80px;height: 25px;" title="结算"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">结算</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        }
                        var detail = '<a class="easyui-linkbutton" href="javascript:;" onclick="settlementDetail(' + row.id + ')" style="width:80px;height: 25px;" title="详情"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">详情</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        return settlement + " " + detail;
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
    queryParams.status = $('#status').val();
    queryParams.startDate = $('#queryStartDate').val();
    queryParams.endDate = $('#queryEndDate').val();
    
    $('#displayTable').datagrid({url: "settlement/querySettlementList"});
    
    getTotal();
}

function doSettle(value) {
	if (confirm("您确定要执行结算操作吗？")) {
    	$.ajax({
            url: 'settlement/doSettle',
            type: 'get',
            data: {'id':value},
            async: false, //默认为true 异步
            error: function () {
                showMessage('错误提示',"更新失败，请重试！");
            },
            success: function (data) {
            	if (data["success"] == true) {
            		$('#displayTable').datagrid('reload');
            	}else{
            		showMessage('错误提示',"更新失败，请重试！");
            	}
            }
        });
    }
}


function doApprove(value) {
    if (confirm("您确定要审核吗？")) {
    	$.ajax({
            url: 'settlement/doApprove',
            type: 'get',
            data: {'id':value},
            async: false, //默认为true 异步
            error: function () {
                showMessage('错误提示',"更新失败，请重试！");
            },
            success: function (data) {
            	if (data["success"] == true) {
            		$('#displayTable').datagrid('reload');
            	}else{
            		showMessage('错误提示',"更新失败，请重试！");
            	}
            }
        });
    }
}

function settlementDetail(id) {
	$("#update_dlg").show();
    $("#update_dlg").dialog({
        title: '结算操作流水',
        width: 800,
        height: 600,
        closed: false
    });
    $.get('settlement/showDetail', {id:id}, function (result) {
        if (result["success"] == true) {
        	var status = result["result"].status;
        	if(status=='已结算'){
        		$("#stat_settled").html('已结算');
        		$("#settler").html(result["result"].settler);
        		$("#settleTime").html(result["result"].settleTime);
        		$("#stat_approved").html('已审核');
        		$("#approver").html(result["result"].approver);
        		$("#approveTime").html(result["result"].approveTime);
        		$("#stat_new").html('新申请');
        		$("#creator").html(result["result"].creator);
        		$("#createTime").html(result["result"].createDate);
        	}else if(status=='已审核'){
        		$("#stat_approved").html('已审核');
        		$("#approver").html(result["result"].approver);
        		$("#approveTime").html(result["result"].approveTime);
        		$("#stat_new").html('新申请');
        		$("#creator").html(result["result"].creator);
        		$("#createTime").html(result["result"].createDate);
        	}else if(status=='新申请'){
        		$("#stat_new").html('新申请');
        		$("#creator").html(result["result"].creator);
        		$("#createTime").html(result["result"].createDate);
        	}
        } else {
            showMessage("Error", result["message"]);
        }
    });
}
function doSearchSettlementRecord() {
    var id = $("#id").val();
    var queryParams = $('#displaySettlementRecordTable').datagrid('options').queryParams;
    queryParams.startDate = $('#queryStartDate').datebox("getValue");
    queryParams.endDate = $('#queryEndDate').datebox("getValue");
    $('#displaySettlementRecordTable').datagrid({url: "anchor/querySettlementRecord?id="+id});
}
function exportSettlementRecord(){
	var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.key = $('#key').val();
    queryParams.value = $('#value').val();
    queryParams.status = $('#status').val();
    queryParams.startDate = $('#queryStartDate').val();
    queryParams.endDate = $('#queryEndDate').val();
    
    window.location.href = "settlement/exportSettlement?key="+queryParams.key+"&value="+queryParams.value+
    	"&status="+queryParams.status+"&startDate="+queryParams.startDate+"&endDate="+queryParams.endDate;
}

function getTotal(){
	var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.key = $('#key').val();
    queryParams.value = $('#value').val();
    queryParams.status = $('#status').val();
    queryParams.startDate = $('#queryStartDate').val();
    queryParams.endDate = $('#queryEndDate').val();
    
	$.get('settlement/getTotalSettlement', queryParams, function (result) {
        if (result["success"] == true) {
            $("#totalPoints").html(result["result"]);
            $("#totalCash").html(result["result"]/50);
        } else {
            showMessage("Error", result["message"]);
        }
    });
}
