package cn.ls.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.ls.util.BaseTool;
import cn.ls.util.ComName;
import cn.ls.view.ProgressBarView;

public class ForbidActivity extends Activity implements OnClickListener {
	/**
	 * onCreat方法中触发倒计时
	 */
	private Button forbidIbtnFail;
	private ProgressBarView progress;
	private TextView daoJiShi;
	private int timeRefresh;
	private int timeCount;
	private Receiver receiver;
	// private boolean flag;
	// private int progressAdd;
	private int currentMode;
	private int[] forbidTitle;
	private TextView forbidTitleText;
	// private int[] forbidIbtuBg;
	private int chenFaMode;
	private SharedPreferences sharedPreChenFaMode;
	private BaseTool baseTool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forbid_activity);
		setUpShare();
		setUpData();
		setUpViews();
		setUpReceiver();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setUpData() {
		// TODO Auto-generated method stub
		currentMode = getIntent().getIntExtra("mode", 0);// 获取当前模式
		forbidTitle = new int[] { R.string.forbid_title0,
				R.string.forbid_title1, R.string.forbid_title2 };
		// forbidIbtuBg = new int[] { R.drawable.ic_btn_mode0_fail,
		// R.drawable.ic_btn_mode1_fail, R.drawable.ic_btn_mode2_fail };
		baseTool = new BaseTool(ForbidActivity.this);

	}

	private void setUpReceiver() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(ComName.REFRESH_DAO_TIME);
		filter.addAction(ComName.TIME_COUNTS);
		filter.addAction(ComName.ZHI_LIAO_SUCCESS);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	private void setUpViews() {
		// TODO Auto-generated method stub
		forbidIbtnFail = (Button) findViewById(R.id.forbidAc_Ibtu_modefail);
		forbidIbtnFail.setOnClickListener(this);
		daoJiShi = (TextView) findViewById(R.id.daojishi);
		forbidTitleText = (TextView) findViewById(R.id.forbidAc_title_text);
		progress = (ProgressBarView) findViewById(R.id.forbidAc_progressbar_circle);
		progress.setCircleRadius(baseTool.getWidthPx() / 2,
				baseTool.getHeightPx() / 2);
		receiver = new Receiver();
		System.out.println("width:" + baseTool.getWidthPx() + "--height:"
				+ baseTool.getHeightPx());
		forbidTitleText.setText(forbidTitle[currentMode]);// 设置显示的标题
		if (chenFaMode == 2) {
			forbidIbtnFail.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == forbidIbtnFail) {
			sendCast();// 取消倒计时服务
			this.finish();
			startAc(false);
		}
	}

	private void setUpShare() {
		// TODO Auto-generated method stub
		sharedPreChenFaMode = getSharedPreferences("chenFaMode",
				Activity.MODE_PRIVATE);
		chenFaMode = sharedPreChenFaMode.getInt("chenFaMode", 0);
	}

	private void startAc(boolean state) {
	System.out.println("是否成功治疗！！"+state);
		Intent intent = new Intent();
		intent.putExtra("work_state", state);
		intent.setClass(ForbidActivity.this, WorkState.class);
		startActivity(intent);
		
	}

	/**
	 * 取消倒计时
	 */
	private void sendCast() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(ComName.CANCLE_TIME);
		sendBroadcast(intent);
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
		progress.setMax(timeCount);
		progress.setProgress(timeRefresh);
		System.out.println("ForbidAc倒计时" + "" + hour + ":" + min + ":" + sec);
	}

	class Receiver extends BroadcastReceiver {
		int i = 0;
		String action;

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			action = intent.getAction();
			if (action.equals(ComName.REFRESH_DAO_TIME)) {
				timeCount = intent.getIntExtra("time_counts", 0);// 时间总数
				timeRefresh = intent.getIntExtra("time_refresh", 0);// 每次刷新时间数
				refreshTime();
			} else if (action.equals(ComName.ZHI_LIAO_SUCCESS)) {
				ForbidActivity.	this.finish();
				startAc(true);
			}
		}
	}
}
