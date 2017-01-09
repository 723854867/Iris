<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>管理员视频</title> 
<link rel="stylesheet" href="files/Jcrop/css/jquery.Jcrop.css" type="text/css" />
<script src="files/Jcrop/js/jquery.Jcrop.js"></script>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<style type="text/css">
#page a {
	margin-left: 10px;
	height: 30px;
	width: 100px;
}

.button {
	width: 120px;
	height: 30px;
}

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
<script src="js/fp/vendor/jquery.ui.widget.js"></script>
<script src="js/fp/jquery.iframe-transport.js"></script>
<script src="js/fp/jquery.fileupload.js"></script>
<script type='text/javascript' src='js/players/jwplayer.js'></script>
<script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
<script type="text/javascript">
var datagridId="#tt";
var searchFormId="#searchForm";
var pageSize=20;
var listUrl="video/searchVideoListPage?filters['dataFrom']=myvideo_restadmin";
var noSelectedRowMessage="你没有选择行或状态已更改";

var url;
var adddialogueId="#dlg";
var playdialogueId="#videoPlay";
var addTitle="播放视频";
var addUrl="video/updatevideo";

function selectPageSize() {
	$("#size").val($("#selectsize").val());
}

function selectPage(p) {
	$("#page").val(p);
	$("#videoform").submit();
}

function formSubmit() {
	$("#page").val(null);
	$("#videoform").submit();
}

function selectStatus(p) {
	$("#status").val(p);
	$("#keyword").val("");
	$("#videoform").submit();
}

function selectAll() {
	var ck = false;
	var sall = $("#sall");
	if(sall[0].checked){
		ck = true;
	}
	var ckboxes = $("input[name='ck']");
	for(var i=0;i<ckboxes.length;i++){
		if(ck){
			 $(datagridId).datagrid('selectRow',i);
		}else{
			 $(datagridId).datagrid('unselectRow',i);
		}
		ckboxes[i].checked = ck;
	}
}

function publishVideos() {
	var row = $(datagridId).datagrid('getChecked');
	var ids=[];
	var inum=0;
	for(var r in row){
		ids.push(row[r]['id']);			
	}
	if(ids.length>0){
		ids=ids.join(",");
	}else{
		showMessage("Error",noSelectedRowMessage);
		return;
	}
	
	$.messager.confirm('发布视频', "确定要发布该视频吗？", function(r) {
		if (r) {
			$.ajax({
				url : 'video/publish',
				data : {
					'videoIds' : ids
				},
				type : "post",
				dataType : "json",
				success : function(result) {
					window.location.reload();
				}
			});
		}
	});
}

function deleteAll() {
	var row = $(datagridId).datagrid('getChecked');
	var ids=[];
	var inum=0;
	for(var r in row){
		ids.push(row[r]['id']);			
	}
	if(ids.length>0){
		ids=ids.join(",");
	}else{
		showMessage("Error",noSelectedRowMessage);
		return;
	}
	
	$.messager.confirm('视频删除', "确定要删除该视频吗？", function(r) {
		if (r) {
			$.ajax({
				url : 'video/deleteVideo',
				data : {
					'videoIds' : ids
				},
				type : "post",
				dataType : "json",
				success : function(result) {
					window.location.reload();
				}
			});
		}
	});
}

function downloadAll(){
	var row = $(datagridId).datagrid('getChecked');
	var ids=[];
	var inum=0;
	for(var r in row){
		ids.push(row[r]['id']);
	}
	ids=ids.join(",");
	if (ids.length>0){
		window.location.href=baseUrl+'video/multidown?ids='+ids;
	}else{
		showMessage("Error",noSelectedRowMessage);
	}	
}

function planPublish() {
	var row = $(datagridId).datagrid('getChecked');
	var ids=[];
	var inum=0;
	for(var r in row){
		ids.push(row[r]['id']);			
	}
	if(ids.length>0){
		ids=ids.join(",");
	}else{
		showMessage("Error",noSelectedRowMessage);
		return;
	}
	
	$.messager.confirm('计划发布', "确定要计划发布该视频吗？", function(r) {
		if (r) {
			location.href = "video/prePlanPublish?videoIds=" + ids;
		}
	});
	
}

