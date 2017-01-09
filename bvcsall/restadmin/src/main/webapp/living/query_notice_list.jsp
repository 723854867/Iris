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
  <title>直播预告管理</title>
  <script type='text/javascript' src='js/admin/query_notice.js'></script>
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
    <h2 style="float:left;padding-left:10px;margin: 1px">直播预告管理</h2>
  </div>
<%--  <div id="search_form">
    <table>
      <tr>
        <td>
          <label>用户名/ID：</label>
        </td>
        <td>
          <input class="easyui-textbox" style="width:150px" name="key" id="key" value=""/>
        </td>
        <td>
          <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
             iconCls="icon-search">搜索</a>
        </td>
      </tr>
    </table>
  </div>--%>
</div>
<!-- 添加礼物弹出框 end -->
<c:import url="/main/common.jsp"/>

</body>
</html>
