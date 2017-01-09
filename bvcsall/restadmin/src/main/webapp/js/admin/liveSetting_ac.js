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
        url: "liveSetting/searchAutoChat",
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">ID</span>', width: 40, align: 'center'},
                {field: 'words', title: '<span class="columnTitle">语句</span>', width: 100, align: 'center'},
                {field: 'uid', title: '<span class="columnTitle">用户ID</span>', width: 100, align: 'center'},
                {field: 'userName', title: '<span class="columnTitle">昵称</span>', width: 60, align: 'center'},
                {field: 'typeName', title: '<span class="columnTitle">类型</span>', width: 60, align: 'center'},
                {field: 'createDateStr', title: '<span class="columnTitle">创建时间</span>', width: 80, align: 'center'},
                {field: 'status', title: '<span class="columnTitle">状态</span>', width: 60, align: 'center',formatter:function(value,row){
                	if(value == 0){
                		return '上架';
                	}else{
                		return '下架';
                	}
                }},
                {field: 'modify', title: '<span class="columnTitle">操作</span>', width: 120, align: 'center',
                    formatter: function (value, row) {
                    	var content = '';
                    	if(row.status==0){
                    		content = '<a class="easyui-linkbutton" href="javascript:;" onclick="editAutoChatStatus(' + row.id + ',1)" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">下架</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                    	}
                    	if(row.status==1){
                    		content = '<a class="easyui-linkbutton" href="javascript:;" onclick="editAutoChatStatus(' + row.id + ',0)" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">上架</span><span class="l-btn-icon icon-add">&nbsp;</span></span></a>';
                    	}
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
	var words = $("#words1").val();
	var uid = $("#uid1").val();
	var typeId = $("#type1").val();
    $('#displayTable').datagrid('reload',{words:words,typeId:typeId,uid:uid});
}


function editTypeDlg(){
    $("#editTypeDlg").dialog('open').dialog('setTitle', "编辑类型");
}

function editSetting(){
	if($('editForm').form('validate')){
		var id = $("#settingId").val();
		var name = $("#usettingname").val();
		var majiaCount = $("#umajiaCount").val();
		if($("#umaxMajiaCount").checked){
			majiaCount = $("#umaxMajiaCount").val();
		}
		var majiaPeriod = $("#umajiaPeriod").val();
		var typeId = $("#utypeId").val();
		var status = $("#ustatus").val();
		
		$.post('liveSetting/modify', {id:id,name:name,majiaCount:majiaCount,majiaPeriod:majiaPeriod,typeId:typeId,status:status}, function (result) {
            if (result["success"] == true) {
            	$('#dlg').dialog('close');
            	$('#displayTable').datagrid('reload'); 
            } else {
            	 showMessage("提示信息", "设置失败，请重试！");
            }
        });
	}
}

function addAutoChat(){
	var words = $("#words2").val();
	if(words==''){
		showMessage("提示信息", "语句不能为空,请填写！");
		return;
	}	
	var uid = $("#uid2").val();
	var typeId = $("#type2").val();
	
	$.post('liveSetting/addAutoChat', {uid:uid,typeId:typeId,words:words}, function (result) {
        if (result["success"] == true) {
        	$('#displayTable').datagrid('reload'); 
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
	
}

function editAutoChatStatus(id,status){
	$.get('liveSetting/updateAutoChatStatus', {id:id,status:status}, function (result) {
        if (result["success"] == true) {
        	$('#displayTable').datagrid('reload'); 
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
}

function editType(id,status){
	$.get('liveSetting/updateChatTypeStatus', {id:id,status:status}, function (result) {
        if (result["success"] == true) {
        	document.location.reload();
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
}


function addType(){
	var type = $("#newType").val();
	if(type=='undefined' || type==''){
		showMessage("ERROR", "请输入类型！");
		return;
	}
	
	$.post('liveSetting/addChatType', {name:type}, function (result) {
        if (result["success"] == true) {
        	document.location.reload();
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
}

