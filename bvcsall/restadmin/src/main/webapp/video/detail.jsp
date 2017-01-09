<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
 -->
<script src="js/fp/vendor/jquery.ui.widget.js"></script>
<script src="js/fp/jquery.iframe-transport.js"></script>
<script src="js/fp/jquery.fileupload.js"></script>

<script type='text/javascript' src='js/players/bootstrap.min.js'></script>
<script type='text/javascript' src='js/players/jwplayer.js'></script>
<script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
<title>视频编辑</title>
<style type="text/css">
		#fm{
		margin:0;
		padding:10px 30px;
		}
		.ftitle{
		font-size:14px;
		font-weight:bold;
		padding:5px 0;
		margin-bottom:10px;
		border-bottom:1px solid #ccc;
		}
		.fitem{
		margin-bottom:5px;
		}
		.fitem label{
		display:inline-block;
		width:80px;
		}
	 </style>
<script type="text/javascript">

var _player = null;
var _url = null;
$(document).ready(function () {
    var player = $('<div/>');
    $(player).attr('id', 'player_id');
    $('#div_container').append(player);
    var conf = {
        file: '${video_play_url_prefix}${os[7]}.m3u8',
        image: '${img}',
        height: 230,
        width: 450,
        autostart: true,
        analytics: { enabled: false}
    };
    _player = jwplayer('player_id').setup(conf);
});

function clickupload(){
	var id;
	$("#playId").val(playId);
	var formjson=getFormJson( '#myform');
	if($.trim(playId)==""){
		showMessage("Error","文件上传失败，请重新上传文件后，再点击确认上传");
		return;
	}
	$.ajax({
	      url: 'video/uploadsave',
	      data: formjson,
	      type: "post",
	      dataType: "json",
	      beforeSend: function(){
	       return $( '#myform' ).form('enableValidation').form( 'validate' );
	      },
	      success: function (result){
	        location.href="user/userlist";
	      }
	  });
}


function clickok(){
	$.post("video/checkOk",getFormJson("#myform"),function(result){
		if (result["success"]==true){
			location.href="video/unchecklist";
		} else {
			showMessage("Error",result["message"]);
		}
	});
}

function clickfail(){
	$("#dlg").dialog('open').dialog('setTitle',"信息编辑");
}

function saveFailReason(mydialogueId,myFormId){
	$.ajax({
        url: "video/check_fail",
        data: getFormJson( fm),
        type: "post",
        dataType: "json",
        beforeSend: function(){
         return $( myFormId).form( 'validate');
        },
        success: function (result){
            if (result[ "success"]== true){
                $( mydialogueId).dialog( 'close'); // close the dialog
                location.href="video/unchecklist";
            } else {
                showMessage( "Error",result[ "message"]);
            }
        }
    });
}

$(function() {
	$(".tag").attr({"disabled":"disabled"});
	$(".subject").attr({"disabled":"disabled"});
});

function clickdelete(){
	id=${os[1]};
	$.messager.confirm('Confirm',"确定要删除吗？",function(r){
		if (r){
			$.post('video/logicremove',{ids:id},function(result){
				if (result["success"]==true){
					location.href="video/checkoklist";
				} else {
					showMessage("Error",result["message"]);
				}
			});
		}
	});
}
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post">
				<input type="hidden" name="id" id="id" value="${os[1]}"/>
				<input type="hidden" name="uploader" id="uploader" value="${os[5] }"/>
				<table cellpadding="5" style="margin:0 auto;width:900px;text-align: center;" class="form-body" >
				<tr>
					<td colspan="2" align="center">
					<div id='player_id'>载入中</div>
					</td>
				</tr>
				<tr>
					<td>视频标题:</td>
					<td>
					${os[2] }</input>
					</td>
				</tr>
				<tr>
					<td>上传者:</td>
					<td>
					${username }
					</td>
				</tr>
				<tr>
					<td>视频简介:</td>
					<td>
					 ${os[4] }
					</td>
				</tr>
				<tr>
					<td>上传时间:</td>
					<td style="">
					${createDate }
					</td>
				</tr>
				
				<!-- 
				<tr>
					<td>选择专题:</td>
					<td style="">
					<c:forEach var="item" items="${subjectList }" varStatus="idxStatus">
							<input type="checkbox" class="subject" name="subject" value="${item['id'] }"  <c:if test="${fn:contains(svList,item['id'])}">checked</c:if>/>${item['name'] }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<c:if test="${ (idxStatus.index+1) % 7 == 0}">
								<br/>
							</c:if>
					</c:forEach>
					</td>
				</tr>
				 -->
				
				<tr>
					<td>选择活动:</td>
					<td style="">
					 <c:forEach var="item" items="${activityList }" varStatus="idxStatus">
							<input type="checkbox" name="activity" value="${item['id'] }"
							
							<c:forEach var="subitem" items="${svList }">
							<c:if test="${item['id']==subitem}">
							checked
							</c:if>
							</c:forEach>
							
							/>
							${item['title'] }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							
							<c:if test="${ (idxStatus.index+1) % 7 == 0}">
								<br/>
							</c:if>
					 </c:forEach>
					</td>
				</tr>
				
				<%-- <tr>
					<td>选择标签:</td>
					<td>
					<c:forEach var="item" items="${vtagList }" varStatus="idxStatus">
						<span id="tag${idxStatus.index }" >
							<input type="hidden" name="tag" value="${item}"/>
							<a href="javascript:void(0)" onclick=""  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-lock'" style="color:#87304B;font-weight:bold;">${item}</a>
						</span>
						
						<c:if test="${ (idxStatus.index+1) % 7 == 0}">
									<br/>
						</c:if>
					 </c:forEach>
					</td>
				</tr> --%>
				<tr>
					<td colspan="2" align="center">
					<!-- <a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="clickdelete()">删除</a> -->
					<a href="javascript:history.go(-1)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">返回</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>