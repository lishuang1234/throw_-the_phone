package cn.ls.view;

import cn.ls.activity.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class ProgressBarView extends View {
	private Paint paint;
	private RectF rectf;
	private int max;
	private int progress;
	private float circleX;
	private float circleY;

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;

	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		invalidate();
	}

	public ProgressBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		paint = new Paint();
		rectf = new RectF();

	}

	public void setCircleRadius(float x, float y) {
		this.circleX = x;
		this.circleY = y;
		invalidate();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		paint.setAntiAlias(true);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setAlpha(0);
		paint.setStrokeWidth(16);
		paint.setStyle(Paint.Style.STROKE);// 中空
		paint.setColor(getResources().getColor(R.color.forbidAc_view_circle));
		canvas.drawCircle(circleX, circleY, circleX - circleX * 2 / 8, paint);

		paint.reset();
		paint.setAntiAlias(true);
		// paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setAlpha(0);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(getResources().getColor(R.color.green_deep));// 设置画笔色
		paint.setStrokeWidth(7);
		// rectf.set(60, 400-180, 420, 400+180);//
		// 设置类似于左上角坐标（45,45），右下角坐标（155,155），这样也就保证了半径为55
		rectf.set(circleX * 2 / 8, circleY - circleX + circleX * 2 / 8, circleX
				+ circleX - circleX * 2 / 8, circleY + circleX - circleX * 2
				/ 8);// 设置类似于左上角坐标（45,45），右下角坐标（155,155），这样也就保证了半径为55
		// canvas.drawArc(rectf, 0, (360 / (float) max) * progress, false,
		// paint);// 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
		canvas.drawArc(rectf, -90, (360 / (float) max) * progress, false, paint);
		// canvas.drawArc(rectf, -90, 270, false, paint);
		//
		paint.reset();// 将画笔重置
		paint.setStrokeWidth(5);// 再次设置画笔的宽度
		paint.setTextSize(35);// 设置文字的大小
		paint.setColor(Color.GREEN);// 设置画笔颜色
		if (progress == max) {
			// canvas.drawText("完成", 165, 200, paint);
			canvas.drawText("完成", circleX / 6 * 5, circleY * 11 / 12, paint);
		} else {
			canvas.drawText((int) ((float) progress / max * 100) + "%",
					circleX / 6 * 5, circleY * 11 / 12, paint);
		}
	}

}
