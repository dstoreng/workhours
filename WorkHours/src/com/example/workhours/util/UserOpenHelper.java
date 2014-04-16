package com.example.workhours.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserOpenHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_USER    = "user";
	
	public static final String COLUMN_NAME   = "name";
	public static final String COLUMN_EMAIL  = "email";
	public static final String COLUMN_PASS   = "passw_hash";
	public static final String COLUMN_EMPL   = "employer_email";
	public static final String COLUMN_WAGE   = "hourly_wage";
	public static final String COLUMN_TAX    = "tax";
	
	public static final String DATABASE_NAME = "user.db";
	public static final int DATABASE_VERSION = 2;
	
	public static final String DATABASE_CREATE = "create table "

			+ TABLE_USER + "("
			+ COLUMN_NAME + " text, "
			+ COLUMN_EMAIL + " text, "
			+ COLUMN_PASS + " text, "
			+ COLUMN_EMPL + " text, "
			+ COLUMN_WAGE + " real, "
			+ COLUMN_TAX + " real"
			+ ");";
	
    public UserOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        
    	db.execSQL(DATABASE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Log.w(UserOpenHelper.class.getName(), "Upgrading database from version" 
		+ oldVersion + " to " + newVersion + ", all data will be wiped");
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
	    onCreate(db);
	}

}
