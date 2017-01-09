<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<title>视频上传</title>
<script type="text/javascript">

	
	
	
	
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" action="activity/activeAdd">
				<table cellpadding="5" style="margin:0 auto;width:100%;text-align: left;"  class="form-body" >
				<tr>
					<th>
						模板描述
					</th>
					<th>
						操作
					</th>
				</tr>
				<tr>
					<td>
						发布一个视频,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=1">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						参与1个活动M次,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=41">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						关注黄V,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=21">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						关注蓝V,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=22">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						关注绿V,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=23">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						评论一次视频,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=31">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						赞一次视频,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=32">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						转发一次视频,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=33">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						参加特定活动,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=1001">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						完善个人资料,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=2001">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						粉丝达到50,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=2021">创建</a>
					</td>
				</tr>
				
				<c:if test="${false }">
				<tr>
					<td>
						粉丝达到100,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=2022">创建</a>
					</td>
				</tr>
				
				<tr>
					<td>
						粉丝达到200,赠送N积分
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=2023">创建</a>
					</td>
				</tr>
				</c:if>
				
				<tr>
					<td>
						签到任务
					</td>
					<td>
						<a href="task/showTaskAdd?typeOne=1&typeTwo=51">创建</a>
					</td>
				</tr>
				
			</table>
		</form>
	</div>
</div>
</body>
</html>