<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!--
    <script type="text/javascript" src="js/ajaxfileupload.js"></script>
     -->
    <link rel="stylesheet" href="files/Jcrop/css/jquery.Jcrop.css" type="text/css"/>
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script src="js/fp/jquery.iframe-transport.js"></script>
    <script src="js/fp/jquery.fileupload.js"></script>
    <script src="files/Jcrop/js/jquery.Jcrop.js"></script>
    <title>视频上传</title>
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
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
    <script type="text/javascript">
        var lisVideotUrl = "video/searchVideoListPage?filters['flowStat']=checked";
        var datagridId = "#tt";
        var searchFormId = "#searchForm";
        var pageSize = 20;

        var playId = "";
        var adddialogueId = "#dlg";
        $(function () {
            //启用表单验证
            $('.validatebox-text').bind('blur', function () {
                $(this).validatebox('enableValidation').validatebox('validate');
            });

            $('#hlsfiles').fileupload({
                url: '${uploadfile_remote_url}',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;
                    if (uploadresult["exception"] != "") {
                        alert("上传失败，请重新上传。。。。。");
                        playId = "error";
                        return;
                    }
                    playId = uploadresult["httpDataFileuploadDataFiles"][0]["playId"];
                    playVideo('', playId + '.mp4');
                    showMessage("通知", "恭喜您上传成功");
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
                    if ((fileext != '.mp4')) {
                        showMessage("Error", "对不起，系统仅支持mp4格式的视频文件，请不要调皮!O(∩_∩)O谢谢~");
                        return false;
                    }
                }
            });

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
        });


        function clickupload() {
            var id;
            $("#playId").val(playId);
            if ($.trim(playId) == "error") {
                showMessage("Error", "文件上传失败，请重新选择文件上传");
                return false;
            }
            if ($.trim(playId) == "") {
                showMessage("提示", "请等待文件上传完成后，再点击确认");
                return false;
            }
            var pic = $("#videoListPic").val();
            if (pic == null || pic == "") {
                showMessage("提示", "请上传视频封面！");
                return false;
            }
            var tags = $("input[type=checkbox][name=tag]:checked").length;
            if (tags > 6) {
                showMessage("Error", "标签最多只能选六个");
                return false;
            }
            var item = $("#pubs option:selected").val();
            if (item == 2) {
                var pubTime = $('#pubTime').textbox('getValue');
                if (pubTime == null || pubTime == "") {
                    showMessage("提示", "请您选择计划发布的时间");
                    return false;
                } else {
                    var sDate = new Date(pubTime.replace("//-/g", "//"));
                    var eDate = new Date();
                    if (sDate < eDate) {
                        showMessage("Error", "计划发布开始时间不能小于当前时间");
                        return false;
                    }
                }
            }

            var rate = $('#playRateToday').numberbox('getValue');
            if (rate == null || rate == "") {
                showMessage("提示", "请您填写播放率，格式：0.0000");
                return false;
            }
            var isShow = document.getElementById("isShow");
            if (isShow.checked) {
                var st = $("#showTitle").val();
                if (st == null || st == "") {
                    showMessage("提示", "请填写视频秀标题！");
                    return false;
                }
                var sd = $("#showDesc").val();
                if (sd == null || sd == "") {
                    showMessage("提示", "请填写视频秀描述！");
                    return false;
                }
                var rv = $("#refVideos").val();
                if (rv == null || rv == "") {
                    showMessage("提示", "请选择视频秀关联视频！");
                    return false;
                }
                var sp = $("#showPic").val();
                if (sp == null || sp == "") {
                    showMessage("提示", "请选择视频秀封面图片！");
                    return false;
                }
            }

            var formjson = getFormJson('#myform');
            $.ajax({
                url: 'video/uploadsave?pub=' + item,
                data: formjson,
                type: "post",
                dataType: "json",
                beforeSend: function () {
                    return $('#myform').form('enableValidation').form('validate');
                },
                success: function (result) {
                    if ('${show}' == '1')
                        location.href = "showvideo/listShows";
                    else
                        location.href = "video/checledVideos";
                }
            });
        }

        function playVideo(playerId, fileName) {
            $('#playerContainer' + playerId).empty();
            var _player = null;
            if (fileName.indexOf('.') < 0) {
                fileName = fileName + '.m3u8';
            }
            var player = $('<div/>');
            $(player).attr('id', 'pl' + playerId);

            $('#playerContainer' + playerId).append(player);
            var conf = {
                file: '${video_play_url_prefix}' + fileName,
                image: '${img}',
                height: 350,
                width: 400,
                autostart: true,
                analytics: {enabled: false}
            };
            _player = jwplayer('pl' + playerId).setup(conf);
        }
        //上传视频封面
        function uploadpic() {
            $(adddialogueId).dialog('open').dialog('setTitle', "上传视频封面");
            $('#videoImg').fileupload({
                url: 'video/uploadVideoImg',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;
                    $("#videoPic").val(uploadresult["result"]);//原图
                    var picPath = "<%=request.getContextPath()%>/download";
                    $("#picCut").attr("src", picPath + uploadresult["result"]);
                    $(".jcrop-holder img").attr("src", picPath + uploadresult["result"]);
                    initJcrop();
                    var viewImg = $("#viewImg");
                    viewImg.attr("src", "<%=request.getContextPath()%>/download" + uploadresult["result"]);
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
        }
        //*********************START*****************************
        var jcrop_api;
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
        //裁剪
        function cutImage() {
            var x1 = $('#x1').val() * 2;
            var y1 = $('#y1').val() * 2;
            var w = $('#w').val() * 2;
            var h = $('#h').val() * 2;
            var imgPath = $("#videoPic").val();
            $.ajax({
                url: 'video/uploadVideoImgJcrop',
                data: {imgPath: imgPath, x1: x1, y1: y1, w: w, h: h},
                type: "post",
                dataType: "json",
                success: function (data) {
                    $("#videoListPic").val(data);
                    $(adddialogueId).dialog('close');
                },
                error: function () {
                    showMessage("通知", "视频封面上传失败");
                }
            });
        }
        //*********************END*****************************
        function start() {
            var value = $('#p').progressbar('getValue');
            if (value < 100) {
                value += Math.floor(Math.random() * 10);
                $('#p').progressbar('setValue', value);
                setTimeout(arguments.callee, 200);
            }
        }
        ;

        function pubClick() {
            var num = $("#pubs option:selected").val();
            if (num == 1) {//立即发布
                $("#pubDiv").attr("style", "display:none");
            } else {//计划发布
                $("#pubDiv").attr("style", "display:block");
                $("#pubTime").datetimebox({
                    showSeconds: false
                });
            }
        }

        var tempradio = null;
        function changeRadio(checkedRadio) {
            if (tempradio == checkedRadio) {
                tempradio.checked = false;
                tempradio = null;
            } else {
                tempradio = checkedRadio;
            }
        }
        function picShow() {
            var isShow = document.getElementById("isShow");
            if (isShow.checked) {
                $("#showProp").attr("style", "display:block");
            } else {
                $("#showProp").attr("style", "display:none");
                $("#showTitle").val("");
                $("#showDesc").val("");
                $("#refVideos").val("");
                $("#showPic").val("");
            }
        }

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
        }

        function doSearch() {
            $(datagridId).datagrid('reload', getFormJson(searchFormId));
        }

        function selectVideo() {
            var row = $(datagridId).datagrid('getChecked');
            var ids = [];
            for (var r in row) {
                ids.push(row[r]['id']);
            }
            $("#refVideos").val(ids.join(","));
            $("#video_dlg").dialog("close");
        }

        function videoDlg() {
            $("#video_dlg").show();
            $("#video_dlg").dialog({
                title: '选择视频',
                width: 800,
                height: 650,
                closed: false,
                cache: true,
                modal: true
            });
        }

        function selectByUserType(value) {
            if (value == 1) {
                $("#uploader").combogrid({
                    panelWidth:450,
                    idField:'id',
                    textField:'name',
                    url:'combogrid/getUserCombogridByType?type=1',
                    columns:[[
                        {field:'id',title:'用户ID',width:60},
                        {field:'name',title:'昵称',width:120},
                        {field:'username',title:'用户名',width:120},
                        {field:'phone',title:'手机号码',width:140}
                    ]]
                });
            } else {
                $("#uploader").combogrid({
                    panelWidth:450,
                    idField:'id',
                    textField:'name',
                    url:'combogrid/getUserCombogridByType?type=2',
                    columns:[[
                        {field:'id',title:'用户ID',width:60},
                        {field:'name',title:'昵称',width:120},
                        {field:'username',title:'用户名',width:120},
                        {field:'phone',title:'手机号码',width:140}
                    ]]
                });
            }
        }

        function getUserList(){
            $('#displayUserTable').datagrid({
                nowrap: true, //是否换行
                autoRowHeight: true, //自动行高
                fitColumns: true,
                fit: true,
                striped: true,
                pageNumber: 1,
                collapsible: true, //是否可折叠
                remoteSort: true,
                singleSelect: true, //是否单选
                pagination: true, //分页控件
                rownumbers: true, //行号
                pagePosition: 'bottom',
                scrollbarSize: 0,
                loadMsg: "数据加载中.....",
                queryParams: {
                    type: $("#select_user").combobox("getValue")
                },
                url: "combogrid/getCombogridUserList",
                columns: [
                    [
                        {field: 'id', title: '<span class="columnTitle">用户ID</span>', width: 120, align: 'center'},
                        {field: 'name', title: '<span class="columnTitle">用户昵称</span>', width: 120, align: 'center'},
                        {field: 'username', title: '<span class="columnTitle">用户名</span>', width: 120, align: 'center'},
                        {field: 'phone', title: '<span class="columnTitle">手机号码</span>', width: 120, align: 'center'}
                    ]
                ],
                onLoadSuccess: function () {
                    $('#displayUserTable').datagrid('clearSelections');
                },
                toolbar: "#dataGridToolbarUser",
                pageSize: 20,
                pageList: [20, 40, 60, 80, 100],
                beforePageText: '第', //页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
        }

        function selectUsers() {
            getUserList();
            $("#select_user_dlg").dialog('open').dialog('setTitle', "选择用户");
        }

        function doSelectUser() {
            var selectObj = $('#displayUserTable').datagrid("getSelected");
            var idStr = selectObj["id"];
            $("#uploader").val(idStr);
            $('#select_user_dlg').dialog('close');
        }

        function doSearchUsers() {
            var queryParams = $('#displayUserTable').datagrid('options').queryParams;
            queryParams.user = $('#queryUser').combobox("getValue");
            queryParams.userKeyword = $('#queryUserKeyword').val();
            $('#displayUserTable').datagrid({url: "combogrid/getCombogridUserList"});
        }

    </script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
        <form id="myform" method="post" enctype="multipart/form-data">
            <input type="hidden" name="playId" id="playId"/>
            <table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="form-body">
                <tr>
                    <td style="width: 20%;">选择视频:</td>
                    <td style="width: 80%;text-align: left;">
                        <input type="file" name="hlsfiles" id="hlsfiles" style="width:50%"/>

                        <div id="playerContainer"></div>
                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
                             class="easyui-progressbar"></div>
                    </td>
                </tr>
                <!-- 				<tr> -->
                <!-- 					<td>输入标题:</td> -->
                <!-- 					<td><input class="easyui-validatebox validatebox-text validatebox-invalid" name="title" id="title" style="width:50%" data-options="validType:'length[0,20]'" required="true"></input></td> -->
                <!-- 				</tr> -->
                <tr>
                    <td>视频描述:</td>
                    <td style="text-align: left;">
                        <input class="easyui-validatebox easyui-textbox" name="description" id="description"
                               data-options="multiline:true,validType:'length[0,100]'" required="true" value=""
                               style="width:50%;height:100px">
                    </td>
                </tr>
                <tr>
                    <td>
                        视频封面:
                    </td>
                    <td style="text-align: left;">
                        <input name="videoPic" id="videoPic" type="hidden" style="width:50%"
                               readonly="readonly"/>
                        <input name="videoListPic" id="videoListPic" type="text" style="width:50%"
                               readonly="readonly"/>

                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
                             class="easyui-progressbar"></div>
                        <a href="javascript:void(0)" onclick="uploadpic()" class="easyui-linkbutton" iconCls="icon-ok">上传图片</a>
                        <img width="150px" height="150px" alt="" src="" id="viewImg" style="display:none">
                    </td>
                </tr>
                <tr>
                    <td>用户类型:</td>
                    <td style="text-align: left;">
                        <select id="select_user" class="easyui-combobox">
                            <option value="1">马甲用户</option>
                            <option value="2">Blive用户</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>上传人:</td>
                    <td style="text-align: left;">
                        <%--<select class="easyui-combobox" name="uploader" id="uploader" style="width:50%">
                            <c:forEach var="item" items="${uploaduserlist}">
                                <option value="${item['id'] }">${item['username'] }</option>
                            </c:forEach>
                        </select>--%>
                        <input type="text" value="" class="easyui-text" onclick="selectUsers()" id="uploader" name="uploader"/>
                    </td>
                </tr>
                <!-- 				<tr> -->
                <!-- 					<td>是否用于活动介绍:</td> -->
                <!-- 					<td> -->
                <!-- 					    <select class="easyui-combobox" name="introductionMark" id="introductionMark"  style="width:50%"> -->
                <!-- 					    	<c:if test="${popFlg != 1}"> -->
                <!-- 					    		<option value="0">否</option> -->
                <!-- 					    	</c:if> -->
                <!-- 						 	<option value="1">是</option> -->
                <!-- 						</select> -->
                <!-- 					</td> -->
                <!-- 				</tr> -->
                <tr>
                    <td>是否贴标:</td>
                    <td style="text-align: left;">
                        <select class="easyui-combobox" name="isLogo" id="isLogo" style="width:50%">
                            <option value="0">否</option>
                            <option value="1">是</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>播放率:</td>
                    <td style="text-align: left;">
                        <input id="playRateToday" name="playRateToday" class="easyui-numberbox" value="0.0000" min="0"
                               max="0.9999" precision="4" maxlength="5" size="5" style="width: 120px;"/>
                    </td>
                </tr>
                <!--
				<tr>
					<td>选择专题:</td>
					<td style="">
					 <c:forEach var="item" items="${subjectList }" varStatus="idxStatus">
						<input type="checkbox" name="subject" value="${item['id'] }"/>${item['name'] }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<c:if test="${ (idxStatus.index+1) % 7 == 0}">
							<br/>
						</c:if>
					 </c:forEach>
					</td>
				</tr>
				-->
                <!-- 				<tr> -->
                <!-- 					<td>选择片头:</td> -->
                <!-- 					<td style=""> -->
                <!-- 						 <c:forEach var="item" items="${templateList }" varStatus="idxStatus"> -->
                <!-- 							<img src="/restadmin/download${item['pic']}" width="60px" height="50px" title="${item['title']}"/> -->
                <!-- 							<input type="radio" name="templateId" onclick="changeRadio(this);" value="${item['id'] }" title="${item['title']}"/>&nbsp;&nbsp; -->
                <!-- 						 </c:forEach> -->
                <!-- 					</td> -->
                <!-- 				</tr> -->
                <tr id="actives">
                    <td>选择活动:</td>
                    <td style="text-align: left;">
                        <c:forEach var="item" items="${activityList }" varStatus="idxStatus">
                            <input type="checkbox" name="activity" value="${item['id'] }"/>${item['title']}&nbsp;&nbsp;
                            <c:if test="${ (idxStatus.index+1) % 7 == 0}">
                                <br/>
                            </c:if>
                        </c:forEach>
                    </td>
                </tr>
                <%-- <tr>
                    <td>选择标签:</td>
                    <td>
                         <c:forEach var="item" items="${tagList }" varStatus="idxStatus">
                            <input type="checkbox" name="tag" value="${item['name'] }"/>${item['name'] }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <c:if test="${ (idxStatus.index+1) % 7 == 0}">
                                <br/>
                            </c:if>
                     </c:forEach>
                    </td>
                </tr> --%>
                <tr>
                    <td>发布类型:</td>
                    <td style="text-align: left;;">
                        <select id="pubs" onchange="pubClick();">
                            <option value="1">立即发布</option>
                            <option value="2">计划发布</option>
                        </select>

                        <div id=pubDiv>
                            <input id="pubTime" name="pubTime" style="display: none;"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>是否Blive秀:</td>
                    <td style="text-align: left;">
                        加入Blive秀 <input id="isShow" type="checkbox" onclick="picShow();"
                                     <c:if test="${show==1}">checked="checked" disabled="disabled"</c:if>/>

                        <div id="showProp" style="<c:if test="${show!=1}">display: none;</c:if>">
                            <div style="padding-bottom: 5px;padding-top:5px">
                                标题：<input type="text" id="showTitle" name="showTitle" size="50"/>
                            </div>
                            <div style="padding-bottom: 5px">
                                描述：<input type="text" id="showDesc" name="showDesc" size="50"/>
                            </div>
                            <div style="padding-bottom: 5px">
                                关联视频：<input type="text" id="refVideos" name="refVideos" readonly="readonly"
                                            onclick="videoDlg()" size="50"/>
                            </div>
                            <div>
                                宣传图：<input type="file" id="showImg" name="showImg"/><img width="150px" height="150px"
                                                                                         alt="" src="" id="viewShowImg"
                                                                                         style="display:none">
                                <input type="hidden" id="showPic" name="showPic">
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <a href="javascript:void(0)" id="upload_submit" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
                           onclick="clickupload()">确认上传</a>
                        <a href="video/checledVideos" style="" class="easyui-linkbutton"
                           data-options="iconCls:'icon-cancel'">返回</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<!-- 弹出的上传图片对话框 -->
<div id="dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true"
     buttons="#dlg-buttons">
    <div class="ftitle">上传图片</div>
    <!-- 添加 -->
    <form id="fm" method="post" novalidate>
        <div class="fitem">
            <label>图片地址:</label>
            <input name="videoImg" id="videoImg" type="file" style="width:50%"></input>

            <div style="margin-left:0px;margin-right:auto;width:90%;display:none" id="q"
                 class="easyui-progressbar"></div>
        </div>
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
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="cutImage();">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 弹出的选择视频对话框 -->
<div id="video_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true"
     buttons="#vdlg-buttons">
    <table id="tt" data-options="border:false,toolbar:'#tb'">
    </table>
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb" style="padding:5px;height:auto">
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
</div>

<!--选择用户弹出框开始-->
<div id="select_user_dlg" class="easyui-dialog" style="width:850px;height:700px;" closed="true"
     buttons="#select-user-dlg-buttons">
    <div id="dataGridToolbarUser">
        <table>
            <tr>
                <td>
                    <select class="easyui-combobox" name="queryUser" id="queryUser">
                        <option value="">用户搜索</option>
                        <option value="1">用户ID</option>
                        <option value="2">用户名</option>
                        <option value="3">昵称</option>
                        <option value="4">手机号码</option>
                    </select>
                </td>
                <td>
                    <input type="text" name="queryUserKeyword" id="queryUserKeyword" class="easyui-validatebox">
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearchUsers()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
    <table class="table-doc" id="displayUserTable" cellspacing="0" width="100%"></table>
</div>
<div id="select-user-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doSelectUser()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#select_user_dlg').dialog('close')">取消</a>
</div>
<!--选择用户弹出框结束-->

</body>
</html>