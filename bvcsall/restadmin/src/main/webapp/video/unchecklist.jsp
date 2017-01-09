<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html  style="background: white">
 <head>
    <title>视频审核</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="css/baseCss.css">
	 <script type='text/javascript' src='js/players/jwplayer.js'></script>
	 <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>

     <script type="text/javascript">
     var deleteConfirmMessage="你确定要删除吗?";
     var pageSize=8;
     
     var listUrl="video/searchListPage?filters['flowStat']=uncheck";
     
     function datagridList(){
    	$.post(listUrl,{rows:pageSize,page:1},function(result){
			$("#uncheckCount").text(result.total);
			if (result.total>0){
				//show message
				var videos = result.rows;
				if(videos.length>0){
					var ul = $("#videoUl");
					ul.html("");
					for(var i=0;i<videos.length;i++){
						var video = videos[i];
						var li = $("<li style='width: 350px;'></li>");
						li.attr("id","li_"+video.id);
						var inner = '<div style="float:left;margin-right: 10px;margin-top:95px;"><input type="checkbox" name="check" value="'+video.id+'" id="checkId'+video.id+'"></div>';
						inner += '<div class="pic" style="float:left;" id="playerContainer'+video.id+'">'
						+ '</div>'
		            	+ '<div class="clearYlz"></div>'
		            	+ '<div class="userLBtn"><div style="width:280px;height: 30px;padding-top:5px;margin-left: 30px;">'
		            	+ '<button onclick="checkVideo('+video.id+',\'check_ok\')" class="button green bigrounded" style="width: 70px;height: 25px;">通过</button>'
		            	+ '<button onclick="checkVideo('+video.id+',\'check_fail\');" class="button orange bigrounded" style="width: 70px;height: 25px;">不通过</button>'
			            + '<button onclick="delVideo('+video.id+')" class="button red bigrounded" style="width: 70px;height: 25px;">删除</button>'	
			            + '</div><div style="width:280px;padding-top:5px;margin-left: 30px;">创建者ID:'
			            + video.creatorId+'</div><div style="width:280px;height: 90px;padding-top:5px;margin-left: 30px;">描述:'
			            + video.description+'</div></div>';	
			            li.html(inner);
			            ul.append(li);
						if (video.type == 1 || video.type == 2) {//1直播，2回放
							playVideo(video.type,video.id, video.playKey);
						}else{
							$("#playerContainer"+video.id).html("<img src='/restadmin/download"+video.videoPic+"' style='width=300px;height: 200px;'/>");
						}

					}
// 					var load = $("#loadNex");
// 					if(result.last){
// 						load.css('visibility','hidden');
// 					}else{
// 						load.css('visibility','visible');
// 					}
				}
			} else {
				showMessage("Error","暂时没有未经审核的视频信息！");
			}
		});
     }
     
	$(function() {
		datagridList();
		setInterval(datagridList,300000);
	});
	
	function playVideo(type,playerId,fileName){
		var _player = null;
		
		var player = $('<div/>');
	    $(player).attr('id', 'pl'+playerId);
	    
	    $('#playerContainer'+playerId).append(player);
		var file = "";
		if(type == 1){
			file = '${video_play_url_prefix}'+fileName+'.m3u8';
		}else{
			file = fileName;
		}
	    var conf = {
	        file: file,
	        image: '${img}',
	        height: 200,
	        width: 300,
	        autostart: true,
	        repeat:true,
	        analytics: { enabled: false}
	    };
	    _player = jwplayer('pl'+playerId).setup(conf);
	}

	function checkVideo(mid,flowStat){
		if(flowStat == "check_ok"){
			$.ajax({
				url: 'video/checkTongGuoPage',
				type: 'post',
				data: {'ids': mid},
				async: false, //默认为true 异步
				error: function () {
					showMessage("提示信息","审核失败，请重试！");
				},
				success: function (result) {
					if (result["success"]==true){
						$.messager.progress('close');
						showMessage("提示信息","审核成功！");
						var sear=new RegExp(',');
						if (sear.test(mid)) {
							var liId = mid.split(",");
							$("#uncheckCount").text(parseInt($("#uncheckCount").text())-liId.length);
							for (var i = 0; i < liId.length; i++) {
								removeVideoDom(liId[i]);
							}
						} else {
							$("#uncheckCount").text(parseInt($("#uncheckCount").text())-1);
							removeVideoDom(mid);
						}
						if(document.getElementById("videoUl").getElementsByTagName("li").length == 0){
							datagridList();
						}
					} else {
						$.messager.progress('close');
						showMessage("提示信息","审核失败，请重试！");
					}
				},
				beforeSend:function(result){
					$.messager.progress({
						title: '审核中',
						msg: '正在审核中...'
					});
				}
			});
		}else if(flowStat == "check_fail"){
			$("#id").val(mid);
			$("#dlg").show();
			$("#dlg").dialog({
				title:'未通过审核',
				width: 400,
				height: 150,
				closed: false,
				cache: true,		    	
		    });
		}
	}
	
	function delVideo(id){
		$.messager.confirm('Confirm',deleteConfirmMessage,function(r){
			if (r){
				$.post('video/logicremove',{ids:id},function(result){
					if (result["success"]==true){
						//datagridList();
						$("#uncheckCount").text(parseInt($("#uncheckCount").text())-1);
						removeVideoDom(id);
					} else {
						showMessage("Error",result["message"]);
					}
				});
			}
		});
	}
	
	function removeVideoDom(id){
		//$("ul[id='videoUl'] li").remove("li[id='li_"+id+"']");
		$("#li_"+id).remove();
	}
	
	function checkReason(){
		var reason = [];
		var ckboxes = $("input[name='ck']");
		for(var i=0;i<ckboxes.length;i++){
			if(ckboxes[i].checked){
				reason.push(ckboxes[i].value);
			}
		}
		$("#failReason").val(reason.join(","));
	}
	
	function checkFail(){
		var mid = $("#id").val();
		var failReason = $("#failReason").val();
		$.post("video/check_fail",{id:mid,failReason:failReason},function(result){
			if (result["success"]==true){
				$("#uncheckCount").text(parseInt($("#uncheckCount").text())-1);
				$('#dlg').dialog('close');
				removeVideoDom(mid);
			} else {
				showMessage("Error",result["message"]);
			}
		});
	}

	 //全选
	 function checkAll() {
		 var el = document.getElementsByTagName('input');
		 var len = el.length;
		 for (var i = 0; i < len; i++) {
			 if ((el[i].type == "checkbox") && (el[i].name == "check")) {
				 el[i].checked = true;
			 }
		 }
	 }
	 //取消全选
	 function clearAll() {
		 var el = document.getElementsByTagName('input');
		 var len = el.length;
		 for (var i = 0; i < len; i++) {
			 if ((el[i].type == "checkbox") && (el[i].name == "check")) {
				 el[i].checked = false;
			 }
		 }
	 }

	 function rtrim(s) {
		 var lastIndex = s.lastIndexOf(',');
		 if (lastIndex > -1) {
			 s = s.substring(0, lastIndex);

		 }
		 return s;
	 }

	 //一键通过审核
	 function oneKeyPass(){
		 var checkStr = "";
		 $("input[name='check']").each(function (){
			 if($(this).is(":checked")){
				 checkStr += $(this).val()+",";
			 }
		 })
		 if(checkStr != ""){
			 if(confirm("您确定要一键通过选中的视频吗？")){
			 	checkVideo(rtrim(checkStr),"check_ok");
			 }
		 }else{
			 showMessage("提示信息","请至少选中一个视频！");
		 }
	 }

	 </script>
  </head>
