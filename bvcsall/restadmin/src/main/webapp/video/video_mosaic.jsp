<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2016/8/9
  Time: 14:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<html>
<head>
    <title>视频合成</title>
    <script>
        $(function () {
            $("#passworddlg-buttons").attr("style", "display:none;");
        });
        function insertDialog() {
            $("#insert_dlg").dialog('open').dialog('setTitle', "视频合成");
        }
        
        function mosaicLiveVideo() {
            $('#mosaicForm').form('submit', {
                url: "video/mosaicLiveVideo",
                success: function (response) {
                    var parsedJson = jQuery.parseJSON(response);
                    if (parsedJson.resultCode == "success") {
                        showMessage("提示信息", parsedJson.resultMessage);
                        $('#insert_dlg').dialog('close');
                        //doSearch();
                    } else {
                        showMessage("错误信息", parsedJson.resultMessage);
                    }
                }
            });
        }

/*        $(function () {
            userDataGrid();
        });*/

        function roomDataGrid() {
            $('#displayRoomTable').datagrid({
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
                url: "combogrid/getCombogridRoomListByRoomId",
                queryParams: {
                    userId: $("#userId").val()
                },
                columns: [
                    [
                        {field: 'id', title: '<span class="columnTitle">房间ID</span>', width: 120, align: 'center'},
                        {field: 'title', title: '<span class="columnTitle">房间标题</span>', width: 120, align: 'center'},
                        {field: 'createDateStr', title: '<span class="columnTitle">开播时间</span>', width: 120, align: 'center'},
                        {field: 'finishDateStr', title: '<span class="columnTitle">结束时间</span>', width: 120, align: 'center'}
                    ]
                ],
                onLoadSuccess: function () {
                    $('#displayRoomTable').datagrid('clearSelections');
                },
                toolbar: "#dataGridToolbarRoom",
                pageSize: 20,
                pageList: [20, 40, 60, 80, 100],
                beforePageText: '第', //页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
        }
        
        function userDataGrid(){
            $('#displayUserTable').datagrid({
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
                url: "combogrid/getCombogridUserList",
                columns: [
                    [
                        {field: 'id', title: '<span class="columnTitle">用户ID</span>', width: 120, align: 'center'},
                        {field: 'name', title: '<span class="columnTitle">用户昵称</span>', width: 120, align: 'center'},
                        {field: 'username', title: '<span class="columnTitle">用户名</span>', width: 120, align: 'center'},
                        {field: 'phone', title: '<span class="columnTitle">手机号码</span>', width: 120, align: 'center'},
                        {
                            field: 'vipStat', title: '<span class="columnTitle">用户等级</span>', width: 120, align: 'center',
                            formatter: function (value, row) {
                                if (value == 0) {
                                    return "普通";
                                } else if (value == 1) {
                                    return "蓝V";
                                } else if (value == 2) {
                                    return "黄V";
                                } else if (value == 3) {
                                    return "绿V";
                                } else {
                                    return "普通";
                                }
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#displayUserTable').datagrid('clearSelections');
                },
                toolbar: "#dataGridToolbarUser",
                pageSize: 20,
                pageList: [20, 40, 60, 80, 100],
                beforePageText: '第', //页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
        }

        //mp = missingPlayback
        function MPDataGrid() {
            $('#displayMPTable').datagrid({
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
                url: "combogrid/getCombogridMPListByStreamId",
                queryParams: {
                    userId: $("#userId").val()
                },
                columns: [
                    [
                        {field: 'roomId', title: '<span class="columnTitle">房间ID</span>', width: 80, align: 'center'},
                        {field: 'playkey', title: '<span class="columnTitle">文件地址</span>', width: 400, align: 'center'},
                        {field: 'persistentId', title: '<span class="columnTitle">唯一ID</span>', width: 250, align: 'center'}
                    ]
                ],
                onLoadSuccess: function () {
                    $('#displayMPTable').datagrid('clearSelections');
                },
                toolbar: "#dataGridToolbarMP",
                pageSize: 20,
                pageList: [20, 40, 60, 80, 100],
                beforePageText: '第', //页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
        }

        function doSearchUsers() {
            var queryParams = $('#displayUserTable').datagrid('options').queryParams;
            queryParams.user = $('#queryUser').combobox("getValue");
            queryParams.userKeyword = $('#queryUserKeyword').val();
            $('#displayUserTable').datagrid({url: "combogrid/getCombogridUserList"});
        }

        function doSearchRooms() {
            var queryParams = $('#displayRoomTable').datagrid('options').queryParams;
            $('#displayRoomTable').datagrid({url: "combogrid/getCombogridRoomListByRoomId"});
        }

        function selectUser() {
            userDataGrid();
            $("#select_user_dlg").dialog('open').dialog('setTitle', "选择用户");
        }

        function selectRoom() {
            roomDataGrid();
            $("#select_room_dlg").dialog('open').dialog('setTitle', "选择房间");
        }

        function selectMP() {
            MPDataGrid();
            $("#select_mp_dlg").dialog('open').dialog('setTitle', "选择文件地址");
        }

        function doSelectUser() {
            var user = $('#displayUserTable').datagrid("getSelected");
            $("#userId").val(user.id);
            $('#select_user_dlg').dialog('close');
        }

        function doSelectRoom() {
            var room = $('#displayRoomTable').datagrid("getSelected");
            $("#roomId").val(room.id);
            $('#select_room_dlg').dialog('close');
        }

        function doSelectMP() {
            var selectObj = $('#displayMPTable').datagrid("getSelections");
            var url = "";
            for (var i = 0; i < selectObj.length; i++) {
                var u = selectObj[i]["playkey"].split("com/");
                url += u[1] + ";";
            }
            $("#videoUrls").val(url);
            $('#select_mp_dlg').dialog('close');
        }

    </script>
</head>
<body>
<!-- 列表 -->
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
            视频合成
        </h2>
    </div>
    <div>
        <a href="javascript:;" onclick="insertDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">视频合成</a>
    </div>
</div>

<div id="insert_dlg" class="easyui-dialog" style="width:900px;height:350px;" closed="true"
     buttons="#insert-dlg-buttons">
    <form id="mosaicForm" method="post">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">用户ID：</td>
                <td style="text-align: left;">
                    <input type="text" value="" name="userId" id="userId" onclick="selectUser()" placeholder="请输入用户ID" style="width: 200px;">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">房间ID：</td>
                <td style="text-align: left;">
                    <input type="text" value="" name="roomId" id="roomId" onclick="selectRoom()" placeholder="请输入房间ID" style="width: 200px;">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">视频文件url：</td>
                <td style="text-align: left;">
                    <textarea type="text" cols="75" rows="6" placeholder="点击选择文件按钮，选择好对应的视频，确定好需要合成的视频文件地址的顺序，然后点击保存按钮" name="videoUrls" id="videoUrls"></textarea>
                    <a href="javascript:;" onclick="selectMP()">选择文件</a>
                </td>
            </tr>
            <tr>
                <td></td>
                <td><span style="color: red;">备注:</span>只有以<span style="color: red;">http://wslz.wopaitv.com</span>开头的视频文件地址才能合成视频，其它的暂不支持</td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="mosaicLiveVideo()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_dlg').dialog('close')">取消</a>
</div>

<!--选择用户弹出框开始-->
<div id="select_user_dlg" class="easyui-dialog" style="width:850px;height:700px;" closed="true"
     buttons="#select-user-dlg-buttons">
    <div id="dataGridToolbarUser">
        <table>
            <tr>
                <td>
                    <select class="easyui-combobox" name="queryUser" id="queryUser">
                        <option value="">用户搜索</option>
                        <option value="1">用户ID</option>
                        <option value="2">用户名</option>
                        <option value="3">昵称</option>
                        <option value="4">手机号码</option>
                    </select>
                </td>
                <td>
                    <input type="text" name="queryUserKeyword" id="queryUserKeyword" class="easyui-validatebox">
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearchUsers()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
    <table class="table-doc" id="displayUserTable" cellspacing="0" width="100%"></table>
</div>
<div id="select-user-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doSelectUser()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#select_user_dlg').dialog('close')">取消</a>
</div>
<!--选择用户弹出框结束-->

<!--选择房间弹出框开始-->
<div id="select_room_dlg" class="easyui-dialog" style="width:850px;height:700px;" closed="true"
     buttons="#select-room-dlg-buttons">
    <div id="dataGridToolbarRoom">
        <%--<table>
            <tr>

                <td>
                    <a href="javascript:;" onclick="doSearchRooms()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>--%>
    </div>
    <table class="table-doc" id="displayRoomTable" cellspacing="0" width="100%"></table>
</div>
<div id="select-room-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doSelectRoom()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#select_room_dlg').dialog('close')">取消</a>
</div>
<!--选择房间弹出框结束-->

<!--选择回放信息弹出框开始-->
<div id="select_mp_dlg" class="easyui-dialog" style="width:900px;height:700px;" closed="true"
     buttons="#select-mp-dlg-buttons">
    <div id="dataGridToolbarMP"></div>
    <table class="table-doc" id="displayMPTable" cellspacing="0" width="100%"></table>
</div>
<div id="select-mp-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doSelectMP()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#select_mp_dlg').dialog('close')">取消</a>
</div>
<!--选择回放信息弹出框结束-->

<c:import url="/main/common.jsp"/>

</body>
</html>
