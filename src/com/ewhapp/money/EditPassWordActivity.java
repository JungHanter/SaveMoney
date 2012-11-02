package com.ewhapp.money;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

//이건 설정에서 들어갈때 경유하는 
public class EditPassWordActivity extends Activity {
	private int cnt = 0;
	private ImageView[] btn = new ImageView[10];
	private ImageView[] imgArray = new ImageView[4];

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_layout);

		for (int i = 0; i < 10; i++) {
			btn[i] = (ImageView) findViewById(R.id.passbtn1 + i);
		}
		
		final ImageView btndel = (ImageView) findViewById(R.id.passbtndel);

		for (int i = 0; i < 4; i++) {
			imgArray[i] = (ImageView) findViewById(R.id.passbox1 + i);
		}

		for (int i = 0; i < 10; i++) {
			final int j = i;
			btn[i].setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == MotionEvent.ACTION_DOWN)
						btn[j].setImageResource(R.drawable.ppass_1 + j);
					if(event.getAction() == MotionEvent.ACTION_UP)
						btn[j].setImageResource(R.drawable.pass_1 + j);
					return false;
				}
			});
			
			btn[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (cnt < 4) {
						switch (cnt) {
						case 0:
							imgArray[0]
									.setImageResource(R.drawable.checkpasswordbox);
							break;
						case 1:
							imgArray[1]
									.setImageResource(R.drawable.checkpasswordbox);
							break;
						case 2:
							imgArray[2]
									.setImageResource(R.drawable.checkpasswordbox);
							break;
						case 3:
							imgArray[3]
									.setImageResource(R.drawable.checkpasswordbox);
							/*
							 * 여기서 유저 데이터랑 비교 cnt = 0; for (int i = 0; i < 4;
							 * i++) { imgArray[i]
							 * .setImageResource(R.drawable.passwordbox); }
							 * Toast.makeText(MainActivity.this, "잘못 입력",
							 * Toast.LENGTH_SHORT) .show();
							 */
							break;
						}
						cnt++;
					}
				}
			});
		}
		btndel.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					btndel.setImageResource(R.drawable.delete_);
				if(event.getAction() == MotionEvent.ACTION_UP)
					btndel.setImageResource(R.drawable.delete);
				return false;
			}
		});
		btndel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				cnt--;
				if (cnt < 0) {
					cnt = 0;
				} else {
					imgArray[cnt].setImageResource(R.drawable.passwordbox);
				}
			}
		});
	}

}
