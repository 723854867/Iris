<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/interest.css?t=<%=new java.util.Date().getTime()%>">
	<script src=''></script>
	<title>感兴趣的人</title>
</head>
<body>
<!-- head -->
<jsp:include page="../head.jsp" />
<div class='main'>
	<div class='nav'>
		<p>点击一键关注关注所有人啦<a class="allAttention" href="javascript:void(0);">一键关注</a></p>
	</div>
	<div class='funPerson'>
	<c:forEach var ="ruser" items="${ruserList}" varStatus="status" >
		<dl>
			<dt>
				<img class='userPortrait' src="" data-userPic='${ruser.pic }' data-userId='${ruser.id }' onerror='this.src="<%=request.getContextPath()%>/img/portrait.png"' alt="" >
				<c:if test="${ ruser.vipStat eq 2}">
					<img class='addV'src="../../img/icons-2x/user_icon_yellow.png"  alt="">
				</c:if>
				<c:if test="${ ruser.vipStat eq 0}">
					
				</c:if>
				<c:if test="${ ruser.vipStat eq 1}">
					<img class='addV' src="../../img/icons-2x/user_icon_blue.png"  alt="">
				</c:if>
				<c:if test="${ ruser.vipStat eq 3}">
					<img class='addV'src="../../img/icons-2x/user_icon_green.png" alt="">
				</c:if>
			<!-- <em class='addV'></em> -->
			</dt>
			</dt>
			<dd>
				<p>${ruser.name }</p>
				<%-- <c:if test="${ruser.signature == ''}">
					
					<c:if test="${ruser.sex == 1}">
						<p>男</p>
					</c:if>
					<c:if test="${ruser.sex == 0}">
						<p>女</p>
					</c:if>
					<c:if test="${ruser.sex == 2}">
						<p>未知</p>
					</c:if>
				</c:if> --%>
				<c:if test="${ruser.signature == '' || ruser.signature==  null}">
					<c:if test="${ruser.addr == '' || ruser.addr ==  null}">
						<c:if test="${ruser.sex == '' || ruser.sex ==  null}">
							<p>懒人没前途o(∩_∩)o 哈哈~</p>
						</c:if>
						<c:if test="${ruser.sex != '' && ruser.sex !=  null}">
							<c:if test="${ruser.sex == 1}">
								<p>男</p>
							</c:if>
							<c:if test="${ruser.sex == 0}">
								<p>女</p>
							</c:if>
							<c:if test="${ruser.sex == 2}">
								<p>未知</p>
							</c:if>
						</c:if>
					</c:if>
					<c:if test="${ruser.addr != '' && ruser.addr !=  null}">
						<p>${ruser.addr }</p>
					</c:if>
				</c:if>
				<c:if test="${ruser.signature != '' && ruser.signature !=  null}">
					<p>${ruser.signature }</p>
				</c:if>
			</dd>
			<dd>
				<c:if test="${ruser.isAttention eq 0}">
					<a href="javascript:;" class='atten'><em class="attenBtn" data-isAttention="${ruser.isAttention}" data-userId="${ruser.id}"></em></a>
				</c:if>
				<c:if test="${ruser.isAttention eq 1}">
					<a href="javascript:;" class='atten'><em class="attenBtnActive" data-isAttention="${ruser.isAttention}" data-userId="${ruser.id}"></em></a>
				</c:if>
			</dd>
		</dl>
	</c:forEach>
	</div>
	<div class="downBar">
		<span class="downClose"></span>
		<a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载我拍，查看更多</a>
	</div>
</div>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/interest" src="<%=request.getContextPath()%>/js/libs/require.js"></script>