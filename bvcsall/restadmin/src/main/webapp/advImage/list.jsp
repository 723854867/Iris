<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>开机大图管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <style type="text/css">
        .file {
            position: relative;
            display: inline-block;
            background: #D0EEFF;
            border: 1px solid #99D3F5;
            border-radius: 4px;
            padding: 4px 12px;
            overflow: hidden;
            color: #1E88C7;
            text-decoration: none;
            text-indent: 0;
            line-height: 20px;
            width: 150px;
        }

        .file input {
            position: absolute;
            font-size: 100px;
            right: 0;
            top: 0;
            opacity: 0;
            width: 150px;
        }

        .file:hover {
            background: #AADFFD;
            border-color: #78C3F3;
            color: #004974;
            text-decoration: none;
        }
    </style>
    <script type="text/javascript">
        var lisVideotUrl = "video/searchVideoListPage?filters['flowStat']=checked";
        var datagridId = "#tt";
        var adddialogueId = "#dlg";
        var editdialogueId = "#updatedlg";
        var addFormId = "#fmadd";
        var editFormId = "#updatefm";
        var addTitle = "新增开机大图";
        var editTitle = "编辑开机大图";
        var deleteConfirmMessage = "你确定要删除吗?";
        var noSelectedRowMessage = "你没有选择行";
        var searchFormId = "#searchForm";
        var searchFormId2 = "#searchForm2";
        var pageSize = 50;

        var listUrl = "advImage/getresult";
        var updateUrl = "advImage/updateTemp";
        var deleteUrl = "advImage/deleteTemp";
        var addUrl = "advImage/create";

        var url;

        $(function () {

            datagridList();

            datagridList2();
            function datagridList() {
                $("#videoTable").datagrid({
                    fitColumns: true,
                    fit: true,
                    rownumbers: true,
                    pagination: true,
                    pageNumber: 1,
                    pageList: [pageSize, pageSize * 2, pageSize * 3],
                    pageSize: pageSize,
                    pagePosition: 'bottom',
                    selectOnCheck: true,
                    singleSelect: true,
                    url: lisVideotUrl,
                    columns: [[
                        {field: 'ck', width: 100, align: 'center', checkbox: true},
                        {
                            field: 'img',
                            title: '缩略图',
                            width: 100,
                            align: 'center',
                            height: 100,
                            formatter: function (value, row, index) {
                                var name = "";
                                if (row.name) {
                                    name = row.name;
                                }
                                var desc = "";
                                if (row.description) {
                                    desc = row.description;
                                    if (desc.length > 20) {
                                        desc = row.description.substring(0, 20) + "...";
                                    }
                                }
                                var tag = "";
                                if (row.tag) {
                                    tag = row.tag;
                                    tag = "#" + tag.replace(/ /g, "#,#") + "#";
                                }
                                var creatorName = "";
                                if (row.creatorName) {
                                    creatorName = row.creatorName;
                                }
                                var activity = "";
                                if (row.activity) {
                                    activity = row.activity;
                                }
                                var stat = "";
                                if (row.flowStat == 'published') {
                                    stat = "已发布";
                                } else if (row.flowStat == 'check_ok') {
                                    stat = "审核通过";
                                } else if (row.flowStat == 'uncheck') {
                                    stat = "未审核";
                                } else if (row.flowStat == 'check_fail') {
                                    stat = "审核未通过";
                                } else if (row.flowStat == 'delete') {
                                    stat = "已删除";
                                }
                                var content = "<div style='border:solid 1px #000; width:100%; height:150px;'>";
                                content += "<div style='float:left; width:65%;'>"
                                        + "<div style='float:left;width:220px;height:150px;padding-right:10px;'> "
                                        + "<div style='width:220px;height:150px;' id='playerContainer" + row.id + "' onclick='playVideo(\"" + row.id + "\",\"" + row.playKey + "\")'><img src='<%=request.getContextPath()%>/download" + row.videoPic + "' style='height:150px;width:200px;'/></div> "
                                        + "</div><div style='width:220px;height:150px;padding-right: 10px;'> "
                                        + "<div style='width:100%;height:40%;' title='" + row.description + "'><h3 sytle='word-wrap:break-word;word-break:break-all;'>" + desc + "</h3></div> "
                                        + "<div style='width:100%;height:30%;color:#969696;'>" + activity + "</div> "
                                        + "<div style='width:100%;height:30%;color:#969696;'>" + tag + "</div> "
                                        + "</div></div>"
                                        + "<div style='float:right;width:35%; height:150px;'>"
                                        + "<div style='float:left;width:200px; height:150px;padding-right:10px'>"
                                        + "<div style='width:100%;height:40%;align:center'>"
                                        + "<span style='color:red;font-size:14px;'>" + stat + "</span> "
                                        + "<span style='font-size:12px;color:#969696;'>" + row.createDate + "上传</span> "
                                        + "</div><div style='width:100%;height:20%;color:#969696;'>" + creatorName + "</div> "
                                        + "</div></div></div> ";
                                return content;
                            }
                        }

                    ]],
                    onLoadSuccess: function () {
                        $(this).datagrid('enableDnd');
                    }
                });
            }

            function datagridList2() {
                $("#userTable").datagrid({
                    fitColumns: true,
                    fit: true,
                    rownumbers: true,
                    pagination: true,
                    pageNumber: 1,
                    pageList: [pageSize, pageSize * 2, pageSize * 3],
                    pageSize: pageSize,
                    pagePosition: 'bottom',
                    selectOnCheck: true,
                    singleSelect: true,
                    url: 'ruser/getNormalUsers',
                    columns: [[
                        {field: 'ck', width: 100, align: 'center', checkbox: true},
                        {field: 'name', title: '昵称', align: 'center', width: 100},
                        {field: 'username', title: '登录名', align: 'center', width: 100},
                        {field: 'phone', title: '手机号', align: 'center', width: 100}

                    ]],
                    onLoadSuccess: function () {
                        $(this).datagrid('enableDnd');
                    }
                });
            }

            $(datagridId).datagrid({
                fitColumns: true,
                rownumbers: true,
                striped: true,
                fit: true,
                pagination: false,
                pageNumber: 1,
                pageList: [pageSize, pageSize * 2, pageSize * 3],
                pageSize: pageSize,
                pagePosition: 'bottom',
                singleSelect: false,
                selectOnCheck: false,
                nowrap: true,
                url: listUrl,
                queryParams: {
                    curdForm: {orders: {idDesc: 'order_num'}}
                },
                rowStyler: function (index, row) {
                    if (row.status == 0) {
                        return 'color:red;';
                    }
                },
                columns: [[{
                    field: 'ck',
                    align: 'center',
                    checkbox: true
                }, {
                    field: 'img',
                    title: '开机大图封面',
                    width: 80,
                    align: 'center',
                    height: 120,
                    formatter: function (value, row, index) {
                        if ($.trim(row.imgBasePath) != "") {
                            return "<img src='/restadmin/download" + row.imgBasePath + "640x714.jpg' width='80' height='120'/>";
                        }
                    }
                }, {
                    field: 'title',
                    title: '广告标题',
                    align: 'center',
                    width: 40
                }, {
                    field: 'operation',
                    title: '跳转类型',
                    align: 'center',
                    width: 30
                }, {
                    field: 'targetid',
                    title: '跳转目标id',
                    align: 'center',
                    width: 30
                }, {
                    field: 'redirectUrl',
                    title: '跳转URL',
                    align: 'center',
                    width: 60
                }, {
                    field: 'startTime',
                    title: '生效时间',
                    align: 'center',
                    width: 40
                }, {
                    field: 'endTime',
                    title: '失效时间',
                    align: 'center',
                    width: 40
                }, {
                    field: 'status',
                    title: '状态',
                    align: 'center',
                    width: 20,
                    formatter: function (value, row, index) {
                        if (row.status == 0) {
                            return "<a style='color:red;' href='javascript:;' onclick='settingStatus("+row.id+",1)'>生效</a>";
                        } else if (row.status == "1") {
                            return "<a href='javascript:;' onclick='settingStatus("+row.id+",0)'>失效</a>";
                        } else {
                            return "已过期";
                        }
                    }
                },
                    {
                        field: 'operate',
                        title: '操作',
                        width: 40,
                        align: 'center',
                        formatter: function (value, row, index) {
                            var content = "<span style='color:#969696;padding:10px 10px'><a href='javascript:void(0)' onclick='editUser(" + index + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>修改</span><span class='l-btn-icon icon-edit'>&nbsp;</span></span></a></span>";
                            content += "<span style='color:#969696;padding:10px 10px'><a href='javascript:void(0)' onclick='destroyUser(" + row.id + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>删除</span><span class='l-btn-icon icon-remove'>&nbsp;</span></span></a></span>";
                            return content;
                        }
                    }
                ]],
                onLoadSuccess: function () {
                    $(this).datagrid('enableDnd');
                }
            });
            $('#startTime_add').datetimebox({
                showSeconds: false
            });
            $('#endTime_add').datetimebox({
                showSeconds: false
            });
            $('#startTime_edit').datetimebox({
                showSeconds: false
            });
            $('#endTime_edit').datetimebox({
                showSeconds: false
            });
        });

        var url;
        //删除开机大图
        function destroyUser(id) {
            $.messager.confirm('删除开机大图', deleteConfirmMessage, function (r) {
                if (r) {
                    $.get(deleteUrl, {
                        id: id
                    }, function (result) {
                        if (result["success"] == true) {
                            $(datagridId).datagrid('reload'); // reload the user data
                        } else {
                            showMessage("Error", result["message"]);
                        }
                    });
                }
            });
        }
        //弹出上传开机大图窗口
        function newUser() {
            $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
            url = addUrl;
            $(addFormId).form('clear');

            $('#addrolelist').combobox({
                valueField: 'id',
                textField: 'name',
                multiple: true,
                panelHeight: 'auto',
                url: 'user/rolelistAjax'
            });

        }
        //编辑开机大图
        function editUser(index) {
            var rows = $(datagridId).datagrid('getRows');//获取所有当前加载的数据行
            var row = rows[index];
            if (row) {
                $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
                $(editFormId).form('load', row);
                $('#startTime_edit').datetimebox("setValue", row.startTime);
                $('#endTime_edit').datetimebox("setValue", row.endTime);
                $('#image_file6').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file6').attr("data"));
                $('#image_file16').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file16').attr("data"));
                $('#image_file7').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file7').attr("data"));
                $('#image_file17').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file17').attr("data"));
                $('#image_file8').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file8').attr("data"));
                $('#image_file18').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file18').attr("data"));
                $('#image_file9').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file9').attr("data"));
                $('#image_file19').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file19').attr("data"));
                $('#image_file10').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file10').attr("data"));
                $('#image_file20').attr("src", "/restadmin/download" + row.imgBasePath + $('#image_file20').attr("data"));
                url = updateUrl;
            } else {
                showMessage("Error", noSelectedRowMessage);
            }
        }
        //保存上传开机大图
        function saveUser(mydialogueId, myFormId, selectId, optType) {
            if (optType == "add") {
                var file1 = $("#file1").val();
                var file2 = $("#file2").val();
                var file3 = $("#file3").val();
                var file4 = $("#file4").val();
                var file5 = $("#file5").val();
                var file11 = $("#file11").val();
                var file12 = $("#file12").val();
                var file13 = $("#file13").val();
                var file14 = $("#file14").val();
                var file15 = $("#file15").val();

                var len1 = file1.length;
                var len2 = file2.length;
                var len3 = file3.length;
                var len4 = file4.length;
                var len5 = file5.length;
                var len11 = file11.length;
                var len12 = file12.length;
                var len13 = file13.length;
                var len14 = file14.length;
                var len15 = file15.length;

                if (len1 == 0 || len2 == 0 || len3 == 0 || len4 == 0 || len5 == 0
                        || len11 == 0 || len12 == 0 || len13 == 0 || len14 == 0 || len15 == 0) {
                    showMessage("Error", "请选择开机大图文件");
                    return false;
                } else {
                    var fileext = "";
                    for (var i = 1; i <= 5; i++) {
                        var temp = eval("file" + i);
                        fileext = temp.substring(temp.lastIndexOf("."));
                        fileext = fileext.toLowerCase();
                        if (fileext != '.jpg' && fileext != '.gif' && fileext != '.jpeg' && fileext != '.png' && fileext != '.bmp') {
                            showMessage("Error", "对不起，新版开机大图仅支持标准格式的图片，请不要调皮!O(∩_∩)O谢谢~");
                            return false;
                        }
                    }
                    for (var i = 11; i <= 15; i++) {
                        var temp = eval("file" + i);
                        fileext = temp.substring(temp.lastIndexOf("."));
                        fileext = fileext.toLowerCase();
                        if (fileext != '.jpg' && fileext != '.gif' && fileext != '.jpeg' && fileext != '.png' && fileext != '.bmp') {
                            showMessage("Error", "对不起,旧版开机大图仅支持标准格式的图片，请不要调皮!O(∩_∩)O谢谢~");
                            return false;
                        }
                    }
                }
                var startTime = $("#startTime_add").textbox('getValue');
                var endTime = $("#endTime_add").textbox('getValue');
                if (!validTime(startTime, endTime)) {
                    alert("失效日期必须大于生效日期，请不要调皮!O(∩_∩)O谢谢~");
                    return false;
                }
            } else {
                var file6 = $("#file6").val();
                var file7 = $("#file7").val();
                var file8 = $("#file8").val();
                var file9 = $("#file9").val();
                var file10 = $("#file10").val();
                var file16 = $("#file16").val();
                var file17 = $("#file17").val();
                var file18 = $("#file18").val();
                var file19 = $("#file19").val();
                var file20 = $("#file20").val();

                var len6 = file6.length;
                var len7 = file7.length;
                var len8 = file8.length;
                var len9 = file9.length;
                var len10 = file10.length;
                var len16 = file16.length;
                var len17 = file17.length;
                var len18 = file18.length;
                var len19 = file19.length;
                var len20 = file20.length;

                if (len6 > 0 || len7 > 0 || len8 > 0 || len9 > 0 || len10 > 0
                        || len16 > 0 || len17 > 0 || len18 > 0 || len19 > 0 || len20 > 0) {
                    var fileext = "";
                    for (var i = 6; i <= 10; i++) {
                        var temp = eval("file" + i);
                        fileext = temp.substring(temp.lastIndexOf("."));
                        fileext = fileext.toLowerCase();
                        if (fileext != '' && fileext != '.jpg' && fileext != '.gif' && fileext != '.jpeg' && fileext != '.png' && fileext != '.bmp') {
                            showMessage("Error", "对不起，新版开机大图仅支持标准格式的图片，请不要调皮!O(∩_∩)O谢谢~");
                            return false;
                        }
                    }
                    for (var i = 16; i <= 20; i++) {
                        var temp = eval("file" + i);
                        fileext = temp.substring(temp.lastIndexOf("."));
                        fileext = fileext.toLowerCase();
                        if (fileext != '' && fileext != '.jpg' && fileext != '.gif' && fileext != '.jpeg' && fileext != '.png' && fileext != '.bmp') {
                            showMessage("Error", "对不起，旧版开机大图仅支持标准格式的图片，请不要调皮!O(∩_∩)O谢谢~");
                            return false;
                        }
                    }
                }
                var startTime = $("#startTime_edit").textbox('getValue');
                var endTime = $("#endTime_edit").textbox('getValue');
                if (!validTime(startTime, endTime)) {
                    alert("失效日期必须大于生效日期，请不要调皮!O(∩_∩)O谢谢~");
                    return false;
                }
            }
            if ($(myFormId).form('validate')) {
                $(myFormId).submit();
            } else {
                return false;
            }
        }
        //比较日期大小
        function validTime(startTime, endTime) {
            var arr1 = startTime.split("-");
            var arr2 = endTime.split("-");
            var date1 = new Date(parseInt(arr1[0]), parseInt(arr1[1]) - 1, parseInt(arr1[2]), 0, 0, 0);
            var date2 = new Date(parseInt(arr2[0]), parseInt(arr2[1]) - 1, parseInt(arr2[2]), 0, 0, 0);
            if (date1.getTime() > date2.getTime()) {
                return false;
            } else {
                return true;
            }
            return false;
        }

        //一键失效
        function Invalid() {
            var row = $(datagridId).datagrid('getChecked');
            var ids = [];
            if (row.length > 0) {
                for (var r in row) {
                    ids.push(row[r]['id']);
                }
            } else {
                showMessage("Error", "请选择数据行");
                return false;
            }
            ids = ids.join(",");
            if (confirm("确定要让其失效吗？")) {
                $.ajax({
                    url: 'advImage/Invalid',
                    data: {
                        id: ids
                    },
                    type: "post",
                    dataType: "json",
                    success: function (result) {
                        if (result["success"]) {
                            $(datagridId).datagrid('reload');
                        } else {
                            showMessage("Error", "操作失败");
                        }
                    }
                });
            }
        }

        function doSearch() {
            $("#videoTable").datagrid('reload', getFormJson(searchFormId));
        }

        function doSearch2() {
            $("#userTable").datagrid('reload', getFormJson(searchFormId2));
        }

        function chgOpration(obj) {
            $("#targetid_edit").val("");
            $("#targetid_add").val("");
        }

        function showTargetDialog(sufix) {
            var opt = $("#operation_" + sufix).combobox("getValue");
            if (opt == 'video') {
                $("#video_dlg").show();
                $("#video_dlg").dialog({
                    title: '选择视频',
                    width: 800,
                    height: 650,
                    closed: false,
                    cache: true,
                    modal: true
                });
            } else if (opt == 'activity') {
                $("#act_dlg").show();
                $("#act_dlg").dialog({
                    title: '选择活动',
                    width: 400,
                    height: 200,
                    closed: false,
                    cache: true,
                    modal: true
                });
            } else if (opt == 'user') {
                $("#user_dlg").show();
                $("#user_dlg").dialog({
                    title: '选择用户',
                    width: 800,
                    height: 650,
                    closed: false,
                    cache: true,
                    modal: true
                });
            }
        }

        function selectActivity() {
            var aid = $("#activities option:selected").val();
            $("#targetid_edit").val(aid);
            $("#targetid_add").val(aid);
            $("#act_dlg").dialog("close");
        }

        function selectVideo() {
            var row = $("#videoTable").datagrid('getChecked');
            var vid = row[0].id;
            $("#targetid_edit").val(vid);
            $("#targetid_add").val(vid);
            $("#video_dlg").dialog("close");
        }

        function selectUser() {
            var row = $("#userTable").datagrid('getChecked');
            var vid = row[0].id;
            $("#targetid_edit").val(vid);
            $("#targetid_add").val(vid);
            $("#user_dlg").dialog("close");
        }

        function settingStatus(id, status) {
            $.ajax({
                url: 'advImage/settingStatus',
                data: {"id": id, "status": status},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result["success"]) {
                        $(datagridId).datagrid('reload');
                    } else {
                        showMessage("错误提示", "状态更新失败！");
                    }
                }
            });
        }
    </script>
