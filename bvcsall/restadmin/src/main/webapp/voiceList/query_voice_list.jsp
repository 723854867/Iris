<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2016/8/2
  Time: 10:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>学员榜</title>
    <script type='text/javascript' src='js/admin/query_voice_list.js'></script>
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
            ${voiceListName}
                <fmt:formatDate value="${voiceListStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/> 至
                <fmt:formatDate value="${voiceListEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
        </h2>
    </div>
    <div>
        <a href="javascript:;" onclick="insertStudentDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加学员</a>
    </div>
</div>

<!-- 添加学员弹出框 start -->
<div id="insert_student_dlg" class="easyui-dialog" style="width:500px;height:150px;" closed="true"
     buttons="#insert-student-dlg-buttons">
    <form id="insertStudentForm" method="post">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">学员ID：</td>
                <td style="text-align: left;">
                    <input type="text" name="id" required="true" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-student-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertStudent()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_student_dlg').dialog('close')">取消</a>
</div>
<!-- 添加学员弹出框 end -->

<!-- 添加战队弹出框 start -->
<div id="corps_dialog" class="easyui-dialog" style="width:500px;height:150px;" closed="true"
     buttons="#corps_dialog-buttons">
    <form id="corpsDialogForm" method="post">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">战队：</td>
                <td style="text-align: left;">
                    <select name="tutorId" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="1">周杰伦</option>
                        <option value="2">汪峰</option>
                        <option value="3">那英</option>
                        <option value="4">庾澄庆</option>
                        <option value="5">王力宏</option>
                    </select>
                    <input type="hidden" id="studentId" name="id" value="">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="corps_dialog-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="settingCorps()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#corps_dialog').dialog('close')">取消</a>
</div>
<!-- 添加战队弹出框 end -->

<!--加人气弹出框开始-->
<div id="select_vote_dlg" class="easyui-dialog" style="width:700px;height:500px;" closed="true">
    <div id="dataGridVoteToolbar">
        <form id="cheatForm">
            <table>
                <tr>
                    <td>
                        时间:
                    </td>
                    <td>
                        <input type="text" name="createTime" id="createTime" style="width: 160px;" class="easyui-validatebox easyui-datetimebox">
                    </td>
                    <td>人气:</td>
                    <td>
                        <input type="text" name="popularity" id="popularity" class="easyui-validatebox">
                        <input type="hidden" name="userId" id="userId">
                    </td>
                    <td>
                        <a href="javascript:;" onclick="cheatPopularity()" class="easyui-linkbutton"
                           iconCls="icon-add">添加</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <table id="displayVoteTable" cellspacing="0" width="100%"></table>

</div>
<!--加人气弹出框结束-->

<c:import url="/main/common.jsp"/>
</body>
</html>
