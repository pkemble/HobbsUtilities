package com.kemblep.hobbsutilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kemblep.hobbsutilities.obj.Forecast;
import com.kemblep.hobbsutilities.obj.StationInfo;
import com.kemblep.hobbsutilities.obj.TempArrayAdapter;

//import android.widget.ImageView;

public class ExplodingSodaFragment extends Fragment
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private String mSodaStationId = MainActivity.SodaStationId;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private final String TAG = ExplodingSodaFragment.class.getName();

    public ExplodingSodaFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View sodaView = inflater.inflate(R.layout.exploding_soda, container, false);
		final Button btnLocate = (Button) sodaView.findViewById(R.id.btn_locate);
		final Button btnSodaStation = (Button) sodaView.findViewById(R.id.btn_soda_station);

		//http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource=stations&requestType=retrieve&format=xml&stationString=<STATION>

		GetSodaFate(sodaView);
        
        buildGoogleApiClient();
        createLocationRequest();

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
                        //TODO handle blank entries
						if(newStationId.length() == 3 && !Character.isDigit(newStationId.charAt(0))){
							newStationId = "K" + newStationId;
						}
                        if(newStationId.length() == 4){
                            mSodaStationId = newStationId;
                            GetSodaFate(sodaView);
                        }
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
		
        btnLocate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mGoogleApiClient.connect();
            }
        });
		
		return sodaView;
	}

    private void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(50000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        Activity mActivity = this.getActivity();
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
        .build();

    }
    private void GetSodaFate(View sodaView){
        StationInfo stationInfo = new StationInfo(mSodaStationId);
        Forecast forecast = new Forecast(stationInfo.Latitude, stationInfo.Longitude);
        GetSodaFate(sodaView, forecast);
    }

    private void GetSodaFate(View sodaView, Forecast forecast) {

        ImageView ivMap = (ImageView) sodaView.findViewById(R.id.img_map);
        if(ivMap != null && forecast.MapForecastLocation != null){
            ivMap.setImageBitmap(forecast.MapForecastLocation);
        }
        if(!forecast.FCTimeTempMap.TimePeriods.isEmpty()){
            TempArrayAdapter tempArrayAdapter = new TempArrayAdapter(sodaView.getContext(), forecast);

            ListView lvForecast = (ListView) sodaView.findViewById(R.id.lv_forecast);
            lvForecast.setAdapter(tempArrayAdapter);
        }

        TextView tvSodaTime = (TextView) sodaView.findViewById(R.id.tv_soda_time);
        TextView tvMoreInfo = (TextView) sodaView.findViewById(R.id.tv_more_info);

        //tvTemp.setText(result + (char) 0x00B0 + "F");
        Util.setText(tvSodaTime, "Refreshed at " + Util.getLocalTime());
        Util.setText(tvMoreInfo, forecast.Description + "\n" + forecast.MoreInfoUrl);

        Activity activity = getActivity();
        SharedPreferences sharedPref = activity.getPreferences(activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString(getString(R.string.pref_default_soda_station_key), mSodaStationId);
        edit.commit();

        Linkify.addLinks(tvMoreInfo, Linkify.ALL);

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            Log.d(TAG, "mLastLocation is not null!");
            Forecast forecast =
                    new Forecast(Double.toString(mLastLocation.getLatitude()),
                            Double.toString(mLastLocation.getLongitude()));
            if(forecast != null){
                GetSodaFate(this.getView(), forecast);
            }
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

