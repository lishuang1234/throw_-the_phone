package cn.ls.activity;

import cn.ls.service.MainService;
import cn.ls.util.ShareSDKHelper;
import cn.ls.view.TitleView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SettingActivity extends Activity implements OnClickListener {

	private RelativeLayout settingAcShuoMing;
	private RelativeLayout settingAcGuanYu;
	private RelativeLayout settingAcChenFa;
	private RelativeLayout settingAcTieShi;
	private RelativeLayout settingAcTuiChu;
	private RelativeLayout settingAcJiHua;
	private RelativeLayout settingAcTuiJian;
	private ShareSDKHelper sdkHelper;
	private TitleView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		setUpViews();
		sdkHelper = new ShareSDKHelper(SettingActivity.this);
	}

	private void setUpViews() {
		// TODO Auto-generated method stub
		settingAcShuoMing = (RelativeLayout) findViewById(R.id.settingAc_rl_shiyongshuoming);
		settingAcShuoMing.setOnClickListener(this);
		settingAcGuanYu = (RelativeLayout) findViewById(R.id.settingAc_rl_guanyu);
		settingAcGuanYu.setOnClickListener(this);
		settingAcChenFa = (RelativeLayout) findViewById(R.id.settingAc_rl_chenfa);
		settingAcChenFa.setOnClickListener(this);
		settingAcTieShi = (RelativeLayout) findViewById(R.id.settingAc_rl_tieshi);
		settingAcTieShi.setOnClickListener(this);
		settingAcTuiChu = (RelativeLayout) findViewById(R.id.settingAc_rl_tuichu);
		settingAcTuiChu.setOnClickListener(this);
		settingAcJiHua = (RelativeLayout) findViewById(R.id.settingAc_rl_jihua);
		settingAcJiHua.setOnClickListener(this);
		settingAcTuiJian = (RelativeLayout) findViewById(R.id.settingAc_rl_tuijian);
		settingAcTuiJian.setOnClickListener(this);
		// 设置标题
		title = (TitleView) findViewById(R.id.settingAc_title);
		title.setTitle("设置");
		title.setBtnVisible(false, false, false, false);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.settingAc_rl_shiyongshuoming:
			Log.i("click", "click");
			intent.putExtra("layoutFlag", 0);
			intent.setClass(SettingActivity.this, AboutActivity.class);// 使用说明
			startActivity(intent);
			break;
		case R.id.settingAc_rl_guanyu:
			intent.putExtra("layoutFlag", 1);
			intent.setClass(SettingActivity.this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.settingAc_rl_chenfa:
			intent.putExtra("layoutFlag", 2);
			intent.setClass(SettingActivity.this, AboutActivity.class);// 设置惩罚方式
			startActivity(intent);
			break;
		case R.id.settingAc_rl_tieshi:
			intent.putExtra("layoutFlag", 3);
			intent.setClass(SettingActivity.this, AboutActivity.class);// 健康贴士。。待定
			startActivity(intent);
			break;
		case R.id.settingAc_rl_tuichu:
			intent.setClass(SettingActivity.this, LoginActivity.class);// 退出登录账号
			startActivity(intent);
			sdkHelper.removeAccount();
			this.finish();
			break;
		case R.id.settingAc_rl_jihua:
			startActivity(new Intent(SettingActivity.this,
					PlanListActivity.class));// 设置计划
			break;
		case R.id.settingAc_rl_tuijian:// 推荐给好友
			sdkHelper.shareToPlat("各位好友看过来！！", "我正在使用“何弃疗”治疗手机依赖症，大家一起来啊。");
			break;
		}
	}

}