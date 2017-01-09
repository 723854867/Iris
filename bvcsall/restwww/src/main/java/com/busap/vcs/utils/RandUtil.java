package com.busap.vcs.utils;

import java.util.Random;

public class RandUtil {
	private static Random rand = new Random(); 
	private static final int max = 999999;
	private static final int min = 100000;

	private static  String base = "012356789";   
    private static Random random = new Random();
	/**
	 * @param args
	 */
	public static void main(String[] args) { 
		for(int i=0;i<20;i++){
			Random random = new Random();   
		    int l = random.nextInt(2);
			System.out.println(l);
		}
	}
	 
	public static String getRandomString(String name) { 
	    int l = random.nextInt(2)+3;
	    StringBuffer sb = new StringBuffer(name);   
	    for (int i = 0; i < l; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  

	public static String getSixRandCode() {   
		int tmp = Math.abs(rand.nextInt());
		return String.valueOf((tmp % (max - min + 1) + min)); 
	} 
}
