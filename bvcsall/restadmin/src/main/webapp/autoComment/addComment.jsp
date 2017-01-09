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
	<title>新建自动评论内容</title>
	<script type="text/javascript">

		function flushPage() {
			window.location.reload();
		}

		function pageSub() {
			var comments=$('#comments').val();
			if(!comments){
				alert('评论内容!');
				return
			}

			$('#myform').submit();
		}



	</script>
	<script src="js/fp/vendor/jquery.ui.widget.js"></script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" action="autoComment/saveAddComment">
			<table cellpadding="5" style="margin:0 auto;width:90%;text-align: left;"  class="form-body" >
                <tr>
                    <td>评论内容: </td>
                    <td>
						<input id="commentId" name="commentId" type="hidden" value="${autoComment.id}">
                        <textarea name="comments" id="comments"  class="" cols="180" rows="20"   >${autoComment.comment}</textarea>
						<br>(PS: 新建的自动评论内容默认为生效状态)
                    </td>
                </tr>

				<tr>
					<td colspan="2" align="center">
						<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="pageSub()">保存</a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:history.back();">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>