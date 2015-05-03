package com.kemblep.hobbsutilities.obj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kemblep.hobbsutilities.R;

import java.util.ArrayList;

/**
 * Created by Pete on 3/25/2015.
 */
public class DutyTimeArrayAdapter extends BaseAdapter {
    private ArrayList<DutyPeriod> mDutyPeriods;
    private Context mContext;

    public DutyTimeArrayAdapter(Context context, ArrayList<DutyPeriod> dutyPeriods){
        mContext = context;
        mDutyPeriods = dutyPeriods;
    }

    @Override
    public int getCount() {
        return mDutyPeriods.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_duty_times, null);
        }

        TextView tvDutyPeriod = (TextView) convertView.findViewById(R.id.tv_duty_period);
        tvDutyPeriod.setText(mDutyPeriods.get(position).Information);

        return convertView;
    }
}
