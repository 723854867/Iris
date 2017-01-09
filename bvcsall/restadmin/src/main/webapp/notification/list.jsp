<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>消息推送-极光</title>
<script type="text/javascript">

var pageSize=50;

$(function() {
	$('#dataGrid').datagrid({
		fitColumns:true,
		rownumbers : true,
	    pagination : true,
	    pageNumber : 1,
	    fit:true,
	    border:false,
	    toolbar:'#tb',
	    pageList : [pageSize,pageSize*2,pageSize*3],
	    pageSize : pageSize,
	    pagePosition : 'bottom',
	    singleSelect : true,
	    nowrap:true,
	    url:'notify/listpage',
	    columns:[[
				{field:'ck',checkbox:true},
				{field:'msgid',title:'消息ID',width:40,align:'right'},
				{field:'content',title:'消息内容',width:100},
				{field:'type',title:'推送类型',width:100,formatter: function(value){
					if (value=='message'){
						return '消息';
					} else {
						return '通知';
					}
				}},
				{field:'result',title:'推送结果',width:100,formatter: function(value){
					if (value==1){
						return '成功';
					} else {
						return '失败';
					}
				}},
				{field:'modifyDateStr',title:'创建时间',width:100},
		    ]],
	});
	
	$('#btn_add').click(function(){
		$('#notifyAdd').dialog('open').dialog('setTitle','添加推送消息');
	});
	$('#btn_remove').click(function(){
		var row = $('#dataGrid').datagrid('getSelected');
		if (row){
			$.messager.confirm('Confirm','确认删除记录？',function(r){
				if (r){
					$.get('notify/delete',{id:row.id},function(result){
						if (result["success"]==true){
							$('#dataGrid').datagrid('reload');
						} else {
							showMessage("Error",result["message"]);
						}
					});
				}
			});
		}else{
			showMessage("Error",'没有选中记录');
		}
	});
	
	//验证最多输入字符//
	var maxLength=58;
	var putLength=0;
	$('charleng').text(maxLength);
	
	$('#content').keyup(function(){
		
		var length=$('#content').val().length;//+Math.floor(($('#pushTypeValue').val().length)/3);//别名3个长度算一个
		putLength = length;
		changeCharLeng(maxLength-length);
		if(length>maxLength){
			showMessage('Warning','最多输入'+maxLength+'个字符');
		}
	});
	$('input[name="type"]').click(function(){
		if($('input[name="type"]:checked').val()=='notification'){
			maxLength=58;
			changeCharLeng(58);
		}else{
			maxLength=234;
			changeCharLeng(234);
		}
	});
	var changeCharLeng=function(length){
		$('#charleng').text(length);
	}
	
	changeCharLeng(maxLength);
	// 结束  //
	
	$('#btn_formSave').click(function(){
		$('#ff').form({
		    url:'notify/notesave',
		    onSubmit: function(){
				if(putLength>maxLength){
					$('#charleng').css({'color':'red',});
					return false;
				}
		        return $('#ff').form('validate');
		    },   
		    success:function(result){
		    	result=eval('(' + result + ')');
		    	if (result["success"]==true){
					$('#notifyAdd').dialog('close');
					$('#dataGrid').datagrid('reload');
				} else {
					showMessage("Error",result["message"]);
				}
		    }   
		}).submit();
	});
	
	$('#btn_formCancel').click(function(){
		$('#notifyAdd').dialog('close');
	});
	
	$('input[name="pushType"]').change(function(){
		if($(this).val()==2){
			$('#val').show();
		}else{
			$('#val').hide();
		}
	});
});
</script>
</head>
<body>
<table id="dataGrid"></table>
<div id="tb">
	<div id="btn_add" class="easyui-linkbutton" iconCls="icon-add"  plain="true">添加</div>
	<div id="btn_remove" class="easyui-linkbutton" iconCls="icon-remove"  plain="true">删除</div>
</div>

<!-- 弹出的添加或者编辑对话框 -->
<div id="notifyAdd" class="easyui-dialog" data-options="resizable:true" style="width:550px;height:320px;padding:10px 20px" closed="true" buttons="#dlg-buttons">
	<div class="ftitle">新增推送</div>
	<form id="ff" method="post">
    <table>
    	<tr>
    		<td style="width: 52px; white-space: nowrap;">发送类型</td>
    		<td>
    			<label><input name="type" type="radio" value="notification" checked="checked"/>通知</label>
    			<label><input name="type" type="radio" value="message" />消息</label>
    		</td>
    	</tr>
    	<tr>
    		<td>消息内容</td>
    		<td>
    			<textarea rows="4" cols="45" name="content" id="content" style="font-size: inherit;" class="easyui-validatebox textbox easyui-fluid" data-options="required:true"></textarea>
    		</td>
    	</tr>
    	<tr>
    		<td colspan="2" align="center"><span id="charleng"> </span>个字符可以输入</td>
    	</tr>
    	<tr>
    		<td>发送平台</td>
    		<td>
    			<select name="platform" class=" textbox easyui-fluid easyui-combobox" style="width: 150px;">
    				<option value="" selected="selected">所有</option>
    				<option value="ios">IOS</option>
    				<option value="android">安卓</option>
    			</select>
    		</td>
    	</tr>
    	<tr>
    		<td>发送类型</td>
    		<td>
    			<label><input type="radio" name="pushType" value="1" checked="checked"/>广播(所有人)</label>
    			<label style="display: none;"><input type="radio" name="pushType" value="2" disabled="disabled"/>设备别名(alias)</label>
    			<label style="display: none;"><input type="radio" name="pushType" value="3" disabled="disabled"/>Registration ID</label>
    		</td>
    	</tr>
    	<tr id="val" style="display: none;">
    		<td colspan="2" align="right"><input name="creator_id" id="pushTypeValue" class="textbox" style="width: 280px"/></td>
    	</tr>
    </table>
    </form>
    <!-- 添加对话框里的保存和取消按钮 -->
	<div id="dlg-buttons">
		<a id="btn_formSave" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		<a id="btn_formCancel" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
	</div>
</div>

</body>
</html>