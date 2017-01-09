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
            //$("#passworddlg-buttons").attr("style","display:none;")
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
                type: 'get',
                data: {idType: idType,checkType: checkType},
                async: false, //默认为true 异步
                error: function () {
                    showMessage("提示信息", "操作失败，请重试！");
                    window.location.reload();
                },
                success: function (result) {
                    if (result == "success") {
                        showMessage("提示信息", "下操作成功！");
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
                error: function () {
                    showMessage("提示信息", "操作失败，请重试！");
                    window.location.reload();
                },
                success: function (result) {
                    if (result == "success") {
                        showMessage("提示信息", "下操作成功！");
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
        
        
        function doSearch() {
            //var queryParams = $('#displayTable').datagrid('options').queryParams;
            //queryParams.userKey = $('#userKey').combobox("getValue");
            var userKey = $('#userKey').combobox("getValue");
            //queryParams.userKeyword = $('#userKeyword').val();
            var userKeyword = $('#userKeyword').val();
            //$('#displayTable').datagrid({url: "check/checkPic",	userKey:userKey,userKeyword:userKeyword});
            $("#a").attr("action", "check/checkPic");
            $("#a").attr("method", "POST");
            $("#a").submit();
        }
         

    </script>
</head>
<body>
<!-- 列表 -->
<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"
       style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;">
</table>
<!-- 列表上面的按钮和搜索条件  -->
<div id="dataGridToolbar" region="north" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">
        	<a href="check/checkPicList"  style="text-decoration: none;color: #000;">图片审核</a>
        	
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;特定图片
        
        
        
        </h2>
    </div>
    <form  id="a" name="a">
    <div>
		<span>
            <label>
                <select class="easyui-combobox" name="userKey" id="userKey">
                    <option value="">用户搜索</option>
                    <option value="1" <c:if test='${userKey eq 1 }'> selected='selected' </c:if> >用户ID</option>
                    <option value="2" <c:if test='${userKey eq 2 }'> selected='selected' </c:if> >手机号码</option>
                    <option value="3" <c:if test='${userKey eq 3 }'> selected='selected' </c:if> >昵称</option>
                </select>
            </label>
        </span>
        <span>
            <input type="text" name="userKeyword" id="userKeyword" class="easyui-validatebox" value="${userKeyword}" >
        </span>
            <span>
                <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                   iconCls="icon-search" style="">搜索</a>
        	</span>
    </div>
</div>
</form>



<div style="display: block;" >
<div class="basic" style="margin-top: 10px;">
	<c:if test="${not empty vo }">
    <div class="room" id="room_${vo.id}" style="border: 1px solid #ccc;margin: 10px;height: 300px;">
                		<span >
			               	 头像	
			            </span>
			            <span class=""  >
			            <div style="display: inline;" >
					                
			                <img src="/restadmin/download/${vo.pic}" id="screenshot_${vo.id}" data="${vo.id}" style="width: 180px;height: 180px;margin: 10px;" class="roomImage"/>
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
			            <span style="margin-left: 40px;">
			                <a href="javascript:;" onclick="checkPics('${vo.id}_head','yes')" class="easyui-linkbutton" iconCls="icon-remove">
			                  	 通过
			                </a>
			                <a href="javascript:;" onclick="checkPics('${vo.id}_head','no')" class="easyui-linkbutton" iconCls="icon-cancel">
			                    	不通过
			                </a>
			            </span>
        </div>
        <div class="room" id="room_${vo.id}" style="border: 1px solid #ccc;margin: 10px;height: 300px;">
                		<span >
			                	频道背景图
			            </span>
			            <span class=""  >
			            <div style="display: inline;" >
			                	<img src="/restadmin/download/${vo.homePic}" id="screenshot_${vo.id}" data="${vo.id}" style="width: 180px;height: 180px;margin: 10px;" class="roomImage"/>
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
			            <span style="margin-left: 40px;">
			                <a href="javascript:;" onclick="checkPics('${vo.id}_home','yes')" class="easyui-linkbutton" iconCls="icon-remove">
			                  	 通过
			                </a>
			                <a href="javascript:;" onclick="checkPics('${vo.id}_home','no')" class="easyui-linkbutton" iconCls="icon-cancel">
			                    	不通过
			                </a>
			            </span>
        </div>
        </c:if>
        
        <c:if test="${not empty userPicList }">
        	<c:forEach items="${userPicList }" var="vo" >
        		<div class="room" id="room_${vo.id}" style="border: 1px solid #ccc;margin: 10px;height: 300px;">
		                		<span >
					               	 头像	
					            </span>
					            <span class=""  >
					            <div style="display: inline;" >
							                
					                <img src="/restadmin/download/${vo.pic}" id="screenshot_${vo.id}" data="${vo.id}" style="width: 180px;height: 180px;margin: 10px;" class="roomImage"/>
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
					            <span style="margin-left: 40px;">
					                <a href="javascript:;" onclick="checkPics('${vo.id}_head','yes')" class="easyui-linkbutton" iconCls="icon-remove">
					                  	 通过
					                </a>
					                <a href="javascript:;" onclick="checkPics('${vo.id}_head','no')" class="easyui-linkbutton" iconCls="icon-cancel">
					                    	不通过
					                </a>
					            </span>
		        </div>
		        <div class="room" id="room_${vo.id}" style="border: 1px solid #ccc;margin: 10px;height: 300px;">
		                		<span >
					                	频道背景图
					            </span>
					            <span class=""  >
					            <div style="display: inline;" >
					                	<img src="/restadmin/download/${vo.homePic}" id="screenshot_${vo.id}" data="${vo.id}" style="width: 180px;height: 180px;margin: 10px;" class="roomImage"/>
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
					            <span style="margin-left: 40px;">
					                <a href="javascript:;" onclick="checkPics('${vo.id}_home','yes')" class="easyui-linkbutton" iconCls="icon-remove">
					                  	 通过
					                </a>
					                <a href="javascript:;" onclick="checkPics('${vo.id}_home','no')" class="easyui-linkbutton" iconCls="icon-cancel">
					                    	不通过
					                </a>
					            </span>
		        </div>
        	</c:forEach>
        </c:if>
</div>

				
</div>



</body>
</html>
