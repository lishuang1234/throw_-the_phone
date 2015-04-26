package cn.ls.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import cn.ls.activity.R;
import cn.ls.util.BaseTool;

public class StartButton extends View implements OnClickListener {
	/**
	 * 自定义View开始显示大按钮
	 */
	private Paint mPaint;
	private Bitmap mBit1, mBit2;
	private int[] drawable_number;
	private int[] drawable_bg;
	private int hours, min_la, min_sm;
    private Rect bitR;
    private RectF bitOnCanvasRf;
    private RectF numRf;
    private RectF numOnCanvasRf;
    private  int widthPX;
    private int heightPx;


public void  setActivity(Activity mActivity){
    BaseTool baseTool = new BaseTool(mActivity);
//    widthPX = baseTool.getWidthPx();
//    heightPx = baseTool.getHeightPx();

}
	public StartButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		hours = 1;
		min_la = 3;
		min_sm = 0;
		setOnClickListener(this);
		drawable_number = new int[] { R.drawable.digital_number_0,
				R.drawable.digital_number_1, R.drawable.digital_number_2,
				R.drawable.digital_number_3, R.drawable.digital_number_4,
				R.drawable.digital_number_5, R.drawable.digital_number_6,
				R.drawable.digital_number_7, R.drawable.digital_number_8,
				R.drawable.digital_number_9, R.drawable.digital_number_colon };
		drawable_bg = new int[] { R.drawable.ic_circle_large_yellow,
				R.drawable.ic_circle_large_blue, R.drawable.ic_circle_large_red };
		mBit1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_circle_large_yellow);
        int mBit1Width,mBit1Height;
        mBit1Width = mBit1.getWidth();
        mBit1Height = mBit1.getHeight();
        bitR = new Rect(0,0,mBit1Width,mBit1Height);
        System.out.println("width"+widthPX);
        widthPX = this.getWidth();
        heightPx = this.getHeight();
        bitOnCanvasRf = new RectF(13*widthPX/480,30*heightPx/800,460*widthPX/480,500*heightPx/800 );

		// IntentFilter filter =new IntentFilter();
		// filter.addAction(ComName.CHANG_VIEW_TIME_ACTION);
		// context.registerReceiver(new ChangT(), filter);


    }

	public void setBg(int currentMode) {

		System.out.println(currentMode);
		mBit1 = BitmapFactory.decodeResource(getResources(),
				drawable_bg[currentMode]);//初始化背景图片
		invalidate();
	}

	public void setViewTime(int hours, int min_la, int min_sm) {
		this.hours = hours;
		this.min_la = min_la;
		this.min_sm = min_sm;
		invalidate();
	}

	/**
	 * 绘图函数
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		//canvas.drawBitmap(mBit1, 13 , 30 , mPaint);
        canvas.drawBitmap(mBit1,bitR,bitOnCanvasRf,mPaint);
		creatBitmap(hours, 10, min_la, min_sm, canvas);
	}

	/**
	 * 画出时间图像
	 * 
	 * @param hours
	 * @param colon
	 * @param min_la
	 * @param min_sm
	 * @param canvas
	 */
	private void creatBitmap(int hours, int colon, int min_la, int min_sm,
			Canvas canvas) {
		// TODO Auto-generated method stub
		int[] bits = new int[] { hours, colon, min_la, min_sm };
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 11; j++) {
				if (j == bits[i]) {
					mBit2 = BitmapFactory.decodeResource(getResources(),
							drawable_number[j]);
					canvas.drawBitmap(mBit2, (135 + i * 50), 210, mPaint);
				}
			}
		}
	}

	/**
	 * 监听器
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 *           stub
	 * 
	 * 
	 *           }
	 */
	/**
	 * class ChangT extends BroadcastReceiver{
	 * 
	 * @Override public void onReceive(Context arg0, Intent intent) { // TODO
	 *           Auto-generated method stub String action = intent.getAction();
	 *           if(action.equals(ComName.CHANG_VIEW_TIME_ACTION)){ int[]
	 *           timeNow=intent.getIntArrayExtra("time"); hours = timeNow[0];
	 *           min_la = timeNow[1]/10; min_sm=timeNow[1]%10;
	 *           System.out.println("StartButton"+ hours); invalidate(); } }
	 * 
	 *           }
	 **/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
