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
  <title>直播管理</title>
  <script type='text/javascript' src='js/admin/query_living_list.js'></script>
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
    <h2 style="float:left;padding-left:10px;margin: 1px">直播管理</h2>
  </div>
<%--  <div id="search_form">
    <table>
      <tr>
        <td>
          <label>用户名/ID：</label>
        </td>
        <td>
          <input class="easyui-textbox" style="width:150px" name="key" id="key" value=""/>
        </td>
        <td>
          <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
             iconCls="icon-search">搜索</a>
        </td>
      </tr>
    </table>
  </div>--%>
   <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="settingLiveNoticeDlg()">设置直播公告</a>
</div>
<!-- 添加礼物弹出框 end -->
<c:import url="/main/common.jsp"/>
<!-- 弹出的选择用户对话框 -->
<div id="dlg" class="easyui-dialog" style="width:420px;height:420px;padding:10px 20px" closed="true"
     buttons="#dlg_buttons">
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
        <form action="" id="searchForm2">
        	<input type="hidden" id="roomId" name="roomId">
            <div>
               	下线时间(分)：<input class="easyui-validatebox easyui-numberbox" id="expireMin" name="expireMin" data-options="validType:'int',min:0" required="true" value="" >
				<br/>
                下线原因：<br/>
                <input type="radio" name="reason" onclick="$('#message').val('色情、裸露尺度过大')"> 色情、裸露尺度过大<br/>
                <input type="radio" name="reason" onclick="$('#message').val('内容低俗')"> 内容低俗<br/>
                <input type="radio" name="reason" onclick="$('#message').val('宣传其他平台')"> 宣传其他平台<br/>
                <input type="radio" name="reason" onclick="$('#message').val('枪支、毒品、赌场')"> 枪支、毒品、赌场<br/>
                <input type="radio" name="reason" onclick="$('#message').val('直播吸烟')"> 直播吸烟<br/>
                <input type="radio" name="reason" onclick="$('#message').val('暴力、血腥、恐怖内容')"> 暴力、血腥、恐怖内容<br/>
                <input type="radio" name="reason" onclick="$('#message').val('麻将、扑克类游戏')"> 麻将、扑克类游戏<br/>
                <input type="radio" name="reason" onclick="$('#message').val('拍摄、转播其他直播平台')"> 拍摄、转播其他直播平台<br/>
                <input type="radio" name="reason" onclick="$('#message').val('服装涉及国会、党徽、军徽、警徽')">
                服装涉及国会、党徽、军徽、警徽<br/>
                <input type="radio" name="reason" onclick="$('#message').val('未成年直播')"> 未成年直播<br/>
                <input type="radio" name="reason" onclick="$('#message').val('危险驾驶')"> 危险驾驶<br/>
                <input type="radio" name="reason" onclick="$('#message').val('涉及政治、宗教内容')"> 涉及政治、宗教内容<br/>
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

<!-- 公告弹出框 start -->
<div id="dlgNotice" class="easyui-dialog" style="width:450px;height:300px;" closed="true">
    <table class="table-doc">
        <tr>
            <td style="text-align: right;">公告内容：</td>
            <td style="text-align: left;">
                <textarea rows="6" cols="50" style="font-size: 12px;" id="content">${liveNotice}</textarea>
            </td>
        </tr>
        <tr>
            <td style="text-align: right;">循环内容：</td>
            <td style="text-align: left;">
                <textarea rows="3" cols="50" style="font-size: 12px;" id="loopNotice">${loopNotice}</textarea>
            </td>
        </tr>
    </table>
    <div style="text-align: center;margin-top: 5px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
           onclick="settingLiveNotice()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="javascript:$('#dlgNotice').dialog('close')">取消</a>
    </div>
</div>
</body>
</html>
