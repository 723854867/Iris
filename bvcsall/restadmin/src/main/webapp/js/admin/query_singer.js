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
        url: "song/querySingerList",
        rowStyler: function (index, row) {
            if (row.state == 0) {
                return 'color:red;';
            }
        },
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">名称</span>', width: 120, align: 'center'},
                {field: 'singerTypeName', title: '<span class="columnTitle">歌手分类</span>', width: 120, align: 'center'},
                {field: 'state', title: '<span class="columnTitle">状态</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if(value == 0){
                            return "删除";
                        }
                        return "正常";
                    }
                },
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 200, align: 'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="dialogUpdate(' + row.id + ')" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        return editStr;
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
    queryParams.id = $('#queryId').val();
    queryParams.name = $('#queryName').val();
    queryParams.singerType = $('#querySingerType').combobox("getValue");
    queryParams.state = $('#queryState').combobox("getValue");
    $('#displayTable').datagrid({url: "song/querySingerList"});
}

function insertSingerDialog() {
    $('#singerType').combobox('reload');
    $("#insert_singer_dlg").dialog('open').dialog('setTitle', "添加歌手");
}

function doInsertSinger() {
    $('#insertSingerForm').form('submit', {
        url: "song/insertSinger",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#insert_singer_dlg').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function dialogUpdate(value) {
    var title = '请求明细';
    var url = 'song/updateSingerTemplate?id=' + value;
    initWindow(title, url, 500, 200);
}

function doUpdate() {
    $('#updateForm').form('submit', {
        url: "song/updateSinger",
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

