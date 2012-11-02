package com.ewhapp.money;

import java.util.ArrayList;

import com.ewhapp.money.UserData.SaveObjectInfo;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class WidgetDialogActivity extends Activity {
	private UserData userData;
	private SimpleAppWidget_coin simpleCoin;
	private ArrayList<SaveObjectInfo> objList;

	private long addmoney = 0L;
	private int rate = 0;
	private boolean use = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_dialog);

		userData = UserData.sharedUserData(this);
		objList = userData.getSaveObjList();
		rate = userData.getAchieveRate();
		
		Button finBtn = (Button) findViewById(R.id.dial_finbtn);
		
		ImageView sel0 = (ImageView) findViewById(R.id.dial_coffee);
		ImageView sel3 = (ImageView) findViewById(R.id.dial_cigarette);
		ImageView sel2 = (ImageView) findViewById(R.id.dial_alcohol);
		ImageView sel1 = (ImageView) findViewById(R.id.dial_food);
		ImageView sel4 = (ImageView) findViewById(R.id.dial_taxi);

		for(int i=0; i<objList.size(); i++){
			if(objList.get(i).isUse() == false){
				switch(i){
				case 0:
					sel0.setVisibility(View.GONE);
					break;
				case 1:
					sel1.setVisibility(View.GONE);
					break;
				case 2:
					sel2.setVisibility(View.GONE);
					break;
				case 3:
					sel3.setVisibility(View.GONE);
					break;
				case 4:
					sel4.setVisibility(View.GONE);
					break;
				}
			}			
		}
		
		if(rate >= 100){
			startActivity(new Intent(WidgetDialogActivity.this, RewardDialogActivity.class));
			WidgetDialogActivity.this.finish();
		}
		
		sel0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addmoney = objList.get(0).getMoney();
				//addmoney = userData.PRICE_SAVEOBJS[0];
				String str = "" + addmoney;
				objList.get(0).addSave();
				rate = userData.getAchieveRate();
				Toast.makeText(WidgetDialogActivity.this, str + "¿øÀ» ¾Æ³¢¼Ì½À´Ï´Ù.",
						Toast.LENGTH_SHORT).show();
				userData.saveData(WidgetDialogActivity.this);
				Intent intent = new Intent();
				intent.setAction("com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST");
				sendBroadcast(intent);
				if(rate >= 100){
					startActivity(new Intent(WidgetDialogActivity.this, RewardDialogActivity.class));
				}
				finish();
			}
		});

		sel3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addmoney = objList.get(3).getMoney();
				String str = "" + addmoney;
				objList.get(3).addSave();
				Toast.makeText(WidgetDialogActivity.this, str + "¿øÀ» ¾Æ³¢¼Ì½À´Ï´Ù.",
						Toast.LENGTH_SHORT).show();
				userData.saveData(WidgetDialogActivity.this);
				Intent intent = new Intent();
				intent.setAction("com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST");
				sendBroadcast(intent);
				if(rate >= 100){
					startActivity(new Intent(WidgetDialogActivity.this, RewardDialogActivity.class));
				}
				finish();
			}
		});

		sel2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addmoney = objList.get(2).getMoney();
				String str = "" + addmoney;
				objList.get(2).addSave();
				Toast.makeText(WidgetDialogActivity.this, str + "¿øÀ» ¾Æ³¢¼Ì½À´Ï´Ù.",
						Toast.LENGTH_SHORT).show();
				userData.saveData(WidgetDialogActivity.this);
				Intent intent = new Intent();
				intent.setAction("com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST");
				sendBroadcast(intent);
				if(rate >= 100){
					startActivity(new Intent(WidgetDialogActivity.this, RewardDialogActivity.class));
				}
				finish();
			}
		});

		sel1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addmoney = objList.get(1).getMoney();
				String str = "" + addmoney;
				objList.get(1).addSave();
				Toast.makeText(WidgetDialogActivity.this, str + "¿øÀ» ¾Æ³¢¼Ì½À´Ï´Ù.",
						Toast.LENGTH_SHORT).show();
				userData.saveData(WidgetDialogActivity.this);
				Intent intent = new Intent();
				intent.setAction("com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST");
				sendBroadcast(intent);
				if(rate >= 100){
					startActivity(new Intent(WidgetDialogActivity.this, RewardDialogActivity.class));
				}
				finish();
			}
		});

		sel4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addmoney = objList.get(4).getMoney();
				String str = "" + addmoney;
				objList.get(4).addSave();
				Toast.makeText(WidgetDialogActivity.this, str + "¿øÀ» ¾Æ³¢¼Ì½À´Ï´Ù.",
						Toast.LENGTH_SHORT).show();
				userData.saveData(WidgetDialogActivity.this);
				if(rate >= 100){
					startActivity(new Intent(WidgetDialogActivity.this, RewardDialogActivity.class));
				}
				finish();
			}
		});

		finBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(WidgetDialogActivity.this,
						SplashActivity.class));
				WidgetDialogActivity.this.finish();
			}
		});
	}

}