</head>
<body>

<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">广告管理</h2>

        <h3 style="float:left;padding-left:20px;margin: 1px;height:26px;line-height:26px;">开机大图</h3>
        <span style="float:left;padding-left:20px;margin: 1px">
            <a href="banner/listbanner" class="easyui-linkbutton">发现Banner</a>
        </span>
    </div>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:newUser()"
       plain="true">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-no" onclick="javascript:Invalid()"
       plain="true">一键失效</a>
</div>

<!-- 弹出的添加对话框 -->
<div id="dlg" class="easyui-dialog" style="width:700px;height:520px;padding:2px 5px;" closed="true"
     buttons="#dlg-buttons">
    <form id="fmadd" method="post" action="advImage/add" novalidate enctype="multipart/form-data">
        <table class="table-doc">
            <tr>
                <td width="15%"><label>广告标题:</label></td>
                <td width="35%">
                    <input id="title_add" name="title" class="easyui-validatebox" required="true">
                </td>
                <td width="15%"><label>跳转类型:</label></td>
                <td width="35%">
                    <select name="operation" class="easyui-combobox" id="operation_add"
                            onchange="chgOpration(this)">
                        <option value="">不跳转</option>
                        <option value="web">跳到网页</option>
                        <option value="video">打开视频详情页</option>
                        <option value="activity">打开活动详情页</option>
                        <option value="user">打开个人主页</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>跳转URL:</label></td>
                <td>
                    <input id="redirectUrl_add" name="redirectUrl" class="easyui-validatebox">
                </td>
                <td><label>跳转目标id:</label></td>
                <td>
                    <input id="targetid_add" name="targetid" class="easyui-validatebox"
                           onfocus="showTargetDialog('add');">
                </td>
            </tr>
            <tr>
                <td><label>生效时间:</label></td>
                <td><input id="startTime_add" name="start_time" required="true" style="width: 150px;"></td>
                <td><label>失效时间:</label></td>
                <td><input id="endTime_add" name="end_time" required="true" style="width: 150px;"></td>
            </tr>
            <tr>
                <th>新版</th>
                <th></th>
                <th>旧版(1.6.0前)</th>
                <th></th>
            </tr>
            <tr>
                <td><label>IOS(iphone 4/4s)640*800</label></td>
                <td>
                    <input class="file" type="file" id="file1" name="files"/>
                </td>
                <td><label>IOS(iphone 4/4s)640*714</label></td>
                <td>
                    <input class="file" type="file" id="file11" name="files2"/>
                </td>
            </tr>
            <tr>
                <td><label>IOS(iphone 5/5s)640*976</label></td>
                <td>
                    <input class="file" type="file" id="file2" name="files"/>
                </td>
                <td><label>IOS(iphone 5/5s)640*890</label></td>
                <td>
                    <input class="file" type="file" id="file12" name="files2"/>
                </td>
            </tr>
            <tr>
                <td><label>IOS(iphone 6)750*1146</label></td>
                <td>
                    <input class="file" type="file" id="file3" name="files"/>
                </td>
                <td><label>IOS(iphone 6)750*1044</label></td>
                <td>
                    <input class="file" type="file" id="file13" name="files2"/>
                </td>
            </tr>
            <tr>
                <td><label>IOS(iphone 6 Plus)1242*1898</label></td>
                <td>
                    <input class="file" type="file" id="file4" name="files"/>
                </td>
                <td><label>IOS(iphone 6 Plus)1242*1732</label></td>
                <td>
                    <input class="file" type="file" id="file14" name="files2"/>
                </td>
            </tr>
            <tr>
                <td><label>Android(1080*1650)</label></td>
                <td>
                    <input class="file" type="file" id="file5" name="files"/>
                </td>
                <td><label>Android(720*1004)</label></td>
                <td>
                    <input class="file" type="file" id="file15" name="files2"/>
                </td>
            </tr>
        </table>
    </form>
