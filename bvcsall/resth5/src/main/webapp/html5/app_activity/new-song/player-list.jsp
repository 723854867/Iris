<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
<%
  WebClientUtils cu=WebClientUtils.getInstance();
  String cdnurl=cu.loadConfigUrl("html5", "interfaceurl");
  String imageurl=cu.loadConfigUrl("html5", "imageurl");
  String interfaceurl=cu.loadConfigUrl("html5", "interfaceurl");
  String chaturl=cu.loadConfigUrl("html5", "chaturl");
  if(cdnurl==null){
    cdnurl="http://api.wopaitv.com";
  }
  if(imageurl==null){
    imageurl="http://api.wopaitv.com";
  }
  if(interfaceurl==null){
    interfaceurl="http://api.wopaitv.com";
  }
  if(chaturl==null){
    chaturl="ws://chat.wopaitv.com/ws/chat";
  }
//  interfaceurl = "http://localhost:8080";
%>
<html>
<head>
    <title>新歌声榜单</title>
  <meta name="description" content="独家视频、精彩爆料、学员八卦……你想知道的都在这里" />
  <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
  <meta name="format-detection" content="telephone=no" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_activity/new-song/css/voice.css"/>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/html5/app_activity/new-song/css/common.css"/>
    <script src=" https://res.wx.qq.com/open/js/jweixin-1.0.0.js "></script>

  <script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/jquery-2.1.0.min.js"></script>

</head>
<body>


<%--图片地址--%>
<input type="hidden" id="serverUrlimg" value="<%=imageurl%>"/>
<input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>
<input type="hidden" value="${type}" id="rankType"/>


<input id="shareImg" type="hidden" value="${shareImg }" />
<input id="shareTitle" type="hidden" value="${shareTitle }" />
<input id="shareText" type="hidden" value="${shareText }" />
<input id="timestamp" type="hidden" value="${timestamp }" />
<input id="noncestr" type="hidden" value="${noncestr }" />
<input id="signature" type="hidden" value="${signature }" />

<div class="main">
  <div class="topBlock">
    <img class="logo" width="40" src="<%=request.getContextPath()%>/img/icons-2x/87.png">
    <dl>
      <p>LIVE直播</p>
      <p>中国新歌声第五战队集结</p>
    </dl>
    <a class="joinBtn" href="javascript:void(0);">立即参战</a>
  </div>
  <div class="main-1">
    <%--学员榜--%>
      <div class="banner">
        <a href="<%=interfaceurl%>/resth5/sing/home?pos=1"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/xyb_banner.png" class="xyb-banner"/></a>
      </div>
      <div class="count-down">
        倒计时：<span class="count-time-1"></span>
      </div>
      <div class='player-list'>
        <ul>
          <%--<li>--%>
          <%--<span class='pic fl'>--%>
          <%--<em class="rank-num">49</em>--%>
          <%--<img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/test.png">--%>
          <%--<span class='pic-bg'></span>--%>
          <%--</span>--%>
          <%--<span class='nice-name fl'>--%>
          <%--<h4>白若溪</h4>--%>
          <%--</span>--%>
          <%--<span class='opertion fr'><p class='user-info'><em class='user-icon'></em>人气值：143534</p></span>--%>
          <%--</li>--%>
        </ul>
      </div>
  </div>

  <div class="main-2">
    <%--综艺榜--%>
    <div class="banner">
      <a href="<%=interfaceurl%>/resth5/sing/home?pos=5"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/zyb_banner.png" class="zyb-banner"/></a>
    </div>
    <div class="count-down">
    倒计时：<span class="count-time-2"></span>
    </div>
    <div class='player-list'>
      <ul>
        <%--<li>--%>
            <%--<span class='pic fl'>--%>
                <%--<em class="rank-num">49</em>--%>
                <%--<img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/test.png">--%>
                <%--<span class='pic-bg'></span>--%>
            <%--</span>--%>
            <%--<span class='nice-name fl'>--%>
                <%--<h4>白若溪</h4>--%>
            <%--</span>--%>
            <%--<span class='opertion fr'><p class='user-info'><em class='user-icon'></em>人气值：143534</p></span>--%>
        <%--</li>--%>
      </ul>
    </div>
  </div>
  <div class="main-3">
    <%--主播榜--%>
    <div class="banner">
      <a href="<%=interfaceurl%>/resth5/sing/home?pos=4"><img src="<%=request.getContextPath()%>/html5/app_activity/new-song/img/zbb_banner.png" class="zbb-banner"/></a>
    </div>
    <div class="count-down">
      倒计时：<span class="count-time-3"></span>
    </div>
      <div class='player-list'>
        <ul>

        </ul>
      </div>
  </div>
</div>
<div class="skyBox">
  <img src="<%=request.getContextPath()%>/img/mask.png" alt=""/>
</div>


</body>
</html>
<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/common.js"></script>
<script src="<%=request.getContextPath()%>/html5/app_activity/new-song/js/player-list.js"></script>
<%--<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>--%>

