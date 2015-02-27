package com.kemblep.hobbsutilities;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

public class HoursConversionFragment extends Fragment {
	
	private static final String TAG = HoursConversionFragment.class.getName();

	public HoursConversionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		final EditText startTimeEntry = (EditText) rootView.findViewById(R.id.start_time_entry);
		
		final EditText endTimeEntry = (EditText) rootView.findViewById(R.id.end_time_entry);

		final TextView resultTime = (TextView) rootView.findViewById(R.id.times_result);
		
		final TextView totalTimes = (TextView) rootView.findViewById(R.id.total);
				
		TextWatcher startTimeWatcher = new TextWatcher() {
			//this is the watcher and functions for the first, i.e. start time entry
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//if the start time has been entered, focus on the end time
				//unless it's also already been entered
				if(s.length() >= 4 && !verifyTimes(startTimeEntry, endTimeEntry)){
					if(s.length() > 4){
						fixErrors(startTimeEntry);
					}
					return;
				}
				
				if (s.toString().length() == 4 && endTimeEntry.length() < 4){
					Util.resetEditText(endTimeEntry);
					endTimeEntry.requestFocus();
				}
				
				//both times have been entered, do the math
				if (s.toString().length() == 4 && endTimeEntry.length() == 4) {
					convertTimes(startTimeEntry, endTimeEntry);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable start) {
				// TODO Auto-generated method stub
			}
		};
		
		TextWatcher endTimeWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence end, int start, int before, int count) {
				if(end.length() >= 4 && !verifyTimes(startTimeEntry, endTimeEntry)){
					//nullify any further input
					if(end.length() > 4){
						fixErrors(endTimeEntry);
					}
					return;
				}
				
				//if the end time has been entered, focus on the start time
				//unless it's also already been entered
				if (end.toString().length() == 4 && startTimeEntry.length() < 4){
					
					Util.resetEditText(startTimeEntry);
					startTimeEntry.requestFocus();
				}
				
				//both times have been entered, do the math
				if (end.toString().length() == 4 && startTimeEntry.length() == 4) {
					convertTimes(startTimeEntry, endTimeEntry);
				}
				
				//here, we assume the next time is being put in so we reset everything
				if(end.toString().length() > 4){
					resetTimes();
					startTimeEntry.requestFocus();
					String extra = end.toString();
					startTimeEntry.setText(extra.substring(extra.length() - 1));
					startTimeEntry.setSelection(1);
					
					//for future implementation, add to the running tally
				}				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable end) {
				

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
		
		startTimeEntry.addTextChangedListener(startTimeWatcher);
		startTimeEntry.setOnClickListener(timesClickListener);
		endTimeEntry.addTextChangedListener(endTimeWatcher);
		endTimeEntry.setOnClickListener(timesClickListener);
		
		Button oopsBtn = (Button) rootView.findViewById(R.id.oops_button);
		oopsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetTimes();
				
			}
		});
		
		Button resetTotal = (Button) rootView.findViewById(R.id.reset_total_button);
		resetTotal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.setText(totalTimes, "0.0");
				Util.setText(resultTime, "0.0");
			}
		});
		
		startTimeEntry.requestFocus();

		return rootView;
	}
	
	public void resetTimes(){
		EditText startTime = (EditText) getView().findViewById(R.id.start_time_entry);
		Util.resetEditText(startTime);
		
		EditText endTime = (EditText) getView().findViewById(R.id.end_time_entry);
		Util.resetEditText(endTime);
		
		startTime.requestFocus();
	}
	
	public void resetTimes(View view){
		resetTimes();
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
	
	private void convertTimes(EditText startTimeEntry, EditText endTimeEntry) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.ENGLISH);
				
		TextView result = (TextView) getView().findViewById(R.id.times_result);
		TextView total = (TextView) getView().findViewById(R.id.total);
		
		try {
			
			if(!verifyTimes(startTimeEntry, endTimeEntry)){
				return;
			}
			
			Date start = sdf.parse(startTimeEntry.getText().toString());
			Date end = sdf.parse(endTimeEntry.getText().toString());
			
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v(TAG, e.getMessage());
		}
	}
	
	
	private void fixErrors(EditText e){
		Util.setText(e, e.getText().toString().substring(4));
		e.setBackgroundColor(Color.TRANSPARENT);
		e.setSelection(1);
	}
}

