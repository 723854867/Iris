<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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

<script type='text/javascript' src='js/players/bootstrap.min.js'></script>
<script type='text/javascript' src='js/players/jwplayer.js'></script>
<script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>
<title>视频编辑</title>
<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}
</style>
<script type="text/javascript">
var lisVideotUrl="video/searchVideoListPage?filters['flowStat']=checked";
var datagridId="#tt";
var searchFormId="#searchForm";
var pageSize =20;

var _player = null;
var _url = null;
$(document).ready(function () {	
    var player = $('<div/>');
    $(player).attr('id', 'player_id');
    $('#div_container').append(player);
    var conf = {
        file: '${video_play_url_prefix}${video.playKey}.m3u8',
        image: '${video.img}',
        height: 230,
        width: 450,
        autostart: true,
        analytics: { enabled: false}
    };
    _player = jwplayer('player_id').setup(conf);    
});

function clickupload(){
	var id;
	$("#playId").val(playId);
	var formjson=getFormJson( '#modify_form');
	if($.trim(playId)==""){
		showMessage("Error","文件上传失败，请重新上传文件后，再点击确认上传");
		return;
	}
	$.ajax({
	      url: 'video/uploadsave',
	      data: formjson,
	      type: "post",
	      dataType: "json",
	      beforeSend: function(){
	       return $( '#modify_form' ).form('enableValidation').form( 'validate' );
	      },
	      success: function (result){
	        location.href="user/userlist";
	      }
	  });
}


