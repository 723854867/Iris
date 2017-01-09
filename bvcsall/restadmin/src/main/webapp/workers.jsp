<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>排行榜定时任务</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        // 上一次的行标识
        var lastIndex;
        // 字典类型集合
        var codeClasss = new Array();
        // 页面加载初始化方法
        $(function () {
            initDataGrid();
        });
        // 数据表格初始化方法
        function initDataGrid() {
            $('#displayTable').datagrid({
                fit: true, //表格自适应
                fitColumns: true, //列自适应
                nowrap: true, //是否换行
                autoRowHeight: false, //自动行高
                striped: true,
                collapsible: true, //是否可折叠
                url: '<c:url value="worker/queryWorkers" />',
                sortName: 'name',
                sortOrder: 'asc',
                remoteSort: false,
                idField: 'name',
                singleSelect: true, //是否单选
                pagination: false, //分页控件
                rownumbers: true, //行号
                scrollbarSize: 0,
                loadMsg: "数据加载中.....",
                columns: [
                    [
                        {title: '<span class="columnTitle">基础信息</span>', colspan: 11},
                        {field: 'userId', title: '<span class="columnTitle">操作</span>', width: 80, align: 'center', rowspan: 2,
                            formatter: function (value, row, index) {
                                var optStr = '<a href="javascript:;" style="color:red;text-decoration:none" onclick="update(this);">';
                                optStr += '更新';
                                optStr += '</a>';
                                optStr += '&nbsp;&nbsp;&nbsp;&nbsp;';
                                // searchUserRoles
                                if (row.triggerState == 0) {
                                    optStr += '<a href="javascript:;" style="color:red;text-decoration:none;" onclick="pause(\'' + row.name + '\',\'' + row.group + '\');">';
                                    optStr += '暂停';
                                    optStr += '</a>';
                                    optStr += '&nbsp;&nbsp;&nbsp;&nbsp;';
                                } else {
                                    optStr += '暂停';
                                    optStr += '&nbsp;&nbsp;&nbsp;&nbsp;';
                                }
                                if (row.triggerState == 1) {
                                    optStr += '<a href="javascript:;" style="color:red;text-decoration:none;" onclick="resume(\'' + row.name + '\',\'' + row.group + '\');">';
                                    optStr += '恢复';
                                    optStr += '</a>';
                                    optStr += '&nbsp;&nbsp;&nbsp;&nbsp;';
                                } else {
                                    optStr += '恢复';
                                    optStr += '&nbsp;&nbsp;&nbsp;&nbsp;';
                                }
                                return optStr;
                            }
                        }
                    ],
                    [
                        {field: 'triggerType', title: '<span class="columnTitle">类型</span>', width: 80, sortable: true},
                        {field: 'group', title: '<span class="columnTitle">分组</span>', width: 80, sortable: true},
                        {field: 'name', title: '<span class="columnTitle">名称</span>', width: 100},
                        {field: 'startTime', title: '<span class="columnTitle">开始时间</span>', width: 100,
                            formatter: function (value) {
                                return value;
                            }
                        },
                        {field: 'previousFireTime', title: '<span class="columnTitle">上一轮时间</span>', width: 100,
                            formatter: function (value) {
                                return value;
                            }
                        },
                        {field: 'nextFireTime', title: '<span class="columnTitle">下一轮时间</span>', width: 100,
                            formatter: function (value) {
                                return value;
                            }
                        },
                        {field: 'endTime', title: '<span class="columnTitle">结束时间</span>', width: 100,
                            formatter: function (value) {
                                return value;
                            }
                        },
                        {field: 'priority', title: '<span class="columnTitle">优先级</span>', width: 50},
                        {field: 'triggerState', title: '<span class="columnTitle">状态</span>', width: 50,
                            formatter: function (value) {
                                return stateFormatter(value);
                            }
                        },
                        {field: 'cronExpression', title: '<span class="columnTitle">表达式</span>', width: 100, editor: 'text'},
                        {field: 'repeatInterval', title: '<span class="columnTitle">间隔时间</span>', width: 50, editor: 'text'}
                    ]
                ],
                toolbar: [
                    {
                        text: 'GetChanges',
                        iconCls: 'icon-search',
                        handler: function () {
                            var rows = $('#displayTable').datagrid('getChanges');
                            alert('changed rows: ' + rows.length + ' lines');
                        }
                    }
                ],
                onBeforeLoad: function () {
                    $(this).datagrid('rejectChanges');
                },
                onDblClickRow: function (rowIndex) {
                    if (lastIndex != rowIndex) {
                        $('#displayTable').datagrid('endEdit', lastIndex);
                        $('#displayTable').datagrid('beginEdit', rowIndex);
                        lastIndex = rowIndex;
                    }
                }
            });
        }

        function dataFormatter(value) {
            if (value != null) {
                var date = new Date(value);
                return date.getFullYear() + '-'
                        + ((date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + '-'
                        + (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' '
                        + (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
                        + (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':'
                        + (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
            }
        }

        function stateFormatter(value) {
            switch (value) {
                case 0:
                    return 'NORMAL';
                case 1:
                    return 'PAUSED';
                case 2:
                    return 'COMPLETE';
                case 3:
                    return 'ERROR';
                case 4:
                    return 'BLOCKED';
                case -1:
                    return 'NONE';
            }

        }

        function getRowIndex(target) {
            var tr = $(target).closest('tr.datagrid-row');
            return parseInt(tr.attr('datagrid-row-index'));
        }

        function endEditRow() {
            if (lastIndex != rowIndex) {
                $('#displayTable').datagrid('endEdit', lastIndex);
            }
        }

        function editrow(target) {
            var rowIndex = getRowIndex(target);
            if (lastIndex != rowIndex) {
                $('#displayTable').datagrid('endEdit', lastIndex);
            }
            $('#displayTable').datagrid('beginEdit', rowIndex);
            lastIndex = rowIndex;
        }

        function update(target) {
            var index = getRowIndex(target);
            $('#displayTable').datagrid('endEdit', index);
            var triggerType = $('#displayTable').datagrid('getRows')[index]['triggerType'];
            var name = $('#displayTable').datagrid('getRows')[index]['name'];
            var group = $('#displayTable').datagrid('getRows')[index]['group'];
            var cronExpression = $('#displayTable').datagrid('getRows')[index]['cronExpression'];
            var repeatInterval = $('#displayTable').datagrid('getRows')[index]['repeatInterval'];
            $.ajax({
                type: "post",
                url: "<c:url value='/worker/updateWorker'/>",
                data: {triggerType: triggerType, name: name, group: group, cronExpression: cronExpression, repeatInterval: repeatInterval},
                success: function (data) {
                    alert(data);
                    window.location.reload();
                }
            });
            lastIndex = null;
        }

        function pause(name,group) {
            if (lastIndex != undefined) {
                $('#displayTable').datagrid('endEdit', lastIndex);
            }
            $.ajax({
                type: "post",
                url: "<c:url value='/worker/pauseWorker'/>",
                data: {name: name, group: group},
                success: function (data) {
                    alert(data);
                    window.location.reload();
                }
            });
        }

        function resume(name,group) {
            if (lastIndex != undefined) {
                $('#displayTable').datagrid('endEdit', lastIndex);
            }
            $.ajax({
                type: "post",
                url: "<c:url value='/worker/resumeWorker'/>",
                data: {name: name, group: group},
                success: function (data) {
                    alert(data);
                    window.location.reload();
                }
            });
        }
    </script>
</head>
<body>
<table id="displayTable"></table>
</body>
</html>
