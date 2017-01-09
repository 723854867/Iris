/**
 * Created by busap on 2016/1/13.
 */
$(function () {
    $('#displayTable').datagrid({
        nowrap: false, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: false,
        singleSelect: true, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: "liveSetting/ajaxPage",
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">名称</span>', width: 100, align: 'center'},
                {field: 'majiaCount', title: '<span class="columnTitle">马甲数</span>', width: 100, align: 'center'},
                {field: 'majiaPeriod', title: '<span class="columnTitle">时间间隔</span>', width: 60, align: 'center'},
                {field: 'typeName', title: '<span class="columnTitle">类型</span>', width: 60, align: 'center'},
                {field: 'status', title: '<span class="columnTitle">状态</span>', width: 60, align: 'center',formatter:function(value,row){
                	if(value == 0){
                		return '有效';
                	}else{
                		return '无效';
                	}
                }},
                {field: 'createDateStr', title: '<span class="columnTitle">创建时间</span>', width: 80, align: 'center'},
                {field: 'modify', title: '<span class="columnTitle">操作</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                    	var content = '<a class="easyui-linkbutton" href="javascript:;" onclick="editSettingDlg(' + row.id + ')" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                    	content += '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteSetting(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                    	content += '<a class="easyui-linkbutton" href="javascript:;" onclick="editUser(' + row.id + ')" style="width:80px;height: 25px;" title="编辑用户"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑用户</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                    	content += '<a class="easyui-linkbutton" href="javascript:;" onclick="userCountDlg(' + row.id + ')" style="width:80px;height: 25px;" title="加人"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">加人</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                    	return content;                        
                    }
                }
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function () {
            $('#displayTable').datagrid('clearSelections');
        },
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
});

function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    $('#displayTable').datagrid({url: "living/queryLivingList"});
}

function offline() {
	var id = $("#roomId").val();
	var mess = $('#message').val();
	var expire = $('#expireMin').val();
	if(mess=='' || expire==''){
		showMessage("提示信息", "请输入数据！");
		return;
	}
	if(mess.length>16){
		showMessage("提示信息", "输入禁播原因不能超过16个字！");
		return;
	}
    $.ajax({
        url: 'living/offline',
        type: 'post',
        data: {roomId: id,message:mess,expireMin:expire},
        async: false, //默认为true 异步
        error: function () {
            showMessage("提示信息", "下线失败，请重试！");
        },
        success: function (result) {
            if (result == "success") {
                showMessage("提示信息", "下线成功！");
                $('#dlg').dialog('close');
                doSearch();
            } else {
                showMessage("提示信息", "下线失败，请重试！");
            }
        }
    });
}

function editSettingDlg(id){
	$("#settingId").val(id);
	var rows = $('#displayTable').datagrid("getRows");
	 for (var i = 0; i < rows.length; i++) {
		 if(rows[i]["id"]==id){
			 $("#editForm").form('load', rows[i]);
		 }
	 }
	
    $("#dlg").dialog('open').dialog('setTitle', "编辑设置");
}

function editSetting(){
	if($('editForm').form('validate')){
		var id = $("#settingId").val();
		var name = $("#usettingname").val();
		var majiaCount = $("#umajiaCount").val();
		if((majiaCount=='undefined' || majiaCount=='') && !$("#umaxMajiaCount")[0].checked){
			showMessage("提示信息", "请输入马甲数");
			return;
		}
		if($("#umaxMajiaCount")[0].checked){
			majiaCount = -1;
		}
		var majiaPeriod = $("#umajiaPeriod").val();
		if(majiaPeriod=='undefined' || majiaPeriod==''){
			showMessage("提示信息", "请输入起始时间间隔");
			return;
		}
		var maxMajiaPeriod = $("#umaxMajiaPeriod").val();
		if(maxMajiaPeriod=='undefined' || maxMajiaPeriod==''){
			showMessage("提示信息", "请输入最大时间间隔");
			return;
		}
		if(parseInt(maxMajiaPeriod)<parseInt(majiaPeriod)){
			showMessage("提示信息", "时间间隔不能颠倒");
			return;
		}
		var typeId = $("#utypeId").val();
		var status = $("#ustatus").val();
		
		$.post('liveSetting/modify', {id:id,name:name,majiaCount:majiaCount,majiaPeriod:majiaPeriod,typeId:typeId,status:status,maxMajiaPeriod:maxMajiaPeriod}, function (result) {
            if (result["success"] == true) {
            	$('#dlg').dialog('close');
            	$('#displayTable').datagrid('reload'); 
            } else {
            	 showMessage("提示信息", "设置失败，请重试！");
            }
        });
	}
}

