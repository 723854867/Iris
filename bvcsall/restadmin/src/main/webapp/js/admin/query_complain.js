/**
 * Created by huoshanwei on 2015/10/29.
 */
var datagridId = "#tt";
var adddialogueId = "#dlg";
var editdialogueId = "#updatedlg";
var addFormId = "#fm";
var editFormId = "#updatefm";
var addTitle = "播放视频";
var editTitle = "编辑投诉";
var cancelConfirmMessage = "你确定要取消投诉吗?";
var deleteConfirmMessage = "你确定要删除吗?";
var noSelectedRowMessage = "你没有选择行";
var searchFormId = "#searchForm";
var pageSize = 50;

var listUrl = "complain/searchpagelist";
//var updateUrl = "complain/updatepage";
var deleteUrl = "complain/delete";
//var addUrl = "complain/create";
var url;


$(function () {
    $(datagridId).datagrid({
        fitColumns: true,
        rownumbers: true,
        striped: true,
        pagination: true,
        pageNumber: 1,
        fit: true,
        pageList: [pageSize, pageSize * 2, pageSize * 3],
        pageSize: pageSize,
        pagePosition: 'bottom',
        singleSelect: true,
        selectOnCheck: false,
        nowrap: true,
        url: listUrl,
        columns: [[
            {field: 'ck', width: 100, checkbox: true},
            {
                field: 'playKey', title: "缩略图", width: 100, align: 'center', formatter: function (value, row, index) {
                var videoPlayUrlPrefix = $("#video_play_url_prefix").val();
                return "<img src='" + videoPlayUrlPrefix + value + ".jpg' width='120' height='100' onclick='playVideo(\"" + row.videoId + "\",\"" + row.playKey + "\")'/>";
            }
            },
            {field: 'videoName', title: "视频名称", width: 100, align: 'center'},
            {field: 'videoCreator', title: "投诉人", width: 100, align: 'center'},
            {field: 'createDate', title: "投诉时间", width: 100, align: 'center'},
            {field: 'content', title: "投诉类型", width: 100, align: 'center'},
            {
                field: 'stat', title: '处理状态', width: 100, align: 'center', formatter: function (value, row, index) {
                var stat = "未处理";
                if (row.stat == 1) {
                    stat = "已处理";
                }
                return stat;
            }
            },
            {
                field: 'id', title: "操作", width: 100, align: 'center', formatter: function (value, row, index) {
                var content = "";
                if (row.stat != 1) {
                    content += '<a class="easyui-linkbutton" onClick="cancel(' + row.id + ')" href="javascript:;" style="width:80px;height: 25px;" title="取消投诉"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">取消投诉</span><span class="l-btn-icon icon-cancel">&nbsp;</span></span></a>';
                }
                content += '<a class="easyui-linkbutton" onClick="deleteVideo(' + row.videoId + ')" href="javascript:;" style="width:80px;height: 25px;" title="删除视频"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除视频</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                return content;
            }
            }
        ]],
    });
});

var url;

function destroyComplain() {
    var row = $(datagridId).datagrid('getChecked');
    var ids = [];
    var inum = 0;
    for (var r in row) {
        ids.push(row[r]['videoId']);
    }
    ids = ids.join(",");
    if (ids != "") {
        $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
            if (r) {
                $.post('video/logicremove', {ids: ids}, function (result) {
                    if (result["success"] == true) {
                        $(datagridId).datagrid('reload'); // reload the user data
                    } else {
                        showMessage("Error", result["message"]);
                    }
                });
            }
        });
    } else {
        showMessage("Error", noSelectedRowMessage);
    }
}

function cancelComplain() {
    var row = $(datagridId).datagrid('getChecked');
    var ids = [];
    var inum = 0;
    for (var r in row) {
        ids.push(row[r]['id']);
    }
    ids = ids.join(",");
    if (ids != "") {
        $.messager.confirm('Confirm', cancelConfirmMessage, function (r) {
            if (r) {
                $.post('complain/logiccancle', {ids: ids}, function (result) {
                    if (result["success"] == true) {
                        $(datagridId).datagrid('reload'); // reload the user data
                    } else {
                        showMessage("Error", result["message"]);
                    }
                });
            }
        });
    } else {
        showMessage("Error", noSelectedRowMessage);
    }
}

function cancel(id) {
    if (id) {
        $.messager.confirm('Confirm', cancelConfirmMessage, function (r) {
            if (r) {
                $.post('complain/logiccancle', {ids: id}, function (result) {
                    if (result["success"] == true) {
                        $(datagridId).datagrid('reload'); // reload the user data
                    } else {
                        showMessage("Error", result["message"]);
                    }
                });
            }
        });
    } else {
        showMessage("Error", noSelectedRowMessage);
    }
}


function deleteVideo(id) {
    if (id) {
        $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
            if (r) {
                $.post('video/logicremove', {ids: id}, function (result) {
                    if (result["success"] == true) {
                        $(datagridId).datagrid('reload'); // reload the user data
                    } else {
                        showMessage("Error", result["message"]);
                    }
                });
            }
        });
    } else {
        showMessage("Error", noSelectedRowMessage);
    }
}

$(function () {
    $('#date_1').datetimebox({
        showSeconds: false
    });
    $('#date_2').datetimebox({
        showSeconds: false
    });
});

function doSearch() {
    $(datagridId).datagrid('load', getFormJson(searchFormId));
}

function playVideo(playerId, fileName) {
    $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
    $('#playerContainer').empty();
    var _player = null;
    var player = $('<div/>');
    $(player).attr('id', 'pl' + playerId);
    $('#playerContainer').append(player);
    var conf = {
        file: $("#video_play_url_prefix").val() + fileName + '.m3u8',
        image: $("#img").val(),
        height: 350,
        width: 400,
        autostart: true,
        analytics: {enabled: false}
    };
    _player = jwplayer('pl' + playerId).setup(conf);
}
