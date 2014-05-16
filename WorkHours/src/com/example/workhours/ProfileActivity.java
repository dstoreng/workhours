package com.example.workhours;

import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.User;
import com.example.workhours.fragments.ProfileFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;



public class ProfileActivity extends FragmentActivity {
	
	private UserDAO dao;
	private TextView name, email, employer_email, hourly_wage, tax, profile_email;
	private TextView user_label, email_label, hourly_wage_label, tax_label;
	private Button change;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Entered", "Profileactivity");
		setContentView(R.layout.activity_profile);
		
		dao = new UserDAOImpl(this);
		dao.open();
		User user = dao.getUser();
		
		name = (TextView) findViewById(R.id.profile_name);
		name.setText(user.getName());
		
		email = (TextView) findViewById(R.id.profile_email);
		email.setText(user.getEmail());
		
		employer_email = (TextView) findViewById(R.id.employer_email);
		employer_email.setText(user.getEmployerEmail());	
		
		hourly_wage = (TextView) findViewById(R.id.hourly_wage);
		hourly_wage.setText(Double.toString(user.getHourlyWage()));
		
		tax = (TextView) findViewById(R.id.tax);
		tax.setText(Double.toString(user.getTax()));
		
		FragmentManager frag = getSupportFragmentManager();
		FragmentTransaction trans = frag.beginTransaction();
		ProfileFragment profile = (ProfileFragment) frag.findFragmentById(R.id.profile);
		trans.hide(profile);
		trans.commit();
		
		
	}
	
	
	
	public void changeDetails(View v){
		showOnlyFragment();
		
	}
	
	private void showOnlyFragment(){
		//Labels
		user_label = (TextView) findViewById(R.id.profileNameLabel);
		user_label.setVisibility(View.INVISIBLE);
		
		profile_email = (TextView) findViewById(R.id.profile_email_label);
		profile_email.setVisibility(View.INVISIBLE);
	
		email_label = (TextView) findViewById(R.id.employer_email_label);
		email_label.setVisibility(View.INVISIBLE);
		
		hourly_wage_label = (TextView) findViewById(R.id.hourly_wage_label);
		hourly_wage_label.setVisibility(View.INVISIBLE);
		
		tax_label = (TextView) findViewById(R.id.tax_label);
		tax_label.setVisibility(View.INVISIBLE);
		
		tax_label = (TextView) findViewById(R.id.tax_label);
		tax_label.setVisibility(View.INVISIBLE);
		
		change = (Button) findViewById(R.id.button_change_details);
		change.setVisibility(View.INVISIBLE);
		
		//Fields
		name = (TextView) findViewById(R.id.profile_name);
		name.setVisibility(View.INVISIBLE);
		
		email = (TextView) findViewById(R.id.profile_email);
		email.setVisibility(View.INVISIBLE);
		
		employer_email = (TextView) findViewById(R.id.employer_email);
		employer_email.setVisibility(View.INVISIBLE);
		
		hourly_wage = (TextView) findViewById(R.id.hourly_wage);
		hourly_wage.setVisibility(View.INVISIBLE);
		
		tax = (TextView) findViewById(R.id.tax);
		tax.setVisibility(View.INVISIBLE);
		
		
		FragmentManager frag = getSupportFragmentManager();
		FragmentTransaction trans = frag.beginTransaction();
		ProfileFragment profile = (ProfileFragment) frag.findFragmentById(R.id.profile);
		trans.show(profile);
		trans.commit();
		
		
	}
	
}

