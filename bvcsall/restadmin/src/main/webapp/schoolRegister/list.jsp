<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>校园大使报名</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type='text/javascript' src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script type='text/javascript' src="js/fp/jquery.iframe-transport.js"></script>
    <script type='text/javascript' src="js/fp/jquery.fileupload.js"></script>
    <script type='text/javascript' src="js/admin/query_schoolRegister.js"></script>
</head>
<body>

<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>

<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">校园大使报名</h2>
    </div>
    <div>
        <table>
            <tr>
                <td><label>搜索条件：</label></td>
                <td>
                    <select id="type" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="name">姓名</option>
                        <option value="phone">手机号</option>
                    </select>
                </td>
                <td>
                    <input type="text" name="param" id="param" style="width: 80px;" class="easyui-validatebox">
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>

</div>
</body>
</html>