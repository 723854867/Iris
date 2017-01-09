<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>活动详情</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type='text/javascript' src='js/players/bootstrap.min.js'></script>
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
    <script type='text/javascript' src='js/admin/query_activity_detail.js'></script>
    <script type="text/javascript">
        var listUrl = "activity/queryActivityVideos?activityId=${activityId}";
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
                        {
                            field: 'videoPic',
                            title: '<span class="columnTitle">视频缩略图</span>',
                            width: 120,
                            align: 'center',
                            formatter: function (value, row) {
                                if (value == null) {
                                    return "";
                                } else {
                                    return "<div id='playerContainer" + row.id + "'><img style='height:100px;padding-left:10px;cursor: pointer;border: none;margin: 3px auto;' src='/restadmin/download/" + value + "' title='活动图片' onclick='playVideo(" + row.id + ",\"" + row.playKey + "\")'/></div>";
                                }
                            }
                        },
                        {
                            field: 'description',
                            title: '<span class="columnTitle">视频描述</span>',
                            width: 80,
                            align: 'center'
                        },
                        {
                            field: 'tag', title: '<span class="columnTitle">话题</span>', width: 80, align: 'center'
                        },
                        {
                            field: 'username',
                            title: '<span class="columnTitle">上传人</span>',
                            width: 80,
                            align: 'center',
                            formatter: function (value, row) {
                                var userId = "<div><span>用户ID：</span><span>" + row.userId + "</span></div>";
                                var username = "<div><span>用户名：</span><span>" + row.username + "</span></div>";
                                var name = "<div><span>昵称：</span><span>" + row.uname + "</span></div>";
                                var phone = "<div><span>手机号码：</span><span>" + row.phone + "</span></div>";
                                return userId + username + name + phone;
                            }
                        },
                        {
                            field: 'details',
                            title: '<span class="columnTitle">详情参数</span>',
                            width: 120,
                            align: 'center',
                            formatter: function (value, row) {
                                var createDateParam = "<div><span>上传时间：</span><span>" + row.createDateStr + "</span></div>";
                                //var uploader = "<div><span>上传人：</span><span>" + row.username + "(ID:"+row.userId+")</span></div>";
                                var content = "<div>" +
                                        "<div><span>点赞：</span><span>" + row.praiseCount + "</span><span>&nbsp;&nbsp;转发：</span><span>" + row.forwardCount + "</span>" +
                                        "<span>&nbsp;&nbsp;评价：</span><span>" + row.evaluationCount + "</span><span style='margin-left: 5px;'><a href='javascript:;' onclick='viewComment(" + row.videoId + ")'>查看评价</a></span></div>" +
                                        "</div>";
                                var dataStr = '<div>' + content + '</div>';
                                return createDateParam + dataStr;
                            }
                        },
                        {
                            field: 'orderNum',
                            title: '<span class="columnTitle">排序</span>',
                            width: 120,
                            align: 'center',
                            formatter: function (value, row) {
                                var sortStr = '<div><span><a href="javascript:;" onclick="activityVideoSort(' + row.id + ',${activityId},1)">置顶</a></span></div><div><span><a href="javascript:;" onclick="activityVideoSort(' + row.id + ',${activityId},2)">上移</a></span></div><div><span><a href="javascript:;" onclick="activityVideoSort(' + row.id + ',${activityId},3)">下移</a></span></div>';
                                var editStr = '<input onblur="editActivityVideoOrderNum(' + row.id + ')" id="orderNum' + row.id + '" name="orderNum" value="' + value + '" class="easyui-numberbox" min="0" max="" precision="0" maxlength="5" size="5"/>';
                                return sortStr + editStr;
                            }
                        },
                        {
                            field: 'modify', title: '<span class="columnTitle">操作</span>', width: 120, align: 'center',
                            formatter: function (value, row) {
                                var joinActivity = '<a href="javascript:;" onclick="showActities(' + row.videoId + ')"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">加入活动</span><span class="l-btn-icon icon-add">&nbsp;</span></span></a>';
                                var downloadStr = '<a href="video/multidown?ids='+row.videoId+'"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">下载</span><span class="l-btn-icon icon-save">&nbsp;</span></span></a>';
                                var removeStr = '<div><a href="javascript:;" onclick="removeActivityVideoById(' + row.id + ')"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">从此活动中移除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a></div>';
                                var outStr = '<a href="javascript:;" onclick="openWin(this,\'' + row.activityId + '\',\'' + row.videoId + '\')"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">移出视频</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                                return joinActivity + " " + downloadStr + " " + outStr + " " + removeStr;
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

        function doSearch() {
            var queryParams = $('#displayTable').datagrid('options').queryParams;
            queryParams.dataFrom = $('#dataFrom').combobox('getValue');
            queryParams.description = $('#description').val();
            queryParams.user = $('#user').combobox('getValue');
            queryParams.userKeyword = $('#userKeyword').val();
            queryParams.startTime = $('#startTime').datebox('getValue');
            queryParams.endTime = $('#endTime').datebox('getValue');
            $('#displayTable').datagrid({url: listUrl});
        }

        var _player = null;
        var _url = null;
        $(document).ready(function () {
            var player = $('<div/>');
            $(player).attr('id', 'player_id');
            $('#div_container').append(player);
            var conf = {
                file: '${video_play_url_prefix}/${activity.playkey}.m3u8',
                image: '${img}',
                height: 150,
                width: 150,
                autostart: true,
                analytics: {enabled: false}
            };
            _player = jwplayer('player_id').setup(conf);
        });

        function playVideo(playerId, fileName) {
            var _player = null;
            var player = $('<div/>');
            $(player).attr('id', 'pl' + playerId);
            $('#playerContainer' + playerId).empty();
            $('#playerContainer' + playerId).append(player);
            var conf = {
                file: '${video_play_url_prefix}' + fileName + '.m3u8',
                image: '${img}',
                height: 150,
                width: 200,
                autostart: true,
                analytics: {enabled: false}
            };
            _player = jwplayer('pl' + playerId).setup(conf);
        }

        function activityHide() {
            $("#activity_div").hide();
            $("#activity_button").attr("onclick", "activityShow()");
            $("#clear_id").attr("class", "l-btn-icon icon-tip");
            $("#text_id").text("显示活动信息");
            doSearch();
        }

        function activityShow() {
            $("#activity_div").show();
            $("#activity_button").attr("onclick", "activityHide()");
            $("#clear_id").attr("class", "l-btn-icon icon-clear");
            $("#text_id").text("隐藏活动信息");
            doSearch();
        }

        function openWin(obj, activityId, videoId) {
            var url = "<c:url value='/activity/showRemoveActivityList?activityId='/>" + activityId + "&videoId=" + videoId;
            var rValue = openWindow(url, '', '', '');
        }

    </script>
</head>
<body>
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">活动管理</h2>
    </div>
    <div id="activity_div" style="margin-left:30px;">
        <div>
            <table width="50%">
                <tr>
                    <c:if test="${not empty activity.cover}">
                        <td>

                            <img src="/restadmin/download<c:out value="${activity.cover}"/>" width="150px"
                                 height="150px">

                            <div>封面</div>
                        </td>
                    </c:if>
                    <c:if test="${not empty activity.bannerPic}">
                        <td>
                            <img src="/restadmin/download/<c:out value="${activity.bannerPic}"/>" width="150px"
                                 height="150px">

                            <div>banner图</div>
                        </td>
                    </c:if>
                    <c:if test="${not empty activity.videoCoverPic}">
                        <td>
                            <img src="/restadmin/download/<c:out value="${activity.videoCoverPic}"/>" width="150px"
                                 height="150px">

                            <div>宣传视频封面</div>
                        </td>
                    </c:if>
                    <c:if test="${not empty activity.playkey}">
                        <td>
                            <div id="div_container"></div>
                            <div>宣传视频</div>
                        </td>
                    </c:if>
                </tr>
            </table>
        </div>
        <div>
            <div>
            <span align="right">
                <label>标题：</label>
            </span>
            <span align="left">
                ${activity.title}
            </span>
            </div>
            <div>
            <span align="right">
                <label>位置：</label>
            </span>
            <span align="left">
                <c:if test="${activity.groupType eq 0 }">首页推荐</c:if>
                <c:if test="${activity.groupType eq 1 }">所有活动</c:if>
                <c:if test="${activity.groupType eq 2 }">发现页推荐</c:if>
            </span>
            </div>
            <div>
            <span align="right">
                <label>创建时间：</label>
            </span>
            <span align="left">
                <fmt:formatDate value="${activity.createDateStr}" pattern="YYYY-MM-DD HH:mm:ss"/>
            </span>
            </div>
            <div>
            <span align="right">
                <label>点赞：</label>
            </span>
            <span align="left">
                ${pCount} 个 <label>评论：</label>${eCount} 条 <label>马甲参与视频：</label>${vCount} 个 <label>Blive参与视频：</label>${uvCount} 个
                <label>参与人数：</label>${uCount} 人
            </span>
            </div>
        </div>
    </div>
    <div style="margin-left:30px;margin-top:10px;">
            <span>
            <c:if test="${activity.status eq 0 }">
                <a href="javascript:;" onclick="updateActiveStatus(this,${activity.id},1);" class="easyui-linkbutton"
                   data-options="iconCls:'icon-add'">上线</a>
            </c:if>
            <c:if test="${activity.status eq 1 }">
                <a href="javascript:;" onclick="updateActiveStatus(this,${activity.id},0);"
                   class="easyui-linkbutton" data-options="iconCls:'icon-remove'">下线</a>
            </c:if>
            </span>
            <span style="margin-left: 5px;">
                <a href="activity/showActivityAdd?activetyId=${activityId}" class="easyui-linkbutton"
                   data-options="iconCls:'icon-edit'">编辑</a>
            </span>
            <span style="margin-left: 5px;">
                <a href="javascript:;" onclick="deleteActivity(${activity.id})" class="easyui-linkbutton"
                   data-options="iconCls:'icon-cut'">删除</a>
            </span>
            <span style="margin-left: 5px;">
                <a href="javascript:;" id="activity_button" class="easyui-linkbutton" onclick="activityHide()"><span
                        class="l-btn-left l-btn-icon-left" style="padding: 0;"><span class="l-btn-text" id="text_id">隐藏活动信息</span><span
                        class="l-btn-icon icon-clear" id="clear_id">&nbsp;</span></span></a>
            </span>
    </div>
    <div style="margin-left:30px;">
        <h3>参与视频</h3>
    </div>
    <div style="margin-left:30px;font-size: 12px;">
        <table>
            <tr>
                <td>
                    <label>
                        <select class="easyui-combobox" name="user" id="user">
                            <option value="">用户搜索</option>
                            <option value="1">用户ID</option>
                            <option value="2">用户名</option>
                            <option value="3">昵称</option>
                            <option value="4">手机号码</option>
                        </select>
                    </label>
                </td>
                <td>
                    <input type="text" name="userKeyword" id="userKeyword" class="easyui-validatebox">
                </td>
                <td align="left">
                    <label>发布者分类：</label>
                </td>
                <td align="left">
                    <select id="dataFrom" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="myvideo_restadmin">官网视频</option>
                        <option value="myvideo_restwww">用户视频</option>
                    </select>
                </td>
                <td>
                    <label>视频描述：</label>
                </td>
                <td align="left">
                    <input type="text" name="description" id="description" class="easyui-validatebox">
                </td>
                <td>
                    <label>上传时间：</label>
                </td>
                <td>
                    <input type="text" class="easyui-datebox" name="startTime"
                           id="startTime">&nbsp;至&nbsp;<input
                        type="text" class="easyui-datebox" name="endTime" id="endTime">
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
        <div>
            <a id="batch_remove" href="javascript:;" onclick="removeActivityVideos()" class="easyui-linkbutton"
               data-options="iconCls:'icon-remove'">从此活动移除</a>
            <a id="batch_download" href="javascript:;" onclick="batchDownload()" class="easyui-linkbutton"
               data-options="iconCls:'icon-save'">批量下载</a>
            <a id="batch_export" href="javascript:;" onclick="exportActivityVideosExcel(${activityId})"
               class="easyui-linkbutton"
               data-options="iconCls:'icon-print'">导出视频信息</a>
        </div>
    </div>

</div>

<!--加入活动弹出框开始-->
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
<!--加入活动弹出框结束-->

<!--查看评价弹出框开始-->
<div id="viewCommentDialog" class="easyui-dialog" style="width:690px;height:600px;padding:5px 10px" closed="true"
     buttons="#vdlg-buttons">
    <table id="commentTable" data-options="border:false,toolbar:'#tb'">
    </table>
    <div id="tb" style="padding:5px;height:auto">
    </div>
</div>
<!--查看评价弹出框结束-->

</body>
</html>