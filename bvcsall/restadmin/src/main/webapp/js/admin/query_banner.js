/**
 * Created by huoshanwei on 2015/10/30.
 */
var datagridId = "#tt";
var deleteConfirmMessage = "你确定要删除吗?";
var searchFormId = "#searchForm";

var listUrl = "banner/searchListPage";

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
            {
                field: 'imgSrc',
                title: "缩略图",
                width: 100,
                height: 100,
                align: 'center',
                formatter: function (value, row, index) {
                    return "<img src='/restadmin/download" + value + "' width='100' height='100' />";
                }
            },
            {field: 'title', title: '标题', align: 'center', width: 100},
            {
                field: 'bannerType', title: 'banner类型', align: 'center', width: 100,
                formatter: function (value, row, index) {
                    if (value == "1") {
                        return "首页"
                    } else if (value == "2") {
                        return "发现页";
                    } else if (value == "3") {
                        return "首页新歌声";
                    } else if (value == "4") {
                        return "新歌声专区";
                    } else if (value == "5") {
                        return "学员榜";
                    } else if (value == "6") {
                        return "综艺榜";
                    } else if (value == "7") {
                        return "综艺预告榜";
                    } else if (value == "8") {
                        return "主播榜";
                    } else if (value == "9") {
                        return "贡献榜";
                    } else if (value == "10") {
                        return "贡献总榜";
                    } else if (value == "11") {
                        return "本期人气";
                    } else {
                        return "其它";
                    }
                }
            },
            {
                field: 'targetType', title: '点击操作', align: 'center', width: 100,
                formatter: function (value, row, index) {
                    //activity：活动、video：视频、user：用户中心、h5：页面 live:直播 rvideo:视频推荐
                    if (value == "activity") {
                        return "活动"
                    } else if (value == "video") {
                        return "视频";
                    } else if (value == "user") {
                        return "用户中心";
                    } else if (value == "h5") {
                        return "页面"+row.targetUrl;
                    } else if (value == "live") {
                        return "直播";
                    } else if (value == "revideo") {
                        return "回放";
                    } else if (value == "hotLabel") {
                        return "热门话题";
                    } else {
                        return "其它";
                    }
                }
            },
            {field: 'createDateStr', title: '创建时间', align: 'center', width: 100},
            {
                field: 'tag', title: '角标', align: 'center', width: 100, formatter: function (value, row, index) {
                if (value == "0") {
                    return "hot"
                } else if (value == "1") {
                    return "new";
                } else if (value == "2") {
                    return "火爆";
                }else if (value == "3") {
                    return "预告";
                }else if (value == "4") {
                    return "直播";
                }
            }
            },
            {
                field: 'orderNum', title: '显示顺序', width: 100, align: 'center',
                formatter: function (value, row) {
                    var sortStr = '<div><span><a href="javascript:;" onclick="bannerSort(' + row.id + ',1)">置顶</a></span></div><div><span><a href="javascript:;" onclick="bannerSort(' + row.id + ',2)">上移</a></span></div><div><span><a href="javascript:;" onclick="bannerSort(' + row.id + ',3)">下移</a></span></div>';
                    var editStr = '<input disabled="true" name="orderNum" value="' + value + '" class="easyui-numberbox" min="0" max="" precision="0" maxlength="5" size="5"/>';
                    return sortStr + editStr;
                }
            },
            {
                field: 'vaf', title: '操作', width: 200, align: 'center', formatter: function (value, row, index) {
                var deleteOpt = "<span style='color:#969696;'><a href='javascript:void(0)' onclick='deleteBanner(" + row.id + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>删除</span><span class='l-btn-icon icon-cut'>&nbsp;</span></span></a></span>";
                var editOpt = "<span style='color:#969696;'><a href='banner/modifybanner?id=" + row.id + "'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>修改</span><span class='l-btn-icon icon-edit'>&nbsp;</span></span></a></span>";
                if (row.showAble == '0') {
                    var showOpt = "<span style='color:#969696;'><a href='javascript:void(0)' onclick='showAble(" + row.id + ",1)'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>隐藏</span><span class='l-btn-icon icon-clear'>&nbsp;</span></span></a></span>";
                } else {
                    var showOpt = "<span style='color:#969696;'><a href='javascript:void(0)' onclick='showAble(" + row.id + ",0)'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>显示</span><span class='l-btn-icon icon-tip'>&nbsp;</span></span></a></span>";
                }
                return deleteOpt + " " + editOpt + " " + showOpt;
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
            $.post('banner/remove', {id: id}, function (result) {
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

function bannerSort(id, type) {
    $.ajax({
        url: 'banner/bannerSort',
        data: {'id': id, 'type': type},
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.resultCode == "success") {
                showMessage("提示信息", result.resultMessage);
                doSearch()
            } else {
                showMessage("错误提示", result.resultMessage);
            }
        }
    });
}

function upload() {
    location.href = "banner/newbanner";
}
