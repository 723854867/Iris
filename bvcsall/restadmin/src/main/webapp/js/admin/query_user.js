/**
 * Created by huoshanwei on 2015/10/9.
 */

$(document).ready(function () {

    $("#queryRegPlatform").combobox({
        onChange: function (n, o) {
            $("#selectContent").empty();
            var content = "<select id=\"queryRegPlatformChannel\" class=\"easyui-combobox\">";
            content += "<option value=\"\">" + "请选择" + "</option>";

            var channelStr = "";

            if (n == 'android') {
                channelStr = $("#androidChannel").val();
            } else if (n == 'ios') {
                channelStr = $("#iosChannel").val();
            } else if (n == 'h5') {
                channelStr = $("#h5Channel").val();
            }

            str = channelStr.split(",");
            for (i = 0; i < str.length; i++) {
                content += "<option value=\"" + str[i] + "\">" + str[i] + "</option>";
            }
            content += "</select>";
            $("#selectContent").append(content);
            $("#queryRegPlatformChannel").combobox({});
        }
    });

});

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
        var vstat = $('#vstat').combobox("getValue");
        var allowPublish = $('#allow_publish').combobox("getValue");
        var vipWeight = $('#vipWeight').val();
        var liveWeight = $('#liveWeight').val();
        var orgId = $('#organizationId').combobox("getValue");
        /*var isBlacklist = $('#isBlacklist').combobox("getValue");*/
        var liveType = $('#liveType').combobox("getValue");
        var recommendBit = $('#recommendBit').combobox("getValue");
        //recommendBit = document.getElementById("recommendBit").value;
        //alert(recommendBit);
        
        $.ajax({
            url: 'user/doEditStateAndVstate',
            data: {
                'id': id,
                'vipStat': vstat,
                'vipWeight': vipWeight,
                'liveWeight': liveWeight,
                'allowPublish': allowPublish,
                "organizationId": orgId,
                /*"isBlacklist": isBlacklist,*/
                "liveType":liveType,
                "recommendBit":recommendBit
            },
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
    } else {
        var id = $("#update_id").val();
        var name = $("#update_name").val();
        var signature = $("#update_signature").val();
        var sex = $("input[name='sex']:checked").val();
        var stat = $('#update_stat').combobox('getValue');
        var vstat = $('#update_vstat').combobox('getValue');
        var rankAble = $('#update_rankable').combobox('getValue');
        $.ajax({
            url: 'user/doUpdateRuser',
            data: {
                'id': id,
                'name': name,
                'signature': signature,
                'sex': sex,
                'stat': stat,
                'vipStat': vstat,
                'rankAble': rankAble
            },
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.resultCode == "ok") {
                    $('#updateDialog').dialog('close');
                    showMessage("成功提示", "更新成功！");
                    doSearch();
                } else {
                    showMessage("错误提示", "更新失败！");
                }
            }
        });
    }
}

/*function deleteRuser(value) {
 if (confirm("确定要删除此用户吗？")) {
 $.ajax({
 url: 'user/deleteRuser',
 data: {'id': value},
 type: "post",
 dataType: "json",
 success: function (result) {
 if (result == "ok") {
 showMessage("成功提示", "删除成功！");
 doSearch();
 } else {
 showMessage("错误提示", "删除失败！");
 }
 }
 });
 }
 }*/

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
                $('#vipWeight').val(result.vipWeight);
                $('#liveWeight').val(result.liveWeight);
                $('#allow_publish').combobox('select', result.allowPublish);
                $('#organizationId').combobox('select', result.organizationId);
                /*$('#isBlacklist').combobox('select', result.isBlacklist);*/
                $('#liveType').combobox('select', result.liveType);
                //alert(result.recommendBit);
                $('#recommendBit').combobox('select', result.recommendBit);
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

/*function updateRuser(value) {
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
 $("#imgShow").attr("src", "http://192.168.108.160/restadmin/download/" + result.pic);
 $("#updateDialog").show();
 $("#updateDialog").dialog();
 } else {
 showMessage("错误提示", "获取数据失败，请稍后重试！");
 }
 }
 });
 }*/

