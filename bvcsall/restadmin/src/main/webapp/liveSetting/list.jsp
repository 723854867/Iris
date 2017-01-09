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
  <title>直播设置</title>
  <script type='text/javascript' src='js/admin/liveSetting_list.js'></script>
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
    <h2 style="float:left;padding-left:10px;margin: 1px">直播设置</h2>
  </div>

   <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="settingLiveNoticeDlg()">添加</a>
   <a href="liveSetting/showAutoChat" class="easyui-linkbutton">语句管理</a>
   <a href="javascript:void(0)" class="easyui-linkbutton" onclick="commenDlg()">通用设置</a>
   <a href="javascript:void(0)" class="easyui-linkbutton" onclick="topDlg()">置顶白名单</a>
</div>
<!-- 添加礼物弹出框 end -->
<c:import url="/main/common.jsp"/>
<!-- 弹出的选择用户对话框 -->
<div id="dlg" class="easyui-dialog" style="width:500px;height:300px;padding:10px 20px" data-options="modal: true,cache:false" closed="true"
     buttons="#dlg_buttons">
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
        <form action="" id="editForm">
        	<input type="hidden" id="settingId" name="id">
            <table class="table-doc">
		        <tr>
		            <td style="text-align: right;">名称：</td>
		            <td style="text-align: left;">
		            	<input class="easyui-validatebox easyui-textbox" id="usettingname" name="name" required="true" value="" >
		            </td>
		        </tr>
		        <tr>
		            <td style="text-align: right;">马甲人数：</td>
		            <td style="text-align: left;">
		            	<input class="easyui-validatebox easyui-numberbox" id="umajiaCount" name="majiaCount" data-options="validType:'int',min:1" required="true" value="" >
		            	<input type="checkbox" id="umaxMajiaCount" value="-1"> 不限
		            </td>
		        </tr>
		        <tr>
		            <td style="text-align: right;">进入间隔：</td>
		            <td style="text-align: left;">
		            	<input class="easyui-validatebox easyui-numberbox" id="umajiaPeriod" name="majiaPeriod" data-options="validType:'int',min:1" required="true" value="" >
		            	-<input class="easyui-validatebox easyui-numberbox" id="umaxMajiaPeriod" name="maxMajiaPeriod" data-options="validType:'int',min:$('#umajiaPeriod').val()" required="true" value="" >(秒)
		            </td>
		        </tr>
		        <tr>
		            <td style="text-align: right;">类型：</td>
		            <td style="text-align: left;">
		            	<select id="utypeId" name="typeId">
		            		<option value="0" selected="selected">无</option>
		            		<c:forEach items="${types}" var="type">
				  				<c:if test="${type.status==0}">
				  					<option value="${type.id}">${type.name}</option>
				  				</c:if>
				  			</c:forEach>
		            	</select>
		            </td>
		        </tr>
		        <tr>
		            <td style="text-align: right;">类型：</td>
		            <td style="text-align: left;">
		            	<select id="ustatus" name="status">
		            		<option value="0">生效</option>
		            		<option value="1">失效</option>
		            	</select>
		            </td>
		        </tr>
		    </table>
        </form>
    </div>

</div>


