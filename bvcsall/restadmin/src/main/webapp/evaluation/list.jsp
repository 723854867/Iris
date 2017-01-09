<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>评论管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_evaluation.js"></script>
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
        <h2 style="float:left;padding-left:10px;margin: 1px">评论管理</h2>
    </div>
    <div>
        <form action="" id="searchForm">
            <span>
                <label>评论人：</label>
                <span>
                    <select name="evaluationPersonType" class="easyui-combobox" id="evaluationPersonType">
                        <option value="">用户搜索</option>
                        <option value="1">用户ID</option>
                        <option value="2">用户名</option>
                        <option value="3">手机号</option>
                        <option value="4">用户昵称</option>
                    </select>
                </span>
                <span>
                    <input type="text" name="evaluationPerson" class="easyui-textbox" style="width: 120px;" id="evaluationPerson">
                </span>
            </span>
            <span>
                <label>日期：</label>
                <span>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="startTime" id="startTime">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="endTime" id="endTime">
                </span>
            </span>
            <span>
                <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                   iconCls="icon-search">搜索</a>
            </span>
            <div style="margin-bottom:5px">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
                   onclick="javascript:deleteEvaluations()" plain="true">批量删除</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
