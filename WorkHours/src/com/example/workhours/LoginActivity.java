package com.example.workhours;

import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.User;
import com.example.workhours.util.InputValidator;



import com.example.workhours.util.PasswordHash;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private EditText fieldEmail, fieldPassword;
	private String email, password;
	private static User user = null;
	private UserDAO dao;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		dao = new UserDAOImpl(this);
		getFields();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	//User clicks button for loggin in
	public void login(View v) {
		
		email    = fieldEmail.getText().toString();
		password = fieldPassword.getText().toString();
		
		//Input validation 
		if(email.matches("") || password.matches(""))
			Toast.makeText(this, "Enter Email address or password", Toast.LENGTH_SHORT).show();
		
		else if(InputValidator.email(email) == false)
			Toast.makeText(this, "Email not valid", Toast.LENGTH_SHORT).show();
		else 
			user = new User("", email, password);
		
		if(user != null) {
			
			try {
				
				dao.open();
				user = dao.getUser(email, PasswordHash.hash(password));
				
				//Stores username in shared preferences 
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				SharedPreferences.Editor editor = preferences.edit(); 
		
				editor.putString("email", user.getEmail());
				editor.commit();
				
				intent = new Intent(v.getContext(), MainActivity.class);
				
			} catch(Exception e) {
				
				Log.d("SQL Exception", e.getMessage());
				Toast.makeText(this, "The password or email address you entered is incorrect. Please try again (make sure your caps lock is off).", Toast.LENGTH_LONG).show();
				user = null;
				intent = new Intent(v.getContext(), LoginActivity.class);
			
			} finally {
				
				startActivity(intent);	
			}
		}
	}
	
	public void getFields() {
		
		fieldEmail     = (EditText) findViewById(R.id.login_email);
		fieldPassword  = (EditText) findViewById(R.id.login_password);
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
