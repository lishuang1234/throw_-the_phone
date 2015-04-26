package cn.ls.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import cn.ls.db.DBHelper;
import cn.ls.util.AlarmPlan;
import cn.ls.util.ListAdapter;

public class PlanListActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	private ListView list;
	private ListAdapter adapter;
	private ArrayList<Object[]> infor;
	private DBHelper dbHelper;
	private Intent intentToPickerAc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.planlist_activity);
		initData();
		setUpViews();
	}

	private void initData() {// 查询数据填充
		// TODO Auto-generated method stub
		dbHelper = new DBHelper(this);
		intentToPickerAc = new Intent(PlanListActivity.this,
				PickerActivity.class);

		List<AlarmPlan> list = dbHelper.quaryAll();// 查询到的数据
		infor = new ArrayList<Object[]>();
		for (int i = 0; i < list.size(); i++) {
			System.out.println("PlanList中查询的ID" + list.get(i).getId()+"以及是否启用"+list.get(i).getIsOnPlan() );
			Object[] data = new Object[5];
			data[3] = list.get(i).getStartTime();// 得到开始时间
			if (list.get(i).getTimes().equals(""))
				data[2] = "无重复";// 得到次數
			else
				data[2] = list.get(i).getTimes();
			data[1] = list.get(i).getDuration();// 得到持续时长
			data[4] = list.get(i).getIsOnPlan();// 是否启用
			data[0] = list.get(i).getId();// 得到ID
			infor.add(data);
		}
	}

	private void setUpViews() {
		// TODO Auto-generated method stub
		list = (ListView) findViewById(R.id.planAc_lv_planlist);
		adapter = new ListAdapter(PlanListActivity.this, infor);// 生成Adapter
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.setOnItemClickListener(this);
		((ImageButton) findViewById(R.id.planAc_ibtn_addplan))
				.setOnClickListener(this);
		((ImageButton) findViewById(R.id.planAc_ibtn_entrue))
				.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
	//	Toast.makeText(this, "被点击！！", Toast.LENGTH_SHORT).show();
		intentToPickerAc.putExtra("flag", 0);// 修改任務
		intentToPickerAc.putExtra("currentID", (String) infor.get(position)[0]);
		startActivity(intentToPickerAc);
		this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.planAc_ibtn_addplan:
			intentToPickerAc.putExtra("flag", 1);// 新建任务呀
			if (infor.size() == 0)
				intentToPickerAc.putExtra("currentID", "-1");// 如果没有Item当前ID为-1
			else
				intentToPickerAc.putExtra("currentID",// 最后一个Item的ID
						(String) infor.get(infor.size() - 1)[0]);
			startActivity(intentToPickerAc);
			this.finish();
			break;
		case R.id.planAc_ibtn_entrue:// //可以在这里实现提醒功能

			this.finish();
			break;
		}
	}
}
