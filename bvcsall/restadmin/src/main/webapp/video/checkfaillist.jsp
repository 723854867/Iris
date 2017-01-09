<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>视频审核</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="css/baseCss.css">
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>

    <script type="text/javascript">
        var deleteConfirmMessage = "你确定要删除吗?";
        var pageSize = 20;
        var searchFormId = "#searchForm";
        var listUrl = "video/searchVideoListPage?filters['flowStat']=check_fail";

        function datagridList(params) {
            params.rows = pageSize;
            params.page = 1;
            $.post(listUrl, params, function (result) {
                if (result.total > 0) {
                    var videos = result.rows;
                    if (videos.length > 0) {
                        var ul = $("#videoUl");
                        ul.html("");
                        for (var i = 0; i < videos.length; i++) {
                            var video = videos[i];
                            var li = $("<li></li>");
                            li.attr("id", "li_" + video.id);
                            var inner = '';
                            inner += '<div class="pic" id="playerContainer' + video.id + '">'
                                    + '</div>'
                                    + '<div class="clearYlz"></div>'
                                    + '<div class="userLBtn"><div style="width:280px;height: 30px;padding-top:5px">'
                                    + '<button onclick="delVideo(' + video.id + ')" class="button red bigrounded" style="width: 70px;height: 25px;">删除</button>'
                                    + '</div><div style="width:280px;height: 90px;padding-top:5px">'
                                    + video.description + '</div></div>';
                            li.html(inner);
                            ul.append(li);

                            playVideo(video.id, video.playKey);
                        }
                    }
                } else {
                    showMessage("Error", "没有视频，换个查询条件试试！");
                    var ul = $("#videoUl");
                    ul.html("");
                }
            });
        }

        $(function () {
            $('#starttime').datetimebox({
                showSeconds: false
            });
            $('#endtime').datetimebox({
                showSeconds: false
            });

            var params = [];
            datagridList(params);
        });

        function playVideo(playerId, fileName) {
            var _player = null;

            var player = $('<div/>');
            $(player).attr('id', 'pl' + playerId);

            $('#playerContainer' + playerId).append(player);
            var conf = {
                file: '${video_play_url_prefix}' + fileName + '.m3u8',
                image: '${img}',
                height: 200,
                width: 300,
                autostart: true,
                repeat: true,
                analytics: {enabled: false}
            };
            _player = jwplayer('pl' + playerId).setup(conf);
        }

        function checkVideo(mid, flowStat) {
            if (flowStat == "check_ok") {
                $.post("video/checkTongGuoPage", {ids: mid}, function (result) {
                    if (result["success"] == true) {
                        //datagridList();
                        removeVideoDom(mid);
                    } else {
                        showMessage("Error", result["message"]);
                    }
                });
            } else if (flowStat == "check_fail") {
                $.post("video/check_fail", {id: mid}, function (result) {
                    if (result["success"] == true) {
                        //datagridList();
                        removeVideoDom(mid);
                    } else {
                        showMessage("Error", result["message"]);
                    }
                });
            }
        }

        function delVideo(id) {
            $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
                if (r) {
                    $.post('video/logicremove', {ids: id}, function (result) {
                        if (result["success"] == true) {
                            //datagridList();
                            removeVideoDom(id);
                        } else {
                            showMessage("Error", result["message"]);
                        }
                    });
                }
            });
        }

        function removeVideoDom(id) {
            $("ul[id='videoUl'] li").remove("li[id='li_" + id + "']");
        }
        function doSearch() {
            datagridList(getFormJson(searchFormId));
        }
    </script>
</head>
<body>
<div data-options="region:'north',border:false"
     style="height: 40px; padding-top: 5px;  overflow: hidden;">
    <h2 style="float:left;padding-left:10px;margin: 1px">视频审核</h2>
    <span style="float:left;padding-left:20px;margin: 1px">未通过审核</span>
		<span style="float:left;padding-left:20px;margin: 1px">
			<a href="video/unchecklist" class="easyui-linkbutton">未审核</a>
		</span>
</div>
<form action="" id="searchForm">
    <div>
        审核人
        <select name="filters['auditorId']" value="">
            <option value="">全部</option>
            <c:forEach items="${users}" var="user">
                <option value="${user.id}">${user.username}</option>
            </c:forEach>
        </select>
        &nbsp;
        未通过原因
        <input name="filters['reason']" value=""/>
        &nbsp;
        发布时间
        <input id="starttime" name="filters['starttime']"/>
        ~
        <input id="endtime" name="filters['endtime']"/>
        &nbsp;
        视频描述
        <input name="filters['videoName']" value=""/>
        &nbsp;
        <input type="button" class="button blue bigrounded" onclick="doSearch()" style="width: 80px;height: 30px;"
               value="查询"/>
    </div>
</form>
<div class="userListVedioBox2" id="videos">
    <ul id="videoUl">

    </ul>
    <!-- 		<div id="loadNex" style="visibility: hidden"><a href="javascript:void(0)" onclick="loadNext()">加载更多</a></div> -->
</div>
</body>
</html>
