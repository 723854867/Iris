/**
 * Created by .
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
var dealConfirmMessage = "你确定要处理投诉吗?";
var searchFormId = "#searchForm";
var pageSize = 50;

var listUrl = "consumeRecord/findConsumeRecordByUser";
//var updateUrl = "complain/updatepage";
var deleteUrl = "consumeRecord/delete";
//var addUrl = "complain/create";
var url;

function doSearch() {
    $(datagridId).datagrid('reload', getFormJson(searchFormId));
}


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
        onLoadSuccess: function (data) { 
        	$('#pSum').html(eval(data).pSum);
        	$('#imSum').html(eval(data).imSum);
    	   
    	} ,
        pagePosition: 'bottom',
        singleSelect: true,
        selectOnCheck: false,
        nowrap: true,
        url: listUrl,
        columns: [[
                   
            {field: 'nick', title: "用户名", width: 100, align: 'center'},
			{field: 'name', title: "赠送者", width: 100, align: 'center'},
			{field: 'giftName', title: "道具名称", width: 100, align: 'center'},
			{field: 'number', title: "个数", width: 100, align: 'center'},
			{field: 'createDate', title: "赠送时间", width: 60, align: 'center',
				formatter: function (value) {
			        if (value == null) {
			            return "";
			        } else {
			            return value;
			            //return value;
			        }
			    }
				
			},
			{field: 'points', title: "折合金豆", width: 100, align: 'center'},
			{field: 'inMoney', title: '<span class="columnTitle">折合现金（元）</span>', width: 80, align: 'center',
                formatter: function (value) {
                	if (value == null) {
			            return "0";
			        } else {
			        	return (value).toFixed(3);
			        }
                    
                }
            },
            {
                field: 'platform', title: '<span class="columnTitle">平台</span>', width: 70, align: 'center'
            },
            {
                field: 'appVersion', title: '<span class="columnTitle">版本</span>', width: 70, align: 'center'
            },
            {
                field: 'channel', title: '<span class="columnTitle">渠道</span>', width: 70, align: 'center'
            },
			
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

function dealComplaints(id,type) {
    if (id&&type) {
        $.messager.confirm('Confirm', dealConfirmMessage, function (r) {
            if (r) {
            	
                $.post('liveComplaint/dealComplaints', {lCId: id,type:type}, function (result) {
                	
                    if (result == 'ok') {
                        $(datagridId).datagrid('reload'); // reload the user data
                    } else {
                        showMessage("Error", result);
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
	var a=getFormJson(searchFormId);
	//alert(a);
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
