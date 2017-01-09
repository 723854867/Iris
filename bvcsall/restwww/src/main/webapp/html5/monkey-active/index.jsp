<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.busap.vcs.webcomn.util.WebClientUtils"%>
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
%>
<!DOCTYPE html>
<html>
  <head>
    <title>测2016猴年运势 </title>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/html5/monkey-active/css/index.css?t=2">
    <script src="<%=request.getContextPath()%>/html5/monkey-active/js/libs/jquery-1.11.1.min.js"></script>
    <script src="<%=request.getContextPath()%>/html5/monkey-active/js/index.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
  </head>
  <body>
  <input type="hidden" id="serverUrlvid" value="<%=cdnurl%>"/>
  <input type="hidden" id="interfaceurl" value="<%=interfaceurl%>"/>

  <input type="hidden" id="timestamp" value="${ timestamp}"/>
  <input type="hidden" id="noncestr" value="${ noncestr}"/>
  <input type="hidden" id="signature" value="${ signature}"/>
    <div class="main">
      <audio src="<%=request.getContextPath()%>/html5/monkey-active/media/2.mp3" class="audio1" autoplay="autoplay" loop="loop"></audio>
      <audio src="<%=request.getContextPath()%>/html5/monkey-active/media/1.mp3" class="audio2"></audio>
      <div class="draw-warp">
        <img src="<%=request.getContextPath()%>/html5/monkey-active/img/draw.png" class="draw1">
        <img src="<%=request.getContextPath()%>/html5/monkey-active/img/draw-move.gif" class="draw2">
        <img src="<%=request.getContextPath()%>/html5/monkey-active/img/monkeytips.png" class="monkey-tips">
      </div>
        <img src="<%=request.getContextPath()%>/html5/monkey-active/img/monkey-bg.jpg" class="bg-img">
      <div class="stick-warp"><img src="<%=request.getContextPath()%>/html5/monkey-active/img/cloud.png" class="cloud">
        <div class="stick"><img src="<%=request.getContextPath()%>/html5/monkey-active/img/stick.png"></div>
      </div>
      <div class="result-1">
        <div class="result-1-1">
          <div class="result-1-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/result1.png" class="result-1-pic" alt=""/>
          </div>
          <div class="share-warp">
            <a href="javascript:;" class="share-btn"></a>
            <p class="share-prompt">
              <a href="javascript:;" class="result-btn"></a>
            </p>
          </div>
        </div>
        <div class="result-1-2">
          <div class="result-2-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/result2.png" class="result-2-pic" alt=""/>
          </div>
          <div class="share-warp">
            <a href="javascript:;" class="share-btn"></a>
            <p class="share-prompt">
              <a href="javascript:;" class="result-btn"></a>
            </p>
          </div>
        </div>
        <div class="result-1-3">
          <div class="result-3-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/result3.png" class="result-3-pic" alt=""/>
          </div>
          <div class="share-warp">
            <a href="javascript:;" class="share-btn"></a>
            <p class="share-prompt">
              <a href="javascript:;" class="result-btn"></a>
            </p>
          </div>
        </div>
        <div class="result-1-4">
          <div class="result-4-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/result4.png" class="result-4-pic" alt=""/>
          </div>
          <div class="share-warp">
            <a href="javascript:;" class="share-btn"></a>
            <p class="share-prompt">
              <a href="javascript:;" class="result-btn"></a>
            </p>
          </div>
        </div>
        <div class="result-1-5">
          <div class="result-5-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/result5.png" class="result-5-pic" alt=""/>
          </div>
          <div class="share-warp">
            <a href="javascript:;" class="share-btn"></a>
            <p class="share-prompt">
              <a href="javascript:;" class="result-btn"></a>
            </p>
          </div>
        </div>
        <div class="result-1-6">
          <div class="result-6-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/result6.png" class="result-6-pic" alt=""/>
          </div>
          <div class="share-warp">
            <a href="javascript:;" class="share-btn"></a>
            <p class="share-prompt">
              <a href="javascript:;" class="result-btn"></a>
            </p>
          </div>
        </div>
      </div>
      <div class="result-2">
        <div class="result-2-1">
          <div class="prize-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/solve11.png" alt=""/>
          </div>
          <div class="prize-hand">
            <span class="left-hand"></span>
            <span class="right-hand"></span>
          </div>
          <a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653" class='down-bnt'></a>
        </div>
        <div class="result-2-2">
          <div class="prize-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/solve22.png" alt=""/>
          </div>
          <div class="prize-hand">
            <span class="left-hand"></span>
            <span class="right-hand"></span>
          </div>
          <a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653" class='down-bnt'></a>
        </div>
        <div class="result-2-3">
          <div class="prize-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/solve33.png" alt=""/>
          </div>
          <div class="prize-hand">
            <span class="left-hand"></span>
            <span class="right-hand"></span>
          </div>
          <a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653" class='down-bnt'></a>
        </div>
        <div class="result-2-4">
          <div class="prize-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/solve44.png" alt=""/>
          </div>
          <div class="prize-hand">
            <span class="left-hand"></span>
            <span class="right-hand"></span>
          </div>
          <a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653" class='down-bnt'></a>
        </div>
        <div class="result-2-5">
          <div class="prize-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/solve55.png" alt=""/>
          </div>
          <div class="prize-hand">
            <span class="left-hand"></span>
            <span class="right-hand"></span>
          </div>
          <a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653" class='down-bnt'></a>
        </div>
        <div class="result-2-6">
          <div class="prize-pic">
            <img src="<%=request.getContextPath()%>/html5/monkey-active/img/souce/solve66.png" alt=""/>
          </div>
          <div class="prize-hand">
            <span class="left-hand"></span>
            <span class="right-hand"></span>
          </div>
          <a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.busap.myvideo&g_f=991653" class='down-bnt'></a>
        </div>
      </div>
      <div class="share-tips skyBox">
        <img src="<%=request.getContextPath()%>/html5/monkey-active/img/share-tips.png" alt=""/>
      </div>
    </div>
    <script>
    jQuery.gameinit();
    init();
    </script>
    <script>var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256005299'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256005299' type='text/javascript'%3E%3C/script%3E"));</script>
  </body>
</html>