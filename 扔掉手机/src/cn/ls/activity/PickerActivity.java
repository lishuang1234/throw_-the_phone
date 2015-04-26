package cn.ls.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import cn.ls.db.DBHelper;
import cn.ls.util.AlarmPlan;
import cn.ls.util.Contant;
import cn.ls.util.SetAlarmClock;

/**
 * 数据库设计： 1.存储数据：（1）设置的时长（2）设置重复次数（3）设置的开始时间 （4）是否启用状态
 * 
 * @author LS
 * 
 */
public class PickerActivity extends Activity implements OnClickListener,
		OnTimeChangedListener, OnValueChangeListener,
		DialogInterface.OnMultiChoiceClickListener {

	private TimePicker tpPicker;// 設置计划开时间
	private RelativeLayout rlSetChongFu, rlSetShiChang;
	private NumberPicker hour, min;// 设置持续时间
	private AlertDialog dialogTimePicker;
	private AlertDialog dialogCiShu;
	private int mHourCounts;
	private int mMinCounts;
	private int currentHour;
	private int currentMin;
	// private List<AlarmPlan> alarmPlanList;//存储AlarmPlan
	private AlarmPlan alarmPlan;
	// private ListAdapter adapter;
	private Calendar clandar;
	private int flag;
	private String ID;
	private int id;
	private boolean[] isChongFu;
	private String setChongFu;
	private DBHelper helper;
	private SetAlarmClock setAlarmClock;

	// private boolean[] repeatFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picker_activity);
		setUpData();
		setUpViews();
		setUpShiChangDialog();
		setUpCiShuDialog();
	}

	private void setUpData() {
		// TODO Auto-generated method stub
		helper = new DBHelper(this);// 初始化数据库
		// adapter = new ListAdapter();//初始化适配器
		setAlarmClock = new SetAlarmClock(PickerActivity.this, helper);
		flag = getIntent().getIntExtra("flag", -1);
		ID = getIntent().getStringExtra("currentID");
		if (flag == 1) {// 执行新建任务
			System.out.println("增加计划----ID" + ID);
			alarmPlan = new AlarmPlan();
			clandar = Calendar.getInstance();
			currentHour = clandar.get(Calendar.HOUR_OF_DAY);
			currentMin = clandar.get(Calendar.MINUTE);// 初始化事件监听变量
			isChongFu = new boolean[] { false, false, false, false, false,
					false, false };
		} else {// 执行修改任务在数据库中查找数据,要修改的变量 重复、时长
			System.out.println("修改计划----ID" + ID);
			alarmPlan = helper.quary(ID);
			isChongFu = Contant.getChongFuFlag(alarmPlan.getTimes());
			mHourCounts = (Contant.getHour(alarmPlan.getDuration()));
			mMinCounts = (Contant.getMin(alarmPlan.getDuration()));// 初始化数据选择
		}
	}

	// 设置选择时长Dialog
	private void setUpShiChangDialog() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		View diaLayout = (View) inflater.inflate(R.layout.timepicker, null);
		hour = (NumberPicker) diaLayout.findViewById(R.id.hour);
		min = (NumberPicker) diaLayout.findViewById(R.id.min);
		hour.setOnValueChangedListener(this);
		min.setOnValueChangedListener(this);
		diaLayout.findViewById(R.id.timepicker_ibtn_entrue).setOnClickListener(
				this);
		diaLayout.findViewById(R.id.timepicker_ibtn_noentrue)
				.setOnClickListener(this);
		AlertDialog.Builder builderTimePicker = new AlertDialog.Builder(this);
		builderTimePicker.setView(diaLayout);
		dialogTimePicker = builderTimePicker.create();
	}

	// 初始化控件
	private void setUpViews() {
		// TODO Auto-generated method stub
		((ImageButton) findViewById(R.id.pickerAc_ibtn_entrue))
				.setOnClickListener(this);// 确认按钮
		((ImageButton) findViewById(R.id.pickerAc_ibtn_noentrue))
				.setOnClickListener(this);// 取消按钮
		rlSetChongFu = (RelativeLayout) findViewById(R.id.pickerAc_rl_setchongfu);// 设置重复
		rlSetChongFu.setOnClickListener(this);
		rlSetShiChang = (RelativeLayout) findViewById(R.id.pickerAc_rl_setshichang);// 设置时长
		rlSetShiChang.setOnClickListener(this);
		((RelativeLayout) findViewById(R.id.pickerAc_rl_cancleJihua))// 刪除計劃
				.setOnClickListener(this);
		tpPicker = (TimePicker) findViewById(R.id.pickerAc_tp_picker);// 时间选择
		tpPicker.setOnTimeChangedListener(this);
		if (flag != 1) {// 执行修改
			tpPicker.setCurrentHour(Contant.getHour(alarmPlan.getStartTime()));
			tpPicker.setCurrentMinute(Contant.getMin(alarmPlan.getStartTime()));// 初始化设置时间选择器
		}
	}

	// 初始化设置重复DiaLog
	private void setUpCiShuDialog() {
		// TODO Auto-generated method stub
		final String[] mItems = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
				"星期日" };
		LayoutInflater inflater = LayoutInflater.from(this);
		View diaLayout = (View) inflater.inflate(R.layout.cishupicker, null);
		diaLayout.findViewById(R.id.cishuPicker_ibtn_entrue)
				.setOnClickListener(this);
		diaLayout.findViewById(R.id.cishuPicker_ibtn_noentrue)
				.setOnClickListener(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCustomTitle(diaLayout);// 自定义标题
		builder.setMultiChoiceItems(mItems, isChongFu, this);// 注意此處 IsChongfu
		dialogCiShu = builder.create();
	}

	// 初始化NumPicker
	private void initNumPicker(int maxHourValue, int maxMinValue) {// 注意设置顺序
		// TODO Auto-generated method stub
		hour.setMinValue(0);
		hour.setMaxValue(maxHourValue);
		hour.setValue(mHourCounts);
		min.setMinValue(0);
		min.setMaxValue(maxMinValue);
		min.setValue(mMinCounts);
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
		case R.id.pickerAc_rl_setshichang:// 选择时长
			initNumPicker(2, 59);
			dialogTimePicker.show();// 显示选择时长对话框
			break;
		case R.id.pickerAc_rl_setchongfu:// 选择重复
			dialogCiShu.show();// 显示选择重复的对话框
			break;
		case R.id.pickerAc_rl_cancleJihua:// 删除计划
			cancleJiHua();
			break;
		case R.id.pickerAc_ibtn_entrue:// 确认按钮
			saveSetting();
			break;
		case R.id.pickerAc_ibtn_noentrue:// 取消按钮
			startActivity(new Intent(PickerActivity.this,
					PlanListActivity.class));
			this.finish();
			break;
		case R.id.timepicker_ibtn_noentrue:// 选择时长的取消按钮
			dialogTimePicker.dismiss();
			break;
		case R.id.timepicker_ibtn_entrue:// 选择时长的确认按钮
			dialogTimePicker.dismiss();
			break;
		case R.id.cishuPicker_ibtn_noentrue:// 选择重复的取消按钮
			dialogCiShu.dismiss();
			// ///////////////////////缺少取消操作
			break;
		case R.id.cishuPicker_ibtn_entrue:
			dialogCiShu.dismiss();
			break;
		}

	}

	// 删除计划
	private void cancleJiHua() {
		// TODO Auto-generated method stub

		if (flag == 1) {// 新建

		} else {
			setAlarmClock.cancleAlarm(ID);
			helper.delect(ID);// 删除计划
			System.out.println("删除计划---lD" + ID);
			// 取消相关脑中设置

		}
		startActivity(new Intent(PickerActivity.this, PlanListActivity.class));
		this.finish();

	}

	private void saveSetting() {
		// TODO Auto-generated method stub
		if (flag == 1) {// 存储新建计划参数
			alarmPlan.setDuration((mHourCounts * 3600 + mMinCounts * 60));
			id = Integer.parseInt(ID);
			id++;
			// System.out.println("新建计划的新ID-----------" + id);
			alarmPlan.setId(id + "");// 装化为字符串
			// System.out.println("新建计划的新ID------=======" + id);
			alarmPlan.setOnPlan(1);// 1 表示执行
			alarmPlan.setStartTime((currentHour * 3600 + currentMin * 60));
			alarmPlan.setTimes(Contant.getChongFuString(isChongFu));
			helper.insert(alarmPlan);
		} else {// 修改计划存储
			alarmPlan.setOnPlan(1);// 1 表示执行
			alarmPlan.setStartTime((currentHour * 3600 + currentMin * 60));
			alarmPlan.setTimes(Contant.getChongFuString(isChongFu));
			alarmPlan.setDuration((mHourCounts * 3600 + mMinCounts * 60));
			helper.upDate(alarmPlan);
		}
		startActivity(new Intent(PickerActivity.this, PlanListActivity.class));
		this.finish();

	}

	// 计划时间监听
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		currentHour = hourOfDay;
		currentMin = minute;
		System.out.println("显示选取的时间：" + currentHour + ":" + currentMin);
	}

	// 计划时长监听
	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		// TODO Auto-generated method stub
		switch (picker.getId()) {
		case R.id.hour:
			mHourCounts = newVal;
			break;
		case R.id.min:
			mMinCounts = newVal;
			break;
		}
	}

	/**
	 * 重复 星期对话框选择监听
	 */
	@Override
	public void onClick(DialogInterface dialog, int id, boolean isChecked) {
		// TODO Auto-generated method stub

		switch (id) {
		case 0:
			if (isChecked) {
				isChongFu[0] = true;
			} else
				isChongFu[0] = false;
			break;
		case 1:
			if (isChecked) {
				isChongFu[1] = true;
			} else
				isChongFu[1] = false;
			break;
		case 2:
			if (isChecked) {
				isChongFu[2] = true;
			} else
				isChongFu[2] = false;
			break;
		case 3:
			if (isChecked) {
				isChongFu[3] = true;
			} else
				isChongFu[3] = false;
			break;
		case 4:
			if (isChecked) {
				isChongFu[4] = true;
			} else
				isChongFu[4] = false;
			break;
		case 5:
			if (isChecked) {
				isChongFu[5] = true;
			} else
				isChongFu[5] = false;
			break;
		case 6:
			if (isChecked) {
				isChongFu[6] = true;
			} else
				isChongFu[6] = false;
			break;
		}
	}
}