function cancelPublish(id) {
	$.messager.confirm('特殊警告', "注意：取消可就没了啊，知道不。", function(r) {
		if (r) {
			$.ajax({
				url : 'video/cancelPublish',
				data : {
					'vid' : id
				},
				type : "post",
				dataType : "json",
				success : function(result) {
					window.location.reload();
				}
			});
		}
	});
}

//上传按钮
function upload() {
	location.href = "video/index";
}
$(function() {
	$('#starttime').datetimebox({
		showSeconds : false
	});
	$('#endtime').datetimebox({
		showSeconds : false
	});
	initShowVideos();
	datagridList();
});
function initShowVideos() {
	var selIndex = $("#selIndex").val();
	if (selIndex == null)
		return false;
	$("#publishStatus").get(0).selectedIndex = selIndex;
}
function showVideos() {
	var url = $("#publishStatus").val();
	var selIndex = $("#publishStatus").prop('selectedIndex');
	$("#selIndex").val(selIndex);
	if (url.indexOf("/") > 0) {
		location.href = url;
	} else {
		eval(url);
	}
}

function showActities(vid) {
	if (vid == 'undefined') {
		showMessage("Error", "不合法的操作！");
	}
	actvid = vid;
	$.post("video/getvideoactivities", {id: vid}, function (result) {
		var acts = $("input[name='activity']");
		var checked_acts = $("input[name='activity']:checked");
		for (var r = 0; r < checked_acts.length; r++) {
			checked_acts[r].checked = false;
		}
		if (result.length > 0) {
			for (var i = 0; i < result.length; i++) {
				for (var j = 0; j < acts.length; j++) {
					if (result[i].activityid == acts[j].value) {
						acts[j].checked = true;
					}
				}
			}
		}
	});
	$("#activityDialog").show();
	$("#activityDialog").dialog({
		title: '选择活动',
		width: 400,
		height: 300,
		closed: false,
		cache: true,
		modal: true
	});
}

var actvid = 0;
function selectActivity(obj) {
	var actid = obj.value;
	if (obj.checked) {
		$.post("video/modifyvideoactivities", {
			videoId: actvid,
			activityId: actid,
			type: 'add'
		}, function (result) {
			if (result["success"] == true) {
				$(datagridId).datagrid('reload'); // reload the user data
			} else {
				showMessage("Error", result["message"]);
			}
		});
	} else {
		$.post("video/modifyvideoactivities", {
			videoId: actvid,
			activityId: actid,
			type: 'del'
		}, function (result) {
			if (result["success"] == true) {
				$(datagridId).datagrid('reload'); // reload the user data
			} else {
				showMessage("Error", result["message"]);
			}
		});
	}
}

function addHotVideo(vid) {
	$.post('video/addHotVideoTop', {id: vid}, function (result) {
		if (result["success"] == true) {
			$(datagridId).datagrid('reload'); // reload the user data
		} else {
			showMessage("Error", result["message"]);
		}
	});
}

function removeHotVideo(vid) {
	$.post('video/removeHotVideos', {ids: vid}, function (result) {
		if (result["success"] == true) {
			$(datagridId).datagrid('reload'); // reload the user data
		} else {
			showMessage("Error", result["message"]);
		}
	});
}

//初始化活动下拉框
function initActivities() {
	var data;
	var url = "video/initActivites";
	$.ajax({
		url : url,
		type : "post",
		dataType : "json",
		success : function(result) {
			data = result.activities;
			$("#activities").empty();
			$("#activities").append("<option value=''>请选择活动</option>");
			$.each(data, function(n, activity) {
				$("#activities").append(
						"<option value='" + activity.id + "'>" + activity.title + "</option>");
			});
		}
	});
}
//编辑视频
function editVideo(id) {
	$("#editFrame").attr("src","video/modify_ui?id="+id);
	$("#dlg_edit").dialog('open').dialog('setTitle',"视频编辑");
}

