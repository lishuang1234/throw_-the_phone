package cn.ls.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.ls.activity.AboutActivity;
import cn.ls.activity.PlanListActivity;
import cn.ls.activity.R;
import cn.ls.activity.SettingActivity;

public class TitleView extends FrameLayout implements OnClickListener {
	private Button back;
	private TextView title;
	private Context context;
	private Button clock;
	private Button shuoming;
	private Button tieshi;

//	public TitleView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//		this.context = context;
//      //  this.isInEditMode();
//		setUpViews();
//	}

	private void setUpViews() {
		// TODO Auto-generated method stub

		LayoutInflater.from(getContext()).inflate(R.layout.title, this);
		back = (Button) findViewById(R.id.titleview_btn_back);
		clock = (Button) findViewById(R.id.titleview_btn_clock);
		shuoming = (Button) findViewById(R.id.titleview_btn_shuoming);
		tieshi = (Button) findViewById(R.id.titleview_btn_tieshi);
		title = (TextView) findViewById(R.id.title);
		back.setOnClickListener(this);
		clock.setOnClickListener(this);
		shuoming.setOnClickListener(this);
		tieshi.setOnClickListener(this);
	}

//	public TitleView(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//		this.context = context;
//      //  this.isInEditMode();
//		setUpViews();
//
//	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
      //  this.isInEditMode();
		this.context = context;
		setUpViews();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titleview_btn_back:
			((Activity) context).finish();
			break;
		case R.id.titleview_btn_clock:
			context.startActivity(new Intent(context , PlanListActivity.class));// 设置计划
			break;
		case R.id.titleview_btn_shuoming:// 说明
			Intent intent = new Intent();
			intent.putExtra("layoutFlag", 0);
			intent.setClass(context , AboutActivity.class);// 使用说明
			context.startActivity(intent);
			break;
		case R.id.titleview_btn_tieshi:// 贴士
			Intent intent2 = new Intent();
			intent2.putExtra("layoutFlag", 3);
			intent2.setClass(context , AboutActivity.class);// 健康贴士。。待定
			context.startActivity(intent2);
			break;
		}

	}

	public void setTitle(String text) {
		title.setText(text);
	}

	/**
	 * 设置标题按钮可见性
	 * 
	 * @param backIsVis
	 * @param clockIsVis
	 * @param shuoMingIsVis
	 */
	public void setBtnVisible(boolean backIsVis, boolean clockIsVis,
			boolean shuoMingIsVis, boolean tieShiIsVis) {
		if (!backIsVis) {
			back.setVisibility(View.GONE);
		}
		if (!clockIsVis) {
			clock.setVisibility(View.GONE);
		}
		if (!shuoMingIsVis) {
			shuoming.setVisibility(View.GONE);
		}
		if (!tieShiIsVis) {
			tieshi.setVisibility(View.GONE);
		}
	}
}
