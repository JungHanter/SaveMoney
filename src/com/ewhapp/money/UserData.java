package com.ewhapp.money;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;

public class UserData {
	private static final String PREFER_NAME = "SaveMoneyData";
	public static final int SAVEOBJ_COFFEE = 0;
	public static final int SAVEOBJ_FOOD = 1;
	public static final int SAVEOBJ_ALCOHOL = 2;
	public static final int SAVEOBJ_CIGARETTE = 3;
	public static final int SAVEOBJ_TAXI = 4;
	public static final int NUM_SAVEOBJS = 5;
	public static final long[] PRICE_SAVEOBJS = { 4000L, 5000L, 3500L, 2500L, 5000L };
	public static final int[] RESOURCES_SAVEOBJ_OFF = {
			R.drawable.btn_coffee_off, R.drawable.btn_food_off,
			R.drawable.btn_alcohol_off, R.drawable.btn_cigarette_off,
			R.drawable.btn_taxi_off };
	public static final int[] RESOURCES_SAVEOBJ_ON = {
			R.drawable.btn_coffee_on, R.drawable.btn_food_on,
			R.drawable.btn_alcohol_on, R.drawable.btn_cigarette_on,
			R.drawable.btn_taxi_on };

	private String goalName = "";
	private long goalMoney = 0L;
	private ArrayList<SaveObjectInfo> saveObjList = null;
	private boolean bLock = false;
	private int password = -1;
	
	
	public void initSaveObjList() {
		if (saveObjList != null)
			saveObjList.clear();
		saveObjList = new ArrayList<SaveObjectInfo>();

		// test 생성
		for (int i = 0; i < NUM_SAVEOBJS; i++) {
			saveObjList.add(new SaveObjectInfo(i, PRICE_SAVEOBJS[i]));
		}
	}

	public ArrayList<SaveObjectInfo> getSaveObjList() {
		return saveObjList;
	}

	public void setSaveObjList(ArrayList<SaveObjectInfo> list) {
		saveObjList = list;
	}

	public String getGoalName() {
		return goalName;
	}

	public long getGoalMoney() {
		return goalMoney;
	}

	public long getNowMoney() {
		long money = 0L;
		for (SaveObjectInfo info : saveObjList) {
			if (info.isUse())
				money += info.getSaveMoney();
		}
		return money;
	}

	public int getAchieveRate() {
		return (int) (((double) getNowMoney() / (double) goalMoney) * 100.0);
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public void setGoalMoney(long money) {
		if (money < 0L)
			money = 0L;
		this.goalMoney = money;
	}
	
	public void setPassword(int password) {
		this.password = password;
	}
	
	public boolean confirmPassword(int password) {
		return (this.password == password);
	}
	
	public boolean isLock() { return bLock; }

	public void resetUserData(Context ctx) {
		goalName = "";
		goalMoney = 0L;
		initSaveObjList();
		removeImage(ctx);
	}

	public void removeImage(Context ctx) {
		try {
			ctx.deleteFile(SaveMoneyUtils.GOAL_IMG_NAME);
		} catch (Exception e) {
			;
		}
	}

	public boolean saveData(Context ctx) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(PREFER_NAME,
				ctx.MODE_WORLD_WRITEABLE).edit();

		editor.putString("GoalName", goalName);
		editor.putLong("GoalMoney", goalMoney);
		for (int i = 0; i < 5; i++) {
			SaveObjectInfo nowObj = saveObjList.get(i);
			editor.putBoolean(i + "_use", nowObj.isUse());
			editor.putLong(i + "_money", nowObj.getMoney());
			editor.putLong(i + "_save", nowObj.getSaveMoney());
		}
		editor.putInt("PW", password);
		editor.putBoolean("Lock", bLock);
		return editor.commit();
	}

	/*
	 * @Params if not have saveData, return false
	 */
	public boolean loadData(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(PREFER_NAME,
				ctx.MODE_WORLD_READABLE);

		String confirm = pref.getString("GoalName", "");
		if (confirm.equals(""))
			return false;

		Map<String, ?> map = pref.getAll();
		goalName = confirm;
		goalMoney = ((Long) map.get("GoalMoney")).longValue();

		try {
			password = ((Integer)map.get("PW")).intValue();
		} catch(NullPointerException e) {
			password = -1;
		}
		try {
			bLock = ((Boolean)map.get("Lock")).booleanValue();
		} catch(NullPointerException e) {
			bLock = false;
		}
		
		saveObjList = new ArrayList<UserData.SaveObjectInfo>();
		for (int i = 0; i < 5; i++) {
			SaveObjectInfo nowInfo = new SaveObjectInfo(i, ((Long) map.get(i
					+ "_money")).longValue());
			if (((Boolean) map.get(i + "_use")).booleanValue())
				nowInfo.use();
			nowInfo.setSaveMoney(((Long) map.get(i + "_save")).longValue());
			saveObjList.add(nowInfo);
		}
		return true;
	}

	static public boolean hasSavedData(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(PREFER_NAME,
				ctx.MODE_WORLD_READABLE);
		String confirm = pref.getString("GoalName", "");
		if (confirm.equals(""))
			return false;
		else
			return true;
	}
	
	public static int convertIntegerPassword(int _1, int _2, int _3, int _4) {
		return (_1*1000 + _2*100 + _3*10 + _4);
	}

	public SaveObjectInfo makeSaveObjectInfo(int what, long initMoney) {
		return new SaveObjectInfo(what, initMoney);
	}

	public class SaveObjectInfo {
		private final int what;
		private boolean bUsing;
		private long money;
		private long saveMoney;

		public SaveObjectInfo(int what, long initMoney) {
			this.what = what;
			this.bUsing = false;
			this.money = initMoney;
			this.saveMoney = 0L;
		}

		int whatObj() {
			return what;
		}

		void addSave() {
			saveMoney += money;
		}

		void subSave() {
			saveMoney -= money;
			if (saveMoney < 0L)
				saveMoney = 0L;
		}

		long getSaveMoney() {
			return saveMoney;
		}

		void setSaveMoney(long saveMoney) {
			if (saveMoney < 0L)
				saveMoney = 0L;
			this.saveMoney = saveMoney;
		}

		void use() {
			bUsing = true;
		}

		void unuse() {
			bUsing = false;
		}

		boolean toggleUse() {
			bUsing = !bUsing;
			return bUsing;
		}

		boolean isUse() {
			return bUsing;
		}

		void changeMoney(long val) {
			money += val;
			if (money < 0L)
				money = 0L;
		}

		void setMoney(long money) {
			this.money = money;
		}

		long getMoney() {
			return money;
		}
	}

	// 싱글턴 패턴으로
	private UserData(Context ctx) {
		if (!loadData(ctx))
			resetUserData(ctx);
	}

	private static UserData userData = null;

	public static UserData sharedUserData(Context ctx) {
		if (userData == null) {
			userData = new UserData(ctx);
		}
		return userData;
	}

	@Override
	protected void finalize() throws Throwable {
		userData = null;

		super.finalize();
	}

}
