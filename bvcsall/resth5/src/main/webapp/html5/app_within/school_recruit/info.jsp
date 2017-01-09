<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>校园大使招聘</title>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
</head>
<body>
<table border="1">
<c:if test="${type =='1'}">
		<tr>
			<td>地区</td>
			<td>注册数</td>
			<td>邀请码使用数</td>
		</tr>
	<c:forEach var="bean" items="${list}">
		<tr>
			<td>${bean.area }</td>
			<td>${bean.register_number }</td>
			<td>${bean.code_number }</td>
		</tr>
	</c:forEach>
</c:if>
	
	<c:if test="${type=='2'}">
		<tr>
			<td>邀请码</td>
			<td>注册数</td>
		</tr>
	<c:forEach var="bean" items="${list}">
		<tr>
			<td>${bean.invite_code }</td>
			<td>${bean.number }</td>
		</tr>
	</c:forEach>
	</c:if>
</table>
</body>
</html>