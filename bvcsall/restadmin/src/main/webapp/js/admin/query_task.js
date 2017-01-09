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

function getTypeOne(value) {
	
	if(value ==1){
		return '日常任务';
	}else if(value ==2){
		return '限时任务';
	}else if(value ==3){
		return '一次性任务';
	}else if(value ==4){
		return '特殊任务';
	}
}

function getTypeTwo(value) {
	
	if(value ==1){
		return '发布一个视频,赠送N积分';
	}else if(value ==41){
		return '参与1个活动M次,赠送N积分';
	}else if(value ==21){
		return '关注黄V,赠送N积分';
	}else if(value ==22){
		return '关注蓝V,赠送N积分';
	}else if(value ==23){
		return '关注绿V,赠送N积分';
	}else if(value ==31){
		return '评论一次视频,赠送N积分';
	}else if(value ==32){
		return '赞一次视频,赠送N积分';
	}else if(value ==33){
		return '转发一次视频,赠送N积分';
	}else if(value ==1001){
		return '参加特定活动,赠送N积分';
	}else if(value ==2001){
		return '完善个人资料,赠送N积分';
	}else if(value ==2021){
		return '粉丝达到50,赠送N积分';
	}else if(value ==2022){
		return '粉丝达到100,赠送N积分';
	}else if(value ==2023){
		return '粉丝达到200,赠送N积分';
	}else if(value ==51){
		return '签到任务';
	}
	
	
}

var listUrl = "task/taskListJson";
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
				    field: 'id',
				    title: '<span class="columnTitle">ID</span>',
				    width: '50px',
				    sortable: true
				},
				{
				    field: 'title',
				    title: '<span class="columnTitle">标题</span>',
				    width: '20%',
				    sortable: true
				},
				{
				    field: 'description',
				    title: '<span class="columnTitle">描述</span>',
				    width: '20%',
				    sortable: true
				},
                {
                    field: 'typeOne',
                    title: '<span class="columnTitle">类型</span>',
                    width: '80px',
                    sortable: true,
                    formatter: function (value) {
                        if (value == null) {
                            return "";
                        } else {
                            //return formatDate(value);
                            return getTypeOne(value);
                        }
                    }
                },
                {
                    field: 'typeTwo',
                    title: '<span class="columnTitle">子类型</span>',
                    width: '160px',
                    sortable: true,
                    formatter: function (value) {
                        if (value == null) {
                            return "";
                        } else {
                            //return formatDate(value);
                            return getTypeTwo(value);
                        }
                    }
                },
                {
                    field: 'taskValue',
                    title: '<span class="columnTitle">任务值</span>',
                    width: '',
                    sortable: true
                },
                {
                    field: 'num',
                    title: '<span class="columnTitle">个数</span>',
                    width: '50px',
                    sortable: true
                },
                {
                    field: 'integral',
                    title: '<span class="columnTitle">奖励(积分/钻石)</span>',
                    width: '90px',
                    sortable: true
                },
                {
                    field: 'previousTaskId',
                    title: '<span class="columnTitle">前置任务ID</span>',
                    width: '70px',
                    sortable: true
                },
                {
                    field: 'weight',
                    title: '<span class="columnTitle">权重</span>',
                    width: '110px',
                    sortable: true,
                    formatter: function (value, row) {
                        var editStr = '<input id="'+row.id+'_weight" name="'+row.id+'_weight" value="'+row.weight+'"  size="10" onblur="changeWeight('+row.id+',this)" ></input>';
                        return editStr;
                    }
                },
                {
                    field: 'deadLine',
                    title: '<span class="columnTitle">过期时间</span>',
                    width: '130px',
                    sortable: true,
                    formatter: function (value) {
                        if (value == null) {
                            return "";
                        } else {
                            //return formatDate(value);
                            return value;
                        }
                    }
                },
                {
                    field: 'aaa',
                    title: '<span class="columnTitle">操作</span>',
                    width: '180px',
                    sortable: true,
                    formatter: function (value, row) {
                        var editStr = '<input class="easyui-linkbutton" style="width:80px;height: 25px;" id="mdOM'+row.id+'" name="mdOM'+row.id+'" type="button" value="编辑" onclick="showTaskAdd(this,'+row.id+');"  />'
                        +'&nbsp;'+
                        '<input class="easyui-linkbutton" style="width:80px;height: 25px;" id="mdOM'+row.id+'" name="mdOM'+row.id+'" type="button" value="删除" onclick="updateTaskStatus(this,'+row.id+',1);"  />';
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

function editShowOrder(id) {
    var showOrder = $("#showOrder" + id).text();
    //alert(showOrder);
    $.ajax({
        url: 'activity/updateHotLabel',
        type: 'post',
        data: 'showOrder=' + showOrder,
        async: false, //默认为true 异步
        error: function () {
            alert('error');
        },
        success: function (data) {
            $("#" + divs).html(data);
        }
    });
}

//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.keyword = $('#keyword').val();
    $('#displayTable').datagrid({url: listUrl});
}

function deleteHotLabel(labelId) {
    if (confirm('确实要删除吗？')) {
        $.ajax({
            url: 'activity/deleteHotLabel',
            data: {'hotLabelId': labelId},
            type: "post",
            dataType: "json",
            success: function (result) {
                alert("删除成功");
                doSearch();
            }
        });
    }
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