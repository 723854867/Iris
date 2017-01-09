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
        url: "anchor/queryAnchorAuditingList?status=0",
        columns: [
            [
                {field: 'realName', title: '<span class="columnTitle">姓名</span>', width: 40, align: 'center'},
                {
                    field: 'sex', title: '<span class="columnTitle">性别</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if (value == 1) {
                            return "男";
                        } else if (value == 0) {
                            return "女";
                        } else {
                            return "未知";
                        }
                    }
                },
                {field: 'phone', title: '<span class="columnTitle">手机号码</span>', width: 120, align: 'center'},
                {
                    field: 'certificateType',
                    title: '<span class="columnTitle">证件/证件号码</span>',
                    width: 120,
                    align: 'center',
                    //1：身份证，2：护照，3：台胞证
                    formatter: function (value, row) {
                        var certificateName = "";
                        if (value == 1) {
                            certificateName = "身份证";
                        } else if (value == 2) {
                            certificateName = "护照";
                        } else {
                            certificateName = "台胞证";
                        }
                        /** @namespace row.certificateNumber */
                        var certificateNumber = row.certificateNumber == null ? "" : "/" + row.certificateNumber;
                        return certificateName + certificateNumber;
                    }
                },
                {
                    field: 'bankName', title: '<span class="columnTitle">银行</span>', width: 80, align: 'center'
                },
                {
                    field: 'bankNumber', title: '<span class="columnTitle">卡号</span>', width: 80, align: 'center'
                },
                {
                    field: 'bankProvince',
                    title: '<span class="columnTitle">开户地</span>',
                    width: 100,
                    align: 'center',
                    formatter: function (value, row) {
                        if(row.bankProvince == null){
                            row.bankProvince = "";
                        }
                        if(row.bankCity == null){
                            row.bankCity = "";
                        }
                        if(row.bankAddress == null){
                            row.bankAddress = "";
                        }
                        return row.bankProvince+" "+row.bankCity+" "+row.bankAddress;
                    }
                },
                {
                    field: 'createDateStr',
                    title: '<span class="columnTitle">申请时间</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'modifyDateStr',
                    title: '<span class="columnTitle">最后审核时间</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'status',
                    title: '<span class="columnTitle">状态</span>',
                    width: 80,
                    align: 'center',
                    formatter: function (value, row) {
                        if (value == -1) {
                            return "认证失败";
                        } else if (value == 0) {
                            return "待审核";
                        } else {
                            return "已通过";
                        }
                    }
                },
                {
                    field: 'rejectReason',
                    title: '<span class="columnTitle">失败原因</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var detail = '<a class="easyui-linkbutton" href="javascript:;" onclick="queryDetail(\'' + row.picOne + '\',\'' + row.picTwo + '\',\'' + row.picThree + '\')" style="width:80px;height: 25px;" title="详情"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">详情</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        var allow = '<a class="easyui-linkbutton" href="javascript:;" onclick="allowAnchorLiving(' + row.id + ')" style="width:80px;height: 25px;" title="通过"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">通过</span><span class="l-btn-icon icon-ok">&nbsp;</span></span></a>';
                        var refuse = "";
                        if (row.status != 1) {
                            refuse = '<a class="easyui-linkbutton" href="javascript:;" onclick="rejectAnchorLivingDialog(' + row.id + ')" style="width:80px;height: 25px;" title="拒绝"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">拒绝</span><span class="l-btn-icon icon-no">&nbsp;</span></span></a>';
                        }
                        return detail + " " + allow + " " + refuse;
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
});
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.phone = $('#phone').val();
    queryParams.status = $('#status').combobox("getValue");
    $('#displayTable').datagrid({url: "anchor/queryAnchorAuditingList"});
}

//查看详情
function queryDetail(pic1, pic2, pic3) {
    $("#detail_dlg").dialog('open').dialog('setTitle', "查看详情");
    if (pic1 == "null") {
        $("#pic1_prompt").html("证件图片信息不存在！");
    }
    if (pic2 == "null") {
        $("#pic2_prompt").html("证件图片信息不存在！");
    }
    if (pic3 == "null") {
        $("#pic3_prompt").html("证件图片信息不存在！");
    }
    $("#pic1").attr("src", "/restadmin/download" + pic1);
    $("#pic2").attr("src", "/restadmin/download" + pic2);
    $("#pic3").attr("src", "/restadmin/download" + pic3);
}

//更新状态 通过
function allowAnchorLiving(id) {
    if (confirm("确定要执行通过操作吗？")) {
        $.ajax({
            url: "anchor/allowAnchorLiving",
            data: {'id': id},
            type: "post",
            dataType: "json",
            success: function (result) {
                showMessage("提示信息", result.resultMessage, 3000);
                doSearch();
            }
        });
    }
}

function rejectAnchorLivingDialog(id) {
    $("#reject_dlg").dialog('open').dialog('setTitle', "审核不通过");
    $("#id").val(id);
}

function rejectAnchorLiving() {
    $('#rejectForm').form('submit', {
        url: "anchor/rejectAnchorLiving",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#reject_dlg').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function checkRejectReason() {
    var rejectReason = "";
    $("#reject input[type=checkbox]").each(function () {
        if (this.checked) {
            rejectReason += $(this).val() + ";";
        }
    });
    $("#rejectReason").val(rejectReason);
}

