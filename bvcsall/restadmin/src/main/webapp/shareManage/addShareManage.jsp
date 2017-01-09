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
	<title>新建分享文案</title>
	<script type="text/javascript">

		function flushPage() {
			window.location.reload();
		}

		function pageSub() {
			var shareType=$('#shareType').val();
			if(!shareType || shareType==0){
				alert('请选择类型!');
				return
			}

//			var shareImg=$('#shareImg').val();
//			if(!shareImg){
//				alert('请上传图片!');
//				return
//			}
			var shareText=$('#shareText').val();
			if(!shareText){
				alert('请填写文案!');
				return
			}

			$('#myform').submit();
		}

		$(function() {

			$('#shareImgFile').fileupload({
				url: 'activity/activityUploadFile',
				sequentialUploads: true,
				dataType: 'json',
				type: 'post',
				crossDomain: true,
				done: function (e, data) {
					uploadresult = data.result;

					$("#shareImg").val(uploadresult["result"]);
					showMessage("通知", "恭喜您上传成功");
				},
				progress: function (e, data) {
					var progress = parseInt(data.loaded / data.total * 100, 10);
					$('#p').progressbar('setValue', progress);
				},
				start: function (e) {
					$('#p').show();
					$('#p').progressbar('setValue', 0);
				},
				change: function (e, data) {
					var fileName = data.files[0].name;
					var fileext = fileName.substring(fileName.lastIndexOf("."));
					fileext = fileext.toLowerCase();
					if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
						showMessage("Error", "对不起，系统仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
						return false;
					}
				}
			});

		})

	</script>
	<script src="js/fp/vendor/jquery.ui.widget.js"></script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" action="shareManage/saveShareManage">
			<table cellpadding="5" style="margin:0 auto;width:90%;text-align: left;"  class="form-body" >
				<tr>
					<input id="shareId" name="shareId" type="hidden" value="${shareManage.id}"/>
					<td>分享类型: </td>
					<td style="vertical-align: middle;text-align: left;">
						<select id="shareType" name="shareType" >
							<option value="0" <c:if test="${shareManage.shareType==0}">selected</c:if> >请选择</option>
							<option value="1" <c:if test="${shareManage.shareType==1}">selected</c:if> >首页</option>
							<option value="2" <c:if test="${shareManage.shareType==2}">selected</c:if> >排行榜</option>
							<option value="3" <c:if test="${shareManage.shareType==3}">selected</c:if> >活动页</option>
							<option value="4" <c:if test="${shareManage.shareType==4}">selected</c:if> >直播</option>
							<option value="5" <c:if test="${shareManage.shareType==5}">selected</c:if> >直播预告</option>
							<option value="6" <c:if test="${shareManage.shareType==6}">selected</c:if> >直播回放</option>
							<option value="7" <c:if test="${shareManage.shareType==7}">selected</c:if> >新歌声【学员榜】</option>
							<option value="8" <c:if test="${shareManage.shareType==8}">selected</c:if> >新歌声【综艺榜】</option>
							<option value="9" <c:if test="${shareManage.shareType==9}">selected</c:if> >新歌声【主播榜】</option>
							<option value="10" <c:if test="${shareManage.shareType==10}">selected</c:if> >拉票</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>分享图片: </td>
					<td valign="middle" style="vertical-align: middle;text-align: left;">
						<div style="float: left;">
							<input name="shareImgFile" id="shareImgFile" type="file"/>
							<input name="shareImg" id="shareImg" type="hidden" value="${shareManage.shareImg }"/>
							<div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
								 class="easyui-progressbar"></div>
						</div>
						<c:if test="${not empty shareManage.shareImg}">
							<div style="float: left;">
								<img style="border: 1px solid #CCCCCC;width: 70px;height: 70px;"
									 src="/restadmin/download${shareManage.shareImg }"/><br>
							</div>
						</c:if>
					</td>
				</tr>
				<tr>
					<td>分享标题: </td>
					<td style="vertical-align: middle;text-align: left;" >
						<input type="text" value="${shareManage.shareTitle}" style="width: 220px;" name="shareTitle" id="shareTitle" class="text-input">
					</td>
				</tr>
				<tr>
					<td>分享文案: </td>
					<td style="vertical-align: middle;text-align: left;" >
						<textarea name="shareText" id="shareText"  class="" cols="60" rows="5"   >${shareManage.shareText}</textarea>
					<div style="color: red;">用户昵称：{NickName}，活动名称：{event}</div>
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