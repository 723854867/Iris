<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="background: white">
<head>
    <title>LIVE用户</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>

    <script type="text/javascript" src="js/admin/query_user.js"></script>
</head>
<body>
<table style="width: 100%;border-top:1px solid #000;border-left:1px solid #000;" id="displayTable">
</table>
<div id="dataGridToolbar" region="north" border="false"
     style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
    <div data-options="region:'north',border:false"
         style="height: 40px; padding-top: 5px; overflow: hidden;">
        <h2 style="float:left;padding-left:10px;margin: 1px">Blive用户</h2>
    </div>
    <input type="hidden" id="iosChannel" value="${iosChannel}">
    <input type="hidden" id="h5Channel" value="${h5Channel}">
    <input type="hidden" id="androidChannel" value="${androidChannel}">
    <div class="easyui-tabs">
        <div id="search" title="搜索" style="margin-left: 10px;">
            <form id="searchForm">
        <span>
            <label>
                <select class="easyui-combobox" name="user" id="user">
                    <option value="">用户搜索</option>
                    <option value="1">用户ID</option>
                    <option value="2">用户名</option>
                    <option value="3">昵称</option>
                    <option value="4">手机号码</option>
                </select>
            </label>
        </span>
        <span>
            <input type="text" name="userKeyword" id="userKeyword" class="easyui-validatebox">
        </span>
        <span>
            <label>
                状态：
            </label>
            <span>
                <select id="query_stat" name="stat" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="0">已激活</option>
                    <option value="1">禁言</option>
                    <option value="2">封号</option>
                </select>
            </span>
        </span>
        <span>
        	<label>等级：</label>
        	<span>
                <select id="query_vstat" name="vipStat" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="0">普通</option>
                    <option value="1">蓝V</option>
                    <option value="2">黄V</option>
                    <option value="3">绿V</option>
                </select>
            </span>
         </span>
         <span>
            <label>排行榜：</label>
            <span>
                <select id="query_rankable" name="rankAble" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="0">允许</option>
                    <option value="1">禁止</option>
                </select>
            </span>
         </span>
         <span>
            <label>性别：</label>
            <span>
                <select id="query_sex" name="sex" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="1">男</option>
                    <option value="0">女</option>
                    <option value="2">未知</option>
                </select>
			</span>
        </span>
        <span>
            <label>注册来源：</label>
            <span>
                <select id="queryRegPlatform" name="regPlatform">
                    <option value="" selected="selected">请选择</option>
                    <option value="android">android</option>
                    <option value="ios">ios</option>
                    <option value="h5">h5</option>
                </select>
			</span>
        </span>
        <span>
            <label>渠道：</label>
            <span id="selectContent">
                <select id="queryRegPlatformChannel" name="regPlatformChannel" class="easyui-combobox">
                    <option value="">请选择</option>
                </select>
			</span>
        </span>
        <span>
            <label>允许后台发布：</label>
            <span>
                <select id="queryAllowPublish" name="allowPublish" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="1">禁止</option>
                    <option value="0">允许</option>
                </select>
			</span>
        </span>
        <span>
            <label>是否主播：</label>
            <span>
                <select id="queryIsAnchor" name="isAnchor" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="1">是</option>
                    <option value="0">否</option>
                </select>
			</span>
        </span>
            <span>
            <label>注册方式：</label>
            <span>
                <select id="thirdFrom" name="thirdFrom" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="qq">QQ</option>
                    <option value="wechat">微信</option>
                    <option value="sina">微博</option>
                    <option value="busonline">手机号</option>
                </select>
			</span>
        </span>
            <span>
            <label>所在地：</label>
            <span>
                <input type="text" name="addr" id="query_addr" value="">
            </span>
        </span>
        <span>
            <label>推荐位：</label>
            <span>
                <select id="recommendBit1" name="recommendBit1" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="0">允许</option>
                    <option value="1">不允许</option>
                    
                </select>
			</span>
        <span>
            <label>机构：</label>
            <span>
                <select class="easyui-combobox" name="organizationId" id="searchOrganizationId"
                        data-options="url:'comboBox/getOrganizationCombobox',method: 'get',valueField:'id',textField:'text'"/>
			</span>
        </span>
            <%--<span>
                <label>渠道：</label>
                <span>
                    <select id="queryRegPlatformTest" class="easyui-combobox">
                        <option value="">请选择</option>
                        <option value="opera">欧朋</option>
                    </select>
                </span>
            </span>--%>

        <span>
             <label>上传视频数：</label>
             <span>
                 <input type="text" style="width: 90px;" name="startCount" id="startCount">
                 &nbsp;至&nbsp;
                 <input type="text" style="width: 90px;" name="endCount" id="endCount">
             </span>
         </span>
     	 <span>
            <label>注册时间：</label>
            <span>

                <input type="text" class="easyui-datebox" style="width: 120px;" name="startTime" id="startTime">
                &nbsp;至&nbsp;
                <input type="text" class="easyui-datebox" style="width: 120px;" name="endTime" id="endTime">
            </span>
         </span>
         
          <span>
         	<label>平台：</label>
            <label>
                <select class="easyui-combobox" name="platform" id="platform">
                    <option value="">全部</option>
                    <option value="ios">IOS</option>
                    <option value="android">Android</option>
                </select>
            </label>
        </span>
        <span>
        	<label>版本：</label>
            <label>
                <select class="easyui-combobox" name="appVersion" id="appVersion">
                    <option value="">全部</option>
                    <c:forEach items="${appVersionList }" var="v" >
                    	<option value="${v }">${v }</option>
                    </c:forEach>
                </select>
            </label>
        </span>
        
         <span>
                <a href="javascript:;" onclick="doSearch()" class="easyui-linkbutton"
                   iconCls="icon-search" style="">搜索</a>
        	</span>
             <span>
                 <a href="javascript:;" onclick="exportWopaiNormalUser()" class="easyui-linkbutton"
                    iconCls="icon-print" style="">导出</a>
         </span>
            </form>
        </div>
        <div id="vipExport" style="margin-left: 10px;" title="VIP用户导出">
        <span>
        	<label>等级：</label>
        	<span>
                <select id="vipStat" name="vipStat" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="1">蓝V</option>
                    <option value="2">黄V</option>
                    <option value="3">绿V</option>
                </select>
            </span>
         </span>
            <a href="javascript:;" onclick="exportWopaiUser()" class="easyui-linkbutton"
               iconCls="icon-print" style="">导出</a>
        </div>
        <%--<div id="normalExport" style="margin-left: 10px;" title="普通用户导出">
            <form id="normalExportForm" method="post">
        <span>
        	<label>等级：</label>
        	<span>
                <select id="export_vstat" name="vstat" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="0">普通</option>
                    <option value="1">蓝V</option>
                    <option value="2">黄V</option>
                    <option value="3">绿V</option>
                </select>
            </span>
         </span>
         &lt;%&ndash;<span>
            <label>
                <select class="easyui-combobox" name="exportUser" id="exportUser">
                    <option value="">用户搜索</option>
                    <option value="1">用户ID</option>
                    <option value="2">用户名</option>
                    <option value="3">昵称</option>
                    <option value="4">手机号码</option>
                </select>
            </label>
        </span>
        <span>
            <input type="text" name="exportUserKeyword" id="exportUserKeyword" class="easyui-validatebox">
        </span>&ndash;%&gt;
        <span>
            <label>
                状态：
            </label>
            <span>
                <select id="export_stat" name="stat" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="0">已激活</option>
                    <option value="1">禁言</option>
                    <option value="2">封号</option>
                </select>
            </span>
        </span>
         <span>
            <label>排行榜：</label>
            <span>
                <select id="export_rankable" name="rankable" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="0">允许</option>
                    <option value="1">禁止</option>
                </select>
            </span>
         </span>
         <span>
            <label>性别：</label>
            <span>
                <select id="export_sex" name="sex" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="1">男</option>
                    <option value="0">女</option>
                    <option value="2">未知</option>
                </select>
			</span>
        </span>
        <span>
            <label>注册来源：</label>
            <span>
                <select id="exportRegPlatform" name="regPlatform" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="android">Android</option>
                    <option value="ios">IOS</option>
                    <option value="h5">Html5</option>
                </select>
			</span>
        </span>
        <span>
            <label>渠道：</label>
            <span>
                <select id="exportRegPlatformChannel" name="regPlatformChannel" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="h5-opera">欧朋</option>
                </select>
			</span>
        </span>
        <span>
            <label>允许后台发布：</label>
            <span>
                <select id="exportAllowPublish" name="allowPublish" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="1">禁止</option>
                    <option value="0">允许</option>
                </select>
			</span>
        </span>
        <span>
            <label>是否主播：</label>
            <span>
                <select id="exportIsAnchor" name="isAnchor" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="1">是</option>
                    <option value="0">否</option>
                </select>
			</span>
        </span>
                <span>
            <label>注册方式：</label>
            <span>
                <select name="thirdFrom" class="easyui-combobox">
                    <option value="">请选择</option>
                    <option value="qq">QQ</option>
                    <option value="wechat">微信</option>
                    <option value="sina">微博</option>
                    <option value="busonline">手机号</option>
                </select>
			</span>
        </span>
                <span>
            <label>所在地：</label>
            <span>
                <input type="text" id="export_addr" name="addr" value="">
            </span>
        </span>
        <span>
            <label>机构：</label>
            <span>
                <select class="easyui-combobox" name="organizationId" id="searchOrganizationId"
                        data-options="url:'comboBox/getOrganizationCombobox',method: 'get',valueField:'id',textField:'text'"/>
			</span>
        </span>

                &lt;%&ndash;<span>
                    <label>渠道：</label>
                    <span>
                        <select id="queryRegPlatformTest" class="easyui-combobox">
                            <option value="">请选择</option>
                            <option value="opera">欧朋</option>
                        </select>
                    </span>
                </span>&ndash;%&gt;

        <span>
             <label>上传视频数：</label>
             <span>
                 <input type="text" style="width: 90px;" id="exportStartCount" name="startCount">
                 &nbsp;至&nbsp;
                 <input type="text" style="width: 90px;" id="exportEndCount" name="endCount">
             </span>
         </span>
     	 <span>
            <label>注册时间：</label>
            <span>

                <input type="text" class="easyui-datebox" style="width: 120px;" name="startTime" id="exportStartTime">
                &nbsp;至&nbsp;
                <input type="text" class="easyui-datebox" style="width: 120px;" name="endTime" id="exportEndTime">
            </span>
         </span>
        <span>
            <label>上传视频时间：</label>
            <span>

                <input type="text" class="easyui-datebox" style="width: 120px;" name="startDate" id="startDate">
                &nbsp;至&nbsp;
                <input type="text" class="easyui-datebox" style="width: 120px;" name="endDate" id="endDate">
            </span>
         </span>
        <span>
            <label>条数：</label>
            <span>
                <input type="text" class="easyui-textbox" style="width: 120px;" name="exportCount" id="exportCount">
            </span>
         </span>
         <span>
                 <a href="javascript:;" onclick="exportWopaiNormalUser()" class="easyui-linkbutton"
                    iconCls="icon-print" style="">导出</a>
         </span>
            </form>
        </div>--%>
    </div>
    <div style="margin-left: 10px;margin-top: 5px;">
        <span><a href="javascript:;" onclick="showRealFans()" id="showRealFans"
                 class="easyui-linkbutton">显示铁粉</a></span>
    </div>
    <%--    <div>
             <span>
                <label>最后登录时间：</label>
                <span>
                    <input type="text" class="easyui-datebox" style="width: 120px;" name="loginStartTime"
                           id="loginStartTime">
                    &nbsp;至&nbsp;
                    <input type="text" class="easyui-datebox" name="loginEndTime" id="loginEndTime">
                </span>
              </span>
        </div>--%>
