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
        url: "living/queryLivingList",
        columns: [
            [
                {field: 'id', title: '<span class="columnTitle">房间ID</span>', width: 40, align: 'center'},
                {
                    field: 'roomPic', title: '<span class="columnTitle">房间图片</span>', width: 80, align: 'center',
                    formatter: function (value, row) {
                        return '<a href="check/checkPageDetail?roomId='+row.id+'" target="_blank"><img src="/restadmin/download' + value + '" style="width:80px;height:80px;"></a>';
                    }
                },
                {field: 'title', title: '<span class="columnTitle">房间名称</span>', width: 100, align: 'center'},
                {field: 'anchorName', title: '<span class="columnTitle">主播昵称</span>', width: 100, align: 'center'},
                {field: 'creatorId', title: '<span class="columnTitle">主播ID</span>', width: 60, align: 'center'},
                {field: 'praiseNumber', title: '<span class="columnTitle">点赞数</span>', width: 60, align: 'center'},
                {field: 'onlineNumber', title: '<span class="columnTitle">当前在线人数</span>', width: 60, align: 'center'},
                {
                    field: 'maxAccessNumber',
                    title: '<span class="columnTitle">最大在线人数</span>',
                    width: 80,
                    align: 'center'
                },
                {field: 'createDate', title: '<span class="columnTitle">开始时间</span>', width: 80, align: 'center'},
                {field: 'streamSource', title: '<span class="columnTitle">CDN提供商</span>', width: 80, align: 'center'},
                {
                    field: 'modify', title: '<span class="columnTitle">操作</span>', width: 60, align: 'center',
                    formatter: function (value, row) {
                        if (row.status == 1) {
                            return '<a class="easyui-linkbutton" href="javascript:;" onclick="settingLiveOfflineDlg(' + row.id + ')" style="width:80px;height: 25px;" title="下线"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">下线</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                        }
                    }
                },
                {field: 'streamUrl', title: '<span class="columnTitle">推流地址</span>', width: 250, align: 'center'}

            ]
        ],
        toolbar: "#dataGridToolbar",
        onLoadSuccess: function () {
            $('#displayTable').datagrid('clearSelections');
        },
        pageSize: 500,
        pageList: [500, 1000,2000,4000],
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

function settingLiveOfflineDlg(id){
	$("#roomId").val(id);
    $("#dlg").dialog('open').dialog('setTitle', "下线禁播");
}

function settingLiveNoticeDlg(){
    $("#dlgNotice").dialog('open').dialog('setTitle', "直播公告");
}

function settingLiveNotice(){
    var content = $("#content").val();
    var loopNotice = $("#loopNotice").val();
    if(content == ""){
        showMessage("提示信息", "公告内容不能为空！");
    }else{
        $.ajax({
            url: 'living/settingLiveNotice',
            type: 'post',
            data: {'content': content,'loopNotice':loopNotice},
            async: false, //默认为true 异步
            error: function () {
                showMessage("提示信息", "设置失败，请重试！");
            },
            success: function (result) {
                if (result == "success") {
                    showMessage("提示信息", "设置成功！");
                    $('#dlgNotice').dialog('close');
                } else {
                    showMessage("提示信息", "设置失败，请重试！");
                }
            }
        });
    }

}