<body>
	<div data-options="region:'north',border:false" style="height: 40px; padding-top: 5px; overflow: hidden;">
		<h2 style="float:left;padding-left:10px;margin: 1px">视频审核</h2>
		<span style="float:left;padding-left:20px;margin: 1px;height:26px;line-height:26px;">未审核(<span id="uncheckCount" style="color: red;">0</span>)</span>
		<span style="float:left;padding-left:20px;margin: 1px">
			<a href="video/checkfaillist" class="easyui-linkbutton" >未通过审核</a>
		</span>
		<span style="float:left;padding-left:20px;margin: 1px">
			<a href="check/forwardCheckPage" class="easyui-linkbutton" >直播审核</a>
		</span>
	</div>
	<div style="margin-left: 10px;margin-bottom: 10px;">
		<input type="button" value="全选" onclick="checkAll()" />
		<input type="button" value="取消全选" onclick="clearAll()" />
		<input type="button" name="submit" onclick="oneKeyPass()" value="一键通过审核">
	</div>
	<div class="userListVedioBox2" id="videos">

		<ul id="videoUl">

		</ul>
<!-- 		<div id="loadNex" style="visibility: hidden"><a href="javascript:void(0)" onclick="loadNext()">加载更多</a></div> -->
	</div>
	
	<!-- 弹出的添加对话框 -->
	<div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
		<form id="updatefm" method="post" novalidate>
			<input type="hidden" name="id" id="id" value=""/>
			不通过原因：<input type="checkbox" value="色情低俗" name="ck" onclick="checkReason()">&nbsp;色情低俗&nbsp;&nbsp;
			<input type="checkbox" value="暴恐类" name="ck" onclick="checkReason()">&nbsp;暴恐类&nbsp;&nbsp;
			<input type="checkbox" value="血腥暴力" name="ck" onclick="checkReason()">&nbsp;血腥暴力&nbsp;&nbsp;
			<br>
			<input type="checkbox" value="政治敏感" name="ck" onclick="checkReason()">&nbsp;政治敏感&nbsp;&nbsp;
			<input type="checkbox" value="非法广告" name="ck" onclick="checkReason()">&nbsp;非法广告&nbsp;&nbsp;
			<input type="checkbox" value="黑屏无画面" name="ck" onclick="checkReason()">&nbsp;黑屏，无画面&nbsp;&nbsp;
			<input type="checkbox" value="其它" name="ck" onclick="checkReason()">&nbsp;其它&nbsp;&nbsp;
			<br>
			其他原因：<input type="text" size="20" id="failReason" name="failReason">
		</form>
	</div>
	
	<!-- 对话框里的保存和取消按钮 -->
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="checkFail()">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	</div>
</body>
</html>
