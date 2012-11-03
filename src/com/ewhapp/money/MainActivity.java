package com.ewhapp.money;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

class ListItem {
	ListItem(int aType, String aText, int aIconRes) {
		Type = aType;
		Text = aText;
		IconRes = aIconRes;
	}

	int Type;
	String Text;
	int IconRes;
}

class MultiAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	ArrayList<ListItem> arSrc;

	public MultiAdapter(Context context, ArrayList<ListItem> arItem) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arSrc = arItem;
	}

	public int getCount() {
		return arSrc.size();
	}

	public ListItem getItem(int position) {
		return arSrc.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return arSrc.get(position).Type;
	}

	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			int res = 0;
			switch (arSrc.get(position).Type) {
			case 0:
				res = R.layout.main_saveobj_view;
				break;
			}
			convertView = mInflater.inflate(res, parent, false);
		}
		switch (arSrc.get(position).Type) {
		case 0:
			ImageView listImg = (ImageView) convertView
					.findViewById(R.id.savelist_img);
			listImg.setImageResource(arSrc.get(position).IconRes);
			TextView listTt = (TextView) convertView.findViewById(R.id.savelist_edt_money);
			listTt.setText(arSrc.get(position).Text);
			break;
		}
		return convertView;
	}
}

public class MainActivity extends SaveMoneyBaseActivity {
	private ImageView btn_AddGoal;
	private ImageView imgv_goalImg;
	private TextView txtv_goalMoney;
	private TextView txtv_goalRate;
	private ArrayList<ListItem> arItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btn_AddGoal = (ImageView) findViewById(R.id.main_btn_addGoal);

		btn_AddGoal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						SetGoalActivity.class));
				MainActivity.this.finish();
			}
		});

		arItem = new ArrayList<ListItem>();
		arItem.add(new ListItem(0, "3000", R.drawable.btn_alcohol_on));
		arItem.add(new ListItem(0, "4000", R.drawable.btn_coffee_on));
		arItem.add(new ListItem(0, "4500", R.drawable.btn_food_on));
		arItem.add(new ListItem(0, "7000", R.drawable.btn_cosmetic_on));
		arItem.add(new ListItem(0, "10000", R.drawable.btn_movie_on));
		
		MultiAdapter MyAdapter = new MultiAdapter(MainActivity.this, arItem);
		ListView MyList;
		MyList = (ListView) findViewById(R.id.confirmgoal_list_saveobj);
		MyList.setAdapter(MyAdapter);
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setMessage("정말 종료하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
/*						Intent intent = new Intent();
						intent.setAction("com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST");
						sendBroadcast(intent);*/
						dialog.dismiss();
						MainActivity.this.finish();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
}
