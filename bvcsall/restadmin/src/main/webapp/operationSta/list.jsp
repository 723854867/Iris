<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2015/12/23
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<html>
<head>
    <title>运营统计</title>
    <script type='text/javascript' src='js/admin/query_operationstas.js'></script>
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
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
                <a href="operationSta/operationStas" <c:if test="${selected eq 'dmr'}">class="bc" </c:if> style="text-decoration: none;color: #000;">月度未消费金币数</a>
                
<!--                 <a href="exchange/forwardExchangeList?type=1" <c:if test="${selected eq 'exchange'}">class="bc" </c:if> style="text-decoration: none;color: #000;">兑换管理</a> -->
<!--                 <a href="exchange/forwardExchangeList?type=2" <c:if test="${selected eq 'withdrawals'}">class="bc" </c:if> style="text-decoration: none;color: #000;">提现管理</a> -->
<!--                 <a href="giveGold/forwardGiveGold" <c:if test="${selected eq 'giveGold'}">class="bc" </c:if> style="text-decoration: none;color: #000;">赠送金币设置</a> -->
        </h2>
    </div>
    
    <c:if test="${false }">
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <label>金币ID：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:80px" name="id" id="queryId" value=""/>
                </td>
                <td>
                    <label>金币名称：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:120px" name="name" id="queryName" value=""/>
                </td>
                <td>
                    <label>支付方式：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="payMethod" id="queryPayMethod">
                        <option value="">请选择</option>
                        <option value="0">苹果支付</option>
                        <option value="1">其它</option>
                    </select>
                </td>
                <td>
                    <label>状态：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="state" id="queryState">
                        <option value="">请选择</option>
                        <option value="1">上架</option>
                        <option value="0">下架</option>
                    </select>
                </td>
                <td>
                    <label>是否赠送：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="isGive" id="queryIsGive">
                        <option value="">请选择</option>
                        <option value="0">不赠送</option>
                        <option value="1">赠送</option>
                    </select>
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
    </c:if>
</div>

</body>
</html>
