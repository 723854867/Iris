/**
 * Created by huoshanwei on 2016/3/22.
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
        url: "organization/queryOrganizationList",
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
                {field: 'orgName', title: '<span class="columnTitle">机构名称</span>', width: 120, align: 'center'},
                {
                    field: 'orgType', title: '<span class="columnTitle">类型</span>', width: 80, align: 'center',
                    formatter: function (value) {
                        if (value == '1') {
                            return "返点";
                        } else  if (value == '2'){
                            return "底薪";
                        }else{
                            return value;
                        }
                    }
                },
                {field: 'leader', title: '<span class="columnTitle">负责人</span>', width: 120, align: 'center'},
                {
                    field: 'leaderPhone', title: '<span class="columnTitle">负责人手机号</span>', width: 80, align: 'center'
                },
                {
                    field: 'anchorCount',
                    title: '<span class="columnTitle">主播人数</span>',
                    width: 100,
                    align: 'center'
                },
                {
                    field: 'createDateStr',
                    title: '<span class="columnTitle">创建时间</span>',
                    width: 80,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" onclick="dialogUpdate(' + row.id + ')" href="javascript:;" style="width:80px;height: 25px;" title="修改"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">修改</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var deleteStr = '<a class="easyui-linkbutton" onclick="deleteOrganization(' + row.id + ')" href="javascript:;" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                        var managerStr = '<a class="easyui-linkbutton" href="organization/forwardManageAnchor?organizationId=' + row.id + '" style="width:80px;height: 25px;" title="管理"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">管理</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        return editStr + " " + deleteStr + " "+managerStr;                    }
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
    queryParams.key = $('#key').combobox("getValue");
    queryParams.useKey = $('#useKey').val();
    $('#displayTable').datagrid({url: "organization/queryOrganizationList"});
}

function insertDialog() {
    $("#insert_dlg").dialog('open').dialog('setTitle', "添加");
}

function doInsert() {
    $('#insertForm').form('submit', {
        url: "organization/insertOrganization",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#insert_dlg').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function dialogUpdate(value) {
    var title = '请求明细';
    var url = 'organization/updateOrganizationTemplate?id=' + value;
    initWindow(title, url, 500, 300);
}

function doUpdate() {
    $('#updateForm').form('submit', {
        url: "organization/updateOrganization",
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

function deleteOrganization(value){
    if(confirm("确定要执行删除操作吗？")){
        $.ajax({
            url: 'organization/deleteOrganization',
            data: {'id': value},
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.resultCode == "success") {
                    showMessage("提示信息", result.resultMessage);
                    doSearch()
                } else {
                    showMessage("错误提示", result.resultMessage);
                }
            }
        });
    }
}