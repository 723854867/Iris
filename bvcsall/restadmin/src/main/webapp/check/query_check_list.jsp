<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2015/12/23
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>直播审核</title>
    <style>
        .room{
            width: 182px;
            height: 200px;
            float: left;
            overflow: hidden;
        }
        a{
            text-decoration: none;
        }
    </style>
    <script type="text/javascript">

        var roomInfo = "";
        $(function () {
            $("#passworddlg-buttons").attr("style","display:none;");
            var roomData = $("#roomData").val();
            roomData = roomData.substring(0,roomData.length-1);
            var roomArray = roomData.split(",");
            for(var i=0;i<roomArray.length;i++){
                var result = setInterval("autoGetNewScreenshot("+roomArray[i]+")",5000);
                roomInfo += roomArray[i]+"-"+result+",";
            }
            $("#removeRoomData").val(roomInfo);
        })

        function autoGetNewScreenshot(roomId){
            if(roomId > 0) {
                $.ajax({
                    url: 'check/getNewScreenshot',
                    type: 'get',
                    data: {roomId: roomId},
                    async: false, //默认为true 异步
                    error: function () {
                        //刷新当前页面
                    },
                    success: function (result) {
                        if (result.resultCode == "601") {
                            //直播已结束，清除房间信息
                            var removeRoomData = $("#removeRoomData").val();
                            removeRoomData = removeRoomData.substring(0,removeRoomData.length-1);
                            var removeRoomArray = removeRoomData.split(",");
                            for(var i=0;i<removeRoomArray.length;i++){
                                var removeArray = removeRoomArray[i].split("-");
                                if(removeArray[0] == roomId){
                                    clearInterval(removeArray[1]);
                                }
                            }
                            var roomCount = $("#roomCount").text();
                            $("#roomCount").text(roomCount-1);
                            $("#room_"+roomId).remove();
                        }else{
                            $("#screenshot_"+roomId).attr("src",result.data.screenshotUrl);
                            $("#online_number_"+roomId).text(result.data.onlineNumber);
                        }
                    }
                });
            }
        }
        function settingLiveOfflineDlg(id){
            $("#roomId").val(id);
            $("#dlg2").dialog('open').dialog('setTitle', "下线禁播");
        }
        
        function shieldLiveDlg(id) {
            $("#shieldRoomId").val(id);
            $("#dlg3").dialog('open').dialog('setTitle', "屏蔽");
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
                        $('#dlg2').dialog('close');
                        var removeRoomData = $("#removeRoomData").val();
                        removeRoomData = removeRoomData.substring(0,removeRoomData.length-1);
                        var removeRoomArray = removeRoomData.split(",");
                        for(var i=0;i<removeRoomArray.length;i++){
                            var removeArray = removeRoomArray[i].split("-");
                            if(removeArray[0] == id){
                                clearInterval(removeArray[1])
                            }
                        }
                        var roomCount = $("#roomCount").text();
                        $("#roomCount").text(roomCount-1);
                        $("#room_"+id).remove();
                    } else {
                        showMessage("提示信息", "下线失败，请重试！");
                    }
                }
            });
        }
/*        function closeAccount(roomId){
            if(confirm("确定要执行封号操作吗？")){
                $.ajax({
                    url: 'check/closeAccount',
                    type: 'get',
                    data: {roomId: roomId},
                    async: false, //默认为true 异步
                    error: function () {
                        showMessage("提示信息", "封号失败，请重试！");
                    },
                    success: function (result) {
                        if (result == "success") {
                            showMessage("提示信息", "封号成功！");
                            $("#room_"+roomId).remove();
                            var removeRoomData = $("#removeRoomData").val();
                            removeRoomData = removeRoomData.substring(0,removeRoomData.length-1);
                            var removeRoomArray = removeRoomData.split(",");
                            for(var i=0;i<removeRoomArray.length;i++){
                                var removeArray = removeRoomArray[i].split("-");
                                if(removeArray[0] == roomId){
                                    clearInterval(removeArray[1])
                                }
                            }
                            var roomCount = $("#roomCount").text();
                            $("#roomCount").text(roomCount-1);
                        } else {
                            showMessage("提示信息", "封号失败，请重试！");
                        }
                    }
                });
            }
        }*/

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
                            $('#dlg3').dialog('close');
                        } else {
                            showMessage("提示信息", "屏蔽失败，请重试！");
                        }
                    }
                });
            }
        }

        function getHistoryScreenshot(roomId,value) {
            if(value == "ucloud"){
                showMessage("提示信息","ucloud CDN 暂不支持历史图片哦");
            }else if(value == "plxy"){
                showMessage("提示信息","星域 CDN 暂不支持历史图片哦");
            }else if(value == "plls"){
                showMessage("提示信息","乐视 CDN 暂时木有截图功能哦");
            }else if(value == "unknown"){
                showMessage("提示信息","暂时木有截图功能哦");
            }else{
                var title = '请求明细';
                var url = 'check/historyTemplate?roomId=' + roomId;
                initWindow(title, url, 1600, 650);
            }

        }

    </script>
