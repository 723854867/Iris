<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>每日直播数据</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type='text/javascript' src="js/admin/query_live_data.js"></script>
    <style>
        .bc{
            background-color: #666666;
            color: #FFFFFF!important;
            padding: 4px;
        }
    </style>
</head>
<body>

<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>

<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
        <a href="liveParam/liveDataList" <c:if test="${selected eq 'dayLiveData'}">class="bc" </c:if> style="text-decoration: none;color: #000;">每日直播数据</a>
        <a href="room/forwardLiveDetailList" <c:if test="${selected eq 'userLiveDetail'}">class="bc" </c:if> style="text-decoration: none;color: #000;">用户直播记录</a>
        </h2>
    </div>
    <div>
        <table>
            <tr>

                <td><label>选择时间：</label></td>
                <td>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="startTime" id="startTime">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="endTime" id="endTime">
                </td>
                <td>
                    <label>平台：</label>
                </td>
                <td>
                    <select class="easyui-combobox" id="platform" name="platform">
                        <option value="">请选择</option>
                        <option value="ios">ios</option>
                        <option value="android">android</option>
                    </select>
                </td>
                <td>
                    <label>渠道：</label>
                </td>
                <td>
                    <select name="singerType" id="regPlatform" name="regPlatform" class="easyui-combobox"
                            style="width: 120px;" data-options="
                            valueField: 'id', textField: 'text',
                            url:'comboBox/getRegPlatformCombobox'"/>
                </td>
                <td>
                    <label>版本：</label>
                </td>
                <td>
                    <select class="easyui-combobox" id="appVersion" name="appVersion">
                        <option value="">请选择</option>
                        <option value="3.0.8">3.0.8</option>
                        <option value="3.0.7">3.0.7</option>
                        <option value="3.0.6">3.0.6</option>
                        <option value="3.0.5">3.0.5</option>
                        <option value="3.0.4">3.0.4</option>
                        <option value="3.0.1">3.0.1</option>
                    </select>
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
            <tr>
                <td>直播总人数：<span id="count"></span></td>
            </tr>
        </table>
    </div>

</div>

<div id="detail_dlg" class="easyui-dialog" style="width:850px;height:700px;" closed="true"
     buttons="#detail-dlg-buttons">
    <div id="dataGridToolbarDetail">

    </div>
    <table class="table-doc" id="displayDetailTable" cellspacing="0" width="100%"></table>
</div>

</body>
</html>