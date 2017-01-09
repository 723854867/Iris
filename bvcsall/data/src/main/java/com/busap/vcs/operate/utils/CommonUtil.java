package com.busap.vcs.operate.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CommonUtil {
	public static boolean isPhone(String s){  //因为有国际号码，所以只判断是否为纯数字
		if (StringUtils.isBlank(s)) {
			return false;
		}
		 Pattern pattern = Pattern.compile("[0-9]*");
		    return pattern.matcher(s).matches();   
	}  
	
	public static void main(String[] args) {
		System.out.println(isPhone("sldkfjsldhsllklkj123"));
	}
	
}
