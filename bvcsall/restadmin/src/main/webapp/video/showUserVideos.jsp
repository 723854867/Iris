<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html  style="background: white">
 <head>
    <title>用户视频</title>
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
		.datagrid-header {
		position: absolute; visibility: hidden;
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
     var noSelectedCheckStat="你没有选择审核状态";
     var searchFormId="#searchForm";
     var pageSize=50;
     
     var popFlg='${popFlg}';
     var listUrl="video/searchVideoListPage?filters['dataFrom']=myvideo_restwww";
     if(popFlg&&popFlg=='1'){
    	 listUrl+="&filters['popFlg']=1";
     }
     var updateUrl="video/update";
     var deleteUrl="video/deleteallpage";
     
     var url;
     
     function datagridList(){
    	 $(datagridId).datagrid({
    			fitColumns:true,
    			fit:true,
    			rownumbers : true,
    		    pagination : true,
    		    pageNumber : 1,
    		    pageList : [pageSize,pageSize*2,pageSize*3],
    		    pageSize : pageSize,
    		    pagePosition : 'bottom',
    		    selectOnCheck:true,
    		    url:listUrl,
    		    columns:[[
    						{field:'ck',wdith:50,checkbox:true},
    						{field:'img',title:'缩略图',wdith:1000,height:100,formatter:function(value,row,index){
    							var name="";
    							if(row.name){
    								name=row.name;
    							}
    							var tag = "";
    							if(row.tag){
    								tag = row.tag;
    							}
    							var creatorName = "";
    							if(row.creatorName){
    								creatorName = row.creatorName;
    							}
    							var activity = "";
    							if(row.activity){
    								activity = row.activity;
    							}
    							var stat = "";
    							if(row.flowStat=='published'){
    								stat = "已发布";
    							} else if(row.flowStat=='check_ok'){
    								stat = "审核通过";
    							} else if(row.flowStat=='uncheck'){
    								stat = "未审核";
    							} else if(row.flowStat=='check_fail'){
    								stat = "审核未通过";
    							} else if(row.flowStat=='delete'){
    								stat = "已删除";
    							}
    							var islogo = "";
    							if(row.isLogo=="1"){
    								islogo = "已贴标";
    							}else if(row.isLogo=="0"){
    								islogo = "未贴标";
    							}
    							
    							var content = "<div style='border:solid 1px #000; width:1000px; height:100px;'>";
    							content = content + "<div style='float:left; width:50%;'><div style='float:left;width:100px;height:100px;padding-right:10px;'> "
										+ "<div style='width:100px;height:100px;'><a href='video/detail?id="+row.id+"'> <img src='${uploadpic_url_prefix}"+row.playKey+".jpg' width='100' height='100'/></a></div> "
									+ "</div><div style='width:200px;height:100px;padding-right: 10px;'> "
	    								+ "<div style='width:100%;height:40%;'><h2 sytle='word-wrap:break-word;word-break:break-all;'>"+ name +"</h2></div> "
	    								+ "<div style='width:100%;height:20%;'>"+ creatorName +"</div> "
	    								+ "<div style='width:100%;height:20%;'>"+ tag +"</div> "
	    								+ "<div style='width:100%;height:20%;'>"+ activity +"</div> "
	    							+ "</div></div><div style='float:right;width:50%; height:100px;'><div style='float:left;width:200px; height:100px;padding-right:10px'>"
		    							+ "<div style='width:100%;height:40%;align:center'>"
		    								+ "<span style='font-size:12;'>"+ row.createDate +"</span> "
		    									+ "<span style='color:red;font-size:12;'>"+ stat +"</span> "
		    									+ "<span style='color:red;font-size:12;'>"+ islogo +"</span> "
	    								+ "</div><div style='width:100%;height:60%;'>"+ row.description +"</div> "
	    							+ "</div><div style='float:right; width:250px; height:100px;'>"
	    								+ "<div style='width:100%;height:50%;text-align:center;padding-top:5px;'>"
		    								+ "<div style='width:50%;'><input type='button' class='button red bigrounded' value='选择' onclick=chooseVideo("+row.id+",'"+row.name+"');></div> "
	    								+ "</div><div style='width:100%;height:50%;text-align:center;'>"
		    								+ "<div style='width:50%;padding-right:5px;float: left;'>"
		    								+ "</div><div style='width:50%;'><a href='"+ row.url +"'><input type='button' class='button blue bigrounded' value='下载'/></a></div> "
	    						+ "</div></div></div></div> "
   								return content;
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
	var x,y;
	$(function(){ 
	    $('#starttime').datetimebox({  
	    	showSeconds:false
		}); 
		$('#endtime').datetimebox({  
			showSeconds:false
		});
		
		$(document).mousemove(function(e){
			e = e || window.event;
		    x = e.pageX || (e.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft));
		    y = e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));
		});
	});

	var url;

	function destroyUser(id){
		if (id){
			$.messager.confirm('Confirm',deleteConfirmMessage,function(r){
				if (r){
					$.post('video/logicremove',{ids:id},function(result){
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
	
	
	function chooseVideo(id,name){
		if(window.opener.setValue){
			window.opener.setValue(id,name);
			window.close();
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
	
	function selectAll(){
		var ck = false;
		var sall = $("#sall");
		if(sall[0].checked){
			ck = true;
		}
		var ckboxes = $("input[name='ck']");
		for(var i=0;i<ckboxes.length;i++){
			if(ck){
				 $(datagridId).datagrid('selectRow',i);
			}else{
				 $(datagridId).datagrid('unselectRow',i);
			}
			ckboxes[i].checked = ck;
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
	
	function checkAll(){
		var row = $(datagridId).datagrid('getChecked');
		var ids=[];
		var inum=0;
		for(var r in row){
			ids.push(row[r]['id']);
		}
		ids=ids.join(",");
		if(row.length>0){
			mopen(ids);
		}else{
			showMessage("Error",noSelectedRowMessage);
		}
	}
	
	function checkVideo(){
		var rbox = $("input[name='f1']");
		var val = "";
		for(var i=0;i<rbox.length;i++){
			if(rbox[i].checked)
				val = rbox[i].value;
		}
		
		if(val ==""){
			showMessage("Error",noSelectedCheckStat);
			return;
		}else{
			if(mid ==""){
				
			}
			if(val == "check_ok"){
				$.post("video/checkTongGuoPage",{ids:mid},function(result){
					if (result["success"]==true){
						$(datagridId).datagrid('reload'); // reload the user data
					} else {
						showMessage("Error",result["message"]);
					}
				});
			}else if(val == "check_fail"){
				$.post("video/check_fail",{id:mid},function(result){
					if (result["success"]==true){
						$(datagridId).datagrid('reload'); // reload the user data
					} else {
						showMessage("Error",result["message"]);
					}
				});
			}
		}
		
	}
	
	function downloadAll(){
		
	}
	
	function doSearch(){
		$(datagridId).datagrid('reload',getFormJson( searchFormId));
	}
	
	var mid="";
	function mopen(id){
		var ddmenuitem = $("#m1");
		if (mid !=""){
			ddmenuitem.css('visibility','hidden');
		}
		mid = id;
		
		ddmenuitem.css('visibility','visible');
		ddmenuitem.css('left',x-310);
		ddmenuitem.css('top',y-50);
		ddmenuitem.css('filter','Alpha(Opacity=50)');
	}
	
	function mclose() {
		var ddmenuitem = $("#m1");
		if (ddmenuitem)
			ddmenuitem.css('visibility','hidden');
		ddmenuitem=0;
	}
	
	function showTags(){
		$("#tagDialog").show();
		$("#tagDialog").dialog({
			 title: '选择标签',
			 width: 400,
			 height: 300,
			 closed: false,
			 cache: true,
			 //href: 'get_content.php',
			 modal: true
	    });
	}
	var tags=[];
	function selectTag(obj){
		if(obj.checked){
			tags.push(obj.value);
		}else{
			var t = tags;
			tags = [];
			for(var i=0;i<t.length;i++){
				if(t[i]!=obj.value){
					tags.push(t[i]);
				}
			}
		}
		if(tags.length>0){
			$("#tag").val(tags.join(","));
		}else{
			$("#tag").val("");
		}
	}
	
	var activities=[];
	function selectActivity(obj){
		if(obj.checked){
			activities.push(obj.value);
		}else{
			var act = activities;
			activities = [];
			for(var i=0;i<act.length;i++){
				if(act[i]!=obj.value){
					activities.push(act[i]);
				}
			}
		}
		if(activities.length>0){
			$("#activities").val(activities.join(","));
		}else{
			$("#activities").val("");
		}
	}
	
	function showActities(){
		$("#activityDialog").show();
		$("#activityDialog").dialog({
			title:'选择活动',
			width: 400,
			height: 300,
			closed: false,
			cache: true,
			 //href: 'get_content.php',
			modal: true
	    	
	    });
	}
	
	function seeDetail(id){
		$("#detail").show();
		$("#detail").dialog({
			title:'选择活动',
			width: 800,
			height: 700,
			closed: false,
			cache: true,
			href: 'video/showdetail?id='+id,
			modal: true
	    	
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
			<div>
				发布时间
				<input id="starttime" name="filters['starttime']"/>
				~
				<input id="endtime" name="filters['endtime']"/>
				&nbsp;&nbsp;
				状态
				<select name="filters['flowStat']" >
		          <option value="">显示全部</option>
		          <option value="check_ok">审核通过</option>
		          <option value="uncheck">未审核</option>
		          <option value="check_fail">审核未通过</option>
		          <option value="delete">已删除</option>
		        </select>
				&nbsp;
				是否贴标
				&nbsp;
				<select name="filters['islogo']" id="isLogo">
					<option value="" >全部</option>
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
			</div>
			<div>
				标签
				<input name="filters['tag']" id="tag" readonly="readonly" onclick="showTags()"/>
				&nbsp;所属活动&nbsp;&nbsp;
				<input id="activities" name="filters['activity']" readonly="readonly" onclick="showActities()"/>
				&nbsp;&nbsp;
				发布者
				<input name="filters['creatorName']" value=""/>
				&nbsp;&nbsp;
				视频名称
				<input name="filters['videoName']"  value=""/>
				&nbsp;&nbsp;
				<input type="button" class="button blue bigrounded" onclick="doSearch()" style="width: 120px;height: 30px;" value="查询" />
			</div>
		        <hr/>
		</form>
	</div>
	
	<div id='m1' style='visibility: hidden;border:1px solid #ccc;background-color:white;position: absolute;width:150px;text-align: center' >
		<div style="padding-bottom: 10px"><input type='radio' name='f1' value='check_ok'>通过&nbsp;<input type='radio' name='f1' value='check_fail'>不通过</div>
		<div><input type='button' class='button blue bigrounded' value='确定' onclick='javascript:checkVideo();'/>&nbsp;<input type='button' class='button blue bigrounded' value='关闭' onclick='javascript:mclose();'/></div>
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
	
	<div id="tagDialog"  style="display:none">
		<ul style="display:table-row-group;">
			<c:forEach items="${tags}" var="tag" varStatus="status">
				<c:if test="${status.index%3==0 && status.index!=0}">
					</ul><ul style="display:table-row-group;">
				</c:if>
				<li sytle="text-align:left;display:table-cell;"><input type="checkbox" value="${tag.name}" onclick="selectTag(this)">&nbsp;${tag.name}</li>
			</c:forEach>
		</ul>
	</div>
	
	<div id="activityDialog"  style="display:none">
		<ul style="display:table-row-group;">
		<c:forEach items="${activites}" var="activity" varStatus="status">
			<c:if test="${status.index%3==0 && status.index!=0}">
				</ul><ul style="display:table-row-group;">
			</c:if>
			<li sytle="text-align:left;display:table-cell;"><input type="checkbox" value="${activity.title}" onclick="selectActivity(this)">&nbsp;${activity.title}</li>
		</c:forEach>
		</ul>
	</div>
	<div id="detail"  style="display:none">
	
	</div>
</body>
</html>