function settingLiveNoticeDlg(){
    $("#dlgNotice").dialog('open').dialog('setTitle', "添加设置");
}

function saveLiveSetting(){
	if($('addForm').form('validate')){
		var name = $("#settingname").val();
		var majiaCount = $("#majiaCount").val();
		if((majiaCount=='undefined' || majiaCount=='') && !$("#maxMajiaCount")[0].checked){
			showMessage("提示信息", "请输入马甲数");
			return;
		}
		if($("#maxMajiaCount").checked){
			majiaCount = -1;
		}
		var majiaPeriod = $("#majiaPeriod").val();
		if(majiaPeriod=='undefined' || majiaPeriod==''){
			showMessage("提示信息", "请输入起始时间间隔");
			return;
		}
		var maxMajiaPeriod = $("#maxMajiaPeriod").val();
		if(maxMajiaPeriod=='undefined' || maxMajiaPeriod==''){
			showMessage("提示信息", "请输入最大时间间隔");
			return;
		}
		if(parseInt(maxMajiaPeriod)<parseInt(majiaPeriod)){
			showMessage("提示信息", "时间间隔不能颠倒");
			return;
		}
		var typeId = $("#typeId").val();
		
		$.post('liveSetting/add', {name:name,majiaCount:majiaCount,majiaPeriod:majiaPeriod,typeId:typeId,maxMajiaPeriod:maxMajiaPeriod}, function (result) {
            if (result["success"] == true) {
            	$('#dlgNotice').dialog('close');
            	$('#displayTable').datagrid('reload'); 
            } else {
            	 showMessage("提示信息", "设置失败，请重试！");
            }
        });
	}
}

function deleteSetting(id){
	if (confirm("确定要执行删除操作吗？")) {
		$.get('liveSetting/remove', {id:id}, function (result) {
	        if (result["success"] == true) {
	        	$('#displayTable').datagrid('reload'); 
	        } else {
	        	 showMessage("提示信息", "设置失败，请重试！");
	        }
	    });
	}
}

function editUser(id){
	$("#settingId").val(id);
	$('#userTable').attr("innerHtml",""); 
	$("#userBoundDlg").dialog('open').dialog('setTitle', "编辑用户");
	loadUser(id);
}

