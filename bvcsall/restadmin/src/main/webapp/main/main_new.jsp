<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/sitemesh-decorator.tld" prefix="decorator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><decorator:title default="busap video content system 后台管理" /> - busap video content system 后台管理</title>
<link rel="stylesheet" type="text/css" href="css/default.css">
<link rel="stylesheet" type="text/css" href="files/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="files/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/button.css">
<link rel="stylesheet" type="text/css" href="files/zTree/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="files/zTree/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="files/easyui/themes/common.css">

<!-- <link rel="stylesheet" type="text/css" href="css/baseCss.css"> -->

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="files/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="files/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="files/easyui/jquery.easyui.patch.js"></script>
<script type="text/javascript" src="js/datagrid-dnd.js"></script>
<script type="text/javascript" src="files/main.js"></script>
<script type="text/javascript" src="files/zTree/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript">
var baseUrl = "<%=basePath%>";
</script>
<script type="text/javascript">
//修改密码
function updatePassword(){
	$("#passwordfm").form('clear');
	$("#id").val(${uid});
	$("#passworddlg").dialog('open').dialog('setTitle',"修改密码");
}

function save(mydialogueId,myFormId){
	var new_password=$("#new_password").val();
	var new_repeat_password=$("#new_repeat_password").val();
	if($.trim(new_password)!=$.trim(new_repeat_password)){
		showMessage( "错误","新密码与重复密码不一致",3000);
		return;
	}

	$.ajax({
        url: "user/updatePasswordAjax",
        data: getFormJson( myFormId),
        type: "post",
        dataType: "json",
        beforeSend: function(){
         return $( myFormId).form( 'validate');
        },
        success: function (result){
            if (result[ "success"]== true){
            	showMessage( "成功提示","修改密码成功",3000);
                $( mydialogueId).dialog( 'close'); // close the dialog
            } else {
            	showMessage( "错误",result["result"],3000);
            }
        },
        error:function (result){
        	showMessage( "Error",JSON.stringify(result),5000);
        }
    });
}
	var audioAble;
	var wschat;
	var menu =  eval(${menu});
	var ztree;
	$(document).ready(function(){
		audioAble = $.cookie('audioAble');
		
		var setting = {
			check: { /**复选框**/
				enable: false,
			 	chkboxType: {"Y":"", "N":""}
			 },
			 view: {
			 	showLine: false,
			 	showIcon: false,
			 	selectedMulti: false,
			 	expandSpeed: 300 //设置树展开的动画速度，IE6下面没效果，
			 },
			 data: {
			 	simpleData: {   //简单的数据源，一般开发中都是从数据库里读取，API有介绍，这里只是本地的
				    enable: true,
				    idKey: "id",  //id和pid，这里不用多说了吧，树的目录级别
				    pIdKey: "pId",
				    rootPId: 0   //根节点
			  	}
			 },
			 callback:{
				 onClick: zTreeOnClick,
				 onNodeCreated: zTreeOnNodeCreated
			 }
	    };

		ztree = $.fn.zTree.init($("#treeMenu"),setting,menu);
		ztree.expandAll(true);

	     console.log('init main frame')
	     if(menu.length>0){
	    	 for(var i=0;i<menu.length;i++){
	    		 var p=menu[i];
	    		 if(p['name']=='审核管理'){
	    			 var children = p['children'];
	    			 I:for(var j=0;j<children.length;j++){
	    				 var child = children[j];
	    				 if(child['name']=='审核视频'){
		    		     	openWS();	
		    		     	addFastLink(child);
	    				 }
	    			 }
	    		 } else if(p['name']=='运营管理'){
	    			 var children = p['children'];
	    			 for(var j=0;j<children.length;j++){
	    				 var child = children[j];
	    				 if(child['name']=='活动管理'){
	    					 addFastLink(child);
	    				 } else if(child['name']=='话题管理'){
	    					 addFastLink(child);
	    				 } else if(child['name']=='视频管理'){
	    					 addFastLink(child);
	    				 }
	    			 }
	    		 } else if(p['name']=='官方视频'){
	    			 var children = p['children'];
	    			 for(var j=0;j<children.length;j++){
	    				 var child = children[j];
	    				 if(child['name']=='管理员视频'){
	    				 	addFastLink(child);
	    				 }
	    			 }
	    		 } else if(p['name']=='用户管理'){
	    			 var children = p['children'];
	    			 for(var j=0;j<children.length;j++){
	    				 var child = children[j];
	    				 if(child['name']=='Blive用户'){
	    					 addFastLink(child);
	    				 }
	    			 }
	    		 }
	    	 }
	     }
	     //setInterval("showRemind()", 30*1000);//消息提醒（如果有新视频未审核则提醒 1分钟/次）
	});

	function zTreeOnClick(evt,treeId, treeNode) {
		var pid = treeNode.pid;
		var id = treeNode.id;

		if(pid>0){
			//saveMenuId(pid,id);
			for(var i=0;i<menu.length;i++){
				if(menu[i]['id']==pid){
					var children = menu[i]['children'];
					if(children){
						for(var j=0;j<children.length;j++){
							if(children[j]['id']==id){
								window.location.href=children[j]['value'];
								break;
							}
						}
					}
					break;
				}
			}
		}

	}

	function zTreeOnNodeCreated(event, treeId, treeNode) {
	    if(treeNode.id=='${menuId}'){
	    	treeNode.font="";
	    }
	}

	//消息提醒：音乐播放
	window.onload = function(){
		initAudio();
	}
	var initAudio = function(){
		audio = document.getElementById('audio');
	}

	function openWS(){
		var checkGroup = $("#checkGroupName").val();
		var uname= $("#uname").val();
		$.ajax({
	        url: "index/wsurl",
	        type: "get",
	        dataType: "json",
	        success: function (result){
	            if (result[ "success"]== true){
	            	var chaturl = result["result"];
	            	if('WebSocket' in window){
	        	 		try{
	        	 			wschat = new WebSocket(chaturl);
	        	 		}catch(e){
	        	 			alert("聊天系统未连接:"+chaturl);
	        	 		}
	        	 	}else if ('MozWebSocket' in window){
	        	 		wschat = new MozWebSocket(chaturl);
	        	 	}else{
	        	 		alert("not support");
	        	 	}

	        	 	wschat.onmessage = function(evt) {
	        	 		var mess = evt.data;
	        	 		if(mess=='000'){
							//alert(mess);
	        	 			showWarning();
	        	 			
	        	 			if(audioAble==undefined){
	        	 				audio.play();	
	        	 			}
	        	 		}else if(mess=='010'){
	        	 			showLiveComplaintsWarning();

	        	 			if(audioAble==undefined){
	        	 				audio.play();	
	        	 			}
	        	 		}/*else if(mess=='020'){
							showIrregularityImageWarning();
							if(audioAble==undefined){
								audio.play();
							}
						}*/else if(mess=='030'){
							showNewLiveWarning();
							var cg = checkGroup.split(",");
							var pathName = window.location.pathname;
							if(cg.indexOf(uname) >= 0 && pathName.indexOf("forwardCheckPage")>=0){
								location.reload();
							}
							if(audioAble==undefined){
								audio.play();
							}
						}
	        	 		console.log(evt.data);
	        	 	};

	        	 	wschat.onclose = function(evt) {
	        	 		if(wschat.readyState==3){
	        	 		}
	        	 		//alert("聊天服务器已关闭，请重新登陆");
						console.log("聊天服务器已关闭，请重新登陆");
	        	 	};
	            } else {
	            	showMessage( "错误",result["result"],3000);
	            }
	        },
	        error:function (result){
	        	showMessage( "Error",JSON.stringify(result),5000);
	        }
	    });

	}
	//消息提醒
	function showRemind(id,obj){
		$.ajax({
	        url: "video/checkNew",
	        type: "post",
	        dataType: "text",
	        success: function (result){
	        	if(result == "YES"){
	        		Message.open();
		    		audio.play();
	        	}
	        }
	    });
	}

