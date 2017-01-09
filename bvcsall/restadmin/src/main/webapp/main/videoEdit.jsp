<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/sitemesh-decorator.tld" prefix="decorator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/default.css">
<link rel="stylesheet" type="text/css" href="files/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="files/easyui/themes/icon.css">

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="files/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="files/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/datagrid-dnd.js"></script>
<script type="text/javascript" src="files/main.js"></script>
<decorator:head />
</head>
<body class="easyui-layout">
	<div id="mainFrame" data-options="region:'center'" title="视频编辑">
		<decorator:body />
	</div>
</body>
</html>