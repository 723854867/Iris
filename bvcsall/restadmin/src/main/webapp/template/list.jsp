<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>视频片头</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_template.js"></script>
</head>
<body>
<!-- 列表 -->
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">视频片头</h2>
    </div>
    <div>
        <form method="post">
            <table>
                <tr>
                    <td><label>类型：</label></td>
                    <td>
                        <select class="easyui-validatebox easyui-combobox" name="type" id="type" type="text" style="width: 100px;">
                            <option value="">全部</option>
                            <option value="0">片头</option>
                            <option value="1">滤镜</option>
                            <option value="2">照片电影</option>
                            <option value="3">MV</option>
                        </select>
                    </td>
                    <td><label>所属活动：</label></td>
                    <td>
                        <select id="actIds" class="easyui-combogrid" name="actId" style="width:150px;"
                                data-options="
                                panelWidth:350,
                                idField:'id',
                                textField:'title',
                                url:'comboBox/getActivitiesCombogrid',
                                columns:[[
                                {field:'title',title:'标题',width:100},
                                {field:'description',title:'描述',width:150},
                                {field:'status',title:'状态',width:80,
                                formatter: function (value) {
                                    if (value == 1) {
                                        return '进行中';
                                    }else{
                                        return '已下线';
                                    }
                                },
                                }
                                ]]"></select>
                    </td>
                    <td><label>名称：</label></td>
                    <td>
                        <input type="text" id="title" name="title" style="width: 100px;" class="easyui-validatebox"/>
                    </td>
                    <td>
                        <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                           iconCls="icon-search">搜索</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:newTemplate()"
           plain="true">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:editTemplate()"
           plain="true">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
           onclick="javascript:destroyTemplate()"
           plain="true">删除</a>
    </div>
</div>

<!-- 弹出的添加对话框 -->
<div id="dlg" class="easyui-dialog" style="width:550px;height:430px;padding:2px 5px;" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post" action="template/add" novalidate enctype="multipart/form-data">
        <table class="table-doc" width="100%">
            <tr>
                <td class="row"><label>名称:</label></td>
                <td class="row" style="text-align: left;"><input name="title" class="easyui-validatebox" required="true"></td>
            </tr>
            <tr>
                <td class="row"><label>描述:</label></td>
                <td class="row" style="text-align: left;">
                    <input name="description" class="easyui-validatebox" required="true">
                </td>
            </tr>
            <tr>
                <td class="row"><label>片头顺序:</label></td>
                <td class="row" style="text-align: left;">
                    <input name="orderNum" class="easyui-numberbox easyui-textbox"
                           data-options="invalidMessage:'请输入正确的数字',required:'true'">
                </td>
            </tr>
            <tr>
                <td class="row"><label>活动:</label></td>
                <td class="row" style="text-align: left;"><select id="insertActId" class="easyui-combogrid" name="actId" style="width:200px;"
                            data-options="
                    panelWidth:350,
                    idField:'id',
                    textField:'title',
                    url:'comboBox/getActivitiesCombogrid',
                    columns:[[
                    {field:'title',title:'标题',width:100},
                    {field:'description',title:'描述',width:150},
                    {field:'status',title:'状态',width:80,
                    formatter: function (value) {
                        if (value == 1) {
                        return '进行中';
                        }else{
                        return '已下线';
                        }
                    },
                    }
                    ]]
                    "></select>
                </td>
            </tr>
            <tr>
                <td class="row"><label>文件类型:</label></td>
                <td class="row" style="text-align: left;">
                    <select id="type_add" class="easyui-combobox" name="type" style="width: 200px;"  required="true">
                        <option value="0">片头</option>
                        <option value="1">滤镜</option>
                        <option value="2">照片电影</option>
                        <option value="3">MV</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="row"><label>文件:</label></td>
                <td class="row" style="text-align: left;">
                    <input type="file" id="file1" name="files" class="UserupAvaterPic" accept="mp4"/>
                    <br><span>片头，滤镜请选择mp4文件。照片电影，MV请选择zip文件</span>
                </td>
            </tr>
            <tr>
                <td class="row"><label>封面图片:</label></td>
                <td class="row" style="text-align: left;">
                    <input type="file" id="file2" name="files" accept="png,jpg,jpeg,gif"/>
                </td>
            </tr>
            <tr>
                <td class="row"><label>背景图片:</label></td>
                <td class="row" style="text-align: left;">
                    <input type="file" id="file3" name="files" accept="png,jpg,jpeg,gif"/>
                </td>
            </tr>
            <tr>
                <td class="row"><label>版本号:</label></td>
                <td class="row" style="text-align: left;">
                    <input  id="versionNum" name="versionNum" class="easyui-validatebox" />
                </td>
            </tr>
        </table>
    </form>
