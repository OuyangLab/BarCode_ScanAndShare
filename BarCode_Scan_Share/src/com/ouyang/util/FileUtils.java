package com.ouyang.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;


public class FileUtils {

	/**
	 * 判断SD
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 创建根目录* 
	 * @param path
	 *            目录路径
	 */
	public static void createDirFile(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); 
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
			return sdDir.toString();
		}
		return null;
	}
	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 创建的文件 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 * 删除文件* 
	 * @param folderPath
	 *            文件夹的路径
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件的路 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		int mLength = tempList.length;
		for (int i = 0; i < mLength; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 * 获取文件的Uri
	 * 
	 * @param path
	 *            文件的路�?	 * @return
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	/**
	 * 换算文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "未知大小";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 通过路径获得文件名字
	 * 
	 * @param path
	 * @return
	 */
	public static String getPathByFullPath(String fullpath) {
		return fullpath.substring(0, fullpath.lastIndexOf(File.separator));
	}

	/**
	 * 通过路径获得文件名字
	 * 
	 * @param path
	 * @return
	 */
	public static String getNameByPath(String path) {
		return path.substring(path.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 通过判断文件是否存在
	 * 
	 * @param path
	 * @return
	 */

	public static boolean isFileExists(String path) {
		if (null == path || path.length() == 0)
			return false;
		try {
			File file = new File(path.trim());
			if (!file.exists()) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	

	public static void writeContentFromFile(Context context, String fileName,
			byte[] content) {
		try {
			FileOutputStream outStream = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			outStream.write(content);
			outStream.close();
			outStream = null;
		} catch (IOException e) {
			return;
		}
	}

	public static void writeContentFromFile(Context context, String fileName,
			String content) {
		try {
			FileOutputStream outStream = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			outStream.write(content.getBytes());
			outStream.close();
			outStream = null;
		} catch (IOException e) {
			return;
		}
	}

	public static void writeContentFromFileWithAppend(Context context,
			String fileName, String content) {
		try {
			FileOutputStream outStream = context.openFileOutput(fileName,
					Context.MODE_APPEND);
			outStream.write(content.getBytes());
			outStream.close();
			outStream = null;
		} catch (IOException e) {
			return;
		}
	}

	public static String readContentFromFile(Context context, String fileName) {
		try {
			FileInputStream inStream = context.openFileInput(fileName);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			stream.close();
			inStream.close();
			buffer = null;
			inStream = null;
			return stream.toString();
		} catch (IOException e) {
			return null;
		}
	}
}

