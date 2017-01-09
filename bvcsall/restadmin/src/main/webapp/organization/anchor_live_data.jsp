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
    <title>机构主播直播数据</title>
    <script type='text/javascript' src='js/admin/query_anchor_live_data.js'></script>
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
<table id="displayAnchorLiveDataTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
            <a href="organization/forwardManageAnchor?organizationId=${organizationId}" <c:if test="${selected eq 'anchor'}">class="bc" </c:if> style="text-decoration: none;color: #000;">机构主播管理</a>
            <a href="organization/forwardAnchorLiveData?organizationId=${organizationId}" <c:if test="${selected eq 'liveData'}">class="bc" </c:if> style="text-decoration: none;color: #000;">机构主播直播数据</a>
        </h2>
        <input type="hidden" value="${organizationId}" id="organizationId">

    </div>
    <div id="search" title="搜索" style="margin-left: 10px;">
        <form id="searchForm">
            <span>
                <label>
                    <select class="easyui-combobox" name="user" id="user">
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
                <label>时间：</label>
                <span>

                    <input type="text" class="easyui-datetimebox" style="width: 150px;" name="startDate" id="startDate">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datetimebox" style="width: 150px;" name="endDate" id="endDate">
                </span>
             </span>
            <span>
                <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                   iconCls="icon-search" style="">搜索</a>
        	</span>
            <span>
                <a href="javascript:;" onclick="exportOrgAnchorLiveData(${organizationId})" class="easyui-linkbutton"
                   iconCls="icon-print" style="">导出</a>
        	</span>
        </form>
    </div>
</div>
<c:import url="/main/common.jsp"/>
</body>
</html>
