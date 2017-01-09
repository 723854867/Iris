<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>直播活动详情</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        $(function () {
            $('#displayTable').datagrid({
                nowrap: true, //是否换行
                autoRowHeight: true, //自动行高
                fitColumns: true,
                fit: true,
                striped: true,
                pageNumber: 1,
                collapsible: true, //是否可折叠
                remoteSort: false,
                singleSelect: true, //是否单选
                pagination: true, //分页控件
                rownumbers: true, //行号
                pagePosition: 'bottom',
                scrollbarSize: 0,
                loadMsg: "数据加载中.....",
                queryParams: {
                    liveActivityId: $("#liveActivityId").val()
                },
                url: "liveActivity/queryRuserLiveActivityDetailRecord",
                columns: [
                    [
                        {
                            field: 'userId',
                            title: '<span class="columnTitle">用户ID</span>',
                            width: 120,
                            align: 'center'
                        },
                        {
                            field: 'name',
                            title: '<span class="columnTitle">昵称</span>',
                            width: 80,
                            align: 'center'
                        },
                        {
                            field: 'giftNumber', title: '<span class="columnTitle">金豆数</span>', width: 70, align: 'center'
                        },
                        {
                            field: 'maxAccessNumber', title: '<span class="columnTitle">访问人数</span>', width: 70, align: 'center'
                        }

                    ]
                ],
                toolbar: "#displayTableToolbar",
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
        function exportRuserLiveActivityDetailRecord(){
            $('#exportForm').form('submit', {
                url: "liveActivity/exportRuserLiveActivityDetailRecord"
            });
        }
    </script>
</head>
<body>
<div data-options="region:'north',border:false"
     style="height: 40px; padding-top: 5px; overflow: hidden;">
    <h2 style="float:left;padding-left:10px;margin: 1px">直播活动详情</h2>
</div>

<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div data-options="fit:true" id="displayTableToolbar" style="height: 125px;">
    <div class="easyui-layout" data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
        <table cellpadding="5" style="margin:0 auto;width:100%;text-align: left;" class="form-body">
            <input type="hidden" name="id" id="userId" value="${anchorDetail.id}">
            <tr>
                <td>ID</td>
                <td>活动名称</td>
                <td>报名人数</td>
                <td>开播人数</td>
                <td>开播人次</td>
                <td>上榜人数</td>
                <td>总观看人次</td>
                <td>总观看人数</td>
                <td>专属礼物赠送金豆数</td>
                <td>送专属礼物人数</td>
                 <td>所有礼物赠送金豆数</td>
                <td>送礼物总人数</td>
            </tr>
            <tr>
                <td>${liveActivity.id}</td>
                <td>${liveActivity.title}</td>
                <td>${liveData.signCount}</td>
                <td>${liveData.distinctLiveNum}</td>
                <td>${liveData.liveNumber}</td>
                <td>${liveData.topCount}</td>
                <td>${liveData.maxAccessNumber}</td>
                <td>${liveData.uv}</td>
                <td>${liveData.totalPoint}</td>
                <td>${liveData.eSenderCount}</td>
                <td>${liveData.sDC}</td>
                <td>${liveData.senderCount}</td>
            </tr>
        </table>
        <div style="margin-top: 7px;">
            <form method="post" id="exportForm">
                <input type="hidden" value="${liveActivity.id}" id="liveActivityId" name="liveActivityId">
                <a href="javascript:;" onclick="exportRuserLiveActivityDetailRecord()" class="easyui-linkbutton"
                   iconCls="icon-print" style="">导出</a>
            </form>
        </div>
    </div>
</div>


</body>
</html>
