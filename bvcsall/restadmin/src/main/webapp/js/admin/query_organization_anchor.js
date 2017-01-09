/**
 * Created by huoshanwei on 2016/3/22.
 */
$(function () {
    $('#displayAnchorTable').datagrid({
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
        url: "organization/queryOrganizationAnchorList",
        queryParams: {
            organizationId: $("#organizationId").val()
        },
        columns: [
            [
                {field: 'userId', title: '<span class="columnTitle">用户ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 120, align: 'center'},
                {field: 'realName', title: '<span class="columnTitle">姓名</span>', width: 120, align: 'center'},
                {
                    field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 80, align: 'center'
                },
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
                        var certificateNumber = row.certificateNumber == null ? "" : "/" + row.certificateNumber;
                        return certificateName + certificateNumber;
                    }
                },
                {
                    field: 'bankName',
                    title: '<span class="columnTitle">开户行</span>',
                    width: 100,
                    align: 'center'
                },
                {
                    field: 'branchBankName',
                    title: '<span class="columnTitle">支行</span>',
                    width: 100,
                    align: 'center'
                },
                {
                    field: 'basicSalary',
                    title: '<span class="columnTitle">底薪</span>',
                    width: 100,
                    align: 'center'
                },
                {
                    field: 'bankNumber',
                    title: '<span class="columnTitle">银行卡号</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'createDate',
                    title: '<span class="columnTitle">创建时间</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" onclick="dialogUpdate(' + row.userId + ')" href="javascript:;" style="width:80px;height: 25px;" title="修改"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">修改</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var deleteStr = '<a class="easyui-linkbutton" onclick="removeAnchorFromOrg(' + row.userId + ')" href="javascript:;" style="width:80px;height: 25px;" title="移除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">移除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                        return editStr + " " + deleteStr;
                    }
                }
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function () {
            $('#displayAnchorTable').datagrid('clearSelections');
        },
        pageSize: 1000,
        pageList: [1000, 2000, 3000, 5000, 10000],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
});

function doSearch() {
    var queryParams = $('#displayAnchorTable').datagrid('options').queryParams;
    $('#displayAnchorTable').datagrid({url: "organization/queryOrganizationAnchorList"});
}

function dialogUpdate(value) {
    var title = '请求明细';
    var url = 'organization/updateOrgAnchorTemplate?id=' + value;
    initWindow(title, url, 500, 410);
}

function doUpdate() {
    $('#updateForm').form('submit', {
        url: "organization/updateOrgAnchor",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#dialogWindow').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function removeAnchorFromOrg(id) {
    if(confirm("确定要执行移除操作吗？")){
        $.ajax({
            url: 'organization/removeAnchorFromOrg',
            data: {'userId': id},
            type: "post",
            dataType: "json",
            success: function (result) {
                showMessage("提示信息", result.resultMessage);
                doSearch();
            }
        });
    }
}

function exportOrgAnchor(organizationId){
    window.location.href = "organization/exportOrganizationAnchor?organizationId=" + organizationId;
}
