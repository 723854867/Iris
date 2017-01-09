/**
 * Created by busap on 2015/10/9.
 */

function closeDialog(value) {
    if (value == 1) {
        $('#dialog').dialog('close');
    } else {
        $('#updateDialog').dialog('close');
    }
}

function doUpdate(type) {
    if (type == 1) {
        var id = $('#id').val();
        //alert(id);
        var stat = $('#stat').combobox("getValue");
        var vstat = $('#vstat').combobox("getValue");
        $.ajax({
            url: 'user/doEditStateAndVstate',
            data: {'id': id, 'stat': stat, 'vipStat': vstat},
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.resultCode == "ok") {
                    $('#dialog').dialog('close');
                    showMessage("成功提示", "更新成功！");
                    doSearch();
                } else {
                    showMessage("错误提示", "数据更新失败，请稍后重试！");
                }
            }
        });
    } else if (type == 2) {
        var url = 'user/doUpdateRuser';
        $('#insertForm').form('submit', {
            url: url,
            success: function (response) {
                var parsedJson = jQuery.parseJSON(response);
                if (parsedJson.resultCode == "ok") {
                    showMessage("提示信息", parsedJson.resultMessage);
                    $('#updateDialog').dialog('close');
                    doSearch();
                } else {
                    showMessage("错误信息", parsedJson.resultMessage);
                }
            }
        });
    } else {
        var url = "videoUploader/doInsertRuser";
        $('#insertForm').form('submit', {
            url: url,
            success: function (response) {
                var parsedJson = jQuery.parseJSON(response);
                if (parsedJson.resultCode == "ok") {
                    showMessage("提示信息", parsedJson.resultMessage);
                    $('#updateDialog').dialog('close');
                    doSearch();
                } else {
                    showMessage("错误信息", parsedJson.resultMessage);
                }
            }
        });
    }
}

function deleteRuser(value) {
    if (confirm("确定要删除此用户吗？")) {
        $.ajax({
            url: 'user/deleteRuser',
            data: {'id': value},
            type: "post",
            dataType: "json",
            success: function (result) {
                showMessage("提示信息", result.resultMessage);
                doSearch();
            }
        });
    }
}

function editStateAndVstate(value) {
    $.ajax({
        url: 'user/getRuserStateInfo',
        data: {'id': value},
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.resultCode == "ok") {
                $("#update-ok").attr("onclick", "doUpdate(1)");
                $("#update-cancel").attr("onclick", "closeDialog(1)");
                $('#stat').combobox('select', result.state);
                $('#vstat').combobox('select', result.vstate);
                //alert(value);
                //alert($('#id').val());
                $("#dialog").show();
                $("#dialog").dialog();
                $('#id').val(value);
            } else {
                showMessage("错误提示", "获取数据失败，请稍后重试！");
            }
        }
    });
}

function addMajiaUser() {
    $("#update_signature").val("");
    $("#update_name").val("");
    $("#imgShow").removeAttr("src", "");
    $("#update_id").val("");
    //$('#myform')[0].reset();
    $("#update-ok").attr("onclick", "doUpdate(3)");
    $("#update-cancel").attr("onclick", "closeDialog(3)");
    $("#updateDialog").show();
    $("#updateDialog").dialog();
}

function updateRuser(value) {
    $.ajax({
        url: 'user/getRuserStateInfo',
        data: {'id': value},
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.resultCode == "ok") {
                $("#update-ok").attr("onclick", "doUpdate(2)");
                $("#update-cancel").attr("onclick", "closeDialog(2)");
                $("#update_name").val(result.name);
                $("#update_signature").val(result.signature);
                if (result.sex == 1) {
                    $("#update_sex1").attr("checked", "checked");
                } else if (result.sex == 2) {
                    $("#update_sex2").attr("checked", "checked");
                } else {
                    $("#update_sex0").attr("checked", "checked");
                }
                $('#update_id').val(result.id);
                $('#update_stat').combobox('select', result.state);
                $('#update_vstat').combobox('select', result.vstate);
                $('#update_rankable').combobox('select', result.rankAble);
                $("#imgShow").attr("src", "/restadmin/download/" + result.pic);
                $("#updateDialog").show();
                $("#updateDialog").dialog();
            } else {
                showMessage("错误提示", "获取数据失败，请稍后重试！");
            }
        }
    });
}

