package com.example.workhours.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ShiftOpenHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_SHIFT    = "shift";
	
	public static final String COLUMN_SHIFT_ID     = "sID";
	public static final String COLUMN_USER_ID      = "uID";
	public static final String COLUMN_T_FROM       = "timeFrom";
	public static final String COLUMN_T_TO         = "timeTo";
	public static final String COLUMN_NOTIFY       = "isNotify";
	public static final String COLUMN_REPEAT       = "isRepeat";
	public static final String COLUMN_M_REPEAT     = "weeklyRepeat";
	public static final String COLUMN_W_REPEAT     = "monthlyRepeat";
	public static final String COLUMN_WORKED       = "isWorked";
	public static final String COLUMN_REPEAT_COUNT = "repeating";
	
	public static final String DATABASE_NAME = "shift.db";
	public static final int DATABASE_VERSION = 6;
	
	public static final String DATABASE_CREATE = "create table "

			+ TABLE_SHIFT + "("
			+ COLUMN_SHIFT_ID       + " INTEGER, "
			+ COLUMN_USER_ID        + " text, "
			+ COLUMN_T_FROM         + " text, "
			+ COLUMN_T_TO           + " text, "
			+ COLUMN_NOTIFY         + " INTEGER, "
			+ COLUMN_REPEAT         + " INTEGER, "
			+ COLUMN_W_REPEAT       + " INTEGER, "
			+ COLUMN_M_REPEAT       + " INTEGER, "
			+ COLUMN_WORKED         + " INTEGER, "
			+ COLUMN_REPEAT_COUNT   + " INTEGER"
			+ ");";
	
	public ShiftOpenHelper(Context context) {
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
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIFT);
	    onCreate(db);
	}
}
