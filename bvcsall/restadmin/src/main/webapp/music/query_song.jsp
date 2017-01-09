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
    <title>查看音乐列表信息</title>
    <script type='text/javascript' src='js/admin/query_song.js'></script>
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
                    <label>歌曲名称：</label>
                </td>
                <td>
                    <input class="easyui-textbox" style="width:80px" name="name" id="queryName" value=""/>
                </td>
                <td>
                    <label>歌手分类：</label>
                </td>
                <td>
                    <select name="singerType" id="querySingerType" class="easyui-validatebox easyui-combobox"
                            style="width: 120px;" data-options="
                            valueField: 'id', textField: 'text',
                            url:'comboBox/getSingerTypeCombobox',
                            require:true"/>
                </td>
                <td>
                    <label>歌手：</label>
                </td>
                <td>
                    <input type="text" name="querySingerId" id="querySingerId" class="easyui-validatebox" value="">
                </td>
                <td>
                    <label>专辑：</label>
                </td>
                <td>
                    <select name="albumId" id="queryAlbumId" class="easyui-validatebox easyui-combobox"
                            style="width: 120px;" data-options="
                            valueField: 'id', textField: 'text',
                            url:'comboBox/getAlbumCombobox',
                            require:true"/>
                </td>
                <td>
                    <label>歌曲类型：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="type" id="queryType">
                        <option value="">请选择</option>
                        <option value="1">伴奏</option>
                        <option value="2">原唱</option>
                    </select>
                </td>
                <td>
                    <label>状态：</label>
                </td>
                <td>
                    <select class="easyui-combobox" name="state" id="queryState">
                        <option value="">请选择</option>
                        <option value="1">上架</option>
                        <option value="0">下架</option>
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
        <a href="javascript:;" onclick="insertSingerDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加歌手</a>
        <a href="javascript:;" onclick="insertAlbumDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加专辑</a>
        <a href="javascript:;" onclick="insertSongDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加歌曲</a>
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

<!-- 添加歌手弹出框 start -->
<div id="insert_singer_dlg" class="easyui-dialog" style="width:500px;height:200px;" closed="true"
     buttons="#insert-singer-dlg-buttons">
    <form id="insertSingerForm" method="post">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">歌手分类：</td>
                <td style="text-align: left;">
                    <select name="singerType" id="singerType" class="easyui-validatebox easyui-combobox"
                            style="width: 120px;" data-options="
                            valueField: 'id', textField: 'text',
                            url:'comboBox/getSingerTypeCombobox',
                            require:true"/>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">歌手名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" required="true" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-singer-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertSinger()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_singer_dlg').dialog('close')">取消</a>
</div>
<!-- 添加歌手弹出框 end -->

<!-- 添加专辑弹出框 start -->
<div id="insert_album_dlg" class="easyui-dialog" style="width:500px;height:200px;" closed="true"
     buttons="#insert-album-dlg-buttons">
    <form id="insertAlbumForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">专辑名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" required="true" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">专辑封面：</td>
                <td style="text-align: left;">
                    <input type="file" name="albumCoverUrl" id="albumCoverUrl" accept="jpg,png,gif" class="easyui-validatebox">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-album-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertAlbum()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_album_dlg').dialog('close')">取消</a>
</div>
<!-- 添加专辑弹出框 end -->

<!-- 添加歌曲弹出框 start -->
<div id="insert_song_dlg" class="easyui-dialog" style="width:500px;height:330px;" closed="true"
     buttons="#insert-song-dlg-buttons">
    <form id="insertSongForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">歌曲名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" required="true" style="width: 200px;" class="easyui-validatebox" value="">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">歌曲类型：</td>
                <td style="text-align: left;">
                    <select class="easyui-combobox" name="type">
                        <option value="1">伴奏</option>
                        <option value="2">原唱</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">歌词文件(lrc)：</td>
                <td style="text-align: left;">
                    <input type="file" name="lyricFile" id="lyricFile" accept="lrc" class="easyui-validatebox">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">歌曲文件(mp3或m4a)：</td>
                <td style="text-align: left;">
                    <input type="file" name="songFile" id="songFile" accept="mp3" class="easyui-validatebox">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">专辑：</td>
                <td style="text-align: left;">
                    <select name="albumId" id="albumId" class="easyui-validatebox easyui-combobox"
                            style="width: 120px;" data-options="
                            valueField: 'id', textField: 'text',
                            url:'comboBox/getAlbumCombobox',
                            require:true"/>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">歌手：</td>
                <td style="text-align: left;">
                    <select name="singerId" id="singerId" class="easyui-validatebox easyui-combobox"
                            style="width: 120px;" data-options="
                            valueField: 'id', textField: 'text',
                            multiple:true,
                            url:'comboBox/getSingerCombobox',
                            require:true"/>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="insert-song-dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doInsertSong()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#insert_song_dlg').dialog('close')">取消</a>
</div>
<!-- 添加歌曲弹出框 end -->


<c:import url="/main/common.jsp"/>
</body>
</html>
