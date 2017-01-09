/**
 * Created by 
 */
var listUrl = "varietyHistory/ajaxList";
$(function () {
    $('#displayTable').datagrid({
        nowrap: true, //是否换行
        autoRowHeight: true, //自动行高
        fitColumns: true,
        fit: true,
        striped: true,
        pageNumber: 1,
        collapsible: true, //是否可折叠
        remoteSort: true,
        singleSelect: false, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        pagePosition: 'bottom',
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        columns: [
            [
                {field: 'name', title: '<span class="columnTitle">期数</span>', width: 120, align:'center'}, 
                {field: 'uids', title: '<span class="columnTitle">用户</span>', width: 120, align:'center'}, 
                {field: 'picUrl', title: '<span class="columnTitle">封面</span>', width: 120, align:'center',formatter: function (value, row, index) {
                	return "<img src='/restadmin/download"+value+"' width='100px' height='100px'/>";
                }}, 
                {field: 'tag', title: '<span class="columnTitle">标签</span>', width: 120, align:'center'}, 
                {field: 'createDateStr', title: '<span class="columnTitle">添加时间</span>', width: 120, align:'center'}, 
                {field: 'id', title: '<span class="columnTitle">操作</span>', width: 120, align:'center',formatter: function (value, row, index) {
                	var content = '<a class="easyui-linkbutton" href="javascript:;" onclick="editDlg(' + row.id + ')" style="width:80px;height: 25px;" title="编辑"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">编辑</span><span class="l-btn-icon icon-edit">&nbsp;</span></span></a>';
                	content += '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteVariety(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                	return content;
                }}
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function () {
            $('#displayTable').datagrid('clearSelections');
        },
        pageSize: 30,
        pageList: [30, 60, 100],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    
    $('#picUpload').fileupload({
        url: 'varietyHistory/uploadpic',
        sequentialUploads: true,
        dataType: 'json',
        type: 'post',
        crossDomain: true,
        done: function (e, data) {
            uploadresult = data.result;
            var path = uploadresult["result"];
            if(path != 'failed'){
            	$("#picUrl").val(path);//原图
                //var viewImg = $("#viewShowImg");
                //viewImg.attr("src", "<%=request.getContextPath()%>/download" + path);
                //viewImg.attr("style", "display:block");
            	
            }else{
            	showMessage("Error", "上传图片失败，请重试");
            }
            
        },
        progress: function (e, data) {
            //var progress = parseInt(data.loaded / data.total * 100, 10);
            //$('#q').progressbar('setValue', progress);
        },
        start: function (e) {
            //$('#q').show();
            //$('#q').progressbar('setValue', 0);
        },
        change: function (e, data) {
            var fileName = data.files[0].name;
            var fileext = fileName.substring(fileName.lastIndexOf("."));
            fileext = fileext.toLowerCase();
            if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                showMessage("Error", "对不起，系统仅支持标准格式的照片，谢谢~");
                return false;
            }
        }
    });
    
    $('#upicUpload').fileupload({
        url: 'varietyHistory/uploadpic',
        sequentialUploads: true,
        dataType: 'json',
        type: 'post',
        crossDomain: true,
        done: function (e, data) {
            uploadresult = data.result;
            var path = uploadresult["result"];
            if(path != 'failed'){
	            $("#upicUrl").val(path);//原图
	            var viewImg = $("#uimg");
	            viewImg.attr("src", "/restadmin/download" + path);
	            //viewImg.attr("style", "display:block");
            }
        },
        progress: function (e, data) {
            //var progress = parseInt(data.loaded / data.total * 100, 10);
            //$('#q').progressbar('setValue', progress);
        },
        start: function (e) {
            //$('#q').show();
            //$('#q').progressbar('setValue', 0);
        },
        change: function (e, data) {
            var fileName = data.files[0].name;
            var fileext = fileName.substring(fileName.lastIndexOf("."));
            fileext = fileext.toLowerCase();
            if ((fileext != '.jpg') && (fileext != '.gif') && (fileext != '.jpeg') && (fileext != '.png') && (fileext != '.bmp')) {
                showMessage("Error", "对不起，系统仅支持标准格式的照片，谢谢~");
                return false;
            }
        }
    });
});

function saveVarietyHistory(){
	var name = $("#name").val();
	if(name == ''){
		showMessage("提示信息", "期数不能为空");
		return;
	}
	var uids = $("#uids").val();
	var re=/^(\d*)(,(\d*))*$/;
	if(uids == '' || re.exec(uids) == null){
		showMessage("提示信息", "输入多个用户id用“,”分割");
		return;
	}
	
	var tag = $("#tag").val();
	if(tag == ''){
		showMessage("提示信息", "标签不能为空");
		return;
	}
	var playUrl = $("#playUrl").val();
	if(playUrl == ''){
		showMessage("提示信息", "视频路径不能为空");
		return;
	}
	var picUrl = $("#picUrl").val();
	if(picUrl == ''){
		showMessage("提示信息", "请上传封面");
		return;
	}
	
	$.post('varietyHistory/addVarietyHistory', {name:name,uids:uids,tag:tag,playUrl:playUrl,picUrl:picUrl}, function (result) {
        if (result["success"] == true) {
        	$('#displayTable').datagrid('reload'); 
        	$('addForm').reset();
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
	
}

function editDlg(id){
	$("#varietyId").val(id);
	var rows = $('#displayTable').datagrid("getRows");
	 for (var i = 0; i < rows.length; i++) {
		 if(rows[i]["id"]==id){
			 $("#editForm").form('load', rows[i]);
			 var viewImg = $("#uimg");
             viewImg.attr("src", "/restadmin/download" + rows[i]["picUrl"]);
		 }
	 }
	
    $("#editdlg").dialog('open').dialog('setTitle', "编辑");
}

function editSetting(){
	var id = $("#varietyId").val();
	if(id == ''){
		showMessage("提示信息", "没有选择行,请重试");
		return;
	}
	var name = $("#udname").val();
	if(name == ''){
		showMessage("提示信息", "期数不能为空");
		return;
	}
	var uids = $("#uuids").val();
	var re=/^(\d*)(,(\d*))*$/;
	if(uids == '' || re.exec(uids) == null){
		showMessage("提示信息", "输入多个用户id用“,”分割");
		return;
	}
	
	var tag = $("#utag").val();
	if(tag == ''){
		showMessage("提示信息", "标签不能为空");
		return;
	}
	var playUrl = $("#uplayUrl").val();
	if(playUrl == ''){
		showMessage("提示信息", "视频路径不能为空");
		return;
	}
	var picUrl = $("#upicUrl").val();
	if(picUrl == ''){
		showMessage("提示信息", "请上传封面");
		return;
	}
	
	$.post('varietyHistory/modifyVarietyHistory', {id:id,name:name,uids:uids,tag:tag,playUrl:playUrl,picUrl:picUrl}, function (result) {
        if (result["success"] == true) {
        	$('#displayTable').datagrid('reload'); 
        	$("#editdlg").dialog('close');
        } else {
        	 showMessage("提示信息", "设置失败，请重试！");
        }
    });
}

function deleteVariety(id){
	if (confirm("确定要执行删除操作吗？")) {
		$.get('varietyHistory/removeVarietyHistory', {id:id}, function (result) {
	        if (result["success"] == true) {
	        	$('#displayTable').datagrid('reload'); 
	        } else {
	        	 showMessage("提示信息", "设置失败，请重试！");
	        }
	    });
	}
}