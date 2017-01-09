<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>推荐用户</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_recommend_user.js"></script>
</head>
<body>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">推荐用户</h2>
    </div>
    <div>
        <a href="javascript:;" onclick="insertDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
    </div>
    <div class="easyui-tabs" id="tabs">
        <div id="homePage" title="首页用户推荐" style="margin-left: 10px;">
        </div>
        <div id="searchPage" title="搜索页用户推荐" style="margin-left: 10px;">
        </div>
        <div id="voicePage" title="新歌声专区用户推荐" style="margin-left: 10px;">
        </div>
    </div>
</div>
<!-- 添加推荐用户弹出框 start -->
<div id="insert_dlg" class="easyui-dialog" style="width:500px;height:200px;" closed="true"
     buttons="#insert-dlg-buttons">
    <form id="insertForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">位置：</td>
                <td style="text-align: left;">
                    <select name="position" id="position" class="easyui-combobox">
                        <option value="1">首页推荐</option>
                        <option value="2">搜索页推荐</option>
                        <option value="3">新歌声推荐</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">选择用户：</td>
                <td style="text-align: left;">
                    <input type="text" name="userId" id="userId" required="true" onclick="selectUsers()" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">权重：</td>
                <td style="text-align: left;">
                    <input type="text" name="weight" style="width: 200px;" id="weight" class="easyui-numberbox" min="0.01" precision="2" required="true" missingMessage="必须填写大于或者等于0的数字"
                           value="">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsert()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_dlg').dialog('close')">取消</a>
</div>

<!--选择用户弹出框开始-->
<div id="select_user_dlg" class="easyui-dialog" style="width:850px;height:700px;" closed="true"
     buttons="#select-user-dlg-buttons">
    <div id="dataGridToolbarUser">
        <table>
            <tr>
                <td>
                    <select class="easyui-combobox" name="queryUser" id="queryUser">
                        <option value="">用户搜索</option>
                        <option value="1">用户ID</option>
                        <option value="2">用户名</option>
                        <option value="3">昵称</option>
                        <option value="4">手机号码</option>
                    </select>
                </td>
                <td>
                    <input type="text" name="queryUserKeyword" id="queryUserKeyword" class="easyui-validatebox">
                </td>
                <td>用户类型：</td>
                <td>
                    <select name="userType" id="userType" class="easyui-combobox">
                        <option value="">全部</option>
                        <option value="normal">Blive</option>
                        <option value="majia">马甲</option>
                    </select>
                </td>
                <td>用户等级：</td>
                <td>
                    <select name="vipStat" id="vipStat" class="easyui-combobox">
                        <option value="">全部</option>
                        <option value="0">普通</option>
                        <option value="1">蓝V</option>
                        <option value="2">黄V</option>
                        <option value="3">绿V</option>
                    </select>
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearchUsers()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
    <table class="table-doc" id="displayUserTable" cellspacing="0" width="100%"></table>
</div>
<div id="select-user-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doSelectUser()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#select_user_dlg').dialog('close')">取消</a>
</div>
<!--选择用户弹出框结束-->
</body>
</html>
