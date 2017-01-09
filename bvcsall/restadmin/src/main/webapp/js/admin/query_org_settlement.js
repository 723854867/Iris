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

var listUrl = "orgSettlement/findOrgSettlementList";
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
        pagePosition: 'bottom',
        onLoadSuccess: function (data) { 
        	//$('#dmSum').html(eval(data).dmSum);
        	//$('#mSum').html(eval(data).mSum);
        	//$('#uCount').html(eval(data).uCount);
    	   
    	} ,
        singleSelect: true,
        selectOnCheck: false,
        nowrap: true,
        url: listUrl,
        columns: [[
            
            {field: 'name', title: "用户名", width: 100, align: 'center'},
            {field: 'phone', title: "手机号", width: 100, align: 'center'},
			{field: 'orgName', title: "机构", width: 100, align: 'center'},
			{field: 'status',title: '状态',width: 100,align: 'center',
            	formatter:function(value,row){
            		var str="";
            		if(value=='1'){
            			str="已结算";
            		}else{
            			str="未结算";
            		}
            		return str;
            	}
            },
			{field: 'points', title: "应结金豆数", width: 100, align: 'center'},
			{field: 'amount', title: "应结现金（元）", width: 100, align: 'center',
				formatter:function(value,row){
					return value/100;
            	}
			},
			{field: 'realPoints', title: "实结金豆数", width: 100, align: 'center'},
			{field: 'realAmount', title: "实结现金（元）", width: 100, align: 'center',
				formatter:function(value,row){
					if(value){
						return value/100;
					}else{
						return value;
					}
            		
            	}
			},
			{field: 'userId', title: "用户编号", width: 100, align: 'center'},
			{
                field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                formatter: function (value, row) {
                	var str="";
            		if(row.status=='0'){
            			str='<a class="easyui-linkbutton" href="javascript:;" onclick="doSettlement(' + row.id + ')" style="width:80px;height: 25px;" title="结算"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">结算</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
            		}else{
            			str="已结算";
            		}
                    return str ;
                }
            }
        ]],
    });
});

function doSettlement(id) {

	if (confirm('确认结算？')) {
        $.ajax({
            url: 'orgSettlement/doOrgSettlement',
            data: {'orId': id},
            type: "post",
            dataType: "json",
            async:false,
            success: function (result) {
                if (result == 'ok') {
                    showMessage("提示信息", '结算成功');
                    doSearch();
                    
                }

            }
        });
    }
}

function doSettlements() {

	var settleMonth = $("#settleMonth").combobox("getValue");;
	if (confirm('确认结算？')) {
        $.ajax({
            url: 'orgSettlement/doOrgSettlements',
            data: {'settleMonth': settleMonth},
            type: "post",
            dataType: "json",
            async:false,
            success: function (result) {
                if (result == 'ok') {
                    showMessage("提示信息", '结算成功');
                    doSearch();
                    
                }

            }
        });
    }
}

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
