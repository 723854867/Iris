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


var listUrl = "logsta/queryStaList?functionId=2";

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
                    field: 'oneKeep', 
                    title: '<span class="one_keep">次日留存</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value / 100+"%";
                    }
                }, {
                	field: 'threeKeep', 
                	title: '<span class="columnTitle">三日留存</span>', 
                	width: 120, 
                	align: 'center',
                    formatter: function (value, row) {
                    	return value / 100+"%";
                    }
                }, {
                    field: 'fiveKeep', 
                    title: '<span class="columnTitle">五日留存</span>', 
                    width: 120, 
                    align: 'center',
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
                }, {
                    field: 'one1', 
                    title: '<span class="one_keep">次日活跃</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value ;
                    }
                }, {
                	field: 'three1', 
                	title: '<span class="columnTitle">三日活跃</span>', 
                	width: 120, 
                	align: 'center',
                    formatter: function (value, row) {
                    	return value ;
                    }
                }, {
                    field: 'five1', 
                    title: '<span class="columnTitle">五日活跃</span>', 
                    width: 120, 
                    align: 'center',
                    formatter: function (value, row) {
                    	return value / 100+"%";
                    }
                }, {
                    field: 'seven1', 
                    title: '<span class="columnTitle">七日活跃</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value ;
                    }
                }, {
                    field: 'one2', 
                    title: '<span class="one_keep">次日注册</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value ;
                    }
                }, {
                	field: 'three2', 
                	title: '<span class="columnTitle">三日注册</span>', 
                	width: 120, 
                	align: 'center',
                    formatter: function (value, row) {
                    	return value ;
                    }
                }, {
                    field: 'five2', 
                    title: '<span class="columnTitle">五日注册</span>', 
                    width: 120, 
                    align: 'center',
                    formatter: function (value, row) {
                    	return value ;
                    }
                }, {
                    field: 'seven2', 
                    title: '<span class="columnTitle">七日注册</span>', 
                    width: 120,
                    align:'center',
                    formatter: function (value, row) {
                    	return value ;
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

function deleteActivity(id) {
    if (confirm("您确定要执行删除操作吗？")) {
        if (id > 0) {
            $.ajax({
                url: 'activity/deleteActivity',
                data: {'id': id},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.resultCode == "ok") {
                        alert(result.resultMessage);
                        doSearch();
                    } else {
                        alert(result.resultMessage);
                    }
                }
            });
        } else {
            alert("系统出错，请重试！");
        }
    }
}

function showData(id) {
    $.ajax({
        url: 'activity/datas',
        data: {'id': id},
        type: "get",
        dataType: "json",
        success: function (result) {
            var content = "<div>马甲视频:" + result['majiaVideoCount']
                +"&nbsp;&nbsp;我拍视频:" + result['userVideoCount']
                + "</div><div>参与人数:" + result['userCount']
                + "&nbsp;&nbsp;点赞总数:" + result['praiseCount']
                + "&nbsp;&nbsp;评论总数:" + result['evaluationCount'] + "</div>";
            $("#datas" + id).html(content);
            $("#datas" + id).attr("style", "display:block");
        }
    });
}

function editActivityOrderNum(id) {
    var orderNum = $("#orderNum" + id).val();
    $.ajax({
        url: 'activity/editActivityOrderNum',
        type: 'post',
        data: {'id':id,'orderNum':orderNum},
        async: false, //默认为true 异步
        error: function () {
            showMessage('错误提示',"更新失败，请重试！");
        },
        success: function (data) {
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
        <h2 style="float:left;padding-left:10px;margin: 1px">留存</h2>
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
