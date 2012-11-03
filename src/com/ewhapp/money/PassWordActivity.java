package com.ewhapp.money;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PassWordActivity extends Activity {
	public static final int TYPE_ENTER = 0;
	public static final int TYPE_UNLOCK = 1;
	public static final int TYPE_LOCK = 2;
	public static final int TYPE_LOCK2 = 3;
	
	public static final int REUSLT_CODE = 1001;
	
	// 맨처음 실행 시킬때
	private int passwordType = TYPE_ENTER;
	private int cnt = 0;
	private UserData userData = null;
	private TextView txtTitle = null, txtContent = null;
	private ImageView[] btn = new ImageView[10];
	private ImageView[] imgArray = new ImageView[4];
	private int eachPass[] = new int[4];
	private int confirmPass[] = new int[4];
	private int password = -1;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_layout);
		userData = UserData.sharedUserData(this);
		
		txtTitle = (TextView)findViewById(R.id.pass_title);
		txtContent = (TextView)findViewById(R.id.pass_content);
		btn[0] = (ImageView) findViewById(R.id.passbtn0);
		for (int i = 1; i < 10; i++) {
			btn[i] = (ImageView) findViewById(R.id.passbtn1 + (i-1));
		}
		final ImageView btndel = (ImageView) findViewById(R.id.passbtndel);
		for (int i = 0; i < 4; i++) {
			imgArray[i] = (ImageView) findViewById(R.id.passbox1 + i);
		}
		
		passwordType = getIntent().getIntExtra("TYPE", TYPE_ENTER);
		switch(passwordType) {
		case TYPE_ENTER:
			txtTitle.setText("암호 잠금");
			break;
		case TYPE_LOCK:
			txtTitle.setText("잠금 설정");
			break;
		case TYPE_UNLOCK:
			txtTitle.setText("잠금 해제");
			break;
		}

		for (int i = 0; i < 10; i++) {
			final int j = i;
			btn[i].setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					int action = event.getAction();
					Rect rect = new Rect();
					btn[j].getGlobalVisibleRect(rect);
					if (action == MotionEvent.ACTION_DOWN)
						btn[j].setImageResource(R.drawable.ppass_0 + j);
					else if (action == MotionEvent.ACTION_MOVE)
						if(rect.contains((int)event.getX(), (int)event.getY()))
							btn[j].setImageResource(R.drawable.ppass_0 + j);
						else
							btn[j].setImageResource(R.drawable.pass_0 + j);
					else if (action == MotionEvent.ACTION_UP ||
							action == MotionEvent.ACTION_CANCEL ||
							action == MotionEvent.ACTION_OUTSIDE)
						btn[j].setImageResource(R.drawable.pass_0 + j);
					return false;
				}
			});

			btn[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					imgArray[cnt].setImageResource(R.drawable.checkpasswordbox);
					if(passwordType == TYPE_LOCK2) {
						confirmPass[cnt] = j;
					} else {
						eachPass[cnt] = j;
					}
					
					if (cnt == 3) {
						if(passwordType != TYPE_LOCK2)
							password = UserData.convertIntegerPassword(
									eachPass[0],eachPass[1],eachPass[2],eachPass[3]);
						
						switch(passwordType) {
						case TYPE_ENTER:
							if(userData.confirmPassword(password)) {
								setResult(RESULT_OK);
								finish();
							} else {
								Toast.makeText(PassWordActivity.this,
										"암호가 틀렸습니다.\n다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
								resetActivity(TYPE_ENTER);
							}
							break;
						case TYPE_UNLOCK:
							if(userData.confirmPassword(password)) {
								userData.unLock();
								userData.savePasswordData(PassWordActivity.this);
								finish();
								Toast.makeText(PassWordActivity.this,
										"잠금 해제 완료", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(PassWordActivity.this,
										"암호가 틀렸습니다.\n다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
								resetActivity(TYPE_UNLOCK);
							}
							break;
						case TYPE_LOCK:
							resetActivity(TYPE_LOCK2);
							break;
						case TYPE_LOCK2:
							int password2 = UserData.convertIntegerPassword(
									confirmPass[0],confirmPass[1],confirmPass[2],confirmPass[3]);
							if(password == password2) {
								userData.lock(password);
								userData.savePasswordData(PassWordActivity.this);
								finish();
								Toast.makeText(PassWordActivity.this,
										"잠금 설정 완료", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(PassWordActivity.this,
										"두 암호가 다릅니다.\n다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
								resetActivity(TYPE_LOCK);
							}
							break;
						}
						
						return;
					}
					
					cnt++;
				}
			});
		}
		btndel.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				Rect rect = new Rect();
				btndel.getGlobalVisibleRect(rect);
				if (action == MotionEvent.ACTION_DOWN)
					btndel.setImageResource(R.drawable.delete_);
				else if (action == MotionEvent.ACTION_MOVE)
					if(rect.contains((int)event.getX(), (int)event.getY()))
						btndel.setImageResource(R.drawable.delete_);
					else
						btndel.setImageResource(R.drawable.delete);
				else if (action == MotionEvent.ACTION_UP ||
						action == MotionEvent.ACTION_CANCEL ||
						action == MotionEvent.ACTION_OUTSIDE)
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
	
	@Override
	public void onBackPressed() {
		if(cnt==0) {
			if(passwordType == TYPE_ENTER) {
				setResult(RESULT_CANCELED);
			} else {
				setResult(RESULT_OK);
			}
			finish();
		} else {
			cnt--;
			if (cnt < 0) {
				cnt = 0;
			} else {
				imgArray[cnt].setImageResource(R.drawable.passwordbox);
			}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		UserData.bFirstResumeApp = false;
	}

	private void resetActivity(int type) {
		cnt = 0;
		for(ImageView passBox : imgArray) {
			passBox.setImageResource(R.drawable.passwordbox);
		}
		passwordType = type;
		txtContent.setText("암호를 입력해 주세요.");
		switch(passwordType) {
		case TYPE_ENTER:
			txtTitle.setText("암호 잠금");
			break;
		case TYPE_LOCK:
			txtTitle.setText("잠금 설정");
			break;
		case TYPE_UNLOCK:
			txtTitle.setText("잠금 해제");
			break;
		case TYPE_LOCK2:
			txtTitle.setText("잠금 확인");
			txtContent.setText("한번 더 입력해 주세요.");
		}
	}
}
