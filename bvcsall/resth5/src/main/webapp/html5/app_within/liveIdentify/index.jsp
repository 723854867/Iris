<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
	WebClientUtils cu=WebClientUtils.getInstance();
	String cdnurl=cu.loadConfigUrl("html5", "interfaceurl");
	String imageurl=cu.loadConfigUrl("html5", "imageurl");
	String interfaceurl=cu.loadConfigUrl("html5", "interfaceurl");
	if(cdnurl==null){
		cdnurl="http://api.wopaitv.com";
	}
	if(imageurl==null){
		imageurl="http://api.wopaitv.com";
	}
	if(interfaceurl==null){
		interfaceurl="http://api.wopaitv.com";
	}
//	interfaceurl = "http://localhost:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link class="linkUrl" rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/liveIdentify/css/style.css?t=1">
	<script src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/js/libs/jquery-2.1.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/js/libs/jquery.form.js"></script>
	<script src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/js/libs/md5-32.js"></script>
	<script src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/js/libs/area.js"></script>

	<title>blive实名认证</title>
</head>
<body>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<input type="hidden" id="catalog" value="<%=request.getContextPath()%>"/>
<input type="hidden" id="uid" name="uid" value="${uid }"/>
<input type="hidden" id="access_token" name="access_token" value="${accessToken }"/>

<input class="form-data" value="${phone}" type="hidden"/>