function submit(){
	$.post("video/modify",getFormJson("#modify_form"),function(result){
		if (result == "SUCCESS"){
			location.href="showvideo/listShows";
		} else {
			showMessage("Error",result);
		}
	});
}
$(function () {
	//启用表单验证
	$('.validatebox-text').bind('blur', function(){
		$(this).validatebox('enableValidation').validatebox('validate');
	});	
	$("#dbtag").toggle();

	datagridList();
	//----------------
	    $('#showImg').fileupload({
	    	url: 'video/uploadShowImg',
	        sequentialUploads: true,
	        dataType: 'json',
	        type:'post',
	        crossDomain:true,
	        done: function (e, data) {
	        	uploadresult=data.result;
	        	var path = uploadresult["result"];
	        	$("#showPic").val(path);//原图
	        	var viewImg = $("#viewShowImg");
	        	viewImg.attr("src","<%=request.getContextPath()%>/download" + path);
	        },
	        progress:function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#q').progressbar('setValue', progress);
	        },
	        start:function (e) {
	        	$('#q').show();
	        	$('#q').progressbar('setValue', 0);
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

function togglecallback(){
	at=$("#updatebutton").text();
	if($.trim(at)=="修改"){
		$("#updatebutton").text("收缩");
	}else if($.trim(at)=="收缩"){
		$("#updatebutton").text("修改");
	}
}

function clickbutton(tagid){
	$("#"+tagid).remove();
}

var tempradio= null; 
function changeRadio(checkedRadio){
	if (tempradio == checkedRadio) {
		tempradio.checked = false;
		tempradio = null;
	} else {
		tempradio = checkedRadio;
	}
}

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
		    idField:'id',
		    selectOnCheck:true,
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
		    onLoadSuccess:function(row){
		    	var rowData = row.rows;
		    	var refvids = $("#refVideos").val();
		    	var vids = refvids.split(",");
                $.each(rowData,function(idx,val){//遍历JSON
                      if(vids.indexOf(val.id.toString())!=-1){
                        $(datagridId).datagrid("selectRow", idx);//如果数据行为已选中则选中改行
                      }
                });       
		    },
		    onSelect:function(idx,rowData){
		    	var row = $(datagridId).datagrid('getChecked');
		    	var ids=[];
		    	for(var r in row){
		    		ids.push(row[r]['id']);			
		    	}
		    	if(ids.lenght==0 || ids.indexOf(rowData["id"]) == -1){
		    		ids.push(rowData["id"]);
		    	}
		    	
		    	$("#refVideos").val(ids.join(","));
		    },
		    onUnselect:function(idx,rowData){
		    	var row = $(datagridId).datagrid('getChecked');
		    	var ids=[];
		    	for(var r in row){
		    		ids.push(row[r]['id']);			
		    	}
		    	var i = ids.indexOf(rowData["id"]);
		    	if(i != -1){
		    		ids.splice(i,1);
		    	}
		    	$("#refVideos").val(ids.join(","));
		    },
		    onSelectAll:function(rows){
		    	var row = $(datagridId).datagrid('getChecked');
		    	var ids=[];
		    	for(var r in row){
		    		ids.push(row[r]['id']);			
		    	}
		    	for(var i in rows){
			    	if(ids.lenght==0 || ids.indexOf(rows[i]["id"]) == -1){
			    		ids.push(rows[i]["id"]);
			    	}
		    	}
		    	
		    	$("#refVideos").val(ids.join(","));
		    }
	});
}

function doSearch(){
	$(datagridId).datagrid('reload',getFormJson( searchFormId));
}

function selectVideo(){
	var row = $(datagridId).datagrid('getChecked');
	var ids=[];
	for(var r in row){
		ids.push(row[r]['id']);			
	}
	$("#refVideos").val(ids.join(","));
	$("#video_dlg").dialog("close");
}

function videoDlg(){
	$("#video_dlg").show();
	$("#video_dlg").dialog({
		 title: '选择视频',
		 width: 800,
		 height: 650,
		 closed: false,
		 cache: true,
		 modal: true
   });
}
</script>
</head>
<body>
<div class="easyui-layout1" data-options="fit:true1">
	<div data-options="region:'center',border:false" style="padding:5px;margin:0 auto;">
		<form id="modify_form" method="post">
				<input type="hidden" name="id" id="id" value="${video.id}"/>
				<input type="hidden" name="uploader" id="uploader" value="${video.creatorId }"/>
				<table cellpadding="5" style="margin:0 auto;width:900px;text-align: left;" class="table-doc" >
				<tr>
					<td colspan="2" align="center">
					<div id='player_id'>载入中</div>
					</td>
				</tr>

				<tr>
					<td>视频描述:</td>
					<td>
					 <input class="validatebox-text easyui-textbox" name="description" id="description" value="${video.description }" data-options="multiline:true,validType:'length[0,20]'" required="true" style="width:50%;height:100px" ></input>
					</td>
				</tr>
				<tr>
					<td>上传马甲:</td>
					<td>
					    <select class="easyui-combobox" name="ruserId" id="ruserId"  style="width:50%">
						     <c:forEach var="item" items="${uploaduserlist }">
							 	<option value="${item['id'] }" <c:if test="${item['id'] eq video.creatorId}">selected</c:if>>${item['username']}</option>
							 </c:forEach>
						</select>
					</td>
				</tr>

				<tr>
					<td>是否贴标:</td>
					<td>
					 	<select class="easyui-combobox" name="isLogo" id="isLogo"  style="width:50%">
						 	<option value="0" <c:if test="${'0' eq video.isLogo}">selected</c:if>>否</option>
						 	<option value="1" <c:if test="${'1' eq video.isLogo}">selected</c:if>>是</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>播放率:</td>
					<td>
						<input id="playRateToday" name="playRateToday" class="easyui-numberbox" value="${video.playRateToday}" min="0" max="0.9999" precision="4"   maxlength="5" size="5" />
					</td>
				</tr>
				<tr>
					<td>上传时间:</td>
					<td>
						${video.createDate }
					</td>
				</tr>
				<tr>
					<td>选择活动:</td>
					<td style="">
					 <c:forEach var="item" items="${activityList }" varStatus="idxStatus">
							<input type="checkbox" name="activity" value="${item['id'] }"  
							
							 <c:forEach var="subitem" items="${svList }">
							<c:if test="${item['id']==subitem}">
							checked
							</c:if>
							</c:forEach>
							
							/>
							${item['title'] }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<c:if test="${ (idxStatus.index+1) % 7 == 0}">
								<br/>
							</c:if>
					 </c:forEach>
					</td>
				</tr>
				<tr>
					<td>是否Blive秀:</td>
					<td>
						加入Blive秀 <input id="isShow" type="checkbox" onclick="picShow();" checked="checked" disabled="disabled"/>
						<input type="hidden" name="showId" value="${showVideo.id}">
						<div id="showProp" style="">
							<div style="padding-bottom: 5px;padding-top:5px">
							 	标题：<input type="text" id="showTitle" style="width:200px;" name="showTitle" value="${showVideo.title}"/>
						 	</div>
						 	<div style="padding-bottom: 5px">
						 		描述：<input type="text" id="showDesc" style="width:200px;" name="showDesc" value="${showVideo.description}"/>
						 	</div>
						 	<div style="padding-bottom: 5px">
						 		关联视频：<input type="text" id="refVideos" style="width:200px;" name="refVideos" readonly="readonly" onclick="videoDlg()" value="${showVideo.refVideoId}"/>
						 	</div>
						 	<div>
						 		宣传图：<input type="file" id="showImg" name="showImg"/>
								<img alt="" height="70px;" style="position: relative;top: 20px;" src="<%=request.getContextPath()%>/download${showVideo.pic}" id="viewShowImg">
						 		<input type="hidden" id="showPic" name="showPic" value="${showVideo.pic}">
						 	</div>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
					<a href="javascript:void(0)" style="" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="submit()">保存</a>
					<a href="showvideo/listShows" style="" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>

<!-- 弹出的选择视频对话框 -->
	<div id="video_dlg" class="easyui-dialog" style="width:390px;height:490px;padding:10px 20px" closed="true" buttons="#vdlg-buttons">
		 <table id="tt" data-options="border:false,toolbar:'#tb'">
		 </table>
	    <!-- 列表上面的按钮和搜索条件  -->
	     <div id="tb" style="padding:5px;height:auto">
			<form action="" id="searchForm">
				<div>
					所属活动&nbsp;
					<select name="filters['activity']" style="width:100px">
						<option value="">选择活动</option>
						<c:forEach items="${activityList}" var="activity" >
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
	<!-- 添加对话框里的保存和取消按钮 -->
	<div id="vdlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="selectVideo();">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#video_dlg').dialog('close')">取消</a>
	</div>
</body>
</html>