<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2015/12/23
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>查看歌手分类信息</title>
    <script type='text/javascript' src='js/admin/query_singer_type.js'></script>
    <style>
        .bc{
            background-color: #666666;
            color: #FFFFFF!important;
            padding: 4px;
        }
    </style>
</head>
<body>
<!-- 列表 -->
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
            <a href="song/forwardSongList" <c:if test="${selected eq 'song'}">class="bc" </c:if> style="text-decoration: none;color: #000;">歌曲管理</a>
            <a href="song/forwardSingerList" <c:if test="${selected eq 'singer'}">class="bc" </c:if> style="text-decoration: none;color: #000;">歌手管理</a>
            <a href="song/forwardAlbumList" <c:if test="${selected eq 'album'}">class="bc" </c:if> style="text-decoration: none;color: #000;">专辑管理</a>
            <a href="song/forwardSingerTypeList" <c:if test="${selected eq 'singerType'}">class="bc" </c:if> style="text-decoration: none;color: #000;">歌手类型管理</a>
        </h2>
    </div>
    <div id="search_form">
        <table>
            <tr>
                <td>
                    <label>ID：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:80px" name="id" id="queryId" value=""/>
                </td>
                <td>
                    <label>分类名称：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:80px" name="name" id="queryName" value=""/>
                </td>
                <td>
                    <label>状态：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="state" id="queryState">
                        <option value="">请选择</option>
                        <option value="1">正常</option>
                        <option value="0">删除</option>
                    </select>
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
    <div>
        <a href="javascript:;" onclick="insertSingerTypeDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加歌手类型</a>
    </div>
</div>

<!-- 添加歌手分类弹出框 start -->
<div id="insert_singer_type_dlg" class="easyui-dialog" style="width:500px;height:150px;" closed="true"
     buttons="#insert-singer-type-dlg-buttons">
    <form id="insertSingerTypeForm" method="post">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">类型名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" required="true" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-singer-type-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertSingerType()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_singer_type_dlg').dialog('close')">取消</a>
</div>
<!-- 添加歌手分类弹出框 end -->

<c:import url="/main/common.jsp"/>
</body>
</html>