$(function () {
    new uploadPreview({UpBtn: "update_pic", DivShow: "imgdiv", ImgShow: "imgShow"});
})
var listUrl = "videoUploader/queryMajiaUserList";
$(function () {
    $('#displayTable').datagrid({
        nowrap: true, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: true,
        singleSelect: false, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        columns: [
            [
                {field: 'ck', width: 100, checkbox: true},
                {field: 'name', title: '<span class="columnTitle">马甲名</span>', width: 120, align: 'center'},
                {field: 'username', title: '<span class="columnTitle">用户名</span>', width: 120, align: 'center'},
                {
                    field: 'signature',
                    title: '<span class="columnTitle">个性签名</span>',
                    width: 80,
                    align: 'center'
                },
                {field: 'addr', title: '<span class="columnTitle">所在地</span>', width: 80, align: 'center'},
                {
                    field: 'videoCount',
                    title: '<span class="columnTitle">发布视频数</span>',
                    width: 120,
                    align: 'center'
                },
                {
                    field: 'stat', title: '<span class="columnTitle">状态</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if (value == 0) {
                            return "已激活";
                        } else if (value == 1) {
                            return "禁言";
                        } else {
                            return "封号";
                        }
                    }
                },
                {
                    field: 'vipStat', title: '<span class="columnTitle">等级</span>', width: 100, align: 'center',
                    formatter: function (value) {
                        if (value == 0) {
                            return "普通";
                        } else if (value == 1) {
                            return "蓝V";
                        } else if (value == 2) {
                            return "黄V";
                        } else {
                            return "绿V";
                        }
                    }
                },
                {
                    field: 'rankAble',
                    title: '<span class="columnTitle">排行榜</span>',
                    width: 100,
                    align: 'center',
                    formatter: function (value, row) {
                        if (value == 1) {
                            return "<a class='easyui-linkbutton' href='javascript:;' onclick='rankAbleSetting(" + row.id + ",0)'>允许上榜</a>";
                        } else {
                            return "<a class='easyui-linkbutton' href='javascript:;' onclick='rankAbleSetting(" + row.id + ",1)'>禁止上榜</a>";
                        }
                    }
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var detailStr = '<a class="easyui-linkbutton" href="video/userdetail?uid=' + row.id + '" style="width:80px;height: 25px;" title="查看"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">查看</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="updateRuser(' + row.id + ')" style="width:80px;height: 25px;" title="修改"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">修改</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteRuser(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                        var stateStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="editStateAndVstate(' + row.id + ')" style="width:80px;height: 25px;" title="状态等级"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">状态等级</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        return detailStr + " " + editStr + " " + stateStr + " " + deleteStr;
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
    queryParams.name = $('#query_name').val();
    queryParams.stat = $('#query_stat').combobox("getValue");
    queryParams.vipStat = $('#query_vstat').combobox("getValue");
    queryParams.rankAble = $('#query_rankable').combobox("getValue");
    queryParams.sex = $('#query_sex').combobox("getValue");
    queryParams.addr = $('#query_addr').val();
    queryParams.startCount = $('#startCount').val();
    queryParams.endCount = $('#endCount').val();
    $('#displayTable').datagrid({url: listUrl});
}

function rankAbleSetting(uid, able) {
    if (confirm("您确定要执行此操作吗？")) {
        $.get("videoUploader/rankAble", {uid: uid, able: able}, function (result) {
            if (result["success"] == true) {
                doSearch(); // reload the user data
            } else {
                showMessage("Error", result["message"]);
            }
        });
    }
}

function batchSettingRankAble(status) {
    var row = $("#displayTable").datagrid('getSelections');
    var idStr = "";
    for (var i = 0; i < row.length; i++) {
        idStr += row[i]["id"] + ","
    }
    var ids = idStr.substring(0, idStr.length - 1);
    if (ids != "" && ids != null) {
        if (confirm("您确定要执行此操作吗？")) {
            $.ajax({
                url: 'user/batchSettingRankAble',
                data: {'ids': ids,'rankAble':status},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.resultCode == "success") {
                        doSearch();
                    } else {
                        showMessage("错误提示", "更新失败，请重试！");
                    }
                }
            });
        }
    }else{
        showMessage("错误提示", "请至少选择一行！");
    }
}

function importDialog() {
    $("#importDialog").show();
    $("#importDialog").dialog();
}

function doImport() {
    if (confirm("确定要执行批量导入操作吗？")) {
        var url = "user/batchImportMajiaUserInfo";
        $('#importForm').form('submit', {
            url: url,
            success: function (response) {
                var parsedJson = jQuery.parseJSON(response);
                if (parsedJson.resultCode == "success") {
                    showMessage("提示信息", parsedJson.resultMessage);
                    $('#importDialog').dialog('close');
                    $.messager.progress('close');
                    doSearch();
                } else {
                    $.messager.progress('close');
                    showMessage("错误信息", parsedJson.resultMessage);
                }
            },
            onSubmit:function(){
                $.messager.progress({
                    title: '批量导入马甲用户',
                    msg: '正在导入中，貌似时间比较长，出去透透气吧...'
                });
            }
        });
    }
}
