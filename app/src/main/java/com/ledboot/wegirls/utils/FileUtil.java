package com.ledboot.wegirls.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.ledboot.wegirls.Boot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.CRC32;

public class FileUtil {

	public static final String ROOT = "/WorldGo";	

	public static final String IMAGE_PATH = ROOT+"/images";//要清理缓存
	
	public static final String LOG_PATH = ROOT+"/log";//要清理缓存

	public static final String USER_ICON_DIR_PATH = ROOT + "/userIcon";//用户头像目录
	
	public static final String GROUP_IMAGE = ROOT+"/groupIcon";//群头像目录
	
	private static final String MICRO_MSG = ROOT+"/microMsg";
	
	public static final String MICRO_IMAGE = MICRO_MSG+"/image";//不清理
	
	public static final String MICRO_VIDEO = MICRO_MSG+"/video";//清理
	
	public static final String MICRO_VOICE = MICRO_MSG+"/voice";//清理(按时间清理)
	
	public static final String MICRO_CARMERA = MICRO_MSG+"/carmera";//清理
	
	public static final String MICRO_MAP = MICRO_MSG+"/map";//清理
	
	public static final String DUMP = ROOT+"/dump";//全部清掉
	
	public static final String DUMP_DATA = DUMP+"/AppData";//要清理
	
	public static final String DUMP_ANR = DUMP+"/ANR";
	
	public static final String DUMP_LOGCAT = DUMP+"/logcat";

	public static final String SAVE_FILE = ROOT+"/WorldGo";//不清理

	public static final String TEMP_IMAGE = ROOT+"/tempImage";//清理
	
	public static final String WAOWOO = ROOT+"/waowoo";//不清理
	
	public static final String GoCache=ROOT+"/GoCache";//清理掉(按时间清理)
	
	public static final String IMAGE_WALL = ROOT+"/imagewall";
	
	public static final String DOWNLOAD=ROOT+"/download";//清理
	
	public static final String GO_BAR_NOTE_IMAGE = ROOT+"/gobarNote";//走吧游记图片目录
	
	public static final String GO_BAR_ACTIVITY_IMAGE = ROOT+"/gobarActivity";//走吧活动图片目录
	
	public static final String USER_BG = ROOT+"/userbg";//用户背景

	public static File createFileDir(String dirPath) {
		File file = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			file = new File(Environment.getExternalStorageDirectory(), dirPath);
		} else {
			file = new File(Boot.getInstance().getApplicationContext().getCacheDir(), dirPath);
		}

		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public static File createFileDir() {
		File file = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			file = new File(Environment.getExternalStorageDirectory(),
					IMAGE_PATH);
		} else {
			file = file = new File(Environment.getExternalStorageDirectory(),
					IMAGE_PATH);
		}

		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	// 拍照获取uri
	public static Uri cameraImageUri() {
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString() + ".jpg";
		File tempFile = createFileDir();
		File file = new File(tempFile, fileName);
		Uri uri = Uri.fromFile(file);
		return uri;
	}

	// 拍照获取图片保存的路径
	public static File cameraImageFile() {
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString() + ".jpg";
		File tempFile = createFileDir();
		File file = new File(tempFile, fileName);
		return file;
	}
	
	public static File cameraImageFile(String dir){
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString() + ".jpg";
		File tempFile = createFileDir(dir);
		File file = new File(tempFile, fileName);
		return file;
	}

