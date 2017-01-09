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
    <title>提现管理</title>
    <script type='text/javascript' src='js/admin/query_withdrawals.js'></script>
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
                <a href="diamond/forwardDiamondList" <c:if test="${selected eq 'diamond'}">class="bc" </c:if> style="text-decoration: none;color: #000;">金币管理</a>
                <a href="exchange/forwardExchangeList?type=1" <c:if test="${selected eq 'exchange'}">class="bc" </c:if> style="text-decoration: none;color: #000;">兑换管理</a>
                <a href="exchange/forwardExchangeList?type=2" <c:if test="${selected eq 'withdrawals'}">class="bc" </c:if> style="text-decoration: none;color: #000;">提现管理</a>
                <a href="giveGold/forwardGiveGold" <c:if test="${selected eq 'giveGold'}">class="bc" </c:if> style="text-decoration: none;color: #000;">赠送金币设置</a>
        </h2>
    </div>
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <label>名称：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:120px" name="name" id="queryName" value=""/>
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
                    <a href="javascript:;" onclick="doSearch(2)" class="easyui-linkbutton"
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
<div id="insert_dlg" class="easyui-dialog" style="width:500px;height:480px;" closed="true"
     buttons="#insert-dlg-buttons">
    <form id="insertForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" required="true" id="name" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">icon：</td>
                <td style="text-align: left;">
                    <input type="file" name="iconUrl" id="iconUrl" class="easyui-validatebox" >
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">金豆数量：</td>
                <td style="text-align: left;">
                    <input type="hidden" name="type" id="type" value="2">
                    <input type="text" name="pointCount" onKeyUp="calculationPrice()" id="pointCount" class="easyui-validatebox" required="true" missingMessage="必须填写大于0的数字" style="width: 200px;" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">价格：</td>
                <td style="text-align: left;">
                    <input type="hidden" name="price" id="price" value="">
                    <span id="priceDisplay">

                    </span>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">状态：</td>
                <td style="text-align: left;">
                    <select name="state" id="state" style="width: 200px;" required="true" class="easyui-validatebox easyui-combobox">
                        <option value="1">上架</option>
                        <option value="0">下架</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">权重：</td>
                <td style="text-align: left;">
                    <input type="text" name="weight" style="width: 200px;" class="easyui-numberbox" min="1" precision="2" required="true" missingMessage="请填写大于0的数字" id="weight" class="easyui-validatebox"
                           value="">
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
<input type="hidden" name="rate" id="rate" value="${rate}">
<!-- 添加拍币弹出框 end -->
<c:import url="/main/common.jsp"/>
</body>
</html>
