package com.ouyang.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;




public class ImageUtils {
	private static final String WINDOW_SERVICE = null;

	public static String savePhotoToSDCard(Bitmap bitmap, String filedir,
			String paramFilename) {
		if (!FileUtils.isSdcardExist()) {
			return null;
		}
		FileUtils.createDirFile(filedir);
		int quality = 60; // 缩略图没必要高质量
		String filename = paramFilename;

		if (TextUtils.isEmpty(paramFilename)) {
			filename = UUID.randomUUID().toString() + ".jpg";
			quality = 100;
		}

		String newFilePath = filedir + filename;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(
					newFilePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
					fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e1) {
			return null;
		}
		return newFilePath;
	}

	/**
	 * 根据比例缩放图片
	 * 
	 * @param screenWidth
	 *            手机屏幕的宽度
	 * @param filePath
	 *            图片的路径
	 * @param ratio
	 *            缩放比例
	 * @return
	 */
	public static Bitmap CompressionPhoto(float screenWidth, String filePath,
			int ratio, Context context) {
		Bitmap bitmap = /* ImageUtils.getBitmapFromPath(filePath); */
		ImageUtils.decodeBitmapFromPath(filePath, context);
		Bitmap compressionBitmap = null;
		float scaleWidth = screenWidth / (bitmap.getWidth() * ratio);
		float scaleHeight = screenWidth / (bitmap.getHeight() * ratio);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		try {
			compressionBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (Exception e) {
			return bitmap;
		}
		return compressionBitmap;
	}

	/**
	 *按屏幕宽高缩放图片 (防止发生内存溢出)
	 * 
	 * @param pathName
	 *             图片路径参数
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap decodeBitmapFromPath(String pathName, Context context) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
		int imageWidth = opts.outWidth;
		int imageHeight = opts.outHeight;
		/* ��ȡ��Ļ��� */
		WindowManager wm = (WindowManager) context
				.getSystemService(WINDOW_SERVICE);
		Display screen = wm.getDefaultDisplay();
		int screenWidth = screen.getWidth();
		int screenHeight = screen.getHeight();
		/* 获取屏幕宽高 */
		int scale = 1;
		int scaleX = imageWidth / screenWidth;
		int scaleY = imageHeight / screenHeight;
		if (scaleX > scaleY && scaleY > 1) {// 按宽度缩放
			scale = scaleX;
		}
		if (scaleY > scaleX && scaleX > 1) {// 按高度缩放
			scale = scaleY;
		}
		/* 计算缩放比 */
		opts = new Options();
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		try {
			bitmap = BitmapFactory.decodeFile(pathName, opts);
		} catch (OutOfMemoryError error) {
			/*
			 * 若按屏幕比例缩放后，仍会报内存溢出，则设置为原缩放采样比例的2倍,3倍,5倍,7倍和9倍.
			 * 则图片采样尺寸变为原来的1/2*1/2(宽 * 高),1/3*1/3,1/5*1/5,1/7*1/7,1/9*1/9
			 */
			try {
				opts.inSampleSize = scale * 2;
				bitmap = BitmapFactory.decodeFile(pathName, opts);
			} catch (OutOfMemoryError e) {
				try {
					opts.inSampleSize = scale * 3;
					bitmap = BitmapFactory.decodeFile(pathName, opts);
				} catch (OutOfMemoryError eMemoryError) {
					try {
						opts.inSampleSize = scale * 5;
						bitmap = BitmapFactory.decodeFile(pathName, opts);
					} catch (OutOfMemoryError e1) {
						try {
							opts.inSampleSize = scale * 7;
							bitmap = BitmapFactory.decodeFile(pathName, opts);
						} catch (OutOfMemoryError e2) {
							opts.inSampleSize = scale * 9;
							bitmap = BitmapFactory.decodeFile(pathName, opts);
						}
					}
				}
			}
		}
		/* 释放内存 */
		opts = null;
		wm = null;
		screen = null;
		return bitmap;
	}
}
