package cn.ls.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.ls.util.AlarmPlan;

public class DBHelper {
	private DBOpenHelper openHelper;

	public DBHelper(Context context) {
		openHelper = new DBOpenHelper(context, "alarmPlan_db");// 新建数据库
	}

	// 添加
	public Long insert(AlarmPlan plan) {
		ContentValues values = new ContentValues();
		// 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
		values.put("alarmPlan_id", plan.getId());
		values.put("alarmPlan_duration", plan.getDuration());
		values.put("alarmPlan_times", plan.getTimes());
		values.put("alarmPlan_startTime", plan.getStartTime());
		values.put("alarmPlan_isOnPlan", plan.getIsOnPlan());
		SQLiteDatabase db = openHelper.getWritableDatabase();
		// 调用insert方法，就可以将数据插入到数据库当中
		// 第一个参数:表名称
		// 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
		// 第三个参数：ContentValues对象
		Long backinfo = db.insert("alarmPlan", null, values);
		db.close();
		return backinfo;
	}

	// 删除
	public void delect(String id) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.delete("alarmPlan", "alarmPlan_id=?", new String[] { id });
		db.close();
	}

	// 更新
	public void upDate(AlarmPlan plan) {
		ContentValues values = new ContentValues();
		// 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
		values.put("alarmPlan_duration", plan.getDuration());
		values.put("alarmPlan_times", plan.getTimes());
		values.put("alarmPlan_startTime", plan.getStartTime());
		values.put("alarmPlan_isOnPlan", plan.getIsOnPlan());
		SQLiteDatabase db = openHelper.getWritableDatabase();
		db.update("alarmPlan", values, "alarmPlan_id=?",
				new String[] { plan.getId() });
		System.out.println("DB更新！ID"+plan.getId());
		db.close();
	}

	// 查询全部
	public List<AlarmPlan> quaryAll() {
		List<AlarmPlan> list = new ArrayList<AlarmPlan>();
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.query("alarmPlan", null, null, null, null, null,
				null);
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndex("alarmPlan_id"));
			int duration = cursor.getInt(cursor
					.getColumnIndex("alarmPlan_duration"));
			String times = cursor.getString(cursor
					.getColumnIndex("alarmPlan_times"));
			int startTime = cursor.getInt(cursor
					.getColumnIndex("alarmPlan_startTime"));
			int isOnPlan = cursor.getInt(cursor
					.getColumnIndex("alarmPlan_isOnPlan"));
			list.add(new AlarmPlan(id, duration, times, startTime, isOnPlan));
		}
		db.close();
		cursor.close();
		return list;
	}

	// 查询部分
	public AlarmPlan quary(String id) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = db.query("alarmPlan", null, "alarmPlan_id=?",
				new String[] { id }, null, null, null);
	//	System.out.println("cursor counts" + cursor.getCount());
		cursor.moveToFirst();
		//System.out.println("cursor counts2222" + cursor.getCount());
		int duration = cursor.getInt(cursor
				.getColumnIndex("alarmPlan_duration"));
		
		String times = cursor.getString(cursor
				.getColumnIndex("alarmPlan_times"));
		int startTime = cursor.getInt(cursor
				.getColumnIndex("alarmPlan_startTime"));
	//	System.out.println("cursor 时间" + startTime);
		int isOnPlan = cursor.getInt(cursor
				.getColumnIndex("alarmPlan_isOnPlan"));
		db.close();
		cursor.close();
		return new AlarmPlan(id, duration, times, startTime, isOnPlan);

	}
}