function closeEditVideo() {
	$("#dlg_edit").dialog('close');
}

function afterEditVideo() {
	$("#dlg_edit").dialog('close');
	$("#videoform").submit();
}

//贴标
function changeLogoState(videoId, state) {
	$.ajax({
		url : 'video/changeLogoState',
		data : {
			'videoId' : videoId,
			'state' : state
		},
		type : "post",
		dataType : "json",
		success : function(result) {
			//alert("修改成功!"); 
			window.location.reload();
		}
	});
}

function playVideo(playerId,fileName){
		$(playdialogueId).dialog('open').dialog('setTitle',addTitle);
		$('#playerContainer').empty();
		var _player = null;
		
		var player = $('<div/>');
	    $(player).attr('id', 'pl'+playerId);
	    
	    $('#playerContainer').append(player);
	    var conf = {
	        file: '${video_play_url_prefix}'+fileName+'.m3u8',
	        image: '${img}',
	        height: 350,
	        width: 400,
	        autostart: true,
	        analytics: { enabled: false}
	    };
	    _player = jwplayer('pl'+playerId).setup(conf);
	}

function uploadpic(id){
	$(adddialogueId).dialog('open').dialog('setTitle',"上传封面");
	url = addUrl;
	$("#id").val(id);
	$('#hlsfiles').fileupload({
    	url: 'video/uploadpic',
        sequentialUploads: true,
        dataType: 'json',
        type:'post',
        crossDomain:true,
        done: function (e, data) {
        	uploadresult=data.result;
        	$("#videoPic").val(uploadresult["result"]);
        	var picPath = "<%=request.getContextPath()%>/download";
        	$("#picCut").attr("src",picPath + uploadresult["result"]);
        	$(".jcrop-holder img").attr("src",picPath + uploadresult["result"]);
        	initJcrop();
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
			if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
				showMessage( "Error","对不起，系统仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
				return false;
			}
        }
    });
}
//*********************START*****************************
var jcrop_api;
//初始化封面裁剪
function initJcrop() {
	$('#picCut').Jcrop({
		onRelease : releaseCheck,
		onChange:   showCoords
	}, function() {
		jcrop_api = this;
		jcrop_api.animateTo([ 0, 0, 241, 120 ]);
		jcrop_api.setOptions({ allowResize: !!this.checked });
		jcrop_api.setOptions({ allowSelect: !!this.checked });
	});
	$('#coords').on('change','input',function(e){
      var x1 = $('#x1').val(),
          x2 = $('#x2').val(),
          y1 = $('#y1').val(),
          y2 = $('#y2').val();
      jcrop_api.setSelect([x1,y1,x2,y2]);
    });
};

function releaseCheck(){
   jcrop_api.setOptions({ allowSelect: true });
};

function showCoords(c){
    $('#x1').val(c.x);
    $('#y1').val(c.y);
    $('#x2').val(c.x2);
    $('#y2').val(c.y2);
    $('#w').val(c.w);
    $('#h').val(c.h);
};