/*	function saveMenuId(pid,menuId){
		$.ajax({
	        url: "permission/saveMenuId",
	        data:{'pid':pid,'menuId':menuId},
	        type: "post",
	        dataType: "json",
	        success: function (result){

	        },
	        error:function (result){
	        	showMessage( "Error",JSON.stringify(result),5000);
	        }
	    });
	}*/
function closeWarning(){
	$("#msgContainer").attr("style","display:none");
}

function showWarning(){
	$("#msgContainer").attr("style","display:inline;width:300px;");
}


function closeLiveComplaintsWarning(){
	$("#msgLiveComplaintsContainer").attr("style","display:none;");
}

function showLiveComplaintsWarning(){
	$("#msgLiveComplaintsContainer").attr("style","display:inline;width:300px;margin-left: 10px;");
}

function showNewLiveWarning(){
	$("#msgNewLiveContainer").attr("style","display:inline;width:300px;margin-left: 20px;");
}

function closeNewLiveWarning(){
	$("#msgNewLiveContainer").attr("style","display:none;");
}

//快速导航链接
function addFastLink(m){
	var container = $("#fastLink");
	var div = $("<div></div>");
	div.attr("style","padding-left:10px;float:left;");
	var link = $("<a></a>");
	link.attr("class","");
	link.attr("href",m['value']);
	var label ="<span class=''><span class=''>"+ m['name'] +"</span></span>";
	link.html(label);
	link.appendTo(div);
	
	div.appendTo(container);
}
function closeAudio(){
	audioAble = '1';
	$.cookie('audioAble','1',{ expires: 7 });
}
</script>
<style>
.bwhite {
	color: #fff;
	font-weight: bold;
}

