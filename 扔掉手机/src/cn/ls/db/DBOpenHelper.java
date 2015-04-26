package cn.ls.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String CREAT_SQL = "create table alarmPlan" + "("
			+ "id int primary key,"
			+ "alarmPlan_id varchar(6)  NOT NULL, "// 计划ID唯一性
			+ "alarmPlan_duration int NOT NULL, "// 计划时长
			+ "alarmPlan_times varchar(20) ,"// 设置重复次数
			+ "alarmPlan_startTime int NOT NULL, "// 开始时间
			+ "alarmPlan_isOnPlan int NOT NULL " + ")";// 是否啓用

	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DBOpenHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DBOpenHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a database");
		db.execSQL(CREAT_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
