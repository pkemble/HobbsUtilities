package com.kemblep.hobbsutilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.kemblep.hobbsutilities.obj.WxReport;

public class WxMainFragment extends Fragment {
	
	private String mStationId = MainActivity.StationId;
	
	public WxMainFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View vWxMainFragment = inflater.inflate(R.layout.wx_fragment_main, container,	false);
        final Button btnRefresh = (Button) vWxMainFragment.findViewById(R.id.btn_refresh);
        final Button btnStation = (Button) vWxMainFragment.findViewById(R.id.btn_station);

		btnRefresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PopulateWx(vWxMainFragment);
			}
		});
		
		if(mStationId != null){
			btnStation.setText(mStationId);
		}
		btnStation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(vWxMainFragment.getContext());
				
				final EditText input = new EditText(v.getContext());
				alert.setView(input);
				alert.setTitle(R.string.station_entry_title);
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String newStationId = input.getText().toString().trim();
						if(newStationId.length() == 3 && !Character.isDigit(newStationId.charAt(0))){
                            newStationId = "K" + newStationId;
						}
						mStationId = newStationId;
						btnStation.setText(newStationId);
						Activity activity = getActivity();
						SharedPreferences sharedPref = activity.getPreferences(activity.MODE_PRIVATE);
						SharedPreferences.Editor edit = sharedPref.edit();
						edit.putString(getString(R.string.pref_default_station_key), newStationId);
						edit.commit();
						
						Util.updateWx(newStationId);
						PopulateWx(vWxMainFragment);
					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
					}
				});
				
				input.requestFocus();
				alert.show();
			}
		});
		PopulateWx(vWxMainFragment);
		return vWxMainFragment;
	}

	public void PopulateWx(View v){
		TextView tvMetar = (TextView) v.findViewById(R.id.tv_metar);
		TextView tvTaf = (TextView) v.findViewById(R.id.tv_taf);
		if(Util.isAirplaneModeOn(getActivity())){
			Util.setText(tvMetar, getString(R.string.airplane_mode));
			return;
		}
		
		WxReport wxReport = MainActivity.WeatherReport;
		
		if(tvMetar != null && tvTaf != null){
			tvMetar.setText(wxReport.Metar.FormattedMetar);
			tvTaf.setText(wxReport.Taf.FormattedTaf);
		}
		TextView tvFetchedTime = (TextView) v.findViewById(R.id.tv_fetched_time);
		if(tvFetchedTime != null){
			Util.setText(tvFetchedTime, "Fetched at " + wxReport.FetchedTime);
		}
		//update the widget
		Context context = getActivity().getBaseContext();
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.wx_widget_main);
		remoteViews.setTextViewText(R.id.w_wx, wxReport.FullReport);
		remoteViews.setTextViewText(R.id.w_time, "Fetched at: " + wxReport.FetchedTime);
		ComponentName widget = new ComponentName(context, WxWidget.class);
		appWidgetManager.updateAppWidget(widget, remoteViews);
	}
}

