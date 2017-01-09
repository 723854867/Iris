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
    <title>主播收益管理</title>
    <script type='text/javascript' src='js/admin/query_anchor.js'></script>
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
        <h2 style="float:left;padding-left:10px;margin: 1px">主播收益管理</h2>
    </div>
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <label>用户搜索：</label>
                </td>
                <td>
                    <select class="easyui-combox" name="key" id="key">
                    	<option value="" selected="selected">--请选择--</option>
                    	<option value="id">ID</option>
                    	<option value="name">昵称</option>
                    	<option value="phone">手机号</option>
                    </select>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:150px" name="value" id="value" value=""/>
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
                <td>
                    <a href="javascript:;" onclick="exportAnchorIncome()" class="easyui-linkbutton"
                       iconCls="icon-print">导出至Excel</a>
                </td>
            </tr>
        </table>
    </div>
    <div>
    	<label>总计</label><label>   金豆数：<span id="totalPoints">0</span></label><label>   现金：<span id="totalCash">0</span></label>
    </div>
</div>
<!-- 添加礼物弹出框 end -->
<c:import url="/main/common.jsp"/>

<!-- 主播结算历史记录查询弹出框 end -->

</body>
</html>
