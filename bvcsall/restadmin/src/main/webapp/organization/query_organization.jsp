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
    <title>机构管理</title>
    <script type='text/javascript' src='js/admin/query_organization.js'></script>
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
        <h2 style="float:left;padding-left:10px;margin: 1px">机构管理</h2>
    </div>
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <label>状态：</label>
                </td>
                <td>
                    <select id="key" name="key" class="easyui-combobox">
                        <option value="">全部</option>
                        <option value="0">机构ID</option>
                        <option value="1">机构名称</option>
                    </select>
                </td>
                <td>
                    <input type="text" name="useKey" id="useKey" class="easyui-validatebox">
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

<!-- 添加拍币弹出框 start -->
<div id="insert_dlg" class="easyui-dialog" style="width:500px;height:300px;" closed="true"
     buttons="#insert-dlg-buttons">
    <form id="insertForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">机构类型：</td>
                <td style="text-align: left;">
                    <select id="orgType" name="orgType" class="easyui-combobox">
                        <option value="1">返点</option>
                        <option value="2">底薪</option>
                    </select>
                    
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">机构名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="orgName" required="true" id="orgName" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">负责人：</td>
                <td style="text-align: left;">
                    <input type="text" name="leader" id="leader" class="easyui-validatebox" required="true" style="width: 200px;" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">负责人电话：</td>
                <td style="text-align: left;">
                    <input type="text" name="leaderPhone" class="easyui-numberbox" id="leaderPhone" required="true" style="width: 200px;" value="">
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
<!-- 添加拍币弹出框 end -->

<!-- 详情弹出框 end -->
<c:import url="/main/common.jsp"/>
</body>
</html>
