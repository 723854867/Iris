<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%@page import="com.busap.vcs.util99bill.Pkipair"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.busap.vcs.webcomn.util.SpringWebUtils"%>
<%@page import="com.busap.vcs.service.ConsumeRecordService"%>
<%@page import="com.busap.vcs.service.*"%>
<%@page import="java.util.*"%>
<%@page import="java.net.URLEncoder"%>

<%
	WebClientUtils cu=WebClientUtils.getInstance();
	String cdnurl=cu.loadConfigUrl("html5", "cdnurl");
	String imageurl=cu.loadConfigUrl("html5", "imageurl");
	String interfaceurl=cu.loadConfigUrl("html5", "interfaceurl");
	if(cdnurl==null){
		cdnurl="http://cdn.wopaitv.com";
	}
	if(imageurl==null){
		imageurl="http://api.wopaitv.com";
	}
	if(interfaceurl==null){
		interfaceurl="http://api.wopaitv.com";
	}
//	interfaceurl = "http://localhost:8080";
%>

<%
	
	
    		
    		String hostUrl="http://ceshi.wopaitv.com/restwww";
//    		String hostUrl="http://api.wopaitv.com/restwww";
//    		String hostUrl="http://1.119.0.59:8080/restwww";
        	
        	
        	//人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
//        	String merchantAcctId = "1001213884201";//demo文件原值
        	String merchantAcctId = "1008311230901";
        	//编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
        	String inputCharset = "1";
        	//接收支付结果的页面地址，该参数一般置为空即可。
        	String pageUrl = "";
        	//服务器接收支付结果的后台地址，该参数务必填写，不能为空。
//        	String bgUrl = "http://219.233.173.50:8801/RMBPORT/receive.jsp";
//        	String bgUrl = hostUrl+"/pay/callback99Bill?";
        	String bgUrl = (String)request.getAttribute("bgurl1");
//        	String bgUrl = "http://api.restwww.com/pay/callback99Bill?orderId=";
        	//网关版本，固定值：v2.0,该参数必填。
        	String version =  "v2.0";
        	//语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
        	String language =  "1";
        	//签名类型,该值为4，代表PKI加密方式,该参数必填。
        	String signType =  "4";
        	//支付人姓名,可以为空。
        	String payerName= ""; 
        	//支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
//        	String payerContactType =  "1";
        	String payerContactType =  "";
        	//支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
//        	String payerContact =  "2532987@qq.com";
        	String payerContact =  "";
        	//商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
        	String orderId = "";
        	
//        	String orderId = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
				
				
        	//订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
//        	String orderAmount = "1";
			String orderAmount ="0";
        	
        	//订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
        	String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        	
        	
        	//商品名称，可以为空。
        	String productName= ""; 
        	productName=URLEncoder.encode(productName,"UTF-8");
        	productName="";
//        	productName="111";
        	//商品数量，可以为空。
        	String productNum = "";
        	//商品代码，可以为空。
        	String productId = "";
        	//商品描述，可以为空。
        	String productDesc = "";
        	//扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        	String ext1 = "";
        	//扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        	String ext2 = "";
        	//支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10，必填。
        	String payType = "10";
        	//银行代码，如果payType为00，该值可以为空；如果payType为10，该值必须填写，具体请参考银行列表。
        	String bankId = "ICBC";
        	//同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
        	String redoFlag = "";
        	//快钱合作伙伴的帐户号，即商户编号，可为空。
        	String pid = "";
        	// signMsg 签名字符串 不可空，生成加密签名串
        	String signMsgVal = "";
        	
        	String signMsg ="";
//        	signMsg=URLEncoder.encode(signMsg,"UTF-8");
%>


<%--图片地址--%>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<%--视频地址--%>
<input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>


<!DOCTYPE html>
<html>
<head lang="en">
	<meta charset="UTF-8">
	<title>LIVE充值平台</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
</head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_within/pc_recharge/css/style.css?t=2">
<script src='<%=request.getContextPath()%>/html5/app_within/pc_recharge/js/jquery-1.7.min.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/pc_recharge/js/jquery.form.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/pc_recharge/js/jQuery.md5.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/pc_recharge/js/common.js'></script>
<script src='<%=request.getContextPath()%>/html5/app_within/pc_recharge/js/recharge.js'></script>

