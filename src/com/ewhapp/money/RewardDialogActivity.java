package com.ewhapp.money;


import java.util.ArrayList;

import com.ewhapp.money.UserData.SaveObjectInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RewardDialogActivity extends Activity {
	
	private UserData userData;
	private ArrayList<SaveObjectInfo> objList;
	private String name = "";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_dialog);
        
        userData = UserData.sharedUserData(this);
		objList = userData.getSaveObjList();
		name = userData.getGoalName();
		
		TextView text = (TextView)findViewById(R.id.rewardtext2);
		text.setText(name+"을(를) 허가합니다.");
		
    	Button openBtn = (Button)findViewById(R.id.reward_openbtn);
    	openBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RewardDialogActivity.this, RewardActivity.class));
				RewardDialogActivity.this.finish();
			}
		});
    	
    	Button finBtn = (Button)findViewById(R.id.reward_finbtn);
    	finBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
}
