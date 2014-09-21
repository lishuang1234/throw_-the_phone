package cn.ls.service;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import cn.ls.activity.ForbidActivity;
import cn.ls.activity.LogoActivity;
import cn.ls.activity.MyInforActivity;
import cn.ls.activity.R;
import cn.ls.activity.Tab;
import cn.ls.activity.WarnInfor;
import cn.ls.activity.WorkState;
import cn.ls.util.ComName;
import cn.ls.util.Contant;

public class MainService extends Service {
	/**
	 * 1.对三种模式的倒计时支持 2.玩机时长的计时实现 3.存储玩机时长
	 */
	private Receiver receiver;

	private Timer Dtimer;
	private TimerTask Dtask;
	private Handler handler;
	private Timer Ltimer;
	private TimerTask Ltask;
	private ActivityManager mg;
	private String lastAc;
	private int currentMode;// 传入现在的模式
	private int timeCount;// 模式倒计时递减数字
	private int mTimeCount;// 得到的模式时间总数
	private Vibrator vibrator;
	private Timer Mtimer;
	private TimerTask Mtask;
	private int jiCounts;// 记录玩机时长的计数值
	private SharedPreferences sharedPreTimeCounts;
	private SharedPreferences.Editor editorTimeCounts;
	private int day;
	private boolean modeTwoOnWork;
	private boolean isScrOn;
	private NotificationManager mNotificationManager;
	private int notifyId;
	private AlarmManager am;
	private PendingIntent pi;
	private long duration;
	private String sharePreKeyString;

	/** Notification构造器 */

	/** Notification的ID */
	// private int notifyId = 100;

	private void showNoti(int flag, String showContent, String ticker,
			String showTitle) {
		notifyId = 100;
		Intent intent = null;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		switch (flag) {
		case 0:
			intent = new Intent(MainService.this, Tab.class);
			intent.putExtra("activity", "MyInforActivity");
			break;
		case 1:
			intent = new Intent(MainService.this, ForbidActivity.class);
			intent.putExtra("mode", currentMode);
			break;
		case 2:
			intent = new Intent(MainService.this, WorkState.class);
			intent.putExtra("work_state", false);
			break;
		}

		PendingIntent pIntent = PendingIntent.getActivity(MainService.this, 0,
				intent, 0);
		mBuilder.setSmallIcon(R.drawable.ic_launcher).setTicker(ticker)
				.setContentTitle(showTitle).setContentText(showContent)
				.setContentIntent(pIntent);
		Notification mNotification = mBuilder.build();
		// 设置通知 消息 图标
		mNotification.icon = R.drawable.ic_launcher1;
		// 在通知栏上点击此通知后自动清除此通知
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;// FLAG_ONGOING_EVENT
																// 在顶部常驻，可以调用下面的清除方法去除
																// FLAG_AUTO_CANCEL
																// 点击和清理可以去调
		// 设置显示通知时的默认的发声、震动、Light效果
		// mNotification.defaults = Notification.DEFAULT_VIBRATE;
		// 设置发出消息的内容
		mNotification.tickerText = ticker;
		// 设置发出通知的时间
		mNotification.when = System.currentTimeMillis();
		// mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		// //在通知栏上点击此通知后自动清除此通知
		// mNotification.setLatestEventInfo(this, "常驻测试",
		// "使用cancel()方法才可以把我去掉哦", null); //设置详细的信息 ,这个方法现在已经不用了
		// mNotificationManager.notify(notifyId, mNotification);
		startForeground(notifyId, mNotification);// 保持前台进程
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		modeTwoOnWork = true;
		isScrOn = true;
		setUpReceiver();
		currentMode = -1;
		handler = new MyHandler();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// long[] pattern = {1000, 2000, 1000, 3000}; // OFF/ON/OFF/ON......
		// vibrator.vibrate(pattern, -1);
		cancleJiTimer();
		setUpShared();// 初始化SharedPreference
		getAppDate();// 读取今日时间
		checkJiCounts();// 将存储数据赋值在计时
		startJiShi();
		startAlarm();
	}

