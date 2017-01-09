<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>任务管理</title> 
<meta http-equiv="content-type" content="text/html; charset=utf-8"/> 
<style type="text/css">

#page a{
margin-left:10px;
height:30px;
width:100px; 
} 
</style> 
<script type="text/javascript">
function selectPageSize(){
	$("#size").val($("#selectsize").val());
	$("#taskForm").submit();
}

function selectPage(p){
	$("#page").val(p); 
	$("#taskForm").submit();
}

function selectStatus(p){
	$("#status").val(p); 
	$("#keyword").val(""); 
	$("#taskForm").submit();
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






function activityDetail(obj,activityId){
	
	$("#taskForm").attr("action","activity/activityDetail?activityId="+activityId);
	$("#taskForm").attr("method","GET");
	$("#taskForm").submit();
	
}




function doClickSearch(value,name){
	$("#taskForm").attr("method","POST"); 
	$("#taskForm").submit();
}


function showTaskAdd(obj,taskId){
	
	window.location='task/showTaskAdd?taskId='+taskId;
}

function updateTaskStatus(obj,taskId,status){
	
	$.ajax({
	      url:'task/updateTaskStatus', 
	      data:{'taskId':taskId,'status':status},
	      type:"post",
	      dataType:"json",
	      success:function (result){ 
	    	//alert("修改成功!"); 
	    	window.location.reload(); 
	      }
	  });
}

function changeWeight(taskId,obj){
	
	$.ajax({
	      url:'task/updateTaskWeight', 
	      data:{'taskId':taskId,'weight':obj.value},
	      type:"post",
	      dataType:"json",
	      success:function (result){ 
	    	//alert("修改成功!"); 
	    	window.location.reload(); 
	      }
	  });
}

</script>
<script type="text/javascript" src="js/admin/query_task.js"></script>
</head>
<body>

<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; height: 110px; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px;overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">任务管理</h2>

    </div>
    <div id="tb" style="padding:5px;height:auto">
        <div style="margin-bottom:5px">
            <a href="task/chooseTaskTemplate" class="easyui-linkbutton" iconCls="icon-add" onclick="" plain="true">新建任务</a>
        </div>
    </div>
</div>

<div id="dlg" class="easyui-dialog" style="width:550px;height:430px;padding:2px 5px;" closed="true"
     buttons="#dlg-buttons">
    <table class="table-doc" width="100%">
        <tr>
            <td>选择话题:</td>
            <td>
                <input name=labelName id="labelName" readonly="readonly" value=""
                       onfocus="showLabelDialog();">
                <input name="labelId" id="labelId" type="hidden" value="">
            </td>
        </tr>
        <tr>
            <td>排序权重:</td>
            <td>
                <input name="displayOrder" id="displayOrder" class="easyui-numberbox" min="1" value="">
            </td>
        </tr>
    </table>
</div>
<!-- 对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertHotLabel()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 弹出的选择话题对话框 -->
<div id="label_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:2px 5px" closed="true"
     buttons="#vdlg-buttons">
    <table id="labelTable" data-options="border:false,toolbar:'#tb_label'">
    </table>
    <div id="tb_label">
        <form id="searchForm">
            <table>
                <tr>
                    <td><label>话题名称：</label></td>
                    <td>
                        <input type="text" name="keyword" id="keyword">
                    </td>
                    <td>
                        <a href="javascript:;" onclick="labelListSearch()" class="easyui-linkbutton"
                           iconCls="icon-search">搜索</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div id="vdlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectLabel();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#label_dlg').dialog('close')">取消</a>
</div>


<div style="padding-left:20px;width:97%">
<div style="">
<h2>任务管理</h2>
</div> 
<div id="tb" style="padding:5px;">
<hr/>  
	<div>
		<table style="width:100%">
			<tr>
				<td>
					<a href="task/chooseTaskTemplate" class="easyui-linkbutton" iconCls="icon-add" onclick="" plain="true">新建任务</a>
				</td>
				<td>
					<form action="task/taskList" id="taskForm" method="p" >
						<div style="float:right;width:250px;text-align:right">每页显示条数
							<select id="selectsize" onchange="selectPageSize()">
					          <option <c:if test="${pageinfo.size==10}">selected</c:if>  value="10">10</option> 
					          <option <c:if test="${pageinfo.size==20}">selected</c:if> value="20">20</option>
					          <option <c:if test="${pageinfo.size==50}">selected</c:if> value="50">50</option>
					          <option <c:if test="${pageinfo.size==100}">selected</c:if> value="100">100</option>
					        </select>
					    </div>
						<input type="hidden" name="size" id="size" value="${pageinfo.size}"/>
						<input type="hidden" name="page" id="page" value="${pageinfo.number}"/>
					</form>
				</td>
			</tr>
		</table>
	</div>
	<hr/>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
	<tr style="line-height: 20px;" >
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			id
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			标题
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			描述
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			类型
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			子类型
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			任务值
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			个数
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			奖励(积分/钻石)
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			前置任务id
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			权重
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			过期时间
		</th>
		<th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
			操作
		</th>
		
	</tr>
	<c:forEach items="${taskList}" var="task" varStatus="status" >
					<tr>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
							${task.id}
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
							${task.title}
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
							${task.description}
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;width:80px;" >
							<c:choose>
								<c:when test="${task.typeOne eq 1 }">
									日常任务
								</c:when>
								<c:when test="${task.typeOne eq 2 }">
									限时任务
								</c:when>
								<c:when test="${task.typeOne eq 3 }">
									一次性任务
								</c:when>
								<c:when test="${task.typeOne eq 4 }">
									特殊任务
								</c:when>
							
							</c:choose>
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;width:160px;" >
							<c:choose>
								<c:when test="${task.typeTwo eq 1 }">
									发布一个视频,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 41 }">
									参与1个活动M次,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 21 }">
									关注黄V,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 22 }">
									关注蓝V,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 23 }">
									关注绿V,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 31 }">
									评论一次视频,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 32 }">
									赞一次视频,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 33 }">
									转发一次视频,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 1001 }">
									参加特定活动,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 2001 }">
									完善个人资料,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 2021 }">
									粉丝达到50,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 2022 }">
									粉丝达到100,赠送N积分
								</c:when>
								<c:when test="${task.typeTwo eq 2023 }">
									粉丝达到200,赠送N积分
								</c:when>
								
								<c:when test="${task.typeTwo eq 2023 }">
									粉丝达到200,赠送N积分
								</c:when>
								
								<c:when test="${task.typeTwo eq 51 }">
									签到任务
								</c:when>
							</c:choose>
							
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;" >
							${task.taskValue}
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;width:50px;" >
							${task.num}
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;width:90px;" >
							${task.integral}
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;width:70px;" >
							${task.previousTaskId}
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;width:110px;" >
							<input id="${task.id}_weight" name="${task.id}_weight" value="${task.weight}"  size="10" onblur="changeWeight('${task.id}',this)" ></input>
							
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;width:130px;" >
							<fmt:formatDate value="${task.deadLine}" pattern="yyyy-MM-dd HH:mm:ss" />
							
						</td>
						<td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000; width:180px;" >
							<a class="easyui-linkbutton" href="task/showTaskAdd?taskId=${task.id }" style="width:80px;height: 25px;" id="mdOM${active.id}" name="mdOM${active.id}" type="button" title="编辑" >编辑</a>
							&nbsp;
							<input class="easyui-linkbutton" style="width:80px;height: 25px;" id="mdOM${active.id}" name="mdOM${active.id}" type="button" value="删除" onclick="updateTaskStatus(this,'${task.id}',1);"  />
							
						</td>
						
					</tr>
					
	</c:forEach>
