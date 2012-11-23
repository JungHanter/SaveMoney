package com.ewhapp.money;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ewhapp.money.UserData.SaveObjectInfo;

public class AdjustObjectDialogActivity extends SaveMoneyBaseActivity {
	private UserData userData;
	private ArrayList<SaveObjectInfo> objList;
	private ImageView[] saveobj_img;
	private ImageView[] saveobj_btn_up;
	private ImageView[] saveobj_btn_down;
	private EditText[] saveobj_price;
	private int size = -1;
	private boolean[] saveobj_isselect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setobject_adjust);
		
		userData = UserData.sharedUserData(this);
		objList = userData.getSaveObjList();
		size = userData.getUsingSaveObjSize();
		
		saveobj_img = new ImageView[UserData.NUM_SAVEOBJS];
		saveobj_btn_up = new ImageView[UserData.NUM_SAVEOBJS];
		saveobj_btn_down = new ImageView[UserData.NUM_SAVEOBJS];
		saveobj_price = new EditText[UserData.NUM_SAVEOBJS];
		saveobj_isselect = new boolean[UserData.NUM_SAVEOBJS];
		
		

		
		for(int i=0; i<UserData.NUM_SAVEOBJS; i++) {
			final UserData.SaveObjectInfo nowObj = objList.get(i);
			final int idx = i;
			

			saveobj_img[i] = (ImageView)findViewById(R.id.setobj_item_img_0 + i*4);
			saveobj_btn_up[i] = (ImageView)findViewById(R.id.setobj_btn_up_0 + i*4);
			saveobj_btn_down[i] = (ImageView)findViewById(R.id.setobj_btn_down_0 + i*4);
			saveobj_price[i] = (EditText)findViewById(R.id.setobj_edt_money_0 + i*4);
			saveobj_isselect[i] = false;
			
			saveobj_price[i].setText("" + nowObj.getMoney());
			saveobj_price[i].setEnabled(false);
			saveobj_price[i].setFilters(new InputFilter[] {
				new InputFilter.LengthFilter(8)	
			});
			if(objList.get(idx).isUse() == true){
				saveobj_isselect[idx] = true;
				saveobj_img[idx].setImageResource(UserData.RESOURCES_SAVEOBJ_ON[idx]);
				saveobj_btn_up[idx].setImageResource(R.drawable.btn_up_on);
				saveobj_btn_up[idx].setClickable(true);
				saveobj_btn_up[idx].setEnabled(true);
				saveobj_btn_down[idx].setImageResource(R.drawable.btn_down_on);
				saveobj_btn_down[idx].setClickable(true);
				saveobj_btn_down[idx].setEnabled(true);
				saveobj_price[idx].setEnabled(true);
				saveobj_price[idx].setClickable(true);
				saveobj_price[idx].setFocusable(true);
				saveobj_price[idx].setFocusableInTouchMode(true);
				saveobj_price[idx].setTextColor(0xFF000000);
				nowObj.use();
			}
			saveobj_img[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if(saveobj_isselect[idx]) { //on->off
						saveobj_isselect[idx] = false;
						saveobj_img[idx].setImageResource(UserData.RESOURCES_SAVEOBJ_OFF[idx]);
						saveobj_btn_up[idx].setImageResource(R.drawable.btn_up_off);
						saveobj_btn_up[idx].setClickable(false);
						saveobj_btn_up[idx].setEnabled(false);
						saveobj_btn_down[idx].setImageResource(R.drawable.btn_down_off);
						saveobj_btn_down[idx].setClickable(false);
						saveobj_btn_down[idx].setEnabled(false);
						saveobj_price[idx].setEnabled(false);
						saveobj_price[idx].setClickable(false);
						saveobj_price[idx].setFocusable(false);
						saveobj_price[idx].setFocusableInTouchMode(false);
						saveobj_price[idx].setTextColor(0xFFDFDFDF);
						nowObj.unuse();
					} else { //off->on
						saveobj_isselect[idx] = true;
						saveobj_img[idx].setImageResource(UserData.RESOURCES_SAVEOBJ_ON[idx]);
						saveobj_btn_up[idx].setImageResource(R.drawable.btn_up_on);
						saveobj_btn_up[idx].setClickable(true);
						saveobj_btn_up[idx].setEnabled(true);
						saveobj_btn_down[idx].setImageResource(R.drawable.btn_down_on);
						saveobj_btn_down[idx].setClickable(true);
						saveobj_btn_down[idx].setEnabled(true);
						saveobj_price[idx].setEnabled(true);
						saveobj_price[idx].setClickable(true);
						saveobj_price[idx].setFocusable(true);
						saveobj_price[idx].setFocusableInTouchMode(true);
						saveobj_price[idx].setTextColor(0xFF000000);
						nowObj.use();
					}
				}
			});
			
			saveobj_btn_up[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if(saveobj_isselect[idx]) {
						//int money = Integer.parseInt(saveobj_price[idx].getText().toString()) + 100;
						//saveobj_price[idx].setText("" + money);
						nowObj.changeMoney(100L);
						saveobj_price[idx].setText("" + nowObj.getMoney());
					}
				}
			});
			saveobj_btn_down[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if(saveobj_isselect[idx]) {
						//int money = Integer.parseInt(saveobj_price[idx].getText().toString()) - 100;
						//if(money<0) money=0;
						//saveobj_price[idx].setText("" + money);
						nowObj.changeMoney(-100L);
						saveobj_price[idx].setText("" + nowObj.getMoney());
					}
				}
			});
			saveobj_price[i].addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s.toString().equals("")) {
						nowObj.setMoney(0L);
						saveobj_price[idx].setText("0");
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				public void afterTextChanged(Editable s) {
					long money = 0L;
					try { money = Long.parseLong(s.toString()); }
					catch(Exception e){ money=0L; }
					nowObj.setMoney(money);
				}
			});
		}
	}
	public void onBackPressed() {
		userData.saveData(AdjustObjectDialogActivity.this);
		Intent intent = new Intent(AdjustObjectDialogActivity.this,
				ConfirmGoalActivity.class);
		startActivity(intent);
		this.finish();
	}
}
