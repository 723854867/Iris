<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>直播投诉</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ==';</script>
    <script type='text/javascript' src='js/admin/query_live_complaint.js'></script>
</head>
<body>
<!-- 列表 -->
<table id="tt" rules="rows" cellspacing="0" style="border: 1px solid;" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">直播投诉</h2>
    </div>
    <div>
        <form action="" id="searchForm">
            <table>
                <tr>
                    <td><label>投诉类型：</label></td>
                    <td>
                        <select name="content" class="easyui-combobox">
                            <option value="" selected>全部</option>
                            <option value="色情">色情</option>
                            <option value="欺诈">欺诈</option>
                            <option value="骚扰">骚扰</option>
                            <option value="侵权">侵权</option>
                            <option value="其他">其他</option>
                        </select>
                    </td>
                    <td><label>处理状态：</label></td>
                    <td>
                        <select name="stat" class="easyui-combobox">
                            <option value="" >全部</option>
                            <option value="1">已处理</option>
                            <option value="0" selected>未处理</option>
                        </select>
                    </td>
<!--                     <td>投诉时间：</td> -->
<!--                     <td> -->
<!--                         <input type="text" name="filters['starttime']" id="date_1"/> -->
<!--                         至 -->
<!--                         <input type="text" name="filters['endtime']" id="date_2"/> -->
<!--                     </td> -->
                    <td>
                        <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                           iconCls="icon-search" style="height:20px;line-height:20px;">搜索</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
<!--     <div> -->
<!--         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="cancelComplain()" -->
<!--            plain="true">取消投诉</a> -->
<!--         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="destroyComplain()" -->
<!--            plain="true">删除</a> -->
<!--     </div> -->

</div>

<!-- 弹出的添加或者编辑对话框 -->
<div id="dlg" class="easyui-dialog" style="width:415px;height:390px;" closed="true">
    <div id="playerContainer"></div>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#dlg','#fm')">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<%--<div id="updatedlg" class="easyui-dialog" style="width:400px;height:380px;padding:10px 20px" closed="true"
     buttons="#updatedlg-buttons">
    <div class="ftitle">修改投诉</div>
    <!-- 修改 -->
    <form id="updatefm" method="post" novalidate>
        <div class="fitem">
            <label>投诉名称:</label>
            <input name="title" class="easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>内容:</label>
            <input name="content" class="easyui-validatebox" required="true">
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
</div>--%>
<input type="hidden" id="video_play_url_prefix" value="${video_play_url_prefix}">
<input type="hidden" id="img" value="${img}">
</body>
</html>
