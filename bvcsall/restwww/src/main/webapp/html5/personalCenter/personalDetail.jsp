<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>${ ruser.name }的个人主页</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/personalCenter/personalDetail.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
	<jsp:include page="../head.jsp" />
	<div class="personalBg">
		<c:if test="${ruser.isAttention eq 0}">
			<a href="javascript:;" class='playData'><em id="attentionBtn" class="attenBtn" data-isAttention="${ruser.isAttention}" data-userId="${ruser.id}"></em></a>
		</c:if>
		<c:if test="${ruser.isAttention eq 1}">
			<a href="javascript:;" class='playData'><em id="attentionBtn" class="attenBtnActive" data-isAttention="${ruser.isAttention}" data-userId="${ruser.id}"></em></a>
		</c:if>
		<img class='userHomePic' data-homePicSrc='${ruser.pic }' src='' onerror='this.src="../../img/portrait.png"'   alt="" />
		<ul>
			<li><strong>${ ruser.name }</strong>
			<c:if test="${ruser.sex eq 0}">
				<img id="sex" src="../../img/icons-2x/prof_icon_woman.png"  alt="">
			</c:if>
			<c:if test="${ruser.sex eq 1}">
				<img id="sex" src="../../img/icons-2x/prof_icon_man.png"  alt="">
			</c:if>
			<c:if test="${ruser.sex eq 2}">
				<img id="sex" src="../../img/icons-2x/prof_icon_man.png"  alt="">
			</c:if>
			<c:if test="${ ruser.vipStat eq 2}">
				<img src="../../img/icons-2x/user_icon_yellow.png"  alt="">
			</c:if>
			<c:if test="${ ruser.vipStat eq 1}">
				<img src="../../img/icons-2x/user_icon_blue.png"  alt="">
			</c:if>
			<c:if test="${ ruser.vipStat eq 3}">
				<img src="../../img/icons-2x/user_icon_green.png" alt="">
			</c:if>
			<c:if test="${ ruser.vipStat eq 0}">
				
			</c:if>
			</li>
			<c:if test="${ruser.signature == '' || ruser.signature==  null}">
				<li>懒人没前途o(∩_∩)o 哈哈~</li>
			</c:if>
			<c:if test="${ruser.signature != '' && ruser.signature !=  null}">
				<li>${ruser.signature }</li>
			</c:if>
			<li>关注 ${ ruser.attentionCount }   |   粉丝 ${ ruser.fansCount }</li>
		</ul>
	</div>
	<div class='activeUseList'>
		<div class='videoList1'>
			<ul>
				<c:forEach var="video" items="${videoList}" varStatus="status" >
				<li>
					<c:if test="${video.isForward eq 1}">
						<div class="lineBg">
							<p>
								<a class="fl">
									<img src="../../img/icons-2x/Forward-48.png" alt="">
								</a>
								<img class='userPortraits' src="" data-userPic="${ ruser.pic }" onerror='this.src="<%=request.getContextPath()%>/img/icons-2x/user_icon_24px.png"' alt="">
								<span>
									${ruser.name }转发了${ video.user.name }的视频
								</span>
							</p>
						</div>
					</c:if>
				
					<div class='listUserMsg'>
						<dl>
						<c:if test="${video.isForward eq 1}">
							<dt>
								<img class='userPortrait' src="" data-userPic='${video.user.pic }' data-userId='${video.user.id }' data-uid='${ruser.id }' onerror='this.src="<%=request.getContextPath()%>/img/icons-2x/user_icon_24px.png"'  alt="" >
								<c:if test="${ video.user.vipStat eq 2}">
									<img class='addV' src="../../img/icons-2x/user_icon_yellow.png"  alt="">
								</c:if>
								<c:if test="${ video.user.vipStat eq 1}">
									<img class='addV' src="../../img/icons-2x/user_icon_blue.png"  alt="">
								</c:if>
								<c:if test="${ video.user.vipStat eq 3}">
									<img class='addV' src="../../img/icons-2x/user_icon_green.png" alt="">
								</c:if>
							<!-- <em class='addV'></em> -->
							</dt>
							<dd>
								<p class="personalName">${video.user.name }</p>
								<p class="myDate" data-time='${video.createDate}'></p>
							</dd>
							</c:if>
							
							<c:if test="${video.isForward eq 0}">
							<dt>
								<img class='userPortrait' src="" data-userPic='${ruser.pic }' data-userId='${video.user.id }' data-uid='${ruser.id }' onerror='this.src="<%=request.getContextPath()%>/img/icons-2x/user_icon_24px.png"' alt="" >
								<c:if test="${ ruser.vipStat eq 2}">
									<img class='addV' src="../../img/icons-2x/user_icon_yellow.png"  alt="">
								</c:if>
								<c:if test="${ ruser.vipStat eq 1}">
									<img class='addV' src="../../img/icons-2x/user_icon_blue.png"  alt="">
								</c:if>
								<c:if test="${ ruser.vipStat eq 3}">
									<img class='addV' src="../../img/icons-2x/user_icon_green.png" alt="">
								</c:if>
							<!-- <em class='addV'></em> -->
							</dt>
							<dd>
								<p class="personalName">${ruser.name }</p>
								<p class="myDate" data-time='${video.createDate}'></p>
							</dd>
							</c:if>
							
							<dd>
								<em class='playIcon'></em><span class='playData1'>${video.showPlayCount}</span>
							</dd>
						</dl>
						
					</div>
					
					<div class='playBox' data-videoKey='${video.playKey }' data-videoId='${video.id }'>
						<div class="bgBox"></div>
						<div class="loadingGif" style="width:30px;height:30px;min-height:30px;position:absolute;left:0;top:0;display:none;z-index:999999;background-color:#000000;opacity:0.6;width:100%;height:50%;color:#ffffff;text-align:center;padding-top:50%;font-size:18px;"></div>
						<img class='videoPic' src=""  alt="" data-videoPic='${video.videoPic }' onerror='this.src="../../img/portrait.png"'>
						<em class="canPlay" data-url='${video.url}'></em>
					</div>
					<span class="videoDiscription face">${video.description }</span>
					<c:if test="${video.isForward eq 1}">
					<div class='shareBox' data-createrId='${video.user.id }' data-createDate='${video.createDate }'>
						
						<div class='zan' data-praise='${video.praise}' data-videoid='${video.id }'>
						<c:choose>
						<c:when test="${video.praise == true || video.praise=='true'}">
							<em class='on'></em>
						</c:when>
						<c:otherwise> 
							<em class='off'></em>
						</c:otherwise>
						</c:choose>
						<%-- <c:if test="${video.praise == false || video.praise == 'false'}">
							<em class='off' data-zan='118978'></em>
						</c:if> --%>
							<span>${ video.praiseCount }</span>
						</div>
						<div class='commit'>
							<a href="javascript:void(0);" data-video='${video.id }'><em></em></a>
							<span>${ video.evaluationCount }</span>
						</div>
						<c:if test="${video.isForward eq 1}">
							<div class='Forward'  data-name="${video.user.name }" data-videoid="${video.id }" data-userId="${video.id }" data-desc="${video.description }" data-videoPic="${video.videoPic }">
								<em></em>
									<%-- <span>${ video.favoriteCount }</span> --%>
							</div>
						</c:if>
						<c:if test="${video.isForward eq 0}">
							<div class='Forward'  data-name="${ruser.name }" data-videoid="${video.id }" data-userId="${video.id }" data-desc="${video.description }" data-videoPic="${video.videoPic }">
								<em></em>
									<%-- <span>${ video.favoriteCount }</span> --%>
							</div>
						</c:if>
					</div>
					</c:if>
					<c:if test="${video.isForward eq 0}">
					<div class='shareBox' data-createrId='${ruser.id }' data-createDate='${video.createDate }'>
						
						<div class='zan' data-praise='${video.praise}' data-videoid='${video.id }'>
						<c:choose>
						<c:when test="${video.praise == true || video.praise=='true'}">
							<em class='on'></em>
						</c:when>
						<c:otherwise> 
							<em class='off'></em>
						</c:otherwise>
						</c:choose>
						<%-- <c:if test="${video.praise == false || video.praise == 'false'}">
							<em class='off' data-zan='118978'></em>
						</c:if> --%>
							<span>${ video.praiseCount }</span>
						</div>
						<div class='commit'>
							<a href="javascript:void(0);" data-video='${video.id }'><em></em></a>
							<span>${ video.evaluationCount }</span>
						</div>
						<c:if test="${video.isForward eq 1}">
							<div class='Forward'  data-name="${video.user.name }" data-videoid="${video.id }" data-userId="${video.id }" data-desc="${video.description }" data-videoPic="${video.videoPic }">
								<em></em>
									<%-- <span>${ video.favoriteCount }</span> --%>
							</div>
						</c:if>
						<c:if test="${video.isForward eq 0}">
							<div class='Forward'  data-name="${ruser.name }" data-videoid="${video.id }" data-userId="${video.id }" data-desc="${video.description }" data-videoPic="${video.videoPic }">
								<em></em>
									<%-- <span>${ video.favoriteCount }</span> --%>
							</div>
						</c:if>
					</div>
					</c:if>
				</li>
			</c:forEach>
			</ul>
		</div>
		<%-- <c:if test="${ videoList.size() >= 20}"> --%>
			<div class="loadMore" id="btnMore" href="javascript:void(0);" style="display:block;">加载更多</div>
		<%-- </c:if> --%>
		<div class="downBar">
			<span class="downClose"></span>
			<a href="http://www.wopaitv.com#cnzz_name=loading&cnzz_from=bottomDownload">下载我拍，查看更多</a>
		</div>
	</div>
	<!-- <div class="scrollTop"></div> -->
	<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/personalDetail" src="<%=request.getContextPath()%>/js/libs/require.js"></script>