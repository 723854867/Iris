/**
 * 榜单js
 * Created by busap on 2016/8/2.
 */

function insertStudentDialog() {
    $("#insert_student_dlg").dialog('open').dialog('setTitle', "添加学员");
}

function doInsertStudent() {
    $('#insertStudentForm').form('submit', {
        url: "voiceList/insertStudent",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#insert_student_dlg').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function insertVoiceListDialog() {
    $("#insert_voice_list_dlg").dialog('open').dialog('setTitle', "榜单设置");
}

function doInsertVoiceList() {
    $('#insertVoiceListForm').form('submit', {
        url: "voiceList/insertVoiceList",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#insert_voice_list_dlg').dialog('close');
                doSearchVoiceList();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function settingCorpsDialog(id) {
    $("#studentId").val(id);
    $("#corps_dialog").dialog('open').dialog('setTitle', "设置战队");
}

function settingCorps() {
    $('#corpsDialogForm').form('submit', {
        url: "voiceList/settingCorps",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#corps_dialog').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function doSearch() {
    $('#displayTable').datagrid({url: "voiceList/querySingMember"});
}

$(function () {
    getSingMember();
    getListInfo();
    $('#startTime').datetimebox({
        required : false,
        onShowPanel:function(){
            $(this).datetimebox("spinner").timespinner("setValue","00:00:00");
        }
    });
    $('#endTime').datetimebox({
        required : false,
        onShowPanel:function(){
            $(this).datetimebox("spinner").timespinner("setValue","23:59:59");
        }
    });
});
function getSingMember(){
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
        url: "voiceList/querySingMember",
        columns: [
            [
                {
                    field: 'pic', title: '<span class="columnTitle">头像</span>', width: 80, align: 'center',
                    formatter: function (value, row) {
                        var img = "";
                        if(value != null){
                            img = '<img src="/restadmin/download' + value + '" style="width:60px;height:60px;">';
                        }
                        return img;
                    }
                },
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">名称</span>', width: 120, align: 'center'},
                {field: 'popularity', title: '<span class="columnTitle">人气</span>', width: 120, align: 'center'},
                {field: 'corps', title: '<span class="columnTitle">战队</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if(value == 1){
                            return "周杰伦";
                        }else if (value == 2){
                            return "汪峰";
                        }else if (value == 3){
                            return "那英";
                        }else if (value == 4){
                            return "庾澄庆";
                        }else if (value == 5){
                            return "王力宏";
                        }
                    }
                },
                {field: 'createDate', title: '<span class="columnTitle">加入时间</span>', width: 120, align: 'center'},
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 200, align: 'center',
                    formatter: function (value, row) {
                        var corps = '<a class="easyui-linkbutton" href="javascript:;" onclick="settingCorpsDialog(' + row.id + ')" style="width:80px;height: 25px;" title="设置战队"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">设置战队</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var popularity = '<a class="easyui-linkbutton" href="javascript:;" onclick="cheatVoteDialog(' + row.id + ')" style="width:80px;height: 25px;" title="加人气"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">加人气</span><span class="l-btn-icon icon-add">&nbsp;</span></span></a>';
                        var del = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteSingMember(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                        return corps+" "+popularity+" "+del;
                    }
                }
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function () {
            $('#displayTable').datagrid('clearSelections');
        },
        pageSize: 200,
        pageList: [200, 400, 600, 800, 1000],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
}
function getListInfo() {
    $('#displayListTable').datagrid({
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
        url: "voiceList/queryListInfo",
        rowStyler: function (index, row) {
            if (row.state != 1) {
                return 'color:red;';
            }
        },
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">名称</span>', width: 120, align: 'center'},
                {field: 'type', title: '<span class="columnTitle">类型</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if(value == 1){
                            return "学员榜";
                        }else if (value == 2){
                            return "网综榜";
                        }else if (value == 3){
                            return "主播榜";
                        }else if (value == 4){
                            return "贡献榜";
                        }else if (value == 5){
                            return "贡献榜总榜";
                        }else{
                            return "<span style='color: red;'>未知，数据错误</span>";
                        }
                    }
                },
                {field: 'startTime', title: '<span class="columnTitle">开始时间</span>', width: 120, align: 'center'},
                {field: 'endTime', title: '<span class="columnTitle">结束时间</span>', width: 120, align: 'center'},
                {field: 'personNumber', title: '<span class="columnTitle">榜单人数</span>', width: 120, align: 'center'},
                {field: 'createTime', title: '<span class="columnTitle">创建时间</span>', width: 120, align: 'center'},
                {field: 'url', title: '<span class="columnTitle">url地址</span>', width: 120, align: 'center'},
                {field: 'state', title: '<span class="columnTitle">状态</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                    if(value == 1){
                        return "正常";
                    }else{
                        return "删除";
                    }
                }
                },
                {field: 'operation', title: '<span class="columnTitle">操作</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        var detail = '<a class="easyui-linkbutton" href="javascript:;" onclick="getVoiceListDetail(' + row.id + ',' + row.type + ')" style="width:80px;height: 25px;" title="查看榜单"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">查看榜单</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        var del = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteVoiceList(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                        return detail+" "+del;
                    }
                }
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function () {
            $('#displayListTable').datagrid('clearSelections');
        },
        pageSize: 200,
        pageList: [200, 400, 600, 800, 1000],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
}

function cheatVoteDialog(id){
    $("#select_vote_dlg").dialog('open').dialog('setTitle', "人气");
    $("#userId").val(id);
    getCheatSingVoteInfo(id);
}

function getCheatSingVoteInfo(id) {
    $('#displayVoteTable').datagrid({
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
        url: "voiceList/queryCheatSingVoteInfo",
        queryParams: {
            userId: id
        },
        columns: [
            [
                {field: 'createTime', title: '<span class="columnTitle">时间</span>', width: 120, align: 'center'},
                {field: 'popularity', title: '<span class="columnTitle">人气</span>', width: 120, align: 'center'},
                {field: 'updateTime', title: '<span class="columnTitle">添加时间</span>', width: 120, align: 'center'},
                {field: 'operator', title: '<span class="columnTitle">操作人</span>', width: 120, align: 'center'},
            ]
        ],
        toolbar: "#dataGridVoteToolbar",
        onLoadSuccess: function () {
            $('#displayVoteTable').datagrid('clearSelections');
        },
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
}

function cheatPopularity(){
    var id = $("#userId").val();
    $('#cheatForm').form('submit', {
        url: "voiceList/insertPopularity",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                getCheatSingVoteInfo(id);
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function deleteSingMember(id){
    if(confirm("确定要执行删除操作吗？")){
        $.ajax({
            url: 'voiceList/deleteSingMember',
            data: {"userId": id},
            type: "post",
            dataType: "json",
            success: function (response) {
                if (response.resultCode == "success") {
                    showMessage("提示信息", response.resultMessage);
                    doSearch();
                } else {
                    showMessage("错误信息", parsedJson.resultMessage);
                }
            }
        });
    }
}

function getVoiceListDetail(id,type) {
    if(type == 1){
        window.location.href = "voiceList/forwardVoiceList?id="+id;
    }else if(type == 2){
        window.location.href = "voiceList/forwardVarietyList?id="+id;
    }else if(type == 3){
        window.location.href = "voiceList/forwardAnchorList?id="+id;
    }else if(type == 4){
        window.location.href = "voiceList/forwardContributionList?id="+id;
    }else if(type == 5){
        window.location.href = "voiceList/forwardVarietyList?id="+id;
    }

}


function deleteVoiceList(id) {
    if(confirm("确定要执行删除操作吗？")){
        $.ajax({
            url: 'voiceList/deleteVoiceList',
            data: {"id": id},
            type: "post",
            dataType: "json",
            success: function (response) {
                if (response.resultCode == "success") {
                    showMessage("提示信息", response.resultMessage);
                    doSearchVoiceList();
                } else {
                    showMessage("错误信息", parsedJson.resultMessage);
                }
            }
        });
    }
}

function doSearchVoiceList() {
    $('#displayListTable').datagrid({url: "voiceList/queryListInfo"});
}