<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg_buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="editSetting();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 公告弹出框 start -->
<div id="dlgNotice" class="easyui-dialog" style="width:450px;height:300px;" closed="true" buttons="#adddlgButton">
	<form action="" id="addForm">
	    <table class="table-doc">
	        <tr>
	            <td style="text-align: right;">名称：</td>
	            <td style="text-align: left;">
	            	<input class="easyui-validatebox easyui-textbox" id="settingname" name="name" required="true" value="" >
	            </td>
	        </tr>
	        <tr>
	            <td style="text-align: right;">马甲人数：</td>
	            <td style="text-align: left;">
	            	<input class="easyui-validatebox easyui-numberbox" id="majiaCount" name="majiaCount" data-options="validType:'int',min:1" required="true" value="" >
	            	<input type="checkbox" id="maxMajiaCount" value="-1"> 不限
	            </td>
	        </tr>
	        <tr>
	            <td style="text-align: right;">进入间隔：</td>
	            <td style="text-align: left;">
	            	<input class="easyui-validatebox easyui-numberbox" id="majiaPeriod" name="majiaPeriod" data-options="validType:'int',min:1" required="true" value="" >
					-<input class="easyui-validatebox easyui-numberbox" id="maxMajiaPeriod" name="maxMajiaPeriod" data-options="validType:'int',min:$('#umajiaPeriod').val()" required="true" value="" >(秒)
	            </td>
	        </tr>
	        <tr>
	            <td style="text-align: right;">类型：</td>
	            <td style="text-align: left;">
	            	<select id="typeId" name="typeId">
	            		<option value="0" selected="selected">无</option>
	            		<c:forEach items="${types}" var="type">
			  				<c:if test="${type.status==0}">
			  					<option value="${type.id}">${type.name}</option>
			  				</c:if>
			  			</c:forEach>
	            	</select>
	            </td>
	        </tr>
	    </table>
    </form>
</div>
<div id="adddlgButton" style="text-align: center;margin-top: 5px;">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveLiveSetting()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlgNotice').dialog('close')">取消</a>
</div>

<div id="userBoundDlg" class="easyui-dialog" style="width:500px;height:600px;" data-options="modal: true,cache:false" closed="true" buttons="#user_button">
	<table id="userTable" data-options="border:false,toolbar:'userToolbar'" style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
	</table>
	<div id="userToolbar" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
	  <div style="height: 40px; padding-top: 5px; overflow: hidden;">
	  	<label>添加用户</label><input class="easyui-numberbox" id="userId" data-options="validType:'int',min:1" value=""> <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="addUser()">添加</a>
	  </div>
	
	</div>
</div>

<div id="user_button" style="text-align: right;margin-top: 5px;">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="removeUsers()">移除</a>
</div>

<!-- 自动加人数设置 -->
<div id="userCountNotice" class="easyui-dialog" style="width:450px;height:300px;" closed="true" buttons="#addUserCountButton">
	<form action="" id="userCountForm">
	    <table class="table-doc">
	        <tr>
	            <td style="text-align: right;">起始马甲数：</td>
	            <td style="text-align: left;">
	            	<input class="easyui-validatebox easyui-numberbox" id="majiaCountBegin" name="majiaCountBegin" data-options="validType:'int',min:1" required="true" value="" >
	            </td>
	        </tr>
	        <tr>
	            <td style="text-align: right;">人数上限：</td>
	            <td style="text-align: left;">
	            	<input class="easyui-validatebox easyui-numberbox" id="maxUserCount" name="maxUserCount" data-options="validType:'int',min:1" required="true" value="" >
	            </td>
	        </tr>
	         <tr>
	            <td style="text-align: right;">每次添加数：</td>
	            <td style="text-align: left;">
	            	<input class="easyui-validatebox easyui-numberbox" id="userCountStep" name="userCountStep" data-options="validType:'int',min:1" required="true" value="" >
	            </td>
	        </tr>
	        <tr>
	            <td style="text-align: right;">进入间隔：</td>
	            <td style="text-align: left;">
	            	<input class="easyui-validatebox easyui-numberbox" id="userCountPeriod" name="userCountPeriod" data-options="validType:'int',min:1" required="true" value="" >
					-<input class="easyui-validatebox easyui-numberbox" id="maxUserCountPeriod" name="maxUserCountPeriod" data-options="validType:'int',min:$('#umajiaPeriod').val()" required="true" value="" >(秒)
	            </td>
	        </tr>
	        <tr>
	            <td style="text-align: right;">状态：</td>
	            <td style="text-align: left;">
	            	<select id="userCountStat" name="userCountStat">
	            		<option value="0" selected="selected">开启</option>
	            		<option value="1">关闭</option>
	            	</select>
	            </td>
	        </tr>
	    </table>
    </form>
</div>

<div id="addUserCountButton" style="text-align: center;margin-top: 5px;">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="userCountSetting()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#userCountNotice').dialog('close')">取消</a>
</div>

