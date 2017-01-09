<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/attention.css?t=<%=new java.util.Date().getTime()%>">
	<script src=''></script>
	<title>关注</title>
</head>
<body>
<!-- head -->
<jsp:include page="../head.jsp" />
<!-- 感兴趣的人 -->
<c:if test="${ ruserList.size() > 0}">
<div class='main'>
	<div class='nav'>
		<p>可能感兴趣的人</p>
	</div>
		<div class='funPerson'>
			<dl>
				<dt>
					<img class='userPortrait-1' src="" data-userPic='${ruserList.get(0).pic }' data-userId="${ruserList.get(0).id }" onerror='this.src="<%=request.getContextPath()%>/img/portrait.png"'  alt="" >
					<c:if test="${ ruserList.get(0).vipStat eq 2}">
						<img class='addV'src="../../img/icons-2x/user_icon_yellow.png"  alt="服务器错误，请重新加载">
					</c:if>
					<c:if test="${ ruserList.get(0).vipStat eq 1}">
						<img class='addV' src="../../img/icons-2x/user_icon_blue.png"  alt="服务器错误，请重新加载">
					</c:if>
					<c:if test="${ ruserList.get(0).vipStat eq 3}">
						<img class='addV'src="../../img/icons-2x/user_icon_green.png" alt="">
					</c:if>
				</dt>
				<dd>
					<p>${ruserList.get(0).name }</p>
					<c:if test="${ruserList.get(0).signature == '' || ruserList.get(0).signature==  null}">
						<c:if test="${ruserList.get(0).addr == '' || ruserList.get(0).addr ==  null}">
							<c:if test="${ruserList.get(0).sex == '' || ruserList.get(0).sex ==  null}">
								<p>懒人没前途o(∩_∩)o 哈哈~</p>
							</c:if>
							<c:if test="${ruserList.get(0).sex != '' && ruserList.get(0).sex !=  null}">
								<c:if test="${ruserList.get(0).sex == 1}">
									<p>男</p>
								</c:if>
								<c:if test="${ruserList.get(0).sex == 0}">
									<p>女</p>
								</c:if>
								<c:if test="${ruserList.get(0).sex == 2}">
									<p>未知</p>
								</c:if>
							</c:if>
						</c:if>
						<c:if test="${ruserList.get(0).addr != '' && ruserList.get(0).addr !=  null}">
							<p>${ruserList.get(0).addr }</p>
						</c:if>
					</c:if>
					<c:if test="${ruserList.get(0).signature != '' && ruserList.get(0).signature !=  null}">
						<p>${ruserList.get(0).signature }</p>
					</c:if>
				</dd>
				<dd>
					<c:if test="${ruserList.get(0).isAttention eq 0}">
						<a href="javascript:;" class='atten'><em id="attentionBtn" class="attenBtn" data-isAttention="${ruserList.get(0).isAttention}" data-userId="${ruserList.get(0).id}"></em></a>
					</c:if>
					<c:if test="${ruserList.get(0).isAttention eq 1}">
						<a href="javascript:;" class='atten'><em id="attentionBtn" class="attenBtnActive" data-isAttention="${ruserList.get(0).isAttention}" data-userId="${ruserList.get(0).id}"></em></a>
					</c:if>
				</dd>
			</dl>
			
			<div class='seeMore'><a href="javascript:void(0);">查看更多</a></div>
		</div>
	</c:if>
	<c:if test="${ ruserList.size() == 0}">
		
	</c:if>
	<div class='videoList1'>
		<ul>
	
		</ul>
		<div class="loadMore" id="btnMore" href="javascript:void(0);">加载更多</div>
	</div>
	<div class="downBar">
		<span class="downClose"></span>
		<a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载我拍，查看更多</a>
	</div>
</div>

<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/attention" src="<%=request.getContextPath()%>/js/libs/require.js"></script>