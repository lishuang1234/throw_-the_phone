package cn.ls.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.ls.activity.R;
import cn.ls.db.DBHelper;

public class ListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	// private boolean[] itemState;
	private Activity mActivity;
	private ArrayList<Object[]> infor;
	private List<Intent[]> intentList;// /存放每个Item的星期Intent
	private boolean[] isChongFu;
	private DBHelper dbHelper;
	private AlarmPlan alarmPlan;
	private SetAlarmClock setAlarm;

	public ListAdapter(Activity mActivity, ArrayList<Object[]> infor) {
		this.mActivity = mActivity;
		this.infor = infor;
		dbHelper = new DBHelper(mActivity);
		setAlarm = new SetAlarmClock(mActivity, dbHelper);
	}

	public ListAdapter() {

	}

	public void addItem(String[] itemInfor) {
		infor.add(itemInfor);
		this.notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infor.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			if (inflater == null) {
				inflater = LayoutInflater.from(mActivity);
			}
			convertView = inflater.inflate(R.layout.listview_item, null);
			holder = new ViewHolder();// 初始化控件类
			holder.entrue = (CheckBox) convertView
					.findViewById(R.id.listview_cb_entrue);
			holder.planShiJian = (TextView) convertView
					.findViewById(R.id.listview_tx_planshijian);
			holder.planShiChang = (TextView) convertView
					.findViewById(R.id.listview_tx_planshichang);
			holder.planChongfu = (TextView) convertView
					.findViewById(R.id.listview_tx_planchongfu);
			convertView.setTag(holder);// 添加控件类
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.planShiJian.setText(Contant.refreshTime((Integer) infor
				.get(position)[3]));// '开始时间
		holder.planChongfu.setText((String) infor.get(position)[2]);// 重复时间
		holder.planShiChang.setText(Contant.refreshTimeCounts((Integer) infor
				.get(position)[1]));// 注意此处与之对应关系，计划时长;
		holder.entrue.setOnCheckedChangeListener(new CheckBoxChangeListener(
				position));
		if (infor.get(position)[4].equals("1")) {// 判断是否启用计划
			holder.entrue.setChecked(true);
			// / setAlarm(position);
			setAlarm.setAlarm((String) infor.get(position)[0]);//获得ID去设置闹钟
		} else
			holder.entrue.setChecked(false);
		return convertView;
	}



	class ViewHolder {
		TextView planShiJian;
		TextView planShiChang;
		TextView planChongfu;
		CheckBox entrue;
	}

	class CheckBoxChangeListener implements OnCheckedChangeListener {
		private int position;

		public CheckBoxChangeListener(int position) {
			this.position = position;

		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {// 更新数据库是否启用计划
				System.out.println("更新adapter--position" + position);
				dbHelper.upDate(new AlarmPlan((String) infor.get(position)[0],
						(Integer) infor.get(position)[1], (String) infor
								.get(position)[2], (Integer) infor
								.get(position)[3], 1));
				// // Position设置闹钟

			} else {
				System.out.println("更新禁止adapter--position" + position);
				dbHelper.upDate(new AlarmPlan((String) infor.get(position)[0],
						(Integer) infor.get(position)[1], (String) infor
								.get(position)[2], (Integer) infor
								.get(position)[3], 0));

			}

		}
	}

}
