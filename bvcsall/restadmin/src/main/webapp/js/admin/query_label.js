/**
 * Created by huoshanwei on 2015/10/30.
 */
var listUrl = "activity/queryLabelList";
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
        singleSelect: true, //是否单选
        pagination: true, //分页控件
        rownumbers: true, //行号
        scrollbarSize: 0,
        loadMsg: "数据加载中.....",
        url: listUrl,
        columns: [
            [
                {field: 'name', title: '<span class="columnTitle">话题名称</span>', width: 120, sortable: true},
                {field: 'num', title: '<span class="columnTitle">使用次数</span>', width: 80, align: 'center'},
                {
                    field: 'createDate',
                    title: '<span class="columnTitle">上传时间</span>',
                    width: 80,
                    align: 'center',
                    formatter: function (value) {
                        if (value == null) {
                            return "";
                        } else {
                            //return formatDate(value);
                            return value;
                        }
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

//新增话题
function newLabelNameAdd() {

    var newLabelName = $('#newLabelName').val();
    if (newLabelName) {
        if (confirm('确认增加新话题？')) {
            $.ajax({
                url: 'activity/labelAdd',
                data: {'newLabelName': newLabelName},
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result == 'ok') {
                        showMessage("提示信息", '新建话题增加成功');
                        doSearch();
                        $("#newLabelName").val("");
                    }

                }
            });
        }
    } else {
        showMessage("错误提示", '请输入新建话题名称');
    }


}

$(function () {
    //var availableTags = ["ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++", "Clojure", "COBOL", "ColdFusion", "Erlang", "Fortran", "Groovy", "Haskell", "Java", "JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby", "Scala", "Scheme"];
    //$( "input[searchboxname='keyword']" ).autocomplete({      source: availableTags    });
    $(":input[placeholder='请输入话题名称'][class='textbox-text validatebox-text textbox-prompt']").autocomplete({source: 'activity/findSuggestLabel'});

    $(":input[placeholder='请输入话题名称']").autocomplete({source: 'activity/findSuggestLabel'});

});

//搜索
function doSearch() {
    var queryParams = $('#displayTable').datagrid('options').queryParams;
    queryParams.keyword = $('#keyword').val();
    $('#displayTable').datagrid({url: listUrl});
}