package cn.ls.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ls.util.ComName;

public class WarnInfor extends Activity implements OnClickListener {
	private Button enTrue;

	private TextView daoJiShi;
	private Receiver receiver;
	private int timeRefresh;
	private int currentMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentMode = getIntent().getIntExtra("mode", 0);// 获取当前模式
		System.out.println("WarnInfor----currentMode-->" + currentMode);
		setUpViews();
		setUpReceiver();
	}

	/**
	 * 取消倒计时
	 */
	private void sendCast() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(ComName.CANCLE_TIME);
		intent.putExtra("mode", currentMode);
		sendBroadcast(intent);
	}

	private void setUpViews() {
		// TODO Auto-generated method stub
		switch (currentMode) {
		case 0:
			setContentView(R.layout.warninfor_activity_mode1);
			break;
		case 1:
			setContentView(R.layout.warninfor_activity_mode2);
			break;
		case 2:
			setContentView(R.layout.warninfor_activity_mode3);
			break;
		}
		enTrue = (Button) findViewById(R.id.warninforAc_btn_entrue);
		daoJiShi = (TextView) findViewById(R.id.warninforAc_tx_daojishi);
		enTrue.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == enTrue) {
			Intent mIntent = new Intent(Intent.ACTION_MAIN);
			mIntent.addCategory(Intent.CATEGORY_HOME);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(mIntent);
			this.finish();
		}
	}

	private void setUpReceiver() {

		// TODO Auto-generated method stub
		receiver = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ComName.REFRESH_DAO_TIME);
		filter.addAction(ComName.TIME_COUNTS);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	/**
	 * 得到时间总数转化为时分秒显示
	 */
	private void refreshTime() {
		// TODO Auto-generated method stub
		int hour, min, sec;
		hour = timeRefresh / 3600;
		if ((timeRefresh / 60) >= 60) {
			min = timeRefresh / 60 - 60 * hour;
		} else {
			min = timeRefresh / 60;
		}
		sec = timeRefresh % 60;
		daoJiShi.setText("" + hour + ":" + min + ":" + sec);
		System.out.println("WarnInfor倒计时" + "" + hour + ":" + min + ":" + sec);
	}

	class Receiver extends BroadcastReceiver {
		int i = 0;
		String action;

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			action = intent.getAction();
			if (action.equals(ComName.REFRESH_DAO_TIME)) {
				// timeCount = intent.getIntExtra("time_counts", 0);// 时间总数
				timeRefresh = intent.getIntExtra("time_refresh", 0);// 每次刷新时间数
				refreshTime();
				// refreshProgress();
			}
		}
	}
}
