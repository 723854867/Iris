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
    <title>提现黑名单管理</title>
    <script type='text/javascript' src='js/admin/query_black_list.js'></script>
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
        <h2 style="float:left;padding-left:10px;margin: 1px">提现黑名单管理</h2>
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
                <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                   iconCls="icon-search" style="">搜索</a>
        	</span>
        </form>
    </div>
    <div>
        <input type="text" style="width: 120px;" placeholder="请输入用户ID" name="userId" id="userId" value=""
               class="easyui-numberbox easyui-validate">
        <a href="javascript:;" onclick="insertWCBlackList()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
    </div>
</div>

<c:import url="/main/common.jsp"/>
</body>
</html>
