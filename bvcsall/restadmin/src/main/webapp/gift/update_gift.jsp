<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: busap
  Date: 2015/12/23
  Time: 15:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>更新礼物信息</title>
</head>
<body>
<!-- 添加礼物弹出框 start -->
<div class="easyui-layout" id="update_dlg" data-options="fit:true">
    <form id="updateForm" method="post" enctype="multipart/form-data">
        <table class="table-doc" cellspacing="0" width="100%">
            <tr>
                <td style="text-align: right;">礼物名称：</td>
                <td style="text-align: left;">
                    <input type="text" name="name" id="name" style="width: 200px;" class="easyui-validatebox" value="${gift.name}">
                    <input type="hidden" name="id" id="id" style="width: 200px;" class="easyui-validatebox" value="${gift.id}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">礼物icon：</td>
                <td style="text-align: left;">
                    <input type="file" name="giftIconUrl" id="giftIconUrl" accept="jpg,png,gif"
                           class="easyui-validatebox">
                    <img src="/restadmin/download/${gift.giftIconUrl}" style="width: 30px;height: 30px;">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">礼物用途：</td>
                <td style="text-align: left;">
                    <select name="giftPurpose" id="giftPurpose" style="width: 200px;"
                            class="easyui-validatebox easyui-combobox">
                        <option value="1" <c:if test="${gift.giftPurpose eq 1}">selected="selected"</c:if>>收视榜</option>
                        <option value="2" <c:if test="${gift.giftPurpose eq 2}">selected="selected"</c:if>>人气榜</option>
                        <option value="3" <c:if test="${gift.giftPurpose eq 3}">selected="selected"</c:if>>直播</option>
                        <option value="4" <c:if test="${gift.giftPurpose eq 4}">selected="selected"</c:if>>其它</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">效果类型：</td>
                <td style="text-align: left;">
                    <select name="effectType" id="effectType" style="width: 200px;"
                            class="easyui-validatebox easyui-combobox">
                        <option value="0" <c:if test="${gift.effectType eq 0}">selected="selected"</c:if>>无动画</option>
                        <option value="1" <c:if test="${gift.effectType eq 1}">selected="selected"</c:if>>雪花</option>
                        <option value="2" <c:if test="${gift.effectType eq 2}">selected="selected"</c:if>>心跳放大</option>
                        <option value="3" <c:if test="${gift.effectType eq 3}">selected="selected"</c:if>>位移</option>
                        <option value="5" <c:if test="${gift.effectType eq 5}">selected="selected"</c:if>>雪花停留</option>
                        <option value="6" <c:if test="${gift.effectType eq 6}">selected="selected"</c:if>>抛物线</option>
                        <option value="7" <c:if test="${gift.effectType eq 7}">selected="selected"</c:if>>跑车</option>
                        <option value="8" <c:if test="${gift.effectType eq 8}">selected="selected"</c:if>>城堡岛屿</option>
                        <option value="9" <c:if test="${gift.effectType eq 9}">selected="selected"</c:if>>游轮</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">效果文件：</td>
                <td style="text-align: left;">
                    <input type="file" name="effectFileUrl" id="effectFileUrl" class="easyui-validatebox"
                           accept="jpg,png,gif">
                    <c:if test="${!empty(gift.effectFileUrl)}">
                        <img src="/restadmin/download/${gift.effectFileUrl}" style="width: 30px;height: 30px;">
                    </c:if>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">价格：</td>
                <td style="text-align: left;">
                    <input type="text" name="price" id="price" style="width: 200px;" class="easyui-validatebox"
                           value="${gift.price}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">点数（拍豆）：</td>
                <td style="text-align: left;">
                    <input type="text" name="point" id="point" style="width: 200px;" class="easyui-validatebox"
                           value="${gift.point}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">状态：</td>
                <td style="text-align: left;">
                    <select name="state" id="state" style="width: 200px;" class="easyui-validatebox easyui-combobox">
                        <option value="1" <c:if test="${gift.state eq 1}">selected="selected"</c:if>>上架</option>
                        <option value="0" <c:if test="${gift.state eq 0}">selected="selected"</c:if>>下架</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">权重：</td>
                <td style="text-align: left;">
                    <input type="text" name="weight" style="width: 200px;" id="weight" class="easyui-validatebox"
                           value="${gift.weight}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">标记状态：</td>
                <td style="text-align: left;">
                    <select name="markerState" id="markerState" style="width: 200px;" class="easyui-combobox">
                        <option value="normal" <c:if test="${gift.markerState eq 'normal'}">selected="selected"</c:if>>正常</option>
                        <option value="new" <c:if test="${gift.markerState eq 'new'}">selected="selected"</c:if>>new</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">是否免费：</td>
                <td style="text-align: left;">
                    <input type="radio" name="isFree" disabled="disabled" <c:if test="${gift.isFree eq 0}">checked="checked"</c:if> class="easyui-validatebox" value="0" onclick="freeCountDisplay(0)">不免费
                    <input type="radio" name="isFree" disabled="disabled" <c:if test="${gift.isFree eq 1}">checked="checked"</c:if> class="easyui-validatebox" onclick="freeCountDisplay(1)" value="1">免费
                </td>
            </tr>
            <tr id="freeCountTr" <c:if test="${gift.isFree eq 0}">style="display: none;"</c:if>>
                <td style="text-align: right;">免费次数：</td>
                <td style="text-align: left;">
                    <input type="text" name="freeCount" disabled="disabled" id="freeCount" class="easyui-numberbox" min="0" max="1000" precision="0" missingMessage="请填写大于0小于1000的整数" value="${gift.freeCount}">
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">支持连发：</td>
                <td style="text-align: left;">
                    <input type="radio" name="loopSupport" <c:if test="${gift.loopSupport eq 0}">checked="checked"</c:if> class="easyui-validatebox" value="0">不支持
                    <input type="radio" name="loopSupport" <c:if test="${gift.loopSupport eq 1}">checked="checked"</c:if> class="easyui-validatebox" value="1">支持
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">是否专属：</td>
                <td style="text-align: left;">
                    <input type="radio" name="isExclusive" disabled="disabled" <c:if test="${gift.isExclusive eq 0}">checked="checked"</c:if> class="easyui-validatebox" value="0">不是
                    <input type="radio" name="isExclusive" disabled="disabled" <c:if test="${gift.isExclusive eq 1}">checked="checked"</c:if> class="easyui-validatebox" value="1">是
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">支持截屏：</td>
                <td style="text-align: left;">
                    <input type="radio" name="screenshotSupport" <c:if test="${gift.screenshotSupport eq 0}">checked="checked"</c:if> class="easyui-validatebox" value="0">不支持
                    <input type="radio" name="screenshotSupport" <c:if test="${gift.screenshotSupport eq 1}">checked="checked"</c:if> class="easyui-validatebox" value="1">支持
                </td>
            </tr>
            <tr>
                <td style="text-align: right;">自动截屏次数：</td>
                <td style="text-align: left;">
                    <input type="text" name="screenshotNumber" id="screenshotNumber" placeholder="请输入大于0的正整数" class="easyui-numberbox" min="0" max="1000000000"
                           precision="0" missingMessage="请填写大于0的整数" value="${gift.screenshotNumber}">
                </td>
            </tr>
        </table>
    </form>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0;">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="doUpdate()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dialogWindow').dialog('close')">取消</a>
    </div>
</div>

<!-- 添加礼物弹出框 end -->
</body>
</html>
