<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>域名管理</title>
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
            width: 90px;
            margin-top: 5px;
        }
    </style>
    <script type="text/javascript">
        var datagridId = "#tt";
        var adddialogueId = "#dlg";
        var editdialogueId = "#updatedlg";
        var addFormId = "#fm";
        var editFormId = "#updatefm";
        var addTitle = "新增加载地址信息";
        var editTitle = "编辑加载地址信息";
        var deleteConfirmMessage = "你确定要删除吗?";
        var noSelectedRowMessage = "你没有选择行";
        var searchFormId = "#searchForm";
        var pageSize = 50;
        var creatorId =${uid};
        var listUrl = "loadconfigurl/listpage";
        var updateUrl = "loadconfigurl/update";
        var deleteUrl = "loadconfigurl/delete";
        var addUrl = "loadconfigurl/create";

        var url;


        $(function () {
            $(datagridId).datagrid({
                fitColumns: true,
                rownumbers: true,
                striped: true,
                fit: true,
                pagination: true,
                pageNumber: 1,
                pageList: [pageSize, pageSize * 2, pageSize * 3],
                pageSize: pageSize,
                pagePosition: 'bottom',
                singleSelect: true,
                selectOnCheck: true,
                nowrap: true,
                url: listUrl,
                columns: [[
                    {
                        field: 'clientPf',
                        title: '客户端平台',
                        width: 50,
                        align: 'center',
                        formatter: function (value, row, index) {
                            if (row.clientPf == 'ios') return 'ios客户端';
                            if (row.clientPf == 'all') return '通用';
                            if (row.clientPf == 'android') return 'android客户端';
                            if (row.clientPf == 'html5') return 'html5客户端';
                        }
                    },
                    {
                        field: 'type',
                        title: 'url类型',
                        width: 40,
                        align: 'center',
                        formatter: function (value, row, index) {
                            if (row.type == 'voiceurl') return '声音文件url';
                            if (row.type == 'interfaceurl') return '接口url';
                            if (row.type == 'imageurl') return '图片url';
                            if (row.type == 'cdnurl') return '视频cdn';
                            if (row.type == 'videouploadurl') return '视频上传url';
                        }
                    },
                    {field: 'url', title: '视频加载地址', width: 100, align: 'center'},
                    {field: 'createDateStr', title: '创建时间', width: 40, align: 'center'},
                    {field: 'modifyDateStr', title: '修改时间', width: 40, align: 'center'},
                    {field: 'modifyName', title: '修改人员', width: 40, align: 'center'},
                    {field: 'description', title: '备注：', width: 80, align: 'center'},
                    {field: 'weight', title: '权重', width: 35, align: 'center'},
                    {
                        field: 'status',
                        title: '状态',
                        align: 'center',
                        width: 20,
                        formatter: function (value, row, index) {
                            if (row.status == 1) return '可用';
                            if (row.status == 0) return '不可用';
                        }
                    }

                ]],
                onLoadSuccess: function () {
                    $(this).datagrid('enableDnd');
                }
            });
        });

        var url;

        function destroyloadconfigurl() {
            var row = $(datagridId).datagrid('getSelected');
            if (row) {
                $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
                    if (r) {
                        $.get(deleteUrl, {id: row.id}, function (result) {
                            if (result["success"] == true) {
                                $(datagridId).datagrid('reload'); // reload the user data
                            } else {
                                showMessage("Error", result["message"]);
                            }
                        });
                    }
                });
            } else {
                showMessage("Error", noSelectedRowMessage);
            }
        }

        function newloadconfigurl() {
            $(adddialogueId).dialog('open').dialog('setTitle', addTitle);
            url = addUrl;
            //$(addFormId).form('clear');
            $("#creatorId").val(creatorId);

        }

        function editloadconfigurl() {
            var row = $(datagridId).datagrid('getSelected');
            if (row) {
                $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
                $(editFormId).form('load', row);
                $("#updatecreatorId").val(creatorId);
                url = updateUrl;
            } else {
                showMessage("Error", noSelectedRowMessage);
            }
        }

        function saveloadconfigurl(mydialogueId, myFormId) {
            if ($(myFormId).form('validate')) {
                $(myFormId).submit();
            } else {
                return false;
            }
        }
        function updateloadconfigurl(mydialogueId, myFormId) {
            if ($(myFormId).form('validate')) {
                $(myFormId).submit();
            } else {
                return false;
            }
        }

        function doSearch() {
            $(datagridId).datagrid('reload', getFormJson(searchFormId));
        }
    </script>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" style="padding:5px;height:auto">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; background: url(./images/tiaobg.png) repeat-x; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">域名管理</h2>
    </div>
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add"
           onclick="javascript:newloadconfigurl()" plain="true">添加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit"
           onclick="javascript:editloadconfigurl()" plain="true">编辑</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
           onclick="javascript:destroyloadconfigurl()" plain="true">删除</a>
    </div>
