package com.example.workhours;

import java.util.Locale;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.Locale;

import com.example.workhours.entities.Shift;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;

import com.example.workhours.entities.CalendarDAO;
import com.example.workhours.entities.CalendarDAOImpl;
import com.example.workhours.entities.SharedPrefs;
import com.example.workhours.entities.Shift;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.SharedPrefs;
import com.facebook.Session;

public class MainActivity extends FragmentActivity {
	public double amountOfHours;
	private UserDAO dao;

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	CalendarView calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("Entered", "MainActivity");
		
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				this, getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		dao = new UserDAOImpl(this);
		dao.open();
		/*
		User user = dao.getUser();
		
		if(user != null)
			Log.d("User account successfully created", user.toString());
		else
			Log.d("User creation failed", "OMG");
			*/
	}
	
	
	@Override
	protected void onResume(){
		super.onResume();
		
		Intent shiftData = getIntent();
		dao.open();
		
		try{
			String intentHours = (String) shiftData.getSerializableExtra("HOURS");
			SharedPrefs prefs = new SharedPrefs(this);
			amountOfHours = Long.parseLong(intentHours);
			prefs.addHours(amountOfHours);
			
			Log.d("HOURS!!", intentHours);
			Toast.makeText(getApplicationContext(), "Hours: " + intentHours, Toast.LENGTH_LONG).show();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	  protected void onPause() {
	    dao.close();
	    super.onPause();
	  }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		public SectionsPagerAdapter(MainActivity m, FragmentManager fm) {
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
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(intent);
				}
			});			
	    }
	}
	
	public static class EventFragment extends Fragment{
		private List<Shift> list;
		private CalendarDAO calDao;
		private int txtSize = 16;
		private int color = Color.BLACK;
		private boolean clickable = true;
		private String spacing = "				";	
		public EventFragment(){
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){	
	
			View rootView = inflater.inflate(R.layout.fragment_view_events, container, false);
			LinearLayout llayout = (LinearLayout) rootView.findViewById(R.id.eventContainer);
			
			calDao = new CalendarDAOImpl(rootView.getContext().getContentResolver());
			list = calDao.getAddedEvents();
			TextView txtView;		
			
			
			int NUM = list.size();
			for(int i = 0; i < NUM; i++){
				txtView = new TextView(getActivity());
				fillLayout(list.get(i).getFromFormatted() + spacing +
						list.get(i).getToFormatted() + spacing + 
						list.get(i).getHours(), list.get(i), txtView, llayout, rootView.getContext());			
			}
			
			return rootView;
		}
		
		public void fillLayout(String data, Shift tmpShift, TextView view, LinearLayout layout, Context contx){
			view = new TextView(getActivity());
			
			/*
			 * 
			 */
			view.setId(tmpShift.getId());
			view.setText(data);
			
			Log.d("FILL LAYOUT", data);
			Log.d("FILL LAYOUT ID", tmpShift.getId() + "");
			
			view.setTextSize(txtSize);
			view.setTextColor(color);
			view.setClickable(clickable);
			
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					TextView obj = (TextView) v;
					String objString = obj.getText().toString();
					int objId = obj.getId();
					
					Log.d("OBJECT LISTENER", objString);
					Log.d("OBJECT LISTENER", objId + "");
									
					/*
					 * Send Shift id to the ChangeActivity class
					 */
					Intent intent = new Intent(getActivity(), ChangeShiftActivity.class);
					intent.putExtra("OBJECT_ID", objId);
					intent.putExtra("OBJECT_STRING", objString);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(intent);
				}
			});
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layout.addView(view, lp);		
		}
	}
	
	public static class DebtFragment extends Fragment{
		public DebtFragment(){}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState){
		
			return inflater.inflate(R.layout.fragment_add_debt, container, false);
		}
		
		@Override
	    public void onActivityCreated(Bundle savedInstanceState)
	    {
	        super.onActivityCreated(savedInstanceState);

			TextView hourText = (TextView) getView().findViewById(R.id.displayHours);
	        SharedPrefs prefs = new SharedPrefs(getActivity());
	        double scheduledHours = prefs.getHours();
	        Log.d("HOURS SCHEDULED", Double.toString(scheduledHours));
	        hourText.append(" -- " + scheduledHours);
	    }

		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(R.id.action_log_out == item.getItemId()) {
			
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
			
			Session session = Session.getActiveSession();
			if(session != null)
				session.closeAndClearTokenInformation();
			
			Intent intent = new Intent(getBaseContext(), InitScreenActivity.class);
			startActivity(intent);
			
			return true;
			
		}else {
			
			return super.onOptionsItemSelected(item);
		}
		
		
	}

}
