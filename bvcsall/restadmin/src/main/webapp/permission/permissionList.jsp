<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>菜单管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
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
    <script type="text/javascript" src="js/admin/query_permission.js"></script>
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
        <h2 style="float:left;padding-left:10px;margin: 1px">菜单管理</h2>
    </div>
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:newPermission()"
           plain="true">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:editPermission()"
           plain="true">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="javascript:destroyPermission()"
           plain="true">删除</a>
    </div>
</div>

<!-- 弹出的添加或者编辑对话框 -->
<div id="dlg" class="easyui-dialog" style="width:400px;height:380px;padding:10px 20px" closed="true"
     buttons="#dlg-buttons">
    <div class="ftitle">添加新菜单</div>
    <!-- 添加 -->
    <form id="fm" method="post" novalidate>
        <div class="fitem">
            <label>菜单名称:</label>
            <input name="name" class="easyui-validatebox easyui-textbox" required="true">
        </div>
        <div class="fitem">
            <label>上级菜单id:</label>
            <select id="addPid" class="easyui-combogrid easyui-validatebox" name="pid"
                    style="width:155px"
                    data-options="
                    require:true,
                    validType:'selectValueRequired',
				    panelWidth:250,
				    idField:'id',
				    textField:'id',
				    fitColumns:true,
				    url:'permission/listpage',
				    columns:[[
				    {field:'id',title:'ID',width:100},
				    {field:'name',title:'菜单名称',width:100}
				    ]]
				    ">
                <option value="">顶级菜单</option>
            </select>
        </div>
        <div class="fitem">
            <label>访问地址:</label>
            <input name="value" type="text" class="easyui-validatebox easyui-textbox">
        </div>
        <div class="fitem">
            <label>权重:</label>
            <input name="sort" id="addSort" class="easyui-numberbox easyui-validatebox"
                   data-options="invalidMessage:'请输入正确的数字',required:'true',validType:'number'">
        </div>
        <%--<input type="hidden" value="" name="id"/>--%>
    </form>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#dlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<div id="updatedlg" class="easyui-dialog" style="width:400px;height:380px;padding:10px 20px" closed="true"
     buttons="#updatedlg-buttons">
    <div class="ftitle">修改标签</div>
    <!-- 修改 -->
    <form id="updatefm" method="post" novalidate>
        <div class="fitem">
            <label>菜单名称:</label>
            <input name="name" class="easyui-validatebox easyui-textbox" required="true">
        </div>
        <div class="fitem">
            <label>上级菜单id:</label>
            <select id="editPid" class="easyui-combogrid easyui-validatebox" name="pid" required="true"
                    style="width:155px"
                    data-options="
				    panelWidth:250,
				    idField:'id',
				    textField:'id',
				    fitColumns:true,
				    url:'permission/listpage',
				    columns:[[
				    {field:'id',title:'ID',width:100},
				    {field:'name',title:'菜单名称',width:100}
				    ]]
				    "></select>
        </div>
        <div class="fitem">
            <label>访问地址:</label>
            <input name="value" class="easyui-validatebox easyui-textbox">
        </div>
        <div class="fitem">
            <label>权重:</label>
            <input name="sort" class="easyui-numberbox easyui-textbox"
                   data-options="invalidMessage:'请输入正确的数字',required:'true',validType:'number'">
        </div>
        <input type="hidden" name="id"/>
    </form>
</div>

<!-- 编辑对话框里的保存和取消按钮 -->
<div id="updatedlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveUser('#updatedlg','#updatefm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
</div>

<div id="mm" class="easyui-menu" style="width:120px;">
    <div onclick="append()" data-options="iconCls:'icon-add'">添加</div>
</div>
</body>
</html>
