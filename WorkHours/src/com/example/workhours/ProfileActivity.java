package com.example.workhours;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.Notifier;
import com.example.workhours.entities.User;
import com.example.workhours.fragments.ProfileFragment;
import com.example.workhours.fragments.ProfileFragmentDetails;
import com.example.workhours.util.DetailsAdapter;
import com.example.workhours.util.InputValidator;

public class ProfileActivity extends FragmentActivity {

	private UserDAO dao;
	private TextView due;
	private EditText employer_email_value, hourly_wage_value;
	private ListView listView;
	private Switch sw;
	private String emp_email;
	private Double hour_wage;
	private User user;
	private boolean updatedDetails;
	private SeekBar dueDateSelector;
	public static int dateDue;
	public static String payment_M;
	
	private DetailsAdapter adapter;
	
	private List<User> users;
	
	private FragmentManager frag;
	private FragmentTransaction trans;
	private ProfileFragment profile;
	private ProfileFragmentDetails profile_d;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		frag = getSupportFragmentManager();
		getFields();
		
		dao = new UserDAOImpl(this);
		dao.open();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String user_email = prefs.getString("email", null);
	
		user = dao.getUser(user_email);
		
		users = new ArrayList<User>(6);
		users.add(user);
		
		//BUGGY
		users.add(null);
		users.add(null);
		users.add(null);
		users.add(null);
		users.add(null);
		
		adapter = new DetailsAdapter(this, R.layout.details_layout, users);
		
		listView = (ListView) findViewById(R.id.listView_details);
		listView.setAdapter(adapter);

		if(user != null)
			fillForm();
	}

	public void changeDetails(View v) {
		
		showChangeDetails();
		updatedDetails = false;
	}
	
	/**
	 * Hides the fragment containing the user details
	 * and brings up the fragment for changing the details
	 * */
	private void showChangeDetails() {
		
		trans = frag.beginTransaction();
		trans.hide(profile_d);
		
		employer_email_value.setText(user.getEmployerEmail());
		
		String s = Double.toString(user.getHourlyWage());
		hourly_wage_value.setText(s);
		hour_wage = 0.0;

		dueDateSelector.setProgress(user.getScheduleDue());
		due.setText("Day of the month: " + Integer.toString(user.getScheduleDue()));
		dateDue = user.getScheduleDue();
		
		if(user.getPerPay().equals("weekly")) {
			
			sw.setChecked(true);
			
		} else if (user.getPerPay().equals("monthly")) {
			
			sw.setChecked(false);
			
		}
		
		trans.show(profile);
		trans.commit();
	}

	public void saveDetails(View v) {
		
		if(dateDue == 0) {
			
			Toast.makeText(this, "Due date can't be 0", Toast.LENGTH_SHORT).show();
			updatedDetails = false;
		}
		
		else if(dateDue != user.getScheduleDue()) {
			
			user.setDueDate(dateDue);
			dao.updateUser(user);
			updatedDetails = true;
			
		}
		
		if(!payment_M.equals(user.getPerPay())) {
			
			user.setPerPay(payment_M);
			dao.updateUser(user);
			updatedDetails = true;
		}

		try {
			
			hour_wage = Double.parseDouble(hourly_wage_value.getText()
					.toString());
			if(user.getHourlyWage() != hour_wage) {
				
				user.setHourlyWage(hour_wage);
				dao.updateUser(user);
				updatedDetails = true;
			} 

		} catch (Exception e) {
			Toast.makeText(this, "Invalid value", Toast.LENGTH_LONG).show();
			updatedDetails = false;
		}
		
		emp_email = employer_email_value.getText().toString();
		
		if (emp_email.matches("")) {
			
		} else if (InputValidator.email(emp_email) == false) {
			
			Toast.makeText(this, "Email not valid", Toast.LENGTH_SHORT).show();
			updatedDetails = false;
			
		} else {
			
			if(!user.getEmployerEmail().equals(emp_email)) {
				
				user.setEmployerEmail(emp_email);
				dao.updateUser(user);
				updatedDetails = true;
			} 
		}

		if (updatedDetails == true) {
			showDetails();
		/**
		 * Non of the details have been modified
		 * */
		} else if(user.getEmployerEmail().equals(emp_email) && user.getHourlyWage() == hour_wage 
				&& user.getScheduleDue() == dateDue && user.getPerPay().equals(payment_M)) {
			
			showDetails();
		
		} else{
			
			showChangeDetails();
		}
		
		Notifier n = new Notifier(getApplicationContext(), null, user);
		n.dueDateNotify();
	}

	public void cancelUpdate(View v) {
		showDetails();
	}

	private void showDetails() {
		
		fillForm();
		
		trans = frag.beginTransaction();
		trans.hide(profile);
		trans.commit();
	}

	private void fillForm() {
		
		users.remove(0);
		users.add(0, user);
		DetailsAdapter.user = users;
		
		adapter.notifyDataSetChanged();

		trans = frag.beginTransaction();
		trans.hide(profile);
		trans.show(profile_d);
		trans.commit();
	}
	
	private void getFields() {
		
		employer_email_value = (EditText) findViewById(R.id.change_employer_email);
		hourly_wage_value = (EditText) findViewById(R.id.change_hourly_wage);
		profile = (ProfileFragment) frag.findFragmentById(R.id.profile);
		profile_d = (ProfileFragmentDetails) frag.findFragmentById(R.id.profile_details);
		dueDateSelector = (SeekBar) findViewById(R.id.due_day);
		sw = (Switch) findViewById(R.id.payment_mode);
		due = (TextView) findViewById(R.id.due_date_displayer);
	}
	
	  @Override
	  protected void onResume() {
	    dao.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    dao.close();
	    super.onPause();
	  }
}
