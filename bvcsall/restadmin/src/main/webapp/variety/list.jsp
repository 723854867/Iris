<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>网综往期</title>
    
    <link rel="stylesheet" href="files/Jcrop/css/jquery.Jcrop.css" type="text/css"/>
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
    <script src="js/fp/jquery.iframe-transport.js"></script>
    <script src="js/fp/jquery.fileupload.js"></script>
    <script src="files/Jcrop/js/jquery.Jcrop.js"></script>
    <script type='text/javascript' src='js/admin/query_varietyHistory.js'></script>
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
        <h2 style="float:left;padding-left:10px;margin: 1px">网综往期</h2>
    </div>
    <div>
    	<form action="" id="addForm">
	        <table>
	            <tr>
	                <td>
	                    <label>期数：</label>
	                </td>
	                <td>
	                    <input class="easyui-validatebox easyui-textbox" id="name" name="name" required="true" value="" >
	                </td>
	                <td>
	                    <label>名单：</label>
	                </td>
	                <td>
	                    <input class="easyui-validatebox easyui-textbox" id="uids" name="uids" required="true" value="" >
	                </td>
	                <td>
	                    <label>标签：</label>
	                </td>
	                <td>
	                    <input class="easyui-validatebox easyui-textbox" id="tag" name="tag" required="true" value="" >
	                </td>
	                <td>
	                    <label>视频地址：</label>
	                </td>
	                <td>
	                    <input class="easyui-validatebox easyui-textbox" id="playUrl" name="playUrl" required="true" value="" >
	                </td>
	                <td>
	                    <label>视频封面：</label>
	                </td>
	                <td>
	                	<input type="file" id="picUpload" name="file">
	                    <input type="hidden" id="picUrl" name="picUrl" required="true" value="">
	                </td>
	                <td>
	                    <a href="javascript:;" onclick="saveVarietyHistory()" class="easyui-linkbutton" iconCls="icon-ok">添加</a>
	                </td>
	            </tr>
	        </table>
        </form>
    </div>
</div>
<!-- 添加礼物弹出框 end -->
<c:import url="/main/common.jsp"/>
<div id="editdlg" class="easyui-dialog" style="width:600px;height:450px;padding:10px 20px" data-options="modal: true,cache:false" closed="true"
     buttons="#dlg_buttons">
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
        <form action="" id="editForm">
        	<input type="hidden" id="varietyId" name="id">
            <table class="table-doc">
		        <tr>
		            <td style="text-align: right;">名称：</td>
		            <td style="text-align: left;">
		            	<input class="easyui-validatebox easyui-textbox" id="udname" name="name" required="true" value="" >
		            </td>
		        </tr>
		        <tr>
		            <td style="text-align: right;">往期名单：</td>
		            <td style="text-align: left;">
		            	<input class="easyui-validatebox easyui-textbox" id="uuids" name="uids" required="true" value="" >
		            </td>
		        </tr>
		        <tr>
		            <td style="text-align: right;">网综视频：</td>
		            <td style="text-align: left;">
		            	<input class="easyui-validatebox easyui-textbox" id="uplayUrl" name="playUrl" required="true" value="" >
		            </td>
		        </tr>
		        <tr>
		            <td style="text-align: right;">视频封面：</td>
		            <td style="text-align: left;">
		            	<input type="file" id="upicUpload" name="file"> <img id="uimg" alt="" src="" width="100px" height="100px">
		            	<input type="hidden" id="upicUrl" name="picUrl" required="true" value="" >
		            </td>
		        </tr>
		        <tr>
		            <td style="text-align: right;">标签：</td>
		            <td style="text-align: left;">
		            	<input class="easyui-validatebox easyui-textbox" id="utag" name="tag" required="true" value="" >
		            </td>
		        </tr>
		    </table>
        </form>
    </div>

</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg_buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="editSetting();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#editdlg').dialog('close')">取消</a>
</div>
</body>
</html>
