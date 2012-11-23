package com.ewhapp.money;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewhapp.money.UserData.SaveObjectInfo;

public class RewardActivity extends SaveMoneyBaseActivity {
	private UserData userData;
	private ArrayList<SaveObjectInfo> objList;
	private String name = "";
	private String blank = "";
	private LinearLayout container;
	private Button captureButton;
	private Button newBtn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reward);

		userData = UserData.sharedUserData(this);
		objList = userData.getSaveObjList();
		name = userData.getGoalName();
		container = (LinearLayout) findViewById(R.id.rewardLayout);

		TextView text = (TextView) findViewById(R.id.rewardtext2);
		text.setText(name + "을(를) 허가합니다.");

		newBtn = (Button) findViewById(R.id.reward_newbtn);
		captureButton = (Button) findViewById(R.id.saveimg_btn);
		captureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout mLinearBottom = (LinearLayout) findViewById(R.id.reward_bottom);

				mLinearBottom.setVisibility(View.INVISIBLE);
				container.setDrawingCacheEnabled(true);
				container.buildDrawingCache();
				Bitmap captureView = container.getDrawingCache();
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(Environment
							.getExternalStorageDirectory().toString()
							+ "/PennyWise.PNG");
					captureView.compress(Bitmap.CompressFormat.PNG, 100, fos);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				mLinearBottom.setVisibility(View.VISIBLE);
				Intent intent = new Intent(RewardActivity.this,
						RewardFBloginActivity.class);
				startActivity(intent);
			}

		});

		newBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userData.resetUserData(RewardActivity.this);
				userData.saveData(RewardActivity.this);
				startActivity(new Intent(RewardActivity.this,
						SetGoalActivity.class));
				RewardActivity.this.finish();
			}
		});
	}
}