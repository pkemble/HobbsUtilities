package com.kemblep.hobbsutilities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HoursConversionFragment extends Fragment {
	
	private static final String TAG = HoursConversionFragment.class.getName();

	public HoursConversionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View vHoursConversion = inflater.inflate(R.layout.fragment_convert_times, container,
				false);
		final EditText etBlockOut = (EditText) vHoursConversion.findViewById(R.id.start_time_entry);
		final EditText etBlockIn = (EditText) vHoursConversion.findViewById(R.id.end_time_entry);
		final TextView tvBlockTime = (TextView) vHoursConversion.findViewById(R.id.times_result);
		final TextView tvTotalBlockTime = (TextView) vHoursConversion.findViewById(R.id.total);
				
		TextWatcher twBlockOut = new TextWatcher() {
			//this is the watcher and functions for the first, i.e. start time entry

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//if the start time has been entered, focus on the end time
				//unless it's also already been entered
				if(s.length() >= 4 && !verifyTimes(etBlockOut, etBlockIn)){
					if(s.length() > 4){
						fixErrors(etBlockOut);
					}
					return;
				}
				
				if (s.toString().length() == 4 && etBlockIn.length() < 4){
					Util.resetEditText(etBlockIn);
					etBlockIn.requestFocus();
				}
				
				//both times have been entered, do the math
				if (s.toString().length() == 4 && etBlockIn.length() == 4) {
					convertTimes(etBlockOut, etBlockIn);
				}
			}

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
		
		TextWatcher twBlockIn = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
			public void onTextChanged(CharSequence end, int start, int before, int count) {
				if(end.length() >= 4 && !verifyTimes(etBlockOut, etBlockIn)){
					//nullify any further input
					if(end.length() > 4){
						fixErrors(etBlockIn);
					}
					return;
				}

				//if the end time has been entered, focus on the start time
				//unless it's also already been entered
				if (end.toString().length() == 4 && etBlockOut.length() < 4){

					Util.resetEditText(etBlockOut);
					etBlockOut.requestFocus();
				}

				//both times have been entered, do the math
				if (end.toString().length() == 4 && etBlockOut.length() == 4) {
					convertTimes(etBlockOut, etBlockIn);
				}

				//here, we assume the next time is being put in so we reset everything
				if(end.toString().length() > 4){
					resetTimes(etBlockIn, etBlockOut);
					etBlockOut.requestFocus();
					String extra = end.toString();
					etBlockOut.setText(extra.substring(extra.length() - 1));
					etBlockOut.setSelection(1);

					//for future implementation, add to the running tally
				}
			}

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

		OnClickListener timesClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setBackgroundColor(Color.TRANSPARENT);
				EditText e = (EditText) getView().findViewById(v.getId());
				Util.resetEditText(e);
			}
		};
		
		etBlockOut.addTextChangedListener(twBlockOut);
		etBlockOut.setOnClickListener(timesClickListener);
		etBlockIn.addTextChangedListener(twBlockIn);
		etBlockIn.setOnClickListener(timesClickListener);
		
		Button btnClearBlockTimes = (Button) vHoursConversion.findViewById(R.id.oops_button);
		btnClearBlockTimes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                resetTimes(etBlockIn, etBlockOut);

            }
        });
		
		Button btnResetBlockTimes = (Button) vHoursConversion.findViewById(R.id.reset_total_button);
		btnResetBlockTimes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Util.setText(tvTotalBlockTime, "0.0");
                Util.setText(tvBlockTime, "0.0");
            }
        });
		
		etBlockOut.requestFocus();

		return vHoursConversion;
	}
	
	public void resetTimes(EditText etBlockIn, EditText etBlockOut){
		Util.resetEditText(etBlockIn);
		Util.resetEditText(etBlockOut);
		etBlockIn.requestFocus();
	}

	private boolean verifyTimes(EditText startTimeEntry, EditText endTimeEntry){
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
	
	private void convertTimes(EditText etBlockIn, EditText etBlockOut) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.ENGLISH);
				
		TextView result = (TextView) getView().findViewById(R.id.times_result);
		TextView total = (TextView) getView().findViewById(R.id.total);
		
		try {
			
			if(!verifyTimes(etBlockIn, etBlockOut)){
				return;
			}
			
			Date start = sdf.parse(etBlockIn.getText().toString());
			Date end = sdf.parse(etBlockOut.getText().toString());
			
			if(start.after(end)){
				Calendar c = Calendar.getInstance();
				c.setTime(end);
				c.add(Calendar.DATE, 1);
				end = c.getTime();
			}
												
			DecimalFormat preciseFormat = new DecimalFormat("0.##");
			DecimalFormat finalFormat = new DecimalFormat("0.0");

			double mills = (end.getTime() - start.getTime());
			float convertedMills = (float) (mills / 3600000);
			//float floatMills = Math.round((mills / 3600000) * 10) /10;
			
			String diff = preciseFormat.format(convertedMills);
			Util.setText(result, finalFormat.format(convertedMills));
			Log.v(TAG, "decimal format 0.00:" + diff
					+ "\n milliseconds: " + mills
					+ "\n decimal format 0.00: " + preciseFormat.format(convertedMills));
			
			//add to total
			if(total.getText().toString().matches("")){
				Util.setText(total, "0.0");
			}
			float totalFloat = Float.parseFloat(total.getText().toString());
			totalFloat = convertedMills + totalFloat;
			
			Util.setText(total, finalFormat.format(totalFloat));		
			
			
		} catch (ParseException e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
	}

	private void fixErrors(EditText e){
		Util.setText(e, e.getText().toString().substring(4));
		e.setBackgroundColor(Color.TRANSPARENT);
		e.setSelection(1);
	}
}

