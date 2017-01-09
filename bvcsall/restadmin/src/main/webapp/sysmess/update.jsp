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
<title>新建系统消息</title>
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
var pageSize=20;
var playId="";
var adddialogueId="#dlg";
	$(function () {
		//启用表单验证
		$('.validatebox-text').bind('blur', function(){
			$(this).validatebox('enableValidation').validatebox('validate');
		});
		datagridList();
		
		$('#files').fileupload({
	    	url: 'sysmess/uploadfile',
	        sequentialUploads: true,
	        dataType: 'json',
	        type:'post',
	        crossDomain:true,
	        done: function (e, data) {
	        	if (data.result[ "success"]== true){
	        		$("#dest_user").val(data.result["result"]);
	        	} else {
	        		alert("上传失败，请重新上传。。。。。");
	        		return;
	        	}
	        	showMessage("通知","恭喜您上传成功");
	        	$("#dlg").dialog("close");
	        },
	        progress:function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#p').progressbar('setValue', progress);
	        },
	        start:function (e) {
	        	$('#p').show();
	        	$('#p').progressbar('setValue', 0);
	        },
	        change:function(e,data){
	        	var fileName = data.files[0].name;
	        	var fileext = fileName.substring(fileName.lastIndexOf("."));
				fileext = fileext.toLowerCase();
				if ((fileext != '.txt')) {
					showMessage( "Error","对不起，系统仅支持txt格式的文本文件，请重新选择！");
					return false;
				}
	        }
	    });
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

	function doSearch(){
		$(datagridId).datagrid('reload',getFormJson( searchFormId));
	}
	
	function clickupload(){
		var item = $("input[name='isplan']:checked").val();
		if(item == 1){
			var pubTime = $('#pubTime').textbox('getValue');
			if(pubTime == null || pubTime == ""){
				showMessage("提示","请您选择计划发布的时间");
				return false;
			}else{
				var sDate = new Date(pubTime.replace("//-/g", "//"));
		    	var eDate = new Date();
		    	if(sDate < eDate){
					showMessage("Error","计划发布开始时间不能小于当前时间");
					return false;
		    	}
			}
		}
		var opt = $("#operation option:selected").val();
		if(opt != 'app'){
			var target = $("#targetid").val();
			if(target=='' || target=='undefind'){
				showMessage("Error","指定操作ID不能为空");
				$("#targetid").focus();
				return false;
			}
		}
		var formjson=getFormJson('#myform');
		$.ajax({
		      url: 'sysmess/add',
		      data: formjson,
		      type: "post",
		      dataType: "json",
		      beforeSend: function(){
		       return $( '#myform' ).form('enableValidation').form( 'validate' );
		      },
		      success: function (result){
		      	location.href="sysmess/listmess";
		      }
		  });
	}

	function pubClick(){
		var num = $("input[name='isplan']:checked").val();;
		if(num == 0){//立即发布
			$("#actives").attr("style","display:none");
		}else{//计划发布
			$("#actives").attr("style","display:block");
			$("#pubTime").datetimebox({
				showSeconds : false
			});
		}
	}
 
	function changeRadio(name){
		if (name == 'input') {
			$("#dest_user").removeAttr("readonly");
		} else {
			$("#dest_user").attr("readonly","readonly");
			if (name == 'all') {
				$("#dest_user").val("all");
			}else if (name == 'file') {
				$("#dlg").show();
				$("#dlg").dialog({
					 title: '选择文件',
					 width: 400,
					 height: 200,
					 closed: false,
					 cache: true,
					 modal: true
			    });
			}
		}
	}

	function chgOpration(){
		var opt = $("#operation option:selected").val();
		if(opt != 'app'){
			$("#target").attr("style","display:block");
		}else{
			$("#target").attr("style","display:none");
		}
		$("#targetid").val("");
	}
	
	function showTargetDialog(){
		var opt = $("#operation option:selected").val();
		if(opt == 'video'){
			$("#video_dlg").show();
			$("#video_dlg").dialog({
				 title: '选择视频',
				 width: 800,
				 height: 650,
				 closed: false,
				 cache: true,
				 modal: true
		    });
		}else if(opt == 'activity'){
			$("#act_dlg").show();
			$("#act_dlg").dialog({
				 title: '选择活动',
				 width: 400,
				 height: 200,
				 closed: false,
				 cache: true,
				 modal: true
		    });
		}
	}
	
	function selectActivity(){
		var aid = $("#activities option:selected").val();
		$("#targetid").val(aid);
		$("#act_dlg").dialog("close");
	}
	
	function selectVideo(){
		var row = $(datagridId).datagrid('getChecked');
		var vid = row[0].id;
		$("#targetid").val(vid);
		$("#video_dlg").dialog("close");
	}
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" enctype="multipart/form-data" action="sysmess/add">
			<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="form-body" >
				<tr>
					<td style="width: 20%;">推送标题：</td>
					<td style="width: 80%;">
						<input class="easyui-validatebox easyui-textbox" name="title" id="title" data-options="validType:'length[0,20]'" required="true" value="" >
					</td>
				</tr>
				<tr>
					<td>推送内容:</td>
					<td>
						<input class="easyui-validatebox easyui-textbox" name="content" id="content" data-options="multiline:true,validType:'length[0,100]'" required="true" value="" style="width:50%;height:100px">
					</td>
				</tr>
				<tr>
					<td>点击操作:</td>
					<td>
						<select name="operation" id="operation"  style="width:50%" onchange="chgOpration()">
							<option value="app">启动应用到首页</option>
							<option value="video">打开视频详情页</option>
							<option value="activity">打开活动详情页</option>
						</select>
						<div style="display:none" id="target">
							ID: <input name="targetid" id="targetid"  value="" onfocus="showTargetDialog();">
						</div>
					</td>
				</tr>
				<!-- <tr>
					<td>推送平台:</td>
					<td>
					    <input type="radio" value="android" name="platform" /> Android &nbsp;&nbsp;
					    <input type="radio" value="ios" name="platform" /> IOS 
					</td>
				</tr> -->
				<tr>
					<td>推送对象:</td>
					<td>
						<input type="radio" value="all" name="platform" onclick="changeRadio('input')"/> 指定用户&nbsp;&nbsp;
					    <input type="radio" value="all" name="platform" onclick="changeRadio('all')"/> 所有用户&nbsp;&nbsp;
					    <input type="radio" value="all" name="platform" onclick="changeRadio('file')"/> 按文件推送
					    <br/>
					 	<input name="destUser" id="dest_user" data-options="multiline:true,validType:'length[0,100]'" value="" style="width:50%;height:100px" readonly="readonly">
					</td>
				</tr>
				<tr>
					<td>推送时机:</td>
					<td>
						<input type="radio" value="0" name="isplan" onclick="pubClick()" checked="checked"/> 立即 &nbsp;&nbsp;
					    <input type="radio" value="1" name="isplan"  onclick="pubClick()"/> 定时
					    <div id="actives" style="display:none;">
					    	请输入有效时间：<input id="pubTime" name="pubTime" />
					    </div>
					</td>
				</tr>
				
				<tr>
					<td colspan="2" align="center">
						<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="clickupload()">保存发布</a>
						<a href="sysmess/listmess" style="" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
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
				标签&nbsp;
				<select name="filters['tag']">
					<option value="">选择标签</option>
					<c:forEach items="${tags }" var="tag" >
						<option value="${tag.name}">${tag.name}</option>
					</c:forEach>
				</select>
				&nbsp;所属活动&nbsp;
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
</body>
</html>