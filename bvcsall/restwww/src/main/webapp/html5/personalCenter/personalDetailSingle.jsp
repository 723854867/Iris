<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>个人详情</title>
	<meta name="viewport" content="width=device-width,target-densitydpi=high-dpi,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="format-detection" content="telephone=no,email=no,address=no" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Pramgma" content="no-cache" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/personalCenter/personalDetailSingle.css?t=<%=new java.util.Date().getTime()%>">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/Connect.css?t=<%=new java.util.Date().getTime()%>">
</head>
<body>
	<div class="header">
		<a class="head-logo" href="http://cdn.wopaitv.com/restwww/html5/index"></a>
		<a href="http://www.wopaitv.com" id="downloadBtn" class="js-span-a btn-bg1 btn-download">
			<i class="icon-download"></i> 下载我拍
		</a>
	</div>
	
	<div class="personalBg">
		<ul>
			<li><strong>留几手粉丝团</strong><img src="../../../img/icons-2x/prof_icon_woman.png" alt=""><img src="../../../img/icons-2x/user_icon_yellow.png" alt=""></li>
			<li>世界那么大，我想去看看…</li>
			<li>关注 100   |   粉丝 1</li>
		</ul>
	</div>

	<div class="lineBg">
		<p>
			<a class="fl" href="#">
				<img src="../../../img/icons-2x/Forward-48.png" alt="">
			</a>
			<img src="../../../img/personalBg/personalPortriat.jpg" alt="">
			<span>
				搞笑祖奶奶转发了草帽的舞台的视频
			</span>
		</p>
	</div>

	<div class='activeUseList'>
		<div class='videoList1'>
			<ul>
				<li>
					<div class='listUserMsg'>
						<dl>
							<dt><img src="../../../img/icons-2x/icon_tabbar_profile_sel.png" alt="头像"></dt>
							<dd>
								<p>草帽的舞台</p>
								<p>03-09 09:19</p>
							</dd>
							<dd>
								<em class='playIcon'></em><span class='playData'>43434</span>
							</dd>
						</dl>
					</div>
					<div class='playBox'>
						<video src="../../../data/3.mp4" controls></video>
					</div>
					<div class='shareBox'>
						<div class='zan'>
							<em></em>
							<span>4343</span>
						</div>
						<div class='commit'>
							<em></em>
							<span>4343</span>
						</div>
						<div class='Forward'>
							<em></em>
							<span>4343</span>
						</div>
					</div>
				</li>
				<li>
					<div class='listUserMsg'>
						<dl>
							<dt><img src="../../../img/icons-2x/icon_tabbar_profile_sel.png" alt="头像"><em class='addV'></em></dt>
							<dd>
								<p>草帽的舞台</p>
								<p>03-09 09:19</p>
							</dd>
							<dd>
								<em class='playIcon'></em><span class='playData'>43434</span>
							</dd>
						</dl>
					</div>
					<div class='playBox'>
						<video src="../../../data/3.mp4" controls></video>
					</div>
					<div class='shareBox'>
						<div class='zan'>
							<em></em>
							<span>4343</span>
						</div>
						<div class='commit'>
							<em></em>
							<span>4343</span>
						</div>
						<div class='Forward'>
							<em></em>
							<span>4343</span>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>

	<div class="downloadApp">
		<a href="#">下载我拍App，查看更多</a>
	</div>
</body>
</html>
<script type="text/javascript" data-main="<%=request.getContextPath()%>/js/views/personalDetailSingle" src="<%=request.getContextPath()%>/js/libs/require.js"></script>