</head>
<body>
<!-- 列表 -->
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
            直播审核
            <span style="margin-left: 10px;">当前房间数：
                <span style="color: red;" id="roomCount">
                    <c:choose>
                        <c:when test="${roomCount eq null}">
                            0
                        </c:when>
                        <c:otherwise>
                            ${roomCount}
                        </c:otherwise>
                    </c:choose>
                </span>
            </span>
            <span style="margin-left: 10px;">
                当前账号：
                <span style="font-size: 12px;font-weight: normal;">
                    ${checkGroup}
                </span>
            </span>
        </h2>
    </div>
    <div style="padding: 2px 5px;">
            <span>
                <a href="check/forwardCheckPage?type=hot" <c:if test="${type eq 'hot'}">style="color: red;"</c:if>>最热</a>
                <a href="check/forwardCheckPage?type=new" <c:if test="${type eq 'new'}">style="color: red;"</c:if>>最新</a>
            </span>
    </div>
</div>

<div class="basic" style="margin-top: 10px;">
    <input type="hidden" value="${roomOrder}" id="roomData">
    <input type="hidden" value="" id="removeRoomData">
    <c:forEach items="${list}" var="vo" varStatus="status">
        <div class="room" id="room_${vo.id}" style="border: 1px solid #ccc;margin: 1px;height: 290px;">
            <a href="check/checkPageDetail?roomId=${vo.id}" target="_blank">
            <span class="image" <%--onclick="getHistoryScreenshot(${vo.id},'${vo.streamSource}')"--%> >
                <c:choose>
                    <c:when test="${vo.streamSource eq 'unknown' || vo.streamSource eq 'plls'}">
                        <img src="images/loading.gif" id="screenshot_${vo.id}" data="${vo.id}" style="width: 180px;height: 180px;margin: 1px;" class="roomImage">
                    </c:when>
                    <c:otherwise>
                        <img src="${vo.screenshotUrl}" id="screenshot_${vo.id}" data="${vo.id}" style="width: 180px;height: 180px;margin: 1px;" class="roomImage">
                    </c:otherwise>
                </c:choose>
            </span>
            </a>
            <span>
                <table width="100%">
                    <tr>
                        <td style="text-align: right;">
                            <label>主播ID:</label>
                        </td>
                        <td style="text-align: left;">
                            ${vo.creatorId}
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">
                            <label>昵称:</label>
                        </td>
                        <td style="text-align: left;" title="${vo.anchorName}">
                            <c:choose>
                                <c:when test="${fn:length(vo.anchorName) > 10}">
                            <c:out value="${fn:substring(vo.anchorName, 0, 10)}"/>
                                </c:when>
                                <c:otherwise>
                            <c:out value="${vo.anchorName}"/>
                                </c:otherwise>
                            </c:choose>

                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">
                            <label>观看人数:</label>
                        </td>
                        <td style="text-align: left;" id="online_number_${vo.id}">
                            ${vo.onlineNumber}
                        </td>
                    </tr>
                </table>
            </span>
            <span style="margin-left: 40px;">
                <c:choose>
                    <c:when test="${vo.shieldStatus eq 1}">
                        已屏蔽
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:;" onclick="shieldLiveDlg(${vo.id})" class="easyui-linkbutton" iconCls="icon-cancel">
                            屏蔽
                        </a>
                    </c:otherwise>
                </c:choose>
                <a href="javascript:;" onclick="settingLiveOfflineDlg(${vo.id})" class="easyui-linkbutton" iconCls="icon-remove">
                    下线
                </a>
            </span>
        </div>
    </c:forEach>
