<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>直播参数管理</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/> 
<style type="text/css">

#page a{
margin-left:10px;
height:30px;
width:100px; 
} 
</style> 
<script type="text/javascript">

function paramEdit(type, count){
	
	window.location='liveParam/paramEdit?type=' + type + '&count=' + count;
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

function deleteParam(type, count){
    if (confirm("确定要删除这条数据吗？删除后不可恢复")) {
        $.ajax({
            url:'liveParam/deleteParam',
            data:{type: type, count: count},
            type:"post",
            dataType:"json",
            success:function (result){
                if (result == 'ok') {
                    window.location.reload();
                } else {
                    alert("删除失败！");
                }
            }
        });
    }
}

function changeLiveSwitch(value){
    $.ajax({
        url:'liveParam/changeLiveSwitch',
        data:{value: value},
        type:"post",
        dataType:"json",
        success:function (result){
            if (result == 'ok') {
               if (value == 1) {
                   $("#liveBtn").html("<span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>关闭直播开关</span><span class='l-btn-icon icon-remove'>&nbsp;</span></span>");
                   $('#liveBtn').attr("href", "javascript:changeLiveSwitch(0);");
               } else {
                   $("#liveBtn").html("<span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>开启直播开关</span><span class='l-btn-icon icon-add'>&nbsp;</span></span>");
                   $('#liveBtn').attr("href", "javascript:changeLiveSwitch(1);");
               }
            } else {
                alert("开启直播开关失败！");
            }
        }
    });
}

function changeLivePushSwitch(value){
    $.ajax({
        url:'liveParam/changeLivePushSwitch',
        data:{value: value},
        type:"post",
        dataType:"json",
        success:function (result){
            if (result == 'ok') {
                if (value == 1) {
                    $("#livePushBtn").html("<span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>关闭直播推送开关</span><span class='l-btn-icon icon-remove'>&nbsp;</span></span></span>");
                    $('#livePushBtn').attr("href", "javascript:changeLivePushSwitch(0);");
                } else {
                    $("#livePushBtn").html("<span class='l-btn-left l-btn-icon-left'><span class='l-btn-text'>开启直播推送开关</span><span class='l-btn-icon icon-add'>&nbsp;</span></span>");
                    $('#livePushBtn').attr("href", "javascript:changeLivePushSwitch(1);");
                }
            } else {
                alert("开启直播开关失败！");
            }
        }
    });
}


</script>
<script type="text/javascript" src="js/admin/query_live_param.js"></script>
</head>
<body>

<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; height: 110px; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px;overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">直播参数管理</h2>

    </div>
    <div id="newComment" style="padding:5px;height:auto">
        <div style="margin-bottom:5px">
            <a href="liveParam/addParam" class="easyui-linkbutton" iconCls="icon-add" onclick="" plain="true">新建</a>

            <c:if test="${liveSwitch == 0}">
                <a id="liveBtn" href="javascript:changeLiveSwitch(1);" class="easyui-linkbutton" iconCls="icon-add">开启直播开关</a>
            </c:if>
            <c:if test="${liveSwitch == 1}">
                <a id="liveBtn" href="javascript:changeLiveSwitch(0);" class="easyui-linkbutton" iconCls="icon-remove">关闭直播开关</a>
            </c:if>
            <c:if test="${livePushSwitch == 0}">
                <a id="livePushBtn" href="javascript:changeLivePushSwitch(1);" class="easyui-linkbutton" iconCls="icon-add">开启直播推送开关</a>
            </c:if>
            <c:if test="${livePushSwitch == 1}">
                <a id="livePushBtn" href="javascript:changeLivePushSwitch(0);" class="easyui-linkbutton" iconCls="icon-remove">关闭直播推送开关</a>
            </c:if>
            <a href="javascript:superAdminDlg();" class="easyui-linkbutton">超管名单</a>
        </div>
    </div>
</div>

<div id="whiteListDlg" class="easyui-dialog" style="width:500px;height:600px;" data-options="modal: true,cache:false" closed="true" buttons="#userbutton">
	<table id="userTable" data-options="border:false,toolbar:'whiteListToolbar'" style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
	</table>
	<div id="whiteListToolbar" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
	  <div style="height: 40px; padding-top: 5px; overflow: hidden;">
	  	<label>添加用户</label><input class="easyui-numberbox" id="userId" data-options="validType:'int',min:1" value=""> <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="addUser()">添加</a>
	  </div>
	
	</div>
</div>

<div id="userbutton" style="text-align: right;margin-top: 5px;">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="removeUsers()">移除</a>
</div>
</body>
</html>