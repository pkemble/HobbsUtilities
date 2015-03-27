package com.kemblep.hobbsutilities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kemblep.hobbsutilities.obj.DutyTimeArrayAdapter;
import com.kemblep.hobbsutilities.obj.HobbsTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pete on 3/25/2015.
 */
public class DutyTimeCalculator extends Fragment {

    public DutyTimeCalculator(){
    }

    private float mTotalDutyDateTime;
    private ArrayList<String> mDutyPeriods = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        final View vDutyTimeCalculator = inflater.inflate(R.layout.fragment_10in24, container, false);

        final EditText etDutyStart = (EditText) vDutyTimeCalculator.findViewById(R.id.et_duty_block_start);
        final EditText etDutyEnd = (EditText) vDutyTimeCalculator.findViewById(R.id.et_duty_block_end);
        final Button btnReset = (Button) vDutyTimeCalculator.findViewById(R.id.btn_reset_duty_times);

        btnReset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resetTimes(etDutyStart, etDutyEnd);
            }
        });

        TextWatcher twBlockOut = new TextWatcher() {
            //this is the watcher and functions for the first, i.e. start time entry

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if the start time has been entered, focus on the end time
                //unless it's also already been entered
                if(s.length() >= 4 && !Util.verifyTimes(etDutyStart, etDutyEnd)){
                    if(s.length() > 4){
                        Util.fixErrors(etDutyStart);
                    }
                    return;
                }

                if (s.toString().length() == 4 && etDutyEnd.length() < 4){
                    Util.resetEditText(etDutyEnd);
                    etDutyEnd.requestFocus();
                }

                //both times have been entered, do the math
                if (s.toString().length() == 4 && etDutyEnd.length() == 4) {
                    addToDuty(etDutyStart, etDutyEnd, vDutyTimeCalculator);
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
                if(end.length() >= 4 && !Util.verifyTimes(etDutyStart, etDutyEnd)){
                    //nullify any further input
                    if(end.length() > 4){
                        Util.fixErrors(etDutyEnd);
                    }
                    return;
                }

                //if the end time has been entered, focus on the start time
                //unless it's also already been entered
                if (end.toString().length() == 4 && etDutyStart.length() < 4){

                    Util.resetEditText(etDutyStart);
                    etDutyStart.requestFocus();
                }

                //both times have been entered, do the math
                if (end.toString().length() == 4 && etDutyStart.length() == 4) {
                    addToDuty(etDutyStart, etDutyEnd, vDutyTimeCalculator);
                }

                //here, we assume the next time is being put in so we reset everything
                if(end.toString().length() > 4){
                    resetTimes(etDutyEnd, etDutyStart);
                    etDutyStart.requestFocus();
                    String extra = end.toString();
                    etDutyStart.setText(extra.substring(extra.length() - 1));
                    etDutyStart.setSelection(1);

                    //for future implementation, add to the running tally
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etDutyStart.addTextChangedListener(twBlockOut);
        etDutyEnd.addTextChangedListener(twBlockIn);

        return vDutyTimeCalculator;
    }

    private void addToDuty(EditText etDutyStart, EditText etDutyEnd, View vDutyTimeCalculator) {
        //get the current time
        Date pre = new Date();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
            //make the current time relative to the other times below (i.e. 1970)
            String sNow = sdf.format(pre);
            Date now = sdf.parse(sNow);

            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            //make a relative time to compare to
            Date twentyFourAgo = cal.getTime();
            //make "now" 24 hours after 'twentyFourAgo'
            cal.add(Calendar.HOUR_OF_DAY, 24);
            now = cal.getTime();

        //parse the start time
            Date blockOut = sdf.parse(etDutyStart.getText().toString());

        //parse the end time
            Date blockIn = sdf.parse(etDutyEnd.getText().toString());

        //if the end time is before now a day ago, return
            if(blockIn.before(twentyFourAgo)){
                return;
            }
        //if the start time is before now a day ago, use now a day ago instead
            if(blockOut.before(twentyFourAgo)){
                blockOut = twentyFourAgo;
            }
        //make a hobbstime object
            HobbsTime dutyTime = new HobbsTime(blockOut, blockIn);

        //display the total duty time
            TextView tvDutyTotal = (TextView) getView().findViewById(R.id.tv_duty_total);
            tvDutyTotal.setText(dutyTime.add(mTotalDutyDateTime));

            String dutyPeriod = sdf.format(dutyTime.StartDate) + " to " + sdf.format(dutyTime.EndDate) + " : "
                    + dutyTime.ConvertedTime;

            mDutyPeriods.add(dutyPeriod);

            DutyTimeArrayAdapter dutyTimeArrayAdapter =
                   new DutyTimeArrayAdapter(vDutyTimeCalculator.getContext(), mDutyPeriods);

            ListView lvDutyPeriods = (ListView) vDutyTimeCalculator.findViewById(R.id.lv_duty_times);
            lvDutyPeriods.setAdapter(dutyTimeArrayAdapter);

            Util.resetEditText(etDutyStart);
            Util.resetEditText(etDutyEnd);
            etDutyStart.requestFocus();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void resetTimes(EditText etBlockIn, EditText etBlockOut){
        Util.resetEditText(etBlockIn);
        Util.resetEditText(etBlockOut);
    }
}
