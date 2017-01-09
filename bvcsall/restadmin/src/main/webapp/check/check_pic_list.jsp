<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2015/12/23
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>图片审核</title>
    <style>
        .room{
            width: 200px;
            height: 200px;
            float: left;
            overflow: hidden;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $("#passworddlg-buttons").attr("style","display:none;")
//             $(".roomImage").each(function(){
//                 var roomId = $(this).attr("data");
//                 setInterval("autoGetNewScreenshot("+roomId+")",10000);
//             })
        })
        function autoGetNewScreenshot(roomId){
            $.ajax({
                url: 'check/getNewScreenshot',
                type: 'get',
                data: {roomId: roomId},
                async: false, //默认为true 异步
                error: function () {
                    //刷新当前页面
                },
                success: function (result) {
                    if(result.resultCode == "601"){
                        //直播已结束，清除房间信息
                        $("#room_"+roomId).remove();
                    }else{
                        $("#screenshot_"+roomId).attr("src",result.data.screenshotUrl);
                    }
                }
            });
        }
        function settingLiveOfflineDlg(id){
            $("#roomId").val(id);
            $("#dlg2").dialog('open').dialog('setTitle', "下线禁播");
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
                type: 'get',
                data: {roomId: id,message:mess,expireMin:expire},
                async: false, //默认为true 异步
                error: function () {
                    showMessage("提示信息", "下线失败，请重试！");
                },
                success: function (result) {
                    if (result == "success") {
                        showMessage("提示信息", "下线成功！");
                        $('#dlg2').dialog('close');
                        $("#room_"+id).remove();
                    } else {
                        showMessage("提示信息", "下线失败，请重试！");
                    }
                }
            });
        }
        function closeAccount(roomId){
            if(confirm("确定要执行封号操作吗？")){
                $.ajax({
                    url: 'check/closeAccount',
                    type: 'get',
                    data: {roomId: roomId},
                    async: false, //默认为true 异步
                    error: function () {
                        showMessage("提示信息", "封号失败，请重试！");
                    },
                    success: function (result) {
                        if (result == "success") {
                            showMessage("提示信息", "封号成功！");
                            $("#room_"+roomId).remove();
                        } else {
                            showMessage("提示信息", "封号失败，请重试！");
                        }
                    }
                });
            }
        }

        function getHistoryScreenshot(roomId) {
            var title = '请求明细';
            var url = 'check/historyTemplate?roomId=' + roomId;
            initWindow(title, url, 720, 650);
        }
        
        function checkPics(idType,checkType) {
            $.ajax({
                url: 'check/checkPics',
                type: 'post',
                data: {idType: idType,checkType: checkType},
                async: false, //默认为true 异步
                error: function (result) {
                    showMessage("提示信息", "操作失败，请重试！");
                    window.location.reload();
                },
                success: function (result) {
                    if (result.resultCode == "success") {
                        showMessage("提示信息", "通过操作成功！");
                        window.location.reload();
                        //$('#dlg2').dialog('close');
                        //$("#room_"+id).remove();
                    } else {
                        showMessage("提示信息", "操作失败，请重试！");
                        window.location.reload();
                    }
                }
            });
        }
        
        function checkAllPics(checkType) {
        	
        	var  idType=$(".allcheck:checked")
        	var _list = [];

            for (var i = 0; i < idType.length; i++) {
                _list[i] = idType[i].value;
               // alert(_list[i]);
            }

            //alert(_list.length);
        	
            $.ajax({
                url: 'check/checkPics',
                type: 'get',
                data: {"idType":_list,checkType: checkType},
                traditional: true,
                async: false, //默认为true 异步
                error: function (result) {
                    showMessage("提示信息", "操作失败，请重试！");
                    window.location.reload();
                },
                success: function (result) {
                    if (result.resultCode == "success") {
                        showMessage("提示信息", "通过操作成功！");
                        window.location.reload();
                        //$('#dlg2').dialog('close');
                        //$("#room_"+id).remove();
                    } else {
                        showMessage("提示信息", "操作失败，请重试！");
                        window.location.reload();
                    }
                }
            });
        }
        
		function checkAll(obj) {
			
			var ls = document.getElementsByName("idType");
            for (var i = 0; i < ls.length; i++) {
                //if (obj.is(':checked')) {
               	if (obj.checked==true) {
                    ls[i].checked = true;
                } else {
                    ls[i].checked = false;
                }
            }
            /*
        	if(obj){
        		alert(obj.checked==true);
        		if(obj.checked==true){
        			$(".allcheck").attr("checked",true);      
//         			$(".allcheck").each(function() {
//         		        $(this).attr('checked', true);
//         			});

            	}else{
            		$(".allcheck").attr("checked",false);
//             		$(".allcheck").each(function() {
//         		        $(this).attr('checked', false);
//         			});
            	}
        	}
            */
        }
		
		
        function changePage(page, rows) {
            $("#a").attr("action", "check/checkPicList?page=" + page+"&rows="+rows);
            $("#a").attr("method", "POST");
            $("#a").submit();
        }
        
        function chgStat() {
        	var id = $("#userId").val();
			var reason = $("#message").val();
			var picType = $("#picType").val();
            $.post('ruser/chgstat', {uid: id, stat: 2,reason:reason}, function (result) {
                if (result["success"] == true) {
                	var cValue=id+"_"+picType;
                	checkPics(cValue,'no');
                    window.location.reload();
                } else {
                    showMessage("Error", result["message"]);
                }
            });
        }

		function chgStatDialog(id,picType){
			$("#userId").val(id);
			$("#picType").val(picType);
			$("#dlg2").dialog('open').dialog('setTitle', "封号");
		}

    </script>
