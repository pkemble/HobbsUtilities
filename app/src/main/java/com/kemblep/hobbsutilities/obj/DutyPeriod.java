package com.kemblep.hobbsutilities.obj;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pete on 3/27/2015.
 */
public class DutyPeriod extends HobbsTime {
    private boolean mTruncated;
    public String Information;

    public DutyPeriod(Date start, Date end, boolean truncated) {
        super(start, end);
        mTruncated = truncated;
        Information = setInformation();
    }

    private String setInformation() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String info = sdf.format(super.StartDate) + " - " + sdf.format(super.EndDate) + " " +
                super.ConvertedTime;
        if(mTruncated){
            return "* " + info;
        }
        return "  " + info;
    }
}
