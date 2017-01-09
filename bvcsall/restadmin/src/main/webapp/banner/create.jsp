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
		getLivingList();
		datagridListReVideo();
		getLiveActivityList();
		getHotLabelList();
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
						{field:'id',title:'用户ID',width : 100},
						{field:'name',title:'昵称',width : 100},
						{field:'username',title:'登录名',width : 100},
						{field:'phone',title:'手机号',width : 100}

				    ]],
			    onLoadSuccess:function(){
			    	$(this).datagrid('enableDnd');
			    }
		});
	}

	function getLivingList(){
	$('#ttLiving').datagrid({
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
		url: "living/queryLivingList",
		columns: [
			[
				{field: 'id', title: '<span class="columnTitle">房间ID</span>', width: 40, align: 'center'},
				{
					field: 'roomPic', title: '<span class="columnTitle">房间图片</span>', width: 100, align: 'center',
					formatter: function (value, row) {
						return '<img src="/restadmin/download' + value + '" style="width:100px;height:100px;">';
					}
				},
				{field: 'title', title: '<span class="columnTitle">房间名称</span>', width: 120, align: 'center'},
				{field: 'anchorName', title: '<span class="columnTitle">主播昵称</span>', width: 120, align: 'center'},
				{field: 'creatorId', title: '<span class="columnTitle">主播ID</span>', width: 80, align: 'center'},
				{field: 'praiseNumber', title: '<span class="columnTitle">点赞数</span>', width: 80, align: 'center'},
				{field: 'onlineNumber', title: '<span class="columnTitle">当前在线人数</span>', width: 80, align: 'center'},
				{
					field: 'maxAccessNumber',
					title: '<span class="columnTitle">最大在线人数</span>',
					width: 80,
					align: 'center'
				},
				{field: 'createDate', title: '<span class="columnTitle">开始时间</span>', width: 80, align: 'center'}
			]
		],
		toolbar: "#dataGridToolbar",
		onLoadSuccess: function () {
			$('#displayTable').datagrid('clearSelections');
		},
		pageSize: 20,
		pageList: [20, 40, 60, 80, 100],
		beforePageText: '第', //页数文本框前显示的汉字
		afterPageText: '页    共 {pages} 页',
		displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
	}


function getHotLabelList(){
	$('#hotLabel_tt').datagrid({
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
		url: "activity/queryHotLabelList",
		columns: [
			[
				{field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
				{field: 'labelName', title: '<span class="columnTitle">名称</span>', width: 120, align: 'center'},
			]
		],
		toolbar: "#dataGridToolbar",
		onLoadSuccess: function () {
			$('#hotLabel_tt').datagrid('clearSelections');
		},
		pageSize: 20,
		pageList: [20, 40, 60, 80, 100],
		beforePageText: '第', //页数文本框前显示的汉字
		afterPageText: '页    共 {pages} 页',
		displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
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

	function datagridListReVideo(){
		$("#revideo_tt").datagrid({
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
			url:"combogrid/getCombogridVideoList?type=2",
			columns:[[
				{field:'ck',width : 100,checkbox:true},
				{
					field: 'img', title: '<span class="columnTitle">缩略图</span>', width: 100, align: 'center',
					formatter: function (value, row) {
						return '<img src="/restadmin/download' + value + '" style="width:100px;height:100px;">';
					}
				},
				{
					field: 'flowStat', title: '<span class="columnTitle">状态</span>', width: 100, align: 'center',
					formatter: function (value, row) {
						if(value=='published'){
							return "已发布";
						} else if(value=='check_ok'){
							return "审核通过";
						} else if(value=='uncheck'){
							return "未审核";
						} else if(value=='check_fail'){
							return "审核未通过";
						} else if(value=='delete'){
							return "已删除";
						}
					}
				}
			]],
			onLoadSuccess:function(){
				$(this).datagrid('enableDnd');
			}
		});
	}

	function doSearch() {
		var queryParams = $('#displayTable').datagrid('options').queryParams;
		$('#displayTable').datagrid({url: "living/queryLivingList"});
	}

	function doSearch(){
		$(datagridId).datagrid('reload',getFormJson( searchFormId));
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
		if(opt =='h5' && ($("#targetUrl").val()=='' || $("#targetUrl").val()=='undefind')){
			showMessage("Error","h5页面地址不能为空");
			$("#targetid").focus();
			return false;
		}
		return true;
// 		var formjson=getFormJson('#myform');
// 		$.ajax({
// 		      url: 'banner/add',
// 		      data: formjson,
// 		      type: "post",
// 		      dataType: "json",
// 		      beforeSend: function(){
// 		       return $( '#myform' ).form('enableValidation').form( 'validate' );
// 		      },
// 		      success: function (result){
// 		      	location.href="banner/listbanner";
// 		      }
// 		  });
	}

	function chgOpration(){
		var opt = $("#targetType option:selected").val();
		if (opt == 'h5') {
			$('#h5_tr').show();
			$('#h5_td').text("H5页面地址");
			$('#common_tr').hide();
			$('#targetUrl').val("");
		}else if(opt == 'hotLabel'){
			$('#h5_tr').show();
			$('#h5_td').text("热门话题名称");
			$('#common_tr').hide();
			$('#targetUrl').val("");
		} else {
			$('#h5_tr').hide();
			$('#common_tr').show();
			$("#targetid").val("");
		}

	}

function showTargetUrlDialog() {
	var opt = $("#targetType option:selected").val();
	if(opt == "hotLabel"){
		$("#hotLabelDlg").show();
		$("#hotLabelDlg").dialog({
			title: '选择话题',
			width: 600,
			height: 600,
			closed: false,
			cache: true,
			modal: true
		});
	}
}

	function showTargetDialog(){
		var opt = $("#targetType option:selected").val();
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
			$("#user_dlg").show();
			$("#user_dlg").dialog({
				 title: '选择文件',
				 width: 800,
				 height: 650,
				 closed: false,
				 cache: true,
				 modal: true
		    });
		}else if(opt == "live"){
			$("#livingDlg").show();
			$("#livingDlg").dialog({
				title: '选择直播',
				width: 800,
				height: 650,
				closed: false,
				cache: true,
				modal: true
			});
		}else if(opt == "revideo"){
			$("#revideo_dlg").show();
			$("#revideo_dlg").dialog({
				title: '选择视频回放',
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

	function selectLive(){
		var row = $("#ttLiving").datagrid('getChecked');
		var vid = row[0].creatorId;
		$("#targetid").val(vid);
		$("#livingDlg").dialog("close");
	}

	function selectReVideo(){
		var row = $("#revideo_tt").datagrid('getChecked');
		var vid = row[0].id;
		$("#targetid").val(vid);
		$("#revideo_dlg").dialog("close");
	}

	function selectLiveActivity(){
		var row = $("#liveActivity_tt").datagrid('getChecked');
		var vid = row[0].id;
		$("#targetid").val(vid);
		$("#liveActivity_dlg").dialog("close");
	}

function selectHotLabel(){
	var row = $("#hotLabel_tt").datagrid('getChecked');
	var vid = row[0].labelName;
	$("#targetUrl").val(vid);
	$("#hotLabelDlg").dialog("close");
}
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" enctype="multipart/form-data" action="banner/add">
			<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="table-doc" >
				<tr>
					<td style="width: 20%;">标题：</td>
					<td style="width: 80%;text-align: left;">
						<input class="easyui-validatebox easyui-textbox" name="title" id="title" data-options="validType:'length[0,20]'" required="true" value="" >
					</td>
				</tr>
				<tr>
					<td>Banner图:</td>
					<td style="text-align: left;">
						<input name="file" id="imgSrc" type="file" required="true" value="" style="width:50%;" required="true" >
					</td>
				</tr>
				<tr>
					<td>banner类型</td>
					<td style="text-align: left;">
						<input type="radio" value="1" name="bannerType">首页
						<input type="radio" value="2" name="bannerType">发现页
						<input type="radio" value="3" name="bannerType">首页新歌声
						<input type="radio" value="4" name="bannerType">新歌声专区
						<input type="radio" value="5" name="bannerType">学员榜
						<input type="radio" value="6" name="bannerType">综艺榜
						<input type="radio" value="7" name="bannerType">综艺预告榜
						<input type="radio" value="8" name="bannerType">主播榜
						<input type="radio" value="9" name="bannerType">贡献榜
						<input type="radio" value="10" name="bannerType">贡献总榜
						<input type="radio" value="11" name="bannerType">本期人气
						<input type="radio" value="0" name="bannerType">其它
					</td>
				</tr>
				<tr>
					<td>点击操作:</td>
					<td style="text-align: left;"  id="other_tr">
						<select name="targetType" id="targetType"  style="width:50%" onchange="chgOpration()">
							<option value="activity">活动详情</option>
							<option value="video">视频详情</option>
							<option value="user">个人中心</option>
							<option value="h5">H5页面</option>
							<option value="live">直播推荐</option>
							<option value="revideo">回放</option>
							<option value="liveAct">直播活动</option>
							<option value="hotLabel">热门话题</option>
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
					    <input name="targetId" id="targetid"  value="" onfocus="showTargetDialog();">
					</td>
				</tr>
				<tr>
					<td>角标显示:</td>
					<td style="text-align: left;">
						<select name="tag" id="tag"  style="width:50%" class="easyui-combobox">
							<option value="-1">选择角标</option>
							<option value="0">hot</option>
							<option value="1">new</option>
							<option value="2">火爆</option>
                            <option value="3">预告</option>
                            <option value="4">直播</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>显示顺序:</td>
					<td style="text-align: left;">
					    <input name="orderNum" id="orderNum"  value="0" required="true">
					</td>
				</tr>

				<tr>
					<td colspan="2" align="center">
						<input type="submit" style="width: 50px;height:25px" class="button green bigrounded" data-options="iconCls:'icon-ok'" onclick="clickupload()" value="保存"/>
						<a href="banner/listbanner" style="" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
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
<div id="livingDlg" class="easyui-dialog" style="width:800px;height:650px;padding:10px 20px" closed="true" buttons="#ldlg-buttons">
	<table id="ttLiving" data-options="border:false,toolbar:'#tb2'">
	</table>
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
				用户ID&nbsp;<input name="id" value="" size="15"/>
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

<div id="liveActivity_dlg" class="easyui-dialog" style="width:800px;height:650px;padding:10px 20px" closed="true" buttons="#ladlg-buttons">
	<table id="liveActivity_tt" data-options="border:false,toolbar:'#tb2'">
	</table>
</div>

<!-- 弹出的选择回放视频对话框 -->
<div id="revideo_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true" buttons="#revdlg-buttons">
	<table id="revideo_tt" data-options="border:false,toolbar:'#revideo_tb'">
	</table>
	<div id="revideo_tb"></div>
</div>

<!-- 弹出的选择话题选择框 -->
<div id="hotLabelDlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true" buttons="#hotLabel-buttons">
	<table id="hotLabel_tt" data-options="border:false,toolbar:'#hotLabel_tb'">
	</table>
	<div id="hotLabel_tb"></div>
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
<div id="ldlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectLive()">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#livingDlg').dialog('close')">取消</a>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="revdlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectReVideo();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#revideo_dlg').dialog('close')">取消</a>
</div>
<div id="ladlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectLiveActivity();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#liveActivity_dlg').dialog('close')">取消</a>
</div>
<div id="hotLabel-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectHotLabel();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#hotLabelDlg').dialog('close')">取消</a>
</div>
</body>
</html>