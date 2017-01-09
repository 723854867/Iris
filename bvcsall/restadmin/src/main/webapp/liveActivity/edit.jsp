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
<title>活动基本信息</title>
<script type="text/javascript">
var playId="";
$(function () {
	//启用表单验证
	$('.validatebox-text').bind('blur', function(){
		$(this).validatebox('enableValidation').validatebox('validate');
	});

    $('#hlsfiles').fileupload({
    	url: 'activity/activityupload',
        sequentialUploads: true,
        dataType: 'json',
        type:'post',
        crossDomain:true,
        done: function (e, data) {
        	uploadresult=data.result;
        	
        	$("#cover").val(uploadresult["result"]);
			console.log(data);
        	showMessage("通知","恭喜您上传成功");
        },
        progress:function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#p').progressbar('setValue', progress);
        },
        start:function (e) {
        	$('#p').show();
        	$('#p').progressbar('setValue', 0);
        },
        change:function(e,data){
        	var fileName = data.files[0].name;
        	var fileext = fileName.substring(fileName.lastIndexOf("."));
			fileext = fileext.toLowerCase();
			if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
				showMessage( "Error","对不起，系统仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
				return false;
			}
        }
    });

	$('#startTime').datetimebox({
		required : false,
		onShowPanel:function(){
			$(this).datetimebox("spinner").timespinner("setValue","00:00:00");
		}
	});
	$('#endTime').datetimebox({
		required : false,
		onShowPanel:function(){
			$(this).datetimebox("spinner").timespinner("setValue","23:59:59");
		}
	});
});


function clickupload(){
	var id;
	$("#playId").val(playId);
	var formjson=getFormJson( '#myform');
	if($.trim(playId)=="error"){
		showMessage("Error","文件上传失败，请重新选择文件上传");
		return;
	}
	if($.trim(playId)==""){
		showMessage("提示","请等待文件上传完成后，再点击确认");
		return;
	}
	$.ajax({
	      url: 'video/uploadsave',
	      data: formjson,
	      type: "post",
	      dataType: "json",
	      beforeSend: function(){
	       return $( '#myform' ).form('enableValidation').form( 'validate' );
	      },
	      success: function (result){
	        location.href="video/checledVideos";
	      }
	  });
}

function start(){
	var value = $('#p').progressbar('getValue');
	if (value < 100){
		value += Math.floor(Math.random() * 10);
		$('#p').progressbar('setValue', value);
		setTimeout(arguments.callee, 200);
	}
};
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" action="liveActivity/save">
				<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="form-body" >
				<input name="id" id="id"  type="hidden" style="width:50%" value="${activity['id']}"/>
				<tr>
					<td style="text-align: right">活动名称:</td>
					<td style="text-align: left"><input class="easyui-validatebox f1 easyui-textbox" name="title" id="title" value="${activity['title'] }" style="width:50%" data-options="validType:'length[0,20]',novalidate:true"></input></td>
				</tr>
                <tr>
                    <td style="text-align: right">活动描述:</td>
                    <td style="text-align: left"><input class="easyui-textarea" name="description" id="description" value="${activity['description'] }" style="width:50%" data-options="validType:'length[0,20]',novalidate:true"></input></td>
                </tr>
				<tr>
					<td style="text-align: right">封面:</td>
					<td style="text-align: left">
					<input name="hlsfiles" id="hlsfiles" type="file" style="width:50%"/>
					<input name="cover" id="cover"  type="hidden" style="width:50%" value="${activity['cover'] }"/>
					<div style="margin-left:0px;margin-right:auto;width:400px;display:none" id="p" class="easyui-progressbar" ></div>
					<img src="/restadmin/download${activity['cover'] }"/>
					</td>
				</tr>
				<tr>
					<td style="text-align: right">专属礼物:</td>
					<td style="text-align: left">
					<%--<select class="easyui-combobox easyui-validatebox" required="true" name="gift" id="gift" style="width:50%;">--%>
						<%--<option value="0">请选择</option>--%>
						<%--<c:forEach items="${gifts }" var="gift" >--%>
							<%--<option value="${gift.id}">${gift.name}</option>--%>
						<%--</c:forEach>--%>
					<%--</select>--%>
					<%--<div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p" class="easyui-progressbar" ></div>--%>
						<c:forEach items="${gifts}" var="gift" >
							<input name="gifts" type="checkbox" value="${gift.id}" <c:if test="${gift.state==1}"> checked="checked" </c:if> > ${gift.name} </input>
						</c:forEach>
					</td>
				</tr>

				<tr>
					<td style="text-align: right">排行榜显示个数:</td>
					<td style="text-align: left">
					<input name="showCountOfTop" id="showCountOfTop" value="${activity['showCountOfTop'] }" class=" easyui-textbox"  style="width:10%"/>
					</td>
				</tr>
                <tr>
                    <td style="text-align: right">权重:</td>
                    <td style="text-align: left">
                        <input name="orderNum" id="orderNum" value="${activity['orderNum'] }" class=" easyui-textbox"  style="width:10%"/>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right">类型:</td>
                    <td style="text-align: left">
						<c:if test="${activity['type']==2}">
							<input type="radio" name="type" value="1" > APP活动
							<input type="radio" name="type" value="2" checked="checked"> H5活动
						</c:if>

						<c:if test="${activity['type']!=2}">
							<input type="radio" name="type" value="1" checked="checked"> APP活动
							<input type="radio" name="type" value="2" > H5活动
						</c:if>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: right">活动时间:</td>
                    <td style="text-align: left">
                        <input name="startTime" id="startTime" value="${activity['startTime'] }" class="easyui-datetimebox"/> --
						<input name="endTime" id="endTime" value="${activity['endTime'] }" class="easyui-datetimebox"/>
                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p" class="easyui-progressbar" ></div>
                    </td>
                </tr>
				<tr>
					<td colspan="2" align="center">
					<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="$('#myform').submit();">保存</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>