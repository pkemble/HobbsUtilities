package com.kemblep.hobbsutilities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kemblep.hobbsutilities.obj.Forecast;
import com.kemblep.hobbsutilities.obj.StationInfo;

public class ExplodingSodaFragment extends Fragment {
	
	private String _sodaStationId = MainActivity.sodaStationId;

	public ExplodingSodaFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View sodaView = inflater.inflate(R.layout.exploding_soda, container, false);
		
		final TextView tvTemp = (TextView) sodaView.findViewById(R.id.tv_exploding_soda);
		final TextView tvSodaTime = (TextView) sodaView.findViewById(R.id.tv_soda_time);
		final TextView tvMoreInfo = (TextView) sodaView.findViewById(R.id.tv_more_info);
		//final Button btnSodaRefresh = (Button) sodaView.findViewById(R.id.btn_soda_refresh);
		final Button btnSodaStation = (Button) sodaView.findViewById(R.id.btn_soda_station);
		final ImageView imgSoda = (ImageView) sodaView.findViewById(R.id.img_soda);
		
		//http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource=stations&requestType=retrieve&format=xml&stationString=<STATION>
		
		//send the request
		GetSodaFate(sodaView);
		
		imgSoda.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GetSodaFate(sodaView);
			}
		});
		
		btnSodaStation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(sodaView.getContext());
				
				final EditText input = new EditText(v.getContext());
				alert.setView(input);
				alert.setTitle(R.string.station_entry_title);
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String newStationId = input.getText().toString().trim();
						if(newStationId.length() == 3){
							newStationId = "K" + newStationId;
						}
						_sodaStationId = newStationId;
						btnSodaStation.setText(newStationId);
						Activity activity = getActivity();
						SharedPreferences sharedPref = activity.getPreferences(activity.MODE_PRIVATE);
						SharedPreferences.Editor edit = sharedPref.edit();
						edit.putString(getString(R.string.pref_default_soda_station_key), newStationId);
						edit.commit();
						
						GetSodaFate(sodaView);

					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
					}
				});
				
				alert.show();
			}
		});
		
		btnSodaStation.setText(_sodaStationId.toUpperCase());
		
		return sodaView;
	}

	private void GetSodaFate(View sodaView) {
		ProgressBar spinner = (ProgressBar) sodaView.findViewById(R.id.soda_progress);
		spinner.setVisibility(View.VISIBLE);
		
		StationInfo stationInfo = new StationInfo(_sodaStationId );
		Forecast forecast = new Forecast(stationInfo.Latitude, stationInfo.Longitude);
		
		spinner.setVisibility(View.GONE);
		String result;
		TextView tvTemp = (TextView) sodaView.findViewById(R.id.tv_exploding_soda);
		ImageView imgSoda = (ImageView) sodaView.findViewById(R.id.img_soda);
		{
			Float low = Float.parseFloat(forecast.NextLow);
			if(low <= 32){
				result = forecast.NextLow;
				tvTemp.setTextColor(getResources().getColor(R.color.exploding_soda_orange));
				imgSoda.setImageResource(R.drawable.soda_plode);
			} else {
				result = forecast.NextLow;
				tvTemp.setTextColor(getResources().getColor(R.color.safe_soda_green));
				imgSoda.setImageResource(R.drawable.soda);
			}
			
			TextView tvSodaTime = (TextView) sodaView.findViewById(R.id.tv_soda_time);
			TextView tvMoreInfo = (TextView) sodaView.findViewById(R.id.tv_more_info); 
			
			tvTemp.setText(result + (char) 0x00B0 + "F");
			Util.setText(tvSodaTime, "Refreshed at " + Util.getLocalTime());
			Util.setText(tvMoreInfo, stationInfo.SiteId + " : " + forecast.Description + "\n" + forecast.moreInfoUrl);
			
			Linkify.addLinks(tvMoreInfo, Linkify.ALL);

		}
	}
}
