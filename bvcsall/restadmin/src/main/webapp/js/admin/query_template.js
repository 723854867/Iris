/**
 * Created by huoshanwei on 2015/10/29.
 */
var datagridId = "#displayTable";
var adddialogueId = "#dlg";
var editdialogueId = "#updatedlg";
var addFormId = "#fm";
var editFormId = "#updatefm";
var addTitle = "新增片头";
var editTitle = "编辑片头";
var deleteConfirmMessage = "你确定要删除吗?";
var noSelectedRowMessage = "你没有选择行";
var searchFormId = "#searchForm";
var pageSize = 50;

var listUrl = "template/queryInitPageList";
var updateUrl = "template/updateTemp";
var deleteUrl = "template/deleteTemp";
var addUrl = "template/create";

var url;

$(function () {
    $(datagridId).datagrid({
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
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        columns: [[{
            field: 'img',
            title: '缩略图',
            width: 100,
            height: 100,
            align: "center",
            formatter: function (value, row, index) {
                if ($.trim(row.pic) != "") {
                    return "<img src='/restadmin/download" + row.pic + "' style='margin:0 auto;' width='90' height='90'/>";
                }
            }
        }, {
            field: 'title',
            title: '文件名称',
            width: 100
        }, {
            field: 'description',
            title: '文件描述',
            width: 100
        }, {
            field: 'type',
            title: '文件类型',
            width: 100,
            formatter: function (value, row, index) {
                if (row.type == "0") {
                    return "片头";
                } else if (row.type == "1"){
                    return "滤镜";
                }else if (row.type == "2"){
                    return "照片电影";
                }else if (row.type == "3"){
                    return "MV";
                }
            }
        },
            /*{
             field: 'orderNum',
             title: '排序',
             width: 100
             }, {
             field: 'createDateStr',
             title: '添加时间',
             width: 100
             },*/
            {
                field: 'activityTitle',
                title: '所属活动',
                width: 100
            },{
                field: 'versionNum',
                title: '版本号',
                width: 100
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

var url;
//删除片头
function destroyTemplate() {
    var row = $(datagridId).datagrid('getSelected');
    if (row) {
        $.messager.confirm('删除片头', deleteConfirmMessage, function (r) {
            if (r) {
                $.get(deleteUrl, {
                    id: row.id
                }, function (result) {
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
//弹出上传片头窗口
function newTemplate() {
    $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
    url = addUrl;
    $(addFormId).form('clear');
    $('#isEnabled').val('1');
    $('#isLocked').val('0');
    $('#type').val('admin');

    $('#addrolelist').combobox({
        valueField: 'id',
        textField: 'name',
        multiple: true,
        panelHeight: 'auto',
        url: 'user/rolelistAjax'
    });

}
//编辑片头
function editTemplate() {
    var row = $(datagridId).datagrid('getSelected');
    if (row) {
        $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
        $(editFormId).form('load', row);
        $("#file5_img").attr("src","/restadmin/download"+row.pic);
        $("#file6_img").attr("src","/restadmin/download"+row.backgroundPic);
        url = updateUrl;
    } else {
        showMessage("Error", noSelectedRowMessage);
    }
}
//保存上传片头
function saveTemplate(mydialogueId, myFormId, selectId, optType) {
    var val = $("#" + selectId).combobox("getValue");
    if (val == null) {
        showMessage("Error", "请选择文件类型!");
        return false;
    }
    if (optType == "add") {
        var file1 = $("#file1").val();
        var file2 = $("#file2").val();
        var file3 = $("#file3").val();

        var len1 = file1.length;
        var len2 = file2.length;
        var len3 = file3.length;

        if (len1 == 0) {
            showMessage("Error", "请选择上传文件");
            return false;
        } else {
            var Suffix = file1.substring(file1.lastIndexOf("."));
            if ((val==0||val==1)&&".mp4" != Suffix.toLowerCase()) {
                showMessage("Error", "对不起，系统仅支持mp4格式文件，请不要调皮!O(∩_∩)O谢谢~");
                return false;
            }
            
            if ((val==2||val==3)&&".zip" != Suffix.toLowerCase()) {
                showMessage("Error", "对不起，系统仅支持zip格式文件，请不要调皮!O(∩_∩)O谢谢~");
                return false;
            }
        }
        if (len2 == 0) {
            showMessage("Error", "请选择封面文件");
            return false;
        } else {
            var fileext = file2.substring(file2.lastIndexOf("."));
            if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                showMessage("Error", "对不起，封面图片仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
                return false;
            }
        }
        if (len3 == 0) {
            showMessage("Error", "请选择背景文件");
            return false;
        } else {
            var fileext = file3.substring(file3.lastIndexOf("."));
            if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                showMessage("Error", "对不起，背景图片仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
                return false;
            }
        }
    } else {
        var file4 = $("#file4").val();
        var len4 = file4.length;
        if (len4 > 0) {
            var Suffix = file4.substring(file4.lastIndexOf("."));
            if ((val==0||val==1)&&".mp4" != Suffix.toLowerCase()) {
                showMessage("Error", "对不起，系统仅支持mp4格式文件，请不要调皮!O(∩_∩)O谢谢~");
                return false;
            }
            
            if ((val==2||val==3)&&".zip" != Suffix.toLowerCase()) {
                showMessage("Error", "对不起，系统仅支持zip格式文件，请不要调皮!O(∩_∩)O谢谢~");
                return false;
            }
        }

        var file5 = $("#file5").val();
        var len5 = file5.length;
        if (len5 > 0) {
            var fileext = file5.substring(file5.lastIndexOf("."));
            if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                showMessage("Error", "对不起，封面图片仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
                return false;
            }
        }

        var file6 = $("#file6").val();
        var len6 = file6.length;
        if (len6 > 0) {
            var fileext = file6.substring(file6.lastIndexOf("."));
            if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                showMessage("Error", "对不起，背景图片仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
                return false;
            }
        }
    }
    if ($(myFormId).form('validate')) {
        $(myFormId).submit();
    } else {
        return false;
    }
}

//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.type = $('#type').combobox("getValue");
    queryParams.title = $('#title').val();
    queryParams.actId = $('#actIds').combobox("getValue");
    $('#displayTable').datagrid({url: listUrl});
}