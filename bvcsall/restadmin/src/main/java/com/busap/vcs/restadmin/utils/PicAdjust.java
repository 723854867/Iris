package com.busap.vcs.restadmin.utils;

import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

public class PicAdjust {
	/*** 
     * 功能 :调整图片大小 开发：wuyechun 2011-7-22 
     * @param srcImgPath 原图片路径 
     * @param distImgPath  转换大小后图片路径 
     * @param width   转换后图片宽度 
     * @param height  转换后图片高度 
     */  
    public static void resizeImage(String srcImgPath, String distImgPath, int width, int height) throws IOException {  
    	Thumbnails.of(srcImgPath).forceSize(width, height).toFile(distImgPath);
    } 
    
    public static void main(String []args){  
        try { 
        	PicAdjust.resizeImage("C:/home/maishi/videoPic/2015-02-04/2015-02-04_104418_88.jpg","C:/home/maishi/videoPic/2015-02-04/1.jpg",640,640);
        } catch (IOException e) {  
            System.out.println("图片转换出现异常！");  
        }  
          
    }  
}