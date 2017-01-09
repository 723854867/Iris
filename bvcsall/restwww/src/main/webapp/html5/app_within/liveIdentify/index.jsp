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
	//interfaceurl = "http://localhost:8080";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link class="linkUrl" rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/liveIdentify/css/style.css?t=1">
	<script src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/js/libs/jquery-2.1.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/js/libs/jquery.form.js"></script>

	<title>直播审核</title>
</head>
<body>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<input type="hidden" id="catalog" value="<%=request.getContextPath()%>"/>
<input type="hidden" id="uid" name="uid" value="${uid }"/>
<input type="hidden" id="access_token" name="access_token" value="${accessToken }"/>

<input class="form-fail" cateType-data="${anchor.certificateType}" sex-data="${anchor.sex}" qq-data="${anchor.qq}" wx-data="${anchor.wechat}" type="hidden"/>


<div class="main">
	<div class="first">
		<div class="banner">
			<img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/ad.jpg" class="ad" alt=""/>
			<button class="go-renzheng go-renzheng-ok">立即认证</button>
		</div>
		<div class="warp1">
			<label for="" class="agree-check-box">
				<em class="agree-check agree-check-ok"></em>
				<span class="agree-check-word">我同意<a href="javascript:;" class="go-agreement">我拍直播协议</a></span>
			</label>
			<dl class="live-condition">
				<dt><b>申请条件</b></dt>
				<dd>1、 形象气质佳、个人基本素养高尚；</dd>
				<dd>2、 普通话良好，声音悦耳，表达顺畅、生动、有个性，具有较强的互动性；</dd>
				<dd>3、 直播效果清晰可见，如需直播设备需自配；<br/>
				•禁止直播内容涉及政治、黄赌毒等违反法律和公共道德标准的内容；</dd>
				<dd>5、 需在微博、微信同步推荐我拍平台；</dd>
			</dl>
			<%--审核协议--%>
		</div>
	</div>
	<%--认证协议文档--%>
	<div class="live-agreement">
		<h3 class="live-agreement-tit">我拍直播协议：</h3>
		<div class="live-agreement-msg">
			<p>尊敬的用户您好，请您仔细阅读以下条款，如果您对本协议的任何条款表示异议，您可以选择不使用；使用则意味着您将同意遵守本协议下全部规定，以及我们后续对使用协议随时所作的任何修改，并完全服从于我们的统一管理。</p>
			<p>第一章 总则</p>
			<p>第1条 我拍是巴士在线科技有限公司(以下统称“我拍”)向用户提供的一种更方便的进行视频直播和观看视频直播服务的平台。</p>
			<p>第2条 我拍所有权、经营权、管理权均属本团队。</p>
			<p>第3条 本协议最终解释权归属我拍。</p>
			<p>第二章 用户说明</p>
			<p>第4条 凡是下载、浏览和发布内容的用户均为我拍用户(以下统称“用户”)。</p>
			<p>第5条 用户的个人信息受到保护，不接受任何个人或单位的查询。国家机关依法查询除外，用户的个人设置公开除外。</p>
			<p>第6条 用户享有言论自由的权利。</p>
			<p>第7条 用户的言行不得违反《计算机信息网络国际联网安全保护管理办法》、《互联网信息服务管理办法》、《互联网电子公告服务管理规定》、《维护互联网安全的决定》、《互联网新闻信息服务管理规定》等相关法律规定，不得在花椒上发布、传播或以其它方式传送含有下列内容之一的信息：</p>
			<div class="box-indent">
				<p>1.反对宪法所确定的基本原则的；</p>
				<p>2.危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；</p>
				<p>3.损害国家荣誉和利益的； </p>
				<p>4.煽动民族仇恨、民族歧视、破坏民族团结的；</p>
				<p>5.破坏国家宗教政策，宣扬邪教和封建迷信的；</p>
				<p>6.散布谣言，扰乱社会秩序，破坏社会稳定的；</p>
				<p>7.散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；</p>
				<p> 8.侮辱或者诽谤他人，侵害他人合法权利的；</p>
				<p> 9.煽动非法集会、结社、游行、示威、聚众扰乱社会秩序的； </p>
				<p>10.以非法民间组织名义活动的；</p>
				<p> 11.含有虚假、有害、胁迫、侵害他人隐私、骚扰、侵害、中伤、粗俗、猥亵、或其它道德上令人反感的内容；</p>
				<p> 12.含有中国法律、法规、规章、条例以及任何具有法律效力之规范所限制或禁止的其它内容的。 </p>
			</div>
			<p>第8条 用户不得在我拍内发布任何形式的广告。</p>
			<p> 第9条 用户应承担一切因其个人的行为而直接或间接导致的民事或刑事法律责任，因用户行为给本团队造成损失的，用户应负责赔偿。</p>
			<p> 第10条 本团队拥有对违反协议的用户进行处理的权力，直至禁止其在我拍内发布信息。</p>
			<p>第11条 禁止任何使用非法软件进行刷内容、违规发布内容的行为。</p>
			<p> 第12条 任何用户发现我拍上有内容涉嫌侮辱或者诽谤他人、侵害他人合法权益的或违反本协议的，有权进行直接举报。</p>
			<p>第13条 为了能够给广大用户提供一个优质的交流平台，同时使得我拍能够良性、健康的发展，将对涉及反动、色情和发布不良内容的用户，进行严厉处理。一经发现此类行为，将给予永久封禁并清空所有发言的处罚。 </p>
			<p>第三章 权利声明</p>
			<p>第14条 我拍发布的内容仅代表作者观点，与我拍无关。对于用户言论的真实性引发的全部责任，由用户自行承担，本团队不承担任何责任。</p>
			<p>第15条 用户之间因使用花椒而产生或可能产生的任何纠纷和/或损失，由用户自行解决并承担相应的责任，与本团队无关。 </p>
			<p>第16条 用户在我拍上发布的内容，我拍有权转载或引用。</p>
			<p> 第17条 用户在任何时间段在花椒上发表的任何内容的著作财产权，用户许可本团队在全世界范围内免费地、永久性地、不可撤销地、可分许可地和非独家地使用的权利，包括但不限于：复制权、发行权、出租权、展览权、表演权、放映权、广播权、信息网络传播权、摄制权、改编权、翻译权、汇编权以及《著作权法》规定的由著作权人享有的其他著作财产权利。并且，用户许可本团队有权利就任何主体侵权而单独提起诉讼，并获得全部赔偿。 
			</p>
			<p>第四章 处罚规则</p>
			<p>第18条 我拍郑重提醒用户，若出现下列情况任意一种或几种，将承担包括被关闭全部或者部分权限、被暂停或被禁止使用的后果，情节严重的，还将承担相应的法律责任。</p>
			<p> 1.使用不雅或不恰当昵称；</p>
			<p> 2.发布含有猥亵、色情、人身攻击和反政府言论等非法或侵权言论的； </p>
			<p>3.从事非法商业活动；</p>
			<p> 4.假冒管理人员或破坏管理人员形象；</p>
			<p> 5.使用非法软件进行刷内容、违规发布内容的行为；</p>
			<p>6.其他我拍认为不恰当的情况。</p>
			 <p> 第19条 凡文章出现以下情况之一，我拍管理人员有权不提前通知作者直接删除，并依照有关规定作相应处罚。情节严重者，我拍管理人员有权对其做出关闭部分权限、暂停直至禁止使用。</p>
			<p> 1.发表含有本协议第7条禁止发布、传播内容的文章； </p>
			<p>2.发表无意义的灌水内容； </p>
			<p>3.同一内容多次出现的；</p>
			<p> 4.违反本协议第8条的规定，发布广告的；</p>
			<p> 5.内容包含有严重影响用户浏览的内容或格式的； </p>
			<p>6.其他我拍认为不恰当的情况。</p>
			<p>第五章 隐私保护</p>
			<p>第20条 我拍不对外公开或向第三方提供单个用户的注册资料及用户在使用网络服务时存储在本网站的非公开内容，但下列情况除外：</p>
			<p>1.事先获得用户的明确授权；</p>
			<p> 2.根据有关的法律法规要求；</p>
			<p>3.按照相关政府主管部门的要求；</p>
			<p> 4.该第三方同意承担与我拍同等的保护用户隐私的责任； 在不透露单个用户隐私资料的前提下，我拍有权对整个用户数据库进行分析并对用户数据库进行商业上的利用。</p>
			<p>第六章 附则</p>
			<p>第21条 关于打击网络谣言的司法解释：《最高人民法院、最高人民检察院关于办理利用信息网络实施诽谤等刑事案件适用法律若干问题的解释》已于2013年9月5日由最高人民法院审判委员会第1589次会议、2013年9月2日由最高人民检察院第十二届检察委员会第9次会议通过，现予公布，自2013年9月10日起施行。
			</p>
			<p> 第22条 所有用户发布的内容而引起的法律纠纷，与我拍无关。</p>
			<p> 第23条 我拍如因系统维护或升级等而需暂停服务时，将事先公告。若因硬件故障或其它不可抗力而导致暂停服务，于暂停服务期间造成的一切不便与损失，本团队不负任何责任。由于花椒的产品调整导致信息丢失和/或其他结果的，我拍不承担任何责任。
			</p>
			<p>第24条本协议未涉及的问题参见国家有关法律法规，当本协议与国家法律法规冲突时，以国家法律法规为准。</p>
		</div>
		<a href="javascript:;" class="close-live-agreement">关闭</a>
	</div>
	<%--默认第一次填写--%>
	<div class="from-box1">
		<div class="apply-status">以下均为必填项，请认真填写！</div>
		<div class="apply-status-fail" fail-data="${anchor.rejectReason}">
			<h4 class="fial-head"><em class="fail-icon"></em>审核未通过原因 <span class="see-fail">隐藏</span></h4>
			<div class="fial-list"></div>
		</div>

		<form action="" id="from1" enctype="multipart/form-data" method="post">
			<div class="form-item">
				<span class="item-left">姓名：</span>
				<span class="item-right">
					<input type="text" value="${anchor.realName}" name="realName" re="[\u4e00-\u9fa5_a-zA-Z]{2,10}" placeholder="您的真实姓名" class="item-txt item-txt-exsc"/>
				</span>

			</div>
			<div class="bottom-line"></div>
			<div class="form-item">
				<span class="item-left">性别：</span>
				<span class="item-right sex-box">
					<input type="hidden" name="sex" value="0" class="sex-select"/>
					<label for="" class="sex-lab"><em class="sex-icon" data="1"></em>男</label>
					<label for="" class="sex-lab"><em class="sex-icon sex-icon-ok" data="0"></em>女</label>
				</span>
			</div>
			<div class="bottom-line"></div>
			<div class="form-item mt10">
				<span class="item-left">手机号码：</span>
				<span class="item-right">
					<%--<input type="number" name="phone" re="1[34578]\d{9}" placeholder="手机号码" class="item-txt item-txt-exsc"/>--%>
					<div class="box3-phone-number hide"><span class="form3-old-phone"></span> <a href="javascript:;" class="phone-modify-btn">修改</a></div>
					<input type="text" name="phone" re="^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$" placeholder="手机号码" value="${anchor.phone}" class="item-txt item-txt-exsc from3-phone"/>
				</span>
			</div>
			<div class="bottom-line"></div>
			<%--<div class="form-item">--%>
				<%--<span class="item-left">验证码：</span>--%>
				<%--<span class="item-right">--%>
					<%--<input type="number" name="code" placeholder="输入验证码" class="item-txt1"/>--%>
					<%--<input type="button" value="获取验证码" class="ident-code ident-code-ok"/>--%>
				<%--</span>--%>
			<%--</div>--%>
			<div class="form-item phone-inspect">
				<span class="item-left">验证码：</span>
				<span class="item-right"><input type="text" re="/^\d+$/" name="code" placeholder="输入验证码" class="item-txt1 from3-code item-txt-exsc"/>
					<input type="button" value="获取验证码" class="ident-code ident-code-ok"/>
				</span>
			</div>
			<div class="bottom-line"></div>
			<div class="form-item mt10">
				<span class="item-left item-select1">
					<h3 class="select-tit"><span class="select-word" data-type="qq">QQ号码</span> <em class="arrow-down"></em></h3>
					<ul class="select-list1">
						<li data-type="qq" re="^\d+$">QQ号码</li>
						<li data-type="wechat" re="">微信号</li>
					</ul>
				</span>
				<span class="item-right">
					<input type="text" name="qq" re="^\d+$" class="item-txt net-number item-txt-exsc" placeholder="QQ号码"/>
				</span>
			</div>
			<div class="bottom-line"></div>
			<div class="form-item">
				<!-- <p class="photo-tips">请填写证件号并上传清晰的证件照片 <a href="javascript:;" class="go-cart-tips">示例图片</a></p> -->

				<span class="item-left item-select">
					<input type="hidden" name="certificateType" value="1" class="cartid"/>
					<h3 class="select-tit"><span class="select-word" data-type="1" re="(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)">身份证</span> <em class="arrow-down"></em></h3>
					<ul class="select-list">
						<li data-type="1" re="(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)">身份证</li>
						<li data-type="2" re="">护照</li>
						<li data-type="3" re="">台胞证</li>
					</ul>
				</span>
				<span class="item-right">
					<input type="text" name="certificateNumber" value="${anchor.certificateNumber}" re="(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)" class="item-txt item-txt-exsc" placeholder="证件号码"/>
				</span>
			</div>
			<div class="bottom-line"></div>
			<%-- <div class="form-item">
				<div class="doc-type1">
					<dl>
						<dt>
							<img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/pic1x/pic-border-icon.png" class="preview-pho1"/>
							<input type="button" value="点击" class="file-btn file1" onClick="abc()"/>
							<input type="file" name="file1" data-num="1" re="" class="file-btn file1 item-txt-exsc" accept="image/*">
						</dt>

						<dd>正面</dd>
					</dl>
					<dl>
						<dt><img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/pic1x/pic-border-icon.png" class="preview-pho2"/>
							<input type="file" name="file2" data-num="2" re="" accept="image/*" class="file-btn file2 item-txt-exsc"/>
						</dt>
						<dd>反面</dd>
					</dl>
				</div>
			</div> --%>
			<div class="form-item nobor last-from-warp">
				<%--<input type="submit" class="form-submit1" value="提交申请"/>--%>
				<a href="javascript:;" class="form-submit1">提交申请</a>
			</div>
		</form>

	</div>
	<%--证件提示图片--%>
	<div class="parmpt-warp skyBox">
		<div class="pic1"><img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/userid.png"/></div>
		<div class="pic2"><img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/huzhao.png"/></div>
		<div class="pic3"><img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/mtp.png"/></div>
		<button class="close-parmpt">我知道了</button>
	</div>
	<%--认证表单--%>
	<div class="from-box2">
		<div class="apply-status">请确认您填写的信息是否准确无误</div>
		<form action="" id="form2">
			<div class="form-item">
				<span class="item-left">姓名：</span>
				<span class="item-right from2-realName"> </span>
			</div>
			<div class="form-item">
				<span class="item-left">性别：</span>
				<span class="item-right from2-sex"></span>
			</div>
			<div class="form-item">
				<span class="item-left">手机号：</span>
				<span class="item-right from2-phone"> </span>
			</div>
			<div class="form-item">
				<span class="item-left net-number">QQ号码：</span>
				<span class="item-right from2-netNumber"> </span>
			</div>
			<div class="form-item">
				<span class="item-left userid-number">身份证号：</span>
				<span class="item-right from2-carNumber"> </span>
			</div>
			<%-- <div class="form-item">
				<span class="item-left user-cart-pic">证件照片：</span>
				<span class="item-right"> </span>
				<div class="liver-msg-pic">
					<dl>
						<dt><img class="from2-file1" src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/pic1x/pic-border-icon.png"/></dt>
						<dd>正面</dd>
					</dl>
					<dl>
						<dt><img class="from2-file2" src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/pic1x/pic-border-icon.png"/></dt>
						<dd>反面</dd>
					</dl>
				</div>
			</div> --%>
			<div class="form-item form-sub">
				<input type="button" value="返回修改" class="modify-btn"/>
				<input type="button" value="确认提交" class="sub-btn"/>
			</div>
		</form>
	</div>
	<%--审核提交成功--%>
	<div class="apply-success">
		<div class="apply-msg">
			<em class="apply-type-icon"></em>
			<div class="apply-msg-con">
				<h4>您的直播认证申请已经提交成功！</h4>
				我们会在48个小时内审核您的信息，<br/>
				申请结果会以短信的形式通知您，请您注意查收！
			</div>
		</div>
		<div class="apply-foot-msg">如果您的申请长时间没有得到回复！<br/>请联系客服：QQ 3064114611</div>
	</div>
	<%--审核提交失败--%>
	<div class="apply-fail">
		<div class="apply-msg">
			<em class="apply-type-icon"></em>
			<div class="apply-msg-con">
				<h4>您的直播认证申请提交失败！</h4>
			</div>
		</div>
		<div class="apply-foot-msg">请联系客服：QQ 3064114611</div>
		<a href="javascript:;" class="again-sub">重新提交</a>
	</div>
	<%--查看状态--%>
	<div class="from-box4">
		<div class="apply-status"></div>
		<form action="" id="form2">
			<div class="form-item">
				<span class="item-left">姓名：</span>
				<span class="item-right">${anchor.realName}</span>
			</div>
			<div class="form-item">
				<span class="item-left">性别：</span>
				<span class="item-right">${anchor.sex} </span>
			</div>
			<div class="form-item">
				<span class="item-left">手机号：</span>
				<span class="item-right">${anchor.phone} </span>
			</div>
			<div class="form-item">
				<span class="item-left net-number">QQ号码：</span>
				<span class="item-right">${anchor.qq} </span>
			</div>
			<div class="form-item">
				<span class="item-left userid-number" data-type="${anchor.certificateType}">身份证号：</span>
				<span class="item-right">${anchor.certificateNumber} </span>
			</div>
			<div class="form-item">
				<span class="item-left user-cart-pic">证件照片：</span>
				<span class="item-right input-gray">为了您的信息安全，证件照不予显示</span>
			</div>
		</form>
		<div class="box4-tip">如有修改信息等问题，请联系客服QQ：3064114611</div>
	</div>
</div>
<div class="loading">
	<img src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/img/londing.png" alt=""/>
</div>
<script src="<%=request.getContextPath()%>/html5/app_within/liveIdentify/js/live.js?t=2"></script>
</body>
</html>