<body>

<%--<form action="<%=request.getContextPath()%>/pay/pay99Bill" target="_blank" method="post">--%>
	<%--<input type="text" value="120" name="productId"/>--%>
	<%--<input type="text" value="ICBC" name="bankId"/>--%>
	<%--<input type="text" id="uid" name="uid"/>--%>
	<%--<input type="text" id="tk" name="at"/>--%>
	<%--<input type="submit" value="提交"/>--%>

<%--</form>--%>


<div class='main'>
	<div class="top">
		<div class="top_box">
			<a href="http://www.wopaitv.com/"><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/logo.png" class="fl logo" alt=""/></a>
			<div class="fr user-box">
				<img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/user_icon_60px.png" class="user_pic fl" alt=""/>
				<span class="fl user-info">
					<p><span class="user_name"></span> <a href="javascript:;" class="exit_btn"></a><span class="exit_box">退出</span></p>
					<%--<a href="javascript:;" class="exit">退出</a>--%>
					<p>ID：<span class="user_code"></span></p>
				</span>

			</div>
			<div class="fr for_log">请登录</div>
		</div>
	</div>
	<div class="warp">
		<div class="fl warp_l">
			<div class="warp_l_tit">金币充值</div>
			<div class="warp_l_info">
				遇充值问题请联系微信客服：LIVE官方助手
			</div>
		</div>
		<div class="fr warp_r">
			<div class="warp_con">
				<form action="https://www.99bill.com/gateway/recvMerchantInfoAction.htm" target="_blank" id="form1" method="post">
					<input type="hidden" name="inputCharset" value="<%=inputCharset%>" />
					<input type="hidden" name="pageUrl" value="<%=pageUrl%>" />
					<input type="hidden" name="bgUrl" value="<%=bgUrl%>" />
					<input type="hidden" name="version" value="<%=version%>" />
					<input type="hidden" name="language" value="<%=language%>" />
					<input type="hidden" name="signType" value="<%=signType%>" />
					<input type="hidden" name="signMsg" value="<%=signMsg%>" />
					<input type="hidden" name="merchantAcctId" value="<%=merchantAcctId%>" />
					<input type="hidden" name="payerName" value="<%=payerName%>" />
					<input type="hidden" name="payerContactType" value="<%=payerContactType%>" />
					<input type="hidden" name="payerContact" value="<%=payerContact%>" />
					<input type="hidden" name="orderId" value="<%=orderId%>" />
					<input type="hidden" name="orderAmount" value="<%=orderAmount%>" />
					<input type="hidden" name="orderTime" value="<%=orderTime%>" />
					<input type="hidden" name="productName" value="<%=productName%>" />
					<input type="hidden" name="productNum" value="<%=productNum%>" />
					<input type="hidden" name="productId" value="120" />
					<input type="hidden" name="productDesc" value="<%=productDesc%>" />
					<input type="hidden" name="ext1" value="<%=ext1%>" />
					<input type="hidden" name="ext2" value="<%=ext2%>" />
					<input type="hidden" name="payType" value="<%=payType%>" />
					<input type="hidden" name="bankId" value="ICBC" />
					<input type="hidden" name="redoFlag" value="<%=redoFlag%>" />
					<input type="hidden" name="pid" value="<%=pid%>" />
					<input type="hidden" id="uid" name="uid"/>
					<input type="hidden" id="tk" name="at"/>
				<div class="warp_item">
					<label for="">当前金币余额：</label>
					<span class="fl balance">0</span>
					<span class="fr warp_tips">金币只用于LIVE平台消费，不可提现！</span>
				</div>
				<div class="warp_item">
					<label>充值金额：</label>
					<div class="fl warp_item_r recharge_list">
						<ul>
							<%--<li class="on">--%>
								<%--<p>180金币</p>--%>
								<%--<p>￥18</p>--%>
							<%--</li>--%>
							<li class="zdy_btn"><input type="radio" name="productId" value="0"/><p>自定义金额</p></li>
						</ul>
						<div class="zdy_val_box">
							<p>自定义金额 <input type="text" name="oAmt" class="zdy_txt"/> 元（您可获得 <span class="diamond_count"></span>金币）</p>
						</div>
						<div class="pay_all">
							<p>应付金额：<span class="zdy_count">0</span>元</p>
						</div>
					</div>

				</div>
				<%--<div class="warp_item">--%>
					<%--<label for="">平台支付：</label>--%>
					<%--<div class="warp_item_r pay_icon_list" id="pay_type">--%>
						<%--<ul>--%>
							<%--<li class="on"><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/zfb_icon.png" alt=""/></li>--%>
							<%--<li><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/zfb_icon.png" alt=""/></li>--%>
							<%--<li><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/zfb_icon.png" alt=""/></li>--%>
						<%--</ul>--%>
					<%--</div>--%>
				<%--</div>--%>
				<div class="warp_item">
					<label for="">网银支付：</label>
					<div class="warp_item_r pay_icon_list" id="bank_list">
						<ul>
							<li data-type="BCOM"><input type="radio" name="bankId" value="BCOM"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_bcom.gif" alt="交通银行"/><em class="corner"></em></li>
							<li data-type="ICBC"><input type="radio" name="bankId" value="ICBC"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_icbc.gif" alt="工商银行"/><em class="corner"></em></li>
							<li data-type="CCB"><input type="radio" name="bankId" value="CCB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_ccb.gif" alt="建设银行"/><em class="corner"></em></li>
							<li data-type="ABC"><input type="radio" name="bankId" value="ABC"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_abc.gif" alt="农业银行"/><em class="corner"></em></li>
							<li data-type="CMB"><input type="radio" name="bankId" value="CMB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_cmb.gif" alt="招商银行"/><em class="corner"></em></li>
							<li data-type="CITIC"><input type="radio" name="bankId" value="CITIC"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_citic.gif" alt="中信银行"/><em class="corner"></em></li>
							<li data-type="CEB"><input type="radio" name="bankId" value="CEB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_ceb.gif" alt="光大银行"/><em class="corner"></em></li>
							<li data-type="HXB"><input type="radio" name="bankId" value="HXB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_hxb.gif" alt="华夏银行"/><em class="corner"></em></li>
							<li class="more_bank">+更多</li>
						</ul>
						<ul class="hide_bank_list">
							<li data-type="GDB"><input type="radio" name="bankId" value="GDB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_gdb.gif" alt="广发银行"/><em class="corner"></em></li>
							<li data-type="CMBC"><input type="radio" name="bankId" value="CMBC"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_cmbc.gif" alt="民生银行"/><em class="corner"></em></li>
							<li data-type="SPDB"><input type="radio" name="bankId" value="SPDB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_spdb.gif" alt="浦发银行"/><em class="corner"></em></li>
							<li data-type="BOC"><input type="radio" name="bankId" value="BOC"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_boc.gif" alt="中国银行"/><em class="corner"></em></li>
							<li data-type="BOB"><input type="radio" name="bankId" value="BOB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_bob.gif" alt="北京银行"/><em class="corner"></em></li>
							<li data-type="PAB"><input type="radio" name="bankId" value="PAB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_pab.gif" alt="平安银行"/><em class="corner"></em></li>
							<li data-type="POST"><input type="radio" name="bankId" value="POST"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_post.gif" alt="中国邮政"/><em class="corner"></em></li>
							<li data-type="CIB"><input type="radio" name="bankId" value="CIB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_cib.gif" alt="兴业银行"/><em class="corner"></em></li>
							<li data-type="BJRCB"><input type="radio" name="bankId" value="BJRCB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_bjrcb.gif" alt="北京农村商业银行"/><em class="corner"></em></li>
							<li data-type="CBHB"><input type="radio" name="bankId" value="CBHB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_cbhb.gif" alt="渤海银行"/><em class="corner"></em></li>
							<%--<li data-type="CZB"><input type="radio" name="bankId" value="CZB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_czb.gif" alt="浙商银行"/></li>--%>
							<%--<li data-type="BEA"><input type="radio" name="bankId" value="BEA"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_bea.gif" alt="东亚银行"/></li>--%>
							<%--<li data-type="GZCB"><input type="radio" name="bankId" value="GZCB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_gzcb.gif" alt="广州银行"/></li>--%>
							<%--<li data-type="GZRCC"><input type="radio" name="bankId" value="GZRCC"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_gzrcc.gif" alt="广州市农村信用合作社"/></li>--%>
							<%--<li data-type="HSB"><input type="radio" name="bankId" value="HSB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_hsb.gif" alt="徽商银行"/></li>--%>
							<%--<li data-type="HZB"><input type="radio" name="bankId" value="HZB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_hzb.gif" alt="杭州银行"/></li>--%>
							<li data-type="NBCB"><input type="radio" name="bankId" value="NBCB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_nbcb.gif" alt="宁波银行"/><em class="corner"></em></li>
							<li data-type="NJCB"><input type="radio" name="bankId" value="NJCB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_njcb.gif" alt="南京银行"/><em class="corner"></em></li>
							<%--<li data-type="SDB"><input type="radio" name="bankId" value="SDB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_sdb.gif" alt="深圳发展银行"/></li>--%>
							<%--<li data-type="SHB"><input type="radio" name="bankId" value="SHB"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_shb.gif" alt="上海银行"/></li>--%>
							<%--<li data-type="SHRCC"><input type="radio" name="bankId" value="SHRCC"/><img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/icon/bank_shrcc.gif" alt="上海农村商业银行"/></li>--%>
							<li class="more_up">收起</li>
						</ul>
					</div>
				</div>
				<div class="btn_box">
					<%--<input type="submit" class="recharge_btn" value="立即充值"/>--%>
					<a href="javascript:;" class="recharge_btn">立即充值</a>
				</div>
				</form>
			</div>
		</div>
	</div>
	<div class="foot">
		<p>版权所有©2016 巴士在线科技有限公司 赣ICP备12001072号-9</p>
		<p>地址:南昌市高新开发区火炬大街201号 | 联系电话:0791-82212891</p>
	</div>
