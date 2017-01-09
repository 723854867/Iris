package com.busap.vcs.service.impl;



public class SolrUtil {
	
	public static String convertSearchName(String name) {
		
//		boolean dataFlg=false;
		boolean chineseFlg=false;
		boolean englishFlg=false;
		
//		try {//该段主要是处理分词时数字匹配问题
//			Long nameLong=Long.parseLong(name);
//			dataFlg=true;
////			name=name+"*";
//		}catch(Exception e){
//			
//		}
		
	    char[] chars = name.toCharArray();   
	    for (int i = 0; i < chars.length; i++) {  
	    	int a=(int)chars[i]; 
	    	if(a>=0&&a<=126) {
	    		englishFlg=true;
	    	}else {
	    		chineseFlg=true;
	    		break;
	    	}
	    }
	    
	    if(chineseFlg) {
	    	if(name.length()==1) {
	    		name="*"+name+"*";
	    	}
    		
    	}else {
    		
    		if(name.length()==1) {
	    		name="*"+name+"*";
	    	}else {
	    		
	    		try {//数字加星
	    			Long nameLong=Long.parseLong(name);
	    			name="*"+name+"*";
	    		}catch(Exception e){
	    			
	    		}
	    		
//	    		if(name.startsWith("#")||name.endsWith("#")) {
//	    			
//	    		}else {
////	    			name="*"+name+"*";
//	    		}
	    	}
    	}
		return name;
	}

}