</head>
<body>
<!-- 列表 -->
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<table>
	<tr>
		<td>
		
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
        	<a href="check/checkPicList" class="bc"  style="text-decoration: none;color: #000;">图片审核</a>
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<a href="check/checkPic"  style="text-decoration: none;color: #000;">特定图片</a>
        
        
        
        </h2>
    </div>
</div>

<div id="dataGridToolbar1" region="north" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false" style="height: 40px; padding-top: 5px; overflow: hidden;">
        <div class="userListVedioBox" style="text-align:left;" id="videos">
        <span id="loadNex" class="loadNext" onclick="checkAll()">
           	<input id="checkAll" type="checkbox" name="checkAll" onclick="checkAll(this)" value="" />全选
        </span>
        <span id="loadNex" class="loadNext" >
        	 <a href="javascript:;" onclick="checkAllPics('yes')" class="easyui-linkbutton" iconCls="icon-remove">
               	 通过
             </a>
        </span>
        <span id="loadNex" class="loadNext" >
           	<a href="javascript:;" onclick="checkAllPics('no')" class="easyui-linkbutton" iconCls="icon-cancel">
                  	不通过
            </a>
        </span>
         <span style="margin-left: 40px;">
			              	  待审核图片总数：${total }
			            </span>
    </div>
    </div>
</div>

<form id="a" name="a" action="">
	    <div  style="text-align:center;" id="page">
	    	<c:if test="${page !=1 }">
	        <span id="loadNex" class="loadNext" onclick="changePage('${page-1}',50)">
	           	上一页
	        </span>
	        </c:if>
	        <span id="loadNex" class="loadNext" onclick="changePage('${page+1}',50)">
	           	下一页
	        </span>
	    </div>
	</form>	

<div style="display: block;" >
<div class="basic" style="margin-top: 10px;">
    <c:forEach items="${picList}" var="vo" varStatus="status">
    	<c:if test="${ not empty vo.id}">
        <div class="room" id="room_${vo.id}" style="border: 1px solid #ccc;margin: 10px;height: 300px;">
                		<span >
                			<input id="id_${vo.id}" type="checkbox" name="idType" class="allcheck" value="${vo.id}_${vo.picType}"  />
			                <c:if test="${vo.picType eq 'head' }">&nbsp;&nbsp;&nbsp;&nbsp;头像</c:if>
			                <c:if test="${vo.picType eq 'home' }">&nbsp;&nbsp;&nbsp;&nbsp;频道背景图</c:if>
			            </span>
			            <span class=""  >
			            <div style="display: inline;" >
					                
			                <c:if test="${vo.picType eq 'head' }">
			                	<img src="/restadmin/download/${vo.pic}" id="screenshot_${vo.id}" data="${vo.id}" style="width: 180px;height: 180px;margin: 10px;" class="roomImage"/>
			                </c:if>
			                <c:if test="${vo.picType eq 'home' }">
			                	<img src="/restadmin/download/${vo.homePic}" id="screenshot_${vo.id}" data="${vo.id}" style="width: 180px;height: 180px;margin: 10px;" class="roomImage"/>
			                </c:if>
					                
			            </div>
			            </span>
			            <span>
			                <table style="margin-left: 30px;">
			                    <tr>
			                        <td style="" >
			                            <label>&nbsp;主播ID:</label>
			                        </td>
			                        <td>
			                            ${vo.id}
			                        </td>
			                    </tr>
			                    <tr>
			                        <td>
			                            <label>昵称:</label>
			                        </td>
			                        <td>
			                            ${vo.name}
			                        </td>
			                    </tr>
			                </table>
			            </span>
			            <span style="margin-left: 10px;">
			                <a href="javascript:;" onclick="checkPics('${vo.id}_${vo.picType}','yes')" class="easyui-linkbutton" iconCls="icon-remove">
			                  	 通过
			                </a>
			                <a href="javascript:;" onclick="checkPics('${vo.id}_${vo.picType}','no')" class="easyui-linkbutton" iconCls="icon-cancel">
			                    	不通过
			                </a>
			                <a href="javascript:;" onclick="chgStatDialog('${vo.id}','${vo.picType}')" class="easyui-linkbutton" iconCls="icon-cancel">
			                    	封号
			                </a>
			            </span>
			             
        </div>
        </c:if>
    </c:forEach>
