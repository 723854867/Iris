<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>自动评论管理</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/> 
<style type="text/css">

#page a{
margin-left:10px;
height:30px;
width:100px; 
} 
</style> 
<script type="text/javascript">

function commentEdit(obj, commentId){
	
	window.location='autoComment/commentEdit?commentId='+commentId;
}

function updateCommentStatus(obj,commentId,actType){
	
	$.ajax({
	      url:'autoComment/updateCommentStatus',
	      data:{'commentId':commentId,'actType':actType},
	      type:"post",
	      dataType:"json",
	      success:function (result){ 
	    	window.location.reload();
	      }
	  });
}

function deleteComment(commentId){
    if (confirm("确定要删除这条数据吗？删除后不可恢复")) {
        $.ajax({
            url:'autoComment/deleteComment',
            data:{'commentId':commentId},
            type:"post",
            dataType:"json",
            success:function (result){
                window.location.reload();
            }
        });
    }
}



</script>
<script type="text/javascript" src="js/admin/query_auto_comment.js"></script>
</head>
<body>

<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; height: 110px; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px;overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">自动评论管理</h2>

    </div>
    <div id="newComment" style="padding:5px;height:auto">
        <div style="margin-bottom:5px">
            <a href="autoComment/addComment" class="easyui-linkbutton" iconCls="icon-add" onclick="" plain="true">新建评论</a>
        </div>
    </div>
</div>

</body>
</html>