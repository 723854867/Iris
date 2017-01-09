<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 
<script type="text/javascript" src="js/ajaxfileupload.js"></script>
 -->
<script src="js/fp/vendor/jquery.ui.widget.js"></script>
<script src="js/fp/jquery.iframe-transport.js"></script>
<script src="js/fp/jquery.fileupload.js"></script>
<title>最热视频</title>
<script type="text/javascript">

	$(function () {
		labelList();
		
	});
	
	function pageSub() {
		var labelName=$('#labelName').val();
		if(!labelName){
			alert('请选择话题!');
			return
		}
		
		var displayOrder=$('#displayOrder').val();
		if(!displayOrder){
			alert('请输入排序权重!');
			return
		}
		
		
		$('#myform').submit();
	}
	
	var labelTable='#labelTable';
	
	var searchFormId="searchForm";
	
	var pageSize=20;
	
	var labelListJsonUrl="activity/labelListJson";
	
	
	function showTargetDialog(){
		$("#label_dlg").show();
		$("#label_dlg").dialog({
			 title: '选择话题',
			 width: 800,
			 height: 700,
			 closed: false,
			 cache: true,
			 modal: true
	    });
	}
	
	function labelList(){
		 $(labelTable).datagrid({
				fitColumns:true,
				fit:true,
				rownumbers : true,
			    pagination : true,
			    pageNumber : 1,
			    pageList : [pageSize,pageSize*2,pageSize*3],
			    pageSize : pageSize,
			    pagePosition : 'bottom',
			    selectOnCheck:true,
			    singleSelect: true,
			    url:labelListJsonUrl,
			    columns:[[
						{field:'ck',width : 100,checkbox:true},
						{field:'name',title:'话题名称',width : 100},
						{field:'num',title:'使用数量',width : 100}
				    ]],
			    onLoadSuccess:function(){
			    	$(this).datagrid('enableDnd');
			    }
		});
	}
	
	
	
	function labelListSearch(){
		$(labelTable).datagrid('reload',{keyword:$('#searchForm').find('[name=keyword]').val()});
	}
	
	function labelListSearch1(value,name){
		$("#searchForm").attr("method","POST"); 
		$("#searchForm").submit();
	}
	
	
	function selectLabel(){
		var row = $(labelTable).datagrid('getChecked');
		var labelName = row[0].name;
		$("#labelName").val(labelName);
		$("#label_dlg").dialog("close");
	}
	
	
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" action="activity/hotLabelAdd">
				<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;"  class="form-body" >

				<tr>
					<td>选择话题:</td>
					<td>
						<input  name=labelName id="labelName" class="  " readonly="readonly" value="${hotLabel.labelName }"  onfocus="showTargetDialog();" ></input>
						<input  name="id" id="id" type="hidden" style="width:50%" value="${hotLabel.labelName }" ></input>
					</td>
				</tr>
				<tr>
					<td>排序权重:</td>
					<td>
					<input name="displayOrder" id="displayOrder"    class="easyui-numberbox"  min="1"  value="${hotLabel.displayOrder }" ></input>
					<div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p" class="easyui-progressbar" ></div>
					</td>
				</tr>


				<tr>
					<td colspan="2" align="center">
					<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="pageSub()">保存</a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="javascript:history.back();">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>



<!-- 弹出的选择视频对话框 -->
<div id="label_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true" buttons="#vdlg-buttons">
	 <table id="labelTable" data-options="border:false,toolbar:'#tb'">
	 </table>
    <!-- 列表上面的按钮和搜索条件  -->
     <div id="tb" style="padding:5px;height:auto">
		<form action="activity/labelList" id="searchForm">
			<div>
				<input class="easyui-searchbox" name="keyword" id="searchtext" data-options="prompt:'请输入话题名称',menu:'#mm',searcher:labelListSearch" style="width:300px"  value="${keyword}" ></input>
				<div id="mm">
					<div data-options="name:'keyword',iconCls:'icon-ok'">话题名称</div>
				</div>
			</div>
		        <hr/>
		</form>
	</div>

</div>

<div id="vdlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectLabel();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#label_dlg').dialog('close')">取消</a>
</div>
</body>
</html>