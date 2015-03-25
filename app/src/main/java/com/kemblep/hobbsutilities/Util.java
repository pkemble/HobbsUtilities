package com.kemblep.hobbsutilities;

import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.TextView;

import com.kemblep.hobbsutilities.obj.WxReport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static boolean verifyTimes(EditText startTimeEntry, EditText endTimeEntry){
        //verify good dates, despite 2500 being interpreted as 0100
        String t1 = "0000", t2 = "0000";

        if(startTimeEntry.getText().length() > 0){
            t1 = startTimeEntry.getText().toString();
        }
        if(endTimeEntry.getText().length() > 0){
            t2 = endTimeEntry.getText().toString();
        }

        int t1h = Integer.parseInt(t1.substring(0, 2));
        int t1m = Integer.parseInt(t1.substring(2, 4));
        int t2h = Integer.parseInt(t2.substring(0, 2));
        int t2m = Integer.parseInt(t2.substring(2, 4));

        if(t1h > 23 | t1m > 59){
            startTimeEntry.setBackgroundColor(Color.RED);
            return false;
        } else {
            startTimeEntry.setBackgroundColor(Color.TRANSPARENT);
        }

        if(t2h > 23 | t2m > 59){
            endTimeEntry.setBackgroundColor(Color.RED);
            return false;
        } else {
            startTimeEntry.setBackgroundColor(Color.TRANSPARENT);
        }
        //everything is good
        return true;
    }

    public static void fixErrors(EditText e){
        Util.setText(e, e.getText().toString().substring(4));
        e.setBackgroundColor(Color.TRANSPARENT);
        e.setSelection(1);
    }

    //if the end time is "before" the start time (i.e. went past midnight)
    //then fix the day to reflect this.
    public static Date fixEndTime(Date start, Date end) {
        if(start.after(end)){
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.DATE, 1);
            end = c.getTime();
        }
        return end;
    }
}
