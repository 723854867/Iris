<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
 -->
<link rel="stylesheet" href="files/Jcrop/css/jquery.Jcrop.css" type="text/css" />
<script src="js/fp/vendor/jquery.ui.widget.js"></script>
<script src="js/fp/jquery.iframe-transport.js"></script>
<script src="js/fp/jquery.fileupload.js"></script>
<script src="files/Jcrop/js/jquery.Jcrop.js"></script>
<title>新建Banner</title>
<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}
</style>
<script type="text/javascript">
var lisVideotUrl="video/searchVideoListPage?filters['flowStat']=checked";
var datagridId="#tt";
var searchFormId="#searchForm";

var datagridId2="#tt2";
var searchFormId2="#searchForm2";

var pageSize=20;
var playId="";
var adddialogueId="#dlg";
	$(function () {
		//启用表单验证
		$('.validatebox-text').bind('blur', function(){
			$(this).validatebox('enableValidation').validatebox('validate');
		});
		datagridList();
		
		datagridList2();
		
	});
	
	function datagridList(){
		 $(datagridId).datagrid({
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
			    url:lisVideotUrl,
			    columns:[[
						{field:'ck',width : 100,checkbox:true},
						{field:'img',title:'缩略图',width : 100,height:100,formatter:function(value,row,index){
							var name="";
							if(row.name){
								name=row.name;
							}
							var desc = "";
							if(row.description){
								desc=row.description;
								if(desc.length>20){
									desc = row.description.substring(0,20)+"...";
								}
							}
							var tag = "";
							if(row.tag){
								tag = row.tag;
								tag = "#"+tag.replace(/ /g,"#,#")+"#";
							}
							var creatorName = "";
							if(row.creatorName){
								creatorName = row.creatorName;
							}
							var activity = "";
							if(row.activity){
								activity = row.activity;
							}
							var stat = "";
							if(row.flowStat=='published'){
								stat = "已发布";
							} else if(row.flowStat=='check_ok'){
								stat = "审核通过";
							} else if(row.flowStat=='uncheck'){
								stat = "未审核";
							} else if(row.flowStat=='check_fail'){
								stat = "审核未通过";
							} else if(row.flowStat=='delete'){
								stat = "已删除";
							}
							var content = "<div style='border:solid 1px #000; width:100%; height:150px;'>";
							content += "<div style='float:left; width:65%;'>"
										+"<div style='float:left;width:220px;height:150px;padding-right:10px;'> "
											+ "<div style='width:220px;height:150px;' id='playerContainer"+row.id+"' onclick='playVideo(\""+row.id+"\",\""+row.playKey+"\")'><img src='<%=request.getContextPath()%>/download"+row.videoPic+"' style='height:150px;width:200px;'/></div> "
										+"</div><div style='width:220px;height:150px;padding-right: 10px;'> "
	   										+ "<div style='width:100%;height:40%;' title='"+row.description+"'><h3 sytle='word-wrap:break-word;word-break:break-all;'>"+ desc + "</h3></div> "
	   										+ "<div style='width:100%;height:30%;color:#969696;'>"+ activity +"</div> "
	   										+ "<div style='width:100%;height:30%;color:#969696;'>"+ tag +"</div> "
	   								+ "</div></div>"
	   								+ "<div style='float:right;width:35%; height:150px;'>"
	   									+ "<div style='float:left;width:200px; height:150px;padding-right:10px'>"
		    								+ "<div style='width:100%;height:40%;align:center'>"
		    									+ "<span style='color:red;font-size:14px;'>"+ stat +"</span> "
		    									+ "<span style='font-size:12px;color:#969696;'>"+ row.createDate +"上传</span> "
	   										+ "</div><div style='width:100%;height:20%;color:#969696;'>"+ creatorName +"</div> "
	   							+ "</div></div></div> ";
							return content;
						}}
						
				    ]],
			    onLoadSuccess:function(){
			    	$(this).datagrid('enableDnd');
			    }
		});
	}
	
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

	function doSearch(){
		$(datagridId).datagrid('reload',getFormJson( searchFormId));
	}
	
	function doSearch2(){
		$(datagridId2).datagrid('reload',getFormJson( searchFormId2));
	}
	
	function selectUser(){
		var row = $(datagridId2).datagrid('getChecked');
		
		if(row.length>0){
			$("#targetid").val(row[0]['id'])
			$("#user_dlg").dialog("close");
		}else{
			showMessage("Error","请选择用户");
		}
	}
	
	function clickupload(){
		
		var target = $("#targetid").val();
		var opt = $("#targetType option:selected").val();
		if(opt !='h5' && (target=='' || target=='undefind')){
			showMessage("Error","指定操作ID不能为空");
			$("#targetid").focus();
			return false;
		}
		return true;
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
	
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" enctype="multipart/form-data" action="autoAttention/add">
			<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="table-doc" >
				<tr id="common_tr">
					<td>选择用户:</td>
					<td style="text-align: left;">
					    <input name="targetId" id="targetid"  value="" onfocus="showTargetDialog();">
					</td>
				</tr>
				<tr>
					<td>涨粉天数:</td>
					<td style="text-align: left;">
					    <input name="days" id="days"  value="0" required="true">
					</td>
				</tr>
								
				<tr>
					<td colspan="2" align="center">
						<input type="submit" style="width: 50px;height:25px" class="button green bigrounded" data-options="iconCls:'icon-ok'" onclick="clickupload()" value="保存"/>
						<a href="autoAttention/list" style="" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
<!-- 弹出的上传图片对话框 -->
<div id="dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true">
	<div class="ftitle">上传文件</div>
	<input type="file" name="files" id="files"/>
	<div style="margin-left:auto;margin-right:auto;width:200px;display:none" id="p" class="easyui-progressbar" ></div>
</div>

<!-- 弹出的选择活动对话框 -->
<div id="act_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true" buttons="#adlg-buttons">
	活动&nbsp;&nbsp;
		<select id="activities" name="activities">
			<c:forEach items="${activites }" var="activity" >
				<option value="${activity.id}">${activity.title}</option>
			</c:forEach>
		</select>
</div>

<!-- 弹出的选择视频对话框 -->
<div id="video_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true" buttons="#vdlg-buttons">
	 <table id="tt" data-options="border:false,toolbar:'#tb'">
	 </table>
    <!-- 列表上面的按钮和搜索条件  -->
     <div id="tb" style="padding:5px;height:auto">
		<form action="" id="searchForm">
			<div>
				所属活动&nbsp;
				<select name="filters['activity']">
					<option value="">选择活动</option>
					<c:forEach items="${activites }" var="activity" >
						<option value="${activity.id}" >${activity.title}</option>
					</c:forEach>
				</select>
				&nbsp;
				发布者
				<input name="filters['creatorName']" value=""/>
				&nbsp;
				视频描述
				<input name="filters['videoName']"  value=""/>
				&nbsp;
				<input type="button" class="button blue bigrounded" onclick="doSearch()" style="width: 80px;height: 30px;" value="查询" />
			</div>
		        <hr/>
		</form>
	</div>
	
</div>

<!-- 弹出的选择用户对话框 -->
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
	
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="fileUpload();">保存</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="adlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectActivity();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#act_dlg').dialog('close')">取消</a>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="vdlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectVideo();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#video_dlg').dialog('close')">取消</a>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="udlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectUser();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#user_dlg').dialog('close')">取消</a>
</div>
</body>
</html>