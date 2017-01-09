<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
<title>网综活动配置</title>
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

function clearRank(){
	var formjson=getFormJson('#myform');
	$.ajax({
	      url: 'wangzongConfig/clearRank',
	      data: {},
	      type: "post",
	      dataType: "json",
	      success: function (result){
	    	  if (result["result"] == "1"){
	    		  showMessage( "提示","榜单已清零");
	    	  }
	      }
	  });
	
}

function addInfo(){
	var uid = $("#uid").val();
	var giftIds = $("#giftIds").val();
	location.href="wangzongConfig/addInfo?uid="+uid+"&giftIds="+giftIds;	
}

function deleteInfo(member){
	location.href="wangzongConfig/deleteInfo?member="+member;	
}

</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="form-body" >
			<tr>
				<td>用户ID：<input type="text" name="uid" id="uid" value="">&nbsp;&nbsp;礼物ID：<input type="text" name="giftIds" id="giftIds" value=""><font color='red'>多个礼物id用逗号隔开</font></td>
				<td><a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="addInfo()">添加</a>&nbsp;&nbsp;<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="clearRank()">榜单清零</a></td>
			</tr>
		</table>
		<form id="myform" method="post" enctype="multipart/form-data">
			<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="form-body" >
				<tr>
					<td>用户ID</td>
					<td>礼物ID</td>
					<td>操作</td>
				</tr>
				  <c:forEach items="${info}" var="item">
						<td>${fn:split(item,'\\|')[0]}</td>
						<td>${fn:split(item,'\\|')[1]}</td>
						<td><a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="deleteInfo('${item}')">删除</a></td>
					</tr>
				</c:forEach>
			</table>
		</form>
	</div>
</div>
</body>
</html>