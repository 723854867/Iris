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


var listUrl = "logsta/queryStaList?functionId=8";

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
                {
                    field: 'statDate', 
                    title: '<span class="columnTitle">统计日期</span>', 
                    width: 120, 
                    align:'center',
                    formatter: function (value, row) {
                        return value;
                    }
                }, {
                    field: 'dau',  
                    title: '<span class="one_keep">DAU</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value;
                    }
                }, {
                	field: 'regNum', 
                	title: '<span class="columnTitle">注册人数</span>', 
                	width: 120, 
                	align: 'center'
                },{
                    field: 'oneKeep', 
                    title: '<span class="one_keep">次日留存</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value / 100+"%";
                    }
                }, {
                    field: 'sevenKeep', 
                    title: '<span class="columnTitle">七日留存</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value / 100+"%";
                    }
                },{
                    field: 'd14Keep', 
                    title: '<span class="columnTitle">14日留存</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value / 100+"%";
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
  //  queryParams.platform = $('#platform').combobox("getValue");
   // queryParams.channel = $('#channel').combobox("getValue");
    queryParams.startTime = $('#startTime').datebox('getValue');
    queryParams.endTime = $('#endTime').datebox('getValue');
    /*            queryParams.pStartTime = $('#pStartTime').datebox('getValue');
     queryParams.pEndTime = $('#pEndTime').datebox('getValue');*/
    $('#displayTable').datagrid({url: listUrl});
}




</script>
 

</head>
<body>

<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>

<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">用户统计</h2>
    </div>
    <div>
        <table>
            <tr>

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