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
    <title>直播申请管理</title>
    <script type='text/javascript' src='js/admin/query_anchor_auditing.js'></script>
    <style>
        #detail_dlg ul {
            list-style: none;
            margin: 15px;
        }
        #detail_dlg ul li {
            float: left;
            margin-left: 5px;
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
        <h2 style="float:left;padding-left:10px;margin: 1px">直播申请管理</h2>
    </div>
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <label>状态：</label>
                </td>
                <td>
                    <select id="status" name="status" class="easyui-combobox">
                        <option value="">全部</option>
                        <option value="0" selected="selected">待审核</option>
                        <option value="1">已通过</option>
                        <option value="-1">未通过</option>
                    </select>
                </td>
                <td>
                    <label>手机号码：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:150px" name="phone" id="phone" value=""/>
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
</div>
<!-- 审核不通过弹出框 start -->
<div id="reject_dlg" class="easyui-dialog" style="width:400px;height:400px;" closed="true">
    <h4 style="margin-left: 15px;">请选择审核不通过原因：</h4>

    <form id="rejectForm" method="post">
        <table id="reject" class="table-doc" style="width: 100%;">
            <tr>
                <td style="text-align: left;border: none;">
                    <input type="checkbox" name="rejectReasonCK" onclick="checkRejectReason()" value="证件号码与证件信息不符！">证件号码与证件信息不符！
                </td>
            </tr>
            <tr>
                <td style="text-align: left;border: none;">
                    <input type="checkbox" name="rejectReasonCK" onclick="checkRejectReason()" value="真实姓名与证件信息不符！">真实姓名与证件信息不符！
                </td>
            </tr>
            <tr>
                <td style="text-align: left;border: none;">
                    <input type="checkbox" name="rejectReasonCK" onclick="checkRejectReason()" value="性别信息与证件信息不符！">性别信息与证件信息不符！
                </td>
            </tr>
            <tr>
                <td style="text-align: left;border: none;">
                    其它原因：<input type="text" name="rejectReason" class="easyui-validatebox" required="true" min="1"
                                max="120" id="rejectReason" style="width: 200px;" value="">
                    <input type="hidden" name="id" id="id" value="">
                </td>
            </tr>
            <tr>
                <td style="border: none;">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
                       onclick="rejectAnchorLiving()">确定</a>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
                       onclick="javascript:$('#reject_dlg').dialog('close')">取消</a>
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- 审核不通过弹出框 end -->
<!-- 详情弹出框 start -->
<div id="detail_dlg" class="easyui-dialog" style="width:800px;height:500px;" closed="true">
    <ul>
        <li data-transition="slide">
            <span id="pic1_prompt"></span>
            <img id="pic1" src="" data-thumb="" data-thumb_bw="" alt="" style="width: 300px;"/>
        </li>
        <li data-transition="fade">
            <span id="pic2_prompt"></span>
            <img id="pic2" src="" data-thumb="" data-thumb_bw="" alt="" style="width: 300px;"/>
        </li>
        <li data-transition="fade">
            <span id="pic3_prompt"></span>
            <img id="pic3" src="" data-thumb="" data-thumb_bw="" alt="" style="width: 300px;"/>
        </li>
    </ul>
</div>
<!-- 详情弹出框 end -->
<c:import url="/main/common.jsp"/>
</body>
</html>
