<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html  style="background: white">
 <head>
    <title>最新视频</title>
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
	 <script type='text/javascript' src='js/players/jwplayer.js'></script>
	 <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
	 
     <script type="text/javascript">
     var datagridId="#tt";
     var adddialogueId="#dlg";
     var editdialogueId="#updatedlg";
     var addFormId="#fm";
     var editFormId="#updatefm";
     var messageTitle="消息";
     var addTitle="播放视频";
     var editTitle="编辑视频";
     var deleteConfirmMessage="你确定要删除吗?";
     var noSelectedRowMessage="你没有选择行马甲用户";
     var noSelectedCheckStat="你没有选择审核状态";
     var searchFormId="#searchForm";
     var pageSize=20;
     
     var popFlg='${popFlg}';
     var listUrl="video/searchNewVideoListPage";
     if(popFlg&&popFlg=='1'){
    	 listUrl+="&filters['popFlg']=1";
    	 alert(listUrl);
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
 						{field:'ck',width : 100,checkbox:true},
 						{field:'img',title:'缩略图',width : 100,height:100,formatter:function(value,row,index){
 							var name="";
 							if(row.name){
 								name=row.name;
 							}
 							var desc = "";
 							if(row.description){
 								desc=row.description;
 								if(desc.length>20){
 									desc = row.description.substring(0,20)+"...";
 								}
 							}
 							var tag = "";
 							if(row.tag){
 								tag = row.tag;
 								tag = "#"+tag.replace(/ /g,"#,#")+"#";
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
 							var content = "<div style='width:100%; height:150px;'>";
 							content += "<div style='float:left; width:50%;'><div style='float:left;width:160px;height:150px;padding-right:10px;'> "
									+ "<div style='width:150px;height:150px;' id='playerContainer"+row.id+"' onclick='playVideo(\""+row.id+"\",\""+row.playKey+"\")'><img src='<%=request.getContextPath()%>/download"+row.videoPic+"' style='height:150px;width:150px;'/></div> "
									+ "</div><div style='width:160px;height:150px;padding-right: 10px;'> "
	    							+ "<div style='width:100%;height:40%;' title='"+ row.description +"'><h3 sytle='word-wrap:break-word;word-break:break-all;'>"+ desc + "</h3></div> "
	    							+ "<div style='width:100%;height:30%;color:#969696;'>"+ activity +"</div> "
	    							+ "<div style='width:100%;height:30%;color:#969696;'>"+ tag +"</div> "
	    							+ "</div></div><div style='float:right;width:50%; height:150px;'><div style='float:left;width:120px; height:150px;padding-right:10px'>"
		    						+ "<div style='width:100%;height:40%;align:center'>"
		    						+ "<span style='font-size:12px;color:#969696;'>"+ row.createDate +"</span></div> "
		    						+ "<div style='width:100%;height:30%;color:#969696;'>赞数："+ row.praiseCount +"</div> "
	    							+ "<div style='width:100%;height:30%;color:#969696;'>评论数："+ row.evaluationCount +"</div> "
		    						+ "</div><div style='float:left;width:100px; height:150px;padding-right:10px'>"
	    							+ "<div style='width:100%;color:#969696;'>"+ creatorName +"</div> "
	    							+ "</div><div style='float:right; width:180px; height:150px;'>"
		    						+ "<div style='width:50%;padding-right:5px;padding-top:60px;float: left;'><input type='button' onclick='showTags("+row.id+")' class='button green bigrounded' value='赞' style='width: 80px;height: 30px;'/></div> "
		    						+ "<div style='width:50%;padding-top:60px;'><input type='button' class='button red bigrounded' value='评论' onclick='showActities("+row.id+")' style='width: 80px;height: 30px;'/></div> "
		    						+ "</div></div></div> "
							return content;
 						}}
 						
 				    ]],
    		    onLoadSuccess:function(){
    		    	$(this).datagrid('enableDnd');
    		    }
    	});
     }
     
     function chooseActivity(videoId,obj){ 
    	 //alert(videoId);
    	 //alert(obj.value);
    	 $.ajax({
		      url:'activity/chooseActivity', 
		      data:{'videoId':videoId,'activityId':obj.value},
		      type:"post",
		      dataType:"json",
		      success:function (result){  
		    	//alert(result);
		      }
		  }); 
   	}
     
	$(function() {
		datagridList();
		doSearch();
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
	
	function playVideo(playerId,fileName){
		$(adddialogueId).dialog('open').dialog('setTitle',addTitle);
		$('#playerContainer').empty();
		var _player = null;
		
		var player = $('<div/>');
	    $(player).attr('id', 'pl'+playerId);
	    
	    $('#playerContainer').append(player);
	    var conf = {
	        file: '${video_play_url_prefix}'+fileName+'.m3u8',
	        image: '${img}',
	        height: 350,
	        width: 400,
	        autostart: true,
	        analytics: { enabled: false}
	    };
	    _player = jwplayer('pl'+playerId).setup(conf);
	}

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
			if(row[r]['flowStat']=='uncheck'){
				ids.push(row[r]['id']);
			}
		}
		
		if(ids.length>0){
			ids=ids.join(",");
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
	var praiseVideo,evaluationVideo;
	function showTags(vid){
		praiseVideo = vid;
		checkMJPraise();
		$("#mjDialog").show();
		$("#mjDialog").dialog({
			 title: '选择马甲',
			 width: 400,
			 height: 300,
			 closed: false,
			 cache: false,
			 //href: 'get_content.php',
			 modal: false
	    });
	}
	
	function checkMJPraise(){
		var ckbox = $("input[name='praiseuser']");
		$.each(ckbox,function(i){
			ckbox[i].checked = false;
		});
		$.post("praise/mjpraise",{videoId:praiseVideo},function(result){
			if (result.length>0){
				$.each(ckbox,function(i){
					$.each(result,function(j){
						if(ckbox[i].value == result[j].creatorId){
							ckbox[i].checked = true;
						}
					});
				});
			}
		});
	}
	
	function savePraise(){
		var majia=[];
		var ckbox = $("input[name='praiseuser']");
		for(var i=0;i<ckbox.length;i++){
			var obj = ckbox[i];
			if(obj.checked){
				majia.push(obj.value);
			}
		}
		if(majia.length>0){
			majia = majia.join(",");
			$.post("praise/createPraise",{videoId:praiseVideo,majia:majia},function(result){
				if (result["success"]==true){
					$(datagridId).datagrid('reload'); // reload the user data
					$("#mjDialog").dialog("close");
				} else {
					showMessage("Error",result["message"]);
				}
			});
		}else{
			showMessage(messageTitle,noSelectedRowMessage);
		}
	}
	
	function saveEvaluation(){
		var content = $("#newEvaluation").val();
		if(content && content.length>0){
			var majia = $("#mjselect").val();
			$.post("evaluation/saveEvaluation",{videoId:evaluationVideo,majia:majia,content:content},function(result){
				if (result["success"]==true){
					searchEvaluation();
					$("#newEvaluation").val("");
				} else {
					showMessage("Error",result["message"]);
				}
			});
		}
	}
	
	function searchEvaluation(){
		if(evaluationVideo){
			var container = $("#evaluations");
			container.empty();
			$.get("evaluation/searchListPage?filters['videoId']="+evaluationVideo,{page:1,rows:20},function(result){
				var evas = result.rows;
				if (evas.length>0){
					$.each(evas,function(i){
						var creator = $("<div></div>");
						creator.html(evas[i].creatorName + " 发表了评论    " + evas[i].createDate);
						creator.appendTo(container);
						
						var content = $("<div></div>")
						content.html(evas[i].content);
						content.appendTo(container);
						
						var hr = $("<hr/>");
						hr.appendTo(container);
					});
				}
			});
		}
	}
	
	function showActities(vid){
		evaluationVideo = vid;
		searchEvaluation();
		
		$("#evaluationDialog").show();
		$("#evaluationDialog").dialog({
			title:'显示评论',
			width: 400,
			height: 600,
			closed: false,
			cache: false,
			 //href: 'get_content.php',
			modal: false
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
	
	function deletePraise(obj){
		if(!obj.checked){
			var majia = obj.value;
			$.post("praise/delmjPraise",{videoId:praiseVideo,majia:majia},function(result){
				if (result["success"]==true){
					$(datagridId).datagrid('reload'); // reload the user data
					//$("#mjDialog").dialog("close");
				} else {
					showMessage("Error",result["message"]);
				}
			});
		}
	}
	
		
</script>
  </head>
<body>
	<!-- 列表 -->
     <table id="tt" data-options="border:false,toolbar:'#tb'">
	 </table>
    
    <!-- 列表上面的按钮和搜索条件  -->
     <div id="tb" style="padding:5px;height:auto">
     	<h2>最新视频</h2>
		<form action="" id="searchForm">
			<div>
				发布时间
				<input id="starttime" name="filters['starttime']"/>
				~
				<input id="endtime" name="filters['endtime']"/>
				&nbsp;&nbsp;
				用户/马甲名
				<input name="filters['creatorName']" value=""/>
				&nbsp;
				视频描述
				<input name="filters['videoName']"  value=""/>
				&nbsp;
				<a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
				   iconCls="icon-search">搜索</a>
			</div>
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
	
	<div id="praise-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="savePraise()">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#mjDialog').dialog('close')">关闭</a>
	</div>
	
	<div id="evaluation-buttons">
		<select id="mjselect">
			<c:forEach items="${mjuserlist}" var="ruser" varStatus="status">
				<option value="${ruser.id}">&nbsp;${ruser.name}</option>
			</c:forEach>
		</select>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveEvaluation()">发表</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#evaluationDialog').dialog('close')">关闭</a>
	</div>
	
	<div id="mjDialog"  style="display:none" buttons="#praise-buttons">
		<c:forEach items="${mjuserlist}" var="ruser" varStatus="status">
			<div style="float:left;width:100px"><input name="praiseuser" type="checkbox" value="${ruser.id}" onclick="deletePraise(this);">&nbsp;${ruser.name}</div>
		</c:forEach>
	</div>
	
	<div id="evaluationDialog"  style="display:none" buttons="#evaluation-buttons">
		<div id="evaluations" style="width:360px;height:430px;overflow:auto;padding-left:5px">
			
		</div>
		<hr>
		<div style="width:360px;padding-left:10px;position: static;">
			<textarea id="newEvaluation" rows="4" style="float:left;width:80%"></textarea>
		</div>
	</div>
	<div id="detail"  style="display:none">
	
	</div>
	
	<!-- 弹出的添加或者编辑对话框 -->
	<div id="dlg" class="easyui-dialog" style="width:415px;height:390px;" closed="true">
		<div id="playerContainer"></div>
	</div>
</body>
</html>
