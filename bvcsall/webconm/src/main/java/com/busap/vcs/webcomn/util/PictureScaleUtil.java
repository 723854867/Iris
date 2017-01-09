package com.busap.vcs.webcomn.util;

import java.util.Date;

import net.coobird.thumbnailator.Thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PictureScaleUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(PictureScaleUtil.class);
	
	public static void region(int x,int y ,int width,int height,String inpath,String outpath){
		try{
			Thumbnails.of(inpath).sourceRegion(x,y,width,height).size(width, height).toFile(outpath);
		}catch(Exception e){
			logger.error("图片裁剪错误",e);
		}
	}
	
	public static void main(String []str){
		long s = new Date().getTime();
		for (int i = 0; i < 1; i++) {
			PictureScaleUtil.region(100, 100, 400, 400, "D:/88.jpg", "D:/outpic"+i+".jpg");
			System.out.println("end");
		}
		long e = new Date().getTime();
		System.out.println("共耗时="+(e-s)/1000);
	}

}
