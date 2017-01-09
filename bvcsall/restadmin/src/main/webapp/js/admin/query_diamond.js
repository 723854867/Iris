/**
 * Created by huoshanwei on 2015/12/23.
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
        url: "diamond/queryDiamondList",
        onClickCell: onClickCell,
        onAfterEdit: function (rowIndex, rowData) {
            $.ajax({
                url: 'diamond/updateWeight',
                type: 'post',
                data: {'id': rowData.id, 'weight': rowData.weight},
                async: false, //默认为true 异步
                error: function () {
                    alert("数据保存失败！");
                    doSearch();
                },
                success: function (result) {
                    if (result == "error") {
                        alert("数据保存失败！");
                        doSearch();
                    }
                }
            });

        },
        rowStyler: function (index, row) {
            if (row.state == 0) {
                return 'color:red;';
            }
        },
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center', sortable: true},
                {field: 'name', title: '<span class="columnTitle">金币名称</span>', width: 120, align: 'center'},
                {
                    field: 'diamondIconUrl',
                    title: '<span class="columnTitle">金币icon</span>',
                    width: 120,
                    align: 'center',
                    formatter: function (value, row) {
                        var img = '<img src="/restadmin/download' + value + '" style="width:60px;height:60px;">';
                        return img;
                    }
                },
                {
                    field: 'price',
                    title: '<span class="columnTitle">价格（元）</span>',
                    width: 100,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'payMethod', title: '<span class="columnTitle">支付方式</span>', width: 80, align: 'center',
                    formatter: function (value) {
                        switch (value) {
                            case 0:
                                return "苹果支付";
                            case 1:
                                return "其它";
                            case 2:
                                return "大额支付";
                            case 3:
                                return "应用宝支付";
                        }
                    }
                },
                {
                    field: 'diamondCount',
                    title: '<span class="columnTitle">金币数量</span>',
                    width: 80,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'isGive', title: '<span class="columnTitle">是否赠送</span>', width: 70, align: 'center',
                    formatter: function (value) {
                        switch (value) {
                            case 1:
                                return "赠送";
                            case 0:
                                return "不赠送";
                        }
                    }
                },
                {
                    field: 'giveCount',
                    title: '<span class="columnTitle">赠送数量</span>',
                    width: 100,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'state', title: '<span class="columnTitle">状态</span>', width: 70, align: 'center',
                    formatter: function (value) {
                        switch (value) {
                            case 0:
                                return "下架";
                            case 1:
                                return "上架";
                        }
                    }
                },
                {
                    field: 'weight',
                    title: '<span class="columnTitle">权重</span>',
                    width: 100,
                    align: 'center',
                    sortable: true,
                    editor: 'text'
                },
                {
                    field: 'createDateStr',
                    title: '<span class="columnTitle">创建时间</span>',
                    width: 100,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="dialogUpdate(' + row.id + ')" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        if (row.state == 1) {
                            var stateStr = '<a class="easyui-linkbutton" onclick="updateState(' + row.id + ',0)" href="javascript:;" style="width:80px;height: 25px;" title="下架"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">下架</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                        } else {
                            var stateStr = '<a class="easyui-linkbutton" onclick="updateState(' + row.id + ',1)" href="javascript:;" style="width:80px;height: 25px;" title="上架"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">上架</span><span class="l-btn-icon icon-add">&nbsp;</span></span></a>';
                        }
                        return editStr + " " + stateStr;
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
    queryParams.id = $('#queryId').val();
    queryParams.name = $('#queryName').val();
    queryParams.isGive = $('#queryIsGive').combobox("getValue");
    queryParams.payMethod = $('#queryPayMethod').combobox("getValue");
    queryParams.state = $('#queryState').combobox("getValue");
    $('#displayTable').datagrid({url: "diamond/queryDiamondList"});
}

function insertDialog() {
    $("#insert_dlg").dialog('open').dialog('setTitle', "添加");
}

function doInsertTemplate() {
    $("#nameConfirm").text($("#name").val());
    $("#priceConfirm").text($("#price").val());
    var method = $("#payMethod").combobox("getValue");
    if(method == 0){
        $("#payMethodConfirm").text("苹果");
    }else if(method == 1){
        $("#payMethodConfirm").text("其它");
    }else if(method == 2){
        $("#payMethodConfirm").text("大额支付");
    }

    $("#diamondCountConfirm").text($("#diamondCount").val());
    $("#isGiveConfirm").text($("input[name='isGive']:checked").val() == 0 ? "不赠送" : "赠送");
    $("#giveCountConfirm").text($("#giveCount").val());
    $("#stateConfirm").text($("#state").combobox("getValue") == 0 ? "下架" : "上架");
    $("#weightConfirm").text($("#weight").val());
    $("#confirm_dlg").dialog('open').dialog('setTitle', "确认");

}

function doInsert() {
    $('#confirm_dlg').dialog('close');
    $('#insertForm').form('submit', {
        url: "diamond/insertDiamond",
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
    var url = 'diamond/updateDiamondTemplate?id=' + value;
    initWindow(title, url, 450, 500);
}

function doUpdate() {
    $('#updateForm').form('submit', {
        url: "diamond/updateDiamond",
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

function updateState(id, value) {
    $.ajax({
        url: 'diamond/updateState',
        data: {'id': id, 'value': value},
        type: "post",
        dataType: "json",
        success: function (result) {
            showMessage("提示信息", result.resultMessage);
            doSearch();
        }
    });
}

function giveCountDisplay(value) {
    if (value == 0) {
        $("input[name='giveCount']").val("");
        $("#giveCountTr").hide();
    } else {
        $("#giveCountTr").show();
    }
}

$.extend($.fn.datagrid.methods, {
    editCell: function (jq, param) {
        return jq.each(function () {
            var opts = $(this).datagrid('options');
            var fields = $(this).datagrid('getColumnFields', true).concat($(this).datagrid('getColumnFields'));
            for (var i = 0; i < fields.length; i++) {
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field) {
                    col.editor = null;
                }
            }
            $(this).datagrid('beginEdit', param.index);
            for (var i = 0; i < fields.length; i++) {
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor = col.editor1;
            }
        });
    }
});

var editIndex = undefined;
function endEditing() {
    if (editIndex == undefined) {
        return true
    }
    if ($('#displayTable').datagrid('validateRow', editIndex)) {
        $('#displayTable').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}
function onClickCell(index, field) {
    if (endEditing()) {
        $('#displayTable').datagrid('selectRow', index)
            .datagrid('editCell', {index: index, field: field});
        editIndex = index;
    }
}