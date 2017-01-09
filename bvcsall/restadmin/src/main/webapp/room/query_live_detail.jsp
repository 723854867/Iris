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
    <title>用户直播记录</title>
    <script type='text/javascript' src='js/admin/query_live_detail.js'></script>
    <style>
        .bc{
            background-color: #666666;
            color: #FFFFFF!important;
            padding: 4px;
        }
    </style>
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
            <a href="liveParam/liveDataList" <c:if test="${selected eq 'dayLiveData'}">class="bc" </c:if> style="text-decoration: none;color: #000;">每日直播数据</a>
            <a href="room/forwardLiveDetailList" <c:if test="${selected eq 'userLiveDetail'}">class="bc" </c:if> style="text-decoration: none;color: #000;">用户直播记录</a>
        </h2>
    </div>
    <div>
<span>
            <label>
                <select class="easyui-combobox" name="userKey" id="userKey">
                    <option value="">用户搜索</option>
                    <option value="1">用户ID</option>
                    <option value="2">用户名</option>
                    <option value="3">昵称</option>
                    <option value="4">手机号码</option>
                </select>
            </label>
        </span>
        <span>
            <input type="text" name="userKeyword" id="userKeyword" class="easyui-validatebox">
        </span>
        <span>
            <label>直播开始时间：</label>
            <span>

                <input type="text" class="easyui-datebox" style="width: 120px;" name="startDate" id="startDate">
                &nbsp;至&nbsp;
                <input type="text" class="easyui-datebox" style="width: 120px;" name="endDate" id="endDate">
            </span>
         </span>
         <span>
         	<label>平台：</label>
            <label>
                <select class="easyui-combobox" name="platform" id="platform">
                    <option value="">全部</option>
                    <option value="ios">IOS</option>
                    <option value="android">Android</option>
                </select>
            </label>
        </span>
        <span>
        	<label>版本：</label>
            <label>
                <select class="easyui-combobox" name="appVersion" id="appVersion">
                    <option value="">全部</option>
                    <c:forEach items="${appVersionList }" var="v" >
                    	<option value="${v }">${v }</option>
                    </c:forEach>
                </select>
            </label>
        </span>
        <td>
        	渠道：
        </td>
        <td>
                <select class="easyui-combobox" name="channel" id="channel">
                    <option value="">全部</option>
                    <c:forEach items="${channelList }" var="v" >
                    	<option value="${v }">${v }</option>
                    </c:forEach>
                </select>
        </td>
            <span>
                <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                   iconCls="icon-search" style="">搜索</a>
        	</span>
    </div>
</div>

<c:import url="/main/common.jsp"/>
</body>
</html>
