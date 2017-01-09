<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>马甲用户管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/uploadPreview.min.js"></script>
    <script type="text/javascript" src="js/jquery.form.js"></script>
    <script type="text/javascript" src="js/admin/query_majia.js"></script>
</head>
<body>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">马甲用户</h2>
    </div>
    <div>
        <table>
            <tr>
                <td><label>马甲名：</label></td>
                <td>
                    <input type="text" class="textbox" id="query_name" value="" style="padding:2px;">
                </td>
                <td><label>
                    状态：
                </label></td>
                <td>
                    <select id="query_stat" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="0">已激活</option>
                        <option value="1">禁言</option>
                        <option value="2">封号</option>
                    </select>
                </td>
                <td><label>等级：</label></td>
                <td>
                    <select id="query_vstat" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="0">普通</option>
                        <option value="1">蓝V</option>
                        <option value="2">黄V</option>
                        <option value="3">绿V</option>
                    </select>
                </td>
                <td><label>排行榜：</label></td>
                <td>
                    <select id="query_rankable" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="0">允许</option>
                        <option value="1">禁止</option>
                    </select>
                </td>
                <td><label>性别：</label></td>
                <td>
                    <select id="query_sex" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="1">男</option>
                        <option value="0">女</option>
                        <option value="2">未知</option>
                    </select>
                </td>
                <td><label>所在地：</label></td>
                <td>
                    <input type="text" class="textbox" id="query_addr" value=""style="padding:2px;">
                </td>
                <td><label>上传视频数：</label></td>
                <td>
                    <input type="text" class="textbox" style="width: 120px;padding:2px;" id="startCount">
                    &nbsp;至&nbsp;
                    <input type="text" class="textbox" style="width: 120px;padding:2px;" id="endCount">
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search" style="height:20px;line-height:20px;">搜索</a>
                </td>
            </tr>
        </table>
    </div>
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="addMajiaUser()" plain="true">添加马甲用户</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="batchSettingRankAble(1)" plain="true">禁止上榜</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="batchSettingRankAble(0)" plain="true">允许上榜</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-print" onclick="importDialog()" plain="true">批量导入</a>
    </div>
</div>

<!--等级状态修改弹出框start-->
<div id="dialog" style="padding:5px;width:400px;height:200px;display:none;"
     title="快捷编辑" toolbar="#dlg-toolbar" buttons="#dlg-buttons">
    <table class="table-doc" width="100%">
        <tr>
            <td width="40%"><label>状态：</label></td>
            <td>
                <select id="stat" class="easyui-combobox">
                    <option value="0">已激活</option>
                    <option value="1">禁言</option>
                    <option value="2">封号</option>
                </select>
            </td>
        </tr>
        <tr>
            <td><label>等级：</label></td>
            <td>
                <select id="vstat" class="easyui-combobox">
                    <option value="0">普通</option>
                    <option value="1">蓝V</option>
                    <option value="2">黄V</option>
                    <option value="3">绿V</option>
                </select>
                <input type="hidden" id="id" name="id" value="">
            </td>
        </tr>
    </table>
</div>
<!--等级状态修改弹出框end-->

<!--更新 start-->
<div id="updateDialog" style="padding:5px;width:450px;height:450px;display:none;line-height: 35px;"
     title="更新" toolbar="#dlg-toolbar" buttons="#dlg-buttons">
    <form id="insertForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" width="100%">
            <tr>
                <td class="row"><label>马甲名：</label></td>
                <td class="row">
                    <input type="text" data-options="missingMessage:'马甲名不能为空！',required:true"
                           class="easyui-validatebox textbox"
                           name="username" id="update_name">
                    <input type="hidden" id="update_id" name="id">
                </td>
            </tr>
            <tr>
                <td class="row"><label>头像：</label></td>
                <td class="row">
                    <input type="file" value="" name="file" id="update_pic"/>
                    <span id="imgdiv">
                        <img style="position: absolute;top:90px;left: 300px;" id="imgShow" width="50"/>
                    </span>
                </td>
            </tr>
            <tr>
                <td class="row"><label>性别：</label></td>
                <td class="row">
                    <input type="radio" class="easyui-validatebox textbox" name="sex" id="update_sex1" value="1">男
                    <input type="radio" class="easyui-validatebox textbox" name="sex" id="update_sex0" value="0">女
                    <input type="radio" class="easyui-validatebox textbox" name="sex" id="update_sex2" value="2">未知
                </td>
            </tr>
            <tr>
                <td class="row"><label>状态：</label></td>
                <td class="row">
                    <select name="stat" id="update_stat" class="easyui-validatebox easyui-combobox">
                        <option value="0">已激活</option>
                        <option value="1">禁言</option>
                        <option value="2">封号</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="row"><label>等级：</label></td>
                <td class="row">
                    <select name="vipStat" id="update_vstat" class="easyui-validatebox easyui-combobox">
                        <option value="0">普通</option>
                        <option value="1">蓝V</option>
                        <option value="2">黄V</option>
                        <option value="3">绿V</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="row"><label>排行榜设置：</label></td>
                <td class="row">
                    <select name="rankAble" id="update_rankable" class="easyui-validatebox easyui-combobox">
                        <option value="0">允许</option>
                        <option value="1">禁止</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="row"><label>个性签名：</label></td>
                <td class="row"><input class="easyui-validatebox" type="text" name="signature" id="update_signature">
                </td>
            </tr>
        </table>
    </form>
</div>
<!--更新 end-->
<div id="dlg-buttons">
    <a href="javascript:;" class="easyui-linkbutton" id="update-ok" iconCls="icon-ok">更新</a>
    <a href="javascript:;" class="easyui-linkbutton" id="update-cancel" iconCls="icon-cancel">取消</a>
</div>

<!--批量导入弹出框start-->
<div id="importDialog" style="padding:5px;width:450px;height:280px;display:none;"
     title="文件导入" toolbar="#dlg-toolbar">
    <div class="easyui-panel" style="border: none;">
        <form method="post" id="importForm" enctype="multipart/form-data">
            <div style="margin-bottom:20px;text-align: center;margin-top: 20px;">
                <div>
                    <label>文件:</label>
                    <input name="ruserFile" type="file" buttonText="选择文件" data-options="prompt:'请选择文件！'">
                </div>
                <div><span style="color: red;">仅限.txt格式的文件，每条用户信息以回车换行</span></div>
            </div>
            <div>
                <input type="button" onclick="doImport()" class="easyui-linkbutton" style="width:100%" value="导入">
            </div>
        </form>
    </div>
</div>
<!--批量导入弹出框end-->

</body>
</html>
