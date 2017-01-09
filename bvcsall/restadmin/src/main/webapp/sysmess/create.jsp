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
var lisVideotUrl="video/searchVideoListPage?filters['flowStat']=checked&filters['type']=1";
var datagridId="#tt";
var searchFormId="#searchForm";

var datagridId2="#tt2";
var searchFormId2="#searchForm2";
var searchFormId3="#searchForm3";
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
		datagridList3();
		getLiveActivityList();
		$('#files').fileupload({
	    	url: 'sysmess/uploadfile',
	        sequentialUploads: true,
	        dataType: 'json',
	        type:'post',
	        crossDomain:true,
	        done: function (e, data) {
	        	if (data.result[ "success"]== true){
	        		$("#dest_user").val(data.result["result"]);
					var ids = data.result["result"].split(";");
					$("#user_count").text(ids.length-1+"位");
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

		$('#msgImage').fileupload({
			url: 'sysmess/uploadImage',
			sequentialUploads: true,
			dataType: 'json',
			type:'post',
			crossDomain:true,
			done: function (e, data) {
				if (data.result[ "success"]== true){
					$("#imagePath").val(data.result["result"]);
					$("#msgImageName").attr('type','text');
					$("#msgImageName").val(data.files[0].name);
				} else {
					alert("上传失败，请重新上传。。。。。");
					return;
				}
				showMessage("通知","恭喜您上传成功");
			},
			change: function(e, data){
				console.log(data);
				var fileName = data.files[0].name;
				var fileext = fileName.substring(fileName.lastIndexOf("."));
				fileext = fileext.toLowerCase();
				if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
					showMessage( "Error","对不起，封面图片仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
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
function datagridList3(){
	$("#tt3").datagrid({
		fitColumns:true,
		fit:true,
		rownumbers : true,
		pagination : true,
		pageNumber : 1,
		pageList : [pageSize,pageSize*2,pageSize*3],
		pageSize : pageSize,
		pagePosition : 'bottom',
		selectOnCheck:true,
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
function getLiveActivityList(){
	$('#liveActivity_tt').datagrid({
		nowrap: true, //是否换行
		autoRowHeight: true, //自动行高
		fitColumns: true,
		fit: true,
		striped: true,
		pageNumber: 1,
		collapsible: true, //是否可折叠
		remoteSort: false,
		singleSelect: true, //是否单选
		pagination: true, //分页控件
		rownumbers: true, //行号
		pagePosition: 'bottom',
		scrollbarSize: 0,
		loadMsg: "数据加载中.....",
		url: "comboBox/getLiveActivitiesCombogrid",
		columns: [
			[
				{field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
				{
					field: 'cover', title: '<span class="columnTitle">封面</span>', width: 100, align: 'center',
					formatter: function (value, row) {
						return '<img src="/restadmin/download' + value + '" style="width:100px;height:100px;">';
					}
				},
				{field: 'title', title: '<span class="columnTitle">标题</span>', width: 120, align: 'center'},
				{field: 'description', title: '<span class="columnTitle">描述</span>', width: 120, align: 'center'},
				{field: 'status', title: '<span class="columnTitle">状态</span>', width: 80, align: 'center',
					formatter: function (value, row) {
						if(value == 1){
							return "上线";
						}else {
							return "下线";
						}
					}
				}
			]
		],
		toolbar: "#dataGridToolbar",
		onLoadSuccess: function () {
			$('#liveActivity_tt').datagrid('clearSelections');
		},
		pageSize: 20,
		pageList: [20, 40, 60, 80, 100],
		beforePageText: '第', //页数文本框前显示的汉字
		afterPageText: '页    共 {pages} 页',
		displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
}

	function doSearch(){
		$(datagridId).datagrid('reload',getFormJson( searchFormId));
	}
	
	function doSearch2(){
		$(datagridId2).datagrid('reload',getFormJson( searchFormId2));
	}
	function doSearch3(){
		$("#tt3").datagrid('reload',getFormJson(searchFormId3));
	}
	function selectUser(){
		var row = $(datagridId2).datagrid('getChecked');
		var ids=[];
		var inum=0;
		for(var r in row){
			ids.push(row[r]['id']);			
		}
		if(ids.length>0){
			$("#user_count").text(ids.length+"位");
			ids=ids.join(";");
			$("#dest_user").val(ids);
			$("#user_dlg").dialog("close");
		}else{
			showMessage("Error","请选择用户");
		}
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
			var targetUrl = $("#targetUrl").val();
			if((target=='' || target=='undefind') && targetUrl == ""){
				showMessage("Error","指定操作ID或url不能为空");
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
			$("#user_dlg").show();
			$("#user_dlg").dialog({
				 title: '选择文件',
				 width: 800,
				 height: 650,
				 closed: false,
				 cache: true,
				 modal: true
		    });
		} else {
			$("#dest_user").attr("readonly","readonly");
			if (name == 'all') {
				$("#dest_user").val("all");
				$("#user_count").text("所有 ");
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
			if(opt == 'h5') {
				$('#common_tr').hide();
				$('#targetUrl').val("");
				$('#h5_tr').show();
				$('#h5_td').text("H5页面地址");
			}else{
				$('#h5_tr').hide();
				$('#common_tr').show();
				$("#targetid").val("");
				$("#target").attr("style","display:block");
			}
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
		}else if(opt == 'user'){
			$("#user_dlg3").show();
			$("#user_dlg3").dialog({
				title: '选择文件',
				width: 800,
				height: 650,
				closed: false,
				cache: true,
				modal: true
			});
		}else if(opt == "liveAct"){
			$("#liveActivity_dlg").show();
			$("#liveActivity_dlg").dialog({
				title: '选择直播活动',
				width: 800,
				height: 650,
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

function selectLiveActivity(){
	var row = $("#liveActivity_tt").datagrid('getChecked');
	var vid = row[0].id;
	$("#targetid").val(vid);
	$("#liveActivity_dlg").dialog("close");
}

function selectUser2(){
	var row = $("#tt3").datagrid('getChecked');
	if(row.length>0){
		$("#targetid").val(row[0]['id'])
		$("#user_dlg3").dialog("close");
	}else{
		showMessage("Error","请选择用户");
	}
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
					<td style="width: 80%;text-align: left;">
						<input class="easyui-validatebox easyui-textbox" name="title" id="title" data-options="validType:'length[0,20]'" required="true" value="" >
					</td>
				</tr>
				<tr>
					<td>推送内容:</td>
					<td style="text-align: left;">
						<input class="easyui-validatebox easyui-textbox" name="content" id="content" data-options="multiline:true,validType:'length[0,100]'" required="true" value="" style="width:50%;height:100px">
					</td>
				</tr>
				<tr>
					<td>点击操作:</td>
					<td style="text-align: left;">
						<select name="operation" id="operation"  style="width:50%" onchange="chgOpration()">
							<option value="app">启动应用到首页</option>
							<option value="video">打开视频详情页</option>
							<option value="activity">打开活动详情页</option>
							<option value="user">个人中心</option>
							<option value="h5">H5页面</option>
							<option value="liveAct">直播活动</option>
						</select>
					</td>
				</tr>
				<tr style="display:none" id="h5_tr">
					<td id="h5_td">H5页面地址:</td>
					<td style="text-align: left;">
						<input name="targetUrl" id="targetUrl" onfocus="showTargetUrlDialog()" value="">
					</td>
				</tr>
				<tr id="common_tr">
					<td>跳转位置:</td>
					<td style="text-align: left;">
						<input name="targetid" id="targetid"  value="" onfocus="showTargetDialog();">
					</td>
				</tr>
				<%--<tr style="display:none" id="h5_tr">
					<td id="h5_td">H5页面地址:</td>
					<td>
							<span style="text-align: left;">
								<input name="targetUrl" id="targetUrl" value="">
							</span>
					</td>
				</tr>
				<tr id="common_tr">
					<td id="common_td">ID:</td>
					<td>
						<input name="targetid" id="targetid"  value="" onfocus="showTargetDialog();">
					</td>
				</tr>--%>
				<!-- <tr>
					<td>推送平台:</td>
					<td>
					    <input type="radio" value="android" name="platform" /> Android &nbsp;&nbsp;
					    <input type="radio" value="ios" name="platform" /> IOS 
					</td>
				</tr> -->
				<tr>
					<td>推送对象:</td>
					<td style="text-align: left;">
						<input type="radio" value="all" name="platform" onclick="changeRadio('input')"/> 指定用户&nbsp;&nbsp;
					    <input type="radio" value="all" name="platform" onclick="changeRadio('all')"/> 所有用户&nbsp;&nbsp;
					    <input type="radio" value="all" name="platform" onclick="changeRadio('file')"/> 按文件推送
					    <br/>
					 	<input name="destUser" id="dest_user" data-options="multiline:true,validType:'length[0,100]'" value="" style="width:50%;height:100px" readonly="readonly">
					</td>
				</tr>
				<tr>
					<td>推送时机:</td>
					<td style="text-align: left;">
						<input type="radio" value="0" name="isplan" onclick="pubClick()" checked="checked"/> 立即 &nbsp;&nbsp;
					    <input type="radio" value="1" name="isplan"  onclick="pubClick()"/> 定时
					    <div id="actives" style="display:none;">
					    	请输入有效时间：<input id="pubTime" name="pubTime" />
					    </div>
					</td>
				</tr>
				<tr>
					<td>视频图片:</td>
					<td style="text-align: left;">
						尺寸：480*480 <br>

						<input type="hidden" disabled="disabled" id="msgImageName" name="msgImageName">
						<input type="file" id="msgImage" name="msgImage"/>
						<input type="hidden" id="imagePath" name="imagePath">
					</td>
				</tr>
				<tr>
					<td>有效时间:</td>
					<td style="text-align: left;">
						<input class="easyui-validatebox easyui-textbox" name="liveTime" id="liveTime" data-options="validType:'Number'"> 分钟
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<span>你即将为 <span id="user_count" style="color: red;">0 位</span> Blive用户发送推送消息！</span>
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

<!-- 弹出的选择用户对话框 -->
<div id="user_dlg3" class="easyui-dialog" style="width:390px;height:450px;padding:10px 20px" closed="true" buttons="#udlg3-buttons">
	<table id="tt3" data-options="border:false,toolbar:'#tb3'">
	</table>
	<!-- 列表上面的按钮和搜索条件  -->
	<div id="tb3" style="padding:5px;height:auto">
		<form action="" id="searchForm3">
			<div>
				昵称&nbsp;<input name="name" value="" size="15"/>

				&nbsp;登录名&nbsp;<input name="username" value="" size="15"/>

				&nbsp;手机号&nbsp;<input name="phone" value="" size="15"/>
				&nbsp;
				<input type="button" class="button blue bigrounded" onclick="doSearch3()" style="width: 80px;height: 30px;" value="查询" />
			</div>
			<hr/>
		</form>
	</div>

</div>

<div id="liveActivity_dlg" class="easyui-dialog" style="width:800px;height:650px;padding:10px 20px" closed="true" buttons="#ladlg-buttons">
	<table id="liveActivity_tt" data-options="border:false,toolbar:'#tb2'">
	</table>
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
<!-- 添加对话框里的保存和取消按钮 -->
<div id="udlg3-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectUser2();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#user_dlg3').dialog('close')">取消</a>
</div>
<!-- 添加对话框里的保存和取消按钮 -->
<div id="ladlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectLiveActivity();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#liveActivity_dlg').dialog('close')">取消</a>
</div>
</body>
</html>