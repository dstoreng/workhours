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
	private TextView name, profile_email, employer_email, hourly_wage, tax;
	private TextView user_label, profile_email_label, employer_email_label, hourly_wage_label, tax_label;
	private Button change;
	private String emp_email; 
	private Double hour_wage, tax_value;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Entered", "Profileactivity");
		setContentView(R.layout.activity_profile);
		
		dao = new UserDAOImpl(this);
		dao.open();
		User user = dao.getUser();
		
		fillForm(user);
		
	}
	
	public void changeDetails(View v){
		showOnlyFragment();
		
	}
	
	private void showOnlyFragment(){
		//Labels
		user_label = (TextView) findViewById(R.id.profileNameLabel);
		user_label.setVisibility(View.INVISIBLE);
		
		profile_email_label = (TextView) findViewById(R.id.profile_email_label);
		profile_email_label.setVisibility(View.INVISIBLE);
	
		employer_email_label = (TextView) findViewById(R.id.employer_email_label);
		employer_email_label.setVisibility(View.INVISIBLE);
		
		hourly_wage_label = (TextView) findViewById(R.id.hourly_wage_label);
		hourly_wage_label.setVisibility(View.INVISIBLE);
		
		tax_label = (TextView) findViewById(R.id.tax_label);
		tax_label.setVisibility(View.INVISIBLE);
		
		//Fields
		name = (TextView) findViewById(R.id.profile_name);
		name.setVisibility(View.INVISIBLE);
		
		profile_email = (TextView) findViewById(R.id.profile_email);
		profile_email.setVisibility(View.INVISIBLE);
		
		employer_email = (TextView) findViewById(R.id.employer_email);
		employer_email.setVisibility(View.INVISIBLE);
		
		hourly_wage = (TextView) findViewById(R.id.hourly_wage);
		hourly_wage.setVisibility(View.INVISIBLE);
		
		tax = (TextView) findViewById(R.id.tax);
		tax.setVisibility(View.INVISIBLE);
		
		change = (Button) findViewById(R.id.button_change_details);
		change.setVisibility(View.INVISIBLE);
		
		FragmentManager frag = getSupportFragmentManager();
		FragmentTransaction trans = frag.beginTransaction();
		ProfileFragment profile = (ProfileFragment) frag.findFragmentById(R.id.profile);
		trans.show(profile);
		trans.commit();
		
	}
	
	public void saveDetails(View v){
		//Oppdaterer IKKE databasen enda
		/*employer_email = (TextView) findViewById(R.id.employer_email);
		emp_email = employer_email.getText().toString();
		
		hourly_wage = (TextView) findViewById(R.id.hourly_wage);
		hour_wage = Double.parseDouble(hourly_wage.getText().toString());
		
		tax = (TextView) findViewById(R.id.tax);
		tax_value = Double.parseDouble(tax.getText().toString());*/
		
		Log.d("FINISH UPDATING VALUES", " WOOOOOOOOOOP");
		showOnlyDetails();		
	}
	
	private void showOnlyDetails(){
		//Labels
		Log.d("INSIDE SHOW ONLY DETAILS", " WOOOOOOOOOOP");
		user_label = (TextView) findViewById(R.id.profileNameLabel);
		user_label.setVisibility(View.VISIBLE);
		
		profile_email_label = (TextView) findViewById(R.id.profile_email_label);
		profile_email_label.setVisibility(View.VISIBLE);
		
		employer_email_label = (TextView) findViewById(R.id.employer_email_label);
		employer_email_label.setVisibility(View.VISIBLE);
		
		hourly_wage_label = (TextView) findViewById(R.id.hourly_wage_label);
		hourly_wage_label.setVisibility(View.VISIBLE);
		
		tax_label = (TextView) findViewById(R.id.tax_label);
		tax_label.setVisibility(View.VISIBLE);
		
		//Fields
		name = (TextView) findViewById(R.id.profile_name);
		name.setVisibility(View.VISIBLE);
		
		profile_email = (TextView) findViewById(R.id.profile_email);
		profile_email.setVisibility(View.VISIBLE);
		
		employer_email = (TextView) findViewById(R.id.employer_email);
		employer_email.setVisibility(View.VISIBLE);
		
		hourly_wage = (TextView) findViewById(R.id.hourly_wage);
		hourly_wage.setVisibility(View.VISIBLE);
		
		tax = (TextView) findViewById(R.id.tax);
		tax.setVisibility(View.VISIBLE);
		
		change = (Button) findViewById(R.id.button_change_details);
		change.setVisibility(View.VISIBLE);
		
		FragmentManager frag = getSupportFragmentManager();
		FragmentTransaction trans = frag.beginTransaction();
		ProfileFragment profile = (ProfileFragment) frag.findFragmentById(R.id.profile);
		trans.hide(profile);
		trans.commit();
	}
	
	
	private void fillForm(User user){
		name = (TextView) findViewById(R.id.profile_name);
		name.setText(user.getName());
		
		profile_email = (TextView) findViewById(R.id.profile_email);
		profile_email.setText(user.getEmail());
		
		employer_email = (TextView) findViewById(R.id.employer_email);
		employer_email.setText("employeremail@email.com");
		//employer_email.setText(user.getEmployerEmail());	
		
		hourly_wage = (TextView) findViewById(R.id.hourly_wage);
		hourly_wage.setText("100.0");
		//hourly_wage.setText(Double.toString(user.getHourlyWage()));
		
		tax = (TextView) findViewById(R.id.tax);
		tax.setText("25");
		//tax.setText(Double.toString(user.getTax()));
		
		FragmentManager frag = getSupportFragmentManager();
		FragmentTransaction trans = frag.beginTransaction();
		ProfileFragment profile = (ProfileFragment) frag.findFragmentById(R.id.profile);
		trans.hide(profile);
		trans.commit();
	}
}

