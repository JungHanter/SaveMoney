package com.ewhapp.money;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ewhapp.money.UserData.SaveObjectInfo;

public class SetSaveObjectActivity extends Activity {
	private UserData userData;
	private ArrayList<SaveObjectInfo> objList;
	
	private ImageView btn_next;
	private ImageView btn_prev;
	
	private ImageView[] saveobj_img;
	private ImageView[] saveobj_btn_up;
	private ImageView[] saveobj_btn_down;
	private EditText[] saveobj_price;
	private boolean[] saveobj_isselect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setobject_nolist);
		
		userData = UserData.sharedUserData(this);
		objList = userData.getSaveObjList();
		
		btn_next = (ImageView)findViewById(R.id.setobj_btn_next);
		btn_prev = (ImageView)findViewById(R.id.setobj_btn_prev);
	
		btn_prev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		btn_next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int checkCnt=0;
				for(int i=0; i<objList.size(); i++) {
					long money = 0L;
					final int idx = i;
					try {
						money = Long.parseLong(saveobj_price[i].getText().toString());
					} catch (Exception e) {
						new AlertDialog.Builder(SetSaveObjectActivity.this)
						.setMessage("금액을 올바르게 입력해 주세요.")
						.setPositiveButton("확인", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								saveobj_price[idx].requestFocus();
								saveobj_price[idx].requestFocusFromTouch();
							}
						}).show();
						return;
					}
					
					//objList.get(i).setMoney(money);
					
					if(objList.get(i).isUse()) checkCnt++;
				}
				
				if(checkCnt > 0) {
					new AlertDialog.Builder(SetSaveObjectActivity.this)
					.setMessage("이 목표로 설정하시겠습니까?")
					.setPositiveButton("네", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//저장
							userData.saveData(SetSaveObjectActivity.this);
							
							dialog.dismiss();
							Intent intent = new Intent(SetSaveObjectActivity.this, ConfirmGoalActivity.class);

							SetSaveObjectActivity.this.startActivity(intent);
							SetSaveObjectActivity.this.finish();
						}
					})
					.setNegativeButton("아니요",  new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
				} else {
					new AlertDialog.Builder(SetSaveObjectActivity.this)
					.setMessage("아낄 것은 적어도 한개 이상은 선택해야 합니다.")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
				}
			}
		});
		
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
	//메뉴 버튼
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0,1,0,"암호 잠금 설정");
		menu.add(0,2,0,"버전 정보");
		menu.add(0,3,0,"이화앱센터 소개");
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case 1:
			startActivity(new Intent(SetSaveObjectActivity.this, EditPassWordActivity.class));
			return true;
		case 2:
			new AlertDialog.Builder(this)
			.setMessage("현재 버전 v0.9")
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
			return true;
		case 3:
			startActivity(new Intent(SetSaveObjectActivity.this, EwhaInfoActivity.class));
			return true;
		}
		return false;
	}
	@Override
	public void onBackPressed() {
		startActivity(new Intent(SetSaveObjectActivity.this, SetGoalActivity.class));
		this.finish();
	}
	
	
	/*//리스트뷰 쓸때 이코드
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setobject);
		
		userData = UserData.sharedUserData();
		
		btn_next = (ImageView)findViewById(R.id.setobj_btn_next);
		btn_prev = (ImageView)findViewById(R.id.setobj_btn_prev);
		listView = (ListView)findViewById(R.id.setobj_listview);
		
		btn_prev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		btn_next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int checkCnt=0;
				for(SaveObjectInfo info : objList) {
					if(info.isUse()) checkCnt++;
				}
				
				if(checkCnt > 0) {
					//저장해야함
					Intent intent = new Intent(SetSaveObjectActivity.this, ConfirmGoalActivity.class);
					
					startActivity(intent);
					SetSaveObjectActivity.this.finish();
				} else {
					new AlertDialog.Builder(SetSaveObjectActivity.this)
					.setMessage("아낄 것은 적어도 한개 이상은 선택해야 합니다.")
					.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
				}
			}
		});
		
		objList = userData.getSaveObjList();
		
		SaveListAdapter adapter = new SaveListAdapter(this, R.layout.saveobj_view, objList);
		listView = (ListView)findViewById(R.id.setobj_listview);
		listView.setAdapter(adapter);
		
	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(SetSaveObjectActivity.this, SetGoalActivity.class));
		this.finish();
	}
	
	class SaveListAdapter extends BaseAdapter {
		Context mCtx;
		LayoutInflater inflater;
		ArrayList<SaveObjectInfo> list;
		int layout;
		
		public SaveListAdapter(Context ctx, int layout, ArrayList<SaveObjectInfo> saveList) {
			mCtx = ctx;
			this.layout = layout;
			list = saveList;
			inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return list.size();
		}
		
		@Override
		public SaveObjectInfo getItem(int position) {
			return list.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return list.get(position).whatObj();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final SaveObjectInfo nowObj = list.get(position);
			
			if(convertView == null) {
				convertView = inflater.inflate(layout, parent, false);
			}
			
			final ImageView img_Goal = (ImageView)convertView.findViewById(R.id.savelist_img);
			if(nowObj.isUse()) img_Goal.setImageResource(android.R.drawable.ic_menu_add);
			else img_Goal.setImageResource(android.R.drawable.ic_menu_gallery);
			//img_Goal.setImage~
			img_Goal.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if(nowObj.toggleUse()) {
						img_Goal.setImageResource(android.R.drawable.ic_menu_add);
					} else {
						img_Goal.setImageResource(android.R.drawable.ic_menu_gallery);
					}
				}
			});
			
			final EditText edt_Money = (EditText)convertView.findViewById(R.id.savelist_edt_money);
			edt_Money.setText("" + nowObj.getMoney());
			
			((Button)convertView.findViewById(R.id.savelist_btn_decrease)).setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					nowObj.changeMoney(-100);
					edt_Money.setText("" + nowObj.getMoney());
				}
			});
			((Button)convertView.findViewById(R.id.savelist_btn_increase)).setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					nowObj.changeMoney(100);
					edt_Money.setText("" + nowObj.getMoney());
				}
			});
			
			return convertView;
		}
	}
	*/
}
