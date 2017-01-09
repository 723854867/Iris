<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>审核日志管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_check_log_list.js"></script>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#dataGridToolbar'">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">审核日志管理</h2>
    </div>
    <div>
        <form action="" id="searchForm">
        	<span>
                <label>类型：</label>
                <span>
                    <select name="type" class="easyui-combobox" id="type">
                    	<option value="">请选择</option>
                        <option value="live">直播</option>
                        <option value="head">头像</option>
                        <option value="home">背景</option>
                    </select>
                </span>
            </span>
            <span>
                <label>用户：</label>
                <span>
                    <select name="uType" class="easyui-combobox" id="uType">
                        <option value="">用户搜索</option>
                        <option value="1">用户ID</option>
                        <option value="3">手机号</option>
                        <option value="4">用户昵称</option>
                    </select>
                </span>
                <span>
                    <input type="text" name="uValue" class="easyui-textbox" style="width: 120px;" id="uValue">
                </span>
            </span>
            <span>
                <label>操作人：</label>
                <span>
                    <select name="uId" class="easyui-combobox" id="uId">
                    	<option value="">请选择</option>
                    	<c:forEach items="${list}" var="user" >
                        	<option value="${user.id}">${user.username}</option>
                        </c:forEach>
                    </select>
                </span>
            </span>
            <span>
                <label>审核结果：</label>
                <span>
                    <select name="operate" class="easyui-combobox" id="operate">
                        <option value="">不限</option>
                        <option value="下线">下线</option>
                        <option value="封号">封号</option>
                        <option value="通过">通过</option>
                        <option value="不通过">不通过</option>
                        <option value="屏蔽">屏蔽</option>
                    </select>
                </span>
            </span>
            <span>
                <label>日期：</label>
                <span>
                    <input type="text" class="easyui-datetimebox" style="width: 120px;" name="startDate" id="startDate">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datetimebox" style="width: 120px;" name="endDate" id="endDate">
                </span>
            </span>
            <span>
                <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                   iconCls="icon-search">搜索</a>
            </span>
        </form>
    </div>
</div>
</body>
</html>
