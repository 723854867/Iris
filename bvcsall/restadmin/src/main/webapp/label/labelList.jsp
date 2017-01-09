<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>话题管理</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="js/admin/query_label.js"></script>
</head>
<body>
<table width="100%" id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'" class="easyui-datagrid">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">话题管理</h2>
        <h2 style="float:left;padding-left:50px;margin: 1px">
            <a href="activity/hotLabelList" class="easyui-linkbutton">热门话题管理</a>
        </h2>
    </div>
    <div style="margin: 5px auto;">
        <input class="easyui-searchbox input input-medium" name="keyword" id="keyword"
               data-options="prompt:'请输入话题名称',menu:'#mm',searcher:doSearch" style="width:300px"
               value="${keyword}"/>

        <div id="mm">
            <div data-options="name:'keyword',iconCls:'icon-ok'">话题名称</div>
        </div>
        <div style="margin-top: 10px;">
            <input class="" name="newLabelName" id="newLabelName" maxlength="20" data-options="prompt:'手动创建话题'"
                   style="width:200px" value=""/>
            &nbsp;&nbsp;
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:newLabelNameAdd()"
               plain="true">添加</a>
        </div>
    </div>
</div>
</body>
</html>