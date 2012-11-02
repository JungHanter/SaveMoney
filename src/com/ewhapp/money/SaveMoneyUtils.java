package com.ewhapp.money;

import android.os.Environment;

public class SaveMoneyUtils {
	static public String convertMoneyComma(long money) {
		StringBuffer strMoney = new StringBuffer("" + money);
		int len = strMoney.length();
		int head = len%3;
		int nComma = 0;
		for(int i=0; i<len/3; i++) {
			int space = head + nComma + i*3;
			if(space==0) continue;
			nComma++;
			strMoney.insert(space, ',');
		}
		return strMoney.toString();
	}
	
	static public String convertMoneyComma(int money) {
		return convertMoneyComma((long)money);
	}
	
	final static public String GOAL_IMG_NAME = "GoalImg.jpg";
	final static private String DATA_PATH = "/data/com.ewhapp.money/";
	static public String getDataFilePath() {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		path += DATA_PATH;
		return path;
	}
	static public void makeNomediaFile() {
		
	}
}
