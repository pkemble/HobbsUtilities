package com.kemblep.hobbsutilities.obj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kemblep.hobbsutilities.R;

import java.util.List;

/**
 * Created by Pete on 3/11/2015.
 */
public class TempArrayAdapter extends BaseAdapter{
    private List<Forecast.TimePeriod> mTimePeriods;
    private Context mContext;

    public TempArrayAdapter(Context context, Forecast forecast) {
        mTimePeriods = forecast.FCTimeTempMap.TimePeriods;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mTimePeriods.size();
    }

    @Override
    public Object getItem(int position) {
        return mTimePeriods.get(position);
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
            convertView = inflater.inflate(R.layout.listview_forecast, null);
        }

        TextView fcLabel = (TextView) convertView.findViewById(R.id.forecast_label);
        TextView fcTemp = (TextView) convertView.findViewById(R.id.forecast_temp);

        Forecast.TimePeriod timePeriod = mTimePeriods.get(position);

        fcLabel.setText(timePeriod.PeriodName);
        if(timePeriod.Temperature < 32){
            fcTemp.setTextColor(parent.getResources().getColor(R.color.exploding_soda_orange));
        } else {
            fcTemp.setTextColor(parent.getResources().getColor(R.color.safe_soda_green));
        }

        fcTemp.setText(Integer.toString(timePeriod.Temperature) + (char) 0x00B0 + "F");

        return convertView;
    }
}
