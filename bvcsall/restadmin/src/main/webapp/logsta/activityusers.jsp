<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <script type='text/javascript' src="../js/fp/vendor/jquery.ui.widget.js"></script>
    <script type='text/javascript' src="../js/fp/jquery.iframe-transport.js"></script>
    <script type='text/javascript' src="../js/fp/jquery.fileupload.js"></script>
<script>


var listUrl = "logsta/queryStaList?functionId=4";

$(function () {
	$.getJSON('logsta/querySelect?param=platform&functionId=1', function(json){
		console.log(json);
		$('#platform').combobox({
			data : json.rows,
			valueField : 'id',
			textField : 'text'
		});
	});
	$.getJSON('logsta/querySelect?param=channel&functionId=1', function(json){
		console.log(json);
		$('#channel').combobox({
			data : json.rows,
			valueField : 'id',
			textField : 'text'
		});
	});
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
                {
                    field: 'statDate', 
                    title: '<span class="columnTitle">统计日期</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value, row) {
                        return value;
                    }
                }, {
                    field: 'activeNum',  
                    title: '<span class="one_keep">活跃人数</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value;
                    }
                }, {
                	field: 'loginNum', 
                	title: '<span class="columnTitle">登陆人数</span>', 
                	width: 120, 
                	align: 'center'
                }, {
                    field: 'actDevNum', 
                    title: '<span class="columnTitle">活跃机器数</span>', 
                    width: 120, 
                    align: 'center',
                    formatter: function (value) {
                    	return value;
                    }
                }, {
                    field: 'sex', 
                    title: '<span class="columnTitle">性别</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	if(value==0){
                    		return "女";
                    	}else if(value==1){
                    		return "男";
                    	}else{
                    		return "不确定";
                    	}
                    }
                }, {
                    field: 'platform', 
                    title: '<span class="columnTitle">平台</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value, row) {
                        return value;
                    }
                }, {
                    field: 'channel', 
                    title: '<span class="columnTitle">渠道</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value, row) {
                        return value;
                    }
                }
            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function (data) {
            $('#displayTable').datagrid('clearSelections');
            $('#startTime').datebox('setValue', data.startDate);
        	$('#endTime').datebox('setValue', data.endDate);
        },
        pageSize: 1000,
        pageList: [1000, 2000, 3000, 5000, 10000],
        beforePageText: '第', //页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
});

//排序
function activitySort(activityId,type){
    $.ajax({
        url: 'logsta/queryNewUserList',
        data: {'type':type,'activityId': activityId},
        type: "post",
        dataType: "json",
        success: function (result) {
            if(result.resultCode == "ok"){
                doSearch()
            }else{
                showMessage("错误提示",result.resultMessage);
            }
        }
    });
}

//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.platform = $('#platform').combobox("getValue");
    queryParams.channel = $('#channel').combobox("getValue");
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');
    /*            queryParams.pStartTime = $('#pStartTime').datebox('getValue');
     queryParams.pEndTime = $('#pEndTime').datebox('getValue');*/
    $('#displayTable').datagrid({url: listUrl});
}

function updateActiveStatus(obj, activityId, status) {

    $.ajax({
        url: 'activity/updateActiveStatus',
        data: {'activeId': activityId, 'status': status},
        type: "post",
        dataType: "json",
        success: function (result) {
            //alert("修改成功!");
            doSearch();
        }
    });
}


</script>
 

</head>
<body>

<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>

<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">活跃用户</h2>
    </div>
    <div>
        <table>
            <tr>
                <td><label>平台：</label></td>
                <td>
                    <select id="platform" class="easyui-combobox">
                        <option value="">请选择</option></select>
                </td>
                <td><label>渠道：</label></td>
                <td>
                    <select id="channel" class="easyui-combobox">
                        <option value="">请选择</option></select>
                </td>
                <td><label>性别：</label></td>
                <td>
                    <select id="sex" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="1">1-男</option>
                    <option value="2">2-不男不女</option>
                    <option value="0">0-女</option>
                     </select>
                </td>
                <td><label>统计日期：</label></td>
                <td>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="startTime" id="startTime">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="endTime" id="endTime">
                </td>
                <td>
                    <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                       iconCls="icon-search">搜索</a>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>