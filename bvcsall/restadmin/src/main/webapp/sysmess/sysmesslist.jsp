<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html style="background: white">
<head>
    <title>消息管理</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type='text/javascript' src='js/players/jwplayer.js'></script>
    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>

    <script type="text/javascript">
        var datagridId = "#tt";
        var deleteConfirmMessage = "你确定要删除吗?";
        var searchFormId = "#searchForm";

        var popFlg = '${popFlg}';
        var listUrl = "sysmess/searchListPage";
        if (popFlg && popFlg == '1') {
            listUrl += "&filters['popFlg']=1";
        }

        function datagridList() {
            $(datagridId).datagrid({
                fitColumns: true,
                fit: true,
                rownumbers: true,
                pagination: true,
                pageNumber: 1,
                pagePosition: 'bottom',
                striped: true,
                checkOnSelect: false,
                selectOnCheck: false,
                url: listUrl,
                columns: [[
                    {field: 'title', title: '消息标题', align: 'center', width: 100},
                    {
                        field: 'content',
                        title: '消息内容',
                        width: 100,
                        align: 'center',
                        formatter: function (value, row, index) {
                            if (value != null && value.length > 50) {
                                return value.substring(0, 50) + "...";
                            } else
                                return value;
                        }
                    },
                    {
                        field: 'destUser',
                        title: '推送对象',
                        width: 100,
                        align: 'center',
                        formatter: function (value, row, index) {
                            if (value != null && value.length > 50) {
                                return value.substring(0, 50) + "...";
                            } else
                                return value;
                        }
                    },
                    {field: 'publishTime', title: '生效时间', align: 'center', width: 100},
                    {field: 'createDate', title: '创建时间', align: 'center', width: 100},
                    {field: 'stat', title: '当前状态', align: 'center', width: 100},
                    {
                        field: 'imagePath',
                        title: '消息图片',
                        width: 100,
                        align: 'center',
                        formatter: function (value, row, index) {
                            if (row && row.imagePath && row.imagePath != undefined && row.imagePath != '') {
                                return '<img src="/restadmin/download' + row.imagePath + '" height="120"/>';
                            }
                            return '';
                        }
                    },
                    {
                        field: 'vaf',
                        title: '操作',
                        width: 100,
                        align: 'center',
                        formatter: function (value, row, index) {
                            if (row.stat == '未生效') {
                                var deleteStr = '<a class="easyui-linkbutton" href="javascript:;" onclick="deleteMess(' + row.id + ')" style="width:80px;height: 25px;" title="删除"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">删除</span><span class="l-btn-icon icon-remove">&nbsp;</span></span></a>';
                                return deleteStr;
                            } else {
                                return "";
                            }
                        }
                    }
                ]],
                onLoadSuccess: function () {
                    $(this).datagrid('clearSelections');
                },
                pageSize: 20,
                pageList: [20, 40, 60, 80, 100],
                beforePageText: '第', //页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
        }

        $(function () {
            datagridList();
            doSearch();
        });
        var x, y;
        $(function () {
            $('#starttime').datetimebox({
                showSeconds: false
            });
            $('#endtime').datetimebox({
                showSeconds: false
            });

            $(document).mousemove(function (e) {
                e = e || window.event;
                x = e.pageX || (e.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft));
                y = e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));
            });
        });

        function deleteMess(id) {
            $.messager.confirm('Confirm', deleteConfirmMessage, function (r) {
                if (r) {
                    $.post('sysmess/remove', {id: id}, function (result) {
                        if (result["success"] == true) {
                            $(datagridId).datagrid('reload'); // reload the user data
                        } else {
                            showMessage("Error", result["message"]);
                        }
                    });
                }
            });
        }

        function doSearch() {
            $(datagridId).datagrid('reload', getFormJson(searchFormId));
        }

        function upload() {
            location.href = "sysmess/newmess";
        }
    </script>
</head>
<body>
<!-- 列表 -->
<table id="tt" data-options="border:false,toolbar:'#tb'">
</table>

<!-- 列表上面的按钮和搜索条件  -->
<div id="tb" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">消息管理</h2>
    </div>
    <div>
        <form action="" id="searchForm">
            <table width="100%">
                <tr>
                    <td>标题：</td>
                    <td>
                        <input type="text" name="filters['title']" class="easyui-validatebox">
                    </td>
                    <td>状态：</td>
                    <td>
                        <select name="filters['stat']" id="status" class="easyui-combobox">
                            <option value="">请选择状态</option>
                            <option value="0">未生效</option>
                            <option value="1">生效</option>
                        </select>
                    </td>
                    <td>推送对象：</td>
                    <td>
                        <select name="filters['destUser']" class="easyui-combobox">
                            <option value="">请选择状态</option>
                            <option value="0">全部</option>
                            <option value="1">指定用户</option>
                            <option value="2">导入文件</option>
                        </select>
                    </td>
                    <td>内容：</td>
                    <td>
                        <input type="text" name="filters['content']" class="easyui-validatebox">
                    </td>
                    <td>
                        <span>创建时间：</span>
                    </td>
                    <td>
                        <input type="text" class="easyui-datebox" name="filters['startTime']"
                               id="startTime"> 至 <input
                            type="text" class="easyui-datebox" name="filters['endTime']"
                            id="endTime">
                    </td>
                    <td>
                        <span>发布时间：</span>
                    </td>
                    <td>
                        <input type="text" class="easyui-datebox" name="filters['publishTimeStart']" id="publishTimeStart">
                        至
                        <input type="text" class="easyui-datebox" name="filters['publishTimeEnd']" id="publishTimeEnd">
                        <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                           iconCls="icon-search">搜索</a>
                    </td>
                </tr>
            </table>
        </form>
        <a href="javascript:;" onclick="upload();" class="easyui-linkbutton" iconCls="icon-add" plain="true">创建消息</a>
    </div>
</div>

</body>
</html>
