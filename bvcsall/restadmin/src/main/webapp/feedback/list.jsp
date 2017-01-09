<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html style="background: white">
<head>
    <title>用户反馈列表</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type='text/javascript' src='js/admin/query_feedback.js'></script>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">用户反馈</h2>
    </div>
    <div>
        <table>
            <tr>
                <td><label>内容：</label></td>
                <td>
                    <input type="text" class="textbox" id="content" value="" style="padding:2px;">
                </td>
                <td><label>来源：</label></td>
                <td>
                    <select id="dataFrom" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="ios">IOS</option>
                        <option value="Android">Android</option>
                        <option value="www">PC</option>
                    </select>
                </td>
                <td><label>反馈时间：</label></td>
                <td>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="startTime" id="startTime">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="endTime" id="endTime">
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="sendMailDialog()"
           plain="true">mail负责人</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-print" onclick="exportFeedBackToExcel()"
           plain="true">导出到Excel</a>
    </div>
</div>

<!--等级状态修改弹出框start-->
<div id="dialog" style="padding:5px;width:600px;height:600px;display:none;"
     title="快捷编辑" toolbar="#dlg-toolbar" buttons="#dlg-buttons">
    <table class="table-doc" width="100%">
        <tr>
            <td>
                <label>常用联系人：</label>
            </td>
            <td height="50" id="email_list">
                <c:forEach items="${emailList}" var="vo">
                    <div>
                        <input type="checkbox" name="email" value="${vo.email}">${vo.email}
                    </div>
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td>
                <label>负责人email：</label>
            </td>
            <td>
                <input type="text" class="textbox" value="" id="new_email">
                <input type="hidden" class="textbox" value="" id="id">
            </td>
        </tr>
    </table>
</div>
<div id="dlg-buttons">
    <a href="javascript:;" class="easyui-linkbutton" id="update-ok" iconCls="icon-ok">发送邮件</a>
    <a href="javascript:;" onclick="closeDialog()" class="easyui-linkbutton" id="update-cancel"
       iconCls="icon-cancel">取消</a>
</div>
<!--等级状态修改弹出框end-->

</body>
</html>
