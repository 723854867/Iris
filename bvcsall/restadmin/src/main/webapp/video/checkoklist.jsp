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
     var addTitle="新增视频";
     var editTitle="编辑视频";
     var deleteConfirmMessage="你确定要删除吗?";
     var noSelectedRowMessage="你没有选择行";
     var searchFormId="#searchForm";
     var pageSize=50;
     
     var listUrl="video/searchListPage?filters['flowStatEq']=published&filters['dataFromEq']=myvideo_restwww";
     var updateUrl="video/update";
     var deleteUrl="video/deleteallpage";
     
     var url;
     
     function datagridList(){
    	 $(datagridId).datagrid({
    			fitColumns:true,
    			rownumbers : true,
    		    striped: true, 
    		    fit:true,
    		    pagination : true,
    		    pageNumber : 1,
    		    pageList : [pageSize,pageSize*2,pageSize*3],
    		    pageSize : pageSize,
    		    pagePosition : 'bottom',
    		    singleSelect : false,
    		    selectOnCheck:true,
    		    selectOnCheck:false,
    		    nowrap:true,
    		    url:listUrl,
    		    columns:[[
    						{field:'ck',wdith:100,checkbox:true},
    						{field:'img',title:'缩略图',wdith:100,height:100,formatter:function(value,row,index){
   								return "<img src='<%=request.getContextPath()%>/download"+value+"' width='100' height='100'/>";
    						}},
    						{field:'name',title:'名称',wdith:150},
    						{field:'createDate',title:'上传时间',width:100,formatter:function(value,row,index){
    							return new Date(value).toLocaleString()+" 上传";
    						}},
    						{field:'operation',title:'操作',align:'center',width:50,formatter:function(value,row,index){
    					        	return '<a href="video/detail?id='+row.id+'" class="easyui-linkbutton" iconCls="icon-ok">查看</a>';
    					    }}
    				    ]],
    		    onLoadSuccess:function(){
    		    	$(this).datagrid('enableDnd');
    		    }
    	});
     }
     
	$(function() {
		datagridList();
	});

	var url;

	function destroyUser(){
		var row = $(datagridId).datagrid('getChecked');
		var ids=[];
		var inum=0;
		for(var r in row){
			ids.push(row[r]['id']);
		}
		ids=ids.join(",");
		if (row){
			$.messager.confirm('Confirm',deleteConfirmMessage,function(r){
				if (r){
					$.post('video/logicremove',{ids:ids},function(result){
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
	
	function doClickSearch(value,name){
		$("#searchtext").attr("name","filters['"+name+"']");
		$("#searchtext").val(value);
		$(datagridId).datagrid('reload',getFormJson( searchFormId));
	}
	
	
	function clickCheckOk(){
		var row = $(datagridId).datagrid('getChecked');
		var ids=[];
		var inum=0;
		for(var r in row){
			ids.push(row[r]['id']);
		}
		ids=ids.join(",");
		if (row){
			$.messager.confirm('Confirm',deleteConfirmMessage,function(r){
				if (r){
					$.post("video/checkTongGuoPage",{ids:ids},function(result){
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
	
	function showAll(){
		var data=$(datagridId).datagrid('getData');
		pageSize=data.total;
		datagridList();
	}
</script>
  </head>
<body>
	<!-- 列表 -->
     <table id="tt" data-options="border:false,toolbar:'#tb'">
	 </table>
    
    <!-- 列表上面的按钮和搜索条件  -->
     <div id="tb" style="padding:5px;height:auto">
		<form action="" id="searchForm">
		<div style="margin-bottom:5px">
			<table style="width:100%">
			<tr>
			<td align="left" >
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="javascript:destroyUser()" plain="true">删除</a>
			</td>
			<td align="right" >
				    <input class="easyui-searchbox" name="name" id="searchtext" data-options="prompt:'请输入',menu:'#mm',searcher:doClickSearch" style="width:300px"></input>
					<div id="mm">
						<div data-options="name:'tag',iconCls:'icon-ok'">标签</div>
						<div data-options="name:'name'">标题</div>
						<div data-options="name:'subject'">专题</div>
					</div>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:showAll()" plain="true">显示全部</a>
			</td>
			</table>
		</div>
		</form>
	</div>
	
	<!-- 弹出的添加对话框 -->
	<div id="updatedlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px" closed="true" buttons="#updatedlg-buttons">
		<form id="updatefm" method="post" novalidate>
			<input type="hidden" name="id"/>
		</form>
	</div>
	
	<!-- 对话框里的保存和取消按钮 -->
	<div id="updatedlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#updatedlg','#updatefm')">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#updatedlg').dialog('close')">取消</a>
	</div>
	
</body>
</html>
