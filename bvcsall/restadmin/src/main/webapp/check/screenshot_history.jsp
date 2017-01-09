<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2015/12/23
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>截图历史信息</title>
</head>
<body>
<div class="basic" style="margin-top: 10px;">
    <c:forEach var="entry" items="${screenshot}">
        <div class="room" style="border: 1px solid #ccc;margin: 5px;height: 180px;">
            <span class="image">
                <img src="${entry}" style="width: 180px;height: 180px;" class="roomImage">
            </span>
        </div>
    </c:forEach>
</div>
</body>
</html>
