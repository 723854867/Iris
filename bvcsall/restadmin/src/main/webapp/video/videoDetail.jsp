<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>视频详情</title>
    <!--
    <script type="text/javascript" src="js/ajaxfileupload.js"></script>
     -->
    <link rel="stylesheet" type="text/css" href="css/baseCss.css">
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script src="js/fp/jquery.iframe-transport.js"></script>
    <script src="js/fp/jquery.fileupload.js"></script>

    <script type='text/javascript' src='js/players/bootstrap.min.js'></script>
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
    <script type="text/javascript" src="js/admin/query_video.js"></script>
    <script type="text/javascript">


        function playVideo() {
            var player = $('<div/>');
            $(player).attr('id', 'player_id');
            $('#div_container').append(player);
            var conf = {
                file: '${video_play_url_prefix}${video.playKey}.m3u8',
                image: '${img}',
                height: 230,
                width: 450,
                autostart: true,
                analytics: {enabled: false}
            };
            _player = jwplayer('player_id').setup(conf);
        }
        var vid = "${video.id}";
        $(function () {
            playVideo();
        });
    </script>
</head>
<body>

<table id="displayTable" data-options="border:false,toolbar:'#tb'"></table>
<div region="north" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;" id="tb">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">视频管理</h2>

        <h3 style="float:left;margin-left:20px;margin-right:20px;margin-top:5px; ">视频详情</h3>
        <a href="javascript:window.history.go(-1)" class="easyui-linkbutton" type="button">返回</a>
    </div>
    <div style="margin:10px auto;height:240px;">
        <div style="width:460px;float:left;overflow: hidden;" id="div_container"></div>
        <div style="margin-left: 20px;width:300px;float:left;">
            <span style="padding:10px 10px">描述：${video.description }</span><br><br>
            <span style="padding:10px 10px">话题：${video.tag}</span><br><br>
            <span style="padding:10px 10px">活动：${video.actives }</span><br><br>
            <span style="padding:10px 10px">上传时间：${video.createDateStr }</span><br><br>
            <span style="padding:10px 10px">上传者：${video.uploader }</span><br><br>
            <span style="padding:10px 10px">点赞：${video.praiseCount}个 &nbsp;&nbsp;评论：${video.evaluationCount}次&nbsp;&nbsp;转发：${video.forwardCount }</span>
        </div>
    </div>
    <div style="margin-left:10px;">
		<span>
			<a href="javascript:;" class="easyui-linkbutton" onclick="showActivitiesDialog(${video.id})"
               data-options="iconCls:'icon-add'">加入活动</a>
		</span>
		<span>
			<c:choose>
                <c:when test="${video.playRateToday > 0}">
                    <a href="javascript:;" class="easyui-linkbutton" onclick="removeHotVideo(${video.id})"
                       data-options="iconCls:'icon-remove'">移除热门视频</a>
                </c:when>
                <c:otherwise>
                    <a href="javascript:;" class="easyui-linkbutton" onclick="addHotVideo(${video.id})"
                       data-options="iconCls:'icon-add'">加入热门视频</a>
                </c:otherwise>
            </c:choose>
		</span>
		<span>
			<a href="video/multidown?ids=${video.id}" class="easyui-linkbutton"
               data-options="iconCls:'icon-save'">下载</a>
		</span>
		<span>
			<a href="javascript:;" class="easyui-linkbutton" onclick="insertComment()"
               data-options="iconCls:'icon-add'">添加评论</a>
		</span>
		<span>
			<a href="javascript:;" class="easyui-linkbutton" onclick="deleteVideo(${video.id})"
               data-options="iconCls:'icon-remove'">删除</a>
		</span>
    </div>
    <div>
        <h3>视频评论</h3>
    </div>
</div>
<%--        <div class="mod-bd">
            <div class="dis" id="sub1">
                <div class="userListVedioBox">
                    <c:forEach items="${evaluations}" var="eva">
                        <div>${eva.username}&nbsp;&nbsp;发表了评论</div>
                        <div style="padding-left:20px">${eva.content}</div>
                        <div style="padding-left:20px"><fmt:formatDate value="${eva.createDate}" pattern="yyyy-MM-dd"
                                                                       type="date"/>&nbsp;&nbsp;
                            <button class="edit-Btn mLRB" onclick="deleteEvaluation(${eva.id})">删除</button>
                        </div>
                    </c:forEach>
                </div>
            </div>
            &lt;%&ndash;<div class="undis" id="sub2">
                <div class="userListVedioBox" id="videos">

                </div>
            </div>&ndash;%&gt;
        </div>--%>

<!--添加评论弹出框 start-->
<div id="insert_comment_dlg" class="easyui-dialog" style="width:400px;height:300px;" closed="true"
     buttons="#insert-comment-dlg-buttons">
    <table class="table-doc" cellspacing="0" width="100%">
        <caption></caption>
        <tr>
            <td class="row">评论人：</td>
            <td class="row">
                <input class="easyui-combobox" id="creator_id" name="creator_id"
                       data-options="url: 'comboBox/getUserComboBoxByType',method: 'get',valueField:'id',textField:'text', require:true,validType:'selectValueRequired'">
            </td>
        </tr>
        <tr>
            <td class="row">评论内容：</td>
            <td class="row">
				<textarea rows="5" cols="30" name="content" id="content" class="easyui-validatebox">
                </textarea>
            </td>
            <input type="hidden" id="videoId" name="videoId" value="${video.id}">
        </tr>
    </table>
</div>
<!-- 添加评论弹出框里的保存和取消按钮 -->
<div id="insert-comment-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertComment()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_comment_dlg').dialog('close')">取消</a>
</div>

<!--活动列表弹出框开始-->
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
<!--活动列表弹出框结束-->
</body>
</html>