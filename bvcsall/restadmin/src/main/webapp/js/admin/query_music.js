/**
 * Created by huoshanwei on 2015/10/29.
 */
var adddialogueId = "#dlg";
var addFormId = "#fm";
var addTitle = "新增音乐";
var deleteConfirmMessage = "你确定要删除吗?";
var noSelectedRowMessage = "你没有选择行";
var updateUrl = "template/updateTemp";
var deleteUrl = "template/deleteTemp";
var addUrl = "music/create";
var listUrl = "music/queryMusicList";

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
        singleSelect: false, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        rowStyler: function (index, row) {
            if (row.status != 1) {
                return 'color:red;';
            }
        },
        columns: [
            [
                {
                    field: 'ck',
                    checkbox: true
                },
                {
                    field: 'faceUrl',
                    title: '<span class="columnTitle">缩略图</span>',
                    width: 120,
                    align: 'center',
                    formatter: function (value, row) {
                        if (value == null) {
                            return "";
                        } else {
                            return "<div>" +
                                "<span><img style='height:100px;padding-left:10px;cursor: pointer;border: none;margin: 3px auto;' src='/restadmin/download/" + value + "' title='缩略图'/></span>" +
                                "</div>";
                        }
                    }
                },
                {field: 'name', title: '<span class="columnTitle">名称</span>', width: 80, align: 'center'},
                {
                    field: 'createPerson',
                    title: '<span class="columnTitle">上传人</span>',
                    width: 80,
                    align: 'center'
                },
                {
                    field: 'createDateStr',
                    title: '<span class="columnTitle">上传时间</span>',
                    width: 80,
                    align: 'center'
                },
                {
                	field: 'orderNumber',
                	title: '<span class="columnTitle">权重</span>',
                	width: 80,
                	align: 'center'
                },
                {
                    field: 'status', title: '<span class="columnTitle">状态</span>', width: 80, align: 'center',
                    formatter: function (value, row) {
                        if (value == 1) {
                            return "有效";
                        } else {
                            return "无效";
                        }
                    }
                },
                {field: 'typeName', title: '<span class="columnTitle">类型</span>', width: 80, align: 'center'},
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                        var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteMusic(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                        var editStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="updateMusicDialog(' + row.id + ')" style="width:80px;height: 25px;" title="修改"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">修改</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var playStr = "<span id='playerContainer" + row.id + "'>" +
                            "<button style='width:60px;height: 25px;overflow: hidden;' class='button blue bigrounded' onclick='playMusic(" + row.id + ",\"" + row.url + "\",\"" + row.faceUrl + "\")' >播放</button>" +
                            "</span>";
                        return editStr + " " + deleteStr + " " + playStr;
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
    queryParams.name = $('#name').val();
    queryParams.musicTypeId = $('#musicTypeId').combobox('getValue');
    queryParams.createPerson = $('#createPerson').combobox('getValue');
    queryParams.startDate = $('#startDate').val();
    queryParams.endDate = $('#endDate').val();
    queryParams.status = $('#musicState').combobox('getValue');
    $('#displayTable').datagrid({url: listUrl});
}

//弹出上传音乐窗口
function musicAdd() {
    $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
    url = addUrl;
    $(addFormId).form('clear');

}
//编辑音乐
function editMusic() {
    var row = $(datagridId).datagrid('getSelected');
    if (row) {
        $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
        $(editFormId).form('load', row);
        url = updateUrl;
    } else {
        showMessage("Error", noSelectedRowMessage);
    }
}
//保存上传音乐
function saveMusic(mydialogueId, myFormId) {
    var size = getFileSize("file2");
    if (size == 0) {
        if ($("#musicId").val() == '') {
            showMessage("提示信息","请选择音乐文件！");
            return false;
        }
    }
    var musicType = $("#musicType").combobox("getValue");
    if (musicType == null) {
        showMessage("提示信息","请选择音乐类型！");
        return false;
    }
    var status = $("#musicStatus").combobox("getValue");
    if (status == null) {
        showMessage("提示信息","请选择启用状态！");
        return false;
    }
    if ($(myFormId).form('validate')) {
        $(myFormId).submit();
    } else {
        return false;
    }
}

function getFileSize(elementId) {
    var agent = window.navigator.userAgent;
    var fileupload = document.getElementById(elementId);
    var isIE6 = agent.indexOf('MSIE 6.0') != -1;
    var isIE = agent.indexOf('MSIE') != -1;
    // var maxSize = 2000000;
    var fileSize = 0;
    try {

        if (isIE6) {//ie6
            filepath = fileupload.value;
            var aa = new ActiveXObject("Scripting.FileSystemObject");
            fileSize = aa.GetFile(filepath).size;
        } else if (isIE) {//其他ie
            fileupload.select();
            filepath = document.selection.createRange().text;
            var aa = new ActiveXObject("Scripting.FileSystemObject");
            fileSize = aa.GetFile(filepath).size;
        } else {//姑且w3c标准
            var fileName = fileupload.files[0].name;
            var fileext = fileName.substring(fileName.lastIndexOf("."));
            fileext = fileext.toLowerCase();
            if (fileext != '.mp3') {
                alert("仅支持mp3格式");
                $("#warnning").html("请选择音乐文件！");
                return 0;
            }
            fileSize = fileupload.files[0].size;
        }
        return fileSize;

    } catch (e) {
        //alert("请修改IE浏览器ActiveX安全设置为启用~！");
        return fileSize;
    }
    return fileSize;
}

function changeMusicStatus(obj, musicId, status) {
    $.ajax({
        url: 'music/changeMusicStatus',
        data: {'musicId': musicId, 'status': status},
        type: "post",
        dataType: "json",
        success: function (result) {
            //alert("修改成功!");
            doSearch();
        }
    });
}


function playMusic(musicId, fileName, imageUrl) {
    var _player = null;
    var player = $('<div/>');
    $(player).attr('id', 'pl' + musicId);
    $('#playerContainer' + musicId).empty();
    $('#playerContainer' + musicId).append(player);
    var conf = {
        file: baseUrl + "/download" + fileName,
        image: "/download" + imageUrl,
        height: 30,
        width: 200,
        autostart: true,
        analytics: {enabled: false}
    };
    _player = jwplayer('pl' + musicId).setup(conf);
}

function musicModify(id) {
    $(addFormId).form('clear');
    $.ajax({
        url: 'music/findOne',
        data: {'musicId': id},
        type: "get",
        dataType: "json",
        success: function (result) {
            if (result["success"] == true) {
                var music = result["result"];
                $("#musicId").val(music.id);
                $("#musicName").val(music.name);
                $("#desc").val(music.description);
                $("#musicType").val(music.typeId);
                $("#musicStatus").val(music.status);
                $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
            }
        }
    });
}

function deleteMusics() {
    if (confirm("确定要执行删除操作吗？")) {
        var row = $("#displayTable").datagrid('getSelections');
        var idStr = "";
        for (var i = 0; i < row.length; i++) {
            idStr += row[i]["id"] + ","
        }
        var ids = idStr.substring(0, idStr.length - 1);
        if (row != null) {
            $.ajax({
                url: 'music/deleteMusics',
                data: {'ids': ids},
                type: "post",
                dataType: "json",
                success: function (result) {
                    showMessage("提示信息", result.resultMessage);
                    doSearch();
                }
            });
        } else {
            showMessage("错误提示", "请至少选择一条！");
        }
    }
}

function deleteMusic(id) {
    if (confirm("确定要执行删除操作吗？")) {
        if (id != null) {
            $.ajax({
                url: 'music/deleteMusic',
                data: {'id': id},
                type: "post",
                dataType: "json",
                success: function (result) {
                    showMessage("提示信息", result.resultMessage);
                    doSearch();
                }
            });
        } else {
            showMessage("错误提示", "ID不存在！");
        }
    }
}

function updateMusicDialog(id) {
    if (id != null) {
        $("#updateDialog").dialog('open').dialog('setTitle', "更新音乐");
        $.ajax({
            url: 'music/queryMusic',
            data: {'id': id},
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.resultCode == "ok") {
                    $("#update_name").val(result.data.name);
                    $("#update_description").val(result.data.description);
                    $("#update_orderNumber").val(result.data.orderNumber);
                    $("#update_type_id").combobox("select", result.data.typeId);
                    $("#update_status").combobox("select", result.data.status);
                    $("#update_id").val(result.data.id);
                    $("#faceUrlImg").attr("src","/restadmin/download"+result.data.faceUrl);
                    $("#dialog_button").attr("onclick", "doUpdate()");
                    $("#dialog_close_button").attr("onclick", "closeDialog('update')");
                } else {
                    showMessage("错误提示", result.resultMessage)
                }
            }
        });
    } else {
        showMessage("错误提示", "ID不存在！");
    }
}

function insertMusicDialog(){
    $("#insertDialog").dialog('open').dialog('setTitle', "添加音乐");
    $("#insert_dialog_close_button").attr("onclick", "closeDialog('insert')");
}

function doInsert(){
    $('#insertForm').form('submit', {
        url: "music/insertMusic",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "ok") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#insertDialog').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function doUpdate(){
    $('#updateForm').form('submit', {
        url: "music/updateMusic",
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "ok") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#updateDialog').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function closeDialog(value) {
    if (value == "insert") {
        $('#insertDialog').dialog('close');
    } else {
        $('#updateDialog').dialog('close');
    }
}