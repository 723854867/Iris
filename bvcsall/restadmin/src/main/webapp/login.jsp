<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>登陆</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link href="css/default.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="files/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="files/easyui/themes/icon.css">
<link rel="stylesheet" href="files/zTree/zTreeStyle.css" type="text/css">
<script src="js/jquery.min.js"></script>
<script src="files/easyui/jquery.easyui.min.js"></script>
<script src="files/easyui/jquery.easyui.patch.js"></script>
<script src="files/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="files/zTree/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="js/DateUtil.js"></script>
<script type="text/javascript" src="files/main.js"></script>
</head>
<script type="text/javascript">
	function clickButton() {
		$.ajax({
			url : 'login/loginAjax',
			data : getFormJson('#myform'),
			type : "post",
			dataType : "json",
			beforeSend : function() {
				return $('#myform').form('enableValidation').form('validate');
			},
			success : function(result) {
				if (result["result"] == "success") {
					location.href = "index/welcome?pid=16";
				} else {
					showMessage("Error", result["message"]);
				}
			}
		});
	}

	$(function() {
		$(':input[type="password"]').change(function(){
			$(":input[name='password']").val($(this).val());
		});
		document.onkeydown = function(e){  
		    var ev = document.all ? window.event : e;   
		    if(ev.keyCode==13) {        
		    	$(':input[type="password"]').trigger("change");                 
		        clickButton();
		    }                                           
		}                                               
	});                                                 
</script>
<body>
<div class="top_green">
	<img class="logo" src="images/logo.jpg" />
	<img class="icon" src="images/icon.jpg" />
</div>
<center>
		<div class="easyui-panel" style="width:530px;margin:0 auto;border:none;">
			<div style="padding:10px 60px 20px 60px">
				<form id="myform" method="post" class="easyui-form" data-options="novalidate:true">
					<table cellpadding="5">
						<tr>
							<td>
								<input class="username" id="username" type="text" placeholder="用户名" name="username" value="" data-options="required:true,prompt:'username',iconCls:'icon-man',iconWidth:38"/>
							</td>
						</tr>
						<tr>
							<td>
								<input id="password" type="password" placeholder="密码" name="password" value="" data-options="required:true,prompt:'password',iconCls:'icon-lock',iconWidth:38"/>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
								<a href="javascript:void(0)" class="linkbtn" data-options="iconCls:'icon-ok'" onclick="clickButton();"><h3>登 陆</h3></a>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</center>	
</body>
</html>
