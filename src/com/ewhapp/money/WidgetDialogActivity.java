package com.ewhapp.money;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userData = UserData.sharedUserData(this);
		usingList = userData.getUsingSaveObjList();
		rate = userData.getAchieveRate();
		size = userData.getUsingSaveObjSize();
		
		Button finBtn = new Button(WidgetDialogActivity.this);
		finBtn.setText("¾Û ½ÇÇà");
		
		ImageView[] imgvs = new ImageView[size];
		for(int i=0; i<size; i++) {
			imgvs[i] = new ImageView(this);
			imgvs[i].setImageResource(UserData.RESOURCES_SAVEOBJ_ON[usingList.get(i).whatObj()]);
		}
		
		int clr = 0xFFFFFFFF;
		LinearLayout mLinearRoot = new LinearLayout(WidgetDialogActivity.this);
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
		DisplayMetrics outMatrix = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMatrix);
		iconparam.width=MyUtils.dpToPixel(50, outMatrix.densityDpi);
		iconparam.height=MyUtils.dpToPixel(50, outMatrix.densityDpi);
		iconparam.setMargins(10, 10, 10, 10);
		
		mLinearRoot.addView(mLinear1, param1);
		mLinearRoot.addView(mLinear2, param2);
		mLinearRoot.addView(mLinear3);
		
		if(size<5) {
			mLinear2.setVisibility(View.GONE);
			for(int i=0; i<size; i++) {
				mLinear1.addView(imgvs[i], iconparam);
			}
		} else {
			int half = (size+1)/2;
			for (int i = 0; i < size; i++) {
				if(i<half) {
					mLinear1.addView(imgvs[i], iconparam);
				} else {
					mLinear2.addView(imgvs[i], iconparam);
				}
			}
		}

		mLinear3.addView(finBtn, param1);


		
		if (rate >= 100) {
			startActivity(new Intent(WidgetDialogActivity.this,
					RewardDialogActivity.class));
			WidgetDialogActivity.this.finish();
		}
		
		for(int j=0; j<size; j++) {
			final int i = j;
			ImageView img = imgvs[i];
			img.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					addmoney = usingList.get(i).getMoney();
					// addmoney = userData.PRICE_SAVEOBJS[0];
					String str = "" + addmoney;
					usingList.get(i).addSave();
					rate = userData.getAchieveRate();
					Toast.makeText(WidgetDialogActivity.this, str + "¿øÀ» ¾Æ³¢¼Ì½À´Ï´Ù.",
							Toast.LENGTH_SHORT).show();
					userData.saveData(WidgetDialogActivity.this);
					Intent intent = new Intent();
					intent.setAction("com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST");
					sendBroadcast(intent);
					if (rate >= 100) {
						startActivity(new Intent(WidgetDialogActivity.this,
								RewardDialogActivity.class));
					}
					finish();
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
