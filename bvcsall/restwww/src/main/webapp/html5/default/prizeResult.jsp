<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>${prize.name }中奖名单</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/prizeResult.css?t=<%=new java.util.Date().getTime()%>">
	<script src="<%=request.getContextPath()%>/js/libs/require.js" data-main="<%=request.getContextPath()%>/js/views/prizeResult"></script>
</head>
<body>
<div class="prizeBox">
	<input type="hidden" value="${levelList}" id="lvList"/>
	<input type="hidden" value="${prize.id}" id="prizeId"/>
	<img class="pageBg" src="<%=request.getContextPath()%>/img/prizeimg/prizeResult.jpg" alt=""/>
	<div class="prizeWarp">
		<jsp:include  page="../cache.jsp"/>

		<h2 class="prizeName" data-id="{$prize.id }">${prize.name }</h2>
		<div class="resultList">
			<c:forEach var="resultItem" items="${result }" varStatus="status">
				<div class="prizeItem">
					<div class="prizeHead">
				<span class="prizeClass">
					<h3>${resultItem[0].prizeLevelName}</h3>
					<p>${resultItem[0].prizeName}</p>
					<%--${resultItem[0].vipStar}--%>
				</span>
					</div>
					<ul>
						<c:forEach var="resultList" items="${resultItem }" varStatus="status">
							<li>
								<span class="fl prizeUserPic">
									<img class="actvideoPic" data-userPic="${resultList.userPic}" src="" onerror="this.src='<%=request.getContextPath()%>/img/icons-2x/user_icon_24px.png'">
									<c:if test="${resultList.vstat eq 1}">
										<em class="userV1"></em>
									</c:if>
									<c:if test="${resultList.vstat eq 2}">
										<em class="userV2"></em>
									</c:if>
									<c:if test="${resultList.vstat eq 3}">
										<em class="userV3"></em>
									</c:if>
								</span>
								<span class="prizeUserName">${resultList.username}</span></li>
						</c:forEach>
					</ul>
				</div>
			</c:forEach>
		</div>
	</div>
</div>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</body>
</html>









