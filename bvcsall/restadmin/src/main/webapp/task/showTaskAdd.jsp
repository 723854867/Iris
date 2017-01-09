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
<title>视频上传</title>
<script type="text/javascript">
var playId="";
$(function () {
	//启用表单验证

    $('#hlsfiles').fileupload({
    	url: 'task/taskIconUpload',
        sequentialUploads: true,
        dataType: 'json',
        type:'post',
        crossDomain:true,
        done: function (e, data) {
        	uploadresult=data.result;
        	
        	$("#icon").val(uploadresult["result"]);
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
    
    
});


function clickupload(){
	var id;
	$("#playId").val(playId);
	var formjson=getFormJson( '#myform');
	if($.trim(playId)=="error"){
		showMessage("Error","文件上传失败，请重新选择文件上传");
		return;
	}
	if($.trim(playId)==""){
		showMessage("提示","请等待文件上传完成后，再点击确认");
		return;
	}
	$.ajax({
	      url: 'video/uploadsave',
	      data: formjson,
	      type: "post",
	      dataType: "json",
	      beforeSend: function(){
	       return $( '#myform' ).form('enableValidation').form( 'validate' );
	      },
	      success: function (result){
	        location.href="video/checledVideos";
	      }
	  });
}

function start(){
	var value = $('#p').progressbar('getValue');
	if (value < 100){
		value += Math.floor(Math.random() * 10);
		$('#p').progressbar('setValue', value);
		setTimeout(arguments.callee, 200);
	}
};


	function chooseOldVideo(obj,taskId,videoId){
		
		var url="<c:url value='/task/showRemoveActivityList?taskId='/>"+taskId+"&videoId="+videoId;
		
		var rValue=openWindow(url,'','','');
		
		
	}	
	
	function createNewVideo(obj){
		
		var url="<c:url value='/video/popUpload?popFlg=1'/>";
		
		var rValue=openWindow(url,'','950','650');
		
		
	}
	
	function chooseOldVideo(obj){
		
		var url="<c:url value='/video/showUserVideos?popFlg=1'/>";
		
		var rValue=openWindow(url,'','1100','');
		
		
	}
	
	function openWindow(url,name,width,height,feature){
		
		var iWidth=800; //弹出窗口的宽度;
		var iHeight=600; //弹出窗口的高度;
		if(width&&width!=''){
			iWidth=width;
		}
		if(height&&height!=''){
			iHeight=height;
		}
		var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
		var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
		//alert(iTop);
		//alert(iLeft);
		if(!feature||feature==null||feature==''){
			feature="height="+iHeight+",width="+iWidth+", top="+iTop+", left="+iLeft+",alwaysRaised=yes,resizable=no,z-look=yes";
		}else{
			feature="height="+iHeight+",width="+iWidth+", top="+iTop+", left="+iLeft+","+feature;
		}
		var rValue=window.open(url,name,feature);
		//alert('window');
		//alert(rValue);
		if(typeof (rValue) == "undefined") {
			rValue = window.ReturnValue;
		}  
		
		return rValue;
	}

	function getValue(obj,key) {
		if(window.showModalDialog){
			window.returnValue=obj;
			if (window.opener && window.opener != null){
				window.opener.ReturnValue = obj;
			}
		}else{
			window.opener.ReturnValue = obj;
			window.opener.setValue(obj,key);
		}
		
	    window.close();
	}

	function setValue(id,name) {
// 		$('#videoId').val(id);
// 		$('#videoName').val(name);
		
		document.getElementById("videoId").value=id;
		document.getElementById("videoName").value=name;
	}
	
	function flushPage() {
		window.location.reload(); 
	}
	
	function pageSub() {
		
		
		var description=$('#description').val();
		if(!description){
			alert('请输入描述!');
			return
		}
		
		var title=$('#title').val();
		if(!title){
			alert('请输入标题!');
			return
		}
		
		var icon=$('#icon').val();
		if(!icon){
			alert('请上传icon!');
			return
		}
		
		var num=$('#num').val();
		if(num!=undefined&&!num){
			alert('请输入数量!');
			return
		}
		
		var typeTwo='${task.typeTwo }';
		
		var taskValue=$('#taskValue').val();
		
// 		if(typeTwo==31){
// 			if(taskValue!=undefined&&!taskValue){
// 				alert('请输入视频id!');
// 				return
// 			}
// 		}else if(typeTwo==32){
// 			if(taskValue!=undefined&&!taskValue){
// 				alert('请输入视频id!');
// 				return
// 			}
// 		}else if(typeTwo==33){
// 			if(taskValue!=undefined&&!taskValue){
// 				alert('请输入视频id!');
// 				return
// 			}
// 		}else
			
		if(typeTwo==1001){
			var deadLine=$("input[name='deadLineStr']").val() 
// 			alert(deadLine);
// 			alert(deadLine!=undefined);
// 			alert(!deadLine);
			if(deadLine!=undefined&&!deadLine){
				alert('请输入过期时间!');
				return
			}
		}
		
		var weight=$('#weight').val();
		if(!weight){
			alert('请输入权重!');
			return
		}
		
		
		var integral=$('#integral').val();
		if(integral!=undefined&&!integral){
			alert('请输入奖励!');
			return
		}
		
		
		
		
		$('#myform').submit();
	}
	
	
	var lisVideotUrl="video/searchVideoListPage?filters['flowStat']=checked";
	var datagridId="#tt";
	var searchFormId="#searchForm";

	var datagridId2="#tt2";
	var searchFormId2="#searchForm2";

	var pageSize=20;
	var playId="";
	var adddialogueId="#dlg";
		$(function () {
			//启用表单验证
			$('.validatebox-text').bind('blur', function(){
				$(this).validatebox('enableValidation').validatebox('validate');
			});
			datagridList();
			
			datagridList2();
			
			$('#files').fileupload({
		    	url: 'sysmess/uploadfile',
		        sequentialUploads: true,
		        dataType: 'json',
		        type:'post',
		        crossDomain:true,
		        done: function (e, data) {
		        	if (data.result[ "success"]== true){
		        		$("#dest_user").val(data.result["result"]);
						var ids = data.result["result"].split(";");
						$("#user_count").text(ids.length-1+"位");
		        	} else {
		        		alert("上传失败，请重新上传。。。。。");
		        		return;
		        	}
		        	showMessage("通知","恭喜您上传成功");
		        	$("#dlg").dialog("close");
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
					if ((fileext != '.txt')) {
						showMessage( "Error","对不起，系统仅支持txt格式的文本文件，请重新选择！");
						return false;
					}
		        }
		    });

			$('#msgImage').fileupload({
				url: 'sysmess/uploadImage',
				sequentialUploads: true,
				dataType: 'json',
				type:'post',
				crossDomain:true,
				done: function (e, data) {
					if (data.result[ "success"]== true){
						$("#imagePath").val(data.result["result"]);
						$("#msgImageName").attr('type','text');
						$("#msgImageName").val(data.files[0].name);
					} else {
						alert("上传失败，请重新上传。。。。。");
						return;
					}
					showMessage("通知","恭喜您上传成功");
				},
				change: function(e, data){
					console.log(data);
					var fileName = data.files[0].name;
					var fileext = fileName.substring(fileName.lastIndexOf("."));
					fileext = fileext.toLowerCase();
					if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
						showMessage( "Error","对不起，封面图片仅支持标准格式的照片，请不要调皮!O(∩_∩)O谢谢~");
						return false;
					}
				}
			});
		});
		
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
				    singleSelect: true,
				    url:lisVideotUrl,
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
								var content = "<div style='border:solid 1px #000; width:100%; height:150px;'>";
								content += "<div style='float:left; width:65%;'>"
											+"<div style='float:left;width:220px;height:150px;padding-right:10px;'> "
												+ "<div style='width:220px;height:150px;' id='playerContainer"+row.id+"' onclick='playVideo(\""+row.id+"\",\""+row.playKey+"\")'><img src='<%=request.getContextPath()%>/download"+row.videoPic+"' style='height:150px;width:200px;'/></div> "
											+"</div><div style='width:220px;height:150px;padding-right: 10px;'> "
		   										+ "<div style='width:100%;height:40%;' title='"+row.description+"'><h3 sytle='word-wrap:break-word;word-break:break-all;'>"+ desc + "</h3></div> "
		   										+ "<div style='width:100%;height:30%;color:#969696;'>"+ activity +"</div> "
		   										+ "<div style='width:100%;height:30%;color:#969696;'>"+ tag +"</div> "
		   								+ "</div></div>"
		   								+ "<div style='float:right;width:35%; height:150px;'>"
		   									+ "<div style='float:left;width:200px; height:150px;padding-right:10px'>"
			    								+ "<div style='width:100%;height:40%;align:center'>"
			    									+ "<span style='color:red;font-size:14px;'>"+ stat +"</span> "
			    									+ "<span style='font-size:12px;color:#969696;'>"+ row.createDate +"上传</span> "
		   										+ "</div><div style='width:100%;height:20%;color:#969696;'>"+ creatorName +"</div> "
		   							+ "</div></div></div> ";
								return content;
							}}
							
					    ]],
				    onLoadSuccess:function(){
				    	$(this).datagrid('enableDnd');
				    }
			});
		}
		
		function datagridList2(){
			 $(datagridId2).datagrid({
					fitColumns:true,
					fit:true,
					rownumbers : true,
				    pagination : true,
				    pageNumber : 1,
				    pageList : [pageSize,pageSize*2,pageSize*3],
				    pageSize : pageSize,
				    pagePosition : 'bottom',
				    selectOnCheck:true,
				    url:'ruser/getNormalUsers',
				    columns:[[
							{field:'ck',width : 100,checkbox:true},
							{field:'name',title:'昵称',width : 100},
							{field:'username',title:'登录名',width : 100},
							{field:'phone',title:'手机号',width : 100}
							
					    ]],
				    onLoadSuccess:function(){
				    	$(this).datagrid('enableDnd');
				    }
			});
		}

		function doSearch(){
			$(datagridId).datagrid('reload',getFormJson( searchFormId));
		}
		
		function doSearch2(){
			$(datagridId2).datagrid('reload',getFormJson( searchFormId2));
		}
		
		function selectUser(){
			var row = $(datagridId2).datagrid('getChecked');
			var ids=[];
			var inum=0;
			for(var r in row){
				ids.push(row[r]['id']);			
			}
			if(ids.length>0){
				$("#user_count").text(ids.length+"位");
				ids=ids.join(";");
				$("#dest_user").val(ids);
				$("#user_dlg").dialog("close");
			}else{
				showMessage("Error","请选择用户");
			}
		}
		
		function clickupload(){
			var item = $("input[name='isplan']:checked").val();
			if(item == 1){
				var pubTime = $('#pubTime').textbox('getValue');
				if(pubTime == null || pubTime == ""){
					showMessage("提示","请您选择计划发布的时间");
					return false;
				}else{
					var sDate = new Date(pubTime.replace("//-/g", "//"));
			    	var eDate = new Date();
			    	if(sDate < eDate){
						showMessage("Error","计划发布开始时间不能小于当前时间");
						return false;
			    	}
				}
			}
			var opt = $("#operation option:selected").val();
			if(opt != 'app'){
				var target = $("#targetid").val();
				if(target=='' || target=='undefind'){
					showMessage("Error","指定操作ID不能为空");
					$("#targetid").focus();
					return false;
				}
			}
			var formjson=getFormJson('#myform');
			$.ajax({
			      url: 'sysmess/add',
			      data: formjson,
			      type: "post",
			      dataType: "json",
			      beforeSend: function(){
			       return $( '#myform' ).form('enableValidation').form( 'validate' );
			      },
			      success: function (result){
			      	location.href="sysmess/listmess";
			      }
			  });
		}

		function pubClick(){
			var num = $("input[name='isplan']:checked").val();;
			if(num == 0){//立即发布
				$("#actives").attr("style","display:none");
			}else{//计划发布
				$("#actives").attr("style","display:block");
				$("#pubTime").datetimebox({
					showSeconds : false
				});
			}
		}
	 
		function changeRadio(name){
			if (name == 'input') {
				$("#dest_user").removeAttr("readonly");
				$("#user_dlg").show();
				$("#user_dlg").dialog({
					 title: '选择文件',
					 width: 800,
					 height: 650,
					 closed: false,
					 cache: true,
					 modal: true
			    });
			} else {
				$("#dest_user").attr("readonly","readonly");
				if (name == 'all') {
					$("#dest_user").val("all");
					$("#user_count").text("所有 ");
				}else if (name == 'file') {
					$("#dlg").show();
					$("#dlg").dialog({
						 title: '选择文件',
						 width: 400,
						 height: 200,
						 closed: false,
						 cache: true,
						 modal: true
				    });
				}
			}
		}

		function chgOpration(){
			var opt = $("#operation option:selected").val();
			if(opt != 'app'){
				$("#target").attr("style","display:block");
			}else{
				$("#target").attr("style","display:none");
			}
			$("#targetid").val("");
		}
	
	
	function showTargetDialog(){
		var typeTwo='${task.typeTwo }';
		
		var taskValue=$('#taskValue').val();
		
		if(typeTwo==31||typeTwo==32||typeTwo==33){
			$("#video_dlg").show();
			$("#video_dlg").dialog({
				 title: '选择视频',
				 width: 800,
				 height: 650,
				 closed: false,
				 cache: true,
				 modal: true
		    });
		}else if(typeTwo==41||typeTwo==1001){
			$("#act_dlg").show();
			$("#act_dlg").dialog({
				 title: '选择活动',
				 width: 400,
				 height: 200,
				 closed: false,
				 cache: true,
				 modal: true
		    });
		}
	}
	
	function selectActivity(){
		var aid = $("#activities option:selected").val();
		$("#taskValue").val(aid);
		$("#act_dlg").dialog("close");
	}
	
	function selectVideo(){
		var row = $(datagridId).datagrid('getChecked');
		var vid = row[0].id;
		$("#taskValue").val(vid);
		$("#video_dlg").dialog("close");
	}
	
	
	