function loadUser(settingId){
	$('#userTable').datagrid({
        nowrap: false, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: false,
        singleSelect: false, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: "liveSetting/searchUser?settingId="+settingId,
        columns: [
            [
             	{field: 'ck', width: 60, checkbox: true,align: 'center'},
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
                {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 100, align: 'center'},
                {field: 'username', title: '<span class="columnTitle">登录名</span>', width: 100, align: 'center'},
                {field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 60, align: 'center'}
            ]
        ],
        toolbar: "#userToolbar",
        onLoadSuccess: function () {
            $('#userTable').datagrid('clearSelections');
        },
        pageSize: 20,
        pageList: [20, 40, 60, 80, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
}

function addUser(){
	var settingId = $("#settingId").val();
	var userId = $("#userId").val();
	if(settingId=='undefined' || settingId==''){
		showMessage("ERROR", "未选中条目");
		return;
	}
	if(userId==''){
		showMessage("提示", "请输入用户ID");
		return;
	}
	$.get('liveSetting/addUser', {settingId:settingId,uid:userId}, function (result) {
        if (result["success"] == true) {
        	$('#userTable').datagrid('reload'); 
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
}

function removeUsers(){
	var rows = $('#userTable').datagrid("getChecked");
	if(rows){
		var ids = [];
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i]["id"]);
		}
		
		ids = ids.join(",");
		
		var settingId = $("#settingId").val();
		if(settingId=='undefined' || settingId==''){
			showMessage("ERROR", "未选中条目");
			return;
		}
		if(ids==''){
			showMessage("提示", "没有选择用户");
			return;
		}
		
		if (confirm("确定要执行移除操作吗？")) {
			$.get('liveSetting/removeUser', {settingId:settingId,uid:ids}, function (result) {
		        if (result["success"] == true) {
		        	$('#userTable').datagrid('reload'); 
		        } else {
		        	 showMessage("提示信息", "设置失败，请重试！");
		        }
		    });
		}
	} else{
		showMessage("提示信息", "没有选择行");
	}
}

//加数设置
function userCountDlg(id){
	$("#settingId").val(id);
	var rows = $('#displayTable').datagrid("getRows");
	 for (var i = 0; i < rows.length; i++) {
		 if(rows[i]["id"]==id){
			 $("#userCountForm").form('load', rows[i]);
		 }
	 }
	
    $("#userCountNotice").dialog('open').dialog('setTitle', "人数设置");
}

function userCountSetting(){
	var settingId = $("#settingId").val();
	
	if(settingId=='undefined' || settingId==''){
		showMessage("ERROR", "未选中条目");
		return;
	}
	var majiaCountBegin = $("#majiaCountBegin").val();
	if(majiaCountBegin=='undefined' || majiaCountBegin==''){
		showMessage("提示信息", "请输入起始马甲数");
		return;
	}
	var maxUserCount = $("#maxUserCount").val();
	if(maxUserCount=='undefined' || maxUserCount==''){
		showMessage("提示信息", "请输入最大人数");
		return;
	}
	var userCountStep = $("#userCountStep").val();
	if(userCountStep=='undefined' || userCountStep==''){
		showMessage("提示信息", "请输入每次增加数");
		return;
	}
	var userCountPeriod = $("#userCountPeriod").val();
	if(userCountPeriod=='undefined' || userCountPeriod==''){
		showMessage("提示信息", "请输入最小时间间隔");
		return;
	}
	var maxUserCountPeriod = $("#maxUserCountPeriod").val();
	if(maxUserCountPeriod=='undefined' || maxUserCountPeriod==''){
		showMessage("提示信息", "请输入最大时间间隔");
		return;
	}
	var userCountStat = $("#userCountStat").val();
	if(userCountStat=='undefined' || userCountStat==''){
		showMessage("提示信息", "请选择开启状态");
		return;
	}
	if(parseInt(maxUserCountPeriod)<parseInt(userCountPeriod)){
		showMessage("提示信息", "时间间隔不能颠倒");
		return;
	}
	
	$.get('liveSetting/modifyUserCount', {settingId:settingId,majiaCountBegin:majiaCountBegin,maxUserCount:maxUserCount,userCountStep:userCountStep,userCountPeriod:userCountPeriod,maxUserCountPeriod:maxUserCountPeriod,userCountStat:userCountStat}, function (result) {
        if (result["success"] == true) {
        	$('#userCountNotice').dialog('close');
        	$('#displayTable').datagrid('reload'); 
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
}
//------------------------//

//通用设置
function commenDlg(){
	$.get('liveSetting/commenSetting',{}, function (result) {
		if (result["success"] == true) {
			 $("#commenForm").form('load', result["result"]);
		}
	});
	
    $("#commenDlg").dialog('open').dialog('setTitle', "通用设置");
}

function commenSetting(){
	var minReal = $("#minReal").val();
	if(minReal=='undefined' || minReal==''){
		showMessage("提示信息", "请输入最小真实用户数");
		return;
	}
	var minMajia = $("#minMajia").val();
	if(minMajia=='undefined' || minMajia==''){
		showMessage("ERROR", "请输入最小机器人数");
		return;
	}
	
	var maxReal = $("#maxReal").val();
	if(maxReal=='undefined' || maxReal==''){
		showMessage("提示信息", "请输入最大真实用户数");
		return;
	}	

	if(parseInt(maxReal)<parseInt(minReal)){
		showMessage("提示信息", "真实用户数不能颠倒");
		return;
	}
	
	var maxStep = $("#maxStep").val();
	if(maxStep=='undefined' || maxStep==''){
		showMessage("提示信息", "请输入最大机器人增加数");
		return;
	}
	var step = $("#step").val();
	if(step=='undefined' || step==''){
		showMessage("提示信息", "请输入机器人每次增加数");
		return;
	}
	var commenPeriod = $("#commenPeriod").val();
	if(commenPeriod=='undefined' || commenPeriod==''){
		showMessage("提示信息", "请输入最小时间间隔");
		return;
	}
	var maxCommenPeriod = $("#maxCommenPeriod").val();
	if(maxCommenPeriod=='undefined' || maxCommenPeriod==''){
		showMessage("提示信息", "请输入最大时间间隔");
		return;
	}
	var commenStat = $("#commenStat").val();
	if(commenStat=='undefined' || commenStat==''){
		showMessage("提示信息", "请选择开启状态");
		return;
	}
	if(parseInt(maxCommenPeriod)<parseInt(commenPeriod)){
		showMessage("提示信息", "时间间隔不能颠倒");
		return;
	}
	
	$.get('liveSetting/modifyCommenSetting', {minRealUser:minReal,minMajiaCount:minMajia,maxRealUser:maxReal,step:step,maxStep:maxStep,period:commenPeriod,maxPeriod:maxCommenPeriod,commenStat:commenStat}, function (result) {
        if (result["success"] == true) {
        	$('#commenDlg').dialog('close');
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
}
//--------------------------------//
//------------置顶 白名单--------------------//
function topDlg(){
	$('#topTable').attr("innerHtml",""); 
	$("#topDlg").dialog('open').dialog('setTitle', "编辑用户");
	loadTopUser();
}

function loadTopUser(){
$('#topTable').datagrid({
    nowrap: false, //是否换行
    autoRowHeight: true, //自动行高
    fitColumns: true,
    fit: true,
    striped: true,
    pageNumber: 1,
    collapsible: true, //是否可折叠
    remoteSort: false,
    singleSelect: false, //是否单选
    pagination: true, //分页控件
    rownumbers: true, //行号
    pagePosition: 'bottom',
    scrollbarSize: 0,
    loadMsg: "数据加载中.....",
    url: "liveSetting/searchTopUser",
    columns: [
        [
         	{field: 'ck', width: 60, checkbox: true,align: 'center'},
            {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
            {field: 'name', title: '<span class="columnTitle">昵称</span>', width: 100, align: 'center'},
            {field: 'username', title: '<span class="columnTitle">登录名</span>', width: 100, align: 'center'},
            {field: 'phone', title: '<span class="columnTitle">手机号</span>', width: 60, align: 'center'}
        ]
    ],
    toolbar: "#topToolbar",
    onLoadSuccess: function () {
        $('#topTable').datagrid('clearSelections');
    },
    pageSize: 20,
    pageList: [20, 40, 60, 80, 100],
    beforePageText: '第', //页数文本框前显示的汉字
    afterPageText: '页    共 {pages} 页',
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});
}

function addTopUser(){
var userId = $("#topUserId").val();
if(settingId=='undefined' || settingId==''){
	showMessage("ERROR", "未选中条目");
	return;
}
if(userId==''){
	showMessage("提示", "请输入用户ID");
	return;
}
$.get('liveSetting/addTopUser', {uid:userId}, function (result) {
    if (result["success"] == true) {
    	$('#topTable').datagrid('reload'); 
    } else {
    	 showMessage("提示信息", "设置失败，请重试！");
    }
});
}

function removeTopUsers(){
var rows = $('#topTable').datagrid("getChecked");
if(rows){
	var ids = [];
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i]["id"]);
	}
	
	ids = ids.join(",");
	
	if(ids==''){
		showMessage("提示", "没有选择用户");
		return;
	}
	
	if (confirm("确定要执行移除操作吗？")) {
		$.get('liveSetting/removeTopUser', {uid:ids}, function (result) {
	        if (result["success"] == true) {
	        	$('#topTable').datagrid('reload'); 
	        } else {
	        	 showMessage("提示信息", "设置失败，请重试！");
	        }
	    });
	}
} else{
	showMessage("提示信息", "没有选择行");
}
}
//------------置顶 白名单--------------------//

