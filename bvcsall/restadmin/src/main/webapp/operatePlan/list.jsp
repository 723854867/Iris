<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    %>
    <title>运营计划管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="<%=basePath%>js/datepicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/datepicker/accessDatePicker.js"></script>
    <script type="text/javascript" src="js/admin/query_operate_plan.js"></script>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">运营计划管理</h2>
    </div>
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:newOperatePlan()"
           plain="true">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
           onclick="javascript:editOperatePlan()"
           plain="true">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
           onclick="javascript:destroyOperatePlan()" plain="true">删除</a>
    </div>
</div>

<!-- 弹出的添加或者编辑对话框 -->
<div id="dlg" class="easyui-dialog" style="width:500px;height:300px;padding:2px 5px" closed="true"
     buttons="#dlg-buttons">
    <!-- 添加 -->
    <form id="fm" action="operatePlan/add" method="post">
        <table width="100%" class="table-doc">
            <tr>
                <td><label>计划类型:</label></td>
                <td>
                    <input name="planType" class="easyui-validatebox" required="true">
                </td>
            </tr>
            <tr>
                <td><label>计划数值:</label></td>
                <td>
                    <input name="targetNum" class="easyui-validatebox easyui-numberbox" required="true">
                </td>
            </tr>
            <tr>
                <td><label>计划时间:</label></td>
                <td>
                    <input id="startDate" name="startDate" type="text" size="20"
                           onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',realDateFmt:'yyyy-MM-dd',readOnly:true,minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'endDate\')}',startDate:'%y-%M-%d',onpicked:function(){startDatePicked('startDate','endDate');}})"
                           class="Wdate" readonly="readonly" required="true"/>&nbsp;至&nbsp;
                    <!-- 结束时间 -->
                    <input id="endDate" name="endDate" type="text" size="20"
                           onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',realDateFmt:'yyyy-MM-dd',readOnly:true,minDate:'#F{$dp.$D(\'startDate\')}',startDate:'#F{$dp.$D(\'startDate\')||\'%y-%M-%d\'}',onpicked:function(){onDateSelected('startDate','endDate');}})"
                           class="Wdate" readonly="readonly" required="true"/>
                </td>
            </tr>
            <tr>
                <td><label>时间单位:</label></td>
                <td>
                    周 <input name="timeUnit" type="radio" value="week" required="true">&nbsp;
                    月 <input name="timeUnit" type="radio" value="month" required="true">
                </td>
            </tr>
        </table>
    </form>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveOperatePlan('#dlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<div id="updatedlg" class="easyui-dialog" style="width:470px;height:300px;padding:2px 5px" closed="true"
     buttons="#updatedlg-buttons">
    <!-- 修改 -->
    <form id="updatefm" action="videoUploader/updatepage" method="post" novalidate enctype="multipart/form-data">
        <table width="100%" class="table-doc">
            <tr>
                <td><label>计划类型:</label></td>
                <td><input name="planType" class="easyui-validatebox" required="true"></td>
            </tr>
            <tr>
                <td><label>计划数值:</label></td>
                <td><input name="targetNum" class="easyui-validatebox easyui-numberbox" required="true"></td>
            </tr>
            <tr>
                <td><label>计划时间:</label></td>
                <td>
                    <input id="ustartDate" name="startDate" type="text" size="20"
                           onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',realDateFmt:'yyyy-MM-dd',readOnly:true,minDate:'%y-%M-%d',maxDate:'#F{$dp.$D(\'uendDate\')}',startDate:'%y-%M-%d',onpicked:function(){startDatePicked('ustartDate','uendDate');}})"
                           class="Wdate" readonly="readonly"/>&nbsp;至&nbsp;
                    <!-- 结束时间 -->
                    <input id="uendDate" name="endDate" type="text" size="20"
                           onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',realDateFmt:'yyyy-MM-dd',readOnly:true,minDate:'#F{$dp.$D(\'ustartDate\')}',startDate:'#F{$dp.$D(\'ustartDate\')||\'%y-%M-%d\'}',onpicked:function(){onDateSelected('ustartDate','uendDate');}})"
                           class="Wdate" readonly="readonly"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label>时间单位:</label></td>
                <td>
                    周 <input name="timeUnit" type="radio" value="week" required="true">&nbsp;
                    月 <input name="timeUnit" type="radio" value="month" required="true">
                    <input type="hidden" name="id"/>
                </td>
            </tr>
        </table>
    </form>
</div>

<!-- 编辑对话框里的保存和取消按钮 -->
<div id="updatedlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveOperatePlan('#updatedlg','#updatefm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
</div>

</body>
</html>