</div>

<!-- 弹出的添加对话框 -->
<div id="dlg" class="easyui-dialog" style="width:400px;height:500px;padding:10px 20px" closed="true"
     buttons="#dlg-buttons">
    <div class="ftitle">加载地址信息</div>
    <form id="fm" method="post" action="loadconfigurl/add" novalidate>
        <div class="fitem">
            <label>客户端平台系统:</label>
            <select name="clientPf" class="easyui-validatebox" required="true" invalidMessage="请选着客户端平台">
                <option value="" selected="selected">请选择</option>
                <option value="all">通用</option>
                <option value="ios">ios客户端</option>
                <option value="android">Android客户端</option>
                <option value="html5">Html5客户端</option>
            </select>
        </div>
        <div class="fitem">
            <label>加载地址:</label>
            <input type="text" id="url" missingMessage="前端播放视频后台提供的地址" name="url" required="true" class="easyui-validatebox" style="width:190px;">
        </div>
        <div class="fitem">
            <label>权重:</label>
            <input id="weight" name="weight" required="true" invalidMessage="请输入1-100之间的数字！"
                   missingMessage="请输入1-100之间的数字！" validType="length[0,2]" class="easyui-numberbox" style="width:60px;"/>
        </div>
        <div class="fitem">
            <label>描述:</label>
            <input type="text" id="description" name="description" class="easyui-validatebox" style="width:190px;">
        </div>
        <div class="fitem">
            <label>是否有效:</label>
            <select name="status" class="easyui-validatebox" required="true">
                <option value="1">有效</option>
                <option value="0">无效</option>
            </select>
        </div>
        <div class="fitem">
            <label>url类型:</label>
            <select name="type" class="easyui-validatebox" required="true">
                <option value="voiceurl">声音文件url</option>
                <option value="interfaceurl">接口url</option>
                <option value="imageurl">图片文件url</option>
                <option value="cdnurl">视频cdn</option>
                <option value="videouploadurl">视频上传url</option>
                <option value="chaturl">聊天服务器地址</option>
            </select>
        </div>
        <input type="hidden" name="id"/>
        <input type="hidden" name="creatorId" id="creatorId" value=""/>
    </form>
</div>

<!-- 对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveloadconfigurl('#dlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 弹出的修改对话框 -->
<div id="updatedlg" class="easyui-dialog" style="width:400px;height:500px;padding:10px 20px" closed="true"
     buttons="#updatedlg-buttons">
    <div class="ftitle">加载信息修改</div>
    <form id="updatefm" method="post" novalidate action="loadconfigurl/modify">
        <div class="fitem">
            <label>客户端平台系统:</label>
            <select name="clientPf" class="easyui-validatebox" required="true">
                <option value="all">通用</option>
                <option value="ios">ios客户端</option>
                <option value="android">Android客户端</option>
                <option value="html5">Html5客户端</option>
            </select>
        </div>
        <div class="fitem">
            <label>加载地址:</label>
           <input type="text" id="url" missingMessage="前端播放视频后台提供的地址" name="url" required="true" class="easyui-validatebox" style="width:190px;">
        </div>
        <div class="fitem">
            <label>权重:</label>
            <input id="weight" name="weight" required="true" invalidMessage="请输入1-100之间的数字！"
                   missingMessage="请输入1-100之间的数字！" validType="length[0,2]" class="easyui-numberbox" style="width:60px;"/>
        </div>
        <div class="fitem">
            <label>描述:</label>
            <input type="text" id="description" name="description" class="easyui-validatebox" style="width:190px;">
        </div>
        <div class="fitem">
            <label>是否有效:</label>
            <select name="status" class="easyui-validatebox" required="true">
                <option value="1">有效</option>
                <option value="0">无效</option>
            </select>
        </div>
        <div class="fitem">
            <label>url类型:</label>
            <select name="type" class="easyui-validatebox" required="true">
                <option value="voiceurl">声音文件url</option>
                <option value="interfaceurl">接口url</option>
                <option value="imageurl">图片文件url</option>
                <option value="cdnurl">视频cdn</option>
                <option value="videouploadurl">视频上传url</option>
                <option value="chaturl">聊天服务器地址</option>
            </select>
        </div>
        <input type="hidden" name="id"/>
        <input type="hidden" name="creatorId" id="updatecreatorId" value=""/>
    </form>
</div>

<!-- 对话框里的保存和取消按钮 -->
<div id="updatedlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
       onclick="updateloadconfigurl('#updatedlg','#updatefm')">修改</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
</div>

</body>
</html>
