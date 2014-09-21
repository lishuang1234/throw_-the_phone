package cn.ls.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import cn.ls.util.ComName;
import cn.ls.util.ShareSDKHelper;

public class WorkState extends Activity implements OnClickListener {
	private ImageButton btnWorkSuccess;
	private ImageButton btnWorkFailYanLi;
	private ImageButton btnWorkFailWenrou;
	private ImageButton btnWorkSuccessBack;
	private ShareSDKHelper sdkHelper;
	private int chenFaMode;
	private SharedPreferences sharedPreChenFaMode;
	private boolean workState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sdkHelper = new ShareSDKHelper(WorkState.this);
		setUpShare();
		if (workState = getIntent().getBooleanExtra("work_state", true)) {
			setContentView(R.layout.workstate_success);
			setUpSuccessView();
		} else {
			setContentView(R.layout.workstate_fail);
			setUpFailView();
		}
	}

	private void setUpShare() {
		// TODO Auto-generated method stub
		sharedPreChenFaMode = getSharedPreferences("chenFaMode",
				Activity.MODE_PRIVATE);// 保存现在模式
		chenFaMode = sharedPreChenFaMode.getInt("chenFaMode", 0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//sdkHelper.stopSDK();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setUpSuccessView() {
		// TODO Auto-generated method stub
		btnWorkSuccess = (ImageButton) findViewById(R.id.workstateAc_ibtn_success);
		btnWorkSuccessBack = (ImageButton) findViewById(R.id.workstateAc_ibtn_back);
		btnWorkSuccess.setOnClickListener(this);
		btnWorkSuccessBack.setOnClickListener(this);

	}

	private void setUpFailView() {
		// TODO Auto-generated method stub
		btnWorkFailYanLi = (ImageButton) findViewById(R.id.workstateAc_ibtn_fail_yanli);
		btnWorkFailYanLi.setOnClickListener(this);
		btnWorkFailWenrou = (ImageButton) findViewById(R.id.workstateAc_ibtn_fail_wenrou);
		btnWorkFailWenrou.setOnClickListener(this);
		switch (chenFaMode) {
		case 0:
			btnWorkFailYanLi.setVisibility(View.VISIBLE);
			btnWorkFailWenrou.setVisibility(View.GONE);
			break;
		case 1:
			btnWorkFailYanLi.setVisibility(View.GONE);
			btnWorkFailWenrou.setVisibility(View.VISIBLE);
			break;
		}

	}

	/**
	 * 取消拦截
	 */
	private void sendCast() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(ComName.CANCLE_LAN_JIE);
		intent.putExtra("work_state", workState);
		sendBroadcast(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnWorkSuccessBack) {
			this.finish();
		
		} else if (v == btnWorkFailYanLi) {
			sdkHelper.shareToPlat("何弃疗！", "上帝都帮不了你！~");
			this.finish();
		} else if (v == btnWorkSuccessBack) {
			this.finish();
		} else if ((v == btnWorkFailWenrou)) {
			this.finish();
		}else if (v==btnWorkSuccess) {
			sdkHelper.shareToPlat("治疗成功！", "哈哈，我通过\"何弃疗\"成功治疗手机依赖症，大家快来一同使用吧！！");
			this.finish();
		}
		sendCast();
	}
}