.t {
	clear: both;
	height: 60px;
	width: 100%;
	overflow: hidden;
	background-color:#91c41f;
}

.t_left {
	background: url(./images/logo.jpg) no-repeat 10px 0;
	line-height: 38px;
	height: 38px;
	text-align: left;
	padding-left: 22px;
	width: 210px;
	float: left;
	background-size:210px 38px;
	margin-top:11px;
}

.t_rtl {
	float: right;
	padding-right: 10px;
	padding-top: 4px;
	margin-top: 18px;
    font-size: 14px;
    font-weight: normal;
}

.panel-title {
	text-align: center;
}

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

#msg_win {
	border: 1px solid #A67901;
	background: #EAEAEA;
	width: 240px;
	position: absolute;
	right: 0;
	font-size: 12px;
	font-family: Arial;
	margin: 0px;
	display: none;
	overflow: hidden;
	z-index: 99;
}

#msg_win .icos {
	position: absolute;
	top: 2px;
	*top: 0px;
	right: 2px;
	z-index: 9;
}

.icos a {
	float: left;
	color: #833B02;
	margin: 1px;
	text-align: center;
	font-weight: bold;
	width: 14px;
	height: 22px;
	line-height: 22px;
	padding: 1px;
	text-decoration: none;
	font-family: webdings;
}

.icos a:hover {
	color: #fff;
}

#msg_title {
	background: #7DEAFD;
	border-bottom: 1px solid #A67901;
	border-top: 1px solid #FFF;
	border-left: 1px solid #FFF;
	color: #000;
	height: 25px;
	line-height: 25px;
	text-indent: 5px;
}

