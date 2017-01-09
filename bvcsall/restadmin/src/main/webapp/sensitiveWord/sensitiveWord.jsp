<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>敏感词管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/jquery.form.js"></script>
    <script type="text/javascript" src="js/admin/query_sensitive_word.js"></script>
</head>
<body>
<table data-options="border:false,toolbar:'#dataGridToolbar'" id="displayTable">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">敏感词管理</h2>
    </div>
    <div style="margin-left:10px;margin-top:10px;">
         <span>
             <label>敏感词：</label>
             <span>
                 <input type="text" class="textbox" style="width: 200px;height:24px;" id="word" value="">
             </span>
         </span>
         <span>
             <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                iconCls="icon-search">搜索</a>
         </span>
    </div>
    <div style="margin-top:10px;margin-left:10px;">
        <span>
            <input type="text" class="textbox" style="width: 200px;height:24px;" id="wordInsert" value="">
        </span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="doInsert()"
           plain="true">添加</a>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-import" onclick="displayImport()"
           plain="true">导入敏感词</a>
    </div>
</div>

<div id="dialog" style="padding:5px;width:450px;height:280px;display:none;"
     title="文件导入" toolbar="#dlg-toolbar" buttons="#dlg-buttons">
    <div class="easyui-panel" style="border: none;">
        <form method="post" id="batchImportForm" enctype="multipart/form-data">
            <div style="margin-bottom:20px">
                <div>
                    <label>文件:</label>
                    <input name="wordFile" type="file" buttonText="选择文件" data-options="prompt:'请选择文件！'">
                </div>
                <div><span style="color: red;">仅限.txt格式的文件，每个敏感词回车换行</span></div>
            </div>
            <div>
                <input type="button" onclick="doBatchImport()" class="easyui-linkbutton" style="width:100%" value="导入">
            </div>
        </form>
    </div>
</div>

</body>
</html>