</div>


<div class="log_box">
	<div class="log_top">
		登录LIVE <img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/close.png" class="close_btn"/>
	</div>
	<div class="log_warp">
		<div class="log_item">
			<label for="">手机号：</label>
			<input type="text" class="log_txt phone"/>
		</div>
		<div class="log_item">
			<label for="">密码：</label>
			<input type="password" class="log_txt pwd"/>
		</div>
		<div class="err_msg"></div>
		<div class="log_item">
			<a href="javascript:;" class="log_btn">登录</a>
		</div>
	</div>
	<div class="log_tips">
		<p>1.请填写您注册LIVE或在LIVE绑定的手机号和密码；</p>
		<p>2.如果您忘记密码请到LIVE客户端内找回密码；</p>
	</div>
</div>


<div class="tip_box">
	<div class="log_top">
		<img src="<%=request.getContextPath()%>/html5/app_within/pc_recharge/img/close.png" class="close_btn"/>
	</div>
	<div class="log_warp tip_warp">
		<p>遇充值问题请联系微信客服：LIVE官方助手</p>
		<p><a href="/restwww/page/user/pcRechargeIndex" class="btn close_tip">充值成功</a></p>
		<p class="close_tip"><a href="javascript:;">返回更改充值方式</a></p>
	</div>
</div>

<%--遮罩层--%>
<div class="skyBox"></div>
<%--提示框--%>
<%--<div class='buy-ok'>--%>
	<%--<em class="close-btn"></em>--%>
	<%--<div class='buy-ok-warp'>--%>
		<%--<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/ok-icon.png" class="ok-pic" alt=""/>--%>
		<%--<h4>提现成功</h4>--%>
		<%--<p class="ok-msg">已为账户：<span class="user-name2"></span>充值<span class="gold-num"></span>,可以给主播送礼物啦，GO！</p>--%>
	<%--</div>--%>
<%--</div>--%>
<%--<div class="load-box">--%>
	<%--<div class="loading">--%>
		<%--<img src="<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/dist/img/londing.png" alt=""/>--%>
	<%--</div>--%>
<%--</div>--%>
<%--<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/common.js'></script>--%>

<%--<script src='<%=request.getContextPath()%>/html5/app_within/Recharge/wxrecharge/src/js/extract.js'></script>--%>

</body>