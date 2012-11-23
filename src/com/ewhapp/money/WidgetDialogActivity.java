package com.ewhapp.money;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ewhapp.money.UserData.SaveObjectInfo;

public class WidgetDialogActivity extends Activity {
	private UserData userData;
	private ArrayList<SaveObjectInfo> usingList;

	private long addmoney = 0L;
	private int rate = 0;
	private int size = -1;
	private int clr = 0xFFFFFFFF;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//액티비티를 종료시키지 않고 홈으로 나왔을때 위젯을 누르면
		//액티비티가 같이 뜸. 그래서 액티비티 강종하기 위한 과정
		ConfirmGoalActivity mActivity = (ConfirmGoalActivity)ConfirmGoalActivity.mActivity;
		mActivity.finish();
		
		userData = UserData.sharedUserData(this);
		usingList = userData.getUsingSaveObjList();
		rate = userData.getAchieveRate();
		size = userData.getUsingSaveObjSize();

		Button finBtn = new Button(WidgetDialogActivity.this);
		finBtn.setText("앱 실행");

		ImageView[] imgvs = new ImageView[size];
		for (int i = 0; i < size; i++) {
			imgvs[i] = new ImageView(this);
			imgvs[i].setImageResource(UserData.RESOURCES_SAVEOBJ_ON[usingList
					.get(i).whatObj()]);
		}

