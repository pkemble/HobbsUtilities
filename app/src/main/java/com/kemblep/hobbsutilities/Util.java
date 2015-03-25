package com.kemblep.hobbsutilities;

import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.TextView;

import com.kemblep.hobbsutilities.obj.WxReport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util {
	
	public static void setText(TextView tv, String text){
		setText(tv, text, false);
	}
	
	//leave the fail functionality off for now, but maybe can be useful later.
	public static void setText(TextView tv, String text, boolean fail){
		if(fail){
			tv.setBackgroundColor(Color.RED);
			//tv.setTextSize(10);
		} else {
			tv.setBackgroundColor(Color.TRANSPARENT);
			//tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
		}
		tv.setText(text);
	}
	
	public static void resetEditText(EditText e){
		e.setBackgroundColor(Color.TRANSPARENT);
		e.setText(null);
	}
	
	public static boolean isAirplaneModeOn(Context context) throws Settings.SettingNotFoundException {

		   return Settings.Global.getInt(context.getContentResolver(),
		           Settings.Global.AIRPLANE_MODE_ON) != 0;

		}

	public static void updateWx(String newStationId) {
		MainActivity.WeatherReport = new WxReport(newStationId);
	}

	public static String getZuluTime() {
	    SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    return sdf.format(new Date()) + "Z";		
	}
	
	public static String getLocalTime() {
	    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy, HH:mm");
	    return sdf.format(new Date());
	}
}