function cutImage(mydialogueId,myFormId){
	var x1 = $('#x1').val()*2;
	var y1 = $('#y1').val()*2;
	var w = $('#w').val()*2;
	var h = $('#h').val()*2;
	var imgPath = $("#videoPic").val();
	var id = $("#id").val();
	$.ajax({
      url: url,
      data:{id:id,imgPath:imgPath,x1:x1,y1:y1,w:w,h:h},
      type: "post",
      dataType: "json",
      success: function (data){
       	$(mydialogueId).dialog( 'close');
        location.href="video/checledVideos"; // reload the user data
      },
      error:function (){
      	showMessage( "Error",result[ "message"]);
      }
   });
}
function enter(e){
	var ev = document.all ? window.event : e;
    if(ev.keyCode==13) {
    	formSubmit();
    }
}

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
		    nowrap:false,
		    url:listUrl,
		    columns:[[
					{field:'ck',width : 100,checkbox:true},
					{field:'img',title:'缩略图',width : 100,height:150,formatter:function(value,row,index){
						var content = "<div style='float:left;'><div style='float:left;width:220px;height:150px;padding-right:10px;'> "
							+ "<div style='width:220px;height:150px;' id='playerContainer"+row.id+"' onclick='playVideo(\""+row.id+"\",\""+row.playKey+"\")'><img src='<%=request.getContextPath()%>/download"+row.videoPic+"' style='height:150px;width:200px;'/></div> "
							+ "</div>";
						return content;
					}},
					{field:'description',title:'视频描述',width : 200,formatter:function(value,row,index){
						var desc = "";
						if(row.description){
							desc=row.description;
							if(desc.length>20){
								desc = row.description.substring(0,20)+"...";
							}
						}
						return "<div style='width:100%;height:40%;' title='"+row.description+"'><h3 sytle='word-wrap:break-word;word-break:break-all;'>"+ desc + "</h3></div> ";
					}},
					{field:'actives',title:'参与活动',width : 200,formatter:function(value,row,index){
						var activity = "";
						if(row.actives){
							activity = row.actives;
						}
						return  "<div style='width:100%;height:30%;color:#969696;'>"+ activity +"&nbsp;</div> ";
					}},
					{field:'tag',title:'参与话题',width : 200},
					{field:'flowStat',title:'详情参数',width : 200,formatter:function(value,row,index){
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
						
						var content = "<span style='width:100%;height:30%;color:#969696;padding:5px 5px'>上传时间："+ new Date(row.createDate).toLocaleString() +"</span><br>"
							+ "<span style='width:100%;height:30%;color:#969696;padding:5px 5px'>上传者："+ row.uploader +"&nbsp;(ID:&nbsp;"+ row.creatorId+") </span><br>"
							+  "<span style='color:#969696;padding:5px 5px'>状态："+ stat +"</span><br> "
							+  "<span style='color:#969696;padding:5px 5px'>点赞："+ row.praiseCount +"个  评论："+ row.evaluationCount +"次</span><br> "
							+  "<span style='color:#969696;padding:5px 5px'>转发："+ row.forwardCount +"次</span><br> "	;
						return content;
					}},
					{field:'operation',title:'操作',width:150,formatter:function(value,row,index){
						var content = "<span style='color:#969696;padding:10px 10px'>"
   							+ "<a href='javascript:void(0)' onclick='showActities("+row.id+")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>加入活动</span><span class='l-btn-icon icon-add'>&nbsp;</span></span></a></span>";
						content += "<span style='width:100%;height:30%;color:#969696;padding:10px 10px'><a href='video/multidown?ids="+row.id+"'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>下载</span><span class='l-btn-icon icon-save'>&nbsp;</span></span></a></span> "
   					+ "<br>";
   					if(row.playRateToday>0){
   						content += "<span style='color:#969696;padding:10px 10px'>"
   							+ "<a href='javascript:void(0)' onclick='removeHotVideo("+row.id+")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>热门视频</span><span class='l-btn-icon icon-remove'>&nbsp;</span></span></a></span>";
   					}else{
   						content += "<span style='color:#969696;padding:10px 10px'>"
   							+ "<a href='javascript:void(0)' onclick='addHotVideo("+row.id+")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>热门视频</span><span class='l-btn-icon icon-add'>&nbsp;</span></span></a></span>";
   					}
   					if(row.flowStat!='delete'){
							content += "<span style='width:100%;height:30%;color:#969696;padding:10px 10px'><a href='javascript:void(0)' onclick='destroyUser("+row.id+")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>删除</span><span class='l-btn-icon icon-remove'>&nbsp;</span></span></a></span> "
						}
						content += "<br>";
						content += "<span style='color:#969696;padding:10px 10px'>"							
						+ "<a href='javascript:void(0)' onclick='editVideo("+row.id+")'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>编辑&nbsp;&nbsp;</span>"
						+ "<span class='l-btn-icon icon-edit'>&nbsp;</span>";
												
						content += "</span></a></span>";
						content += "<span style='color:#969696;padding:20px 10px'><a href='video/videoDetail?vid="+ row.id +"'><span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>查看</span><span class='l-btn-icon icon-search'>&nbsp;</span></span></a></span> ";
						
						content += "<br><span style='color:#969696;padding:10px 10px'><a href='video/downloadPic?id="+row.id+"'><span class='l-btn-left'><span class='l-btn-text'>下载封面&nbsp;</span></span></a></span> "
							+ "<span style='color:#969696;padding:10px 10px'><a href='javascript:void(0)' onclick='uploadpic("+row.id+")' ><span class='l-btn-left'><span class='l-btn-text'>上传封面</span></span></a></span> "
						
						content += "<br>";
						
						return  content;
					}}
			    ]],
		    onLoadSuccess:function(){
				$(datagridId).datagrid('clearSelections');
		    }
	});
}

