/**
 * Created by huoshanwei on 2015/10/29.
 */
var datagridId = "#tt";
var listUrl = "feedback/queryFeedBackList";
$(function () {
    $(datagridId).datagrid({
        nowrap: true, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: false,
        singleSelect: false, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        columns: [[
            {field: 'content', title: '消息内容', width: 100, align: 'center'},
            {field: 'createDateStr', title: '反馈时间', width: 100, align: 'center'},
            {field: 'creatorId', title: '用户id', width: 100, align: 'center'},
            {field: 'contact', title: '联系方式', width: 100, align: 'center'},
            {field: 'dataFrom', title: '来源', width: 100, align: 'center'},
            {field: 'appVersion', title: '版本号', width: 100, align: 'center'},
            {
                field: 'status', title: '状态', width: 100, align: 'center',
                formatter: function (value, row) {
                    if(value != null && value != ""){
                        if (value == 0) {
                            return "未跟踪";
                        } else if (value == 1) {
                            return "已跟踪";
                        } else {
                            return "已处理";
                        }
                    }else{
                        return "未跟踪";
                    }
                }
            },
            {
                field: 'modify', title: '操作', width: 100, align: 'center',
                formatter: function (value, row) {
                    var mailStr = '<a class="easyui-linkbutton" href="javascript:;" style="width:80px;height: 25px;" type="button" onclick="sendRowMailDialog(' + row.id + ')" title="mail负责人" >mail负责人</a>';
                    return mailStr;
                }
            }
        ]],
        onLoadSuccess: function () {
            $(datagridId).datagrid('clearSelections');
        },
        toolbar: "#tb",
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
});

function doSearch() {
    var queryParams = $(datagridId).datagrid('options').queryParams;
    queryParams.content = $('#content').val();
    queryParams.dataFrom = $('#dataFrom').combobox('getValue');
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');
    $(datagridId).datagrid({url: listUrl});
}

function sendMailDialog() {
    var ids = [];
    var rows = $('#tt').datagrid('getSelections');
    for (var i = 0; i < rows.length; i++) {
        ids.push(rows[i].id);
    }
    if (ids.length > 0) {
        $("#update-ok").attr("onclick", "sendMail(1)");
        $("#dialog").show();
        $("#dialog").dialog();
    }else{
        showMessage("错误信息", "对不起，请选择有效的行!");
    }
}

function rtrim(s) {
    var lastIndex = s.lastIndexOf(',');
    if (lastIndex > -1) {
        s = s.substring(0, lastIndex);

    }
    return s;
}

function sendMail(type) {
    if (type == 1) {
        var feedbackIds = [];
        var rows = $('#tt').datagrid('getSelections');
        for (var i = 0; i < rows.length; i++) {
            feedbackIds += rows[i].id + ",";
        }
        feedbackIds = rtrim(feedbackIds);
    } else {
        feedbackIds = $("#id").val();
    }
    var emailList = [];
    $('input[name="email"]:checked').each(function () {
        emailList += $(this).val() + ";";
    });
    var newEmail = $("#new_email").val();
    if (emailList == "" && newEmail == "") {
        showMessage("错误信息", "请选择一个邮箱地址或输入一个新的邮箱！");
    } else {
        if (newEmail != "") {
            var reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
            if (!reg.test(newEmail)) {
                showMessage("错误信息", "对不起，您输入的邮箱地址格式不正确!");
            }
        }
        closeDialog();
        $.ajax({
            url: 'feedback/sendMail',
            data: {'emailList': emailList, 'feedbackIds': feedbackIds, 'newEmail': newEmail},
            type: "post",
            dataType: "json",
            success: function (result) {
                showMessage("提示信息", result.resultMessage);
                doSearch();
            }
        });
    }
}

function sendRowMailDialog(id) {
    if (id > 0) {
        $("#update-ok").attr("onclick", "sendMail(2)");
        $("#id").val(id);
        $("#dialog").show();
        $("#dialog").dialog();
    } else {
        showMessage("错误提示", "请选择有效的一行！");
    }
}

function closeDialog() {
    $('#dialog').dialog('close');
}

function exportFeedBackToExcel(){
    content = $('#content').val();
    dataFrom = $('#dataFrom').combobox('getValue');
    startTime = $('#startTime').datebox('getValue');
    endTime = $('#endTime').datebox('getValue');
    window.location.href = baseUrl + 'feedback/exportFeedBackToExcel?content='+content+'&dataFrom='+dataFrom+'&startTime='+startTime+'&endTime='+endTime;
}