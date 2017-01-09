<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>管理组管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_role.js"></script>
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
        <h2 style="float:left;padding-left:10px;margin: 1px">管理组管理</h2>
    </div>
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:newUser()"
           plain="true">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:editUser()"
           plain="true">编辑</a>
    </div>
    <!-- <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="javascript:destroyUser()" plain="true"></a> -->
</div>

<!-- 弹出的添加或者编辑对话框 -->

<div id="dlg" class="easyui-dialog" style="width:600px;height:380px;padding:10px 20px" closed="true"
     buttons="#dlg-buttons">

    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west',border:false" style="padding:5px;margin:0 auto;width:350px">
            <div class="ftitle">角色信息</div>
            <!-- 添加 -->
            <form id="fm" method="post" novalidate>
                <div class="fitem">
                    <label>角色名:</label>
                    <input class="easyui-validatebox easyui-textbox" name="name" style="width:50%"
                           data-options="validType:'length[0,20]',required:true">
                </div>
                <div class="fitem">
                    <label>是否系统角色:</label>
                    <select class="easyui-combobox easyui-validatebox" required="true" name="isSystem"
                            style="width:50%;">
                        <option value="1">是</option>
                        <option value="0">否</option>
                    </select>
                </div>
                <div class="fitem">
                    <label>描述:</label>
                    <input class="easyui-validatebox easyui-textbox" name="description" id="description_add"
                           data-options="multiline:true,validType:'length[0,100]',novalidate:true" value=""
                           style="width:50%;height:100px">
                </div>
                <input type="hidden" name="id"/>
            </form>
        </div>

        <div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
            <div class="ftitle">分配权限</div>
            <ul id="permissionTree"></ul>
        </div>
    </div>

</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveUser('#dlg','#fm','#permissionTree')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<div id="updatedlg" class="easyui-dialog" style="width:600px;height:380px;padding:10px 20px" closed="true"
     buttons="#updatedlg-buttons">

    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west',border:false" style="padding:5px;margin:0 auto;width:350px">
            <div class="ftitle">角色信息</div>
            <!-- 修改 -->
            <form id="updatefm" method="post" novalidate>
                <div class="fitem">
                    <label>角色名:</label>
                    <input name="name" id="name_update" class="easyui-validatebox easyui-textbox" required="true">
                </div>
                <div class="fitem">
                    <label>描述:</label>
                    <input class="easyui-validatebox easyui-textbox" name="description" id="description_update"
                           data-options="multiline:true,validType:'length[0,100]',novalidate:true" value=""
                           style="width:50%;height:100px">
                </div>
                <input type="hidden" name="id" id="update_id"/>
            </form>
        </div>

        <div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
            <div class="ftitle">分配权限</div>
            <ul id="editPermissionTree"></ul>
        </div>
    </div>

    <!-- 编辑对话框里的保存和取消按钮 -->
    <div id="updatedlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
           onclick="saveUser('#updatedlg','#updatefm','#editPermissionTree')">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
    </div>
</div>
</body>
</html>