function destroyUser(id) {
	if (id) {
		if (confirm("确定要执行删除操作吗？")) {
			$.post('video/logicremove', {ids: id}, function (result) {
				if (result["success"] == true) {
					doSearch(); // reload the user data
				} else {
					showMessage("Error", result["message"]);
				}
			});
		}
	} else {
		showMessage("Error", noSelectedRowMessage);
	}
}

function doSearch(){
	$(datagridId).datagrid('reload',getFormJson( searchFormId));
}
//*********************END*****************************
</script>
</head>
<body>

	<!-- 列表 -->
     <table id="tt" data-options="border:false,toolbar:'#tb'">
	 </table>
    
    <!-- 列表上面的按钮和搜索条件  -->
     <div id="tb" style="padding:5px;height:auto">
	    <div data-options="region:'north',border:false" style="height: 40px; padding-top: 5px; overflow: hidden;">
			<h2 style="float:left;padding-left:10px;margin: 1px">官方视频</h2>
			<span style="float:left;padding-left:20px;margin: 1px"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:upload()" plain="true">上传</a></span>
			<h2 style="float:left;padding-left:20px;margin: 1px">管理员视频</h2>
			<span style="float:left;padding-left:20px;margin: 1px"><a href="showvideo/listShows" class="easyui-linkbutton" >Blive秀视频</a></span>
		</div>
		<form action="" id="searchForm">
			<div>
				马甲&nbsp;&nbsp;
				<select class="easyui-combobox" name="filters['creatorId']" id="username">
			    		<option value="">请选择马甲用户</option>
				     <c:forEach var="item" items="${uploaduserlist }">
					 	<option value="${item['id'] }" <c:if test="${ruserid eq item['id']}">selected</c:if>>${item['username'] }</option>
					 </c:forEach>
				</select>
				&nbsp;
				发布时间
				<input id="starttime" name="filters['starttime']"/>
				~
				<input id="endtime" name="filters['endtime']"/>
				&nbsp;&nbsp;
				状态
				<select name="filters['flowStat']" >
		          <option value="">显示全部</option>
		          <option value="check_ok">审核通过</option>
		          <option value="uncheck">未审核</option>
		          <option value="check_fail">审核未通过</option>
		          <option value="delete">已删除</option>
				  <option value="published">计划发布</option>
		        </select>
				&nbsp;
				排行榜
				&nbsp;
				<select name="filters['rankAble']" id="rankAble">
					<option value="" >全部</option>
					<option value="1">禁止</option>
					<option value="0">允许</option>
				</select>
				&nbsp;活动&nbsp;
				<input class="easyui-combobox" id="activities" name="filters['activity']"
					   data-options="url: 'comboBox/getActivityCombobox',method: 'get',valueField:'id',textField:'text'">
				&nbsp;话题&nbsp;
				<input type="text" name="filters['label']" id="label" style="width:120px;" class="easyui-validatebox easyui-combobox" data-options="url:'comboBox/queryComboBoxLabels',method: 'get',valueField:'id',textField:'text'">
				&nbsp;<br>
				视频描述
				<input name="filters['videoName']"  value=""/>

			<!-- </div>
			<div> -->
				计划发布时间：
				<input id="planStartTime" class="easyui-datetimebox" name="filters['planStartTime']"/>
				~
				<input id="planEndTime" class="easyui-datetimebox" name="filters['planEndTime']"/>
				&nbsp;
				<input type="button" class="button blue bigrounded" onclick="doSearch()" style="width: 80px;height: 30px;" value="查询" />
			</div>
			
			<div style="padding-left:20px;margin-top:10px;">
		        <input style="float:left;margin:7px 3px 3px 8px;width:15px;height:18px;" type="checkbox" onclick="selectAll()" id="sall"/><span style="float:left;height:18px;line-height:18px;margin-top:4px;">全选 </span>

				&nbsp;
				<button class="button blue bigrounded" type="button" onclick="publishVideos()">直接发布</button>
				&nbsp;
				<button class="button blue bigrounded" type="button" onclick="planPublish()">计划发布</button>
				&nbsp;
				<button class="button green bigrounded" type="button" onclick="deleteAll()" style="width: 80px;height: 30px;">批量删除</button>
				&nbsp;
				<button class="button green bigrounded" type="button" onclick="downloadAll()" style="width: 80px;height: 30px;">批量下载</button>
			</div> 
		</form>
	</div>