<div class="main">
	<div class="first">
		<div class="banner">
			<img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/bg.png" class="ad" alt=""/>
			<div class="renzheng-box">
				<p class="rengzheng-tip">LIVE将会严格保密您提交的所有资料</p>
				<button class="go-renzheng go-renzheng-ok">立即认证</button>
			</div>

		</div>

	</div>
	<%--认证协议文档放置处--%>

	<%--默认第一次填写--%>
	<div class="from-box1">
		<div class="apply-status">以下信息为必填项，LIVE会严格保密您的资料信息，请放心填写。</div>
		<div class="apply-status-fail" fail-data="${anchor.rejectReason}">
			<h4 class="fial-head"><em class="fail-icon"></em>审核未通过原因 <span class="see-fail">隐藏</span></h4>
			<div class="fial-list"></div>
		</div>
		<form action="" id="from1" enctype="multipart/form-data" method="post">
			<div class="form-item">
				<span class="item-left">姓名</span>
				<span class="item-right">
					<input type="text" value="" name="realName" re="[\u4e00-\u9fa5_a-zA-Z]{2,10}" placeholder="真实姓名" class="item-txt item-txt-exsc"/>
				</span>
			</div>
			<div class="form-item">
				<span class="item-left">手机号</span>
				<span class="item-right">
					<%--<input type="number" name="phone" re="1[34578]\d{9}" placeholder="手机号码" class="item-txt item-txt-exsc"/>--%>
					<input type="text" name="phone" re="^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$" placeholder="11位手机号" value="" class="item-txt item-txt-exsc from3-phone"/>
				</span>
			</div>
			<div class="form-item phone-inspect nobor">
				<span class="item-left">验证码</span>
				<span class="item-right">
					<input type="text" re ="/^\d+$/" name="code" placeholder="" class="item-txt1 from3-code item-txt-exsc"/>
					<input type="button" value="获取验证码" class="ident-code ident-code-ok"/>
				</span>
			</div>
			<div class="form-item phone-inspect">
				<span class="item-left">设置密码</span>
				<span class="item-right">
					<input type="hidden" name="password" id="pwd1"/>
					<input type="password" name="test1" id="pwd" re="[a-zA-Z0-9]{6,20}" placeholder="设定手机号登录密码" class="item-txt item-txt-exsc"/>
				</span>
			</div>
			<div class="form-item phone-inspect mt10">
				<span class="item-left">银行卡号</span>
				<span class="item-right">
					<input type="text" re="^(\d{16}|\d{19})$" name="bankNumber" placeholder="请输入银行借记卡号" class="item-txt item-txt-exsc"/>
				</span>
			</div>
			<div class="form-item phone-inspect">
				<span class="item-left">开户地</span>
				<span class="item-right">
					<div class="sele-box">
						<span class="sele-key">省份</span>
						<em class="arrow"></em>
						<select id="s_province" name="bankProvince"></select>  
					</div>
					<div class="sele-box">
						<span class="sele-key">城市</span>
						<em class="arrow"></em>
						<select id="s_city" name="bankCity"></select>
					</div>
				</span>
			</div>
			<div class="form-item phone-inspect mt10">
				<span class="item-left">开户行</span>
				<span class="item-right">
					<div class="sele-box2">
						<span class="sele-key">开户行</span>
						<em class="arrow"></em>
						<select name="bankName" id="s_bankType" class="item-sele2 item-txt-exsc">
							<option value='请选择'>请选择</option>
							<option value='招商银行'>招商银行</option>
							<option value='中国工商银行'>中国工商银行</option>
							<option value='中国农业银行'>中国农业银行</option>
							<option value='中国银行'>中国银行</option>
							<option value='中国建设银行'>中国建设银行</option>
							<option value='交通银行'>交通银行</option>
							<option value='中信银行'>中信银行</option>
							<option value='兴业银行'>兴业银行</option>
							<option value='光大银行'>光大银行</option>
							<option value='民生银行'>民生银行</option>
							<option value='中国邮政储蓄银行'>中国邮政储蓄银行</option>
							<option value='华夏银行'>华夏银行</option>
							<option value='北京银行'>北京银行</option>
							<option value='广发银行'>广发银行</option>
						</select>
					</div>
				</span>
			</div>
			<div class="form-item phone-inspect nobor">
				<span class="item-left">支行名称</span>
				<span class="item-right"><input type="text" name="bankAddress" placeholder="支行名称" class="item-txt item-txt-exsc back-name"/>
				</span>
			</div>
			<%--<div class="form-item mt10">--%>
				<%--<span class="item-left item-select1">--%>
					<%--<h3 class="select-tit"><span class="select-word" data-type="qq">QQ号码</span> <em class="arrow-down"></em></h3>--%>
					<%--<ul class="select-list1">--%>
						<%--<li data-type="qq" re="^\d+$">QQ号码</li>--%>
						<%--<li data-type="wechat" re="">微信号</li>--%>
					<%--</ul>--%>
				<%--</span>--%>
				<%--<span class="item-right">--%>
					<%--<input type="text" name="qq" re="^\d+$" class="item-txt net-number item-txt-exsc" placeholder="QQ号码"/>--%>
				<%--</span>--%>
			<%--</div>--%>
			<div class="form-item phone-inspect mt10">
				<span class="item-left">身份类型</span>
				<span class="item-right">
					<div class="sele-box2">
						<span class="sele-key">居民身份证</span>
						<em class="arrow"></em>
						<select name="certificateType" id="s_userid" class="item-sele2 item-txt-exsc">
							<option value="1">居民身份证</option>
						</select>
					</div>

				</span>
			</div>
			<div class="form-item">
				<!-- <p class="photo-tips">请填写证件号并上传清晰的证件照片 <a href="javascript:;" class="go-cart-tips">示例图片</a></p> -->
				<span class="item-left">
					证件号
				</span>
				<span class="item-right">
					<input type="text" name="certificateNumber" value="" re="(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)" class="item-txt item-txt-exsc" placeholder="15或18位身份信息"/>
				</span>
			</div>
			<%--<div class="bottom-line"></div>--%>
			<div class="form-item " id="nopadd">
				<div class="doc-type1">
					<dl>
						<dt>
							<img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/pic1x/pic-border-icon.png" class="preview-pho1"/>
							<input type="file" name="file1" data-num="1" re="" class="file-btn file1 item-txt-exsc" accept="image/*">
						</dt>

						<dd>身份证正面</dd>
					</dl>
					<dl>
						<dt><img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/pic1x/pic-border-icon.png" class="preview-pho2"/>
							<input type="file" name="file2" data-num="2" re="" accept="image/*" class="file-btn file2 item-txt-exsc"/>
						</dt>
						<dd>身份证反面</dd>
					</dl>
					<dl>
						<dt><img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/pic1x/pic-border-icon.png" class="preview-pho3"/>
							<input type="file" name="file3" data-num="3" re="" accept="image/*" class="file-btn file3 item-txt-exsc"/>
						</dt>
						<dd>手持身份证</dd>
					</dl>
				</div>
			</div>
			<p class="shili-tips">手持身份证示例(信息要清晰可见)</p>
			<img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/img_userid.png" class="shili-pic" alt=""/>
			<div class="form-item nobor last-from-warp">
				<%--<input type="submit" class="form-submit1" value="提交申请"/>--%>
				<a href="javascript:;" class="form-submit1">提交资料</a>
			</div>
		</form>

	</div>
	<%--证件提示图片--%>
	<%--<div class="parmpt-warp skyBox">--%>
		<%--<div class="pic1"><img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/userid.png"/></div>--%>
		<%--<div class="pic2"><img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/huzhao.png"/></div>--%>
		<%--<div class="pic3"><img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/mtp.png"/></div>--%>
		<%--<button class="close-parmpt">我知道了</button>--%>
	<%--</div>--%>
	<%--审核提交成功--%>
	<div class="apply-success">
		<div class="apply-msg">
			<p class="apply-icon-right"><em class="apply-type-icon"></em></p>
			<div class="apply-msg-con">
				我们会以最快的速度审核，请耐心等待！
			</div>
		</div>
	</div>
	<%--审核提交失败--%>
	<%--<div class="apply-fail">--%>
		<%--<div class="apply-msg">--%>
			<%--<em class="apply-type-icon"></em>--%>
			<%--<div class="apply-msg-con">--%>
				<%--<h4>您的直播认证申请提交失败！</h4>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="apply-foot-msg">请联系客服：QQ 3064114611</div>--%>
		<%--<a href="javascript:;" class="again-sub">重新提交</a>--%>
	<%--</div>--%>
</div>
<div class="loading">
	<img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/londing.png" alt=""/>
</div>
<script src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/js/live.js?t=3"></script>
<script type="text/javascript">_init_area();</script>
</body>
</html>