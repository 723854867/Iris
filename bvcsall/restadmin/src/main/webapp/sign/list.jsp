<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>积分管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type='text/javascript' src='js/admin/query_sign.js'></script>
    <style type="text/css">
        .statistical {
            margin-left: 20px;
        }
    </style>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">积分管理</h2>
    </div>
    <div>
            <span>
                <label>日期：</label>
                <span>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="startTime" id="startTime">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="endTime" id="endTime">
                </span>
            </span>
            <span>
                <label>签到数量：</label>
                <span>
                    <input type="text" style="width: 120px;" name="startCount" id="startCount">
                    &nbsp;至&nbsp;
                    <input type="text" style="width: 120px;" name="endCount" id="endCount">
                </span>
            </span>
            <span>
                <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                   iconCls="icon-search">搜索</a>
            </span>
        </span>
    </div>
    <div>
        <span class="statistical">30以内签到用户数量：${signvo.lessMaxNum}</span><span
            class="statistical">30以上签到用户数量：${signvo.beyongMaxNum}</span>
    </div>
</div>

</body>
</html>
