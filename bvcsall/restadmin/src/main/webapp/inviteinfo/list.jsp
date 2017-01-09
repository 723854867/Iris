<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>邀请信息</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="js/admin/query_invite.js"></script>
    <script type="text/javascript">
        var creatorId =${uid};
        function editinviteinfo() {
            var row = $(datagridId).datagrid('getSelected');
            if (row) {
                $(editdialogueId).dialog('open').dialog('setTitle', editTitle);
                $("#invitePic").attr("src", "/restadmin/download" + row.picPath);
                $(editFormId).form('load', row);
                $("#updatecreatorId").val(creatorId);
            } else {
                showMessage("Error", noSelectedRowMessage);
            }
        }
    </script>
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
            <h2 style="float:left;padding-left:10px;margin: 1px">邀请信息管理</h2>
    </div>
    <div>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:editinviteinfo()"
           plain="true">编辑</a>
    </div>
</div>

<!-- 弹出的编辑对话框 -->
<div id="updatedlg" class="easyui-dialog" style="width:500px;height:530px;padding:2px 5px;background:#fff;"
     closed="true"
     buttons="#updatedlg-buttons">
    <form id="updatefm" method="post" novalidate action="inviteinfo/modify" enctype="multipart/form-data">
        <table class="table-doc" width="100%">
            <tr>
                <td>邀请平台名称:</td>
                <td>
                    <input name="platformName" class="easyui-validatebox" required="true">
                </td>
            </tr>
            <tr>
                <td>邀请标示:</td>
                <td>
                    <input name="platformMark" class="easyui-validatebox" required="true" readonly="readonly">
                </td>
            </tr>
            <tr>
                <td>标题:</td>
                <td>
                    <input id="title" name="title"/>
                </td>
            </tr>
            <tr>
                <td>邀请内容图片:</td>
                <td>
                    <div style="float: left;overflow: hidden;">
                        <input type="file" id="updatefile1" name="file" accept="png,jpg,jpeg,gif"/>
                    </div>
                    <div style="float: left;">
                        <img src="" id="invitePic" style="width: 70px;height: 70px;border: 1px solid #CCCCCC;">
                    </div>
                </td>
            </tr>
            <tr>
                <td>邀请内容描述:</td>
                <td>
                    <textarea autocomplete="off" id="description" name="description" class="easyui-validatebox"
                              required="true"
                              placeholder="" style="margin-left: 0px; margin-right: 0px; height: 90px; width: 300px;"
                              title=""></textarea>
                </td>
            </tr>
            <tr>
                <td>邀请url:</td>
                <td>
                    <textarea autocomplete="off" id="inviteInfoUrl" name="inviteInfoUrl" class="easyui-validatebox"
                              required="true" placeholder=""
                              style="margin-left: 0px; margin-right: 0px; height: 60px; width: 300px;"
                              title=""></textarea>
                </td>
            </tr>
            <tr>
                <td>邀请url:</td>
                <td>
                    <select name="status" class="easyui-validatebox easyui-combobox" required="true"
                            style="width: 100px;">
                        <option value="1">有效</option>
                        <option value="0">无效</option>
                    </select>
                    <input type="hidden" name="id"/>
                    <input type="hidden" name="creatorId" id="updatecreatorId" value=""/>
                </td>
            </tr>
        </table>
        <div id="updatedlg-buttons">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
               onclick="updateinviteinfo('#updatedlg','#updatefm')">保存</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
               onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
        </div>
    </form>
</div>
</body>
</html>
