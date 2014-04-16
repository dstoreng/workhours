package com.example.workhours.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.workhours.entities.User;
import com.example.workhours.util.UserOpenHelper;

public class UserDAOImpl implements UserDAO {
	
	private SQLiteDatabase database;
	private UserOpenHelper dbHelper;
	private String[] allColumns =
		{
			UserOpenHelper.COLUMN_NAME,
			UserOpenHelper.COLUMN_EMAIL,
			UserOpenHelper.COLUMN_PASS,
			UserOpenHelper.COLUMN_EMPL,
			UserOpenHelper.COLUMN_WAGE,
			UserOpenHelper.COLUMN_TAX
		};
	
	public UserDAOImpl(Context context) {
		
		dbHelper = new UserOpenHelper(context);
	}
	
	@Override
	public void open() throws SQLException {
	    
		database = dbHelper.getWritableDatabase();
	  }
	
	 public void close() {
	
		 dbHelper.close();
	}


	@Override
	public void addUser(User user) {
		
		Log.d("Add User", user.toString()); 
		
		ContentValues values = new ContentValues();
		values.put(UserOpenHelper.COLUMN_NAME, user.getName());
		values.put(UserOpenHelper.COLUMN_EMAIL, user.getEmail());
		values.put(UserOpenHelper.COLUMN_PASS, user.getPassHash());
		values.put(UserOpenHelper.COLUMN_EMPL, user.getEmployerEmail());
		values.put(UserOpenHelper.COLUMN_WAGE, user.getHourlyWage());
		values.put(UserOpenHelper.COLUMN_TAX, user.getTax());
		
		database.insert(UserOpenHelper.TABLE_USER, null, values);

	}

	@Override
	public void updateUser(User user) {
		

	}

	@Override
	public User getUser() {
		
		User user;
		
		Cursor cursor = database.query(UserOpenHelper.TABLE_USER,
		        allColumns, null, null, null, null, null);
		
		if (cursor != null)
	        cursor.moveToFirst();			
		
		
		user = cursorToUser(cursor);
		
		if(cursor != null && !cursor.isClosed())
		    cursor.close();
		
		return user;
	}
	
	@Override
	public void dropUser() {
		
		database.delete(UserOpenHelper.TABLE_USER, null, null);
	}
	
	private User cursorToUser(Cursor cursor) {
		
		User user = new User();
		
		user.setName(cursor.getString(0));
		user.setEmail(cursor.getString(1));
		user.setPassHash(cursor.getString(2));
		user.setEmployerEmail(cursor.getString(3));
		user.setHourlyWage(cursor.getDouble(4));
		user.setTax(cursor.getDouble(5));
		
		return user;
	}

}
