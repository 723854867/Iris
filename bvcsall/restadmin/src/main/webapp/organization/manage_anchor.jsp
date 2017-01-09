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
    <title>机构主播管理</title>
    <script type='text/javascript' src='js/admin/query_organization_anchor.js'></script>
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
<table id="displayAnchorTable" data-options="border:false,toolbar:'#dataGridToolbar'"
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
    <div>
        <a href="javascript:;" onclick="exportOrgAnchor(${organizationId})" class="easyui-linkbutton"
           iconCls="icon-print" style="">导出</a>
    </div>
</div>
<c:import url="/main/common.jsp"/>
</body>
</html>