#msg_content {
	margin: 5px;
	margin-right: 0;
	width: 230px;
	height: 126px;
	overflow: hidden;
}
</style>
<decorator:head />
</head>
<body class="easyui-layout">
	<%@ include file="/files/globalvar.jsp"%>
	<!-- 头部标题 -->
	<div data-options="region:'north',border:false" style="height: 85px; overflow: hidden;">
		<div class="t">
			<div class="t_left bwhite"></div>
			<div style="height: 60px; width:50%;float:left;margin-left:10%;">
				
				<div style="float:left;overflow: hidden;text-align: center;margin-top:10px;" id="fastLink">
					
				</div>
				
			</div>
			<div class="t_rtl bwhite">
				你好！<a href="javascript:void(0)" onclick="window.location.href='user/userInfo'" style="color:#fff;text-decoration:none;">${uname }</a>
				 <a href="login/logout" target="_top" style="color:#fff;text-decoration:none;">【退出】</a>
				<input type="hidden" value="${checkGroupName}" id="checkGroupName">
				<input type="hidden" value="${uname}" id="uname">
			</div>
		</div>
		<!-- <div style="float:left;text-align:center; overflow: hidden;margin-top:4px;">
			<span style="float:left;"><img src="./images/right.jpg" /></span><span style="float:left;margin-left:14px;">隐藏菜单</span>
		</div> -->
		<div style="float:left;text-align:center; overflow: hidden;margin-top:4px;margin-left:14px;">
			<span style="float:left;"><img src="./images/icon_voice.jpg" onclick="closeAudio()"/></span><span style="float:left;margin-left:10px;">关闭提示音</span>
		</div>
		<div style="float:left;text-align:left; overflow: hidden;margin-top:4px;margin-left:14px;">
			<span id="msgContainer" style="display:none;">
				<%--<a href="javascript:void(0)" iconCls="icon-no" onclick="javascript:closeAudio()" plain="true" style="text-decoration:none;color:#7A7A7A;margin-left:5px;">关闭声音</a>--%>
				<a href="video/unchecklist?pid=11" style="text-decoration:none;color: #ff634a;">
					<span style="font-size: 13px;">系统消息:有新视频需要审核,去审核吧!</span>
				</a>
				<a href="javascript:void(0)" iconCls="icon-no" onclick="javascript:closeWarning()" plain="true" style="text-decoration:none;color:#ff634a;margin-left:5px;">X</a>
			</span>
			<span id="msgLiveComplaintsContainer" style="display:none;margin-left: 10px;">
				<%--<a href="javascript:void(0)" iconCls="icon-no" onclick="javascript:closeAudio()" plain="true" style="text-decoration:none;color:#7A7A7A;margin-left:5px;">关闭声音</a>--%>
				<a href="liveComplaint/liveComplaintlist?pid=11" style="text-decoration:none;color: #ff634a;">
					<span style="font-size: 13px;">系统消息:有新的直播投诉,去处理吧!&nbsp;</span>
				</a>
				<a href="javascript:void(0)" iconCls="icon-no" onclick="javascript:closeLiveComplaintsWarning()" plain="true" style="text-decoration:none;color:#ff634a;margin-left:5px;">X</a>
			</span>
			<span id="msgNewLiveContainer" style="display:none;margin-left: 20px;">
				<%--<a href="javascript:void(0)" iconCls="icon-no" onclick="javascript:closeAudio()" plain="true" style="text-decoration:none;color:#7A7A7A;margin-left:5px;">关闭声音</a>--%>
				<a href="check/forwardCheckPage" style="text-decoration:none;color: #ff634a;">
					<span style="font-size: 13px;">系统消息:有新的直播需要审核,Go!&nbsp;</span>
				</a>
				<a href="javascript:void(0)" iconCls="icon-no" onclick="javascript:closeNewLiveWarning()" plain="true" style="text-decoration:none;color:#ff634a;margin-left:5px;">X</a>
			</span>
		</div>
	</div>

	<!-- 左侧导航 -->
	<div data-options="region:'west',split:true,title:'导航菜单'" style="width: 210px;overflow-y:scroll;padding: 1px;">
		<div id="treeMenu" class="ztree"></div>
		<%-- <div class="easyui-accordion" data-options="fit:true,border:false">
			<c:forEach var="item" items="${ menu}">
				<c:if test="${item['pid']==0 }">
					<div id="pmenu${item['id']}" title="${item['name'] }" data-options="" style="text-align: center; overflow-x: hidden;">
						<c:forEach var="subitem" items="${ item['children']}">
							<div id="menu${subitem['id']}" style="border-bottom: 1px solid #E8F1FF">
								<a href="${subitem['value'] }" class="easyui-linkbutton" data-options="plain:true" onclick="saveMenuId('${subitem['pid']}','${subitem['id']}')" style="width: 100%;">${subitem['name'] }</a>
							</div>
						</c:forEach>
					</div>
				</c:if>
			</c:forEach>
		</div> --%>
	</div>
	<!-- 页脚信息 -->
	<div data-options="region:'south',border:false" style="height: 30px; background: #E2EDFF; padding: 2px; vertical-align: middle;">
		<span id="sysInfo">版权所有©2012 巴士在线控股有限公司</span> <span id="sysVersion">系统版本：V&nbsp;&nbsp;1.2.0</span>
	</div>
	<!-- 内容tabs -->
	<div id="mainFrame" data-options="region:'center'" style="">
		<decorator:body />
	</div>
	<%--<c:import url="../main/common.jsp"/>--%>
	<!-- 修改密码弹出的添加对话框 -->
