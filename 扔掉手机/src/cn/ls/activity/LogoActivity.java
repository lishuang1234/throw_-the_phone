package cn.ls.activity;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class LogoActivity extends Activity {
	private String ac;
	private View logo_rl;
	private ImageView logoImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo_activity);
		int[] img = { R.drawable.logo_img_1, R.drawable.logo_img_2,
				R.drawable.logo_img_3, R.drawable.logo_img_4,
				R.drawable.logo_img_5, };
		logo_rl = (RelativeLayout) findViewById(R.layout.logo_activity);
		logoImageView = (ImageView) findViewById(R.id.logoAc_img);
		// for (int i = 0; i < 5; i++) {
		logoImageView.setImageResource(img[new Random().nextInt(5)]);
		// }
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.3f);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				ac = getIntent().getStringExtra("activity");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// handler.sendEmptyMessageDelayed(1, 2000);
				if (ac != null) {// 跳转到MyInfor
					intent.setClass(LogoActivity.this, Tab.class);
					intent.putExtra("activity", ac);
				} else {
					intent.setClass(LogoActivity.this, LoginActivity.class);
				}
				startActivity(intent);
				LogoActivity.this.finish();

			}
		});
		logoImageView.startAnimation(alphaAnimation);

	}
}
