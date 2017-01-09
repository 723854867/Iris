<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2016/8/25
  Time: 15:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>房间详情</title>
    <script type='text/javascript' src='js/sewise.player.min.js'></script>
    <style>
        .room {
            width: 182px;
            height: 200px;
            float: left;
            overflow: hidden;
        }

        a {
            text-decoration: none;
        }
    </style>
    <script>
        function shieldLive(){
            if(confirm("确定要执行屏蔽操作吗？")){
                var roomId = $("#shieldRoomId").val();
                var expireTime = $("#expireTime").val();
                var shieldMessage = $("#shieldMessage").val();
                if(shieldMessage=='' || expireTime==''){
                    showMessage("提示信息", "不能为空！");
                    return;
                }
                $.ajax({
                    url: 'check/shieldLive',
                    type: 'post',
                    data: {roomId: roomId,expireTime:expireTime,shieldMessage:shieldMessage},
                    async: false, //默认为true 异步
                    error: function () {
                        showMessage("提示信息", "屏蔽失败，请重试！");
                    },
                    success: function (result) {
                        if (result.resultCode == "success") {
                            showMessage("提示信息", "屏蔽成功！");
                            $("#shieldText").text("已屏蔽");
                        } else {
                            showMessage("提示信息", "屏蔽失败，请重试！");
                        }
                    }
                });
            }
        }
        function offline() {
            var id = $("#roomId").val();
            var mess = $('#message').val();
            var expire = $('#expireMin').val();
            if(mess=='' || expire==''){
                showMessage("提示信息", "请输入数据！");
                return;
            }
            if(mess.length>16){
                showMessage("提示信息", "输入禁播原因不能超过16个字！");
                return;
            }
            $.ajax({
                url: 'living/offline',
                type: 'get',
                data: {roomId: id,message:mess,expireMin:expire},
                async: false, //默认为true 异步
                error: function () {
                    showMessage("提示信息", "下线失败，请重试！");
                },
                success: function (result) {
                    if (result == "success") {
                        showMessage("提示信息", "下线成功！");
                    } else {
                        showMessage("提示信息", "下线失败，请重试！");
                    }
                }
            });
        }
    </script>
</head>
<body>
<c:if test="${userInfo.isLive eq 1}">
    <div>
        <div style="background-color: #666666;color:#FFFFFF;height: 30px;width: 60%;text-align:center;">
            <span style="font-size: 14px;">审核操作</span>
        </div>
        <div>
            主播ID:${userInfo.userId}
            主播昵称:${userInfo.name}
            在线人数：${userInfo.onlineNumber}
        </div>
    </div>
    <div>
        <div style="background-color: #666666;color:#FFFFFF;height: 25px;width: 60%;text-align:center;">
            <span style="font-size: 14px;">屏蔽</span>
        </div>
        <!-- 列表上面的按钮和搜索条件  -->
        <div id="tb3" style="padding:5px;height:auto">
            <form action="" id="searchForm3">
                <input type="hidden" value="${userInfo.id}" id="shieldRoomId" name="roomId">
                <div>
                    屏蔽原因：<br/>
                    <input type="radio" name="reason" onclick="$('#shieldMessage').val('画面转播电影、电视或游戏')">
                    画面转播电影、电视或游戏<br/>
                    <input type="radio" name="reason" onclick="$('#shieldMessage').val('语言低俗、骂人')"> 语言低俗、骂人<br/>
                    <input type="radio" name="reason" onclick="$('#shieldMessage').val('内容低俗无意义')"> 内容低俗无意义<br/>
                    <input type="radio" name="reason" onclick="$('#shieldMessage').val('挂机、黑屏、定格')"> 挂机、黑屏、定格<br/>
                    <input class="easyui-validatebox" data-options="lenght:20" id="shieldMessage" name="shieldMessage"
                           required="true">
                    屏蔽时间(分)：<input class="easyui-validatebox easyui-numberbox" id="expireTime" name="expireTime"
                                   data-options="validType:'int',min:0" required="true" value="">
                    <span id="shieldText">
                        <c:choose>
                            <c:when test="${userInfo.shieldStatus eq 1}">
                                已屏蔽
                            </c:when>
                            <c:otherwise>
                                <a href="javascript:;" onclick="shieldLive()" class="easyui-linkbutton" iconCls="icon-cancel">
                                    屏蔽
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </form>
        </div>
    </div>
    <div>
        <div style="background-color: #666666;color:#FFFFFF;height: 30px;width: 60%;text-align:center;">
            下线
        </div>
        <div id="tb2" style="padding:5px;height:auto">
            <form action="" id="searchForm2">
                <input type="hidden" value="${userInfo.id}" id="roomId" name="roomId">
                <div>
                    下线原因：<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('色情、裸露尺度过大')"> 色情、裸露尺度过大<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('内容低俗')"> 内容低俗<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('宣传其他平台')"> 宣传其他平台<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('枪支、毒品、赌场')"> 枪支、毒品、赌场<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('直播吸烟')"> 直播吸烟<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('暴力、血腥、恐怖内容')"> 暴力、血腥、恐怖内容<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('麻将、扑克类游戏')"> 麻将、扑克类游戏<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('拍摄、转播其他直播平台')"> 拍摄、转播其他直播平台<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('服装涉及国会、党徽、军徽、警徽')">服装涉及国会、党徽、军徽、警徽<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('未成年直播')"> 未成年直播<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('危险驾驶')"> 危险驾驶<br/>
                    <input type="radio" name="reason" onclick="$('#message').val('涉及政治、宗教内容')"> 涉及政治、宗教内容<br/>
                    <input class="easyui-validatebox" data-options="lenght:20" id="message" name="message"
                           required="true">
                    下线时间(分)：<input class="easyui-validatebox easyui-numberbox" id="expireMin" name="expireMin"
                                   data-options="validType:'int',min:0" required="true" value="">
                    <a href="javascript:;" onclick="offline()" class="easyui-linkbutton" iconCls="icon-remove">
                        下线
                    </a>
                </div>
            </form>
        </div>
    </div>
    <div style="float: right;position: absolute;top:10px;right: 150px;">
        <div id="container" style="width: 275px; height: 431px; ">
            <script type="text/javascript">
                SewisePlayer.setup({
                    server: "vod",
                    type: "flv",
                    videourl: "${userInfo.rtmpLiveUrl}",
                    poster: "http://jackzhang1204.github.io/materials/poster.png",
                    skin: "vodWhite",
                    title: "${userInfo.name}",
                    lang: 'en_US',
                    claritybutton: 'disable'
                }, "container");
            </script>
        </div>
    </div>
</c:if>
<div>
    <div style="background-color: #666666;color:#FFFFFF;height: 25px;width: 100%;text-align:center;">
        <span style="font-size: 14px;">历史截图</span>
    </div>
    <div class="basic" style="margin-top: 10px;">
        <c:forEach var="entry" items="${screenshot}">
            <div class="room" style="border: 1px solid #ccc;margin: 5px;height: 180px;">
            <span class="image">
                <img src="${entry}" style="width: 180px;height: 180px;" class="roomImage">
            </span>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