</div>

<!-- 对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveUser('#dlg','#fmadd','type_add','add')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 弹出的选择活动对话框 -->
<div id="act_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true"
     buttons="#adlg-buttons">
    活动&nbsp;&nbsp;
    <select id="activities" name="activities">
        <c:forEach items="${activites }" var="activity">
            <option value="${activity.id}">${activity.title}</option>
        </c:forEach>
    </select>
</div>

<!-- 弹出的选择视频对话框 -->
<div id="video_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true"
     buttons="#vdlg-buttons">
    <table id="videoTable" data-options="border:false,toolbar:'#tbVideoTable'">
    </table>
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tbVideoTable" style="padding:5px;height:auto">
        <form action="" id="searchForm">
            <div>
                标签&nbsp;
                <select name="filters['tag']">
                    <option value="">选择标签</option>
                    <c:forEach items="${tags }" var="tag">
                        <option value="${tag.name}">${tag.name}</option>
                    </c:forEach>
                </select>
                &nbsp;所属活动&nbsp;
                <select name="filters['activity']">
                    <option value="">选择活动</option>
                    <c:forEach items="${activites }" var="activity">
                        <option value="${activity.id}">${activity.title}</option>
                    </c:forEach>
                </select>
                &nbsp;
                发布者
                <input name="filters['creatorName']" value=""/>
                &nbsp;
                视频描述
                <input name="filters['videoName']" value=""/>
                &nbsp;
                <input type="button" class="button blue bigrounded" onclick="doSearch()"
                       style="width: 80px;height: 30px;" value="查询"/>
            </div>
            <hr/>
        </form>
    </div>

