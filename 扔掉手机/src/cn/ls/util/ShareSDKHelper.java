package cn.ls.util;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;
import cn.ls.activity.LoginActivity;
import cn.ls.activity.R;
import cn.ls.activity.Tab;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

public class ShareSDKHelper {
	private Activity mActrivity;
	private int loginFlag;
	private ProgressDialog progressDialog;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			progressDialog.dismiss();
			switch (msg.what) {
			case 1:
				Toast.makeText(mActrivity, "授权成功", Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(mActrivity, "授权失败", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(mActrivity, "授权取消", Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};

	public ShareSDKHelper(Activity activity) {
		this.mActrivity = activity;
		ShareSDK.initSDK(mActrivity);
	}

	/**
	 * 检查是否已授权
	 */
	public String checkInti() {
		// TODO Auto-generated method stub
		ShareSDK.initSDK(mActrivity);
		Platform plat1 = ShareSDK.getPlatform(mActrivity, SinaWeibo.NAME);
		Platform plat2 = ShareSDK.getPlatform(mActrivity, QZone.NAME);
		if (plat1.isValid()) {
			return SinaWeibo.NAME;
		} else if (plat2.isValid()) {
			return QZone.NAME;
		} else
			return null;
	}

	/**
	 * 获取授权的平台
	 */
	public Platform getInitPlat() {
		// TODO Auto-generated method stub
		ShareSDK.initSDK(mActrivity);
		Platform plat1 = ShareSDK.getPlatform(mActrivity, SinaWeibo.NAME);
		Platform plat2 = ShareSDK.getPlatform(mActrivity, QZone.NAME);
		if (plat1.isValid()) {
			return plat1;
		} else if (plat2.isValid()) {
			return plat2;
		} else
			return null;
	}

	/**
	 * 停止SDK
	 */
	public void stopSDK() {
		ShareSDK.stopSDK(mActrivity);
	}

	/**
	 * 登录账号
	 * 
	 * @param name
	 */
	public void login(final String name) {
		ShareSDK.initSDK(mActrivity);
		// TODO Auto-generated method stub
	
		
		progressDialog = new ProgressDialog(mActrivity,
				ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("正在登录请稍后...");
		progressDialog.show();
		Platform plat = ShareSDK.getPlatform(mActrivity, name);
		plat.SSOSetting(true);
		plat.authorize();
		plat.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				startOther();
				mHandler.sendEmptyMessage(1);
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(2);
			}
		});

	}

	protected void startOther() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(mActrivity, Tab.class);
		mActrivity.startActivity(intent);
		mActrivity.finish();
	}

	/**
	 * 清除帐号信息
	 */
	public void removeAccount() {
		if (getInitPlat() != null) {
			getInitPlat().removeAccount();
		}
	}

	/**
	 * 帐号分享
	 * 
	 * @param title
	 * @param text
	 */
	public void shareToPlat(String title, String text) {
		ShareSDK.initSDK(mActrivity);
		// TODO Auto-generated method stub
		OnekeyShare oks = new OnekeyShare();
		// 分享时 Notification 的图标和文字
		oks.setNotification(R.drawable.ic_launcher1, "正在分享！");
		// title 标题，印象笔记、邮箱、信息、微信、人人网和 QQ 空间使用
		oks.setTitle(title);
		// titleUrl 是标题的网络链接，仅在人人网和 QQ 空间使用
		oks.setTitleUrl("http://user.qzone.qq.com/550269115");
		// text 是分享文本，所有平台都需要这个字段
		oks.setText(text);
		// comment 是我对这条分享的评论，仅在人人网和 QQ 空间使用
		oks.setComment("");
		// site 是分享此内容的网站名称，仅在 QQ 空间使用
		oks.setSite("何弃疗");
		// siteUrl 是分享此内容的网站地址，仅在 QQ 空间使用
		oks.setSiteUrl("http://user.qzone.qq.com/550269115");
		// 是否直接分享（true 则直接分享）
		oks.setSilent(true);
		if (checkInti() != null) {
			oks.setPlatform(checkInti());
		}
		oks.show(mActrivity);
	}

}
