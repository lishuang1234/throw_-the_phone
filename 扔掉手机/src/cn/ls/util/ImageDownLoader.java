package cn.ls.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

public class ImageDownLoader {

	private LruCache<String, Bitmap> mMemoryCache;// 缓存image
	private SaveImageFile saveImageFile;
	private  static  Handler handler;

	public ImageDownLoader(Context context) {
		saveImageFile = new SaveImageFile(context);
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int mCacheSize = maxMemory / 8;// 分配4M内存
		mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes() * value.getHeight();// 位图所占用的内存数
			}
		};
	}

	/**
	 * 添加Image到缓存中
	 */
	public void addBitmapToMemorryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemorryCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 从内存中获取Bitmap
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemorryCache(String key) {
		return mMemoryCache.get(key);
	}

	public Bitmap showCacheImagne(String url) {
		final String subUrl = url.replaceAll("[^\\w]", "");
		if (getBitmapFromMemorryCache(subUrl) != null) {// 缓存中存在
			return getBitmapFromMemorryCache(subUrl);
		} else if (saveImageFile.getBitmap(subUrl) != null
				&& saveImageFile.bitmapIsExist(subUrl)) {// 文件中存在
			Bitmap bitmap = saveImageFile.getBitmap(subUrl);
			addBitmapToMemorryCache(subUrl, bitmap);// 添加入缓存中
			return bitmap;
		} 
//			else
//			downLoadImage(url);
		return null;
	}

	public Bitmap downLoadImage(String url) {
		final String subUrl = url.replaceAll("[^\\w]", "");
		// Bitmap bitmap = showCacheImagne(subUrl);
		// if (bitmap != null) {
		// return bitmap;
		// } else {// 下载
		handler = new Handler() {
			Bitmap bitmap;

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				bitmap = (Bitmap) msg.obj;
				addBitmapToMemorryCache(subUrl, bitmap);
				saveImageFile.saveBitmap(subUrl, bitmap);
			}
		};
		new DownloadPhoto(url).start();
		// }

		return null;
	}

	/**
	 * 新线程现在图片
	 * 
	 * @author LS
	 * 
	 */
	class DownloadPhoto extends Thread {
		private String photo_path;
		private Bitmap bitmap;

		public DownloadPhoto(String url) {
			this.photo_path = url;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			try {
				URL url = new URL(photo_path);
				HttpURLConnection connect = (HttpURLConnection) url
						.openConnection();
				connect.setRequestMethod("GET"); // 设置请求方法为GET
				connect.setReadTimeout(5 * 1000); // 设置请求过时时间为5秒
				InputStream input = connect.getInputStream();// 获取输入流
				bitmap = BitmapFactory.decodeStream(input);// 生成位图
				Message msg = new Message();
				msg.obj = bitmap;
				handler.sendMessage(msg);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
