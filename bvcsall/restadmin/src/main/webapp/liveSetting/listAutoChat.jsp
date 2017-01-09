<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
  <title>直播设置</title>
  <script type='text/javascript' src='js/admin/liveSetting_ac.js'></script>
</head>
<body>
<!-- 列表 -->
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;height:auto;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false" style="height:auto;border-bottom: 1px solid #ddd; padding: 2px 5px;">
  <div data-options="region:'north',border:false"
       style="height: 40px; padding-top: 5px; overflow: hidden;">
    <h2 style="float:left;padding-left:10px;margin: 1px">直播设置</h2>
  </div>

	<label>用户ID:</label><input class="easyui-validatebox easyui-numberbox" id="uid1" data-options="validType:'int',min:1">
	<label>类型:</label>
	<select id="type1">
		<option value="0">无</option>
		<c:forEach items="${types}" var="type">
			<c:if test="${type.status == 0}">
				<option value="${type.id}">${type.name}</option>
			</c:if>
		</c:forEach>
	</select>
	<label>语句:</label><input class="easyui-validatebox easyui-textbox" id="words1"> 
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="doSearch();">查询</a>
  	<br>
	<label>用户ID:</label><input class="easyui-validatebox easyui-numberbox" id="uid2" data-options="validType:'int',min:1">
	<label>类型:</label>
	<select id="type2">
		<option value="0">无</option>
		<c:forEach items="${types}" var="type">
			<c:if test="${type.status==0}">
				<option value="${type.id}">${type.name}</option>
			</c:if>
		</c:forEach>
	</select><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="editTypeDlg();">编辑</a>
	<label>语句:</label><input class="easyui-validatebox easyui-textbox" id="words2"> 
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="addAutoChat();">添加</a>
  	
</div>

<c:import url="/main/common.jsp"/>

<div id="editTypeDlg" class="easyui-dialog" style="width:500px;height:600px;" data-options="modal: true,cache:false" closed="true">
	<div id="typeToolbar" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
	  <div style="height: 40px; padding-top: 5px; overflow: hidden;">
	  	<label>添加类型</label><input class="easyui-textbox" id="newType" required="true">
	  	 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="addType()">添加</a>
	  </div>
	  
	  <table id="addTypeTable" data-options="border:false" style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
		<thead>
			<tr>
				<th>ID</th>
				<th>分类名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${types}" var="type">
				<tr>
					<td>${type.id}</td>
					<td>${type.name}</td>
					<td>
						<c:if test="${type.status==0}">
							<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="editType(${type.id},1)">生效</a>
						</c:if>
						<c:if test="${type.status==1}">
							<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="editType(${type.id},0)">失效</a>
						</c:if>
					</td>
				</tr>
				<td>
				<option value=""></option>
			</c:forEach>
		</tbody>
		
	</table>
	</div>
</div>

</body>
</html>