<%-- 	<div id="passworddlg" class="easyui-dialog" style="width: 400px; height: 280px; padding: 10px 20px" closed="true" buttons="#passworddlg-buttons">
		<div class="ftitle">修改密码</div>
		<form id="passwordfm" method="post" novalidate>
			<div class="fitem">
				<label>原密码:</label> <input type="password" name="old_password"
					class="easyui-validatebox easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>新密码:</label> <input type="password" name="new_password" id="new_password" class="easyui-validatebox easyui-textbox" required="true">
			</div>
			<div class="fitem">
				<label>重复密码:</label> <input type="password" name="new_repeat_password" id="new_repeat_password" class="easyui-validatebox easyui-textbox" required="true">
			</div>
			<input type="hidden" name="id" id="id" value="${uid }" />
		</form>
	</div> --%>

	<!-- 对话框里的保存和取消按钮 -->
	<div id="passworddlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="save('#passworddlg','#passwordfm')">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#passworddlg').dialog('close')">取消</a>
	</div>
	<div id="msg_win" style="display:block;top:490px;visibility:visible;opacity:1;">
		<div class="icos">
			<a id="msg_min" title="最小化" href="javascript:void 0">_</a>
			<a id="msg_close" title="关闭" href="javascript:void 0">×</a>
		</div>
		<div id="msg_title">有新视频需要审核</div>
		<div id="msg_content" style="text-align: center;padding-top: 50px;">
			<a href="video/unchecklist?pid=11">
				<button class="button green bigrounded" type="button" style="width: 80px;height: 30px;">
				去审核
				</button>
			</a>
		</div>
	</div>

