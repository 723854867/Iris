<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>用户详情</title>
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

    <style type="text/css">
        table.gridtable {
            font-family: verdana, arial, sans-serif;
            color: #333333;
            border-collapse: collapse;
        }

        table.gridtable td {
            padding-left: 5px;
            background-color: #ffffff;
            height: 30px;
        }

        .fitem label {
            display: inline-block;
            width: 80px;
        }

        .dis {
            display: block;
        }

   /*     .undis {
            display: none;
        }*/

        ol, ul {
            list-style: none
        }

        .nav_current {
            float: left;
            background: #FFF;
            color: gray;
        }

        .nav_link {
            float: left;
        }

        .nav_current:hover, .nav_link:hover {
            color: #FF6600;
        }

        #dww-menu {
            width: 978px;
            overflow: hidden;
        }

        #dww-menu .mod-hd {
            height: 60px;
            line-height: 30px;
            position: relative;
        }

        #dww-menu .mod-hd li {
            float: left;
            cursor: pointer;
            text-align: center;
            height: 35px;
            line-height: 35px;
            text-transform: uppercase;
            padding-right: 10px
        }

        #dww-menu .mod-bd {
            padding: 5px 10px
        }

        #dww-menu .mod-bd div {
            color: #BFBFBF;
            line-height: 24px
        }

        #dww-menu .mod-bd div.show {
            display: block
        }

        #dww-menu .mod-bd div a {
            display: inline-block;
            padding: 0 4px
        }

        .loadNext {
            cursor: pointer;
            visibility: hidden;
            width: 100%;
            text-align: center;
            line-height: 35px;
            margin-bottom: 10px;
            height: 35px;
            background-color: #CCCCCC;
            -moz-border-radius: 10px;
            -webkit-border-radius: 10px;
        }
    </style>
    <script type="text/javascript">
        var deleteConfirmMessage = "你确定要删除吗?";
        var adddialogueId = "#dlg";
        var addTitle = "播放视频";

        var x, y;
        $(function () {
            $(document).mousemove(function (e) {
                e = e || window.event;
                x = e.pageX || (e.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft));
                y = e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));
            });
        });

        function changeNav(o) {
            //当前被中的对象设置css
            o.className = "nav_link";
            var j;
            var id;
            var e;
            //遍历所有的页签，没有被选中的设置其没有被选中的css
            for (var i = 1; i <= 2; i++) { //i<7 多少个栏目就填多大值
                id = "nav" + i;
                j = document.getElementById(id);
                e = document.getElementById("sub" + i);
                if (id != o.id) {
                    j.className = "nav_current";
                    e.style.display = "none";
                } else {
                    e.style.display = "block";
                }
            }
        }

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

        function delVideo(id) {
            if (id) {
                $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
                    if (r) {
                        $.post('video/logicremove', {ids: id}, function (result) {
                            if (result["success"] == true) {
                                window.location.reload();
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

        function chgStat(id, stat) {
            $.post('ruser/chgstat', {uid: id, stat: stat}, function (result) {
                if (result["success"] == true) {
                    window.location.reload();
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function closeAccount(id, stat) {
            var reason = $("#message").val();
            $.post('ruser/chgstat', {uid: id, stat: stat,reason:reason}, function (result) {
                if (result["success"] == true) {
                    window.location.reload();
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function chgVipStat(id, stat) {
            $.post('ruser/chgvipstat', {uid: id, stat: stat}, function (result) {
                if (result["success"] == true) {
                    window.location.reload();
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function chgVipWeight(id, vipWeight) {
            var vipWeight = $("#vipWeight").val();
            $.post('ruser/chgVipWeight', {uid: id, vipWeight: vipWeight}, function (result) {
                if (result["success"] == true) {
                    window.location.reload();
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        var uid = "${ruser.id}";
        var pageSize = 24;
        var page = 1;
        function getVideos(pageNo) {
            $.post('video/getuservideo', {uid: uid, page: pageNo, size: pageSize}, function (result) {
                if (result) {
                    var videos = result.content;
                    if (videos.length > 0) {
                        var ul = $("#videoUl");
                        for (var i = 0; i < videos.length; i++) {
                            var video = videos[i];
                            var li = $("<li></li>");
                            var flowStat = '';
                            var isLogo = 0;
                            var logos = '';
                            if (video.flowStat == 'uncheck') {
                                flowStat = '未审核';
                            } else if (video.flowStat == 'check_ok') {
                                flowStat = '审核通过';
                            } else if (video.flowStat == 'check_fail') {
                                flowStat = '审核未通过';
                            } else if (video.flowStat == 'delete') {
                                flowStat = '已删除';
                            } else if (video.flowStat == 'published') {
                                flowStat = '已发布';
                            }

                            if (video.isLogo == 1) {
                                isLogo = 0;
                                logos = '取消贴标';
                            } else {
                                isLogo = 1;
                                logos = '贴标';
                            }

                            var vname = '';
                            if (video.name) {
                                vname = video.name.length < 10 ? video.name : video.name.substr(0, 10) + "...";
                            }

                            var inner = '';
                            inner += '<div class="pic" id="playerContainer' + video.id + '" onclick="playVideo(\'' + video.id + '\',\'' + video.playKey + '\')">'
                                    + '<img style="height:150px;width:200px;padding-top:10px;padding-left:10px;" src="/restadmin/download' + video.videoPic + '" />'
                                    + '</div>'
                                    + '<dl>'
                                    + '<dt>描述</dt>'
                                    + '<dd title="' + video.description + '">' + video.description.substring(0, 9) + '</dd>'
                                    + '<dt>上传时间</dt>'
                                    + '<dd>' + video.createDateStr + '</dd>'
                                    + '<dt>时长</dt>'
                                    + '<dd>' + video.duration + '</dd>'
                                    + '<dt>审核状态</dt>'
                                    + '<dd>' + flowStat + '</dd>'
                                    + '</dl>'
                                    + '<div class="clearYlz"></div>'
                                    + '<div class="userLBtn">'
                                        /*           + '<div style="padding-top:5px">'
                                         + '<button onclick="mopen(' + video.id + ')" class="button yellow bigrounded" style="width: 70px;height: 25px;">审核</button>'
                                         + '<button onclick="changeLogoState(' + video.id + ',' + isLogo + ');" class="button orange bigrounded" style="width: 70px;height: 25px;">' + logos + '</button>'
                                         + '</div>' */
                                    + '<div style="padding-top:5px">'
                                    + '<a href="' + baseUrl + 'video/multidown?ids=' + video.id + '"><button class="button blue bigrounded" style="width: 70px;height: 25px;">下载</button></a>'
                                        /*+ '<button onclick="delVideo(' + video.id + ')" class="button red bigrounded" style="width: 70px;height: 25px;">删除</button>'*/
                                    + '</div></div>';

                            li.html(inner);

                            ul.append(li);
                        }
                        var load = $("#loadNex");
                        if (result.last) {
                            load.css('visibility', 'hidden');
                        } else {
                            load.css('visibility', 'visible');
                        }
                    }
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

        function loadNext() {
            page = page + 1;
            getVideos(page);
        }

        $(function () {
            getVideos(page);
        });

        function chgStatDialog(){
            $("#dlg2").dialog('open').dialog('setTitle', "封号");
        }
    </script>
</head>
<body>
<div style="padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">用户详情</h2>
        <button class="button green bigrounded" type="button" style="width: 60px;height: 25px;float: right;"
                onclick="javascript:history.back(-1)">返回
        </button>
    </div>
    <table style="border-style:none;font-size: 14px;" border="0" class="gridtable" width="70%"
           cellpadding="0" cellspacing="0">
        <tr>
            <td>&nbsp;</td>
            <td style="font-weight: bold;text-align: right" align="right">
                用户ID：
            </td>
            <td style="text-align: left;">
                ${ruser.id}
                <input type="hidden" id="userId" value="${ruser.id}">
            </td>
            <td style="font-weight: bold;text-align: right" align="right">等级：</td>
            <td style="text-align: left;">
                <c:choose>
                    <c:when test="${ruser.vipStat == 0}">普通</c:when>
                    <c:when test="${ruser.vipStat == 1}">蓝V</c:when>
                    <c:when test="${ruser.vipStat == 2}">黄V</c:when>
                    <c:when test="${ruser.vipStat == 3}">绿V</c:when>
                </c:choose>
                <c:if test="${ruser.vipStat != 0}">
                    <button class="button green bigrounded" type="button" style="width: 60px;height: 25px;"
                            onclick="javascript:chgVipStat(${ruser.id},0)">普通
                    </button>
                </c:if>
                <c:if test="${ruser.vipStat != 1}">
                    <button class="button blue bigrounded" type="button" style="width: 60px;height: 25px;"
                            onclick="javascript:chgVipStat(${ruser.id},1)">蓝V
                    </button>
                </c:if>
                <c:if test="${ruser.vipStat != 2}">
                    <button class="button yellow bigrounded" type="button" style="width: 60px;height: 25px;"
                            onclick="javascript:chgVipStat(${ruser.id},2)">黄V
                    </button>
                </c:if>
                <c:if test="${ruser.vipStat != 3}">
                    <button class="button green bigrounded" type="button" style="width: 60px;height: 25px;"
                            onclick="javascript:chgVipStat(${ruser.id},3)">绿V
                    </button>
                </c:if>
                <c:if test="${ruser.vipStat != 0}">
                    &nbsp;&nbsp;VIP权重:<input type="text" id="vipWeight" name="vipWeight" value="${ruser.vipWeight }" size="8">
                    <button class="button green bigrounded" type="button" style="width: 60px;height: 25px;"
                            onclick="javascript:chgVipWeight(${ruser.id})">修改
                    </button>
                </c:if>
            </td>
        </tr>
        <tr>
            <td rowspan="4" style="width: 8%;text-align: center;">
                <div style="border: 1px solid #CCCCCC;width: 150px;height: 150px;">
                    <img alt="头像" style="width: 150px;height: 150px;" src="/restadmin/download${ruser.pic}">
                </div>
            </td>
            <td style="font-weight: bold;text-align: right" align="right">用户类型：</td>
            <td style="text-align: left;">
                <c:choose>
                    <c:when test="${ruser.type == 'majia'}">马甲</c:when>
                    <c:when test="${ruser.type == 'normal'}">Blive</c:when>
                </c:choose>
            </td>
            <td style="font-weight: bold;text-align: right" align="right">状态：</td>
            <td style="text-align: left;">
                <c:choose>
                    <c:when test="${ruser.stat == 0}">激活</c:when>
                    <c:when test="${ruser.stat == 1}">禁言</c:when>
                    <c:when test="${ruser.stat == 2}">封号</c:when>
                </c:choose>&nbsp;&nbsp;
                <button class="button orange bigrounded" type="button" style="width: 60px;height: 25px;"
                        onclick="javascript:chgStat(${ruser.id},1)">禁言
                </button>
                <button class="button red bigrounded" type="button" style="width: 60px;height: 25px;"
                        onclick="javascript:chgStatDialog()">封号
                </button>
                <button class="button green bigrounded" type="button" style="width: 60px;height: 25px;"
                        onclick="javascript:chgStat(${ruser.id},0)">解封
                </button>
            </td>
        </tr>

        <tr>
            <td style="font-weight: bold;text-align: right" align="right">用户昵称：</td>
            <td style="text-align: left;">${ruser.name}</td>
            <td style="font-weight: bold;text-align: right" align="right">
                是否主播：
            </td>
            <td style="text-align: left;">
                <c:choose>
                    <c:when test="${ruser.isAnchor == 1}">是</c:when>
                    <c:otherwise>否</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td style="font-weight: bold;text-align: right" align="right">注册号码：</td>
            <td style="text-align: left;">${ruser.phone}</td>
        </tr>
        <tr>
            <td style="font-weight: bold;text-align: right" align="right">性别：</td>
            <td style="text-align: left;">
                <c:choose>
                    <c:when test="${ruser.sex == 0}">女</c:when>
                    <c:when test="${ruser.sex == 1}">男</c:when>
                    <c:when test="${ruser.sex == 2}">未知</c:when>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td style="font-weight: bold;text-align: right" align="right">注册时间：</td>
            <td style="width: 20%;text-align: left;"><fmt:formatDate value="${ruser.createDateStr}"
                                                                     pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td style="font-weight: bold;text-align: right" align="right">所在地：</td>
            <td style="text-align: left;">${ruser.addr}</td>
        </tr>
    </table>
    <div id="dww-menu" class="mod-tab">
        <div class="mod-hd">
            <ul class="tab-nav">
                <li class="nav_link" id="nav1" onclick="changeNav(this)"><h3>我的视频</h3></li>
                <li class="nav_current" id="nav2" onclick="changeNav(this)"><h3>我的评论</h3></li>
            </ul>
        </div>
    </div>
    <div class="mod-bd">
        <div class="dis" id="sub1">
            <div class="userListVedioBox" style="text-align:center;" id="videos">
                <ul id="videoUl">

                </ul>
                <div id="loadNex" class="loadNext" onclick="loadNext()">
                    加载更多
                </div>
            </div>
        </div>
        <div class="undis" id="sub2">
            <table class="easyui-datagrid" style="width: 830px;display: none;" data-options="
                                rownumbers:true,
                                singleSelect:true,
                                autoRowHeight:false,
                                pagination:true,
                                pageSize:20,
                                url:'video/queryUserEvaluationList/?userId=${ruser.id}',
                                method:'get',
                                toolbar:'#tb'">
                <thead>
                    <tr>
                        <th data-options="field:'content',align:'center',width:400">评论内容</th>
                        <th data-options="field:'createDateStr',align:'center',width:400">评论时间</th>
                    </tr>
                </thead>
            </table>
            <div class="userListVedioBox" style="font-size: 12px;" id="tb">
                <%--<c:forEach items="${evaluations}" var="eva">
                    <div style="padding-left:20px;padding-top:6px; ">
                        <span>时间：</span><fmt:formatDate value="${eva.createDate}" pattern="yyyy-MM-dd HH:mm:ss"
                                                        type="date"/>
                    </div>
                    <div style="padding-left:20px;padding-top:6px;padding-bottom:6px;border-bottom:1px solid #CDCDCD; ">
                        <span>评论内容：</span>${eva.content}</div>
                    &lt;%&ndash;<button class="edit-Btn mLRB" onclick="deleteEvaluation(${eva.id})">删除</button>&ndash;%&gt;
                </c:forEach>--%>
            </div>
        </div>
    </div>
</div>
<!-- 弹出的添加或者编辑对话框 -->
<div id="dlg" class="easyui-dialog" style="width:415px;height:390px;" closed="true">
    <div id="playerContainer">

    </div>
</div>

<div id="dlg2" class="easyui-dialog" style="width:420px;height:420px;padding:10px 20px" closed="true"
     buttons="#dlg2_buttons">
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
        <form action="" id="searchForm2">
            <input type="hidden" id="roomId" name="roomId">
            <div>
                封号原因：<br/>
                <input type="radio" name="reason" onclick="$('#message').val('宣传其它平台')">宣传其它平台<br/>
                <input type="radio" name="reason" onclick="$('#message').val('冒充官方')">内容低俗冒充官方<br/>
                <input type="radio" name="reason" onclick="$('#message').val('观看直播时发广告')">观看直播时发广告<br/>
                <input type="radio" name="reason" onclick="$('#message').val('恶意刷屏')">恶意刷屏<br/>
                <input type="radio" name="reason" onclick="$('#message').val('扰乱直播环境')">扰乱直播环境<br/>
                <input type="radio" name="reason" onclick="$('#message').val('语言低俗，带有侮辱性质')">语言低俗，带有侮辱性质<br/>
                <input class="easyui-validatebox" data-options="lenght:20" id="message" name="message" required="true">
            </div>
        </form>
    </div>
</div>
<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg2_buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="closeAccount(${ruser.id},2);">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>
</body>
</html>