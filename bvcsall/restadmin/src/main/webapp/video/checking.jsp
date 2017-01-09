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

$(function(){
	$("#dbtag").toggle();
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

function clickbutton(tagid){
	$("#"+tagid).remove();
}

function togglecallback(){
	at=$("#updatebutton").text();
	if($.trim(at)=="修改"){
		$("#updatebutton").text("收缩");
	}else if($.trim(at)=="收缩"){
		$("#updatebutton").text("修改");
	}
}
$(function () {
	//启用表单验证
	$('.validatebox-text').bind('blur', function(){
		$(this).validatebox('enableValidation').validatebox('validate');
	});	
});
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
					<input class="validatebox-text f1 easyui-textbox" name="title" id="title" style="width:50%" value="${os[2] }" data-options="validType:'length[0,10]',novalidate:true"></input>
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
					 <input class="validatebox-text easyui-textbox" name="description" id="description" value="${os[4] }" data-options="multiline:true,validType:'length[0,20]',novalidate:true" style="width:50%;height:100px" ></input>
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
							<input type="checkbox" name="subject" value="${item['id'] }"  <c:if test="${fn:contains(svList,item['id'])}">checked</c:if>/>${item['name'] }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
					<a href="javascript:void(0)" id="updatebutton" onclick="$('#dbtag').toggle('slow',togglecallback);"  class="easyui-linkbutton" data-options="plain:true" style="color:blue">修改</a>
					<c:forEach var="item" items="${vtagList }" varStatus="idxStatus">
						<span id="tag${idxStatus.index }" >
							<input type="hidden" name="tag" value="${item}"/>
							<a href="javascript:void(0)" onclick="clickbutton('tag${idxStatus.index }')"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-clear'" style="color:#87304B;font-weight:bold;">${item}</a>
						</span>
						
						<c:if test="${ (idxStatus.index+1) % 7 == 0}">
									<br/>
						</c:if>
					 </c:forEach>
					<br/>
					<br/>
					<span id="dbtag">
						 <c:forEach var="item" items="${tagList }" varStatus="idxStatus">
							<input type="checkbox" name="tag" value="${item['name'] }"/>${item['name'] }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<c:if test="${ (idxStatus.index+1) % 7 == 0}">
								<br/>
							</c:if>
					 </c:forEach>
					 </span>
					</td>
				</tr> --%>
				<tr>
					<td colspan="2" align="center">
					<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="clickok()">通过</a>
					<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="clickfail()">不合格</a>
					<a href="video/unchecklist" style="" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>

<!-- 弹出的不合格对话框 -->
	<div id="dlg" class="easyui-dialog" style="width:500px;height:180px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
		<div class="ftitle">审核不合格原因:</div>
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<input type="checkbox" name="failReason" value="视频内容不合格"/>视频内容不合格
				<input type="checkbox" name="failReason" value="文字内容不合格"/>文字内容不合格
				<input type="checkbox" name="failReason" value="音频内容不合格"/>音频内容不合格
				<input type="checkbox" name="failReason" value="其它"/>其它
			</div>
			<input type="hidden" name="id" id="id" value="${os[1] }"/>
		</form>
	</div>
	
	<!-- 添加对话框里的保存和取消按钮 -->
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFailReason('#dlg','#fm')">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
</body>
</html>