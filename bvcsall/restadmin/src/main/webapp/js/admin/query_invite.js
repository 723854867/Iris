/**
 * Created by huoshanwei on 2015/10/30.
 */
var datagridId = "#tt";
var editdialogueId = "#updatedlg";
var editFormId = "#updatefm";
var editTitle = "编辑编辑信息";
var noSelectedRowMessage = "你没有选择行";
var searchFormId = "#searchForm";
var listUrl = "inviteinfo/listpage";

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
        columns: [[
            {field: 'platformName', title: '邀请的平台名称', width: 35, align: 'center'},
            {field: 'platformMark', title: '邀请平台标示', width: 50, align: 'center'},
            {field: 'title', title: '标题', width: 35, align: 'center'},
            {
                field: 'picPath',
                title: '图片内容地址',
                width: 40,
                align: 'center',
                formatter: function (value, row, index) {
                    if ($.trim(row.picPath) != "") return "<img src='/restadmin/download" + row.picPath + "' width='60' height='50'/>";

                }
            },
            {field: 'description', title: '文字内容', width: 80, align: 'center'},
            {field: 'createDateStr', title: '创建时间', width: 40, align: 'center'},
            {field: 'modifyDateStr', title: '修改时间', width: 40, align: 'center'},
            {field: 'modifyName', title: '修改人员', width: 20, align: 'center'},
            {field: 'inviteInfoUrl', title: '邀请url', width: 120, align: 'center'},
            {
                field: 'status', title: '是否有效', align: 'center', width: 20,
                formatter: function (value, row, index) {
                    if (row.status == 1) return '有效';
                    if (row.status == 0) return '无效';
                }
            }
        ]],
        onLoadSuccess: function () {
            $(datagridId).datagrid('clearSelections');
        },
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
});

function updateinviteinfo(mydialogueId, myFormId) {
    var file1 = $("#updatefile1").val();
    var len1 = file1.length;
    if (len1 == 0) {
        //showMessage("Error", "请选择邀请内容图片");
    } else {
        var fileext = file1.substring(file1.lastIndexOf("."));
        if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
            showMessage("Error", "对不起，邀请内容图片仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
            return false;
        }
    }
    if ($(myFormId).form('validate')) {
        $(myFormId).submit();
    } else {
        return false;
    }
}

function doSearch() {
    $(datagridId).datagrid('reload', getFormJson(searchFormId));
}