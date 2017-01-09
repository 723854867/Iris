<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html> 
<html lang="zh-cn"> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 

<style type="text/css" media="screen"> 
	body,h1,h2,h3,h4,h5,h6,blockquote,p,pre,dl,dd,menu,ol,ul,caption,th,td,form,fieldset,legend,input,button,textarea{font-family:'Microsoft Yahei';margin:0;padding:0;background-color:black;color:white}
</style> 
<script type="text/javascript" src="../js/jquery.min.js"></script>
<script type="text/javascript"> 
	$(function() {
		showCount();
		setInterval(showCount,300000);
	});
	
	function showCount(){
		$.get("registcount",function(result){
			if(result.count){
				$("#registNum").html(result.count);
				$("#lastTime").html(result.date);
			}
		});
	}
</script>  
</head> 
<body> 
<div > 
	<img src="../images/wopai.png" style="padding-top:50px;padding-left:20px"/>
	<div>
		<div style="font-size:50px;padding-top:10px;padding-left:150px">注册量</div>
		<div id="registNum" align="center" style="font-size:120px;padding-top:10px">120,000</div>
		<div id="lastTime" style="font-size:30px;padding-top:10px;padding-right:40px;float:right">2015.04.23 00:00</div>
	</div>
</div> 
</body> 
</html>