<div style="padding-left:20px;width:97%;">

<form action="video/checledVideos" id="videoform" method="post">

<!-- 弹出的上传图片对话框 -->
<div id="dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
	<div class="ftitle">上传图片</div>
	<!-- 添加 -->
	<form id="fm" method="post" novalidate>
		<div class="fitem">
			<label>图片地址:</label>
			<input name="hlsfiles" id="hlsfiles" type="file" style="width:50%"></input>
			<input name="videoPic" id="videoPic"  type="hidden" style="width:50%"></input>
<!-- 			<input name="videoListPic" id="videoListPic"  type="text" style="width:50%" readonly="readonly"></input> -->
			<div style="margin-left:auto;margin-right:auto;width:90%;display:none" id="p" class="easyui-progressbar" ></div>
		</div>
		<input type="hidden" name="id" id="id"/>
		<!-- START -->
		<form id="coords" class="coords" onsubmit="return false;">
		    <div class="inline-labels">
			    <label>X1 <input type="text" size="4" id="x1" name="x1" readonly="readonly"/></label>
			    <label>Y1 <input type="text" size="4" id="y1" name="y1" readonly="readonly" /></label>
			    <label>X2 <input type="text" size="4" id="x2" name="x2" readonly="readonly" /></label><br>
			    <label>Y2 <input type="text" size="4" id="y2" name="y2"  readonly="readonly"/></label>
			    <label>W&nbsp; <input type="text" size="4" id="w" name="w" readonly="readonly" /></label>
			    <label>H&nbsp; <input type="text" size="4" id="h" name="h" readonly="readonly" /></label>
			    <br>
			    <img alt="封面裁剪" src="" name="picCut" id="picCut" style="width: 240px;height: 240px;">
		    </div>
		</form>
		<!-- END -->
	</form>
</div>
<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="cutImage('#dlg','#fm')">保存</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div> 
<!-- 弹出的添加或者编辑对话框 -->
<div id="videoPlay" class="easyui-dialog" style="width:415px;height:390px;" closed="true">
	<div id="playerContainer"></div>
</div>
<br/>

<!-- 编辑弹出窗 start -->
<div id="dlg_edit" class="easyui-dialog" closed="true">
	<iframe id="editFrame" style="width:900px;height:820px;"></iframe>
</div>
<!-- 编辑弹出窗 end -->
</div>
<!--加入活动弹出框 start-->
	<div id="activityDialog" style="display:none">
		<ul style="display:table-row-group;">
			<c:forEach items="${activites}" var="activity" varStatus="status">
			<c:if test="${status.index%3==0 && status.index!=0}">
		</ul>
		<ul style="display:table-row-group;">
			</c:if>
			<li sytle="text-align:left;display:table-cell;"><input type="checkbox" value="${activity.id}" name="activity"
																   onclick="selectActivity(this)">&nbsp;${activity.title}
			</li>
			</c:forEach>
		</ul>
	</div>
</body>
</html>