/**
 * Created by huoshanwei on 2015/10/29.
 */
var listUrl = "liveActivity/queryLiveActivityList";
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
                {field: 'id', title: '<span class="columnTitle">活动id</span>', width: 30, align: 'center'},
                {
                    field: 'cover', title: '<span class="columnTitle">活动图片</span>', width: 120,align:'center',
                    formatter: function (value, row) {
                        if (value == null) {
                            return "";
                        } else {
                            return "<div><img style='height:100px;cursor: pointer;border: none;margin: 3px auto;' src='/restadmin/download" + value + "' title='活动图片'/></div>";
                        }
                    }
                },
                {field: 'title', title: '<span class="columnTitle">活动标题</span>', width: 120, align: 'center'},
                {field: 'gifts', title: '<span class="columnTitle">专属礼物</span>', width: 100, align: 'center'},
                {field: 'createTime', title: '<span class="columnTitle">创建时间</span>', width: 80, align: 'center'},
                {field: 'startTime', title: '<span class="columnTitle">活动时间</span>', width: 100,align:'center',
                    formatter : function (value, row) {
                        return "开始：" + row.startTime + "<br/>结束：" + row.endTime;
                    }
                },
                {field: 'orderNum', title: '<span class="columnTitle">权重</span>', width: 20,align:'center'},
                {field: 'type', title: '<span class="columnTitle">活动类型</span>', width: 40,align:'center',
                    formatter :function (value) {
                        if(value ==1) {
                            return "<span style='color: orangered'>App活动</h6>";
                        } else if (value == 2) {
                            return "<span style='color: darkslategray'>H5活动</h6>";
                        } else {
                            return "";
                        }
                    }
                },
                {field: 'status', title: '<span class="columnTitle">上线状态</span>', width: 30,align:'center',
                    formatter :function (value, row) {
                        if(row.status ==1) {
                            return "<span style='color: forestgreen'>上线</h6>";
                        } else {
                            return "<span style='color: red'>下线</span>";
                        }
                    }
                },

                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 50,align:'center',
                    formatter: function (value, row) {
                        var editStr = '<a class="easyui-linkbutton" href="liveActivity/edit?activityId=' + row.id + '" style="width:80px;height: 25px;" id="mdOM' + row.id + '" name="mdOM' + row.id + '" type="button" title="编辑" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                        var detailStr = '<a class="easyui-linkbutton" href="liveActivity/forwardLiveActivityDetail?liveActivityId=' + row.id + '" style="width:80px;height: 25px;" id="mdOM' + row.id + '" name="mdOM' + row.id + '" type="button" title="详细" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">查看</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        //var detailStr = '<a class="easyui-linkbutton" href="activity/queryActivityDetail?activityId=' + row.id + '" style="width:80px;height: 25px;" id="mdOM' + row.id + '" name="mdOM' + row.id + '" type="button" title="详细" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">查看</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
                        //var deleteStr = '<a class="easyui-linkbutton" href="javascript:void(0)" onclick="deleteActivity(' + row.id + ');" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-cut">&nbsp;</span></span></a>';
                        //if (row.status == 0) {
                        //    var optionStr = '<div><a class="easyui-linkbutton" plain="true" href="javascript:;" style="width:80px;height: 30px;" id="mdOM' + row.id + '" name="mdOM' + row.id + '" value="上线" onclick="updateActiveStatus(this,\'' + row.id + '\',1);"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">上线</span><span class="l-btn-icon icon-add">&nbsp;</span></span></a>' + " "
                        //        + editStr + '</div> <div>' + detailStr + ' '+deleteStr+'</div>';
                        //} else {
                        //    var optionStr = '<div><a class="easyui-linkbutton" plain="true" href="javascript:;" style="width:80px;height: 30px;" id="mdOM' + row.id + '" name="mdOM' + row.id + '" value="下线" onclick="updateActiveStatus(this,\'' + row.id + '\',0);" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">下线</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>' + " "
                        //        + editStr + '</div> <div>' + detailStr + ' '+deleteStr+'</div>';
                        //}
                        return editStr + " " +detailStr;
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
    queryParams.status = $('#status').combobox("getValue");
    queryParams.title = $('#title').val();
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');

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
                url: 'liveActivity/deleteLiveActivity',
                data: {'activityId': id},
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

function onLineAll(status) {
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
                url: 'liveActivity/updateStatus',
                data: {'ids': ids, "status": status},
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