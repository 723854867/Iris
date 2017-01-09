<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html style="background: white">
<head>
    <title>Banner列表</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
    <script type="text/javascript" src="js/admin/query_banner.js"></script>
</head>
<body>

<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" style="padding:5px;height:auto">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">广告管理</h2>
        <h2 style="float:left;padding-left:20px;margin: 1px">发现Banner</h2>
        <span style="float:left;padding-left:20px;margin: 1px"><a href="advImage/initPage" class="easyui-linkbutton">开机大图</a></span>
    </div>
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:upload()"
           plain="true">创建</a>
    </div>

</div>

</body>
</html>
