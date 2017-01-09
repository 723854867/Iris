<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>


<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index/video_show.css?t=<%=new java.util.Date().getTime()%>">
	<script src="<%=request.getContextPath()%>/js/libs/require.js" data-main="<%=request.getContextPath()%>/js/views/video_show"></script>
	<title>${video.description }</title>
</head>
<body>
<iframe src="#" frameborder="2" width="0" height="0" name="openapp" id="openapp"></iframe>
<jsp:include  page="../head.jsp"/>
<input type="hidden" id="videoId" value="${video.id }"/>
<input type="hidden" id="userId" value="${userId}"/>
<input type="hidden" id="creatId" value="${video.creatorId }"/>
<input type="hidden" id="activityId" value="${activityId }"/>
<input type="hidden" id="tag" value="${tag }"/>
<input type="hidden" id="isNext" value="${isNext }"/>
<input type="hidden" id="nextType" value="${nextType }"/>
<input type="hidden" id="hasNext" value="${hasNext }"/>
<%--//默认调取评论数需要视频ID 用户UserID--%>
<!-- 内容 -->
<div class='main'>
	<div class='videoList1'>
		<c:if test="${video.type eq 1}">
			<div class='playBox' data-videoKey='${video.playKey }' data-videoId='${video.id }'>
				<div class="loadingGif" style="width:30px;height:30px;min-height:30px;position:absolute;left:0;top:0;display:none;z-index:999999;background-color:#000000;opacity:0.6;width:100%;height:50%;color:#ffffff;text-align:center;padding-top:50%;font-size:18px;"></div>
				<img data-videoPic="${video.videoPic }" src="" alt="" class='videoPic'>
				<em class='canPlay'></em>
			</div>
		</c:if>
		<c:if test="${video.type eq 3}">
			<div class='playBox' data-videoKey='${video.playKey }' data-videoId='${video.id }'>
				<img data-videoPic="${video.videoPic }" src="" class="videoPic-img" alt="">
			</div>
		</c:if>

		<div class='openApp'>
			<a href="javascript:void(0);" class="open-app-btn" data-userId='${video.user.id }'>用我拍播放更流畅</a>
			<div class="fr">
				<span class="zan" class="zan" data-praise="${video.praise}" data-videoid="${video.id }">
					<c:if test="${video.praise eq false}">
						<em class='loveIcon'></em>
					</c:if>
					<c:if test="${video.praise eq true}">
						<em class='loveIconOn'></em>
					</c:if>
					<p>赞</p>
				</span>
				<span class="shareBtn" data-name="${video.user.name }" data-videoid="${video.id }" data-userId='${video.user.id }' data-desc="${video.description }" data-videoPic="${video.videoPic }">
					<em class='shareIcon'></em>
					<p>转发</p>
				</span>
				<span class="next-video"><i class="next-btn"></i>
					<p>下一条</p>
				</span>
			</div>
		</div>
		<div class='listUserMsg'>
			<dl>
				<dt>
					<img class="userImg go-user-page" data-userPic="${video.user.pic }" data-userId="${video.user.id }" onclick='_czc.push(["_trackEvent","点击视频发布者头像","点击进入视频发布者详情"])' src="" onerror="this.src='<%=request.getContextPath()%>/img/icons-2x/user_icon_24px.png'" alt="">
					<c:if test="${ video.user.vipStat eq 1}">
						<em class="userV1"></em>
					</c:if><c:if test="${ video.user.vipStat eq 2}">
						<em class="userV2"></em>
					</c:if>
					<c:if test="${ video.user.vipStat eq 3}">
						<em class="userV3"></em>
					</c:if>
				</dt>
				<dd>
					<p>${video.user.name }</p>
					<p class="myDate" data-time="${video.createDate }"></p>
				</dd>
				<dd>
					<a href="javascript:;" class='playData'>
						<c:if test="${video.attentionAuthor eq 0}">
							<em class="attenBtn" data-isAttention="${video.attentionAuthor}" data-userId="${video.user.id}"></em>
						</c:if>
						<c:if test="${video.attentionAuthor eq 1}">
							<em class="attenBtnActive" data-isAttention="${video.attentionAuthor}" data-userId="${video.user.id}"></em>
						</c:if>
					</a>
				</dd>
			</dl>
		</div>
		<div class='commontBox'>
			<p class='wishWord face'>${video.description }</p><!-- 去掉tag -->
			<dl class='likeUser'>
				<div class="listUserList">
					<c:forEach var="goodList" items="${praiseUserList }">
						<dt>
							<img class='go-user-page' data-userPic="${goodList.pic}" data-userId="${goodList.id}" src="<%=request.getContextPath()%>/restwww/img/icons-2x/user_icon_24px.png" onerror="this.src='<%=request.getContextPath()%>/img/icons-2x/user_icon_24px.png'" >
							<c:if test="${ goodList.vipStat eq 1}">
								<em class="userV1"></em>
							</c:if>
							<c:if test="${ goodList.vipStat eq 2}">
								<em class="userV2"></em>
							</c:if>
							<c:if test="${ goodList.vipStat eq 3}">
								<em class="userV3"></em>
							</c:if>
						</dt>
					</c:forEach>
				</div>
				<dd class="likeNum"><a href="javascript:;">${video.praiseCount }</a></dd>
			</dl>
			<div class="commont">
				<ol class="commontHead">
					<li class="on" data-videoId="${video.id }" onclick='_czc.push(["_trackEvent","切换评论","详情页切换到评论"])'><label for="">最新评论(${video.evaluationCount})</label></li>
					<li data-videoId="${video.id }" onclick='_czc.push(["_trackEvent","切换相关视频","详情页切换到相关视频"])'><label for="">相关视频</label></li>
				</ol>
				<div class='listUserMsg'>
					<div class="evalutionList">
						<div class="evalutionListConWarp">
							<div class="evalutionListCon"></div>
						</div>
						<div class='commontFireBox'>
							<input type="text"  class='commontTxt' placeholder='登录后发表评论'>
							<a href="javascript:;" class='commontSub'>发 送 </a>
						</div>
					</div>
					
					<div class='videoListBox'>
						<ul></ul>
						<div class="hotlistMore">点击加载更多数据</div>
					</div>
				</div>
			</div>
		</div>
		<div class="integral-ad">
			<span class="integral-ad-close-warp"><i class="integral-ad-close"></i></span>

			<a href="http://www.wopaitv.com/#cnzz_name=loading&cnzz_from=integralAdvert"><img class="integral-ad-pic" src="<%=request.getContextPath()%>/img/icons-2x/gg.png" alt=""/></a>
		</div>
	</div>
</div>
<div class="skyBox">
	<img src="<%=request.getContextPath()%>/img/gobrowser.png" alt=""/>
</div>
<div class="skyBox-anz">
	<img src="<%=request.getContextPath()%>/img/anz-browser.png" alt=""/>
</div>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>