</div>

<!--等级状态修改弹出框start-->
<div id="dialog" style="padding:5px;width:500px;height:370px;display:none;"
     title="快捷编辑" toolbar="#dlg-toolbar" buttons="#dlg-buttons">
    <table class="table-doc" width="100%">
        <%--<tr>
            <td class="row">
                <label>状态：</label>
            </td>
            <td>
                <select id="stat" class="easyui-combobox">
                    <option value="0">已激活</option>
                    <option value="1">禁言</option>
                    <option value="2">封号</option>
                </select>
            </td>
        </tr>--%>
        <tr>
            <td class="row">
                <label>等级：</label>
            </td>
            <td>
                <select id="vstat" class="easyui-combobox">
                    <option value="0">普通</option>
                    <option value="1">蓝V</option>
                    <option value="2">黄V</option>
                    <option value="3">绿V</option>
                </select>
                <input type="hidden" id="id" name="id" value="">
            </td>
        </tr>
        <tr>
            <td class="row">
                <label>允许后台发布：</label>
            </td>
            <td>
                <select id="allow_publish" class="easyui-combobox">
                    <option value="1">禁止</option>
                    <option value="0">允许</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="row"><label>VIP权重：</label></td>
            <td>
                <input type="text" id="vipWeight" name="vipWeight" value="" class="easyui-validatebox">
            </td>
        </tr>
        <tr>
            <td class="row"><label>直播权重：</label></td>
            <td>
                <input type="text" id="liveWeight" name="liveWeight" value="" class="easyui-validatebox">
            </td>
        </tr>
        <tr>
            <td class="row">
                <label>机构：</label>
            </td>
            <td>
                <select class="easyui-combobox" name="organizationId" id="organizationId"
                        data-options="url:'comboBox/getOrganizationCombobox',method: 'get',valueField:'id',textField:'text'"/>

            </td>
        </tr>
