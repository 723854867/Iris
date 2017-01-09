<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>热门话题管理</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script src="js/fp/jquery.iframe-transport.js"></script>
    <script src="js/fp/jquery.fileupload.js"></script>
    <script type="text/javascript">

        $(function() {

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
        })

    </script>
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="js/admin/query_hot_label.js"></script>
</head>
<body>
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">热门话题管理</h2>

        <h2 style="float:left;padding-left:50px;margin: 1px">
            <a href="activity/labelList" class="easyui-linkbutton">话题管理</a>
        </h2>
    </div>
    <div id="tb" style="padding:5px;height:auto">
        <div style="margin-bottom:5px">
            <a href="javascript:;" onclick="showInsertDialog()" class="easyui-linkbutton" iconCls="icon-add"
               plain="true">新建热门话题</a>
        </div>
        <div>
            话题名称: <input class="text-input" name="keyword" id="keyword" style="width:200px">
            <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
        </div>
    </div>
</div>

<div id="dlg" class="easyui-dialog" style="width:550px;height:430px;padding:2px 5px;" closed="true"
     buttons="#dlg-buttons">
    <table class="table-doc" width="100%">
        <tr>
            <td>选择话题:</td>
            <td>
                <input name=labelName id="labelName" readonly="readonly" value=""
                       onfocus="showLabelDialog();">
                <input name="labelId" id="labelId" type="hidden" value="">
            </td>
        </tr>
        <tr>
            <td>排序权重:</td>
            <td>
                <input name="displayOrder" id="displayOrder"  min="1" value="">
            </td>
        </tr>
        <tr>
            <td>分享图片: </td>
            <td valign="middle" style="vertical-align: middle;text-align: left;">
                <div style="float: left;">
                    <div style="float: left;">
                        <input name="shareImgFile" id="shareImgFile" type="file"/>
                        <input name="shareImg" id="shareImg" type="hidden" />
                        <div style="margin-left:auto;margin-right:auto;display:none" id="p"
                             class="easyui-progressbar"></div>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td>分享文案: </td>
            <td style="vertical-align: middle;text-align: left;" >
                <textarea name="shareText" id="shareText"  class="" cols="40" rows="2" ></textarea>
            </td>
        </tr>
    </table>
</div>
<!-- 对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertHotLabel()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 弹出的选择话题对话框 -->
<div id="label_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:2px 5px" closed="true"
     buttons="#vdlg-buttons">
    <table id="labelTable" data-options="border:false,toolbar:'#tb_label'">
    </table>
    <div id="tb_label">
        <form id="searchForm">
            <table>
                <tr>
                    <td><label>话题名称：</label></td>
                    <td>
                        <input type="text" name="keyword">
                    </td>
                    <td>
                        <a href="javascript:;" onclick="labelListSearch()" class="easyui-linkbutton"
                           iconCls="icon-search">搜索</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div id="vdlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectLabel();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#label_dlg').dialog('close')">取消</a>
</div>

</body>
</html>