</div>

<!-- 对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveTemplate('#dlg','#fm','type_add','add')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 弹出的添加对话框 -->
<div id="updatedlg" class="easyui-dialog" style="width:550px;height:430px;padding:2px 5px" closed="true"
     buttons="#updatedlg-buttons">
    <form id="updatefm" method="post" action="template/updateTemp" novalidate enctype="multipart/form-data">
        <table class="table-doc" width="100%">
            <tr>
                <td class="row"><label>名称:</label></td>
                <td class="row" style="text-align: left;">
                    <input name="title" class="easyui-validatebox" required="true">
                </td>
            </tr>
            <tr>
                <td class="row"><label>描述:</label></td>
                <td class="row" style="text-align: left;">
                    <input name="description" class="easyui-validatebox" required="true">
                </td>
            </tr>
            <tr>
                <td class="row"><label>片头顺序:</label></td>
                <td class="row" style="text-align: left;">
                    <input name="orderNum" class="easyui-numberbox easyui-textbox"
                           data-options="invalidMessage:'请输入正确的数字',required:'true'">
                </td>
            </tr>
            <tr>
                <td class="row"><label>活动:</label></td>
                <td class="row" style="text-align: left;">
                    <select id="actId" class="easyui-combogrid" name="actId" style="width:200px;"
                            data-options="
                    panelWidth:350,
                    idField:'id',
                    textField:'title',
                    url:'comboBox/getActivitiesCombogrid',
                    columns:[[
                    {field:'title',title:'标题',width:100},
                    {field:'description',title:'描述',width:150},
                    {field:'status',title:'状态',width:80,
                    formatter: function (value) {
                        if (value == 1) {
                        return '进行中';
                        }else{
                        return '已下线';
                        }
                    },
                    }
                    ]]
                    "></select>
                </td>
            </tr>
            <tr>
                <td class="row"><label>文件类型:</label></td>
                <td class="row" style="text-align: left;">
                    <select id="type_edit" name="type" class="easyui-combobox" style="width: 200px;">
                        <option value="0">片头</option>
                        <option value="1">滤镜</option>
                        <option value="2">照片电影</option>
                        <option value="3">MV</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="row"><label>文件:</label></td>
                <td class="row" style="text-align: left;">
                    <input type="file" id="file4" name="files" class="UserupAvaterPic" accept="mp4"/>
                    <br><span>片头，滤镜请选择mp4文件。照片电影，MV请选择zip文件</span>
                </td>
            </tr>
            <tr>
                <td class="row"><label>封面图片:</label></td>
                <td class="row" style="text-align: left;">
                    <input type="file" id="file5" name="files" accept="png,jpg,jpeg,gif"/>
                    <img src="" style="height: 70px;" id="file5_img" alt="封面图片">
                </td>
            </tr>
            <tr>
                <td class="row"><label>背景图片:</label></td>
                <td class="row" style="text-align: left;">
                    <input type="file" id="file6" name="files" accept="png,jpg,jpeg,gif"/>
                    <img src="" style="height: 70px;" id="file6_img" alt="背景图片">
                </td>
                <input type="hidden" name="id"/>
            </tr>
            <tr>
                <td class="row"><label>版本号:</label></td>
                <td class="row" style="text-align: left;">
                    <input  id="versionNum" name="versionNum" class="easyui-validatebox" />
                </td>
            </tr>
        </table>
    </form>
</div>

<!-- 对话框里的保存和取消按钮 -->
<div id="updatedlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="saveTemplate('#updatedlg','#updatefm','type_edit','edit')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
</div>
</body>
</html>
