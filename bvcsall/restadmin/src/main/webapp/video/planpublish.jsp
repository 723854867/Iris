<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>计划发布</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<style type="text/css">
table tr td {
	background-color: #fff;
	text-align: center;
	font-size: 14px;
}

table tr td a {
	padding-right: 20px;
	text-decoration: none;
	color: #000;
}

table {
	background-color: #000;
	width: 100%;
	border-width: 1px;
}
</style>
<script type="text/javascript">
	function up(obj) {
		var objParentTR = $(obj).parent().parent();
		var prevTR = objParentTR.prev();
		if (prevTR.length > 0) {
			prevTR.insertAfter(objParentTR);
		} else {
			return;
		}
	}
	function down(obj) {
		var objParentTR = $(obj).parent().parent();
		var nextTR = objParentTR.next();
		if (nextTR.length > 0) {
			nextTR.insertBefore(objParentTR);
		} else {
			return;
		}
	}

	function first(obj) {
		var objParentTR = $(obj).parent().parent();
		var f = $('#videotable tr:eq(1)');
		objParentTR.insertBefore(f);
	}
	function tail(obj) {
		var c = $('#videotable tr').size() - 1;
		var objParentTR = $(obj).parent().parent();
		var t = $('#videotable tr:eq(' + c + ')');
		objParentTR.insertAfter(t);
	}

	function planPublish() {
		var starttime = document.getElementsByName("starttime")[0].value;
		if (starttime == "") {
			alert("请选择发布开始时间");
			return false;
		}
		var sDate = new Date(starttime.replace("//-/g", "//"));
    	var eDate = new Date();
    	if(sDate < eDate){
    		alert("计划发布开始时间不能小于当前时间");
			return false;
    	}
		var ls = document.getElementsByName("ids");
		var sids = "";
		for (var i = 0; i < ls.length; i++) {
			if (sids == "")
				sids += ls[i].value;
			else
				sids += "," + ls[i].value;
		}
		if (sids != "") {
			$("#videoIds").val(sids);
			$("#videoform").submit();
		}
	}
</script>
</head>
<body>
	<div style="padding-left:20px;width:650px">
		<h2>计划发布</h2>
		<hr/>
		<form action="video/planPublish" id="videoform">
			发布起始时间：
			<input class="easyui-datetimebox" id="starttime" name="starttime" required style="width:200px" /> <br />
			<br /> 发布间隔时间：
			<select id="interver" name="interver">
				<option value="0">0</option>
				<option value="5">5</option>
				<option value="10">10</option>
				<option value="20">15</option>
				<option value="30">30</option>
				<option value="60">60</option>
			</select>
			(分钟) 
			<input type="hidden" name="videoIds" id="videoIds" />
		</form>
		<br />
		<br /> 发布顺序：
		<table id="videotable">
			<tr>
				<td>标题</td>
				<td>操作</td>
			</tr>
			<c:forEach items="${videolist}" var="video">
				<tr>
					<td>
						<input name="ids" type="hidden" value="${video.id}" />${video.name}
					</td>
					<td>
						<a href="javascript:void(null)" onclick="up(this)">上升一位</span>
						<a href="javascript:void(null)" onclick="down(this)">下降一位</span> 
						<a href="javascript:void(null)" onclick="first(this)">首位</span> 
						<a href="javascript:void(null)" onclick="tail(this)">末位</span>
					</td>
				</tr>
			</c:forEach>
		</table>
		<div style="float:right">
			<button type="button" onclick="planPublish()">&nbsp;&nbsp;确认&nbsp;&nbsp;</button>
			&nbsp;&nbsp;
			<button type="button" onclick="if(confirm('确认取消？')){location.href='video/checledVideos'}">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
		</div>
	</div>
	<br />
	<br />
</body>
</html>