package cn.ls.util;

public class AlarmPlan {
	/**
	 * 设置治疗计划配置参数集合 id：标志唯一性 duration：持续时间 times：重复次数 isOnPlan：是否启用
	 */
	private String id;
	private int duration;
	private String times;
	private int startTime;// /类型不清楚
	private int isOnPlan;

	public AlarmPlan(String id, int duration, String times, int startTime,
			int isOnPlan) {
		this.id = id;
		this.duration = duration;
		this.times = times;
		this.startTime = startTime;
		this.isOnPlan = isOnPlan;
	}

	public AlarmPlan() {
       
	}

	public int getIsOnPlan() {
		return isOnPlan;
	}

	public void setIsOnPlan(int isOnPlan) {
		this.isOnPlan = isOnPlan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int i) {
		this.startTime = i;
	}

	public int isOnPlan() {
		return isOnPlan;
	}

	public void setOnPlan(int isOnPlan) {
		this.isOnPlan = isOnPlan;
	}

}
