package com.kemblep.hobbsutilities;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kemblep.hobbsutilities.obj.WxReport;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private static final String ARG_SECTION_NUMBER = "section_number";
	private static String I_COME_FROM_THE_WIDGET = "WxWidgetClick";
	public static String stationId = null;
	public static String sodaStationId = null;
	public static WxReport WeatherReport;
			
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	@Override
	protected void onNewIntent(Intent intent) {
		if(intent.getAction().equals(I_COME_FROM_THE_WIDGET)){
			getSupportActionBar().setSelectedNavigationItem(2);
		}
		super.onNewIntent(intent);
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		stationId = sharedPref.getString(getString(R.string.pref_default_station_key), "KBED");
		sodaStationId = sharedPref.getString(getString(R.string.pref_default_soda_station_key), "KBED");
		

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		mViewPager.setOffscreenPageLimit(3);

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		//get the weather
		WeatherReport = new WxReport(stationId);
		
		//set the wx tab if needed
		Intent intent = getIntent();
		if(intent.getAction().equals(I_COME_FROM_THE_WIDGET)){
			actionBar.setSelectedNavigationItem(2);
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		int selectedTab = tab.getPosition(); 
		mViewPager.setCurrentItem(selectedTab);
		if(selectedTab == 0){
			EditText e = (EditText) findViewById(R.id.start_time_entry);
			if(e != null) e.requestFocus();
			imm.showSoftInput(e, 0);
		}
		
		if(selectedTab == 1){
			EditText e = (EditText) findViewById(R.id.quick_add_entry);
			if(e != null) e.requestFocus();
			imm.showSoftInput(e, 0);
		}
		
		if(selectedTab == 2){
			TextView t = (TextView) findViewById(R.id.tv_metar);
			imm.hideSoftInputFromWindow(t.getWindowToken(), 0);
		}
		
		if(selectedTab == 3){
			TextView t = (TextView) findViewById(R.id.tv_exploding_soda);
			imm.hideSoftInputFromWindow(t.getWindowToken(), 0);
		}

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
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
				Fragment quickAddFragment = new QuickAddFragment();
				Bundle args1 = new Bundle();
				args1.putInt(ARG_SECTION_NUMBER, position + 1);
				quickAddFragment.setArguments(args1);
				return quickAddFragment;
				
			case 2:
				Fragment wxMainFragment = new WxMainFragment();
				Bundle args2 = new Bundle();
				args2.putInt(ARG_SECTION_NUMBER, position + 1);
				wxMainFragment.setArguments(args2);
				return wxMainFragment;
				
			case 3:
				Fragment explodingSodaFragment = new ExplodingSodaFragment();
				Bundle args3 = new Bundle();
				args3.putInt(ARG_SECTION_NUMBER, position + 1);
				explodingSodaFragment.setArguments(args3);
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
			switch (position) {
			case 0:
				return getString(R.string.title_convert).toUpperCase(l);
			case 1:
				return getString(R.string.title_quick_add).toUpperCase(l);
			case 2:
				return getString(R.string.title_wx).toUpperCase(l);
			case 3:
				return getString(R.string.title_soda).toUpperCase(l);
			}
			return null;
		}
	}
}
