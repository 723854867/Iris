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
	var formjson=getFormJson( '#modify_form');
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
	       return $( '#modify_form' ).form('enableValidation').form( 'validate' );
	      },
	      success: function (result){
	        location.href="user/userlist";
	      }
	  });
}


function submit(){
	$.post("video/modify",getFormJson("#modify_form"),function(result){
		if (result == "SUCCESS"){
			parent.afterEditVideo();
		} else {
			showMessage("Error",result);
		}
	});
}
$(function () {
	//启用表单验证
	$('.validatebox-text').bind('blur', function(){
		$(this).validatebox('enableValidation').validatebox('validate');
	});	
	$("#dbtag").toggle();
});

function togglecallback(){
	at=$("#updatebutton").text();
	if($.trim(at)=="修改"){
		$("#updatebutton").text("收缩");
	}else if($.trim(at)=="收缩"){
		$("#updatebutton").text("修改");
	}
}

function clickbutton(tagid){
	$("#"+tagid).remove();
}

var tempradio= null; 
function changeRadio(checkedRadio){
	if (tempradio == checkedRadio) {
		tempradio.checked = false;
		tempradio = null;
	} else {
		tempradio = checkedRadio;
	}
}

function selectByUserType(value) {
	if (value == 1) {
		$("#ruserId").combogrid({
			panelWidth:450,
			idField:'id',
			textField:'name',
			url:'combogrid/getUserCombogridByType?type=1',
			value:'',
			columns:[[
				{field:'id',title:'用户ID',width:60},
				{field:'name',title:'昵称',width:120},
				{field:'username',title:'用户名',width:120},
				{field:'phone',title:'手机号码',width:140}
			]]
		});
	} else {
		$("#ruserId").combogrid({
			panelWidth:450,
			idField:'id',
			textField:'name',
			url:'combogrid/getUserCombogridByType?type=2',
			value:'',
			columns:[[
				{field:'id',title:'用户ID',width:60},
				{field:'name',title:'昵称',width:120},
				{field:'username',title:'用户名',width:120},
				{field:'phone',title:'手机号码',width:140}
			]]
		});
	}
}
</script>
</head>
<body>
<div class="easyui-layout1" data-options="fit:true1">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="modify_form" method="post">
				<input type="hidden" name="id" id="id" value="${os[1]}"/>
				<input type="hidden" name="uploader" id="uploader" value="${os[5] }"/>
				<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="form-body" >
				<tr>
					<td colspan="2" align="right" style="text-align: right;">
					<div id='player_id'>载入中</div>
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td>视频标题:</td> -->
<!-- 					<td> -->
<!-- 					<input class="validatebox-text f1 easyui-textbox" name="title" id="title" style="width:50%" value="${os[2] }" data-options="validType:'length[0,10]'" required="true"></input> -->
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
					<td style="text-align: right;">视频描述:</td>
					<td style="text-align: left;">
					 <input class="validatebox-text easyui-textbox" name="description" id="description" value="${os[4] }" data-options="multiline:true,validType:'length[0,20]'" required="true" style="width:50%;height:100px" ></input>
					</td>
				</tr>
					<tr>
						<td style="text-align: right;">用户类型:</td>
						<td style="text-align: left;">
							<select id="select_user">
								<option value="1" onclick="selectByUserType(1)" <c:if test="${allowPublish eq '1'}">selected="selected"</c:if>>马甲用户</option>
								<option value="2" onclick="selectByUserType(2)" <c:if test="${allowPublish eq '0'}">selected="selected"</c:if>>Blive用户</option>
							</select>
						</td>
					</tr>
				<tr>
					<td style="text-align: right;">上传马甲:</td>
					<td style="text-align: left;">
					    <%--<select class="easyui-combobox" name="ruserId" id="ruserId"  style="width:50%">
						     <c:forEach var="item" items="${uploaduserlist }">
							 	<option value="${item['id'] }" <c:if test="${item['id'] eq ruserId}">selected</c:if>>${item['username']}</option>
							 </c:forEach>
						</select>--%>
						<input type="text" value="" class="easyui-combogrid" id="ruserId" name="ruserId" data-options="
                        panelWidth:450,
                        idField:'id',
                        textField:'name',
                        <c:if test="${allowPublish eq '1'}">
                        	url:'combogrid/getUserCombogridByType?type=1',
						</c:if>
						<c:if test="${allowPublish eq '0'}">
                        	url:'combogrid/getUserCombogridByType?type=2',
						</c:if>
                        value:'<c:out value="${ruserId}"/>',
                        columns:[[
                        {field:'id',title:'用户ID',width:60},
                        {field:'name',title:'昵称',width:120},
                        {field:'username',title:'用户名',width:120},
                        {field:'phone',title:'手机号码',width:140}
                        ]]">
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td>是否用于活动介绍:</td> -->
<!-- 					<td> -->
<!-- 					    <select class="easyui-combobox" name="introductionMark" id="introductionMark"  style="width:50%"> -->
<!-- 						 	<option value="0" <c:if test="${'0' eq introductionMark}">selected</c:if>>否</option> -->
<!-- 						 	<option value="1" <c:if test="${'1' eq introductionMark}">selected</c:if>>是</option> -->
<!-- 						</select> -->
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
					<td style="text-align: right;">是否贴标:</td>
					<td style="text-align: left;">
					 	<select class="easyui-combobox" name="isLogo" id="isLogo"  style="width:50%">
						 	<option value="0" <c:if test="${'0' eq isLogo}">selected</c:if>>否</option>
						 	<option value="1" <c:if test="${'1' eq isLogo}">selected</c:if>>是</option>
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">播放率:</td>
					<td style="text-align: left;">
						<input id="playRateToday" name="playRateToday" class="easyui-numberbox" value="${playRateToday}" min="0" max="0.9999" precision="4"   maxlength="5" size="5" />
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td>选择片头:</td> -->
<!-- 					<td style=""> -->
<!-- 					 <c:forEach var="item" items="${templateList }" varStatus="idxStatus"> -->
<!-- 						<img src="/restadmin/download${item['pic']}" width="60px" height="50px" title="${item['title']}"/> -->
<!-- 						<c:if test="${os[9] eq item['id']}"> -->
<!-- 							<input type="radio" name="templateId" onclick="changeRadio(this);" value="${item['id'] }" title="${item['title']}" checked="checked"/>&nbsp;&nbsp; -->
<!-- 						</c:if> -->
<!-- 						<c:if test="${!(os[9] eq item['id'])}"> -->
<!-- 							<input type="radio" name="templateId" onclick="changeRadio(this);" value="${item['id'] }" title="${item['title']}"/>&nbsp;&nbsp; -->
<!-- 						</c:if> -->
<!-- 					 </c:forEach> -->
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
					<td style="text-align: right;">上传时间:</td>
					<td style="text-align: left;">
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
					<td style="text-align: right;">选择活动:</td>
					<td style="text-align: left;">
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
					<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="submit()">保存</a>
					<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:parent.closeEditVideo();">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>