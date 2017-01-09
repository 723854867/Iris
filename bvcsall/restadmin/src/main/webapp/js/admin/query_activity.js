/**
 * Created by huoshanwei on 2015/10/29.
 */
var listUrl = "activity/queryActivityList";
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
        columns: [
            [
                {field: 'ck', wdith: 100, checkbox: true},
                {
                    field: 'cover', title: '<span class="columnTitle">活动图片</span>', width: 150,align:'center',
                    formatter: function (value, row) {
                        if (value == null) {
                            return "";
                        } else {
                            return "<div><img style='height:100px;cursor: pointer;border: none;margin: 3px auto;' src='/restadmin/download" + value + "' title='活动图片'/></div>";
                        }
                    }
                },
                {field: 'title', title: '<span class="columnTitle">活动标题</span>', width: 120, align: 'center'},
                {
                    field: 'status', title: '<span class="columnTitle">状态</span>', width: 80, align: 'center',
                    formatter: function (value) {
                        if (value == 1) {
                            return "进行中";
                        } else {
                            //return formatDate(value);
                            return "下线";
                        }
                    }
                },
                {
                    field: 'details',
                    title: '<span class="columnTitle">详情参数</span>',
                    width: 150,
                    align: 'center',
                    formatter: function (value, row) {
                        var createDateParam = "<span>上传时间：</span><span>" + row.createDateStr + "</span>";
                        var dataStr = '<div><a class="easyui-linkbutton" href="javascript:void(0)" onclick="showData(' + row.id + ');" style="width:80px;height: 25px;" title="数据" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">数据</span><span class="l-btn-icon icon-back">&nbsp;</span></span></a></div>';
                        /*var content = "参与视频:" + row.avCount
                         + "&nbsp;&nbsp;参与人数:" + row.rCount
                         + "&nbsp;&nbsp;点赞总数:" + row.pCount
                         + "&nbsp;&nbsp;评论总数:" + row.eCount + "&nbsp;&nbsp;";*/
                        var content = "<div id='datas" + row.id + "'></div>"
                        dataStr += '<div>' + content + '</div>';
                        return createDateParam + dataStr;
                    }
                },
                {
                    field: 'order_num', title: '<span class="columnTitle">排序</span>', width: 120,align:'center',
                    formatter: function (value, row) {
                        var sortStr = '<div><span><a href="javascript:;" onclick="activitySort('+row.id+',1)">置顶</a></span></div><div><span><a href="javascript:;" onclick="activitySort('+row.id+',2)">上移</a></span></div><div><span><a href="javascript:;" onclick="activitySort('+row.id+',3)">下移</a></span></div>';
                        var editStr = '<input onblur="editActivityOrderNum(' + row.id + ')" id="orderNum' + row.id + '" name="orderNum" value="' + value + '" class="easyui-numberbox" min="0" max="" precision="0" maxlength="5" size="5"/>';
                        return sortStr+editStr;
                    }
                },
                {field: 'source', title: '<span class="columnTitle">来源</span>', width: 70,align:'center'},
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 120,align:'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" href="activity/showActivityAdd?activetyId=' + row.id + '" style="width:80px;height: 25px;" id="mdOM' + row.id + '" name="mdOM' + row.id + '" type="button" title="编辑" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var detailStr = '<a class="easyui-linkbutton" href="activity/queryActivityDetail?activityId=' + row.id + '" style="width:80px;height: 25px;" id="mdOM' + row.id + '" name="mdOM' + row.id + '" type="button" title="详细" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">查看</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        var deleteStr = '<a class="easyui-linkbutton" href="javascript:void(0)" onclick="deleteActivity(' + row.id + ');" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                        if (row.status == 0) {
                            var optionStr = '<div><a class="easyui-linkbutton" plain="true" href="javascript:;" style="width:80px;height: 30px;" id="mdOM' + row.id + '" name="mdOM' + row.id + '" value="上线" onclick="updateActiveStatus(this,\'' + row.id + '\',1);"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">上线</span><span class="l-btn-icon icon-add">&nbsp;</span></span></a>' + " "
                                + editStr + '</div> <div>' + detailStr + ' '+deleteStr+'</div>';
                        } else {
                            var optionStr = '<div><a class="easyui-linkbutton" plain="true" href="javascript:;" style="width:80px;height: 30px;" id="mdOM' + row.id + '" name="mdOM' + row.id + '" value="下线" onclick="updateActiveStatus(this,\'' + row.id + '\',0);" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">下线</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>' + " "
                                + editStr + '</div> <div>' + detailStr + ' '+deleteStr+'</div>';
                        }
                        var prizeStr = '<div><a class="easyui-linkbutton" plain="true" href="activity/forwardQueryActivityPrizes?activityId='+ row.id +'"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">中奖活动</span><span class="l-btn-icon icon-detail">&nbsp;</span></span></a></div>';
                        return optionStr + prizeStr;
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

//排序
function activitySort(activityId,type){
    $.ajax({
        url: 'activity/activitySort',
        data: {'type':type,'activityId': activityId},
        type: "post",
        dataType: "json",
        success: function (result) {
            if(result.resultCode == "ok"){
                doSearch()
            }else{
                showMessage("错误提示",result.resultMessage);
            }
        }
    });
}

//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.groupType = $('#groupType').combobox("getValue");
    queryParams.status = $('#status').combobox("getValue");
    queryParams.source = $('#source').combobox("getValue");
    queryParams.title = $('#title').val();
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');
    /*            queryParams.pStartTime = $('#pStartTime').datebox('getValue');
     queryParams.pEndTime = $('#pEndTime').datebox('getValue');*/
    $('#displayTable').datagrid({url: listUrl});
}

