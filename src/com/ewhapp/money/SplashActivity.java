package com.ewhapp.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SplashActivity extends Activity {
	private static int FADE_TIME = 500;
	private ImageView logo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		UserData.bFirstResumeApp = true;

		logo = (ImageView)findViewById(R.id.splashLogo);
		
		
		final AlphaAnimation fadeIn = new AlphaAnimation(0.f, 1.f);
		fadeIn.setDuration(FADE_TIME);
		fadeIn.setFillBefore(true);
		fadeIn.setStartOffset(FADE_TIME);
		
		final AlphaAnimation fadeOut = new AlphaAnimation(1.f, 0.f);
		fadeOut.setDuration(FADE_TIME);
		fadeOut.setFillAfter(true);

		final Handler startActivityHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(UserData.hasSavedData(SplashActivity.this)) {
					startActivity(new Intent(SplashActivity.this, ConfirmGoalActivity.class));
				} else {
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
				}
				SplashActivity.this.finish();
			}
		};
		
		final Handler startFadeOutHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				logo.startAnimation(fadeOut);
				startActivityHandler.sendEmptyMessageDelayed(0, FADE_TIME);
			}
		};
		
		
		logo.setVisibility(View.VISIBLE);
		logo.startAnimation(fadeIn);
		startFadeOutHandler.sendEmptyMessageDelayed(0, FADE_TIME + 2500);
	}
	
	@Override
	public void onBackPressed() {
		return;
	}
}