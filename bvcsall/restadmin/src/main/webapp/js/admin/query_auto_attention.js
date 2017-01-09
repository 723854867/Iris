var datagridId = "#tt";
var deleteConfirmMessage = "你确定要删除吗?";
var searchFormId = "#searchForm";

var listUrl = "autoAttention/searchListPage";

function datagridList() {
    $(datagridId).datagrid({
        fitColumns: true,
        rownumbers: true,
        striped: true,
        pagination: true,
        pageNumber: 1,
        fit: true,
        pagePosition: 'bottom',
        singleSelect: true,
        selectOnCheck: false,
        nowrap: true,
        url: listUrl,
        columns: [[
            {field: 'userId', title: '用户id', align: 'center', width: 100},
            {field: 'name', title: '昵称', align: 'center', width: 100},
            {field: 'vipStat', title: 'VIP', align: 'center', width: 100,formatter: function (value) {
                if (value == "0") {
                    return "普通"
                } else if (value == "1") {
                    return "蓝V";
                } else if (value == "2") {
                    return "黄V";
                } else if (value == "3") {
                    return "绿V";
                }
            }
            	
            },
            {field: 'endTime', title: '结束时间', align: 'center', width: 100},
            {
                field: 'vaf', title: '操作', width: 200, align: 'center', formatter: function (value, row, index) {
                var deleteOpt = "<span style='color:#969696;'><a href='javascript:void(0)' onclick='deleteBanner(" + row.userId + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>删除</span><span class='l-btn-icon icon-cut'>&nbsp;</span></span></a></span>";
                return deleteOpt;
            }
            }
        ]],
        onLoadSuccess: function () {
            $(datagridId).datagrid('clearSelections');
        }
    });
}

function showAble(id, val) {
    $.post('banner/showable', {id: id, show: val}, function (result) {
        if (result["success"] == true) {
            $(datagridId).datagrid('reload'); // reload the user data
        } else {
            showMessage("Error", result["message"]);
        }
    });
}

$(function () {
    datagridList();
    doSearch();
});
var x, y;
$(function () {
    $('#starttime').datetimebox({
        showSeconds: false
    });
    $('#endtime').datetimebox({
        showSeconds: false
    });

    $(document).mousemove(function (e) {
        e = e || window.event;
        x = e.pageX || (e.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft));
        y = e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));
    });
});

function deleteBanner(id) {
    $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
        if (r) {
            $.post('autoAttention/remove', {userId: id}, function (result) {
                if (result["success"] == true) {
                    $(datagridId).datagrid('reload'); // reload the user data
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }
    });
}

function doSearch() {
    $(datagridId).datagrid('reload', getFormJson(searchFormId));
}


function upload() {
    location.href = "autoAttention/new";
}
