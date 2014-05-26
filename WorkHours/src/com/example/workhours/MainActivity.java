package com.example.workhours;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import com.example.workhours.entities.Calculations;
import com.example.workhours.entities.Shift;
import com.example.workhours.entities.User;
import com.example.workhours.util.EmailService;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
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
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
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
		
		dao.open();
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case (R.id.action_profile):
			Log.d("item selected", R.id.action_profile + "");
			Intent profile = new Intent(this, ProfileActivity.class);
			startActivity(profile);
			break;
		case R.id.action_log_out:
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
		
			Session session = Session.getActiveSession();
			if(session != null)
				session.closeAndClearTokenInformation();
		
			Intent intent = new Intent(getBaseContext(), InitScreenActivity.class);
			startActivity(intent);
			break;
		
		default:
			return super.onOptionsItemSelected(item);
		}
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
	
	public static class ShiftFragment extends Fragment{
		private long date;
		private ShiftDAO sdao;
		private UserDAO udao;
		private TextView salaryLastMonth, salaryNextMonth;
		
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
			
			Button email = (Button) getView().findViewById(R.id.sendEmailButton);
			email.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					// Get user email so that we can retrieve user settings in email manager
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
							getActivity().getApplicationContext());
					Intent i = new Intent(getActivity(), EmailService.class);
					i.putExtra("USER_EMAIL", prefs.getString("email", ""));
					v.getContext().
					startService(i);
				}
			});
	    }
		
		@Override
		public void onResume(){
			super.onResume();
			salaryLastMonth = (TextView) getView().findViewById(R.id.salaryViewLast);
			salaryNextMonth = (TextView) getView().findViewById(R.id.salaryViewNext);
			
			sdao = new ShiftDAOImpl(getActivity());
			udao = new UserDAOImpl(getActivity());
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			String uid = prefs.getString("email", null);
			
			//Get shifts
			sdao.open();
			List<Shift> shifts = sdao.getShifts();
			sdao.close();
			
			//Get user
			udao.open();
			User usr = udao.getUser(uid);
			udao.close();
			
			/*
			 * Calculate the salary using a help class
			 */
			Calculations cl = new Calculations(usr, shifts);
			double lmonth = cl.getPrevMonthSalary();
			double nmonth = cl.getNextMonthSalary();
			
			salaryLastMonth.setText(Double.toString(lmonth));
			salaryNextMonth.setText(Double.toString(nmonth));
			
		}
		
	}
	
	public static class EventFragment extends Fragment{
		private List<Shift> list, tmpList;
		private ShiftDAO shiftDao;
		private TextView txtView, hourText;
		private View rootView;
		private LinearLayout mainLayout;
		private double scheduledHours;
		private int txtSize = 19;
		private boolean clickable = true;
		private String spacing = "				";	
		public EventFragment(){
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){	
			rootView = inflater.inflate(R.layout.fragment_view_events, container, false);
			mainLayout = (LinearLayout) rootView.findViewById(R.id.eventContainer);
			hourText = (TextView)rootView.findViewById(R.id.displayHours);
			
			/*
			shiftDao = new ShiftDAOImpl(getActivity().getApplicationContext());
			shiftDao.open();
			tmpList = shiftDao.getShifts();
			shiftDao.close();
			
			list = new ArrayList<Shift>();
			scheduledHours = 0;
			//Get all shifts that are not confirmed == scheduled
			for(Shift t : tmpList){
				if( (!t.isWorked()) && (t.getFrom().isAfter(DateTime.now())) ){
					list.add(t);
					scheduledHours += t.getHours();
				}
			}		
			//Update layout with the shifts
			refreshView();*/
			
			return rootView;
		}
		
		public void refreshView(){
			int NUM = list.size();
			int colorTeal = Color.parseColor("#33B5E5");
			int colorWhite = Color.parseColor("#111111");
			int color;
			for(int i = 0; i < NUM; i++){
				if(i%2 == 0)
					color = colorTeal;
				else
					color = colorWhite;
				txtView = new TextView(getActivity());
				fillLayout(true, list.get(i).getFromFormatted() + spacing +
						list.get(i).getToFormatted() + spacing,
						list.get(i), txtView, mainLayout, rootView.getContext(), color);			
			}
			
			hourText.setText("Scheduled Hours: " +scheduledHours);
		}
		
		public void fillLayout(boolean mainView, String data, Shift tmpShift, TextView view, LinearLayout layout, Context contx, int color){
			view = new TextView(getActivity());
			view.setId(tmpShift.getId());
			view.setBackgroundColor(color);
			view.setText(data);
			view.setTextSize(txtSize);
			view.setTextColor(Color.WHITE);
			view.setClickable(clickable);
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					TextView obj = (TextView) v;
					int objId = obj.getId();
									
					/*
					 * Send Shift id to the Shift activity class
					 */
					Intent intent = new Intent(getActivity(), ChangeShiftActivity.class);
					intent.putExtra("SHIFT_ID", objId);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(intent);
				}
			});
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layout.addView(view, lp);		
		}
		
		@Override
		public void onResume(){
			super.onResume();
			
			mainLayout.removeAllViews();
			
			shiftDao = new ShiftDAOImpl(getActivity().getApplicationContext());
			shiftDao.open();
			tmpList = shiftDao.getShifts();
			shiftDao.close();
			
			list = new ArrayList<Shift>();
			scheduledHours = 0;
			//Get all shifts that are not confirmed == scheduled
			for(Shift t : tmpList){
				if( (!t.isWorked()) && (t.getFrom().isAfter(DateTime.now())) ){
					list.add(t);
					scheduledHours += t.getHours();
				}
			}
			refreshView();
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

			
	    }

		
	}

}
