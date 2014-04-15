package com.example.workhours;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import com.example.workhours.entities.Shift;

import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	CalendarView calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}	
	
	@Override
	protected void onResume(){
		super.onResume();
		
		Intent shiftData = getIntent();
		Calendar dateFrom = (Calendar) shiftData.getSerializableExtra("DATEFROM");
		Calendar dateTo = (Calendar) shiftData.getSerializableExtra("DATETO");
		
		try{
			Toast.makeText(getApplicationContext(), "From:"+dateFrom.toString(), Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "To:"+dateTo.toString(), Toast.LENGTH_LONG).show();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
				Fragment fragment = null;
				switch(position){
				case 0:
					fragment = new ShiftFragment();
					break;
				case 1:
					fragment = new EventFragment();	
					break;
				case 2:
					fragment = new DebtFragment();
					break;
				}
				return fragment;	
				
				/*
				 	fragment = new DummySectionFragment();
					Bundle args = new Bundle();
					args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
					fragment.setArguments(args);
				 */
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
	
	public static class ShiftFragment extends Fragment{
		private long date;
		
		public ShiftFragment(){}
				
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState){

			return inflater.inflate(R.layout.fragment_add_shift, container, false);
		}
		
		@Override
	    public void onActivityCreated(Bundle savedInstanceState)
	    {
	        super.onActivityCreated(savedInstanceState);
	        
	        //View rootView = getView().findViewById(R.layout.fragment_add_shift);
			CalendarView calendar = (CalendarView) getView().findViewById(R.id.calendarMain);
			date = calendar.getDate();
			calendar.setOnDateChangeListener(new OnDateChangeListener(){
				
				@Override
				public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
					date = view.getDate();
				}
			});
			
			Button btn = (Button) getView().findViewById(R.id.selectDateButton);
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), ShiftActivity.class);
					intent.putExtra("DATE", date);	
					startActivity(intent);
				}
			});			
	    }
	}
	
	public static class EventFragment extends Fragment{
		private List<Shift> list;
		private TextView view;
		
		private int txtSize = 16;
		private int color = Color.BLACK;
		private boolean clickable = true;
		
		public EventFragment(){
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
			
			View rootView = inflater.inflate(R.layout.fragment_view_events, container, false);
			LinearLayout layoutFrom = (LinearLayout) rootView.findViewById(R.id.containerFrom);
			LinearLayout layoutTo = (LinearLayout) rootView.findViewById(R.id.containerTo);
			LinearLayout layoutHours = (LinearLayout) rootView.findViewById(R.id.containerHours);
			
			TextView from, to, hours;		
			
			
			final int NUM = 50;
			for(int i = 0; i < NUM; i++){
				from = new TextView(getActivity());
				fillLayout("from", from, layoutFrom);			
			}
			for(int i = 0; i < NUM; i++){
				to = new TextView(getActivity());
				fillLayout("to", to, layoutTo);
			}
			for(int i = 0; i < NUM; i++){
				hours = new TextView(getActivity());
				fillLayout("hours", hours, layoutHours);
			}
			
			return rootView;
		}
		
		public void fillLayout(String data, TextView view, LinearLayout layout){
			view = new TextView(getActivity());
			view.setText(data);
			view.setTextSize(txtSize);
			view.setTextColor(color);
			view.setClickable(clickable);
			
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * TODO Change activity
					 */
					Intent intent = new Intent(getActivity(), MainActivity.class);	
					startActivity(intent);					
				}
			});
			
			layout.addView(view, LayoutParams.MATCH_PARENT);
		}
		
	}
	
	public static class DebtFragment extends Fragment{
		public DebtFragment(){}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState){
			
			return inflater.inflate(R.layout.fragment_add_debt, container, false);
		}
		
	}

}
