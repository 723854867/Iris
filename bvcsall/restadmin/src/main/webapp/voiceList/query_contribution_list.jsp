<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2016/8/2
  Time: 10:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>贡献榜</title>
    <script type='text/javascript' src='js/admin/query_voice_list.js'></script>
    <script type="text/javascript">
        $(function () {
            getVoiceListDetailInfo();
        });
        function getVoiceListDetailInfo() {
            $('#displayVoiceListTable').datagrid({
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
                url:"voiceList/queryVoiceListInfoById",
                queryParams: {
                    id: ${voiceListId}
                },
                columns: [
                    [
                        {field: 'creatorId', title: '<span class="columnTitle">用户ID</span>', width: 40, align: 'center'},
                        {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 120, align: 'center'},
                        {field: 'total', title: '<span class="columnTitle">人气</span>', width: 120, align: 'center'},
                        {field: 'createTime', title: '<span class="columnTitle">加入时间</span>', width: 80, align: 'center'},
                    ]
                ],
                toolbar: "#dataGridToolbar",
                onLoadSuccess: function () {
                    $('#displayVoiceListTable').datagrid('clearSelections');
                },
                pageSize: 20,
                pageList: [20, 40, 60, 80, 100],
                beforePageText: '第', //页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
        }
    </script>
</head>
<body>
<!-- 列表 -->
<table id="displayVoiceListTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
            ${voiceListName}
            <fmt:formatDate value="${voiceListStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/> 至
            <fmt:formatDate value="${voiceListEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
        </h2>
    </div>
</div>

<c:import url="/main/common.jsp"/>
</body>
</html>
