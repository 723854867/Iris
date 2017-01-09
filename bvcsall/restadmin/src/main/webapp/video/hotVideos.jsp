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
        var listUrl = "video/searchHotVideoListPage";
        if (popFlg && popFlg == '1') {
            listUrl += "&filters['popFlg']=1";
        }
        var updateUrl = "video/update";
        var deleteUrl = "video/deleteallpage";

        var url;

        function datagridList() {
            $(datagridId).datagrid({
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
                onClickCell: function (index, field, value) {
                    if (field == 'playRateToday') {
                        var ind = document.getElementById("playRate" + index);
                        ind.focus();
                    } else {
                        $(datagridId).focus();
                    }
                },
                columns: [[
                    {field: 'ck', width: 100, checkbox: true},
                    {
                        field: 'img', title: '缩略图', width: 100, height: 150, formatter: function (value, row, index) {
                        var content = "<div><div style='width:110px;height:75px;margin: 0 auto;'> "
                                + "<div style='width:110px;height:75px;' id='playerContainer" + row.id + "' onclick='playVideo(\"" + row.id + "\",\"" + row.playKey + "\")'><img src='<%=request.getContextPath()%>/download" + row.videoPic + "' style='height:75px;width:100px;'/></div> "
                                + "</div>";
                        return content;
                    }
                    },
                    {
                        field: 'description',
                        title: '视频描述',
                        width: 200,
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
                        field: 'actives',
                        title: '参与活动',
                        width: 200,
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
                        field: 'uploader',
                        title: '上传者',
                        width: 200,
                        nowrap: false,
                        formatter: function (value, row, index) {
                            var name = "<div><span>用户昵称：</span>" + row.uploader + "</div>";
                            var userId = "<div><span>用户ID：</span>" + row.creatorId + "</div>";
                            var username = "<div><span>用户名：</span>" + row.username + "</div>";
                            if(row.phone != null && row.phone != ""){
                                var phone = "<div><span>手机号码：</span>" + row.phone + "</div>";
                            }else{
                                var phone = "";
                            }
                            return userId + username + name + phone;
                        }
                    },
                    {
                        field: 'flowStat',
                        title: '详情参数',
                        width: 200,
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

                            var content = "<span style='width:100%;height:30%;color:#969696;padding:5px 5px'>上传时间：" + row.createDateStr + "</span><br>"
                                    + "<span style='color:#969696;padding:5px 5px'>状态：" + stat + "</span><br> "
                                    + "<span style='color:#969696;padding:5px 5px'>点赞：" + row.praiseCount + "个  评论：" + row.evaluationCount + "次</span><br> "
                                    + "<span style='color:#969696;padding:5px 5px'>转发：" + row.forwardCount + "次</span><br> ";
                            return content;
                        }
                    },
                    {
                        field: 'playRateToday', title: '排序', width: 100, formatter: function (value, row, index) {
                        var content = "<span style='color:#969696;padding:10px 10px'>"
                                + "<a href='javascript:void(0)' onclick='goTop(" + row.id + ")' type='button'><span class='l-btn-left'><span class='l-btn-text'>置顶</span></span></a></span><br>";
                        content += content = "<span style='color:#969696;padding:10px 10px'>"
                                + "<a href='javascript:void(0)' onclick='goUp(" + row.id + ")' type='button'><span class='l-btn-left'><span class='l-btn-text'>上移</span></span></a></span><br>";
                        content += content = "<span style='color:#969696;padding:10px 10px'>"
                                + "<a href='javascript:void(0)' onclick='goDown(" + row.id + ")' type='button'><span class='l-btn-left'><span class='l-btn-text'>下移</span></span></a></span><br>";
                        content += content = "<span style='color:#969696;padding:10px 10px'>"
                                + "<input id='playRate" + index + "' onclick='setTextValuePosition("+ index + ")' type='text' value='" + value + "' style='width:55px' onchange='goChange(" + row.id + ",this.value)'></span><br>";
                        return content;
                    }
                    },
                    {
                        field: 'operation', title: '操作', width: 150, formatter: function (value, row, index) {
                        var content = "<span style='color:#969696;padding:10px 10px'>"
                                + "<a href='javascript:void(0)' onclick='showActities(" + row.id + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>加入活动</span><span class='l-btn-icon icon-add'>&nbsp;</span></span></a></span>";
                        content += "<span style='width:100%;height:30%;color:#969696;padding:10px 10px'><a href='video/multidown?ids=" + row.id + "'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>下载</span><span class='l-btn-icon icon-save'>&nbsp;</span></span></a></span> "
                                + "<br>";
                        content += "<span style='color:#969696;padding:10px 10px'>"
                                + "<a href='javascript:void(0)' onclick='removeHotVideo(" + row.id + ")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>热门视频</span><span class='l-btn-icon icon-remove'>&nbsp;</span></span></a></span>";
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

                        return content;
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

        function removeHotVideo(vid) {
            $.post('video/removeHotVideos', {ids: vid}, function (result) {
                if (result["success"] == true) {
                    $(datagridId).datagrid('reload'); // reload the user data
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function goTop(vid) {
            $.post('video/addHotVideoTop', {id: vid}, function (result) {
                if (result["success"] == true) {
                    $(datagridId).datagrid('reload'); // reload the user data
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function goUp(vid) {
            $.post('video/moveHotVideoUp', {id: vid}, function (result) {
                if (result["success"] == true) {
                    $(datagridId).datagrid('reload'); // reload the user data
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function goDown(vid) {
            $.post('video/moveHotVideoDown', {id: vid}, function (result) {
                if (result["success"] == true) {
                    $(datagridId).datagrid('reload'); // reload the user data
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function goChange(vid, value) {
            var r = /^[0-9]+(.[0-9]{0,4})?$/;
            if (r.test(value)) {
                $.post('video/changePlayRateToday', {videoId: vid, playRateToday: value}, function (result) {
                    if (result["success"] == true) {
                        $(datagridId).datagrid('reload'); // reload the user data
                    } else {
                        showMessage("Error", result["message"]);
                    }
                });
            } else {
                showMessage("Error", "请输入大于0的数字");
                $(datagridId).datagrid('reload');
            }
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


        function setTextValuePosition(obj) {
            var tobj = document.getElementById("playRate"+obj);
            var spos = tobj.value.length;
            if (tobj.setSelectionRange) { //兼容火狐
                setTimeout(function () {
                    tobj.setSelectionRange(spos, spos);
                    tobj.focus();
                }, 0);
            } else if (tobj.createTextRange) { //兼容IE
                var rng = tobj.createTextRange();
                rng.move('character', spos);
                rng.select();
            }
        }
    </script>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" region="north" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false" style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">最热视频</h2>
        <h2 style="float:left;padding-left:50px;margin: 1px">
            <a href="video/uservideos" class="easyui-linkbutton">视频管理</a>
        </h2>
    </div>
    <form action="" id="searchForm">
        <div style="padding:10px;">
            <select class="easyui-combobox" id="user" name="filters['user']">
                <option value="">用户搜索</option>
                <option value="1">用户ID</option>
                <option value="2">用户名</option>
                <option value="3">昵称</option>
                <option value="4">手机号码</option>
            </select>
            <input type="text" name="filters['userKeyword']" id="userKeyword" class="easyui-validatebox">
            &nbsp;
            发布者类型
            &nbsp;
            <select name="filters['dataFrom']" id="dataFrom" class="easyui-combobox">
                <option value="">全部</option>
                <option value="myvideo_restadmin">管理员视频</option>
                <option value="myvideo_restwww">用户视频</option>
            </select>
<%--            &nbsp;
            用户名/马甲名
            <input name="filters['creatorName']" value=""/>--%>
            &nbsp;
            视频描述
            <input name="filters['videoName']" value=""/>
            &nbsp;
            上传时间
            <input id="starttime" name="filters['starttime']"/>
            ~
            <input id="endtime" name="filters['endtime']"/>
            &nbsp;
            &nbsp;活动&nbsp;
            <input class="easyui-combobox" id="activities" name="filters['activity']"
                   data-options="url: 'comboBox/getActivityCombobox',method: 'get',valueField:'id',textField:'text'">
            &nbsp;话题&nbsp;
            <input type="text" name="filters['label']" id="label"
                   class="easyui-validatebox easyui-combobox"
                   data-options="url:'comboBox/queryComboBoxLabels',method: 'get',valueField:'id',textField:'text'">
            &nbsp;
            状态
            <select name="filters['flowStat']" class="easyui-combobox">
                <option value="">显示全部</option>
                <option value="check_ok">审核通过</option>
                <option value="uncheck">未审核</option>
                <option value="check_fail">审核未通过</option>
                <option value="delete">已删除</option>
            </select>
            &nbsp;
            排行榜
            &nbsp;
            <select name="filters['rankAble']" id="rankAble" class="easyui-combobox">
                <option value="">全部</option>
                <option value="1">禁止</option>
                <option value="0">允许</option>
            </select>
            &nbsp;
            <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
               iconCls="icon-search">搜索</a>
        </div>
        <div style="padding:10px 10px 10px 25px;">
            <input type="checkbox" onclick="selectAll()" id="sall"/><span style='height:13px;line-height:13px;'>全选</span>
            &nbsp;
            <button class="button green bigrounded" type="button" onclick="deleteAll()"
                    style="width: 80px;height: 30px;">批量删除
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
</body>
</html>
