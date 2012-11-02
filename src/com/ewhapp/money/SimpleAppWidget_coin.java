package com.ewhapp.money;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class SimpleAppWidget_coin extends AppWidgetProvider {

	private UserData userData;
	private int rate = 0;
	private String str = "";

	public static final String APPWIDGET_REQUEST = "com.ewhapp.money.SplashActivity.APPWIDGET_REQUEST";

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		userData = UserData.sharedUserData(context);
		rate = userData.getAchieveRate();
		str = Integer.toString(rate);

		AppWidgetManager appWidgetManager1 = AppWidgetManager
				.getInstance(context);
		ComponentName widget = new ComponentName(context,
				SimpleAppWidget_coin.class);
		int[] appWidgetIds1 = appWidgetManager1.getAppWidgetIds(widget);

		Intent intent = new Intent(context, WidgetDialogActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		RemoteViews remoteView = new RemoteViews(context.getPackageName(),
				R.layout.widget_coin_layout);

		remoteView.setOnClickPendingIntent(R.id.coinimg, pendingIntent);
		
		remoteView.setTextViewText(R.id.coin_rate_txt, str + "%");
		if (rate >= 25) {
			remoteView.setImageViewResource(R.id.coinimg, R.drawable.w_coin_25);
		}
		if (rate >= 50) {
			remoteView.setImageViewResource(R.id.coinimg, R.drawable.w_coin_50);
		}
		if (rate >= 75) {
			remoteView.setImageViewResource(R.id.coinimg, R.drawable.w_coin_75);
		}
		if (rate >= 100) {
			remoteView.setTextViewText(R.id.coin_rate_txt, "100%");
			remoteView
					.setImageViewResource(R.id.coinimg, R.drawable.w_coin_100);
		}
		if (rate < 25) {
			remoteView.setImageViewResource(R.id.coinimg, R.drawable.w_coin_0);
		}
		appWidgetManager.updateAppWidget(appWidgetIds1, remoteView);

	}

	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		userData = UserData.sharedUserData(context);
		rate = userData.getAchieveRate();
		str = Integer.toString(rate);

		if (APPWIDGET_REQUEST.equals(intent.getAction())) {
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName widget = new ComponentName(context,
					SimpleAppWidget_coin.class);
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);
			final int N = appWidgetIds.length;
			for (int i = 0; i < N; i++) {
				int appWidgetId = appWidgetIds[i];
				RemoteViews updateViews = new RemoteViews(
						context.getPackageName(), R.layout.widget_coin_layout);
				updateViews.setTextViewText(R.id.coin_rate_txt, str + "%");
				if (rate >= 25) {
					updateViews.setImageViewResource(R.id.coinimg,
							R.drawable.w_coin_25);
				}
				if (rate >= 50) {
					updateViews.setImageViewResource(R.id.coinimg,
							R.drawable.w_coin_50);
				}
				if (rate >= 75) {
					updateViews.setImageViewResource(R.id.coinimg,
							R.drawable.w_coin_75);
				}
				if (rate >= 100) {
					updateViews.setTextViewText(R.id.coin_rate_txt, "100%");
					updateViews.setImageViewResource(R.id.coinimg,
							R.drawable.w_coin_100);

				}
				if (rate < 25) {
					updateViews.setImageViewResource(R.id.coinimg,
							R.drawable.w_coin_0);
				}

				appWidgetManager.updateAppWidget(appWidgetId, updateViews);
			}
		}
	}
}
