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
<html>
<head>
    <title>查看礼物列表信息</title>
    <script type='text/javascript' src='js/admin/query_gift.js'></script>
</head>
<body>
<!-- 列表 -->
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">礼物管理</h2>
    </div>
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <label>礼物ID：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:80px" name="id" id="queryId" value=""/>
                </td>
                <td>
                    <label>礼物名称：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:120px" name="name" id="queryName" value=""/>
                </td>
                <td>
                    <label>礼物用途：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="giftPurpose" id="queryGiftPurpose">
                        <option value="">请选择</option>
                        <option value="1">收视榜</option>
                        <option value="2">人气榜</option>
                        <option value="3">直播</option>
                        <option value="4">其它</option>
                    </select>
                </td>
                <td>
                    <label>效果类型：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="effectType" id="queryEffectType">
                        <option value="">请选择</option>
                        <option value="0">无动画</option>
                        <option value="1">雪花</option>
                        <option value="2">心跳放大</option>
                        <option value="3">位移</option>
                        <option value="5">雪花停留</option>
                        <option value="6">抛物线</option>
                        <option value="7">跑车</option>
                        <option value="8">城堡岛屿</option>
                        <option value="9">游轮</option>
                    </select>
                </td>
                <td>
                    <label>状态：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="state" id="queryState">
                        <option value="">请选择</option>
                        <option value="1" selected="selected">上架</option>
                        <option value="0">下架</option>
                    </select>
                </td>
                <td>
                    <label>标记状态：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="markerState" id="queryMarkerState">
                        <option value="">请选择</option>
                        <option value="normal">正常</option>
                        <option value="new">new</option>
                    </select>
                </td>
                <td>
                    <label>免费：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="isFree" id="queryIsFree">
                        <option value="">请选择</option>
                        <option value="1">免费</option>
                        <option value="0">不免费</option>
                    </select>
                </td>
                <td>
                    <label>连发：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="loopSupport" id="queryLoopSupport">
                        <option value="">请选择</option>
                        <option value="1">支持</option>
                        <option value="0">不支持</option>
                    </select>
                </td>
                <td>
                    <label>专属：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="isExclusive" id="queryIsExclusive">
                        <option value="">请选择</option>
                        <option value="1">是</option>
                        <option value="0">不是</option>
                    </select>
                </td>
                <td>
                    <label>截屏：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="screenshotSupport" id="queryScreenshotSupport">
                        <option value="">请选择</option>
                        <option value="1">支持</option>
                        <option value="0">不支持</option>
                    </select>
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
    <div>
        <a href="javascript:;" onclick="insertDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
    </div>
</div>

<!-- 添加礼物弹出框 start -->
<div id="insert_dlg" class="easyui-dialog" style="width:500px;height:630px;" closed="true"
     buttons="#insert-dlg-buttons">
    <form id="insertForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">礼物名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" id="name" required="true" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">礼物icon：</td>
                <td style="text-align: left;">
                    <input type="file" name="giftIconUrl" id="giftIconUrl" required="true" accept="jpg,png,gif"
                           class="easyui-validatebox">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">礼物用途：</td>
                <td style="text-align: left;">
                    <select name="giftPurpose" id="giftPurpose" required="true" style="width: 200px;"
                            class="easyui-validatebox easyui-combobox">
                        <option value="1">收视榜</option>
                        <option value="2">人气榜</option>
                        <option value="3">直播</option>
                        <option value="4">其它</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">效果类型：</td>
                <td style="text-align: left;">
                    <select name="effectType" id="effectType" required="true" style="width: 200px;"
                            class="easyui-validatebox easyui-combobox">
                        <option value="0">无动画</option>
                        <option value="1">雪花</option>
                        <option value="2">心跳放大</option>
                        <option value="3">位移</option>
                        <option value="5">雪花停留</option>
                        <option value="6">抛物线</option>
                        <option value="7">跑车</option>
                        <option value="8">城堡岛屿</option>
                        <option value="9">游轮</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">效果文件：</td>
                <td style="text-align: left;">
                    <input type="file" name="effectFileUrl" id="effectFileUrl" class="easyui-validatebox"
                           accept="jpg,png,gif">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">价格：</td>
                <td style="text-align: left;">
                    <input type="text" name="price" id="price" class="easyui-numberbox" min="0" precision="0" required="true" missingMessage="必须填写大于或者等于0的数字" style="width: 200px;" class="easyui-validatebox"
                           value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">点数（拍豆）：</td>
                <td style="text-align: left;">
                    <input type="text" name="point" id="point" style="width: 200px;" class="easyui-numberbox" min="0" precision="0" required="true" missingMessage="必须填写大于或者等于0的整数"
                           value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">状态：</td>
                <td style="text-align: left;">
                    <select name="state" id="state" style="width: 200px;" required="true" class="easyui-validatebox easyui-combobox">
                        <option value="1">上架</option>
                        <option value="0">下架</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">权重：</td>
                <td style="text-align: left;">
                    <input type="text" name="weight" style="width: 200px;" id="weight" class="easyui-numberbox" min="0.01" precision="2" required="true" missingMessage="必须填写大于或者等于0的数字"
                           value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">标记状态：</td>
                <td style="text-align: left;">
                    <select name="markerState" id="markerState" style="width: 200px;" required="true" class="easyui-validatebox easyui-combobox">
                        <option value="normal">正常</option>
                        <option value="new">new</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">是否免费：</td>
                <td style="text-align: left;">
                    <input type="radio" name="isFree" class="easyui-validatebox" value="0" checked="checked"
                           onclick="freeCountDisplay(0)">不免费
                    <input type="radio" name="isFree" class="easyui-validatebox" onclick="freeCountDisplay(1)"
                           value="1">免费
                </td>
            </tr>
            <tr id="freeCountTr" style="display: none;">
                <td style="text-align: right;">免费次数：</td>
                <td style="text-align: left;">
                    <input type="text" name="freeCount" id="freeCount" class="easyui-numberbox" min="1" max="1000"
                           precision="0" missingMessage="请填写大于0的整数" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">支持连发：</td>
                <td style="text-align: left;">
                    <input type="radio" name="loopSupport" class="easyui-validatebox" value="0" checked="checked">不支持
                    <input type="radio" name="loopSupport" class="easyui-validatebox" value="1">支持
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">是否专属：</td>
                <td style="text-align: left;">
                    <input type="radio" name="isExclusive" class="easyui-validatebox" value="0" checked="checked">不是
                    <input type="radio" name="isExclusive" class="easyui-validatebox" value="1">是
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">支持截屏：</td>
                <td style="text-align: left;">
                    <input type="radio" name="screenshotSupport" class="easyui-validatebox" value="0" checked="checked">不支持
                    <input type="radio" name="screenshotSupport" class="easyui-validatebox" value="1">支持
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">自动截屏次数：</td>
                <td style="text-align: left;">
                    <input type="text" name="screenshotNumber" id="screenshotNumber" class="easyui-numberbox" min="1" max="1000000000"
                           precision="0" missingMessage="请填写大于0的整数" value="">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsert()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_dlg').dialog('close')">取消</a>
</div>
<!-- 添加礼物弹出框 end -->
<c:import url="/main/common.jsp"/>
</body>
</html>
