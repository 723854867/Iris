<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html style="background: white">
<head>
    <title>Blive秀</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script src="js/fp/jquery.iframe-transport.js"></script>
    <script src="js/fp/jquery.fileupload.js"></script>
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>

    <script type="text/javascript">
        var datagridId = "#tt";
        var datagridId2 = "#tt2";
        var adddialogueId = "#dlg";
        var addTitle = "播放视频";
        var editTitle = "修改Blive秀";
        var pageSize = 20;
        var editdialogueId = "#udlg";
        var editFormId = "#fm";
        var searchFormId = "#searchForm";
        var lisVideotUrl = "video/searchVideoListPage?filters['flowStat']=checked";
        var listUrl = "showvideo/searchListPage";

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
                selectOnCheck: false,
                nowrap: false,
                url: listUrl,
                columns: [[
                    {field: 'ck', width: 10, checkbox: true},
                    {
                        field: 'pic', title: '缩略图', width: 150, align:'center',formatter: function (value, row, index) {
                        var content = "<span style='width:120px;height:120px;' id='playerContainer" + row.id + "' onclick='playVideo(\"" + row.id + "\",\"" + row.playKey + "\")'>" +
                                "<img src='<%=request.getContextPath()%>/download" + row.pic + "' style='height:100px;width:100px;'/></span>";
                        return content;
                    }
                    },
                    {field: "title", title: "标题", width: 100, align:'center'},
                    {field: 'description', title: '描述', width: 100, align:'center'},
                    {field: 'refVideoId', title: '关联视频', width: 100, align:'center'},
                    {
                        field: 'id', title: '操作', width: 100, nowrap: false,align:'center', formatter: function (value, row, index) {
                        return '<a class="easyui-linkbutton" href="showvideo/modifyShow?id='+row.id+'" style="width:80px;height: 25px;" type="button" title="编辑" >编辑</a>';
                    }
                    }
                ]],
                onLoadSuccess: function () {
                    $(this).datagrid('clearSelections');
                }
            });
        }

        $(function () {
            $('#showImg').fileupload({
                url: 'video/uploadShowImg',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;
                    var path = uploadresult["result"];
                    $("#showPic").val(path);//原图
                    var viewImg = $("#viewShowImg");
                    viewImg.attr("src", "<%=request.getContextPath()%>/download" + path);
                    viewImg.attr("style", "display:block");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#q').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#q').show();
                    $('#q').progressbar('setValue', 0);
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
            datagridList();
            datagridList2();
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

        function upload() {
            location.href = "video/index?show=1";
        }

/*        function editShowVideo(index) {
            var row = $(datagridId).datagrid('getData').rows[index];
            if (row) {
                $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
                $(editFormId).form('load', row);
                $("#viewShowImg").attr("src", "<%=request.getContextPath()%>/download" + row.pic);
            }
        }*/
/*        function updateShowVideo(editDialog, fm) {
            var data = getFormJson(fm);
            if (data) {
                $.post("showvideo/updateShowVideo", data, function (result) {
                    if (result["success"] == true) {
                        $(datagridId).datagrid('reload'); // reload the user data
                    } else {
                        showMessage("Error", "内部错误！" + result["message"]);
                    }
                });
            }
            $(editdialogueId).dialog('close');
        }*/

/*        function videoDlg() {
            $("#video_dlg").show();
            $("#video_dlg").dialog({
                title: '选择视频',
                width: 800,
                height: 650,
                closed: false,
                cache: true,
                modal: true
            });
        }*/
/*        function selectVideo() {
            var row = $(datagridId2).datagrid('getChecked');
            var ids = [];
            for (var r in row) {
                ids.push(row[r]['id']);
            }
            $("#refVideos").val(ids.join(","));
            $("#video_dlg").dialog("close");
        }*/
/*        function datagridList2() {
            $(datagridId2).datagrid({
                fitColumns: true,
                fit: true,
                rownumbers: true,
                pagination: true,
                pageNumber: 1,
                pageList: [pageSize, pageSize * 2, pageSize * 3],
                pageSize: pageSize,
                pagePosition: 'bottom',
                idField: 'id',
                selectOnCheck: true,
                url: lisVideotUrl,
                columns: [[
                    {field: 'ck', width: 100, checkbox: true},
                    {
                        field: 'img', title: '缩略图', width: 100, height: 100, formatter: function (value, row, index) {
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
        }*/
        function doSearch() {
            $(datagridId2).datagrid('reload', getFormJson(searchFormId));
        }

    </script>
</head>




    <!-- 列表上面的按钮和搜索条件  -->
    <div region="north" border="false"
         style="border-bottom: 1px solid #ddd; padding: 2px 5px;" id="tb">
        <div data-options="region:'north',border:false" style="height: 40px; padding-top: 5px; overflow: hidden;">
            <h2 style="float:left;padding-left:10px;margin: 1px">官方视频</h2>
            <span style="float:left;padding-left:20px;margin: 1px"><a href="javascript:void(0)"
                                                                      class="easyui-linkbutton" iconCls="icon-add"
                                                                      onclick="javascript:upload()" plain="true">上传</a></span>
            <h3 style="float:left;padding-left:20px;margin: 1px">Blive秀视频</h3>
            <span style="float:left;padding-left:20px;margin: 1px"><a href="video/checledVideos"
                                                                      class="easyui-linkbutton">管理员视频</a></span>
        </div>
    </div>
<table id="tt" data-options="border:false,toolbar:'#tb'">
    <!-- 列表 -->
</table>
<!-- 弹出的者编辑对话框 -->
<%--<div id="udlg" class="easyui-dialog" style="padding:10px 20px" closed="true" buttons="#udlg-buttons">
    <div class="ftitle">修改Blive秀</div>
    <!-- 添加 -->
    <form id="fm" method="post" novalidate>
        <input type="hidden" name="id" id="id" value=""/>

        <div style="padding-bottom: 5px;padding-top:5px">
            标题：<input type="text" id="showTitle" name="title" size="50"/>
        </div>
        <div style="padding-bottom: 5px">
            描述：<input type="text" id="showDesc" name="description" size="50"/>
        </div>
        <div style="padding-bottom: 5px">
            关联视频：<input type="text" id="refVideos" name="refVideoId" readonly="readonly" onclick="videoDlg()"
                        size="50"/>
        </div>
        <div>
            宣传图：<input type="file" id="showImg" name="showImg"/><img width="150px" height="150px" alt="" src=""
                                                                     id="viewShowImg">
            <input type="hidden" id="showPic" name="pic">
        </div>
    </form>
</div>
<!-- 添加对话框里的保存和取消按钮 -->
<div id="udlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="updateShowVideo('#udlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#udlg').dialog('close')">取消</a>
</div>
<!-- 弹出的添加或者编辑对话框 -->
<div id="dlg" class="easyui-dialog" style="width:415px;height:390px;" closed="true">
    <div id="playerContainer"></div>
</div>--%>

<!-- 弹出的选择视频对话框 -->
<%--<div id="video_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true"
     buttons="#vdlg-buttons">
    <table id="tt2" data-options="border:false,toolbar:'#tb2'">
    </table>
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
        <form action="" id="searchForm">
            <div>
                所属活动&nbsp;
                <select name="filters['activity']" style="width:100px">
                    <option value="">选择活动</option>
                    <c:forEach items="${activityList}" var="activity">
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
<!-- 添加对话框里的保存和取消按钮 -->
<div id="vdlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectVideo();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#video_dlg').dialog('close')">取消</a>
</div>--%>
</body>
</html>
