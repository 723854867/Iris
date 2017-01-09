<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/sitemesh-decorator.tld" prefix="decorator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<% 
	String path = request.getContextPath(); 
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
%> 
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><decorator:title default="busap video content system 后台管理" /> - busap video content system 后台管理</title>
<link rel="stylesheet" type="text/css" href="css/default.css" >
<link rel="stylesheet" type="text/css" href="files/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="files/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/button.css">

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="files/easyui/jquery.easyui.min.js" ></script>
<script type="text/javascript" src="files/easyui/locale/easyui-lang-zh_CN.js" ></script>
<script type="text/javascript" src="files/easyui/jquery.easyui.patch.js"></script>
<script type="text/javascript" src="js/datagrid-dnd.js"></script>
<script type="text/javascript" src="files/main.js"></script>
<script type="text/javascript">
var baseUrl = "<%=basePath%>";
</script>
<script type="text/javascript">
//修改密码
function updatePassword(){
	$("#passwordfm").form('clear');
	$("#id").val(${uid});
	$("#passworddlg").dialog('open').dialog('setTitle',"修改密码");
}

function save(mydialogueId,myFormId){
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
                $( mydialogueId).dialog( 'close'); // close the dialog
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
<style>
<!--
.bwhite {
	color: #fff;
	font-weight: bold;
}
.t {
	clear: both;
	height: 39px;
	width: 100%;
	overflow: hidden;
}

.t_left {
	background: url(./images/tl.png) no-repeat top left;
	line-height: 25px;
	height: 39px;
	text-align: left;
	padding-left: 22px;
	width: 260px;
	float: left;
}

.t_rtl {
	float: right;
	padding-right: 10px;
	padding-top: 4px;
}

.panel-title{
text-align:center;
}

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

-->
</style>
<decorator:head />
</head>
<body class="easyui-layout">
<%@ include file="/files/globalvar.jsp"%>
<!-- 头部标题 -->
<div data-options="region:'north',border:false" style="height:40px; padding-top:5px; background: url(./images/tbg.png) repeat-x;overflow: hidden;"> 
</div>


<!-- 页脚信息 -->
<div data-options="region:'south',border:false" style="height:30px; background:#E2EDFF; padding:2px; vertical-align:middle;">
	<span id="sysInfo">版权所有©2012 巴士在线控股有限公司</span>
	<span id="sysVersion">系统版本：V1.0</span>    
</div>

<!-- 内容tabs -->
<div id="mainFrame" data-options="region:'center'" title="主页" style="">
  <decorator:body />
</div>





	
	<!-- 对话框里的保存和取消按钮 -->
	<div id="passworddlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="save('#passworddlg','#passwordfm')">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#passworddlg').dialog('close')">取消</a>
	</div>

</body>
</html>