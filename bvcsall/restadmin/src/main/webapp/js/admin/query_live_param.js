/**
 * 
 */

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


var listUrl = "liveParam/paramListJson";
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
        singleSelect: true, //是否单选
        pagination: true, //分页控件
        rownumbers: false, //行号
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        onClickCell: onClickCell,
        onAfterEdit: function (rowIndex, rowData, changes) {
            

        },
        columns: [
            [
				{
				    field: 'type',
				    title: '<span class="columnTitle">提示类型</span>',
				    width: '120px',
                    align: 'left',
				    sortable: true,
                    formatter: function (value, row) {
                        if (row.type == 1) {
                            return '观看人数';
                        } else if (row.type == 2) {
                            return '获得点赞数';
                        }
                    }
				},
                {
                    field: 'count',
                    title: '<span class="columnTitle">阀值</span>',
                    width: '120px',
                    align: 'left',
                    sortable: true
                },
                {
                    field: 'description',
                    title: '<span class="columnTitle">描述</span>',
                    width: '50%',
                    sortable: true
                },
                {
                    field: 'aaa',
                    title: '<span class="columnTitle">操作</span>',
                    width: '280px',
                    sortable: true,
                    formatter: function (value, row) {
                        var editStr = '';
                        if (row.available == 0) {
                            // 不生效
                            editStr = '<input class="easyui-linkbutton" style="width:80px;height: 25px;" id="mdOM'+row.type + '-' + row.count+'" name="mdOM'+row.type + '-' + row.count+'" type="button" value="编辑" onclick="paramEdit('+row.type + ',' + row.count+');"  />'
                                +'&nbsp;' +
                                '<input class="easyui-linkbutton" style="width:80px;height: 25px;" id="mdOM'+row.type + '-' + row.count+'" name="mdOM'+row.type + '-' + row.count+'" type="button" value="删除" onclick="deleteParam('+row.type + ',' + row.count+');"  />';
                        } else {
                            // 生效
                            editStr = '<input class="easyui-linkbutton" style="width:80px;height: 25px;" id="mdOM'+row.type + '-' + row.count+'" name="mdOM'+row.type + '-' + row.count+'" type="button" value="编辑" onclick="paramEdit('+row.type + ',' + row.count+');"  />'
                                +'&nbsp;' +
                                '<input class="easyui-linkbutton" style="width:80px;height: 25px;" id="mdOM'+row.type + '-' + row.count+'" name="mdOM'+row.type + '-' + row.count+'" type="button" value="删除" onclick="deleteParam('+row.type + ',' + row.count+');"  />';
                        }

                        return editStr;
                    }
                },
                
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

    $("#labelTable").datagrid({
        nowrap: true, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: true,
        singleSelect: true, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        pagePosition: 'bottom',
        url: "activity/labelListJson",
        columns: [[
            {field: 'name', title: '话题名称', width: 100},
            {field: 'num', title: '使用数量', width: 100}
        ]],
        onLoadSuccess: function () {
            $('#labelTable').datagrid('clearSelections');
        },
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });

});


//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.keyword = $('#keyword').val();
    $('#displayTable').datagrid({url: listUrl});
}


function showLabelDialog() {
    $("#label_dlg").show();
    $("#label_dlg").dialog({
        title: '选择话题',
        width: 800,
        height: 700,
        closed: false,
        cache: true,
        modal: true
    });
}

function showInsertDialog() {
    $("#dlg").show();
    $("#dlg").dialog({
        title: '选择话题',
        width: 400,
        height: 200,
        closed: false,
        cache: true,
        modal: true
    });
}

function selectLabel(){
    var row = $("#labelTable").datagrid('getChecked');
    var labelName = row[0].name;
    var labelId = row[0].id;
    $("#labelId").val(labelId);
    $("#labelName").val(labelName);
    $("#label_dlg").dialog("close");
}

function doInsertHotLabel(){
    var labelId = $("#labelId").val();
    var labelName = $("#labelName").val();
    var displayOrder = $("#displayOrder").val();
    $.ajax({
        url: 'activity/insertHotLabel',
        data: {'labelName': labelName,'labelId': labelId, 'displayOrder': displayOrder},
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.resultCode == "success") {
                $('#dlg').dialog('close');
                showMessage("提示信息", result.resultMessage);
                doSearch()
            } else {
                showMessage("错误提示", result.resultMessage);
            }
        }
    });
}

function labelListSearch(){
    $("#labelTable").datagrid('reload',{keyword:$('#searchForm').find('[name=keyword]').val()});
}
//------------超级管理员-白名单------------------
function superAdminDlg(){
	$("#whiteListDlg").dialog('open').dialog('setTitle', "超管白名单管理");
	loadUser();
}

function loadUser(){
	$('#userTable').datagrid({
        nowrap: false, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: false,
        singleSelect: false, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: "liveParam/whiteList",
        columns: [
            [
             	{field: 'ck', width: 60, checkbox: true,align: 'center'},
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 100, align: 'center'},
                {field: 'username', title: '<span class="columnTitle">登录名</span>', width: 100, align: 'center'},
                {field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 60, align: 'center'}
            ]
        ],
        toolbar: "#whiteListToolbar",
        onLoadSuccess: function () {
            $('#userTable').datagrid('clearSelections');
        },
        pageSize: 50,
        pageList: [50, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
}

function addUser(){
	var userId = $("#userId").val();

	if(userId==''){
		showMessage("提示", "请输入用户ID");
		return;
	}
	$.get('liveParam/addSuperAdmin', {uid:userId}, function (result) {
        if (result["success"] == true) {
        	$('#userTable').datagrid('reload'); 
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
}

function removeUsers(){
	var rows = $('#userTable').datagrid("getChecked");
	if(rows){
		var ids = [];
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i]["id"]);
		}
		
		ids = ids.join(",");
		
		if(ids==''){
			showMessage("提示", "没有选择用户");
			return;
		}
		
		if (confirm("确定要执行移除操作吗？")) {
			$.get('liveParam/removeSuperAdmin', {uid:ids}, function (result) {
		        if (result["success"] == true) {
		        	$('#userTable').datagrid('reload'); 
		        } else {
		        	 showMessage("提示信息", "设置失败，请重试！");
		        }
		    });
		}
	} else{
		showMessage("提示信息", "没有选择行");
	}
}
