package cn.ls.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import cn.ls.service.MainService;
import cn.ls.view.TitleView;

public class StartActivity extends Activity implements OnClickListener,
        OnValueChangeListener {
    private ImageView circleImg;
    private Button smallButton;
    private ImageView hourImg;
    private ImageView min1Img;
    private ImageView min2Img;
    private Button changeBtn;
    private NumberPicker hour;
    private NumberPicker min;
    private int mHour = 1;
    private int mMin = 30;
    private int currentMode;// 代表三种模式，0：立即扔掉手机 1：今日玩机时长 2：健康模式
    private int[] txLargeString;
    private int[] txSmallString;
    private int[] circleImgBgRes;
    private int[] digtalNumberRes;
    private TextView txLarge;
    private TextView txSmall;

    private AlertDialog.Builder builderTimePicker;
    private AlertDialog dialogTimePicker;
    private TitleView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        setUpViews();
        setUpData();
        // setUpDia();
        testDisplay();
        startService(new Intent(StartActivity.this, MainService.class));
    }

    private void testDisplay() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        System.out.println("DisplayMetrics density " + metrics.density);
        System.out.println("DisplayMetrics densityDpi " + metrics.densityDpi);
        System.out.println("DisplayMetrics scaledDensity " + metrics.scaledDensity);


    }

    private void setUpData() {
        // TODO Auto-generated method stub
        txLargeString = new int[]{R.string.startAc_txLarge_mode1,
                R.string.startAc_txLarge_mode2, R.string.startAc_txLarge_mode3};
        txSmallString = new int[]{R.string.startAc_txSmall_mode1,
                R.string.startAc_txSmall_mode2, R.string.startAc_txSmall_mode3};
        circleImgBgRes = new int[]{R.drawable.ic_circle_large_yellow, R.drawable.ic_circle_large_blue, R.drawable.ic_circle_large_red};
        digtalNumberRes = new int[]{R.drawable.digital_number_0, R.drawable.digital_number_1,
                R.drawable.digital_number_2, R.drawable.digital_number_3, R.drawable.digital_number_4, R.drawable.digital_number_5,
                R.drawable.digital_number_6, R.drawable.digital_number_7, R.drawable.digital_number_8,
                R.drawable.digital_number_9
        };
    }

    /**
     * 初始化View相关
     */
    private void setUpViews() {
        // TODO Auto-generated method stub
        circleImg = (ImageView) findViewById(R.id.start_btn);
        //  circleImg.setActivity(StartActivity.this);
        hourImg = (ImageView) findViewById(R.id.startAc_img_hour);//小时
        min1Img = (ImageView) findViewById(R.id.startAc_img_min1);//分钟十位
        min2Img = (ImageView) findViewById(R.id.startAc_img_min2);//分钟个位
        smallButton = (Button) findViewById(R.id.small_btn);//执行按钮
        changeBtn = (Button) findViewById(R.id.start_chang_btn);//切换按钮
        txLarge = (TextView) findViewById(R.id.startAc_tx_large);
        txSmall = (TextView) findViewById(R.id.startAc_tx_small);
        circleImg.setOnClickListener(this);
        smallButton.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View diaLayout = (View) inflater.inflate(R.layout.timepicker, null);//选择时长布局
        hour = (NumberPicker) diaLayout.findViewById(R.id.hour);
        min = (NumberPicker) diaLayout.findViewById(R.id.min);
        hour.setOnValueChangedListener(this);
        min.setOnValueChangedListener(this);
        diaLayout.findViewById(R.id.timepicker_ibtn_entrue).setOnClickListener(
                this);//时长确认按钮
        diaLayout.findViewById(R.id.timepicker_ibtn_noentrue)
                .setOnClickListener(this);//时长取消按钮
        builderTimePicker = new AlertDialog.Builder(this);
        builderTimePicker.setView(diaLayout);
        dialogTimePicker = builderTimePicker.create();
        // 设置标题属性
        title = (TitleView) findViewById(R.id.startAc_title);
        title.setTitle("开始");
        title.setBtnVisible(false, true, true, false);
    }

    /**
     * Button监听器
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.start_btn://圆形背景
                initNumPicker();
                dialogTimePicker.show();
                break;
            case R.id.timepicker_ibtn_noentrue://取消按钮
                dialogTimePicker.dismiss();
                break;
            case R.id.timepicker_ibtn_entrue://确认按钮
                dialogTimePicker.dismiss();
                hourImg.setImageResource(digtalNumberRes[mHour]);
                min1Img.setImageResource(digtalNumberRes[mMin / 10]);
                min2Img.setImageResource(digtalNumberRes[mMin % 10]);
                //	circleImg.setViewTime(mHour, mMin / 10, mMin % 10);//
                break;
            case R.id.small_btn://执行按钮
                Intent intent = new Intent(StartActivity.this, ForbidActivity.class);
                intent.putExtra("mode", currentMode);
                startActivity(intent);// 跳转到倒计时
                startServiceToDao();// 发送广播倒计时
                this.finish();
                break;
            case R.id.start_chang_btn:
                changeMode();// 改变模式
                //	circleImg.setBg(currentMode);
                circleImg.setImageResource(circleImgBgRes[currentMode]);
                break;
        }
    }

    private void initNumPicker() {
        // TODO Auto-generated method stub
        if (currentMode == 0) {// 模式0
            initNumPicker(2, 59);
        } else if (currentMode == 1) {
            initNumPicker(3, 59);
        } else if (currentMode == 2) {
            mHour = 0;
            initNumPicker(0, 30);
        }
    }

    private void initNumPicker(int maxHourValue, int maxMinValue) {// 注意设置顺序
        // TODO Auto-generated method stub
        hour.setMinValue(0);
        hour.setMaxValue(maxHourValue);
        hour.setValue(mHour);
        min.setMinValue(0);
        min.setMaxValue(maxMinValue);
        min.setValue(mMin);
    }

    /**
     * mode切换
     */
    private void changeMode() {
        // TODO Auto-generated method stub
        currentMode++;
        if (currentMode > 2) {
            currentMode = 0;
        }
        txLarge.setText(txLargeString[currentMode]);
        txSmall.setText(txSmallString[currentMode]);
    }

    /**
     * 发送广播更新UI时间显示
     */
    private void startServiceToDao() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(StartActivity.this, MainService.class);
        intent.putExtra("time_count", new int[]{mHour, mMin});
        intent.putExtra("mode", currentMode);
        System.out.println("StartActivity---->timeCount" + mHour + ":" + mMin);
        startService(intent);
    }

    /**
     * NumberPicker监听器
     */
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        // TODO Auto-generated method stub
        switch (picker.getId()) {
            case R.id.hour:
                mHour = newVal;
                break;
            case R.id.min:
                mMin = newVal;
                break;
        }
    }


}