</div>
<c:import url="/main/common.jsp"/>

<!-- 弹出的下线对话框 -->
<div id="dlg2" class="easyui-dialog" style="width:390px;height:450px;padding:10px 20px" closed="true"
     buttons="#dlg2_buttons">
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
        <form action="" id="searchForm2">
            <input type="hidden" id="roomId" name="roomId">
            <div>
                禁播时间(分)：<input class="easyui-validatebox easyui-numberbox" id="expireMin" name="expireMin" data-options="validType:'int',min:0" required="true" value="" >
                <br/>
                禁播原因：<br/>
                <input type="radio" name="reason" onclick="$('#message').val('色情、裸露尺度过大')"> 色情、裸露尺度过大<br/>
                <input type="radio" name="reason" onclick="$('#message').val('内容低俗')"> 内容低俗<br/>
                <input type="radio" name="reason"  onclick="$('#message').val('宣传其他平台')"> 宣传其他平台<br/>
                <input type="radio" name="reason" onclick="$('#message').val('枪支、毒品、赌场')"> 枪支、毒品、赌场<br/>
                <input type="radio" name="reason" onclick="$('#message').val('直播吸烟')"> 直播吸烟<br/>
                <input type="radio" name="reason"  onclick="$('#message').val('暴力、血腥、恐怖内容')"> 暴力、血腥、恐怖内容<br/>
                <input type="radio" name="reason" onclick="$('#message').val('麻将、扑克类游戏')"> 麻将、扑克类游戏<br/>
                <input type="radio" name="reason" onclick="$('#message').val('拍摄、转播其他直播平台')"> 拍摄、转播其他直播平台<br/>
                <input type="radio" name="reason"  onclick="$('#message').val('服装涉及国会、党徽、军徽、警徽')"> 服装涉及国会、党徽、军徽、警徽<br/>
                <input type="radio" name="reason" onclick="$('#message').val('未成年直播')"> 未成年直播<br/>
                <input type="radio" name="reason" onclick="$('#message').val('危险驾驶')"> 危险驾驶<br/>
                <input type="radio" name="reason"  onclick="$('#message').val('涉及政治、宗教内容')"> 涉及政治、宗教内容<br/>
                <input class="easyui-validatebox" data-options="lenght:20" id="message" name="message" required="true">
            </div>
        </form>
    </div>

</div>
<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg2_buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="offline();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg2').dialog('close')">取消</a>
</div>

<!-- 弹出的下线对话框 -->
<div id="dlg3" class="easyui-dialog" style="width:430px;height:250px;padding:10px 20px" closed="true"
     buttons="#dlg3_buttons">
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb3" style="padding:5px;height:auto">
        <form action="" id="searchForm3">
            <input type="hidden" id="shieldRoomId" name="roomId">
            <div>
                屏蔽时间(分)：<input class="easyui-validatebox easyui-numberbox" id="expireTime" name="expireTime" data-options="validType:'int',min:0" required="true" value="" >
                <br/>
                屏蔽原因：<br/>
                <input type="radio" name="reason" onclick="$('#shieldMessage').val('画面转播电影、电视或游戏')"> 画面转播电影、电视或游戏<br/>
                <input type="radio" name="reason" onclick="$('#shieldMessage').val('语言低俗、骂人')"> 语言低俗、骂人<br/>
                <input type="radio" name="reason"  onclick="$('#shieldMessage').val('内容低俗无意义')"> 内容低俗无意义<br/>
                <input type="radio" name="reason"  onclick="$('#shieldMessage').val('挂机、黑屏、定格')"> 挂机、黑屏、定格<br/>
                <input class="easyui-validatebox" data-options="lenght:20" id="shieldMessage" name="shieldMessage" required="true">
            </div>
        </form>
    </div>

</div>
<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg3_buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="shieldLive();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg3').dialog('close')">取消</a>
</div>

</body>
</html>