</script>
<script src="js/fp/vendor/jquery.ui.widget.js"></script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="myform" method="post" action="task/taskAdd">
				<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;"  class="form-body" >
				<c:choose>
					<c:when test="${task.typeTwo eq 1 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
	                
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
					
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:发布一个视频,赠送N积分
						</td>
					</tr>
					
					<c:if test="${false }">
					<tr>
						<td>视频id:</td>
						<td>
							<input class="" name="taskValue" id="taskValue"  value="${task.taskValue }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
							<br>
							可填写一个视频id，不填代表适用全部视频
						</td>
					</tr>
					</c:if>
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="1"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="1" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="1" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					</c:when>
					
					<c:when test="${task.typeTwo eq 41 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
					
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:参与1个活动2次,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>活动id:</td>
						<td>
							<input class="" name="taskValue" id="taskValue"  value="${task.taskValue }" class="easyui-numberbox" onfocus="showTargetDialog();"  min="1" max="" precision="0"  size="10" ></input>
							<br>
							填写一个活动id
						</td>
					</tr>
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="${task.num }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="1"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="41" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="41" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					</c:when>
					
					<c:when test="${task.typeTwo eq 21 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:关注黄V,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" class="easyui-numberbox" readonly="readonly"  min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="1"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="21" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="21" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					</c:when>
					
					<c:when test="${task.typeTwo eq 22 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:关注蓝V,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" class="easyui-numberbox" readonly="readonly"  min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="1"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="22" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="22" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					</c:when>
					
					<c:when test="${task.typeTwo eq 23 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:关注绿V,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" class="easyui-numberbox" readonly="readonly"  min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="1"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="23" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="23" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					</c:when>
					
					<c:when test="${task.typeTwo eq 31 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:评论一次视频,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>视频id:</td>
						<td>
							<input class="" name="taskValue" id="taskValue"  value="${task.taskValue }" class="easyui-numberbox"  onfocus="showTargetDialog();"   min="1" max="" precision="0"  size="10" ></input>
							<br>
							填写一个视频id
						</td>
					</tr>
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" class="easyui-numberbox" readonly="readonly"  min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="1"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="31" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="31" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					</c:when>
					
					<c:when test="${task.typeTwo eq 32 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:赞一次视频,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>视频id:</td>
						<td>
							<input class="" name="taskValue" id="taskValue"  value="${task.taskValue }" class="easyui-numberbox"  onfocus="showTargetDialog();"  min="1" max="" precision="0"  size="10" ></input>
							<br>
							填写一个视频id
						</td>
					</tr>
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" class="easyui-numberbox" readonly="readonly"  min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="1"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="32" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="32" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					</c:when>
					
					<c:when test="${task.typeTwo eq 33 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:转发一次视频,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>视频id:</td>
						<td>
							<input class="" name="taskValue" id="taskValue"  value="${task.taskValue }" class="easyui-numberbox"  onfocus="showTargetDialog();"  min="1" max="" precision="0"  size="10" ></input>
							<br>
							填写一个视频id
						</td>
					</tr>
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" class="easyui-numberbox" readonly="readonly"  min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="1"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="33" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="33" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					</c:when>
					
					<c:when test="${task.typeTwo eq 1001 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:参与a-lin活动,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>活动id:</td>
						<td>
							<input class="" name="taskValue" id="taskValue"  value="${task.taskValue }" class="easyui-numberbox" onfocus="showTargetDialog();"  min="1" max="" precision="0"  size="10" ></input>
							<br>
							填写一个活动id
						</td>
					</tr>
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" readonly="readonly" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="2"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="1001" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="1001" ></input>
						</td>
					</tr>
					<tr>
						<td>过期时间:</td>
						<td>
							<input  name="deadLineStr" id="deadLineStr"  value="${task.deadLine }"  class="easyui-datetimebox"  data-options="required:true" value="${task.deadLine }" style="width:150px"></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					</c:when>
					
					<c:when test="${task.typeTwo eq 2001 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:完善个人资料,赠送N积分
						</td>
					</tr>
					
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" readonly="readonly" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="3"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="2001" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="2001" ></input>
						</td>
					</tr>
					
					</c:when>
					
					<c:when test="${task.typeTwo eq 2021 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:粉丝达到50,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>粉丝数:</td>
						<td>
							<input  name="taskValue" id="taskValue"  value="${task.taskValue }"  class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
							
						</td>
					</tr>
					
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" readonly="readonly" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="3"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="2021" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="2021" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					
					</c:when>
					
					<c:when test="${task.typeTwo eq 2022 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:粉丝达到100,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>粉丝数:</td>
						<td>
							<input  name="taskValue" id="taskValue"  value="100" readonly="readonly" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
							
						</td>
					</tr>
					
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" readonly="readonly" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="3"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="2022" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="2022" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					
					</c:when>
					
					<c:when test="${task.typeTwo eq 2023 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:粉丝达到200,赠送N积分
						</td>
					</tr>
					
					<tr>
						<td>粉丝数:</td>
						<td>
							<input  name="taskValue" id="taskValue"  value="200" readonly="readonly" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
							
						</td>
					</tr>
					
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" readonly="readonly" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>奖励(积分/钻石):</td>
						<td>
							<input class="" name="integral" id="integral" type="text"  value="${task.integral }" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="3"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="2023" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="2023" ></input>
						</td>
					</tr>
					<tr>
						<td>前置任务:</td>
						<td>
							<input class="" name="previousTaskId" id="previousTaskId"  value="${task.previousTaskId }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
						</td>
					</tr>
					
					</c:when>
					
					<c:when test="${task.typeTwo eq 51 }">
					<tr>
						<td>任务标题:</td>
						<td>
							<textarea name="title" id="title"  class="" cols="60" rows="5"   >${task.title }</textarea>
						</td>
					</tr>
					
					<tr>
	                    <td>icon:</td>
	                    <td>
	                        <c:if test="${not empty task.icon }">
	                            <img style="padding-top:5px;padding-left:10px;" src="/restadmin/download${task.icon}"/><br>
	                        </c:if>
	                        <input name="hlsfiles" id="hlsfiles" type="file"/>
	                        <input name="icon" id="icon" type="hidden" value="${task.icon }"/>
	
	                        <div style="margin-left:auto;margin-right:auto;width:400px;display:none" id="p"
	                             class="easyui-progressbar"></div>
	                    </td>
	                </tr>
	                
					<tr>
						<td>任务描述:</td>
						<td>
							<textarea name="description" id="description"  class="" cols="60" rows="5"   >${task.description }</textarea>
							<br>
							例:签到任务,赠送N积分
						</td>
					</tr>
					
					
					<tr>
						<td>数量:</td>
						<td>
							<input class="" name="num" id="num" type="text"  value="1" readonly="readonly" class="easyui-numberbox"   min="1" max="" precision="0"  size="10" ></input>
						</td>
					</tr>
					
					
					<tr>
						<td>权重:</td>
						<td>
							<input class="" name="weight" id="weight"  value="${(not empty task.weight)?task.weight:1 }"  size="10"  class="easyui-numberbox"  min="1" max="" precision="0"   ></input>
							
							<input class="" name="id" id="id" type="hidden"  value="${task.id }"  ></input>
							<input class="" name="typeOne" id="typeOne" type="hidden"  value="4"  ></input>
							<input class="" name="typeTwo" id="typeTwo" type="hidden"  value="51" ></input>
							<input class="" name="jumpType" id="jumpType" type="hidden"  value="task" ></input>
							<input class="" name="jumpTargetId" id="jumpTargetId" type="hidden"  value="51" ></input>
						</td>
					</tr>
					
					</c:when>
				
				</c:choose>
				
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

