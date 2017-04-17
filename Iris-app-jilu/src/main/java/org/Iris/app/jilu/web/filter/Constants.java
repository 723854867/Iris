package org.Iris.app.jilu.web.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 无需过滤的页面
 * @author 樊水东
 * 2017年3月24日
 */
public class Constants {

	public static List<String> noFilterPage;
	
	public static void init(){
		noFilterPage = new ArrayList<String>();
		//配置需要过滤的页面路径
		noFilterPage.add("/login.html");
		noFilterPage.add("/login.jsp");
		noFilterPage.add("/anno/index.html");
	}
}
