/**
 * Created by huoshanwei on 2015/10/27.
 */

var deleteConfirmMessage = "你确定要删除吗?";
var adddialogueId = "#dlg";
var addTitle = "播放视频";

var x, y;
$(function () {
    $(document).mousemove(function (e) {
        e = e || window.event;
        x = e.pageX || (e.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft));
        y = e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));
    });
});

function changeNav(o) {
    //当前被中的对象设置css
    o.className = "nav_current";
    var j;
    var id;
    var e;
    //遍历所有的页签，没有被选中的设置其没有被选中的css
    for (var i = 1; i <= 2; i++) { //i<7 多少个栏目就填多大值
        id = "nav" + i;
        j = document.getElementById(id);
        e = document.getElementById("sub" + i);
        if (id != o.id) {
            j.className = "nav_link";
            e.style.display = "none";
        } else {
            e.style.display = "block";
        }
    }
}

function delVideo(id) {
    if (id) {
        $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
            if (r) {
                $.post('video/logicremove', {ids: id}, function (result) {
                    if (result["success"] == true) {
                        window.location.reload();
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


function deleteEvaluation(evaId) {
    $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
        if (r) {
            $.get('evaluation/remove', {id: evaId}, function (result) {
                if (result == "ok") {
                    window.location.reload();
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }
    });
}

function insertComment() {
    $("#insert_comment_dlg").dialog('open').dialog('setTitle', "添加评论");
}

function doInsertComment() {
    var videoId = $("#videoId").val();
    var content = $("#content").val();
    var majia = $("#creator_id").combogrid("getValue");
    $.ajax({
        url: 'evaluation/saveEvaluation',
        data: {"majia": majia, "videoId": videoId, "content": content},
        type: "post",
        dataType: "json",
        success: function (result) {
            $('#insert_comment_dlg').dialog('close');
            showMessage("提示信息", "添加成功！");
            window.location.reload();
        }
    });
}

//加入热门视频
function addHotVideo(vid) {
    $.post('video/addHotVideoTop', {id: vid}, function (result) {
        if (result["success"] == true) {
            showMessage("提示信息", "加入成功！");
            window.location.reload();
        } else {
            showMessage("错误提示", result["message"]);
        }
    });
}

//移除热门视频
function removeHotVideo(vid) {
    $.post('video/removeHotVideos', {ids: vid}, function (result) {
        if (result["success"] == true) {
            showMessage("提示信息", "移除成功！");
            window.location.reload();
        } else {
            showMessage("错误提示", result["message"]);
        }
    });
}

function deleteVideo(id) {
    if (id) {
        $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
            if (r) {
                $.post('video/logicremove', {ids: id}, function (result) {
                    if (result["success"] == true) {
                        showMessage("提示信息", "删除成功！");
                        history.go(-1);
                    } else {
                        showMessage("错误提示", result["message"]);
                    }
                });
            }
        });
    } else {
        showMessage("Error", noSelectedRowMessage);
    }
}

function showActivitiesDialog(vid) {
    if (vid == 'undefined') {
        showMessage("Error", "不合法的操作！");
    }
    actvid = vid;
    $.post("video/getvideoactivities", {id: vid}, function (result) {
        var acts = $("input[name='activity']");
        var checked_acts = $("input[name='activity']:checked");
        for (var r = 0; r < checked_acts.length; r++) {
            checked_acts[r].checked = false;
        }
        if (result.length > 0) {
            for (var i = 0; i < result.length; i++) {
                for (var j = 0; j < acts.length; j++) {
                    if (result[i].activityid == acts[j].value) {
                        acts[j].checked = true;
                    }
                }
            }
        }
    });
    $("#activityDialog").show();
    $("#activityDialog").dialog({
        title: '选择活动',
        width: 400,
        height: 300,
        closed: false,
        cache: true,
        modal: true
    });
}

var actvid = 0;
function selectActivity(obj) {
    var actid = obj.value;
    if (obj.checked) {
        $.post("video/modifyvideoactivities", {videoId: actvid, activityId: actid, type: 'add'}, function (result) {
            if (result["success"] == true) {
                $(datagridId).datagrid('reload'); // reload the user data
            } else {
                showMessage("Error", result["message"]);
            }
        });
    } else {
        $.post("video/modifyvideoactivities", {videoId: actvid, activityId: actid, type: 'del'}, function (result) {
            if (result["success"] == true) {
                $(datagridId).datagrid('reload'); // reload the user data
            } else {
                showMessage("Error", result["message"]);
            }
        });
    }
}

var listUrl = "video/queryEvaluationListByVideoId";
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
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        queryParams: {
            videoId: $("#videoId").val()
        },
        columns: [
            [
                {field: 'creatorName', title: '<span class="columnTitle">评论人</span>', width: 120, align: 'center'},
                {field: 'content', title: '<span class="columnTitle">内容</span>', width: 120, align: 'center'},
                {field: 'createDate', title: '<span class="columnTitle">评论时间</span>', width: 80, align: 'center'},
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 160, align: 'center',
                    formatter: function (value, row) {
                        var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteEvaluation(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                        return deleteStr;
                    }
                }
            ]
        ],
        toolbar: "#tb",
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