		final LinearLayout mLinearRoot = new LinearLayout(
				WidgetDialogActivity.this);
		mLinearRoot.setOrientation(LinearLayout.VERTICAL);
		mLinearRoot.setBackgroundColor(clr);
		LinearLayout mLinear1 = new LinearLayout(WidgetDialogActivity.this);
		mLinear1.setOrientation(LinearLayout.HORIZONTAL);
		mLinear1.setGravity(Gravity.CENTER);
		LinearLayout mLinear2 = new LinearLayout(WidgetDialogActivity.this);
		mLinear2.setOrientation(LinearLayout.HORIZONTAL);
		mLinear2.setGravity(Gravity.CENTER);
		LinearLayout mLinear3 = new LinearLayout(WidgetDialogActivity.this);
		mLinear3.setOrientation(LinearLayout.HORIZONTAL);
		mLinear3.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams paramBtn = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		param1.topMargin = 10;
		param1.bottomMargin = 10;
		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		param2.bottomMargin = 10;
		LinearLayout.LayoutParams iconparam = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		final DisplayMetrics outMatrix = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMatrix);
		iconparam.width = MyUtils.dpToPixel(50, outMatrix.densityDpi);
		iconparam.height = MyUtils.dpToPixel(50, outMatrix.densityDpi);
		iconparam.setMargins(10, 10, 10, 10);

		mLinearRoot.addView(mLinear1, param1);
		mLinearRoot.addView(mLinear2, param2);
		mLinearRoot.addView(mLinear3);

		if (size < 5) {
			mLinear2.setVisibility(View.GONE);
			for (int i = 0; i < size; i++) {
				mLinear1.addView(imgvs[i], iconparam);
			}
		} else {
			int half = (size + 1) / 2;
			for (int i = 0; i < size; i++) {
				if (i < half) {
					mLinear1.addView(imgvs[i], iconparam);
				} else {
					mLinear2.addView(imgvs[i], iconparam);
				}
			}
		}

		mLinear3.addView(finBtn, paramBtn);

		if (rate >= 100) {
			startActivity(new Intent(WidgetDialogActivity.this,
					RewardDialogActivity.class));
			WidgetDialogActivity.this.finish();
		}

		for (int j = 0; j < size; j++) {

			final int i = j;
			ImageView img = imgvs[i];
			img.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Button confirmBtn = new Button(WidgetDialogActivity.this);
					confirmBtn.setText("확인");

					addmoney = usingList.get(i).getMoney();

					final LinearLayout mAdjustRoot = new LinearLayout(
							WidgetDialogActivity.this);
					mAdjustRoot.setOrientation(LinearLayout.VERTICAL);
					mAdjustRoot.setBackgroundColor(clr);
					LinearLayout mAdjustLinear1 = new LinearLayout(
							WidgetDialogActivity.this);
					mAdjustLinear1.setGravity(Gravity.CENTER);
					mAdjustLinear1.setOrientation(LinearLayout.HORIZONTAL);
					LinearLayout mAdjustLinear2 = new LinearLayout(
							WidgetDialogActivity.this);
					mAdjustLinear2.setGravity(Gravity.CENTER);
					mAdjustLinear2.setOrientation(LinearLayout.HORIZONTAL);
					final LinearLayout.LayoutParams paramAdjust1 = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					final LinearLayout.LayoutParams paramAdjust2 = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);

					final LinearLayout.LayoutParams aiconparam = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					aiconparam.width = MyUtils.dpToPixel(50,
							outMatrix.densityDpi);
					aiconparam.height = MyUtils.dpToPixel(50,
							outMatrix.densityDpi);
					aiconparam.setMargins(10, 10, 10, 10);
					final ImageView leftBtn = new ImageView(
							WidgetDialogActivity.this);

					leftBtn.setImageResource(R.drawable.btn_down_on);
					final ImageView rightBtn = new ImageView(
							WidgetDialogActivity.this);
					rightBtn.setImageResource(R.drawable.btn_up_on);
					final EditText edtText = new EditText(
							WidgetDialogActivity.this);
					edtText.setInputType(InputType.TYPE_CLASS_NUMBER);
					edtText.setText("" + addmoney);

					leftBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							long temp = (long) Integer.parseInt(edtText
									.getText().toString());
							temp -= 100;
							edtText.setText("" + temp);
						}
					});
					rightBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							long temp = (long) Integer.parseInt(edtText
									.getText().toString());
							temp += 100;
							edtText.setText("" + temp);
						}
					});
					confirmBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							long temp = (long) Integer.parseInt(edtText
									.getText().toString());
							usingList.get(i).setMoney(temp);
							usingList.get(i).addSave();
							rate = userData.getAchieveRate();
							Toast.makeText(WidgetDialogActivity.this,
									temp + "원을 아끼셨습니다.", Toast.LENGTH_SHORT)
									.show();
							userData.saveData(WidgetDialogActivity.this);
							Intent intent = new Intent();
							intent.setAction("com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST");
							sendBroadcast(intent);
							if (rate >= 100) {
								startActivity(new Intent(
										WidgetDialogActivity.this,
										RewardDialogActivity.class));
							}
							finish();
						}
					});
					mAdjustRoot.addView(mAdjustLinear1);
					mAdjustRoot.addView(mAdjustLinear2);
					mAdjustLinear1.addView(leftBtn, aiconparam);
					mAdjustLinear1.addView(edtText);
					mAdjustLinear1.addView(rightBtn, aiconparam);
					mAdjustLinear2.addView(confirmBtn);
					mLinearRoot.removeAllViews();
					setContentView(mAdjustRoot);

					// TODO Auto-generated method stub
					// addmoney = usingList.get(i).getMoney();
					// String str = "" + addmoney;
					// usingList.get(i).addSave();
					// rate = userData.getAchieveRate();
					// Toast.makeText(WidgetDialogActivity.this,
					// str + "원을 아끼셨습니다.", Toast.LENGTH_SHORT).show();
					// userData.saveData(WidgetDialogActivity.this);
					// Intent intent = new Intent();
					// intent.setAction("com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST");
					// sendBroadcast(intent);
					// if (rate >= 100) {
					// startActivity(new Intent(WidgetDialogActivity.this,
					// RewardDialogActivity.class));
					// }
					// finish();

				}
			});
		}

		finBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(WidgetDialogActivity.this,
						SplashActivity.class));
				WidgetDialogActivity.this.finish();
			}
		});

		setContentView(mLinearRoot);
	}
}
