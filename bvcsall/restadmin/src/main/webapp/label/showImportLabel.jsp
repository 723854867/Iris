<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title>最热视频</title> 
<meta http-equiv="content-type" content="text/html; charset=utf-8"/> 
<style type="text/css">

#page a{
margin-left:10px;
height:30px;
width:100px; 
} 
</style> 
<script type='text/javascript' src='js/players/bootstrap.min.js'></script>
<script type='text/javascript' src='js/players/jwplayer.js'></script>
<script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>

<script type="text/javascript">

function setDefaultPageState(){
	$("#size").val('');
	$("#page").val('');
}

function selectPageSize(){
	setDefaultPageState();
	$("#size").val($("#selectsize").val());
	$("#videoform").submit();
}

function selectPage(p){
	$("#page").val(p); 
	$("#videoform").submit();
}

function selectStatus(p){
	$("#status").val(p); 
	$("#keyword").val(""); 
	$("#videoform").submit();
}

function selectAll(){   
	var ls = document.getElementsByName("ids");
	for(var i=0;i<ls.length;i++){
		if($("#sall").is(':checked')){ 
			ls[i].checked = true;
		}else{ 
			ls[i].checked = false;
		}
	} 
}






function doClickSearch(value,name){
	setDefaultPageState();
	$("#videoform").attr("method","POST"); 
	$("#videoform").submit();
}

function newLabelNameAdd(){ 
	$.ajax({
	      url:'activity/importFromTagToLabel', 
	      type:"post",
	      dataType:"json",
	      success:function (result){  
	    	  if(result=='ok'){
	    		  alert('导入成功');
	    		  window.location.reload(); 
	    	  }
	    	
	      }
	  }); 
}

function newLabelNameVideoTagAdd(){ 
	$.ajax({
	      url:'activity/importFromVideoTagToLabel', 
	      type:"post",
	      dataType:"json",
	      success:function (result){  
	    	  if(result=='ok'){
	    		  alert('导入成功');
	    		  window.location.reload(); 
	    	  }
	    	
	      }
	  }); 
}


function generateVideoLabelRelation(){ 
	$.ajax({
	      url:'activity/generateVideoLabelRelation', 
	      type:"post",
	      dataType:"json",
	      success:function (result){  
	    	  if(result=='ok'){
	    		  alert('生成成功');
	    		  window.location.reload(); 
	    	  }
	    	
	      }
	  }); 
}



</script>
</head>
<body>
<div style="padding-left:20px;width:97%">
	<div data-options="region:'north',border:false"
		 style="height: 40px; padding-top: 5px; background: url(./images/tiaobg.png) repeat-x; overflow: hidden;">
		<h2 style="float:left;padding-left:10px;margin: 1px">导入管理</h2>
	</div>
<form action="activity/labelList" id="videoform" method="post" >
<div>


<br>
<br>
&nbsp;&nbsp;<button type="button" onclick="newLabelNameAdd()" class="button green bigrounded" style="width: 300px;height: 30px;">从tag表导入到label</button>
&nbsp;&nbsp;<button type="button" onclick="newLabelNameVideoTagAdd()" class="button green bigrounded" style="width: 300px;height: 30px;">从视频tag属性导入到label</button>
&nbsp;&nbsp;<button type="button" onclick="generateVideoLabelRelation()" class="button green bigrounded" style="width: 300px;height: 30px;">生成视频话题关系</button>

</div> 
<hr/>
<input type="hidden" name="size" id="size" value="${pageinfo.size}"/>
<input type="hidden" name="page" id="page" value="${pageinfo.number}"/>
<input type="hidden" name="status" id="status" value="${status}"/>
<input type="hidden" name="videoIds" id="videoIds"/>
</form>

<br/>
<br/><br/><br/>
</div>
</body>
</html>