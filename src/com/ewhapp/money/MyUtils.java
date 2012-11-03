package com.ewhapp.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

public class MyUtils {
	public static int dpToPixel(float dp, float densityDpi) {
		int pixel = (int)(dp * (densityDpi / DisplayMetrics.DENSITY_DEFAULT));
		return pixel;
	}
	
	public static void createGlobalOptionMenu(Menu menu, Activity nowActivity) {
		if( UserData.sharedUserData(nowActivity).isLock() )
			menu.add(0,0,0,"암호 잠금 해제");
		else
			menu.add(0,0,0,"암호 잠금 설정");
		menu.add(0,1,0,"버전 정보");
		menu.add(0,2,0,"이화앱센터 소개");
	}
	
	public static void setGlobalMenuOpened(int featureId, Menu menu, Activity nowActivity) {
		if( UserData.sharedUserData(nowActivity).isLock() )
			menu.getItem(0).setTitle("암호 잠금 해제");
		else
			menu.getItem(0).setTitle("암호 잠금 설정");
	}
	
	public static boolean setGlobalOptionsItemSeleted(MenuItem item, Activity nowActivity) {
		switch(item.getItemId()){
		case 0:
			Intent intent = new Intent(nowActivity, PassWordActivity.class);
			if( UserData.sharedUserData(nowActivity).isLock() )
				intent.putExtra("TYPE", PassWordActivity.TYPE_UNLOCK);
			else
				intent.putExtra("TYPE", PassWordActivity.TYPE_LOCK);
			nowActivity.startActivity(intent);
			return true;
		case 1:
			new AlertDialog.Builder(nowActivity)
			.setTitle("버전 정보")
			.setMessage("현재 버전 : v1.0.0")
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
			return true;
		case 2:
			nowActivity.startActivity(new Intent(nowActivity, EwhaInfoActivity.class));
			return true;
		}
		return false;
	}
}
