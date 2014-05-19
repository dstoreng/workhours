package com.example.workhours;

import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.User;
import com.example.workhours.util.InputValidator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpEmailActivity extends Activity {
	
	private EditText fieldUsername, fieldEmail, fieldPassword, fieldPassword2;
	private String username, email, password, password2;
	private static User user = null;
	private UserDAO dao;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sign_up_email);
		dao = new UserDAOImpl(this);
		getFields();
	}
	
	//User clicks button to submit form
	public void signUp(View v) {
		
		username  = fieldUsername.getText().toString();
		email     = fieldEmail.getText().toString();
		password  = fieldPassword.getText().toString();
		password2 = fieldPassword2.getText().toString();
		
		//Input validation
		if(username.matches("") && email.matches("") && password.matches("") && password2.matches(""))
			Toast.makeText(this, "Enter data in fields", Toast.LENGTH_SHORT).show();
		
		else if(username.matches("") || email.matches(""))
			Toast.makeText(this, "Enter username or email address", Toast.LENGTH_SHORT).show();
		
		else if(InputValidator.email(email) == false)
			Toast.makeText(this, "Email not valid", Toast.LENGTH_SHORT).show();
		
		else if(password.matches("") || password2.matches(""))
			Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
		
		else if(!password.equals(password2))
			Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
		
		else if(InputValidator.passwordStrength(password) == false)
			Toast.makeText(this, "Password should have more than 8 characters and at least one digit", Toast.LENGTH_SHORT).show();
		
		else 
			user = new User(username, email, password);
		
		if(user != null) {
			
			Toast.makeText(this, "Hello " + user.getName(), Toast.LENGTH_SHORT).show();
			
			//Stores username in shared preferences 
		//	SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = preferences.edit(); 
	
			editor.putString("email", user.getEmail());
			editor.commit();
			
			dao.open();
			dao.addUser(user);
			
			Intent intent = new Intent(v.getContext(), MainActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.sign_up_email, menu);
		return true;
	}
	
	public void getFields() {
		
		fieldUsername  = (EditText) findViewById(R.id.name);
		fieldEmail     = (EditText) findViewById(R.id.email_address);
		fieldPassword  = (EditText) findViewById(R.id.password);
		fieldPassword2 = (EditText) findViewById(R.id.password2);
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
