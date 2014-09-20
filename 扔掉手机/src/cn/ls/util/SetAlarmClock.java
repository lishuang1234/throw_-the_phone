package cn.ls.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import cn.ls.db.DBHelper;
import cn.ls.service.MainService;

/**
 * 实现设置闹钟功能: 考虑：1. 设置闹钟时间 2.设置重复次数
 * 
 * @author LS
 * 
 */
public class SetAlarmClock {
	private Context context;
	// private Intent[] alarmIntent = new Intent[7];
	private int requestCode;// PendingIntent唯一标示否则会被覆盖掉
	private DBHelper dbHelper;// 查询数据库助手类
	private AlarmPlan alarmPlan;

	public SetAlarmClock(Context context, DBHelper dbHelper) {
		this.context = context;
		this.dbHelper = new DBHelper(context);
	}

	public void setAlarm(String id) {

		this.requestCode = Integer.valueOf(id);// 标示
		alarmPlan = dbHelper.quary(id);

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		int[] durationCounts = Contant
				.changeTimeCounts(alarmPlan.getDuration());
		boolean[] isChongFu = Contant.getChongFuFlag(alarmPlan.getTimes());
		// FLAG_ONE_SHOT
		if (!alarmPlan.getTimes().equals("无重复")) {// 如果有设置重复
			System.out.println("启动重复计划===》"
					+ Contant.refreshTime(alarmPlan.getStartTime()) + "---->>"
					+ alarmPlan.getTimes());
			for (int i = 0; i < isChongFu.length; i++) {
				if (isChongFu[i]) {
					System.out.println("重复的星期--->" + i);
					// alarmIntent[i].putExtra("mode", 0);
					// alarmIntent[i].putExtra("time_count", durationCounts);
					Intent repeatIntent = new Intent();
					repeatIntent.putExtra("mode", 0);
					repeatIntent.putExtra("time_count", durationCounts);
					repeatIntent.setClass(context, MainService.class);
					PendingIntent pi = PendingIntent.getService(context,
							(requestCode + i * 100), repeatIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);// 如果当前系统中已经存在一个相同的PendingIntent对象，那么就将先将已有的PendingIntent取消，然后重新生成一个PendingIntent对象。
					am.setRepeating(AlarmManager.RTC_WAKEUP,
							getMillis(alarmPlan.getStartTime(), i),// ///////////此处参数有问题
							7 * 24 * 60 * 60 * 1000, pi);// 现在开始 周期七天
				} else {// 没选中的取消
					Intent repeatIntent = new Intent();
					System.out.println("取消的星期：：：" + i);
					repeatIntent.setClass(context, MainService.class);
					repeatIntent.putExtra("mode", 0);
					repeatIntent.putExtra("time_count", durationCounts);
					PendingIntent pi = PendingIntent.getService(context,
							(requestCode + i * 100), repeatIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					am.cancel(pi);
				}
			}
		} else// 只是今日
		{
			System.out.println("启动计划===不重複--》"
					+ Contant.refreshTime(alarmPlan.getStartTime()) + "---->>");
			Intent intent = new Intent();
			intent.setClass(context, MainService.class);
			intent.putExtra("mode", 0);
			intent.putExtra("time_count", durationCounts);
			PendingIntent pi = PendingIntent.getService(context, requestCode,
					intent, PendingIntent.FLAG_ONE_SHOT);// 如果当前系统中已经存在一个相同的PendingIntent对象，那么就将先将已有的PendingIntent取消，然后重新生成一个PendingIntent对象。
			am.set(AlarmManager.RTC_WAKEUP,
					getMillis(alarmPlan.getStartTime()), pi);
		}

	}
//
//	public void cancleNoRepeatAlarm(String id) {
//		this.requestCode = Integer.valueOf(id);// 标示
//		alarmPlan = dbHelper.quary(id);
//
//		AlarmManager am = (AlarmManager) context
//				.getSystemService(Context.ALARM_SERVICE);
//		int[] durationCounts = Contant
//				.changeTimeCounts(alarmPlan.getDuration());
//		Intent intent = new Intent();
//		intent.setClass(context, MainService.class);
//		intent.putExtra("mode", 0);
//		intent.putExtra("time_count", durationCounts);
//		PendingIntent pi = PendingIntent.getService(context, requestCode,
//				intent, PendingIntent.FLAG_ONE_SHOT);// 如果当前系统中已经存在一个相同的PendingIntent对象，那么就将先将已有的PendingIntent取消，然后重新生成一个PendingIntent对象。
//		am.cancel(pi);
//	}

	public void cancleAlarm(String id) {
		this.requestCode = Integer.valueOf(id);// 标示
		alarmPlan = dbHelper.quary(id);

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		int[] durationCounts = Contant
				.changeTimeCounts(alarmPlan.getDuration());
		boolean[] isChongFu = Contant.getChongFuFlag(alarmPlan.getTimes());
		if (!alarmPlan.getTimes().equals("")) {// 如果有设置重复
			// FLAG_ONE_SHOT
			for (int i = 0; i < isChongFu.length; i++) {
				if (isChongFu[i]) {
					Intent repeatIntent = new Intent();
					System.out.println("删除计划取消的星期：：：" + i);
					repeatIntent.setClass(context, MainService.class);
					repeatIntent.putExtra("mode", 0);
					repeatIntent.putExtra("time_count", durationCounts);
					PendingIntent pi = PendingIntent.getService(context,
							(requestCode + i * 100), repeatIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					am.cancel(pi);
				}
			}
		} else {
			Intent intent = new Intent();
			intent.setClass(context, MainService.class);
			intent.putExtra("mode", 0);
			intent.putExtra("time_count", durationCounts);
			PendingIntent pi = PendingIntent.getService(context, requestCode,
					intent, PendingIntent.FLAG_ONE_SHOT);// 如果当前系统中已经存在一个相同的PendingIntent对象，那么就将先将已有的PendingIntent取消，然后重新生成一个PendingIntent对象。
			am.cancel(pi);

		}
	}

	private long getMillis(int startTime, int i) {
		// TODO Auto-generated method stub
		i = i + 2;
		if (i == 8) {
			i = 1;
		}
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int week = c.get(Calendar.DAY_OF_WEEK);// 周日是第一天
		System.out.println("本周星期--->>>" + week);
		c.set(Calendar.YEAR, year);// 设置年
		c.set(Calendar.MONTH, month);// 设置月份
		c.set(Calendar.HOUR_OF_DAY, Contant.getHour(startTime));// 设置小时
		c.set(Calendar.MINUTE, Contant.getMin(startTime));// 设置分钟
		c.set(Calendar.SECOND, 0);// 设置秒钟
		if (week > i) {// 表明已经今天过了计划的星期
			c.set(Calendar.DAY_OF_MONTH, day + (7 - (week - i)));// 设置过去的时间
			System.out.println("今天过了计划的星期--->>>下周计划日期："
					+ (day + (7 - (week - i))));
		} else if (week == i) {//
			c.set(Calendar.DAY_OF_MONTH, day);// 设置將來的时间
			System.out.println("计划星期就在今天的日期--->>>：" + day);
			if (c.getTimeInMillis() < System.currentTimeMillis()) {
				c.set(Calendar.DAY_OF_MONTH, day + 7);// 今天日期已过一周后生肖
			}
		} else {
			c.set(Calendar.DAY_OF_MONTH, day + (i - week));// 设置將來的时间
			System.out.println("计划星期将来日期--->>>：" + (day + (i - week)));
		}
		return c.getTimeInMillis();
	}

	private long getMillis(int startTime) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);// 也可以填数字，0-11,一月为0
		c.set(Calendar.HOUR_OF_DAY, Contant.getHour(startTime));
		c.set(Calendar.MINUTE, Contant.getMin(startTime));
		c.set(Calendar.SECOND, 0);
		// 设定时间
		// c.set(2011, 05,28, 19,50, 0);
		// 也可以写在一行里
		if (c.getTimeInMillis() < System.currentTimeMillis()) {
			c.set(Calendar.DAY_OF_MONTH, day + 1);
		}
		return c.getTimeInMillis();

	}
}