	/**
	 * 得到SharedPreferences
	 */
	private void setUpShared() {
		// TODO Auto-generated method stub
		sharedPreTimeCounts = getSharedPreferences("jiShiCounts",
				Activity.MODE_PRIVATE);// 保存当前计时得到的总数目
		editorTimeCounts = sharedPreTimeCounts.edit();
		// editorTimeCounts.clear();
		// editorTimeCounts.commit();
	}

	/**
	 * 得到系统日期
	 */
	private void getAppDate() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		this.day = c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);// 也可以填数字，0-11,一月为0
		c.set(Calendar.DAY_OF_MONTH, (day + 1));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		// 设定时间
		// c.set(2011, 05,28, 19,50, 0);
		// 也可以写在一行里
		duration = c.getTimeInMillis();
		sharePreKeyString = month + "." + day;
		System.out.println("系統日期是：" + day + "..." + month);
	}

	// /**
	// * 得到时间总数转化为时分秒显示
	// */
	// private String refreshTime(long counts) {
	// // TODO Auto-generated method stub
	// long hour, min, sec;
	// hour = counts / 3600;
	// if ((counts / 60) >= 60) {
	// min = counts / 60 - 60 * hour;
	// } else {
	// min = counts / 60;
	// }
	// sec = counts % 60;
	// return hour + ":" + min + ":" + sec;
	// }

	/**
	 * 检查是否有数据存储
	 */
	public void checkJiCounts() {
		if (sharedPreTimeCounts.getInt(sharePreKeyString, 0) != 0) {
			jiCounts = sharedPreTimeCounts.getInt(sharePreKeyString, 0);
			System.out.println("读取存储数据！" + jiCounts);
		} else
			System.out.println("存储数据为空！");
		System.out.println("读取" + (day - 1) + ":存储数据！"
				+ sharedPreTimeCounts.getInt(sharePreKeyString, 0));
	}

	/**
	 * 保存今日使用数据
	 */
	public void saveTimeCounts() {
		System.out.println("保存数据！" + day + ":" + jiCounts);
		editorTimeCounts.putInt(sharePreKeyString, jiCounts);
		editorTimeCounts.commit();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("onLowMemory中保存--》" + "timeCount---" + timeCount);
		mNotificationManager.cancel(notifyId);
		stopForeground(true);
		cancleJiTimer();
		saveTimeCounts();// 保存数据
		unregisterReceiver(receiver);

		Intent intent = new Intent(this, MainService.class);
		intent.putExtra("mode", -1);
		startService(intent);

		super.onDestroy();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// cancleDaoTimer();
		currentMode = intent.getIntExtra("mode", -1);
		int[] getTime = intent.getIntArrayExtra("time_count");
		if (getTime != null)
			mTimeCount = timeCount = getTime[0] * 3600 + getTime[1] * 60;
		switch (currentMode) {
		case 0:
			lanJie();
			daoJiShi(0);
			showNoti(1, "正在接受治疗中，时长：" + Contant.refreshTime(timeCount),
					"正在接受治疗", "正在接受治疗中");
			break;
		case 1:
			daoJiShi(1);
			showNoti(1, "今日手机可用时长：" + Contant.refreshTime(mTimeCount), "开始计时",
					"正在计时中");
			break;
		case 2:
			daoJiShi(2);
			showNoti(1, "正在进行全天陪护，时长：" + Contant.refreshTime(timeCount),
					"全天陪护", "全天陪护中");
			break;
		case -1:
			showNoti(0, "今日手机已使用：" + Contant.refreshTime(jiCounts), "正在后台运行中",
					"手机正在使用中");
			break;
		default:
			break;
		}
		// am.cancel(pi);

		return START_STICKY_COMPATIBILITY;

	}

	private void startAlarm() {// 设置零点清零
		// TODO Auto-generated method stub
		Intent alramIntent = new Intent(ComName.MY_ALARM);
		pi = PendingIntent.getBroadcast(MainService.this, 1, alramIntent, 1);
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, duration, 24 * 60 * 60 * 1000,
				pi);
		System.out.println("start alarm");
	}

	/**
	 * 启动计时
	 */
	private synchronized void startJiShi() {
		// TODO Auto-generated method stub
		Mtimer = new Timer();
		Mtask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				jiCounts++;
				sendCastToMyInfor();// 发送给MyInfor
				System.out.println("正在计时sendCastToMyInfor" + jiCounts);
			}
		};
		Mtimer.schedule(Mtask, 0, 1000);
	}

	/**
	 * 发送MyInfor广播
	 */
	protected void sendCastToMyInfor() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(ComName.REFRESH_JI_TIME);
		intent.putExtra("time_ji_counts", jiCounts);
		sendBroadcast(intent);
	}

	/**
	 * 取消计时
	 */
	private void cancleJiTimer() {
		if (Mtask != null) {
			Mtask.cancel();
			Mtask = null;
		}
		if (Mtimer != null) {
			Mtimer.cancel();
			Mtimer.purge();
			Mtimer = null;
		}

	}

	/**
	 * 实现拦截软件功能
	 */
	private void lanJie() {
		Ltimer = new Timer();
		Ltask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mg == null) {
					mg = (ActivityManager) MainService.this
							.getSystemService(ACTIVITY_SERVICE);
				}
				List<RecentTaskInfo> list = mg.getRecentTasks(2,
						ActivityManager.RECENT_WITH_EXCLUDED);
				RecentTaskInfo task = list.get(0);
				Intent intent = task.baseIntent;
				String recentTaskName = intent.getComponent().getPackageName();
				if (lastAc != null) {
					if (lastAc.endsWith(recentTaskName)
							&& !recentTaskName.equals("cn.ls.activity")
							&& (!recentTaskName
									.equals(getLauncherPackageName(MainService.this)))) {

						Intent newIntent = new Intent(MainService.this,
								WarnInfor.class);// 拦截之后的跳转页
						newIntent.putExtra("mode", currentMode);
						newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(newIntent);

					}
				}
				lastAc = recentTaskName;
			}
		};
		Ltimer.schedule(Ltask, 0, 10);// 开始拦截软件
	}

	/**
	 * 获取正在运行桌面包名（注：存在多个桌面时且未指定默认桌面时，该方法返回Null,使用时需处理这个情况）
	 */
	public String getLauncherPackageName(Context context) {
		final Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		final ResolveInfo res = context.getPackageManager().resolveActivity(
				intent, 0);
		if (res.activityInfo == null) {
			// should not happen. A home is always installed, isn't it?
			return null;
		}
		if (res.activityInfo.packageName.equals("android")) {
			// 有多个桌面程序存在，且未指定默认项时；
			return null;
		} else {
			return res.activityInfo.packageName;
		}
	}

	/**
	 * 发送广播
	 */
	private void sendCastToForbid() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(ComName.REFRESH_DAO_TIME);
		intent.putExtra("time_refresh", timeCount);
		intent.putExtra("time_counts", mTimeCount);
		sendBroadcast(intent);

	}

	/**
	 * 注册广播
	 */
	private void setUpReceiver() {
		// TODO Auto-generated method stub
		receiver = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ComName.TIME_COUNTS);
		filter.addAction(ComName.CANCLE_TIME);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(ComName.CANCLE_LAN_JIE);
		filter.addAction(ComName.MY_ALARM);
		registerReceiver(receiver, filter);
	}

	/**
	 * 倒计时函数
	 */
	public synchronized void daoJiShi(final int mode) {
		Dtimer = new Timer(true);
		Dtask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(mode);
			}
		};
		Dtimer.schedule(Dtask, 0, 1000);
	}

	/**
	 * 取消倒计时
	 */
	public void cancleDaoTimer() {
		// TODO Auto-generated method stub
		if (Dtask != null) {
			Dtask.cancel();
			Dtask = null;
		}
		if (Dtimer != null) {
			Dtimer.cancel();
			Dtimer.purge();
			Dtimer = null;
			// handler.removeMessages(currentMode);
		}
	}

	/**
	 * 取消拦截
	 */
	public void cancleLanTimer() {

		if (Ltask != null) {
			Ltask.cancel();
			Ltask = null;
		}
		if (Ltimer != null) {
			Ltimer.cancel();
			Ltimer.purge();
			Ltimer = null;
		}
	}

	/**
	 * 处理及时Handler
	 * 
	 * @author LS
	 * 
	 */
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:// 模式0
				sendCastToForbid();// 发送广播更新显示时间
				timeCount--;
				if (timeCount < 0) {// 关闭倒计时与拦截功能
					cancleDaoTimer();
					sendSuccessCastToForbid();
					vibrator.vibrate(1000);
					cancleLanTimer();
					// ///////////////////////////////////////////////
					showNoti(0, "今日手机已使用：" + Contant.refreshTime(jiCounts),
							"恭喜治疗成功！", "手机正在使用中");
				}
				break;
			case 1:
				sendCastToForbid();// 发送广播更新显示时间
				timeCount--;
				if (timeCount < 0) {// 開啓拦截功能
					lanJie();// 进行拦截软件
					vibrator.vibrate(1000);
					cancleDaoTimer();
					showNoti(1, "今日手机已使用：" + Contant.refreshTime(jiCounts),
							"禁止使用手机", "手机正在使用中");
				}
				break;
			case 2:
				sendCastToForbid();// 发送广播更新显示时间
				timeCount--;
				if (timeCount < 0) {// 開啓拦截功能
					if (modeTwoOnWork) {
						lanJie();// 进行拦截软件//暂定
						vibrator.vibrate(1000);
						cancleDaoTimer();
						timeCount = 60;
						modeTwoOnWork = false;
						daoJiShi(2);
					} else {
						cancleDaoTimer();
						timeCount = mTimeCount;
						modeTwoOnWork = true;
						cancleLanTimer();
						if (isScrOn)
							daoJiShi(2);
					}
				}
				break;
			}
		}
	}

	private void sendSuccessCastToForbid() {
		// TODO Auto-generated method stub
		System.out.println("治疗成功！！在MianService");
		Intent intent = new Intent();
		intent.setAction(ComName.ZHI_LIAO_SUCCESS);
		sendBroadcast(intent);
	}

	/**
	 * 广播接收器
	 * 
	 * @author LS
	 * 
	 */
	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (currentMode == 2) {// 模式2的关屏开屏处理
				if (action.equals(Intent.ACTION_SCREEN_OFF)) {
					if (modeTwoOnWork) {
						cancleDaoTimer();
						timeCount = mTimeCount;
					}
				} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
					if (modeTwoOnWork) {
						daoJiShi(2);
					}
				}
			} else if (currentMode == 1) {// 模式1的关屏处理
				if (action.equals(Intent.ACTION_SCREEN_OFF)) {
					cancleDaoTimer();
				} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
					daoJiShi(1);
					showNoti(1, "今日手机可用时长：" + Contant.refreshTime(mTimeCount)
							+ "  可用时长：" + Contant.refreshTime(timeCount),
							"开始计时", "正在计时中");
				} else if (currentMode == 0) {// 模式0的关屏处理
					if (action.equals(Intent.ACTION_SCREEN_OFF)) {
						cancleLanTimer();
					} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
						lanJie();
					}
				}
			}
			if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				isScrOn = false;
				cancleJiTimer();
				saveTimeCounts();
			} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
				isScrOn = true;
				if (currentMode == -1) {
					showNoti(0, "今日手机已使用：" + Contant.refreshTime(jiCounts),
							"正在后台运行中", "手机正在使用中");
				}
				cancleJiTimer();
				checkJiCounts();
				startJiShi();
			} else if (action.equals(ComName.MY_ALARM)) {// 日期改变的广播
				saveTimeCounts();
				getAppDate();// 重新获取改变的时间
				jiCounts = 0;

			} else if (action.equals(ComName.CANCLE_LAN_JIE)) {
				cancleLanTimer();
				if (intent.getBooleanExtra("work_state", true)) {
					showNoti(0, "今日手机已使用：" + Contant.refreshTime(jiCounts),
							"治疗成功！", "手机正在使用中");
				}else
				showNoti(0, "今日手机已使用：" + Contant.refreshTime(jiCounts),
						"放弃治疗！", "手机正在使用中");
			} else if (action.equals(ComName.CANCLE_TIME)) {
				cancleDaoTimer();
				showNoti(2, "今日手机已使用：" + Contant.refreshTime(jiCounts),
						"真是懦弱！", "手机正在使用中");
			}
		}
	}

}