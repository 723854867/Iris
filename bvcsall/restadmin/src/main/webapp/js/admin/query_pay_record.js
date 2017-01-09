/**
 * Created by busap on 2015/12/23.
 */
var listUrl = "payConsume/recordList";

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
        url: listUrl,
        loadMsg: "数据加载中.....",
        columns: [
            [
                {field: 'time', title: '<span class="columnTitle">时间</span>', width: 90, align: 'center',sortable: true},
                {field: 'transactionNo', title: '<span class="columnTitle">订单号</span>', width: 120, align: 'center'},
                {field: 'userId', title: '<span class="columnTitle">用户id</span>', width: 60, align: 'center'},
                {field: 'username', title: '<span class="columnTitle">昵称</span>', width: 80, align: 'center'},
                {field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 100, align: 'center'},
                {field: 'channel', title: '<span class="columnTitle">充值渠道</span>', width: 80, align: 'center'},
                {field: 'amount', title: '<span class="columnTitle">金额（元）</span>', width: 80, align: 'center',
                    formatter: function (value) {
                        return (value / 100).toFixed(2);
                    }
                },
                {field: 'received', title: '<span class="columnTitle">金币数</span>', width: 80, align: 'center'},
                {field: 'extra', title: '<span class="columnTitle">赠送金币</span>', width: 80, align: 'center',
                    formatter: function (value) {
                        return (value).toFixed(2);
                    }
                },
                {field: 'extraMoney', title: '<span class="columnTitle">赠送现金（元）</span>', width: 80, align: 'center',
                    formatter: function (value) {
                        return (value).toFixed(2);
                    }
                },
                {field: 'status', title: '<span class="columnTitle">充值状态</span>', width: 40, align: 'center',
                    formatter: function (value) {
                        if (value > 1) {
                            return '充值成功';
                        } else {
                            return '待付款';
                        }
                    }
                },
                {field: 'appVersion', title: '<span class="columnTitle">版本</span>', width: 40, align: 'center'},
                {field: 'platform', title: '<span class="columnTitle">平台</span>', width: 40, align: 'center'},
                {field: 'source', title: '<span class="columnTitle">渠道</span>', width: 40, align: 'center'}

            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function (data) {
            $('#displayTable').datagrid('clearSelections');
        	$('#amountSum').html(eval(data).amountSum);
            $('#receivedSum').html(eval(data).receivedSum);
        	$('#extraSum').html(eval(data).extraSum);
        	$('#extraMSum').html(eval(data).extraMSum);
            $('#usersSum').html(eval(data).usersSum);
        },
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });

    $('#start').datetimebox({
        required : false,
        onShowPanel:function(){
            $(this).datetimebox("spinner").timespinner("setValue","00:00:00");
        }
    });

    $('#end').datetimebox({
        required : false,
        onShowPanel:function(){
            $(this).datetimebox("spinner").timespinner("setValue","23:59:59");
        }
    });

});

function doSearch() {
    var paramType = $('#paramType').combobox("getValue");
    var param = $('#param').val();
    var start = $('#start').datebox('getValue');
    var end = $('#end').datebox('getValue');
    var appVersion = $('#appVersion').combobox("getValue");
    var platform = $('#platform').combobox("getValue");
    var source = $('#source').combobox("getValue");
    var channel =[];
    $('input[name="gifts"]:checked').each(function(){
        channel.push($(this).val());
    });
    //var channel = $('#channel').combobox('getValue');
    var isGive = $('#isGive').combobox('getValue');
    var chargeType = $('#chargeType').combobox("getValue");

    if (paramType == 5 && param != 'wx' && param != 'app_store' && param != 'alipay' && param !='wx_pub' ) {
        alert('支付渠道参数错误! 请按以下规则填写: \r\n微信支付 : wx \r\n苹果内购 : app_store \r\n支付宝 : alipay \r\n微信公众号 : wx_pub');
    } else {
        var url = "payConsume/recordList?paramType=" + paramType + "&param=" + param + "&start=" + start + "&end=" + end + "&chargeType=" + chargeType+ "&channel=" + channel
            + "&isGive=" + isGive + "&appVersion=" + appVersion + "&platform=" + platform + "&source=" + source;

        $('#displayTable').datagrid({url: url});
    }
}

function exportExcel() {
    var paramType = $('#paramType').combobox("getValue");
    var param = $('#param').val();
    var start = $('#start').datebox('getValue');
    var end = $('#end').datebox('getValue');
    var chargeType = $('#chargeType').combobox("getValue");
    //var channel = $('#channel').combobox('getValue');
    var appVersion = $('#appVersion').val();
    var platform = $('#platform').val();
    var channel =[];
    $('input[name="gifts"]:checked').each(function(){
        channel.push($(this).val());
    });
    var isGive = $('#isGive').combobox('getValue');
    var url = 'payConsume/exportToExcel?paramType=' + paramType;
    if (param && param.length > 0) {
        url += "&param=" + param;
    }
    if (start != undefined) {
        url += "&start=" + start;
    }
    if (end != undefined) {
        url += "&end=" + end;
    }
    if (chargeType != undefined) {
        url += "&chargeType=" + chargeType;
    }
    if (channel != undefined) {
        url += "&channel=" + channel;
    }
    if (isGive != undefined) {
        url += "&isGive=" + isGive;
    }
    if (appVersion != undefined) {
        url += "&appVersion=" + appVersion;
    }
    if (platform != undefined) {
        url += "&platform=" + platform;
    }

    window.location.href = url;
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
function exportSettlementRecord(){
    var id = $("#id").val();
    var startDate = $('#queryStartDate').datebox("getValue");
    var endDate = $('#queryEndDate').datebox("getValue");
    window.location.href = "anchor/exportSettlementRecordToExcel?id="+id+"&startDate="+startDate+"&endDate="+endDate;
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

