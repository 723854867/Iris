<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html  style="background: white">
 <head>
    <title>MyHtml.html</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
     <style type="text/css">
		#fm{
		margin:0;
		padding:10px 30px;
		}
		.ftitle{
		font-size:14px;
		font-weight:bold;
		padding:5px 0;
		margin-bottom:10px;
		border-bottom:1px solid #ccc;
		}
		.fitem{
		margin-bottom:5px;
		}
		.fitem label{
		display:inline-block;
		width:80px;
		}
	 </style>
     <script type="text/javascript">
     var datagridId="#tt";
     var adddialogueId="#dlg";
     var editdialogueId="#updatedlg";
     var addFormId="#fm";
     var editFormId="#updatefm";
     var addTitle="新增频道";
     var editTitle="编辑频道";
     var deleteConfirmMessage="你确定要删除吗?";
     var noSelectedRowMessage="你没有选择行";
     var searchFormId="#searchForm";
     var pageSize=50;
     
     var listUrl="channel/listpage";
     var updateUrl="channel/update";
     var deleteUrl="channel/delete";
     var addUrl="channel/create";
     
     var url;
     
     
	$(function() {
		$(datagridId).datagrid({
		fitColumns:true,
		rownumbers : true,
	    striped: true, 
	    pagination : true,
	    pageNumber : 1,
	    pageList : [pageSize,pageSize*2,pageSize*3],
	    pageSize : pageSize,
	    pagePosition : 'bottom',
	    singleSelect : true,
	    selectOnCheck:false,
	    nowrap:true,
	    url:listUrl,
	    columns:[[
					{field:'name',title:'频道名称',width:100},
					{field:'useStat',title:'状态',width:100,formatter:function(value,row,index){
						if(value == 'live') return '启用';
						if(value == 'keep') return '禁止';
					}},
					{field:'createDate',title:'创建时间',width:100},  
					{field:'modifyDate',title:'修改时间',width:100}
			    ]],
		});
		
		
	});

	var url;

	function destroyUser(){
		var row = $(datagridId).datagrid('getSelected');
		if (row){
			$.messager.confirm('Confirm',deleteConfirmMessage,function(r){
				if (r){
					$.get(deleteUrl,{id:row.id},function(result){
						if (result["success"]==true){
							$(datagridId).datagrid('reload'); // reload the user data
						} else {
							showMessage("Error",result["message"]);
						}
					});
				}
			});
		}else{
			showMessage("Error",noSelectedRowMessage);
		}
	}
	
	function newUser(){
		$(adddialogueId).dialog('open').dialog('setTitle',addTitle);
		url = addUrl;
	}
	
	function editUser(){
		var row = $(datagridId).datagrid('getSelected');
		if (row){
			$(editdialogueId).dialog('open').dialog('setTitle',editTitle);
			$(editFormId).form('load',row);
			url = updateUrl;
		}else{
			showMessage("Error",noSelectedRowMessage);
		}
	}
	
	function saveUser(mydialogueId,myFormId){
		$.ajax({
            url: url,
            data: getFormJson( myFormId),
            type: "post",
            dataType: "json",
            beforeSend: function(){
             return $( myFormId).form( 'validate');
            },
            success: function (result){
                if (result[ "success"]== true){
                    $( mydialogueId).dialog( 'close'); // close the dialog
                    $( datagridId).datagrid( 'reload'); // reload the user data
                } else {
                    showMessage( "Error",result[ "message"]);
                }
            }
        });
	}
	
	function doSearch(){
		$(datagridId).datagrid('reload',getFormJson( searchFormId));
	}
</script>
  </head>
<body>
	<!-- 列表 -->
    <table id="tt" data-options="border:false,toolbar:'#tb'">
    </table>
    
    <!-- 列表上面的按钮和搜索条件  -->
     <div id="tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:newUser()" plain="true"></a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" onclick="javascript:editUser()" plain="true"></a> 
		</div>
	</div>
	
	<!-- 弹出的添加或者编辑对话框 -->
	<div id="dlg" class="easyui-dialog" style="width:400px;height:380px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
		<div class="ftitle">新增频道</div>
		<!-- 添加 -->
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<label>频道名称:</label>
				<input name="name" class="easyui-validatebox" required="true">
			</div>
			<input type="hidden" name="id"/>
		</form>
	</div>
	
	<!-- 添加对话框里的保存和取消按钮 -->
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#dlg','#fm')">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
	
	<div id="updatedlg" class="easyui-dialog" style="width:400px;height:380px;padding:10px 20px" closed="true" buttons="#updatedlg-buttons">
		<div class="ftitle">修改频道</div>
		<!-- 修改 -->
		<form id="updatefm" method="post" novalidate>
			<div class="fitem">
				<label>频道名称:</label>
				<input name="name" class="easyui-validatebox" required="true">
			</div>
			<input type="hidden" name="id"/>
		</form>
	</div>
	
	<!-- 编辑对话框里的保存和取消按钮 -->
	<div id="updatedlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#updatedlg','#updatefm')">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
	</div>
	
</body>
</html>
