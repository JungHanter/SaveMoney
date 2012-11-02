package com.ewhapp.money;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ewhapp.money.UserData.SaveObjectInfo;

public class RewardActivity extends Activity{
	private UserData userData;
	private ArrayList<SaveObjectInfo> objList;
	private String name = "";
	private String blank = "";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward);
        
        userData = UserData.sharedUserData(this);
		objList = userData.getSaveObjList();
		name = userData.getGoalName();
		
		TextView text = (TextView)findViewById(R.id.rewardtext2);
		text.setText(name+"을(를) 허가합니다.");
		
    	Button newBtn = (Button)findViewById(R.id.reward_newbtn);
    	newBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userData.resetUserData(RewardActivity.this);
				userData.saveData(RewardActivity.this);
				startActivity(new Intent(RewardActivity.this, SetGoalActivity.class));
				RewardActivity.this.finish();
			}
		});
	}
}