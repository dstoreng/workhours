package com.example.workhours;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.joda.time.DateTime;
import com.example.workhours.entities.Calculations;
import com.example.workhours.entities.Shift;
import com.example.workhours.entities.User;
import com.example.workhours.fragments.ConfirmDialog;
import com.example.workhours.fragments.DatePickerFragment;
import com.example.workhours.util.EventAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
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
import android.widget.ListView;
import android.widget.TextView;
import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.facebook.Session;

public class MainActivity extends FragmentActivity {
	private UserDAO dao;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		dao = new UserDAOImpl(this);
		dao.open();
	}

	@Override
	protected void onResume() {
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case (R.id.action_profile):
			Log.d("item selected", R.id.action_profile + "");
			Intent profile = new Intent(this, ProfileActivity.class);
			startActivity(profile);
			break;
		case R.id.action_log_out:
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
			editor.commit();

			Session session = Session.getActiveSession();
			if (session != null)
				session.closeAndClearTokenInformation();

			Intent intent = new Intent(getBaseContext(),
					InitScreenActivity.class);
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
			switch (position) {
			case 0:
				fragment = new ShiftFragment();
				break;
			case 1:
				fragment = new EventFragment();
				break;
			case 2:
				fragment = new AllEventsFragment();
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

	/*
	 * This fragment displays a calendar
	 * The user can add new events or email existing 
	 * It also displays useful info (salary, schedule)
	 */
	public static class ShiftFragment extends Fragment {
		private long date;
		private ShiftDAO sdao;
		private UserDAO udao;
		private TextView salaryLastMonth, salaryNextMonth, scheduledHours;

		public ShiftFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
			return inflater.inflate(R.layout.fragment_add_shift, container, false);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState){
			super.onActivityCreated(savedInstanceState);
			
			CalendarView calendar = (CalendarView) getView().findViewById(R.id.calendarMain);
			date = calendar.getDate();
			calendar.setOnDateChangeListener(new OnDateChangeListener() {
				@Override
				public void onSelectedDayChange(CalendarView view, int year,
						int month, int dayOfMonth) {
					date = view.getDate();
				}
			});

			Button btn = (Button) getView().findViewById(R.id.selectDateButton);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							ShiftActivity.class);
					intent.putExtra("DATE", date);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(intent);
				}
			});

			Button email = (Button) getView().findViewById(R.id.sendEmailButton);
			email.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DialogFragment dateDialog = new DatePickerFragment();
					dateDialog.show(getActivity().getSupportFragmentManager(),
							"Choose");
				}
			});
		}

		@Override
		public void onResume() {
			super.onResume();
			
			salaryLastMonth = (TextView) getView().findViewById(R.id.salaryViewLast);
			salaryNextMonth = (TextView) getView().findViewById(R.id.salaryViewNext);
			scheduledHours = (TextView) getView().findViewById(R.id.scheduledHours);

			sdao = new ShiftDAOImpl(getActivity());
			udao = new UserDAOImpl(getActivity());

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			String uid = prefs.getString("email", null);

			// Get shifts
			sdao.open();
			List<Shift> shifts = sdao.getShifts();
			sdao.close();

			// Get user
			udao.open();
			User usr = udao.getUser(uid);
			udao.close();

			/*
			 * Calculate the salary using a help class
			 */
			Calculations cl = new Calculations(usr, shifts);
			double lmonth = cl.getPrevMonthSalary();
			double nmonth = cl.getNextMonthSalary();
			String shours = cl.getScheduledHours();

			salaryLastMonth.setText(Double.toString(lmonth));
			salaryNextMonth.setText(Double.toString(nmonth));
			scheduledHours.setText(shours);
		}
	}
	
	/*
	 * This fragment displays all upcoming shifts that are not confirmed
	 * aka schedule
	 */
	public static class EventFragment extends ListFragment {
		private EventAdapter adapter;
		private List<Shift> list, tmpList;
		private ShiftDAO shiftDao;
		private ListView rootView;
		
		public EventFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			super.onCreateView(inflater, container, savedInstanceState);
			
			//Set the listview which we will fill with layouts
			rootView = (ListView)inflater.inflate(R.layout.list_view, container, false);
			refreshContent();
			
			return rootView;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			TextView textViewItem = ((TextView) v.findViewById(R.id.tvFrom));
			String sId = textViewItem.getTag().toString();

			Intent intent = new Intent(getActivity(), ChangeShiftActivity.class);
			intent.putExtra("SHIFT_ID", Integer.parseInt(sId));
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
		
		public void refreshContent(){
			shiftDao = new ShiftDAOImpl(getActivity());
			shiftDao.open();
			tmpList = shiftDao.getShifts();
			shiftDao.close();
			
			list = new ArrayList<Shift>();
			// Get all shifts that are not confirmed == scheduled
			for (Shift t : tmpList) {
				if ((!t.isWorked()) && (t.getFrom().isAfter(DateTime.now()))) {
					list.add(t);
				}
			}		
			adapter = new EventAdapter(getActivity(), R.layout.event_layout, list, false);
			rootView.setAdapter(adapter);
		}
	}
	
	/*
	 * This fragment displays all shifts to the user
	 * It is possible to click an item to change the isWorked status.
	 */
	public static class AllEventsFragment extends ListFragment {
		private List<Shift> list;
		private ShiftDAO shiftDao;
		private EventAdapter adapter;
		private ListView v;

		public AllEventsFragment() {
		}
			
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
		{
			super.onCreateView(inflater, container, savedInstanceState);
			
			//Set the listview which we will fill with layouts
			v = (ListView) inflater.inflate(R.layout.list_view, container, false);
			refreshContent();
			
		    return v;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			TextView textViewItem = ((TextView) v.findViewById(R.id.tvFrom));
			String sId = textViewItem.getTag().toString();
			int ssId = Integer.parseInt(sId);
			
			DialogFragment dia = ConfirmDialog.newInstance(ssId);
			dia.show(getFragmentManager(), "Confirm");
			
			refreshContent();
		}
		
		public void refreshContent(){
			shiftDao = new ShiftDAOImpl(getActivity());
			shiftDao.open();
			list = shiftDao.getShifts();
			shiftDao.close();
			
			adapter = new EventAdapter(getActivity(), R.layout.event_layout, list, true);
			v.setAdapter(adapter);
		}
	}
	
}
