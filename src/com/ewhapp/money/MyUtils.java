package com.ewhapp.money;

import android.util.DisplayMetrics;

public class MyUtils {
	public static int dpToPixel(float dp, float densityDpi) {
		int pixel = (int)(dp * (densityDpi / DisplayMetrics.DENSITY_DEFAULT));
		return pixel;
	}
}