<!-- 弹出的选择活动对话框 -->
<div id="act_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true" buttons="#adlg-buttons">
	活动&nbsp;&nbsp;
		<select id="activities" name="activities">
			<c:forEach items="${activites }" var="activity" >
				<option value="${activity.id}">${activity.title}</option>
			</c:forEach>
		</select>
</div>
<!-- 弹出的选择视频对话框 -->
<div id="video_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true" buttons="#vdlg-buttons">
	 <table id="tt" data-options="border:false,toolbar:'#tb'">
	 </table>
    <!-- 列表上面的按钮和搜索条件  -->
     <div id="tb" style="padding:5px;height:auto">
		<form action="" id="searchForm">
			<div>
				标签&nbsp;
				<select name="filters['tag']">
					<option value="">选择标签</option>
					<c:forEach items="${tags }" var="tag" >
						<option value="${tag.name}">${tag.name}</option>
					</c:forEach>
				</select>
				&nbsp;所属活动&nbsp;
				<select name="filters['activity']">
					<option value="">选择活动</option>
					<c:forEach items="${activites }" var="activity" >
						<option value="${activity.id}" >${activity.title}</option>
					</c:forEach>
				</select>
				&nbsp;
				发布者
				<input name="filters['creatorName']" value=""/>
				&nbsp;
				视频描述
				<input name="filters['videoName']"  value=""/>
				&nbsp;
				<input type="button" class="button blue bigrounded" onclick="doSearch()" style="width: 80px;height: 30px;" value="查询" />
			</div>
		        <hr/>
		</form>
	</div>
	
