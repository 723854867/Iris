<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>移出视频</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <style type="text/css">
        #page a {
            margin-left: 10px;
            height: 30px;
            width: 100px;
        }
    </style>
    <script type="text/javascript">

        jQuery(function () {
            var closeFlg = "${closeFlg}";
            if (closeFlg && closeFlg == 'true') {
                if (window.opener.setValue) {
                    window.opener.flushPage();
                }

                window.close();
            }
        });
        function selectPageSize() {
            $("#size").val($("#selectsize").val());
            $("#videoform").submit();
        }

        function selectPage(p) {
            $("#page").val(p);
            $("#videoform").submit();
        }

        function selectStatus(p) {
            $("#status").val(p);
            $("#keyword").val("");
            $("#videoform").submit();
        }

        function selectAll() {
            var ls = document.getElementsByName("ids");
            for (var i = 0; i < ls.length; i++) {
                if ($("#sall").is(':checked')) {
                    ls[i].checked = true;
                } else {
                    ls[i].checked = false;
                }
            }
        }

        function publishVideos() {
            var ls = document.getElementsByName("ids");
            var f = false;
            var sids = "";
            for (var i = 0; i < ls.length; i++) {
                if ($(ls[i]).is(':checked')) {
                    f = true;
                    if (sids == "")
                        sids += ls[i].value;
                    else
                        sids += "," + ls[i].value;
                }
            }
            if (f == false) {
                alert("请选择要发布的视频");
            } else {
                $.ajax({
                    url: 'video/publish',
                    data: {'videoIds': sids},
                    type: "post",
                    dataType: "json",
                    success: function (result) {
                        alert("发布成功");
                        window.location.reload();
                    }
                });
            }
        }

        function updateOrderNum(obj, activeId) {
            var om = "om" + activeId;
            var orderNum = document.getElementById(om);


            if (orderNum && orderNum.value) {
                $.ajax({
                    url: 'activity/updateOrderNum',
                    data: {'activeId': activeId, 'orderNum': orderNum.value},
                    type: "post",
                    dataType: "json",
                    success: function (result) {
                        if (result == 'ok') {
                            window.location.reload();
                        } else if (result == 'exist') {
                            alert('该展示排序已存在！');
                        }
                        //alert("修改成功!");

                    }
                });

            } else {
                alert("请填写播放次数！");
            }
        }

        function changePlayRateToday(obj, videoId) {
            var pc = "playRateToday" + videoId;
            var playRateToday = document.getElementById(pc);


            if (playRateToday && playRateToday.value) {
                $.ajax({
                    url: 'video/changePlayRateToday',
                    data: {'videoId': videoId, 'playRateToday': playRateToday.value},
                    type: "post",
                    dataType: "json",
                    success: function (result) {
                        //alert("修改成功!");
                        window.location.reload();
                    }
                });

            } else {
                alert("请填写播放率！");
            }
        }

        function updateActiveStatus(obj, activityId, status) {

            $.ajax({
                url: 'activity/updateActiveStatus',
                data: {'activeId': activityId, 'status': status},
                type: "post",
                dataType: "json",
                success: function (result) {
                    //alert("修改成功!");
                    window.location.reload();
                }
            });
        }

        function deleteVideos() {
            var ls = document.getElementsByName("ids");
            var f = false;
            var sids = "";
            for (var i = 0; i < ls.length; i++) {
                if ($(ls[i]).is(':checked')) {
                    f = true;
                    if (sids == "")
                        sids += ls[i].value;
                    else
                        sids += "," + ls[i].value;
                }
            }
            if (f == false) {
                alert("请选择要删除的视频");
            } else {
                $.ajax({
                    url: 'video/deleteVideo',
                    data: {'videoIds': sids},
                    type: "post",
                    dataType: "json",
                    success: function (result) {
                        alert("删除成功");
                        window.location.reload();
                    }
                });
            }
        }

        function activityDetail(obj, activityId) {
            $("#videoform").attr("action", "activity/activityDetail?activityId=" + activityId);
            $("#videoform").attr("method", "POST");
            $("#videoform").submit();
        }

        function doClickSearch(value, name) {
            $("#videoform").attr("method", "POST");
            $("#videoform").submit();
        }

        function getValue(obj, key) {
            if (window.opener.setValue) {
                window.opener.setValue(obj, key);
            }

            window.close();
        }

    </script>
</head>
<body>
<div style="padding-left:20px;width:97%">
    <form action="activity/removeActivity" id="videoform" method="post">
        <input type="hidden" name="size" id="size" value="${pageinfo.size}"/>
        <input type="hidden" name="page" id="page" value="${pageinfo.number}"/>
        <input type="hidden" name="status" id="status" value="${status}"/>
        <input type="hidden" name="videoIds" id="videoIds"/>
        <input type="hidden" name="videoId" id="videoId" value="${videoId}"/>
        <input type="hidden" name="activityId" id="activityId" value="${activityId}"/>
        <table style="width: 100%;">
            <tr>
                <td style="vertical-align: middle;width: 15px;">
                    <input type="radio" value="0" name="outType" id="outType0" checked="checked"/>直接移出<br>
                    <input type="radio" value="1" name="outType" id="outType1"/>移至其他活动
                </td>
            </tr>
            <c:forEach items="${actives}" var="active" varStatus="status">
                <tr>
                    <td>
                        <table style="border:1px solid #CCCCCC;width: 100%;">
                            <tr>
                                <td style="vertical-align: middle;width: 10%;">
                                    <input type="checkbox" value="${active.id}" name="ids" id="ids"/>
                                </td>
                                <c:if test="${false }">
                                    <td style="vertical-align: middle;width: 20%;">
                                        <img style="height:63px;width:100px;padding-top:10px;padding-left:10px;"
                                             src="/restadmin/download${active.cover}"/>
                                    </td>
                                </c:if>
                                <td style="vertical-align: middle;width: 20%;" title="">
                                    <span style="font-size:16px">${active.title}</span>
                                </td>
                                <td colspan="1" title="">
                                    <c:if test="${active.status eq 0}">
                                        <span style="font-size: 15;color: red;">下线</span>
                                    </c:if>
                                    <c:if test="${active.status eq 1}">
                                        <span style="font-size: 15;color: red;">进行中</span>
                                    </c:if>
                                </td>
                                <c:if test="${false }">
                                    <td colspan="1" title="">
                                        <input id="mdOM${active.id}" name="mdOM${active.id}" type="button" value="上线"
                                               onclick="updateActiveStatus(this,'${active.id}',1);"/>
                                    </td>
                                </c:if>

                            </tr>
                        </table>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td style="tvertical-align: middle;width: 15px;text-align: center;">
                    <input type="submit" value="提交" name="" id="" checked=""/>
                    <input type="submit" value="取消" name="" id="" onclick="window.close();"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>