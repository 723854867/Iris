/**
 * Created by huoshanwei on 2015/10/29.
 */
function activityVideoSort(activityVideoId, activityId, type) {
    $.ajax({
        url: 'activity/activityVideoSort',
        data: {'type': type, 'activityId': activityId, "activityVideoId": activityVideoId},
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.resultCode == "ok") {
                doSearch()
            } else {
                showMessage("错误提示", result.resultMessage);
            }
        }
    });
}

function viewComment(id) {
    if (id > 0) {
        $("#viewCommentDialog").dialog('open').dialog('setTitle', "查看评价");
        $('#commentTable').datagrid({
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
            url: "activity/viewComment?id=" + id,
            columns: [
                [
                    {
                        field: 'name',
                        title: '<span class="columnTitle">用户昵称</span>',
                        width: 120,
                        align: 'center'
                    },
                    {
                        field: 'username',
                        title: '<span class="columnTitle">用户名</span>',
                        width: 120,
                        align: 'center'
                    },
                    {
                        field: 'content',
                        title: '<span class="columnTitle">内容</span>',
                        width: 80,
                        align: 'center'
                    },
                    {
                        field: 'createDateStr',
                        title: '<span class="columnTitle">时间</span>',
                        width: 80,
                        align: 'center'
                    }
                ]
            ],
            toolbar: "#tb",
            onLoadSuccess: function () {
                $('#commentTable').datagrid('clearSelections');
            },
            pageSize: 20,
            pageList: [20, 40, 60, 80, 100],
            beforePageText: '第', //页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
        });
    }
}

function publishVideos() {
    var ls = document.getElementsByName("ids");
    var f = false;
    var sids = "";
    for (var i = 0; i < ls.length; i++) {
        if ($(ls[i]).is(':checked')) {
            f = true;
            if (sids == "")
                sids += ls[i].value;
            else
                sids += "," + ls[i].value;
        }
    }
    if (f == false) {
        alert("请选择要发布的视频");
    } else {
        $.ajax({
            url: 'video/publish',
            data: {'videoIds': sids},
            type: "post",
            dataType: "json",
            success: function (result) {
                alert("发布成功");
                window.location.reload();
            }
        });
    }
}

function changePlayCount(obj, videoId) {
    var pc = "playCount" + videoId;
    var playCount = document.getElementById(pc);


    if (playCount && playCount.value) {
        $.ajax({
            url: 'video/changePlayCount',
            data: {'videoId': videoId, 'playCount': playCount.value},
            type: "post",
            dataType: "json",
            success: function (result) {
                //alert("修改成功!");
                window.location.reload();
            }
        });

    } else {
        alert("请填写播放次数！");
    }
}

function changePlayRateToday(obj, videoId) {
    var pc = "playRateToday" + videoId;
    var playRateToday = document.getElementById(pc);


    if (playRateToday && playRateToday.value) {
        $.ajax({
            url: 'video/changePlayRateToday',
            data: {'videoId': videoId, 'playRateToday': playRateToday.value},
            type: "post",
            dataType: "json",
            success: function (result) {
                //alert("修改成功!");
                window.location.reload();
            }
        });

    } else {
        alert("请填写播放率！");
    }
}

function updatePlayRateState(obj, videoId) {
    $.ajax({
        url: 'video/updatePlayRateState',
        data: {'videoId': videoId, 'playRateState': 0},
        type: "post",
        dataType: "json",
        success: function (result) {
            //alert("修改成功!");
            window.location.reload();
        }
    });
}

function updateActivityVideoOrderNum(obj, videoId, activityId) {

    var pc = "orderNum" + videoId;
    var orderNum = document.getElementById(pc);
    $.ajax({
        url: 'video/updateActivityVideoOrderNum',
        data: {'videoId': videoId, 'activityId': activityId, 'orderNum': orderNum.value},
        type: "post",
        dataType: "json",
        success: function (result) {
            //alert("修改成功!");
            window.location.reload();
        }
    });
}

function changeLogoState(obj, videoId, state) {

    $.ajax({
        url: 'video/changeLogoState',
        data: {'videoId': videoId, 'state': state},
        type: "post",
        dataType: "json",
        success: function (result) {
            //alert("修改成功!");
            window.location.reload();
        }
    });
}

function deleteVideos() {
    var ls = document.getElementsByName("ids");
    var f = false;
    var sids = "";
    for (var i = 0; i < ls.length; i++) {
        if ($(ls[i]).is(':checked')) {
            f = true;
            if (sids == "")
                sids += ls[i].value;
            else
                sids += "," + ls[i].value;
        }
    }
    if (f == false) {
        alert("请选择要删除的视频");
    } else {
        $.ajax({
            url: 'video/deleteVideo',
            data: {'videoIds': sids},
            type: "post",
            dataType: "json",
            success: function (result) {
                alert("删除成功");
                window.location.reload();
            }
        });
    }
}

function deleteActivity(id) {
    if (confirm("您确定要执行删除操作吗？")) {
        if (id > 0) {
            $.ajax({
                url: 'activity/deleteActivity',
                data: {'id': id},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.resultCode == "ok") {
                        alert(result.resultMessage);
                        history.back(-1);
                    } else {
                        alert(result.resultMessage);
                    }
                }
            });
        } else {
            alert("系统出错，请重试！");
        }
    }
}

function planPublish() {
    var ls = document.getElementsByName("ids");
    var f = false;
    var sids = "";
    for (var i = 0; i < ls.length; i++) {
        if ($(ls[i]).is(':checked')) {
            f = true;
            if (sids == "")
                sids += ls[i].value;
            else
                sids += "," + ls[i].value;
        }
    }
    if (f == false) {
        alert("请选择要计划发布的视频");
    } else {
        location.href = "video/prePlanPublish?videoIds=" + sids;
    }
}

