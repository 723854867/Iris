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
    <title>用户充值明细查询</title>
    <script type='text/javascript' src='js/admin/query_pay_record.js'></script>
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
        <h2 style="float:left;padding-left:10px;margin: 1px">用户充值明细查询</h2>
    </div>
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <select name="paramType" id="paramType" class="easyui-combobox">
                        <option value="0">请选择</option>
                        <option value="1">手机号</option>
                        <option value="2">用户名</option>
                        <option value="3">用户ID</option>
                        <c:if test="${false }">
                        <option value="4">流水号</option>
                        <option value="5">充值渠道</option>
                        </c:if>
                    </select>
                    <input class="easyui-textbox" style="width:150px" name="param" id="param" value=""/>
                    
                    <%--&nbsp;&nbsp;&nbsp;&nbsp;支付渠道：--%>
                    <%--<select name="channel" id="channel" class="easyui-combobox">--%>
                        <%--<option value="">全部</option>--%>
                        <%--<option value="wx">微信</option>--%>
                        <%--<option value="wx_pub">H5微信</option>--%>
                        <%--<option value="alipay">支付宝</option>--%>
                        <%--<option value="app_store">苹果支付</option>--%>
                        <%--<option value="exchange">兑换</option>--%>
                        <%--<option value="live">首播赠送</option>--%>
                        <%--<option value="share">分享赠送</option>--%>
                    <%--</select>--%>
                    
                    &nbsp;&nbsp;&nbsp;&nbsp;是否赠送
                    <select name="isGive" id="isGive" class="easyui-combobox">
                        <option value="">全部</option>
                        <option value="1">有</option>
                        <option value="2">无</option>
                    </select>
                    
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <select name="chargeType" id="chargeType" class="easyui-combobox">
                        <option value="0">付款状态</option>
                        <option value="1">待付款</option>
                        <option value="2">充值成功</option>
                    </select>

                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    时间：
                    <input class="easyui-datetimebox" data-options="showSeconds:true" style="width:150px" name="start" id="start" value=""/>
                    - <input class="easyui-datetimebox" data-options="showSeconds:true" style="width:150px" name="end" id="end" value=""/>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    版本
                    <select class="easyui-combobox" name="appVersion" id="appVersion">
                        <option value="">全部</option>
                        <c:forEach items="${appVersionList }" var="v" >
                            <option value="${v }">${v }</option>
                        </c:forEach>
                    </select>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    平台
                    <select class="easyui-combobox" name="platform" id="platform">
                        <option value="">全部</option>
                        <option value="android">android</option>
                        <option value="ios">ios</option>
                    </select>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    渠道
                    <select class="easyui-combobox" name="source" id="source">
                        <option value="">全部</option>
                        <c:forEach items="${platformList }" var="v" >
                            <option value="${v }">${v }</option>
                        </c:forEach>
                    </select>
                </td>

            </tr>
            <tr>
                <td class="left">
                    支付渠道：
                    <input name="gifts" type="checkbox" checked="checked" value="wx">微信</input>
                    <input name="gifts" type="checkbox" checked="checked" value="wx_pub">H5微信</input>
                    <input name="gifts" type="checkbox" checked="checked" value="alipay">支付宝</input>
                    <input name="gifts" type="checkbox" checked="checked" value="app_store">苹果支付</input>
                    <input name="gifts" type="checkbox" checked="checked" value="99bill">PC_快钱</input>
                    <input name="gifts" type="checkbox" checked="checked" value="exchange">兑换</input>
                    <input name="gifts" type="checkbox" checked="checked" value="live">首播赠送</input>
                    <input name="gifts" type="checkbox" checked="checked" value="share">分享赠送</input>
                    <input name="gifts" type="checkbox" checked="checked" value="register_360">360渠道赠送</input>
                    <input name="gifts" type="checkbox" checked="checked" value="yyb">应用宝</input>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                    <c:if test="${true }">
                        <a href="javascript:;" onclick="exportExcel()" class="easyui-linkbutton"
                           iconCls="icon-print">导出Excel</a>
                    </c:if>
                </td>
            </tr>
            <tr><td>
                &nbsp;&nbsp;&nbsp;&nbsp;充值总人数：<span id="usersSum" ></span>
                &nbsp;&nbsp;&nbsp;&nbsp;充值总金额：<span id="amountSum" ></span>
                &nbsp;&nbsp;&nbsp;&nbsp;收到总金币数：<span id="receivedSum" ></span>
                &nbsp;&nbsp;&nbsp;&nbsp;充值赠送总金币:<span id="extraSum" ></span>
                &nbsp;&nbsp;&nbsp;&nbsp;充值赠送总金额（元）:<span id="extraMSum" ></span>
            </td></tr>
        </table>
    </div>
</div>

<!-- 主播结算历史记录查询弹出框 start -->
<div id="settlement_record_dlg" class="easyui-dialog" style="width:600px;height:600px;" closed="true"
     buttons="#settlement-record-dlg-buttons">
    <div id="dataGridToolbarSettlementRecord">
        <table>
            <tr>
                <td>
                    操作时间：
                </td>
                <td>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="startDate"
                           id="queryStartDate"> 至
                </td>
                <td>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="endDate"
                           id="queryEndDate">
                    <input type="hidden" value="" name="id" id="id">
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearchSettlementRecord()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
            <tr>
                <td>
                    <a href="javascript:;" onclick="exportSettlementRecord()" class="easyui-linkbutton"
                       iconCls="icon-print">导出至Excel</a>
                </td>
            </tr>
        </table>
    </div>
    <table class="table-doc" id="displaySettlementRecordTable" cellspacing="0" width="100%"></table>
</div>
<!-- 主播结算历史记录查询弹出框 end -->

</body>
</html>
