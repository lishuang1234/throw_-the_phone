package cn.ls.activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ls.util.ComName;
import cn.ls.util.Contant;
import cn.ls.util.ImageDownLoader;
import cn.ls.util.ShareSDKHelper;
import cn.ls.view.CircleImagine;
import cn.ls.view.LineChartShow;
import cn.ls.view.TitleView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

public class MyInforActivity extends Activity implements View.OnClickListener{
	// private String platName;
	private CircleImagine user_photo;
	private TextView user_name;
	private String photo_path;
	private Bitmap bit;
	private Handler handler;
	private Receiver receiver;
	private LineChartShow lineChart;
	// private long jiCounts;
	private TextView jinRiShow;
	private TextView pingJunShow;
	private SharedPreferences sharedPre;
	private ShareSDKHelper sdkHelper;
	private int day;
	private int month;
	private TitleView title;
	private ImageDownLoader imageDownLoader;
	private TextView chengHao;
	private String[] chengHaoString;
	private ImageView qqImageView;
	private ImageView weiBoImageView;
    private 		int jiCounts;
 private int avare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfor_activity);
		checkLogin();
		setUpViews();// 初始化控件
		getAppDate();
		setUpShared();
		setUpData();// 传入绘图坐标
		setUpReceiver();// 注册广播
		getUserInfor();// 获取用户信息
		// handler = new PhotoHandler();
		System.out
				.println("18号的时长" + sharedPre.getInt(Integer.toString(18), 0));
	}

	private void checkLogin() {
		// TODO Auto-generated method stub
		sdkHelper = new ShareSDKHelper(MyInforActivity.this);
		if (sdkHelper.getInitPlat() == null) {
			startActivity(new Intent(MyInforActivity.this, LoginActivity.class));
			this.finish();
		}
	}

	private void setUpShared() {
		// TODO Auto-generated method stub
		sharedPre = getSharedPreferences("jiShiCounts", Activity.MODE_PRIVATE);
	}

	private void getAppDate() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		// int year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
		this.day = c.get(Calendar.DAY_OF_MONTH);

	}

	/**
	 * 读取历史用机时长
	 */
	public ArrayList<double[]> getHyCounts() {
		ArrayList<double[]> list = new ArrayList<double[]>();
		int counts = 0;
		for (int i = 1; i < day; i++) {
			counts = sharedPre.getInt(month + "." + i, 0);
			avare = avare + counts;
			list.add(new double[] { i, (long) (counts / 3600.0 * 100) / 100.0 });
			System.out.println("历史时长：" + ((long) (counts / 3600 * 1000))
					/ 1000.0);
		}
		pingJunShow.setText(Contant.refreshTime(avare / day));
		return list;
	}

	/**
	 * 传入绘图坐标
	 */
	private void setUpData() {
		// TODO Auto-generated method stub
		lineChart = new LineChartShow(MyInforActivity.this);
		lineChart.setUpData(getHyCounts());
	}

	/**
	 * 注册刷新计时广播
	 */
	private void setUpReceiver() {
		// TODO Auto-generated method stub
		receiver = new Receiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ComName.REFRESH_JI_TIME);
		registerReceiver(receiver, filter);
	}

	/**
	 * 获取帐号信息
	 */
	private void getUserInfor() {
		Platform plat = sdkHelper.getInitPlat();

		if (plat != null) {
			photo_path = plat.getDb().getUserIcon();
			// System.out.println("photo_path----->" + photo_path);
			bit = imageDownLoader.showCacheImagne(photo_path.replaceAll(
					"[^\\w]", ""));
			if (bit == null)
				imageDownLoader.downLoadImage(photo_path);
			user_photo.setImageBitmap(bit);
			user_name.setText(plat.getDb().getUserName());
			if (plat.getName().equals(QZone.NAME)) {
				qqImageView.setVisibility(View.VISIBLE);
			} else if (plat.getName().equals(SinaWeibo.NAME)) {
				weiBoImageView.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 初始化控件
	 */
	private void setUpViews() {
		// TODO Auto-generated method stub
		imageDownLoader = new ImageDownLoader(this);
		user_photo = (CircleImagine) findViewById(R.id.user_photo);
		user_name = (TextView) findViewById(R.id.user_name);
		qqImageView = (ImageView) findViewById(R.id.myinforAc_img_qq);
		weiBoImageView = (ImageView) findViewById(R.id.myinforAc_img_weibo);
		chengHao = (TextView) findViewById(R.id.chenghao);
		jinRiShow = (TextView) findViewById(R.id.myinfor_tx_jinritime);
		pingJunShow = (TextView) findViewById(R.id.myinfor_tx_pingjuntime);
        ( (RelativeLayout)findViewById(R.id.myinforAc_rl_infor)).setOnClickListener(this);
        ( (RelativeLayout)findViewById(R.id.myinforAc_rl_show)).setOnClickListener(this);
		// 设置标题
		title = (TitleView) findViewById(R.id.myinforAc_title);
		title.setTitle("我");
		title.setBtnVisible(false, false, false, true);
		chengHaoString = new String[] { "安卓接触者", "安卓感染者 ", "安卓依赖患者", "安卓重症患者",
				"安卓病危患者" };

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int counts = 0;
		counts = sharedPre.getInt(month + "." + day, 0);
		if (counts < 60 * 60) {
			chengHao.setText(chengHaoString[0]);
			chengHao.setTextColor(getResources().getColor(
					R.color.myinforAc_tx_chenghao_0));
			return;
		} else if (counts < 60 * 60 * 2) {
			chengHao.setText(chengHaoString[1]);
			chengHao.setTextColor(getResources().getColor(
					R.color.myinforAc_tx_chenghao_1));
			return;
		} else if (counts < 60 * 60 * 3) {
			chengHao.setText(chengHaoString[2]);
			chengHao.setTextColor(getResources().getColor(
					R.color.myinforAc_tx_chenghao_2));
			return;
		} else if (counts < 60 * 60 * 4) {
			chengHao.setText(chengHaoString[3]);
			chengHao.setTextColor(getResources().getColor(
					R.color.myinforAc_tx_chenghao_3));
			return;
		} else if (counts > 60 * 60 * 4) {
			chengHao.setText(chengHaoString[4]);
			chengHao.setTextColor(getResources().getColor(
					R.color.myinforAc_tx_chenghao_4));
			return;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		sdkHelper.stopSDK();
	}

    @Override
    public void onClick(View view) {
        Intent intent  = new Intent();
        switch (view.getId()){

            case  R.id.myinforAc_rl_infor://个人登录信息，点击说明称号意义
                intent.putExtra("layoutFlag", 4);
                intent.setClass(MyInforActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.myinforAc_rl_show://分享个人使用时长
                sdkHelper.shareToPlat("快来看看我的手机使用时长！","今日手机使用："+Contant.refreshTime(jiCounts)+"历史平均使用："+Contant.refreshTime(avare));
                break;
        }


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

			// System.out.println("xx" + action);
			if (action.equals(ComName.REFRESH_JI_TIME)) {
				jiCounts = intent.getIntExtra("time_ji_counts", 0);
				// 显示倒计时
				// System.out.println("正在计时jiCounts" + jiCounts);
				jinRiShow.setText(Contant.refreshTime(jiCounts));
			}
		}
	}

}
