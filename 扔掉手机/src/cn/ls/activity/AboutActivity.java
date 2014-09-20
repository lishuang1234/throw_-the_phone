package cn.ls.activity;

import cn.ls.view.TitleView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class AboutActivity extends Activity implements OnClickListener {
	private int layoutFlag;
	private CheckBox CbchenFaYanLi;
	private CheckBox CbchenFaWenRou;
	private CheckBox CbchenFaJianChi;
	private int chenFaMode;
	private SharedPreferences sharedPreChenFaMode;
	private SharedPreferences.Editor editorChenFaMode;
	private TitleView title;
	// private AlertDialog.Builder builder;
	private AlertDialog banBengDialog;
	private AlertDialog kaiFaRenYuanDialog;
	private AlertDialog bangZhuDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		layoutFlag = getIntent().getIntExtra("layoutFlag", 0);
		setUpLayout();
	}

	private void setUpLayout() {
		// TODO Auto-generated method stub
		switch (layoutFlag) {
		case 0:
			setContentView(R.layout.about_activity_shuoming);// 说明
			// 设置标题属性
			title = (TitleView) findViewById(R.id.aboutAc_shuoming_title);
			title.setTitle("使用说明");
			title.setBtnVisible(true, false, false, false);
			break;
		case 1:
			setContentView(R.layout.about_activity_guanyu);// 关于应用
			// 设置标题属性
			title = (TitleView) findViewById(R.id.aboutAc_guanyu_title);
			title.setTitle("关于应用");
			title.setBtnVisible(true, false, false, false);
			setUpGuanYuViews();
			break;
		case 2:
			setContentView(R.layout.about_activity_chenfa);// 惩罚方式
			setUpShare();
			setUpChenFaViews();
			title = (TitleView) findViewById(R.id.aboutAc_chengfa_title);
			title.setTitle("惩罚方式");
			title.setBtnVisible(true, false, false, false);
			break;
		case 3:
			setContentView(R.layout.about_activity_tieshi);// 健康贴士
			title = (TitleView) findViewById(R.id.aboutAc_tieshi_title);
			title.setTitle("健康贴士");
			title.setBtnVisible(true, false, false, false);
			break;
            case  4://称号说明
                setContentView(R.layout.myinfor_ll_chenghao);
                title = (TitleView)findViewById(R.id.myinfor_chenghao_title);
                title.setTitle("称号说明");
                title.setBtnVisible(true, false, false, false);
		}
	}

	private void setUpGuanYuViews() {
		// TODO Auto-generated method stub
		((RelativeLayout) findViewById(R.id.aboutAc_guanyu_rl_banbengengxin))
				.setOnClickListener(this);// 关于：版本更新
		((RelativeLayout) findViewById(R.id.aboutAc_guanyu_rl_bangzhufankui))
				.setOnClickListener(this);// 关于 ：帮助反馈
		((RelativeLayout) findViewById(R.id.aboutAcAc_guanyu_rl_kaifarenyuan))
				.setOnClickListener(this);// 关于：开发人员
		LayoutInflater inflater1 = LayoutInflater.from(this);
		View diaLayout = (View) inflater1.inflate(
				R.layout.guanyu__dia_banbenggengxin, null);
		diaLayout.findViewById(R.id.guanyu_dia_btn_banbengenggin_entrue)
				.setOnClickListener(this);// 版本更新确认按钮
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(diaLayout);
		banBengDialog = builder.create();// 生成dia
		LayoutInflater inflater2 = LayoutInflater.from(this);
		View diaLayout2 = (View) inflater2.inflate(
				R.layout.guanyu_dia_kaifarenyuan, null);
		diaLayout2.findViewById(R.id.guanyu_dia_btn_kaifarenyuan_entrue)
				.setOnClickListener(this);// 开发人员确认按钮
		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
		builder2.setView(diaLayout2);
		kaiFaRenYuanDialog = builder2.create();// 生成dia
		LayoutInflater inflater3 = LayoutInflater.from(this);
		View diaLayout3 = (View) inflater3.inflate(
				R.layout.guanyu_dia_bangzhufankui, null);
		diaLayout3.findViewById(R.id.guanyu_dia_btn_bangzhuyufankui_entrue)
				.setOnClickListener(this);// 帮助与反馈确认按钮
		diaLayout3.findViewById(R.id.guanyu_dia_btn_bangzhuyufankui_cancle)
				.setOnClickListener(this);// 取消按钮
		AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
		builder3.setView(diaLayout3);
		bangZhuDialog = builder3.create();// 生成dia
	}

	/**
	 * 存储当前惩罚模式
	 */
	private void setUpShare() {
		// TODO Auto-generated method stub
		sharedPreChenFaMode = getSharedPreferences("chenFaMode",
				Activity.MODE_PRIVATE);// 保存现在模式
		editorChenFaMode = sharedPreChenFaMode.edit();
		chenFaMode = sharedPreChenFaMode.getInt("chenFaMode", 0);
		System.out.println("AboutAC-->CheckSavechenFaMode" + chenFaMode);
	}

	/**
	 * 初始化控件
	 */
	private void setUpChenFaViews() {
		// TODO Auto-generated method stub
		CbchenFaYanLi = (CheckBox) findViewById(R.id.aboutAc_chenfa_cb_yanli);
		CbchenFaWenRou = (CheckBox) findViewById(R.id.aboutAc_chenfa_cb_wenrou);
		CbchenFaJianChi = (CheckBox) findViewById(R.id.aboutAc_chenfa_cb_jianchi);
		((RelativeLayout) findViewById(R.id.aboutAc_chenfa_rl_yanli))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.aboutAc_chenfa_rl_wenrou))
				.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.aboutAc_chenfa_rl_jianchi))
				.setOnClickListener(this);
		if (chenFaMode == 1) {
			setCheckBoxState(false, true, false);
		} else if (chenFaMode == 2) {
			setCheckBoxState(false, false, true);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.aboutAc_chenfa_rl_yanli:// 严厉惩罚
			chenFaMode = 0;
			setCheckBoxState(true, false, false);
			System.out.println("AboutAC-->HaveSavechenFaMode" + chenFaMode);
			editorChenFaMode.putInt("chenFaMode", chenFaMode);
			editorChenFaMode.commit();

			break;
		case R.id.aboutAc_chenfa_rl_wenrou:// 温柔惩罚
			chenFaMode = 1;
			setCheckBoxState(false, true, false);
			System.out.println("AboutAC-->HaveSavechenFaMode" + chenFaMode);
			editorChenFaMode.putInt("chenFaMode", chenFaMode);
			editorChenFaMode.commit();

			break;
		case R.id.aboutAc_chenfa_rl_jianchi:// 坚持惩罚
			chenFaMode = 2;
			setCheckBoxState(false, false, true);
			System.out.println("AboutAC-->HaveSavechenFaMode" + chenFaMode);
			editorChenFaMode.putInt("chenFaMode", chenFaMode);
			editorChenFaMode.commit();
			break;
		case R.id.aboutAc_guanyu_rl_banbengengxin:// 版本更新
			banBengDialog.show();
			break;
		case R.id.guanyu_dia_btn_banbengenggin_entrue:// 版本更新确认按钮
			banBengDialog.dismiss();
			break;
		case R.id.aboutAcAc_guanyu_rl_kaifarenyuan:// 开发人员
			kaiFaRenYuanDialog.show();
			break;
		case R.id.guanyu_dia_btn_kaifarenyuan_entrue:// 开发人员确认按钮
			kaiFaRenYuanDialog.dismiss();
			break;
		case R.id.aboutAc_guanyu_rl_bangzhufankui:// 帮助与反馈
			bangZhuDialog.show();
			break;
		case R.id.guanyu_dia_btn_bangzhuyufankui_cancle:// 帮助与反馈取消
			bangZhuDialog.dismiss();
			break;
		case R.id.guanyu_dia_btn_bangzhuyufankui_entrue:// 帮助与反馈确认
			bangZhuDialog.dismiss();
			break;

		}

	}

	/**
	 * 设置CheckBox状态
	 * 
	 * @param box1
	 * @param box2
	 * @param box3
	 */
	private void setCheckBoxState(boolean box1, boolean box2, boolean box3) {
		CbchenFaYanLi.setChecked(box1);
		CbchenFaWenRou.setChecked(box2);
		CbchenFaJianChi.setChecked(box3);
	}
}
