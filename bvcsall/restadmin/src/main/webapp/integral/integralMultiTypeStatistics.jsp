<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>积分产出</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="js/admin/query_integral.js"></script>
</head>
<body>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">积分产出</h2>
    </div>
</div>

<%--<div style="padding-left:20px;width:97%">
    <div style="margin:20px 20px 20px 20px">
        <h2>积分产出</h2>
    </div>
    <div id="tb" style="padding:5px;">
        <hr/>
        <table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
            <tr style="line-height: 20px;">
                <th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;">
                    日期
                </th>
                <th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;">
                    签到
                </th>
                <th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;">
                    日常任务
                </th>
                <th style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;">
                    总和
                </th>

            </tr>
            <c:forEach items="${staMap}" var="integral" varStatus="status">
                <tr>
                    <td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;">
                            ${integral.value.createTime}
                    </td>
                    <td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;">
                            ${integral.value.signSum}
                    </td>
                    <td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;">
                            ${integral.value.dailySum}
                    </td>
                    <td style="vertical-align: middle;border-right:1px solid #000;border-bottom:1px solid #000;">
                            ${integral.value.signSum+integral.value.dailySum}
                    </td>

                </tr>

            </c:forEach>
        </table>
        <br/>
        <br/><br/><br/>
    </div>
</div>--%>
</body>
</html>