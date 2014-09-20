package cn.ls.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class SaveImageFile {

	private static String mSDRootPath = Environment
			.getExternalStorageDirectory().getPath();// SD卡根目录
	private static String mCacheRootPath = null;// 手机缓存根目录
	private static final String FOLDER_NAME = "/PlatPhoto";// 文件夹名称

	public SaveImageFile(Context context) {
		mCacheRootPath = context.getCacheDir().getPath();
	}

	/**
	 * 获取存储的目录
	 * 
	 * @return
	 */

	public String getSaveDirectory() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? mCacheRootPath + FOLDER_NAME
				: mSDRootPath + FOLDER_NAME;
	}

	/**
	 * 获得位图
	 * 
	 * @return
	 */
	public Bitmap getBitmap(String fileName) {

			return BitmapFactory.decodeFile(getSaveDirectory() + File.separator
					+ fileName);// 返回制定位置构造的位图
	
	}

	/**
	 * 存储位图文件
	 * 
	 */
	public void saveBitmap(String fileName, Bitmap bitMap) {
		if (bitMap == null) {
			return;
		}
		String path = getSaveDirectory();
		File fileHolder = new File(path);
		if (!fileHolder.exists()) {
			fileHolder.mkdir();// 如果不存在就创建文件夹
		}
		File file = new File(path + File.separator + fileName);
		try {
			file.createNewFile();// 创建新文件
			FileOutputStream out = new FileOutputStream(file);
			bitMap.compress(CompressFormat.JPEG, 100, out);// 存储图片
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 检查文件是否已存在
	 */
	public boolean bitmapIsExist(String fileName) {
		return new File(getSaveDirectory() + File.separator + fileName)
				.exists();
	}


}
