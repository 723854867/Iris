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
     var recoverConfirmMessage="你确定要恢复吗?";
     
     var noSelectedRowMessage="你没有选择行";
     var searchFormId="#searchForm";
     var pageSize=50;
     
     var listUrl="video/searchListPage?filters['flowStatEq']=delete";//&filters['dataFromEq']=myvideo_restwww
     var updateUrl="video/update";
     
     function datagridList(){
    	 $(datagridId).datagrid({
    		    height:$("#mainFrame").height(),
    			width:$("#mainFrame").width(),
    			fitColumns:true,
    			rownumbers : true,
    		    striped: true, 
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
    					        	return '<a href="video/modify_ui?id='+row.id+'" class="easyui-linkbutton" iconCls="icon-ok">编辑</a>';
    					    }}
    				    ]],
    		    onLoadSuccess:function(){
    		    	$(this).datagrid('enableDnd');
    		    }
    	});
     }
     
	
	
	
	function doSearch(){
		$(datagridId).datagrid('reload',getFormJson( searchFormId));
	}
	
	function doClickSearch(value,name){
		 $("searchtext").val(value);
		 $("#searchtext").attr("name","filters['"+name+"']");
		 $("#searchtext").val(value);
		 doSearch();
	}
	
	function clickCheck(index){
		alert(index);
	}
	/*recover videos*/
	function recoverVideo(){
		var row = $(datagridId).datagrid('getChecked');
		var ids=[];
		var inum=0;
		for(var r in row){
			ids.push(row[r]['id']);
		}
		ids=ids.join(",");
		if (row){
			$.messager.confirm('Confirm',recoverConfirmMessage,function(r){
				if (r){
					$.post("video/recoverPage",{ids:ids},function(result){
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
	
	$(function() {
		/*初始化列表*/
		datagridList();
	});
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
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:recoverVideo()" plain="true">恢复</a>
			</td>
			<td align="right" >
				    <input class="easyui-searchbox" name="filters['nameEq']" id="searchtext" data-options="prompt:'请输入',menu:'#mm',searcher:doClickSearch" style="width:300px"></input>
					<div id="mm">
						<div data-options="name:'tag',iconCls:'icon-ok'">标签</div>
						<div data-options="name:'title'">标题</div>
						<div data-options="name:'subject'">专题</div>
					</div>
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:showAll()" plain="true">显示全部</a>
			</td>
			</table>
		</div>
		</form>
	</div>
</body>
</html>
