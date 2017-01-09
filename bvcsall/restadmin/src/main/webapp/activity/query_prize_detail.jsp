<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>中奖名单</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_prize_detail.js"></script>
</head>
<body>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">中奖名单</h2>
    </div>
    <div style="margin-top: 10px;">
        <span>
                    <label>
                        <select class="easyui-combobox" name="user" id="user">
                            <option value="">用户搜索</option>
                            <option value="1">用户ID</option>
                            <option value="2">用户名</option>
                            <option value="3">昵称</option>
                            <option value="4">手机号码</option>
                        </select>
                    </label>
                <span>
                    <input type="text" name="userKeyword" id="userKeyword" class="easyui-validatebox">
                </span>
        </span>
        <span>
                <label>奖项名称：</label>
                <span>
                    <select name="prizeLevel" id="query_prize_level" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="1">一等奖</option>
                        <option value="2">二等奖</option>
                        <option value="3">三等奖</option>
                        <option value="4">四等奖</option>
                        <option value="5">五等奖</option>
                    </select>
                </span>
        </span>
        <span>
            <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
               iconCls="icon-search">搜索</a>
         </span>
    </div>
    <div>
        <a href="javascript:;" onclick="insertDialog()" class="easyui-linkbutton" iconCls="icon-add"
           plain="true">设置中奖名单</a>
    </div>
</div>
<input type="hidden" value="${prizeId}" id="commonPrizeId">
<input type="hidden" value="${activityId}" id="commonActivityId">
<!--添加弹出框 start-->
<div id="insert_dlg" class="easyui-dialog" style="width:400px;height:400px;" closed="true"
     buttons="#insert-dlg-buttons">
    <table class="table-doc" cellspacing="0" width="100%">
        <caption></caption>
        <tr>
            <td class="row">奖项级别：</td>
            <td class="row">
                <select name="prizeLevel" id="prizeLevel" class="easyui-validatebox easyui-combobox">
                    <option value="1">一等奖</option>
                    <option value="2">二等奖</option>
                    <option value="3">三等奖</option>
                    <option value="4">四等奖</option>
                    <option value="5">五等奖</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="row">奖品名称：</td>
            <td class="row">
                <input type="text" name="prizeName" id="prizeName" class="easyui-validatebox" value="" required="true">
            </td>
        </tr>
        <tr>
            <td class="row">中奖用户：</td>
            <td class="row">
                <textarea rows="5" cols="30" name="userIds" id="userIds" class="easyui-validatebox"
                          onclick="selectUsers()">
                </textarea>
                <input value="" type="hidden" id="picInput" name="picInput">
            </td>
        </tr>
    </table>
</div>
<!-- 编辑对话框里的保存和取消按钮 -->
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

<!--编辑弹出框开始-->
<div id="update_dlg" class="easyui-dialog" style="width:400px;height:400px;" closed="true"
     buttons="#update-dlg-buttons">
    <table class="table-doc" cellspacing="0" width="100%">
        <caption></caption>
        <tr>
            <td class="row">奖项级别：</td>
            <td class="row">
                <select name="prizeLevel" id="update_prize_level" class="easyui-validatebox easyui-combobox">
                    <option value="1">一等奖</option>
                    <option value="2">二等奖</option>
                    <option value="3">三等奖</option>
                    <option value="4">四等奖</option>
                    <option value="5">五等奖</option>
                </select>
                <input type="hidden" name="id" id="update_id" value="" class="easyui-validatebox" required="true">
            </td>
        </tr>
        <tr>
            <td class="row">奖品名称：</td>
            <td class="row">
                <input type="text" name="prizeName" id="update_prize_name" class="easyui-validatebox" value=""
                       required="true">
            </td>
        </tr>
        <tr>
            <td class="row">中奖用户：</td>
            <td class="row">
                <select type="text" name="userId" id="update_user_id" style="width:120px;"
                        class="easyui-validatebox easyui-combogrid"
                        data-options="panelWidth:420,
                                idField:'id',
                                textField:'username',
                                url:'comboBox/queryCombogridUsers',
                                queryParams:{
                                	userType:'all'
                                },
                                columns:[[
                                {field:'id',title:'用户ID',hidden:true},
                                {field:'username',title:'用户名',width:100},
                                {field:'phone',title:'手机号',width:100},
                                {field:'vipStat',title:'用户级别',width:100,
                                    formatter: function (value, row) {
                                        if (value == 0) {
                                        return '普通';
                                        } else if (value == 1) {
                                        return '蓝V';
                                        } else if (value == 2) {
                                        return '黄V';
                                        } else if (value == 3) {
                                        return '绿V';
                                        } else {
                                        return '普通';
                                        }
                                    }
                                },
                                {field:'isLocked',title:'状态',width:100,
                                formatter: function (value, row) {
                                        if (value == 0) {
                                            return '有效';
                                        } else {
                                            return '无效';
                                        }
                                    }
                                }
                                ]],
                                require:true,
                                validType:'selectValueRequired'
                                ">

                </select>
            </td>
        </tr>
    </table>
</div>
<!-- 编辑弹出框里的保存和取消按钮 -->
<div id="update-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doUpdate()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#update_dlg').dialog('close')">取消</a>
</div>
<!--编辑弹出框结束-->
</body>
</html>
