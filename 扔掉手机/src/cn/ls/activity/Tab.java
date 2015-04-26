package cn.ls.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Tab extends TabActivity implements OnCheckedChangeListener {

	private RadioGroup radioGroup;
	private TabHost tabHost;
	public static final String TAB_MYINFOR = "MyInforActivity";
	public static final String TAB_SETTING = "SettingrActivity";
	public static final String TAB_START = "StartActivity";
	// public static final String TAB_ABOUT = "AboutActivity";
	private String jActivity;
	private RadioButton radioButtonStart;
	private RadioButton radioButtonMe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		jActivity = getIntent().getStringExtra("activity");///////////////////
		radioGroup = (RadioGroup) findViewById(R.id.group);
		radioButtonStart = (RadioButton) findViewById(R.id.btu_start);
		radioButtonMe = (RadioButton) findViewById(R.id.btu_me);
		radioGroup.setOnCheckedChangeListener(this);
		tabHost = this.getTabHost();
		TabSpec start = tabHost.newTabSpec(TAB_START);
		TabSpec myInfor = tabHost.newTabSpec(TAB_MYINFOR);
		TabSpec setting = tabHost.newTabSpec(TAB_SETTING);
		// TabSpec about = tabHost.newTabSpec(TAB_ABOUT);
		start.setIndicator(TAB_START).setContent(
				new Intent(this, StartActivity.class));
		myInfor.setIndicator(TAB_MYINFOR).setContent(
				new Intent(this, MyInforActivity.class));
		setting.setIndicator(TAB_SETTING).setContent(
				new Intent(this, SettingActivity.class));
		// about.setIndicator(TAB_ABOUT).setContent(
		// new Intent(this, AboutActivity.class));
		tabHost.addTab(start);
		tabHost.addTab(myInfor);
		tabHost.addTab(setting);
		setDefaultTab(TAB_START);
		radioButtonStart.setChecked(true);
		if (jActivity != null) {
			tabHost.setCurrentTabByTag(jActivity);
			radioButtonMe.setChecked(true);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.btu_me:
			tabHost.setCurrentTabByTag(TAB_MYINFOR);
			break;
		case R.id.btu_setting:
			tabHost.setCurrentTabByTag(TAB_SETTING);
			break;
		case R.id.btu_start:
			tabHost.setCurrentTabByTag(TAB_START);
			break;
		// case R.id.btu_about:
		// tabHost.setCurrentTabByTag(TAB_ABOUT);
		// default:
		// break;

		}
	}

}
