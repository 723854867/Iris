<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!--
    <script type="text/javascript" src="js/ajaxfileupload.js"></script>
     -->
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script src="js/fp/jquery.iframe-transport.js"></script>
    <script src="js/fp/jquery.fileupload.js"></script>
    <title>活动管理</title>
    <script type="text/javascript">
        var playId = "";
        $(function () {
            //启用表单验证
            $('.validatebox-text').bind('blur', function () {
                $(this).validatebox('enableValidation').validatebox('validate');
            });

            $('#hlsfiles').fileupload({
                url: 'activity/activityupload',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#cover").val(uploadresult["result"]);
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
                    if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                        showMessage("Error", "对不起，系统仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
                        return false;
                    }
                }
            });

            $('#shareImgFile').fileupload({
                url: 'activity/activityUploadFile',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;
                    $("#shareImg").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#sp').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#sp').show();
                    $('#sp').progressbar('setValue', 0);
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

            $('#bannerPicFile').fileupload({
                url: 'activity/activityBannerPicFileupload',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    var uploadresult = data.result;

                    $("#bannerPic").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#bannerPicFileDiv').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#bannerPicFileDiv').show();
                    $('#bannerPicFileDiv').progressbar('setValue', 0);
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

            $('#videoCoverPicFile').fileupload({
                url: 'activity/activityVideoCoverPicFileupload',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;

                    $("#videoCoverPic").val(uploadresult["result"]);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#videoCoverPicFileDiv').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#videoCoverPicFileDiv').show();
                    $('#videoCoverPicFileDiv').progressbar('setValue', 0);
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


            var playkey = '';
            $('#playkeyFile').fileupload({
                url: '${uploadfile_remote_url}',
                sequentialUploads: true,
                dataType: 'json',
                type: 'post',
                crossDomain: true,
                done: function (e, data) {
                    uploadresult = data.result;
                    if (uploadresult["exception"] != "") {
                        alert("上传失败，请重新上传。。。。。");
                        playkey = "error";
                        return;
                    }
                    playkey = uploadresult["httpDataFileuploadDataFiles"][0]["playId"];
                    $("#playkey").val(playkey);
                    showMessage("通知", "恭喜您上传成功");
                },
                progress: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    $('#playkeyFileDiv').progressbar('setValue', progress);
                },
                start: function (e) {
                    $('#playkeyFileDiv').show();
                    $('#playkeyFileDiv').progressbar('setValue', 0);
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

        });


        function clickupload() {
            var id;
            $("#playId").val(playId);
            var formjson = getFormJson('#myform');
            if ($.trim(playId) == "error") {
                showMessage("Error", "文件上传失败，请重新选择文件上传");
                return;
            }
            if ($.trim(playId) == "") {
                showMessage("提示", "请等待文件上传完成后，再点击确认");
                return;
            }
            $.ajax({
                url: 'video/uploadsave',
                data: formjson,
                type: "post",
                dataType: "json",
                beforeSend: function () {
                    return $('#myform').form('enableValidation').form('validate');
                },
                success: function (result) {
                    location.href = "video/checledVideos";
                }
            });
        }

        function start() {
            var value = $('#p').progressbar('getValue');
            if (value < 100) {
                value += Math.floor(Math.random() * 10);
                $('#p').progressbar('setValue', value);
                setTimeout(arguments.callee, 200);
            }
        }
        ;


        function chooseOldVideo(obj, activityId, videoId) {

            var url = "<c:url value='/activity/showRemoveActivityList?activityId='/>" + activityId + "&videoId=" + videoId;

            var rValue = openWindow(url, '', '', '');


        }

        function createNewVideo(obj) {

            var url = "<c:url value='/video/popUpload?popFlg=1'/>";

            var rValue = openWindow(url, '', '950', '650');


        }

        function chooseOldVideo(obj) {

            var url = "<c:url value='/video/showUserVideos?popFlg=1'/>";

            var rValue = openWindow(url, '', '1100', '');


        }

        function openWindow(url, name, width, height, feature) {

            var iWidth = 800; //弹出窗口的宽度;
            var iHeight = 600; //弹出窗口的高度;
            if (width && width != '') {
                iWidth = width;
            }
            if (height && height != '') {
                iHeight = height;
            }
            var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //获得窗口的垂直位置;
            var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; //获得窗口的水平位置;
            //alert(iTop);
            //alert(iLeft);
            if (!feature || feature == null || feature == '') {
                feature = "height=" + iHeight + ",width=" + iWidth + ", top=" + iTop + ", left=" + iLeft + ",alwaysRaised=yes,resizable=no,z-look=yes";
            } else {
                feature = "height=" + iHeight + ",width=" + iWidth + ", top=" + iTop + ", left=" + iLeft + "," + feature;
            }
            var rValue = window.open(url, name, feature);
            //alert('window');
            //alert(rValue);
            if (typeof (rValue) == "undefined") {
                rValue = window.ReturnValue;
            }

            return rValue;
        }

        function getValue(obj, key) {
            if (window.showModalDialog) {
                window.returnValue = obj;
                if (window.opener && window.opener != null) {
                    window.opener.ReturnValue = obj;
                }
            } else {
                window.opener.ReturnValue = obj;
                window.opener.setValue(obj, key);
            }

            window.close();
        }

        function setValue(id, name) {
// 		$('#videoId').val(id);
// 		$('#videoName').val(name);

            document.getElementById("videoId").value = id;
            document.getElementById("videoName").value = name;
        }

        function flushPage() {
            window.location.reload();
        }

        function pageSub() {
            var title = $('#title').val();
            if (!title) {
                alert('请输入标题!');
                return
            }

            var description = $('#description').val();
            if (!description) {
                alert('请输入描述!');
                return
            }

            var activityId = $('#id').val();
            if (activityId) {

            } else {
                /* var videoName=$('#videoName').val();
                 if(!videoName){
                 alert('请选择宣传视频!');
                 return
                 } */
            }


            var order_num = $('#order_num').val();
            if (!order_num) {
                alert('请输入展示排序!');
                return
            } else {
                var updateFlg = true;
                var activityId = '${activity.id }';
                if (activityId) {
                    $.ajax({
                        url: 'activity/isOrderNumOnly',
                        async: false,
                        data: {'activeId': activityId, 'orderNum': order_num},
                        type: "post",
                        dataType: "json",
                        success: function (result) {
                            if (result == 'ok') {
                                //window.location.reload();
                            } else if (result == 'exist') {
                                alert('该展示排序已存在！');
                                updateFlg = false;
                            }
                        }
                    });
                    if (!updateFlg) {
                        return;
                    }
                } else {
                    $.ajax({
                        url: 'activity/updateOrderNum',
                        async: false,
                        data: {'orderNum': order_num},
                        type: "post",
                        dataType: "json",
                        success: function (result) {
                            if (result == 'ok') {
                                //window.location.reload();
                            } else if (result == 'exist') {
                                alert('该展示排序已存在！');
                                updateFlg = false;
                            }
                        }
                    });
                    if (!updateFlg) {
                        return;
                    }
                }

            }
            $('#myform').submit();
        }


        function videoCoverPicFileClick() {
            var playkey = $('#playkey').val();
            //alert(playkey);

            if (!playkey) {
                alert('请先上传宣传视频，再上传视频封面！');
                return false;
            }
        }

        function cancelBanner() {
            $('#bannerPic').val('');
            $('#bannerPicImg').attr('src', '');

        }

        function cancelVideo() {
            $('#playkey').val('');
            $('#videoCoverPic').val('');
            $('#videoCoverPicImg').attr('src', '');
            alert('撤销动作在保存活动后生效！');

        }
    </script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true" style="overflow-y:scroll;">
    <div region="north" border="false"
         style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
        <div data-options="region:'north',border:false"
             style="height: 40px; padding-top: 5px; overflow: hidden;">
            <h2 style="float:left;padding-left:10px;margin: 1px">活动管理</h2>
        </div>
        <div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
            <form id="myform" method="post" action="activity/activeAdd">
                <table style="margin:0 auto;text-align: left;" class="table-doc">
                    <tr>
                        <td style="text-align: right;">输入标题:</td>
                        <td style="text-align: left;">
                            <input class="easyui-validate" name="title" id="title" value="${activity.title}"
                                   <%--<c:if test="${not empty activity.title }">readonly="readonly"</c:if>--%>/>
                            <input class="easyui-validate" name="id" id="id" type="hidden" value="${activity.id}"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">封面:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="hlsfiles" id="hlsfiles" type="file"/>
                                <input name="cover" id="cover" type="hidden" value="${activity.cover }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.cover}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.cover}"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">分享图片:</td>
                        <td colspan="2" valign="middle" style="vertical-align: middle;text-align: left;">
                            <div style="float: left;">
                                <input name="shareImgFile" id="shareImgFile" type="file"/>
                                <input name="shareImg" id="shareImg" type="hidden" value="${activity.shareImg }"/>
                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="sp"
                                     class="easyui-progressbar"></div>
                            </div>
                            <c:if test="${not empty activity.shareImg}">
                                <div style="float: left;">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         src="/restadmin/download${activity.shareImg }"/><br>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">分享文字:</td>
                        <td style="text-align: left;">
                        <textarea name="shareText" id="shareText" class="" cols="60" rows="5"
                                  value="">${activity.shareText }</textarea>
                            <br>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">类型:</td>
                        <td style="text-align: left;">
                            <select required="true" class="easyui-combobox" name="groupType">
                                <option value="1" <c:if test="${activity.groupType eq 1 }">selected="true"</c:if>>所有活动</option>
                                <%-- 							<c:if test="${false }"> --%>
                                <option value="0" <c:if test="${activity.groupType eq 0 }">selected="true"</c:if>>首页推荐</option>
                                <%-- 							</c:if> --%>
                                <option value="2" <c:if test="${activity.groupType eq 2 }">selected="true"</c:if>>发现页推荐</option>
                            </select>

                            <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
                                 class="easyui-progressbar"></div>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">来源:</td>
                        <td style="text-align: left;">
                            <select name="source" class="easyui-validate easyui-combobox" style="width: 120px;" required="true">
                                <option value="all" <c:if test="${activity.source eq 'all'}">selected="true"</c:if>>所有</option>
                                <option value="ios" <c:if test="${activity.source eq 'ios'}">selected="true"</c:if>>Ios</option>
                                <option value="android" <c:if test="${activity.source eq 'android'}">selected="true"</c:if>>Android</option>
                            </select>
                        </td>
                    </tr>
                    <c:if test="${false }">
                        <tr>
                            <td style="text-align: right;">宣传视频</td>
                            <td style="text-align: left;">
                                <input name="videoId" id="videoId" type="hidden" class="" value="${imVideoId}"/>
                                <input name="videoName" type="text" id="videoName" readonly="readonly" class=" "
                                       value="${imVideoName}"/>
                                <br>
                                <br>

                                <input id="mdOM${active.id}" name="mdOM${active.id}" type="button" value="选择已有视频"
                                       onclick="chooseOldVideo(this);"/>&nbsp;&nbsp;&nbsp;&nbsp;

                                <input id="mdOM${active.id}" name="mdOM${active.id}" type="button" value="上传新视频"
                                       onclick="createNewVideo(this);"/>
                            </td>
                        </tr>
                    </c:if>

                    <tr>
                        <td style="text-align: right;">描述:</td>
                        <td style="text-align: left;">
                        <textarea name="description" id="description" class="easyui-validate" cols="60" rows="5"
                                  value="">${activity.description}</textarea>

                            <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
                                 class="easyui-progressbar"></div>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">banner图:</td>
                        <td style="text-align: left;">
                            <div style="float: left;">
                                <input name="bannerPicFile" id="bannerPicFile" type="file"/>
                                <input name="status" id="status" type="hidden" value="1"/>
                                <span>
                                    <a href="javascript:void(0)" style="" class="easyui-linkbutton"
                                       onclick="cancelBanner();">&nbsp;&nbsp;撤销banner图&nbsp;&nbsp;</a>
                                </span>
                                <input name=bannerPic id="bannerPic" type="hidden" value="${activity.bannerPic}"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none"
                                     id="bannerPicFileDiv"
                                     class="easyui-progressbar"></div>
                            </div>
                            <div style="float:left;">
                                <c:if test="${not empty activity.bannerPic }">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;" id="bannerPicImg"
                                         src="/restadmin/download${activity.bannerPic}"/><br>
                                </c:if>
                            </div>
                            <!-- 					<br><span style="color: red" >图片名称中请勿使用中文</span> -->
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">宣传视频:</td>
                        <td style="text-align: left;">
                            <input type="hidden" name="playkey" id="playkey"/>
                            <input type="file" name="playkeyFile" id="playkeyFile" style="width:50%"/>

                            <span>
                                <a href="javascript:void(0)" style="" class="easyui-linkbutton"
                                   onclick="cancelVideo();">
                                    &nbsp;&nbsp;撤销宣传视频&nbsp;&nbsp;</a>
                            </span>

                            <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="playkeyFileDiv"
                                 class="easyui-progressbar"></div>

                        </td>
                    </tr>

                    <tr>
                        <td style="text-align: right;">宣传视频封面:</td>
                        <td style="text-align: left;">
                            <div style="float: left;">
                                <input name="videoCoverPicFile" id="videoCoverPicFile" type="file"
                                       onclick="javascript:return videoCoverPicFileClick();"/>
                                <input name="videoCoverPic" id="videoCoverPic" type="hidden"
                                       value="${activity.videoCoverPic }"/>

                                <div style="margin-left:auto;margin-right:auto;width:400px;display:none"
                                     id="videoCoverPicFileDiv" class="easyui-progressbar"></div>
                            </div>
                            <div style="float: left;">
                                <c:if test="${not empty activity.videoCoverPic }">
                                    <img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
                                         id="videoCoverPicImg"
                                         src="/restadmin/download${activity.videoCoverPic}"/><br>
                                </c:if>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align: right;">展示排序:</td>
                        <td style="text-align: left;">
                            <input name="order_num" id="order_num" class="" value="${activity.order_num }"/>

                            <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
                                 class="easyui-progressbar"></div>
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align: right;">活动标签:</td>
                        <td style="text-align: left;">
                        <textarea name="tags" id="tags" class="" cols="60" rows="5"
                                  value="">${activity.tags }</textarea>
                            <br>标签以英文分号（;）分隔,标签两端不加（#）号
                        </td>
                    </tr>

                    <tr>
                        <td style="text-align: right;">VIP频道:</td>
                        <td style="text-align: left;">
                        <textarea name="vips" id="vips" class="" cols="60" rows="3"
                                  value="">${activity.vips }</textarea>
                            <br>蓝V 1，黄V 2，绿V 3 允许多个，以英文分号（;）分隔。（如蓝V和绿V组合配置为： 1;3 )
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">个人/组合明星频道:</td>
                        <td style="text-align: left;">
                        <textarea name="rusers" id="rusers" class="" cols="60" rows="3"
                                  value="">${activity.rusers }</textarea>
                            <br>用户id，允许多个，以英文分号（;）分隔。（如： 1256;5598 )
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" align="center">
                            <a href="javascript:void(0)" style="" class="easyui-linkbutton"
                               data-options="iconCls:'icon-ok'"
                               onclick="pageSub()">保存</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="javascript:void(0)" style="" class="easyui-linkbutton"
                               data-options="iconCls:'icon-cancel'" onclick="javascript:history.back();">取消</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</body>
</html>