	/**
	 * 获取目录的总大小(单位MB)
	 * 
	 * @param file
	 * @return
	 */
	public static long getDirSize(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				for (File f : children) {
					size += getDirSize(f);
				}
				size = size / (1024 * 1024);
				long totalSize = (long) Math.ceil(size);
				return totalSize;
			} else {// 如果是文件则直接返回其大小,以“兆”为单位
				return file.length();
			}
		} else {
			return 0;
		}
	}

	/**
	 * 文件与目录的剪切操作<br>
	 * <><><> *注：存在与文件复制同样的问题，因为该方法调用了文件复制操作这个方法！<br>
	 * <><><> *不过有一点可以放心，就是即使剪切操作失败了，也不会把原始的文件给删除！
	 * 
	 * @param source_file
	 *            原始文件（目录）
	 * @param target_file
	 *            目标文件（目录）
	 * @return 复制成功返回true，失败返回false
	 */
	public static boolean cutFile(File source_file, File target_file) {
		// 如果复制成功，则删除源文件（这就实现了剪切操作）
		if (copyFile(source_file, target_file)) {
			deleteFile(source_file);
			return true;
		} else {
			// 复制失败，则剪切也失败了，当然不会删除源文件
			return false;
		}
	}

	/**
	 * 文件与目录的复制操作<br>
	 * <><><> *注：存在的问题为，若复制目录到一半时操作失败了，那么已经复制过去的文件没有被删除！
	 * 
	 * @param source_file
	 *            原始文件（目录）
	 * @param target_file
	 *            目标文件（目录）
	 * @return 复制成功返回true，失败返回false
	 */
	public static boolean copyFile(File source_file, File target_file) {
		if (source_file.isDirectory()) {
			// 获取当前目录下的所有文件或目录
			File[] dir = source_file.listFiles();
			// 逐个拷贝到顶层目录下
			for (int i = 0; i < dir.length; i++) {
				File sFile = dir[i];
				File top_file = null;
				if (sFile.isDirectory()) {
					top_file = new File(target_file, source_file.getName());
					top_file.mkdir();
				} else {
					top_file = target_file;
				}
				if (!copyFile(sFile, top_file))
					return false;
			}
			// 复制成功
			return true;
		} else {
			// 复制文件
			try {
				File top_file = null;
				if (target_file.isDirectory()) {
					top_file = new File(target_file, source_file.getName());
				} else {
					top_file = target_file;
				}
				// 文件输入输出流
				FileInputStream fis = new FileInputStream(source_file);
				FileOutputStream fos = new FileOutputStream(top_file);
				// 缓存
				byte[] buffer = new byte[1024];
				int c = 0;
				while ((c = fis.read(buffer)) != -1)
					fos.write(buffer, 0, c);
				// 关闭流
				fis.close();
				fos.close();
			} catch (FileNotFoundException e) {
				// 文件未找到异常
				System.out.println("文件未找到: " + e);
				return false;
			} catch (IOException e) {
				// 其他IO异常
				System.err.println("IOException: " + e);
				return false;
			}
			// 复制成功
			return true;
		}
	}

	/**
	 * 删除文件（目录）<br>
	 * <><><> *本例主要用于剪切操作 *@param source_file 要删除的文件（目录）
	 */
	public static void deleteFile(File source_file) {
		// 如果是目录，则
		if (source_file.isDirectory()) {
			// 获取当前目录下的所有文件或目录
			File[] files = source_file.listFiles();
			for (int i = 0; i < files.length; i++) {
				// 对子目录或文件进行同样的操作
				if (files[i].isDirectory()) {
					deleteFile(files[i]);
				} else
					files[i].delete();
			}
			// 删除父目录
			source_file.delete();
		} else {
			// 删除源文件
			source_file.delete();
		}
	}

	/**
	 * 获取文件的后缀名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {
		String ext = "";
		if ((filename != null) && (filename.trim().length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				ext = filename.substring(dot + 1);
			}
		}
		return ext;
	}

	/**
	 * 获取文件的CRC32
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileCRC32(String filePath) {
		int BUFFER = 8192;
		CRC32 crc32 = new CRC32();
		FileInputStream fileinputstream = null;
		String crc = "";
		int count;
		try {
			fileinputstream = new FileInputStream(filePath);
			byte data[] = new byte[BUFFER];
			while ((count = fileinputstream.read(data, 0, BUFFER)) != -1) {
				crc32.update(data, 0, count);
			}
			crc = Long.toHexString(crc32.getValue()).toUpperCase();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileinputstream != null) {
				try {
					fileinputstream.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		return crc;
	}
	
	/**
	 * 获取gif图片的缓存
	 * @param url
	 * @return
	 */
	public static File getGifFile(String url) {
		// 将url的hashCode作为缓存的文件名
		String filename = String.valueOf(url.hashCode());
		// Another possible solution
		// String filename = URLEncoder.encode(url);
		File cacheDir=createFileDir(IMAGE_PATH);
		File f = new File(cacheDir, filename);
		return f;
	}
	
	/**
	 * 判断手机是否有sd卡
	 * @return
	 */
	public static boolean existExternalStorage(){
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}

	}
	
	
	/**
	 * 返回一个共享的目录地址，不存在则返回null
	 * @return
	 */
	public static String getCommonSharedPathDir(Context cxt) {
		File commonSharedPath = new File(cxt.getFilesDir()+"/CommonShared");
		if(commonSharedPath.exists() && !commonSharedPath.isDirectory()) {
			commonSharedPath.delete();
		}
		
		if(!commonSharedPath.exists()) {
			commonSharedPath.mkdirs();
			try {
				Runtime.getRuntime().exec("chmod 777 "+commonSharedPath.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				commonSharedPath = null;
			}
		}
		
		if(commonSharedPath != null && commonSharedPath.exists()) {
			return commonSharedPath.getAbsolutePath();
		}
		
		return null;
	}
	
	/**
	 * 让一个在CommonSharedDir中的文件开放共享
	 * @return
	 */
	public static boolean shareCommonFile(Context cxt , String fileName) {
		String SharedDir = getCommonSharedPathDir(cxt);
		if(SharedDir == null) return false;
		
		File file = new File(SharedDir+"/"+fileName);
		if(file == null || !file.getParent().equals(SharedDir) || !file.exists()) return false;
	
		try {
			Runtime.getRuntime().exec("chmod 666 "+file.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 让所有在CommonSharedDir中的文件开放共享
	 * @return
	 */
	public static boolean shareAllCommonFile(Context cxt) {
		String SharedDir = getCommonSharedPathDir(cxt);
		if(SharedDir == null) return false;
	
		try {
			Runtime.getRuntime().exec("chmod 777 -R "+SharedDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	



}