<script language="javascript">
var Message={
	set: function() {//最小化与恢复状态切换
		var set=this.minbtn.status == 1?[0,1,'block',this.char[0],'最小化']:[1,0,'none',this.char[1],'展开'];
		this.minbtn.status=set[0];
		this.win.style.borderBottomWidth=set[1];
		this.content.style.display =set[2];
		this.minbtn.innerHTML =set[3]
		this.minbtn.title = set[4];
		this.win.style.top = this.getY().top;
	},
	close: function() {//关闭
		this.win.style.display = 'none';
		window.onscroll = null;
	},
	setOpacity: function(x) {//设置透明度
		var v = x >= 100 ? '': 'Alpha(opacity=' + x + ')';
		this.win.style.visibility = x<=0?'hidden':'visible';//IE有绝对或相对定位内容不随父透明度变化的bug
		this.win.style.filter = v;
		this.win.style.opacity = x / 100;
	},
	show: function() {//渐显
		clearInterval(this.timer2);
		var me = this,fx = this.fx(0, 100, 0.1),t = 0;
		this.timer2 = setInterval(function() {
		t = fx();
		me.setOpacity(t[0]);
		if (t[1] == 0) {clearInterval(me.timer2) }
		},10);
	},
	fx: function(a, b, c) {//缓冲计算
		var cMath = Math[(a - b) > 0 ? "floor": "ceil"],c = c || 0.1;
		return function() {return [a += cMath((b - a) * c), a - b]}
	},
	getY: function() {//计算移动坐标
		var d = document,b = document.body, e = document.documentElement;
		var s = Math.max(b.scrollTop, e.scrollTop);
		var h = /BackCompat/i.test(document.compatMode)?b.clientHeight:e.clientHeight;
		var h2 = this.win.offsetHeight;
		return {foot: s + h + h2 + 2+'px',top: s + h - h2 - 2+'px'}
	},
	moveTo: function(y) {//移动动画
		clearInterval(this.timer);
		var me = this,a = parseInt(this.win.style.top)||0;
		var fx = this.fx(a, parseInt(y));
		var t = 0 ;
		this.timer = setInterval(function() {
		t = fx();
		me.win.style.top = t[0]+'px';
		if (t[1] == 0) {
			clearInterval(me.timer);
			me.bind();
		}
		},10);
	},
	bind:function (){//绑定窗口滚动条与大小变化事件
		var me=this,st,rt;
		window.onscroll = function() {
		clearTimeout(st);
		clearTimeout(me.timer2);
		me.setOpacity(0);
		st = setTimeout(function() {
		me.win.style.top = me.getY().top;
		me.show();
		},600);
		};
		window.onresize = function (){
			clearTimeout(rt);
			rt = setTimeout(function() {me.win.style.top = me.getY().top},100);
		}
	},
	init: function() {//创建HTML
		function $(id) {return document.getElementById(id)};
		this.win=$('msg_win');
		var set={minbtn: 'msg_min',closebtn: 'msg_close',title: 'msg_title',content: 'msg_content'};
		for (var Id in set) {this[Id] = $(set[Id])};
		var me = this;
		this.minbtn.onclick = function() {me.set();this.blur()};
		this.closebtn.onclick = function() {me.close()};
		this.char=navigator.userAgent.toLowerCase().indexOf('firefox')+1?['_','::','×']:['0','2','r'];//FF不支持webdings字体
		this.minbtn.innerHTML=this.char[0];
		this.closebtn.innerHTML=this.char[2];
		setTimeout(function() {//初始化最先位置
			me.win.style.display = 'none';
			me.win.style.top = me.getY().foot;
			me.moveTo(me.getY().top);
		},0);
		return this;
	},
	open: function() {//创建HTML
		function $(id) {return document.getElementById(id)};
		this.win=$('msg_win');
		var set={minbtn: 'msg_min',closebtn: 'msg_close',title: 'msg_title',content: 'msg_content'};
		for (var Id in set) {this[Id] = $(set[Id])};
		var me = this;
		this.minbtn.onclick = function() {me.set();this.blur()};
		this.closebtn.onclick = function() {me.close()};
		this.char=navigator.userAgent.toLowerCase().indexOf('firefox')+1?['_','::','×']:['0','2','r'];//FF不支持webdings字体
		this.minbtn.innerHTML=this.char[0];
		this.closebtn.innerHTML=this.char[2];
		setTimeout(function() {//初始化最先位置
			me.win.style.display = 'block';
			me.win.style.top = me.getY().foot;
			me.moveTo(me.getY().top);
		},0);
		return this;
	}
};
Message.init();
</script>
<audio id="audio" controls="controls" style="width: 0px;height: 0px;">
	<!--   <source src="/i/song.ogg" type="audio/ogg"> -->
	<source src="sound/new_Message.mp3" type="audio/mpeg">
</audio>
</body>
</html>