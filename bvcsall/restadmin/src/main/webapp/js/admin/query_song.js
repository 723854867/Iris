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
        url: "song/querySongList",
        rowStyler: function (index, row) {
            if (row.state == 0) {
                return 'color:red;';
            }
        },
        columns: [
            [
                {
                    field: 'albumCover', title: '<span class="columnTitle">专辑封面</span>', width: 80, align: 'center',
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
                {field: 'singerName', title: '<span class="columnTitle">歌手名称</span>', width: 120, align: 'center'},
                {field: 'singerTypeName', title: '<span class="columnTitle">歌手分类</span>', width: 120, align: 'center'},
                {field: 'albumName', title: '<span class="columnTitle">专辑名称</span>', width: 120, align: 'center'},
                {field: 'type', title: '<span class="columnTitle">文件类型</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if(value == 1){
                            return "伴奏";
                        }
                        return "原唱";
                    }
                },
                {field: 'state', title: '<span class="columnTitle">状态</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        if(value == 0){
                            return "下架";
                        }
                        return "上架";
                    }
                },
                {field: 'downloadCount', title: '<span class="columnTitle">下载量</span>', width: 120, align: 'center'},
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
    queryParams.singerName = $('#querySingerId').val();
    queryParams.albumId = $('#queryAlbumId').combobox("getValue");
    queryParams.type = $('#queryType').combobox("getValue");
    queryParams.state = $('#queryState').combobox("getValue");
    $('#displayTable').datagrid({url: "song/querySongList"});
}

function insertSingerTypeDialog() {
    $("#insert_singer_type_dlg").dialog('open').dialog('setTitle', "添加歌手类型");
}

function doInsertSingerType() {
    $('#insertSingerTypeForm').form('submit', {
        url: "song/insertSingerType",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#insert_singer_type_dlg').dialog('close');
                $('#querySingerType').combobox('reload');
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
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
                $('#querySingerId').combobox('reload');
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function insertAlbumDialog() {
    $("#insert_album_dlg").dialog('open').dialog('setTitle', "添加专辑");
}

function doInsertAlbum() {
    $('#insertAlbumForm').form('submit', {
        url: "song/insertAlbum",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#insert_album_dlg').dialog('close');
                $('#queryAlbumId').combobox('reload');
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function insertSongDialog() {
    $('#albumId').combobox('reload');
    $('#singerId').combobox('reload');
    $("#insert_song_dlg").dialog('open').dialog('setTitle', "添加歌曲");
}

function doInsertSong() {
    var singerIds = $('#singerId').combobox('getValues');
    alert(singerIds);
    $('#insertSongForm').form('submit', {
        url: "song/insertSong",
        data:{singerId:singerIds},
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "success") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#insert_song_dlg').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function dialogUpdate(value) {
    var title = '请求明细';
    var url = 'song/updateSongTemplate?id=' + value;
    initWindow(title, url, 500, 360);
}

function doUpdate() {
    $('#updateForm').form('submit', {
        url: "song/updateSong",
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

