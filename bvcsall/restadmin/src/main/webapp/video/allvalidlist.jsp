<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html  style="background: white">
 <head>
    <title>全部视频</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script src="js/fp/vendor/jquery.ui.widget.js"></script>
<script src="js/fp/jquery.iframe-transport.js"></script>
<script src="js/fp/jquery.fileupload.js"></script>
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
     
     var addUrl="video/updatevideo";
     var listUrl="video/allvalidlistPage";
     var updateUrl="video/update";
     var deleteUrl="video/deleteallpage";
     
     var url;
     
     function datagridList(){
    	 $(datagridId).datagrid({
    			fitColumns:true,
    			rownumbers : true,
    		    striped: false, 
    		    fit:true,
    		    pagination : true,
    		    pageNumber : 1,
    		    pageList : [pageSize,pageSize*2,pageSize*3],
    		    pageSize : pageSize,
    		    pagePosition : 'bottom',
    		    singleSelect : false,
    		    selectOnCheck:true,
    		    nowrap:true,
    		    url:listUrl,
				onClickRow: function (rowIndex, rowData) {
					$("#id_"+rowIndex).focus();
                },
    		    columns:[[
 						{field:'ck',wdith:100,checkbox:true},
 						{field:'img',title:'缩略图',wdith:100,height:100,formatter:function(value,row,index){
 							if($.trim(row.videoPic)!=""){
 								return "<img src='/restadmin/download"+row.videoPic+"' width='100' height='100'/>";
 							}
 						}},
 						{field:'name',title:'名称',width:100},
 						{field:'playKey',title:'PlayKey',width:100},
 						{field:'createDate',title:'上传时间',width:70,align:'center',formatter:function(value,row,index){
 							return new Date(value).toLocaleString()+" 上传";
 						}},
 						{field:'isRecommend',title:'是否推荐',width:30,align:'center',formatter:function(value,row,index){
 							if(row.isRecommend==0){
 								return "否";	
 							}else{
 								return "<font color='red'><b>是</b></font>";
 							}
 						}},
 						{field:'dataFrom',title:'数据来源',width:80,formatter:function(value,row,index){
 							if(row.dataFrom=="myvideo_restadmin"){
 								return "移动麦视后台";
 							}else if(row.dataFrom=="myvideo_admin"){
 								return "麦视网站后台";
 							}else if(row.dataFrom=="myvideo_restwww"){
 								return "麦视手机上传";
 							}else{
 								return "";
 							}
 						}},
						{
							field:'sordNum',
							title:'排序',
							align:'center',
							hidden:true,
							editable:true,
					        formatter:function(value,row,index){
					        	return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class='easyui-numberbox' id=id_"+index+" name='demo' value='"+row.orderNum+"' style='width:50px;'/>"
						        	+"<input type='hidden' id=hidden_"+index+" value='"+row.playCountToday+"' />"
						        	+"&nbsp;|&nbsp;<button style='width:60px;height:20px;cursor: pointer;' onclick='submitOrderNum("+index+");'>提交</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					        }
				        },
 						
 						{field:'operation',title:'操作',align:'center',width:80,formatter:function(value,row,index){
// 								if(row.isRecommend==0){
 					        		return '<a href="video/detail?id='+row.id+'" class="easyui-linkbutton" iconCls="icon-ok">查看</a>'+
						        	'&nbsp;|&nbsp;<a href="javascript:void(0)" onclick="recommend('+row.id+')" class="easyui-linkbutton" iconCls="icon-ok">推荐</a>'
						        	+'&nbsp;|&nbsp;<a href="javascript:void(0)" onclick="uploadpic('+row.id+')" class="easyui-linkbutton" iconCls="icon-ok">上传图片</a>';
// 								}else{
// 									return '<a href="video/detail?id='+row.id+'" class="easyui-linkbutton" iconCls="icon-ok">查看</a>';
// 								}
 					    }}
 				    ]],
 		    onLoadSuccess:function(){
//  		    	$(this).datagrid('enableDnd');
 		    }
    	});
     }
     
	$(function() {
		datagridList();
		initActivities();
	});
	
	//初始化活动下拉框
	function initActivities(){
		var data;
		var url = "video/initActivites";
		$.ajax({
            url: url,
            type: "post",
            dataType: "json",
            success: function (result){
                data = result.activities;
				$("#activities").empty();
				$("#activities").append("<option value='0'>请选择活动</option>");
				$.each(data, function(n, activity) {
				 	$("#activities").append("<option value='" + activity.id + "' >" + activity.title + "</option>");
				}); 
            }
        });
	}
	
	//活动选择事件
	function activityChange(){
		var activityId = $("#activities").val();
		if(activityId == 0){return false;}
		
		$("#searchtext").attr("name","filters['activity']");
		$("#searchtext").val(activityId);
		$(datagridId).datagrid('showColumn','sordNum');
		$(datagridId).datagrid('reload',getFormJson( searchFormId));
		
	}
	
	//保存排序数字
	function submitOrderNum(index){
		var url = "video/saveOrderNum";
		var orderNum = $("#id_" + index).val();
		if(isNaN(orderNum)){
		    showMessage("Error","请输入纯数字");
		}
		var id = $("#hidden_" + index).val();
		$.ajax({
            url: url,
            type: "post",
            dataType: "json",
            data:{id:id,orderNum:orderNum},
            success: function (result){
               if(result.num == 1){
              		$(datagridId).datagrid('reload',getFormJson( searchFormId));
               }
            }
        });
	}

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
		$(datagridId).datagrid('hideColumn','sordNum');
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
	
	function recommend(id){
			$.get('video/recommend',{id:id},function(result){
				if (result["success"]==true){
					$(datagridId).datagrid('reload'); // reload the user data
				} else {
					showMessage("Error",result["message"]);
				}
			});
	}
	
	function newUser(){
		$(adddialogueId).dialog('open').dialog('setTitle',addTitle);
		url = addUrl;
	}
	
	function uploadpic(id){
		newUser();
		$("#id").val(id);
		

		$('#hlsfiles').fileupload({
	    	url: 'video/uploadpic',
	        sequentialUploads: true,
	        dataType: 'json',
	        type:'post',
	        crossDomain:true,
	        done: function (e, data) {
	        	uploadresult=data.result;
	        	
	        	$("#videoPic").val(uploadresult["result"]);
	        	showMessage("通知","恭喜您上传成功");
	        },
	        progress:function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#p').progressbar('setValue', progress);
	        },
	        start:function (e) {
	        	$('#p').show();
	        	$('#p').progressbar('setValue', 0);
	        },
	        change:function(e,data){
	        	var fileName = data.files[0].name;
	        	var fileext = fileName.substring(fileName.lastIndexOf("."));
				fileext = fileext.toLowerCase();
				if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
					showMessage( "Error","对不起，系统仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
					return false;
				}
	        }
	    });
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
			<h2>全部视频</h2>
    		<hr>
			<table style="width:100%">
			<tr>
			<td align="left" >
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" onclick="javascript:destroyUser()" plain="true">删除</a> 
			    <input class="easyui-searchbox" name="name" id="searchtext" data-options="prompt:'请输入',menu:'#mm',searcher:doClickSearch" style="width:300px"></input>
				<div id="mm">
					<div data-options="name:'tag',iconCls:'icon-ok'">标签</div>
					<div data-options="name:'name'">标题</div>
				</div>
				<span>
					活动检索
				</span>
				<select id="activities" onchange="activityChange();">
				</select>
			</td>
			<td align="right" >
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="javascript:showAll()" plain="true">显示全部</a>
			</td>
			</table>
		</div>
		</form>
	</div>
	
	
	<!-- 弹出的上传图片对话框 -->
	<div id="dlg" class="easyui-dialog" style="width:500px;height:220px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
		<div class="ftitle">上传图片</div>
		<!-- 添加 -->
		<form id="fm" method="post" novalidate>
			<div class="fitem">
				<label>图片地址:</label>
				<input name="hlsfiles" id="hlsfiles" type="file" style="width:50%"></input>
				<input name="videoPic" id="videoPic"  type="hidden" style="width:50%"></input>
				<div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p" class="easyui-progressbar" ></div>
			</div>
			<input type="hidden" name="id" id="id"/>
		</form>
	</div>
	
	<!-- 添加对话框里的保存和取消按钮 -->
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser('#dlg','#fm')">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
</body>
</html>
