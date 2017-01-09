/**
 * Created by huoshanwei on 2015/10/30.
 */
var datagridId = "#displayTable";
var listUrl = "sensitiveWord/querySensitiveWordList";
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
            field: 'word',
            title: '内容',
            width: 100,
            align:'center'
        }, {
            field: 'status',
            title: '状态',
            width: 100,
            align:'center',
            formatter: function (value, row, index) {
                if (value == 0) return '屏蔽';
                if (value == 1) return '暂停屏蔽';
            }
        }, {
            field: 'createDateStr',
            title: '创建时间',
            width: 100,
            align:'center'
        },
            {
                field: 'modify',
                title: '操作',
                width: 100,
                align:'center',
                formatter: function (value, row, index) {
                    var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteSensitiveWord(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                    return deleteStr;
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


function displayImport() {
    $("#dialog").show();
    $("#dialog").dialog();
}

function deleteSensitiveWord(value) {
    if (confirm("您确定执行删除操作吗？")) {
        $.ajax({
            url: 'sensitiveWord/deleteSensitiveWord',
            data: {'id': value},
            type: "post",
            dataType: "json",
            success: function (result) {
                showMessage("提示信息", result.resultMessage);
                doSearch();
            }
        });
    }
}

function doInsert() {
    var word = $("#wordInsert").val();
    if (word != "") {
        $.ajax({
            url: 'sensitiveWord/doInsert',
            data: {'word': word},
            type: "post",
            dataType: "json",
            success: function (result) {
                showMessage("提示信息", result.resultMessage);
                doSearch();
            }
        });
    } else {
        showMessage("错误信息", "敏感词不能为空！");
    }
}

function closeDialog() {
    $('#dialog').dialog('close');
}

function doBatchImport() {
    var url = "sensitiveWord/doBatchImport";
    $('#batchImportForm').form('submit', {
        url: url,
        success: function (response) {
            var parsedJson = jQuery.parseJSON(response);
            if (parsedJson.resultCode == "ok") {
                showMessage("提示信息", parsedJson.resultMessage);
                $('#dialog').dialog('close');
                doSearch();
            } else {
                showMessage("错误信息", parsedJson.resultMessage);
            }
        }
    });
}

function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.word = $('#word').val();
    $('#displayTable').datagrid({url: listUrl});
}