function updateActiveStatus(obj, activityId, status) {
    $.ajax({
        url: 'activity/updateActiveStatus',
        data: {'activeId': activityId, 'status': status},
        type: "post",
        dataType: "json",
        success: function (result) {
            //alert("修改成功!");
            window.location.reload();
        }
    });
}

function updateOrderNum(obj, activeId) {
    var om = "om" + activeId;
    var orderNum = document.getElementById(om);
    if (orderNum && orderNum.value) {
        $.ajax({
            url: 'activity/updateOrderNum',
            data: {'activeId': activeId, 'orderNum': orderNum.value},
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result == 'ok') {
                    window.location.reload();
                } else if (result == 'exist') {
                    alert('该展示排序已存在！');
                }
                //alert("修改成功!");
            }
        });

    } else {
        alert("请填写播放次数！");
    }
}



function openWindow(url, name, width, height, feature) {

    var iWidth = 600; //弹出窗口的宽度;
    var iHeight = 800; //弹出窗口的高度;
    if (width && width != '') {
        iWidth = width;
    }
    if (height && height != '') {
        iHeight = height;
    }
    var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //获得窗口的垂直位置;
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; //获得窗口的水平位置;
    //alert(iTop);
    //alert(iLeft);
    if (!feature || feature == null || feature == '') {
        feature = "height=" + iHeight + ",width=" + iWidth + ", top=" + iTop + ", left=" + iLeft + ",alwaysRaised=yes,resizable=no,z-look=yes";
    } else {
        feature = "height=" + iHeight + ",width=" + iWidth + ", top=" + iTop + ", left=" + iLeft + "," + feature;
    }
    var rValue = window.open(url, name, feature);
    //alert('window');
    //alert(rValue);
    if (typeof (rValue) == "undefined") {
        rValue = window.ReturnValue;
    }

    return rValue;
}

function updateActiveStatus(obj, activityId, status) {

    $.ajax({
        url: 'activity/updateActiveStatus',
        data: {'activeId': activityId, 'status': status},
        type: "post",
        dataType: "json",
        success: function (result) {
            //alert("修改成功!");
            window.location.reload();
        }
    });
}


function removeActivityVideos() {
    if (confirm("确定要从此活动中移除吗？")) {
        var row = $("#displayTable").datagrid('getSelections');
        var idStr = "";
        for (var i = 0; i < row.length; i++) {
            idStr += row[i]["id"] + ","
        }
        var ids = idStr.substring(0, idStr.length - 1);
        if (row != null) {
            $.ajax({
                url: 'activity/removeActivityVideos',
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

function removeActivityVideoById(id) {
    if (confirm("确定要从此活动中移除吗")) {
        if (id > 0) {
            $.ajax({
                url: 'activity/removeActivityVideoById',
                data: {'id': id},
                type: "post",
                dataType: "json",
                success: function (result) {
                    showMessage("提示信息", result.resultMessage);
                    doSearch();
                }
            });
        } else {
            showMessage("错误提示", "此视频不存在！");
        }
    }
}

function batchDownload() {
    var row = $("#displayTable").datagrid('getSelections');
    var idStr = "";
    for (var i = 0; i < row.length; i++) {
        idStr += row[i]["videoId"] + ","
    }
    var ids = idStr.substring(0, idStr.length - 1);
    if (ids != "") {
        window.location.href = baseUrl + 'video/multidown?ids=' + ids;
    } else {
        showMessage("错误提示", "请至少选择一条！");
    }
}

function exportActivityVideosExcel(id) {
    var startTime = $("#startTime").datebox("getValue");
    var endTime = $("#endTime").datebox("getValue");
    /*var row = $("#displayTable").datagrid('getSelections');
     var idStr = "";
     for (var i = 0; i < row.length; i++) {
     idStr += row[i]["id"] + ","
     }
     var ids = idStr.substring(0, idStr.length - 1);
     if(ids != ""){*/
    window.location.href = baseUrl + 'activity/exportActivityVideosToExcel?activityId=' + id + '&startTime=' + startTime + '&endTime=' + endTime;
    /*}else{
     showMessage("错误提示", "请至少选择一条！");
     }*/
}

var actvid = 0;
function selectActivity(obj) {
    var actid = obj.value;
    if (obj.checked) {
        $.post("video/modifyvideoactivities", {
            videoId: actvid,
            activityId: actid,
            type: 'add'
        }, function (result) {
            if (result["success"] == true) {
                $(datagridId).datagrid('reload'); // reload the user data
            } else {
                showMessage("Error", result["message"]);
            }
        });
    } else {
        $.post("video/modifyvideoactivities", {
            videoId: actvid,
            activityId: actid,
            type: 'del'
        }, function (result) {
            if (result["success"] == true) {
                $(datagridId).datagrid('reload'); // reload the user data
            } else {
                showMessage("Error", result["message"]);
            }
        });
    }
}

function showActities(vid) {
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

function editActivityVideoOrderNum(id) {
    var orderNum = $("#orderNum" + id).val();
    $.ajax({
        url: 'activity/editActivityVideoOrderNum',
        type: 'post',
        data: {'id':id,'orderNum':orderNum},
        async: false, //默认为true 异步
        error: function () {
            showMessage('错误提示',"更新失败，请重试！");
        },
        success: function (data) {
            doSearch();
        }
    });
}