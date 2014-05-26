package com.example.workhours;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.User;
import com.example.workhours.fragments.ProfileFragment;
import com.example.workhours.util.InputValidator;

public class ProfileActivity extends FragmentActivity {

	private UserDAO dao;
	private TextView name, profile_email, employer_email, hourly_wage, tax;
	private TextView user_label, profile_email_label, employer_email_label,
			hourly_wage_label, tax_label;
	private EditText employer_email_value, hourly_wage_value, tax_number_value;
	private Button change;
	private String emp_email;
	private Double hour_wage, tax_value;
	private User user;
	private boolean updatedDetails;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		
		getFields();
		
		dao = new UserDAOImpl(this);
		dao.open();
		
		// Mja
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String existingAccount = prefs.getString("email", null);
	
		user = dao.getUser(existingAccount);
		
		//user = dao.getUser();
		if(user != null)
			fillForm();

	}

	public void changeDetails(View v) {
		showOnlyFragment();
		updatedDetails = false;

	}

	private void showOnlyFragment() {
		
		// Labels
		user_label.setVisibility(View.INVISIBLE);
		profile_email_label.setVisibility(View.INVISIBLE);
		employer_email_label.setVisibility(View.INVISIBLE);
		hourly_wage_label.setVisibility(View.INVISIBLE);
		tax_label.setVisibility(View.INVISIBLE);

		// Fields
		name.setVisibility(View.INVISIBLE);
		profile_email.setVisibility(View.INVISIBLE);
		employer_email.setVisibility(View.INVISIBLE);
		hourly_wage.setVisibility(View.INVISIBLE);
		tax.setVisibility(View.INVISIBLE);

		change.setVisibility(View.INVISIBLE);

		FragmentManager frag = getSupportFragmentManager();
		FragmentTransaction trans = frag.beginTransaction();
		ProfileFragment profile = (ProfileFragment) frag
				.findFragmentById(R.id.profile);
		
		employer_email_value.setText(user.getEmployerEmail());
		
		String s = Double.toString(user.getHourlyWage());
		hourly_wage_value.setText(s);
		
		s = Double.toString(user.getTax());
		tax_number_value.setText(s);
		
		trans.show(profile);
		trans.commit();

	}

	public void saveDetails(View v) {

		emp_email = employer_email_value.getText().toString();
		
		if (emp_email.matches("")) {
			
		} else if (InputValidator.email(emp_email) == false) {
			
			Toast.makeText(this, "Email not valid", Toast.LENGTH_SHORT).show();
			updatedDetails = false;
			
		} else {
			
			user.setEmployerEmail(emp_email);
			dao.updateUser(user);
			updatedDetails = true;
		}

		try {
			
			hour_wage = Double.parseDouble(hourly_wage_value.getText()
					.toString());
			user.setHourlyWage(hour_wage);
			updatedDetails = true;

		} catch (Exception e) {
			updatedDetails = false;
		}

		try {
			
			tax_value = Double.parseDouble(tax_number_value.getText()
					.toString());
			user.setTax(tax_value);
			updatedDetails = true;

		} catch (Exception e) {
			
			updatedDetails =false;
		}

		if (updatedDetails == true) {
			Toast.makeText(
					this,
					"Updated values are employer email: " + emp_email
							+ " hourly wage: " + hour_wage + " tax: "
							+ tax_value, Toast.LENGTH_SHORT).show();
			showOnlyDetails();

		}else{
			showOnlyFragment();
		}
	}

	public void cancelUpdate(View v) {
		showOnlyDetails();
	}

	private void showOnlyDetails() {
		
		// Labels
		Log.d("INSIDE SHOW ONLY DETAILS", " WOOOOOOOOOOP");
		user_label.setVisibility(View.VISIBLE);
		profile_email_label.setVisibility(View.VISIBLE);
		employer_email_label.setVisibility(View.VISIBLE);
		hourly_wage_label.setVisibility(View.VISIBLE);
		tax_label.setVisibility(View.VISIBLE);

		// Fields
		name.setVisibility(View.VISIBLE);
		profile_email.setVisibility(View.VISIBLE);
		employer_email.setVisibility(View.VISIBLE);
		hourly_wage.setVisibility(View.VISIBLE);
		tax.setVisibility(View.VISIBLE);
		change.setVisibility(View.VISIBLE);

		fillForm();

		FragmentManager frag = getSupportFragmentManager();
		FragmentTransaction trans = frag.beginTransaction();
		ProfileFragment profile = (ProfileFragment) frag
				.findFragmentById(R.id.profile);
		trans.hide(profile);
		trans.commit();

	}

	private void fillForm() {
		
		name.setText(user.getName());
		profile_email.setText(user.getEmail());
		employer_email.setText(user.getEmployerEmail());
		hourly_wage.setText(Double.toString(user.getHourlyWage()));
		tax.setText(Double.toString(user.getTax()));

		FragmentManager frag = getSupportFragmentManager();
		FragmentTransaction trans = frag.beginTransaction();
		ProfileFragment profile = (ProfileFragment) frag
				.findFragmentById(R.id.profile);
		trans.hide(profile);
		trans.commit();
	}
	
	private void getFields() {
		
		user_label = (TextView) findViewById(R.id.profileNameLabel);
		profile_email_label = (TextView) findViewById(R.id.profile_email_label);
		employer_email_label = (TextView) findViewById(R.id.employer_email_label);
		hourly_wage_label = (TextView) findViewById(R.id.hourly_wage_label);
		tax_label = (TextView) findViewById(R.id.tax_label);
		
		name = (TextView) findViewById(R.id.profile_name);
		profile_email = (TextView) findViewById(R.id.profile_email);
		employer_email = (TextView) findViewById(R.id.employer_email);
		hourly_wage = (TextView) findViewById(R.id.hourly_wage);
		tax = (TextView) findViewById(R.id.tax);
		
		change = (Button) findViewById(R.id.button_change_details);
		
		employer_email_value = (EditText) findViewById(R.id.change_employer_email);
		hourly_wage_value = (EditText) findViewById(R.id.change_hourly_wage);
		tax_number_value = (EditText) findViewById(R.id.change_tax);
	}

}
