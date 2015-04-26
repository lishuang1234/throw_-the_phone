package cn.ls.util;


public class Contant {

	final static String[] week = { "周一", "周二", "周三", "周四", "周五", "周六",
			"周日" };
//	final static Intent[] alarmIntent = new Intent[7];

	public static boolean[] getChongFuFlag(String str) {
		String[] zone = str.split(",");
		boolean[] isChongFu = new boolean[] { false, false, false, false,
				false, false, false };
		for (int i = 0; i < zone.length; i++) {
			System.out.println("zone" + zone[i]);
			if (zone[i].equals(week[0])) {
				isChongFu[0] = true;
			} else if (zone[i].equals(week[1])) {
				isChongFu[1] = true;
			} else if (zone[i].equals(week[2])) {
				isChongFu[2] = true;
			} else if (zone[i].equals(week[3])) {
				isChongFu[3] = true;
			} else if (zone[i].equals(week[4])) {
				isChongFu[4] = true;
			} else if (zone[i].equals(week[5])) {
				isChongFu[5] = true;
			} else if (zone[i].equals(week[6])) {
				isChongFu[6] = true;
			}
		}
		return isChongFu;
	}

	public static String getChongFuString(boolean[] b) {
		StringBuffer buffer = new StringBuffer();
		int  i2 = 0; 
		for (int i = 0; i < b.length; i++) {
			if (b[i]) {
//				i2++;
//				if(i2>=5){
//					
//				}
				buffer.append(week[i] + ",");
			}
		}
		System.out.println("得到的星期字符串--->" + buffer.toString());
		// getChongFuFlag(buffer.toString());
		return buffer.toString();
	}

	/**
	 * 得到时间总数转化为时分秒显示
	 */
	public static String refreshTime(int counts) {
		// TODO Auto-generated method stub
		int hour, min, sec;
		hour = counts / 3600;
		if ((counts / 60) >= 60) {
			min = counts / 60 - 60 * hour;
		} else {
			min = counts / 60;
		}
		sec = counts % 60;
		return hour + ":" + min+":"+sec;
	}

	/**
	 * 得到时间总数转化为时分秒显示
	 */
	public static String refreshTimeCounts(int counts) {
		// TODO Auto-generated method stub
		long hour, min, sec;
		hour = counts / 3600;
		if ((counts / 60) >= 60) {
			min = counts / 60 - 60 * hour;
		} else {
			min = counts / 60;
		}
		sec = counts % 60;
		return hour + "小时" + min + "分";
	}

	/**
	 * 得到时间总数转化为时分秒显示
	 */
	public static int[] changeTimeCounts(int counts) {
		// TODO Auto-generated method stub
		long hour, min, sec;
		int[] i = new int[2];
		hour = counts / 3600;
		if ((counts / 60) >= 60) {
			min = counts / 60 - 60 * hour;
		} else {
			min = counts / 60;
		}
		sec = counts % 60;
		i[0] = (int) hour;
		i[1] = (int) min;
		return  i;
	}

	/**
	 * 得到时间总数返回时间
	 */
	public static int getHour(int counts) {
		// TODO Auto-generated method stub
		int hour, min, sec;
		hour = counts / 3600;
		if ((counts / 60) >= 60) {
			min = counts / 60 - 60 * hour;
		} else {
			min = counts / 60;
		}
		sec = counts % 60;
		return hour;
	}

	/**
	 * 得到时间总数返回分钟显示
	 */
	public static int getMin(int counts) {
		// TODO Auto-generated method stub
		int hour, min, sec;
		hour = counts / 3600;
		if ((counts / 60) >= 60) {
			min = counts / 60 - 60 * hour;
		} else {
			min = counts / 60;
		}
		sec = counts % 60;
		return min;
	}

//	public static Intent[] initIntent(Context context) {
//		Intent monIntent = new Intent(context, MainService.class);
//		Intent tuesIntent = new Intent(context, MainService.class);
//		Intent wedIntent = new Intent(context, MainService.class);
//		Intent thursIntent = new Intent(context, MainService.class);
//		Intent friIntent = new Intent(context, MainService.class);
//		Intent saturIntent = new Intent(context, MainService.class);
//		Intent sunIntent = new Intent(context, MainService.class);
//		alarmIntent[0] = monIntent;
//		alarmIntent[1] = tuesIntent;
//		alarmIntent[2] = wedIntent;
//		alarmIntent[3] = thursIntent;
//		alarmIntent[4] = friIntent;
//		alarmIntent[5] = saturIntent;
//		alarmIntent[6] = sunIntent;
//		return alarmIntent;
//	}
}
