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
    <title>查看金币列表信息</title>
    <script type='text/javascript' src='js/admin/query_diamond.js'></script>
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
</div>
<!-- 添加拍币弹出框 start -->
<div id="insert_dlg" class="easyui-dialog" style="width:500px;height:480px;" closed="true"
     buttons="#insert-dlg-buttons">
    <form id="insertForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">金币名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" required="true" id="name" style="width: 200px;"
                           class="easyui-validatebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">金币icon：</td>
                <td style="text-align: left;">
                    <input type="file" name="diamondIconUrl" id="diamondIconUrl" required="true"
                           class="easyui-validatebox">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">价格（人民币）：</td>
                <td style="text-align: left;">
                    <input type="text" name="price" id="price" class="easyui-numberbox" min="0.01" precision="2"
                           required="true" missingMessage="必须填写大于0的数字" style="width: 200px;" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">支付方式：</td>
                <td style="text-align: left;">
                    <select name="payMethod" id="payMethod" required="true" style="width: 200px;"
                            class="easyui-validatebox easyui-combobox">
                        <option value="0">苹果</option>
                        <option value="1">其它</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">金币数量：</td>
                <td style="text-align: left;">
                    <input type="text" name="diamondCount" class="easyui-numberbox" min="1" precision="0"
                           required="true" missingMessage="请填写大于0的整数" id="diamondCount" style="width: 200px;"
                           value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">是否赠送：</td>
                <td style="text-align: left;">
                    <input type="radio" name="isGive" class="easyui-validatebox" checked="checked"
                           onclick="giveCountDisplay(0)" value="0">不赠送
                    <input type="radio" name="isGive" class="easyui-validatebox" value="1"
                           onclick="giveCountDisplay(1)">赠送
                </td>
            </tr>
            <tr id="giveCountTr" style="display: none;">
                <td style="text-align: right;">赠送数量：</td>
                <td style="text-align: left;">
                    <input type="text" name="giveCount" id="giveCount" class="easyui-numberbox" min="1" max="1000000"
                           precision="0" missingMessage="请填写大于0的整数" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">状态：</td>
                <td style="text-align: left;">
                    <select name="state" id="state" style="width: 200px;" required="true"
                            class="easyui-validatebox easyui-combobox">
                        <option value="1">上架</option>
                        <option value="0">下架</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">权重：</td>
                <td style="text-align: left;">
                    <input type="text" name="weight" style="width: 200px;" class="easyui-numberbox" min="1"
                           precision="2" required="true" missingMessage="请填写大于0的数字" id="weight"
                           class="easyui-validatebox"
                           value="">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertTemplate()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_dlg').dialog('close')">取消</a>
</div>
<!-- 添加拍币弹出框 end -->
<c:import url="/main/common.jsp"/>

<div id="confirm_dlg" class="easyui-dialog" style="width:350px;height:350px;" closed="true"
     buttons="#confirm-dlg-buttons">
    <table class="table-doc" cellspacing="0" width="100%">
        <th width="40%">&nbsp;</th>
        <th width="60%">&nbsp;</th>
        <tr>
            <td style="text-align: right;">金币名称：</td>
            <td style="text-align: left;color:red;" id="nameConfirm">
            </td>
        </tr>
        <tr>
            <td style="text-align: right;">价格（人民币）：</td>
            <td style="text-align: left;color:red;" id="priceConfirm">
            </td>
        </tr>
        <tr>
            <td style="text-align: right;">支付方式：</td>
            <td style="text-align: left;color:red;" id="payMethodConfirm">
            </td>
        </tr>
        <tr>
            <td style="text-align: right;">拍币数量：</td>
            <td style="text-align: left;color:red;" id="diamondCountConfirm">
            </td>
        </tr>
        <tr>
            <td style="text-align: right;">是否赠送：</td>
            <td style="text-align: left;color:red;" id="isGiveConfirm"></td>
        </tr>
        <tr>
            <td style="text-align: right;">赠送数量：</td>
            <td style="text-align: left;color:red;" id="giveCountConfirm">
            </td>
        </tr>
        <tr>
            <td style="text-align: right;">状态：</td>
            <td style="text-align: left;color:red;" id="stateConfirm">
            </td>
        </tr>
        <tr>
            <td style="text-align: right;">权重：</td>
            <td style="text-align: left;color:red;" id="weightConfirm">
            </td>
        </tr>
    </table>
</div>
<div id="confirm-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsert()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#confirm_dlg').dialog('close')">取消</a>
</div>

</body>
</html>