</table> 
<br/>
<div style="float:left">
	<span id="page">  
	<a href="javascript:selectPage('1');">首页</a>
	<c:if test="${pageinfo.number>0}">
	<a href="javascript:selectPage('${pageinfo.number}');">上一页</a>
	</c:if>
	<c:if test="${pageinfo.totalPages<=7}"> 
	 	<c:set var="endNum" scope="request" value="${pageinfo.totalPages}"/>
	</c:if>
	<c:if test="${pageinfo.number>=4&&pageinfo.number+3<=pageinfo.totalPages}"> 
	 	<c:set var="endNum" scope="request" value="${pageinfo.number+3}"/>
	</c:if>
	<c:if test="${pageinfo.number>=4&&pageinfo.number+3>pageinfo.totalPages}"> 
	 	<c:set var="endNum" scope="request" value="${pageinfo.totalPages}"/>
	</c:if>
	<c:if test="${pageinfo.number<=3&&pageinfo.totalPages>7}">
	 	<c:set var="endNum" scope="request" value="7"/>
	</c:if> 
	<c:forEach var="i" begin="${pageinfo.number>3?pageinfo.number-3:1}"  end="${endNum}" step="1">  
		<a href="javascript:selectPage('${i}');">${i}</a>  
	</c:forEach> 
	<c:if test="${(pageinfo.number+1)<pageinfo.totalPages}">
	<a href="javascript:selectPage('${pageinfo.number+2}');">下一页</a>
	</c:if>
	<a href="javascript:selectPage('${pageinfo.totalPages}');">尾页</a>
	</span>
</div>
<br/><br/><br/>
</div>
</div>
</body>
</html>