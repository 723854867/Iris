/**
 * Created by caojunming on 2016/8/14.
 */
var listUrl = 'logsta/getAutoData'
var query_component;
var ids;
function init_auto_query(one_query) {
	switch(one_query.type) {
		case 1:
			$('#auto_query').append('<td><label>'+ one_query.label +':</label></td>')
				.append('<td><select id="'+one_query._id+'" class="easyui-combobox"></select></td>');
			$('#'+ one_query._id).combobox({
				data : one_query.inits,
				valueField : 'id',
				textField : 'text'
			});
			break;
		case 2:
			$('#auto_query').append('<td><label>'+ one_query.label +':</label></td>')
				.append('<td><select id="'+one_query._id+'" class="easyui-combobox"></select></td>');
			$('#'+ one_query._id).combobox({
				data : one_query.inits,
				valueField : 'id',
				textField : 'text'
			});
			break;
		case 3:
			$('#auto_query').append('<td><label>'+ one_query.label +':</label></td>')
				.append('<td><input type="text" class="easyui-datebox" style="'+one_query.style+'" id="'+one_query._id+'"></td>');
			$('#'+ one_query._id).datebox();
			break;
		default:
			alert('初始化失败：查询组件类别未知!');
	}
}

function init_search_icon() {
	$('#auto_query').append('<td><a href="javascript:void(0);" onclick="doSearch()" class="easyui-linkbutton">搜索</a></td>');
	$('.easyui-linkbutton').linkbutton({iconCls: 'icon-search'});
}

$(function() {
	$.ajax({
		url : 'logsta/autoLogList?tableId=' + $('#hidden_table_id').val(),
		dataType : 'json',
		success : function(data) {
			$('h2:lt(1)').html(data.tableLabel);
			query_component = data.querys;
			$.each(query_component, function() {
				init_auto_query(this);
			});
			init_search_icon();
			var initNum = data.initNum;
			var numList = data.numList.split(',');
			$('#displayTable').datagrid({
				nowrap : true, //是否换行
				autoRowHeight : true, //自动行高
				fitColumns : true,
				fit : true,
				striped : true,
				pageNumber : 1,
				collapsible : true, //是否可折叠
				remoteSort : true,
				singleSelect : false, //是否单选
				pagination : true, //分页控件
				rownumbers : true, //行号
				pagePosition : 'bottom',
				scrollbarSize : 0,
				loadMsg : "数据加载中.....",
				url : listUrl + '?tableId=' + $('#hidden_table_id').val(),
				columns : [ data.columns ],
				toolbar : "#dataGridToolbar",
				onLoadSuccess : function(data) {
					$('#displayTable').datagrid('clearSelections');
					$('#startTime').datebox('setValue', data.startDate);
					$('#endTime').datebox('setValue', data.endDate);
				},
				pageSize : initNum,
				pageList : numList,
				beforePageText : '第', //页数文本框前显示的汉字
				afterPageText : '页    共 {pages} 页',
				displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
			});
		},
		error : function(xhr) {
			alert('动态页有问题或者返回了不标准的JSON字符串\n' + xhr.responseText);
		}
	});
});

//搜索
function doSearch() {
	var url = listUrl + '?tableId=' + $('#hidden_table_id').val();
	var str = "";
	$.each(query_component, function(i, item) {
		if (getCompValue(item)) {
			if (i == 0) {
				str = str + '"' + item._id + '":"' + getCompValue(item) + '"';
			} else {
				str = str + ',"' + item._id + '":"' + getCompValue(item) + '"';
			}
		}
	});
	str = '{' + str + '}';
	var obj = $.parseJSON(str);
	if (!$.isEmptyObject(obj)) {
		url = url + '&body=' + JSON.stringify(obj);
	}
	$('#displayTable').datagrid('options').url = url;
	$('#displayTable').datagrid('reload');
	
//	var queryParams = $('#displayTable').datagrid('options').queryParams;
//	queryParams.platform = $('#platform').combobox("getValue");
//	queryParams.channel = $('#channel').combobox("getValue");
//	queryParams.startTime = $('#startTime').datebox('getValue');
//	queryParams.endTime = $('#endTime').datebox('getValue');
//	$('#displayTable').datagrid({
//		url : listUrl
//	});
}

function getCompValue(component) {
	switch(component.type) {
	case 1:
		return $('#' + component._id).combobox("getValue");
	case 2:
		return $('#' + component._id).combobox("getValue");
	case 3:
		return $('#' + component._id).datebox("getValue");
	default:
		return null;
}
}

//排序
function activitySort(activityId, type) {
	$.ajax({
		url : 'logsta/queryNewUserList',
		data : {
			'type' : type,
			'activityId' : activityId
		},
		type : "post",
		dataType : "json",
		success : function(result) {
			if (result.resultCode == "ok") {
				doSearch()
			} else {
				showMessage("错误提示", result.resultMessage);
			}
		}
	});
}

function updateActiveStatus(obj, activityId, status) {

	$.ajax({
		url : 'activity/updateActiveStatus',
		data : {
			'activeId' : activityId,
			'status' : status
		},
		type : "post",
		dataType : "json",
		success : function(result) {
			//alert("修改成功!");
			doSearch();
		}
	});
}

function deleteActivity(id) {
	if (confirm("您确定要执行删除操作吗？")) {
		if (id > 0) {
			$.ajax({
				url : 'activity/deleteActivity',
				data : {
					'id' : id
				},
				type : "post",
				dataType : "json",
				success : function(result) {
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
		url : 'activity/datas',
		data : {
			'id' : id
		},
		type : "get",
		dataType : "json",
		success : function(result) {
			var content = "<div>马甲视频:" + result['majiaVideoCount']
					+ "&nbsp;&nbsp;我拍视频:" + result['userVideoCount']
					+ "</div><div>参与人数:" + result['userCount']
					+ "&nbsp;&nbsp;点赞总数:" + result['praiseCount']
					+ "&nbsp;&nbsp;评论总数:" + result['evaluationCount']
					+ "</div>";
			$("#datas" + id).html(content);
			$("#datas" + id).attr("style", "display:block");
		}
	});
}

function editActivityOrderNum(id) {
	var orderNum = $("#orderNum" + id).val();
	$.ajax({
		url : 'activity/editActivityOrderNum',
		type : 'post',
		data : {
			'id' : id,
			'orderNum' : orderNum
		},
		async : false, //默认为true 异步
		error : function() {
			showMessage('错误提示', "更新失败，请重试！");
		},
		success : function(data) {
			doSearch();
		}
	});
}