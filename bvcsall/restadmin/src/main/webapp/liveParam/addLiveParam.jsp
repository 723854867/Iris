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
	<title>直播参数设置</title>
	<script type="text/javascript">

		function flushPage() {
			window.location.reload();
		}

		function pageSub() {
			var paramType=$('#paramType').val();
			if(!paramType){
				alert('选择参数类型!');
				return
			}
			var paramCount=$('#paramCount').val();
			if(!paramCount){
				alert('填写参数阀值!');
				return
			}
			var description=$('#paramDescription').val();
			if(!description){
				alert('填写描述信息!');
				return
			}

			$('#myform').submit();
		}

		function typeChange() {

		}

	</script>
	<script src="js/fp/vendor/jquery.ui.widget.js"></script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" action="liveParam/saveParam">
			<table cellpadding="5" style="margin:0 auto;width:50%;text-align: left;"  class="form-body" >
                <tr>
                    <td>参数类型: </td>
                    <td>
						<c:choose>
							<c:when test="${param.type > 0}">
								<c:if test="${param.type==1}">
									<input id="paramType" name="paramType" type="hidden" value="1" readonly="readonly" />
									<input type="text" value="观看人数" readonly="readonly" />
								</c:if>
								<c:if test="${param.type==2}">
									<input id="paramType" name="paramType" type="hidden" value="2" readonly="readonly" />
									<input type="text" value="获得点赞数" readonly="readonly" />
								</c:if>
							</c:when>
							<c:otherwise>
								<select id="paramType" name="paramType" >
									<option value="0" <c:if test="${param.type==0}">selected</c:if> >请选择</option>
									<option value="1" <c:if test="${param.type==1}">selected</c:if> >观看人数</option>
									<option value="2" <c:if test="${param.type==2}">selected</c:if> >获得点赞数</option>
								</select>
							</c:otherwise>
						</c:choose>


                    </td>
                </tr>
				<tr>
					<td>参数阀值: </td>
					<td>
						<input id="paramCount" name="paramCount" type="text" value="${param.count}" <c:if test="${param.type>0}">readonly="readonly" </c:if> >
					</td>
				</tr>
				<tr>
					<td>描述信息: </td>
					<td>
						<textarea id="paramDescription" name="paramDescription" cols="70" >${description}</textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="pageSub()">保存</a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:location.href='liveParam/paramList';">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>