<%--        <tr>
            <td class="row">
                <label>允许提现：</label>
            </td>
            <td>
                <select id="isBlacklist" class="easyui-combobox">
                    <option value="1">是</option>
                    <option value="0">否</option>
                </select>
            </td>
        </tr>--%>
        <tr>
            <td class="row">
                <label>直播方式：</label>
            </td>
            <td>
                <select id="liveType" class="easyui-combobox">
                    <option value="0">手机</option>
                    <option value="1">摄像机</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="row">
                <label>推荐位：</label>
            </td>
            <td>
                <select id="recommendBit"  name="recommendBit"  class="easyui-combobox">
                    <option value="0">允许</option>
                    <option value="1">不允许</option>
                </select>
            </td>
        </tr>
        <%--<tr>
            <td class="row"><label>设备号：</label></td>
            <td>
                <input type="text" id="deviceId" name="deviceId" value="" class="easyui-validatebox">
            </td>
        </tr>
        <tr>
            <td class="row"><label>播放地址：</label></td>
            <td>
                <input type="text" id="streamUrl" name="streamUrl" value="" class="easyui-validatebox">
            </td>
        </tr>--%>
    </table>
</div>
<!--等级状态修改弹出框end-->

<!--更新 start-->
<%--<div id="updateDialog" style="padding:5px;width:420px;height:350px;display:none;line-height: 35px;"
     title="更新" toolbar="#dlg-toolbar" buttons="#dlg-buttons">
    <form action="/user/updateRuser" method="post">
        <div>
            <span>马甲名：</span>
    <span>
        <input type="text" require="true" class="easyui-validatebox" name="name" id="update_name">
        <input type="hidden" id="update_id" name="id">
    </span>
        </div>
        <div>
            <div style="clear: both;overflow:hidden;">
                <span>头像：</span>
                <input type="file" name="pic" id="update_pic" value="">
                <span style="width: 100px;" id="imgdiv">
                    <img style="position: absolute;top:40px;" id="imgShow" width="90" height="90"/>
                </span>
            </div>
        </div>
        <div>
            <span>性别：</span>
    <span>
        <input type="radio" require="true" name="sex" id="update_sex1" value="1">男
        <input type="radio" require="true" name="sex" id="update_sex0" value="0">女
        <input type="radio" require="true" name="sex" id="update_sex2" value="2">不男不女
    </span>
        </div>
        <div>
            <span>状态：</span>
    <span>
        <select name="stat" id="update_stat" require="true" class="easyui-combobox">
            <option value="0">已激活</option>
            <option value="1">禁言</option>
            <option value="2">封号</option>
        </select>
    </span>
        </div>
        <div>
            <span>等级：</span>
    <span>
        <select name="vstat" id="update_vstat" require="true" class="easyui-combobox">
            <option value="0">普通</option>
            <option value="1">蓝V</option>
            <option value="2">黄V</option>
            <option value="3">绿V</option>
        </select>
    </span>
        </div>
        <div>
            <span>排行榜设置：</span>
    <span>
        <select name="update_rankable" id="update_rankable" require="true" class="easyui-combobox">
            <option value="0">允许</option>
            <option value="1">禁止</option>
        </select>
    </span>
        </div>
        <div>
            <span>个性签名：</span>
    <span>
        <input class="easyui-validatebox" type="text" name="signature" id="update_signature">
    </span>
        </div>
    </form>
</div>--%>
<!--更新 end-->
<div id="dlg-buttons">
    <a href="javascript:;" class="easyui-linkbutton" id="update-ok" iconCls="icon-ok">更新</a>
    <a href="javascript:;" class="easyui-linkbutton" id="update-cancel" iconCls="icon-cancel">取消</a>
</div>
</body>
</html>