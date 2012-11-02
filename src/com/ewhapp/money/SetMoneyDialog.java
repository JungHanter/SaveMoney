package com.ewhapp.money;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Window;
import android.widget.LinearLayout;

public class SetMoneyDialog extends Dialog {
	private Context mCtx = null;
	
	private SetMoneyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	private SetMoneyDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}


	public SetMoneyDialog(Context context) {
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