/*function updateOrderNum(obj, activeId) {
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
                    doSearch();
                } else if (result == 'exist') {
                    alert('该展示排序已存在！');
                }
                //alert("修改成功!");

            }
        });

    } else {
        alert("请填写播放次数！");
    }
}*/

function updateActiveStatus(obj, activityId, status) {

    $.ajax({
        url: 'activity/updateActiveStatus',
        data: {'activeId': activityId, 'status': status},
        type: "post",
        dataType: "json",
        success: function (result) {
            //alert("修改成功!");
            doSearch();
        }
    });
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
                        doSearch();
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

/*function deleteVideos() {
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
                doSearch();
            }
        });
    }
}*/

/*function activityDetail(obj, activityId) {
    $("#videoform").attr("action", "activity/activityDetail?activityId=" + activityId);
    $("#videoform").attr("method", "GET");
    $("#videoform").submit();
}*/

function onLineAll(type) {
    if (confirm("您确定要执行此操作吗？")) {
        var selectObj = $('#displayTable').datagrid("getSelections");
        var idStr = "";
        for (var i = 0; i < selectObj.length; i++) {
            idStr += selectObj[i]["id"] + ","
        }
        var ids = idStr.substring(0, idStr.length - 1);
        if (ids.length <= 0) {
            alert("请至少选择一行！");
        } else {
            $.ajax({
                url: 'activity/batchOnline',
                data: {'ids': ids, "type": type},
                type: "post",
                dataType: "json",
                success: function (result) {
                    doSearch();
                }
            });
        }
    }
}

function showData(id) {
    $.ajax({
        url: 'activity/datas',
        data: {'id': id},
        type: "get",
        dataType: "json",
        success: function (result) {
            var content = "<div>马甲视频:" + result['majiaVideoCount']
                +"&nbsp;&nbsp;我拍视频:" + result['userVideoCount']
                + "</div><div>参与人数:" + result['userCount']
                + "&nbsp;&nbsp;点赞总数:" + result['praiseCount']
                + "&nbsp;&nbsp;评论总数:" + result['evaluationCount'] + "</div>";
            $("#datas" + id).html(content);
            $("#datas" + id).attr("style", "display:block");
        }
    });
}

function editActivityOrderNum(id) {
    var orderNum = $("#orderNum" + id).val();
    $.ajax({
        url: 'activity/editActivityOrderNum',
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