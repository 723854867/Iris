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
  <title>违规图片管理</title>
  <script type='text/javascript' src='js/admin/query_irregularity_image.js'></script>
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
    <h2 style="float:left;padding-left:10px;margin: 1px">违规图片管理</h2>
  </div>
</div>
<!-- 添加礼物弹出框 end -->
<c:import url="/main/common.jsp"/>

<!-- 弹出的提示对话框 -->
<div id="dlg" class="easyui-dialog" style="width:390px;height:250px;padding:10px 20px" closed="true"
     buttons="#dlg_buttons">
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
        <form action="" id="searchForm2">
            <input type="hidden" id="roomId" name="roomId">
            <input type="hidden" id="streamId" name="streamId">
            <div>
                禁播时间(分)：<input class="easyui-validatebox easyui-numberbox" id="expireMin" name="expireMin" data-options="validType:'int',min:0" required="true" value="" >
                <br/>
                禁播原因：<br/>
                <input type="radio" name="reason" onclick="$('#message').val('直播时衣冠不整或过于暴露')"> 直播时衣冠不整或过于暴露<br/>
                <input type="radio" name="reason" onclick="$('#message').val('进行脱衣舞等具有挑逗性的表演')"> 进行脱衣舞等具有挑逗性的表演<br/>
                <input type="radio" name="reason"  onclick="$('#message').val('艺人长时间不在画面中')"> 艺人长时间不在画面中<br/>
                <input class="easyui-validatebox" data-options="lenght:20" id="message" name="message" required="true">
            </div>
        </form>
    </div>

</div>
<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg_buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="offline();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

</body>
</html>
