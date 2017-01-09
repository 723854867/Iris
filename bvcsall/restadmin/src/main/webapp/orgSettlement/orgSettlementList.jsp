<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>机构结算</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type='text/javascript' src='js/admin/query_org_settlement.js'></script>
  <style type="text/css">
  .mytable
 {
     padding: 0;
     margin: 10px auto;
     border-collapse: collapse;
     font-family: @宋体;
     empty-cells: show;
 }
 
.mytable caption
 {
     font-size: 12px;
     color: #0E2D5F;
     height: 16px;
     line-height: 16px;
     border: 1px dashed red;
     empty-cells: show;
 }
 
.mytable tr th
 {
     border: 1px dashed #C1DAD7;
     letter-spacing: 2px;
     text-align: left;
     padding: 6px 6px 6px 12px;
     font-size: 13px;
     height: 16px;
     line-height: 16px;
     empty-cells: show;
 }
 
.mytable tr td
 {
     font-size: 12px;
     border: 1px dashed #C1DAD7;
     padding: 6px 6px 6px 12px;
     empty-cells: show;
     border-collapse: collapse;
 }
 .cursor
 {
     cursor: pointer;
 }
 .tdbackground
 {
     background-color: #FFE48D;
 }
  
  
  </style>  
    
    <script type="text/javascript">

var datagridId2="#tt2";
var searchFormId2="#searchForm2";

var pageSize=20;
var playId="";
var adddialogueId="#dlg";
	$(function () {
		
		//datagridList2();
		
		$('#settleMonth').combobox('yearandmonth')
	});
	
	
	function datagridList2(){
		 $(datagridId2).datagrid({
				fitColumns:true,
				fit:true,
				rownumbers : true,
			    pagination : true,
			    pageNumber : 1,
			    pageList : [pageSize,pageSize*2,pageSize*3],
			    pageSize : pageSize,
			    pagePosition : 'bottom',
			    selectOnCheck:true,
			    singleSelect: true,
			    url:'ruser/getNormalUsers',
			    columns:[[
						{field:'ck',width : 100,checkbox:true},
						{field:'name',title:'昵称',width : 100},
						{field:'username',title:'登录名',width : 100},
						{field:'phone',title:'手机号',width : 100}
						
				    ]],
			    onLoadSuccess:function(){
			    	$(this).datagrid('enableDnd');
			    }
		});
	}

	
	function doSearch2(){
		$(datagridId2).datagrid('reload',getFormJson( searchFormId2));
	}
	
	function selectUser(){
		var row = $(datagridId2).datagrid('getChecked');
// 		var ids=[];
// 		var inum=0;
// 		for(var r in row){
// 			ids.push(row[r]['id']);			
// 		}
		
		if(row.length>0){
			$("#userName").val(row[0]['name'])
			$("#userId").val(row[0]['id'])
			$("#user_dlg").dialog("close");
		}else{
			showMessage("Error","请选择用户");
		}
	}
	
	function deleteUser(){
		$("#userName").val('');
		$("#userId").val('');
		$("#user_dlg").dialog("close");
	}
	

	
	function showTargetDialog(){
		$("#user_dlg").show();
		$("#user_dlg").dialog({
			 title: '选择文件',
			 width: 800,
			 height: 650,
			 closed: false,
			 cache: true,
			 modal: true
	    });
	}
	
	function exportData(){
		var settleMonth = $("#settleMonth").combobox("getValue");;
	    var paramType = $("#paramType").combobox("getValue");
	    var param = $("#param").val();
	    var oId = $("#orgId").combobox("getValue");
	    window.location.href = "orgSettlement/findOrgSettlementListExport?settleMonth="+settleMonth+"&key="+paramType+"&value="+param+"&orgId="+oId;
	}
	
	
	
	
	$.extend($.fn.combobox.methods, {
	     yearandmonth: function (jq) {
	         return jq.each(function () {
	             var obj = $(this).combobox();
	             var date = new Date();
	             var year = date.getFullYear();
	             var month = date.getMonth() + 1;
	             var table = $('<table>');
	             var tr1 = $('<tr>');
	             var tr1td1 = $('<td>', {
	                 text: '-',
	                 click: function () {
	                     var y = $(this).next().html();
	                     y = parseInt(y) - 1;
	                     $(this).next().html(y);
	                 }
	             });
	             tr1td1.appendTo(tr1);
	             var tr1td2 = $('<td>', {
	                 text: year
	             }).appendTo(tr1);
	 
	            var tr1td3 = $('<td>', {
	                 text: '+',
	                 click: function () {
	                     var y = $(this).prev().html();
	                     y = parseInt(y) + 1;
	                     $(this).prev().html(y);
	                 }
	             }).appendTo(tr1);
	             tr1.appendTo(table);
	 
	            var n = 1;
	             for (var i = 1; i <= 4; i++) {
	                 var tr = $('<tr>');
	                 for (var m = 1; m <= 3; m++) {
	                     var td = $('<td>', {
	                         text: n,
	                         click: function () {
	                             var yyyy = $(table).find("tr:first>td:eq(1)").html();
	                             var cell = $(this).html();
	                             var v = yyyy + '-' + (cell.length < 2 ? '0' + cell : cell);
	                             obj.combobox('setValue', v).combobox('hidePanel');
	 
	                        }
	                     });
	                     if (n == month) {
	                         td.addClass('tdbackground');
	                     }
	                     td.appendTo(tr);
	                     n++;
	                 }
	                 tr.appendTo(table);
	             }
	             table.addClass('mytable cursor');
	             table.find('td').hover(function () {
	                 $(this).addClass('tdbackground');
	             }, function () {
	                 $(this).removeClass('tdbackground');
	             });
	             table.appendTo(obj.combobox("panel"));
	 
	        });
	     }
	 });
	
