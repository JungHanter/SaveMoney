package com.ewhapp.money;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class SaveMoneyBaseActivity extends Activity {
	private boolean bHide = false;
	private boolean bStartActivity = false;
	
	@Override
	protected void onResume() {
		super.onResume();
//		if (UserData.bFirstResumeApp && UserData.sharedUserData(this).isLock()) {
//			startActivityForResult(new Intent(this, PassWordActivity.class),
//					PassWordActivity.REUSLT_CODE);
//		}
		if(bStartActivity) {
			bStartActivity = false;
		} else {
			if (UserData.sharedUserData(this).isLock()) {
				if(getIntent().getBooleanExtra("PW", true)) {
					startActivityForResult(new Intent(this, PassWordActivity.class),
							PassWordActivity.REUSLT_CODE);
				} else {
					if(bHide) {
						startActivityForResult(new Intent(this, PassWordActivity.class),
								PassWordActivity.REUSLT_CODE);
					}
				}
			}
		}
		bHide = false;
	}
	
	@Override
	public void startActivity(Intent intent) {
		bStartActivity = true;
		intent.putExtra("PW", false);
		super.startActivity(intent);
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		bStartActivity = true;
		intent.putExtra("PW", false);
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onPause() {
		super.onPause();
		bHide = true;
//		UserData.bFirstResumeApp = true;
	}

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		UserData.bFirstResumeApp = false;
//	}
//	
//	@Override
//	public void finish() {
//		super.finish();
//		UserData.bFirstResumeApp = false;
//	}
//	
//	@Override
//	protected void finalize() throws Throwable {
//		super.finalize();
//		try {
//			UserData.bFirstResumeApp = false;
//		} catch(Exception e) {}
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PassWordActivity.REUSLT_CODE) {
			if (resultCode == RESULT_OK) {
//				UserData.bFirstResumeApp = false;
			} else if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}

	// 메뉴 버튼
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MyUtils.createGlobalOptionMenu(menu, this);
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		MyUtils.setGlobalMenuOpened(featureId, menu, this);
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MyUtils.setGlobalOptionsItemSeleted(item, this);
	}
}
