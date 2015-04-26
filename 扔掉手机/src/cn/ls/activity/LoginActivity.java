package cn.ls.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.ls.util.ShareSDKHelper;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

public class LoginActivity extends Activity {

	private ImageButton qzone;
	private ImageButton sinaWeibo;
	private ShareSDKHelper sdkHelper;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		sdkHelper = new ShareSDKHelper(LoginActivity.this);
		checkInit();
		setupViews();
	}

	private void checkInit() {
		// TODO Auto-generated method stub
		if (sdkHelper.checkInti() != null)
			startOther();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sdkHelper.stopSDK();

	}

	/**
	 * 启动下一个Activity
	 * 
	 * @param name
	 */
	protected void startOther() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, Tab.class);
		startActivity(intent);
		this.finish();
	}

	/**
	 * 初始化控件
	 */
	private void setupViews() {
		// TODO Auto-generated method stub
		qzone = (ImageButton) findViewById(R.id.qzone);
		sinaWeibo = (ImageButton) findViewById(R.id.sinaweibo);
		qzone.setOnClickListener(new MyListener());
		sinaWeibo.setOnClickListener(new MyListener());
	

	}

	/**
	 * 按钮监听器
	 * 
	 * @author LS
	 * 
	 */
	class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.qzone:
				sdkHelper.login(QZone.NAME);
				break;
			case R.id.sinaweibo:
				sdkHelper.login(SinaWeibo.NAME);
				break;
			}
		}
	}
}
