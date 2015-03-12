package com.kemblep.hobbsutilities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.kemblep.hobbsutilities.obj.sWxReport;

public class WxWidget extends AppWidgetProvider {
	
	public static String WX_WIDGET_CLICK = "WxWidgetClick"; //TODO put this somewhere global
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.wx_widget_main);
		ComponentName thisWidget = new ComponentName(context, WxWidget.class);
		
		SharedPreferences sharedPref = context.getSharedPreferences("MainActivity", context.MODE_PRIVATE);
		String stationId = sharedPref.getString(context.getString(R.string.pref_default_station_key), "KBED");
		
		sWxReport wxReport;
		if(MainActivity.WeatherReport == null){
			wxReport = new sWxReport(stationId);
		} else {	
			wxReport = MainActivity.WeatherReport;
		}
		
		remoteViews.setTextViewText(R.id.w_wx, wxReport.FullReport);
		remoteViews.setTextViewText(R.id.w_time, "Fetched at: " + wxReport.FetchedTime);
		
		Intent intent = new Intent(context, MainActivity.class);
		intent.setAction(WX_WIDGET_CLICK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, R.xml.wxod_widget, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.w_wx, pendingIntent);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	
}
