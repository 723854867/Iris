<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html style="background: white">
<head>
    <title>用户视频</title>
    <link rel="stylesheet" href="files/Jcrop/css/jquery.Jcrop.css" type="text/css"/>
    <script src="files/Jcrop/js/jquery.Jcrop.js"></script>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <style type="text/css">
        #fm {
            margin: 0;
            padding: 10px 30px;
        }

        .ftitle {
            font-size: 14px;
            font-weight: bold;
            padding: 5px 0;
            margin-bottom: 10px;
            border-bottom: 1px solid #ccc;
        }

        .fitem {
            margin-bottom: 5px;
        }

        .fitem label {
            display: inline-block;
            width: 80px;
        }
    </style>
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script src="js/fp/jquery.iframe-transport.js"></script>
    <script src="js/fp/jquery.fileupload.js"></script>
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>

    <script type="text/javascript">
        var datagridId = "#tt";
        var adddialogueId = "#dlg";
        var editdialogueId = "#updatedlg";
        var addFormId = "#fm";
        var editFormId = "#updatefm";
        var addTitle = "播放视频";
        var editTitle = "编辑视频";
        var deleteConfirmMessage = "你确定要删除吗?";
        var noSelectedRowMessage = "你没有选择行或状态已更改";
        var noSelectedCheckStat = "你没有选择审核状态";
        var searchFormId = "#searchForm";
        var pageSize = 20;

        var popFlg = '${popFlg}';
        var listUrl = "video/searchVideoListPage";
        if (popFlg && popFlg == '1') {
            listUrl += "&filters['popFlg']=1";
        }
        var updateUrl = "video/update";
        var deleteUrl = "video/deleteallpage";

        var url;

        function datagridList() {
            $(datagridId).datagrid({
                fitColumns: true,
                fit: true,
                rownumbers: true,
                pagination: true,
                pageNumber: 1,
                pageList: [pageSize, pageSize * 2, pageSize * 3],
                pageSize: pageSize,
                pagePosition: 'bottom',
                selectOnCheck: true,
                nowrap: false,
                url: listUrl,
                columns: [[
                    {field: 'ck', width: 100, checkbox: true},
                    {
                        field: 'img',
                        title: '缩略图',
                        width: 100,
                        height: 150,
                        align: 'center',
                        formatter: function (value, row, index) {
                            var content = "";
                            if (row.type == 3 || row.type == 4) {
                                content = "<div><div style='width:110px;height:75px;margin: 0 auto;'> "
                                        + "<div style='width:110px;height:75px;' id='playerContainer" + row.id + "'><img onclick='playImage(\"" + row.videoPic + "\")' src='<%=request.getContextPath()%>/download" + row.videoPic + "' style='height:75px;width:100px;'/></div> "
                                        + "</div>";
                            } else {
                                content = "<div><div style='width:110px;height:75px;margin: 0 auto;'> "
                                        + "<div style='width:110px;height:75px;' id='playerContainer" + row.id + "' onclick='playVideo(\"" + row.id + "\",\"" + row.playKey + "\")'><img src='<%=request.getContextPath()%>/download" + row.videoPic + "' style='height:75px;width:100px;'/></div> "
                                        + "</div>";
                            }
                            return content;
                        }
                    },
                    {
                        field: 'description',
                        title: '视频描述',
                        width: 150,
                        nowrap: false,
                        formatter: function (value, row, index) {
                            var desc = "";
                            if (row.description) {
                                desc = row.description;
                                if (desc.length > 20) {
                                    desc = row.description.substring(0, 20) + "...";
                                }
                            }
                            return "<div style='width:100%;height:40%;' title='" + row.description + "'><h3 sytle='word-wrap:break-word;word-break:break-all;'>" + desc + "</h3></div> ";
                        }
                    },
                    {
                        field: 'type',
                        title: '类型',
                        width: 100,
                        nowrap: false,
                        formatter: function (value, row, index) {
                            if(value == 1){
                                return "视频";//1：视频，2：直播回放，3：普通图片，4：预告图片
                            }else if(value == 2){
                                return "直播回放";
                            }else if(value == 3){
                                return "普通图片";
                            }else if(value == 4){
                                return "预告图片";
                            }
                        }
                    },
                    {
                        field: 'actives',
                        title: '参与活动',
                        width: 200,
                        nowrap: false,
                        formatter: function (value, row, index) {
                            var activity = "";
                            if (row.actives) {
                                activity = row.actives;
                            }
                            return "<div style='width:100%;height:30%;color:#969696;'>" + activity + "&nbsp;</div> ";
                        }
                    },
                    {field: 'tag', title: '参与话题', width: 200},
                    {
                        field: 'flowStat',
                        title: '详情参数',
                        width: 200,
                        nowrap: false,
                        formatter: function (value, row, index) {
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

                            var content = "<div>" +
                                    "<div>" +
                                    "<span>上传时间：</span><span>" + row.createDateStr + "</span>" +
                                    "</div>" +
                                    "<div>" +
                                    "<span>状态：</span><span>" + stat + "</span>" +
                                    "</div>" +
                                    "<div>" +
                                    "<span>点赞：</span><span>" + row.praiseCount + "个</span>" +
                                    "</div>" +
                                    "<div>" +
                                    "<span>转发：</span><span>" + row.forwardCount + "次</span>" +
                                    "</div>" +
                                    "<div>" +
                                    "<span>评论：</span><span>" + row.evaluationCount + "次</span>" +
                                    "</div>" +
                                    "</div>";
                            return content;
                        }
                    },
                    {
                        field: 'uploader',
                        title: '上传者',
                        width: 150,
                        nowrap: false,
                        formatter: function (value, row, index) {
                            var content = "<div>" +
                                    "<div><span>用户昵称：</span><span>" + row.uploader + "</span></div>" +
                                    "<div><span>用户ID：</span><span>" + row.creatorId + "</span></div>" +
                                    "<div><span>用户名：</span><span>" + row.username + "</span></div>" +
                                    "<div><span>手机号码：</span><span>" + row.phone + "</span></div>" +
                                    "</div>";
                            return content;
                        }
                    },
                    {
                        field: 'weight',
                        title: '<span class="columnTitle">权重</span>',
                        width: 120,
                        align: 'center',
                        formatter: function (value, row) {
                            var sortStr = '<div><span><a href="javascript:;" onclick="videoSort(' + row.id + ',1)">置顶</a></span></div><div><span><a href="javascript:;" onclick="videoSort(' + row.id + ',2)">上移</a></span></div><div><span><a href="javascript:;" onclick="videoSort(' + row.id + ',3)">下移</a></span></div>';
                            var editStr = '<input onblur="editVideoOrderNum(' + row.id + ')" id="orderNum' + row.id + '" name="weight" value="' + value + '" class="easyui-numberbox" min="0" max="" precision="0" maxlength="5" size="5"/>';
                            return sortStr + editStr;
                        }
                    },
                    {
                        field: 'operation', title: '操作', width: 150, formatter: function (value, row, index) {
                        var content = "<span style='color:#969696;padding:10px 10px'>"
                                + "<a href='javascript:void(0)' onclick='showActities(" + row.id + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>加入活动</span><span class='l-btn-icon icon-add'>&nbsp;</span></span></a></span>";
                        content += "<span style='width:100%;height:30%;color:#969696;padding:10px 10px'><a href='video/multidown?ids=" + row.id + "'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>下载</span><span class='l-btn-icon icon-save'>&nbsp;</span></span></a></span> "
                                + "<br>";
                        if (row.playRateToday > 0) {
                            content += "<span style='color:#969696;padding:10px 10px'>"
                                    + "<a href='javascript:void(0)' onclick='removeHotVideo(" + row.id + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>热门视频</span><span class='l-btn-icon icon-remove'>&nbsp;</span></span></a></span>";
                        } else {
                            content += "<span style='color:#969696;padding:10px 10px'>"
                                    + "<a href='javascript:void(0)' onclick='addHotVideo(" + row.id + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>热门视频</span><span class='l-btn-icon icon-add'>&nbsp;</span></span></a></span>";
                        }
                        if (row.flowStat != 'delete') {
                            content += "<span style='width:100%;height:30%;color:#969696;padding:10px 10px'><a href='javascript:void(0)' onclick='destroyUser(" + row.id + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>删除</span><span class='l-btn-icon icon-remove'>&nbsp;</span></span></a></span> "
                        }
                        content += "<br>";
                        content += "<span style='color:#969696;padding:10px 10px'>"
                                + "<a href='javascript:void(0)' onclick='rankAble(" + row.id + "," + row.rankAble + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>排行榜&nbsp;&nbsp;</span>";
                        if (row.rankAble != 1) {
                            content += "<span class='l-btn-icon icon-ok'>&nbsp;</span>";
                        } else {
                            content += "<span class='l-btn-icon icon-no'>&nbsp;</span>";
                        }
                        content += "</span></a></span>";
                        content += "<span style='color:#969696;padding:20px 10px'><a href='video/videoDetail?vid=" + row.id + "'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>查看</span><span class='l-btn-icon icon-search'>&nbsp;</span></span></a></span> ";

                        content += "<br><span style='color:#969696;padding:10px 10px'><a href='video/downloadPic?id=" + row.id + "'><span class='l-btn-left'><span class='l-btn-text'>下载封面&nbsp;</span></span></a></span> "
                                + "<span style='color:#969696;padding:10px 10px'><a href='javascript:void(0)' onclick='uploadpic(" + row.id + ")' ><span class='l-btn-left'><span class='l-btn-text'>上传封面</span></span></a></span> "

                        content += "<br>";

                        return content;
                    }
                    }
                ]],
                onLoadSuccess: function () {
                    $(datagridId).datagrid('clearSelections');
                }
            });
        }

        function addHotVideo(vid) {
            $.post('video/addHotVideoTop', {id: vid}, function (result) {
                if (result["success"] == true) {
                    $(datagridId).datagrid('reload'); // reload the user data
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function removeHotVideo(vid) {
            $.post('video/removeHotVideos', {ids: vid}, function (result) {
                if (result["success"] == true) {
                    $(datagridId).datagrid('reload'); // reload the user data
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function rankAble(vid, able) {
            if (able != 1) {
                able = 1;
            } else {
                able = 0;
            }
            $.post('video/rankAble', {id: vid, rankAble: able}, function (result) {
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

        function playVideo(playerId, fileName) {
            $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
            $('#playerContainer').empty();
            var _player = null;

            var player = $('<div/>');
            $(player).attr('id', 'pl' + playerId);

            $('#playerContainer').append(player);
            var conf = {
                file: '${video_play_url_prefix}' + fileName + '.m3u8',
                image: '${img}',
                height: 350,
                width: 400,
                autostart: true,
                analytics: {enabled: false}
            };
            _player = jwplayer('pl' + playerId).setup(conf);
        }

        var url;
        function destroyUser(id) {
            if (id) {
                $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
                    if (r) {
                        $.post('video/logicremove', {ids: id}, function (result) {
                            if (result["success"] == true) {
                                $(datagridId).datagrid('reload'); // reload the user data
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

        function selectAll() {
            var ck = false;
            var sall = $("#sall");
            if (sall[0].checked) {
                ck = true;
            }
            var ckboxes = $("input[name='ck']");
            for (var i = 0; i < ckboxes.length; i++) {
                if (ck) {
                    $(datagridId).datagrid('selectRow', i);
                } else {
                    $(datagridId).datagrid('unselectRow', i);
                }
                ckboxes[i].checked = ck;
            }
        }

        function saveUser(mydialogueId, myFormId) {
            $.ajax({
                url: url,
                data: getFormJson(myFormId),
                type: "post",
                dataType: "json",
                beforeSend: function () {
                    return $(myFormId).form('validate');
                },
                success: function (result) {
                    if (result["success"] == true) {
                        $(mydialogueId).dialog('close'); // close the dialog
                        $(datagridId).datagrid('reload'); // reload the user data
                    } else {
                        showMessage("Error", result["message"]);
                    }
                }
            });
        }

        function deleteAll() {
            var row = $(datagridId).datagrid('getChecked');
            var ids = [];
            var inum = 0;
            for (var r in row) {
                ids.push(row[r]['id']);
            }
            if (ids.length > 0) {
                ids = ids.join(",");
            } else {
                showMessage("Error", noSelectedRowMessage);
                return;
            }
            $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
                if (r) {
                    $.post('video/logicremove', {ids: ids}, function (result) {
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

        function seeDetail(id) {
            $("#detail").show();
            $("#detail").dialog({
                title: '选择活动',
                width: 800,
                height: 700,
                closed: false,
                cache: true,
                href: 'video/showdetail?id=' + id,
                modal: true

            });
        }

        function downloadAll() {
            var row = $(datagridId).datagrid('getChecked');
            var ids = [];
            var inum = 0;
            for (var r in row) {
                ids.push(row[r]['id']);
            }
            ids = ids.join(",");
            if (ids.length > 0) {
                window.location.href = baseUrl + 'video/multidown?ids=' + ids;
            } else {
                showMessage("Error", noSelectedRowMessage);
            }
        }

        function uploadpic(id) {
            $('#uploadPicDlg').dialog('open').dialog('setTitle', "上传封面");
            $("#videoId").val(id);
            $('#hlsfiles').fileupload({
                url: 'video/uploadpic',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;
                    $("#videoPic").val(uploadresult["result"]);
                    var picPath = "<%=request.getContextPath()%>/download";
                    $("#picCut").attr("src", picPath + uploadresult["result"]);
                    $(".jcrop-holder img").attr("src", picPath + uploadresult["result"]);
                    initJcrop();
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#p').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#p').show();
                    $('#p').progressbar('setValue', 0);
                },
                change: function (e, data) {
                    var fileName = data.files[0].name;
                    var fileext = fileName.substring(fileName.lastIndexOf("."));
                    fileext = fileext.toLowerCase();
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "对不起，系统仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
                        return false;
                    }
                }
            });
        }

        //初始化封面裁剪
        function initJcrop() {
            $('#picCut').Jcrop({
                onRelease: releaseCheck,
                onChange: showCoords
            }, function () {
                jcrop_api = this;
                jcrop_api.animateTo([0, 0, 241, 120]);
                jcrop_api.setOptions({allowResize: !!this.checked});
                jcrop_api.setOptions({allowSelect: !!this.checked});
            });
            $('#coords').on('change', 'input', function (e) {
                var x1 = $('#x1').val(),
                        x2 = $('#x2').val(),
                        y1 = $('#y1').val(),
                        y2 = $('#y2').val();
                jcrop_api.setSelect([x1, y1, x2, y2]);
            });
        }
        ;

        function releaseCheck() {
            jcrop_api.setOptions({allowSelect: true});
        }
        ;

        function showCoords(c) {
            $('#x1').val(c.x);
            $('#y1').val(c.y);
            $('#x2').val(c.x2);
            $('#y2').val(c.y2);
            $('#w').val(c.w);
            $('#h').val(c.h);
        }
        ;

        function cutImage(mydialogueId, myFormId) {
            var x1 = $('#x1').val() * 2;
            var y1 = $('#y1').val() * 2;
            var w = $('#w').val() * 2;
            var h = $('#h').val() * 2;
            var imgPath = $("#videoPic").val();
            var id = $("#videoId").val();
            $.ajax({
                url: "video/updatevideo",
                data: {id: id, imgPath: imgPath, x1: x1, y1: y1, w: w, h: h},
                type: "post",
                dataType: "json",
                success: function (data) {
                    $(mydialogueId).dialog('close');
                    location.href = "video/uservideos"; // reload the user data
                },
                error: function () {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function videoSort(videoId, type) {
            $.ajax({
                url: 'video/videoSort',
                data: {'type': type, "videoId": videoId},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.resultCode == "success") {
                        doSearch()
                    } else {
                        showMessage("错误提示", result.resultMessage);
                    }
                }
            });
        }

        function editVideoOrderNum(id) {
            var orderNum = $("#orderNum" + id).val();
            $.ajax({
                url: 'video/editVideoOrderNum',
                type: 'post',
                data: {'videoId':id,'weight':orderNum},
                async: false, //默认为true 异步
                error: function () {
                    showMessage('错误提示',"更新失败，请重试！");
                },
                success: function (data) {
                    if(data.resultCode != "success"){
                        showMessage(data.resultMessage);
                    }else{
                        doSearch();
                    }
                }
            });
        }

        function playImage(value){
            $("#imageDlg").show();
            $("#imageDlg").dialog({
                title:'图片/直播预告',
                width: 500,
                height: 500,
                closed: false,
                cache: true,
            });
            $("#bigImage").attr("src","/restadmin/download"+value)
        }

    </script>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" style="padding:5px;height:auto">
    <div data-options="region:'north',border:false" style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">视频管理</h2>

        <h2 style="float:left;padding-left:50px;margin: 1px"><a href="video/hotVideos"
                                                                class="easyui-linkbutton">最热视频</a></h2>
    </div>
    <form action="" id="searchForm">
        <div style="padding:4px;">
            <select class="easyui-combobox" id="user" name="filters['user']">
                <option value="">用户搜索</option>
                <option value="1">用户ID</option>
                <option value="2">用户名</option>
                <option value="3">昵称</option>
                <option value="4">手机号码</option>
            </select>
            <input type="text" name="filters['userKeyword']" id="userKeyword" class="easyui-validatebox">
            &nbsp;&nbsp;
            状态
            <select name="filters['flowStat']" class="easyui-combobox">
                <option value="">显示全部</option>
                <option value="check_ok">审核通过</option>
                <option value="uncheck">未审核</option>
                <option value="check_fail">审核未通过</option>
                <option value="delete">已删除</option>
            </select>
            &nbsp;
            发布者类型
            &nbsp;
            <select name="filters['dataFrom']" id="dataFrom" class="easyui-combobox">
                <option value="">全部</option>
                <option value="myvideo_restadmin">管理员视频</option>
                <option value="myvideo_restwww">用户视频</option>
            </select>
            &nbsp;
            排行榜
            &nbsp;
            <select name="filters['rankAble']" id="rankAble" class="easyui-combobox">
                <option value="">全部</option>
                <option value="1">禁止</option>
                <option value="0">允许</option>
            </select>
            &nbsp;活动&nbsp;
            <input class="easyui-combobox" id="activities" name="filters['activity']"
                   data-options="url: 'comboBox/getActivityCombobox',method: 'get',valueField:'id',textField:'text'">
            &nbsp;话题&nbsp;
            <input type="text" name="filters['label']" id="label" style="width:120px;"
                   class="easyui-validatebox easyui-combobox"
                   data-options="url:'comboBox/queryComboBoxLabels',method: 'get',valueField:'id',textField:'text'">
            &nbsp;
            视频描述
            <input name="filters['videoName']" value=""/>
            &nbsp;
            活动筛选
            <select name="filters['noActivities']" id="noActivities" class="easyui-combobox">
                <option value="">全部</option>
                <option value="1">未参加活动</option>
                <option value="2">已参加活动</option>
            </select>
        </div>
        <div>
            发布时间
            <input id="starttime" name="filters['starttime']"/>
            ~
            <input id="endtime" name="filters['endtime']"/>
            &nbsp;
            发布者类型
            <select name="filters['type']" id="type" class="easyui-combobox">
                <option value="">全部</option>
                <option value="1">视频</option>
                <option value="2">直播回放</option>
                <option value="3">普通图片</option>
                <option value="4">预告图片</option>
            </select>
            <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
               iconCls="icon-search">搜索</a>
        </div>

        <div style="padding-left:20px;margin-top:10px;">
            <input style="float:left;margin:7px 3px 3px 8px;width:15px;height:18px;" type="checkbox"
                   onclick="selectAll()" id="sall"/><span
                style="float:left;height:18px;line-height:18px;margin-top:4px;">全选 </span>
            &nbsp;
            <button class="button red bigrounded" type="button" onclick="deleteAll()" style="width: 80px;height: 30px;">
                批量删除
            </button>
            &nbsp;
            <button class="button green bigrounded" type="button" onclick="downloadAll()"
                    style="width: 80px;height: 30px;">批量下载
            </button>
        </div>
    </form>
</div>

<div id='m1'
     style='visibility: hidden;border:1px solid #ccc;background-color:white;position: absolute;width:150px;text-align: center'>
    <div style="padding-bottom: 10px"><input type='radio' name='f1' value='check_ok'>通过&nbsp;<input type='radio'
                                                                                                    name='f1'
                                                                                                    value='check_fail'>不通过
    </div>
    <div><input type='button' class='button blue bigrounded' value='确定' onclick='javascript:checkVideo();'/>&nbsp;<input
            type='button' class='button blue bigrounded' value='关闭' onclick='javascript:mclose();'/></div>
</div>
<!-- 弹出的添加对话框 -->
<div id="updatedlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px" closed="true"
     buttons="#updatedlg-buttons">
    <form id="updatefm" method="post" novalidate>
        <input type="hidden" name="id"/>
    </form>
</div>

<!-- 对话框里的保存和取消按钮 -->
<div id="updatedlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveUser('#updatedlg','#updatefm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
</div>

<%-- <div id="tagDialog"  style="display:none">
    <ul style="display:table-row-group;">
        <c:forEach items="${tags}" var="tag" varStatus="status">
            <c:if test="${status.index%3==0 && status.index!=0}">
                </ul><ul style="display:table-row-group;">
            </c:if>
            <li sytle="text-align:left;display:table-cell;"><input type="checkbox" value="${tag.name}" onclick="selectTag(this)">&nbsp;${tag.name}</li>
        </c:forEach>
    </ul>
</div> --%>

<div id="activityDialog" style="display:none">
    <ul style="display:table-row-group;">
        <c:forEach items="${activites}" var="activity" varStatus="status">
        <c:if test="${status.index%3==0 && status.index!=0}">
    </ul>
    <ul style="display:table-row-group;">
        </c:if>
        <li sytle="text-align:left;display:table-cell;"><input type="checkbox" value="${activity.id}" name="activity"
                                                               onclick="selectActivity(this)">&nbsp;${activity.title}
        </li>
        </c:forEach>
    </ul>
</div>
<div id="detail" style="display:none">

</div>

<!-- 弹出的添加或者编辑对话框 -->
<div id="dlg" class="easyui-dialog" style="width:415px;height:390px;" closed="true">
    <div id="playerContainer"></div>
</div>

<!-- 弹出的上传图片对话框 -->
<div id="uploadPicDlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true"
     buttons="#uploadPicDlg-buttons">
    <div class="ftitle">上传图片</div>
    <!-- 添加 -->
    <form id="fm" method="post" novalidate>
        <div class="fitem">
            <label>图片地址:</label>
            <input name="hlsfiles" id="hlsfiles" type="file" style="width:50%"></input>
            <input name="videoPic" id="videoPic" type="hidden" style="width:50%"></input>
            <!-- 			<input name="videoListPic" id="videoListPic"  type="text" style="width:50%" readonly="readonly"></input> -->
            <div style="margin-left:auto;margin-right:auto;width:90%;display:none" id="p"
                 class="easyui-progressbar"></div>
        </div>
        <input type="hidden" name="videoId" id="videoId"/>
        <!-- START -->
        <form id="coords" class="coords" onsubmit="return false;">
            <div class="inline-labels">
                <label>X1 <input type="text" size="4" id="x1" name="x1" readonly="readonly"/></label>
                <label>Y1 <input type="text" size="4" id="y1" name="y1" readonly="readonly"/></label>
                <label>X2 <input type="text" size="4" id="x2" name="x2" readonly="readonly"/></label><br>
                <label>Y2 <input type="text" size="4" id="y2" name="y2" readonly="readonly"/></label>
                <label>W&nbsp; <input type="text" size="4" id="w" name="w" readonly="readonly"/></label>
                <label>H&nbsp; <input type="text" size="4" id="h" name="h" readonly="readonly"/></label>
                <br>
                <img alt="封面裁剪" src="" name="picCut" id="picCut" style="width: 240px;height: 240px;">
            </div>
        </form>
        <!-- END -->
    </form>
</div>
<!-- 添加对话框里的保存和取消按钮 -->
<div id="uploadPicDlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="cutImage('#uploadPicDlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#uploadPicDlg').dialog('close')">取消</a>
</div>

<div id="imageDlg" class="easyui-dialog" style="width:500px;height:500px;padding:5px 10px" closed="true">
    <img id="bigImage" src="" alt="图片" style="width: 450px;height: 450px;">
</div>

</body>
</html>
