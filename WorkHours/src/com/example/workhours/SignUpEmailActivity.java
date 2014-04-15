package com.example.workhours;

import com.example.workhours.entities.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpEmailActivity extends Activity {
	
	private EditText fieldUsername, fieldEmail, fieldPassword, fieldPassword2;
	private String username, email, password, password2;
	private static User user;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sign_up_email);
		
		getFields();
	}
	//User clicks button to submit form
	public void login(View v) {
		
		username  = fieldUsername.getText().toString();
		email     = fieldEmail.getText().toString();
		password  = fieldPassword.getText().toString();
		password2 = fieldPassword2.getText().toString();
		
		if(username.matches("") && email.matches("") && password.matches("") && password2.matches(""))
			Toast.makeText(this, "Enter data in fields", Toast.LENGTH_SHORT).show();
		
		else if(username.matches("") || email.matches(""))
			Toast.makeText(this, "Enter username or email address", Toast.LENGTH_SHORT).show();
		
		else if(password.matches("") || password2.matches(""))
			Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
		
		else if(!password.equals(password2))
			Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
		
		else 
			user = new User(username, email, password);
		
		Intent intent = new Intent(v.getContext(), SignUpOptionsActivity.class);
		startActivity(intent);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.sign_up_email, menu);
		return true;
	}
	
	public void getFields() {
		
		fieldUsername  = (EditText) findViewById(R.id.name);
		fieldEmail     = (EditText) findViewById(R.id.email_address);
		fieldPassword   = (EditText) findViewById(R.id.password);
		fieldPassword2 = (EditText) findViewById(R.id.password2);
	}

}
