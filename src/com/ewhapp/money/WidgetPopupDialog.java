package com.ewhapp.money;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class WidgetPopupDialog extends Dialog {
	private Context mCtx = null;
	
	private WidgetPopupDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	private WidgetPopupDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}


	public WidgetPopupDialog(Context context) {
		super(context);
		init(context);
	}

	private LinearLayout customView = null;
	private void init(final Context context) {
		mCtx = context;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//customView = (LinearLayout) View.inflate(context, R.layout.mydialog, null);
		//setContentView(customView);
	}
}
