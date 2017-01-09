<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>赠送礼物查询</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ==';</script>
    <script type='text/javascript' src='js/admin/query_consume_record_sender.js'></script>
    
    
    <script type="text/javascript">

var datagridId2="#tt2";
var searchFormId2="#searchForm2";

var pageSize=20;
var playId="";
var adddialogueId="#dlg";
	$(function () {
		
		datagridList2();
		
		$('#startDate').datetimebox({
			required : false,
			onShowPanel:function(){
				$(this).datetimebox("spinner").timespinner("setValue","00:00:00");
			}
		});
		$('#endDate').datetimebox({
			required : false,
			onShowPanel:function(){
				$(this).datetimebox("spinner").timespinner("setValue","23:59:59");
			}
		});
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
		var userId = $("#userId").val();
	    var paramType = $("#paramType").combobox("getValue");
	    var param = $("#param").val();
	    var oId = $("#oId").combobox("getValue");
	    var gId = $("#gId").combobox("getValue");
	    var startDate = $('#startDate').datebox("getValue");
	    var endDate = $('#endDate').datebox("getValue");
	    window.location.href = "consumeRecord/findConsumeRecordBySenderIdExport?startDate="+startDate+"&endDate="+endDate+"&paramType="+paramType+"&param="+param+"&oId="+oId+"&gId="+gId;
	}
	
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
        <h2 style="float:left;padding-left:10px;margin: 1px">赠送礼物查询</h2>
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
						<select name="paramType" id="paramType" class="easyui-combobox">
	                        <option value="0">请选择</option>
	                        <option value="1">手机号</option>
	                        <option value="2">用户名</option>
	                        <option value="3">用户ID</option>
	                    </select>
					</td>
					<td style="text-align: left;">
						<input class="easyui-textbox" style="width:150px" name="param" id="param" value=""/>
					</td>
					
					<td style="text-align: left;">
						机构：
					</td>
					<td>
						<select name="oId" id="oId" class="easyui-combobox">
							<option value="" >请选择</option>
							<c:forEach items="${oList }" var="o" >
								<option value="${o.id }" <c:if test="${oId eq o.id }"> selected='true' </c:if> >${o.orgName }</option>
							</c:forEach>
	                    </select>
					</td>
					
					<td style="text-align: left;">
						道具：
					</td>
					<td>
						<select name="gId" id="gId" class="easyui-combobox">
							<option value="" >请选择</option>
							<c:forEach items="${gList }" var="g" >
								<option value="${g.id }" <c:if test="${gId eq g.id }"> selected='true' </c:if> >${g.name }_${g.id }</option>
							</c:forEach>
	                    </select>
					</td>
					
					
                    
                    
					
                    <td>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;创建时间：</span>
                    </td>
                    <td>
                        <input type="text" class="easyui-datetimebox" name="startDate"
                               id="startDate"> 至 <input
                            type="text" class="easyui-datetimebox" name="endDate"
                            id="endDate">
                    </td>
                    
                    <td>
			         	平台：
			        </td>
			        <td>
			                <select class="easyui-combobox" name="platform" id="platform">
			                    <option value="">全部</option>
			                    <option value="ios">IOS</option>
			                    <option value="android">Android</option>
			                </select>
			        </td>
			        <td>
			        	版本：
			        </td>
			        <td>
			                <select class="easyui-combobox" name="appVersion" id="appVersion">
			                    <option value="">全部</option>
			                    <c:forEach items="${appVersionList }" var="v" >
			                    	<option value="${v }">${v }</option>
			                    </c:forEach>
			                </select>
			        </td>
			        <td>
			        	渠道：
			        </td>
			        <td>
			                <select class="easyui-combobox" name="channel" id="channel">
			                    <option value="">全部</option>
			                    <c:forEach items="${channelList }" var="v" >
			                    	<option value="${v }">${v }</option>
			                    </c:forEach>
			                </select>
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
                    </tr>
                    <tr>
                   
                	<td>
                    	&nbsp;&nbsp;&nbsp;&nbsp;折合金币总数：<c:out value="${dmSum }"></c:out><span id="dmSum" ></span>
                	</td>
                	<td>
                    	&nbsp;&nbsp;&nbsp;&nbsp;折合现金总数（元）:<c:out value="${mSum }"></c:out><span id="mSum" ></span>
                	</td>
                	<td>
                    	&nbsp;&nbsp;&nbsp;&nbsp;赠送礼物人数:<c:out value="${uCount }"></c:out><span id="uCount" ></span>
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