</div>

<!-- 弹出的选择用户对话框 -->
<div id="user_dlg" class="easyui-dialog" style="width:390px;height:450px;padding:10px 20px" closed="true"
     buttons="#udlg-buttons">
    <table id="userTable" data-options="border:false,toolbar:'#tb2'">
    </table>
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
        <form action="" id="searchForm2">
            <div>
                昵称&nbsp;<input name="name" value="" size="15"/>

                &nbsp;登录名&nbsp;<input name="username" value="" size="15"/>

                &nbsp;手机号&nbsp;<input name="phone" value="" size="15"/>
                &nbsp;
                <input type="button" class="button blue bigrounded" onclick="doSearch2()"
                       style="width: 80px;height: 30px;" value="查询"/>
            </div>
            <hr/>
        </form>
    </div>

</div>


<!-- 添加对话框里的保存和取消按钮 -->
<div id="adlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectActivity();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#act_dlg').dialog('close')">取消</a>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="vdlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectVideo();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#video_dlg').dialog('close')">取消</a>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="udlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectUser();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#user_dlg').dialog('close')">取消</a>
</div>


<!-- 弹出的编辑对话框 start-->
<div id="updatedlg" class="easyui-dialog" style="width:700px;height:600px;padding:2px 5px" closed="true"
     buttons="#updatedlg-buttons">
    <form id="updatefm" method="post" action="advImage/updateTemp" novalidate enctype="multipart/form-data">
        <table class="table-doc" width="100%">
            <tr>
                <td><label>广告标题:</label></td>
                <td><input id="title_edit" name="title" class="easyui-validatebox" required="true"></td>
                <td><label>跳转类型:</label></td>
                <td>
                    <select name="operation" id="operation_edit" class="easyui-combobox" style="width:50%"
                            onchange="chgOpration(this)">
                        <option value="">不跳转</option>
                        <option value="web">跳到网页</option>
                        <option value="video">打开视频详情页</option>
                        <option value="activity">打开活动详情页</option>
                        <option value="user">打开个人主页</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>跳转URL:</label></td>
                <td>
                    <input id="redirectUrl_edit" name="redirectUrl" class="easyui-validatebox">
                </td>
                <td><label>跳转目标ID:</label></td>
                <td>
                    <input id="targetid_edit" name="targetid" class="easyui-validatebox"
                           onfocus="showTargetDialog('edit');">
                </td>
                <input type="hidden" name="id"/>
            </tr>
            <tr>
                <td><label>生效时间:</label></td>
                <td>
                    <input id="startTime_edit" name="start_time" required="true" style="width: 150px;">
                </td>
                <td><label>失效时间:</label></td>
                <td>
                    <input id="endTime_edit" name="end_time" required="true" style="width: 150px;">
                </td>
            </tr>
            <tr>
                <th>新版:</th>
                <th></th>
                <th>旧版(1.6.0前)</th>
                <th></th>
            </tr>
            <tr>
                <td>
                    <img src="" alt="IOS(iphone 4/4s)640*800" id="image_file6" data="640x800.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">IOS(iphone 4/4s)640*800
                        <input type="file" id="file6" name="files"/>
                    </a>
                </td>
                <td>
                    <img src="" alt="IOS(iphone 4/4s)640*714" id="image_file16" data="640x714.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">IOS(iphone 4/4s)640*714
                        <input type="file" id="file16" name="files2"/>
                    </a>
                </td>
            </tr>
            <tr>
                <td>
                    <img src="" alt="IOS(iphone 5/5s)640*976" id="image_file7" data="640x976.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">IOS(iphone 5/5s)640*976
                        <input type="file" id="file7" name="files"/>
                    </a>
                </td>
                <td>
                    <img src="" alt="IOS(iphone 5/5s)640*890" id="image_file17" data="640x890.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">IOS(iphone 5/5s)640*890
                        <input type="file" id="file17" name="files2"/>
                    </a>
                </td>
            </tr>
            <tr>
                <td>
                    <img src="" alt="IOS(iphone 6)750*1146" id="image_file8" data="750x1146.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">IOS(iphone 6)750*1146
                        <input type="file" id="file8" name="files"/>
                    </a>
                </td>
                <td>
                    <img src="" alt="IOS(iphone 6)750*1044" id="image_file18" data="750x1044.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">IOS(iphone 6)750*1044
                        <input type="file" id="file18" name="files2"/>
                    </a>
                </td>
            </tr>
            <tr>
                <td>
                    <img src="" alt="IOS(iphone 6 Plus)1242*1898" id="image_file9" data="1242x1898.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">IOS(iphone 6 Plus)1242*1898
                        <input type="file" id="file9" name="files"/>
                    </a>
                </td>
                <td>
                    <img src="" alt="IOS(iphone 6 Plus)1242*1732" id="image_file19" data="1242x1732.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">IOS(iphone 6 Plus)1242*1732
                        <input type="file" id="file19" name="files2"/>
                    </a>
                </td>
            </tr>
            <tr>
                <td>
                    <img src="" alt="Android(1080*1650)" id="image_file10" data="1080x1650.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">Android(1080*1650)
                        <input type="file" id="file10" name="files"/>
                    </a>
                </td>
                <td>
                    <img src="" alt="Android(720*1004)" id="image_file20" data="720x1004.jpg"
                         style="width: 60px;height: 60px;">
                </td>
                <td>
                    <a href="javascript:;" class="file">Android(720*1004)
                        <input type="file" id="file20" name="files2"/>
                    </a>

                </td>
            </tr>
        </table>
    </form>
</div>
<div id="updatedlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveUser('#updatedlg','#updatefm','type_edit','edit')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
</div>
<!-- 弹出的编辑对话框 end-->
</body>
</html>
