package com.example.workhours.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.workhours.entities.Shift;
import com.example.workhours.util.ShiftOpenHelper;

public class ShiftDAOImpl implements ShiftDAO {
	
	private SQLiteDatabase database;
	private List<Shift> shifts = new ArrayList<Shift>();
	private ShiftOpenHelper dbHelper;
	
	private String[] allColumns = 
		{
			ShiftOpenHelper.COLUMN_SHIFT_ID,
			ShiftOpenHelper.COLUMN_USER_ID,
			ShiftOpenHelper.COLUMN_T_FROM,
			ShiftOpenHelper.COLUMN_T_TO,
			ShiftOpenHelper.COLUMN_NOTIFY,
			ShiftOpenHelper.COLUMN_REPEAT,
			ShiftOpenHelper.COLUMN_W_REPEAT,
			ShiftOpenHelper.COLUMN_M_REPEAT
		};
	
	public ShiftDAOImpl(Context context) {
		
		dbHelper = new ShiftOpenHelper(context);
	}
	
	@Override
	public void open() throws SQLException {
		
		database = dbHelper.getWritableDatabase();
	}

	@Override
	public void addShift(Shift shift) {
		
		ContentValues values = new ContentValues();
		
		values.put(ShiftOpenHelper.COLUMN_SHIFT_ID, shift.getId());
		values.put(ShiftOpenHelper.COLUMN_USER_ID, shift.getUserId());
		values.put(ShiftOpenHelper.COLUMN_T_FROM, shift.getFrom());
		values.put(ShiftOpenHelper.COLUMN_T_TO, shift.getTo());
		values.put(ShiftOpenHelper.COLUMN_NOTIFY, shift.isNotify());
		values.put(ShiftOpenHelper.COLUMN_REPEAT, shift.isRepeat());
		values.put(ShiftOpenHelper.COLUMN_W_REPEAT, shift.isWeekly());
		values.put(ShiftOpenHelper.COLUMN_M_REPEAT, shift.isMonthly());
		
		database.insert(ShiftOpenHelper.TABLE_SHIFT, null, values);
	}

	@Override
	public Shift getShift(Integer sID) {
		
		Shift shift;
		
		Cursor cursor = database.query (
										ShiftOpenHelper.TABLE_SHIFT,
										allColumns,
										" sID ='" + sID + "'",
										null,
										null,
										null,
										null
									 	);
		
		if(cursor != null)
			cursor.moveToFirst();
		
		shift = cursorToShift(cursor);
		
		if(cursor != null && !cursor.isClosed())
			cursor.close();
		
		return shift;
	}

	@Override
	public List<Shift> getShifts() {
		
		Shift shift;
		
		Cursor cursor = database.query(ShiftOpenHelper.TABLE_SHIFT,
				allColumns, null, null, null, null, null);
		
		if (cursor != null)
	        cursor.moveToFirst();	
		
		while(!cursor.isAfterLast()) {
			
			shift = cursorToShift(cursor);
			shifts.add(shift);
			cursor.moveToNext();
		}
		
		if(cursor != null && !cursor.isClosed())
		    cursor.close();
		
		return shifts;
	}

	@Override
	public void deleteShift(Integer sID) {
		
		database.delete(
						ShiftOpenHelper.TABLE_SHIFT,
						" sID ='" + sID + "'",
						null
						);
	}

	@Override
	public void updateShift(Integer sID, Shift shift) {
		
		ContentValues values = new ContentValues();
		values.put(ShiftOpenHelper.COLUMN_SHIFT_ID, shift.getId());
		values.put(ShiftOpenHelper.COLUMN_USER_ID, shift.getUserId());
		values.put(ShiftOpenHelper.COLUMN_T_FROM, shift.getFrom());
		values.put(ShiftOpenHelper.COLUMN_T_TO, shift.getTo());
		values.put(ShiftOpenHelper.COLUMN_NOTIFY, shift.isNotify());
		values.put(ShiftOpenHelper.COLUMN_REPEAT, shift.isRepeat());
		values.put(ShiftOpenHelper.COLUMN_W_REPEAT, shift.isWeekly());
		values.put(ShiftOpenHelper.COLUMN_M_REPEAT, shift.isMonthly());
		
		database.update(
						ShiftOpenHelper.TABLE_SHIFT,
						values,
						" sID ='" + sID + "'",
						null
						);
	}

	@Override
	public void close() {
		dbHelper.close();
		
	}
	
	private Shift cursorToShift(Cursor cursor) {
		
		Shift shift = new Shift();
		boolean notify, repeat;
		
		shift.setId(cursor.getInt(0));
		shift.setUId(cursor.getString(1));
		shift.setFrom(Long.parseLong(cursor.getString(2)));
		shift.setTo(Long.parseLong(cursor.getString(3)));
		
		if(cursor.getInt(4) == 0)
			notify = false;
		else 
			notify = true;
			
		shift.setNotify(notify);
		
		if(cursor.getInt(5) == 0)
			repeat = false;
		else
			repeat = true;
		
		shift.setRepeatWeekly(cursor.getInt(6));
		shift.setRepeatMonthly(cursor.getInt(7));
		
		return shift;
	}

}