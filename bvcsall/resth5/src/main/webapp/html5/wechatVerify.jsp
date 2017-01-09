<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %><%@ page import="com.busap.vcs.utils.SpellComparator"%><%@ page import="java.util.*"%><%@ page import="org.apache.commons.codec.digest.DigestUtils"%><%
	System.out.println(request.getRemoteAddr());
 	System.out.println(request.getQueryString());
	String signature = request.getParameter("signature");
	String timestamp = request.getParameter("timestamp");
	String nonce = request.getParameter("nonce");
	String echostr = request.getParameter("echostr");
	System.out.println(timestamp+"************************");
	System.out.println(nonce+"************************");
	System.out.println(echostr+"************************");
	System.out.println(signature+"************************");
	  ArrayList list = new ArrayList();
	  list.add(nonce);
	  list.add(timestamp);
	  list.add("wopai");
	  String str = "";
	  Collections.sort(list,new SpellComparator());
	  for (int i = 0; i < list.size(); i++) {
		   str += list.get(i);
	  }
	  String result = DigestUtils.shaHex(str);
	  System.out.println(result+"************************");
	  if (result.equals(signature)){
		  System.out.println("equals");
		  out.write(echostr);
		  out.flush();
	  } else {
		  out.write("error");
		out.flush();
	  }
%>