</div>
</td>
</tr>
<tr>
<td>
</div>
<div data-options="" style="height: 40px; padding-top: 5px; overflow: hidden;">
        <div class="userListVedioBox" style="text-align:left;" id="videos1">
        <span id="loadNex" class="loadNext" onclick="checkAll()">
           	<input id="checkAll" type="checkbox" name="checkAll" onclick="checkAll(this)" value="" />全选
        </span>
        <span id="loadNex" class="loadNext" >
        	 <a href="javascript:;" onclick="checkAllPics('yes')" class="easyui-linkbutton" iconCls="icon-remove">
               	 通过
             </a>
        </span>
        <span id="loadNex" class="loadNext" >
           	<a href="javascript:;" onclick="checkAllPics('no')" class="easyui-linkbutton" iconCls="icon-cancel">
                  	不通过
            </a>
        </span>
        <span style="margin-left: 40px;">
           	  待审核图片总数：${total }
         </span>
    </div>
</div>
		</td>
	</tr>
</table>
<c:import url="/main/common.jsp"/>


<!-- 弹出的下线对话框 -->
<div id="dlg2" class="easyui-dialog" style="width:390px;height:400px;padding:10px 20px" closed="true"
     buttons="#dlg2_buttons">
    <!-- 列表上面的按钮和搜索条件  -->
    <div id="tb2" style="padding:5px;height:auto">
            <input type="hidden" id="userId" name="userId">
			<input type="hidden" id="picType" name="picType">
            <div>
                封号原因：<br/>
                <input type="radio" name="reason" onclick="$('#message').val('用户头像及封面涉黄')">用户头像及封面涉黄<br/>
                <input type="radio" name="reason" onclick="$('#message').val('用户头像及封面尺度过大')">用户头像及封面尺度过大<br/>
                <input type="radio" name="reason"  onclick="$('#message').val('用户头像及封面血腥暴力')">用户头像及封面血腥暴力<br/>
				<input type="radio" name="reason" onclick="$('#message').val('头像含二维码')">头像含二维码<br/>
				<input type="radio" name="reason" onclick="$('#message').val('用户头像及封面涉及吸烟')">用户头像及封面涉及吸烟<br/>
				<input type="radio" name="reason"  onclick="$('#message').val('用户昵称低俗')">用户昵称低俗<br/>
				<input type="radio" name="reason" onclick="$('#message').val('用户昵称或签名宣传其它平台')">用户昵称或签名宣传其它平台<br/>
				<input type="radio" name="reason" onclick="$('#message').val('服装设计党徽，国徽，军徽，警徽')">服装设计党徽，国徽，军徽，警徽<br/>
				<input type="radio" name="reason"  onclick="$('#message').val('昵称或头像存在广告')">昵称或头像存在广告<br/>
				<input type="radio" name="reason" onclick="$('#message').val('未成年封面')">未成年封面<br/>
				<input type="radio" name="reason" onclick="$('#message').val('头像涉及政治、宗教内容')">头像涉及政治、宗教内容<br/>
                <input class="easyui-validatebox" data-options="lenght:20" id="message" name="message" required="true">
            </div>
    </div>
</div>
		
<!-- 添加对话框里的保存和取消按钮 -->
<div id="dlg2_buttons">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" onclick="chgStat();">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="javascript:$('#dlg2').dialog('close')">取消</a>
</div>

</body>
</html>
