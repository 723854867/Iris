package com.busap.vcs.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageCompressUtil {

	public static void saveMinPhoto(String srcURL, String deskURL,
			double comBase,
			double scale)  {

		try {
			File srcFile = new java.io.File(srcURL);
			Image src = ImageIO.read(srcFile);
			int srcHeight = src.getHeight(null);
			int srcWidth = src.getWidth(null);
			int deskHeight = 0;
			int deskWidth = 0;
			double srcScale = (double) srcHeight / srcWidth;
			if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
				if (srcScale >= scale || 1 / srcScale > scale) {
					if (srcScale >= scale) {
						deskHeight = (int) comBase;
						deskWidth = srcWidth * deskHeight / srcHeight;
					} else {
						deskWidth = (int) comBase;
						deskHeight = srcHeight * deskWidth / srcWidth;
					}
				} else {
					if ((double) srcHeight > comBase) {
						deskHeight = (int) comBase;
						deskWidth = srcWidth * deskHeight / srcHeight;
					} else {
						deskWidth = (int) comBase;
						deskHeight = srcHeight * deskWidth / srcWidth;
					}
				}
			} else {
				deskHeight = srcHeight;
				deskWidth = srcWidth;
			}

			BufferedImage tag = new BufferedImage(deskWidth, deskHeight,
					BufferedImage.TYPE_3BYTE_BGR);

			tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); // ������С���ͼ
			
			File temp = new File(deskURL);
			if (!temp.getParentFile().exists()){
				temp.getParentFile().mkdirs();
			}
			FileOutputStream deskImage = new FileOutputStream(deskURL); // ������ļ���
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
			encoder.encode(tag); 
			deskImage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]) throws Exception {
		ImageCompressUtil.saveMinPhoto("D:/123.png", "D:/sd/11.jpg", 800, 0.9d);
	}

}