</script>
</head>
<body>
<!-- 列表 -->
<table id="tt" rules="rows" cellspacing="0" style="border: 1px solid;" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">机构结算</h2>
    </div>
    <div>
        <form action="" id="searchForm">
            <table>
            	<tr id="common_tr">
            		<c:if test="${false }">
					<td>
						<span>用户：</span>
					</td>
					<td style="text-align: left;">
						<input name="userName" id="userName" readonly="readonly" width="100"  value="" onfocus="showTargetDialog();">
					    <input name="userId" id="userId" class="" type="hidden"  value="" >
					</td>
					</c:if>
					
					<td>
						<select name="key" id="paramType" class="easyui-combobox">
	                        <option value="0">请选择</option>
	                        <option value="1">用户ID</option>
	                        <option value="2">用户名</option>
	                        <option value="3">手机号</option>
	                        
	                    </select>
					</td>
					<td style="text-align: left;">
						<input class="easyui-textbox" style="width:150px" name="value" id="param" value=""/>
					</td>
					
					<td style="text-align: left;">
						机构：
					</td>
					<td>
						<select name="orgId" id="orgId" class="easyui-combobox">
							<option value="" >请选择</option>
							<c:forEach items="${oList }" var="o" >
								<option value="${o.id }" <c:if test="${oId eq o.id }"> selected='true' </c:if> >${o.orgName }</option>
							</c:forEach>
	                    </select>
					</td>
					
					
                    <td>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;创建时间：</span>
                    </td>
                    <td>
                        <input type="text" class="mytable"   style="width: 150px;" name="settleMonth" id="settleMonth">
                    </td>
                    
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                           iconCls="icon-search" style="height:20px;line-height:20px;">搜索</a>
                    </td>
                     <c:if test="${true }">
                    <td>
                    	&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onclick="exportData()" class="easyui-linkbutton"
                       iconCls="icon-print">导出至Excel</a>
                	</td>
                	</c:if>
                	<td>
                    	&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onclick="doSettlements()" class="easyui-linkbutton"
                       iconCls="icon-print">全部结算</a>
                	</td>
                    </tr>
            </table>
        </form>
    </div>
<!--     <div> -->
<!--         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="cancelComplain()" -->
<!--            plain="true">取消投诉</a> -->
<!--         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="destroyComplain()" -->
<!--            plain="true">删除</a> -->
<!--     </div> -->

</div>

<!-- 弹出的添加或者编辑对话框 -->
<div id="dlg" class="easyui-dialog" style="width:415px;height:390px;" closed="true">
    <div id="playerContainer"></div>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#dlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<%--<div id="updatedlg" class="easyui-dialog" style="width:400px;height:380px;padding:10px 20px" closed="true"
     buttons="#updatedlg-buttons">
    <div class="ftitle">修改投诉</div>
    <!-- 修改 -->
    <form id="updatefm" method="post" novalidate>
        <div class="fitem">
            <label>投诉名称:</label>
            <input name="title" class="easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>内容:</label>
            <input name="content" class="easyui-validatebox" required="true">
        </div>
        <input type="hidden" name="id"/>
    </form>
</div>

<!-- 编辑对话框里的保存和取消按钮 -->
<div id="updatedlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveUser('#updatedlg','#updatefm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
</div>--%>
<input type="hidden" id="video_play_url_prefix" value="${video_play_url_prefix}">
<input type="hidden" id="img" value="${img}">

<div id="user_dlg" class="easyui-dialog" style="width:390px;height:450px;padding:10px 20px" closed="true" buttons="#udlg-buttons">
 <table id="tt2" data-options="border:false,toolbar:'#tb2'">
 </table>
   <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
	<form action="" id="searchForm2">
		<div>
			昵称&nbsp;<input name="name" value="" size="15"/>
			
			&nbsp;登录名&nbsp;<input name="username" value="" size="15"/>
			
			&nbsp;手机号&nbsp;<input name="phone" value="" size="15"/>
			&nbsp;
			<input type="button" class="button blue bigrounded" onclick="doSearch2()" style="width: 80px;height: 30px;" value="查询" />
		</div>
	        <hr/>
	</form>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="udlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectUser();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="deleteUser();">清空</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#user_dlg').dialog('close')">取消</a>
</div>
	
</div>
</body>
</html>
