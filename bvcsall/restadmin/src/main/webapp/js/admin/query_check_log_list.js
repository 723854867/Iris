/**
 * Created by .
 */
var datagridId = "#tt";
var adddialogueId = "#dlg";
var editdialogueId = "#updatedlg";
var addFormId = "#fm";
var editFormId = "#updatefm";
var addTitle = "审核日志";
var editTitle = "编辑审核日志";
var cancelConfirmMessage = "你确定要取消审核日志吗?";
var deleteConfirmMessage = "你确定要删除吗?";
var noSelectedRowMessage = "你没有选择行";
var dealConfirmMessage = "你确定要处理审核日志吗?";
var searchFormId = "#searchForm";
var pageSize = 50;

var listUrl = "check/findCheckLogList";
//var updateUrl = "complain/updatepage";
var deleteUrl = "check/checkLogDelete";
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
        	$('#dmSum').html(eval(data).dmSum);
        	$('#mSum').html(eval(data).mSum);
        	$('#uCount').html(eval(data).uCount);
        	$('#rows').html(eval(data).rows);
    	   
    	} ,
        singleSelect: true,
        selectOnCheck: false,
        nowrap: true,
        url: listUrl,
        columns: [[
            
            {field: 'userId', title: "用户id", width: 100, align: 'center'},
            {field: 'name', title: "用户昵称", width: 100, align: 'center'},
			{field: 'phone', title: "手机号", width: 60, align: 'center'},
			{field: 'uName', title: "审核人", width: 60, align: 'center'},
			{field: 'operate', title: "审核结果", width: 50, align: 'center'},
			{field: 'type', title: "类型", width: 50, align: 'center'},
			{field: 'createDate', title: "操作时间", width: 60, align: 'center',
				formatter: function (value) {
			        if (value == null) {
			            return "";
			        } else {
			            //return formatDate(value);
			            return value;
			        }
			    }
				
			},
			{field: 'reason', title: "原因", width: 300, align: 'center'},
            {field: 'modify', title: "操作", width: 60, align: 'center',
                formatter: function (value,row) {
                    if(row.type == 'live'){
                        return '<a href="check/checkPageDetail?roomId=' + row.roomId + '">直播详情</a>';
                    }
                }

            },
			
        ]],
    });
});

var url;



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

