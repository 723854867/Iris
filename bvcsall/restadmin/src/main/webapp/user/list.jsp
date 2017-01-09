<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>管理员管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_admin.js"></script>
    <style type="text/css">
        #fm {
            margin: 0;
            padding: 10px 30px;
        }

        .ftitle {
            font-size: 14px;
            font-weight: bold;
            padding: 5px 0;
            margin-bottom: 10px;
            border-bottom: 1px solid #ccc;
        }

        .fitem {
            margin-bottom: 5px;
        }

        .fitem label {
            display: inline-block;
            width: 80px;
        }
    </style>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" style="border-bottom: 1px solid #ddd;padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">管理员管理</h2>
    </div>
    <div>
            <span>
                <label>用户名：</label>
                <span>
                    <input type="text" class="textbox" id="username" value="" style="padding:2px;">
                </span>
            </span>
                    <span>
                <label>
                    所属管理组：
                </label>
                <span>
                    <input id="group" class="easyui-combobox"
                           data-options="valueField:'id',textField:'name',url:'user/rolelistAjax'">
                </span>
            </span>
            <span>
                <label>手机号：</label>
                <span>
                    <input type="text" class="textbox" id="phone" value=""style="padding:2px;">
                </span>
            </span>
            <span>
                <label>email：</label>
                <span>
                    <input type="text" class="textbox" id="email" value=""style="padding:2px;">
                </span>
            </span>
            <span>
                <label>锁定状态:</label>
                <select id="isLockedSearch" name="isLocked" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="0">未锁定</option>
                    <option value="1">已锁定</option>
                </select>
            </span>
            <span>
                <label>账户状态:</label>
                <select id="isEnabledSearch" name="isEnabled" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="0">不可用</option>
                    <option value="1">可用</option>
                </select>
            </span>
            <span>
            <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
               iconCls="icon-search">搜索</a>
         </span>
    </div>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:newUser()"
       plain="true">添加</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:editUser()"
       plain="true">编辑</a>
</div>

<!-- 弹出的添加对话框 -->
<div id="dlg" class="easyui-dialog" style="width:400px;height:350px;padding:10px 20px" closed="true"
     buttons="#dlg-buttons">
    <div class="ftitle">用户信息</div>
    <form id="fm" method="post" novalidate>
        <div class="fitem">
            <label>用户名:</label>
            <input name="username" class="easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>姓名:</label>
            <input name="name" class="easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>密码:</label>
            <input type="password" name="password" class="easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>分配角色:</label>
            <input id="addrolelist" name="rids"/>
        </div>
        <div class="fitem">
            <label>手机号码:</label>
            <input name="phone" class="easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>邮箱:</label>
            <input name="email" class="easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>锁定状态:</label>
            <select id="isLockedUpdate" name="isLocked" class="easyui-combobox">
                <option value="0">未锁定</option>
                <option value="1">已锁定</option>
            </select>
        </div>
        <input type="hidden" name="id"/>
        <input type="hidden" name="isEnabled" id="isEnabled" value="1"/>
        <input type="hidden" name="type" id="type" value="admin"/>
    </form>
</div>

<!-- 对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#dlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 弹出的添加对话框 -->
<div id="updatedlg" class="easyui-dialog" style="width:400px;height:350px;padding:10px 20px" closed="true"
     buttons="#updatedlg-buttons">
    <div class="ftitle">用户信息</div>
    <form id="updatefm" method="post" novalidate>
        <div class="fitem">
            <label>用户名:</label>
            <input name="username" class="easyui-validatebox" required="true" readonly="readonly">
        </div>
        <div class="fitem">
            <label>姓名:</label>
            <input name="name" class="easyui-validatebox" required="true">
        </div>
        <%--        <div class="fitem">
                    <label>密码:</label>
                    <input name="password" class="easyui-validatebox">
                </div>--%>
        <div class="fitem">
            <label>分配角色:</label>
            <input id="editrolelist" name="rids"/>
        </div>
        <div class="fitem">
            <label>手机号码:</label>
            <input name="phone" class="easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>邮箱:</label>
            <input name="email" class="easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>锁定状态:</label>
            <select id="isLocked" name="isLocked" class="easyui-combobox">
                <option value="0">未锁定</option>
                <option value="1">已锁定</option>
            </select>
        </div>
        <input type="hidden" name="id"/>
    </form>
</div>

<!-- 对话框里的保存和取消按钮 -->
<div id="updatedlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveUser('#updatedlg','#updatefm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
</div>

</body>
</html>
