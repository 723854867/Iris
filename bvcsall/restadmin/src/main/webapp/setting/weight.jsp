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
<title>权重设置</title>
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
var lisVideotUrl="video/searchVideoListPage";
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
	
	});

	function clickupload(){
		
		var target = $("#targetid").val();
		if(target=='' || target=='undefind'){
			showMessage("Error","指定操作ID不能为空");
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

</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form method="post" enctype="multipart/form-data" action="setting/rankWeight">
			<h3 align="center">排行榜权重设置</h3>
			<table cellpadding="5" style="margin:0 auto;width:1200px;text-align: left;" class="form-body">
				<tr>
					<th>当日视频热度榜设置</th>
				</tr>
				<tr>
					<td>
						播放次数权重：
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" name="playcount" data-options="validType:'double',min:0,precision:2" required="true" value="${dayHotVideo.day_playcount_weight}" >
					</td>

					<td>
						点赞次数权重：
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" precision="2" name="praiseCount" data-options="validType:'Number'" required="true" value="${dayHotVideo.day_praisecount_weight}" >
					</td>
					<td>
						评论数权重：
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" precision="2" name="evaluationCount" data-options="validType:'Number'" required="true" value="${dayHotVideo.day_evaluationcount_weight}" >
					</td>
				</tr>
				<tr>
					<th>当日人气榜设置</th>
				</tr>
				<tr>
					<td>
						上传视频数权重：
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" precision="2" name="videoCount" data-options="validType:'double'" required="true" value="${dayPopularity.day_videocount_weight}" >
					</td>
					<td>
						当日粉丝数权重：
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" precision="2" name="fansCount" data-options="validType:'Number'" required="true" value="${dayPopularity.day_fanscount_weight}" >
					</td>
				</tr>
			</table>
		</form>
		<br>
		<hr>
		<h3 align="center">最热视频权重设置</h3>
		<form method="post" enctype="multipart/form-data" action="setting/weight">
			<table cellpadding="5" style="margin:0 auto;width:1200px;text-align: left;" class="form-body" >
				<thead>
					<td style="width: 20%;"></td>
					<td style="width: 40%;">最热视频权重设置</td>
					<td style="width: 40%;">活动视频权重设置</td>
				</thead>
				<tr>
					<td>点赞权重：</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" name="all_praise" data-options="validType:'Number'" required="true" value="${hotVideo.praise_weight}" >
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" name="act_praise" data-options="validType:'Number'" required="true" value="${hotActivity.praise_weight}" >
					</td>
				</tr>
				<tr>
					<td>评论权重：</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" name="all_evaluation" data-options="validType:'Number'" required="true" value="${hotVideo.evaluation_weight}" >
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" name="act_evaluation" data-options="validType:'Number'" required="true" value="${hotActivity.evaluation_weight}" >
					</td>
				</tr>
				<!-- 
				<tr>
					<td>发布时间权重：</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" name="all_pubtime" data-options="validType:'Number'" required="true" value="${hotVideo.publish_weight}" >
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" name="act_pubtime" data-options="validType:'Number'" required="true" value="${hotActivity.publish_weight}" >
					</td>
				</tr>
				 -->
				<tr>
					<td>播放权重：</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" name="all_playcount" data-options="validType:'Number'" required="true" value="${hotVideo.play_weight}" >
					</td>
					<td>
						<input class="easyui-validatebox easyui-numberbox" name="act_playcount" data-options="validType:'Number'" required="true" value="${hotActivity.play_weight}" >
					</td>
				</tr>
				<tr>
					<td colspan="3" align="center">
						<input type="submit" style="width: 50px;height:25px" class="button green bigrounded" data-options="iconCls:'icon-ok'" onclick="clickupload()" value="保存"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>

</body>
</html>