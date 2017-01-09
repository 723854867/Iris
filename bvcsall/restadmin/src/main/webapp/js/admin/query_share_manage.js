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


function getShareType(type) {
	if (type == 0) {
		return '未知';
	} else if (type == 1) {
		return '首页';
	} else if (type == 2) {
        return '排行榜';
    } else if (type == 3) {
        return '活动页';
    }else if (type == 4) {
        return '直播';
    }else if (type == 5) {
        return '直播预告';
    }else if (type == 6) {
    	return '直播回放';
    }else if (type == 7) {
        return '新歌声【学员榜】';
    }else if (type == 8) {
        return '新歌声【综艺榜】';
    }else if (type == 9) {
        return '新歌声【主播榜】';
    }else if (type == 10) {
        return '拉票';
    }
}

function getImage(url) {
    if (url != undefined && url!='') {
        var divStr = '<div style="float: left;">' +
            '<img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"' +
            'src="/restadmin/download' + url + '"/><br>' +
            '</div>';
        return divStr;
    }
    return '';
}


var listUrl = "shareManage/shareManageListJson";
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
				    width: '60px',
				    sortable: true
				},
                {
                    field: 'shareImg',
                    title: '<span class="columnTitle">分享图片</span>',
                    width: '10%',
                    align: 'left',
                    sortable: true,
                    formatter: function (value) {
                        if (value == null) {
                            return "";
                        } else {
                            return getImage(value);
                        }
                    }
                },
				{
				    field: 'shareType',
				    title: '<span class="columnTitle">分享类型</span>',
				    width: '10%',
                    align: 'left',
				    sortable: true,
                    formatter: function (value) {
                            if (value == null) {
                            return "";
                        } else {
                            return getShareType(value);
                        }
                    }
				},
                {
                    field: 'shareTitle',
                    title: '<span class="columnTitle">分享标题</span>',
                    width: '20%',
                    sortable: true
                },
                {
                    field: 'shareText',
                    title: '<span class="columnTitle">分享文案</span>',
                    width: '30%',
                    sortable: true
                },
                {
                    field: 'aaa',
                    title: '<span class="columnTitle">操作</span>',
                    width: '280px',
                    sortable: true,
                    formatter: function (value, row) {
                        return  '<input class="easyui-linkbutton" style="width:80px;height: 25px;" id="mdOM'+row.id+'" name="mdOM'+row.id+'" type="button" value="编辑" onclick="editShare('+row.id+');"  />'
                                +'&nbsp;'+
                                '<input class="easyui-linkbutton" style="width:80px;height: 25px;" id="mdOM'+row.id+'" name="mdOM'+row.id+'" type="button" value="删除" onclick="deleteShare('+row.id+');"  />';


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