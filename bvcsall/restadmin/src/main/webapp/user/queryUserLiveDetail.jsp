<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>用户直播详情</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_anchor_detail.js"></script>
</head>
<body>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div data-options="fit:true" id="displayTableToolbar" style="height: 100px;">
    <div class="easyui-layout" data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
        <table cellpadding="5" style="margin:0 auto;width:100%;text-align: left;" class="form-body">
            <input type="hidden" name="id" id="userId" value="${anchorDetail.id}">
            <tr>
                <td>ID</td>
                <td>昵称</td>
                <td>手机号码</td>
                <td>机构</td>
                <td>礼物数</td>
                <td>金豆</td>
                <td>总时长</td>
                <td>总天数</td>
                <td>入驻时间</td>
                <td>操作</td>
            </tr>
            <tr>
                <td>${anchorDetail.id}</td>
                <td>${anchorDetail.name}</td>
                <td>${anchorDetail.phone}</td>
                <td>${anchorDetail.orgName}</td>
                <td>${anchorDetail.giftNumber}</td>
                <td>${anchorDetail.pointNumber}</td>
                <td>${anchorDetail.hourDuration}</td>
                <td>${anchorDetail.totalDuration}</td>
                <td><fmt:formatDate value="${anchorDetail.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                <td>
                    <input type="text" value="${startDate}" class="easyui-datebox" style="width: 120px;" name="startDate" id="startDate">
                    &nbsp;至&nbsp;
                    <input type="text" value="${endDate}" class="easyui-datebox" style="width: 120px;" name="endDate" id="endDate">
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search" style="">查询</a>
                </td>
            </tr>
        </table>
    </div>
</div>


</body>
</html>
