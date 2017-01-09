<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理员信息</title>
<!-- 
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
 -->
 <link rel="stylesheet" type="text/css" href="css/baseCss.css">

<style type="text/css">
		#fm{
		margin:0;
		padding:10px 30px;
		}
		.ftitle{
		font-size:14px;
		font-weight:bold;
		padding:5px 0;
		margin-bottom:10px;
		border-bottom:1px solid #ccc;
		}
		.fitem{
		margin-bottom:5px;
		}
		.fitem label{
		display:inline-block;
		width:80px;
		}
		
		.dis{display:block;} 
		.undis{display:none;} 
		ol, ul { list-style:none } 
		.show { display:block } 
		.hide { display:none } 
		.highlight { color:#F30!important } 
		.important { font-weight:bold!important } 
		.center { text-align:center!important } 
		.nav_current{ float:left;background:#FFF; color:gray; } 
		.nav_link{ float:left;} 
		.nav_current:hover ,.nav_link:hover{ color:#FF6600;} 
		#dww-menu { width:978px; overflow:hidden; } 
		#dww-menu .mod-hd {height:60px; line-height:30px; position:relative;} 
		#dww-menu .mod-hd li { float:left; cursor:pointer; text-align:center; height:35px; line-height:35px; text-transform:uppercase;padding-right:10px} 
		#dww-menu .mod-bd { padding:5px 10px } 
		#dww-menu .mod-bd div {color:#BFBFBF; line-height:24px } 
		#dww-menu .mod-bd div.show { display:block } 
		#dww-menu .mod-bd div a { display:inline-block; padding:0 4px } 
	 </style>
<script type="text/javascript">
var deleteConfirmMessage="你确定要删除吗?";
var adddialogueId="#dlg";
var addTitle="播放视频";


$(function(){ 
	
});

function changeNav(o){
	//当前被中的对象设置css 
	o.className="nav_current"; 
	var j; 
	var id; 
	var e; 
	//遍历所有的页签，没有被选中的设置其没有被选中的css 
	for(var i=1;i<=2;i++){ //i<7 多少个栏目就填多大值 
		id ="nav"+i; 
		j = document.getElementById(id); 
		e = document.getElementById("sub"+i); 
		if(id != o.id){ 
			j.className="nav_link"; 
			e.style.display = "none"; 
		}else{ 
			e.style.display = "block"; 
		} 
	} 
}

function updatePass(myFormId){
	var new_password=$("#new_password").val();
	var new_repeat_password=$("#new_repeat_password").val();
	if($.trim(new_password)!=$.trim(new_repeat_password)){
		showMessage( "错误","新密码与重复密码不一致",3000);
		return;
	}

	$.ajax({
        url: "user/updatePasswordAjax",
        data: getFormJson( myFormId),
        type: "post",
        dataType: "json",
        beforeSend: function(){
         return $( myFormId).form( 'validate');
        },
        success: function (result){
            if (result[ "success"]== true){
            	showMessage( "成功提示","修改密码成功",3000);
            	$( myFormId).form("clear"); // close the dialog
            } else {
            	showMessage( "错误",result["result"],3000);
            }
        },
        error:function (result){
        	showMessage( "Error",JSON.stringify(result),5000);
        }
    });
}

function updateInfo(myFormId){
	var phone=$("#phone").val();
	var email=$("#email").val();
	
	$.ajax({
        url: "user/updateInfoAjax",
        data: getFormJson( myFormId),
        type: "post",
        dataType: "json",
        beforeSend: function(){
         return $( myFormId).form( 'validate');
        },
        success: function (result){
            if (result[ "success"]== true){
            	showMessage( "成功提示","修改成功",3000);
            } else {
            	showMessage( "错误",result["result"],3000);
            }
        },
        error:function (result){
        	showMessage( "Error",JSON.stringify(result),5000);
        }
    });
}
</script>
</head>
<body>
<div data-options="region:'north',border:false" style="height: 40px; padding-top: 5px; overflow: hidden;">
	<h2 style="float:left;margin-left:10px;">管理员信息</h2>
</div>

<div style="margin-left:30px;">
	<div id="dww-menu" class="mod-tab"> 
		<div class="mod-hd"> 
			<ul class="tab-nav"> 
				<li class="nav_current" id="nav1" onclick="changeNav(this)"><h3>个人信息</h3></li> 
				<li class="nav_link" id="nav2" onclick="changeNav(this)"><h3>修改密码</h3></li> 
			</ul>
		</div> 
	</div> 
	<div class="mod-bd">
		<div class="dis" id="sub1">
			<form id="infofm" method="post" novalidate>
				<div class="fitem">
					<label>用户名:</label> <span>${user.username}</span>
				</div>
				<div class="fitem">
					<label>最后登录时间:</label> <span>${user.loginDate}</span>
				</div>
				<div class="fitem">
					<label>所属分组:</label> <span>${role.name}</span>
				</div>
				<div class="fitem">
					<label>手机号:</label> <input name="phone" id="phone" value="${user.phone}" class="easyui-textbox" >
				</div>
				<div class="fitem">
					<label>Email:</label> <input name="email" id="email" class="easyui-textbox" value="${user.email}">
				</div>
				<input type="hidden" name="id" id="id2" value="${user.id }" />
				<div class="fitem">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="updateInfo('#infofm')">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="window.history.go(-1)">返回</a>
				</div>
			</form>
		</div> 
		<div class="undis" id="sub2">
			<form id="passwordfm" method="post" novalidate>
				<div class="fitem">
					<label>原密码:</label> <input type="password" name="old_password"	class="easyui-validatebox easyui-textbox" style="width:200px;" required="true">
				</div>
				<div class="fitem">
					<label>新密码:</label> <input type="password" name="new_password" id="new_password" class="easyui-validatebox easyui-textbox" style="width:200px;" required="true">
				</div>
				<div class="fitem">
					<label>重复密码:</label> <input type="password" name="new_repeat_password" id="new_repeat_password" class="easyui-validatebox easyui-textbox" style="width:200px;" required="true">
				</div>
				<input type="hidden" name="id" id="id" value="${user.id}" />
				<div class="fitem">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="updatePass('#passwordfm')">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="window.history.go(-1)">返回</a>
				</div>
			</form>
		</div> 
	</div> 
</div>
</body>
</html>