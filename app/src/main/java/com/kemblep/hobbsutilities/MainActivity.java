package com.kemblep.hobbsutilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kemblep.hobbsutilities.obj.WxReport;

import java.util.Locale;

public class MainActivity extends ActionBarActivity {

	private static final String ARG_SECTION_NUMBER = "section_number";
    public static String StationId = null;
	public static String SodaStationId = null;
	public static WxReport WeatherReport;

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

//
//    /**
//     * On a widget click go to the TAF tab
//     * @param intent
//     */
//	@Override
//	protected void onNewIntent(Intent intent) {
//		if(intent.getAction().equals(I_COME_FROM_THE_WIDGET)){
//			mViewPager.setCurrentItem(2);
//		}
//		super.onNewIntent(intent);
//	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		StationId = sharedPref.getString(getString(R.string.pref_default_station_key), "KBED");
        if(StationId.isEmpty()){
            StationId = "KBED";
        }
		SodaStationId = sharedPref.getString(getString(R.string.pref_default_soda_station_key), "KBED");
        if(SodaStationId.isEmpty()){
            SodaStationId = "KBED";
        }

        //ActionBar toolbar = getSupportActionBar();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
                        // When the given tab is selected, switch to the corresponding page in
                        // the ViewPager.
                   		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                        if(position == 0){
                            EditText e = (EditText) findViewById(R.id.start_time_entry);
                            if(e != null) e.requestFocus();
                            imm.showSoftInput(e, 0);
                        }

                        if(position == 1){
                            EditText e = (EditText) findViewById(R.id.et_duty_block_start);
                            if(e != null) e.requestFocus();
                            imm.showSoftInput(e, 0);
                        }

                        if(position == 2){
                            EditText e = (EditText) findViewById(R.id.quick_add_entry);
                            if(e != null) e.requestFocus();
                            imm.showSoftInput(e, 0);
                        }

                        if(position == 3){
                            imm.hideSoftInputFromWindow(findViewById(R.id.tv_metar).getWindowToken(), 0);
                        }

                        if(position == 4){
                            imm.hideSoftInputFromWindow(findViewById(R.id.lv_forecast).getWindowToken(), 0);
                        }
					}
				});
		
		mViewPager.setOffscreenPageLimit(3);

		//get the weather
        try {
            if(!Util.isAirplaneModeOn(getApplicationContext())) {
                WeatherReport = new WxReport(StationId);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        //set the wx tab if needed
		Intent intent = getIntent();
        String widgetClick = "WxWidgetClick";
        if(intent.getAction().equals(widgetClick)){
			mViewPager.setCurrentItem(3);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			
			switch (position){
			case 0:
				Fragment hoursConvFragment = new HoursConversionFragment();
				Bundle args = new Bundle();
				args.putInt(ARG_SECTION_NUMBER, position + 1);
				hoursConvFragment.setArguments(args);
				return hoursConvFragment;
            case 1:
                Fragment dutyTimeCalculator = new DutyTimeCalculator();
                Bundle args1 = new Bundle();
                args1.putInt(ARG_SECTION_NUMBER, position + 1);
                dutyTimeCalculator.setArguments(args1);
                return dutyTimeCalculator;
			case 2:
				Fragment quickAddFragment = new QuickAddFragment();
				Bundle args2 = new Bundle();
				args2.putInt(ARG_SECTION_NUMBER, position + 1);
				quickAddFragment.setArguments(args2);
				return quickAddFragment;
				
			case 3:
				Fragment wxMainFragment = new WxMainFragment();
				Bundle args3 = new Bundle();
				args3.putInt(ARG_SECTION_NUMBER, position + 1);
				wxMainFragment.setArguments(args3);
				return wxMainFragment;
				
			case 4:
				Fragment explodingSodaFragment = new ExplodingSodaFragment();
				Bundle args4 = new Bundle();
				args4.putInt(ARG_SECTION_NUMBER, position + 1);
				explodingSodaFragment.setArguments(args4);
				return explodingSodaFragment;
				
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
            //TODO serialize these so they can be disabled
			switch (position) {
			case 0:
				return getString(R.string.title_convert).toUpperCase(l);
            case 1:
                return getString(R.string.title_duty).toUpperCase(l);
			case 2:
				return getString(R.string.title_quick_add).toUpperCase(l);
			case 3:
				return getString(R.string.title_wx).toUpperCase(l);
			case 4:
				return getString(R.string.title_soda).toUpperCase(l);
			}
			return null;
		}
	}
}
