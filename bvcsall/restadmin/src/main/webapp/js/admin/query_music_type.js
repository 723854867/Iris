/**
 * Created by busap on 2015/12/23.
 */
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
        url: "music/queryMusicTypeList",
        columns: [
            [
                {field: 'typeName', title: '<span class="columnTitle">音乐类型名称</span>', width: 120, align: 'center'},
                {
                    field: 'createDateStr',
                    title: '<span class="columnTitle">创建时间</span>',
                    width: 100,
                    align: 'center'
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="dialogUpdate(' + row.id + ')" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var delStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteMusicType(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                        return editStr + " " + delStr;
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
    //var queryParams = $('#displayTable').datagrid('options').queryParams;
    $('#displayTable').datagrid({url: "music/queryMusicTypeList"});
}

function insertDialog() {
    $("#insert_dlg").dialog('open').dialog('setTitle', "添加");
}

function doInsert() {
    $('#insertForm').form('submit', {
        url: "music/insertMusicType",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#insert_dlg').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function dialogUpdate(value) {
    var title = '请求明细';
    var url = 'music/updateMusicTypeTemplate?id=' + value;
    initWindow(title, url, 350, 200);
}

function doUpdate() {
    $('#updateForm').form('submit', {
        url: "music/updateMusicType",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#dialogWindow').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function deleteMusicType(id) {
    if (confirm("删除后将不可恢复，您确定要执行删除操作吗？")) {
        $.ajax({
            url: 'music/deleteMusicType',
            data: {'id': id},
            type: "post",
            dataType: "json",
            success: function (result) {
                showMessage("提示信息", result.resultMessage);
                doSearch();
            }
        });
    }

}