<!-- 通用设置 -->
<div id="commenDlg" class="easyui-dialog" style="width:650px;height:500px;" closed="true" buttons="#commenButton">
	<form action="" id="commenForm">
		<div style="padding-top: 10px;padding-bottom: 10px;padding-left:5px">
			<label>真实用户带机器人，机器人数量根据真实用户计算</label>
		</div>
		<div style="padding-top: 10px;padding-bottom: 10px;padding-left:5px">
			<label><strong>阶梯一： </strong></label><label>真实用户小于</label><input class="easyui-validatebox easyui-numberbox" id="minReal" name="minRealUser" data-options="validType:'int',min:0" required="true" value="" >
			<label>机器人数量=</label><input class="easyui-validatebox easyui-numberbox" id="minMajia" name="minMajiaCount" data-options="validType:'int',min:0" required="true" value="" >
		</div>
		<div style="padding-top: 10px;padding-bottom: 10px;padding-left:5px">
			<label><strong>阶梯二： </strong><label>真实用户大于或等于阶梯一，小于或等于阶梯三</label>
			<br><label>机器人数量= 阶梯一机器人数+阶梯二实际用户 X</label><input class="easyui-validatebox easyui-numberbox" id="step" name="step" data-options="validType:'int',min:1" required="true" value="" >
		</div>
		<div style="padding-top: 10px;padding-bottom: 10px;padding-left:5px">
			<label><strong>阶梯三： </strong><label>真实用户大于</label><input class="easyui-validatebox easyui-numberbox" id="maxReal" name="maxRealUser" data-options="validType:'int',min:1" required="true" value="" >
			<br><label>机器人数量= 阶梯一机器人数+阶梯二机器人数+阶梯三实际用户 X</label><input class="easyui-validatebox easyui-numberbox" id="maxStep" name="maxStep" data-options="validType:'int',min:1" required="true" value="" >
		</div>
	    <div style="padding-top: 10px;padding-bottom: 10px;padding-left:5px">
	    	<label>间隔：</label><input class="easyui-validatebox easyui-numberbox" id="commenPeriod" name="period" data-options="validType:'int',min:1" required="true" value="" >
					-<input class="easyui-validatebox easyui-numberbox" id="maxCommenPeriod" name="maxPeriod" data-options="validType:'int',min:$('#umajiaPeriod').val()" required="true" value="" >(秒)
	   </div>       
	   <div style="padding-top: 10px;padding-bottom: 10px;padding-left:5px">
	        <label>状态：</label>
           	<select id="commenStat" name="commenStat">
           		<option value="0" selected="selected">开启</option>
           		<option value="1">关闭</option>
           	</select>
	   </div>   
	   <div style="padding-top: 20px;padding-bottom: 10px;padding-left:5px">
			<label><strong>阶梯二实际用户数=阶梯二实际真实用户数-阶梯一实际真实用户数</strong></label><br>
			<label><strong>阶梯三实际用户数=阶梯三实际真实用户数-阶梯二实际真实用户数</strong></label>
		</div>
    </form>
</div>

<div id="commenButton" style="text-align: center;margin-top: 5px;">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="commenSetting()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#commenDlg').dialog('close')">取消</a>
</div>
<!-- 置顶白名单 -->
<div id="topDlg" class="easyui-dialog" style="width:500px;height:600px;" data-options="modal: true,cache:false" closed="true" buttons="#topButton">
	<table id="topTable" data-options="border:false,toolbar:'topToolbar'" style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
	</table>
	<div id="topToolbar" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
	  <div style="height: 40px; padding-top: 5px; overflow: hidden;">
	  	<label>添加用户</label><input class="easyui-numberbox" id="topUserId" data-options="validType:'int',min:1" value=""> <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="addTopUser()">添加</a>
	  </div>
	
	</div>
</div>

<div id="topButton" style="text-align: right;margin-top: 5px;">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="removeTopUsers()">移除</a>
</div>
</body>
</html>