</div>

<!-- 弹出的选择用户对话框 -->
<div id="user_dlg" class="easyui-dialog" style="width:390px;height:450px;padding:10px 20px" closed="true" buttons="#udlg-buttons">
	 <table id="tt2" data-options="border:false,toolbar:'#tb2'">
	 </table>
    <!-- 列表上面的按钮和搜索条件  -->
     <div id="tb2" style="padding:5px;height:auto">
		<form action="" id="searchForm2">
			<div>
				昵称&nbsp;<input name="name" value="" size="15"/>
				
				&nbsp;登录名&nbsp;<input name="username" value="" size="15"/>
				
				&nbsp;手机号&nbsp;<input name="phone" value="" size="15"/>
				&nbsp;
				<input type="button" class="button blue bigrounded" onclick="doSearch2()" style="width: 80px;height: 30px;" value="查询" />
			</div>
		        <hr/>
		</form>
	</div>
	
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="fileUpload();">保存</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="adlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectActivity();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#act_dlg').dialog('close')">取消</a>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="vdlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectVideo();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#video_dlg').dialog('close')">取消</a>
</div>

<!-- 添加对话框里的保存和取消按钮 -->
<div id="udlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectUser();">确定</a>
	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#user_dlg').dialog('close')">取消</a>
</div>
</body>
</html>