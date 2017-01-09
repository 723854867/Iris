<%--
  Created by IntelliJ IDEA.
  User: huoshanwei
  Date: 2016/4/13
  Time: 14:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>主播提现记录</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_exchange_record.js"></script>
</head>
<body>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">主播提现记录</h2>
    </div>
<%--    <div>
        <span>
            <label>结算时间：</label>
            <span>

                <input type="text" class="easyui-datebox" style="width: 120px;" name="startDate" id="startDate">
                &nbsp;至&nbsp;
                <input type="text" class="easyui-datebox" style="width: 120px;" name="endDate" id="endDate">
            </span>
         </span>
        <a href="javascript:;" onclick="exportExcel()" class="easyui-linkbutton" iconCls="icon-print" plain="true">导出Excel</a>
    </div>--%>
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <label>
                        <select class="easyui-combobox" name="userKey" id="userKey">
                            <option value="">用户搜索</option>
                            <option value="1">用户ID</option>
                            <option value="2">用户名</option>
                            <option value="3">昵称</option>
                            <option value="4">手机号码</option>
                        </select>
                    </label>
                </td>
                <td>
                    <input type="text" name="userKeyword" id="userKeyword" class="easyui-validatebox">
                </td>
                <td>
                <label>机构：</label>
                </td>
                <td>
                <select class="easyui-combobox" name="organizationId" id="organizationId"
                        data-options="url:'comboBox/getOrganizationCombobox',method: 'get',valueField:'id',textField:'text'"/>
                </td>
                <td>
                    <label>结算时间：</label>
                </td>
                <td>
                <input type="text" class="easyui-datetimebox" style="width: 120px;" name="startDate" id="startDate" >
                &nbsp;至&nbsp;
                <input type="text" class="easyui-datetimebox" style="width: 120px;" name="endDate" id="endDate">
                </td>
                <td>
                    <label>状态：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="status" id="status">
                        <option value="">请选择</option>
                        <option value="paid">成功</option>
                        <option value="pending">结算中</option>
                        <option value="failed">失败</option>
                    </select>
                </td>
                <td>
                    <label>类型：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="channel" id="channel">
                        <option value="">请选择</option>
                        <option value="wx_pub">提现</option>
                        <option value="exchange">兑换</option>
                        <option value="org">机构</option>
                    </select>
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                    <a href="javascript:;" onclick="exportExcel()" class="easyui-linkbutton" iconCls="icon-print" plain="true">导出Excel</a>
                </td>
            </tr>
        </table>
    </div>
    <div>
        <span>
            <label>金豆数：</label>
            <span id="goldCoinCount">0个</span>
            <label>现金：</label>
            <span id="cashCount">0元</span>
            <label>提现人数：</label>
            <span id="personCount">0人</span>
        </span>
    </div>
</div>


</body>
</html>