var listUrl = "user/queryFrontUserList";
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
        url: listUrl,
        columns: [
            [
                /*{field: 'ck', width: 40, checkbox: true},*/
                {field: 'id', title: '<span class="columnTitle">用户ID</span>', width: 80, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 80, align: 'center'},
                {field: 'username', title: '<span class="columnTitle">用户名</span>', width: 80, align: 'center'},
                {
                    field: 'phone',
                    title: '<span class="columnTitle">手机号</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'createDateStr',
                    title: '<span class="columnTitle">注册时间</span>',
                    width: 120,
                    align: 'center'
                },
                /*{field: 'addr', title: '<span class="columnTitle">所在地</span>', width: 80, align: 'center'},*/
                {
                    field: 'videoCount',
                    title: '<span class="columnTitle">发布视频数</span>',
                    width: 80,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'recommendBit',
                    title: '<span class="columnTitle">推荐位</span>',
                    width: 60,
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row) {
                        if (value == 0) {
                            return "允许";
                        }  else {
                            return "不允许";
                        }
                    }
                },
                {
                    field: 'stat',
                    title: '<span class="columnTitle">状态</span>',
                    width: 80,
                    align: 'center',
                    sortable: true,
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
                    field: 'signSum',
                    title: '<span class="columnTitle">积分</span>',
                    width: 80,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'vipStat',
                    title: '<span class="columnTitle">等级</span>',
                    width: 80,
                    align: 'center',
                    sortable: true,
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
                    width: 80,
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
                    field: 'fansCount',
                    title: '<span class="columnTitle">粉丝</span>',
                    width: 70,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'realFansCount',
                    title: '<span class="columnTitle">铁粉</span>',
                    width: 70,
                    align: 'center',
                    sortable: true,
                    hidden: true
                },
                /*{
                    field: 'twentyFourHourPopularity',
                    title: '<span class="columnTitle">24时人气</span>',
                    width: 100,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'weekPopularity',
                    title: '<span class="columnTitle">周人气</span>',
                    width: 70,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'monthPopularity',
                    title: '<span class="columnTitle">月人气</span>',
                    width: 70,
                    align: 'center',
                    sortable: true
                },*/
                {
                 field: 'thirdFrom',
                 title: '<span class="columnTitle">注册方式</span>',
                 width: 120,
                 align: 'center',
                    formatter: function (value, row) {
                        if (value == "qq") {
                            return "QQ";
                        } else if(value == "wechat"){
                            return "微信";
                        }else if(value == "sina"){
                            return "微博";
                        }else{
                            return "手机号";
                        }
                    }
                 },
                {
                     field: 'appVersion', title: '<span class="columnTitle">版本</span>', width: 70, align: 'center'
                 },
                {
                    field: 'isAnchor',
                    title: '<span class="columnTitle">是否主播</span>',
                    width: 70,
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row) {
                        if (value == 1) {
                            return "是";
                        } else {
                            return "";
                        }
                    }
                },
                {
                    field: 'vipWeight',
                    title: '<span class="columnTitle">VIP权重</span>',
                    width: 80,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'liveWeight',
                    title: '<span class="columnTitle">直播权重</span>',
                    width: 80,
                    align: 'center',
                    sortable: true
                },
                {
                    field: 'regPlatform',
                    title: '<span class="columnTitle">注册来源</span>',
                    width: 80,
                    align: 'center',
                    sortable: true
                },
                /*{
                 field: 'loginDate',
                 title: '<span class="columnTitle">最后登录时间</span>',
                 width: 120,
                 align: 'center'
                 },*/
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 180, align: 'center',
                    formatter: function (value, row) {
                        var liveDetailStr = '<a href="ruser/forwardUserLiveDetail?id=' + row.id + '">直播详情</a>';
                        var detailStr = '<a class="easyui-linkbutton" href="video/userdetail?uid=' + row.id + '" style="width:80px;height: 25px;" title="查看"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">查看</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        //var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="updateRuser(' + row.id + ')" style="width:80px;height: 25px;" title="修改"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">修改</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        //var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteRuser(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                        var stateStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="editStateAndVstate(' + row.id + ')" style="width:80px;height: 25px;" title="快捷编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">快捷编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';

                        return liveDetailStr + " " + detailStr + " " + stateStr;
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
    queryParams.userKeyword = $('#userKeyword').val();
    queryParams.user = $('#user').combobox("getValue");
    queryParams.stat = $('#query_stat').combobox("getValue");
    queryParams.vipStat = $('#query_vstat').combobox("getValue");
    queryParams.rankAble = $('#query_rankable').combobox("getValue");
    queryParams.sex = $('#query_sex').combobox("getValue");
    queryParams.addr = $('#query_addr').val();
    queryParams.startCount = $('#startCount').val();
    queryParams.endCount = $('#endCount').val();
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');
    queryParams.regPlatform = $('#queryRegPlatform').combobox("getValue");
    queryParams.regPlatformChannel = $('#queryRegPlatformChannel').combobox("getValue");
    queryParams.allowPublish = $("#queryAllowPublish").combobox("getValue");
    queryParams.isAnchor = $("#queryIsAnchor").combobox("getValue");
    queryParams.organizationId = $("#searchOrganizationId").combobox("getValue");
    queryParams.thirdFrom = $("#thirdFrom").combobox("getValue");
    queryParams.recommendBit = $("#recommendBit").combobox("getValue");
    queryParams.appVersion = $("#appVersion").combobox("getValue");
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

function exportWopaiUser() {
    var vipStat = $('#vipStat').combobox("getValue");
    if (vipStat != 0) {
        window.location.href = "user/exportWopaiUsersToExcel?vipStat=" + vipStat;
    } else {
        showMessage("错误提示", "请选择用户等级！");
    }
}

function exportWopaiNormalUser() {
        $('#searchForm').form('submit', {
            url: "user/exportWopaiNormalUsersToExcel"
        });
}

function showRealFans() {
    $("#showRealFans").attr("onclick", "hideRealFans()");
    $("#showRealFans").html('<span class="l-btn-left"><span class="l-btn-text">隐藏铁粉</span></span>');
    $('#displayTable').datagrid('showColumn', 'realFansCount');
}

function hideRealFans() {
    $("#showRealFans").attr("onclick", "showRealFans()");
    $("#showRealFans").html('<span class="l-btn-left"><span class="l-btn-text">显示铁粉</span></span>');
    $('#displayTable').datagrid('hideColumn', 'realFansCount');
}
function chgOpration(obj) {
    alert($(obj).val());
}