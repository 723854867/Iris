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
        url: "gift/queryGiftList?state=1",
        onClickCell: onClickCell,
        onAfterEdit: function (rowIndex, rowData,changes) {
            $.ajax({
                url: 'gift/updateWeight',
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
                {field: 'name', title: '<span class="columnTitle">礼物名称</span>', width: 120, align: 'center'},
                {
                    field: 'giftIconUrl', title: '<span class="columnTitle">礼物icon</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        var img = '<img src="/restadmin/download' + value + '" style="width:60px;height:60px;">';
                        return img;
                    }
                },
                {
                    field: 'giftPurpose', title: '<span class="columnTitle">礼物用途</span>', width: 80, align: 'center',
                    formatter: function (value) {
                        switch (value) {
                            case 1:
                                return "收视榜";
                            case 2:
                                return "人气榜";
                            case 3:
                                return "直播";
                            case 4:
                                return "其它";
                            default :
                                return "收视榜";
                        }
                    }
                },
                {
                    field: 'effectType', title: '<span class="columnTitle">效果类型</span>', width: 80, align: 'center',
                    formatter: function (value) {
                        switch (value) {
                            case 0:
                                return "无动画";
                            case 1:
                                return "雪花";
                            case 2:
                                return "心跳放大";
                            case 3:
                                return "位移";
                            case 5:
                                return "雪花停留";
                            case 6:
                                return "抛物线";
                            case 7:
                                return "跑车";
                            case 8:
                                return "城堡岛屿";
                            case 9:
                                return "游轮";
                            default :
                                return "无动画";
                        }
                    }
                },
                {
                    field: 'effectFileUrl', title: '<span class="columnTitle">效果文件</span>', width: 80, align: 'center',
                    formatter: function (value, row) {
                        var img = "";
                        if(value != null){
                            img = '<img src="/restadmin/download' + value + '" style="width:60px;height:60px;">';
                        }
                        return img;
                    }
                },
                {
                    field: 'price',
                    title: '<span class="columnTitle">价格（拍币）</span>',
                    width: 100,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'point',
                    title: '<span class="columnTitle">点数（拍豆）</span>',
                    width: 80,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'state', title: '<span class="columnTitle">状态</span>', width: 70, align: 'center',
                    formatter: function (value) {
                        switch (value) {
                            case 0:
                                return "下线";
                            case 1:
                                return "上线";
                        }
                    }
                },
                {
                    field: 'weight',
                    title: '<span class="columnTitle">权重</span>',
                    width: 80,
                    align: 'center',
                    sortable: true,
                    editor: 'text'
                },
                {
                    field: 'markerState', title: '<span class="columnTitle">标记状态</span>', width: 100, align: 'center'
                    /*formatter: function (value) {
                     if (value == "new"){
                     return "new";
                     }
                     }*/
                },
                {
                    field: 'isFree', title: '<span class="columnTitle">是否免费</span>', width: 100, align: 'center',
                    formatter: function (value) {
                        if (value == "0") {
                            return "不免费";
                        } else {
                            return "免费";
                        }
                    }
                },
                {
                    field: 'freeCount',
                    title: '<span class="columnTitle">免费次数</span>',
                    width: 100,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'createDateStr',
                    title: '<span class="columnTitle">创建时间</span>',
                    width: 100,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'loopSupport', title: '<span class="columnTitle">是否连发</span>', width: 70, align: 'center',
                    formatter: function (value) {
                        if (value == "0") {
                            return "不支持";
                        } else {
                            return "支持";
                        }
                    }
                },
                {
                    field: 'isExclusive', title: '<span class="columnTitle">是否专属</span>', width: 70, align: 'center',
                    formatter: function (value) {
                        if (value == "0") {
                            return "不是";
                        } else {
                            return "是";
                        }
                    }
                },
                {
                    field: 'screenshotNumber', title: '<span class="columnTitle">截屏次数</span>', width: 70, align: 'center',
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 200, align: 'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="dialogUpdate(' + row.id + ')" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        if (row.markerState != "new") {
                            var newStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="updateState(' + row.id + ',2,\'new\')" style="width:80px;height: 25px;" title="设为new"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">设为new</span><span class="l-btn-icon icon-new">&nbsp;</span></span></a>';
                        } else {
                            var newStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="updateState(' + row.id + ',2,\'normal\')" style="width:80px;height: 25px;" title="设为正常"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">设为正常</span><span class="l-btn-icon">&nbsp;</span></span></a>';
                        }

                        if (row.state == 1) {
                            var stateStr = '<a class="easyui-linkbutton" onclick="updateState(' + row.id + ',1,0)" href="javascript:;" style="width:80px;height: 25px;" title="下架"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">下架</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                        } else {
                            var stateStr = '<a class="easyui-linkbutton" onclick="updateState(' + row.id + ',1,1)" href="javascript:;" style="width:80px;height: 25px;" title="上架"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">上架</span><span class="l-btn-icon icon-add">&nbsp;</span></span></a>';
                        }
                        return editStr + " " + newStr + " " + stateStr;
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
    queryParams.giftPurpose = $('#queryGiftPurpose').combobox("getValue");
    queryParams.effectType = $('#queryEffectType').combobox("getValue");
    queryParams.state = $('#queryState').combobox("getValue");
    queryParams.markerState = $('#queryMarkerState').combobox("getValue");
    queryParams.isFree = $('#queryIsFree').combobox("getValue");
    queryParams.loopSupport = $('#queryLoopSupport').combobox("getValue");
    queryParams.isExclusive = $('#queryIsExclusive').combobox("getValue");
    queryParams.screenshotSupport = $('#queryScreenshotSupport').combobox("getValue");
    $('#displayTable').datagrid({url: "gift/queryGiftList"});
}

function insertDialog() {
    $("#insert_dlg").dialog('open').dialog('setTitle', "添加");
}

function doInsert() {
    $('#insertForm').form('submit', {
        url: "gift/insertGift",
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
    var url = 'gift/updateGiftTemplate?id=' + value;
    initWindow(title, url, 500, 650);
}

function doUpdate() {
    $('#updateForm').form('submit', {
        url: "gift/updateGift",
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

function updateState(id, type, value) {
    $.ajax({
        url: 'gift/updateState',
        data: {'id': id, 'type': type, 'value': value},
        type: "post",
        dataType: "json",
        success: function (result) {
            showMessage("提示信息", result.resultMessage);
            doSearch();
        }
    });
}

function freeCountDisplay(value) {
    if (value == 0) {
        $("input[name='freeCount']").val("");
        $("#freeCountTr").hide();
    } else {
        $("#freeCountTr").show();
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